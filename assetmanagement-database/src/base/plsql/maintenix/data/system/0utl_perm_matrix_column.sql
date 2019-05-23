--liquibase formatted sql


--changeSet 0utl_perm_matrix_column:1 stripComments:false
-- 4 Columns.
-- TODO NEED TO ADD MATRIX ID
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MATRIX_COLUMN"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
/************** USER KEY MATRIX *************/
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (1, 1, 1, 1, 0);

--changeSet 0utl_perm_matrix_column:2 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (1, 2, 2, 2, 0);

--changeSet 0utl_perm_matrix_column:3 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (1, 3, 3, 3, 0);

--changeSet 0utl_perm_matrix_column:4 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (1, 4, 4, 4, 0);

--changeSet 0utl_perm_matrix_column:5 stripComments:false
-- 4 Columns.
/************** ORG KEY MATRIX *************/
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (2, 5, 5, 1, 0);

--changeSet 0utl_perm_matrix_column:6 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (2, 6, 6, 2, 0);

--changeSet 0utl_perm_matrix_column:7 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (2, 7, 7, 3, 0);

--changeSet 0utl_perm_matrix_column:8 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (2, 8, 8, 4, 0);

--changeSet 0utl_perm_matrix_column:9 stripComments:false
-- 4 Columns.
/************** ORG KEY PICKER MATRIX *************/
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (3, 9, 5, 1, 0);

--changeSet 0utl_perm_matrix_column:10 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (3, 10, 6, 2, 0);

--changeSet 0utl_perm_matrix_column:11 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (3, 11, 7, 3, 0);

--changeSet 0utl_perm_matrix_column:12 stripComments:false
INSERT INTO UTL_PERM_MATRIX_COLUMN (MATRIX_ID, MATRIX_COLUMN_ID, QUESTION_ID, COLUMN_ORD, UTL_ID)
VALUES (3, 12, 8, 4, 0);