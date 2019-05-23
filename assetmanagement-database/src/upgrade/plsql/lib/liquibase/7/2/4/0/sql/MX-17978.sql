--liquibase formatted sql


--changeSet MX-17978:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'WARN', 'FAULT_DEFERRAL_REFERENCE_VALIDATION', 'LOGIC','Controls Validation Logic for Invalid Deferral References in the Defer Fault workflow','USER', 'OFF/WARN/ERROR', 'WARN', 1,'Maint - Faults', '7.5',0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'FAULT_DEFERRAL_REFERENCE_VALIDATION' );

--changeSet MX-17978:2 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'FAULT_DEFERRAL_REFERENCE_VALIDATION' AND PARM_TYPE = 'LOGIC';