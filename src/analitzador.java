
/** 
import java.io.FileReader;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import compiler.lexic.Scanner;
import compiler.sintactic.Parser;


public class analitzador {
    public static void main(String[] args) {
        try {
            // Archivo de entrada
            FileReader reader = new FileReader("C:\\Users\\franc\\Desktop\\Informatica\\Curso 3\\Compiladors\\Practica\\src\\input.txt");
            Scanner scanner = new Scanner(reader);

            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scanner, sf);
            parser.parse();

            System.out.println("Análisis léxico completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

import java.io.FileReader;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import compiler.lexic.Scanner;
import compiler.sintactic.Parser;
import compiler.sintactic.ParserSym;

public class analitzador {
    public static void main(String[] args) {
        try {
            // Ruta al archivo de entrada
            FileReader reader = new FileReader("C:\\Users\\franc\\Desktop\\Informatica\\Curso 3\\Compiladors\\Practica\\src\\input.txt");
            Scanner scanner = new Scanner(reader);

            Symbol token;
            do {
                // Obtener el siguiente token
                token = scanner.next_token();

                // Obtener el nombre del token utilizando ParserSym
                String tokenName = ParserSym.terminalNames[token.sym];

                // Obtener el valor del token (si tiene)
                Object tokenValue = token.value;

                // Imprimir el token y su valor
                if (tokenValue != null) {
                    System.out.println("Token: " + tokenName + " (" + tokenValue + ")");
                } else {
                    System.out.println("Token: " + tokenName);
                }

            } while (token.sym != ParserSym.EOF);
            
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scanner, sf);
            parser.parse();

            System.out.println("Análisis léxico completado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
