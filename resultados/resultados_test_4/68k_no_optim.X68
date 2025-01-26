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
t12	DS.L	1
t11	DS.L	1
t14	DS.L	1
t13	DS.L	1
t16	DS.L	1
t15	DS.L	1
t18	DS.L	1
t17	DS.L	1
t19	DS.L	1
t21	DS.L	1
t20	DS.L	1
t23	DS.L	1
t22	DS.L	1
ID2	DS.L	1
ID1	DS.L	1
ID4	EQU	10
ID3	EQU	6
ID6	DS.L	1
ID5	DS.L	8
t0	DS.L	1
ID8	DS.L	1
t1	DS.L	1
ID7	DS.L	1
t2	DS.L	1
t3	DS.L	1
ID9	DS.L	1
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
	MULS.W	D1,D0
	MOVE.L	D0,t2
	MOVE.L	t2,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
; ---- FIN DE LAS SUBRUTINAS ----------------
START
	MOVE.L	 #6,ID3
	MOVE.L	 #10,ID4
	MOVE.L	 #0,t2
	MOVEA.L	t2,A0
	MOVE.L	ID3,(ID5,A0)
	MOVE.L	 #4,t2
	MOVEA.L	t2,A0
	MOVE.L	ID4,(ID5,A0)
	MOVE.L	 #0,t3
	MOVE.L	 t3,ID6
	MOVE.L	 #-1,t4
	MOVE.L	 t4,ID7
L1:
	MOVE.L	 ID7,t5
	MOVE.L	 ID6,t6
	MOVE.L	 #200,t7
	MOVE.L	t6,D0
	MOVE.L	t7,D1
	CMP.L	D1,D0
	BLT	L2
	MOVE.L	 #0,t8
	JMP	L3
L2:
	MOVE.L	 #-1,t8
L3:
	MOVE.L	t5,D0
	AND.L	t8,D0
	TST.L	D0
	BNE	L4
	MOVE.L	 #0,t9
	JMP	L5
L4:
	MOVE.L	 #-1,t9
L5:
	MOVE.L	t9,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L6
	JSR	INPUT_INT
	MOVE.L	D1,ID8
	MOVE.L	 ID8,t10
	MOVE.L	 #10,t11
	MOVE.L	t10,D0
	MOVE.L	t11,D1
	CMP.L	D1,D0
	BGT	L7
	MOVE.L	 #0,t12
	JMP	L8
L7:
	MOVE.L	 #-1,t12
L8:
	MOVE.L	t12,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L9
	MOVE.L	 #0,t13
	MOVE.L	 t13,ID7
	JMP	L10
L9:
	MOVE.L	 #0,t14
	MOVEA.L	t14,A0
	MOVE.L	(ID5,A0),t15
	MOVE.L	t15,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 #4,t16
	MOVEA.L	t16,A0
	MOVE.L	(ID5,A0),t17
	MOVE.L	t17,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t18
	ADDA.L	#8,SP
	MOVE.L	 t18,ID9
	MOVE.L	 ID6,t20
	MOVE.L	 ID9,t21
	MOVE.L	t20,D0
	MOVE.L	t21,D1
	ADD.L	D1,D0
	MOVE.L	D0,t22
	MOVE.L	 t22,ID6
L10:
	JMP	L1
L6:
	MOVE.L	 ID6,t23
	MOVE.L	t23,D0
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
