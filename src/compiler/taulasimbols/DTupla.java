package compiler.taulasimbols;

import java.util.List;

// Descripción para tuplas en la tabla de símbolos
public class DTupla extends Descripcio {
    private final List<TipoSubyacente> tipos; // Estructura de tipos de la tupla
    private final List<Object> valores; // Valores de la tupla

    // Constructor que toma una lista de tipos y una lista de valores
    public DTupla(List<TipoSubyacente> tipos, List<Object> valores) {
        super("tupla"); // Asigna el tipo "tupla" a esta descripción
        this.tipos = tipos; // Asignación directa de la lista de tipos
        this.valores = valores; // Asignación directa de la lista de valores
    }

    // Método para obtener la lista de tipos
    public List<TipoSubyacente> getTipos() {
        return tipos;
    }

    // Método para obtener la lista de valores
    public List<Object> getValores() {
        return valores;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DTupla{");
        for (int i = 0; i < valores.size(); i++) {
            sb.append("(").append(tipos.get(i)).append("): ").append(valores.get(i));
            if (i < valores.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
