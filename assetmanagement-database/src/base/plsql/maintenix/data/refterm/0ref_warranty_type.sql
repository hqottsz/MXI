--liquibase formatted sql


--changeSet 0ref_warranty_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_WARRANTY_TYPE"
** 0-Level
** DATE: 29-APR-08
*********************************************/
INSERT INTO REF_WARRANTY_TYPE(WARRANTY_TYPE_DB_ID, WARRANTY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'ASSEMBLY', 'Assembly Warranty', 'Assembly Warranty', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_type:2 stripComments:false
INSERT INTO REF_WARRANTY_TYPE(WARRANTY_TYPE_DB_ID, WARRANTY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'COMPONENT', 'Component Warranty', 'Component Warranty', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');

--changeSet 0ref_warranty_type:3 stripComments:false
INSERT INTO REF_WARRANTY_TYPE(WARRANTY_TYPE_DB_ID, WARRANTY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'TASK', 'Task Warranty', 'Task Warranty', 0, '15-JULY-08', '15-JULY-08', 0, 'MXI');