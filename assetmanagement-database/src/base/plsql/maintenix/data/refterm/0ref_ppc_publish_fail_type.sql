--liquibase formatted sql


--changeSet 0ref_ppc_publish_fail_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PPC_PUBLISH_FAIL_TYPE"
** 0-Level
*********************************************/
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'WPHIST', 'WPHIST', 'Work Package is historic.', 0, 1, 0, '17-OCT-09', '17-OCT-09', 0, 'MXI');

--changeSet 0ref_ppc_publish_fail_type:2 stripComments:false
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'TASKHIST', 'TASKHIST', 'Task is historic.', 0, 1, 0, '17-OCT-09', '17-OCT-09', 0, 'MXI');

--changeSet 0ref_ppc_publish_fail_type:3 stripComments:false
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'TASKUNEX', 'TASKUNEX', 'Task has unexpected status.', 0, 1, 0, '17-OCT-09', '17-OCT-09', 0, 'MXI');

--changeSet 0ref_ppc_publish_fail_type:4 stripComments:false
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'TASKOOS', 'TASKOOS', 'Task is out of the scope of the work package.', 0, 1, 0, '17-OCT-09', '17-OCT-09', 0, 'MXI');

--changeSet 0ref_ppc_publish_fail_type:5 stripComments:false
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'LBRHR', 'LBRHR', 'Fail to set HR of Labor Role.', 0, 1, 0, '09-JUN-10', '09-JUN-10', 0, 'MXI');

--changeSet 0ref_ppc_publish_fail_type:6 stripComments:false
insert into REF_PPC_PUBLISH_FAIL_TYPE(PPC_PUBLISH_FAIL_TYPE_DB_ID, PPC_PUBLISH_FAIL_TYPE_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(0, 'TASKLRD', 'TASKLRD', 'Task labor role duration mismatch.', 0, 1, 0, '09-JUN-10', '09-JUN-10', 0, 'MXI');