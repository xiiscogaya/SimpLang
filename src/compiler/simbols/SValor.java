package compiler.simbols;
import compiler.taulasimbols.TipoSubyacente;

public class SValor {

    private TipoSubyacente tipo;
    private Object valor;

    public SValor(TipoSubyacente tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
