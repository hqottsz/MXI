--liquibase formatted sql


--changeSet OPER-4201:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add columns control_user_id (with respective Foreign Key), control_dt to the PPC_WP table 
/********************************************************************
* Migration script for PPC Release Control functionality refactoring.
* By new requirements, we need to downgrade Control property from
* Plan/Template level to Workpackage one.
*********************************************************************/
BEGIN
   utl_migr_schema_pkg.table_column_add('
               Alter table PPC_WP add ( 
                  CONTROL_USER_ID Raw(16)
                  )
           ');

   utl_migr_schema_pkg.table_column_add('
               Alter table PPC_WP add ( 
                  CONTROL_DT Date
                  )
           ');
END;
/

--changeSet OPER-4201:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove old constraint for PPC_PLAN on control_user_id
BEGIN
   utl_migr_schema_pkg.table_column_cons_chk_drop('ppc_plan', 'control_user_id');
END;
/

--changeSet OPER-4201:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Establish new constraint for PPC_WP on control_user_id
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
               Alter table "PPC_WP" add Constraint "FK_PPCWP_CONTROLUSER" foreign key 
                     ("CONTROL_USER_ID") references "UTL_USER" ("ALT_ID")  DEFERRABLE
            ');
END;
/

--changeSet OPER-4201:4 stripComments:false
-- Propagate control_user_id and control_dt values from ppc_plan to respective columns of ppc_wp 
UPDATE ppc_wp 
SET ppc_wp.control_user_id = (SELECT control_user_id FROM ppc_plan WHERE ppc_plan.plan_id = ppc_wp.plan_id),
    ppc_wp.control_dt      = (SELECT control_dt FROM ppc_plan WHERE ppc_plan.plan_id = ppc_wp.plan_id);

--changeSet OPER-4201:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the obsolete columns control_user_id, control_dt from the PPC_PLAN table
BEGIN
   utl_migr_schema_pkg.table_column_drop('ppc_plan', 'control_user_id');
   utl_migr_schema_pkg.table_column_drop('ppc_plan', 'control_dt');
END;
/

--changeSet OPER-4201:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for Production
* Workpackage Request of the Production Workpackage API.
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PRODUCTION_WORKPACKAGE_REQUEST',
      'Permission to allow API Production Workpackage Request call.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.2-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-4201:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Create new action configuration parameter for 
* Release Control of the Production Workpackage API.
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_WORKPACKAGE_RELEASE_CONTROL_REQUEST',
      'Permission to allow API Production Workpackage release control action call.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - MAINTENANCE',
      '8.2-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet OPER-4201:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************************************
 * Delete the following records from the utl_action_config_parm table:
 *    API_PRODUCTION_RELEASE_CONTROL_REQUEST
 ******************************************************************************************************/
BEGIN
     utl_migr_data_pkg.action_parm_delete('API_PRODUCTION_RELEASE_CONTROL_REQUEST');
END;
/