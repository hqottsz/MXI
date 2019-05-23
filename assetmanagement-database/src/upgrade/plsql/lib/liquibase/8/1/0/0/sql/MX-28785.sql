--liquibase formatted sql


--changeSet MX-28785:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- remove lpr.PlanComparisionReport
BEGIN
   utl_migr_data_pkg.config_parm_delete('lpr.PlanComparisionReport');
END;
/

--changeSet MX-28785:2 stripComments:false
-- Migration step removed.  Superseded by the data move to UTL_REPORT_TYPE (DEV-2516)
-- update munu item label    
UPDATE utl_menu_item
SET menu_name = 'web.dev.menuitem.PLAN_COMPARISON_REPORT'
WHERE menu_name = 'web.dev.menuitem.PLAN_COMPARISION_REPORT';