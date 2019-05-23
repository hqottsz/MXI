--liquibase formatted sql


--changeSet 0ref_event_status:1 stripComments:false
-- Component Fault
/********************************************
** INSERT SCRIPT FOR TABLE "REF_EVENT_STATUS"
** 0-Level
** DATE: 20-MAY-05 TIME: 16:24:40
*********************************************/
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFCLOSE', 0, 'CF', 0, 80, 'Close', 'Fault is closed', 'CLOSE', '20', '0',  0, '22-FEB-08', '23-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:2 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFACTV', 0, 'CF', 0, 80, 'Active', 'Fault is active/outstanding', 'OPEN', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:3 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFPOSS', 0, 'CF', 0, 81, 'Possible', 'Message is a possible fault', 'POSSIBLE', '10', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:4 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFREJECT', 0, 'CF', 0, 86, 'Reject', 'Message was not a fault', 'REJECT', '15', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:5 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFCERT', 0, 'CF', 0, 83, 'Certified', 'Problem fix is certified based on engineering research/final disposition', 'CERTIFY', '35', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:6 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFNFF', 0, 'CF', 0, 82, 'No fault found', 'No fault found on equipment', 'NFF', '45', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:7 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFDEFER', 0, 'CF', 0, 84, 'Defer', 'Corrective action is deferred', 'DEFER', '25', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:8 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CFERR', 0, 'CF', 0, 86, 'Error', 'Error was found in record', 'ERROR', '95', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:9 stripComments:false
-- Shipment
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IXINTR', 0, 'IX', 0, 83, 'In Transit', 'Item Shipment In-Transit (likely only use this if the system is NOT using Material Requests)', 'IN-TRANS', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:10 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IXCMPLT', 0, 'IX', 0, 83, 'Received', 'Item Shipment Complete ', 'COMPLETE', '40', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:11 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IXCANCEL', 0, 'IX', 0, 80, 'Cancel', 'Cancel', 'CANCEL', '30', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:12 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IXPEND', 0, 'IX', 0, 79, 'Pending', 'Pending', 'PEND', '50', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:13 stripComments:false
-- Task
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACTV', 0, 'TS', 0, 80, 'Active', 'Task is active against asset and should be scheduled', 'ACTV', '10', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:14 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COMMIT', 0, 'TS', 0, 83, 'Commit', 'The amount of work to be done in a check/WO is set.', 'COMMIT', '25', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:15 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ASSIGN', 0, 'TS', 0, 131, 'Task Assigned', 'Task has been assigned for work', 'ASSIGN', '30', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:16 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UNASSIGN', 0, 'TS', 0, 84, 'Unassign', 'Task has been removed out of the check/WO workscope.', 'UNASSIGN', '35', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:17 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IN WORK', 0, 'TS', 0, 117, 'Task In Work', 'Task is presently in work', 'IN WORK', '40', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:18 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COMPLETE', 0, 'TS', 0, 83, 'Complete', 'Task was completed', 'COMPLETE', '50', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:19 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CANCEL', 0, 'TS', 0, 82, 'Cancelled', 'Task will never be completed (unnecessary)', 'CANCEL', '75', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:20 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ERROR', 0, 'TS', 0, 117, 'Error', 'Error was found in record', 'ERROR', '95', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:21 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DELAY', 0, 'TS', 0, 1, 'Delay', 'The Task has been delayed', 'DELAY', '105', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:22 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PAUSE', 0, 'TS', 0, 1, 'Pause', 'Pause', 'PAUSE', '107', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:23 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FORECAST', 0, 'TS', 0, 1, 'Forecast', 'Forecast', 'FORECAST', '109', '0',  0, '03-SEP-03','03-SEP-03', 100, 'MXI');

--changeSet 0ref_event_status:24 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'INSPREQ', 0, 'TS', 0, 83, 'Inspection Required', 'Check has been completed by maintenance and requires final inspection before the aircraft can be released.', 'INSPREQ', '106', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:25 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REJECT', 0, 'TS', 0, 145, 'Rejected', 'Task no completed acceptably.', 'REJECT', '101', '0',  0, '10-SEP-01', '10-SEP-01', 100, 'MXI');

