package compiler.codigo_intermedio;
import compiler.taulasimbols.Descripcio;

public class  Variable {
    public String nombreFuncion;
    public int tamañoTotal;
    public Descripcio descripcio;

    public Variable(String nombreFuncion, int tamañoTotal, Descripcio descripcio) {
        this.nombreFuncion = nombreFuncion;
        this.tamañoTotal = tamañoTotal;
        this.descripcio = descripcio;
    }        
}