package compiler.simbols;

public class SListaDimensiones extends SBase{
    private SValor size;
    private SListaDimensiones lista;
    private int line;
    

    public SListaDimensiones(int line, SValor size, SListaDimensiones lista) {
        super("SListaDimensiones", null);
        this.line = line;
        this.size = size;
        this.lista = lista;
    }

    public SListaDimensiones() {
        super();
    }

    public int getLine() {
        return line;
    }

    public SValor getSize() {
        return size;
    }

    public SListaDimensiones getListaDimensiones() {
        return lista;
    }
}
