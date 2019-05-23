--liquibase formatted sql

--changeSet OPER-18263:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE SD_FAULT ADD RESOLUTION_ASSMBL_DB_ID NUMBER (10)
   ');
END;
/

--changeSet OPER-18263:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE SD_FAULT ADD RESOLUTION_ASSMBL_CD VARCHAR2 (8)
   ');
END;
/

--changeSet OPER-18263:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE SD_FAULT ADD RESOLUTION_ASSMBL_BOM_ID NUMBER (10)
   ');
END;
/

--changeSet OPER-18263:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT ADD CHECK ( RESOLUTION_ASSMBL_DB_ID BETWEEN 0 AND 4294967295 )
   ');
END;
/

--changeSet OPER-18263:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT ADD CHECK ( RESOLUTION_ASSMBL_BOM_ID BETWEEN 0 AND 4294967295 )
   ');
END;
/

--changeSet OPER-18263:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_EQPASSMBLBOM_SDFAULT ON SD_FAULT
      (
         RESOLUTION_ASSMBL_DB_ID ASC,
         RESOLUTION_ASSMBL_CD ASC,
		 RESOLUTION_ASSMBL_BOM_ID ASC
      )
   ');
END;
/

--changeSet OPER-18263:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SD_FAULT ADD CONSTRAINT FK_EQPASSMBLBOM_SDFAULT FOREIGN KEY ( RESOLUTION_ASSMBL_DB_ID, RESOLUTION_ASSMBL_CD, RESOLUTION_ASSMBL_BOM_ID ) REFERENCES EQP_ASSMBL_BOM ( ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID ) NOT DEFERRABLE
   ');
END;
/
