package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SReturn extends SBase {
    private SValor valor; // El valor retornado, si lo hay

    public SReturn(SValor valor) {
        super("SReturn", valor);
        this.valor = valor;
    }

    public SReturn() {
        super("SReturn", null);
        this.valor = null; // Return sin valor
    }

    public SValor getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor != null ? "return " + valor : "return;";
    }
}
