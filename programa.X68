	ORG $1000
; ------- DECLARACIÓN DE CONSTANTES Y VARIABLES --------
t4	DS.L	1
t5	DS.L	1
t6	DS.L	1
t7	DS.L	1
t8	DS.L	1
ID2	DS.L	1
ID1	DS.L	1
ID4	DS.L	1
ID3	DS.L	1
ID5	DS.L	1
t0	DS.L	1
t1	DS.L	1
t2	DS.L	1
t3	DS.L	1
; -----------------------------------------------------------------------------
; Subrutina para la función: L0
; -----------------------------------------------------------------------------
L0:
	MOVEM.L	D0-D7/A0-A6,-(A7)	; Guardar registros
	CLR.L	D0
	MOVE.L	68(SP),D0
	MOVE.L	D0,ID1
	CLR.L	D0
	MOVE.L	72(SP),D0
	MOVE.L	D0,ID2
	MOVE.L	 ID1,t0
	MOVE.L	 ID2,t1
	MOVE.L	t0,D0
	MOVE.L	t1,D1
	ADD.L	D1,D0
	MOVE.L	D0,t2
	MOVE.L	t2,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6	; Restaurar registros
	RTS	; Retornar de la función
; ---- FIN DE LAS SUBRUTINAS ----------------
START
	MOVE.L	 #9,t3
	MOVE.L	 t3,ID3
	JSR	INPUT_INT
	MOVE.L	D1,ID4
	MOVE.L	 ID3,t4
	MOVE.L	t4,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 ID4,t5
	MOVE.L	t5,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t6
	MOVE.L	#$01000000,SP
	MOVE.L	 t6,ID5
	MOVE.L	 ID5,t8
	MOVE.L	t8,D0
	MOVE.L	D0,-(SP)
	JSR	PRINT_GENERAL
	SIMHALT
; -----------------------------------------------------------------------------
PRINT_GENERAL
; General Print Subroutine
; INPUT: Stack contains items to print, each 4 bytes
; OUTPUT: Items are printed one by one
; -----------------------------------------------------------------------------
	MOVEM.L	D0-D2,-(A7)	; Guardar D0 y D1
	ADDA.L	#16,SP
	CLR.L	D2
PRINT_LOOP:
	CMP.L	#$01000000,SP
	BEQ	PRINT_END	; Si no hay más elementos, finalizar
	ADD.L	#4,D2
	MOVE.L	(SP)+,D1
	JSR	PRINT_VALUE	; Llamar a subrutina para imprimir
	BRA	PRINT_LOOP
PRINT_END:
	SUBA.L	D2,SP
	SUBA.L	#16,SP
	MOVEM.L	(A7)+,D0-D2	; Restaurar D1 y D0
	RTS	; Retornar de la subrutina
; -----------------------------------------------------------------------------
PRINT_VALUE
; Prints a single value
; INPUT: A1 contains the value to print
; OUTPUT: None
; -----------------------------------------------------------------------------
	MOVE.W	#3,D0	; Código para imprimir enteros
	TRAP	#15	; Llamada al sistema
	JSR	PRINT_NEWLINE
	RTS	; Retornar de la subrutina
PRINT_NEWLINE
	MOVEA.L	#NEWLINE,A1
	MOVE.W	#13,D0
	TRAP	#15
	RTS
NEWLINE	DC.B	' ',0
; ------------------------------------------------------------------------------
INPUT_INT
; General Input Subrutina
; INPUT: None
; OUTPUT: Reads an integer or value and stores it
	MOVE.L	D0,-(A7)	; SAVE D0
	CLR.L	D0	; CLEAR D0
	CLR.L	D1	; CLEAR D1
	MOVE.W	#4,D0	; READ_INT
	TRAP	#15
	MOVE.L	(A7)+,D0	
	RTS
; -----------------------------------------------------------------------------
INPUT_BOOL
; READS A STRING
; INPUT: NONE
; OUTPUT: A1 - STRING READ
; -----------------------------------------------------------------------------
	MOVE.W	D0,-(A7)	; SAVE D0
	CLR.L	D0	; CLEAR D0
	MOVE.W	D0, A1	; CLEAR A1
	MOVE.W	#2,D0	; READ_STRING
	TRAP	#15	; READ_STRING CALL TO OS
	MOVE.W	(A7)+,D0	; RESTORE D0
	RTS	; RETURN FROM SUBROUTINE
; -----------------------------------------------------------------------------
STRING_TO_BOOLEAN
; CONVERTS A STRING TO A BOOLEAN
; ASSERT: INPUT STRING IS:
; TRUE/true OR FALSE/false
; INPUT: A1 - STRING TO CONVERT
; OUTPUT: D1 - BOOLEAN VALUE
; -----------------------------------------------------------------------------
	MOVEM.W	D0/A1,-(A7)	; SAVE D0/A1
	CLR.L	D0	; CLEAR D0
	MOVE.B	(A1),D0	: FIRST CHARACTER
	CMP.W	#'F',D0	; IS FALSE?
	BEQ	.STR_F	; IS F
	CMP.W	#'f',D0	; IS FALSE?
	BEQ	.STR_F	; IS f
	CLR.L	D1
	MOVE.B	#$FF,D1
	JMP	.STR_END	; END
.STR_F
	MOVE.L	#0,D1
.STR_END
	MOVEM.W	(A7)+,D0/A1	; SAVE D0/A1
	RTS	; RETURN FROM SUBROUTINE
	END	START
