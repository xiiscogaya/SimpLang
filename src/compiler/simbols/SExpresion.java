package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SExpresion extends SBase {
    private SExpresion e1;
    private SExpresion e2;
    private String operador;
    private SValor valor;
    private TipoSubyacente tipo;
    private String varGenerada;
    private SReferencia referencia;
    private int line;

    // Constructor único para cualquier tipo de expresión
    public SExpresion(int line, SExpresion e1, SExpresion e2, String operador) {
        super("SExpresion", null);
        this.line = line;
        this.e1 = e1;
        this.e2 = e2;
        this.operador = operador;
    }

    public SExpresion(int line, SValor valor) {
        super("SExpresion", null);
        this.line = line;
        this.valor = valor;
        this.tipo = valor.getTipo();
    }

    public SExpresion(int line, SReferencia referencia) {
        super("SReferencia", null);
        this.line = line;
        this.referencia = referencia;
    }

    public SExpresion() {
        super();
    }

    // Getters
    public int getLine() {
        return line;
    }

    public SExpresion getE1() {
        return e1;
    }

    public SExpresion getE2() {
        return e2;
    }

    public String getOperador() {
        return operador;
    }

    public SValor getValor() {
        return valor;
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public String getVarGenerada() {
        return varGenerada;
    }

    public SReferencia getReferencia() {
        return referencia;
    }

    public void setVarGenerada(String varGenerada) {
        this.varGenerada = varGenerada;
    }

    public void setTipo(TipoSubyacente tipo) {
        this.tipo = tipo;
    }
}
