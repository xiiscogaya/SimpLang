package compiler.simbols;

public class SListaTupla extends SBase{
    private SListaTupla lista;
    private String id;

    
    public SListaTupla(SListaTupla lista, String id) {
        super("SListaTupla", null);
        this.lista = lista;
        this.id = id;
    }

    public SListaTupla(String id) {
        super("SListaTupla", null);
        this.id = id;
    }

    public SListaTupla() {
        super();
    }
    
    public SListaTupla getLista() {
        return lista;
    }
    public void setLista(SListaTupla lista) {
        this.lista = lista;
    }
    public String getID() {
        return id;
    }

}
