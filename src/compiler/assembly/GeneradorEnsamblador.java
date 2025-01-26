package compiler.assembly;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.codigo_intermedio.Instruccion;
import compiler.codigo_intermedio.Procedimiento;
import compiler.codigo_intermedio.Variable;
import compiler.taulasimbols.DArray;
import compiler.taulasimbols.DConst;
import compiler.taulasimbols.DFuncion;
import compiler.taulasimbols.DTupla;
import compiler.taulasimbols.TipoSubyacente;
import compiler.taulasimbols.Tipus;


import java.io.BufferedWriter;
import java.io.IOException;

public class GeneradorEnsamblador {
    private CodigoIntermedio codigoIntermedio;
    private List<String> codigoEnsamblador;
    private int desplazamiento;
    private boolean isPrint = false;
    private boolean isInput = false;
    private boolean hayRTN = false;

    public GeneradorEnsamblador(CodigoIntermedio codigoIntermedio) {
        this.codigoIntermedio = codigoIntermedio;
        this.codigoEnsamblador = new ArrayList<>();
    }

    public void generarCodigo68k() {
        generarPreambulo();

        // Indentificar funciones y el main
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();
        Map<String, List<Instruccion>> funciones = new LinkedHashMap<>();
        List<Instruccion> instruccionesMain = new ArrayList<>();

        String funcionActual = null;
        List<Instruccion> instruccionesFuncion = null;

        

        for (int i = 0; i < instrucciones.size(); i++) {
            Instruccion instr = instrucciones.get(i);

            // Comprobamos si hay algun print o input, para asi imprimir las subrutinas
            if (instr.operador.equals("PRINT")) {
                this.isPrint = true;
            }
            if (instr.operador.equals("INPUT")) {
                this.isInput = true;
            }
            if (instr.operador.equals("RTN")) {
                this.hayRTN = true;
            }
    
            if (instr.operador.equals("NEWFUN") && !instr.destino.equals("MAIN")) {
                // Encontramos un nuevo bloque (función o main)
                if (funcionActual != null) {
                    // Guardar la función anterior
                    funciones.put(funcionActual, instruccionesFuncion);
                }
                funcionActual = instr.destino; // Nombre de la función
                instruccionesFuncion = new ArrayList<>();
            } else if (i == instrucciones.size() - 1 || (instrucciones.get(i + 1).operador.equals("NEWFUN"))) {
                // Final del bloque actual
                if (funcionActual != null) {
                    instruccionesFuncion.add(instr);
                    funciones.put(funcionActual, instruccionesFuncion);
                    funcionActual = null;
                    instruccionesFuncion = null;
                } else {
                    instruccionesMain.add(instr);
                }
            } else if (funcionActual != null) {
                // Agregar instrucciones a la función actual
                instruccionesFuncion.add(instr);
            } else {
                // Agregar instrucciones al main
                instruccionesMain.add(instr);
            }
        }

        // Generar subrutinas de funciones
        for (Map.Entry<String, List<Instruccion>> entry : funciones.entrySet()) {
            generarSubrutina(entry.getKey(), entry.getValue());
        }

        // Generar el bloque principal (main)
        codigoEnsamblador.add("; ---- FIN DE LAS SUBRUTINAS ----------------");
        codigoEnsamblador.add("START");
        for (Instruccion instr : instruccionesMain) {
            traducirInstruccion(instr);
        }
        codigoEnsamblador.add("\tSIMHALT");

        // Generar subrutinas comunes
        if (this.isPrint) {
            generarSubrutinaPrintGeneral();
        }
        if (this.isInput) {
            generarSubrutinaInputInt();
            generarSubrutinaInputBool();
        }

        codigoEnsamblador.add("\tEND\tSTART");
    }