--changeSet 0ref_event_status:26 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EDIT', 0, 'TS', 0, 6, 'Edited', 'The event has been edited after completion.', 'EDIT', '110', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:27 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'N/A', 0, 'TS', 0, 145, 'Non Applicable', 'The task is not applicable anymore', 'N/A', '108', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:28 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TERMINATE', 0, 'TS', 0, 82, 'Terminated', 'Task has been terminated', 'TERMINATE', '75', '0',  0, '22-FEB-08', '22-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:29 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LRP', 0, 'TS', 0, 80, 'LRP', 'Task only exists in Long Range Planner.', 'LRP', '111', '0',  0, '03-JUN-08', '03-JUN-08', 100, 'MXI');

--changeSet 0ref_event_status:30 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SETPLNDT', 0, 'TS', 0, 1, 'Set Plan By Date', 'Plan By Date set for the Task.', 'SETPLNDT', '50', '0',  0, '19-MAY-09', '19-MAY-09', 100, 'MXI');

--changeSet 0ref_event_status:31 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PREVENTEXE', 0, 'TS', 0, 117, 'Prevent Execution', 'Task execution is being prevented', 'PREVENT EXE', '85', '0',  0, '20-JAN-11', '20-JAN-11', 100, 'MXI');

--changeSet 0ref_event_status:32 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ALLOWEXE', 0, 'TS', 0, 80, 'Allow Execution', 'Task execution is being allowed', 'ALLOW EXE', '85', '0',  0, '20-JAN-11', '20-JAN-11', 100, 'MXI');

--changeSet 0ref_event_status:33 stripComments:false
-- Create Inventory
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRRFI', 0, 'ICR', 0, 80, 'Ready for Issue', 'Ready for Issue', 'RFI', '20', '0',  0, '02-FEB-15', '02-FEB-15', 100, 'MXI');

--changeSet 0ref_event_status:34 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRINSRV', 0, 'ICR', 0, 80, 'In Service', 'In Service', 'INSRV', '20', '0',  0, '30-APR-15', '30-APR-15', 100, 'MXI');

--changeSet 0ref_event_status:35 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRREPREQ', 0, 'ICR', 0, 80, 'Repair Required', 'Repair Required', 'REPREQ', '50', '0',  0, '02-FEB-15', '02-FEB-15', 100, 'MXI');

--changeSet 0ref_event_status:36 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRSCRAP', 0, 'ICR', 0, 80, 'Scrap Asset', 'Scrap Asset', 'SCRAP', '50', '0',  0, '30-APR-15', '30-APR-15', 100, 'MXI');

--changeSet 0ref_event_status:37 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRQUAR', 0, 'ICR', 0, 80, 'Quarantine', 'Quarantine', 'QUAR', '200', '0',  0, '10-MAR-15', '10-MAR-15', 100, 'MXI');

--changeSet 0ref_event_status:38 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRARCHIVE', 0, 'ICR', 0, 80, 'Archive', 'Archive', 'ARCHIVE', '200', '0',  0, '30-APR-15', '30-APR-15', 100, 'MXI');

--changeSet 0ref_event_status:39 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRINREP', 0, 'ICR', 0, 80, 'In Repair', 'In Repair', 'INREP', '200', '0',  0, '30-APR-15', '30-APR-15', 100, 'MXI');

--changeSet 0ref_event_status:40 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRINSPREQ', 0, 'ICR', 0, 80, 'Awaiting Inspection', 'Awaiting Inspection', 'INSPREQ', '200', '0',  0, '02-FEB-15', '02-FEB-15', 100, 'MXI');

--changeSet 0ref_event_status:41 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICRCONDEMN', 0, 'ICR', 0, 80, 'Condemned', 'Condemned', 'CONDEMN', '200', '0',  0, '30-APR-15', '30-APR-15', 100, 'MXI');

