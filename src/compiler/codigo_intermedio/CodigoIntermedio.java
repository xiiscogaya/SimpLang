package compiler.codigo_intermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.taulasimbols.Descripcio;

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

    public String nuevaVariableTemporal() {
        String temporal = "t" + contadorVariablesTemporales++;
        tablaVariables.put(temporal, null);
        return temporal;
    }

    public String nuevaEtiqueta() {
        return "L" + contadorEtiquetas++;
    }

    public void agregarInstruccion(String operador, String operando1, String operando2, String destino) {
        instrucciones.add(new Instruccion(operador, operando1, operando2, destino));
    }

    public void registrarVariable(String nombre, String nombreFuncion, int tamañoTotal, Descripcio descripcio) {
        tablaVariables.put(nombre, new Variable(nombreFuncion, tamañoTotal, descripcio));
    }

    public void registrarProcedimiento(String nombre, int numParametros, String etiquetaInicio, Boolean yaAnalizado) {
        tablaProcedimientos.put(nombre, new Procedimiento(nombre, numParametros, etiquetaInicio, yaAnalizado));
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
    public void generarCondicional(String condicion, String etiquetaVerdadero, String etiquetaFalso) {
        agregarInstruccion("if", condicion, "", etiquetaVerdadero);
        agregarInstruccion("goto", "", "", etiquetaFalso);
    }

    // Generación de código para bloques if-else
    public void generarIfElse(String condicion, String etiquetaThen, String etiquetaElse, String etiquetaFin) {
        agregarInstruccion("if", condicion, "", etiquetaThen);
        agregarInstruccion("goto", "", "", etiquetaElse);
        agregarInstruccion(etiquetaThen + ":", "", "", "");
        agregarInstruccion("goto", "", "", etiquetaFin);
        agregarInstruccion(etiquetaElse + ":", "", "", "");
        agregarInstruccion(etiquetaFin + ":", "", "", "");
    }

    // Generación de código para bucles while
    public void generarWhile(String condicion, String etiquetaInicio, String etiquetaFin) {
        agregarInstruccion(etiquetaInicio + ":", "", "", "");
        agregarInstruccion("if", condicion, "", etiquetaFin);
        agregarInstruccion("goto", etiquetaInicio, "", "");
        agregarInstruccion(etiquetaFin + ":", "", "", "");
    }

    // Generación de código para bucles for
    public void generarFor(String inicializacion, String condicion, String actualizacion, String etiquetaInicio, String etiquetaFin) {
        agregarInstruccion(inicializacion, "", "", "");
        agregarInstruccion(etiquetaInicio + ":", "", "", "");
        agregarInstruccion("if", condicion, "", etiquetaFin);
        agregarInstruccion(actualizacion, "", "", "");
        agregarInstruccion("goto", etiquetaInicio, "", "");
        agregarInstruccion(etiquetaFin + ":", "", "", "");
    }

    public void imprimirTablaVariables() {
        System.out.println("Tabla de Variables:");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.println("|  Nombre  | Función            | Tamaño  | Descripción                              |");
        System.out.println("-----------------------------------------------------------------------------------------------------");
    
        for (Map.Entry<String, Variable> entry : tablaVariables.entrySet()) {
            String nombre = entry.getKey();
            Variable variable = entry.getValue();
    
            if (variable != null) {
                String funcion = variable.nombreFuncion != null ? variable.nombreFuncion : "Global";
                String tamaño = variable.tamañoTotal > 0 ? String.valueOf(variable.tamañoTotal) : "N/A";
                String descripcion = variable.descripcio != null ? variable.descripcio.toString() : "N/A";
    
                System.out.printf("| %-10s | %-18s | %-7s | %-25s |\n", nombre, funcion, tamaño, descripcion);
            } else {
                System.out.printf("| %-10s | %-18s | %-7s | %-25s |\n", nombre, "N/A", "N/A", "N/A");
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
            String nombre = entry.getKey();
            Procedimiento procedimiento = entry.getValue();
    
            if (procedimiento != null) {
                System.out.printf("| %-14s | %-20d | %-8s |\n", 
                                  nombre, procedimiento.numParametros, procedimiento.etiquetaInicio);
            } else {
                System.out.printf("| %-14s | %-20s | %-8s |\n", nombre, "N/A", "N/A");
            }
        }
    
        System.out.println("---------------------------------------------------");
    }
    
}

