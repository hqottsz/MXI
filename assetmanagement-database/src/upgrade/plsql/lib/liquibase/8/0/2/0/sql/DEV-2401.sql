--liquibase formatted sql


--changeSet DEV-2401:1 stripComments:false
UPDATE utl_config_parm SET allow_value_desc='BOE, JASPER_SSO', modified_in='8.1' WHERE parm_name='oilconsumption.OilConsumptionReport';