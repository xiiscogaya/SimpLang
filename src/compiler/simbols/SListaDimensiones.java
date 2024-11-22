package compiler.simbols;

public class SListaDimensiones extends SBase{
    private SValor size;
    private SListaDimensiones lista;
    

    public SListaDimensiones(SValor size, SListaDimensiones lista) {
        super("SListaDimensiones", null);
        this.size = size;
        this.lista = lista;
    }

    public SListaDimensiones() {
        super();
    }

    public SValor getSize() {
        return size;
    }

    public SListaDimensiones getListaDimensiones() {
        return lista;
    }
}
