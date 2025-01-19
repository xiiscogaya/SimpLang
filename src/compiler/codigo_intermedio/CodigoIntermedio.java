package compiler.codigo_intermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.sintactic.ErrorManager;
import compiler.taulasimbols.Descripcio;
import compiler.taulasimbols.TipoSubyacente;

public class CodigoIntermedio {

    private Map<String, Variable> tablaVariables;
    private Map<String, Procedimiento> tablaProcedimientos;
    private List<Instruccion> instrucciones;
    private int contadorEtiquetas;
    private int contadorVariablesTemporales;

    public CodigoIntermedio() {
        this.tablaVariables = new HashMap<>();
        this.tablaProcedimientos = new HashMap<>();
        this.instrucciones = new ArrayList<>();
        this.contadorVariablesTemporales = 0;
        this.contadorEtiquetas = 0;
    }

    public List<Instruccion> obtenerInstrucciones() {
        return this.instrucciones;
    }

    // Obtener lista de variables
    public Map<String, Variable> getTablaVariables() {
        return this.tablaVariables;
    }

    // Obtener lista de procedimientos
    public Map<String, Procedimiento> getTablaProcedimientos() {
        return this.tablaProcedimientos;
    }

    public String nuevaVariableTemporal() {
        String temporal = "t" + contadorVariablesTemporales++;
        tablaVariables.put(temporal, null);
        return temporal;
    }

    public String nuevaEtiqueta() {
        return "L" + contadorEtiquetas++;
    }

    public String getVariableTemp() {
        int variable = contadorVariablesTemporales;
        variable--;
        return "t" + variable;
    }

    public void agregarInstruccion(String operador, String operando1, String operando2, String destino) {
        instrucciones.add(new Instruccion(operador, operando1, operando2, destino));
    }

    public void registrarVariable(String idUnico, String nombre, String nombreFuncion, int tamañoTotal, TipoSubyacente tipo, Descripcio descripcio) {
        tablaVariables.put(idUnico, new Variable(nombre, nombreFuncion, tamañoTotal, tipo, descripcio));
    }

    public void registrarProcedimiento(String idUnico, String nombre, int numParametros, List<String> listaIDUnicos, String etiquetaInicio, Boolean yaAnalizado, int ocupacionLocales, Descripcio descripcio) {
        tablaProcedimientos.put(idUnico, new Procedimiento(nombre, numParametros, listaIDUnicos, etiquetaInicio, yaAnalizado, ocupacionLocales, descripcio));
    }

    public String obtenerEtiquetaInicio(String idUnico) {
        // Buscar el procedimiento en la tabla de procedimientos
        Procedimiento procedimiento = tablaProcedimientos.get(idUnico);
    
        // Verificar si el procedimiento existe
        if (procedimiento == null) {
            ErrorManager.addError(3, "Error: No se encontro el id: " + idUnico + " en la tabla de procedimientos");
            return null; // Retorna null si no se encuentra
        }
    
        // Retornar la etiqueta de inicio del procedimiento
        return procedimiento.etiquetaInicio;
    }
    

    public void imprimirCodigo() {
        for (Instruccion instr : instrucciones) {
            System.out.println(instr);
        }
    }

    // Generación de código para expresiones aritméticas
    public String generarExpresion(String operador, String operando1, String operando2) {
        String temporal = nuevaVariableTemporal();
        agregarInstruccion(operador, operando1, operando2, temporal);
        return temporal;
    }

    // Generación de código para condicionales
    public void generarIf(String operador, String operando1, String operando2, String destino) {
        switch (operador) {
            case "<":
                agregarInstruccion("IF_LT", operando1, operando2, destino);
                break;
            case ">":
                agregarInstruccion("IF_GT", operando1, operando2, destino);
                break;
            case ">=":
                agregarInstruccion("IF_GE", operando1, operando2, destino);
                break;
            case "<=":
                agregarInstruccion("IF_LE", operando1, operando2, destino);
                break;
            case "==":
                agregarInstruccion("IF_EQ", operando1, operando2, destino);
                break;
            case "!=":
                agregarInstruccion("IF_NE", operando1, operando2, destino);
                break;
            default:
                break;
        }
        
    }

    public void imprimirTablaVariables() {
        System.out.println("Tabla de Variables:");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.println("|  IDUnico |   Nombre  | Función            | Tamaño  | Descripción                              |");
        System.out.println("-----------------------------------------------------------------------------------------------------");
    
        for (Map.Entry<String, Variable> entry : tablaVariables.entrySet()) {
            String idUnico = entry.getKey();
            Variable variable = entry.getValue();
    
            if (variable != null) {
                String nombre = variable.nombre != null ? variable.nombre : "N/A";
                String funcion = variable.nombreFuncion != null ? variable.nombreFuncion : "Global";
                String tamaño = variable.tamañoTotal > 0 ? String.valueOf(variable.tamañoTotal) : "N/A";
                String descripcion = variable.descripcio != null ? variable.descripcio.toString() : "N/A";
    
                System.out.printf("| %-10s | %-10s | %-18s | %-7s | %-25s |\n", idUnico, nombre, funcion, tamaño, descripcion);
            } else {
                System.out.printf("| %-10s | %-10s | %-18s | %-7s | %-25s |\n", idUnico, "N/A", "N/A", "N/A", "N/A");
            }
        }
    
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }
    
    public void imprimirTablaProcedimientos() {
        System.out.println("Tabla de Procedimientos:");
        System.out.println("---------------------------------------------------");
        System.out.println("| Nombre         | Número de Parámetros | Etiqueta |");
        System.out.println("---------------------------------------------------");
    
        for (Map.Entry<String, Procedimiento> entry : tablaProcedimientos.entrySet()) {
            String idUnico = entry.getKey();
            Procedimiento procedimiento = entry.getValue();
    
            if (procedimiento != null) {
                System.out.printf("| %-10s | %-14s | %-20d | %-8s |\n", idUnico, procedimiento.nombre, procedimiento.numParametros, procedimiento.etiquetaInicio);
            } else {
                System.out.printf("| %10-s | %-14s | %-20s | %-8s |\n", idUnico, "N/A", "N/A", "N/A");
            }
        }
    
        System.out.println("---------------------------------------------------");
    }
    
}

