package compiler.simbols;

public class SDecArray extends SBase {
    private String tipo;
    private String id;
    private SListaDimensiones dimensiones;
    private int line;

    public SDecArray(int line, String tipo, String id, SListaDimensiones dimensiones) {
        super("SDecArray", null);
        this.line = line;
        this.tipo = tipo;
        this.id = id;
        this.dimensiones = dimensiones;
    }

    public SDecArray() {
        super();
    }

    public int getLine() {
        return line;
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SListaDimensiones getDimensiones() {
        return dimensiones;
    }

    @Override
    public String toString() {
        return "Array " + id + " de tipo " + tipo + " con dimensiones " + dimensiones;
    }
}
