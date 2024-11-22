package compiler.taulasimbols;

import java.util.ArrayList;
import java.util.List;

// Descripción para tuplas en la tabla de símbolos
public class DTupla extends Descripcio{
    private final List<TipoSubyacente> listatipos; // Estructura de tipos de la tupla
    private final List<Object> nombresParametros; // Valores de la tupla

    // Constructor que toma una lista de tipos y una lista de valores
    public DTupla() {
        super("tupla"); // Asigna el tipo "tupla" a esta descripción
        this.listatipos = new ArrayList<>(); // Asignación directa de la lista de tipos
        this.nombresParametros = new ArrayList<>(); // Asignación directa de la lista de valores
    }

    // Método para obtener la lista de tipos
    public List<TipoSubyacente> getTipos() {
        return listatipos;
    }

    // Método para obtener la lista de valores
    public List<Object> getValores() {
        return nombresParametros;
    }

    public void addParametro(TipoSubyacente tipo, String id) {
        this.listatipos.add(tipo);
        this.nombresParametros.add(id);
    }

    @Override
    public String toString() {
        return "DTupla{tipos=" + listatipos + ", valores=" + nombresParametros + "}";
    }
}
