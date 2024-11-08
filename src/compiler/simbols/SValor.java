package compiler.simbols;

import compiler.taulasimbols.TipoSubyacente;

public class SValor extends SBase {

    private TipoSubyacente tipo;
    private Object valor;

    /**
     * Constructor para crear un SValor con un tipo y un valor específico.
     * Este constructor verifica si el valor está dentro del rango permitido.
     * Si el valor no es válido, establece `isError` a true.
     *
     * @param tipo TipoSubyacente que representa el tipo del valor.
     * @param valor Valor asignado, puede ser Integer, Float, o String dependiendo del tipo.
     */
    public SValor(TipoSubyacente tipo, Object valor) {
        super("SValor", valor); // Llama al constructor de SBase con nombre y valor.
        this.tipo = tipo;
        this.valor = valor;
    }

    /**
     * Constructor para crear un SValor "vacío" o con error.
     * Este constructor se utiliza cuando el valor es inválido o si ocurre un error.
     */
    public SValor() {
        super(); // Llama al constructor vacío de SBase y marca isError como true.
    }

    public TipoSubyacente getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return isError ? "Valor vacío o con error" : valor.toString();
    }
}
