/********************************************
** INSERT SCRIPT FOR TABLE "REF_STAGE_REASON"
** 10-Level
** DATE: 05-05-05 TIME: 00:00:00
*********************************************/

--Task
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'AGREEMNT', 0, 'ASSIGN', 0, 83, 'Under Contractual Agreement', 'Under Contractual Agreement', 'AGREEMNT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ADNTIME', 0, 'ASSIGN', 0, 63, 'Additonal Time Available ', 'Additional time is available in Check', 'ADNTIME',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'UACNTPRB', 0, 'UNASSIGN', 0, 82, 'Contractual Problems', 'When a task is unassigned by a customer', 'CONTRPRB',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'AVOVDUE', 0, 'UNASSIGN', 0, 31, 'Avoid Overdue Check ', 'Unassign task to avoid overdue check', 'AVOVDUE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'RECERROR', 0, 'ERROR', 0, 82, 'Bogus Task Record', 'Task did not happen at all, must delete record', 'RECERROR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'DOCERROR', 0, 'ERROR', 0, 82, 'Documentation Error', 'Task happened, but not the exact way as stated in record (Need to edit record)', 'DOCERROR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'APPROVAL', 0, 'COMMIT', 0, 83, 'Workscope Approval', 'Workscope Approval', 'APPROVAL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWP', 0, 'DELAY', 0, 105, 'Awaiting Parts', 'Task deferral due to unavailable parts for work completion', 'AWP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWM', 0, 'DELAY', 0, 56, 'Awaiting Maintenance', 'Task deferred due to outstanding maintenance; personnel not available', 'AWM',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWT', 0, 'DELAY', 0, 56, 'Awaiting Tools', 'Task deferral due to unavailable tools', 'AWT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWS', 0, 'DELAY', 0, 50, 'Awaiting Shop Space', 'Task deferral due to unavailable shop space', 'AWS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWF', 0, 'DELAY', 0, 1, 'Awaiting Test Flight', 'Awaiting Test Flight', 'AWF',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDOPS', 0, 'DELAY', 0, 97, 'Awaiting Operations', 'Task deferred until completion of functional check', 'OPS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TDAWD', 0, 'DELAY', 0, 80, 'Awaiting Documentation', 'Task deferred due to lack of instructions', 'AWD',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'NA', 0, 'CANCEL', 0, 86, 'Non applicable', 'Task is not applicable for this aircraft', 'NA',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CONTRPRB', 0, 'CANCEL', 0, 82, 'Contractual Problems', 'When a task is cancelled by a customer', 'CONTRPRB',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'NFF', 0, 'CANCEL', 0, 82, 'No Fault Found', 'Task was cancelled because no fault was found', 'NFF',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TPAWP', 0, 'PAUSE', 0, 105, 'Awaiting Parts', 'Task paused due to unavailable parts for work completion', 'AWP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TPAWT', 0, 'PAUSE', 0, 56, 'Awaiting Tools', 'Task paused due to unavailable tools', 'AWT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TPAWS', 0, 'PAUSE', 0, 50, 'Awaiting Shop Space', 'Task paused due to unavailable shop space', 'AWS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TPAWD', 0, 'PAUSE', 0, 80, 'Awaiting Documentation', 'Task paused  due to lack of instructions', 'AWD',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TPSHFT', 0, 'PAUSE', 0, 56, 'Shift Change ', 'Task paused  due to shift change', 'SHFT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRES', 0, 'PREVENTEXE', 0, 114, 'Preservation', 'Time accruement has been suspended due to equipment preservation', 'PRES',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TLADJUST', 0, 'ACTV', 0, 63, 'Adjust Workscope', 'Adjust Workscope', 'ADJUST',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TCVEND', 0, 'COMPLETE', 0, 83, 'Performed by Vendor', 'Task was performed by vendor', 'VEND',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TCOWN', 0, 'COMPLETE', 0, 83, 'Performed by previous owner', 'Task was performed by previous owner', 'OWN',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TCSYS', 0, 'COMPLETE', 0, 83, 'System setup/ Data Conversions', 'Task has to created because of system setup or data conversion', 'SYS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'TKNA', 0, 'N/A', 0, 82, 'Not Applicable', 'Task is no longer applicable', 'NA',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'NATERM', 0, 'TERMINATE', 0, 86, 'Non applicable', 'Task is not applicable for this aircraft', 'NA',  0, '10-OCT-08', '10-OCT-08', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CONPTERM', 0, 'TERMINATE', 0, 82, 'Contractual Problems', 'When a task is terminated by a customer', 'CONTRPRB',  0, '10-OCT-08', '10-OCT-08', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'NFFTERM', 0, 'TERMINATE', 0, 82, 'No Fault Found', 'Task was terminated because no fault was found', 'NFF',  0, '10-OCT-08', '10-OCT-08', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'OBSTERM', 0, 'TERMINATE', 0, 82, 'Obsolete', 'When a task is terminated by a customer', 'OBSOLETE',  0, '10-OCT-08', '10-OCT-08', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOMTERM', 0, 'TERMINATE', 0, 82, 'No Fault Found', 'A BOM transformation has occured resulting in this task being terminated.', 'BOMTRANS',  0, '10-OCT-08', '10-OCT-08', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'R1', 0, 'EDIT', 0, 1, 'Reason 1 Short Description', 'Reason 1 Long Description', 'USER-R1',  0, '05-JUL-10', '05-JUL-10', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'R2', 0, 'EDIT', 0, 1, 'Reason 2 Short Description', 'Reason 2 Long Description', 'USER-R2',  0, '05-JUL-10', '05-JUL-10', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'R3', 0, 'EDIT', 0, 1, 'Reason 3 Short Description', 'Reason 3 Long Description', 'USER-R3',  0, '05-JUL-10', '05-JUL-10', 100, 'MXI');

