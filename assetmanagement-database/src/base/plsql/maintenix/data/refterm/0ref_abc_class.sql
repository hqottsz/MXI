--liquibase formatted sql


--changeSet 0ref_abc_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_ABC_CLASS"
** DATE: 09/22/2004 TIME: 00:00:00
*********************************************/
insert into REF_ABC_CLASS (ABC_CLASS_DB_ID, ABC_CLASS_CD, DESC_SDESC, DESC_LDESC, VALUE_ORD, VALUE_PCT, COUNT_INTERVAL_MONTHS, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'A', 'A class', null, 1, 0.80, 3, null, null, 0, to_date('01-10-2004', 'dd-mm-yyyy'), to_date('06-06-2006', 'dd-mm-yyyy'), 0, 'mxi');

--changeSet 0ref_abc_class:2 stripComments:false
insert into REF_ABC_CLASS (ABC_CLASS_DB_ID, ABC_CLASS_CD, DESC_SDESC, DESC_LDESC, VALUE_ORD, VALUE_PCT, COUNT_INTERVAL_MONTHS, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'B', 'B class', null, 2, 0.15, 6, null, null, 0, to_date('01-10-2004', 'dd-mm-yyyy'), to_date('06-06-2006', 'dd-mm-yyyy'), 0, 'mxi');

--changeSet 0ref_abc_class:3 stripComments:false
insert into REF_ABC_CLASS (ABC_CLASS_DB_ID, ABC_CLASS_CD, DESC_SDESC, DESC_LDESC, VALUE_ORD, VALUE_PCT, COUNT_INTERVAL_MONTHS, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'C', 'C class', null, 3, 0.04, 12, null, null, 0, to_date('01-10-2004', 'dd-mm-yyyy'), to_date('06-06-2006', 'dd-mm-yyyy'), 0, 'mxi');

--changeSet 0ref_abc_class:4 stripComments:false
insert into REF_ABC_CLASS (ABC_CLASS_DB_ID, ABC_CLASS_CD, DESC_SDESC, DESC_LDESC, VALUE_ORD, VALUE_PCT, COUNT_INTERVAL_MONTHS, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'D', 'D class', null, 4, 0.01, 12, null, null, 0, to_date('06-06-2006', 'dd-mm-yyyy'), to_date('06-06-2006', 'dd-mm-yyyy'), 0, 'mxi');

--changeSet 0ref_abc_class:5 stripComments:false
insert into REF_ABC_CLASS (ABC_CLASS_DB_ID, ABC_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, VALUE_ORD, VALUE_PCT, COUNT_INTERVAL_MONTHS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'N/A', null, null, null, 0, 1, 12, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');