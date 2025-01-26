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
INPUT_MARK	DC.B	'>>>',0
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
	MOVE.L	#$01000000,SP
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
VALTRUE	DC.B	'true',0
VALFALSE	DC.B	'false',0
NEWLINE	DC.B	' ',0
	DS.W	0
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
; READS A STRING
; INPUT: NONE
; OUTPUT: A1 - STRING READ
; -----------------------------------------------------------------------------
	MOVE.L	D0,-(A7)	; SAVE D0
	LEA	INPUT_MARK,A1
	MOVE.L	#14,D0
	TRAP	#15
	CLR.L	D0	; CLEAR D0
	MOVE.L	D0, A1	; CLEAR A1
	MOVE.L	#2,D0	; READ_STRING
	TRAP	#15	; READ_STRING CALL TO OS
	MOVE.L	(A7)+,D0	; RESTORE D0
	RTS	; RETURN FROM SUBROUTINE
; -----------------------------------------------------------------------------
STRING_TO_BOOLEAN
; CONVERTS A STRING TO A BOOLEAN
; ASSERT: INPUT STRING IS:
; TRUE/true OR FALSE/false
; INPUT: A1 - STRING TO CONVERT
; OUTPUT: D1 - BOOLEAN VALUE
; -----------------------------------------------------------------------------
	MOVEM.L	D0/A1,-(A7)	; SAVE D0/A1
	CLR.L	D0	; CLEAR D0
	MOVE.B	(A1),D0	: FIRST CHARACTER
	CMP.L	#'F',D0	; IS FALSE?
	BEQ	.STR_F	; IS F
	CMP.L	#'f',D0	; IS FALSE?
	BEQ	.STR_F	; IS f
	CLR.L	D1
	MOVE.B	#$FF,D1
	JMP	.STR_END	; END
.STR_F
	MOVE.L	#0,D1
.STR_END
	MOVEM.L	(A7)+,D0/A1	; SAVE D0/A1
	RTS	; RETURN FROM SUBROUTINE
	END	START
