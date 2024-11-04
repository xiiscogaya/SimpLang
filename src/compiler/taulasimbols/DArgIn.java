package compiler.taulasimbols;

// Descripción para argumentos de solo lectura
class DArgIn extends Descripcio {
    String tipoArgumento;
    String nombreArgumento;

    DArgIn(String tipoArgumento, String nombreArgumento) {
        super("darg_in");
        this.tipoArgumento = tipoArgumento;
        this.nombreArgumento = nombreArgumento;
    }
}
