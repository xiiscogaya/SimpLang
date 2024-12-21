package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SLlamadaTupla extends SBase{
    private String tuplaName;
    private String fieldName;
    private int line;
    public TipoSubyacente tipoTupla;
    public String varGenerada;
    public String idUnico;

    public SLlamadaTupla(int line, String tuplaName, String fieldName) {
        super("SLlamadaTupla", null);
        this.line = line;
        this.tuplaName = tuplaName;
        this.fieldName = fieldName;
    }

    public SLlamadaTupla() {
        super();
    }

    public int getLine() {
        return line;
    }

    public String getTuplaName() {
        return tuplaName;
    }

    public void setTuplaName(String tuplaName) {
        this.tuplaName = tuplaName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
}
