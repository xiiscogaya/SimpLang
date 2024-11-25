package compiler.simbols;

public class SLlamadaTupla extends SBase{
    private String tuplaName;
    private String fieldName;

    public SLlamadaTupla(String tuplaName, String fieldName) {
        super("SLlamadaTupla", null);
        this.tuplaName = tuplaName;
        this.fieldName = fieldName;
    }

    public SLlamadaTupla() {
        super();
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
