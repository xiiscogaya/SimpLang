package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;
import java.util.ArrayList;
import java.util.List;

public class STipoLista {
    private List<TipoSubyacente> tipos; // Lista de tipos subyacentes

    // Constructor inicializa la lista de tipos vacía
    public STipoLista() {
        this.tipos = new ArrayList<>();
    }

    // Constructor para inicializar la lista con tipos dados
    public STipoLista(List<TipoSubyacente> tipos) {
        this.tipos = tipos;
    }

    // Método para añadir un tipo subyacente a la lista
    public void addTipo(TipoSubyacente tipo) {
        this.tipos.add(tipo);
    }

    // Método para obtener la lista de tipos subyacentes
    public List<TipoSubyacente> getTipos() {
        return tipos;
    }

    @Override
    public String toString() {
        return tipos.toString();
    }
}
