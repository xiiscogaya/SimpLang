package compiler.sintactic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    private static List<String> errorLexic = new ArrayList<>();
    private static List<String> errorSintactic = new ArrayList<>();
    private static List<String> errorSemantic = new ArrayList<>();

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

    public static void guardarErroresEnArchivo(BufferedWriter writer) throws IOException {
        if (hasError()) {
            writer.write("===== ERRORES ENCONTRADOS =====\n");
            if (hasErrorLexic()) {
                writer.write("\nErrores Léxicos:\n");
                for (String error : errorLexic) {
                    writer.write(" - " + error + "\n");
                }
            }
            if (hasErrorSintactic()) {
                writer.write("\nErrores Sintácticos:\n");
                for (String error : errorSintactic) {
                    writer.write(" - " + error + "\n");
                }
            }
            if (hasErrorSemantic()) {
                writer.write("\nErrores Semánticos:\n");
                for (String error : errorSemantic) {
                    writer.write(" - " + error + "\n");
                }
            }
        } else {
            writer.write("No se encontraron errores.\n");
        }
    }
}    