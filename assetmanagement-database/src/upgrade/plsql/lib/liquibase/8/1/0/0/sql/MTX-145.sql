--liquibase formatted sql
      

--changeSet MTX-145:1 stripComments:false
-- ADD ALERT TYPE
INSERT INTO utl_alert_type ( alert_type_id, alert_name, alert_ldesc, notify_cd, notify_class, category, message, key_bool, priority, priority_calc_class, active_bool, utl_id )
SELECT 242, 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_name', 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_description', 'ROLE', null, 'DVM', 'mxcommonejb.alert.ALL_BLOB_IN_BLOB_DATA_ARE_PROCESSED_message', 1, 0, null, 1, 0
FROM dual WHERE NOT EXISTS ( SELECT 1 FROM utl_alert_type WHERE alert_type_id = 242 );