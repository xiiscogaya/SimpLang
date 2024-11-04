package compiler.sintactic;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    private static List<String> errorMessages = new ArrayList<>();

    // Códigos ANSI para colores
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    // Método para añadir un error al listado
    public static void addError(String message) {
        System.out.println(message);
        errorMessages.add(message);
    }

    // Método para verificar si hubo errores
    public static boolean hasErrors() {
        return !errorMessages.isEmpty();
    }

    // Método para obtener todos los mensajes de error
    public static List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages); // Devolver una copia para evitar modificaciones externas
    }

    // Método para limpiar los errores (por ejemplo, si se hace un nuevo análisis)
    public static void reset() {
        errorMessages.clear();
    }

    // Método para mostrar todos los errores acumulados, si es necesario
    public static void printErrors() {
        if (hasErrors()) {
            System.err.println(ANSI_RED + "Se encontraron los siguientes errores:" + ANSI_RESET);
            for (String error : errorMessages) {
                System.err.println(ANSI_RED + " - " + error + ANSI_RESET);
            }
        } else {
            System.out.println("No se encontraron errores.");
        }
    }
}



