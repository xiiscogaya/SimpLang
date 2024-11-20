package compiler.simbols;

public class SAsignacion extends SBase{
    private String id;
    private SExpresion expresion;

    public SAsignacion(String id, SExpresion expresion) {
        super("SAsignacion", null);
        this.id = id;
        this.expresion = expresion;
    }

    public SAsignacion() {
        super();
    }

    public String getId() {
        return id;
    }

    public SExpresion getTipoExpresion() {
        return expresion;
    }

    @Override
    public String toString() {
        return "Variable " + id  + " = tipo de la expresión: " + expresion;
    }
}
