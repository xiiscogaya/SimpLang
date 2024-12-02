package compiler.simbols;

public class SElif extends SBase{
    private SElif lista;
    private SExpresion expresion;
    private SBloque bloque;
    private int line;

    public SElif(int line, SElif lista, SExpresion expresion, SBloque bloque) {
        super("SElif", null);
        this.line = line;
        this.lista = lista;
        this.expresion = expresion;
        this.bloque = bloque;
    }

    public SElif(int line, SExpresion expresion, SBloque bloque) {
        super("SElif", null);
        this.line = line;
        this.expresion = expresion;
        this.bloque = bloque;
    }


    public SElif() {
        super();
    }

    public int getLine() {
        return line;
    }
    
    public SElif getLista() {
        return lista;
    }
    public void setLista(SElif lista) {
        this.lista = lista;
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
