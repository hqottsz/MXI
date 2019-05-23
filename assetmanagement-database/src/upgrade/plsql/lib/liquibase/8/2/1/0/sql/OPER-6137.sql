--liquibase formatted sql


--changeSet OPER-6137:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- table definition
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create (
        'CREATE TABLE TASK_WEIGHT_BALANCE
	 (
	    TASK_WEIGHT_BALANCE_DB_ID NUMBER (10) NOT NULL ,
	    TASK_WEIGHT_BALANCE_ID    NUMBER (10) NOT NULL ,
	    TASK_DB_ID                NUMBER (10) NOT NULL ,
	    TASK_ID                   NUMBER (10) NOT NULL ,
	    PART_NO_DB_ID             NUMBER (10) ,
	    PART_NO_ID                NUMBER (10) ,
	    WEIGHT                    NUMBER (15,5) ,
	    MOMENT                    NUMBER (15,5) ,
	    ALT_ID RAW (16) NOT NULL ,
	    RSTAT_CD       NUMBER (3) NOT NULL ,
	    CREATION_DT    DATE NOT NULL ,
	    REVISION_DT    DATE NOT NULL ,
	    REVISION_DB_ID NUMBER (10) NOT NULL ,
	    REVISION_USER  VARCHAR2 (30) NOT NULL
	  )
	  LOGGING'

   );

END;
/     

--changeSet OPER-6137:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- indexes
BEGIN
   utl_migr_schema_pkg.index_create('      
      CREATE INDEX IX_TASKWEIGHTBAL_TASKTASK ON TASK_WEIGHT_BALANCE
        (
          TASK_DB_ID ASC ,
          TASK_ID ASC
        )
   ');
END;
/  

--changeSet OPER-6137:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('      
      CREATE INDEX IX_TASKWEIGHTBAL_EQPPARTNO ON TASK_WEIGHT_BALANCE
        (
          PART_NO_DB_ID ASC ,
          PART_NO_ID ASC
        )
   ');
END;
/   

--changeSet OPER-6137:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- constraints
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
        'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT PK_TASK_WEIGHT_BALANCE PRIMARY KEY ( TASK_WEIGHT_BALANCE_DB_ID, TASK_WEIGHT_BALANCE_ID )'
   );
  
END;
/      

--changeSet OPER-6137:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
     'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT IX_TASKWEIGHTBALANCE_ALTID UNIQUE ( ALT_ID )'
   );
  
END;
/      

--changeSet OPER-6137:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT FK_TASKWEIGHTBAL_EQPPARTNO FOREIGN KEY ( PART_NO_DB_ID, PART_NO_ID ) REFERENCES EQP_PART_NO ( PART_NO_DB_ID, PART_NO_ID ) NOT DEFERRABLE'
   );
  
END;
/      

--changeSet OPER-6137:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT FK_TASKWEIGHTBAL_MIMDB FOREIGN KEY ( TASK_WEIGHT_BALANCE_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
  
END;
/    

--changeSet OPER-6137:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT FK_TASKWEIGHTBAL_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE'
   );
  
END;
/  

--changeSet OPER-6137:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CONSTRAINT FK_TASKWEIGHTBAL_TASKTASK FOREIGN KEY ( TASK_DB_ID, TASK_ID ) REFERENCES TASK_TASK ( TASK_DB_ID, TASK_ID ) NOT DEFERRABLE'
   );
  
END;
/  

--changeSet OPER-6137:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))'
   );
  
END;
/  

--changeSet OPER-6137:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE TASK_WEIGHT_BALANCE ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)'
   );
  
END;
/          

--changeSet OPER-6137:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- triggers (insertupdate)    
CREATE OR REPLACE TRIGGER "TIBR_TASK_WEIGHT_BALANCE" BEFORE INSERT
   ON "TASK_WEIGHT_BALANCE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet OPER-6137:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TASK_WEIGHT_BALANCE" BEFORE UPDATE
   ON "TASK_WEIGHT_BALANCE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet OPER-6137:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- sequence
BEGIN
   utl_migr_schema_pkg.sequence_create('TASK_WEIGHT_BALANCE_ID_SEQ', 100000);
END;
/

--changeSet OPER-6137:15 stripComments:false
INSERT 
INTO 
   utl_sequence ( 
	    sequence_cd, 
      next_value, 
      table_name, 
      column_name, 
      oracle_seq, 
      utl_id 
   )
SELECT 
   'TASK_WEIGHT_BALANCE_ID_SEQ', 
   100000, 
   'TASK_WEIGHT_BALANCE', 
   'TASK_WEIGHT_BALANCE_ID' , 
   1, 
   0
FROM
	 dual
WHERE
	 NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'TASK_WEIGHT_BALANCE_ID_SEQ' );

--changeSet OPER-6137:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- INSERT ADDEDITREMOVE wieght balance to UTL_ACTION_CONFIG_PARM
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_EDIT_REQ_WEIGHT_BALANCE', 
      'Permission to edit weight and balance to a requirement.',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'Maint Program - Requirements', 
      '8.2-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
  
  utl_migr_data_pkg.action_parm_insert(
      'ACTION_REMOVE_REQ_WEIGHT_BALANCE', 
      'Permission to remove weight and balance to a requirement.',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'Maint Program - Requirements', 
      '8.2-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
  
  utl_migr_data_pkg.action_parm_insert(
      'ACTION_ADD_REQ_WEIGHT_BALANCE', 
      'Permission to add weight and balance to a requirement.',
      'TRUE/FALSE',    
      'FALSE',  
      1, 
      'Maint Program - Requirements', 
      '8.2-SP2', 
      0, 
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );
END;
/ 

--changeSet OPER-6137:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- INSERT WEIGHT_BALANCE_WEIGHTWEIGHT_BLANACE_MOMENT UNIT
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'WEIGHT_BALANCE_WEIGHT_UNIT',
      p_parm_type        => 'MXWEB',
      p_parm_desc        => 'Unit of measurement for weight used by the Weight and Balance feature.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => '',
      p_default_value    => 'LBS',
      p_mand_config_bool => 1,
      p_category         => 'Maint - Tasks',
      p_modified_in      => '8.2-SP2',
      p_utl_id           => 0
   );
   
END;
/

--changeSet OPER-6137:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'WEIGHT_BALANCE_MOMENT_UNIT',
      p_parm_type        => 'MXWEB',
      p_parm_desc        => 'Unit of measurement for moment used by the Weight and Balance feature.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => '',
      p_default_value    => 'IN-LBS',
      p_mand_config_bool => 1,
      p_category         => 'Maint - Tasks',
      p_modified_in      => '8.2-SP2',
      p_utl_id           => 0
   );
   
END;
/ 

--changeSet OPER-6137:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add TASK_WEIGHT_BALANCE_ATL_ID
CREATE OR REPLACE TRIGGER "TIBR_TASK_WEIGHT_BAL_ALT_ID" BEFORE INSERT
   ON "TASK_WEIGHT_BALANCE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/ 