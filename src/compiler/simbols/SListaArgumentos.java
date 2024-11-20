package compiler.simbols;

import java.util.ArrayList;
import java.util.List;

public class SListaArgumentos extends SBase {
    private List<SValor> argumentos;

    /**
     * Constructor para crear una lista de argumentos inicializada vacía.
     */
    public SListaArgumentos() {
        super("SListaArgumentos", null);
        this.argumentos = new ArrayList<>();
    }

    /**
     * Método para añadir un argumento a la lista.
     * 
     * @param argumento El argumento a añadir.
     */
    public void addArgumento(SValor argumento) {
        this.argumentos.add(argumento);
    }

    /**
     * Obtener la lista de argumentos.
     * 
     * @return Lista de argumentos.
     */
    public List<SValor> getArgumentos() {
        return argumentos;
    }

    @Override
    public String toString() {
        return "SListaArgumentos{argumentos=" + argumentos + "}";
    }
}
