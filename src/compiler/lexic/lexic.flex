package compiler.lexic;

import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

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
SChar       = [^\"\\\n\r] | {EscChar}
EscChar     = \\[ntbrf\\\'\"]

whitespace  = [ \t\r]+


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
{whitespace}    { /* No hacer nada, ignorar */ }
"#" [^\n]*      { /* Ignorar comentarios */ }
\n              { return symbol(ParserSym.NEWLINE); }



// Palabras clave
"int"           { return symbol(ParserSym.INT); }
"float"         { return symbol(ParserSym.FLOAT); }
"string"        { return symbol(ParserSym.STRING); }
"bool"          { return symbol(ParserSym.BOOL); }
"void"          { return symbol(ParserSym.VOID); }
"const"         { return symbol(ParserSym.CONST); }
"def"           { return symbol(ParserSym.DEF); }
"class"         { return symbol(ParserSym.CLASS); }
"self"          { return symbol(ParserSym.SELF); }

// Delimitadores
"("             { return symbol(ParserSym.LPAREN); }
")"             { return symbol(ParserSym.RPAREN); }
"{"             { return symbol(ParserSym.LBRACE); }
"}"             { return symbol(ParserSym.RBRACE); }

// Condicionales
"if"            { return symbol(ParserSym.IF); }
"elif"          { return symbol(ParserSym.ELIF); }
"else"          { return symbol(ParserSym.ELSE); }
"while"         { return symbol(ParserSym.WHILE); }
"for"           { return symbol(ParserSym.FOR); }
"in"            { return symbol(ParserSym.IN); }
"repeat"        { return symbol(ParserSym.REPEAT); }
"until"         { return symbol(ParserSym.UNTIL); }
"switch"        { return symbol(ParserSym.SWITCH); }
"case"          { return symbol(ParserSym.CASE); }
"default"       { return symbol(ParserSym.DEFAULT); }
"print"         { return symbol(ParserSym.PRINT); }
"input"         { return symbol(ParserSym.INPUT); }

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
"."             { return symbol(ParserSym.PUNTO); }

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

[^]             { System.err.println("Error léxico en línea " + (yyline+1) + ", columna " + (yycolumn+1) + ": caracter no reconocido '" + yytext() + "'");
                return symbol(ParserSym.error);  }