--Change Inventory Condition
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACREP', 0, 'ACRFI', 0, 80, 'Repaired', 'Inventory has been repaired', 'REP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACOVHL', 0, 'ACRFI', 0, 80, 'Overhaul', 'Inventory has been overhauled', 'OVHL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACSOS', 0, 'ACRFI', 0, 80, 'Safe or Save', 'Inventory is made serviceable, after it was determined that the fault on the aircraft was not due to the inventory', 'SOS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACNREP', 0, 'ACSCRAP', 0, 80, 'Non-repairable', 'Non-repairable Item', 'NREP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACBER', 0, 'ACSCRAP', 0, 80, 'Beyond Economical Repair', 'Beyond Economical Repair', 'BER',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACSUP', 0, 'ACSCRAP', 0, 80, 'Suspected Unapproved Part', 'Scrap because inventorys records could not be found, or the serial no or part no (identification record) is no good', 'SUP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACLLP', 0, 'ACSCRAP', 0, 80, 'Life Limited Part', 'Life Limited Part', 'LLP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACEXPR', 0, 'ACSCRAP', 0, 80, 'Shelf Life Expiry', 'Shelf Life Expiry', 'EXPIRE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACSOLD', 0, 'ACARCHIVE', 0, 80, 'Sold', 'Sold', 'SOLD',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACEXCHG', 0, 'ACARCHIVE', 0, 80, 'Exchanged', 'Exchanged', 'EXCHG',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACSCRAP', 0, 'ACARCHIVE', 0, 80, 'Scrapped', 'Scrapped', 'SCRAP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACCEXPR', 0, 'ACCONDEMN', 0, 80, 'Shelf Life Expiry', 'Shelf Life Expiry', 'EXPIRE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACCNREP', 0, 'ACCONDEMN', 0, 80, 'Non-repairable', 'Non-repairable Item', 'NREP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACCBER', 0, 'ACCONDEMN', 0, 80, 'Beyond Economical Repair', 'Beyond Economical Repair', 'BER',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACCSUP', 0, 'ACCONDEMN', 0, 80, 'Suspected Unapproved Part', 'Scrap because inventorys records could not be found, or the serial no or part no (identification record) is no good', 'SUP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACCLLP', 0, 'ACCONDEMN', 0, 80, 'Life Limited Part', 'Life Limited Part', 'LLP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACQAPW', 0, 'ACQUAR', 0, 80, 'Awaiting Paper Work', 'Quarantined until paper work is received', 'APW',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ACQSOS', 0, 'ACQUAR', 0, 80, 'Ship or Shelf', 'Quarantined until it is detemined that the fault on the aircraft is not due to the inventory', 'SOS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');



