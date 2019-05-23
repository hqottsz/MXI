--liquibase formatted sql

--changeSet OPER-17884:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add table level check constraint CK_EQPPARTNO_PARTNOOEM to table EQP_PART_NO
DECLARE

   lv_table_name          VARCHAR2(30)    := 'EQP_PART_NO';
   lv_column_name         VARCHAR2(30)    := 'PART_NO_OEM';
   lv_column_name_2       VARCHAR2(30)    := 'MANUFACT_CD';
   lv_constraint_name     VARCHAR2(30)    := 'CK_EQPPARTNO_PARTNOOEM';
   lv_conversion_prefix   VARCHAR2(30)    := 'UPPER(TRIM(';
   lv_conversion_suffix   VARCHAR2(30)    := '))';

   ln_count               NUMBER;
  
BEGIN

   -- Check if any records in parent table require data conversion
   EXECUTE IMMEDIATE
      'SELECT COUNT(*) FROM ' || lv_table_name ||
      ' WHERE '||
      lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ' <> ' || lv_column_name
   INTO
      ln_count;

   -- Skip data conversion if it is not required
   IF ln_count = 0 THEN
   
      GOTO apply_constraint;
   
   END IF;

   -- Check if any records in parent table require data conversion
   EXECUTE IMMEDIATE
      'SELECT COUNT(*) FROM (SELECT 1 FROM ' || lv_table_name ||
      ' GROUP BY ' ||
      lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ', ' ||
      lv_conversion_prefix || lv_column_name_2 || lv_conversion_suffix ||
      ' HAVING COUNT(*) > 1)'
   INTO
      ln_count;

   -- Check if the automatic conversion is possible
   IF ln_count > 0 THEN
   
      RAISE_APPLICATION_ERROR
      (
         -20000,
         'Unable to perform automatic conversion of ' || lv_table_name || '.' || lv_column_name ||
         ' to trimmed uppercase values because some records become duplicates after such conversion.'
      );
   
   END IF;

   -- No need to check for FK constraints or disable constraints for eqp_part_no

   -- Convert to trimmed uppercase the column value 
   EXECUTE IMMEDIATE
      'UPDATE ' || lv_table_name || ' SET ' || lv_column_name || ' = ' ||
      lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ' WHERE ' ||
      lv_column_name || ' <> ' || lv_conversion_prefix || lv_column_name || lv_conversion_suffix;
      
   COMMIT;
   
   -- If the above data conversion succeeded or skipped then apply the check constraint
  <<apply_constraint>>
  
   upg_migr_schema_v1_pkg.table_constraint_add
   (
      'ALTER TABLE ' || lv_table_name || ' ADD CONSTRAINT "' || lv_constraint_name || '" CHECK ( ' ||
      lv_column_name || ' = ' || lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ')'
   );

END;
/ 

--changeSet OPER-17884:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Ensure that manufact_cd constraint is UPPER(TRIM

BEGIN

-- if manufact_cd constraint exists drop it so that it can be recreated to as UPPER(TRIM
   utl_migr_schema_pkg.table_constraint_drop('EQP_MANUFACT', 'CK_EQPMANUFACT_MANUFACTCD');

-- Add constraint for manufact_cd UPPER(TRIM
   upg_migr_schema_v1_pkg.table_constraint_add
      (
         'ALTER TABLE EQP_MANUFACT ADD CONSTRAINT CK_EQPMANUFACT_MANUFACTCD CHECK (MANUFACT_CD = UPPER(TRIM(MANUFACT_CD)))'
      );
END;
/
