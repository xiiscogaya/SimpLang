package compiler.simbols;

public class SInput extends SBase {
    private String id;
    private int line;

    public SInput(int line, String id) {
        super("SInput", null);
        this.line = line;
        this.id = id;
    }

    public int getLine() {
        return line;
    }

    public String getId() {
        return id;
    }
}
