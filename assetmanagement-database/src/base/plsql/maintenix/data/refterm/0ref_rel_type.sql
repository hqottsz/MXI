--liquibase formatted sql


--changeSet 0ref_rel_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REL_TYPE"
** 0-Level
** DATE: 03-SEP-03
*********************************************/
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'DISCF', 'Discovered Fault', 'task uncovers component fault',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:2 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'DEPT', 'Dependency Created Task', 'task creates dependent task',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:3 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'CORRECT', 'Corrective Task', 'component fault creates corrective task',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:4 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RESLF', 'Resolution Fault', 'fault leads to fault',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:5 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RREQ', 'Raised Material Request', 'task generates material request',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:6 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RECUR', 'Recurring Fault', 'fault relates to another fault',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:7 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'STRTSTRT', 'Start-Start Constraint', 'NULL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:8 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'ENDSTRT', 'End-Start Constraint', 'NULL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:9 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'TTFG', 'Configuration Change', 'task causes configuration change',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rel_type:10 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RECSRC', 'Recurring Source', 'Recurring Source',  0, '18-SEP-02', '18-SEP-02', 100, 'MXI');

--changeSet 0ref_rel_type:11 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'DRVTASK', 'Driving Task', 'Relationship between a Check/Work Order to a driving task.',  0, '18-SEP-02', '18-SEP-02', 100, 'MXI');

--changeSet 0ref_rel_type:12 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'WORMVL', 'Work Order Removal ', 'Replacement task link to Work Order',  0, '18-SEP-02', '18-SEP-02', 100, 'MXI');

--changeSet 0ref_rel_type:13 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'TCO', 'Tool Check Out', 'Relationship between a checkout event and the task for which the tool was checked out.',  0, '17-JAN-06', '17-JAN-06', 100, 'MXI');

--changeSet 0ref_rel_type:14 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RELMAINT', 'Related Maintenance Event', 'Related Maintenance Event',  0, '09-JAN-08', '09-JAN-08', 100, 'MXI');

--changeSet 0ref_rel_type:15 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'TRNSFER', 'Related Transfer Event', 'Related Transfer Event',  0, '11-AUG-10', '11-AUG-10', 100, 'MXI');

--changeSet 0ref_rel_type:16 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'FAULTREL', 'The relationships between a fault and all its related tasks.', 
'When a fault is raised, it may either be deferred (with repeating tasks) or completed (triggering changes to the inspection and repair program). \
To provide tractability, it is important to show a relationship between the originating fault and all related tasks. \
This includes recurring tasks created upon the deferral, and tasks created when the fault has a requirement sub-task \
that has CREATE links to other tasks, and when the tasks created by the CREATE link have CREATE links to other tasks. \
This relationship does not show sub-tasks of the fault (either ad-hoc or baselined) that were completed during the execution \
of the fault. This relationship also does not show child tasks of any related task if a requirement is linked via a CREATE link, \
and the requirement has child job cards, the child job cards will not appear. This relationship is created when the related task is created.',  
0, TO_DATE('2018-04-06','YYYY-MM-DD'), TO_DATE('2018-04-06','YYYY-MM-DD'), 100, 'MXI');

--changeSet 0ref_rel_type:17 stripComments:false
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES (0, 'RECSRCRP', 'Replaced repetitive task.', 'The relationship between a fault and repetitive task when that task has been replaced by another repetitive task.', 
   0, TO_DATE('2018-05-30','YYYY-MM-DD'), TO_DATE('2018-05-30','YYYY-MM-DD'), 100, 'MXI');
