; Código ensamblador Motorola 68k generado:
	ORG $1000
; ------- DECLARACIÓN DE CONSTANTES Y VARIABLES --------
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
t25	DS.L	1
t24	DS.L	1
t27	DS.L	1
t26	DS.L	1
t29	DS.L	1
t28	DS.L	1
t0	DS.L	1
t1	DS.L	1
t2	DS.L	1
t3	DS.L	1
t4	DS.L	1
t5	DS.L	1
t6	DS.L	1
t7	DS.L	1
t8	DS.L	1
t9	DS.L	1
t30	DS.L	1
t32	DS.L	1
t31	DS.L	1
t34	DS.L	1
t33	DS.L	1
t36	DS.L	1
t35	DS.L	1
ID10	DS.L	1
t38	DS.L	1
ID11	DS.L	1
t37	DS.L	1
t39	DS.L	1
ID14	DS.L	1
ID12	DS.L	1
ID13	DS.L	1
t41	DS.L	1
t40	DS.L	1
t43	DS.L	1
t42	DS.L	1
t45	DS.L	1
t44	DS.L	1
ID2	DS.L	1
t47	DS.L	1
ID1	DS.L	1
t46	DS.L	1
ID4	DS.L	1
t49	DS.L	1
t48	DS.L	1
ID6	DS.L	1
ID5	DS.L	1
ID8	DS.L	1
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
; -----------------------------------------------------------------------------
; Subrutina para la función: L1
; -----------------------------------------------------------------------------
L1:
	MOVEM.L	D0-D7/A0-A6,-(A7)	; Guardar registros
	CLR.L	D0
	MOVE.L	68(SP),D0
	MOVE.L	D0,ID4
	CLR.L	D0
	MOVE.L	72(SP),D0
	MOVE.L	D0,ID5
	MOVE.L	 ID4,t3
	MOVE.L	t3,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 ID5,t4
	MOVE.L	t4,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t5
	ADDA.L	#8,SP
	MOVE.L	 t5,ID6
	MOVE.L	 ID6,t7
	MOVE.L	t7,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 ID6,t8
	MOVE.L	t8,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t9
	ADDA.L	#8,SP
	MOVE.L	 t9,ID6
	MOVE.L	 ID6,t11
	MOVE.L	t11,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
; -----------------------------------------------------------------------------
; Subrutina para la función: L2
; -----------------------------------------------------------------------------
L2:
	MOVEM.L	D0-D7/A0-A6,-(A7)	; Guardar registros
	CLR.L	D0
	MOVE.L	68(SP),D0
	MOVE.L	D0,ID8
	MOVE.L	 ID8,t12
	MOVE.L	 #0,t13
	MOVE.L	t12,D0
	MOVE.L	t13,D1
	CMP.L	D1,D0
	BGT	L3
	MOVE.L	 #0,t14
	JMP	L4
L3:
	MOVE.L	 #-1,t14
L4:
	MOVE.L	t14,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L5
	MOVE.L	 #-1,t15
	MOVE.L	t15,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
L5:
	MOVE.L	 #0,t16
	MOVE.L	t16,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
; -----------------------------------------------------------------------------
; Subrutina para la función: L7
; -----------------------------------------------------------------------------
L7:
	MOVEM.L	D0-D7/A0-A6,-(A7)	; Guardar registros
	CLR.L	D0
	MOVE.L	68(SP),D0
	MOVE.L	D0,ID10
	MOVE.L	 #1,ID11
L8:
	MOVE.L	 ID10,t18
	MOVE.L	 #0,t19
	MOVE.L	t18,D0
	MOVE.L	t19,D1
	CMP.L	D1,D0
	BGT	L9
	MOVE.L	 #0,t20
	JMP	L10
L9:
	MOVE.L	 #-1,t20
L10:
	MOVE.L	t20,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L11
	MOVE.L	 ID11,t21
	MOVE.L	 ID10,t22
	MOVE.L	t21,D0
	MOVE.L	t22,D1
	MULS.W	D1,D0
	MOVE.L	D0,ID11
	MOVE.L	 ID10,t24
	MOVE.L	 #1,t25
	MOVE.L	t24,D0
	MOVE.L	t25,D1
	SUB.L	D1,D0
	MOVE.L	D0,ID10
	JMP	L8
L11:
	MOVE.L	 ID11,t27
	MOVE.L	t27,64(SP)
	MOVEM.L	(A7)+,D0-D7/A0-A6
	RTS
; ---- FIN DE LAS SUBRUTINAS ----------------
START
	MOVE.L	 #0,ID12
	MOVE.L	 #2,t29
	MOVE.L	t29,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 #3,t30
	MOVE.L	t30,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L0
	MOVE.L	(SP)+,t31
	ADDA.L	#8,SP
	MOVE.L	 t31,ID12
	MOVE.L	 ID12,t33
	MOVE.L	t33,D0
	MOVE.L	D0,-(SP)
	MOVE.L	#0,-(SP)
	JSR	PRINT_GENERAL
	MOVE.L	#$01000000,SP
	JSR	INPUT_INT
	MOVE.L	D1,ID13
	MOVE.L	 ID13,t34
	MOVE.L	t34,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L2
	MOVE.L	(SP)+,t35
	ADDA.L	#4,SP
	MOVE.L	t35,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L12
	MOVE.L	 ID13,t37
	MOVE.L	t37,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L7
	MOVE.L	(SP)+,t38
	ADDA.L	#4,SP
	MOVE.L	 t38,ID12
	MOVE.L	 ID12,t40
	MOVE.L	t40,D0
	MOVE.L	D0,-(SP)
	MOVE.L	#0,-(SP)
	JSR	PRINT_GENERAL
	MOVE.L	#$01000000,SP
L12:
	MOVE.L	 #10,ID12
L14:
	MOVE.L	 ID12,t42
	MOVE.L	t42,D0
	MOVE.L	D0,-(SP)
	MOVE.L	 ID12,t43
	MOVE.L	t43,D0
	MOVE.L	D0,-(SP)
	SUBA.L	#4,SP
	JSR	L1
	MOVE.L	(SP)+,t44
	ADDA.L	#8,SP
	MOVE.L	 t44,ID12
	MOVE.L	 ID12,t46
	MOVE.L	t46,D0
	MOVE.L	D0,-(SP)
	MOVE.L	#0,-(SP)
	JSR	PRINT_GENERAL
	MOVE.L	#$01000000,SP
	JSR	INPUT_BOOL
	MOVE.L	D1,ID14
	MOVE.L	 ID14,t47
	MOVE.L	 #-1,t48
	MOVE.L	t47,D0
	MOVE.L	t48,D1
	CMP.L	D1,D0
	BEQ	L15
	MOVE.L	 #0,t49
	JMP	L16
L15:
	MOVE.L	 #-1,t49
L16:
	MOVE.L	t49,D0
	MOVE.L	#0,D1
	CMP.L	D1,D0
	BEQ	L17
	JMP	L14
L17:
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
