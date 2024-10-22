package compiler.sintactic.Symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import compiler.sintactic.ParserSym;

public class SymbolType extends SymbolBase {
    private int type;

    public SymbolType() {
        super();    // Crear instància amb un valor fals
    }

    public SymbolType(int type) {
        this.type = type;
    }
}
