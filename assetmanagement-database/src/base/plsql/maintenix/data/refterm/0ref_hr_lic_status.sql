--liquibase formatted sql


--changeSet 0ref_hr_lic_status:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_HR_LIC_STATUS"
** 0-Level
** DATE: 17-JAN-2008 TIME: 15:43:00
*********************************************/
insert into ref_hr_lic_status (HR_LIC_STATUS_DB_ID, HR_LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ACTV', 'ACTV', 'Active', 'The user license is active.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');

--changeSet 0ref_hr_lic_status:2 stripComments:false
insert into ref_hr_lic_status (HR_LIC_STATUS_DB_ID, HR_LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'SUSPEND', 'SUSPEND', 'Suspend', 'The user license is suspended.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');

--changeSet 0ref_hr_lic_status:3 stripComments:false
insert into ref_hr_lic_status (HR_LIC_STATUS_DB_ID, HR_LIC_STATUS_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'INVALID', 'INVALID', 'Invalid', 'The user license is invalid.', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');