--liquibase formatted sql


--changeSet MX-19632:1 stripComments:false
UPDATE UTL_CONFIG_PARM
  SET PARM_DESC = 'Permission to delete bin levels.'
 WHERE PARM_NAME = 'ACTION_DELETE_BIN_LEVEL'; 

--changeSet MX-19632:2 stripComments:false
UPDATE UTL_CONFIG_PARM
  SET PARM_DESC = 'Permission to edit bin levels.'
 WHERE PARM_NAME = 'ACTION_EDIT_BIN_LEVEL';   