package compiler.simbols;

public class SRepeatUntil extends SBase{
    public int line;
    private SBloque bloque;
    private SExpresion expresion;

    public SRepeatUntil(int line, SBloque bloque, SExpresion expresion) {
        super("SRepeatUntil", null);
        this.line = line;
        this.bloque = bloque;
        this.expresion = expresion;
    }

    public SRepeatUntil() {
        super();
    }

    public SBloque getBloque() {
        return bloque;
    }
    public void setBloque(SBloque bloque) {
        this.bloque = bloque;
    }
    public SExpresion getExpresion() {
        return expresion;
    }
    public void setExpresion(SExpresion expresion) {
        this.expresion = expresion;
    }
}
