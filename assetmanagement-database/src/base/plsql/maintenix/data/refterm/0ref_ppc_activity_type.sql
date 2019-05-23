--liquibase formatted sql


--changeSet 0ref_ppc_activity_type:1 stripComments:false
/*****************************************************
** INSERT SCRIPT FOR TABLE "REF_PPC_ACTIVITY_TYPE"
** 0-Level
** DATE: 10-Nov-10
******************************************************/
insert into REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'MILESTONE', 'MILESTONE', 'Milestone activity type.', 0, '10-NOV-10', '10-NOV-10', 0, 'MXI');

--changeSet 0ref_ppc_activity_type:2 stripComments:false
insert into REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'PHASE', 'PHASE', 'Phase activity type.', 0, '10-NOV-10', '10-NOV-10', 0, 'MXI');

--changeSet 0ref_ppc_activity_type:3 stripComments:false
insert into REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'TASK', 'TASK', 'Task activity type.', 0, '10-NOV-10', '10-NOV-10', 0, 'MXI');

--changeSet 0ref_ppc_activity_type:4 stripComments:false
insert into REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'WORKAREA', 'WORKAREA', 'Work area activity type.', 0, '10-NOV-10', '10-NOV-10', 0, 'MXI');

--changeSet 0ref_ppc_activity_type:5 stripComments:false
insert into REF_PPC_ACTIVITY_TYPE (PPC_ACTIVITY_TYPE_DB_ID, PPC_ACTIVITY_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values (0, 'MPC_TMPL_TASK', 'MPC_TMPL_TASK', 'MPC Template task activity type.', 0, '30-MAR-17', '30-MAR-17', 0, 'MXI');