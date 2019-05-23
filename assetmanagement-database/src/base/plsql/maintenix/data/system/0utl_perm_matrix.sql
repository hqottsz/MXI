--liquibase formatted sql


--changeSet 0utl_perm_matrix:1 stripComments:false
-- first row
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MATRIX"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
/************** USER KEY MATRIX *************/
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (1, 1, 'YES', 0);

--changeSet 0utl_perm_matrix:2 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (1, 2, '*', 0);

--changeSet 0utl_perm_matrix:3 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (1, 3, '*', 0);

--changeSet 0utl_perm_matrix:4 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (1, 4, '*', 0);

--changeSet 0utl_perm_matrix:5 stripComments:false
-- second row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (2, 1, 'NO', 0);

--changeSet 0utl_perm_matrix:6 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (2, 2, 'YES', 0);

--changeSet 0utl_perm_matrix:7 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (2, 3, 'YES', 0);

--changeSet 0utl_perm_matrix:8 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (2, 4, 'NO', 0);

--changeSet 0utl_perm_matrix:9 stripComments:false
-- third row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (3, 1, 'NO', 0);

--changeSet 0utl_perm_matrix:10 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (3, 2, 'NO', 0);

--changeSet 0utl_perm_matrix:11 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (3, 3, 'YES', 0);

--changeSet 0utl_perm_matrix:12 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (3, 4, 'YES', 0);

--changeSet 0utl_perm_matrix:13 stripComments:false
-- fourth row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (4, 1, 'NO', 0);

--changeSet 0utl_perm_matrix:14 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (4, 2, 'YES', 0);

--changeSet 0utl_perm_matrix:15 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (4, 3, '*', 0);

--changeSet 0utl_perm_matrix:16 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (4, 4, '*', 0);

--changeSet 0utl_perm_matrix:17 stripComments:false
-- firth row - catch all
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (5, 1, 'NO', 0);

--changeSet 0utl_perm_matrix:18 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (5, 2, 'NO', 0);

--changeSet 0utl_perm_matrix:19 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (5, 3, 'NO', 0);

--changeSet 0utl_perm_matrix:20 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (5, 4, 'NO', 0);

--changeSet 0utl_perm_matrix:21 stripComments:false
-- first row
/************** ORG KEY MATRIX *************/
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (6, 5, 'YES', 0);

--changeSet 0utl_perm_matrix:22 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (6, 6, '*', 0);

--changeSet 0utl_perm_matrix:23 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (6, 7, '*', 0);

--changeSet 0utl_perm_matrix:24 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (6, 8, '*', 0);

--changeSet 0utl_perm_matrix:25 stripComments:false
-- second row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (7, 5, 'NO', 0);

--changeSet 0utl_perm_matrix:26 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (7, 6, 'YES', 0);

--changeSet 0utl_perm_matrix:27 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (7, 7, 'YES', 0);

--changeSet 0utl_perm_matrix:28 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (7, 8, '*', 0);

--changeSet 0utl_perm_matrix:29 stripComments:false
-- third row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (8, 5, 'NO', 0);

--changeSet 0utl_perm_matrix:30 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (8, 6, 'NO', 0);

--changeSet 0utl_perm_matrix:31 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (8, 7, 'YES', 0);

--changeSet 0utl_perm_matrix:32 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (8, 8, 'YES', 0);

--changeSet 0utl_perm_matrix:33 stripComments:false
-- fourth row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (9, 5, 'NO', 0);

--changeSet 0utl_perm_matrix:34 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (9, 6, 'YES', 0);

--changeSet 0utl_perm_matrix:35 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (9, 7, '*', 0);

--changeSet 0utl_perm_matrix:36 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (9, 8, '*', 0);

--changeSet 0utl_perm_matrix:37 stripComments:false
-- firth row - catch all
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (10, 5, 'NO', 0);

--changeSet 0utl_perm_matrix:38 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (10, 6, 'NO', 0);

--changeSet 0utl_perm_matrix:39 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (10, 7, 'NO', 0);

--changeSet 0utl_perm_matrix:40 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (10, 8, 'NO', 0);

--changeSet 0utl_perm_matrix:41 stripComments:false
-- first row
/************** ORG PICKER KEY MATRIX *************/
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (11, 9, 'YES', 0);

--changeSet 0utl_perm_matrix:42 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (11, 10, '*', 0);

--changeSet 0utl_perm_matrix:43 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (11, 11, '*', 0);

--changeSet 0utl_perm_matrix:44 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (11, 12, '*', 0);

--changeSet 0utl_perm_matrix:45 stripComments:false
-- second row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (12, 9, '*', 0);

--changeSet 0utl_perm_matrix:46 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (12, 10, 'YES', 0);

--changeSet 0utl_perm_matrix:47 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (12, 11, 'YES', 0);

--changeSet 0utl_perm_matrix:48 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (12, 12, '*', 0);

--changeSet 0utl_perm_matrix:49 stripComments:false
-- third row
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (13, 9, '*', 0);

--changeSet 0utl_perm_matrix:50 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (13, 10, '*', 0);

--changeSet 0utl_perm_matrix:51 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (13, 11, '*', 0);

--changeSet 0utl_perm_matrix:52 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (13, 12, 'YES', 0);

--changeSet 0utl_perm_matrix:53 stripComments:false
-- fourth row - catch all
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (14, 9, '*', 0);

--changeSet 0utl_perm_matrix:54 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (14, 10, '*', 0);

--changeSet 0utl_perm_matrix:55 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (14, 11, '*', 0);

--changeSet 0utl_perm_matrix:56 stripComments:false
INSERT INTO UTL_PERM_MATRIX (MATRIX_ROW_ID, MATRIX_COLUMN_ID, ANSWER_TYPE_CD, UTL_ID)
VALUES (14, 12, '*', 0);