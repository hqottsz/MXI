--liquibase formatted sql

-- ****************************************************
-- * 0-Level INSERT SCRIPT FOR ref_sensitivity
-- ****************************************************
--changeSet 0ref_sensitivity:1 stripComments:false
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('CAT_III', 'CAT III', 'Category III Instrument Landing System', 10, 'CAT III Sensitive - Refer to MPM for Details', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI');

--changeSet 0ref_sensitivity:2 stripComments:false
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('ETOPS', 'ETOPS', 'Extended Operations', 20, 'ETOPS Sensitive - Refer to MPM for Details', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI');

--changeSet 0ref_sensitivity:3 stripComments:false
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('RII', 'RII', 'Required Inspection Item', 30, 'RII Sensitive - Refer to MPM for Details', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI');

--changeSet 0ref_sensitivity:4 stripComments:false
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('FCBS', 'FCBS', 'Fatigue Critical Baseline Structure', 40, 'FCBS Sensitive - Refer to MPM for Details', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI');

--changeSet 0ref_sensitivity:5 stripComments:false
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool, rstat_cd, ctrl_db_id, creation_dt, creation_db_id, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('RVSM', 'RVSM', 'Reduced Vertical Separation Minimum', 50, 'RVSM Sensitive - Refer to MPM for Details', 0, 0, 0, sysdate, 0, 1, sysdate, 0, 'MXI');
