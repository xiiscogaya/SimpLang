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
        if (procesarValor(valor).isError()) {
            // No procesamos nada más ya que se ha producido un error
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
        
        if (expresion != null && procesarExpresion(expresion, taulaSim).isError()) {
            return;
        }
        
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
        DVar descripcionVariable = new DVar(idType.getTipo());
        taulaSim.posar(id, descripcionVariable);
        taulaSim.imprimirTabla();
    }


    // Procesamiento de un Array
    public static void processArray(SType tipo, String id, SListaDimensiones dimension, TaulaSimbols taulaSim) {
        Descripcio desc = taulaSim.consultar(id);
        
        // Consultamos el id en la tabla de símbolos
        if (desc != null) {
            ErrorManager.addError("Error: Redefinición de mismo nombre en el array " + id);
            return;
        }
        
        DArray desc_array = new DArray(tipo.getTipo());
        if (procesarDimension(id, dimension, desc_array, taulaSim).isError()) {
            return;
        }

        taulaSim.imprimirTabla();

    }
    
    // Procesamiento de las dimensiones de un array
    public static SListaDimensiones procesarDimension(String id, SListaDimensiones dimension, DArray desc_array, TaulaSimbols taulaSim) {
        
        SListaDimensiones dimension_local = dimension;
        do {
            SValor valor_procesado = procesarValor(dimension_local.getSize());
            if (valor_procesado.isError()) {
                return new SListaDimensiones(); // Valor con rangos incorrectos
            }
            if (!valor_procesado.getTipo().equals(new TipoSubyacente(Tipus.INT))) {
                ErrorManager.addError("Error: La dimension del array no es un entero, sino que es " + valor_procesado.getTipo());
                return new SListaDimensiones();
            }

            desc_array.addDimension(valor_procesado.getValor());
            dimension_local = dimension_local.getListaDimensiones();
        } while (dimension_local != null);

        taulaSim.posar(id, desc_array);
        return dimension; 
    }

    // Procesamiento de una tupla
    public static void procesarTupla(String id, SListaTupla lista, TaulaSimbols taulaSim) {
        // Comprobar si hay error en los parametros
        if (taulaSim.consultar(id) != null) {
            ErrorManager.addError("Error: Redefinición de la tupla '" + id + "'.");
            return;
        }

        DTupla tupla = new DTupla();

        do {
            SExpresion expresionProcesada = procesarExpresion(lista.getExpresion(), taulaSim);
            if (expresionProcesada.isError()) {
                return; //Fallo en la expresión, abortamos
            }

            // Verificar que el tipo de la expresión coincide con el declarado 
            if (!lista.getTipo().getTipo().equals(expresionProcesada.getTipo())) {
                ErrorManager.addError("Error: Tipo incompatible en la tupla para el campo con tipo " + lista.getTipo().getTipo());
                return;
            }

            tupla.addParametro(lista.getTipo().getTipo(), expresionProcesada.getID());

            lista = lista.getLista();

        } while (lista != null);

        taulaSim.posar(id, tupla);
        taulaSim.imprimirTabla();
           
    }

    // Procesamiento de una asignación
    public static void processAsignacion(String id, SExpresion expresion, TaulaSimbols taulaSim) {
        // Verificar si el valor tiene errores
        if (procesarExpresion(expresion, taulaSim).isError()) {
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

        taulaSim.imprimirTabla();
    }

    public static SListaParametros procesarListaParametros(String id, SListaParametros parametro, DFuncion descD, TaulaSimbols taulaSim) {
        SListaParametros parametro_local = parametro;
        do {
            if (taulaSim.consultar(parametro_local.getID()) != null ) {
                ErrorManager.addError("Error: Redefinición del parámetro '" + parametro_local.getID() + "'.");
                return new SListaParametros();
            }

            descD.addParametro(parametro_local.getTipo().getTipo(), parametro_local.getID());

            parametro_local = parametro_local.getParametro();
        } while ((parametro_local != null));

        taulaSim.posar(id, descD);
        return parametro;
    }
    
        /**
     * Crea un SValor si el valor cumple con las restricciones de tipo y tamaño.
     * En caso contrario, devuelve un SValor vacío (con error).
     *
     * @param tipo TipoSubyacente del valor que se va a crear.
     * @param valor El valor específico a asignar.
     * @return Una instancia de SValor válida o una instancia vacía con error.
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
                            ErrorManager.addError("Error: Valor fuera de rango para el tipo " + tipo);
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError("Error: Número demasiado grande para el tipo " + tipo);
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    break;
                    
                case FLOAT:
                    try {
                        float floatValue = Float.parseFloat(valor);
                        if (!esValorValido(floatValue, tipo)) {
                            ErrorManager.addError("Error: Valor fuera de rango para el tipo " + tipo);
                            return new SValor(); // Devuelve un SValor vacío con error
                        }
                    } catch (NumberFormatException e) {
                        ErrorManager.addError("Error: Número demasiado grande para el tipo " + tipo);
                        return new SValor(); // Devuelve un SValor vacío con error
                    }
                    break;
                    
                case STRING:
                    // No hacemos nada
                    return sim_valor;
                case BOOLEAN:
                    // No hacemos nada
                    return sim_valor;
                    
                default:
                    ErrorManager.addError("Error: Tipo no compatible para el valor proporcionado.");
                    return new SValor(); // Devuelve un SValor vacío con error
            }
            
        }
        return sim_valor;

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
    
        // Caso `return;` (sin expresión)
        if (retorno.getExpresion() == null) {
            if (!tipoRetornoEsperado.equals(new TipoSubyacente(Tipus.VOID))) {
                ErrorManager.addError("Error: La función '" + funcionActual + "' debe retornar un valor de tipo '" + tipoRetornoEsperado + "'.");
            }
            return;
        }
    
        // Caso `return expresion;`
        SExpresion expresionProcesada = procesarExpresion(retorno.getExpresion(), taulaSim);
        if (expresionProcesada.isError()) {
            return;
        }

        // Verificar compatibilidad de tipos
        if (!tipoRetornoEsperado.equals(expresionProcesada.getTipo())) {
            ErrorManager.addError("Error: El tipo de la expresión retornada ('" + expresionProcesada.getTipo() + "') no coincide con el tipo de retorno declarado ('" + tipoRetornoEsperado + "').");
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
            TipoSubyacente tipoParametro = parametros.getTipo().getTipo();
            String nombreParametro = parametros.getID();
    
            // Verificar si el nombre del parámetro ya existe en el ámbito actual
            if (taulaSim.consultar(nombreParametro) != null) {
                ErrorManager.addError("Error: Redefinición del parámetro '" + nombreParametro + "' en la función '" + id + "'.");
                continue;
            }
    
            // Registrar el parámetro como una variable
            DVar variable = new DVar(tipoParametro);
            taulaSim.posar(nombreParametro, variable);
            parametros = parametros.getParametro();
        } while (parametros != null);
        

        // Variable para indicar si una funcion tiene return
        boolean tieneReturnValido = false;
    
        // Procesar el bloque de la función
        
        do {
            SBase sentencia = cuerpo.getSentencia();
            if (sentencia instanceof SDecConstante) {
                SDecConstante decConst = (SDecConstante) sentencia;
                processConstantDeclaration(decConst.getTipo(), decConst.getId(), decConst.getValor(), taulaSim);
            } else if (sentencia instanceof SDecVar) {
                SDecVar decVar = (SDecVar) sentencia;
                processVarDeclaration(decVar.getTipo(), decVar.getId(), decVar.getTipoExpresion(), taulaSim);
            } else if (sentencia instanceof SDecArray) {
                SDecArray decArray = (SDecArray) sentencia;
                processArray(decArray.getTipo(), decArray.getId(), decArray.getDimensiones() , taulaSim);
            } else if (sentencia instanceof SDecTupla) {
                SDecTupla decTupla = (SDecTupla) sentencia;
                procesarTupla(decTupla.getId(), decTupla.getListaParametros(), taulaSim);
            } else if (sentencia instanceof SAsignacion) {
                SAsignacion asignacion = (SAsignacion) sentencia;
                processAsignacion(asignacion.getId(), asignacion.getTipoExpresion(), taulaSim);
            } else if (sentencia instanceof SReturn) {
                SReturn retorno = (SReturn) sentencia;
                procesarReturn(retorno, taulaSim);
                tieneReturnValido = true;
            } else if (sentencia instanceof SLlamadaFuncion) {
                SLlamadaFuncion llamadaFuncion = (SLlamadaFuncion) sentencia;
                procesarLlamadaFuncion(llamadaFuncion, taulaSim);
                if (llamadaFuncion.isEsVoid()) {
                    ErrorManager.addError("Error: llamada a una funcion como sentencia y no como asignación");
                }
            } else {
                ErrorManager.addError("Error: Sentencia no reconocida dentro del cuerpo de la función '" + id + "'.");
            }
            cuerpo = cuerpo.getBloque();
        } while (cuerpo != null);
        

        // Verificar que la función tiene un return válido si no es de tipo void
        if (!tipoRetorno.getTipo().equals(new TipoSubyacente(Tipus.VOID)) && !tieneReturnValido) {
            ErrorManager.addError("Error: La función '" + id + "' debe retornar un valor de tipo '" + tipoRetorno.getTipo() + "'.");
        }
    
        // Eliminar el ámbito de la función
        taulaSim.eliminarNivelAmbito();

        taulaSim.salirFuncion();
    }

    public static SListaArgumentos procesarListaArgumentos(SListaArgumentos lista, DFuncion funcion, TaulaSimbols taulaSim) {
        int i = 0;
        SListaArgumentos lista_local = lista;
        do {
            if (procesarExpresion(lista_local.getExpresion(), taulaSim).isError()) {
                return new SListaArgumentos();
            }
            SExpresion expresion = procesarExpresion(lista_local.getExpresion(), taulaSim);
            if (!funcion.getListaTipos().get(i).equals(expresion.getTipo())) {
                ErrorManager.addError("Error: El tipo de la expresion que es: " + expresion.getTipo() + " no coincide con el tipo de la funcion que es: " + funcion.getListaTipos().get(i));
                return new SListaArgumentos();
            }
            i++;
            lista_local = lista_local.getArgumentos();
        } while (lista_local != null);
        return lista;
    }

    public static void procesarLlamadaFuncion(SLlamadaFuncion llamadaFuncion, TaulaSimbols taulaSim) {
        // Consultar la función en la tabla de símbolos
        Descripcio desc = taulaSim.consultar(llamadaFuncion.getIdFuncion());
    
        // Verificar que la función existe y es una descripción de función
        if (desc == null || !(desc instanceof DFuncion)) {
            ErrorManager.addError("Error: La función '" + llamadaFuncion.getIdFuncion() + "' no está declarada.");
            return;
        }
    
        DFuncion funcion = (DFuncion) desc;

        if (procesarListaArgumentos(llamadaFuncion.getArgumentos(), funcion, taulaSim).isError()) {
            return; // No hacer nada ya que hay un error en un nodo anterior
        }

        if (funcion.getTipoRetorno().equals(new TipoSubyacente(Tipus.VOID))) {
            llamadaFuncion.setEsVoid(true);
        }
    
        // Todo está correcto: la llamada a la función es válida
        System.out.println("Llamada a la funcion " + llamadaFuncion.getIdFuncion() + " correcta");
        taulaSim.imprimirTabla();
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
    
    public static void procesarMain(SMain main, TaulaSimbols taulaSim) {
        
        do {
            SBase sentencia = main.getSentencia();
            if (sentencia instanceof SDecConstante) {
                SDecConstante decConst = (SDecConstante) sentencia;
                processConstantDeclaration(decConst.getTipo(), decConst.getId(), decConst.getValor(), taulaSim);
            } else if (sentencia instanceof SDecVar) {
                SDecVar decVar = (SDecVar) sentencia;
                processVarDeclaration(decVar.getTipo(), decVar.getId(), decVar.getTipoExpresion(), taulaSim);
            } else if (sentencia instanceof SDecArray) {
                SDecArray decArray = (SDecArray) sentencia;
                processArray(decArray.getTipo(), decArray.getId(), decArray.getDimensiones() , taulaSim);
            } else if (sentencia instanceof SDecTupla) {
                SDecTupla decTupla = (SDecTupla) sentencia;
                procesarTupla(decTupla.getId(), decTupla.getListaParametros(), taulaSim);
            } else if (sentencia instanceof SAsignacion) {
                SAsignacion asignacion = (SAsignacion) sentencia;
                processAsignacion(asignacion.getId(), asignacion.getTipoExpresion(), taulaSim);
            } else if (sentencia instanceof SReturn) {
                ErrorManager.addError("Error: Sentencia return no disponible dentro del bloque Main.");
            } else if (sentencia instanceof SLlamadaFuncion) {
                SLlamadaFuncion llamadaFuncion = (SLlamadaFuncion) sentencia;
                procesarLlamadaFuncion(llamadaFuncion, taulaSim);
                if (!llamadaFuncion.isEsVoid()) {
                    ErrorManager.addError("Error: llamada a una funcion como sentencia y no como asignación");
                }
    
            } else {
                ErrorManager.addError("Error: Sentencia no reconocida dentro del bloque Main.");
            }   
            main = main.getMain();
        } while (main != null);
        
    }

    public static SExpresion procesarExpresion(SExpresion expresion, TaulaSimbols taulaSim) {
        // Si la expresión es nula, devolver una expresión vacía
        if (expresion == null) {
            ErrorManager.addError("Error: Expresión nula detectada en ." + expresion);
            return new SExpresion();
        }
    
        // Si la expresión es un valor simple
        if (expresion.getValor() != null) {
    
            SValor valorProcesado = procesarValor(expresion.getValor());
            if (valorProcesado.isError()) {
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }
            expresion.setTipo(valorProcesado.getTipo());
            return expresion;
        }
    
        // Si la expresión es una llamada a función
        if (expresion.getLlamadaFuncion() != null) {
            SLlamadaFuncion llamadaFuncion = expresion.getLlamadaFuncion();
            procesarLlamadaFuncion(llamadaFuncion, taulaSim);

            if (llamadaFuncion.isEsVoid()) {
                ErrorManager.addError("Error: No se puede usar una función 'void' como parte de una expresión.");
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }
    
            // Asignar el tipo de retorno de la función a la expresión
            Descripcio desc = taulaSim.consultar(llamadaFuncion.getIdFuncion());
            if (desc instanceof DFuncion) {
                TipoSubyacente tipoRetorno = ((DFuncion) desc).getTipoRetorno();
                expresion.setTipo(tipoRetorno);
            } else {
                ErrorManager.addError("Error: La función '" + llamadaFuncion.getIdFuncion() + "' no está declarada correctamente.");
                return new SExpresion(); // Devolver expresión vacía
            }
    
            return expresion; // Todo correcto
        }
    
        // Si es una operación compuesta
        if (expresion.getE1() != null && expresion.getE2() != null && expresion.getOperador() != null) {
            // Procesar las subexpresiones
            SExpresion e1Procesada = procesarExpresion(expresion.getE1(), taulaSim);
            SExpresion e2Procesada = procesarExpresion(expresion.getE2(), taulaSim);

            if (e1Procesada.getTipo() == null || e2Procesada.getTipo() == null) {
                ErrorManager.addError("Error: Tipos inválidos en los operandos de la operación '" + expresion.getOperador() + "'.");
                return new SExpresion();
            }

            // Verificar compatibilidad de tipos
            if (!e1Procesada.getTipo().esCompatibleCon(e2Procesada.getTipo())) {
                ErrorManager.addError("Error: Tipos incompatibles en la operación '" + expresion.getOperador() + "'.");
                return new SExpresion();
            }

            // Determinar el tipo de la operación según el operador
            TipoSubyacente tipoResultado = determinarTipoOperacion(e1Procesada.getTipo(), e2Procesada.getTipo(), expresion.getOperador());
            if (tipoResultado == null) {
                ErrorManager.addError("Error: Operación '" + expresion.getOperador() + "' no válida para los tipos proporcionados.");
                return new SExpresion();
            }

            // Asignar el tipo resultante a la expresión
            expresion.setTipo(tipoResultado);

            return expresion;
        }

        // Si la expresión es una comparación
        if (expresion.getOperador() != null && esOperadorComparacion(expresion.getOperador())) {
            // Procesar las subexpresiones
            SExpresion e1Procesada = procesarExpresion(expresion.getE1(), taulaSim);
            SExpresion e2Procesada = procesarExpresion(expresion.getE2(), taulaSim);

            if (e1Procesada.isError() || e2Procesada.isError()) {
                return new SExpresion(); // Devolver expresión vacía en caso de error
            }

            // Validar tipos: Solo se permiten comparaciones entre tipos numéricos
            if (!e1Procesada.getTipo().esNumerico() || !e2Procesada.getTipo().esNumerico()) {
                ErrorManager.addError("Error: Operadores de comparación solo válidos para tipos numéricos.");
                return new SExpresion();
            }

            // Asignar el tipo BOOLEAN a la expresión de comparación
            expresion.setTipo(new TipoSubyacente(Tipus.BOOLEAN));

            return expresion;
    }

        // Si la expresion es un id
        if (expresion.getID() != null) {
            Descripcio expr_id = taulaSim.consultar(expresion.getID());
            if (expr_id == null) {
                ErrorManager.addError("Error: ID no declarado, expresión inválida");
                return new SExpresion();
            }

            // Comprobamos que tipo de descripción válida es
            if (expr_id instanceof DConst) {
                DConst id_table = (DConst) expr_id;
                expresion.setTipo(id_table.getTipoSubyacente());
            } else if (expr_id instanceof DVar) {
                DVar id_table = (DVar) expr_id;
                expresion.setTipo(id_table.getTipoSubyacente());
            } else {
                ErrorManager.addError("Error: El id de una expresión debe ser una variable o constante");
                return new SExpresion();
            }
        
            return expresion;
        }

        // Si llega aquí, la expresión no es válida
        ErrorManager.addError("Error: Expresión inválida.");
        return new SExpresion();
    }

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

    // Función para identificar operadores de comparación
    private static boolean esOperadorComparacion(String operador) {
        return operador.equals(">") || operador.equals("<") ||
            operador.equals(">=") || operador.equals("<=") ||
            operador.equals("==") || operador.equals("!=");
    }

    
}