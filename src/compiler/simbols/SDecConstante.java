package compiler.simbols;


public class SDecConstante extends SBase{
    private String tipo;
    private String id;
    private SValor valor;
    private int line;

    public SDecConstante(int line, String tipo, String id, SValor valor) {
        super("SDecConstante", valor);
        this.line = line;
        this.tipo = tipo;
        this.id = id;
        this.valor = valor;
    }

    public SDecConstante() {
        super();
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SValor getValor() {
        return valor;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return isError ? "Error en declaración de constante" : "Constante " + id + ": " + tipo + " = " + valor;
    }
}

