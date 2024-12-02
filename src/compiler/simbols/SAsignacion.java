package compiler.simbols;

public class SAsignacion extends SBase{
    private String id;
    private SExpresion expresion;
    private String operacion;
    private SLlamadaArray array;
    private int line;

    public SAsignacion(int line, String id, String operacion, SExpresion expresion) {
        super("SAsignacion", null);
        this.line = line;
        this.id = id;
        this.operacion = operacion;
        this.expresion = expresion;
        this.array = null;
    }

    public SAsignacion(int line, SLlamadaArray array, String operacion, SExpresion expresion) {
        super("SAsignacion", null);
        this.line = line;
        this.array = array;
        this.operacion = operacion;
        this.expresion = expresion;
    }

    public SAsignacion() {
        super();
    }

    public int getLine() {
        return line;
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
