#ifndef TOKENS_H
#define TOKENS_H

#include <string>

// Definir los tipos de tokens como enumeraciones
enum TokenType {
    // Tipos de datos
    KEYWORD_INT,
    KEYWORD_FLOAT,
    KEYWORD_STRING,
    KEYWORD_BOOL,

    // Operadores aritméticos
    OP_ADD,
    OP_SUB,
    OP_MUL,
    OP_DIV,
    OP_MOD,

    // Operadores lógicos y relacionales
    OP_EQ,
    OP_NEQ,
    OP_LT,
    OP_GT,
    OP_LTE,
    OP_GTE,
    OP_AND,
    OP_OR,
    OP_NOT,
    OP_XOR,

    // Palabras clave
    KEYWORD_IF,
    KEYWORD_ELIF,
    KEYWORD_ELSE,
    KEYWORD_FOR,
    KEYWORD_WHILE,
    KEYWORD_DEF,
    KEYWORD_CLASS,
    KEYWORD_VOID,
    KEYWORD_CONST,
    KEYWORD_SWITCH,
    KEYWORD_CASE,
    KEYWORD_DEFAULT,
    KEYWORD_REPEAT,
    KEYWORD_UNTIL,
    KEYWORD_BREAK,
    KEYWORD_RETURN,

    // Símbolos
    ASSIGN,
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    COMMA,
    SEMICOLON,
    COLON,

    // Literales
    INT_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,
    BOOL_LITERAL,

    // Identificadores
    IDENTIFIER
};

// Variable global para el valor semántico del token
extern int yylval;

#endif // TOKENS_H
