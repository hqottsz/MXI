--liquibase formatted sql


--changeSet 0ref_domain_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DOMAIN_TYPE"
** 0-Level
** DATE: 01-MAR-04 TIME: 16:10:53
*********************************************/
insert into ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'US', 'Numeric Usage Parameter', 'Numeric Usage Parameter (accrues)',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_domain_type:2 stripComments:false
insert into ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CA', 'Calendar Parameter', 'Calendar Parameter',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_domain_type:3 stripComments:false
-- Numeric type measurement
insert into ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'ME', 'Measurement', 'Real values (that are not usage values and do not accrue)',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_domain_type:4 stripComments:false
-- Character type measurement
insert into ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CH', 'Characteristic', 'String based domain type',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_domain_type:5 stripComments:false
-- Calendar type measurement
insert into ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CME', 'Calendar Measurement', 'Calendar based domain type',  0, '15-JUL-05', '15-JUL-05', 100, 'MXI');

--changeSet 0ref_domain_type:6 stripComments:false
-- Free Form Text
INSERT INTO ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'TEXT', 'Free Form Text', 'Free Form Text',  0, '15-JAN-08', '15-JAN-08', 100, 'MXI');

--changeSet 0ref_domain_type:7 stripComments:false
-- Check boxes
INSERT INTO ref_domain_type (domain_type_db_id, domain_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'CHK', 'Check Boxes', 'CheckBoxes',  0, '15-JAN-08', '15-JAN-08', 100, 'MXI');