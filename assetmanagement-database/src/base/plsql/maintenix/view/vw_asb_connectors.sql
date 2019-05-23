--liquibase formatted sql


--changeSet vw_asb_connectors:1 stripComments:false
/********************************************************************************
*
* View:           VW_ASB_CONNECTORS
*
* Description:    This view returns a sorted list of the distinct modules (connectors) and message types (connector messages) that have messages logged against them in the ASB
*
*********************************************************************************/
CREATE OR REPLACE VIEW VW_ASB_CONNECTORS
(
	module,
   msg_type
)
AS
SELECT
   asb_transaction_log.module,
   asb_transaction_log.msg_type
FROM
   asb_transaction_log
GROUP BY
   asb_transaction_log.module,
   asb_transaction_log.msg_type;