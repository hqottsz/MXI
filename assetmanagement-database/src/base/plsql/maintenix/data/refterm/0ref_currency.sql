--liquibase formatted sql


--changeSet 0ref_currency:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_XACTION_TYPE"
** 0-Level
** DATE: 03-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into REF_CURRENCY (CURRENCY_DB_ID, CURRENCY_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, DEFAULT_BOOL, EXCHG_QT, SPEC2K_MULT_QT, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLK', 0, 1, 'N/A', null, 0, 1, 1, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');