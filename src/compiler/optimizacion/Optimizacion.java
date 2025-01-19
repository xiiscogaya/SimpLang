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

    public void imprimirCodigo() {
        List<Instruccion> instrucciones = codigoIntermedio.obtenerInstrucciones();
        for (Instruccion instr : instrucciones) {
            System.out.println(instr);
        }
    }


}
