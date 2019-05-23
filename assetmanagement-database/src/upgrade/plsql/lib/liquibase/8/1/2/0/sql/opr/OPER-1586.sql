--liquibase formatted sql


--changeSet OPER-1586:1 stripComments:false
delete from UTL_PURGE_STRATEGY where PURGE_POLICY_CD IN ('ASB_SUCCESS','ASB_FAILED');

--changeSet OPER-1586:2 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_INBOUND_LOG', 10, 
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:3 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_NOTIFICATION_LOG', 20,
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:4 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_OUTBOUND_LOG', 30, 
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:5 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_REQUEST_LOG', 40,  
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:6 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_RESPONSE_LOG',50 ,  
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:7 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_EXCEPTION_LOG', 60,  
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_EXCEPTION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:8 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_TRANSACTION_LOG', 70, 
 'NOT EXISTS' || chr(10) || '(' || chr(10) || '   SELECT' || chr(10) || '      :1' || chr(10) || '   FROM' || chr(10) || '      vw_asb_connector_messages' || chr(10) || '   WHERE' || chr(10) || '      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id' || chr(10) || ')' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:9 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_INBOUND_LOG', 10, 
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   NOT EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:10 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_NOTIFICATION_LOG', 20, 
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:11 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_OUTBOUND_LOG', 30, 
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:12 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_REQUEST_LOG', 40, 
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:13 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_RESPONSE_LOG',50 , 
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000) AND' || chr(10) || '   EXISTS' || chr(10) || '   (' || chr(10) || '      SELECT' || chr(10) || '         1' || chr(10) || '      FROM' || chr(10) || '         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON' || chr(10) || '            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID' || chr(10) || '      WHERE' || chr(10) || '         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id' || chr(10) || '   )' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:14 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_EXCEPTION_LOG', 60, 
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (:1 * 86400000)', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:15 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_TRANSACTION_LOG', 70, 
'NOT EXISTS' || chr(10) || '(' || chr(10) || '   SELECT' || chr(10) || '      :1' || chr(10) || '   FROM' || chr(10) || '      vw_asb_connector_messages' || chr(10) || '   WHERE' || chr(10) || '      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id' || chr(10) || ')' || chr(10) || '', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet OPER-1586:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE purge_pkg IS

	/****************************
   	Package Exceptions
	*****************************/
	gc_ex_purge_err CONSTANT NUMBER := -20700;
	

	/********************************************************************************

		Procedure:	purge_records
	
		Arguments:	
	
		Description:
		
			Purges stale records from the database. 

			The records to be deleted are determined by the purging policies stored in 
			UTL_PURGE_POLICY. Each policy specifies a retention period for a business 
			entity, and provides a strategy for deleting records from all the tables 
			that form the entity. 
			
			The purging strategy is represented as an set of rows in the 
			UTL_PURGE_STRATEGY table, which specify the ordered set of tables from 
			which to delete records. For each such table, a SQL predicate to use as 
			part of the WHERE clause of the delete statement.
	
	*********************************************************************************/
	PROCEDURE purge_records;

END purge_pkg;
/