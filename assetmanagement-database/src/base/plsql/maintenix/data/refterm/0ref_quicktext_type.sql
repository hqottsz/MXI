--liquibase formatted sql


--changeSet 0ref_quicktext_type:1 stripComments:false
-- QuickText Type References
/********************************************
** INSERT SCRIPT FOR TABLE "REF_QUICKTEXT_TYPE"
** 0-Level
*********************************************/
INSERT INTO ref_quicktext_type (quicktext_type_cd, desc_sdesc, desc_ldesc, rstat_cd, ctrl_db_id, creation_dt, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('MX_ENG_STEPDESC', 'Engineering Step Description', 'Step descriptions that can be used to quickly add steps to tasks.', 0, 0, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 1, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 0, 'MXI');

--changeSet 0ref_quicktext_type:2 stripComments:false
INSERT INTO ref_quicktext_type (quicktext_type_cd, desc_sdesc, desc_ldesc, rstat_cd, ctrl_db_id, creation_dt, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('MX_MTC_ACTION', 'Maintenance Action', 'Action notes that can be used to quickly add actions to work capture.', 0, 0, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 1, TO_DATE('2018-06-13', 'YYYY-MM-DD'), 0, 'MXI');

--changeSet 0ref_quicktext_type:3 stripComments:false
INSERT INTO ref_quicktext_type (quicktext_type_cd, desc_sdesc, desc_ldesc, rstat_cd, ctrl_db_id, creation_dt, revision_no, revision_dt, revision_db_id, revision_user)
VALUES ('MX_MTC_CERT_INSP', 'Certification/Inspection Action', 'Certification and inspection action notes that can be used to quickly add actions to work capture.', 0, 0, TO_DATE('2018-07-03', 'YYYY-MM-DD'), 1, TO_DATE('2018-07-03', 'YYYY-MM-DD'), 0, 'MXI');
