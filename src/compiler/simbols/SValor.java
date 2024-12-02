package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SValor extends SBase {

    private TipoSubyacente tipo;
    private String valor;
    private int tamañoChar;


    public SValor(TipoSubyacente tipo, String valor) {
        super("SValor", valor); // Llama al constructor de SBase con nombre y valor.
        this.tipo = tipo;
        this.valor = valor;
    }

    public SValor() {
        super();
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public int getTamañoChar() {
        return tamañoChar;
    }

    public void setTamañoChar(int tamañoChar) {
        this.tamañoChar = tamañoChar;
    }

}
