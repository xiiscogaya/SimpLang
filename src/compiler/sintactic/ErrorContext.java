package compiler.sintactic;

public class ErrorContext {
    private static int currentLine = 0;

    public static int getCurrentLine() {
        return currentLine;
    }

    public static void setCurrentLine(int line) {
        currentLine = line;
    }
}
