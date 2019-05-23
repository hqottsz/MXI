--liquibase formatted sql

--changeset OPER-26432:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add column SCHED_WP.MANUAL_USAGE_BOOL
BEGIN

   upg_migr_schema_v1_pkg.table_column_add('
      ALTER TABLE SCHED_WP ADD MANUAL_USAGE_BOOL NUMBER(1)
   ');
   
END;
/

--changeset OPER-26432:2 stripComments:false
--comment update column SCHED_WP.MANUAL_USAGE_BOOL
UPDATE
   SCHED_WP
SET
   MANUAL_USAGE_BOOL = 0   
WHERE
   MANUAL_USAGE_BOOL IS NULL;

--changeset OPER-26432:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment modify column SCHED_WP.MANUAL_USAGE_BOOL
BEGIN

   upg_migr_schema_v1_pkg.table_column_modify('
      Alter table SCHED_WP modify (
         MANUAL_USAGE_BOOL Number(1) DEFAULT 0 NOT NULL
      )
   ');

END;
/

--changeset OPER-26432:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 utl_migr_schema_pkg.table_constraint_add ('
    ALTER TABLE SCHED_WP ADD CHECK ( MANUAL_USAGE_BOOL IN (0,1)) 
 ');
 
END;
/

--changeSet OPER-26432:5 stripComments:false
COMMENT ON COLUMN SCHED_WP.MANUAL_USAGE_BOOL
IS
  'If a user enters or edits usage manually, the value is True. If Maintenix autocalculates usage based on historical information in the system, the value is False.' ;

