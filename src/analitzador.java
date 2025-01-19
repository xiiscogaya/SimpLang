import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import compiler.assembly.GeneradorEnsamblador;
import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.lexic.Scanner;
import compiler.optimizacion.Optimizacion;
import compiler.sintactic.*;

public class analitzador {
    public static void main(String[] args) {
        try {
            // Verificar si se pasó un argumento
            if (args.length == 0) {
                System.err.println("Error: Debes proporcionar la ruta del archivo de entrada como argumento.");
                System.err.println("Uso: java analitzador <ruta_al_test>");
                System.exit(1);
            }

            // Ruta al archivo de entrada
            String inputFilePath = args[0];
            File inputFile = new File(inputFilePath);

            // Verificar si el archivo existe
            if (!inputFile.exists()) {
                System.err.println("Error: El archivo especificado no existe: " + inputFilePath);
                System.exit(1);
            }

            // Abrir el archivo para leer
            FileReader reader = new FileReader(inputFile);

            // Lexer
            long tInit = System.currentTimeMillis();
            Scanner scanner = new Scanner(reader);
            long tFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución del lexer: " + (tFin - tInit) + " milisegundos");

            // Front (parser + semantic + codigo intermedio)
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scanner, sf);
            tInit = System.currentTimeMillis();
            parser.parse();
            tFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución del parser: " + (tFin - tInit) + " milisegundos");

            // Generación de código 68k
            CodigoIntermedio instrucciones = Parser.codigoIntermedio;
            GeneradorEnsamblador generador = new GeneradorEnsamblador(instrucciones);
            tInit = System.currentTimeMillis();
            generador.generarCodigo68k();
            tFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución del generador de instrucciones 68K: " + (tFin - tInit) + " milisegundos");

            generador.imprimirInstrucciones68k();
            generador.guardarArchivoX68("programa.X68");

            CodigoIntermedio instrucciones1 = Parser.codigoIntermedio;
            Optimizacion optimizacion = new Optimizacion(instrucciones1);
            optimizacion.generarCodigoOptimizado();
            optimizacion.imprimirCodigo();
            System.out.println("Análisis léxico completado.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de entrada: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ErrorManager.printErrors();
    }
}
