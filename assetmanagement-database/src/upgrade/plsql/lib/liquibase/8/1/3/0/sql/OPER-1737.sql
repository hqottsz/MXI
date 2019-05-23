--liquibase formatted sql


--changeSet OPER-1737:1 stripComments:false
-- add ref event type
insert into ref_event_type( event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
select 0, 'ICR', 0, 1,  'Inventory Creation', 'This is an inventory creation event.', 0,  0, to_date('2015-02-02', 'YYYY-MM-DD'), to_date('2015-02-02', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_type where event_type_db_id = 0 and event_type_cd = 'ICR' ); 

--changeSet OPER-1737:2 stripComments:false
-- add ref event status
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRRFI', 0, 'ICR', 0, 80, 'Ready for Issue', 'Ready for Issue', 'RFI', '20', '0',  0, to_date('2015-02-02', 'YYYY-MM-DD'), to_date('2015-02-02', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRRFI' );

--changeSet OPER-1737:3 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRREPREQ', 0, 'ICR', 0, 80, 'Repair Required', 'Repair Required', 'REPREQ', '50', '0',  0, to_date('2015-02-02', 'YYYY-MM-DD'), to_date('2015-02-02', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRREPREQ' );

--changeSet OPER-1737:4 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRINSPREQ', 0, 'ICR', 0, 80, 'Awaiting Inspection', 'Awaiting Inspection', 'INSPREQ', '200', '0',  0, to_date('2015-02-02', 'YYYY-MM-DD'), to_date('2015-02-02', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRINSPREQ' );