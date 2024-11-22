package compiler.taulasimbols;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import compiler.sintactic.ErrorManager;

public class TaulaSimbols {
    private int n = 1; // Nivel de ámbito actual
    private final Stack<Integer> ta; // Tabla de ámbitos
    private final Map<String, EntradaDesc> td; // Tabla de descripciones
    private final Stack<String> funcionActual; // Pila para registrar la función actual

    public TaulaSimbols() {
        this.ta = new Stack<>();
        this.ta.push(0); // Inicializa el primer nivel en ta
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

    /**
     * Añadir un nuevo nivel de ámbito
     */
    public void nuevoNivelAmbito() {
        n++;
        ta.push(n); // Añadir el nuevo nivel al stack
    }

    /**
     * Eliminar el nivel de ámbito actual
     */
    public void eliminarNivelAmbito() {
        if (n > 1) {
            int nivelActual = n;
            ta.pop(); // Elimina el nivel actual del stack
            n--; // Reduce el nivel de ámbito actual

            // Limpia las variables locales del nivel actual en la tabla de descripciones
            td.entrySet().removeIf(entry -> entry.getValue().np == nivelActual);
        } else {
            ErrorManager.addError("Error: Intento de eliminar el ámbito global.");
        }
    }

    /**
     * Limpiar la tabla (para reiniciar el analizador)
     */
    public void buidar() {
        n = 1;
        ta.clear();
        ta.push(0);
        td.clear();
        funcionActual.clear();
    }

    /**
     * Añadir un símbolo a la tabla de descripciones (td)
     */
    public void posar(String id, Descripcio descripcio) {
        // Verifica si la variable ya existe en el nivel actual
        if (td.containsKey(id) && td.get(id).np == n) {
            ErrorManager.addError("Error: Redefinición de la variable '" + id + "' en el mismo nivel.");
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

        // Imprimir los niveles de ámbitos
        System.out.println("\nTabla de Ámbitos (ta):");
        System.out.println("Niveles de Ámbito en la Pila:");
        for (int i = 0; i < ta.size(); i++) {
            System.out.printf("Nivel %d%n", ta.get(i));
        }

        System.out.println("===============================================");
    }
}