--changeSet 0ref_event_status:42 stripComments:false
-- Inventory Lock
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ILLOCK', 0, 'IL', 0, 144, 'Lock Inventory', 'Inventory has been locked.', 'LOCK', '100', '0',  0, '10-SEP-01', '10-SEP-01', 100, 'MXI');

--changeSet 0ref_event_status:43 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ILUNLOCK', 0, 'IL', 0, 145, 'Unlock Inventory', 'Inventory has been unlocked.', 'UNLOCK', '100', '0',  0, '10-SEP-01', '10-SEP-01', 100, 'MXI');

--changeSet 0ref_event_status:44 stripComments:false
-- Change Inventory Condition
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACRFI', 0, 'AC', 0, 80, 'Ready for Issue', 'Ready for Issue', 'RFI', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:45 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACINSRV', 0, 'AC', 0, 80, 'In Service', 'In Service', 'INSRV', '40', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:46 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACREPREQ', 0, 'AC', 0, 80, 'Repair Required', 'Repair Required', 'REPREQ', '50', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:47 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACQUAR', 0, 'AC', 0, 80, 'Quarantined', 'Quarantined', 'QUAR', '100', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:48 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACSCRAP', 0, 'AC', 0, 80, 'Scrap Asset', 'Scrap Asset', 'SCRAP', '120', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:49 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACARCHIVE', 0, 'AC', 0, 80, 'Archive', 'Archive', 'ARCHIVE', '190', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:50 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACINSPREQ', 0, 'AC', 0, 80, 'Awaiting Inspection', 'Awaiting Inspection', 'INSPREQ', '200', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:51 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACINREP', 0, 'AC', 0, 80, 'In Repair', 'In Repair', 'INREP', '210', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:52 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ACCONDEMN', 0, 'AC', 0, 80, 'Condemned', 'Condemned', 'CONDEMN', '220', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:53 stripComments:false
-- Change Inventory Details
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IC', 0, 'IC', 0, 1, 'Inventory Details Changed', 'Inventory Details Changed', 'MODIFIED', '10', 0,  0, '10-MAY-06', '10-MAY-06', 100, 'MXI');

--changeSet 0ref_event_status:54 stripComments:false
-- Change Inventory Custody
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICCR', 0, 'ICC', 0, 1, 'Inventory Custody Change', 'Inventory Custody Change', 'MODIFIED', '10', 0,  0, '24-MAY-06', '24-MAY-06', 100, 'MXI');

--changeSet 0ref_event_status:55 stripComments:false
-- Change Aircraft Operating Status
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CONORM', 0, 'CO', 0, 80, 'Normal', 'Normal', 'NORM', '10', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:56 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COAWR', 0, 'CO', 0, 80, 'Awaiting Release', 'Awaiting Release', 'AWR', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:57 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COAOG', 0, 'CO', 0, 80, 'Aicraft On Ground', 'Aicraft On Ground', 'AOG', '30', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:58 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COINM', 0, 'CO', 0, 80, 'In Maintenance', 'In Maintenance', 'INM', '40', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:59 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COOOS', 0, 'CO', 0, 80, 'Out Of Service', 'Out Of Service', 'OOS', '50', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:60 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COOPEN', 0, 'CO', 0, 80, 'Open Faults', 'Aircraft has open faults.', 'OPEN', '80', '0',  0, '13-NOV-01', '13-NOV-01', 100, 'MXI');

--changeSet 0ref_event_status:61 stripComments:false
-- Usage Record
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'UCCOMPLETE', 0, 'UC', 0, 80, 'Complete', 'Usage Correction was Completed', 'COMPLETE', '10', '0',  0, '16-OCT-01', '16-OCT-01', 100, 'MXI');

--changeSet 0ref_event_status:62 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'UCERROR', 0, 'UC', 0, 80, 'Error', 'Error was found in the record', 'ERROR', '20', '0',  0, '16-OCT-01', '16-OCT-01', 100, 'MXI');

--changeSet 0ref_event_status:63 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'URCOMPLETE', 0, 'UR', 0, 80, 'Complete', 'Usage Completed', 'COMPLETE', '10', '0',  0, '16-OCT-01', '16-OCT-01', 100, 'MXI');

