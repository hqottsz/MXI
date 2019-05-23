/********************************************
** INSERT SCRIPT FOR TABLE "REF_EVENT_REASON"
** 10-Level
** DATE: 09/30/1998 TIME: 16:56:27
*********************************************/
-- Task reason codes
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'ACCESS', 0, 'TS', 0, 42, 'Access or convenience', 'Equipment removed to access internal equipment', 'ACCESS',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'ROB', 0, 'TS', 0, 5, 'Rob or Cannibalization', 'Equipment removed for installation on another item.', 'ROB',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'ADMIN', 0, 'TS', 0, 143, 'Administrative', 'Required for administrative reasons', 'ADMIN',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'ONCOND', 0, 'TS', 0, 1, 'On Condition', 'Performed due to on-condition criteria', 'ONCOND',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'LOANER', 0, 'TS', 0, 1, 'Loaner', ' Item removed to return to owner (this was a loaner item).', 'LOANER',  0, '17-OCT-01', '17-OCT-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'WARRANTY', 0, 'TS', 0, 1, 'Warranty', 'Item removed to return to warranty provider.', 'WARRANTY',  0, '17-OCT-01', '17-OCT-01', 100, 'MXI');

-- Shipment reason codes
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXLVL', 0, 'IX', 0, 145, 'Stock Leveling', 'Inventory was transferred for stock leveling', 'LVL',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXDEBIT', 0, 'IX', 0, 145, 'Debit Transfer', 'Inventory was transferred for debit', 'DEBIT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXREP', 0, 'IX', 0, 75, 'Replacement', 'Inventory was transferred for replacement', 'REP',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXIR', 0, 'IX', 0, 130, 'Inventory Reservation', 'Inventory was transferred for reservation (or pre-positioning).', 'IR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXTR', 0, 'IX', 0, 131, 'Tool reservation', 'Tool was transferred for reservation (or pre-positioning)', 'TR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXRETURN', 0, 'IX', 0, 1, 'Return to owner', 'Inventory was returned to owner', 'RETURN',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXREPAIR', 0, 'IX', 0, 50, 'Major Repair/Overhaul', 'Inventory was returned from repair/overhaul', 'REPAIR',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXPRESV', 0, 'IX', 0, 46, 'Item shipped for Preservation', 'Item shipped for Preservation', 'PRESERVE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXSMAINT', 0, 'IX', 0, 63, 'Scheduled Maintenance', 'Inventory was shipped to have scheduled maintenance performed', 'S-MAINT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IXCMAINT', 0, 'IX', 0, 119, 'Minor Corrective Maintenance', 'Inventory was shipped to have corrective maintenance performed', 'C-MAINT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

-- Fault reason codes
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CFMAINT', 0, 'CF', 0, 86, 'Improper Maintenance', 'Improper Maintenance', 'MAINT',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CFDESIGN', 0, 'CF', 0, 145, 'Improper Design', 'Improper Design', 'DESIGN',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CFUSE', 0, 'CF', 0, 135, 'Improper Use/Operation', 'Improper Use/Operation', 'USE',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

-- Blackout reason codes
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BLKHM', 0, 'BLK', 0, 1, 'Heavy Maintenance', 'Heavy Maintenance', 'BLKHM',  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BLKAI', 0, 'BLK', 0, 1, 'Aircraft Induction', 'Aircraft Induction', 'BLKAI',  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BLKAR', 0, 'BLK', 0, 1, 'Aircraft Retirement', 'Aircraft Retirement', 'BLKAR',  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');
insert into ref_event_reason(event_reason_db_id, event_reason_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  user_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BLKOOS', 0, 'BLK', 0, 1, 'Out of Service', 'Out of Service', 'BLKOOS',  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');