--Shipment
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'IXALL', 0, 'IXCMPLT', 0, 54, 'All Items Received', 'All Items Received', 'ALL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'IXALLNT', 0, 'IXCMPLT', 0, 54, 'Accepted Not Received', 'Accepted that not all items have been received', 'ALLNT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--Inventory Lock
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ILSRV', 0, 'ILLOCK', 0, 144, 'Flight Incident', 'Investigation is pending due to serious safety incident', 'SRV',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ILINVC', 0, 'ILUNLOCK', 0, 144, 'Investigation Complete', 'Investigation Complete', 'INVC',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--Part Edit
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PECORR', 0, 'PEGEN', 0, 01, 'Part Correction', 'Part Correction', 'CORRECT',  0, '06-JUL-06', '06-JUL-06', 100, 'MXI');

insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PENEWIN', 0, 'PEGEN', 0, 01, 'New Information for Part', 'New Information for Part', 'NEWINFO',  0, '06-JUL-06', '06-JUL-06', 100, 'MXI');

insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PEMANCHG', 0, 'PEGEN', 0, 01, 'Part Manufacturer Changes', 'Part Manufacturer Changes', 'MANUFCHG',  0, '06-JUL-06', '06-JUL-06', 100, 'MXI');

insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PEINTADJ', 0, 'PEGEN', 0, 01, 'Internal Adjustments', 'Internal Adjustments', 'INTADJ',  0, '06-JUL-06', '06-JUL-06', 100, 'MXI');

--Quantity Correction
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'QCCOUNT', 0, 'QCPOS', 0, 78, 'Stock Count', 'Stock Count', 'COUNT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'QCCONSUM', 0, 'QCPOS', 0, 78, 'Bench Stock Consumption', 'Bench Stock Consumption', 'CONSUM',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'QCERROR', 0, 'QCPOS', 0, 78, 'Error Correction', 'Error Correction', 'ERROR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--Part Request
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PREXTRA', 0, 'PRCANCEL', 0, 1, 'These are miscellaneous extra parts that are not needed', 'These are miscellaneous extra parts that are not needed', 'EXTRA',  0, '05-MAY-05', '05-MAY-05', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRLOCAL', 0, 'PRCANCEL', 0, 1, 'The technician has found a local stockpile (benchstock) of inventory', 'The technician has found a local stockpile (benchstock) of inventory', 'LOCAL',  0, '05-MAY-05', '05-MAY-05', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PROS', 0, 'PRPOREQ', 0, 1, 'Out Of Stock', 'Out Of Stock', 'OUTSTOCK',  0, '06-MAY-05', '06-MAY-05', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRNS', 0, 'PRPOREQ', 0, 1, 'Not Stocked', 'Not Stocked', 'NOTSTOCK',  0, '06-MAY-05', '06-MAY-05', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRAVAIL', 0, 'PROPEN', 0, 1, 'Stock Available', 'Stock Available', 'AVAIL',  0, '23-JUNE-06', '23-JUNE-06', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRNPP', 0, 'PROPEN', 0, 1, 'Non Procurement Part', 'Non Procurement Part', 'NPP',  0, '23-JUNE-06', '23-JUNE-06', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'PRUNAPPR', 0, 'PROPEN', 0, 1, 'Unapproved', 'Unapproved', 'UNAPPR',  0, '23-JUNE-06', '23-JUNE-06', 100, 'MXI');

