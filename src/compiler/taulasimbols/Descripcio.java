package compiler.taulasimbols;

public class Descripcio {
    private static int contador = 0;
    public String idUnico;
    public String tipo;

    public Descripcio(String tipo) {
        this.tipo = tipo;
        this.idUnico = "ID" + (contador++);
    }
    

}
