--liquibase formatted sql


--changeSet MX-18012:1 stripComments:false
INSERT INTO UTL_ASYNC_ACTION_TYPE ( ASYNC_ACTION_TYPE_CD, USER_TYPE_CD, DESC_SDESC, DESC_LDESC, REF_NAME, METHOD_NAME, UTL_ID )
   SELECT 'SILENT_PRINT', 'SILENT_PRINT', 'Prints the Issue Ticket for a Part Request', '', 'com.mxi.mx.core.ejb.PartRequest', 'performSilentPrint', 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM UTL_ASYNC_ACTION_TYPE WHERE ASYNC_ACTION_TYPE_CD = 'SILENT_PRINT' );      