package compiler.simbols;

public class SInput extends SBase {
    private String id;

    public SInput(String id) {
        super("SInput", null);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
