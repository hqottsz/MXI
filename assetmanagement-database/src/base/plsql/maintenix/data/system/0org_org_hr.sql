--liquibase formatted sql


--changeSet 0org_org_hr:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ORG_ORG_HR"
** 0-Level
** DATE: 06-MAY-08
*********************************************/
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 3, 0, 1, 1, 0, '06-MAY-08', '06-MAY-08', 0, 'MXI');

--changeSet 0org_org_hr:2 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 2, 0, 1, 1, 0, '06-MAY-08', '06-MAY-08', 0, 'MXI');

--changeSet 0org_org_hr:3 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 4, 0, 1, 1, 0, '15-JUL-08', '15-JUL-08', 0, 'MXI');

--changeSet 0org_org_hr:4 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 5, 0, 1, 1, 0, '28-AUG-09', '28-AUG-09', 0, 'MXI');

--changeSet 0org_org_hr:5 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 6, 0, 1, 1, 0, '28-AUG-09', '28-AUG-09', 0, 'MXI');

--changeSet 0org_org_hr:6 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 10, 0, 1, 1, 0, '28-AUG-09', '28-AUG-09', 0, 'MXI');

--changeSet 0org_org_hr:7 stripComments:false
INSERT INTO org_org_hr(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
            VALUES    ( 0, 11, 0, 1, 1, 0, to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');