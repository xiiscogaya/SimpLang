package compiler.simbols;

public class SListaTupla extends SBase{
    private SListaTupla lista;
    private SDecVar var;

    
    public SListaTupla(SListaTupla lista, SDecVar var) {
        super("SListaTupla", null);
        this.lista = lista;
        this.var = var;
    }

    public SListaTupla(SDecVar var) {
        super("SListaTupla", null);
        this.var = var;
    }


    public SListaTupla getLista() {
        return lista;
    }
    public void setLista(SListaTupla lista) {
        this.lista = lista;
    }
    public SDecVar getVar() {
        return var;
    }
 

}
