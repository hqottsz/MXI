--liquibase formatted sql


--changeSet 0ref_log_action:1 stripComments:false
-- -- -- --  Part Group Codes -- -- -- --
/********************************************
** INSERT SCRIPT FOR TABLE "REF_LOG_ACTION"
** 0-Level
** DATE: 16-APR-09 TIME: 16:24:40
*********************************************/
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPUNASSN','Unassign Part No','Unassign Part No','BPUNASSN', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:2 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPAPPR','Approve Alternate Part','Approve Alternate Part','BPAPPR', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:3 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPUNAPPR','Unapprove Alternate Part','Unapprove Alternate Part','BPUNAPPR', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:4 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BPADDPARTINCOMP', 'Add Part Incompatibility', 'Add Part Incompatibility','BPADDPARTINCOMP', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:5 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BPRMPARTINCOMP','Remove Part Incompatibility','Remove Part Incompatibility','BPRMPARTINCOMP', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:6 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BPADDTASKINCOMP','Add Task Incompatibility','Add Task Incompatibility','BPADDTASKINCOMP', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:7 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BPRMTASKINCOMP','Remove Task Incompatibility','Remove Task Incompatibility','BPRMTASKINCOMP', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:8 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPEDIT','Edit Part Group','Edit Part Group','BPEDIT', 0 , '17-APR-09' , '17-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:9 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPEDITPARTS','Edit Assigned Part Details','Edit Assigned Part Details','BPEDITPARTS', 0 , '17-APR-09' , '17-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:10 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'BPASSN','Assign Part No','Assign Part No','BPASSN', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:11 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CHANGEINVCLASS','Change Inventory Class.','Change Inventory Class.','CHANGEINVCLASS', 0 , '13-MAY-09' , '13-MAY-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:12 stripComments:false
-- -- -- --  Part Codes -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSEDIT','Edit Part','Edit Part','PSEDIT', 0 , '29-MAY-09' , '29-MAY-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:13 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSAPPROVE','Approve Part','Approve Part','PSAPPROVE', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:14 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSCREATE','Create Part','Create Part','PSCREATE', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:15 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSREJECT','Reject Part','Reject Part','PSREJECT', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:16 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSMIGR','Migration','Migration','PSMIGR', 0 , '22-APR-09' , '22-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:17 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PPAAUP','Average Unit Price Adjustment', 'The average unit price for this part has been adjusted','PPAAUP', 0 , TO_DATE('2012-11-02', 'YYYY-MM-DD'), TO_DATE('2012-11-02', 'YYYY-MM-DD'), 0 , 'MXI' );

--changeSet 0ref_log_action:18 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PAATS','Total Spares Adjustment', 'The total spares amount for this part has been adjusted.','PAATS', 0 , TO_DATE('2014-11-28', 'YYYY-MM-DD'), TO_DATE('2014-11-28', 'YYYY-MM-DD'), 0 , 'MXI' );

--changeSet 0ref_log_action:19 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PT','Part Type Change', 'Part Type Change', 'PTYPE', 0 , TO_DATE('2012-11-02', 'YYYY-MM-DD'), TO_DATE('2012-11-02', 'YYYY-MM-DD'), 0 , 'MXI' );

--changeSet 0ref_log_action:20 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSCHANGEFINCLASS','Part Financial Class Change', 'Part Financial Class Change', 'PSCHANGEFINCLASS', 0 , TO_DATE('2016-07-19', 'YYYY-MM-DD'), TO_DATE('2016-07-19', 'YYYY-MM-DD'), 0 , 'MXI' );

--changeSet 0ref_log_action:21 stripComments:false
-- -- -- --  Kit Codes -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSKITEDIT','Edit Kit Contents','Edit Kit Contents','PSKITEDIT', 0 , '30-JUL-09' , '30-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:22 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSKITASSN','Assign Part No from Part Group to Kit','Assign Part No from Part Group to Kit','PSKITASSN', 0 , '30-JUL-09' , '30-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:23 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'PSKITUNASSN','Unassgin Kit Content Part No from Part Group','Unassgin Kit Content Part No from Part Group','PSKITUNASSN', 0 , '30-JUL-09' , '30-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:24 stripComments:false
-- -- -- --  Config Slot Codes -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSDELETE','Delete Config Slot','Delete Config Slot','CSDELETE', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:25 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSCHGPARENT','Change Parent Config Slot','Change Parent Config Slot','CSCHGPARENT', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:26 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSEDIT' , 'Edit Config Slot' , 'Edit Config Slot' , 'CSEDIT', 0, '16-APR-09', '16-APR-09', 0, 'MXI' );

--changeSet 0ref_log_action:27 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSBPCREATE' ,  'Create Part Group' , 'Create Part Group' , 'CSBPCREATE', 0, '16-APR-09', '16-APR-09', 0, 'MXI' );

--changeSet 0ref_log_action:28 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSBPMOVE',  'Move Part Group','Move Part Group', 'CSBPMOVE', 0, '16-APR-09', '16-APR-09', 0, 'MXI' );

