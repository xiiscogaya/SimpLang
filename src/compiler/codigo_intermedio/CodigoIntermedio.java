package compiler.codigo_intermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodigoIntermedio {
    public static class Instruccion {
        public String operador;
        public String operando1;
        public String operando2;
        public String destino;

        public Instruccion(String operador, String operando1, String operando2, String destino) {
            this.operador = operador;
            this.operando1 = operando1;
            this.operando2 = operando2;
            this.destino = destino;
        }

        @Override
        public String toString() {
            if (operando2 == null || operando2.isEmpty()) {
                return String.format("%s %s -> %s", operador, operando1, destino);
            }
            return String.format("%s %s, %s -> %s", operador, operando1, operando2, destino);
        }
    }

    private Map<String, String> tablaVariables;
    private int contadorVariablesTemporales;
    private Map<String, Procedimiento> tablaProcedimientos;
    private List<Instruccion> instrucciones;
    private int contadorEtiquetas;

    public CodigoIntermedio() {
        this.tablaVariables = new HashMap<>();
        this.contadorVariablesTemporales = 0;
        this.tablaProcedimientos = new HashMap<>();
        this.instrucciones = new ArrayList<>();
        this.contadorEtiquetas = 0;
    }

    public String nuevaVariableTemporal() {
        String temporal = "t" + contadorVariablesTemporales++;
        tablaVariables.put(temporal, "temporal");
        return temporal;
    }

    public String nuevaEtiqueta() {
        return "L" + contadorEtiquetas++;
    }

    public void agregarInstruccion(String operador, String operando1, String operando2, String destino) {
        instrucciones.add(new Instruccion(operador, operando1, operando2, destino));
    }

    public void registrarVariable(String nombre, String tipo) {
        tablaVariables.put(nombre, tipo);
    }

    public void registrarProcedimiento(String nombre, int numParametros, String etiquetaInicio) {
        tablaProcedimientos.put(nombre, new Procedimiento(nombre, numParametros, etiquetaInicio));
    }

    public void imprimirCodigo() {
        for (Instruccion instr : instrucciones) {
            System.out.println(instr);
        }
    }

    public static class Procedimiento {
        public String nombre;
        public int numParametros;
        public String etiquetaInicio;

        public Procedimiento(String nombre, int numParametros, String etiquetaInicio) {
            this.nombre = nombre;
            this.numParametros = numParametros;
            this.etiquetaInicio = etiquetaInicio;
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

    // Generación de código para acceso a arrays
    public String generarAccesoArray(String nombreArray, String indice) {
        String temporal = nuevaVariableTemporal();
        agregarInstruccion("load", nombreArray + "[" + indice + "]", "", temporal);
        return temporal;
    }

    public void generarAsignacionArray(String nombreArray, String indice, String valor) {
        agregarInstruccion("store", valor, "", nombreArray + "[" + indice + "]");
    }
}

