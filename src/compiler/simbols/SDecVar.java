package compiler.simbols;

public class SDecVar extends SBase{
    private SType tipo;
    private String id;
    private SExpresion expresion;

    public SDecVar(SType tipo, String id, SExpresion expresion) {
        super("SDecVar", null);
        this.tipo = tipo;
        this.id = id;
        this.expresion = expresion;
    }

    public SDecVar() {
        super();
    }

    public SType getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SExpresion getTipoExpresion() {
        return expresion;
    }
    
    @Override
    public String toString() {
        return isError ? "Error en declaración de constante" : "Variable " + id + ": " + tipo + " = tipo de la expresión: " + expresion;
    }
}
