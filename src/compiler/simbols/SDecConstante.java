package compiler.simbols;


public class SDecConstante {
    private SType tipo;
    private String id;
    private SValor valor;
    private boolean hasError;

    public SDecConstante(SType tipo, String id, SValor valor) {
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
        this.hasError = false;
    }

    public SDecConstante() {
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
        return hasError ? "Error en declaración de constante" : "Constante " + id + ": " + tipo + " = " + valor;
    }
}

