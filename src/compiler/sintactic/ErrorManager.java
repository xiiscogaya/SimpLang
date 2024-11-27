package compiler.sintactic;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    private static List<String> errorLexic = new ArrayList<>();
    private static List<String> errorSintactic = new ArrayList<>();
    private static List<String> errorSemantic = new ArrayList<>();

    // Códigos ANSI para colores
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    // Método para añadir un error al listado
    public static void addError(int tipoError,String message) {
        System.out.println(message);
        switch (tipoError) {
            case 1:
                errorLexic.add(message);
                break;
            case 2:
                errorSintactic.add(message);
                break;
            case 3:
                errorSemantic.add(message);
        }
    }

    // Método para verificar si hubo errores lexicos
    public static boolean hasErrorLexic() {
        return !errorLexic.isEmpty();
    }

    // Método para verificar si hubo errores sintacticos
    public static boolean hasErrorSintactic() {
        return !errorSintactic.isEmpty();
    }

    // Método para verificar si hubo errores semanticos
    public static boolean hasErrorSemantic() {
        return !errorSemantic.isEmpty();
    }

    // Método para obtener todos los mensajes de error lexicos
    public static List<String> getErrorLexic() {
        return new ArrayList<>(errorLexic); // Devolver una copia para evitar modificaciones externas
    }

    // Método para obtener todos los mensajes de error sintacticos
    public static List<String> getErrorSintactic() {
        return new ArrayList<>(errorSintactic); // Devolver una copia para evitar modificaciones externas
    }

    // Método para obtener todos los mensajes de error
    public static List<String> getErrorSemantic() {
        return new ArrayList<>(errorSemantic); // Devolver una copia para evitar modificaciones externas
    }

    // Método para limpiar los errores (por ejemplo, si se hace un nuevo análisis)
    public static void reset() {
        errorLexic.clear();
        errorSintactic.clear();
        errorSemantic.clear();
    }

    public static boolean hasError() {
        return hasErrorLexic() || hasErrorSintactic() || hasErrorSemantic();
    }

    // Método para mostrar todos los errores acumulados, si es necesario
    public static void printErrors() {
        if (hasError()) {
            System.err.println(ANSI_RED + "Se encontraron los siguientes errores" + ANSI_RESET);
            if (hasErrorLexic()) {
                System.out.println(ANSI_RED + "Errores léxicos:" + ANSI_RED);
                for (String error : errorLexic) {
                    System.err.println(ANSI_RED + " - " + error + ANSI_RESET);
                }
            }
            if (hasErrorSintactic()) {
                System.out.println(ANSI_RED + "Errores sintácticos:" + ANSI_RED);
                for (String error : errorSintactic) {
                    System.err.println(ANSI_RED + " - " + error + ANSI_RESET);
                }
            }
            if (hasErrorSemantic()) {
                System.out.println(ANSI_RED + "Errores semánticos:" + ANSI_RED);
                for (String error : errorSemantic) {
                    System.err.println(ANSI_RED + " - " + error + ANSI_RESET);
                }
            }
            
        } else {
            System.out.println("No se encontraron errores.");
        }
    }
}



