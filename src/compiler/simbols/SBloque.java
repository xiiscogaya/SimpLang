package compiler.simbols;

import java.util.ArrayList;
import java.util.List;

public class SBloque extends SBase {
    private List<SBase> sentencias; // Lista de sentencias del bloque

    /**
     * Constructor para un bloque vacío.
     */
    public SBloque() {
        super("SBloque", null);
        this.sentencias = new ArrayList<>();
    }

    /**
     * Añade una sentencia al bloque.
     * 
     * @param sentencia La sentencia que se añadirá.
     */
    public void addSentencia(SBase sentencia) {
        this.sentencias.add(sentencia);
    }

    /**
     * Obtiene la lista de sentencias.
     * 
     * @return Lista de sentencias del bloque.
     */
    public List<SBase> getSentencias() {
        return sentencias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bloque:\n");
        for (SBase sentencia : sentencias) {
            sb.append("  ").append(sentencia).append("\n");
        }
        return sb.toString();
    }
}
