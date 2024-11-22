package compiler.simbols;

// Nodo de AST para la declaración de tupla
public class SDecTupla extends SBase {
    private String id;
    private SListaTupla listaParametros;  // Contiene tanto tipos como valores

    /**
     * Constructor para crear una instancia de SDecTupla con el id y los parámetros.
     * 
     * @param id Identificador de la tupla.
     * @param listaParametros Lista de tipos y valores de los parámetros.
     */
    public SDecTupla(String id, SListaTupla listaParametros) {
        super("SDecTupla", listaParametros);  // Inicializa con el nombre y la lista de parámetros
        this.id = id;
        this.listaParametros = listaParametros;
    }

    /**
     * Constructor vacío para crear una instancia de SDecTupla en caso de error.
     */
    public SDecTupla() {
        super();  // Llama al constructor vacío de SBase y marca como error
    }

    public String getId() {
        return id;
    }

    public SListaTupla getListaParametros() {
        return listaParametros;
    }

    @Override
    public String toString() {
        return isError ? "Error en declaración de tupla" : "Tupla " + id + " con parámetros " + listaParametros;
    }
}
