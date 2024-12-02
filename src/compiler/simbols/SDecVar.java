package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SDecVar extends SBase{
    private String tipo;
    private String id;
    private SExpresion expresion;
    private TipoSubyacente tipoBasico;
    private int line;

    public SDecVar(int line, String tipo, String id, SExpresion expresion) {
        super("SDecVar", null);
        this.line = line;
        this.tipo = tipo;
        this.id = id;
        this.expresion = expresion;
    }

    public SDecVar() {
        super();
    }

    public int getLine() {
        return line;
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    public TipoSubyacente getTipoBasico() {
        return tipoBasico;
    }

    public void setTipoBasico(TipoSubyacente tipoBasico) {
        this.tipoBasico = tipoBasico;
    }
    
    @Override
    public String toString() {
        return isError ? "Error en declaración de constante" : "Variable " + id + ": " + tipo + " = tipo de la expresión: " + expresion;
    }
}
