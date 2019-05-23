--liquibase formatted sql


--changeSet 0ref_financial_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FINANCIAL_CLASS"
** 0-Level
*********************************************/
insert into REF_FINANCIAL_CLASS(FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'KIT', 'KIT', 'Maintenance Kit', 0, 1, 0, 'KIT', 0, '17-JUN-09', '17-JUN-09', 0, 'MXI');

--changeSet 0ref_financial_class:2 stripComments:false
insert into REF_FINANCIAL_CLASS (FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'N/A', 'N/A', 0, 1, 0, 'BLKOUT', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');