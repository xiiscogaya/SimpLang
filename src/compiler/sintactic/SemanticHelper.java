package compiler.sintactic;

import compiler.taulasimbols.*;
import java.util.ArrayList;

import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.simbols.*;

public class SemanticHelper {

    /**
     * Método para procesar la declaración de las constantes, siempre con valor literal
     * 
     * @param idType        Tipo de la constante
     * @param id            Identificador de la constante
     * @param valor         Valor declarado a la constante
     * @param taulaSim      Tabla de Símbolos
     *
    */
    public static void processConstantDeclaration(SDecConstante constante, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        // Comprobar si en la producción anterior se ha producido un error
        // Solo puede venir un fallo de la producción Valor, no de la Type

        SValor valor = constante.getValor();
        String id = constante.getId();
        String type = constante.getTipo();
        if (procesarValor(valor).isError()) {
            // No procesamos nada más ya que se ha producido un error
            return;
        }

        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError(3 , "Error: Redefinición de constante '" + id + "' en línea " + constante.getLine() + ".");
            return;
        }

        // Verificar que el tipo de valor coincida con el tipo de la constante
        if (!TipoSubyacente.getNomTSB(valor.getTipo().getTipoBasico()).equals(type)) {
            ErrorManager.addError(3 , "Error: Tipo incompatible en constante '" + id + "' en línea " + constante.getLine() + ".");
            return;  // Retorna null para indicar que hubo un error
        }
        // Crear la descripción de la constante usando DConst
        DConst descripcionConstante = new DConst(valor.getValor(), new TipoSubyacente(type));
        taulaSim.posar(id, descripcionConstante);

        // Generar código intermedio
        String valorLiteral = valor.getValor();
        codigoIntermedio.agregarInstruccion("COPY", valorLiteral, "", id);

