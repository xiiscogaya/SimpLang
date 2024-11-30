package compiler.lexic;

import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import compiler.sintactic.ErrorManager;
import compiler.sintactic.ParserSym;
import compiler.sintactic.ErrorContext;

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

        return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
    }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private ComplexSymbol symbol(int type, Object value) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        Location esquerra = new Location(yyline+1, yycolumn+1);
        Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1);

        return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
    }

%}

%%

// Ignorar espacios en blanco
{whitespace}    { /* Ignorar */ }

"#" [^\n]*      { /* Ignorar comentarios */ }



// Palabras clave
"int"           { return symbol(ParserSym.INT); }
"float"         { return symbol(ParserSym.FLOAT); }
"string"        { return symbol(ParserSym.STRING); }
"bool"          { return symbol(ParserSym.BOOL); }
"void"          { return symbol(ParserSym.VOID); }
"const"         { return symbol(ParserSym.CONST); }
"tupla"         { return symbol(ParserSym.TUPLA); }
"array"         { return symbol(ParserSym.ARRAY); }
"def"           { return symbol(ParserSym.DEF); }
"return"        { return symbol(ParserSym.RETURN); }
"true"|"false"  { return symbol(ParserSym.BOOLEAN_LITERAL); }
"if"            { return symbol(ParserSym.IF); }
"elif"          { return symbol(ParserSym.ELIF); }
"else"          { return symbol(ParserSym.ELSE); }
"while"         { return symbol(ParserSym.WHILE); }
"for"           { return symbol(ParserSym.FOR); }

"print"         { return symbol(ParserSym.PRINT); }
"input"         { return symbol(ParserSym.INPUT); }


"{"             { return symbol(ParserSym.LBRACE); }
"}"             { return symbol(ParserSym.RBRACE); }
"("             { return symbol(ParserSym.LPAREN); }
")"             { return symbol(ParserSym.RPAREN); }
"["             { return symbol(ParserSym.LBRACKET); }
"]"             { return symbol(ParserSym.RBRACKET); }
"+="|"-="|"*="|"/="     { return symbol(ParserSym.OP_ASSIGN, yytext()); }
"&&"|"||"               { return symbol(ParserSym.OP_ARITMETICO, yytext()); }
"<"|">"|">="|"<="|"=="|"!="     { return symbol(ParserSym.OP_LOGICO, yytext()) ;}
"="             { return symbol(ParserSym.EQUAL); }

"+"|"-"|"*"|"/"         { return symbol(ParserSym.OP_ARITMETICO, yytext()); }

";"             { return symbol(ParserSym.SEMICOLON); }
","             { return symbol(ParserSym.COMMA); }
"."             { return symbol(ParserSym.DOT); }

// Literales de cadena

\"{SChar}*\"    {   String contenidoSinComillas = yytext().substring(1, yytext().length() - 1);
                    return symbol(ParserSym.STRING_LITERAL, contenidoSinComillas); 
                }


// Literales de punto flotante o enteros
-?[0-9]+\.[0-9]+    { return symbol(ParserSym.FLOAT_LITERAL, yytext()); }

-?[0-9]+        { return symbol(ParserSym.INT_LITERAL, yytext()); }

// Identificadores
{id}            { return symbol(ParserSym.ID, yytext()); }

// Fin de archivo
<<EOF>>         { return symbol(ParserSym.EOF); }

// Caracter no reconocido

[^]             { 
                ErrorManager.addError(1, "Error: línea " + (yyline+1) + ", columna " + (yycolumn+1) + ": caracter no reconocido '" + yytext() + "'");
                  }