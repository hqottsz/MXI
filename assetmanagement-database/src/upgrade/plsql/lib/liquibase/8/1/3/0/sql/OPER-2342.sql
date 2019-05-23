--liquibase formatted sql


--changeSet OPER-2342:1 stripComments:false
DELETE FROM INT_BP_LOOKUP WHERE ROOT_NAME = 'trigger_external_maintenix_message';