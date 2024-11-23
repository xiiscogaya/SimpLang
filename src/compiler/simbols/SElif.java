package compiler.simbols;

public class SElif extends SBase{
    private SElif lista;
    private SExpresion expresion;
    private SBloque bloque;

    public SElif(SElif lista, SExpresion expresion, SBloque bloque) {
        super("SElif", null);
        this.lista = lista;
        this.expresion = expresion;
        this.bloque = bloque;
    }

    public SElif(SExpresion expresion, SBloque bloque) {
        super("SElif", null);
        this.expresion = expresion;
        this.bloque = bloque;
    }


    public SElif() {
        super();
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
