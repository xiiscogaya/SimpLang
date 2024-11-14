package compiler.simbols;

import java.util.List;
import compiler.taulasimbols.TipoSubyacente;

public class SDecArray extends SBase {
    private TipoSubyacente tipo;
    private String id;
    private List<Integer> dimensiones;

    public SDecArray(TipoSubyacente tipo, String id, List<Integer> dimensiones) {
        super("SDecArray", null);
        this.tipo = tipo;
        this.id = id;
        this.dimensiones = dimensiones;
    }

    public SDecArray() {
        super();
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getDimensiones() {
        return dimensiones;
    }

    @Override
    public String toString() {
        return "Array " + id + " de tipo " + tipo + " con dimensiones " + dimensiones;
    }
}
