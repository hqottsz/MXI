--liquibase formatted sql


--changeSet MTX-1459:1 stripComments:false
-- Change PART_REQUEST_DETAILS_CHANGE alert 
UPDATE 
   utl_alert_type 
SET 
   alert_name = 'core.alert.PART_REQUEST_DETAILS_CHANGE_name',
   alert_ldesc = 'core.alert.PART_REQUEST_DETAILS_CHANGE_description',
   notify_cd = 'ROLE',
   notify_class = 'com.mxi.mx.core.plugin.alert.req.PartRequestDetailsChangedContactHrRule',
   category = 'REQ',
   message = 'core.alert.PART_REQUEST_DETAILS_CHANGE_message'
WHERE 
   alert_type_id = 118;

--changeSet MTX-1459:2 stripComments:false
-- Change the alert from PART_REQUEST_WHERE_NEEDED:244 to PART_REQUEST_DETAILS_CHANGE:118
UPDATE utl_alert SET alert_type_id = 118 WHERE alert_type_id = 244;

--changeSet MTX-1459:3 stripComments:false
-- Remove PART_REQUEST_WHERE_NEEDED alert type and its roles
DELETE FROM utl_alert_type_role WHERE alert_type_id = 244;

--changeSet MTX-1459:4 stripComments:false
DELETE FROM utl_alert_type WHERE alert_type_id = 244;