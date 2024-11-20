package compiler.taulasimbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import compiler.sintactic.ErrorManager;

public class TaulaSimbols {
    private int n = 1; // Nivel de ámbito actual
    private final Stack<Integer> ta; // Tabla de ámbitos
    private final List<EntradaExp> te; // Tabla de expansión
    private final Map<String, EntradaDesc> td; // Tabla de descripciones
    private final Stack<String> funcionActual; // Pila para registrar la función actual

    public TaulaSimbols() {
        this.ta = new Stack<>();
        this.ta.push(0); // Inicializa el primer nivel en ta
        this.te = new ArrayList<>();
        this.td = new HashMap<>();
        this.funcionActual = new Stack<>();
    }

    // Clase para las entradas en la tabla de descripciones (td)
    private static class EntradaDesc {
        Descripcio descripcio;
        int np; // Nivel de ámbito de la declaración
        int first; // Apuntador al primer campo en te para estructuras

        EntradaDesc(Descripcio descripcio, int np) {
            this.descripcio = descripcio;
            this.np = np;
            this.first = -1;
        }
    }

    // Clase para las entradas en la tabla de expansión (te)
    private static class EntradaExp {
        String idcamp; // Identificador del campo
        Descripcio descripcio; // Tipo completo (como objeto Tipo)
        int np; // Nivel de ámbito o -1 si es desplazado
        int next; // Índice al siguiente campo de la estructura

        EntradaExp(String idcamp, Descripcio descripcio, int np, int next) {
            this.idcamp = idcamp;
            this.descripcio = descripcio;
            this.np = np;
            this.next = next;
        }
    }

    /**
     * Añadir un nuevo nivel de ámbito
     */
    public void nuevoNivelAmbito() {
        n++;
        ta.push(te.size()); // Se añade un nuevo ámbito en la pila con el índice actual de `te`
    }

    /**
     * Eliminar el nivel de ámbito actual
     */
    public void eliminarNivelAmbito() {
        if (n > 1) {
            int idxTe = ta.pop(); // Recupera el índice del último nivel de expansión
            while (te.size() > idxTe) {
                te.remove(te.size() - 1); // Limpia `te` hasta el índice del ámbito anterior
            }
            n--;
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
        te.clear();
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

        // Si la variable existe pero en un nivel diferente, la añade a la tabla de expansión
        if (td.containsKey(id)) {
            int idxe = ta.peek();
            te.add(new EntradaExp(id, td.get(id).descripcio, td.get(id).np, -1));
            ta.set(n - 1, idxe + 1);
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
     * Actualizar el valor de una variable existente en la tabla
     */
    public void actualizarVariable(String id, Object nuevoValor) {
        Descripcio desc = consultar(id);
        if (desc instanceof DVar) {
            ((DVar) desc).setValor(nuevoValor);
        } else {
            ErrorManager.addError("Error: Intento de modificar constante o identificador inexistente '" + id + "'.");
        }
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
            System.out.printf("ID: %s, Tipo: %s, Nivel: %d, First: %d%n",
                    id, desc.descripcio, desc.np, desc.first);
        }

        // Imprimir la tabla de expansión (te)
        System.out.println("\nTabla de Expansión (te):");
        for (int i = 0; i < te.size(); i++) {
            EntradaExp exp = te.get(i);
            System.out.printf("Posición: %d, ID Campo: %s, Tipo: %s, Nivel: %d, Next: %d%n",
                    i, exp.idcamp, exp.descripcio, exp.np, exp.next);
        }

        // Imprimir la tabla de ámbitos (ta)
        System.out.println("\nTabla de Ámbitos (ta):");
        System.out.println("Niveles de Ámbito en la Pila:");
        for (int i = 0; i < ta.size(); i++) {
            System.out.printf("Nivel %d -> Índice en te: %d%n", i + 1, ta.get(i));
        }

        System.out.println("===============================================");
    }
}
