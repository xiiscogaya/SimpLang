package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;
import java.util.List;

public class SDecFun extends SBase {
    private String nombre;
    private TipoSubyacente tipoRetorno;
    private List<TipoSubyacente> tiposParametros;
    private List<String> nombresParametros;

    /**
     * Constructor para crear una declaración de función con un tipo de retorno, nombre, lista de parámetros y cuerpo.
     * 
     * @param nombre Nombre de la función.
     * @param tipoRetorno Tipo de retorno de la función.
     * @param tiposParametros Lista de tipos de los parámetros.
     * @param nombresParametros Lista de nombres de los parámetros.
     * @param cuerpo Cuerpo de la función (bloque de código).
     */
    public SDecFun(String nombre, TipoSubyacente tipoRetorno, List<TipoSubyacente> tiposParametros, List<String> nombresParametros) {
        super("SDecFun", null); // Indica que este es un nodo de declaración de función en el AST
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.tiposParametros = tiposParametros;
        this.nombresParametros = nombresParametros;

    }

    /**
     * Constructor para crear una instancia vacía en caso de error en la declaración de la función.
     */
    public SDecFun() {
        super();
    }

    // Getters para acceder a los atributos de la función
    public String getNombre() {
        return nombre;
    }

    public TipoSubyacente getTipoRetorno() {
        return tipoRetorno;
    }

    public List<TipoSubyacente> getTiposParametros() {
        return tiposParametros;
    }

    public List<String> getNombresParametros() {
        return nombresParametros;
    }


    /**
     * Sobrescribe el método toString() para mostrar la información de la función.
     * Incluye el tipo de retorno, el nombre de la función, los parámetros y el cuerpo de la función.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Funcion ").append(nombre).append("(");
        
        // Añadir los parámetros al StringBuilder
        for (int i = 0; i < tiposParametros.size(); i++) {
            sb.append(tiposParametros.get(i)).append(" ").append(nombresParametros.get(i));
            if (i < tiposParametros.size() - 1) {
                sb.append(", ");
            }
        }
        
        sb.append(") -> ").append(tipoRetorno);

        return sb.toString();
    }
}

