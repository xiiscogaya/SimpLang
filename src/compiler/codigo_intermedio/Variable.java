package compiler.codigo_intermedio;
import compiler.taulasimbols.Descripcio;

public class  Variable {
    public String nombre;
    public String nombreFuncion;
    public int tamañoTotal;
    public Descripcio descripcio;

    public Variable(String nombre, String nombreFuncion, int tamañoTotal, Descripcio descripcio) {
        this.nombre = nombre;
        this.nombreFuncion = nombreFuncion;
        this.tamañoTotal = tamañoTotal;
        this.descripcio = descripcio;
    }        
}