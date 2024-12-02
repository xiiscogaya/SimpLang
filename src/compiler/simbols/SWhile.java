package compiler.simbols;

public class SWhile extends SBase{
    private SExpresion expresion;
    private SBloque bloque;
    private int line;

    
    public SWhile(int line, SExpresion expresion, SBloque bloque) {
        super("SWhile", null);
        this.line = line;
        this.expresion = expresion;
        this.bloque = bloque;
    }

    public int getLine() {
        return line;
    }

    public SExpresion getExpresion() {
        return expresion;
    }
    public void setExpresion(SExpresion expresion) {
        this.expresion = expresion;
    }
    public SBloque getBloque() {
        return bloque;
    }
    public void setBloque(SBloque bloque) {
        this.bloque = bloque;
    }
}
