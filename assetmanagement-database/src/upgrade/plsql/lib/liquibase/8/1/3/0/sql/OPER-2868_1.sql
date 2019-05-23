--liquibase formatted sql


--changeSet OPER-2868_1:1 stripComments:false
-- Add a new ref term
INSERT INTO
   ref_event_status
   (
     event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   )
   SELECT 0, 'PRAWAITISSUE', 0, 'PR', 0, 01, 'Part request is waiting to be issued.', 'Part request is waiting to be issued.', 'AWAITING ISSUE', '10', '0',  0, TO_DATE('2015-04-09', 'YYYY-MM-DD'), TO_DATE('2015-04-09', 'YYYY-MM-DD'), 100, 'MXI'
   FROM
     dual
   WHERE
     NOT EXISTS ( SELECT 1 FROM ref_event_status WHERE event_status_db_id = 0 AND event_status_cd = 'PRAWAITISSUE' );