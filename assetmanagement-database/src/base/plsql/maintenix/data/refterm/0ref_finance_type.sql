--liquibase formatted sql


--changeSet 0ref_finance_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FINANCE_TYPE"
** 0-Level
*********************************************/
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ROTABLE', 'ROTABLE', 'ROTABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_finance_type:2 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CONSUM', 'CONSUMABLE', 'CONSUMABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_finance_type:3 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EXPENSE', 'EXPENDABLE', 'EXPENDABLE', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_finance_type:4 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'KIT', 'KIT', 'KIT', 0, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_finance_type:5 stripComments:false
insert into REF_FINANCE_TYPE (FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');