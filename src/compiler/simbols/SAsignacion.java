package compiler.simbols;

public class SAsignacion extends SBase{
    private SReferencia referencia;
    private SExpresion expresion;
    private int line;

    public SAsignacion(int line, SReferencia referencia, SExpresion expresion) {
        super("SAsignacion", null);
        this.line = line;
        this.referencia = referencia;
        this.expresion = expresion;
    }

    public SAsignacion() {
        super();
    }

    public int getLine() {
        return line;
    }

    public SReferencia getReferencia() {
        return referencia;
    }

    public SExpresion getExpresion() {
        return expresion;
    }
}
