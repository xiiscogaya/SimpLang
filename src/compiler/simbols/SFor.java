package compiler.simbols;

public class SFor extends SBase {
    private SDecVar inicializacion;
    private SExpresion condicion;
    private SAsignacion actualizacion;
    private SBloque bloque;

    public SFor(SDecVar inicializacion, SExpresion condicion, SAsignacion actualizacion, SBloque bloque) {
        super("SFor", null);
        this.inicializacion = inicializacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.bloque = bloque;
    }

    public SDecVar getInicializacion() {
        return inicializacion;
    }

    public SExpresion getCondicion() {
        return condicion;
    }

    public SAsignacion getActualizacion() {
        return actualizacion;
    }

    public SBloque getBloque() {
        return bloque;
    }
}

