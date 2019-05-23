--liquibase formatted sql


--changeSet DEV-2291:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- dropping action_cd column from arc_asset 
BEGIN  
   utl_migr_schema_pkg.table_column_drop('ARC_ASSET', 'ACTION_CD');
END;
/

--changeSet DEV-2291:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- adding val_result_cd column to arc_asset 
BEGIN  
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ARC_ASSET ADD(
         VAL_RESULT_CD VARCHAR2(20)
      )
   ');
END;
/