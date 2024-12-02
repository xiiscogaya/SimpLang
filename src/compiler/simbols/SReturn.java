package compiler.simbols;

public class SReturn extends SBase {
    private SExpresion expresion; // El expresion retornado, si lo hay
    private int line;

    public SReturn(int line, SExpresion expresion) {
        super("SReturn", null);
        this.line = line;
        this.expresion = expresion;
    }

    public SReturn(int line) {
        super("SReturn", null);
        this.line = line;
        this.expresion = null; // Return sin expresion
    }

    public int getLine() {
        return line;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    @Override
    public String toString() {
        return expresion != null ? "return " + expresion : "return;";
    }
}
