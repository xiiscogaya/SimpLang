package compiler.simbols;

public class SAsignacion extends SBase{
    private String id;
    private SExpresion expresion;
    private String operacion;
    private SLlamadaArray array;

    public SAsignacion(String id, String operacion, SExpresion expresion) {
        super("SAsignacion", null);
        this.id = id;
        this.operacion = operacion;
        this.expresion = expresion;
        this.array = null;
    }

    public SAsignacion(SLlamadaArray array, String operacion, SExpresion expresion) {
        super("SAsignacion", null);
        this.array = array;
        this.operacion = operacion;
        this.expresion = expresion;
    }

    public SAsignacion() {
        super();
    }

    public String getId() {
        return id;
    }

    public String getOperacion() {
        return operacion;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    public SLlamadaArray getArray() {
        return array;
    }

    @Override
    public String toString() {
        return "Variable " + id  + " = tipo de la expresión: " + expresion;
    }
}
