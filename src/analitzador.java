

import java.io.FileReader;
import java_cup.runtime.Symbol;
import compiler.lexic.Scanner;
import compiler.sintactic.ParserSym;

public class analitzador {
    public static void main(String[] args) {
        try {
            // Archivo de entrada
            FileReader reader = new FileReader("C:\\Users\\franc\\Desktop\\Informatica\\Curso 3\\Compiladors\\Practica\\src\\input.txt");
            Scanner scanner = new Scanner(reader);

            Symbol token;
            while ((token = scanner.next_token()).sym != ParserSym.EOF) {
                switch (token.sym) {
                    case ParserSym.INT:
                        System.out.println("Palabra clave 'int' detectada.");
                        break;
                    case ParserSym.ID:
                        System.out.println("Identificador detectado: " + token.value);
                        break;
                    case ParserSym.INT_LITERAL:
                        System.out.println("Literal entero detectado: " + token.value);
                        break;
                    default:
                        System.out.println("Otro token detectado: " + token);
                }
            }

            System.out.println("Análisis léxico completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
