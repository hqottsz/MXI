--liquibase formatted sql


--changeSet 0ref_task_class:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_CLASS"
** 0-Level
*********************************************/
/** BLOCK Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool,  auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CHECK', 0, 79,  'Check', 'Collector for Aircraft (used to manage a grouping of task) and infers and aircraft release.', 0, 0, 0, 'BLOCK',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:2 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RO', 0, 81,  'Repair/Work Order', 'Collector for non-aircraft repair work (used to manage a grouping of task).' , 0, 0, 0,'BLOCK', 0, '04-SEP-01', '04-SEP-01', 100, 'MXI');

--changeSet 0ref_task_class:3 stripComments:false
/** REQ Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REPL', 0, 76,  'Replacement', 'This is a set of RMVL, INST and any ancilliary actions.', 1, 0, 1,'REQ',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:4 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MOD', 0, 78,  'Part Transformation', 'Due to maintenance action, part number has been changed', 1, 0, 1, 'REQ', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:5 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OVHL', 0, 78,  'Overhaul', 'Due to maintenance action, life limits reset',1, 0, 1,'REQ', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:6 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CORR', 0, 76,  'Corrective Action', 'Collector for all corrective actions associated with a fault',0, 1, 0, 'REQ',0, '04-SEP-01', '04-SEP-01', 100, 'MXI');

--changeSet 0ref_task_class:7 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DISCARD', 0, 46,  'Discard', 'Certificate of Destruction', 1, 0, 1, 'REQ',0, '26-APR-06', '26-APR-06', 100, 'MXI');

--changeSet 0ref_task_class:8 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REQ', 0, 120,  'Requirement', 'A requirement is a collection of granular maintenance items making up a maintenance action ', 1, 0, 1, 'REQ',0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:9 stripComments:false
/** JIC Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'INSP', 0, 77,  'Inspection', 'An inspection type task identifies preventive maintenance action.', 0, 1, 0, 'JIC', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:10 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'JIC', 0, 77,  'Job Instruction Card', 'Job Instruction Card.', 0, 1, 0, 'JIC', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_task_class:11 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OPENPANEL', 0, 77,  'Open Panel JIC', 'An open panel job card is used to specify instructions for opening a panel. Open panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, '26-JAN-10', '26-JAN-10', 100, 'MXI');

--changeSet 0ref_task_class:12 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CLOSEPANEL', 0, 77,  'Close Panel JIC', 'An close panel job card is used to specify instructions for closing a panel. Close panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, '26-JAN-10', '26-JAN-10', 100, 'MXI');

--changeSet 0ref_task_class:13 stripComments:false
/** Other Classifications **/
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ADHOC', 0, 77,  'Ad-Hoc Task', 'Ad-Hoc Task', 0, 1, 0, 'ADHOC', 0, 0, '14-SEP-07', '14-SEP-07', 100, 'MXI');

--changeSet 0ref_task_class:14 stripComments:false
-- MPC Classifications
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'MPC', 0, 77,  'Master Panel Card', 'Master Panel Card', 0, 1, 0, 'MPC', 0, 0, '22-FEB-08', '22-FEB-08', 100, 'MXI');

--changeSet 0ref_task_class:15 stripComments:false
-- Deployed Ops Blackout Data
insert into REF_TASK_CLASS (TASK_CLASS_DB_ID, TASK_CLASS_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, AUTO_COMPLETE_BOOL, UNIQUE_BOOL, WORKSCOPE_BOOL, CLASS_MODE_CD, NR_EST_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 'N/A', 'N/A', 0, 0, 0, 'ADHOC', 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_task_class:16 stripComments:false
-- operator OPER-86
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'AMP', 0, 1,  'Aircraft Maintenance Program', 'Aircraft Maintenance Program', 0, 0, 0, 'REF', 0, 0, '19-FEB-14', '19-FEB-14', 100, 'MXI');

--changeSet 0ref_task_class:17 stripComments:false
-- ADSB (modelled as Reference Documents)
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'AD', 0, 1,  'Airworthiness Directive', 'An airworthiness directive is a mandatory order issued by an air transportation regulatory body advising all concerned of an unsafe condition on an aircraft that jeopardizes the safety of the flying public and requires immediate attention.', 0, 0, 0, 'REF', 0, 0, '29-JUL-14', '29-JUL-14', 100, 'MXI');

--changeSet 0ref_task_class:18 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SB', 0, 1,  'Service Bulletin', 'A service bulletin is a manufacturer''s advisory bulletin that broadcasts alerts, warnings, and recommendations to their customers.', 0, 0, 0, 'REF', 0, 0, '29-JUL-14', '29-JUL-14', 100, 'MXI');

--changeSet 0ref_task_class:19 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, assignable_to_block_bool, assignable_to_maint_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FOLLOW', 0, 1, 'Follow-on Task', 'A follow-on repair task for allowable damage tracking', 0, 0, 0, 'REQ', 0, 0, 0, 0, TO_DATE('29-03-2018','DD-MM-YYYY'), TO_DATE('29-03-2018','DD-MM-YYYY'), 100, 'MXI');

--changeSet 0ref_task_class:20 stripComments:false
insert into ref_task_class(task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, nr_est_bool, assignable_to_block_bool, assignable_to_maint_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REPREF', 0, 1, 'Repair Reference', 'A task class used for all types of repairs (EA, SRM, etc.). Can only be selected when resolving a fault.', 0, 1, 1, 'REQ', 0, 0, 0, 0, TO_DATE('27-07-2018','DD-MM-YYYY'), TO_DATE('27-07-2018','DD-MM-YYYY'), 100, 'MXI');