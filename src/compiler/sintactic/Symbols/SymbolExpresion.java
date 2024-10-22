package compiler.sintactic.Symbols;

public class SymbolExpresion extends SymbolBase{
    private int tipo;
    private Object valor;

    public SymbolExpresion(int tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }
}
