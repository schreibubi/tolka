/**
 * Copyright (C) 2009 jwerner <schreibubi@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

grammar TolkaGrammar;

options {
    output=AST;
    ASTLabelType=CommonTree;
}

tokens {
    BITLIST;
    BITORRANGE;
    RULE;
    SWITCHALT;
    RANGE;
    NUMBER;
    ALTS;
    ALT;
    CHAR_FUNC;
    HEX_FUNC;
    BIN_FUNC;
    MATCH_FUNC;
    COMMANDS;
    ASSIGN_COMMAND;
    PRINT_COMMAND;
    PRINTLN_COMMAND;
    CALL_COMMAND;
    UNARY_PLUS;
    UNARY_MINUS;
}

@header {
    package org.schreibubi.tolka;
    import java.util.LinkedHashMap;
}

@lexer::header {
    package org.schreibubi.tolka;
    import java.util.Vector;
}


@lexer::members {
// only from 3.1 onwards!!!
List tokens = new ArrayList();
public void emit(Token token) {
        state.token = token;
      tokens.add(token);
}
public Token nextToken() {
      super.nextToken();
        if ( tokens.size()==0 ) {
            return Token.EOF_TOKEN;
        }
        return (Token)tokens.remove(0);
}
}


@members {
    LinkedHashMap<String,CommonTree> rulesLookup = new LinkedHashMap<String,CommonTree>();
}

rules
    : rule+ EOF!
    ;

rule
    : name=ID (LBRACK ID (COMMA ID)+ RBRACK)? LCURLY statements+ RCURLY -> ^(RULE ID+ statements+)
    ;

statements
    : ( (varExpression ASSIGN alts SEMI) => varExpression ASSIGN alts SEMI-> ^(SWITCHALT varExpression alts)
      | command SEMI -> command)
    ;

alts
    : LPAREN! alt (ALTOR! alt)* RPAREN! //-> ^(ALTS alt+)
    ;

alt
    : altchoose COLON commands -> ^(ALT altchoose commands+)
    ;

altchoose
    : numberOrRange (COMMA! numberOrRange)*
    | 'default'
    ;

commands
    : (command SEMI)+ -> ^(COMMANDS command+)
    ;

command
    : (callCommand | printCommand | assignment)
    ;

assignment
    : ID ASSIGN expr -> ^(ASSIGN_COMMAND ID expr)
    ;

printCommand
    : 'print' expr -> ^(PRINT_COMMAND expr)
    | 'println' expr -> ^(PRINTLN_COMMAND expr)
    ;

callCommand
    : ID (LBRACK expr (COMMA expr)* RBRACK)? -> ^(CALL_COMMAND ID expr+)
    ;

expr
    : numericMultiplicativeExpression ( (PLUS^ | MINUS^) numericMultiplicativeExpression)*
    ;

numericMultiplicativeExpression
    : numericUnaryExpression ( (STAR^ | SLASH^) numericUnaryExpression)*
    ;

numericUnaryExpression
    : ( p=PLUS numericPrimaryExpression -> ^(UNARY_PLUS[$p] numericPrimaryExpression)
      | m=MINUS numericPrimaryExpression -> ^(UNARY_MINUS[$m] numericPrimaryExpression)
      | NOT numericPrimaryExpression -> ^(NOT numericPrimaryExpression) 
      | numericPrimaryExpression -> numericPrimaryExpression)
    ;

numericPrimaryExpression
    : constExpression
    | varExpression
    | functionExpression
    | LPAREN! expr RPAREN!
    ;

functionExpression
    :
      'char' LPAREN expr RPAREN -> ^(CHAR_FUNC expr)
    | 'hex' LPAREN expr RPAREN -> ^(HEX_FUNC expr)
    | 'bin' LPAREN expr RPAREN -> ^(BIN_FUNC expr)
    | 'match' LPAREN expr COMMA expr COMMA expr RPAREN -> ^(MATCH_FUNC expr+) 
     ;

constExpression
    : constNumericExpression
    | constStringExpression
    ;

constNumericExpression
    : FLT_CONST
    | INT_CONST
    | HEX_CONST
    | BIN_CONST
    ;

constStringExpression
    : STR_CONST
    ;

varExpression
    : ID bitchoose?
    ;

bitchoose
    : LBRACK! numberOrRange (COMMA! numberOrRange)* RBRACK!
    ;

numberOrRange
    :  expr ( DDOT expr -> ^(RANGE expr+) 
      |  -> ^(NUMBER expr) )
    ;


ALTOR             :    '|'        ;
COLON             :    ':'        ;
SEMI              :    ';'        ;
LCURLY            :    '{'        ;
RCURLY            :    '}'        ;
LBRACK            :    '['        ;
RBRACK            :    ']'        ;
COMMA             :    ','        ;
LPAREN            :    '('        ;
RPAREN            :    ')'        ;
ASSIGN            :    '='        ;
SLASH             :    '/'        ;
PLUS              :    '+'        ;
MINUS             :    '-'        ;
STAR              :    '*'        ;
NOT               :    '~'        ;
DOT               :    '.'        ;
DDOT              :    '..'       ;

ID
    : ('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$')*
    ;

INT_CONST: DECIMAL_DIGIT+ ;

// FLT_CONST: i=INT_CONST d=DOT (INT_CONST | DOT { skip(); i.setType(INT_CONST); emitJ(i); d.setType(DDOT); d.setText(".."); emitJ(d); })  ;

// only from 3.1 onwards!!!
FLT_CONST:   d=INT_CONST r=DDOT { $d.setType(INT_CONST); emit($d); $r.setType(DDOT); emit($r); }  
           | INT_CONST DOT INT_CONST
           | DOT INT_CONST
           ;


HEX_CONST: '#' HEX_DIGIT+ ;

BIN_CONST: '%' BINARY_DIGIT+ ;

STR_CONST
    : '\"'
      ( options {greedy=false;}
      : ESCAPE_SEQUENCE
      | ~'\\'
      )*
      '\"'
    ;

fragment
ESCAPE_SEQUENCE
    : '\\' '\"'
    | '\\' '\''
    | '\\' '\\'
    | UNICODE_CHAR
    ;

fragment
DECIMAL_DIGIT
    : '0'..'9'
    ;

fragment
HEX_DIGIT
    : '0'..'9'|'a'..'f'|'A'..'F'
    ;

fragment
BINARY_DIGIT
    : '0'..'1'
    ;



fragment
UNICODE_CHAR
    : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

SL_COMMENT
    : '//' ~'\n'* '\n' {$channel=HIDDEN; }
    ;

ML_COMMENT
    : '/*'
      ( options {greedy=false;} : . )*
      '*/'
      {$channel=HIDDEN;}
    ;

WS : ( ' '
     | '\t'
     | '\r'? '\n'
     )+
     { $channel=HIDDEN; }
    ;
