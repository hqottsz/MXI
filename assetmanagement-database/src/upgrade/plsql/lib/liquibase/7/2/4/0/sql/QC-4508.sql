--liquibase formatted sql


--changeSet QC-4508:1 stripComments:false
/* This script updates the default settings and description for the 
   NOTIFICATION_AREA_UPDATE_INTERVAL config parm. */
UPDATE
   utl_config_parm ucp
SET
   ucp.default_value = 0,
   ucp.modified_in = '6.8.12-SP1',
   ucp.parm_desc = 
      'Sets the polling interval (in milliseconds) between updates of the alert notification area, except for the following special cases: if the value is negative, the notification area is never updated (and thus never visible); if zero, the notification area is updated just once, on page load; and if between 0 and 5000 exclusive, the notification area is updated at the minimum interval of every 5000.'
WHERE
   ucp.parm_name = 'NOTIFICATION_AREA_UPDATE_INTERVAL';