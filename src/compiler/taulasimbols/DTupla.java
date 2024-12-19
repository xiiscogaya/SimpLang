package compiler.taulasimbols;

import java.util.LinkedHashMap;
import java.util.Map;

// Descripción para tuplas en la tabla de símbolos
public class DTupla extends Descripcio {
    private final Map<String, TipoSubyacente> campos; // Mapa que asocia nombres de campos con sus tipos

    // Constructor
    public DTupla() {
        super("tupla"); // Asigna el tipo "tupla" a esta descripción
        this.campos = new LinkedHashMap<>();
    }

    // Método para agregar un campo a la tupla
    public void addParametro(TipoSubyacente tipo, String nombreCampo) {
        campos.put(nombreCampo, tipo);
    }

    // Verificar si la tupla tiene un campo
    public boolean tieneCampo(String nombreCampo) {
        return campos.containsKey(nombreCampo);
    }

    // Obtener desplazamiento
    public int obtenerDesplazamiento(String nombreCampo) {
        int desplazamiento = 0;
        for (Map.Entry<String, TipoSubyacente> entry : campos.entrySet()) {
            String campoActual = entry.getKey();
            TipoSubyacente tipoActual = entry.getValue();

            if (campoActual.equals(nombreCampo)) {
                return desplazamiento;
            }

            desplazamiento += TipoSubyacente.sizeOf(tipoActual.getTipoBasico());
        }
        return desplazamiento;
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
