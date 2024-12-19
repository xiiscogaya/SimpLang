package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SReferencia extends SBase {
    private SBase llamada;
    private String id;
    public String tipoLlamada;
    public TipoSubyacente tipoSubyacente;
    public String varGenerada;

    public SReferencia(SBase llamada) {
        super("SReferencia", null);
        this.llamada = llamada;
    }

    public SReferencia(String id) {
        super("SReferencia", null);
        this.id = id;
    }

    public SBase getLlamada() {
        return llamada;
    } 

    public void setID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
