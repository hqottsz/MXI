--liquibase formatted sql


--changeSet OPER-452:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***********************************************************************************************************
* Add new column UNIVERSAL_BOOL to table MIM_DATA_TYPE
* This new column is to indicate if the measurement is universal or not.
* By default, a measurement is universal when it is created. This also applies to the assembly measurement.
************************************************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table MIM_DATA_TYPE add (
"UNIVERSAL_BOOL" Number(1,0) Check (UNIVERSAL_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet OPER-452:2 stripComments:false
UPDATE
   MIM_DATA_TYPE
SET
   UNIVERSAL_BOOL=1
WHERE
   UNIVERSAL_BOOL IS NULL;

--changeSet OPER-452:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MIM_DATA_TYPE modify (
"UNIVERSAL_BOOL" Default 1 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-452:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************************************************
* Create a new table EQP_ASSMBL_DATA_TYPE to store the mappings from assembly to datatype if it is assembly specific  
* measurement. For measurement with UNIVERSAL_BOOL=1 and measurement that is not applicable to any assembly, 
* no mappings will be stored. 
*********************************************************************************************************************/
BEGIN
  utl_migr_schema_pkg.table_create('
    Create table EQP_ASSMBL_DATA_TYPE (
      ASSMBL_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      ASSMBL_CD VARCHAR2(8) NOT NULL DEFERRABLE,
      DATA_TYPE_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      DATA_TYPE_ID Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE ,
     Constraint PK_EQP_ASSMBL_DATA_TYPE primary key (DATA_TYPE_DB_ID,DATA_TYPE_ID,ASSMBL_DB_ID,ASSMBL_CD) 
    )'
  );
END;
/

--changeSet OPER-452:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table EQP_ASSMBL_DATA_TYPE add Constraint FK_MIMRSTAT_EQPASSMBLDATATYPE foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet OPER-452:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
        Alter table EQP_ASSMBL_DATA_TYPE add Constraint FK_EQPASSMBL_EQPASSMBLDATATYPE foreign key (ASSMBL_DB_ID,ASSMBL_CD) references EQP_ASSMBL (ASSMBL_DB_ID,ASSMBL_CD)  DEFERRABLE
      ');
END;
/

--changeSet OPER-452:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
        Alter table EQP_ASSMBL_DATA_TYPE add Constraint FK_DATATYPE_EQPASSMBLDATATYPE foreign key (DATA_TYPE_DB_ID,DATA_TYPE_ID) references MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID)  DEFERRABLE
      ');
END;
/

--changeSet OPER-452:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_EQP_EQPASSMBLDATATYPE" ON "EQP_ASSMBL_DATA_TYPE" ("ASSMBL_DB_ID","ASSMBL_CD") 
');
END;
/ 

--changeSet OPER-452:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_DATA_EQPASSMBLDATATYPE" ON "EQP_ASSMBL_DATA_TYPE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/ 

--changeSet OPER-452:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQPASSMBL_DATATYPE" BEFORE INSERT
   ON "EQP_ASSMBL_DATA_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-452:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_EQPASSMBL_DATATYPE" BEFORE UPDATE
   ON "EQP_ASSMBL_DATA_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-452:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/****************************************************************************************************************
*Add a new action parameter for button Assign Assembly on the Applicable Assembly Tab page
*
*****************************************************************************************************************/
BEGIN
 
utl_migr_data_pkg.action_parm_insert(
 'ACTION_ASSIGN_ASSEMBLY',
 'Permission to assign assembly to a measurement',
 'TRUE/FALSE',
 'FALSE',
 1,
 'Assembly - Usage Definitions',
 '8.1-SP2',
 0,
 0,
UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
);
 
END;
/

--changeSet OPER-452:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/****************************************************************************************************************
*Add a new action parameter for button UnAssign Assembly on the Applicable Assembly Tab page
*
*****************************************************************************************************************/
BEGIN
 
utl_migr_data_pkg.action_parm_insert(
 'ACTION_UNASSIGN_ASSEMBLY',
 'Permission to unassign assembly from a measurement',
 'TRUE/FALSE',
 'FALSE',
 1,
 'Assembly - Usage Definitions',
 '8.1-SP2',
 0,
 0,
UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
);
 
END;
/