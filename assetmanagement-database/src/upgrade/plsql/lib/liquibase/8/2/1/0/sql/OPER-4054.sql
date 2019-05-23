--liquibase formatted sql


--changeSet OPER-4054:1 stripComments:false
DELETE FROM UTL_PURGE_STRATEGY
WHERE 
   purge_policy_cd = 'ASB_FAILED' and
   purge_table_cd = 'ASB_INBOUND_LOG'
;

--changeSet OPER-4054:2 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_INBOUND_LOG', 10, 
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI')
;