        taulaSim.imprimirTabla();
        
    }


    /**
     * Método para procesar la declaración de una variable
     * 
     * @param idType        Tipo de la variable
     * @param id            Identificador de la variable
     * @param expresion     Expresión asignada a la variable
     * @param forSentencia  Booleano para indicar si es una sentencia for
     * @param DecTupla      Booleano para indicar si es una declaración de una tupla
     * @param taulaSim      Tabla de Símbolos
     */
    public static void processVarDeclaration(SDecVar variable, Boolean forSentencia, Boolean DecTupla, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SExpresion expresion = variable.getExpresion();
        String id = variable.getId();
        String type = variable.getTipo();
        
        // Verificamos que si es la declaracion de un for, sea con expresion
        if (forSentencia && expresion == null) {
            ErrorManager.addError(3 , "Error: Declaración de la variable en el for sin valor inicial en línea " + variable.getLine() + ".");
            return;
        }
        
        if (expresion != null && procesarExpresion(expresion, taulaSim, codigoIntermedio).isError()) {
            return;
        }
        

        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError(3 , "Error: Redefinición de la variable '" + id + "' en línea " + variable.getLine() + ".");
            return;
        }
    
        // Si se asigna un valor inicial, verificar su tipo
        if (expresion != null) {
            if (expresion.isError()) {
                return;
            }
    
            if (!TipoSubyacente.getNomTSB(expresion.getTipo().getTipoBasico()).equals(type)) {
                ErrorManager.addError(3 , "Error: Tipo incompatible en variable '" + id + "' en línea " + variable.getLine() + ".");
                return;
            }
        }

        variable.setTipoBasico(new TipoSubyacente(type));
        
        if (!DecTupla) {
            // Si no hay errores, la variable ya está creada, simplemente valida y registra su descripción
            DVar descripcionVariable = new DVar(variable.getTipoBasico());
            taulaSim.posar(id, descripcionVariable);
        }

        // Generar código intermedio
        if (expresion != null) {
            codigoIntermedio.agregarInstruccion("COPY", expresion.getVarGenerada(), "", id);
        }
        taulaSim.imprimirTabla();
    }


    /**
     * Método para la declaración de un array
     * 
     * @param tipo          Tipo de los elementos del array
     * @param id            Identificador del array
     * @param dimension     Nodo asociado a las dimensiones del array
     * @param taulaSim      Tabla de Símbolos
     */
    public static void processArray(SDecArray array, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SListaDimensiones dimension = array.getDimensiones();
        String id = array.getId();
        String tipo = array.getTipo();
        
        Descripcio desc = taulaSim.consultar(id);
        
        // Consultamos el id en la tabla de símbolos
        if (desc != null) {
            ErrorManager.addError(3 , "Error: Redefinición de mismo nombre en el array " + id +  " en línea " + array.getLine() + ".");
            return;
        }
        
        DArray desc_array = new DArray(new TipoSubyacente(tipo));
        SListaDimensiones dimensionesProcesadas = procesarDimension(id, dimension, desc_array, taulaSim);
        if (dimensionesProcesadas.isError()) {
            return;
        }
        taulaSim.posar(id, desc_array);

        
        int baseSize = TipoSubyacente.sizeOf(new TipoSubyacente(tipo).getTipoBasico());
        if (baseSize == -1) {
            ErrorManager.addError(3, "Error: Tipo de tamaño dinámico no soportado para arrays, en línea " + array.getLine() + ".");
            return;
        }

        String baseSizeVar = codigoIntermedio.nuevaVariableTemporal();
        codigoIntermedio.agregarInstruccion("COPY", String.valueOf(baseSize), "", baseSizeVar);

        SListaDimensiones currentDimension = dimensionesProcesadas;
        String totalSizeVar = baseSizeVar;

        while (currentDimension != null) {
            SValor dimensionSize = currentDimension.getSize();

            // Copiar el valor de la dimensión en una variable temporal
            String dimensionSizeVar = codigoIntermedio.nuevaVariableTemporal();
            codigoIntermedio.agregarInstruccion("COPY", dimensionSize.getValor(), "", dimensionSizeVar);

            // Multiplicamos la dimension actual por el tamaño calculado
            String newTotalSizeVar = codigoIntermedio.nuevaVariableTemporal();
            codigoIntermedio.agregarInstruccion("MUL", totalSizeVar, dimensionSizeVar, newTotalSizeVar);

            // Actualizamos el tamaño total acumulado
            totalSizeVar = newTotalSizeVar;

            // Avanzamos a la siguiente dimension
            currentDimension = currentDimension.getListaDimensiones();
        }

        // Generamos la instruccion final para declarar el array
        codigoIntermedio.agregarInstruccion("ALLOC", totalSizeVar, "", id);

        taulaSim.imprimirTabla();

    }

    
    /**
     * Método para la declaración de un tupla
     * 
     * @param id            Identificador de la tupla
     * @param lista         Nodo asociado a la lista de atributos
     * @param taulaSim      Tabla de Símbolos
     */
    public static void procesarTupla(SDecTupla tupla, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SListaTupla lista = tupla.getListaParametros();
        String id = tupla.getId();
        
        // Comprobar si hay error en los parametros
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError(3 , "Error: Redefinición de la tupla '" + id + "' en línea " + tupla.getLine() + ".");
            return;
        }

        DTupla tuplaDef = new DTupla();

        // Inicializar el espacio total que ocupará la tupla

        String totalSizeVar = codigoIntermedio.nuevaVariableTemporal();
        codigoIntermedio.agregarInstruccion("COPY", String.valueOf(0), "", totalSizeVar);

        do {
            SDecVar variable = lista.getVar();

            processVarDeclaration(variable, false, true, taulaSim, codigoIntermedio);

            // Calcular el tamaño del atributo
            int atributoSize = TipoSubyacente.sizeOf(variable.getTipoBasico().getTipoBasico());
            String atributoSizeVar = codigoIntermedio.nuevaVariableTemporal();
            codigoIntermedio.agregarInstruccion("COPY", String.valueOf(atributoSize), "", atributoSizeVar);

            codigoIntermedio.agregarInstruccion("ADD", atributoSizeVar, totalSizeVar, totalSizeVar);

            tuplaDef.addParametro(lista.getVar().getTipoBasico(), lista.getVar().getId());

            lista = lista.getLista();

        } while (lista != null);

        taulaSim.posar(id, tuplaDef);

        codigoIntermedio.agregarInstruccion("ALLOC", totalSizeVar, "", id);
        taulaSim.imprimirTabla();
           
    }

    /**
     * Método para procesar la asignación, de una variable o un array
     * 
     * @param id                Identificador en la que se producirá la asignación
     * @param expresion         Expresión que se asignará al ID
     * @param op                Operación que se realizará
     * @param array             Array 
     * @param forActualizacion  Boolean para indicar si pertenece a una sentencia for
     * @param taulaSim          Tabla de Símbolos
     */
    public static void processAsignacion(SAsignacion asignacion, Boolean forActualizacion, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        String id = asignacion.getId();
        SExpresion expresion = asignacion.getExpresion();
        String op = asignacion.getOperacion();
        SLlamadaArray array = asignacion.getArray();
        if (array == null) {
        // Verificar si el valor tiene errores
            if (procesarExpresion(expresion, taulaSim, codigoIntermedio).isError()) {
                return;
            }
        
            // Consultar la variable en la tabla de símbolos
            Descripcio desc = taulaSim.consultar(id);
        
            // Verificar si la variable está declarada
            if (desc == null) {
                ErrorManager.addError(3 , "Error: La variable '" + id + "' no está declarada en línea " + asignacion.getLine() + ".");
                return;
            }
        
            // Verificar que sea una variable y no otro tipo de símbolo
            if (!(desc instanceof DVar)) {
                ErrorManager.addError(3 , "Error: '" + id + "' no es una variable, en la linea " + asignacion.getLine() + ".");
                return;
            }
        
            // Verificar compatibilidad de tipo
            DVar variable = (DVar) desc;
            if (!variable.getTipoSubyacente().equals(expresion.getTipo())) {
                ErrorManager.addError(3 , "Error: Tipo incompatible en asignación a '" + id + "' en línea " + asignacion.getLine() + ".");
                return;
            }

            if (forActualizacion && op == "=") {
                ErrorManager.addError(3 , "Error: Tipo incompatible de asignacion no disponible en un for, en línea " + asignacion.getLine() + ".");
                
            }

            if (op.equals("=")) {
                codigoIntermedio.agregarInstruccion("COPY", expresion.getVarGenerada(), "", id);
            } else {
                String temporal = codigoIntermedio.generarExpresion(op.replace("=", ""), id, expresion.getVarGenerada());
                codigoIntermedio.agregarInstruccion("COPY", temporal, "", id);
            }
        } else {
            SLlamadaArray llamadaProcesada = procesarLlamadaArray(array, taulaSim);
            if (llamadaProcesada.isError()) {
                return;
            }

            // Consultar el array en la tabla de símbolos
            Descripcio desc = taulaSim.consultar(array.getArrayName());
            if (desc == null || !(desc instanceof DArray)) {
                ErrorManager.addError(3 , "Error: '" + array.getArrayName() + "' no es un array válido, en línea " + asignacion.getLine() + ".");
                return;
            }

            DArray array_local = (DArray) desc;

            // Procesar la expresión del valor asignado
            SExpresion expresionProcesada = procesarExpresion(expresion, taulaSim, codigoIntermedio);
            if (expresionProcesada.isError()) {
                return;
            }

            // Verificar que el tipo de la expresión coincide con el tipo del array
            if (!array_local.getTipo().equals(expresionProcesada.getTipo())) {
                ErrorManager.addError(3 , "Error: Tipo incompatible en la asignación al array '" + array.getArrayName() + "' en línea " + asignacion.getLine() + ".");
                return;
            }
        }
        taulaSim.imprimirTabla();

    }
    
    /**
     * Crea un SValor si el valor cumple con las restricciones de tipo y tamaño.
     * En caso contrario, devuelve un SValor vacío (con error).
     *
     * @param tipo          TipoSubyacente del valor que se va a crear.
     * @param valor         El valor específico a asignar.
     * @return              Una instancia de SValor válida o una instancia vacía con error.
     */
    public static SValor procesarValor(SValor sim_valor) {
        TipoSubyacente tipo = sim_valor.getTipo();
        String valor = sim_valor.getValor();

        if (tipo != null) {
            switch (tipo.getTipoBasico()) {
                case INT:
                    try {
                        int intValue = Integer.parseInt(valor);
                        if (!esValorValido(intValue, tipo)) {
                            ErrorManager.addError(3 , "Error: Valor fuera de rango para el tipo " + tipo + ", en línea " + 012345 + ".");
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError(3 , "Error: Número demasiado grande para el tipo " + tipo + ", en línea " + 012345 + ".");
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    break;
                    
                case FLOAT:
                    try {
                        float floatValue = Float.parseFloat(valor);
                        if (!esValorValido(floatValue, tipo)) {
                            ErrorManager.addError(3 , "Error: Valor fuera de rango para el tipo " + tipo + ", en línea " + 012345 + ".");
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError(3 , "Error: Número demasiado grande para el tipo " + tipo + ", en línea " + 012345 + ".");
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    break;
                    
                case CHAR:
                    // No hacemos nada
                    sim_valor.setTamañoChar(valor.length());
                    return sim_valor;
                case BOOLEAN:
                    // No hacemos nada
                    return sim_valor;
                    
                default:
                    ErrorManager.addError(3 , "Error: Tipo no compatible para el valor proporcionado, en línea " + 012345 + ".");
                    return new SValor(); // Devuelve un SValor vacío con error
            }
            
        }
        return sim_valor;

    }
    


    /**
     * Método para procesar la sentencia Return, comprueba si el valor de retorno coincide con el tipo de la función
     * 
     * @param retorno           Nodo asociado al return
     * @param taulaSim          Tabla de Símbolos
     */
    public static void procesarReturn(SReturn retorno, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        // Verificar si estamos dentro de una función
        String funcionActual = taulaSim.obtenerFuncionActual();
        if (funcionActual == null) {
            ErrorManager.addError(3 , "Error: 'return' sólo puede utilizarse dentro de una función, en línea " + 012345 + ".");
            return;
        }
    
        // Consultar la función actual
        Descripcio desc = taulaSim.consultar(funcionActual);
        if (!(desc instanceof DFuncion)) {
            ErrorManager.addError(3 , "Error interno: La función actual no está registrada correctamente, en línea " + 012345 + ".");
            return;
        }
    
        DFuncion funcion = (DFuncion) desc;
    
        // Verificar el tipo de retorno de la función
        TipoSubyacente tipoRetornoEsperado = funcion.getTipoRetorno();
    
        // Caso `return;` (sin expresión)
        if (retorno.getExpresion() == null) {
            if (!tipoRetornoEsperado.equals(new TipoSubyacente(Tipus.VOID))) {
                ErrorManager.addError(3 , "Error: La función '" + funcionActual + "' debe retornar un valor de tipo '" + tipoRetornoEsperado + "', en línea " + 012345 + ".");
            }
            return;
        }
    
        // Caso `return expresion;`
        SExpresion expresionProcesada = procesarExpresion(retorno.getExpresion(), taulaSim, codigoIntermedio);
        if (expresionProcesada.isError()) {
            return;
        }

        // Verificar compatibilidad de tipos
        if (!tipoRetornoEsperado.equals(expresionProcesada.getTipo())) {
            ErrorManager.addError(3 , "Error: El tipo de la expresión retornada ('" + expresionProcesada.getTipo() + "') no coincide con el tipo de retorno declarado ('" + tipoRetornoEsperado + "'), en línea " + 012345 + ".");
        }
    }
    



    /**
     * Método para procesar la declaración de una función
     * Comprueba los parámetros y procesa el bloque
     * 
     * @param tipoRetorno           Tipo de la función
     * @param id                    Identificador de la función
     * @param parametros            Parámetros pasados a la función
     * @param cuerpo                Bloque que contiene las sentencias de la función
     * @param taulaSim              Tabla de Símbolos
     */
    public static void procesarDecFuncion(int line, String tipoRetorno, String id, SListaParametros parametros, SBloque cuerpo, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        // Verificar si la función ya está declarada en el ámbito global
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError(3 , "Error: Redefinición de la función '" + id + "', en línea " + line + ".");
            return;
        }
    
        // Registrar la función en la tabla de descripciones
        DFuncion descripcionFuncion = new DFuncion(
            new TipoSubyacente(tipoRetorno),
            new ArrayList<>(),
            new ArrayList<>()
        );

        if (procesarListaParametros(id, parametros, descripcionFuncion, taulaSim).isError()) {
            return;
        }

        taulaSim.entrarFuncion(id);
    
        // Crear un nuevo ámbito para la función
        taulaSim.nuevoNivelAmbito();
    
        // Registrar los parámetros en el nuevo ámbito
        do {
            String tipoParametro = parametros.getTipo();
            String nombreParametro = parametros.getID();
    
            // Verificar si el nombre del parámetro ya existe en el ámbito actual
            if (taulaSim.consultar(nombreParametro) != null) {
                ErrorManager.addError(3 , "Error: Redefinición del parámetro '" + nombreParametro + "' en la función '" + id + "', en línea " + line + ".");
                continue;
            }
    
            // Registrar el parámetro como una variable
            DVar variable = new DVar(new TipoSubyacente(tipoParametro));
            taulaSim.posar(nombreParametro, variable);
            parametros = parametros.getParametro();
        } while (parametros != null);
        
    
        // Procesar el bloque de la función
        procesarBloque(cuerpo, taulaSim, false, "Funcion", codigoIntermedio);

    
        // Eliminar el ámbito de la función
        taulaSim.eliminarNivelAmbito();

        taulaSim.salirFuncion();
    }


    /**
     * Método para procesar la llamada a una función
     * 
     * @param llamadaFuncion        Nodo que contiene la info de la llamada a la función
     * @param taulaSim              Tabla de Símbolos
     */
    public static void procesarLlamadaFuncion(SLlamadaFuncion llamadaFuncion, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        // Consultar la función en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(llamadaFuncion.getIdFuncion());
    
        // Verificar que la función existe y es una descripción de función
        if (desc == null || !(desc instanceof DFuncion)) {
            ErrorManager.addError(3 , "Error: La función '" + llamadaFuncion.getIdFuncion() + "' no está declarada, en línea " + llamadaFuncion.getLine() + ".");
            return;
        }
    
        DFuncion funcion = (DFuncion) desc;

        if (procesarListaArgumentos(llamadaFuncion.getArgumentos(), funcion, taulaSim, codigoIntermedio).isError()) {
            return; // No hacer nada ya que hay un error en un nodo anterior
        }

        if (funcion.getTipoRetorno().equals(new TipoSubyacente(Tipus.VOID))) {
            llamadaFuncion.setEsVoid(true);
        }

        taulaSim.imprimirTabla();
    }



    /**
     * Método para procesar la llamada a un array
     * 
     * @param llamadaArray      Nodo que contiene la info de la llamada al array
     * @param taulaSim          Tabla de Símbolos
     * @return                  Una instancia de SLlamadaArray válida o una instancia vacía con error.       
     */
    public static SLlamadaArray procesarLlamadaArray(SLlamadaArray llamadaArray, TaulaSimbols taulaSim) {
        Descripcio desc = taulaSim.consultar(llamadaArray.getArrayName());
        // Comprobamos si no es null
        if (desc == null) {
            ErrorManager.addError(3 , "Error: El ID " + llamadaArray.getArrayName() + " no esta inicializado, en línea " + llamadaArray.getLine() + ".");
            return new SLlamadaArray();
        }
        
        // Comprobamos si el id es un array
        if (!(desc instanceof DArray)) {
            ErrorManager.addError(3 , "Error: '" + llamadaArray.getArrayName() + "' no es un array, en línea " + llamadaArray.getLine() + ".");
            return new SLlamadaArray();
        }
        
        // Verificamos las dimensiones
        SListaDimensiones dimensiones = llamadaArray.getDimensiones();

        do {
            SValor valor_procesado = procesarValor(dimensiones.getSize());
            if (valor_procesado.isError()) {
                return new SLlamadaArray();
            }
            if (!valor_procesado.getTipo().equals(new TipoSubyacente(Tipus.INT))) {
                ErrorManager.addError(3 , "Error: La dimension del array no es un entero, sino que es " + valor_procesado.getTipo() + ", en línea " + llamadaArray.getLine() + ".");
            }
            dimensiones = dimensiones.getListaDimensiones();
        } while (dimensiones != null);

        return llamadaArray;
    }

    /**
     * Método para procesar la llamada a una tupla
     * 
     * @param llamadaTupla          Nodo que contiene la info de la llamada a la tupla
     * @param taulaSim              Tabla de Símbolos
     * @return                      Una instancia de SLlamadaTupla válida o una instancia vacía con error.
     */
    public static SLlamadaTupla procesarLlamadaTupla(SLlamadaTupla llamadaTupla, TaulaSimbols taulaSim) {
        Descripcio desc = taulaSim.consultar(llamadaTupla.getTuplaName());

        if (desc == null) {
            ErrorManager.addError(3 , "Error: El ID " + llamadaTupla.getTuplaName() + " no esta inicializado, en línea " + llamadaTupla.getLine() + ".");
            return new SLlamadaTupla();
        }

        if (!(desc instanceof DTupla)) {
            ErrorManager.addError(3 , "Error: " + llamadaTupla.getTuplaName() + " no es una Tupla, en línea " + llamadaTupla.getLine() + ".");
            return new SLlamadaTupla();
        }

        DTupla tupla = (DTupla) desc;
        if (!tupla.tieneCampo(llamadaTupla.getFieldName())) {
            ErrorManager.addError(3 , "Error: El campo '" + llamadaTupla.getFieldName() + "' no existe en la tupla '" + llamadaTupla.getTuplaName() + "', en línea " + llamadaTupla.getLine() + ".");
            return new SLlamadaTupla();
        }

        return llamadaTupla;
    }

    /**
     * Método para procesar la sentencia If
     * 
     * @param expresion         Condición del if, tiene que ser de tipo booleano
     * @param bloqueIf          Bloque que se ejecutará si se cumple la condición
     * @param listaElseIf       Nodo que contiene todos los posibles else if
     * @param bloqueElse        Método para procesar el else
     * @param taulaSim          Tabla de Símbolos
     */

    public static void procesarIf(SIf ifSentencia, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SExpresion expresion = ifSentencia.getExpresion();
        SBloque bloqueIf = ifSentencia.getBloque1();
        SElif listaElseIf = ifSentencia.getLista();
        SBloque bloqueElse = ifSentencia.getBloque2();

        // Procesar la condición del if principal
        SExpresion expresionProcesada = procesarExpresion(expresion, taulaSim, codigoIntermedio);
        if (expresionProcesada.isError()) {
            return;
        }
    
        // Verificar que la condición sea de tipo booleano
        if (!expresionProcesada.getTipo().equals(new TipoSubyacente(Tipus.BOOLEAN))) {
            ErrorManager.addError(3 , "Error: La condición del 'if' debe ser de tipo booleano, no " + expresionProcesada.getTipo() + ", en línea " + ifSentencia.getLine() + ".");
            return;
        }
    
        // Procesamos el bloque del `if`
        procesarBloque(bloqueIf, taulaSim, true, "IF", codigoIntermedio);
    
        // Procesamos los else-if si existen
        if (listaElseIf != null) {
            procesarListaElseIf(listaElseIf, taulaSim, codigoIntermedio);
        }
    
        // Procesamos el bloque `else` si existe
        if (bloqueElse != null) {
            procesarBloque(bloqueElse, taulaSim, true, "ELSE", codigoIntermedio);
        }
        taulaSim.imprimirTabla();
    }
    


    
    /**
     * Método para procesar todos los else if
     * 
     * @param lista         Nodo que contiene toda la info de los else if
     * @param taulaSim      Tabla de Símbolos
     * @return              Una instancia de SELif válida o una instancia vacía con error.
     */
    public static SElif procesarListaElseIf(SElif lista, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SElif listaLocal = lista;

        while (listaLocal != null) {
            // Procesar la condición del else-if
            SExpresion condicion = listaLocal.getExpresion();
            if (procesarExpresion(condicion, taulaSim, codigoIntermedio).isError()) {
                return new SElif(); // Si la expresión tiene un error, abortamos
            }

            // Verificar que la condición sea de tipo booleano
            if (!condicion.getTipo().equals(new TipoSubyacente(Tipus.BOOLEAN))) {
                ErrorManager.addError(3 , "Error: La condición del 'else-if' debe ser de tipo booleano, no " + condicion.getTipo() + ", en línea " + listaLocal.getLine() + ".");
                return new SElif();
            }

            // Procesar el bloque del else-if
            SBloque bloque = listaLocal.getBloque();
            procesarBloque(bloque, taulaSim, true, "ELSE IF", codigoIntermedio);

            // Avanzar al siguiente else-if en la lista
            listaLocal = listaLocal.getLista();
        }
        taulaSim.imprimirTabla();
        return lista;
    }


    /**
     * Método para procesar el While
     *  
     * @param expresion         Condición del While
     * @param bloque            Sentencias que se ejecutarán si se cumple la condición
     * @param taulaSim          Tabla de Símbolos
     */
    public static void procesarWhile(SWhile whileSentencia, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        SExpresion expresion = whileSentencia.getExpresion();
        SBloque bloque = whileSentencia.getBloque();
        // Procesamos la condición del while
        SExpresion expresionProcesada = procesarExpresion(expresion, taulaSim, codigoIntermedio);
        if (expresionProcesada.isError()) {
            return;
        }

        // Verificar que la condición sea de tipo booleano
        if (!expresionProcesada.getTipo().equals(new TipoSubyacente(Tipus.BOOLEAN))) {
            ErrorManager.addError(3 , "Error: La condición del 'while ' debe ser de tipo booleano, no " + expresionProcesada.getTipo() + ", en línea " + whileSentencia.getLine() + ".");
            return;
        }

        // Procesar el bloque del bucle
        procesarBloque(bloque, taulaSim, true, "WHILE", codigoIntermedio);

    }

    /**
     * Método para el procesamiento de un for 
     * 
     * @param init              Inicialización del for
     * @param condicion         Condición del for
     * @param actualizacion     Actualización del for
     * @param cuerpo            Cuerpo que se procesará mientras se cumpla la condición
     * @param taulaSim          Tabla de Símbolos
     */
    public static void procesarFor(SDecVar init, SExpresion condicion, SAsignacion actualizacion, SBloque cuerpo, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        //Creamos un nuevo nivel de ámbito
        taulaSim.nuevoNivelAmbito();
        // Procesamos la incialización como la declaración de una variable
        processVarDeclaration(init, true, false, taulaSim, codigoIntermedio);

        // Procesamos la condición
        SExpresion condicionProcesada= procesarExpresion(condicion, taulaSim, codigoIntermedio);
        if (condicionProcesada.isError()) {
            taulaSim.eliminarNivelAmbito();
            return;
        }

        if (!condicionProcesada.getTipo().equals(new TipoSubyacente(Tipus.BOOLEAN))) {
            ErrorManager.addError(3 , "Error: La condición del 'for' debe ser de tipo booleano, en línea " + 012345 + ".");
            taulaSim.eliminarNivelAmbito();
            return;
        }

        // Procesamos la actualizacion
        processAsignacion(actualizacion, true, taulaSim, codigoIntermedio);
        // Procesamos el cuerpo del bucle
        procesarBloque(cuerpo, taulaSim, false, "FOR", codigoIntermedio);

        

        taulaSim.eliminarNivelAmbito();
    }


    /**
     * Método para procesar un print por pantalla
     * 
     * @param lista         Nodo que contiene todas las expresiones a mostrar por pantalla
     * @param taulaSim      Tabla de Símbolos
     */
    public static void procesarPrint(SListaExpresiones lista, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        while (lista != null) {
            SExpresion expresionProcesada = procesarExpresion(lista.getExpresion(), taulaSim, codigoIntermedio);
            if (expresionProcesada.isError()) {
                return;
            }
            lista = lista.getLista();
        }
    }

    /**
     * Método para procesar el Input
     * 
     * @param id            Identificador donde se guardará la info introducida
     * @param taulaSim      Tabla de Símbolos
     */
    public static void procesarInput(SInput input, TaulaSimbols taulaSim) {
        String id = input.getId();
        Descripcio desc = taulaSim.consultar(id);

        if(desc == null) {
            ErrorManager.addError(3 , "Error: La variable '" + id + "' no está declarada, en línea " + input.getLine() + ".");
            return;
        }

        // Verificar que sea una variable y no otro tipo de símbolo
        if (!(desc instanceof DVar)) {
            ErrorManager.addError(3 , "Error: '" + id + "' no es una variable, en línea " + input.getLine() + ".");
            return;
        }
    }

    
    /**
     * Método para procesar el Main del programa
     * 
     * @param bloque        Contiene las sentencias del main
     * @param taulaSim      Tabla de Símbolos
     */
    public static void procesarMain(SBloque bloque, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        procesarBloque(bloque, taulaSim, false, "Main", codigoIntermedio);
        codigoIntermedio.imprimirCodigo();
    }


    /**
     * Método para procesar un bloque de sentencias
     * 
     * @param bloque            Contiene todas las sentencias a procesar
     * @param taulaSim          Tabla de Símbolos
     * @param crearNuevoNivel   Boolean por si hay que crear un nuevo nivel de ámbito en la Tabla de Símbolos
     * @param contexto          A que contexto pertenece este bloque
     */
    public static void procesarBloque(SBloque bloque, TaulaSimbols taulaSim, boolean crearNuevoNivel, String contexto, CodigoIntermedio codigoIntermedio) {
        if (crearNuevoNivel) {
            taulaSim.nuevoNivelAmbito();
        }
        
        boolean dentroDeFuncion = taulaSim.obtenerFuncionActual() != null;
        boolean tieneReturnValido = false;

    
        while (bloque != null) {
            SBase sentencia = bloque.getSentencia();
            bloque = bloque.getBloque();
            if (sentencia instanceof SDecConstante) {
                SDecConstante decConst = (SDecConstante) sentencia;
                processConstantDeclaration(decConst, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SDecVar) {
                SDecVar decVar = (SDecVar) sentencia;
                processVarDeclaration(decVar, false, false, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SDecArray) {
                SDecArray decArray = (SDecArray) sentencia;
                processArray(decArray, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SDecTupla) {
                SDecTupla decTupla = (SDecTupla) sentencia;
                procesarTupla(decTupla, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SAsignacion) {
                SAsignacion asignacion = (SAsignacion) sentencia;
                processAsignacion(asignacion, false, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SReturn) {
                SReturn retorno = (SReturn) sentencia;
                if (!dentroDeFuncion) {     // Si no estamos dentro de una función, es un error
                    ErrorManager.addError(3 , "Error: Sentencia 'return' no permitida en el contexto '" + contexto + "', en línea " + retorno.getLine() + ".");
                    continue;
                }
                procesarReturn(retorno, taulaSim, codigoIntermedio);
                tieneReturnValido = true;
            } else if (sentencia instanceof SLlamadaFuncion) {
                SLlamadaFuncion llamadaFuncion = (SLlamadaFuncion) sentencia;
                procesarLlamadaFuncion(llamadaFuncion, taulaSim, codigoIntermedio);
                if (!llamadaFuncion.isEsVoid() && !dentroDeFuncion) {
                    ErrorManager.addError(3 , "Error: Llamada a función con retorno no utilizada dentro del bloque '" + contexto + "', en línea " + llamadaFuncion.getLine() + ".");
                }
            } else if (sentencia instanceof SIf) {
                SIf ifSentencia = (SIf) sentencia;
                procesarIf(ifSentencia, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SWhile) {
                SWhile whileSentencia = (SWhile) sentencia;
                procesarWhile(whileSentencia, taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SFor) {
                SFor forSentencia = (SFor) sentencia;
                procesarFor(forSentencia.getInicializacion(), forSentencia.getCondicion(), forSentencia.getActualizacion(), forSentencia.getBloque(), taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SPrint) {
                SPrint printSentencia = (SPrint) sentencia;
                procesarPrint(printSentencia.getLista(), taulaSim, codigoIntermedio);
            } else if (sentencia instanceof SInput) {
                SInput inputSentencia = (SInput) sentencia;
                procesarInput(inputSentencia, taulaSim);
            } else {
                ErrorManager.addError(3 , "Error: Sentencia no reconocida dentro del bloque '" + contexto + "'.");
            }
        } 
    
        if (dentroDeFuncion && contexto.equals("Funcion")) {
            String funcionActual = taulaSim.obtenerFuncionActual();
            Descripcio desc = taulaSim.consultar(funcionActual);

            if (desc instanceof DFuncion) {
                DFuncion funcion = (DFuncion) desc;
                if (!funcion.getTipoRetorno().equals(new TipoSubyacente(Tipus.VOID)) && !tieneReturnValido) {
                    // Si no es void y no hay return, es un error
                    ErrorManager.addError(3 , "Error: La función '" + funcionActual + "' debe contener un 'return' válido en todas las rutas posibles.");
                }
            }

        }

        if (crearNuevoNivel) {
            taulaSim.eliminarNivelAmbito();
        }
    }
    
    

    /**
     * Método para procesar todo tipo de expresiones
     * 
     * @param expresion     Nodo que contiene la info de la expresión
     * @param taulaSim      Tabla de Símbolos
     * @return              Una instancia de SExpresion válida o una instancia vacía con error.
     */
    public static SExpresion procesarExpresion(SExpresion expresion, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        // Si la expresión es nula, devolver una expresión vacía
        if (expresion == null) {
            ErrorManager.addError(3 , "Error: Expresión nula detectada en " + expresion + ", en línea " + expresion.getLine() + ".");
            return new SExpresion();
        }
    
        // Si la expresión es un valor simple
        if (expresion.getValor() != null) {
    
            SValor valorProcesado = procesarValor(expresion.getValor());
            if (valorProcesado.isError()) {
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }
            expresion.setTipo(valorProcesado.getTipo());
            String varGenerada = codigoIntermedio.generarExpresion("COPY", valorProcesado.getValor(), "");
            expresion.setVarGenerada(varGenerada);
            return expresion;
        }
    
        // Si la expresión es una llamada a función
        if (expresion.getLlamadaFuncion() != null) {
            SLlamadaFuncion llamadaFuncion = expresion.getLlamadaFuncion();
            procesarLlamadaFuncion(llamadaFuncion, taulaSim, codigoIntermedio);

            if (llamadaFuncion.isEsVoid()) {
                ErrorManager.addError(3 , "Error: No se puede usar una función 'void' como parte de una expresión, en línea " + expresion.getLine() + ".");
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }
    
            // Asignar el tipo de retorno de la función a la expresión
            Descripcio desc = taulaSim.consultar(llamadaFuncion.getIdFuncion());
            if (desc instanceof DFuncion) {
                TipoSubyacente tipoRetorno = ((DFuncion) desc).getTipoRetorno();
                expresion.setTipo(tipoRetorno);
            } else {
                ErrorManager.addError(3 , "Error: La función '" + llamadaFuncion.getIdFuncion() + "' no está declarada correctamente, en línea " + expresion.getLine() + ".");
                return new SExpresion(); // Devolver expresión vacía
            }
    
            return expresion; // Todo correcto
        }
    
        // Si la expresion es una llamada a un array
        if (expresion.getArray() != null) {
            SLlamadaArray llamadaArray = expresion.getArray();
            SLlamadaArray arrayProcesado = procesarLlamadaArray(llamadaArray, taulaSim);

            if (arrayProcesado.isError()) {
                return new SExpresion();
            }

            // Asignar el tipo del array a la expresión 
            Descripcio desc = taulaSim.consultar(arrayProcesado.getArrayName());
            if (desc instanceof DArray) {
                expresion.setTipo(((DArray) desc).getTipo());
            } else {
                ErrorManager.addError(3 , "Error interno: El ID '" + arrayProcesado.getArrayName() + "' no corresponde a un array, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }
            return expresion;
        }

        // Si la expresion es una llamada a una tupla
        if (expresion.getTupla() != null) {
            SLlamadaTupla llamadaTupla = expresion.getTupla();
            SLlamadaTupla tuplaProcesada = procesarLlamadaTupla(llamadaTupla, taulaSim);

            if (tuplaProcesada.isError()) {
                return new SExpresion();
            }

            Descripcio desc = taulaSim.consultar(tuplaProcesada.getTuplaName());
            if (desc instanceof DTupla) {
                DTupla tupla = (DTupla) desc;
                TipoSubyacente tipoCampo = tupla.getTipoCampo(tuplaProcesada.getFieldName());
                if (tipoCampo != null) {
                    expresion.setTipo(tipoCampo);
                } else {
                    ErrorManager.addError(3 , "Error interno: El campo '" + tuplaProcesada.getFieldName() + "' no tiene un tipo asociado, en línea " + expresion.getLine() + ".");
                    return new SExpresion();
                }
            } else {
                ErrorManager.addError(3 , "Error interno: El ID '" + tuplaProcesada.getTuplaName() + "' no corresponde a una tupla, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }
            return expresion;
        }

        // Si es una operación compuesta
        if (expresion.getOperador() != null && !esOperadorComparacion(expresion.getOperador())) {
            // Procesar las subexpresiones
            SExpresion e1Procesada = procesarExpresion(expresion.getE1(), taulaSim, codigoIntermedio);
            SExpresion e2Procesada = procesarExpresion(expresion.getE2(), taulaSim, codigoIntermedio);

            if (e1Procesada.getTipo() == null || e2Procesada.getTipo() == null) {
                ErrorManager.addError(3 , "Error: Tipos inválidos en los operandos de la operación '" + expresion.getOperador() + "', en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }

            // Verificar compatibilidad de tipos
            if (!e1Procesada.getTipo().esCompatibleCon(e2Procesada.getTipo())) {
                ErrorManager.addError(3 , "Error: Tipos incompatibles en la operación '" + expresion.getOperador() + "', en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }

            // Determinar el tipo de la operación según el operador
            TipoSubyacente tipoResultado = determinarTipoOperacion(e1Procesada.getTipo(), e2Procesada.getTipo(), expresion.getOperador());
            if (tipoResultado == null) {
                ErrorManager.addError(3 , "Error: Operación '" + expresion.getOperador() + "' no válida para los tipos proporcionados, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }

            String varGenerada = codigoIntermedio.generarExpresion(expresion.getOperador(), e1Procesada.getVarGenerada(), e2Procesada.getVarGenerada());
            expresion.setVarGenerada(varGenerada);

            // Asignar el tipo resultante a la expresión
            expresion.setTipo(tipoResultado);

            return expresion;
        }

        // Si la expresión es una comparación
        if (expresion.getOperador() != null && esOperadorComparacion(expresion.getOperador())) {
            // Procesar las subexpresiones
            SExpresion e1Procesada = procesarExpresion(expresion.getE1(), taulaSim, codigoIntermedio);
            SExpresion e2Procesada = procesarExpresion(expresion.getE2(), taulaSim, codigoIntermedio);

            if (e1Procesada.isError() || e2Procesada.isError()) {
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }

            // Validar tipos: Solo se permiten comparaciones entre tipos numéricos
            if (!e1Procesada.getTipo().esNumerico() || !e2Procesada.getTipo().esNumerico()) {
                ErrorManager.addError(3 , "Error: Operadores de comparación solo válidos para tipos numéricos, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }

            String varGenerada = codigoIntermedio.generarExpresion(expresion.getOperador(), e1Procesada.getVarGenerada(), e2Procesada.getVarGenerada());
            expresion.setVarGenerada(varGenerada);

            // Asignar el tipo BOOLEAN a la expresión de comparación
            expresion.setTipo(new TipoSubyacente(Tipus.BOOLEAN));

            return expresion;
        }

        // Si la expresion es un id
        if (expresion.getID() != null) {
            Descripcio expr_id = taulaSim.consultar(expresion.getID());
            if (expr_id == null) {
                ErrorManager.addError(3 , "Error: ID no declarado, expresión inválida, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }

            // Generar una nueva variable temporal
            String temporal = codigoIntermedio.nuevaVariableTemporal();

            // Generar la instrucción COPY para asociar el valor del ID al temporal
            codigoIntermedio.agregarInstruccion("COPY", expresion.getID(), "", temporal);

            // Configurar la referencia generada por la expresión
            expresion.setVarGenerada(temporal);

            // Comprobamos que tipo de descripción válida es
            if (expr_id instanceof DConst) {
                DConst id_table = (DConst) expr_id;
                expresion.setTipo(id_table.getTipoSubyacente());
            } else if (expr_id instanceof DVar) {
                DVar id_table = (DVar) expr_id;
                expresion.setTipo(id_table.getTipoSubyacente());
            } else {
                ErrorManager.addError(3 , "Error: El id de una expresión debe ser una variable o constante, en línea " + expresion.getLine() + ".");
                return new SExpresion();
            }
        
            return expresion;
        }

        // Si llega aquí, la expresión no es válida
        ErrorManager.addError(3 , "Error: Expresión inválida, en línea " + expresion.getLine() + ".");
        return new SExpresion();
    }





    //---------------------------------------------------------------------------------------------------------------------
    
    //------- MÉTODOS AUXILIARES ------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------------------



    /**
     * Método para procesar las dimensiones de un array, comprueba que son de tipo entero
     * 
     * @param id            Identificador del array
     * @param dimension     Nodo asociado a las dimensiones del array
     * @param desc_array    Descripción del array en la Tabla de Símbolos
     * @param taulaSim      Tabla de Símbolo
     * @return              Si no hay error, devuelve el mismo nodo dimensión
     *                      Si hay error, devuelve un constructor vacio
     */
    public static SListaDimensiones procesarDimension(String id, SListaDimensiones dimension, DArray desc_array, TaulaSimbols taulaSim) {
        
        SListaDimensiones dimension_local = dimension;
        int i = 1;
        do {
            SValor valor_procesado = procesarValor(dimension_local.getSize());
            if (valor_procesado.isError()) {
                return new SListaDimensiones(); // Valor con rangos incorrectos
            }
            if (!valor_procesado.getTipo().equals(new TipoSubyacente(Tipus.INT))) {
                ErrorManager.addError(3 , "Error: La dimension número " + i + " del array no es un entero, sino que es " + valor_procesado.getTipo() + ", en línea " + dimension_local.getLine() + ".");
                return new SListaDimensiones();
            }

            desc_array.addDimension(valor_procesado.getValor());
            dimension_local = dimension_local.getListaDimensiones();
            i++;
        } while (dimension_local != null);

        return dimension; 
    }


    /**
     * Método para procesar la lista de parámetros de una función
     * 
     * @param id            Identificador de una función
     * @param parametro     Nodo asociado a la lista de todos los parámetros
     * @param descD         Descripción de la Función en la Tabla de Símbolos
     * @param taulaSim      Tabla de Símbolos
     * @return              Si no hay error, devuelve el mismo nodo parametro
     *                      Si hay error, devuelve el nodo vacio
     */
    public static SListaParametros procesarListaParametros(String id, SListaParametros parametro, DFuncion descD, TaulaSimbols taulaSim) {
        SListaParametros parametro_local = parametro;
        do {
            if (taulaSim.consultar(parametro_local.getID()) != null ) {
                ErrorManager.addError(3 , "Error: Redefinición del parámetro '" + parametro_local.getID() + "', en línea " + parametro_local.getLine() + ".");
                return new SListaParametros();
            }

            descD.addParametro(new TipoSubyacente(parametro_local.getTipo()), parametro_local.getID());

            parametro_local = parametro_local.getParametro();
        } while ((parametro_local != null));

        taulaSim.posar(id, descD);
        return parametro;
    }


    /**
     * Verifica si el valor proporcionado está dentro del rango permitido para su tipo.
     *
     * @param valor El valor a verificar.
     * @param tipo El TipoSubyacente del valor.
     * @return true si el valor está dentro del rango permitido, false si no lo está.
     */
    private static boolean esValorValido(Object valor, TipoSubyacente tipo) {
        if (tipo == null || valor == null) return false;
    
        int byteSize = TipoSubyacente.sizeOf(tipo.getTipoBasico());
        int bitSize = byteSize * 8;
    
        switch (tipo.getTipoBasico()) {
            case INT:
                System.out.println("Tipo de valor: " + valor.getClass().getName());

                if (valor instanceof Integer) {
                    long minValue = -(1L << (bitSize - 1));
                    long maxValue = (1L << (bitSize - 1)) - 1;
                    int intValue = (Integer) valor;
                    return intValue >= minValue && intValue <= maxValue;
                }
                break;
    
            case FLOAT:
                if (valor instanceof Float) {
                    // Para float, asumiremos los valores en 32 bits de IEEE-754 (4 bytes)
                    return byteSize >= 4; // En este caso, nos aseguramos que `byteSize` sea al menos 4 bytes
                }
                break;
    
            case CHAR:
                if (valor instanceof String) {
                    String strValue = (String) valor;
                    if (strValue.length() == 1) {
                        int charValue = (int) strValue.charAt(0);
                        return charValue >= 0 && charValue <= ((1 << bitSize) - 1); // Verifica el rango del char en bits
                    }
                }
                break;
    
            case BOOLEAN:
                // Suponiendo que boolean ocupa 1 byte (true o false)
                return valor instanceof Boolean;
    
            default:
                return true; // Otros tipos pueden no requerir verificación aquí
        }
    
        // Si el tipo y el valor no coinciden, el valor no es válido
        return false;
    }


    /**
     * Método para procesar los argumentos de una llamada a una función
     * 
     * @param lista         Nodo asociado a la lista de los argumentos
     * @param funcion       Descripción de la Función en la tabla de símbolos
     * @param taulaSim      Tabla de Símbolos
     * @return              Una instancia de SListaArgumentos válida o una instancia vacía con error.
     */
    public static SListaArgumentos procesarListaArgumentos(SListaArgumentos lista, DFuncion funcion, TaulaSimbols taulaSim, CodigoIntermedio codigoIntermedio) {
        int i = 0;
        SListaArgumentos lista_local = lista;
        do {
            if (procesarExpresion(lista_local.getExpresion(), taulaSim, codigoIntermedio).isError()) {
                return new SListaArgumentos();
            }
            SExpresion expresion = procesarExpresion(lista_local.getExpresion(), taulaSim, codigoIntermedio);
            if (!funcion.getListaTipos().get(i).equals(expresion.getTipo())) {
                ErrorManager.addError(3 , "Error: El tipo de la expresion que es: " + expresion.getTipo() + " no coincide con el tipo de la funcion que es: " + funcion.getListaTipos().get(i) + ", en línea " + lista_local.getLine() + ".");
                return new SListaArgumentos();
            }
            i++;
            lista_local = lista_local.getArgumentos();
        } while (lista_local != null);
        return lista;
    }

    /**
     * Método para obtener el tipo de una función
     * 
     * @param idFuncion     Identificador de la función
     * @param taulaSim      Tabla de Símbolos
     * @return              Devuelve el Tipo Subyacente de la función o null en caso de error
     */
    public static TipoSubyacente obtenerTipoFuncion(String idFuncion, TaulaSimbols taulaSim) {
        // Consultar la función en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(idFuncion);
    
        // Verificar que la función existe y es una descripción de función
        if (desc == null) {
            ErrorManager.addError(3 , "Error: La función '" + idFuncion + "' no está declarada.");
            return null; // Retorna null si no existe
        }
    
        if (!(desc instanceof DFuncion)) {
            ErrorManager.addError(3 , "Error: '" + idFuncion + "' no es una función.");
            return null; // Retorna null si no es una función
        }
    
        // Retornar el tipo de retorno de la función
        DFuncion funcion = (DFuncion) desc;
        return funcion.getTipoRetorno();
    }


    /**
     * Método para determinar el tipo que retorna una operación
     * 
     * @param tipo1         Tipo Subyacente del primer elemento
     * @param tipo2         Tipo Subyacente del segundo elemento
     * @param operador      Operador
     * @return              Una instancia de TipoSubyacente válida o una instancia vacía con error.
     */
    private static TipoSubyacente determinarTipoOperacion(TipoSubyacente tipo1, TipoSubyacente tipo2, String operador) {
        // Operaciones aritméticas
        if (operador.matches("[+\\-*/]")) {
            if (tipo1.esNumerico() && tipo2.esNumerico()) {
                return tipo1.getTipoBasico() == Tipus.FLOAT || tipo2.getTipoBasico() == Tipus.FLOAT
                    ? new TipoSubyacente(Tipus.FLOAT)
                    : new TipoSubyacente(Tipus.INT);
            }
        }
    
        // Operaciones lógicas
        if (operador.equals("&&") || operador.equals("||")) {
            if (tipo1.getTipoBasico() == Tipus.BOOLEAN && tipo2.getTipoBasico() == Tipus.BOOLEAN) {
                return new TipoSubyacente(Tipus.BOOLEAN);
            }
        }
    
        return null; // Operación no válida
    }

    /**
     * Método booleano que determina si el operador es comparador
     * 
     * @param operador      Operador
     * @return              True si es un operador comparador
     *                      False si es otro tipo de comparador
     */
    private static boolean esOperadorComparacion(String operador) {
        return operador.equals(">") || operador.equals("<") ||
            operador.equals(">=") || operador.equals("<=") ||
            operador.equals("==") || operador.equals("!=");
    }

    
}