--liquibase formatted sql


--changeSet OPER-6179:1 stripComments:false
-- Remove Rotable Adjustment Turnins and Issues report
DELETE FROM utl_report_type WHERE REPORT_NAME = 'Finance.RotableAdjustmentTurninsIssues';

--changeSet OPER-6179:2 stripComments:false
DELETE FROM UTL_MENU_GROUP_ITEM WHERE MENU_ID = 140023;

--changeSet OPER-6179:3 stripComments:false
DELETE FROM UTL_MENU_ITEM WHERE MENU_ID = 140023 AND MENU_NAME = 'Turnins and Issues';