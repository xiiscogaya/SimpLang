package compiler.codigo_intermedio;

public class Procedimiento {
    public String nombre;
    public int numParametros;
    public String etiquetaInicio;
    public Boolean yaAnalizado;
    public int ocupacionLocales;

    public Procedimiento(String nombre, int numParametros, String etiquetaInicio, Boolean yaAnalizado,int ocupacionLocales) {
        this.nombre = nombre;
        this.numParametros = numParametros;
        this.etiquetaInicio = etiquetaInicio;
        this.yaAnalizado = yaAnalizado;
        this.ocupacionLocales = ocupacionLocales;
    }
}