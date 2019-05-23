--liquibase formatted sql

--changeSet OPER-17638:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$

BEGIN
   upg_migr_schema_v1_pkg.table_column_add('ALTER TABLE MIM_PART_NUMDATA add(
         ASSMBL_PART_NO_DB_ID NUMBER (10)
      )'
   );
END;
/

--changeSet OPER-17638:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('ALTER TABLE MIM_PART_NUMDATA add(
         ASSMBL_PART_NO_ID NUMBER (10)
      )
   ');
END;
/

--changeSet OPER-17638:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('ALTER TABLE MIM_PART_NUMDATA add(
         AGGREGATED_DATA_TYPE_DB_ID NUMBER (10)
      )'
   );
END;
/

--changeSet OPER-17638:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_column_add('ALTER TABLE MIM_PART_NUMDATA add(
         AGGREGATED_DATA_TYPE_ID NUMBER (10)
      )
   ');
END;
/

--changeSet OPER-17638:5 stripComments:false
  COMMENT ON COLUMN MIM_PART_NUMDATA.ASSMBL_PART_NO_DB_ID
IS
  'FK to EQP_PART_NO which is the assembly part number the data type associated to.' ;

--changeSet OPER-17638:6 stripComments:false
  COMMENT ON COLUMN MIM_PART_NUMDATA.ASSMBL_PART_NO_ID
IS
  'FK to EQP_PART_NO which is the assembly part number the data type associated to.' ;

--changeSet OPER-17638:7 stripComments:false
  COMMENT ON COLUMN MIM_PART_NUMDATA.AGGREGATED_DATA_TYPE_DB_ID
IS
  'FK to MIM_DATA_TYPE referencing to the super data type which contains the sum of values of the assembly part-specific usage parameters at each thrust rating.' ;

--changeSet OPER-17638:8 stripComments:false
  COMMENT ON COLUMN MIM_PART_NUMDATA.AGGREGATED_DATA_TYPE_ID
IS
  'FK to MIM_DATA_TYPE referencing to the super data type which contains the sum of values of the assembly part-specific usage parameters at each thrust rating.' ;

--changeSet OPER-17638:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('ALTER TABLE MIM_PART_NUMDATA ADD CONSTRAINT FK_EQPPARTNO_MIMPARTNUMDATA FOREIGN KEY ( ASSMBL_PART_NO_DB_ID, ASSMBL_PART_NO_ID ) REFERENCES EQP_PART_NO ( PART_NO_DB_ID, PART_NO_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-17638:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('ALTER TABLE MIM_PART_NUMDATA ADD CONSTRAINT FK_AGDATATYPE_MIMPARTNUMDATA FOREIGN KEY ( AGGREGATED_DATA_TYPE_DB_ID, AGGREGATED_DATA_TYPE_ID ) REFERENCES MIM_DATA_TYPE ( DATA_TYPE_DB_ID, DATA_TYPE_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-17638:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('CREATE INDEX IX_ASSMBL_PART_NO ON MIM_PART_NUMDATA
    (
      ASSMBL_PART_NO_DB_ID ASC ,
      ASSMBL_PART_NO_ID ASC
    )');
END;
/

--changeSet OPER-17638:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.index_create('CREATE INDEX IX_AG_DATATYPE ON MIM_PART_NUMDATA
    (
      AGGREGATED_DATA_TYPE_DB_ID ASC ,
      AGGREGATED_DATA_TYPE_ID ASC
    )');
END;
/
