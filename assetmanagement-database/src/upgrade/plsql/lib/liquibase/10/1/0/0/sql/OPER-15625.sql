--liquibase formatted sql

--changeSet OPER-15625:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add table level check constraint CK_EQPMANUFACT_MANUFACTCD to table EQP_MANUFACT to enforce natural key
DECLARE

   lv_table_name          VARCHAR2(30)    := 'EQP_MANUFACT';
   lv_column_name         VARCHAR2(30)    := 'MANUFACT_CD';
   lv_constraint_name     VARCHAR2(30)    := 'CK_EQPMANUFACT_MANUFACTCD';
   lv_conversion_prefix   VARCHAR2(30)    := 'TRIM(UPPER(';
   lv_conversion_suffix   VARCHAR2(30)    := '))';

   TYPE typ_object_names IS TABLE OF VARCHAR2(30) INDEX BY PLS_INTEGER;
   larr_constr_name     typ_object_names;
   larr_table_name      typ_object_names;
   larr_column_name     typ_object_names;
   larr_cons_status     typ_object_names;
   ln_index             NUMBER      := 0;
   ln_count             NUMBER;

BEGIN


   -- Check if any records in parent table require data conversion
   EXECUTE IMMEDIATE
      'SELECT COUNT(*) FROM ' || lv_table_name || ' WHERE ' ||
      lv_column_name || ' <> ' || lv_conversion_prefix || lv_column_name || lv_conversion_suffix
   INTO
      ln_count;

   -- Skip data conversion if it is not required
   IF ln_count = 0 THEN
   
      GOTO apply_constraint;
   
   END IF;

   -- Check if the automatic conversion is possible
   EXECUTE IMMEDIATE
      'SELECT COUNT(*) FROM (SELECT 1 FROM ' || lv_table_name || ' GROUP BY ' ||
      lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ' HAVING COUNT(*) > 1)'
   INTO
      ln_count;

   IF ln_count > 0 THEN
   
      RAISE_APPLICATION_ERROR
      (
         -20000,
         'Unable to perform automatic conversion of ' || lv_table_name || '.' || lv_column_name ||
         ' to trimmed uppercase values because some records become duplcates after such conversion.'
      );
   
   END IF;

   -- Store into in-memory array the FK child constraint name, table name and column name 
   FOR lcur_cons IN
   (
      SELECT
         user_cons_columns.constraint_name,
         user_cons_columns.table_name,
         user_cons_columns.column_name,
         user_constraints.status
      FROM
         user_cons_columns pk_cons_columns
         INNER JOIN user_constraints pk_constraints ON
            pk_constraints.constraint_name = pk_cons_columns.constraint_name AND
            pk_constraints.constraint_type = 'P' 
         INNER JOIN user_constraints ON
            user_constraints.r_constraint_name = pk_constraints.constraint_name
         INNER JOIN user_cons_columns ON
            user_cons_columns.constraint_name  = user_constraints.constraint_name AND
            user_cons_columns.position         = pk_cons_columns.position
      WHERE
         pk_cons_columns.table_name  = lv_table_name AND
         pk_cons_columns.column_name = lv_column_name
      ORDER BY
         -- Process the FK constraints first and then PK constraint
         user_constraints.constraint_type DESC
   )
   LOOP
   
      ln_index := ln_index + 1;

      larr_constr_name(ln_index)  := lcur_cons.constraint_name;
      larr_table_name(ln_index)   := lcur_cons.table_name;
      larr_column_name(ln_index)  := lcur_cons.column_name;
      larr_cons_status(ln_index)  := lcur_cons.status;
      
   END LOOP;
   
   -- Disable selected FK constraints
   FOR ln_index IN 1..larr_constr_name.COUNT LOOP
   
      IF larr_cons_status(ln_index) = 'ENABLED' THEN

         EXECUTE IMMEDIATE
            'ALTER TABLE ' || larr_table_name(ln_index) || ' DISABLE CONSTRAINT "' || larr_constr_name(ln_index) || '"';

      END IF;

   END LOOP;
   
   -- Convert to trimmed uppercase value the corresponding column on FK child tables
   FOR ln_index IN 1..larr_constr_name.COUNT LOOP
   
      EXECUTE IMMEDIATE
         'UPDATE ' || larr_table_name(ln_index) || ' SET ' || larr_column_name(ln_index) || ' = ' ||
         lv_conversion_prefix || larr_column_name(ln_index) || lv_conversion_suffix || ' WHERE ' ||
         larr_column_name(ln_index) || ' <> ' || lv_conversion_prefix || larr_column_name(ln_index) || lv_conversion_suffix;

   END LOOP;

   -- Convert to trimmed uppercase the column value on parent table
   EXECUTE IMMEDIATE
      'UPDATE ' || lv_table_name || ' SET ' || lv_column_name || ' = ' ||
      lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ' WHERE ' ||
      lv_column_name || ' <> ' || lv_conversion_prefix || lv_column_name || lv_conversion_suffix;
      
   COMMIT;
   
   -- Enable selected FK constraints
   FOR ln_index IN REVERSE 1..larr_constr_name.COUNT LOOP
   
      IF larr_cons_status(ln_index) = 'ENABLED' THEN

         EXECUTE IMMEDIATE 
            'ALTER TABLE ' || larr_table_name(ln_index) || ' ENABLE CONSTRAINT "' || larr_constr_name(ln_index) || '"';

      END IF;

   END LOOP;

   -- If the above data conversion succeeded or skipped then apply the check constraint
  <<apply_constraint>>
  
   upg_migr_schema_v1_pkg.table_constraint_add
   (
      'ALTER TABLE ' || lv_table_name || ' ADD CONSTRAINT "' || lv_constraint_name || '" CHECK ( ' ||
      lv_column_name || ' = ' || lv_conversion_prefix || lv_column_name || lv_conversion_suffix || ')'
   );

END;
/ 
