package compiler.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.codigo_intermedio.Instruccion;
import compiler.codigo_intermedio.Variable;
import compiler.taulasimbols.DArray;
import compiler.taulasimbols.DConst;
import compiler.taulasimbols.DTupla;
import compiler.taulasimbols.DVar;

public class GeneradorEnsamblador {
    private CodigoIntermedio codigoIntermedio;
    private List<String> codigoEnsamblador;

    public GeneradorEnsamblador(CodigoIntermedio codigoIntermedio) {
        this.codigoIntermedio = codigoIntermedio;
        this.codigoEnsamblador = new ArrayList<>();
    }

    public void generarCodigo68k() {
        generarPreambulo();
        generarInstrucciones();
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
                    codigoEnsamblador.add(String.format("%s\tDS.W\t%d", idUnico, variable.tamañoTotal));
                } else if (variable.descripcio instanceof DTupla) {
                    // Declaración de una tupla
                    codigoEnsamblador.add(String.format("%s\tDS.W\t%d", idUnico, variable.tamañoTotal));
                } else {
                    codigoEnsamblador.add(String.format("%s\tDS.W\t1", idUnico));
                }
            } else {
                    // Otras variables
                    codigoEnsamblador.add(String.format("%s\tDS.W\t1", idUnico));
            }
        }
    }

    private void generarInstrucciones() {
        codigoEnsamblador.add("; -- Codigo Intermedio traducido a ensamblador --");
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();

        for (Instruccion instr : instrucciones) {
            traducirInstruccion(instr);
        }
    }

    private void traducirInstruccion(Instruccion instr) {
        // Traduccion básica de instrucciones intermedias a ensamblador 68k
        switch (instr.operador) {
            case "COPY":
                codigoEnsamblador.add("\tMOVE.W\t " + instr.operando1 + "," + instr.destino);
                break;
            case "+":
                // ADD operation (op1 + op2 -> destino)
                codigoEnsamblador.add("\tMOVE.W\t" + instr.operando1 + ",D0"); // Cargar op1 en D0
                codigoEnsamblador.add("\tADD.W\t" + instr.operando2 + ",D0");  // Sumar op2 a D0
                codigoEnsamblador.add("\tMOVE.W\tD0," + instr.destino);  // Guardar resultado en destino
                break;
            default:
                break;
        }
    }

    public void imprimirInstrucciones68k() {
        System.out.println("; Código ensamblador Motorola 68k generado:");
        for (String linea : codigoEnsamblador) {
            System.out.println(linea);
        }
    }
    
}
