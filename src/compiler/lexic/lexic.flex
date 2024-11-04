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
{whitespace}    { /* No hacer nada, ignorar */ }
"#" [^\n]*      { /* Ignorar comentarios */ }



// Palabras clave
"int"           { return symbol(ParserSym.INT); }
"float"         { return symbol(ParserSym.FLOAT); }
"string"        { return symbol(ParserSym.STRING); }
"bool"          { return symbol(ParserSym.BOOL); }
"void"          { return symbol(ParserSym.VOID); }
"const"         { return symbol(ParserSym.CONST); }

"{"             { return symbol(ParserSym.LBRACE); }
"}"             { return symbol(ParserSym.RBRACE); }
"="             { return symbol(ParserSym.EQUAL); }
"+="            { return symbol(ParserSym.PLUS_IGUAL); }
"-="            { return symbol(ParserSym.MENOS_IGUAL); }
"*="            { return symbol(ParserSym.TIMES_IGUAL); }
"/="            { return symbol(ParserSym.DIVIDE_IGUAL); }

";"             { return symbol(ParserSym.SEMICOLON); }

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