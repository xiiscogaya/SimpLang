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

whitespace  = [ \t]+

%{
    /***
       Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
       aquest cas potser no és del tot necessari.
     ***/
    /**
     Construcció d'un symbol sense atribut associat.
     **/
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
"tuple"         { return symbol(ParserSym.TUPLE); }
"file"          { return symbol(ParserSym.FILE); }
"def"           { return symbol(ParserSym.DEF); }
"void"          { return symbol(ParserSym.VOID); }
"return"        { return symbol(ParserSym.RETURN); }
"if"            { return symbol(ParserSym.IF); }
"elif"          { return symbol(ParserSym.ELIF); }
"else"          { return symbol(ParserSym.ELSE); }
"for"           { return symbol(ParserSym.FOR); }
"in"            { return symbol(ParserSym.IN); }
"while"         { return symbol(ParserSym.WHILE); }
"repeat"        { return symbol(ParserSym.REPEAT); }
"until"         { return symbol(ParserSym.UNTIL); }
"switch"        { return symbol(ParserSym.SWITCH); }
"case"          { return symbol(ParserSym.CASE); }
"break"         { return symbol(ParserSym.BREAK); }
"default"       { return symbol(ParserSym.DEFAULT); }
"open"          { return symbol(ParserSym.OPEN); }
"read"          { return symbol(ParserSym.READ); }
"close"         { return symbol(ParserSym.CLOSE); }
"TRUE"          { return symbol(ParserSym.TRUE); }
"FALSE"         { return symbol(ParserSym.FALSE); }
"print"         { return symbol(ParserSym.PRINT); }
"input"         { return symbol(ParserSym.INPUT); }
"class"         { return symbol(ParserSym.CLASS); }
"self"          { return symbol(ParserSym.SELF); }

// Operadores lógicos
"and"           { return symbol(ParserSym.AND); }
"or"            { return symbol(ParserSym.OR); }
"not"           { return symbol(ParserSym.NOT); }
"xor"           { return symbol(ParserSym.XOR); }

// Operadores aritméticos
"+"             { return symbol(ParserSym.PLUS); }
"-"             { return symbol(ParserSym.MINUS); }
"*"             { return symbol(ParserSym.MULT); }
"/"             { return symbol(ParserSym.DIV); }
"%"             { return symbol(ParserSym.MOD); }

// Operadores relacionales
"=="            { return symbol(ParserSym.EQUAL); }
"!="            { return symbol(ParserSym.NOTEQUAL); }
"<="            { return symbol(ParserSym.LESSEQUAL); }
">="            { return symbol(ParserSym.GREATEREQUAL); }
"<"             { return symbol(ParserSym.LESS); }
">"             { return symbol(ParserSym.GREATER); }

// Delimitadores
"("             { return symbol(ParserSym.LPAREN); }
")"             { return symbol(ParserSym.RPAREN); }
"["             { return symbol(ParserSym.LBRACKET); }
"]"             { return symbol(ParserSym.RBRACKET); }
"{"             { return symbol(ParserSym.LCURLY); }
"}"             { return symbol(ParserSym.RCURLY); }
"="             { return symbol(ParserSym.ASSIGN); }
","             { return symbol(ParserSym.COMMA); }
":"             { return symbol(ParserSym.COLON); }

// Literales
{digit}+        { return symbol(ParserSym.INT_LITERAL, new Integer(yytext())); }
{digit}+"."{digit}+ { return symbol(ParserSym.FLOAT_LITERAL, new Float(yytext())); }

// Cadenas de texto
\"[^\"]*\"      { return symbol(ParserSym.STRING_LITERAL, yytext()); }

// Identificadores
{id}            { return symbol(ParserSym.ID, yytext()); }

// Fin de archivo
<<EOF>>         { return symbol(ParserSym.EOF); }

// Caracter no reconocido
[^]             { return symbol(ParserSym.error); }

