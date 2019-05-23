--liquibase formatted sql


--changeSet vw_asb_connector_messages:1 stripComments:false
/********************************************************************************
*
* View:           vw_asb_connector_messages
*
* Description:    This view returns a unioned list of all the messages that have been logged in the ASB logging tables
*
*********************************************************************************/
CREATE OR REPLACE VIEW VW_ASB_CONNECTOR_MESSAGES
(
   message_id,
   transaction_id,
   external_id,
   message_date,
   message_type,
   message_source,
   body_blob,
   binary_blob,
   conversation_id,
   module,
   msg_type,
   server,
   sync_bool
)
AS
SELECT
   message.message_id,
   message.transaction_id,
   message.external_id,
   message.message_date,
   message.message_type,
   message.message_source,
   message.body_blob,
   message.binary_blob,
   asb_transaction_log.conversation_id,
   asb_transaction_log.module,
   asb_transaction_log.msg_type,
   asb_transaction_log.server,
   asb_transaction_log.sync_bool
   FROM
   (
      SELECT
         asb_inbound_log.msg_id AS message_id,
         'INBOUND' AS message_type,
         asb_inbound_log.transaction_id AS transaction_id,
         asb_inbound_log.external_id AS external_id,
         asb_inbound_log.msg_date AS message_date,
         asb_inbound_log.msg_source AS message_source,
         asb_inbound_log.body_blob AS body_blob,
         asb_inbound_log.binary_blob AS binary_blob
      FROM
         asb_inbound_log
      UNION ALL
      SELECT
         asb_outbound_log.msg_id AS message_id,
         'OUTBOUND' AS message_type,
         asb_outbound_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_outbound_log.msg_date AS message_date,
         asb_outbound_log.msg_dest AS message_source,
         asb_outbound_log.body_blob AS body_blob,
         asb_outbound_log.binary_blob AS binary_blob
      FROM
         asb_outbound_log
      UNION ALL
      SELECT
         asb_exception_log.exception_id AS message_id,
         'EXCEPTION' AS message_type,
         asb_exception_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_exception_log.exception_date AS message_date,
         NULL AS message_source,
         asb_exception_log.body_blob AS body_blob,
         asb_exception_log.binary_blob AS binary_blob
      FROM
         asb_exception_log
      UNION ALL
      SELECT
         asb_notification_log.notification_id AS message_id,
         'NOTIFICATION' AS message_type,
         asb_notification_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_notification_log.notification_date AS message_date,
         asb_notification_log.notification_source AS message_source,
         asb_notification_log.body_blob AS body_blob,
         asb_notification_log.binary_blob AS binary_blob
      FROM
         asb_notification_log
      UNION ALL
      SELECT
         asb_request_log.request_id AS message_id,
         'REQUEST' AS message_type,
         asb_request_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_request_log.request_date AS message_date,
         asb_request_log.request_dest AS message_source,
         asb_request_log.body_blob AS body_blob,
         asb_request_log.binary_blob AS binary_blob
      FROM
         asb_request_log
      UNION ALL
      SELECT
         asb_response_log.response_id AS message_id,
         'RESPONSE' AS message_type,
         asb_response_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_response_log.response_date AS message_date,
         asb_response_log.response_source AS message_source,
         asb_response_log.body_blob AS body_blob,
         asb_response_log.binary_blob AS binary_blob
      FROM
         asb_response_log
   ) message INNER JOIN asb_transaction_log ON
      message.transaction_id = asb_transaction_log.transaction_id;