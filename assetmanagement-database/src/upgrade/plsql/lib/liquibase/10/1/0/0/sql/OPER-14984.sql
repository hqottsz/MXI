--liquibase formatted sql
-- if SWA-122 was previously applied by upgrading after SP1 but before SP3-u3, revert it, unless it has been activated, and re-add TrgOrderInformationById40 properly with a new id
-- if SWA-778 was previously applied by upgrading after SP2 but before SP3-u3, revert it, unless it has been activated, and re-add TrgDetailedFinancialLogById30 properly with a new id

--changeSet OPER-14984:1 stripComments:false
UPDATE utl_trigger
SET class_name = 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById30'
WHERE
    trigger_id = 99999 AND
    trigger_cd = 'MX_FNC_SEND_ORDER_INFORMATION' AND
    class_name = 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById40' AND
    active_bool = 0;
	
--changeSet OPER-14984:2 stripComments:false
UPDATE utl_trigger
SET class_name = 'com.mxi.mx.core.adapter.finance.trg.TrgDetailedFinancialLogById20'
WHERE
    trigger_id = 99982 AND
    trigger_cd = 'MX_FNC_SEND_DETAILED_FINANCIAL_LOG' AND
    class_name = 'com.mxi.mx.core.adapter.finance.trg.TrgDetailedFinancialLogById30' AND
    active_bool = 0;

--changeSet OPER-14984:3 stripComments:false
INSERT INTO 
    utl_trigger(
    trigger_id,
    trigger_cd,
    exec_order,
    type_cd,
    trigger_name,
    class_name,
    active_bool,
    utl_id
    )
SELECT
    99913,
    'MX_FNC_SEND_ORDER_INFORMATION',
    1,
    'COMPONENT',
    'Send Order Information',
    'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById40',
    0,
    0
FROM	
    dual
WHERE
    NOT EXISTS
    (
    SELECT *
    FROM
        utl_trigger
    WHERE
        trigger_id = 99913
    );


--changeSet OPER-14984:4 stripComments:false
INSERT INTO 
    utl_trigger(
    trigger_id,
    trigger_cd,
    exec_order,
    type_cd,
    trigger_name,
    class_name,
    active_bool,
    utl_id
    )
SELECT
    99912,
    'MX_FNC_SEND_DETAILED_FINANCIAL_LOG',
    1,
    'COMPONENT',
    'Send Detailed Financial Log',
    'com.mxi.mx.core.adapter.finance.trg.TrgDetailedFinancialLogById30',
    0,
    0
FROM	
    dual
WHERE
    NOT EXISTS
    (
    SELECT *
    FROM
        utl_trigger
    WHERE
        trigger_id = 99912
    );