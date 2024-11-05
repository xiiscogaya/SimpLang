package compiler.sintactic;

import compiler.taulasimbols.*;

import java.util.ArrayList;
import java.util.List;

import compiler.simbols.*;

public class SemanticHelper {

    public static SDecConstante processConstantDeclaration(Object idType, String id, Object valor, TaulaSimbols taulaSim) {
        SType tipoConstante = (SType) idType;
        String idConstante = (String) id;
        SValor valorConstante = (SValor) valor;

        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(idConstante) != null) {
            ErrorManager.addError("Error: Redefinición de constante '" + idConstante + "'");
            return new SDecConstante();
        }

        // Verificar que el tipo de valor coincida con el tipo de la constante
        if (!valorConstante.getTipo().equals(tipoConstante.getTipo())) {
            ErrorManager.addError("Error: Tipo incompatible en constante '" + idConstante + "'");
            return new SDecConstante();  // Retorna null para indicar que hubo un error
        }
        // Crear la descripción de la constante usando DConst
        DConst descripcionConstante = new DConst(valorConstante.getValor(), tipoConstante.getTipo());
        taulaSim.posar(idConstante, descripcionConstante);
        taulaSim.imprimirTabla();
        return new SDecConstante(tipoConstante, idConstante, valorConstante);

    }


    // Procesamiento de una declaración de variable
    public static SDecVar processVarDeclaration(Object idType, String id, Object valor, TaulaSimbols taulaSim) {
        SType tipoVar = (SType) idType;
        String idVar = (String) id;

        if (valor != null) {
            SValor valorVar = (SValor) valor;

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
        }

        // Si valor = null
        DVar descripcionConstante = new DVar(null, tipoVar.getTipo());
        taulaSim.posar(idVar, descripcionConstante);
        taulaSim.imprimirTabla();
        return new SDecVar(tipoVar, idVar, null); 
    }

    // Procesamiento de una asignación
    public static SAsignacion processAsignacion(String id, Object valor, String operador, TaulaSimbols taulaSim) {
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

    public static SDecTupla processTuplaDeclaration(String id, STipoLista tipoLista, SValorLista valorLista, TaulaSimbols taulaSim) {
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de la tupla '" + id + "'.");
            return new SDecTupla();
        }

        // Extraer la lista de tipos desde STipoLista
        List<TipoSubyacente> tipos = new ArrayList<>();
        for (TipoSubyacente tipo : tipoLista.getTipos()) {
            tipos.add(tipo);
        }

        // Verificar que la cantidad de tipos y valores coincida
        if (tipos.size() != valorLista.getValores().size()) {
            ErrorManager.addError("Error: La cantidad de valores no coincide con la cantidad de tipos en la tupla '" + id + "'.");
            return new SDecTupla();
        }

        // Verificación de tipos para cada valor
        List<Object> valoresAsignados = new ArrayList<>();
        for (int i = 0; i < tipos.size(); i++) {
            TipoSubyacente tipoEsperado = tipos.get(i);
            SValor valorActual = valorLista.getValores().get(i);

            // Comprobar que el tipo de cada valor coincida con el tipo esperado
            if (!tipoEsperado.equals(valorActual.getTipo())) {
                ErrorManager.addError("Error: Tipo incompatible en la posición " + i + " de la tupla '" + id + "'.");
                return new SDecTupla();
            }

            // Añadimos el valor si los tipos coinciden
            valoresAsignados.add(valorActual.getValor()); 
        }
        
        // Crear una descripción para la tupla con los tipos convertidos
        DTupla descripcioTupla = new DTupla(tipos, valoresAsignados);

        // Añadir la tupla a la tabla de símbolos
        taulaSim.posar(id, descripcioTupla);
        taulaSim.imprimirTabla();

        // Retornar un nodo de declaración de tupla
        return new SDecTupla(id, tipoLista);
    }
}