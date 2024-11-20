package compiler.simbols;

import java.util.List;
import compiler.taulasimbols.TipoSubyacente;

public class SLlamadaFuncion extends SBase {
    private String idFuncion; // Identificador de la función llamada
    private SListaArgumentos argumentos; // Lista de expresiones que son los argumentos de la llamada
    private TipoSubyacente tipoRetorno; // Tipo de retorno de la función

    /**
     * Constructor de SLLamadaFuncion
     * 
     * @param idFuncion   Nombre de la función llamada.
     * @param argumentos  Lista de argumentos (como expresiones).
     * @param tipoRetorno Tipo de retorno de la función.
     */
    public SLlamadaFuncion(String idFuncion, SListaArgumentos argumentos, TipoSubyacente tipoRetorno) {
        super("SLlamadaFuncion", null);
        this.idFuncion = idFuncion;
        this.argumentos = argumentos;
        this.tipoRetorno = tipoRetorno;
    }

    /**
     * Obtener el identificador de la función llamada.
     * 
     * @return Identificador de la función.
     */
    public String getIdFuncion() {
        return idFuncion;
    }

    /**
     * Obtener los argumentos de la llamada.
     * 
     * @return Lista de expresiones que son los argumentos.
     */
    public SListaArgumentos getArgumentos() {
        return argumentos;
    }

    /**
     * Obtener el tipo de retorno de la función.
     * 
     * @return Tipo de retorno de la función.
     */
    public TipoSubyacente getTipoRetorno() {
        return tipoRetorno;
    }

    @Override
    public String toString() {
        return "SLLamadaFuncion{idFuncion='" + idFuncion + "', argumentos=" + argumentos + ", tipoRetorno=" + tipoRetorno + "}";
    }
}
