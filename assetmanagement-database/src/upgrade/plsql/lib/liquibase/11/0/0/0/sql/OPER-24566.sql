--liquibase formatted sql

--changeSet OPER-24566:1 stripComments:false
INSERT INTO UTL_TODO_TAB (TODO_TAB_ID, TODO_TAB_CD, TODO_TAB_NAME, PATH, TODO_TAB_LDESC, UTL_ID)
SELECT  11026, 'idTabStockDistributionRequests', 'web.todotab.STOCK_DISTRIBUTION_REQUESTS', '/web/todolist/StockDistributionRequestsTab.jsp', 'web.todotab.STOCK_DISTRIBUTION_REQUESTS_TAB', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_TODO_TAB WHERE TODO_TAB_ID = 11026);
