package compiler.simbols;


public class SListaParametros extends SBase {
    private SListaParametros lista;     // Lista de los parámetros
    private SType tipo;                 // Tipo de los parámetros
    private String id;                  // ID de los parámetros

        /**
     * Constructor para cuando haya más parámetros.
     */
    public SListaParametros(SType tipo, String id, SListaParametros lista) {
        super("SListaParametros", null);
        this.tipo = tipo;
        this.id = id;
        this.lista = lista;
    }

    // Constructor para cuando no haya más parámetros
    public SListaParametros(SType tipo, String id) {
        super("SListaParametros", null);
        this.tipo = tipo;
        this.id = id;
        this.lista = null;
    }

    public SListaParametros() {
        super();
    }

    
    // Getters
    public SType getTipo() {
        return tipo;
    }


    public String getID() {
        return id;
    }

    public SListaParametros getParametro() {
        return lista;
    }

    @Override
    public String toString() {
        return "SListaParametros{tipos=" + tipo + ", nombres=" + id + "}";
    }
}
