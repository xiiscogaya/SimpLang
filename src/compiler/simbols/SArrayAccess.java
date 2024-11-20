package compiler.simbols;


/**
 * Clase que representa el acceso a un elemento de un array en el AST.
 */
public class SArrayAccess extends SBase {
    private String id;                // Identificador del array
    private SDimension indices;       // Lista de índices para el acceso

    /**
     * Constructor para crear un acceso a un array con un identificador y una lista de índices.
     *
     * @param id Identificador del array.
     * @param indices Lista de índices para acceder al elemento del array.
     */
    public SArrayAccess(String id, SDimension indices) {
        super("SArrayAccess", indices);
        this.id = id;
        this.indices = indices;
    }

    /**
     * Constructor vacío para un acceso inválido o con error.
     */
    public SArrayAccess() {
        super();
    }

    /**
     * Obtiene el identificador del array.
     *
     * @return El identificador del array.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la lista de índices usados en el acceso al array.
     *
     * @return La lista de índices.
     */
    public SDimension getIndices() {
        return indices;
    }

    @Override
    public String toString() {
        return isError() ? "Acceso a array inválido" : "Acceso a array " + id + " con índices " + indices;
    }
}
