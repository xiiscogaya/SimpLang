package compiler.simbols;

public class SListaDimensiones extends SBase{
    private String size;
    private SListaDimensiones lista;
    private int line;
    

    public SListaDimensiones(int line, String size, SListaDimensiones lista) {
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

    public String getSize() {
        return size;
    }

    public SListaDimensiones getListaDimensiones() {
        return lista;
    }
}
