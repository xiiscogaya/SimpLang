package compiler.taulasimbols;

public class TipoSubyacente {

    private Tipus tipoBasico;

    public TipoSubyacente(Tipus tipoBasico) {
        this.tipoBasico = tipoBasico;
    }

    public TipoSubyacente(String tipo) {
        switch (tipo) {
            case "int":
                this.tipoBasico = Tipus.INT;
                break;
            case "bool":
                this.tipoBasico = Tipus.BOOLEAN;
                break;
            case "caracter":
                this.tipoBasico = Tipus.CHAR;
                break;
            case "float":
                this.tipoBasico = Tipus.FLOAT;
                break;
            case "void":
                this.tipoBasico = Tipus.VOID;
                break;
        }
    }

    public Tipus getTipoBasico() {
        return tipoBasico;
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
                return "int";
            case BOOLEAN:
                return "bool";
            case CHAR:
                return "caracter";
            case FLOAT:
                return "float";
            case VOID:
                return "void";
            default:
                return "desconegut";
        }
    }

    @Override
    public String toString() {
        return getNomTSB(tipoBasico);
    }

    // Método para verificar compatibilidad de tipos
    public boolean esCompatibleCon(TipoSubyacente otroTipo) {
        if (otroTipo == null) {
            return false;
        }

        // Si los tipos son iguales, son compatibles
        if (this.tipoBasico == otroTipo.tipoBasico) {
            return true;
        }

        // Compatibilidad entre números (INT y FLOAT)
        if (this.esNumerico() && otroTipo.esNumerico()) {
            return true;
        }

        // Compatibilidad lógica: solo BOOLEAN
        if (this.tipoBasico == Tipus.BOOLEAN && otroTipo.tipoBasico == Tipus.BOOLEAN) {
            return true;
        }

        // Si no cumplen ninguno de los criterios, no son compatibles
        return false;
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
