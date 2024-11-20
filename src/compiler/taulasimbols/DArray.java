package compiler.taulasimbols;

import java.util.List;

public class DArray extends Descripcio {
    private TipoSubyacente tipo;
    private List<Integer> dimensiones; // Almacena el tamaño de cada dimensión

    public DArray(TipoSubyacente tipo, List<Integer> dimensionesArray) {
        super("array");
        this.tipo = tipo;
        this.dimensiones = dimensionesArray;
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public List<Integer> getDimensiones() {
        return dimensiones;
    }

    @Override
    public String toString() {
        return "DArray{" + "tipo=" + tipo + ", dimensiones=" + dimensiones + "}";
    }
}
