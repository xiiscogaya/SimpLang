package compiler.simbols;

import java.util.ArrayList;
import java.util.List;

public class SValorLista {
    private List<SValor> valores;

    // Constructor inicializa la lista vacía
    public SValorLista() {
        this.valores = new ArrayList<>();
    }

    // Constructor para inicializar la lista con valores dados
    public SValorLista(List<SValor> valores) {
        this.valores = valores;
    }

    // Método para añadir un valor a la lista
    public void addValor(SValor valor) {
        this.valores.add(valor);
    }

    // Método para obtener la lista de valores
    public List<SValor> getValores() {
        return valores;
    }

    @Override
    public String toString() {
        return valores.toString();
    }
}
