package compiler.sintactic;

import compiler.taulasimbols.*;

import java.util.ArrayList;
import java.util.List;

import compiler.simbols.*;

public class SemanticHelper {

    
    public static SDecConstante processConstantDeclaration(SType idType, String id, SValor valor, TaulaSimbols taulaSim) {
        // Comprobar si en la producción anterior se ha producido un error
        // Solo puede venir un fallo de la producción Valor, no de la Type
        if (valor.isError()) {
            return new SDecConstante();
        }

        String idConstante = (String) id;
        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(idConstante) != null) {
            ErrorManager.addError("Error: Redefinición de constante '" + idConstante + "'");
            return new SDecConstante();
        }

        // Verificar que el tipo de valor coincida con el tipo de la constante
        if (!valor.getTipo().equals(idType.getTipo())) {
            ErrorManager.addError("Error: Tipo incompatible en constante '" + idConstante + "'");
            return new SDecConstante();  // Retorna null para indicar que hubo un error
        }
        // Crear la descripción de la constante usando DConst
        DConst descripcionConstante = new DConst(valor.getValor(), idType.getTipo());
        taulaSim.posar(idConstante, descripcionConstante);
        taulaSim.imprimirTabla();
        return new SDecConstante(idType, idConstante, valor);
        
    }


    // Procesamiento de una declaración de variable
    public static SDecVar processVarDeclaration(Object idType, String id, Object valor, TaulaSimbols taulaSim) {
        SType tipoVar = (SType) idType;
        String idVar = (String) id;
        
        // Comprobamos que es una declaración de variable con valor
        if (valor != null ) {
            SValor valorVar = (SValor) valor;
            // Comprobamos si en la producción Valor se produce un error
            if (!valorVar.isError()){
                // Verificar que el tipo de valor coincida con el tipo de la variable
                if (!valorVar.getTipo().equals(tipoVar.getTipo())) {
                    ErrorManager.addError("Error: Tipo incompatible en variable '" + idVar + "'");
                    return new SDecVar(); // Devuelve una variable en estado de error
                }
                // Crear la descripción de la variable usando DVar
                DVar descripcionConstante = new DVar(valorVar.getValor(), tipoVar.getTipo());
                taulaSim.posar(idVar, descripcionConstante);
                taulaSim.imprimirTabla();
                return new SDecVar(tipoVar, idVar, valorVar);
            } else {
                return new SDecVar();
            }
        }

        // Si valor = null
        DVar descripcionConstante = new DVar(null, tipoVar.getTipo());
        taulaSim.posar(idVar, descripcionConstante);
        taulaSim.imprimirTabla();
        return new SDecVar(tipoVar, idVar, null); 
    }

    // Procesamiento de una asignación
    public static SAsignacion processAsignacion(String id, Object valor, String operador, TaulaSimbols taulaSim) {
        SValor valorA = (SValor) valor;
        // Comprobamos si la producción Valor tiene algun error
        if (!valorA.isError()) {
            SValor nuevoValor = (SValor) valor;
            Descripcio desc = taulaSim.consultar(id);

            // Verificar si la variable está definida en la tabla de símbolos
            if (desc == null) {
                ErrorManager.addError("Error: La variable '" + id + "' no está declarada.");
                return new SAsignacion(); // Retorna un objeto vacío si hay error
            }

            // Verificar que la descripción sea de tipo variable
            if (!(desc instanceof DVar)) {
                ErrorManager.addError("Error: '" + id + "' no es una variable y no se le puede asignar un valor.");
                return new SAsignacion();
            }

            DVar variable = (DVar) desc;
            TipoSubyacente tipoVariable = variable.getTipoSubyacente();
            TipoSubyacente tipoValor = nuevoValor.getTipo();

            // Verificar la compatibilidad de tipo entre la variable y el nuevo valor
            if (!tipoVariable.equals(tipoValor)) {
                ErrorManager.addError("Error: Tipo incompatible en la asignación a '" + id + "'.");
                return new SAsignacion();
            }

            Object valorActual = variable.getValor();

            // Procesar según el operador
            try {
                switch (operador) {
                    case "=":
                        // Asignación simple
                        variable.setValor(nuevoValor.getValor());
                        break;

                    case "+=":
                    case "-=":
                    case "*=":
                    case "/=":
                        // Asignación compuesta (+=, -=, *=, /=)
                        if (!tipoVariable.esCompatibleConOperacionCompuesta()) {
                            ErrorManager.addError("Error: Operación '" + operador + "' no es compatible con el tipo de la variable '" + id + "'.");
                            return new SAsignacion();
                        }

                        // Ejecutar la operación compuesta y asignar el resultado
                        Object resultado = ejecutarOperacionCompuesta(valorActual, nuevoValor.getValor(), tipoVariable, operador);
                        if (resultado == null) {
                            // Error durante la operación compuesta (como división por cero)
                            return new SAsignacion();
                        }
                        variable.setValor(resultado);
                        break;

                    default:
                        ErrorManager.addError("Error: Operador '" + operador + "' no soportado en la asignación a '" + id + "'.");
                        return new SAsignacion();
                }
            } catch (ArithmeticException e) {
                // Capturar división por cero u otros errores aritméticos
                ErrorManager.addError("Error: " + e.getMessage() + " en operación '" + operador + "' para la variable '" + id + "'.");
                return new SAsignacion();
            }
            taulaSim.imprimirTabla();
            return new SAsignacion(id, nuevoValor);
        } else {
            return new SAsignacion();
        }
    }

    // Método auxiliar para ejecutar la operación compuesta
    private static Object ejecutarOperacionCompuesta(Object valorActual, Object nuevoValor, TipoSubyacente tipoVariable, String operador) {
        // Convertir valores explícitamente según el tipo subyacente
        if (tipoVariable.esNumerico()) {
            if (tipoVariable.equals(new TipoSubyacente(Tipus.INT))) {
                int actual = convertirAEntero(valorActual);
                int nuevo = convertirAEntero(nuevoValor);
                switch (operador) {
                    case "+=": return actual + nuevo;
                    case "-=": return actual - nuevo;
                    case "*=": return actual * nuevo;
                    case "/=":
                        if (nuevo == 0) {
                            ErrorManager.addError("Error: División entre cero en operación '/='.");
                            return null;
                        }
                        return actual / nuevo;
                }
            } else if (tipoVariable.equals(new TipoSubyacente(Tipus.FLOAT))) {
                float actual = convertirAFlotante(valorActual);
                float nuevo = convertirAFlotante(nuevoValor);
                switch (operador) {
                    case "+=": return actual + nuevo;
                    case "-=": return actual - nuevo;
                    case "*=": return actual * nuevo;
                    case "/=":
                        if (nuevo == 0.0f) {
                            ErrorManager.addError("Error: División entre cero en operación '/='.");
                            return null;
                        }
                        return actual / nuevo;
                }
            }
        }

        ErrorManager.addError("Error: Tipos incompatibles para la operación '" + operador + "'.");
        return null;
    }

    // Métodos auxiliares de conversión
    private static int convertirAEntero(Object valor) {
        if (valor instanceof Integer) {
            return (Integer) valor;
        } else if (valor instanceof String) {
            try {
                return Integer.parseInt((String) valor);
            } catch (NumberFormatException e) {
                ErrorManager.addError("Error: No se puede convertir el valor a entero.");
                return 0; // Valor por defecto en caso de error
            }
        } else {
            ErrorManager.addError("Error: Tipo no compatible para entero: " + valor.getClass().getName());
            return 0; // Valor por defecto en caso de tipo no compatible
        }
    }

    private static float convertirAFlotante(Object valor) {
        if (valor instanceof Float) {
            return (Float) valor;
        } else if (valor instanceof String) {
            try {
                return Float.parseFloat((String) valor);
            } catch (NumberFormatException e) {
                ErrorManager.addError("Error: No se puede convertir el valor a flotante.");
                return 0.0f; // Valor por defecto en caso de error
            }
        } else if (valor instanceof Integer) {
            return ((Integer) valor).floatValue();
        } else {
            ErrorManager.addError("Error: Tipo no compatible para entero: " + valor.getClass().getName());
            return 0.0f; // Valor por defecto en caso de tipo no compatible
        }
    }

    // Instancia de SListaParametros que se irá acumulando entre llamadas
    private static SListaParametros listaParametros = new SListaParametros(new ArrayList<>(), new ArrayList<>());

    /**
     * Procesa un tipo y un valor, realizando las comprobaciones necesarias.
     * Si el tipo y valor son compatibles, se añaden a la lista acumulada `SListaParametros`.
     * Si hay algún error, se devuelve una instancia vacía de `SListaParametros`.
     *
     * @param tipo El tipo de parámetro.
     * @param valor El valor correspondiente al parámetro.
     * @return Una instancia de `SListaParametros` acumulada si no hay errores, o una vacía si hubo error.
     */
    public static SListaParametros procesarListaParametros(SType tipo, SValor valor) {
        // Si hay error en una producción anterior
        if (valor.isError()) {
            return new SListaParametros();
        }

        // Comprobar si ya hubo un error en una llamada anterior a `procesarListaParametros`
        if (listaParametros.isError()) {
            return listaParametros; // Devuelve la instancia de error acumulada
        }

        // Comprobar la compatibilidad de tipo
        if (!tipo.getTipo().equals(valor.getTipo())) {
            ErrorManager.addError("Error: Tipo incompatible para el valor proporcionado.");
            listaParametros = new SListaParametros(); // Reinicia `listaParametros` como instancia vacía con error
            return listaParametros;
        }

        // Si no hay error, añadir el tipo y el valor a `listaParametros`
        listaParametros.addParametro(tipo, valor);
        
        return listaParametros;
    }

    public static SDecTupla processTuplaDeclaration(String id, SListaParametros listaParametros, TaulaSimbols taulaSim) {
        // Comprobar si hubo un error en `listaParametros`
        if (listaParametros.isError()) {
            return new SDecTupla(); // Devuelve una instancia vacía con error
        }
        
        // Verificar si ya existe una declaración con el mismo id
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de la tupla '" + id + "'.");
            return new SDecTupla(); // Devuelve una instancia vacía con error
        }
    
        // Crear una lista de tipos y valores para la tupla
        List<TipoSubyacente> tipos = listaParametros.getTipos();
        List<Object> valores = new ArrayList<>();
    
        for (SValor valor : listaParametros.getValores()) {
            valores.add(valor.getValor());
        }
    
        // Crear la descripción de la tupla y agregarla a la tabla de símbolos
        DTupla descripcioTupla = new DTupla(tipos, valores);
        taulaSim.posar(id, descripcioTupla);
        taulaSim.imprimirTabla();
    
        // Retornar una instancia de `SDecTupla` para construir el árbol sintáctico
        return new SDecTupla(id, listaParametros);
    }
    
    
    /**
     * Crea un SValor si el valor cumple con las restricciones de tipo y tamaño.
     * En caso contrario, devuelve un SValor vacío (con error).
     *
     * @param tipo TipoSubyacente del valor que se va a crear.
     * @param valor El valor específico a asignar.
     * @return Una instancia de SValor válida o una instancia vacía con error.
     */
    public static SValor crearValorConComprobacion(TipoSubyacente tipo, Object valor) {
        if (valor instanceof String) {
            String valorStr = (String) valor;
            
            switch (tipo.getTipoBasico()) {
                case INT:
                    try {
                        int intValue = Integer.parseInt(valorStr);
                        if (esValorValido(intValue, tipo)) {
                            return new SValor(tipo, intValue);
                        } else {
                            ErrorManager.addError("Error: Valor fuera de rango para el tipo " + tipo);
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError("Error: Número demasiado grande para el tipo " + tipo);
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    
                case FLOAT:
                    try {
                        float floatValue = Float.parseFloat(valorStr);
                        if (esValorValido(floatValue, tipo)) {
                            return new SValor(tipo, floatValue);
                        } else {
                            ErrorManager.addError("Error: Valor fuera de rango para el tipo " + tipo);
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError("Error: Número demasiado grande para el tipo " + tipo);
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    
                case CHAR:
                    // Aquí asumimos que `CHAR` como tipo básico puede representar un `String`
                    if (valorStr.length() > TipoSubyacente.sizeOf(Tipus.CHAR) / 2) { // UTF-16 implica 2 bytes por `CHAR`
                        ErrorManager.addError("Error: Longitud de cadena excede el tamaño permitido para CHAR.");
                        return new SValor(); // Devuelve un SValor vacío con error
                    } else {
                        return new SValor(tipo, valorStr);
                    }
                    
                default:
                    ErrorManager.addError("Error: Tipo no compatible para el valor proporcionado.");
                    return new SValor(); // Devuelve un SValor vacío con error
            }
            
        } else if (esValorValido(valor, tipo)) {
            return new SValor(tipo, valor);
        }

        ErrorManager.addError("Error: Valor fuera de rango para el tipo " + tipo);
        return new SValor(); // Devuelve un SValor vacío con error
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
}    