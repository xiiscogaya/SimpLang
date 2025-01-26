import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import compiler.assembly.GeneradorEnsamblador;
import compiler.codigo_intermedio.CodigoIntermedio;
import compiler.lexic.Scanner;
import compiler.optimizacion.Optimizacion;
import compiler.sintactic.*;

public class analitzador {

    // Códigos ANSI para colores
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        String resultadosPath = null;
        try {
            // Verificar si se pasó un argumento
            if (args.length == 0) {
                System.err.println(colorize("Error: Debes proporcionar la ruta del archivo de entrada como argumento.", "red"));
                System.err.println(colorize("Uso: java analitzador <ruta_al_test>", "red"));
                System.exit(1);
            }

            // Ruta al archivo de entrada
            String inputFilePath = args[0];
            File inputFile = new File(inputFilePath);

            // Verificar si el archivo existe
            if (!inputFile.exists()) {
                System.err.println(colorize("Error: El archivo especificado no existe: " + inputFilePath, "red"));
                System.exit(1);
            }

            // Obtener el número del test
            int testNumber = getTestNumber(inputFilePath);

            // Crear carpeta de resultados
            resultadosPath = "resultados/resultados_test_" + testNumber;
            File resultadosDir = new File(resultadosPath);
            limpiarCarpeta(resultadosDir);
            if (!resultadosDir.exists() && !resultadosDir.mkdirs()) {
                System.err.println(colorize("Error: No se pudo crear la carpeta de resultados.", "red"));
                System.exit(1);
            }

            // Abrir el archivo para leer
            FileReader reader = new FileReader(inputFile);

            // Lexer
            long tInit = System.currentTimeMillis();
            Scanner scanner = new Scanner(reader);
            long tFin = System.currentTimeMillis();
            System.out.println(colorize("Tiempo de ejecución del lexer: " + (tFin - tInit) + " milisegundos", "blue"));

            // Front (parser + semantic + código intermedio)
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scanner, sf);
            tInit = System.currentTimeMillis();
            parser.parse();
            tFin = System.currentTimeMillis();
            System.out.println(colorize("Tiempo de ejecución del parser: " + (tFin - tInit) + " milisegundos", "blue"));

            if (ErrorManager.hasError()) {
        
                // Crear el archivo de errores
                File archivoErrores = new File(resultadosDir, "errores.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoErrores))) {
                    ErrorManager.guardarErroresEnArchivo(writer);
                }
            
                System.out.println(colorize("Se encontraron errores. Ver 'errores.txt' para más detalles.", "red"));
                return;
            }

            // Archivo de salida para los tokens
            File tokensFile = new File(resultadosDir, "tokens.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tokensFile))) {
                for (String token : Scanner.tokens) {
                    writer.write(token + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo de tokens: " + e.getMessage());
            }
            

            // Archivo de salida para la tabla de símbolos
            File tablaSimbolosFile = new File(resultadosDir, "tabla_simbolos.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tablaSimbolosFile))) {
                Parser.taulaSim.imprimirTabla(writer);
            }

            // Archivo para guardar la tabla de variables y procedimientos
            File tablaVariablesProcedimientos = new File(resultadosDir, "variables_procedimientos.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tablaVariablesProcedimientos))) {
                writer.write("===== TABLA DE VARIABLES =====\n");
                Parser.codigoIntermedio.imprimirTablaVariables(writer);
                writer.write("\n===== TABLA DE PROCEDIMIENTOS =====\n");
                Parser.codigoIntermedio.imprimirTablaProcedimientos(writer);
            }

            // Archivo para guardar el código intermedio
            File codigoIntermedio = new File(resultadosDir, "codigo_intermedio.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(codigoIntermedio))) {
                Parser.codigoIntermedio.imprimirCodigo(writer);
            }

            // Generación de código 68k
            CodigoIntermedio instrucciones = Parser.codigoIntermedio;
            GeneradorEnsamblador generador = new GeneradorEnsamblador(instrucciones);
            tInit = System.currentTimeMillis();
            generador.generarCodigo68k();
            tFin = System.currentTimeMillis();
            System.out.println(colorize("Tiempo de ejecución del generador de instrucciones 68K: " + (tFin - tInit) + " milisegundos", "blue"));

            File noOptim68k = new File(resultadosDir, "68k_no_optim.X68");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(noOptim68k))) {
                generador.imprimirInstrucciones68k(writer);
            }

            /**
             * OPTIMIZACIONES
             */
            // Optimixación del código
            CodigoIntermedio instrucciones1 = Parser.codigoIntermedio;
            Optimizacion optimizacion = new Optimizacion(instrucciones1);
            optimizacion.generarCodigoOptimizado();
            
            // Generación del 68k optimizado
            GeneradorEnsamblador generador1 = new GeneradorEnsamblador(instrucciones1);
            tInit = System.currentTimeMillis();
            generador1.generarCodigo68k();
            tFin = System.currentTimeMillis();
            System.out.println(colorize("Tiempo de ejecución del generador de instrucciones 68K optimizado: " + (tFin - tInit) + " milisegundos", "blue"));
            File Optim68k = new File(resultadosDir, "68k_optim.X68");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Optim68k))) {
                generador1.imprimirInstrucciones68k(writer);
            }


            System.out.println(colorize("PROCESO DE COMPILACION TERMINADO", "blue"));
            System.out.println(colorize("ARCHIVOS GENERADOS EN /resultados", "blue"));
        } catch (IOException e) {
            System.err.println(colorize("Error al leer el archivo de entrada: " + e.getMessage(), "red"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Extrae el número del test desde la ruta del archivo de entrada.
     */
    private static int getTestNumber(String filePath) {
        String[] parts = filePath.split("_");
        try {
            return Integer.parseInt(parts[parts.length - 1].replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0; // Valor predeterminado si no se puede extraer el número
        }
    }

    /**
     * Limpia todos los archivos de una carpeta
     * 
     * @param carpeta
     */
    private static void limpiarCarpeta(File carpeta) {
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] files = carpeta.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Aplica colores ANSI al mensaje según el tipo.
     * 
     * @param message Mensaje a colorear.
     * @param color   Tipo de color ("red" o "blue").
     * @return Mensaje coloreado.
     */
    private static String colorize(String message, String color) {
        switch (color.toLowerCase()) {
            case "red":
                return ANSI_RED + message + ANSI_RESET;
            case "blue":
                return ANSI_BLUE + message + ANSI_RESET;
            default:
                return message;
        }
    }
}
