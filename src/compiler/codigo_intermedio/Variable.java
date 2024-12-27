package compiler.codigo_intermedio;
import compiler.taulasimbols.Descripcio;
import compiler.taulasimbols.TipoSubyacente;

public class  Variable {
    public String nombre;
    public String nombreFuncion;
    public int tamañoTotal;
    public Descripcio descripcio;
    public TipoSubyacente tipo;

    public Variable(String nombre, String nombreFuncion, int tamañoTotal, TipoSubyacente tipo, Descripcio descripcio) {
        this.nombre = nombre;
        this.nombreFuncion = nombreFuncion;
        this.tamañoTotal = tamañoTotal;
        this.tipo = tipo;
        this.descripcio = descripcio;
    }        
}