package compiler.taulasimbols;

import java.util.ArrayList;
import java.util.List;

public class DArray extends Descripcio {
    private TipoSubyacente tipo;
    private List<Object> dimensiones; // Almacena el tamaño de cada dimensión

    public DArray(TipoSubyacente tipo) {
        super("array");
        this.tipo = tipo;
        this.dimensiones = new ArrayList<>();
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public List<Object> getDimensiones() {
        return dimensiones;
    }

    public void addDimension(Object dimension) {
        this.dimensiones.add(dimension);
    }

    @Override
    public String toString() {
        return "DArray{" + "tipo=" + tipo + ", dimensiones=" + dimensiones + " idUnico=" + idUnico + "}";
    }
}
