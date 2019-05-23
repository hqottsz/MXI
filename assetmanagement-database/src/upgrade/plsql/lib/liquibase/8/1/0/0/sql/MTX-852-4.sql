--liquibase formatted sql


--changeSet MTX-852-4:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE migrate_alt_id( p_table_name IN VARCHAR2 ) IS

   v_step          NUMBER(4);
   v_convert_flag  NUMBER(1);
   v_trigger_name  VARCHAR2(30);
   v_trigger_sql   VARCHAR2(1000);
   v_err_msg       VARCHAR2(2000);
   v_err_code      VARCHAR2(200);
   v_method_name   VARCHAR2(40);

BEGIN

   v_step := 10;
   -- Check if any deferrable constraints exist on the alt_id column
   SELECT 
      COUNT(*) 
   INTO 
      v_convert_flag
   FROM 
      DUAL
   WHERE 
      EXISTS (
         SELECT 
            1
         FROM 
            user_constraints, 
            user_cons_columns 
         WHERE
            user_constraints.constraint_name = user_cons_columns.constraint_name AND
            user_constraints.constraint_type in ('U', 'C') AND
            user_constraints.deferrable = 'DEFERRABLE' AND
            user_constraints.table_name = p_table_name AND
            user_cons_columns.column_name = 'ALT_ID'
      )
   ;

   v_step := 20;
   -- Check if any deferrable constraints exist on this column
   IF v_convert_flag = 0
   THEN
   
      v_step := 30;
      -- There are no deferrable constraints on the alt_id column, therefore, 
      -- notify the user that the constraints will not be recreated.
      dbms_output.put_line('INFO: There are no deferrable constraints on the ' || 
                            upper(p_table_name) || '.ALT_ID column, therefore, ' ||
                            'the constraints will not be recreated.');      
   
   ELSE

      -- Deferrable constraints exist on this column, therefore, recreate them
      -- without the deferrable clause.

      v_step := 40;
      -- Notify the user that the constraints are being recreated.
      dbms_output.put_line('INFO: Converting constraints on the ' || p_table_name || '.ALT_ID ' || 
                           'column to non-deferrable and removing the default.');  
                           
      v_step := 45;
      -- Drop the check constraints
      utl_migr_schema_pkg.table_column_cons_chk_drop(p_table_name, 'ALT_ID');

      v_step := 50;
      -- Drop the unique constraints
      utl_migr_schema_pkg.table_column_cons_unq_drop(p_table_name, 'ALT_ID');

      v_step := 60;
      -- Re-add the NOT NULL and unique constraints as non-deferrable
      -- and change the default to NULL (to remove the previous default)
      utl_migr_schema_pkg.table_column_modify('
         Alter table ' || p_table_name || ' modify (
            ALT_ID Raw(16) DEFAULT NULL  NOT NULL  UNIQUE 
         )
      ');

      v_step := 65;
      -- Notify the user that the constraints were recreated.
      dbms_output.put_line('INFO: The constraints on the ' || p_table_name || '.ALT_ID ' || 
                           'column were recreated as non-deferrable.');  
                           
      v_step := 70;
      -- Set the new trigger name
      v_trigger_name := 'TIBR_' || UPPER(SUBSTR(p_table_name,1,18)) || '_ALT_ID';

      v_step := 80;
      -- Set the trigger sql statement 
      v_trigger_sql :=
         'CREATE OR REPLACE TRIGGER "' || v_trigger_name || '" BEFORE INSERT' || CHR(10) ||
         '   ON "' || upper(p_table_name) || '" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW ' || CHR(10) ||
         'BEGIN' || CHR(10) ||
         '  IF :NEW.alt_id IS NULL THEN' || CHR(10) ||
         '     :NEW.alt_id := mx_key_pkg.new_uuid();' || CHR(10) ||
         '  END IF;' || CHR(10) ||
         'END;';
      
      v_step := 90;
      -- Create trigger
      EXECUTE IMMEDIATE v_trigger_sql;

      v_step := 100;
      -- Notify the user that the trigger was created.
      dbms_output.put_line('INFO: The ' || v_trigger_name ||' trigger has been created.');   
                           
   END IF;

EXCEPTION
   WHEN OTHERS THEN
      v_err_code    := SQLCODE;
      v_method_name := 'MIGRATE_ALT_ID';
      v_err_msg     := substr(v_method_name ||
                              ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

      raise_application_error(-20001,
                              v_err_msg,
                              TRUE);

END;
/

--changeSet MTX-852-4:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Convert the ALT_ID columns that were created before 8.1-SP1
BEGIN
   MIGRATE_ALT_ID('EQP_PART_NO');
END;
/

--changeSet MTX-852-4:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('INV_INV');
END;
/

--changeSet MTX-852-4:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('INV_LOC');
END;
/

--changeSet MTX-852-4:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('ORG_HR');
END;
/

--changeSet MTX-852-4:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('REQ_PART');
END;
/

--changeSet MTX-852-4:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('SCHED_STASK');
END;
/

--changeSet MTX-852-4:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('SD_FAULT');
END;
/

--changeSet MTX-852-4:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('SHIP_SHIPMENT');
END;
/

--changeSet MTX-852-4:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('TASK_TASK');
END;
/

--changeSet MTX-852-4:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('SCHED_LABOUR_ROLE');
END;
/

--changeSet MTX-852-4:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('ORG_VENDOR');
END;
/

--changeSet MTX-852-4:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('INV_OWNER');
END;
/

--changeSet MTX-852-4:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('FNC_ACCOUNT');
END;
/

--changeSet MTX-852-4:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('ORG_CARRIER');
END;
/

--changeSet MTX-852-4:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('PO_LINE');
END;
/

--changeSet MTX-852-4:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('FC_MODEL');
END;
/

--changeSet MTX-852-4:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('LIC_DEFN');
END;
/

--changeSet MTX-852-4:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('ORG_AUTHORITY');
END;
/

--changeSet MTX-852-4:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   MIGRATE_ALT_ID('EQP_ASSMBL_POS');
END;
/

--changeSet MTX-852-4:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PROCEDURE_DROP('MIGRATE_ALT_ID');
END;
/