package compiler.simbols;

public class SListaDimensionesRef extends SBase {
    private SExpresion expresion;
    private SListaDimensionesRef lista;
    private int line;

    public SListaDimensionesRef(int line, SExpresion expresion, SListaDimensionesRef lista) {
        super("SListaDimensionesRef", null);
        this.line = line;
        this.expresion = expresion;
        this.lista = lista;
    }

    public SListaDimensionesRef() {
        super();
    }

    public int getLine() {
        return line;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(SExpresion expresion) {
        this.expresion = expresion;
    }

    public SListaDimensionesRef getLista() {
        return lista;
    }

    public void setLista(SListaDimensionesRef lista) {
        this.lista = lista;
    }

    
}
