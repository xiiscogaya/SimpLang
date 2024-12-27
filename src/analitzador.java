

import java.io.FileReader;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import compiler.assembly.GeneradorEnsamblador;
import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.lexic.Scanner;
import compiler.sintactic.*;

public class analitzador {
    public static void main(String[] args) {
        try {
            // Ruta al archivo de entrada
            FileReader reader = new FileReader("C:\\Users\\franc\\Desktop\\Informatica\\Curso 3\\Compiladors\\Practica\\src\\input.txt");

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

            System.out.println("Análisis léxico completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ErrorManager.printErrors();
    }
}
