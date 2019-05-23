--liquibase formatted sql


--changeSet MX-19802:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
UTL_MIGR_DATA_PKG.config_parm_insert( 'ACTION_EDIT_WORKCAPTURE_START_VALUE', 
                                      'SECURED_RESOURCE', 
                                      'Permission to edit start values for a task on the Work Capture page.', 
                                      'USER', 
                                      'TRUE/FALSE', 
                                      'FALSE', 
                                      1, 
                                      'Maint - Tasks', 
                                      '7.5', 
                                      0, 
                                      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/

--changeSet MX-19802:2 stripComments:false
UPDATE UTL_CONFIG_PARM SET MODIFIED_IN = '7.5' WHERE PARM_NAME = 'ACTION_EDIT_WORKCAPTURE_START_VALUE' AND PARM_TYPE = 'SECURED_RESOURCE';