--changeSet 0ref_log_action:29 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSADDPOS', 'Add Positions', 'Add Positions', 'CSADDPOS', 0, '16-APR-09', '16-APR-09', 0, 'MXI' );

--changeSet 0ref_log_action:30 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CSRMVPOS', 'Remove Position','Remove Position', 'CSRMVPOS', 0, '16-APR-09', '16-APR-09', 0, 'MXI' );

--changeSet 0ref_log_action:31 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSEDITPOS', 'Edit Positions','Edit Positions','CSEDITPOS', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:32 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSTRKTOSER','Change To Serialized','Change To Serialized','CSTRKTOSER', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:33 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSASSNPARM','Assign Parameter','Assign Parameter','CSASSNPARM', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:34 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSUNASSNPARM','Unassign Parameter','Unassign Parameter','CSUNASSNPARM', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:35 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CSCREATE','Create Subconfig Slot','Create Subconfig Slot','CSCREATE', 0 , '16-APR-09' , '16-APR-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:36 stripComments:false
-- -- -- --  Task Definition Codes -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDPREVENTEXE','Prevent Execution','Prevent Execution','TDPREVENTEXE', 0 , '07-JUL-09' , '07-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:37 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDALLOWEXE','Allow Execution','Allow Execution','TDALLOWEXE', 0 , '07-JUL-09' , '07-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:38 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDLOCK','Lock Task Definition','Lock Task Definition','TDLOCK', 0 , '10-JUL-09' , '10-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:39 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDUNLOCK','Unlock Task Definition','Unlock Task Definition','TDUNLOCK', 0 , '10-JUL-09' , '10-JUL-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:40 stripComments:false
-- -- -- - Oil Consumption -- -- -- -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'OILSTATUS','Engine/APU oil consumption rate status change','Engine/APU oil consumption rate status change','OILSTATUS', 0 , '30-OCT-09' , '30-OCT-09' , 0 , 'MXI' );

--changeSet 0ref_log_action:41 stripComments:false
-- -- -- - Enforce Workscope Order -- -- -- -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDTOGGLEON','Toggle Workscope Order Set to TRUE','Toggle Workscope Order Set to TRUE','TDTOGGLEON', 0 , '10-MAR-10' , '10-MAR-10' , 0 , 'MXI' );

--changeSet 0ref_log_action:42 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDTOGGLEOFF','Toggle Workscope Order Set to FALSE','Toggle Workscope Order Set to FALSE','TDTOGGLEOFF', 0 , '10-MAR-10' , '10-MAR-10' , 0 , 'MXI' );

--changeSet 0ref_log_action:43 stripComments:false
-- -- -- - Maint Prgm -- -- -- -- -- -- --
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'MP_LOCK','Lock Maintenance Program','Lock Maintenance Program','MP_LOCK', 0 , '23-JUN-10' , '23-JUN-10' , 0 , 'MXI' );

--changeSet 0ref_log_action:44 stripComments:false
insert into REF_LOG_ACTION(LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'MP_UNLOCK','Unlock Maintenance Program','Unlock Maintenance Program','MP_UNLOCK', 0 , '23-JUN-10' , '23-JUN-10' , 0 , 'MXI' );

--changeSet 0ref_log_action:45 stripComments:false
INSERT INTO ref_log_action(log_action_db_id, log_action_cd,desc_sdesc,desc_ldesc,user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0,'CSCONFIGSS','Configure Sensitive Systems','Config slot configure sensitive systems','CSCONFIGSS', 0 , '07-NOV-17' , '07-NOV-17' , 0 , 'MXI' );

--changeSet 0ref_log_action:46 stripComments:false
INSERT INTO ref_log_action(log_action_db_id, log_action_cd,desc_sdesc,desc_ldesc,user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0,'CSPROPAGATESS','Propagate Sensitive Systems','Config slot sensitive system propagation to sub config slots','CSPROPAGATESS', 0 , '07-NOV-17' , '07-NOV-17' , 0 , 'MXI' );

--changeSet 0ref_log_action:47 stripComments:false
INSERT INTO ref_log_action(log_action_db_id, log_action_cd,desc_sdesc,desc_ldesc,user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0,'TDMANUALPREVENT','Prevent manual initialization of task definitions.','Prevent manual initialization of task definitions.','TDMANUALPREVENT', 0, TO_DATE('2018-05-08', 'YYYY-MM-DD'), TO_DATE('2018-05-08', 'YYYY-MM-DD'), 0, 'MXI');

--changeSet 0ref_log_action:48 stripComments:false
INSERT INTO ref_log_action(log_action_db_id, log_action_cd,desc_sdesc,desc_ldesc,user_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0,'TDMANUALALLOW','Allow manual initialization of task definitions.','Allow manual initialization of task definitions.','TDMANUALALLOW', 0, TO_DATE('2018-05-08', 'YYYY-MM-DD'), TO_DATE('2018-05-08', 'YYYY-MM-DD'), 0, 'MXI');