    private void generarPreambulo() {
        codigoEnsamblador.add("\tORG $1000");
        codigoEnsamblador.add("; ------- DECLARACIÓN DE CONSTANTES Y VARIABLES --------");
        Map<String, Variable> variables = codigoIntermedio.getTablaVariables();
        for (Map.Entry<String, Variable> entry : variables.entrySet()) {
            String idUnico = entry.getKey();
            Variable variable = entry.getValue();
            if (variable != null) {
                // Diferenciar entre tipos de variable, constante, tupla, array, etc.
                if (variable.descripcio instanceof DConst) {
                    // Declaración de una constante
                    DConst constante = (DConst) variable.descripcio;
                    codigoEnsamblador.add(String.format("%s\tEQU\t%s", idUnico, constante.getValor()));
                } else if (variable.descripcio instanceof DArray) {
                    // Declaración de un array
                    codigoEnsamblador.add(String.format("%s\tDS.L\t%d", idUnico, variable.tamañoTotal));
                } else if (variable.descripcio instanceof DTupla) {
                    // Declaración de una tupla
                    codigoEnsamblador.add(String.format("%s\tDS.L\t%d", idUnico, variable.tamañoTotal));
                } else {
                    codigoEnsamblador.add(String.format("%s\tDS.L\t1", idUnico));
                }
            } else {
                    // Otras variables
                    codigoEnsamblador.add(String.format("%s\tDS.L\t1", idUnico));
            }
        }
        codigoEnsamblador.add("VALTRUE\tDC.B\t'true',0");
        codigoEnsamblador.add("VALFALSE\tDC.B\t'false',0");
        codigoEnsamblador.add("NEWLINE\tDC.B\t' ',0");
        codigoEnsamblador.add("\tDS.W\t0");

    }

    private void generarSubrutina(String nombre, List<Instruccion> instrucciones) {
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");
        codigoEnsamblador.add("; Subrutina para la función: " + nombre);
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");
        codigoEnsamblador.add(nombre + ":");
    
        // Guardar registros necesarios
        codigoEnsamblador.add("\tMOVEM.L\tD0-D7/A0-A6,-(A7)\t; Guardar registros");

        // Procesar las instrucciones de la función
        for (Instruccion instruccion : instrucciones) {
            traducirInstruccion(instruccion);
        }
    
        if (!this.hayRTN) {
            // Restaurar registros y retornar
            codigoEnsamblador.add("\tMOVEM.L\t(A7)+,D0-D7/A0-A6\t; Restaurar registros");
            codigoEnsamblador.add("\tRTS\t; Retornar de la función");
        }
    }
    



