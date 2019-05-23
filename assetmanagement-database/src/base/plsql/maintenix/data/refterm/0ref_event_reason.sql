--liquibase formatted sql


--changeSet 0ref_event_reason:1 stripComments:false
-- Local Transfer Reason codes
/********************************************
** INSERT SCRIPT FOR TABLE "REF_EVENT_REASON"
** 0-Level
** DATE: 09/30/1998 TIME: 16:56:27
*********************************************/
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXMAINT', 0, 'LX', 0, 12, 'Issue for Maintenance', 'Inventory was transferred for maintenance', 'MAINT', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:2 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXPREDRW', 0, 'LX', 0, 12, 'Predraw', 'Inventory was transferred for predraw', 'PREDRAW', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:3 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXQUAR', 0, 'LX', 0, 12, 'Quarantine', 'Inventory was transferred to be quarantined', 'QUAR', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:4 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXSTOCK', 0, 'LX', 0, 12, 'Stock Leveling', 'Inventory was transferred for stock leveling', 'STOCK', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:5 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXSTORGE', 0, 'LX', 0, 12, 'Storage', 'Inventory was transferred to be put in storage', 'STORAGE',  0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:6 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXTURNIN', 0, 'LX', 0, 12, 'Turn In', 'Inventory was transferred for turn in', 'TURNIN', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');

--changeSet 0ref_event_reason:7 stripComments:false
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LXSTKTRN', 0, 'LX', 0, 12, 'Ad-hoc Transfer', 'Inventory was transferred on an ad-hoc basis', 'STKTRN', 0, '20-JAN-05', '20-JAN-05', 100, 'MXI');