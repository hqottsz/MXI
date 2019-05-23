--liquibase formatted sql


--changeSet 0_BLKOUT_eqp_manufact:1 stripComments:false
/************************************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE EQP_MANUFACT FOR BLACKOUT DATA ONLY**
*************************************************************************/
insert into EQP_MANUFACT (MANUFACT_DB_ID, MANUFACT_CD, MANUFACT_NAME, COUNTRY_DB_ID, COUNTRY_CD, STATE_CD, ADDRESS_PMAIL, CITY_NAME, ZIP_CD, PHONE_PH, FAX_PH, ADDRESS_EMAIL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'N/A', 'N/A', null, null, null, null, null, null, null, null, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');