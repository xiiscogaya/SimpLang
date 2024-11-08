package compiler.simbols;

public class SAsignacion extends SBase{
    private String id;
    private SValor valor;

    public SAsignacion(String id, SValor valor) {
        super("SAsignacion", valor);
        this.id = id;
        this.valor = valor;
    }

    public SAsignacion() {
        super();
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
