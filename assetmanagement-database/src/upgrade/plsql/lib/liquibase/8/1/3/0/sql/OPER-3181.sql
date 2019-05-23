--liquibase formatted sql


--changeSet OPER-3181:1 stripComments:false
-- add ref event status
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRINSRV', 0, 'ICR', 0, 80, 'In Service', 'In Service', 'INSRV', '20', '0',  0, to_date('2015-04-30', 'YYYY-MM-DD'), to_date('2015-04-30', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRINSRV' );

--changeSet OPER-3181:2 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRSCRAP', 0, 'ICR', 0, 80, 'Scrap Asset', 'Scrap Asset', 'SCRAP', '50', '0',  0, to_date('2015-04-30', 'YYYY-MM-DD'), to_date('2015-04-30', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRSCRAP' );

--changeSet OPER-3181:3 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRARCHIVE', 0, 'ICR', 0, 80, 'Archive', 'Archive', 'ARCHIVE', '200', '0',  0, to_date('2015-04-30', 'YYYY-MM-DD'), to_date('2015-04-30', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRARCHIVE' );

--changeSet OPER-3181:4 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRINREP', 0, 'ICR', 0, 80, 'In Repair', 'In Repair', 'INREP', '200', '0',  0, to_date('2015-04-30', 'YYYY-MM-DD'), to_date('2015-04-30', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRINREP' );

--changeSet OPER-3181:5 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
select 0, 'ICRCONDEMN', 0, 'ICR', 0, 80, 'Condemned', 'Condemned', 'CONDEMN', '200', '0',  0, to_date('2015-04-30', 'YYYY-MM-DD'), to_date('2015-04-30', 'YYYY-MM-DD'), 100, 'MXI'
from dual where not exists ( select 1 from ref_event_status where event_status_db_id = 0 and event_status_cd = 'ICRCONDEMN' );