package compiler.simbols;


public class SBloque extends SBase {
    private SBloque bloque;
    private SBase sentencia;
    /**
     * Constructor para un bloque vacío.
     */
    public SBloque(SBloque bloque, SBase sentencia) {
        super("SBloque", null);
        this.sentencia = sentencia;
        this.bloque = bloque;
    }

    public SBloque(SBase sentencia) {
        super("SBloque", null);
        this.sentencia = sentencia;
        this.bloque = null;
    }

    public SBase getSentencia() {
        return sentencia;
    }

    public SBloque getBloque() {
        return bloque;
    }
}
