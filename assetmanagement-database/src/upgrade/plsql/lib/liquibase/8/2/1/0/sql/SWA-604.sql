--liquibase formatted sql


--changeSet SWA-604:1 stripComments:false
/**************************************************************************************
* 
* SWA-604: Add vendor updated event status
*
***************************************************************************************/
INSERT INTO 
   ref_event_status 
   ( 
      event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
   ) 
SELECT 
   0, 'VNUPDATED', 0, 'VN', 0, 1, 'Vendor is updated', 'Indicates the vendor details are updated', 'UPDATED', '10', '0',  0, to_date('11-05-2016', 'dd-mm-yyyy'), to_date('11-05-2016', 'dd-mm-yyyy'), 100, 'MXI'
FROM
   dual
WHERE 
   NOT EXISTS ( SELECT 1 FROM ref_event_status WHERE event_status_cd = 'VNUPDATED' );   