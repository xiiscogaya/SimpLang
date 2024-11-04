package compiler.taulasimbols;

// Descripción para índices de tablas
class DIndex extends Descripcio {
    String tipoSubrango; // Tipo de subrango (ej. int, char)
    int rangoInicio;     // Valor inicial del rango
    int rangoFin;        // Valor final del rango

    DIndex(String tipoSubrango, int rangoInicio, int rangoFin) {
        super("dindex");
        this.tipoSubrango = tipoSubrango;
        this.rangoInicio = rangoInicio;
        this.rangoFin = rangoFin;
    }
}
