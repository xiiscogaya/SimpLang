package compiler.sintactic;

import compiler.taulasimbols.*;

import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

import compiler.simbols.*;

public class SemanticHelper {

    
    public static void processConstantDeclaration(SType idType, String id, SValor valor, TaulaSimbols taulaSim) {
        // Comprobar si en la producción anterior se ha producido un error
        // Solo puede venir un fallo de la producción Valor, no de la Type
        if (valor.isError()) {
            return;
        }

        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de constante '" + id + "'");
            return;
        }

        // Verificar que el tipo de valor coincida con el tipo de la constante
        if (!valor.getTipo().equals(idType.getTipo())) {
            ErrorManager.addError("Error: Tipo incompatible en constante '" + id + "'");
            return;  // Retorna null para indicar que hubo un error
        }
        // Crear la descripción de la constante usando DConst
        DConst descripcionConstante = new DConst(valor.getValor(), idType.getTipo());
        taulaSim.posar(id, descripcionConstante);
        taulaSim.imprimirTabla();
        
    }


    // Procesamiento de una declaración de variable
    public static void processVarDeclaration(SType idType, String id, SExpresion expresion, TaulaSimbols taulaSim) {
        // Verificar si el ID ya está definido en el ámbito actual
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de la variable '" + id + "'");
            return;
        }
    
        // Si se asigna un valor inicial, verificar su tipo
        if (expresion != null) {
            if (expresion.isError()) {
                return;
            }
    
            if (!expresion.getTipo().equals(idType.getTipo())) {
                ErrorManager.addError("Error: Tipo incompatible en variable '" + id + "'");
                return;
            }
        }
    
        // Si no hay errores, la variable ya está creada, simplemente valida y registra su descripción
        DVar descripcionVariable = new DVar(expresion != null ? expresion.getTipo() : null, idType.getTipo());
        taulaSim.posar(id, descripcionVariable);
        taulaSim.imprimirTabla();
    }
    

    // Procesamiento de una asignación
    public static void processAsignacion(String id, SExpresion expresion, TaulaSimbols taulaSim) {
        // Verificar si el valor tiene errores
        if (expresion.isError()) {
            return;
        }
    
        // Consultar la variable en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(id);
    
        // Verificar si la variable está declarada
        if (desc == null) {
            ErrorManager.addError("Error: La variable '" + id + "' no está declarada.");
            return;
        }
    
        // Verificar que sea una variable y no otro tipo de símbolo
        if (!(desc instanceof DVar)) {
            ErrorManager.addError("Error: '" + id + "' no es una variable.");
            return;
        }
    
        // Verificar compatibilidad de tipo
        DVar variable = (DVar) desc;
        if (!variable.getTipoSubyacente().equals(expresion.getTipo())) {
            ErrorManager.addError("Error: Tipo incompatible en asignación a '" + id + "'.");
            return;
        }
    }

    public static SListaParametros procesarListaParametros(SListaParametros listaParametros, SType tipo, String id, TaulaSimbols taulaSim) {
        
        if (listaParametros == null) {
            listaParametros = new SListaParametros();
        }
    
        // Verificar si el identificador ya existe en el ámbito actual
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición del parámetro '" + id + "'.");
            listaParametros.setError(true);
            return listaParametros;
        }
    
        // Añadir el tipo y el nombre del parámetro a la lista
        listaParametros.addParametro(tipo.getTipo(), id);
        return listaParametros;
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
        if (valor instanceof String && tipo != null) {
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
                    
                case STRING:
                    // No realizamos comprobación ya que es de tamaño variable
                    return new SValor(tipo, valorStr);

                case BOOLEAN:
                    // No realizamos comprobación
                    return new SValor(tipo, valorStr);
                    
                default:
                    ErrorManager.addError("Error: Tipo no compatible para el valor proporcionado.");
                    return new SValor(); // Devuelve un SValor vacío con error
            }
            
        } else if (tipo == null) {
            return new SValor(valor.toString());
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

    public static void procesarReturn(SReturn retorno, TaulaSimbols taulaSim) {
        // Verificar si estamos dentro de una función
        String funcionActual = taulaSim.obtenerFuncionActual();
        if (funcionActual == null) {
            ErrorManager.addError("Error: 'return' sólo puede utilizarse dentro de una función.");
            return;
        }
    
        // Consultar la función actual
        Descripcio desc = taulaSim.consultar(funcionActual);
        if (!(desc instanceof DFuncion)) {
            ErrorManager.addError("Error interno: La función actual no está registrada correctamente.");
            return;
        }
    
        DFuncion funcion = (DFuncion) desc;
    
        // Verificar el tipo de retorno de la función
        TipoSubyacente tipoRetornoEsperado = funcion.getTipoRetorno();
    
        // Caso `return;` (sin valor)
        if (retorno.getValor() == null) {
            if (!tipoRetornoEsperado.equals(new TipoSubyacente(Tipus.VOID))) {
                ErrorManager.addError("Error: La función '" + funcionActual + "' debe retornar un valor de tipo '" + tipoRetornoEsperado + "'.");
            }
            return;
        }
    
        // Caso `return valor;`
        SValor valorRetorno = retorno.getValor();
        if (valorRetorno.getID() != null) {
            // Si el valor es un identificador, verificar su existencia y tipo
            Descripcio varDesc = taulaSim.consultar(valorRetorno.getID());
            if (varDesc == null) {
                ErrorManager.addError("Error: La variable '" + valorRetorno.getID() + "' no está declarada.");
                return;
            }
    
            if (!(varDesc instanceof DVar)) {
                ErrorManager.addError("Error: '" + valorRetorno.getID() + "' no es una variable válida para retornar.");
                return;
            }
    
            DVar variable = (DVar) varDesc;
    
            // Verificar el tipo de la variable
            if (!tipoRetornoEsperado.equals(variable.getTipoSubyacente())) {
                ErrorManager.addError("Error: El tipo de la variable '" + valorRetorno.getID() + "' no coincide con el tipo de retorno esperado ('" + tipoRetornoEsperado + "').");
            }
        } else {
            // Si el valor es una constante o literal, verificar el tipo directamente
            if (!tipoRetornoEsperado.equals(valorRetorno.getTipo())) {
                ErrorManager.addError("Error: El tipo del valor retornado ('" + valorRetorno.getTipo() + "') no coincide con el tipo de retorno declarado ('" + tipoRetornoEsperado + "').");
            }
        }
    }
    


    public static void procesarDecFuncion(SType tipoRetorno, String id, SListaParametros parametros, SBloque cuerpo, TaulaSimbols taulaSim) {
        // Verificar si la función ya está declarada en el ámbito global
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de la función '" + id + "'.");
            return;
        }
    
        // Registrar la función en la tabla de descripciones
        DFuncion descripcionFuncion = new DFuncion(
            tipoRetorno.getTipo(),
            parametros != null ? parametros.getTipos() : new ArrayList<>(),
            parametros != null ? parametros.getNombres() : new ArrayList<>()
        );
        taulaSim.posar(id, descripcionFuncion);

        taulaSim.entrarFuncion(id);
    
        // Crear un nuevo ámbito para la función
        taulaSim.nuevoNivelAmbito();
    
        // Registrar los parámetros en el nuevo ámbito
        if (parametros != null) {
            for (int i = 0; i < parametros.getTipos().size(); i++) {
                TipoSubyacente tipoParametro = parametros.getTipos().get(i);
                String nombreParametro = parametros.getNombres().get(i);
    
                // Verificar si el nombre del parámetro ya existe en el ámbito actual
                if (taulaSim.consultar(nombreParametro) != null) {
                    ErrorManager.addError("Error: Redefinición del parámetro '" + nombreParametro + "' en la función '" + id + "'.");
                    continue;
                }
    
                // Registrar el parámetro como una variable
                DVar parametro = new DVar(null, tipoParametro);
                taulaSim.posar(nombreParametro, parametro);
            }
        }

        // Variable para indicar si una funcion tiene return
        boolean tieneReturnValido = false;
    
        // Procesar el bloque de la función
        if (cuerpo != null) {
            for (SBase sentencia : cuerpo.getSentencias()) {
                if (sentencia instanceof SDecConstante) {
                    SDecConstante decConst = (SDecConstante) sentencia;
                    processConstantDeclaration(decConst.getTipo(), decConst.getId(), decConst.getValor(), taulaSim);
                } else if (sentencia instanceof SDecVar) {
                    SDecVar decVar = (SDecVar) sentencia;
                    processVarDeclaration(decVar.getTipo(), decVar.getId(), decVar.getTipoExpresion(), taulaSim);
                } else if (sentencia instanceof SAsignacion) {
                    SAsignacion asignacion = (SAsignacion) sentencia;
                    processAsignacion(asignacion.getId(), asignacion.getTipoExpresion(), taulaSim);
                } else if (sentencia instanceof SReturn) {
                    SReturn retorno = (SReturn) sentencia;
                    procesarReturn(retorno, taulaSim);
                    tieneReturnValido = true;
                } else if (sentencia instanceof SLlamadaFuncion) {
                    SLlamadaFuncion llamadaFuncion = (SLlamadaFuncion) sentencia;
                    procesarLlamadaFuncion(llamadaFuncion.getIdFuncion(), llamadaFuncion.getArgumentos(), taulaSim);
                } else {
                    ErrorManager.addError("Error: Sentencia no reconocida dentro del cuerpo de la función '" + id + "'.");
                }
            }
        }

        // Verificar que la función tiene un return válido si no es de tipo void
        if (!tipoRetorno.getTipo().equals(new TipoSubyacente(Tipus.VOID)) && !tieneReturnValido) {
            ErrorManager.addError("Error: La función '" + id + "' debe retornar un valor de tipo '" + tipoRetorno.getTipo() + "'.");
        }
    
        // Eliminar el ámbito de la función
        taulaSim.eliminarNivelAmbito();

        taulaSim.salirFuncion();
    }

    public static SListaArgumentos procesarListaArgumentos(SListaArgumentos lista, SValor valor, TaulaSimbols taulaSim) {
        if (lista == null) {
            lista = new SListaArgumentos();
        }
    
        // Verificar si el identificador ya existe en el ámbito actual
        if (taulaSim.consultar(valor.getID()) != null) {
            ErrorManager.addError("Error: Redefinición del parámetro '" + valor.getID() + "'.");
            lista.setError(true);
            return lista;
        }
    
        // Añadir el tipo y el nombre del parámetro a la lista
        lista.addArgumento(valor);
        return lista;
    }

    public static void procesarLlamadaFuncion(String id, SListaArgumentos lista, TaulaSimbols taulaSim) {
        // Consultar la función en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(id);
    
        // Verificar que la función existe y es una descripción de función
        if (desc == null || !(desc instanceof DFuncion)) {
            ErrorManager.addError("Error: La función '" + id + "' no está declarada.");
            return;
        }
    
        DFuncion funcion = (DFuncion) desc;
    
        // Verificar número de argumentos
        if (funcion.getListaTipos().size() != lista.getArgumentos().size()) {
            ErrorManager.addError(
                "Error: La función '" + id + "' espera " 
                + funcion.getListaTipos().size() 
                + " argumentos, pero se pasaron " 
                + lista.getArgumentos().size() + "."
            );
            return;
        }
    
        // Verificar tipos de argumentos
        for (int i = 0; i < lista.getArgumentos().size(); i++) {
            TipoSubyacente tipoEsperado = funcion.getListaTipos().get(i);
            SValor argumento = lista.getArgumentos().get(i);
    
            // Verificar compatibilidad de tipos
            if (!tipoEsperado.equals(argumento.getTipo())) {
                ErrorManager.addError(
                    "Error: Argumento " + (i + 1) 
                    + " de la función '" + id + "' tiene un tipo incompatible. "
                    + "Se esperaba '" + tipoEsperado + "', pero se recibió '" + argumento.getTipo() + "'."
                );
                return;
            }
        }
    
        // Todo está correcto: la llamada a la función es válida
        System.out.println("Llamada a la función '" + id + "' válida con argumentos: " + lista);
    }

    public static TipoSubyacente obtenerTipoFuncion(String idFuncion, TaulaSimbols taulaSim) {
        // Consultar la función en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(idFuncion);
    
        // Verificar que la función existe y es una descripción de función
        if (desc == null) {
            ErrorManager.addError("Error: La función '" + idFuncion + "' no está declarada.");
            return null; // Retorna null si no existe
        }
    
        if (!(desc instanceof DFuncion)) {
            ErrorManager.addError("Error: '" + idFuncion + "' no es una función.");
            return null; // Retorna null si no es una función
        }
    
        // Retornar el tipo de retorno de la función
        DFuncion funcion = (DFuncion) desc;
        return funcion.getTipoRetorno();
    }
    


}    