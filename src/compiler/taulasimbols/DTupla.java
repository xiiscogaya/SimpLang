package compiler.taulasimbols;

import java.util.HashMap;
import java.util.Map;

// Descripción para tuplas en la tabla de símbolos
public class DTupla extends Descripcio {
    private final Map<String, TipoSubyacente> campos; // Mapa que asocia nombres de campos con sus tipos

    // Constructor
    public DTupla() {
        super("tupla"); // Asigna el tipo "tupla" a esta descripción
        this.campos = new HashMap<>();
    }

    // Método para agregar un campo a la tupla
    public void addParametro(TipoSubyacente tipo, String nombreCampo) {
        campos.put(nombreCampo, tipo);
    }

    // Verificar si la tupla tiene un campo
    public boolean tieneCampo(String nombreCampo) {
        return campos.containsKey(nombreCampo);
    }

    // Obtener el tipo de un campo
    public TipoSubyacente getTipoCampo(String nombreCampo) {
        return campos.get(nombreCampo);
    }

    // Método para obtener todos los campos (opcional, según lo que necesites)
    public Map<String, TipoSubyacente> getCampos() {
        return campos;
    }

    @Override
    public String toString() {
        return "DTupla{campos=" + campos + "}";
    }
}
