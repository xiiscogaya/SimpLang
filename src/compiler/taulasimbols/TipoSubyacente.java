// TipoSubyacente.java
package compiler.taulasimbols;

public class TipoSubyacente {

    private Tipus tipoBasico;

    public TipoSubyacente(Tipus tipoBasico) {
        this.tipoBasico = tipoBasico;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TipoSubyacente other = (TipoSubyacente) obj;
        return tipoBasico == other.tipoBasico;
    }

    @Override
    public int hashCode() {
        return tipoBasico != null ? tipoBasico.hashCode() : 0;
    }


    /**
     * Retorna la mida d'un TSB en bytes
     */
    public static int sizeOf(Tipus tipoBasico) {
        switch (tipoBasico) {
            case BOOLEAN:
                return 1; // En Java, el boolean ocupa 1 byte
            case INT:
                return 4; // En Java, el int ocupa 4 bytes
            case CHAR:
                return 2; // En Java, el char ocupa 2 bytes (UTF-16)
            case FLOAT:
                return 4; // Tamaño típico de float en Java
            case POINTER:
            case ARRAY:
                return 8; // Tamaño de referencia en 64 bits
            case NUL:
                return 0;
            default:
                throw new IllegalArgumentException("Tipo no reconocido: " + tipoBasico);
        }
    }

    /**
     * Retorna el nom del TSB en català
     */
    public static String getNomTSB(Tipus tipoBasico) {
        switch (tipoBasico) {
            case INT:
                return "enter";
            case BOOLEAN:
                return "boolean";
            case CHAR:
                return "caracter";
            case FLOAT:
                return "real";
            case ARRAY:
                return "array";
            case POINTER:
                return "punter";
            case NUL:
                return "nul";
            default:
                return "desconegut";
        }
    }

    @Override
    public String toString() {
        return getNomTSB(tipoBasico);
    }

    /**
     * Método que verifica si el tipo actual es numérico (int o float),
     * útil para operaciones aritméticas en la tabla de símbolos.
     */
    public boolean esNumerico() {
        return tipoBasico == Tipus.INT || tipoBasico == Tipus.FLOAT;
    }

    /**
     * Método que verifica si el tipo actual es compatible para asignación aritmética compuesta.
     * (por ejemplo: +=, -=, *=, /=) donde solo los tipos numéricos son válidos.
     */
    public boolean esCompatibleConOperacionCompuesta() {
        return esNumerico(); // Actualmente solo los tipos numéricos son compatibles
    }
}
