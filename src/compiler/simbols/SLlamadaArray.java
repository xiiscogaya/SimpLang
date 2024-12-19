package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SLlamadaArray extends SBase {
    private String arrayName;
    private SListaDimensionesRef dimensiones;
    private int line;
    private String varGenerada;
    public TipoSubyacente tipoArray;


    public SLlamadaArray(int line, String arrayName, SListaDimensionesRef dimensiones) {
        super("SLlamadaArray", null);
        this.line = line;
        this.arrayName = arrayName;
        this.dimensiones = dimensiones;
    }

    public SLlamadaArray() {
        super();
    }

    public int getLine() {
        return line;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public SListaDimensionesRef getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(SListaDimensionesRef dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getVarGenerada() {
        return varGenerada;
    }

    public void setVarGenerada(String varGenerada) {
        this.varGenerada = varGenerada;
    }
    
}
