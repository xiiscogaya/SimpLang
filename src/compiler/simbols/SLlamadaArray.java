package compiler.simbols;

public class SLlamadaArray extends SBase {
    private String arrayName;
    private SListaDimensiones dimensiones;
    private int line;

    public SLlamadaArray(int line, String arrayName, SListaDimensiones dimensiones) {
        super("SLlamadaArray", null);
        this.line = line;
        this.arrayName = arrayName;
        this.dimensiones = dimensiones;
    }

    public SLlamadaArray() {
        super();
    }

    public int getLine() {
        return line;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public SListaDimensiones getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(SListaDimensiones dimensiones) {
        this.dimensiones = dimensiones;
    }
    
}
