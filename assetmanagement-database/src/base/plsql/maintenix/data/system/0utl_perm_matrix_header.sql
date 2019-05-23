--liquibase formatted sql


--changeSet 0utl_perm_matrix_header:1 stripComments:false
-- USER KEY MATRIX DEFINITION
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MATRIX_HEADER"
** 0-Level
** DATE: 3-JUN-08
*********************************************/
INSERT INTO UTL_PERM_MATRIX_HEADER (MATRIX_ID, ANSWER_FUNCTION_NAME, MATRIX_SDESC, MATRIX_LDESC, UTL_ID)
VALUES (1, 'getUserKeyPermissions', 'User Key Matrix', 'Permission Matrix for User Key', 0);

--changeSet 0utl_perm_matrix_header:2 stripComments:false
-- ORG KEY MATRIX DEFINITION
INSERT INTO UTL_PERM_MATRIX_HEADER (MATRIX_ID, ANSWER_FUNCTION_NAME, MATRIX_SDESC, MATRIX_LDESC, UTL_ID)
VALUES (2, 'getOrgKeyPermissions', 'Org Key Matrix', 'Permission Matrix for Organization Key', 0);

--changeSet 0utl_perm_matrix_header:3 stripComments:false
-- ORG KEY PICKER MATRIX DEFINITION - VIRTUAL KEY MATRIX - USED FOR ALTERNATIVE PERMISSION MATRICES
INSERT INTO UTL_PERM_MATRIX_HEADER (MATRIX_ID, ANSWER_FUNCTION_NAME, MATRIX_SDESC, MATRIX_LDESC, UTL_ID)
VALUES (3, 'getOrgKeyPermissions', 'Org Key Picker Matrix', 'Permission Matrix for Organization Key', 0);