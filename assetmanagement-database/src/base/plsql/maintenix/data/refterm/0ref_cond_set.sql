--liquibase formatted sql


--changeSet 0ref_cond_set:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_COND_SET"
** 0-Level
** DATE: 17-JUL-2008 TIME: 16:24:27
*********************************************/
INSERT INTO REF_COND_SET(COND_SET_DB_ID, COND_SET_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
VALUES(0,'N/A','Non-Applicable', 'Non-Applicable',0, '15-Jul-08', '15-Jul-08', 100, 'MXI');