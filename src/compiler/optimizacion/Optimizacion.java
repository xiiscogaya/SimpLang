package compiler.optimizacion;

import java.util.ArrayList;
import java.util.List;

import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.codigo_intermedio.Instruccion;

public class Optimizacion {
    private CodigoIntermedio codigoIntermedio;
    private List<String> codigoOptimizado;
    
    public Optimizacion(CodigoIntermedio codigoIntermedio) {
        this.codigoIntermedio = codigoIntermedio;
        this.codigoOptimizado = new ArrayList<>();
    }

    public void generarCodigoOptimizado() {
        EliminarRedundacias();
        eliminarTemporalesInnecesarios();
    }

    public void EliminarRedundacias() {
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();
        for (int i = 0; i < instrucciones.size()-1; i++) {
            Instruccion instr = instrucciones.get(i);
            Instruccion siguienteInstr = instrucciones.get(i+1);
            // Si el destino es igual al operando
            if (instr.operador.equals("COPY") && 
                siguienteInstr.operador.equals("COPY") && 
                instr.destino.equals(siguienteInstr.operando1)) {
                    Instruccion newIns = new Instruccion("COPY", instr.operando1, "", siguienteInstr.destino);
                    instrucciones.set(i, newIns);
                    instrucciones.remove(i + 1);
                    i--;
            }
            
        }
    }

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

    public boolean esTemporal(String variable) {
        return variable.startsWith("t");
    }

    public void imprimirCodigo() {
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();
        for (Instruccion instr : instrucciones) {
            System.out.println(instr);
        }
    }


}
