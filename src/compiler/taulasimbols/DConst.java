package compiler.taulasimbols;

// Descripción para constantes
public class DConst extends Descripcio {
    public Object valor; // Valor de la constante
    public TipoSubyacente tipoSubyacente; // Tipo de la constante

    public DConst(Object valor, TipoSubyacente tipoSubyacente) {
        super("const");
        this.valor = valor;
        this.tipoSubyacente = tipoSubyacente;
    }

    @Override
    public String toString() {
        return String.format("DConst{valor=%s, tipoSubyacente=%s}", valor, tipoSubyacente);
    }

    public Object getValor() {
        return valor;
    }

    public TipoSubyacente getTipoSubyacente() {
        return tipoSubyacente;
    }
}