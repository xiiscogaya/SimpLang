package compiler.taulasimbols;

import java.util.List;

public class DFuncion extends Descripcio {
    private TipoSubyacente tipoRetorno;
    private List<TipoSubyacente> listaTipos; // Tipos de los parámetros
    private List<String> nombresParametros; // Nombres de los parámetros

    /**
     * Constructor para una descripción de función.
     * 
     * @param tipoRetorno Tipo de retorno de la función.
     * @param listaTipos  Lista de tipos de los parámetros.
     * @param nombresParametros Lista de nombres de los parámetros.
     */
    public DFuncion(TipoSubyacente tipoRetorno, List<TipoSubyacente> listaTipos, List<String> nombresParametros) {
        super("funcion");
        this.tipoRetorno = tipoRetorno;
        this.listaTipos = listaTipos;
        this.nombresParametros = nombresParametros;
    }

    // Getters
    public TipoSubyacente getTipoRetorno() {
        return tipoRetorno;
    }

    public List<TipoSubyacente> getListaTipos() {
        return listaTipos;
    }

    public List<String> getNombresParametros() {
        return nombresParametros;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFuncion{tipoRetorno=").append(tipoRetorno).append(", parametros=[");
        for (int i = 0; i < listaTipos.size(); i++) {
            sb.append(listaTipos.get(i)).append(" ").append(nombresParametros.get(i));
            if (i < listaTipos.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
