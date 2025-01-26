package compiler.optimizacion;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.codigo_intermedio.Instruccion;

public class Optimizacion {
    private CodigoIntermedio codigoIntermedio;
    
    public Optimizacion(CodigoIntermedio codigoIntermedio) {
        this.codigoIntermedio = codigoIntermedio;
    }

    public void generarCodigoOptimizado() {
        // Dos optimizaciones de mireta
        eliminarTemporalesInnecesarios();
        eliminarCodigoInalcanzable();
    }

    /**
     * Método que determina si hay variables temporales innecesarias
     * y en tal caso las elimina
     */
    public void eliminarTemporalesInnecesarios() {
        List<Instruccion> instruccions = codigoIntermedio.obtenerInstrucciones();
        for (int i=0; i<instruccions.size(); i++) {
            Instruccion instr = instruccions.get(i);
            if (esTemporal(instr.destino)) {
                String temporal = instr.destino;
                String operacion = instr.operador;
                String operando1 = instr.operando1;
                String operando2 = instr.operando2;
                for (int j = i + 1; j < instruccions.size(); j++) {
                    Instruccion siguiente = instruccions.get(j);
                    if (siguiente.operador.equals("COPY") && siguiente.operando1.equals(temporal)) {
                        // Reemplazar la instrucción original
                        siguiente.operador = operacion;
                        siguiente.operando1 = operando1;
                        siguiente.operando2 = operando2;
                        instruccions.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }


    /**
     * Método que elimina el código inalcanzable que hay entre un salto
     * incondicional y una etiqueta
     */
    public void eliminarCodigoInalcanzable() {
        List<Instruccion> instruccions = codigoIntermedio.obtenerInstrucciones();
        for (int i=0; i < instruccions.size(); i++) {
            Instruccion instr = instruccions.get(i);
            if (instr.operador.equals("GOTO")) {
                // Eliminar instruccions después del GOTO hasta un SKIP
                while (i + 1 < instruccions.size() && !instruccions.get(i + 1).operador.equals("SKIP")) {
                    instruccions.remove(i + 1);
                }
            }
        }
    }

    public boolean esTemporal(String variable) {
        return variable.startsWith("t");
    }

    public void imprimirCodigo(BufferedWriter writer) throws IOException {
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();
        for (Instruccion instr : instrucciones) {
            writer.write(instr.toString());
            writer.newLine();
        }
    }


}
