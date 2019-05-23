--liquibase formatted sql


--changeSet QC-4567:1 stripComments:false
UPDATE UTL_CONFIG_PARM
 SET MODIFIED_IN = '7.1-SP1'
 WHERE MODIFIED_IN = '7.2';