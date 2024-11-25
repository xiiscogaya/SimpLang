package compiler.simbols;

public class SListaExpresiones extends SBase{
    private SListaExpresiones lista;
    private SExpresion expresion;

    public SListaExpresiones(SListaExpresiones lista, SExpresion expresion) {
        super("SListaExpresiones", null);
        this.lista = lista;
        this.expresion = expresion;
    }

    public SListaExpresiones(SExpresion expresion) {
        super("SListaExpresiones", null);
        this.expresion = expresion;
    }

    public SListaExpresiones() {
        super();
    }

    public SListaExpresiones getLista() {
        return lista;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

}