    private void traducirInstruccion(Instruccion instr) {
        // Traduccion básica de instrucciones intermedias a ensamblador 68k
        switch (instr.operador) {
            case "COPY":
                copyInstr(instr);
                break;
            case "+":
                addInstr(instr);
                break;
            case "*":
                multInstr(instr);
                break;
            case "-":
                restaInstr(instr);
                break;
            case "/":
                divInstr(instr);
                break;
            case "IF_EQ":
            case "IF_LT":
            case "IF_GT":
            case "IF_GE":
            case "IF_LE":
            case "IF_NE":
                ifInstr(instr);
                break;
            case "GOTO":
                // GOTO operation (jump to destino)
                codigoEnsamblador.add("\tJMP\t" + instr.destino);
                break;
            case "SKIP":
                // SKIP operation (define a label destino)
                if (!instr.destino.equals("MAIN")) {
                    codigoEnsamblador.add(instr.destino + ":");
                }
                break;
            case "IND_VAL":
                // INDIRECT VALUE operation (e.g., load value at op1 + op2 -> destino)
                codigoEnsamblador.add("\tMOVEA.L\t" + instr.operando2 + ",A0"); // Cargar dirección base en A0
                codigoEnsamblador.add("\tMOVE.L\t(" + instr.operando1 + ",A0)," + instr.destino); // Cargar valor indirecto
                break;
            case "IND_ASS":
                // INDIRECT ASSIGN operation (e.g., assign op1 to address op2 + base -> destino)
                codigoEnsamblador.add("\tMOVEA.L\t" + instr.operando2 + ",A0"); // Cargar dirección base en A0
                codigoEnsamblador.add("\tMOVE.L\t" + instr.operando1 + ",(" + instr.destino + ",A0)"); // Guardar valor indirecto
                break;
            case "CALL":
                callInstr(instr);
                break;
            case "&&":
                andLogical(instr);
                break;
            case "||":
                orLogical(instr);
                break;
            case "PARAM_S":
                param_sInstr(instr);
                break;
            case "PARAM_PI":
                param_piInstr(instr);
                break;
            case "PARAM_PB":
                param_pbInstr(instr);
                break;
            case "PRINT":
                printInstr();
                break;
            case "INPUT":
                inputPrint(instr);
                break;
            case "PMB":
                pmbInstr(instr);
                break;
            case "RTN":
                rtnInstr(instr);
                break;
            default:
                break;
        }
    }
    private void copyInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String destino = instruccion.destino;
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        codigoEnsamblador.add("\tMOVE.L\t " + op1 + "," + destino);
    }

    private void multInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String op2 = instruccion.operando2;
        String destino = instruccion.destino;
    
        // Comprobar si los operandos son valores inmediatos o variables
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        if (!esVariable(op2)) {
            op2 = "#" + op2; // Si no es variable, es un valor inmediato
        }
    
        // Generar código ensamblador para multiplicación
        codigoEnsamblador.add("\tMOVE.L\t" + op1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tMOVE.L\t" + op2 + ",D1"); // Mover el segundo operando a D1
        codigoEnsamblador.add("\tMULS.W\tD1,D0");         // Realizar multiplicación
        codigoEnsamblador.add("\tMOVE.L\tD0," + destino); // Almacenar el resultado en el destino
    }

    private void restaInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String op2 = instruccion.operando2;
        String destino = instruccion.destino;
    
        // Comprobar si los operandos son valores inmediatos o variables
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        if (!esVariable(op2)) {
            op2 = "#" + op2; // Si no es variable, es un valor inmediato
        }

        codigoEnsamblador.add("\tMOVE.L\t" + op1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tMOVE.L\t" + op2 + ",D1"); // Mover el segundo operando a D1
        codigoEnsamblador.add("\tSUB.L\tD1,D0"); 
        codigoEnsamblador.add("\tMOVE.L\tD0," + destino); // Almacenar el resultado en el destino
    }
    
    private void addInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String op2 = instruccion.operando2;
        String destino = instruccion.destino;
    
        // Comprobar si los operandos son valores inmediatos o variables
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        if (!esVariable(op2)) {
            op2 = "#" + op2; // Si no es variable, es un valor inmediato
        }
        codigoEnsamblador.add("\tMOVE.L\t" + op1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tMOVE.L\t" + op2 + ",D1"); // Mover el segundo operando a D1
        codigoEnsamblador.add("\tADD.L\tD1,D0"); 
        codigoEnsamblador.add("\tMOVE.L\tD0," + destino); // Almacenar el resultado en el destino
    }

    private void divInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String op2 = instruccion.operando2;
        String destino = instruccion.destino;
    
        // Comprobar si los operandos son valores inmediatos o variables
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        if (!esVariable(op2)) {
            op2 = "#" + op2; // Si no es variable, es un valor inmediato
        }

        // No se permite la división de registros de 4 bytes en el 68k
        // Se debe hacer por partes
        codigoEnsamblador.add("\tMOVE.L\t" + op1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tMOVE.L\t" + op2 + ",D1"); // Mover el segundo operando a D1
        codigoEnsamblador.add("\tDIVS.W\tD1,D0");
        codigoEnsamblador.add("\tANDI.L\t#$0000FFFF,D0");
        codigoEnsamblador.add("\tMOVE.L\tD0," + destino);
    }

    private void ifInstr(Instruccion instruccion) {
        String op1 = instruccion.operando1;
        String op2 = instruccion.operando2;
        String destino = instruccion.destino;
    
        // Comprobar si los operandos son valores inmediatos o variables
        if (!esVariable(op1)) {
            op1 = "#" + op1; // Si no es variable, es un valor inmediato
        }
        if (!esVariable(op2)) {
            op2 = "#" + op2; // Si no es variable, es un valor inmediato
        }

        codigoEnsamblador.add("\tMOVE.L\t" + op1 + ",D0"); // Cargar op1 en D0
        codigoEnsamblador.add("\tMOVE.L\t" + op2 + ",D1"); // Mover el segundo operando a D1
        codigoEnsamblador.add("\tCMP.L\tD1,D0");           // Comparar op2 con D0
        switch (instruccion.operador) {
            case "IF_LT":
                codigoEnsamblador.add("\tBLT\t" + destino); // Branch if Less Than
                break;
            case "IF_GT":
                codigoEnsamblador.add("\tBGT\t" + destino); // Branch if Greater Than
                break;
            case "IF_LE":
                codigoEnsamblador.add("\tBLE\t" + destino); // Branch if Less or Equal
                break;
            case "IF_GE":
                codigoEnsamblador.add("\tBGE\t" + destino); // Branch if Greater or Equal
                break;
            case "IF_EQ":
                codigoEnsamblador.add("\tBEQ\t" + destino); // Branch if Equal
                break;
            case "IF_NE":
                codigoEnsamblador.add("\tBNE\t" + destino); // Branch if Not Equal
                break;
            default:
                break;
        }
        
    }

    private void callInstr(Instruccion ins) {
        Procedimiento procedimiento = codigoIntermedio.getTablaProcedimientos().get(ins.destino);

        DFuncion dFuncion = (DFuncion) procedimiento.descripcio;
        if (!dFuncion.getTipoRetorno().equals(new TipoSubyacente(Tipus.VOID))) {
            codigoEnsamblador.add("\tSUBA.L\t#4,SP");
        }

        codigoEnsamblador.add("\tJSR\t"+ procedimiento.etiquetaInicio);

        if (!dFuncion.getTipoRetorno().equals(new TipoSubyacente(Tipus.VOID))) {
            codigoEnsamblador.add("\tMOVE.L\t(SP)+," + ins.operando1);
        }

        codigoEnsamblador.add("\tADDA.L\t#" + (procedimiento.numParametros*4) + ",SP");


    }

    private void andLogical(Instruccion ins) {
        String var1 = ins.operando1;
        String var2 = ins.operando2;
        String destino = ins.destino;
    
        // Verificar si los operandos son valores inmediatos o variables
        if (!esVariable(var1)) {
            var1 = "#" + var1;
        }
        if (!esVariable(var2)) {
            var2 = "#" + var2;
        }

        // Generar código ensamblador para AND lógico
        codigoEnsamblador.add("\tMOVE.L\t" + var1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tAND.L\t" + var2 + ",D0"); // AND lógico entre var1 y var2
        codigoEnsamblador.add("\tTST.L\tD0");              // Probar si el resultado es cero
        codigoEnsamblador.add("\tBNE\t" + destino);  // Saltar a falso si es cero
    }

    private void orLogical(Instruccion ins) {
        String var1 = ins.operando1;
        String var2 = ins.operando2;
        String destino = ins.destino;

        // Verificar si los operandos son valores inmediatos o variables
        if (!esVariable(var1)) {
            var1 = "#" + var1;
        }
        if (!esVariable(var2)) {
            var2 = "#" + var2;
        }

        // Generar código ensamblador para OR lógico
        codigoEnsamblador.add("\tMOVE.L\t" + var1 + ",D0"); // Mover el primer operando a D0
        codigoEnsamblador.add("\tOR.L\t" + var2 + ",D0");  // OR lógico entre var1 y var2
        codigoEnsamblador.add("\tTST.L\tD0");              // Probar si el resultado es cero
        codigoEnsamblador.add("\tBEQ\t" + destino); // Saltar a verdadero si no es cero
    }

    private void param_sInstr(Instruccion ins) {
        String parametro = ins.destino;
        // Empujar el operando al stack (siempre como 4 bytes)
        codigoEnsamblador.add("\tMOVE.L\t" + parametro + ",D0");
        codigoEnsamblador.add("\tMOVE.L\tD0,-(SP)"); // Empuja 4 bytes al stack
    }

    private void param_piInstr(Instruccion ins) {
        String parametro = ins.destino;
        codigoEnsamblador.add("\tMOVE.L\t" + parametro + ",D0");
        codigoEnsamblador.add("\tMOVE.L\tD0,-(SP)"); // Empuja 4 bytes al stack
        // Indicamos que es un tipo (entero) con un 0
        codigoEnsamblador.add("\tMOVE.L\t#0,-(SP)");
    }

    private void param_pbInstr(Instruccion ins) {
        String parametro = ins.destino;
        codigoEnsamblador.add("\tMOVE.L\t" + parametro + ",D0");
        codigoEnsamblador.add("\tMOVE.L\tD0,-(SP)"); // Empuja 4 bytes al stack
        // Indicamos que es un tipo (entero) con un 0
        codigoEnsamblador.add("\tMOVE.L\t#1,-(SP)");
    }

    private void printInstr() {
        codigoEnsamblador.add("\tJSR\tPRINT_GENERAL"); // Llama a la subrutina de impresión
        codigoEnsamblador.add("\tMOVE.L\t#$01000000,SP");
    }

    private void inputPrint(Instruccion ins) {
        Variable var = codigoIntermedio.getTablaVariables().get(ins.destino);

        if (TipoSubyacente.getNomTSB(var.tipo.getTipoBasico()).equals("int")){
            codigoEnsamblador.add("\tJSR\tINPUT_INT");
        } else {
            codigoEnsamblador.add("\tJSR\tINPUT_BOOL");
        }
        codigoEnsamblador.add("\tMOVE.L\tD1," + ins.destino);
    }

    private void pmbInstr(Instruccion ins) {
        Procedimiento proc = codigoIntermedio.getTablaProcedimientos().get(ins.destino);
        desplazamiento = 68;
        for (int i = 0; i < proc.numParametros; i++) {
            codigoEnsamblador.add("\tCLR.L\tD0");
            codigoEnsamblador.add("\tMOVE.L\t" + desplazamiento + "(SP),D0");
            codigoEnsamblador.add("\tMOVE.L\tD0," + proc.listaIDUnicos.get(i));
            desplazamiento+=4;
        }
    }

    private void rtnInstr(Instruccion ins) {
        codigoEnsamblador.add("\tMOVE.L\t" + ins.destino + ",64(SP)");
        codigoEnsamblador.add("\tMOVEM.L\t(A7)+,D0-D7/A0-A6");
        codigoEnsamblador.add("\tRTS");
    }

    private boolean esVariable(String operando) {
        // Comprobar si el operando está en la tabla de variables
        return codigoIntermedio.getTablaVariables().containsKey(operando);
    }


//------------------------------------------------------------------------------------------------------------------------------
//------------------  SUBRUTINAS  ----------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------

    private void generarSubrutinaPrintGeneral() {
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");
        codigoEnsamblador.add("PRINT_GENERAL");
        codigoEnsamblador.add("; General Print Subroutine");
        codigoEnsamblador.add("; INPUT: Stack contains items to print, each 4 bytes");
        codigoEnsamblador.add("; OUTPUT: Items are printed one by one");
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");

        // Guardar registros necesarios
        codigoEnsamblador.add("\tMOVEM.L\tD0-D3,-(A7)\t; Guardar D0 y D1");
        codigoEnsamblador.add("\tADDA.L\t#20,SP");

        // Inicio del bucle de impresión
        codigoEnsamblador.add("PRINT_LOOP:");
        codigoEnsamblador.add("\tCMP.L\t#$01000000,SP");
        codigoEnsamblador.add("\tBEQ\tPRINT_END\t; Si no hay más elementos, finalizar");

        // Extraer un elemento del stack
        codigoEnsamblador.add("\tADD.L\t#8,D3");
        codigoEnsamblador.add("\tMOVE.L\t(SP)+,D2");    // Extraer el valor de la pila 
        codigoEnsamblador.add("\tMOVE.L\t(SP)+,D1");    // Extraer el tipo de la pila

        codigoEnsamblador.add("\tCMP.L\t#1,D2");
        codigoEnsamblador.add("\tBEQ\tPRINT_BOOLEAN");

        // Imprimir entero
        codigoEnsamblador.add("\tMOVE.W\t#3,D0");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tMOVEA.L\t#NEWLINE,A1");
        codigoEnsamblador.add("\tMOVE.L\t#13,D0");
        codigoEnsamblador.add("\tTRAP\t#15");

        // Repetir para el siguiente elemento
        codigoEnsamblador.add("\tBRA\tPRINT_LOOP");

        // Imprimir boolean
        codigoEnsamblador.add("PRINT_BOOLEAN:");
        codigoEnsamblador.add("\tCMP.L\t#$FF,D1");
        codigoEnsamblador.add("\tBEQ\tPRINT_TRUE");
        codigoEnsamblador.add("\tLEA\tVALFALSE,A1");
        codigoEnsamblador.add("\tBRA\tPRINT_STRING");

        codigoEnsamblador.add("PRINT_TRUE:");
        codigoEnsamblador.add("\tLEA\tVALTRUE,A1");

        // Imprimir el string 
        codigoEnsamblador.add("PRINT_STRING:");
        codigoEnsamblador.add("\tMOVE.W\t#13,D0");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tBRA\tPRINT_LOOP");

        // Fin de la subrutina
        codigoEnsamblador.add("PRINT_END:");
        codigoEnsamblador.add("\tSUBA.L\t#28,SP");
        codigoEnsamblador.add("\tMOVEM.L\t(A7)+,D0-D3\t; Restaurar D1 y D0");
        codigoEnsamblador.add("\tRTS\t; Retornar de la subrutina");

    }

    private void generarSubrutinaInputInt() {
        codigoEnsamblador.add("; ------------------------------------------------------------------------------");
        codigoEnsamblador.add("INPUT_INT");
        codigoEnsamblador.add("; General Input Subrutina");
        codigoEnsamblador.add("; INPUT: None");
        codigoEnsamblador.add("; OUTPUT: Reads an integer or value and stores it");

        codigoEnsamblador.add("\tMOVE.L\tD0,-(A7)\t; SAVE D0");
        codigoEnsamblador.add("\tLEA\tINPUT_MARK,A1");
        codigoEnsamblador.add("\tMOVE.L\t#14,D0");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tCLR.L\tD0\t; CLEAR D0");
        codigoEnsamblador.add("\tCLR.L\tD1\t; CLEAR D1");
        codigoEnsamblador.add("\tMOVE.L\t#4,D0\t; READ_INT");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tMOVE.L\t(A7)+,D0\t");
        codigoEnsamblador.add("\tRTS");
    }

    private void generarSubrutinaInputBool() {
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");
        codigoEnsamblador.add("INPUT_BOOL");
        codigoEnsamblador.add("; READS A BOOLEAN VALUE");
        codigoEnsamblador.add("; INPUT: NONE");
        codigoEnsamblador.add("; OUTPUT: D1 - BOOLEAN VALUE (-1 for true, 0 for false)");
        codigoEnsamblador.add("; -----------------------------------------------------------------------------");
        codigoEnsamblador.add("\tMOVE.L\tD0,-(A7)\t; SAVE D0");
        codigoEnsamblador.add("BUCLE:");
        codigoEnsamblador.add("\tLEA\tINPUT_MARK,A1\t; Mostrar marca de entrada");
        codigoEnsamblador.add("\tMOVE.L\t#14,D0\t; Llamada al sistema para imprimir");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tCLR.L\tD0\t; Limpiar D0");
        codigoEnsamblador.add("\tMOVE.L\tD0,A1\t; Limpiar A1");
        codigoEnsamblador.add("\tMOVE.L\t#2,D0\t; Leer como cadena");
        codigoEnsamblador.add("\tTRAP\t#15\t; Llamada al sistema para leer");
        codigoEnsamblador.add("\tMOVE.B\t(A1),D0\t; Leer el primer carácter del input");
    
        // Verificar si es 't' (true)
        codigoEnsamblador.add("\tCMP.B\t#'t',D0");
        codigoEnsamblador.add("\tBEQ\tSET_TRUE");
    
        // Verificar si es 'f' (false)
        codigoEnsamblador.add("\tCMP.B\t#'f',D0");
        codigoEnsamblador.add("\tBEQ\tSET_FALSE");
    
        // Si no es válido, mostrar error y volver a pedir entrada
        codigoEnsamblador.add("INPUT_ERROR_BOOL:");
        codigoEnsamblador.add("\tLEA\tINPUT_ERROR,A1\t; Mensaje de error");
        codigoEnsamblador.add("\tMOVE.L\t#13,D0\t; Imprimir mensaje");
        codigoEnsamblador.add("\tTRAP\t#15");
        codigoEnsamblador.add("\tBRA\tBUCLE\t; Reintentar");
    
        // Configurar valor booleano para true
        codigoEnsamblador.add("SET_TRUE:");
        codigoEnsamblador.add("\tMOVE.L\t#-1,D1\t; Valor true (-1)");
        codigoEnsamblador.add("\tBRA\tINPUT_BOOL_END");
    
        // Configurar valor booleano para false
        codigoEnsamblador.add("SET_FALSE:");
        codigoEnsamblador.add("\tMOVE.L\t#0,D1\t; Valor false (0)");
    
        codigoEnsamblador.add("INPUT_BOOL_END:");
        codigoEnsamblador.add("\tMOVE.L\t(A7)+,D0\t; Restaurar D0");
        codigoEnsamblador.add("\tRTS\t; Retornar de la subrutina");
    
        codigoEnsamblador.add("INPUT_MARK\tDC.B\t'>>> ',0");
        codigoEnsamblador.add("INPUT_ERROR\tDC.B\t'ERROR: Valor invalido. Introduzca true o false.',0");
    }
    

//----------------------------------------------------------------------------------------------------------------
//---------------------  FUNCION AUXILIAR  -----------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------



    public void imprimirInstrucciones68k(BufferedWriter writer) throws IOException {
        writer.write("; Código ensamblador Motorola 68k generado:\n");
        for (String linea : codigoEnsamblador) {
            writer.write(linea);
            writer.newLine(); // Agregar salto de línea
        }
    }
    
}
