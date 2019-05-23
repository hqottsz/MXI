--liquibase formatted sql


--changeSet 0utl_perm_answer_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_ANSWER_TYPE"
** 0-Level
** DATE: 03-JUNE-2008 TIME: 11:15:54
*********************************************/
INSERT INTO UTL_PERM_ANSWER_TYPE (ANSWER_TYPE_CD, ANSWER_TYPE_SDESC, ANSWER_TYPE_VALUE, UTL_ID)
VALUES ('NO', 'No', 0, 0);

--changeSet 0utl_perm_answer_type:2 stripComments:false
INSERT INTO UTL_PERM_ANSWER_TYPE (ANSWER_TYPE_CD, ANSWER_TYPE_SDESC, ANSWER_TYPE_VALUE, UTL_ID)
VALUES ('YES', 'Yes', 1, 0);

--changeSet 0utl_perm_answer_type:3 stripComments:false
INSERT INTO UTL_PERM_ANSWER_TYPE (ANSWER_TYPE_CD, ANSWER_TYPE_SDESC, ANSWER_TYPE_VALUE, UTL_ID)
VALUES ('*', 'Don''t Care', 0, 0);