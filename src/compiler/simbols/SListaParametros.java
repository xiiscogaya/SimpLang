package compiler.simbols;

import java.util.ArrayList;
import java.util.List;
import compiler.taulasimbols.TipoSubyacente;

public class SListaParametros extends SBase {
    private List<TipoSubyacente> tipos; // Tipos de los parámetros
    private List<String> nombres;     // Nombres de los parámetros

        /**
     * Constructor vacío para crear una instancia vacía de SListaParametros.
     */
    public SListaParametros() {
        super("SListaParametros", null);
        this.tipos = new ArrayList<>(); // Inicializar listas
        this.nombres = new ArrayList<>(); // Inicializar listas
    }

    /**
     * Método para añadir un tipo, un valor y un nombre a la lista.
     *
     * @param tipo Tipo subyacente a añadir.
     * @param valor Valor a añadir.
     * @param nombre Nombre del parámetro.
     */
    public void addParametro(TipoSubyacente tipo, String nombre) {
        this.tipos.add(tipo);
        this.nombres.add(nombre);
    }

    // Getters
    public List<TipoSubyacente> getTipos() {
        return tipos;
    }


    public List<String> getNombres() {
        return nombres;
    }

    @Override
    public String toString() {
        return "SListaParametros{tipos=" + tipos + ", nombres=" + nombres + "}";
    }
}
