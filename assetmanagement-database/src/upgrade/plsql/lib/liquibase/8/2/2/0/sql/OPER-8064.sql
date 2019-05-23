--liquibase formatted sql


--changeSet OPER-8064:1 stripComments:false
-- Updates the records in the UTL_TODO_TAB table to reference new tab name i18n tag for Consignment Stock Requests

UPDATE
    utl_todo_tab
SET
    todo_tab_name = 'web.todotab.CONSIGN_STOCK_REQUESTS'
WHERE
    todo_tab_cd = 'idTabConsignStockRequests' AND
    todo_tab_name = 'web.todotab.CONSIGN_STOCK_REQUESTS_TAB';

--changeSet OPER-8064:2 stripComments:false
-- Updates the records in the UTL_TODO_TAB table to reference existing tab ldesc i18n tag for Assigned Quarantine Corrective Actions
UPDATE
    utl_todo_tab
SET
    todo_tab_ldesc = 'web.todotab.ASSIGNED_QUARANTINE_ACTIONS_TAB'
WHERE
    todo_tab_cd = 'idTabQuarantineCorrectiveActions' AND
    todo_tab_ldesc = 'web.todotab.ASSIGNED_QUARANTINE_ACTIONS';