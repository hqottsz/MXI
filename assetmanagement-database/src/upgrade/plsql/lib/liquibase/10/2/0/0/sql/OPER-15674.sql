--liquibase formatted sql

--changeSet OPER-15674:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table ORG_HR_SHIFT add (
	CREW_DB_ID NUMBER (10)
	)
');
END;
/

--changeSet OPER-15674:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table ORG_HR_SHIFT add (
	CREW_ID  NUMBER (10)
	)
');
END;
/

--changeSet OPER-15674:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE ORG_HR_SHIFT ADD CHECK ( CREW_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/


--changeSet OPER-15674:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE ORG_HR_SHIFT ADD CHECK ( CREW_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-15674:5 stripComments:false
  COMMENT ON COLUMN ORG_HR_SHIFT.CREW_DB_ID
IS
  'FK to ORG_WORK_DEPT.  Represents temporary crew for which the user is assigned.' ;
 

--changeSet OPER-15674:6 stripComments:false
  COMMENT ON COLUMN ORG_HR_SHIFT.CREW_ID
IS
  'FK to ORG_WORK_DEPT.  Represents temporary crew for which the user is assigned.' ;


--changeSet OPER-15674:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE ORG_HR_SHIFT ADD CONSTRAINT FK_ORGWORKDEPT_ORGHRSHIFT1 FOREIGN KEY ( CREW_DB_ID, CREW_ID ) REFERENCES ORG_WORK_DEPT ( DEPT_DB_ID, DEPT_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-15674:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('CREATE INDEX IX_ORGWORKDEPT_ORGHRSHIFTv1 ON ORG_HR_SHIFT
    (
      CREW_DB_ID ASC ,
      CREW_ID ASC
    )');
END;
/