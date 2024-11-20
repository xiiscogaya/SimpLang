package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SExpresion extends SBase {
    private TipoSubyacente tipo; // Tipo de la expresión

    // Constructor único para cualquier tipo de expresión
    public SExpresion(TipoSubyacente tipo) {
        super("SExpresion", null);
        this.tipo = tipo;
    }

    // Obtener el tipo de la expresión
    public TipoSubyacente getTipo() {
        return tipo;
    }

    // Actualizar el tipo (si fuera necesario en ciertas situaciones)
    public void setTipo(TipoSubyacente tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "SExpresion{tipo=" + tipo + "}";
    }
}
