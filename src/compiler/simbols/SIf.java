package compiler.simbols;

public class SIf extends SBase {
    private SExpresion expresion; // Expresión booleana del if
    private SBloque bloqueIf;     // Bloque principal
    private SElif listaElseIf;    // Lista de else-if (opcional)
    private SBloque bloqueElse;   // Bloque else (opcional)
    private int line;

    // Constructor con else y else-if
    public SIf(int line, SExpresion expresion, SBloque bloqueIf, SElif listaElseIf, SBloque bloqueElse) {
        super("SIf", null);
        this.line = line;
        this.expresion = expresion;
        this.bloqueIf = bloqueIf;
        this.listaElseIf = listaElseIf;
        this.bloqueElse = bloqueElse;
    }

    // Constructor sin else
    public SIf(int line, SExpresion expresion, SBloque bloqueIf, SElif listaElseIf) {
        super("SIf", null);
        this.line = line;
        this.expresion = expresion;
        this.bloqueIf = bloqueIf;
        this.listaElseIf = listaElseIf;
        this.bloqueElse = null;
    }

    // Constructor sin else y else-if
    public SIf(int line, SExpresion expresion, SBloque bloqueIf) {
        super("SIf", null);
        this.line = line;
        this.expresion = expresion;
        this.bloqueIf = bloqueIf;
        this.listaElseIf = null;
        this.bloqueElse = null;
    }

    // Getters y setters
    public int getLine() {
        return line;
    }
    
    public SExpresion getExpresion() {
        return expresion;
    }

    public SBloque getBloque1() {
        return bloqueIf;
    }

    public SElif getLista() {
        return listaElseIf;
    }

    public SBloque getBloque2() {
        return bloqueElse;
    }
}
