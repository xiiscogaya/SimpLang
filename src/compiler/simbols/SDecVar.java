package compiler.simbols;

public class SDecVar extends SBase{
    private SType tipo;
    private String id;
    private SValor valor;

    public SDecVar(SType tipo, String id, SValor valor) {
        super("SDecVar", valor);
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
    }

    public SDecVar() {
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
        return isError ? "Error en declaración de constante" : "Variable " + id + ": " + tipo + " = " + valor;
    }
}
