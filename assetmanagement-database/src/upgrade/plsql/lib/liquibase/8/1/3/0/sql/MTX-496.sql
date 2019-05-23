--liquibase formatted sql


--changeSet MTX-496:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************
* Add API action config parms
****************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_PART_REQUEST_REQUEST', 
	  'Permission to search part requests',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MATERIALS', 
	  '8.1-SP3', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_RESERVE_ASSET_REQUEST', 
	  'Permission to allow reserve asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MATERIALS', 
	  '8.1-SP3', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_UNRESERVE_ASSET_REQUEST', 
	  'Permission to allow unreserve asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - MATERIALS', 
	  '8.1-SP3', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ARCHIVE_SERIAL_CONTROLLED_ASSET_REQUEST', 
	  'Permission to allow archive serial controller asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ASSET', 
	  '8.1-SP3', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_ARCHIVE_BATCH_ASSET_REQUEST', 
	  'Permission to allow archive batch asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ASSET', 
	  '8.1-SP3', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
    'API_CREATE_SERIAL_CONTROLLED_ASSET_REQUEST', 
	  'Permission to allow create serial controlled asset api call',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - ASSET', 
	  '8.1-SP3', 
	  0, 
	  0,
    UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-496:7 stripComments:false
/***************************************************************
* Add ALT_ID surrogate key column to EVT_STAGE table
****************************************************************/
ALTER TRIGGER TUBR_EVT_STAGE DISABLE;

--changeSet MTX-496:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EVT_STAGE add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-496:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_PARALLEL_PKG.PARALLEL_UPDATE_BEGIN('EVT_STAGE');

   UPDATE /*+ PARALLEL  */
      EVT_STAGE 
   SET 
      alt_id = mx_key_pkg.new_uuid() 
   WHERE 
      alt_id IS NULL;

   -- Don't need to validate constraints because we're only updating the alt_id
   UTL_PARALLEL_PKG.PARALLEL_UPDATE_END('EVT_STAGE', FALSE);

END;
/

--changeSet MTX-496:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EVT_STAGE modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-496:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_STAGE_ALT_ID" BEFORE INSERT
   ON "EVT_STAGE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-496:12 stripComments:false
ALTER TRIGGER TUBR_EVT_STAGE ENABLE;
