--liquibase formatted sql


--changeSet 0ref_stock_dist_req_status:1 stripComments:false
/******************************************************
** INSERT SCRIPT FOR TABLE "REF_STOCK_DIST_REQ_STATUS"
** 0-Level
** DATE: 07-SEPT-2018
*******************************************************/

INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('OPEN', 0, 'Stock distribution request created.',  'Stock distribution request is automatically created when a SRVSTORE stock level is set with DISTREQ as the stock low action and when a stock low is detected.', 0, 1, 0, sysdate, sysdate, 0, user);

--changeSet 0ref_stock_dist_req_status:2 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('INPROGRESS', 0, 'Picking started for stock distribution request.',  'When one item is picked from the supplier SRVSTORE for a stock distribution request, the status of the request changes to INPROGRESS.', 0, 1, 0, sysdate, sysdate, 0, user);

--changeSet 0ref_stock_dist_req_status:3 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('PICKED', 0, 'All items picked for stock distribution request.',  'The requested quantity is picked from the supplier SRVSTORE for the stock distribution request.', 0, 1, 0, sysdate, sysdate, 0, user);

--changeSet 0ref_stock_dist_req_status:4 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('COMPLETED', 0, 'Stock transferred to requesting location.',  'All picked stock is transferred to requesting SRVSTORE.', 0, 1, 0, sysdate, sysdate, 0, user);

--changeSet 0ref_stock_dist_req_status:5 stripComments:false
INSERT INTO ref_stock_dist_req_status ( status_cd, status_db_id, desc_sdesc, desc_ldesc, rstat_cd, revision_no, ctrl_db_id, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES ('CANCELED', 0, 'Stock distribution request was canceled.',  'The stock distribution request was canceled.', 0, 1, 0, sysdate, sysdate, 0, user);

