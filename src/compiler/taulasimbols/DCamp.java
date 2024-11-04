package compiler.taulasimbols;

// Descripción para campos de tuplas
class DCamp extends Descripcio {
    String tipoCampo;
    int desplazamiento;

    DCamp(String tipoCampo, int desplazamiento) {
        super("dcamp");
        this.tipoCampo = tipoCampo;
        this.desplazamiento = desplazamiento;
    }
}