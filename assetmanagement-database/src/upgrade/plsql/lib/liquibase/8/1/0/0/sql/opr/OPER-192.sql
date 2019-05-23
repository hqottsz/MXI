--liquibase formatted sql


--changeSet OPER-192:1 stripComments:false
-- RotableAdjustment
    UPDATE UTL_REPORT_TYPE
       SET 
           REPORT_ENGINE_TYPE = 'JASPER_SSO'
           WHERE
           REPORT_NAME = 'inventory.RotableAdjustment'
;

--changeSet OPER-192:2 stripComments:false
-- SummaryInvFncLog
    UPDATE UTL_REPORT_TYPE
       SET
           REPORT_ENGINE_TYPE = 'JASPER_SSO'
           WHERE
           REPORT_NAME = 'inventory.SummaryInvFncLog'
;

--changeSet OPER-192:3 stripComments:false
-- DetailInvFncLog 
    UPDATE UTL_REPORT_TYPE
       SET
           REPORT_ENGINE_TYPE = 'JASPER_SSO'
           WHERE
           REPORT_NAME = 'inventory.DetailInvFncLog' 
;