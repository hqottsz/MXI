--liquibase formatted sql

--changeSet OPER-25015-2:1 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'OPEN', 0, 'Stock distribution request created.',  'Stock distribution request is automatically created when a SRVSTORE stock level is set with DISTREQ as the stock low action and when a stock low is detected.', 0, 1, 0, sysdate, sysdate, 0, user
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ref_stock_dist_req_status WHERE status_db_id = 0 AND status_cd = 'OPEN' );

--changeSet OPER-25015-2:2 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'INPROGRESS', 0, 'Picking started for stock distribution request.',  'When one item is picked from the supplier SRVSTORE for a stock distribution request, the status of the request changes to INPROGRESS.', 0, 1, 0, sysdate, sysdate, 0, user 
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ref_stock_dist_req_status WHERE status_db_id = 0 AND status_cd = 'INPROGRESS' );

--changeSet OPER-25015-2:3 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'PICKED', 0, 'All items picked for stock distribution request.',  'The requested quantity is picked from the supplier SRVSTORE for the stock distribution request.', 0, 1, 0, sysdate, sysdate, 0, user
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ref_stock_dist_req_status WHERE status_db_id = 0 AND status_cd = 'PICKED' );


--changeSet OPER-25015-2:4 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'COMPLETED', 0, 'Stock transferred to requesting location.',  'All picked stock is transferred to requesting SRVSTORE.', 0, 1, 0, sysdate, sysdate, 0, user
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ref_stock_dist_req_status WHERE status_db_id = 0 AND status_cd = 'COMPLETED' );

--changeSet OPER-25015-2:5 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 'CANCELED', 0, 'Stock distribution request was canceled.',  'The stock distribution request was canceled.', 0, 1, 0, sysdate, sysdate, 0, user
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ref_stock_dist_req_status WHERE status_db_id = 0 AND status_cd = 'CANCELED' );


--changeSet OPER-25015-2:6 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'STOCK_DIST_REQ_LOG_ID_SEQ', 1, 'STOCK_DIST_REQ_LOG', 'STOCK_DIST_REQ_LOG_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'STOCK_DIST_REQ_LOG_ID_SEQ');

 
--changeSet OPER-25015:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('STOCK_DIST_REQ_LOG_ID_SEQ', 1);
END;
/