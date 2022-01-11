%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define YYDEBUG 1

int workingStack[100];
int currentIndex = 0;

void push(int value) {
    workingStack[currentIndex++] = value;
}

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
program : START cmpdstmt FINISH
	;
cmpdstmt : statement SEMICOLON stmtTemp
    ;
stmtTemp : /*Empty*/  | cmpdstmt
    ;
declaration_statement : VAR ID COLON primitive_type init_statement
    ;
init_statement : /*Empty*/ | ATTRIB expression
    ;
primitive_type : INT typeTemp | CHAR | STRING
    ;
typeTemp : /*Empty*/ | OPEN_RIGHT_BRACKET CONST CLOSED_RIGHT_BRACKET
    ;
statement : declaration_statement | assignment_statement | io_statement | if_statement | while_statement | EXIT
    ;
io_statement : read_statement | write_statement
    ;
assignment_statement : ID ATTRIB expression
    ;
read_statement : READ OPEN_ROUND_BRACKET ID CLOSED_ROUND_BRACKET
    ;
write_statement : PRINT OPEN_ROUND_BRACKET expression CLOSED_ROUND_BRACKET
    ;
expression : arithmetic2 arithmetic1
    ;
arithmetic1 : PLUS arithmetic2 arithmetic1 | MINUS arithmetic2 arithmetic1 | /*Empty*/
    ;
arithmetic2 : multiply2 multiply1
    ;
multiply1 : MUL multiply2 multiply1 | DIV multiply2 multiply1 | /*Empty*/
    ;
multiply2 : OPEN_ROUND_BRACKET expression CLOSED_ROUND_BRACKET | ID | CONST
    ;
if_statement : IF OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET THEN OPEN_CURLY_BRACKET statement CLOSED_CURLY_BRACKET temp_if
    ;
temp_if : /*Empty*/ | ELSE OPEN_CURLY_BRACKET statement CLOSED_CURLY_BRACKET
    ;
while_statement : WHILE OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET EXECUTE OPEN_CURLY_BRACKET statement CLOSED_CURLY_BRACKET
    ;
condition : expression relational_operator expression
    ;
relational_operator : LT | GT | EQ | NE | GTE | LTE
    ;
%%

void printStack() {
    printf("Stack: \n");
    for (int i = 0; i < currentIndex; ++i) {
        printf("\t%s\t", workingStack[i]);
    }
    printf("\n");
}

yyerror(char *s)
{
	printf("%s\n",s);
}

extern FILE *yyin;

int main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	yydebug = 1;
	if(!yyparse()) fprintf(stderr, "\tO.K.\n");

	printStack();
}