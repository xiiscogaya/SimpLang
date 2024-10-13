package compiler.lexic;

import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import compiler.sintactic.ParserSym;

%%

%cup 

%public
%class Scanner

%char
%line
%column

%eofval{
    return symbol(ParserSym.EOF);
%eofval}

// DECLARACIONES

digit       = [0-9]
letter      = [a-zA-Z]
id          = {letter}({letter}|{digit})*
SChar       = [^\"\\\n\r] | {EscChar}
EscChar     = \\[ntbrf\\\'\"]

whitespace  = [ \t\n\r]+

%{
    /***
       Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
       aquest cas potser no és del tot necessari.
     ***/
    /**
     Construcció d'un symbol sense atribut associat.
     **/

    StringBuffer string = new StringBuffer();
    
    private ComplexSymbol symbol(int type) {
        return new ComplexSymbol(ParserSym.terminalNames[type], type);
    }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private Symbol symbol(int type, Object value) {
        return new ComplexSymbol(ParserSym.terminalNames[type], type, value);
    }
%}

%%

// Ignorar espacios en blanco
{whitespace} { /* No hacer nada, ignorar */ }

// Palabras clave
"int"           { return symbol(ParserSym.INT); }
"float"         { return symbol(ParserSym.FLOAT); }
"string"        { return symbol(ParserSym.STRING); }
"bool"          { return symbol(ParserSym.BOOL); }
"const"         { return symbol(ParserSym.CONST); }
"def"           { return symbol(ParserSym.DEF); }
"class"         { return symbol(ParserSym.CLASS); }

// Delimitadores
"("             { return symbol(ParserSym.LPAREN); }
")"             { return symbol(ParserSym.RPAREN); }
"{"             { return symbol(ParserSym.LBRACE); }
"}"             { return symbol(ParserSym.RBRACE); }

// Operadores matemáticos y lógicos
"/"             { return symbol(ParserSym.DIVIDE); }
">="            { return symbol(ParserSym.GE); }
"+="            { return symbol(ParserSym.PLUS_IGUAL); }
"-"             { return symbol(ParserSym.MINUS); }
"/="            { return symbol(ParserSym.DIVIDE_IGUAL); }
"not"           { return symbol(ParserSym.NOT); }
"and"           { return symbol(ParserSym.AND); }
"="             { return symbol(ParserSym.IGUAL); }
"<"             { return symbol(ParserSym.LT); }
"or"            { return symbol(ParserSym.OR); }
"\+"            { return symbol(ParserSym.PLUS); }
"<="            { return symbol(ParserSym.LE); }
"%="            { return symbol(ParserSym.MOD_IGUAL); }
"TRUE"          { return symbol(ParserSym.TRUE); }
"*="            { return symbol(ParserSym.TIMES_IGUAL); }
"%"             { return symbol(ParserSym.MOD); }
"=="            { return symbol(ParserSym.EQ); }
"\*"            { return symbol(ParserSym.TIMES); }
"!="            { return symbol(ParserSym.NE); }
"FALSE"         { return symbol(ParserSym.FALSE); }
"-="            { return symbol(ParserSym.MENOS_IGUAL); }
">"             { return symbol(ParserSym.GT); }

// Simbolos
","             { return symbol(ParserSym.COMMA); }

// Literales de cadena

\"{SChar}*\"    { return symbol(ParserSym.STRING_LITERAL, yytext()); }


// Literales de punto flotante
[0-9]+\.[0-9]+      { return symbol(ParserSym.FLOAT_LITERAL, yytext()); }

// Literales de entero
[0-9]+              { return symbol(ParserSym.INT_LITERAL, yytext()); }

// Identificadores
{id}            { return symbol(ParserSym.ID, yytext()); }

// Fin de archivo
<<EOF>>         { return symbol(ParserSym.EOF); }

// Caracter no reconocido
. {
    throw new RuntimeException("Caracter no reconocido: " + yytext() + " en la línea " + (yyline + 1));
}


