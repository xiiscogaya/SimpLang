package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SReferencia extends SBase {
    private SBase llamada;
    public String nameid;

    public String idUnico;
    public TipoSubyacente tipoSubyacente;
    public String varGenerada;

    public SReferencia(SBase llamada) {
        super("SReferencia", null);
        this.llamada = llamada;
    }

    public SReferencia(String nameid) {
        super("SReferencia", null);
        this.nameid = nameid;
    }

    public SBase getLlamada() {
        return llamada;
    } 

    public void setIdUnico(String idUnico) {
        this.idUnico = idUnico;
    }

    public String getIdUnico() {
        return idUnico;
    }
}
