package compiler.simbols;

public class SDecVar extends SBase{
    private String tipo;
    private String id;
    private int line;

    public SDecVar(int line, String tipo, String id) {
        super("SDecVar", null);
        this.line = line;
        this.tipo = tipo;
        this.id = id;
    }

    public SDecVar() {
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
}
