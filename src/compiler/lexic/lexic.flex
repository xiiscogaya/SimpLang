package compiler.lexic;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import compiler.sintactic.ErrorManager;
import compiler.sintactic.ParserSym;
import java.util.List;
import java.util.LinkedList;


%%

%cup 

%public
%class Scanner

%unicode
%line
%column

%eofval{
    return symbol(ParserSym.EOF);
%eofval}

// DECLARACIONES

digit       = [0-9]
letter      = [a-zA-Z_]
id          = {letter}({letter}|{digit})*

whitespace  = [ \n\t\r]+

%{
    /***
       Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
       aquest cas potser no és del tot necessari.
     ***/
    /**
     Construcció d'un symbol sense atribut associat.
     **/

     

    public static final List<String> tokens = new LinkedList<>();

    
    /**
     Construcció d'un symbol sense atribut associat.
     **/
    private Symbol symbol(int type) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        Location esquerra = new Location(yyline+1, yycolumn+1);
        Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1);

        ComplexSymbol cs = new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
        cs.left = yyline + 1;
        cs.right = yycolumn + 1;

        // Agregar token a la lista
        tokens.add("TOKEN: " + ParserSym.terminalNames[type] + ", VALOR: " + yytext() + 
               ", LINEA: " + (yyline + 1) + ", COLUMNA: " + (yycolumn + 1));

        return cs;
    }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private ComplexSymbol symbol(int type, Object value) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        Location esquerra = new Location(yyline+1, yycolumn+1);
        Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1);

        ComplexSymbol cs = new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
        cs.left = yyline + 1;
        cs.right = yycolumn + 1;

        // Agregar token a la lista
        tokens.add("TOKEN: " + ParserSym.terminalNames[type] + 
                ", VALOR: " + (value != null ? value : yytext()) + 
                ", LINEA: " + (yyline + 1) + ", COLUMNA: " + (yycolumn + 1));

        return cs;
    }

%}

%%

// Ignorar espacios en blanco
{whitespace}    { /* Ignorar */ }

"#" [^\n]*      { /* Ignorar comentarios */ }



// Palabras clave con valores asignados
"int"|"float"|"string"|"bool"|"void"          { return symbol(ParserSym.TYPE, yytext()); }
"const"         { return symbol(ParserSym.CONST, yytext()); }
"tupla"         { return symbol(ParserSym.TUPLA, yytext()); }
"array"         { return symbol(ParserSym.ARRAY, yytext()); }
"def"           { return symbol(ParserSym.DEF, yytext()); }
"return"        { return symbol(ParserSym.RETURN, yytext()); }
"true"|"false"  { return symbol(ParserSym.BOOLEAN_LITERAL, yytext()); }
"if"            { return symbol(ParserSym.IF, yytext()); }
"elif"          { return symbol(ParserSym.ELIF, yytext()); }
"else"          { return symbol(ParserSym.ELSE, yytext()); }
"while"         { return symbol(ParserSym.WHILE, yytext()); }
"repeat"        { return symbol(ParserSym.REPEAT, yytext()); }
"until"         { return symbol(ParserSym.UNTIL, yytext()); }
"print"         { return symbol(ParserSym.PRINT, yytext()); }
"input"         { return symbol(ParserSym.INPUT, yytext()); }

// Símbolos y operadores con valores asignados
"{"             { return symbol(ParserSym.LBRACE, yytext()); }
"}"             { return symbol(ParserSym.RBRACE, yytext()); }
"("             { return symbol(ParserSym.LPAREN, yytext()); }
")"             { return symbol(ParserSym.RPAREN, yytext()); }
"["             { return symbol(ParserSym.LBRACKET, yytext()); }
"]"             { return symbol(ParserSym.RBRACKET, yytext()); }
"&&"|"||"       { return symbol(ParserSym.OP_COMPARACION, yytext()); }
"<"|">"|">="|"<="|"=="|"!=" { return symbol(ParserSym.OP_LOGICO, yytext()); }
"="             { return symbol(ParserSym.EQUAL, yytext()); }
"+"|"-"|"*"|"/" { return symbol(ParserSym.OP_ARITMETICO, yytext()); }
";"             { return symbol(ParserSym.SEMICOLON, yytext()); }
","             { return symbol(ParserSym.COMMA, yytext()); }
"."             { return symbol(ParserSym.DOT, yytext()); }

// Literales e identificadores
-?[0-9]+        { return symbol(ParserSym.INT_LITERAL, yytext()); }
{id}            { return symbol(ParserSym.ID, yytext()); }

// Fin de archivo
<<EOF>>         { return symbol(ParserSym.EOF); }

// Caracter no reconocido
[^]             { 
    ErrorManager.addError(1, "Error: línea " + (yyline + 1) + ", columna " + (yycolumn + 1) + ": caracter no reconocido '" + yytext() + "'");
}