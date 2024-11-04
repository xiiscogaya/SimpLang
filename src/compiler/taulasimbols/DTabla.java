package compiler.taulasimbols;

// Descripción para tablas (arrays)
class DTabla extends Descripcio {
    String tipoElemento; // Tipo de los elementos dentro de la tabla
    int numeroDimensiones; // Número de dimensiones del array

    DTabla(String tipoElemento, int numeroDimensiones) {
        super("dtabla");
        this.tipoElemento = tipoElemento;
        this.numeroDimensiones = numeroDimensiones;
    }
}