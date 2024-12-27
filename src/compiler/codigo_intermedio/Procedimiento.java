package compiler.codigo_intermedio;

import java.util.List;

import compiler.taulasimbols.Descripcio;

public class Procedimiento {
    public String nombre;
    public int numParametros;
    public List<String> listaIDUnicos;
    public String etiquetaInicio;
    public Boolean yaAnalizado;
    public int ocupacionLocales;
    public Descripcio descripcio;

    public Procedimiento(String nombre, int numParametros, List<String> listaIDUnicos, String etiquetaInicio, Boolean yaAnalizado,int ocupacionLocales, Descripcio descripcio) {
        this.nombre = nombre;
        this.numParametros = numParametros;
        this.listaIDUnicos = listaIDUnicos;
        this.etiquetaInicio = etiquetaInicio;
        this.yaAnalizado = yaAnalizado;
        this.ocupacionLocales = ocupacionLocales;
        this.descripcio = descripcio;
    }
}