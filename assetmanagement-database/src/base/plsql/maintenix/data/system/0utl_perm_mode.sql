--liquibase formatted sql


--changeSet 0utl_perm_mode:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "UTL_PERM_MODE"
** 0-Level
** DATE: 03-JUNE-2008 TIME: 11:15:54
*********************************************/
INSERT INTO UTL_PERM_MODE (MODE_CD, MODE_SDESC, UTL_ID)
VALUES ('NONE', 'No Permissions', 0);

--changeSet 0utl_perm_mode:2 stripComments:false
INSERT INTO UTL_PERM_MODE (MODE_CD, MODE_SDESC, UTL_ID)
VALUES ('READ', 'Read Permissions', 0);

--changeSet 0utl_perm_mode:3 stripComments:false
INSERT INTO UTL_PERM_MODE (MODE_CD, MODE_SDESC, UTL_ID)
VALUES ('EDIT', 'Edit Permission - full access', 0);