--changeSet 0ref_event_status:64 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'URERROR', 0, 'UR', 0, 80, 'Error', 'Error was found in the record', 'ERROR', '20', '0',  0, '16-OCT-01', '16-OCT-01', 100, 'MXI');

--changeSet 0ref_event_status:65 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'URPEND', 0, 'UR', 0, 80, 'Pend', 'usage record/correction is pending and has not been applied to the database', 'PEND', '30', '0',  0, '22-MAR-02', '22-MAR-02', 100, 'MXI');

--changeSet 0ref_event_status:66 stripComments:false
-- Usage Correction
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UCPEND', 0, 'UC', 0, 80, 'Pending', 'usage record/correction is pending and has not been applied to the database', 'PEND', '30', '0',  0, '22-MAR-02', '22-MAR-02', 100, 'MXI');

--changeSet 0ref_event_status:67 stripComments:false
-- Configuration Change
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FGINST', 0, 'FG', 0, 74, 'Installation', 'Installation', 'INST', '10', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:68 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FGRMVL', 0, 'FG', 0, 75, 'Removal', 'Removal', 'RMVL', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:69 stripComments:false
-- Part Edit
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PEGEN', 0, 'PE', 0, 80, 'Part Edit', 'The part has been edited', 'PEGEN', '10', '0',  0, '07-JUL-06', '07-JUL-06', 100, 'MXI');

--changeSet 0ref_event_status:70 stripComments:false
-- Inventory Part Number Change
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IPN', 0, 'IPN', 0, 80, 'Inventory Part Number Change', 'Inventory Part Number Change', 'IPN', '10', '0',  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_status:71 stripComments:false
-- Inventory Serial Number Change
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ISN', 0, 'ISN', 0, 80, 'Inventory Serial Number Change', 'Inventory Serial Number Change', 'ISN', '10', '0',  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_status:72 stripComments:false
-- Inventory Manufacture Date Change
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IMD', 0, 'IMD', 0, 80, 'Inventory Manufacture Date Change', 'Inventory Manufacture Date Change', 'IMD', '10', '0',  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_status:73 stripComments:false
-- Inventory Modification Status Note Change
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IMS', 0, 'IMS', 0, 1, 'Inventory Modification Status Note Change', 'Inventory Modification Status Note Change', 'IMS', '10', '0',  0, '12-JUL-07', '12-JUL-07', 100, 'MXI');

--changeSet 0ref_event_status:74 stripComments:false
-- Quantity Correction
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QCNEG', 0, 'QC', 0, 01, 'Negative adjustment', 'Negative quantity inventory adjustment', 'QCNEG', '10', '0',  0, '05-OCT-04', '05-OCT-04', 100, 'MXI');

--changeSet 0ref_event_status:75 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QCPOS', 0, 'QC', 0, 01, 'Positive adjustment', 'Positive quantity inventory adjustment', 'QCPOS', '10', '0',  0, '05-OCT-04', '05-OCT-04', 100, 'MXI');

--changeSet 0ref_event_status:76 stripComments:false
-- Transfer
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LXPEND', 0, 'LX', 0, 01, 'Pending', 'Pending', 'PEND', '10', '0',  0, '13-DEC-04', '13-DEC-04', 100, 'MXI');

--changeSet 0ref_event_status:77 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LXCANCEL', 0, 'LX', 0, 01, 'Cancelled', 'Cancelled', 'CANCEL', '10', '0',  0, '13-DEC-04', '13-DEC-04', 100, 'MXI');

--changeSet 0ref_event_status:78 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LXCMPLT', 0, 'LX', 0, 01, 'Complete', 'Complete', 'CMPLT', '10', '0',  0, '13-DEC-04', '13-DEC-04', 100, 'MXI');

