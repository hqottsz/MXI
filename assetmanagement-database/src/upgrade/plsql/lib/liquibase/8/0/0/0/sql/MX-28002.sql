--liquibase formatted sql


--changeSet MX-28002:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/** This migration is here in case they ran the broken base */
/************************************************************************** 
 * Drop the existing unique "natural key" index on FL_LEG
 * and replace it with a non-unique index.
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_FL_LEG_NK');
END;
/

--changeSet MX-28002:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_FL_LEG" ON "FL_LEG" ("LEG_NO","AIRCRAFT_ID","AIRCRAFT_DB_ID","DEPARTURE_LOC_ID","DEPARTURE_LOC_DB_ID","ACTUAL_DEPARTURE_DT") 
   ');
END;
/

--changeSet MX-28002:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Drop the existing unique "natural key" index on USG_USAGE_RECORD
 * and replace it with a non-unique index.
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_USGUSAGERECORD_NK');
END;
/

--changeSet MX-28002:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_USGUSAGERECORD" ON "USG_USAGE_RECORD" ("INV_NO_ID","INV_NO_DB_ID","USAGE_DT")
   ');
END;
/

--changeSet MX-28002:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add a recorded date column to FL_LEG
 *
 * This column is to hold the date that the flight was recorded by Mx, 
 * so we will initialize any existing rows with the value of CREATION_DT.
 *
 * We will also update the insert trigger to populate this recorded date 
 * with the date of the insertion.
 **************************************************************************/ 
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table FL_LEG add RECORDED_DT Date
   ');
END;
/ 

--changeSet MX-28002:6 stripComments:false
UPDATE
   fl_leg
SET
   recorded_dt = creation_dt
WHERE
   recorded_dt IS NULL;
 

--changeSet MX-28002:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table FL_LEG modify RECORDED_DT Date NOT NULL DEFERRABLE
   ');
END;
/

--changeSet MX-28002:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_RECORDED_DT" BEFORE INSERT
   ON "FL_LEG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet MX-28002:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add a recorded date column to USG_USAGE_RECORD
 *
 * This column is to hold the date that the usage was recorded by Mx, 
 * so we will initialize any existing rows with the value of CREATION_DT.
 *
 * We will also update the insert trigger to populate this recorded date 
 * with the date of the insertion.
 **************************************************************************/ 
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table USG_USAGE_RECORD add RECORDED_DT Date
');
END;
/ 

--changeSet MX-28002:10 stripComments:false
UPDATE
   usg_usage_record
SET
   recorded_dt = CREATION_DT
WHERE
   recorded_dt IS NULL
;

--changeSet MX-28002:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table USG_USAGE_RECORD modify RECORDED_DT Date NOT NULL DEFERRABLE
   ');
END;
/

--changeSet MX-28002:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USG_USAGE_RECORDED_DT" BEFORE INSERT
   ON "USG_USAGE_RECORD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet MX-28002:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new ALLOW_DUPLICATE_HISTORIC_FLIGHT_MESSAGES configuration parameter.
 **************************************************************************/ 
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'ALLOW_DUPLICATE_HISTORIC_FLIGHT_MESSAGES', -- parameter name
      'LOGIC',                                    -- parameter type
      'Determines whether historic flight messages for the same flight are permitted within the flight adaptor.',
      'GLOBAL',                                   -- configuration type (GLOBAL or USER)
      'TRUE/FALSE',                               -- allowed values for the parameter
      'FALSE',                                    -- default value of the parameter
      1,                                          -- whether or not the parameter is mandatory
      'Integration',                              -- the parameter category
      '8.0-SP1',                                  -- the version in which the parm was modified
      0                                           -- the utl_id
   );
END;
/