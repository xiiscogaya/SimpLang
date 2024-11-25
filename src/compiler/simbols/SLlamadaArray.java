package compiler.simbols;

public class SLlamadaArray extends SBase {
    private String arrayName;
    private SListaDimensiones dimensiones;

    public SLlamadaArray(String arrayName, SListaDimensiones dimensiones) {
        super("SLlamadaArray", null);
        this.arrayName = arrayName;
        this.dimensiones = dimensiones;
    }

    public SLlamadaArray() {
        super();
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
