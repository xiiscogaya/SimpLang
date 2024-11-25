package compiler.simbols;

public class SPrint extends SBase{
    private SListaExpresiones lista;

    public SPrint(SListaExpresiones lista) {
        super("SPrint", null);
        this.lista = lista;
    }

    public SListaExpresiones getLista() {
        return lista;
    }
}
