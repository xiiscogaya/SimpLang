package compiler.simbols;

// Nodo de AST para la declaración de tupla
public class SDecTupla {
    private String id;
    private STipoLista tipos;

    public SDecTupla(String id, STipoLista tipos) {
        this.id = id;
        this.tipos = tipos;
    }

    public SDecTupla() {
        
    }

    public String getId() {
        return id;
    }

    public STipoLista getTipos() {
        return tipos;
    }

    @Override
    public String toString() {
        return "Tupla " + id + " con tipos " + tipos;
    }
}

