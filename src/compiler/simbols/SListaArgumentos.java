package compiler.simbols;


public class SListaArgumentos extends SBase {
    private SListaArgumentos argumentos;
    private SExpresion expresion;
    private int line;

    /**
     * Constructor para cuando haya más argumentos
     */
    public SListaArgumentos(int line, SListaArgumentos argumentos, SExpresion expresion) {
        super("SListaArgumentos", null);
        this.line = line;
        this.argumentos = argumentos;
        this.expresion = expresion;
    }

    // Constructor para cuando no haya más argumentos
    public SListaArgumentos(int line, SExpresion expresion) {
        super("SListaArgumentos", null);
        this.line = line;
        this.argumentos =null;
        this.expresion = expresion;
    }

    public SListaArgumentos() {
        super();
    }

    public int getLine() {
        return line;
    }

    /**
     * Obtener la lista de argumentos.
     * 
     * @return Lista de argumentos.
     */
    public SListaArgumentos getArgumentos() {
        return argumentos;
    }

    public SExpresion getExpresion() {
        return expresion;
    }

    @Override
    public String toString() {
        return "SListaArgumentos{argumentos=" + argumentos + "}";
    }
}
