package compiler.simbols;


public class SDecConstante extends SBase{
    private SType tipo;
    private String id;
    private SValor valor;

    public SDecConstante(SType tipo, String id, SValor valor) {
        super("SDecConstante", valor);
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
    }

    public SDecConstante() {
        super();
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

    @Override
    public String toString() {
        return isError ? "Error en declaración de constante" : "Constante " + id + ": " + tipo + " = " + valor;
    }
}

