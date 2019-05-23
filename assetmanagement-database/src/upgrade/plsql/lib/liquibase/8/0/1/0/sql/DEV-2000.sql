--liquibase formatted sql


--changeSet DEV-2000:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************
* Add the new column TALLY_LINE_ORD to the SCHED_WO_LINE table
*********************************************************************/
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SCHED_WO_LINE add (
         "TALLY_LINE_ORD" Number(10,0)
      )
   ');
END;
/

--changeSet DEV-2000:2 stripComments:false
/**********************************************************
* update TALLY_LINE_ORD with WO_LINE_ID
***********************************************************/
UPDATE 
       sched_wo_line
SET 
       tally_line_ord = wo_line_id
WHERE
       tally_line_ord IS NULL
;

--changeSet DEV-2000:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************
* Add the NOT NULL constraint to the new column 
*********************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_WO_LINE modify (
         "TALLY_LINE_ORD" Number(10,0) NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet DEV-2000:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
* Create new configuration parameter ACTION_EDIT_TALLY_LINE_NO
**************************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_EDIT_TALLY_LINE_NO',
      'Permission to reorder the Tally Line Order in Workscope.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.0-SP2',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/