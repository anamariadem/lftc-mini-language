%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define YYDEBUG 1

int yylex();
yyerror(char *s);

%}

%token AND
%token START
%token FINISH
%token VAR
%token ELSE
%token EXECUTE
%token WHILE
%token IF
%token THEN
%token INT
%token CHAR
%token READ
%token PRINT
%token STRING
%token EXIT

%token ID
%token CONST

%token ATTRIB
%token EQ
%token NE
%token LTE
%token GTE
%token LT
%token GT
%token NOT


%left '+' '-' '*' '/'

%token PLUS
%token MINUS
%token DIV
%token MUL

%token OPEN_CURLY_BRACKET
%token CLOSED_CURLY_BRACKET
%token OPEN_ROUND_BRACKET
%token CLOSED_ROUND_BRACKET
%token OPEN_RIGHT_BRACKET
%token CLOSED_RIGHT_BRACKET

%token COMMA
%token SEMICOLON
%token COLON
%token SPACE

%start program

%%
program : START compound_statement FINISH
    ;
compound_statement : statement SEMICOLON stmtTemp
    ;
stmtTemp : /*Empty*/  | compound_statement
    ;
declaration_statement : VAR ID COLON primitive_type | VAR ID COLON primitive_type ATTRIB expression
    ;
primitive_type : INT | CHAR | STRING
    ;
array_declaration_statement : VAR ID COLON primitive_type OPEN_RIGHT_BRACKET CONST CLOSED_RIGHT_BRACKET
    ;
statement : declaration_statement | array_declaration_statement | assignment_statement | io_statement | if_statement | while_statement
    ;
io_statement : read_statement | write_statement
    ;
assignment_statement : ID ATTRIB expression
    ;
read_statement : READ OPEN_ROUND_BRACKET ID CLOSED_ROUND_BRACKET
    ;
write_statement : PRINT OPEN_ROUND_BRACKET expression CLOSED_ROUND_BRACKET
    ;
expression : term | term PLUS expression | term MINUS expression
    ;
term : factor | factor MUL term | factor DIV term
    ;
factor : CONST | ID | OPEN_ROUND_BRACKET expression CLOSED_ROUND_BRACKET
    ;
if_statement : IF OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET THEN OPEN_CURLY_BRACKET compound_statement CLOSED_CURLY_BRACKET
    ;
while_statement : WHILE OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET EXECUTE OPEN_CURLY_BRACKET compound_statement CLOSED_CURLY_BRACKET
    ;
condition : expression relational_operator expression
    ;
relational_operator : GT | LT | LTE | GTE | NE | EQ
    ;

%%

yyerror(char *s)
{
	printf("%s\n",s);
}

extern FILE *yyin;

int main(int argc, char **argv)
{
	if(argc>1) yyin = fopen(argv[1],"r");
    //yydebug = 1;
	if(!yyparse()) fprintf(stderr, "\tO.K.\n");
}