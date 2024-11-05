package compiler.taulasimbols;

public class DVar extends Descripcio{
    public Object valor; // Valor de la constante
    public TipoSubyacente tipoSubyacente; // Tipo de la constante

    public DVar(Object valor, TipoSubyacente tipoSubyacente) {
        super("variable");
        this.valor = valor;
        this.tipoSubyacente = tipoSubyacente;
    }

    @Override
    public String toString() {
        return String.format("DVar{valor=%s, tipoSubyacente=%s}", valor, tipoSubyacente);
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object nuevoValor) {
        this.valor = nuevoValor;
    }
 
    public TipoSubyacente getTipoSubyacente() {
        return tipoSubyacente;
    }
}
