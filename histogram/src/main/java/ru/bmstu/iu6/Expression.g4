grammar Expression;
OP         : ('+' | '-' | '*' | '/' | '&' | '|' | '#|' | '#/');
ELEMENT    : ([a-z] | [A-Z])+[0-9]*;
term       : (ELEMENT | '(' expr ')' | ELEMENT1D);
ELEMENT1D  : '(' WS* ELEMENT WS* ',' WS* ELEMENT WS* ')';
expr       : term (OP term)*;
WS : [ \t\r\n]+ -> skip;