--changeSet 0ref_event_status:79 stripComments:false
-- Part Request
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PROPEN', 0, 'PR', 0, 01, 'The part request has not been addressed yet.', 'The part request has not been addressed yet.', 'OPEN', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:80 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRAVAIL', 0, 'PR', 0, 01, 'An inventory has been reserved against the request and it is locally available.', 'An inventory has been reserved against the request and it is locally available', 'AVAIL', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:81 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRISSUED', 0, 'PR', 0, 01, 'Inventory has been issued against this part request and it is complete.', 'Inventory has been issued against this part request and it is complete.', 'ISSUED', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:82 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRPOREQ', 0, 'PR', 0, 01, 'A purchase request has been created to fill this part request.', 'A purchase request has been created to fill this part request.', 'POREQ', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:83 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRONORDER', 0, 'PR', 0, 01, 'A purchase order has been created, and the requested item is now on order.', 'A purchase order has been created, and the requested item is now on order.', 'ON ORDER', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:84 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRCANCEL', 0, 'PR', 0, 01, 'The request is no longer needed and has been cancelled.', 'The request is no longer needed, and has been cancelled.', 'CANCEL', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:85 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRCOMPLETE', 0, 'PR', 0, 01, 'The request has been completed and the part has been consumed.', 'The request has been completed and the part has been consumed.', 'COMPLETE', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:86 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRQUAR', 0, 'PR', 0, 01, 'The inventory has been reserved against the request but has been quarantined.', 'The inventory has been reserved against the request but has been quarantined.', 'QUAR', '10', '0',  0, '13-MAR-06', '13-MAR-06', 100, 'MXI');

--changeSet 0ref_event_status:87 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRINSPREQ', 0, 'PR', 0, 01, 'The inventory has been reserved against the request but requires inspection.', 'The inventory has been reserved against the request but requires inspection.', 'INSPREQ', '10', '0',  0, '13-MAR-06', '13-MAR-06', 100, 'MXI');

--changeSet 0ref_event_status:88 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRRESERVE', 0, 'PR', 0, 01, 'Inventory is reserved but not ready for pick up.', 'Inventory is reserved but not ready for pick up.', 'RESERVE', '10', '0',  0, '11-APR-06', '11-APR-06', 100, 'MXI');

--changeSet 0ref_event_status:89 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRREMOTE', 0, 'PR', 0, 01, 'Inventory is to be reserved against the request from a remote location.', 'Inventory is to be reserved against the request from a remote location.', 'REMOTE', '10', '0',  0, '15-JUNE-07', '15-JUNE-07', 100, 'MXI');

--changeSet 0ref_event_status:90 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRAWAITISSUE', 0, 'PR', 0, 01, 'Part request is waiting to be issued.', 'Part request is waiting to be issued.', 'AWAITING ISSUE', '10', '0',  0, '07-APR-2015', '07-APR-2015', 100, 'MXI');

--changeSet 0ref_event_status:91 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRSENT', 0, 'PR', 0, 01, 'Part request has been sent.', 'Part request has been sent', 'SENT', '10', '0',  0, '27-JUL-2015', '27-JUL-2015', 100, 'MXI');

--changeSet 0ref_event_status:92 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRACKNOWLEDGED', 0, 'PR', 0, 01, 'Part request has been acknowledged.', 'Part request has been acknowledged', 'ACKNOWLEDGED', '10', '0',  0, '04-AUG-2015', '04-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:93 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRREADYFORPICKUP', 0, 'PR', 0, 01, 'Part request is ready for pick up.', 'Part request is ready for pick up', 'READY TO PICK UP', '10', '0',  0, '14-AUG-2015', '14-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:94 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRNOTAVAILABLE', 0, 'PR', 0, 01, 'Part request is not available.', 'Part request is not available.', 'NOT AVAILABLE', '10', '0',  0, '17-AUG-2015', '17-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:95 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRINTRANSIT', 0, 'PR', 0, 01, 'Part request is in transit.', 'Part request is in transit', 'IN TRANSIT', '10', '0',  0, '18-AUG-2015', '18-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:96 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRINREPAIR', 0, 'PR', 0, 01, 'Part request is in repair.', 'Part request is in repair', 'IN REPAIR', '10', '0',  0, '18-AUG-2015', '18-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:97 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRONHAND', 0, 'PR', 0, 01, 'Part request is on hand.', 'Part request is on hand', 'ON HAND', '10', '0',  0, '19-AUG-2015', '19-AUG-2015', 100, 'MXI');

