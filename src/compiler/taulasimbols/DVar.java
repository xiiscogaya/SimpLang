package compiler.taulasimbols;

public class DVar extends Descripcio{
    public TipoSubyacente tipoSubyacente; // Tipo de la constante

    public DVar(TipoSubyacente tipoSubyacente) {
        super("variable");
        this.tipoSubyacente = tipoSubyacente;
    }

    @Override
    public String toString() {
        return String.format("DVar{tipoSubyacente=%s, idUnico=%s}", tipoSubyacente, idUnico);
    }

    public DVar crearCopiaLigera() {
        return new DVar(this.tipoSubyacente);
    }

    public TipoSubyacente getTipoSubyacente() {
        return tipoSubyacente;
    }
}
