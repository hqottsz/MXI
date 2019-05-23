--liquibase formatted sql


--changeSet 0utl_perm_question:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MATRIX_QUESTION"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
/************** USER KEY MATRIX QUESTIONS *************/
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (1, 'Does the user work for my organization?', 0);

--changeSet 0utl_perm_question:2 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (2, 'Does the user work for my company?', 0);

--changeSet 0utl_perm_question:3 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (3, 'Does the user work for my sub-organization?', 0);

--changeSet 0utl_perm_question:4 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (4, 'Do I work for an Admin Organization?', 0);

--changeSet 0utl_perm_question:5 stripComments:false
/************** ORG KEY MATRIX QUESTIONS *************/
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (5, 'Is it my organization?', 0);

--changeSet 0utl_perm_question:6 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (6, 'Is it my company?', 0);

--changeSet 0utl_perm_question:7 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (7, 'Is it my sub-organization?', 0);

--changeSet 0utl_perm_question:8 stripComments:false
INSERT INTO UTL_PERM_QUESTION (QUESTION_ID, QUESTION_SDESC, UTL_ID)
VALUES (8, 'Do I work for an Admin Organization?', 0);