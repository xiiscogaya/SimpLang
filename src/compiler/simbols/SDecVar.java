package compiler.simbols;

public class SDecVar {
    private SType tipo;
    private String id;
    private SValor valor;
    private boolean hasError;

    public SDecVar(SType tipo, String id, SValor valor) {
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
        this.hasError = false;
    }

    public SDecVar() {
        this.hasError = true;
    }

    public SType getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SValor getValor() {
        return valor;
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public String toString() {
        return hasError ? "Error en declaración de constante" : "Variable " + id + ": " + tipo + " = " + valor;
    }
}
