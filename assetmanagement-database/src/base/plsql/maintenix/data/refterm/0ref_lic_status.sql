--liquibase formatted sql


--changeSet 0ref_lic_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_LIC_STATUS"
** 0-Level
** DATE: 17-JAN-2008 TIME: 15:43:00
*********************************************/
insert into ref_lic_status (LIC_STATUS_DB_ID, LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BUILD', 'BUILD', 'Build', 'License definition is new and in work.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');

--changeSet 0ref_lic_status:2 stripComments:false
insert into ref_lic_status (LIC_STATUS_DB_ID, LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ACTV', 'ACTV', 'Active', 'License definition is active.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');

--changeSet 0ref_lic_status:3 stripComments:false
insert into ref_lic_status (LIC_STATUS_DB_ID, LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'OBSLT', 'OBSLT', 'Obsolete', 'License definition is obsolete.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');