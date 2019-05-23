--liquibase formatted sql


--changeSet 0utl_perm_matrix_row:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MATRIX_ROW"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
/************** USER KEY MATRIX ROWS *************/
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (1, 'EDIT', 1, 1, 0);

--changeSet 0utl_perm_matrix_row:2 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (2, 'EDIT', 1, 2, 0);

--changeSet 0utl_perm_matrix_row:3 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (3, 'EDIT', 1, 3, 0);

--changeSet 0utl_perm_matrix_row:4 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (4, 'READ', 1, 4, 0);

--changeSet 0utl_perm_matrix_row:5 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (5, 'NONE', 1, 5, 0);

--changeSet 0utl_perm_matrix_row:6 stripComments:false
/*************** ORG KEY MATRIX ROWS ************/
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (6, 'EDIT', 2, 1, 0);

--changeSet 0utl_perm_matrix_row:7 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (7, 'EDIT', 2, 2, 0);

--changeSet 0utl_perm_matrix_row:8 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (8, 'EDIT', 2, 3, 0);

--changeSet 0utl_perm_matrix_row:9 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (9, 'READ', 2, 4, 0);

--changeSet 0utl_perm_matrix_row:10 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (10, 'NONE', 2, 5, 0);

--changeSet 0utl_perm_matrix_row:11 stripComments:false
/*************** ORG KEY PICKER MATRIX ROWS ************/
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (11, 'READ', 3, 1, 0);

--changeSet 0utl_perm_matrix_row:12 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (12, 'READ', 3, 2, 0);

--changeSet 0utl_perm_matrix_row:13 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (13, 'READ', 3, 3, 0);

--changeSet 0utl_perm_matrix_row:14 stripComments:false
INSERT INTO UTL_PERM_MATRIX_ROW (MATRIX_ROW_ID, MODE_CD, MATRIX_ID, ROW_ORD, UTL_ID)
VALUES (14, 'NONE', 3, 4, 0);