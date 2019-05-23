--liquibase formatted sql
 

--changeSet MX-26460:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter SCHEDULE_WORK_PACK_EXTERNALLY_BY_DEFAULT
**************************************************************************/
BEGIN
  UTL_MIGR_DATA_PKG.config_parm_insert( 
     'SCHEDULE_WORK_PACK_EXTERNALLY_BY_DEFAULT',
     'MXWEB', 
     'Indicates if a work package will always be scheduled externally.', 
     'USER', 
     'TRUE/FALSE', 
     'FALSE', 
     1, 
     'Maint - Work Packages', 
     '1209', 
     0);
END;
/