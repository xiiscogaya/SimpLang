package compiler.simbols;

public class SReturn extends SBase {
    private SExpresion expresion; // El expresion retornado, si lo hay

    public SReturn(SExpresion expresion) {
        super("SReturn", null);
        this.expresion = expresion;
    }

    public SReturn() {
        super("SReturn", null);
        this.expresion = null; // Return sin expresion
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    @Override
    public String toString() {
        return expresion != null ? "return " + expresion : "return;";
    }
}
