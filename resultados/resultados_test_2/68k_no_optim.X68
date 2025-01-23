; Código ensamblador Motorola 68k generado:
	ORG $1000
; ------- DECLARACIÓN DE CONSTANTES Y VARIABLES --------
t4	DS.L	1
t5	DS.L	1
t6	DS.L	1
t7	DS.L	1
t8	DS.L	1
t9	DS.L	1
t10	DS.L	1
t11	DS.L	1
ID2	DS.L	1
ID1	DS.L	1
ID3	DS.L	1
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
	MOVE.L	 #4,t3
	MOVE.L	 #6,t4
	MOVE.L	t3,D0
	MOVE.L	t4,D1
	ADD.L	D1,D0
	MOVE.L	D0,t5
	MOVE.L	 #4,t6
	MOVE.L	t5,D0
	MOVE.L	t6,D1
	SUB.L	D1,D0
	MOVE.L	D0,t7
	MOVE.L	t7,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 #5,t8
	MOVE.L	t8,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t9
	MOVE.L	#$01000000,SP
	MOVE.L	 t9,ID3
	MOVE.L	 ID3,t11
	MOVE.L	t11,D0
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