--changeSet 0ref_event_status:98 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRERROR', 0, 'PR', 0, 01, 'Part request is in error.', 'Part request is in error', 'ERROR', '10', '0',  0, '21-SEP-2015', '21-SEP-2015', 100, 'MXI');

--changeSet 0ref_event_status:99 stripComments:false
-- Part Request Assignment
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRAASSIGNED', 0, 'PRA', 0, 01, 'Part request is assigned.', 'Part request is assigned.', 'ASSIGNED', '10', '0',  0, '16-APR-08', '16-APR-08', 100, 'MXI');

--changeSet 0ref_event_status:100 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRASTOLEN', 0, 'PRA', 0, 01, 'Part request is stolen.', 'Part request is stolen.', 'STOLEN', '10', '0',  0, '16-APR-08', '16-APR-08', 100, 'MXI');

--changeSet 0ref_event_status:101 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRAUNASSIGNED', 0, 'PRA', 0, 01, 'Part request is unassigned.', 'Part request is unassigned.', 'UNASSIGNED', '10', '0',  0, '16-APR-08', '16-APR-08', 100, 'MXI');

--changeSet 0ref_event_status:102 stripComments:false
-- Purchase Order
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POOPEN', 0, 'PO', 0, 01, 'The order is new and/or being prepared.', 'The order is new and/or being prepared.', 'OPEN', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:103 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POISSUED', 0, 'PO', 0, 01, 'The order has been issued.', 'The order has been issued.', 'ISSUED', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:104 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PORECEIVED', 0, 'PO', 0, 01, 'The order has been received partially or completely.', 'The order has been received partially or completely.', 'RECEIVED', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:105 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POCLOSED', 0, 'PO', 0, 01, 'The order has been marked for payment and is therefore closed.', 'The order has been marked for payment and is therefore closed.', 'CLOSED', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:106 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POCANCEL', 0, 'PO', 0, 01, 'Cancelled', 'The order has been cancelled.', 'CANCEL', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:107 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POPARTIAL', 0, 'PO', 0, 01, 'The order has been partially received.', 'The order has been partially received.', 'PARTIAL', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:108 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POACKNOWLEDGED', 0, 'PO', 0, 01, 'The order has been acknowledged by the vendor.', 'The order has been acknowledged by the vendor.', 'ACKNOWL', '10', '0',  0, '31-MAY-05', '31-MAY-05', 100, 'MXI');

--changeSet 0ref_event_status:109 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POCONVERTBO', 0, 'PO', 0, 01, 'The borrow order has been converted to PO.', 'The borrow order has been converted to PO.', 'BO-TO-PO', '10', '0',  1, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_status:110 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POCONVERTEO', 0, 'PO', 0, 01, 'The exchange order has been converted to PO.', 'The exchange order has been converted to PO.', 'EO-TO-PO', '10', '0',  1, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_status:111 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POAUTH', 0, 'PO', 0, 01, 'Order authorized', 'This order has been authorized and can be issued.', 'AUTH', '10', '0',  0, to_date('27-07-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-07-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 100, 'MXI');

--changeSet 0ref_event_status:112 stripComments:false
-- Exchange Order
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EOCONVERTRO', 0, 'EO', 0, 01, 'The repair order has been converted to EO.', 'The repair order has been converted to EO.', 'RO-TO-EO', '10', '0',  1, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_status:113 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EOCONVERTBO', 0, 'EO', 0, 01, 'The borrow order has been converted to EO.', 'The borrow order has been converted to EO.', 'BO-TO-EO', '10', '0',  1, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_status:114 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EOCONVERTPO', 0, 'EO', 0, 01, 'The purchase order has been converted to EO.', 'The purchase order has been converted to EO.', 'PO-TO-EO', '10', '0',  1, '02-JUN-08', '02-JUN-08', 100, 'MXI');

