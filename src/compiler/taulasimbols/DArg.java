package compiler.taulasimbols;

// Descripción para argumentos
class DArg extends Descripcio {
    String tipoArgumento;
    String modoAcceso; // Puede ser mode_in o mode_in_out
    String nombreArgumento;

    DArg(String tipoArgumento, String modoAcceso, String nombreArgumento) {
        super("darg");
        this.tipoArgumento = tipoArgumento;
        this.modoAcceso = modoAcceso;
        this.nombreArgumento = nombreArgumento;
    }
}