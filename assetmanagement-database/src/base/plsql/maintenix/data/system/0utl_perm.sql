--liquibase formatted sql


--changeSet 0utl_perm:1 stripComments:false
-- USER KEY MATRIX
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
INSERT INTO UTL_PERM (MATRIX_ID, CLASS_NAME, UTL_ID)
VALUES (1, 'com.mxi.mx.core.key.UserKey', 0);

--changeSet 0utl_perm:2 stripComments:false
-- ORG KEY MATRIX 
INSERT INTO UTL_PERM (MATRIX_ID, CLASS_NAME, UTL_ID)
VALUES (2, 'com.mxi.mx.core.key.OrgKey', 0);

--changeSet 0utl_perm:3 stripComments:false
-- ORG PICKER KEY MATRIX 
INSERT INTO UTL_PERM (MATRIX_ID, CLASS_NAME, UTL_ID)
VALUES (3, 'com.mxi.mx.core.key.OrgPickerKey', 0);