--changeSet 0ref_event_status:115 stripComments:false
-- Purchase Invoice
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PIOPEN', 0, 'PI', 0, 01, 'This invoice is open, and has not been sent for payment.', 'This invoice is open, and has not been sent for payment.', 'OPEN', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:116 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PITOBEPAID', 0, 'PI', 0, 01, 'This invoice has been sent for payment.', 'This invoice has been sent for payment.', 'TOBEPAID', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:117 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PICANCEL', 0, 'PI', 0, 01, 'This invoice has been cancelled.', 'This invoice has been cancelled.', 'CANCEL', '10', '0',  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_status:118 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PIPAID', 0, 'PI', 0, 01, 'This invoice has been paid.', 'This invoice has paid.', 'PAID', '10', '0',  0, '08-JUN-06', '08-JUN-06', 100, 'MXI');

--changeSet 0ref_event_status:119 stripComments:false
-- Tool Checkout
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TCOOUT', 0, 'TCO', 0, 1, 'The tool is checked out.', 'The tool is checked out by a user and this event is active.', 'OUT', '10', '0',  0, '20-JAN-06', '20-JAN-06', 100, 'MXI');

--changeSet 0ref_event_status:120 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TCOIN', 0, 'TCO', 0, 1, 'The tool is checked in.', 'The tool is checked in and this event is now complete.', 'IN', '10', '0',  0, '20-JAN-06', '20-JAN-06', 100, 'MXI');

--changeSet 0ref_event_status:121 stripComments:false
-- Vendor
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VNAPPROVED', 0, 'VN', 0, 01, 'Vendor is approved', 'This vendor is an acceptable provider of parts, both on the basis of regulatory approval, and on the basis of vendor performance for your organization', 'APPROVED', '10', '0',  0, '30-JUN-06', '30-JUN-06', 100, 'MXI');

--changeSet 0ref_event_status:122 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VNWARNING', 0, 'VN', 0, 01,'Vendor has a warning', 'Indicates a potential problem with the vendor that is being investigated.  Likely not a good idea to purchase parts from this vendor at this time', 'WARNING', '10', '0',  0,'30-JUN-06', '30-JUN-06', 100, 'MXI');

--changeSet 0ref_event_status:123 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VNUNAPPRVD', 0, 'VN', 0, 01,'Vendor is unapproved', 'The vendor is, for any number of reasons, considered to be a risk - it has failed to receive regulatory approval, or has a trend of poor performance for your organization. ', 'UNAPPRVD', '10', '0',  0, '30-JUN-06', '30-JUN-06', 100, 'MXI');

--changeSet 0ref_event_status:124 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VNUPDATED', 0, 'VN', 0, 1, 'Vendor is updated', 'Indicates the vendor details are updated', 'UPDATED', '10', '0',  0, '11-MAY-16', '11-MAY-16', 100, 'MXI');

--changeSet 0ref_event_status:125 stripComments:false
-- Blackouts
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'BLACKOUT', 0, 'BLK', 0, 01,'Blackout', 'The aircraft is in a state of blackout.', 'BLACKOUT', '10', '0',  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');

--changeSet 0ref_event_status:126 stripComments:false
-- Request For Quote
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFQOPEN', 0, 'RFQ', 0, 01, 'The RFQ is new and/or being prepared.', 'The RFQ is new and/or being prepared.', 'OPEN', '10', '0',  0, '28-NOV-06', '28-NOV-06', 100, 'MXI');

--changeSet 0ref_event_status:127 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFQSENT', 0, 'RFQ', 0, 01, 'The RFQ has been sent to the vendor.', 'The RFQ has been sent to the vendor.', 'SENT', '10', '0',  0, '28-NOV-06', '28-NOV-06', 100, 'MXI');

--changeSet 0ref_event_status:128 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFQCLOSED', 0, 'RFQ', 0, 01, 'The RFQ has been closed.', 'The RFQ has been closed.', 'CLOSED', '10', '0',  0, '28-NOV-06', '28-NOV-06', 100, 'MXI');

