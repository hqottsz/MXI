--liquibase formatted sql


--changeSet OPER-3556:1 stripComments:false
insert into ref_event_status(event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRACKNOWLEDGED', 0, 'PR', 0, 01, 'Part request has been acknowledged.', 'Part request has been acknowledged', 'ACKNOWLEDGED', '10', '0',  0, TO_DATE('2015-AUG-04', 'YYYY-MM-DD'), TO_DATE('2015-AUG-04', 'YYYY-MM-DD'), 100, 'MXI');