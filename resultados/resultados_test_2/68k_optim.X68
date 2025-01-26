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
ID2	DS.L	1
ID1	DS.L	1
ID4	DS.L	1
ID3	DS.L	1
t0	DS.L	1
t1	DS.L	1
t2	DS.L	1
t3	DS.L	1
VALTRUE	DC.B	'true',0
VALFALSE	DC.B	'false',0
NEWLINE	DC.B	' ',0
	DS.W	0
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
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
; ---- FIN DE LAS SUBRUTINAS ----------------
START
	JSR	INPUT_INT
	MOVE.L	D1,ID4
L1:
	JSR	INPUT_INT
	MOVE.L	D1,ID3
	MOVE.L	 ID4,t3
	MOVE.L	t3,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 ID3,t4
	MOVE.L	t4,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t5
	ADDA.L	#8,SP
	MOVE.L	 t5,ID4
	MOVE.L	 ID4,t7
	MOVE.L	 #200,t8
	MOVE.L	t7,D0
	MOVE.L	t8,D1
	CMP.L	D1,D0
	BLT	L2
	MOVE.L	 #0,t9
	JMP	L3
L2:
	MOVE.L	 #-1,t9
L3:
	MOVE.L	t9,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L4
	JMP	L1
L4:
	MOVE.L	 ID4,t10
	MOVE.L	t10,D0
	MOVE.L	D0,-(SP)
	MOVE.L	#0,-(SP)
	JSR	PRINT_GENERAL
	MOVE.L	#$01000000,SP
	SIMHALT
; -----------------------------------------------------------------------------
PRINT_GENERAL
; General Print Subroutine
; INPUT: Stack contains items to print, each 4 bytes
; OUTPUT: Items are printed one by one
; -----------------------------------------------------------------------------
	MOVEM.L	D0-D3,-(A7)	; Guardar D0 y D1
	ADDA.L	#20,SP
PRINT_LOOP:
	CMP.L	#$01000000,SP
	BEQ	PRINT_END	; Si no hay más elementos, finalizar
	ADD.L	#8,D3
	MOVE.L	(SP)+,D2
	MOVE.L	(SP)+,D1
	CMP.L	#1,D2
	BEQ	PRINT_BOOLEAN
	MOVE.W	#3,D0
	TRAP	#15
	MOVEA.L	#NEWLINE,A1
	MOVE.L	#13,D0
	TRAP	#15
	BRA	PRINT_LOOP
PRINT_BOOLEAN:
	CMP.L	#$FF,D1
	BEQ	PRINT_TRUE
	LEA	VALFALSE,A1
	BRA	PRINT_STRING
PRINT_TRUE:
	LEA	VALTRUE,A1
PRINT_STRING:
	MOVE.W	#13,D0
	TRAP	#15
	BRA	PRINT_LOOP
PRINT_END:
	SUBA.L	#28,SP
	MOVEM.L	(A7)+,D0-D3	; Restaurar D1 y D0
	RTS	; Retornar de la subrutina
; ------------------------------------------------------------------------------
INPUT_INT
; General Input Subrutina
; INPUT: None
; OUTPUT: Reads an integer or value and stores it
	MOVE.L	D0,-(A7)	; SAVE D0
	LEA	INPUT_MARK,A1
	MOVE.L	#14,D0
	TRAP	#15
	CLR.L	D0	; CLEAR D0
	CLR.L	D1	; CLEAR D1
	MOVE.L	#4,D0	; READ_INT
	TRAP	#15
	MOVE.L	(A7)+,D0	
	RTS
; -----------------------------------------------------------------------------
INPUT_BOOL
; READS A BOOLEAN VALUE
; INPUT: NONE
; OUTPUT: D1 - BOOLEAN VALUE (-1 for true, 0 for false)
; -----------------------------------------------------------------------------
	MOVE.L	D0,-(A7)	; SAVE D0
BUCLE:
	LEA	INPUT_MARK,A1	; Mostrar marca de entrada
	MOVE.L	#14,D0	; Llamada al sistema para imprimir
	TRAP	#15
	CLR.L	D0	; Limpiar D0
	MOVE.L	D0,A1	; Limpiar A1
	MOVE.L	#2,D0	; Leer como cadena
	TRAP	#15	; Llamada al sistema para leer
	MOVE.B	(A1),D0	; Leer el primer carácter del input
	CMP.B	#'t',D0
	BEQ	SET_TRUE
	CMP.B	#'f',D0
	BEQ	SET_FALSE
INPUT_ERROR_BOOL:
	LEA	INPUT_ERROR,A1	; Mensaje de error
	MOVE.L	#13,D0	; Imprimir mensaje
	TRAP	#15
	BRA	BUCLE	; Reintentar
SET_TRUE:
	MOVE.L	#-1,D1	; Valor true (-1)
	BRA	INPUT_BOOL_END
SET_FALSE:
	MOVE.L	#0,D1	; Valor false (0)
INPUT_BOOL_END:
	MOVE.L	(A7)+,D0	; Restaurar D0
	RTS	; Retornar de la subrutina
INPUT_MARK	DC.B	'>>> ',0
INPUT_ERROR	DC.B	'ERROR: Valor invalido. Introduzca true o false.',0
	END	START