--changeSet 0ref_event_status:129 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFQCANCEL', 0, 'RFQ', 0, 01, 'The RFQ has been cancelled.', 'The RFQ has been cancelled.', 'CANCEL', '10', '0',  0, '28-NOV-06', '28-NOV-06', 100, 'MXI');

--changeSet 0ref_event_status:130 stripComments:false
-- Change Owner
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OCOWNERCHG', 0, 'OC', 0, 01, 'Change Owner.', 'Change Owner.', 'OC', '10', '0',  0, '17-MAR-07', '17-MAR-07', 100, 'MXI');

--changeSet 0ref_event_status:131 stripComments:false
-- Change Authority
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CAAUTHCHG', 0, 'CA', 0, 01, 'Change Authority.', 'Change Authority.', 'CA', '10', '0',  0, '02-DEC-09', '02-DEC-09', 100, 'MXI');

--changeSet 0ref_event_status:132 stripComments:false
-- Licensing
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDBUILD', 0, 'LD', 0, 01, 'Build', 'License definition was new and in work.', 'BUILD', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:133 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDACTV', 0, 'LD', 0, 01, 'Active', 'License definition was active.', 'ACTV', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:134 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDOBSL', 0, 'LD', 0, 01, 'Obselete', 'License definition was obselete.', 'OBSL', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:135 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'HRACTV', 0, 'HR', 0, 01, 'Active', 'The user license was active.', 'ACTV', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:136 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'HRSPND', 0, 'HR', 0, 01, 'Suspend', 'The user license was suspended.', 'SUSPEND', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:137 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'HRINVAL', 0, 'HR', 0, 01, 'Invalid', 'The user license was invalid.', 'INVALID', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:138 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'HRUNSPND', 0, 'HR', 0, 01, 'Un-suspend', 'The user license was unsuspended.', 'UNSUSPEND', '10', '0',  0, '19-FEB-09', '19-FEB-09', 100, 'MXI');

--changeSet 0ref_event_status:139 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LDMOD', 0, 'LD', 0, 01, 'Modified.', 'License definition was modified.', 'MOD', '10', '0',  0, '20-FEB-08', '20-FEB-08', 100, 'MXI');

--changeSet 0ref_event_status:140 stripComments:false
-- Claims
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CLOPEN', 0, 'CL', 0, 1, 'Open', 'This claim is open.', 'OPEN', '10', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:141 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CLSUBMIT', 0, 'CL', 0, 1, 'Submitted', 'This claim is submitted.', 'SUBMIT', '20', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:142 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CLCLOSE', 0, 'CL', 0, 1, 'Closed', 'This claim is closed.', 'CLOSE', '30', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:143 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CLCANCEL', 0, 'CL', 0, 1, 'Cancelled', 'This claim is cancelled.', 'CANCEL', '40', '0',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_status:144 stripComments:false
-- Deployed Ops Blackout Data
insert into REF_EVENT_STATUS (EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, USER_STATUS_CD, STATUS_ORD, AUTH_REQD_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'IXBLKOUT', 0, 'IX', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_event_status:145 stripComments:false
insert into REF_EVENT_STATUS (EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, USER_STATUS_CD, STATUS_ORD, AUTH_REQD_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'POBLKOUT', 0, 'PO', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_event_status:146 stripComments:false
insert into REF_EVENT_STATUS (EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, USER_STATUS_CD, STATUS_ORD, AUTH_REQD_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'PRBLKOUT', 0, 'PR', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_event_status:147 stripComments:false
insert into REF_EVENT_STATUS (EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, USER_STATUS_CD, STATUS_ORD, AUTH_REQD_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 'TS', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_event_status:148 stripComments:false
insert into REF_EVENT_STATUS (EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, USER_STATUS_CD, STATUS_ORD, AUTH_REQD_BOOL, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CFBLKOUT', 0, 'CF', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, 0, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_event_status:149 stripComments:false
-- Update Operation Restriction note
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UPDOPSRES', 0, 'CF', 0, 1, 'Update', 'Update Operational Restriction', 'UPD', '1', '0',  0, to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-08-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 100, 'MXI');