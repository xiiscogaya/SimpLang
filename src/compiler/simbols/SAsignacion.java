package compiler.simbols;

public class SAsignacion {
    private String id;
    private SValor valor;

    public SAsignacion(String id, SValor valor) {
        this.id = id;
        this.valor = valor;
    }

    public SAsignacion() {
        
    }

    public String getId() {
        return id;
    }

    public SValor getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Variable " + id  + " = " + valor;
    }
}
