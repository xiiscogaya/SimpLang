package compiler.lexic;

import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import compiler.sintactic.ErrorManager;
import compiler.sintactic.ParserSym;

%%

%cup 

%public
%class Scanner

%unicode
%char
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

     

    StringBuffer string = new StringBuffer();
    
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

        return cs;
    }

%}

%%

// Ignorar espacios en blanco
{whitespace}    { /* Ignorar */ }

"#" [^\n]*      { /* Ignorar comentarios */ }



// Palabras clave
"int"|"float"|"string"|"bool"|"void"          { return symbol(ParserSym.TYPE, yytext()); }
"const"         { return symbol(ParserSym.CONST); }
"tupla"         { return symbol(ParserSym.TUPLA); }
"array"         { return symbol(ParserSym.ARRAY); }
"def"           { return symbol(ParserSym.DEF); }
"return"        { return symbol(ParserSym.RETURN); }
"true"|"false"  { return symbol(ParserSym.BOOLEAN_LITERAL, yytext()); }
"if"            { return symbol(ParserSym.IF); }
"elif"          { return symbol(ParserSym.ELIF); }
"else"          { return symbol(ParserSym.ELSE); }
"while"         { return symbol(ParserSym.WHILE); }
"repeat"        { return symbol(ParserSym.REPEAT); }
"until"         { return symbol(ParserSym.UNTIL); }

"print"         { return symbol(ParserSym.PRINT); }
"input"         { return symbol(ParserSym.INPUT); }


"{"             { return symbol(ParserSym.LBRACE); }
"}"             { return symbol(ParserSym.RBRACE); }
"("             { return symbol(ParserSym.LPAREN); }
")"             { return symbol(ParserSym.RPAREN); }
"["             { return symbol(ParserSym.LBRACKET); }
"]"             { return symbol(ParserSym.RBRACKET); }
"&&"|"||"               { return symbol(ParserSym.OP_COMPARACION, yytext()); }
"<"|">"|">="|"<="|"=="|"!="     { return symbol(ParserSym.OP_LOGICO, yytext()) ;}
"="             { return symbol(ParserSym.EQUAL); }

"+"|"-"|"*"|"/"         { return symbol(ParserSym.OP_ARITMETICO, yytext()); }

";"             { return symbol(ParserSym.SEMICOLON); }
","             { return symbol(ParserSym.COMMA); }
"."             { return symbol(ParserSym.DOT); }

-?[0-9]+        { return symbol(ParserSym.INT_LITERAL, yytext()); }

// Identificadores
{id}            { return symbol(ParserSym.ID, yytext()); }

// Fin de archivo
<<EOF>>         { return symbol(ParserSym.EOF); }

// Caracter no reconocido

[^]             { 
                ErrorManager.addError(1, "Error: línea " + (yyline+1) + ", columna " + (yycolumn+1) + ": caracter no reconocido '" + yytext() + "'");
                  }