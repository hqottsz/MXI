--liquibase formatted sql


--changeSet 0ref_labour_role_type:1 stripComments:false
/************************************************
** INSERT SCRIPT FOR TABLE "REF_LABOUR_ROLE_TYPE"
** 0-Level
** DATE: 31-AUG-09
*************************************************/
INSERT INTO REF_LABOUR_ROLE_TYPE( LABOUR_ROLE_TYPE_DB_ID, LABOUR_ROLE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'TECH', 'Technician', 'This labor role represents the technician who performed the work', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );

--changeSet 0ref_labour_role_type:2 stripComments:false
INSERT INTO REF_LABOUR_ROLE_TYPE( LABOUR_ROLE_TYPE_DB_ID, LABOUR_ROLE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'CERT', 'Certifier', 'This labor role represents the certifier who performed certification of the work', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );

--changeSet 0ref_labour_role_type:3 stripComments:false
INSERT INTO REF_LABOUR_ROLE_TYPE( LABOUR_ROLE_TYPE_DB_ID, LABOUR_ROLE_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'INSP', 'Inspector', 'This labor role represents the inspector who performed the independently inspection of the work', 0, '08-SEP-09', '08-SEP-09', 100, 'MXI' );