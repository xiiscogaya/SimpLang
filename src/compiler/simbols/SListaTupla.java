package compiler.simbols;

public class SListaTupla {
    private SListaTupla lista;
    private SType tipo;
    private SExpresion expresion;

    
    public SListaTupla(SListaTupla lista, SType tipo, SExpresion expresion) {
        this.lista = lista;
        this.tipo = tipo;
        this.expresion = expresion;
    }

    public SListaTupla(SType tipo, SExpresion expresion) {
        this.tipo = tipo;
        this.expresion = expresion;
    }


    public SListaTupla getLista() {
        return lista;
    }
    public void setLista(SListaTupla lista) {
        this.lista = lista;
    }
    public SType getTipo() {
        return tipo;
    }
    public void setTipo(SType tipo) {
        this.tipo = tipo;
    }
    public SExpresion getExpresion() {
        return expresion;
    }
    public void setExpresion(SExpresion expresion) {
        this.expresion = expresion;
    }


}
