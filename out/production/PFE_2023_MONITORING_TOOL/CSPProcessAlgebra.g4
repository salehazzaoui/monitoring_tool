grammar CSPProcessAlgebra;

program : interleaving | choice | sequential | process ;

interleaving: ('||')* process (interleaving | choice | sequential)* ;

choice: ('[]')* process (interleaving | choice | sequential)* ;

sequential: (';')* process (interleaving | choice | sequential)* ;

/*process :  statements ;*/

process : statement ( '->' statement )* ;

statement : ID (event)* | 'skip' | 'stop' ;

event : ( '!' | '?' ) ID ;

ID : [a-zA-Z][a-zA-Z0-9]* ;

WS : [ \t\r\n]+ -> skip ;