	ORG $1000
; ------- DECLARACIÓN DE CONSTANTES Y VARIABLES --------
t4	DS.L	1
ID0	DS.L	1
t0	DS.L	1
t1	DS.L	1
t2	DS.L	1
t3	DS.L	1
; ---- FIN DE LAS SUBRUTINAS ----------------
START
	MOVE.L	 #1,t0
	MOVE.L	 t0,ID0
	MOVE.L	 ID0,t1
	MOVE.L	 #1,t2
	MOVE.L	t1,D0
	MOVE.L	t2,D1
	ADD.L	D1,D0
	MOVE.L	D0,t3
	MOVE.L	 t3,ID0
	MOVE.L	 ID0,t4
	MOVE.L	t4,D0
	MOVE.L	D0,-(SP)
	JSR	PRINT_GENERAL
	ADDA.L	#4,SP
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
	MOVE.L	#3,D0	; Código para imprimir enteros
	TRAP	#15	; Llamada al sistema
	MOVEA.L	#NEWLINE,A1
	MOVE.L	#13,D0
	TRAP	#15
	RTS	; Retornar de la subrutina
NEWLINE	DC.B	' ',0
	END	START
