package compiler.simbols;


public class SDecArray extends SBase {
    private SType tipo;
    private String id;
    private SListaDimensiones dimensiones;

    public SDecArray(SType tipo, String id, SListaDimensiones dimensiones) {
        super("SDecArray", null);
        this.tipo = tipo;
        this.id = id;
        this.dimensiones = dimensiones;
    }

    public SDecArray() {
        super();
    }

    public SType getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SListaDimensiones getDimensiones() {
        return dimensiones;
    }

    @Override
    public String toString() {
        return "Array " + id + " de tipo " + tipo + " con dimensiones " + dimensiones;
    }
}
