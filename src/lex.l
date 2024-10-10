%{
#include "tokens.h"
#include <iostream>
#include <string>

extern int yylval; // Variable global para almacenar el valor del token
%}

%option noyywrap
%option c++

/* Definir patrones regulares */
digit       [0-9]
letter      [a-zA-Z]
id          {letter}({letter}|{digit})*

%%

/* Ignorar espacios en blanco */
[ \t]+                  { /* No hacer nada, ignorar espacios */ }

/* Comentarios */
\#.*                    { /* Ignorar comentarios */ }

/* Tipos de datos */
"int"                   { return KEYWORD_INT; }
"float"                 { return KEYWORD_FLOAT; }
"string"                { return KEYWORD_STRING; }
"bool"                  { return KEYWORD_BOOL; }

/* Palabras clave */
"if"                    { return KEYWORD_IF; }
"elif"                  { return KEYWORD_ELIF; }
"else"                  { return KEYWORD_ELSE; }
"for"                   { return KEYWORD_FOR; }
"while"                 { return KEYWORD_WHILE; }
"def"                   { return KEYWORD_DEF; }
"class"                 { return KEYWORD_CLASS; }
"void"                  { return KEYWORD_VOID; }
"const"                 { return KEYWORD_CONST; }
"switch"                { return KEYWORD_SWITCH; }
"case"                  { return KEYWORD_CASE; }
"default"               { return KEYWORD_DEFAULT; }
"repeat"                { return KEYWORD_REPEAT; }
"until"                 { return KEYWORD_UNTIL; }
"break"                 { return KEYWORD_BREAK; }
"return"                { return KEYWORD_RETURN; }

/* Operadores lógicos */
"and"                   { return OP_AND; }
"or"                    { return OP_OR; }
"not"                   { return OP_NOT; }
"xor"                   { return OP_XOR; }

/* Operadores aritméticos */
"=="                    { return OP_EQ; }
"!="                    { return OP_NEQ; }
"<="                    { return OP_LTE; }
">="                    { return OP_GTE; }
"<"                     { return OP_LT; }
">"                     { return OP_GT; }
"\+"                    { return OP_ADD; }
"-"                     { return OP_SUB; }
"\*"                    { return OP_MUL; }
"/"                     { return OP_DIV; }
"%"                     { return OP_MOD; }

/* Símbolos */
"="                     { return ASSIGN; }
"\("                    { return LEFT_PAREN; }
"\)"                    { return RIGHT_PAREN; }
"\{"                    { return LEFT_BRACE; }
"\}"                    { return RIGHT_BRACE; }
"\["                    { return LEFT_BRACKET; }
"\]"                    { return RIGHT_BRACKET; }
","                     { return COMMA; }
";"                     { return SEMICOLON; }
":"                     { return COLON; }

/* Literales */
{digit}+                { yylval = atoi(yytext); return INT_LITERAL; }
{digit}+"."{digit}+     { yylval = atof(yytext); return FLOAT_LITERAL; }
\"([^\"\\]|\\.)*\"      { yylval = new std::string(yytext); return STRING_LITERAL; }
"TRUE"|"FALSE"          { yylval = (strcmp(yytext, "TRUE") == 0) ? 1 : 0; return BOOL_LITERAL; }

/* Identificadores */
{id}                    { yylval = new std::string(yytext); return IDENTIFIER; }

%%

int yywrap() { return 1; }
