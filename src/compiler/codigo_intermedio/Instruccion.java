package compiler.codigo_intermedio;

public class Instruccion {
    public String operador;
    public String operando1;
    public String operando2;
    public String destino;

    public Instruccion(String operador, String operando1, String operando2, String destino) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.destino = destino;
    }

    @Override
    public String toString() {
        if (operando2 == null || operando2.isEmpty()) {
            return String.format("%s %s -> %s", operador, operando1, destino);
        }
        if (operador.equals("IND_ASS")) {
            return String.format("%s, %s -> %s[%s]", operador, operando1, destino, operando2);
        }
        return String.format("%s %s, %s -> %s", operador, operando1, operando2, destino);
    }
}