-- Vendor Status Change
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'VNNLE', 0, 'VNUNAPPRVD', 0, 1, 'No Longer Exists', 'No Longer Exists', 'NLE',  0, '04-JULY-06', '04-JULY-06', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'VNPERF', 0, 'VNUNAPPRVD', 0, 1, 'Poor Performance', 'Poor Performance', 'PP',  0, '04-JULY-06', '04-JULY-06', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'VNSUPP', 0, 'VNUNAPPRVD', 0, 1, 'No Longer Supplies Part', 'No Longer Supplies Part', 'NLSP',  0, '04-JULY-06', '04-JULY-06', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'VNNN', 0, 'VNUNAPPRVD', 0, 1, 'Not Necessary', 'Not Necessary', 'NN',  0, '04-JULY-06', '04-JULY-06', 100, 'MXI');

-- Change Owner
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'SALE', 0, 'OCOWNERCHG', 0, 1, 'Sale of Part', 'Sale of Part', 'OC',  0, '17-MAR-07', '17-MAR-07', 10, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'EXCHANGE', 0, 'OCOWNERCHG', 0, 1, 'Vendor Exchange', 'Vendor Exchange', 'OC',  0, '17-MAR-07', '17-MAR-07', 10, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'DEPTCHG', 0, 'OCOWNERCHG', 0, 1, 'Department Change', 'Department Change', 'OC',  0, '17-MAR-07', '17-MAR-07', 10, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CONSIGN', 0, 'OCOWNERCHG', 0, 1, 'Consignment', 'Consignment', 'OC',  0, '17-MAR-07', '17-MAR-07', 10, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'OTHER', 0, 'OCOWNERCHG', 0, 1, 'Other', 'Other', 'OC',  0, '17-MAR-07', '17-MAR-07', 10, 'MXI');

-- Conversion between RO, EO, BO, and PO
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ROEOCONT', 0, 'EOCONVERTRO', 0, 1, 'Change of Contract Terms', 'Change of Contract Terms', 'CONTRACT',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ROEOOFF', 0, 'EOCONVERTRO', 0, 1, 'Vendor Exchange Offer', 'Vendor Exchange Offer', 'OFFER',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'ROEOTIME', 0, 'EOCONVERTRO', 0, 1, 'Repair Expedited', 'Repair Expedited', 'TIME',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOPOCONT', 0, 'POCONVERTBO', 0, 1, 'Change of Contract Terms', 'Change of Contract Terms', 'CONTRACT',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOPOLATE', 0, 'POCONVERTBO', 0, 1, 'Late Return', 'Late Return', 'LATE',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOPOROUT', 0, 'POCONVERTBO', 0, 1, 'Aircraft Not Routed', 'Aircraft Not Routed', 'ROUTING',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOEOCONT', 0, 'EOCONVERTBO', 0, 1, 'Change of Contract Terms', 'Change of Contract Terms', 'CONTRACT',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOEOLATE', 0, 'EOCONVERTBO', 0, 1, 'Late Return', 'Late Return', 'LATE',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'BOEOROUT', 0, 'EOCONVERTBO', 0, 1, 'Aircraft Not Routed', 'Aircraft Not Routed', 'ROUTING',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'EOPOLATE', 0, 'POCONVERTEO', 0, 1, 'Late Return', 'Late Return', 'LATE',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'NOTFOUND', 0, 'POCONVERTEO', 0, 1, 'Return Item Not Found', 'Return Item Not Found', 'NOTFOUND',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'EOPOCONT', 0, 'POCONVERTEO', 0, 1, 'Change of Contract Terms', 'Change of Contract Terms', 'CONTRACT',  0, '09-APR-07', '09-APR-07', 100, 'MXI');

-- Claims
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLCMPLT', 0, 'CLCLOSE', 0, 1, 'Claim handled.', 'This claim has been handled.', 'COMPLETE',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLERROR', 0, 'CLCANCEL', 0, 1, 'Claim fell through.', 'This claim has fallen through.', 'ERROR',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLTSTCLS', 0, 'CLCLOSE', 0, 1, 'Test close reason.', 'Test close.', 'TSTCMPLT',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
insert into ref_stage_reason (stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 'CLTSTERR', 0, 'CLCANCEL', 0, 1, 'Test cancel reason.', 'Test cancel.', 'TSTERROR',  0, '09-APR-07', '09-APR-07', 100, 'MXI');
