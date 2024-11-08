package compiler.simbols;

import java.util.List;
import compiler.taulasimbols.TipoSubyacente;

public class SListaParametros extends SBase {
    private List<TipoSubyacente> tipos;
    private List<SValor> valores;

    /**
     * Constructor para crear una instancia de SListaParametros con listas inicializadas.
     * 
     * @param tipos Lista de tipos subyacentes.
     * @param valores Lista de valores correspondientes.
     */
    public SListaParametros(List<TipoSubyacente> tipos, List<SValor> valores) {
        super("SListaParametros", valores);
        this.tipos = tipos;
        this.valores = valores;
    }

    /**
     * Constructor vacío para crear una instancia vacía de SListaParametros.
     * Permite controlar errores
     */
    public SListaParametros() {
        super();
    }

    /**
     * Método para añadir un tipo y un valor a la lista.
     * 
     * @param tipo Tipo subyacente a añadir.
     * @param valor Valor a añadir.
     */
    public void addParametro(SType tipo, SValor valor) {
        this.tipos.add(tipo.getTipo());
        this.valores.add(valor);
    }

    public List<TipoSubyacente> getTipos() {
        return tipos;
    }

    public List<SValor> getValores() {
        return valores;
    }

    @Override
    public String toString() {
        return "SListaParametros{tipos=" + tipos + ", valores=" + valores + "}";
    }
}
