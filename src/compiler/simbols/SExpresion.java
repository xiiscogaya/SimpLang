package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SExpresion extends SBase {
    private SExpresion e1;
    private SExpresion e2;
    private String operador;
    private SValor valor;
    private SLlamadaFuncion llamadaFuncion;
    private TipoSubyacente tipo;
    private String id;
    private SLlamadaArray arrayAcces;
    private SLlamadaTupla tuplaAcces;

    // Constructor único para cualquier tipo de expresión
    public SExpresion(SExpresion e1, SExpresion e2, String operador) {
        super("SExpresion", null);
        this.e1 = e1;
        this.e2 = e2;
        this.operador = operador;
        this.valor = null;
        this.llamadaFuncion = null;
        this.tipo = tipo;
    }

    public SExpresion(SLlamadaFuncion llamadaFuncion) {
        super("SExpresion", null);
        this.llamadaFuncion = llamadaFuncion;
        this.e1 = null;
        this.e2 = null;
        this.operador = null;
        this.valor = null;
        this.tipo = llamadaFuncion.getTipoRetorno();
        this.id = null;
    }

    public SExpresion(SValor valor) {
        super("SExpresion", null);
        this.llamadaFuncion = null;
        this.e1 = null;
        this.e2 = null;
        this.operador = null;
        this.valor = valor;
        this.tipo = valor.getTipo();
        this.id = null;
    }

    public SExpresion(String id) {
        super("SExpresion", null);
        this.llamadaFuncion = null;
        this.e1 = null;
        this.e2 = null;
        this.operador = null;
        this.valor = null;
        this.id = id;
        this.tipo = null;
    }

    public SExpresion(SLlamadaArray arrayAcces) {
        super("SLlamadaArray", null);
        this.arrayAcces = arrayAcces;
    }

    public SExpresion(SLlamadaTupla tuplaAcces) {
        super("SLlamadaArray", null);
        this.tuplaAcces = tuplaAcces;
    }

    public SExpresion() {
        super();
    }

    // Getters
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

    public String getID () {
        return id;
    }

    public SLlamadaArray getArray() {
        return arrayAcces;
    }

    public SLlamadaTupla getTupla() {
        return tuplaAcces;
    }

    public void setTipo(TipoSubyacente tipo) {
        this.tipo = tipo;
    }
    public SLlamadaFuncion getLlamadaFuncion() {
        return llamadaFuncion;
    }
}
