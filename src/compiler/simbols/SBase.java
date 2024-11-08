package compiler.simbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Clase base para todas las producciones en la gramática.
 * Permite marcar una instancia como "vacía" o "con error" en caso de fallo,
 * lo que ayuda a continuar el análisis sin detenerlo.
 */
public class SBase extends ComplexSymbol {
    private static int idAutoIncrement = 0;  // Identificador único autoincrementado para cada símbolo
    protected boolean isError;  // Indica si la instancia es "vacía" o contiene un error

    /**
     * Constructor para crear una instancia de SBase con un identificador y valor específico.
     * Utilizado normalmente en producciones sin error.
     * 
     * @param variable Nombre de la variable
     * @param valor Valor asociado
     */
    public SBase(String variable, Object valor) {
        super(variable, idAutoIncrement++, valor);
        this.isError = false; // No es un error por defecto
    }

    /**
     * Constructor para crear una instancia "vacía" de SBase.
     * Utilizado en caso de error o cuando la producción es lambda.
     */
    public SBase() {
        super("", idAutoIncrement++);
        this.isError = true; // Se marca como error o vacío
    }

    /**
     * Verifica si esta instancia es un error o está vacía.
     * 
     * @return true si es un error o está vacía, false en caso contrario.
     */
    public boolean isError() {
        return isError;
    }

    /**
     * Establece el estado de error de esta instancia.
     * 
     * @param isError true si es un error, false en caso contrario.
     */
    public void setError(boolean isError) {
        this.isError = isError;
    }
}
