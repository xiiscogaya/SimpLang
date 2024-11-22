package compiler.simbols;


public class SMain extends SBase {
    private SMain main;
    private SBase sentencia;

    public SMain(SMain main, SBase sentencia) {
        super("SMain", null);
        this.sentencia = sentencia;
        this.main = main;
    }

    public SMain(SBase sentencia) {
        super("SMain", null);
        this.sentencia = sentencia;
        this.main = null;
    }

    public SBase getSentencia() {
        return sentencia;
    }

    public SMain getMain() {
        return main;
    }
    
}
