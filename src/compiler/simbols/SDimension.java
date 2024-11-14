package compiler.simbols;

import java.util.List;

public class SDimension extends SBase{
    private List<Integer> dimension;

    public SDimension(List<Integer> dimension) {
        super("SDimension", dimension);
        this.dimension = dimension;
    }

    public SDimension() {
        super();
    }

    public List<Integer> getDimension() {
        return dimension;
    }

    public void addParametro(Integer size) {
        this.dimension.add(size);
    }

    
}
