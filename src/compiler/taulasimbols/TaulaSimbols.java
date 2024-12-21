package compiler.taulasimbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import compiler.sintactic.ErrorManager;

public class TaulaSimbols {
    private int n = 1; // Nivel de ámbito actual
    private final Map<String, EntradaDesc> td; // Tabla de descripciones
    private final Stack<String> funcionActual; // Pila para registrar la función actual

    public TaulaSimbols() {
        this.td = new HashMap<>();
        this.funcionActual = new Stack<>();
    }

    // Clase para las entradas en la tabla de descripciones (td)
    private static class EntradaDesc {
        Descripcio descripcio;
        int np; // Nivel de ámbito de la declaración

        EntradaDesc(Descripcio descripcio, int np) {
            this.descripcio = descripcio;
            this.np = np;
        }
    }

    private final List<LlamadaPendiente> llamadasPendientes = new ArrayList<>();

    // Clase interna para representar llamadas pendientes
    private static class LlamadaPendiente {
        String nombreFuncion;
        int linea;

        public LlamadaPendiente(String nombreFuncion, int linea) {
            this.nombreFuncion = nombreFuncion;
            this.linea = linea;
        }
    }

    public void registrarLlamadaPendiente(String nombreFuncion, int linea) {
        llamadasPendientes.add(new LlamadaPendiente(nombreFuncion, linea));
    }

    public void validarLlamadasPendientes() {
        for (LlamadaPendiente llamada : llamadasPendientes) {
            if (consultar(llamada.nombreFuncion) == null) {
                ErrorManager.addError(3, "Error: La función '" + llamada.nombreFuncion + "' llamada en línea " + llamada.linea + " no está declarada.");
            }
        }
    }
    
    


    /**
     * Añadir un nuevo nivel de ámbito
     */
    public void nuevoNivelAmbito() {
        n++;
    }

    /**
     * Eliminar el nivel de ámbito actual
     */
    public void eliminarNivelAmbito() {
        if (n > 1) {
            int nivelActual = n;
            n--; // Reduce el nivel de ámbito actual

            // Limpia las variables locales del nivel actual en la tabla de descripciones
            td.entrySet().removeIf(entry -> entry.getValue().np == nivelActual);
        } else {
            ErrorManager.addError(3, "Error: Intento de eliminar el ámbito global.");
        }
    }

    /**
     * Limpiar la tabla (para reiniciar el analizador)
     */
    public void buidar() {
        n = 1;
        td.clear();
        funcionActual.clear();
    }

    /**
     * Añadir un símbolo a la tabla de descripciones (td)
     */
    public void posar(String id, Descripcio descripcio) {
        // Verifica si la variable ya existe en el nivel actual
        if (td.containsKey(id) && td.get(id).np == n) {
            ErrorManager.addError(3, "Error: Redefinición de la variable '" + id + "' en el mismo nivel.");
            return; // No agrega el símbolo de nuevo
        }

        // Agrega la variable a la tabla de descripciones
        td.put(id, new EntradaDesc(descripcio, n));
    }

    /**
     * Consultar un símbolo en la tabla de descripciones
     */
    public Descripcio consultar(String id) {
        return td.get(id) != null ? td.get(id).descripcio : null;
    }


    /**
     * Registrar la función actual (cuando se entra en su ámbito)
     */
    public void entrarFuncion(String nombreFuncion) {
        funcionActual.push(nombreFuncion);
    }

    /**
     * Salir de la función actual (cuando se termina su procesamiento)
     */
    public void salirFuncion() {
        if (!funcionActual.isEmpty()) {
            funcionActual.pop();
        }
    }

    /**
     * Obtener el nombre de la función actual
     */
    public String obtenerFuncionActual() {
        return funcionActual.isEmpty() ? null : funcionActual.peek();
    }

    /**
     * 
     */
    public int calcularOcupacionLocales() {
        int ocupacion = 0;
    
        // Recorre la tabla de descripciones para el nivel actual
        for (Map.Entry<String, EntradaDesc> entry : td.entrySet()) {
            EntradaDesc entrada = entry.getValue();
            if (entrada.np == n) { // Si la variable pertenece al nivel actual
                Descripcio descripcio = entrada.descripcio;
    
                // Calcula el tamaño según el tipo de la variable
                if (descripcio instanceof DVar) {
                    DVar var = (DVar) descripcio;
                    ocupacion += TipoSubyacente.sizeOf(var.getTipoSubyacente().getTipoBasico());
                } else if (descripcio instanceof DArray) {
                    DArray array = (DArray) descripcio;
                    int baseSize = TipoSubyacente.sizeOf(array.getTipo().getTipoBasico());
                    int totalSize = baseSize;
                    for (Object dimension : array.getDimensiones()) {
                        totalSize *= Integer.parseInt((String) dimension);
                    }
                    ocupacion += totalSize;
                } else if (descripcio instanceof DTupla) {
                    DTupla tupla = (DTupla) descripcio;
                    for (TipoSubyacente tipo : tupla.getCampos().values()) {
                        ocupacion += TipoSubyacente.sizeOf(tipo.getTipoBasico());
                    }
                }
            }
        }
    
        return ocupacion;
    }
    

    /**
     * Imprimir el estado de la tabla de símbolos (para depuración)
     */
    public void imprimirTabla() {
        System.out.println("===== Estado Actual de la Tabla de Símbolos =====");

        // Imprimir la tabla de descripciones (td)
        System.out.println("Tabla de Descripciones (td):");
        for (Map.Entry<String, EntradaDesc> entry : td.entrySet()) {
            String id = entry.getKey();
            EntradaDesc desc = entry.getValue();
            System.out.printf("ID: %s, Tipo: %s, Nivel: %d%n",
                    id, desc.descripcio, desc.np);
        }

        System.out.println("===============================================");
    }
}
