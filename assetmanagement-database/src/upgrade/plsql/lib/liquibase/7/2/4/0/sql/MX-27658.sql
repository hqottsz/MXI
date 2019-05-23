--liquibase formatted sql


--changeSet MX-27658:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: START OF SCRIPT
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Consolidated data upgrade migration script for
-- v8 FlightUsage Data Model Improvements
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
utl_migr_schema_pkg.table_create('
Create table "USG_USAGE_RECORD" (
    "USAGE_RECORD_ID" Raw(16) NOT NULL DEFERRABLE ,
    "USAGE_TYPE_CD" Varchar2 (12) NOT NULL DEFERRABLE ,
    "USAGE_NAME" Varchar2 (500) NOT NULL DEFERRABLE ,
    "USAGE_DESC" Varchar2 (4000),
    "DOCUMENT_REF" Varchar2 (80),
    "EXT_KEY" Varchar2 (80),
    "INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USAGE_DT" Date NOT NULL DEFERRABLE ,
    "NEGATED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (NEGATED_BOOL IN (0, 1) ) DEFERRABLE ,
    "RECORD_DT" Date NOT NULL DEFERRABLE ,
    "RECORD_HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RECORD_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RECORD_HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (RECORD_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "APPLIED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (APPLIED_BOOL IN (0, 1) ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (21),
    "RECORDED_DT" Date NOT NULL DEFERRABLE ,
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_USG_USAGE_RECORD" primary key ("USAGE_RECORD_ID")
)
');
END;
/

--changeSet MX-27658:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_USAGE_TYPE" (
    "USAGE_TYPE_CD" Varchar2 (12) NOT NULL DEFERRABLE ,
    "DISPLAY_CODE" Varchar2 (12) NOT NULL DEFERRABLE ,
    "DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
    "DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
    "DISPLAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "BITMAP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "BITMAP_TAG" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_TAG BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_USAGE_TYPE" primary key ("USAGE_TYPE_CD")
)
');
END;
/

--changeSet MX-27658:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "USG_USAGE_DATA" (
    "USAGE_DATA_ID" Raw(16) NOT NULL DEFERRABLE ,
    "USAGE_RECORD_ID" Raw(16) NOT NULL DEFERRABLE ,
    "INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_ORD" Number(4,0),
    "ASSMBL_INV_NO_DB_ID" Number(10,0) Check (ASSMBL_INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ASSMBL_INV_NO_ID" Number(10,0) Check (ASSMBL_INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ASSMBL_DB_ID" Number(10,0) Check (ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ASSMBL_CD" Varchar2 (8) ,
    "ASSMBL_BOM_ID" Number(10,0) Check (ASSMBL_BOM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ASSMBL_POS_ID" Number(10,0) Check (ASSMBL_POS_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "TSN_QT" Float NOT NULL DEFERRABLE ,
    "TSO_QT" Float NOT NULL DEFERRABLE ,
    "TSI_QT" Float NOT NULL DEFERRABLE ,
    "TSN_DELTA_QT" Float NOT NULL DEFERRABLE ,
    "TSO_DELTA_QT" Float NOT NULL DEFERRABLE ,
    "TSI_DELTA_QT" Float NOT NULL DEFERRABLE ,
    "NEGATED_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (NEGATED_BOOL IN (0, 1) ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (54),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_USG_USAGE_DATA" primary key ("USAGE_DATA_ID")
)
');
END;
/

--changeSet MX-27658:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG" (
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_NO" Varchar2 (500) NOT NULL DEFERRABLE ,
    "LEG_DESC" Varchar2 (4000),
    "MASTER_FLIGHT_NO" Varchar2 (80),
    "EXT_KEY" Varchar2 (80),
    "HIST_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (HIST_BOOL IN (0, 1) ) DEFERRABLE ,
    "FLIGHT_LEG_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
    "FLIGHT_REASON_CD" Varchar2 (8),
    "FLIGHT_TYPE_DB_ID" Number(10,0) Check (FLIGHT_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "FLIGHT_TYPE_CD" Varchar2 (8),
    "ETOPS_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ETOPS_BOOL IN (0, 1) ) DEFERRABLE ,
    "AIRCRAFT_DB_ID" Number(10,0) Check (AIRCRAFT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "AIRCRAFT_ID" Number(10,0) Check (AIRCRAFT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "INV_CAPABILITY_DB_ID" Number(10,0) Check (INV_CAPABILITY_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "INV_CAPABILITY_CD" Varchar2 (8),
    "LOGBOOK_REF" Varchar2 (80),
    "USAGE_RECORD_ID" Raw(16),
    "DEPARTURE_LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DEPARTURE_LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DEPARTURE_LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DEPARTURE_LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DEPARTURE_GATE_CD" Varchar2 (8),
    "SCHED_DEPARTURE_DT" Date,
    "ACTUAL_DEPARTURE_DT" Date,
    "OFF_DT" Date,
    "ARRIVAL_LOC_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ARRIVAL_LOC_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ARRIVAL_LOC_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ARRIVAL_LOC_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PLAN_ASSMBL_DB_ID" Number(10,0) Check (PLAN_ASSMBL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "PLAN_ASSMBL_CD" Varchar2 (8),
    "ARRIVAL_GATE_CD" Varchar2 (8),
    "SCHED_ARRIVAL_DT" Date,
    "ACTUAL_ARRIVAL_DT" Date NOT NULL DEFERRABLE ,
    "ON_DT" Date,
    "LEGACY_KEY" Varchar2 (21),
    "RECORDED_DT" Date NOT NULL DEFERRABLE ,
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG" primary key ("LEG_ID")
)
');
END;
/

--changeSet MX-27658:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_NOTE" (
    "FLIGHT_NOTE_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "ENTRY_DT" Date NOT NULL DEFERRABLE ,
    "ENTRY_ORD" Number(10,0) NOT NULL DEFERRABLE ,
    "SYSTEM_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SYSTEM_BOOL IN (0, 1) ) DEFERRABLE ,
    "HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "ENTRY_NOTE" Varchar2 (4000) NOT NULL DEFERRABLE ,
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (32),
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_NOTE" primary key ("FLIGHT_NOTE_ID")
)
');
END;
/

--changeSet MX-27658:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_FAIL_EFFECT" (
    "LEG_FAIL_EFFECT_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "FAIL_EFFECT_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_EFFECT_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "FAIL_EFFECT_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "FAIL_EFFECT_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_EFFECT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "FAIL_EFFECT_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_EFFECT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "EFFECT_DT" Date NOT NULL DEFERRABLE ,
    "EFFECT_DESC" Varchar2 (80),
    "FLIGHT_STAGE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FLIGHT_STAGE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "FLIGHT_STAGE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (32),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_FAIL_EFFECT" primary key ("LEG_FAIL_EFFECT_ID")
)
');
END;
/

--changeSet MX-27658:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_FLIGHT_REASON" (
    "FLIGHT_REASON_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "DISPLAY_CODE" Varchar2 (8) NOT NULL DEFERRABLE ,
    "DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
    "DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
    "DISPLAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "BITMAP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "BITMAP_TAG" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_TAG BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (19),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_FLIGHT_REASON" primary key ("FLIGHT_REASON_CD")
)
');
END;
/

--changeSet MX-27658:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_FLIGHT_LEG_STATUS" (
    "FLIGHT_LEG_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
    "DISPLAY_CODE" Varchar2 (16) NOT NULL DEFERRABLE ,
    "DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
    "DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
    "DISPLAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "BITMAP_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "BITMAP_TAG" Number(10,0) NOT NULL DEFERRABLE  Check (BITMAP_TAG BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (27),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_NO BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_FLIGHT_LEG_STATUS" primary key ("FLIGHT_LEG_STATUS_CD")
)
');
END;
/

--changeSet MX-27658:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_STATUS_LOG" (
    "LEG_STATUS_LOG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LOG_DT" Date NOT NULL DEFERRABLE ,
    "FLIGHT_LEG_STATUS_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
    "SYSTEM_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SYSTEM_BOOL IN (0, 1) ) DEFERRABLE ,
    "HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "USER_NOTE_ID" Raw(16),
    "SYSTEM_NOTE_ID" Raw(16),
    "LEGACY_KEY" Varchar2 (32),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_STATUS_LOG" primary key ("LEG_STATUS_LOG_ID")
)
');
END;
/

--changeSet MX-27658:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_MEASUREMENT" (
    "LEG_MEASUREMENT_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_TYPE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DATA_TYPE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_ORD" Number(4,0) NOT NULL DEFERRABLE ,
    "DATA_VALUE_DB_ID" Number(10,0) Check (DATA_VALUE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DATA_VALUE_CD" Varchar2 (8),
    "DATA_QT" Float,
    "DATA_DT" Date,
    "DATA_TEXT" Varchar2 (4000),
    "DATA_BOOL" Number(1,0) Default 0 Check (DATA_BOOL IN (0, 1) ) DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (76),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_MEASUREMENT" primary key ("LEG_MEASUREMENT_ID")
)
');
END;
/

--changeSet MX-27658:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_DISRUPT" (
    "LEG_DISRUPT_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_ID" Raw(16) NOT NULL DEFERRABLE ,
    "EXT_REF" Varchar2 (80),
    "FLIGHT_STAGE_DB_ID" Number(10,0) Check (FLIGHT_STAGE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "FLIGHT_STAGE_CD" Varchar2 (8),
    "SCHED_DB_ID" Number(10,0) Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "SCHED_ID" Number(10,0) Check (SCHED_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DELAY_CODE_DB_ID" Number(10,0) Check (DELAY_CODE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DELAY_CODE_CD" Varchar2 (8),
    "MAINT_DELAY_TIME_QT" Float,
    "TECH_DELAY_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (TECH_DELAY_BOOL IN (0, 1) ) DEFERRABLE ,
    "DISRUPTION_DESC" Varchar2 (80),
    "DISRUPTION_NOTE" Varchar2 (4000),
    "LEGACY_KEY" Varchar2 (21),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_NO BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_DISRUPT" primary key ("LEG_DISRUPT_ID")
)
');
END;
/

--changeSet MX-27658:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "FL_LEG_DISRUPT_TYPE" (
    "LEG_DISRUPT_TYPE_ID" Raw(16) NOT NULL DEFERRABLE ,
    "LEG_DISRUPT_ID" Raw(16) NOT NULL DEFERRABLE ,
    "DISRUPT_TYPE_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (DISRUPT_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "DISRUPT_TYPE_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
    "LEGACY_KEY" Varchar2 (41),
    "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
    "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
    "CREATION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DT" Date NOT NULL DEFERRABLE ,
    "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
    "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_FL_LEG_DISRUPT_TYPE" primary key ("LEG_DISRUPT_TYPE_ID")
)
');
END;
/

--changeSet MX-27658:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EVT_FAIL_EFFECT add (
    "LEG_ID" Raw(16)
)
');
END;
/

--changeSet MX-27658:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_AC_FLIGHT_PLAN add (
    "ARR_LEG_ID" Raw(16)
)
');
END;
/

--changeSet MX-27658:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table INV_AC_FLIGHT_PLAN add (
    "DEP_LEG_ID" Raw(16)
)
');
END;
/

--changeSet MX-27658:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SD_FAULT add (
    "LEG_ID" Raw(16)
)
');
END;
/

--changeSet MX-27658:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "REF_USAGE_TYPE_NK" ON "REF_USAGE_TYPE" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_FLLEGFAILEFFECT_NK" ON "FL_LEG_FAIL_EFFECT" ("LEG_ID","FAIL_EFFECT_TYPE_CD","FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_ID","FAIL_EFFECT_DB_ID","EFFECT_DT")
');
END;
/

--changeSet MX-27658:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGFAILEFF_FLLEG_FK" ON "FL_LEG_FAIL_EFFECT" ("LEG_ID")
');
END;
/

--changeSet MX-27658:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGFAILEFF_REFFAILETYP_FK" ON "FL_LEG_FAIL_EFFECT" ("FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_TYPE_CD")
');
END;
/

--changeSet MX-27658:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGFAILEFF_FAILEFF" ON "FL_LEG_FAIL_EFFECT" ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID")
');
END;
/

--changeSet MX-27658:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGFAILEFF_REFFLSTG_FK" ON "FL_LEG_FAIL_EFFECT" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")
');
END;
/

--changeSet MX-27658:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGFAILEFF_MIMDB_FK" ON "FL_LEG_FAIL_EFFECT" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "REF_FLIGHT_REASON_NK" ON "REF_FLIGHT_REASON" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "REF_FLIGHT_LEG_STATUS_NK" ON "REF_FLIGHT_LEG_STATUS" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_FLLEGDISRUPT_NK" ON "FL_LEG_DISRUPT" ("LEG_ID","DELAY_CODE_CD","DELAY_CODE_DB_ID")
');
END;
/

--changeSet MX-27658:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_DIS_FLLEG_FK" ON "FL_LEG_DISRUPT" ("LEG_ID")
');
END;
/

--changeSet MX-27658:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_DIS_REFFLSTG_FK" ON "FL_LEG_DISRUPT" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")
');
END;
/

--changeSet MX-27658:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_DIS_SCHEDSTASK_FK" ON "FL_LEG_DISRUPT" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-27658:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_DIS_REFDELCD_FK" ON "FL_LEG_DISRUPT" ("DELAY_CODE_DB_ID","DELAY_CODE_CD")
');
END;
/

--changeSet MX-27658:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_FLLEGDISRUPTTYPE_NK" ON "FL_LEG_DISRUPT_TYPE" ("LEG_DISRUPT_ID","DISRUPT_TYPE_CD","DISRUPT_TYPE_DB_ID")
');
END;
/

--changeSet MX-27658:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGDISTYPE_FLLEGDIS_FK" ON "FL_LEG_DISRUPT_TYPE" ("LEG_DISRUPT_ID")
');
END;
/

--changeSet MX-27658:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGDISTYPE_REFDISTYP_FK" ON "FL_LEG_DISRUPT_TYPE" ("DISRUPT_TYPE_DB_ID","DISRUPT_TYPE_CD")
');
END;
/

--changeSet MX-27658:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGDISTYPE_MIMDB_FK" ON "FL_LEG_DISRUPT_TYPE" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_FLEFFECT_FLLEGFLEFF" foreign key ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID") references "FAIL_EFFECT" ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_USAGE_TYPE" add Constraint "FK_MIMDB_REFUSTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT_TYPE" add Constraint "FK_MIMDB_FLLEGDISTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_MIMDB_FLLEGFAILEFF" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_REASON" add Constraint "FK_MIMDB_REFFLREASON" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_MIMDB_FLLEGDIS" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_LEG_STATUS" add Constraint "FK_MIMDB_REFFLLEGSTAT" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_USAGE_TYPE" add Constraint "FK_REFBMAP_REFUSTYPE" foreign key ("BITMAP_DB_ID","BITMAP_TAG") references "REF_BITMAP" ("BITMAP_DB_ID","BITMAP_TAG")  DEFERRABLE
');
END;
/

--changeSet MX-27658:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_REASON" add Constraint "FK_REFBMAP_REFFLREASON" foreign key ("BITMAP_DB_ID","BITMAP_TAG") references "REF_BITMAP" ("BITMAP_DB_ID","BITMAP_TAG")  DEFERRABLE
');
END;
/

--changeSet MX-27658:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_LEG_STATUS" add Constraint "FK_REFBMAP_REFFLSTATUS" foreign key ("BITMAP_DB_ID","BITMAP_TAG") references "REF_BITMAP" ("BITMAP_DB_ID","BITMAP_TAG")  DEFERRABLE
');
END;
/

--changeSet MX-27658:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_REFFAILEFFTYP_FLLEGFAILEF" foreign key ("FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_TYPE_CD") references "REF_FAIL_EFFECT_TYPE" ("FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_REFFLSTG_FLLEGDIS" foreign key ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD") references "REF_FLIGHT_STAGE" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_REFFLSTG_FLLEGFAILEFF" foreign key ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD") references "REF_FLIGHT_STAGE" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_SCHEDSTASK_FLLEGDIS" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_USAGE_TYPE" add Constraint "FK_MIMRSTAT_REFUSTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT_TYPE" add Constraint "FK_MIMRSTAT_FLLEGDISTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_MIMRSTAT_FLLEGFAILEFF" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_REASON" add Constraint "FK_MIMRSTAT_REFFLREASON" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_MIMRSTAT_FLLEGDIS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FLIGHT_LEG_STATUS" add Constraint "FK_MIMRSTAT_REFFLLEGSTAT" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT_TYPE" add Constraint "FK_REFDISTYPE_FLLEGDISTYPE" foreign key ("DISRUPT_TYPE_DB_ID","DISRUPT_TYPE_CD") references "REF_DISRUPT_TYPE" ("DISRUPT_TYPE_DB_ID","DISRUPT_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_REFDELCODE_FLLEGDIS" foreign key ("DELAY_CODE_DB_ID","DELAY_CODE_CD") references "REF_DELAY_CODE" ("DELAY_CODE_DB_ID","DELAY_CODE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT" add Constraint "FK_FLLEG_FLLEGDISRUPT" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_FAIL_EFFECT" add Constraint "FK_FLLEG_FLLEGFAILEFF" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EVT_FAIL_EFFECT" add Constraint "FK_FLLEG_EVTFAILEFF" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "INV_AC_FLIGHT_PLAN" add Constraint "FK_ARRFLLEG_INVACFLPLAN" foreign key ("ARR_LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "INV_AC_FLIGHT_PLAN" add Constraint "FK_DEPFLLEG_INVACFLPLAN" foreign key ("DEP_LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_DISRUPT_TYPE" add Constraint "FK_FLLEGDIS_FLLEGDISTYP" foreign key ("LEG_DISRUPT_ID") references "FL_LEG_DISRUPT" ("LEG_DISRUPT_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "MX_KEY_PKG" IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new Type 1 UUID value using the
*                 java-uuid-generator library.  This is loaded into Oracle via
*                 the Type1UUIDGenerator.sql script.
*
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE;

END MX_KEY_PKG;
/

--changeSet MX-27658:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_KEY_PKG"
IS

/********************************************************************************
*
* Function:       new_uuid
* Returns:        A new UUID value
*
* Description:    This function returns a new Type 1 UUID value using the
*                 java-uuid-generator library.  This is loaded into Oracle via
*                 the Type1UUIDGenerator.sql script.
*
*********************************************************************************
*
* Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
   FUNCTION new_uuid RETURN RAW PARALLEL_ENABLE AS
      LANGUAGE JAVA NAME 'com.mxi.uuid.Type1UUIDGenerator.generate() return byte[]';

END MX_KEY_PKG;
/

--changeSet MX-27658:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************/
    /* Copyright 2000-2010 Mxi Technologies, Ltd. All Rights Reserved.      */
    /* Any distribution of the MxI Maintenix source code by any other party */
    /* than MxI Technologies Ltd is prohibited.                             */
    /************************************************************************/
    CREATE OR REPLACE PACKAGE "MX_TRIGGER_PKG" IS
    /************************************************************************/
    /* Object Name : MX_TRIGGER_PKG                                 */
    /* Object Type : Package Header                                         */
    /* Date        : OCT-5-2010                                         */
    /* Coder       : sdevi                                             */
    /* Recent Date : OCT-5-2010                                             */
    /* Recent Coder:                                               */
    /* Description :                                                        */
    /* This package contains procedures to set audit and other common attributes on insert and update. */
    /************************************************************************/

   /* Procedure to set the audit attributes on insert */
   PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                            an_revision_no    IN OUT NUMBER,
                            an_ctrl_db_id     IN OUT NUMBER,
                            adt_creation_dt   IN OUT DATE,
                            an_creation_db_id IN OUT NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2);

   /* Procedure to set the audit attributes on update */
   PROCEDURE before_update(an_old_rstat_cd   IN NUMBER,
                            an_new_rstat_cd   IN NUMBER,
                            an_old_revision_no IN NUMBER,
                            an_new_revision_no IN OUT NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2);

END MX_TRIGGER_PKG;
/

--changeSet MX-27658:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************/
    /* Copyright 2000-2010 Mxi Technologies, Ltd. All Rights Reserved.      */
    /* Any distribution of the MxI Maintenix source code by any other party */
    /* than MxI Technologies Ltd is prohibited.                             */
    /************************************************************************/
CREATE OR REPLACE PACKAGE BODY "MX_TRIGGER_PKG" IS

   /************************************************************************/
    /* Object Name : MX_TRIGGER_PKG                                 */
    /* Object Type : Package Header                                 */
    /* Date        : OCT-5-2010                                     */
    /* Coder       : sdevi                                          */
    /* Recent Date : OCT-5-2010                                     */
    /* Recent Coder:                                                */
    /* Description :                                                */
    /* This package contains procedures to set audit and other common attributes on insert and update. */
    /************************************************************************/

   /*--------------------------------------------------------
   *  INSTANCE VARIABLES
   *---------------------------------------------------------*/

   /* The update error number and description */
   in_audit_upd_error NUMBER := -20001;
   iv_audit_upd_error_inactive VARCHAR2(32767) := 'Cannot update a non-active (non-0 rstat_cd) record.';
   iv_audit_upd_error_optlock VARCHAR2(32767) := 'Cannot update a record with stale data: the supplied revision_no is less than the current revision_no.';

/*******************************************************************************
   *
   *  Procedure:  before_insert
   *  Description:   Called by the TIBR_* triggers on all tables, this procedure will
   *     will set the auditting attributes (creation and revision
   *     information) for the new record.
   *
   *  Arguments:  an_rstat_cd (number) - record status code
   *     an_revision_no (number) - revision number
   *     an_ctrl_db_id (number) - control database id
   *     adt_creation_dt (date) - creation date
   *     an_creation_db_id (number) - creation database id
   *     adt_revision_dt (date) - revision date
   *     an_revision_db_id (number) - revision database id
   *     an_revision_user (varchar2) - revision user
   *  Returns: none
   *
   *  Original Coder: sdevi
   *  Recent Coder:
   *  Last Modified: OCT-5-2010
   *********************************************************************************/
  PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                            an_revision_no    IN OUT NUMBER,
                            an_ctrl_db_id     IN OUT NUMBER,
                            adt_creation_dt   IN OUT DATE,
                            an_creation_db_id IN OUT NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2) IS
   BEGIN

      -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
      IF application_object_pkg.gv_avoid_trigger = TRUE
      THEN
         -- Audit information only updated for live records
         IF an_rstat_cd <> 0
         THEN
            RETURN;
         ELSE
            an_ctrl_db_id  := NVL(an_ctrl_db_id, application_object_pkg.getdbid);
            an_creation_db_id := NVL(an_creation_db_id, application_object_pkg.getdbid);
            -- if revision number not provided then set it to 1
            IF an_revision_no = 0 OR an_revision_no IS NULL
            THEN
                an_revision_no := 1 ;
            END IF;
            adt_revision_dt   := application_object_pkg.gettimestamp;
            an_revision_db_id := application_object_pkg.getdbid;
            av_revision_user  := application_object_pkg.getuser;
         END IF;
         RETURN;
      END IF;

      IF an_rstat_cd IS NULL
      THEN
         an_rstat_cd := 0;
      END IF;
      adt_creation_dt := NVL(adt_creation_dt, application_object_pkg.gettimestamp);
      an_ctrl_db_id  := NVL(an_ctrl_db_id, application_object_pkg.getdbid);
      an_creation_db_id := NVL(an_creation_db_id, application_object_pkg.getdbid);
      -- if revision number not provided then set it to 1
      IF an_revision_no = 0 OR an_revision_no IS NULL
        THEN
           an_revision_no := 1 ;
      END IF;
      adt_revision_dt   := application_object_pkg.gettimestamp;
      an_revision_db_id := application_object_pkg.getdbid;
      av_revision_user  := application_object_pkg.getuser;
   END before_insert;

   /*******************************************************************************
   *
   *  Procedure:  before_update
   *  Description:   Called by the TUBR_* triggers on all tables, this procedure will
   *     will set the auditting attributes (revision
   *     information) for the record.
   *
   *  Arguments:  an_new_rstat_cd (number) - new record status code
   *     an_old_rstat_cd (number) - old record status code
   *     an_old_revision_no (number) - old record revision number
   *     an_new_revision_no (number) - new record revision number
   *     adt_revision_dt (date) - revision date
   *     an_revision_db_id (number) - revision database id
   *     an_revision_user (varchar2) - revision user
   *  Returns: none
   *
   *  Original Coder:sdevi
   *  Recent Coder:
   *  Last Modified: OCT-5-2010
   *********************************************************************************/
   PROCEDURE before_update (an_old_rstat_cd   IN NUMBER,
                            an_new_rstat_cd   IN NUMBER,
                            an_old_revision_no IN NUMBER,
                            an_new_revision_no IN OUT NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2 ) IS
   ln_error NUMBER := 0;
   lv_error VARCHAR2(32767) := '';
   BEGIN

      -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
      IF application_object_pkg.gv_avoid_trigger = TRUE
      THEN
         -- Audit info only updated for those records that are being updated to a new
         -- rstat_cd
         IF an_old_rstat_cd = an_new_rstat_cd
         THEN
            RETURN;
         ELSE
            adt_revision_dt   := application_object_pkg.gettimestamp;
            an_revision_db_id := application_object_pkg.getdbid;
            av_revision_user  := application_object_pkg.getuser;
            IF an_new_revision_no = an_old_revision_no
            THEN
               an_new_revision_no := an_old_revision_no + 1;
            ELSE
              IF an_new_revision_no < an_old_revision_no
              THEN
                   ln_error := in_audit_upd_error;
                   lv_error := iv_audit_upd_error_optlock;
               END IF;
            END IF;
         END IF;
         RETURN;
      END IF;

      -- If the old rstat_cd is non-active (non-0) then raise an error if the rstat_cd
      -- is changed except when gv_skip_rstat_upd_errors has been set to true (for cases
      -- such as migrations) where this errors should be skipped.
      IF an_old_rstat_cd <> 0
         AND an_old_rstat_cd = an_new_rstat_cd
         AND application_object_pkg.gv_skip_rstat_upd_errors = FALSE
      THEN
         --raise exception
         ln_error := in_audit_upd_error;
         lv_error := iv_audit_upd_error_inactive;
      ELSE
         adt_revision_dt   := application_object_pkg.gettimestamp;
         an_revision_db_id := application_object_pkg.getdbid;
         av_revision_user  := application_object_pkg.getuser;
         IF an_new_revision_no = an_old_revision_no
         THEN
             an_new_revision_no := an_old_revision_no + 1;
         ELSE
              IF an_new_revision_no < an_old_revision_no
              THEN
                   ln_error := in_audit_upd_error;
                   lv_error := iv_audit_upd_error_optlock;
               END IF;
         END IF;
      END IF;
    -- raise error if there is any
    IF ln_error <> 0 THEN
       RAISE_APPLICATION_ERROR (ln_error, lv_error);
    END IF;
   END before_update;

END MX_TRIGGER_PKG;
/

--changeSet MX-27658:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_DISRUPT_TYPE" BEFORE INSERT
   ON "FL_LEG_DISRUPT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_DISRUPT_TYPE" BEFORE UPDATE
   ON "FL_LEG_DISRUPT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_DISRUPT" BEFORE INSERT
   ON "FL_LEG_DISRUPT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_DISRUPT" BEFORE UPDATE
   ON "FL_LEG_DISRUPT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_FAIL_EFFECT" BEFORE INSERT
   ON "FL_LEG_FAIL_EFFECT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_FAIL_EFFECT" BEFORE UPDATE
   ON "FL_LEG_FAIL_EFFECT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_FLIGHT_REASON" BEFORE INSERT
   ON "REF_FLIGHT_REASON" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_FLIGHT_REASON" BEFORE UPDATE
   ON "REF_FLIGHT_REASON" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_FLIGHT_LEG_STATUS" BEFORE INSERT
   ON "REF_FLIGHT_LEG_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_FLIGHT_LEG_STATUS" BEFORE UPDATE
   ON "REF_FLIGHT_LEG_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_USAGE_TYPE" BEFORE INSERT
   ON "REF_USAGE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_USAGE_TYPE" BEFORE UPDATE
   ON "REF_USAGE_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:79 stripComments:false
CREATE OR REPLACE VIEW vw_evt_flight(
flight_id,
flight_sdesc,
flight_ldesc,
flight_status_cd,
flight_status_user_cd,
flight_reason_cd,
flight_reason_user_cd,
flight_type_db_id,
flight_type_cd,
inv_capability_db_id,
inv_capability_cd,
etops_bool,
sched_dep_dt,
sched_arr_dt,
actual_dep_dt,
actual_arr_dt,
dep_location_db_id,
dep_location_id,
dep_gate_cd,
dep_airport_cd,
arr_location_db_id,
arr_location_id,
arr_gate_cd,
arr_airport_cd,
up_dt,
down_dt,
master_flight_sdesc,
ext_key_sdesc,
doc_ref_sdesc,
hist_bool,
acft_inv_no_db_id,
acft_inv_no_id,
acft_inv_no_sdesc,
acft_assmbl_db_id,
acft_assmbl_cd,
acft_part_no_db_id,
acft_part_no_id,
acft_part_no_sdesc,
acft_part_no_oem
) AS
SELECT
fl_leg.leg_id as flight_id,
fl_leg.leg_no as flight_sdesc,
fl_leg.leg_desc as flight_ldesc,
ref_flight_leg_status.flight_leg_status_cd as flight_status_cd,
ref_flight_leg_status.display_code as flight_status_user_cd,
fl_leg.flight_reason_cd as flight_reason_cd,
ref_flight_reason.display_code as flight_reason_user_cd,
fl_leg.flight_type_db_id,
fl_leg.flight_type_cd,
fl_leg.inv_capability_db_id,
fl_leg.inv_capability_cd,
fl_leg.etops_bool,
fl_leg.sched_departure_dt as sched_dep_dt,
fl_leg.sched_arrival_dt as sched_arr_dt,
fl_leg.actual_departure_dt as actual_dep_dt,
fl_leg.actual_arrival_dt as actual_arr_dt,
fl_leg.departure_loc_db_id,
fl_leg.departure_loc_id,
fl_leg.departure_gate_cd,
dep_loc.loc_cd as dep_airport_cd,
fl_leg.arrival_loc_db_id,
fl_leg.arrival_loc_id,
fl_leg.arrival_gate_cd,
arr_loc.loc_cd as arr_airport_cd,
fl_leg.off_dt as up_dt,
fl_leg.on_dt as down_dt,
fl_leg.master_flight_no as master_flight_sdesc,
fl_leg.ext_key as ext_key_sdesc,
fl_leg.logbook_ref as doc_ref_sdesc,
fl_leg.hist_bool as hist_bool,
fl_leg.aircraft_db_id as acft_inv_no_db_id,
fl_leg.aircraft_id as acft_inv_no_id,
inv_inv.inv_no_sdesc,
inv_inv.assmbl_db_id,
inv_inv.assmbl_cd,
inv_inv.part_no_db_id,
inv_inv.part_no_id,
eqp_part_no.part_no_sdesc,
eqp_part_no.part_no_oem as acft_part_no_oem
FROM
fl_leg
INNER JOIN ref_flight_leg_status ON
fl_leg.flight_leg_status_cd = ref_flight_leg_status.flight_leg_status_cd
INNER JOIN inv_loc dep_loc ON
fl_leg.departure_loc_db_id = dep_loc.loc_db_id AND
fl_leg.departure_loc_id = dep_loc.loc_id
INNER JOIN inv_loc arr_loc ON
fl_leg.arrival_loc_db_id = arr_loc.loc_db_id AND
fl_leg.arrival_loc_id = arr_loc.loc_id
INNER JOIN inv_inv ON
fl_leg.aircraft_db_id = inv_inv.inv_no_db_id AND
fl_leg.aircraft_id = inv_inv.inv_no_id
INNER JOIN eqp_part_no ON
inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
inv_inv.part_no_id = eqp_part_no.part_no_id
LEFT OUTER JOIN ref_flight_reason ON
fl_leg.flight_reason_cd = ref_flight_reason.flight_reason_cd;

--changeSet MX-27658:80 stripComments:false
CREATE OR REPLACE VIEW VW_EVT_FAULT(
fault_db_id,
fault_id,
fault_sdesc,
bitmap_db_id,
bitmap_tag,
bitmap_name,
editor_hr_db_id,
editor_hr_id,
editor_hr_sdesc,
fault_status_db_id,
fault_status_cd,
fault_status_user_cd,
fault_ldesc,
fault_dt,
fault_gdt,
raised_dt,
raised_gdt,
ext_key_sdesc,
doc_ref_sdesc,
hist_bool,
sub_event_ord,
fail_mode_db_id,
fail_mode_id,
fail_catgry_db_id,
fail_catgry_cd,
flight_stage_db_id,
flight_stage_cd,
fail_sev_db_id,
fail_sev_cd,
fail_priority_db_id,
fail_priority_cd,
fail_type_db_id,
fail_type_cd,
fault_source_db_id,
fault_source_cd,
found_by_hr_db_id,
found_by_hr_id,
fail_defer_db_id,
fail_defer_cd,
defer_cd_sdesc,
defer_ref_sdesc,
prec_proc_ldesc,
sdr_bool,
eval_bool,
maint_evt_bool,
op_restriction_ldesc,
log_inv_no_db_id,
log_inv_no_id,
log_inv_no_sdesc,
log_nh_inv_no_db_id,
log_nh_inv_no_id,
log_nh_inv_no_sdesc,
log_assmbl_inv_no_db_id,
log_assmbl_inv_no_id,
log_assmbl_inv_no_sdesc,
log_h_inv_no_db_id,
log_h_inv_no_id,
log_h_inv_no_sdesc,
log_assmbl_db_id,
log_assmbl_cd,
log_assmbl_bom_id,
log_assmbl_pos_id,
log_assmbl_bom_cd,
log_eqp_pos_cd,
log_bom_part_db_id,
log_bom_part_id,
log_bom_part_cd,
log_part_no_db_id,
log_part_no_id,
log_part_no_sdesc,
log_part_no_oem,
corrective_sched_db_id,
corrective_sched_id,
corrective_sched_sdesc,
previous_incident_db_id,
previous_incident_id,
previous_incident_sdesc,
previous_incident_raised_dt,
previous_sched_db_id,
previous_sched_id,
previous_sched_sdesc,
next_incident_db_id,
next_incident_id,
next_incident_sdesc,
next_sched_db_id,
next_sched_id,
next_sched_sdesc,
parent_flight_id,
parent_flight_sdesc,
parent_flight_type_cd,
parent_sched_db_id,
parent_sched_id,
parent_sched_sdesc,
parent_barcode_sdesc,
contact_info_sdesc,
init_fault_db_id,
init_fault_id,
init_fault_sdesc)
AS
SELECT /*+ rule */
      sd_fault.fault_db_id,
      sd_fault.fault_id,
      evt_event.event_sdesc,
      evt_event.bitmap_db_id,
      evt_event.bitmap_tag,
      ref_bitmap.bitmap_name,
      evt_event.editor_hr_db_id,
      evt_event.editor_hr_id,
      DECODE( evt_event.editor_hr_db_id, NULL, NULL, org_hr.hr_cd || ' (' || utl_user.last_name || ', ' || utl_user.first_name || ')' ),
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      ref_event_status.user_status_cd,
      evt_event.event_ldesc,
      evt_event.event_dt,
      evt_event.event_gdt,
      evt_event.actual_start_dt,
      evt_event.actual_start_gdt,
      evt_event.ext_key_sdesc,
      evt_event.doc_ref_sdesc,
      evt_event.hist_bool,
      evt_event.sub_event_ord,
      sd_fault.fail_mode_db_id,
      sd_fault.fail_mode_id,
      sd_fault.fail_catgry_db_id,
      sd_fault.fail_catgry_cd,
      sd_fault.flight_stage_db_id,
      sd_fault.flight_stage_cd,
      sd_fault.fail_sev_db_id,
      sd_fault.fail_sev_cd,
      sd_fault.fail_priority_db_id,
      sd_fault.fail_priority_cd,
      sd_fault.fail_type_db_id,
      sd_fault.fail_type_cd,
      sd_fault.fault_source_db_id,
      sd_fault.fault_source_cd,
      sd_fault.found_by_hr_db_id,
      sd_fault.found_by_hr_id,
      sd_fault.fail_defer_db_id,
      sd_fault.fail_defer_cd,
      sd_fault.defer_cd_sdesc,
      sd_fault.defer_ref_sdesc,
      sd_fault.prec_proc_ldesc,
      sd_fault.sdr_bool,
      sd_fault.eval_bool,
      sd_fault.maint_evt_bool,
      sd_fault.op_restriction_ldesc,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      inv_inv.inv_no_sdesc,
      evt_inv.nh_inv_no_db_id,
      evt_inv.nh_inv_no_id,
      nh_inv_inv.inv_no_sdesc,
      evt_inv.assmbl_inv_no_db_id,
      evt_inv.assmbl_inv_no_id,
      assmbl_inv_inv.inv_no_sdesc,
      evt_inv.h_inv_no_db_id,
      evt_inv.h_inv_no_id,
      h_inv_inv.inv_no_sdesc,
      evt_inv.assmbl_db_id,
      evt_inv.assmbl_cd,
      evt_inv.assmbl_bom_id,
      evt_inv.assmbl_pos_id,
      eqp_assmbl_bom.assmbl_bom_cd,
      eqp_assmbl_pos.eqp_pos_cd,
      evt_inv.bom_part_db_id,
      evt_inv.bom_part_id,
      eqp_bom_part.bom_part_cd,
      evt_inv.part_no_db_id,
      evt_inv.part_no_id,
      eqp_part_no.part_no_sdesc,
      eqp_part_no.part_no_oem,
      corrective_sched_event.event_db_id,
      corrective_sched_event.event_id,
      corrective_sched_event.event_sdesc,
      previous_incident_event.event_db_id,
      previous_incident_event.event_id,
      previous_incident_event.event_sdesc,
      previous_incident_event.actual_start_dt,
      previous_sched_event.event_db_id,
      previous_sched_event.event_id,
      previous_sched_event.event_sdesc,
      next_incident_event.event_db_id,
      next_incident_event.event_id,
      next_incident_event.event_sdesc,
      next_sched_event.event_db_id,
      next_sched_event.event_id,
      next_sched_event.event_sdesc,
      sd_fault.leg_id,
      fl_leg.leg_no,
      'FL',
      parent_sched_event.event_db_id,
      parent_sched_event.event_id,
      parent_sched_event.event_sdesc,
      parent_sched_stask.barcode_sdesc,
      evt_event.contact_info_sdesc,
      init_fault_event.event_db_id,
      init_fault_event.event_id,
      init_fault_event.event_sdesc
 FROM evt_event,
      evt_inv,
      evt_ietm,
      sd_fault,
      ref_event_status,
      ref_bitmap,
      evt_event_rel corrective_sched_rel,
      evt_event_rel previous_incident_rel,
      evt_event_rel previous_sched_rel,
      evt_event_rel next_incident_rel,
      evt_event_rel next_sched_rel,
      evt_event_rel parent_sched_rel,
      evt_event_rel init_fault_rel,
      evt_event corrective_sched_event,
      evt_event previous_incident_event,
      evt_event previous_sched_event,
      evt_event next_incident_event,
      evt_event next_sched_event,
      fl_leg,
      evt_event parent_sched_event,
      evt_event init_fault_event,
      sched_stask parent_sched_stask,
      eqp_assmbl_bom,
      eqp_assmbl_pos,
      eqp_bom_part,
      eqp_part_no,
      inv_inv,
      inv_inv nh_inv_inv,
      inv_inv assmbl_inv_inv,
      inv_inv h_inv_inv,
      org_hr,
      utl_user
      WHERE evt_event.event_db_id = sd_fault.fault_db_id AND
      evt_event.event_id    = sd_fault.fault_id
      AND
      evt_inv.event_db_id = evt_event.event_db_id AND
      evt_inv.event_id    = evt_event.event_id AND
      evt_inv.main_inv_bool = 1
      AND
      eqp_assmbl_bom.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
      eqp_assmbl_bom.assmbl_cd     (+)= evt_inv.assmbl_cd AND
      eqp_assmbl_bom.assmbl_bom_id (+)= evt_inv.assmbl_bom_id
      AND
      eqp_assmbl_pos.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
      eqp_assmbl_pos.assmbl_cd     (+)= evt_inv.assmbl_cd AND
      eqp_assmbl_pos.assmbl_bom_id (+)= evt_inv.assmbl_bom_id AND
      eqp_assmbl_pos.assmbl_pos_id (+)= evt_inv.assmbl_pos_id
      AND
      eqp_bom_part.bom_part_db_id (+)= evt_inv.bom_part_db_id AND
      eqp_bom_part.bom_part_id    (+)= evt_inv.bom_part_id
      AND
      inv_inv.inv_no_db_id (+)= evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id    (+)= evt_inv.inv_no_id
      AND
      nh_inv_inv.inv_no_db_id (+)= evt_inv.nh_inv_no_db_id AND
      nh_inv_inv.inv_no_id    (+)= evt_inv.nh_inv_no_id
      AND
      h_inv_inv.inv_no_db_id (+)= evt_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id    (+)= evt_inv.h_inv_no_id
      AND
      assmbl_inv_inv.inv_no_db_id (+)= evt_inv.assmbl_inv_no_db_id AND
      assmbl_inv_inv.inv_no_id    (+)= evt_inv.assmbl_inv_no_id
      AND
      eqp_part_no.part_no_db_id (+)= evt_inv.part_no_db_id AND
      eqp_part_no.part_no_id    (+)= evt_inv.part_no_id
      AND
      evt_ietm.event_db_id (+)= evt_event.event_db_id AND
      evt_ietm.event_id    (+)= evt_event.event_id
      AND
      corrective_sched_rel.event_db_id (+)= evt_event.event_db_id AND
      corrective_sched_rel.event_id    (+)= evt_event.event_id AND
      corrective_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      corrective_sched_event.event_db_id (+)= corrective_sched_rel.rel_event_db_id AND
      corrective_sched_event.event_id    (+)= corrective_sched_rel.rel_event_id
      AND
      previous_incident_rel.event_db_id (+)= evt_event.event_db_id AND
      previous_incident_rel.event_id    (+)= evt_event.event_id AND
      previous_incident_rel.rel_type_cd (+)= 'RECUR'
      AND
      previous_incident_event.event_db_id (+)= previous_incident_rel.rel_event_db_id AND
      previous_incident_event.event_id    (+)= previous_incident_rel.rel_event_id
      AND
      previous_sched_rel.event_db_id (+)= previous_incident_event.event_db_id AND
      previous_sched_rel.event_id    (+)= previous_incident_event.event_id AND
      previous_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      previous_sched_event.event_db_id (+)= previous_sched_rel.rel_event_db_id AND
      previous_sched_event.event_id    (+)= previous_sched_rel.rel_event_id
      AND
      next_incident_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      next_incident_rel.rel_event_id    (+)= evt_event.event_id AND
      next_incident_rel.rel_type_cd     (+)= 'RECUR'
      AND
      next_incident_event.event_db_id (+)= next_incident_rel.event_db_id AND
      next_incident_event.event_id    (+)= next_incident_rel.event_id
      AND
      next_sched_rel.event_db_id (+)= next_incident_event.event_db_id AND
      next_sched_rel.event_id    (+)= next_incident_event.event_id AND
      next_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      next_sched_event.event_db_id (+)= next_sched_rel.rel_event_db_id AND
      next_sched_event.event_id    (+)= next_sched_rel.rel_event_id
      AND
      fl_leg.leg_id (+)= sd_fault.leg_id
      AND
      parent_sched_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      parent_sched_rel.rel_event_id    (+)= evt_event.event_id AND
      parent_sched_rel.rel_type_cd     (+)= 'DISCF'
      AND
      parent_sched_event.event_db_id (+)= parent_sched_rel.event_db_id AND
      parent_sched_event.event_id    (+)= parent_sched_rel.event_id
      AND
      parent_sched_stask.sched_db_id (+)= parent_sched_event.event_db_id AND
      parent_sched_stask.sched_id (+)= parent_sched_event.event_id
      AND
      init_fault_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      init_fault_rel.rel_event_id    (+)= evt_event.event_id AND
      init_fault_rel.rel_type_cd     (+)= 'RESLF'
      AND
      init_fault_event.event_db_id (+)= init_fault_rel.event_db_id AND
      init_fault_event.event_id    (+)= init_fault_rel.event_id
      AND
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
      AND
      ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
      AND
      org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
      org_hr.hr_id    (+)= evt_event.editor_hr_id
      AND
      utl_user.user_id(+)= org_hr.user_id;

--changeSet MX-27658:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION GEN_FLIGHT_DISRUPTION_BARCODE RETURN VARCHAR2 is

  lBarcode          VARCHAR2(30);
  lNum              NUMBER;

  CURSOR lcur_ExistingBarcode ( as_Barcode VARCHAR2) IS
        SELECT FL_LEG_DISRUPT.DISRUPTION_DESC
        FROM   FL_LEG_DISRUPT
        WHERE  FL_LEG_DISRUPT.DISRUPTION_DESC = as_Barcode;
  lrec_ExistingBarcode lcur_ExistingBarcode%ROWTYPE;
BEGIN
  WHILE TRUE LOOP
        -- get the numerical portion of the barcode
        SELECT FLIGHT_DISRUPTION_BARCODE_SEQ.NEXTVAL INTO lNum FROM dual;

        -- Convert to base 34
        lBarcode := CONVERTBASE10TO34(lNum);

        -- pad right hand side with 0's to make a 7 digit string
        SELECT Lpad(lBarcode,7,'0') into lBarcode from dual;

        -- Prefix the barcode with T
        lBarcode := 'F' || lBarcode;

        -- verify that it does not already exist
        OPEN lcur_ExistingBarcode(lBarcode);
        FETCH lcur_ExistingBarcode INTO lrec_ExistingBarcode;

        IF NOT lcur_ExistingBarcode%FOUND THEN
            RETURN(lBarcode);
        END IF;
        CLOSE lcur_ExistingBarcode;
  END LOOP;
END GEN_FLIGHT_DISRUPTION_BARCODE;
/

--changeSet MX-27658:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getFDDisruptionTypes
(
   aFdId fl_leg_disrupt.leg_disrupt_id%TYPE
) RETURN String
IS
   lFdDisruptionTypes String(4000);

   /* cursor declarations */
   CURSOR lcur_DisruptionTypes (
         cl_FdId fl_leg_disrupt.leg_disrupt_id%type
      ) IS

      SELECT
        fl_leg_disrupt_type.disrupt_type_cd

      FROM
        fl_leg_disrupt,
        fl_leg_disrupt_type

      WHERE
        fl_leg_disrupt.leg_disrupt_id = cl_FdId
        AND
        fl_leg_disrupt.leg_disrupt_id = fl_leg_disrupt_type.leg_disrupt_id

      ORDER BY
        disrupt_type_cd;

   lrec_DisruptionTypes lcur_DisruptionTypes%ROWTYPE;


BEGIN

   lFdDisruptionTypes := NULL;

   /* loop for every disruption type define for this flight disruption */
   FOR lrec_DisruptionTypes IN lcur_DisruptionTypes(aFdId) LOOP

      IF lFdDisruptionTypes IS NULL THEN
        /* *** If this is the first row, don't add a comma *** */
        lFdDisruptionTypes := lrec_DisruptionTypes.disrupt_type_cd;

      ELSE
        /* *** add a comma *** */
        lFdDisruptionTypes := lFdDisruptionTypes || ', ' || lrec_DisruptionTypes.disrupt_type_cd;

      END IF;

   END LOOP;


   RETURN lFdDisruptionTypes;

END getFDDisruptionTypes;
/

--changeSet MX-27658:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE FlightInterface IS
/********************************************************************************
*
* Package:     FlightInterface
* Description: This package is used to perform various actions on flight events
*              1) Execute aircraft swapping
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*********************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

/* constant codes for error handling*/
iSuccess                          CONSTANT NUMBER := 1;
iNoProc                           CONSTANT NUMBER := 0;
iCreateEvtInvEntryError           CONSTANT NUMBER := -1;
iUsageCalculationsError           CONSTANT NUMBER := -2;
iUpdateDeadlineForInvTreeError    CONSTANT NUMBER := -3;
iOracleError                      CONSTANT NUMBER := -100;


/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aFlightLegId (raw16):
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure,
*               private procedure 'generateFlightPlanForAircraft' will be invoked, and also
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree'.
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE aircraftSwap(aFlightLegId IN fl_leg.leg_id%TYPE,
                       aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                       aInvNoId IN inv_inv.inv_no_id%TYPE,
                       aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                       aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                       aReturn OUT NUMBER
                       );

END FlightInterface;
/

--changeSet MX-27658:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FlightInterface IS

/*-------------------- Private procedures --------------------------------------*/
PROCEDURE generateFlightPlanForAircraft(aInvNoDbId IN evt_inv.inv_no_db_id%TYPE,
                                          aInvNoId IN evt_inv.inv_no_id%TYPE) IS

CURSOR lcur_GetActiveFlights(aInvNoDbId inv_inv.inv_no_db_id%TYPE, aInvNoId inv_inv.inv_no_id%TYPE) IS
SELECT
   fl_leg.leg_id,
   fl_leg.flight_leg_status_cd,
   fl_leg.arrival_loc_db_id,
   fl_leg.arrival_loc_id,
   fl_leg.departure_loc_db_id,
   fl_leg.departure_loc_id
   FROM
   fl_leg
    WHERE
    fl_leg.aircraft_db_id = aInvNoDbId AND
    fl_leg.aircraft_id = aInvNoId AND
    fl_leg.hist_bool = 0
    ORDER BY
    fl_leg.sched_arrival_dt ASC;

CURSOR lcur_GetLastCompletedFlight(aInvNoDbId inv_inv.inv_no_db_id%TYPE, aInvNoId inv_inv.inv_no_id%TYPE) IS
SELECT * FROM
(
  SELECT
  fl_leg.leg_id
  FROM
  fl_leg
  WHERE
  fl_leg.aircraft_db_id = aInvNoDbId AND
  fl_leg.aircraft_id = aInvNoId AND
  fl_leg.hist_bool = 1  AND
  fl_leg.flight_leg_status_cd = 'MXCMPLT'
  ORDER BY
  fl_leg.actual_arrival_dt
)
WHERE
ROWNUM = 1;


--type definitions
TYPE flight IS RECORD( aFlightId         fl_leg.leg_id%TYPE,
                       aFlightStatus   fl_leg.flight_leg_status_cd%TYPE,
                       aArrLocDbId           fl_leg.arrival_loc_db_id%TYPE,
                       aArrLocId                fl_leg.arrival_loc_id%TYPE,
                       aDepLocDbId           fl_leg.departure_loc_db_id%TYPE,
                       aDepLocId                fl_leg.departure_loc_id%TYPE
                      );

TYPE activeFlightsArray IS TABLE OF flight INDEX BY BINARY_INTEGER;
type typ_ac_flight_plan is table of inv_ac_flight_plan%rowtype index by binary_integer;

--Local variables
lFlight flight;
lActiveFlightsArray   activeFlightsArray;
lrec_GetLastCompletedFlight lcur_GetLastCompletedFlight%ROWTYPE;
lCurrentLocDbId inv_inv.loc_db_id%TYPE;
lCurrentLocId inv_inv.loc_id%TYPE;
lFlightPlanOrd NUMBER;
l_tab_ac_flight_plan typ_ac_flight_plan;
l_ac_flight_plan inv_ac_flight_plan%rowtype;

BEGIN

       -- Initial order
     lFlightPlanOrd := 1;

     -- Clear collection
     l_tab_ac_flight_plan.delete;

     --Delete all of the rows from the flight plan for this aircraft
     DELETE FROM inv_ac_flight_plan
           WHERE inv_ac_flight_plan.inv_no_db_id = aInvNoDbId AND
                    inv_ac_flight_plan.inv_no_id = aInvNoId;


     --Get all the active flights for the aircraft
     OPEN lcur_GetActiveFlights(aInvNoDbId,aInvNoId);
     FETCH lcur_GetActiveFlights BULK COLLECT INTO lActiveFlightsArray;


     FOR i IN 1 .. lactiveFlightsArray.count
     LOOP

         lFlight := lActiveFlightsArray(i);
         IF lcur_GetActiveFlights%ROWCOUNT = 1 THEN
            IF  lFlight.aFlightStatus = 'MXPLAN' OR
                lFlight.aFlightStatus = 'MXOUT'  OR
                lFlight.aFlightStatus = 'MXRETURN' OR
                lFlight.aFlightStatus = 'MXDELAY' THEN

               --Get most recent historic flight
               OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
               FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
               CLOSE lcur_GetLastCompletedFlight;

               --Create new flight plan record
               l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
               l_ac_flight_plan.inv_no_id := aInvNoId;
               l_ac_flight_plan.loc_db_id := lFlight.aDepLocDbId;
               l_ac_flight_plan.loc_id := lFlight.aDepLocId;
               l_ac_flight_plan.arr_leg_id := lrec_GetLastCompletedFlight.leg_id;
               l_ac_flight_plan.flight_plan_ord := lflightplanord;

           -- Add new record to the existing collection
               l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;

           -- Increment counter
               lFlightPlanOrd := lFlightPlanOrd + 1;
            end if;
         end if;

     -- Capture the data for the new record
         l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
         l_ac_flight_plan.inv_no_id := aInvNoId;
         l_ac_flight_plan.loc_db_id := lFlight.aArrLocDbId;
         l_ac_flight_plan.loc_id := lFlight.aArrLocId;
         l_ac_flight_plan.arr_leg_id := lFlight.aFlightId;
         l_ac_flight_plan.flight_plan_ord := lflightplanord;

     -- Add new record to the existing collection
     l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;

         IF lFlightPlanOrd <> 1 THEN

         -- get the values from the previous collection record
         l_ac_flight_plan := l_tab_ac_flight_plan(lflightplanord-1);

         -- Capture previous flight key

         l_ac_flight_plan.dep_leg_id := lFlight.aFlightId;

         -- Set new record to the previous collection record
         l_tab_ac_flight_plan(lflightplanord-1) := l_ac_flight_plan;

            END IF;

     -- Increment counter
         lFlightPlanOrd := lFlightPlanOrd + 1;


     END LOOP;

     -- If there are NO active flight
     IF lcur_GetActiveFlights%ROWCOUNT = 0 THEN

        --Get the current location of the aircraft
        SELECT inv_inv.loc_db_id,
                  inv_inv.loc_id
          INTO   lCurrentLocDbId,
                   lCurrentLocId
         FROM inv_inv
         WHERE inv_inv.inv_no_db_id = aInvNoDbId AND
                  inv_inv.inv_no_id = aInvNoId;

        -- Get most recent historic flight
        OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
        FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
        CLOSE lcur_GetLastCompletedFlight;

        -- Create new flight plan record
        l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
        l_ac_flight_plan.inv_no_id := aInvNoId;
        l_ac_flight_plan.loc_db_id := lCurrentLocDbId;
        l_ac_flight_plan.loc_id := lCurrentLocId;
        l_ac_flight_plan.arr_leg_id := lrec_GetLastCompletedFlight.leg_id;
        l_ac_flight_plan.flight_plan_ord := lflightplanord;

        l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;


     END IF;
     CLOSE lcur_GetActiveFlights;

     forall x in indices of l_tab_ac_flight_plan
        insert into inv_ac_flight_plan values l_tab_ac_flight_plan(x);


END generateFlightPlanForAircraft;

/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aFlightLegId (raw16):*
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure,
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree' procedures will be invoked
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE aircraftSwap( aFlightLegId IN fl_leg.leg_id%TYPE,
                         aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         aInvNoId IN inv_inv.inv_no_id%TYPE,
                         aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                         aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                         aReturn OUT NUMBER
                      ) IS

CURSOR lcur_GetUsgUsageData(aFlightLegId IN fl_leg.leg_id%TYPE ) IS
      SELECT i.data_type_db_id,
             i.data_type_id,
             i.tsn_delta_qt,
             i.tso_delta_qt,
             i.tsi_delta_qt,
             e.inv_no_db_id,
             e.inv_no_id
        FROM fl_leg f,
             usg_usage_record e,
             usg_usage_data i
       WHERE f.leg_id = aFlightLegId AND
             e.usage_record_id  = f.usage_record_id AND
             i.usage_record_id =  e.usage_record_id
;
CURSOR lcur_GetUsgInv(aFlightLegId IN fl_leg.leg_id%TYPE ) IS
      SELECT usg_usage_data.inv_no_db_id, usg_usage_data.inv_no_id
       FROM usg_usage_data
       WHERE usg_usage_data.usage_record_id = ( SELECT fl_leg.usage_record_id
                                FROM fl_leg
                                WHERE leg_id = aFlightLegId );

--local variables
lOriginalInvNoDbId inv_inv.inv_no_db_id%TYPE ;
lOriginalInvNoId  inv_inv.inv_no_id%TYPE;
lHist_bool fl_leg.hist_bool%TYPE;
lUsageRecordId usg_usage_record.usage_record_id%TYPE;
lReturn NUMBER;

--exceptions
xc_UsageCalculations        EXCEPTION;
xc_UpdateDeadlineForInvTree EXCEPTION;

BEGIN
     -- Initialize the return
     aReturn := iNoProc;

      -- Store the original aircraft inventory keys
      SELECT fl_leg.aircraft_db_id,
            fl_leg.aircraft_id
       INTO   lOriginalInvNoDbId,
                lOriginalInvNoId
       FROM  fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

      -- Store the hist_bool for the flight
       SELECT fl_leg.hist_bool
       INTO lHist_bool
       FROM fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

      -- Store the usage_record_id for the flight
       SELECT fl_leg.usage_record_id
       INTO lUsageRecordId
       FROM fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

     -- Check if the event is complete
     IF lHist_bool = 1 THEN

         FOR lrec_UsgUsageData IN lcur_GetUsgUsageData(aFlightLegId) LOOP

             UPDATE inv_curr_usage
             SET tsn_qt = tsn_qt - lrec_UsgUsageData.Tsn_Delta_Qt,
                 tso_qt = tso_qt - lrec_UsgUsageData.Tso_Delta_Qt,
                 tsi_qt = tsi_qt - lrec_UsgUsageData.Tsi_Delta_Qt
             WHERE data_type_db_id = lrec_UsgUsageData.Data_Type_Db_Id AND
                   data_type_id    = lrec_UsgUsageData.Data_Type_Id
             AND ( inv_no_db_id, inv_no_id )
                 IN
                 ( SELECT inv_no_db_id, inv_no_id
                     FROM inv_inv
                    WHERE ( assmbl_inv_no_db_id = lrec_UsgUsageData.Inv_No_Db_Id and
                            assmbl_inv_no_id    = lrec_UsgUsageData.Inv_No_Id   and
                            orig_assmbl_db_id is null
                          )
                      OR ( inv_no_db_id = lrec_UsgUsageData.Inv_No_Db_Id AND
                           inv_no_id    = lrec_UsgUsageData.Inv_No_Id )
                 );

       END LOOP;
       -- update deadlines for aircraft
       FOR lrec_UsgInv IN lcur_GetUsgInv(aFlightLegId) LOOP

           --Execute usage calculations on the inventory
           USAGE_PKG.UsageCalculations(lrec_UsgInv.Inv_No_Db_Id, lrec_UsgInv.Inv_No_Id, lReturn);
           IF lReturn < 0 THEN
              RAISE xc_UsageCalculations;
           END IF;
       END LOOP;
       --update deadlines for aircraft
       EVENT_PKG.UpdateDeadlineForInvTree(lOriginalInvNoDbId, lOriginalInvNoId, lReturn);
              IF lReturn < 0 THEN
                 RAISE xc_UpdateDeadlineForInvTree;
              END IF;

     END IF;

      --Remove rows from relevant tables for current flight
      --remove usage data
      DELETE FROM usg_usage_data
      WHERE usage_record_id = lUsageRecordId;

      --remove measurements data
      DELETE FROM fl_leg_measurement
      WHERE fl_leg_measurement.leg_id = aFlightLegId;

      -- update usage record row with new aircraft
      UPDATE usg_usage_record
      SET inv_no_db_id = aInvNoDbId,
          inv_no_id    = aInvNoId
      WHERE usg_usage_record.usage_record_id = lUsageRecordId;

     -- Assign the new aircraft to flight
      UPDATE fl_leg
        SET aircraft_db_id = aInvNoDbId,
            aircraft_id    = aInvNoId
      WHERE fl_leg.leg_id = aFlightLegId;

      -- Assign the new aircraft type if it is provided
      IF aAssmblDbId > 0 AND aAssmblCd IS NOT NULL THEN
         UPDATE fl_leg
            SET plan_assmbl_db_id = aAssmblDbId,
                plan_assmbl_cd = aAssmblCd
         WHERE fl_leg.leg_id = aFlightLegId;
      END IF;

      --return success
      aReturn := iSuccess;

      EXCEPTION
          WHEN xc_UsageCalculations THEN
               aReturn := iUsageCalculationsError;
          WHEN xc_UpdateDeadlineForInvTree THEN
               aReturn := iUpdateDeadlineForInvTreeError;
          WHEN OTHERS THEN
               aReturn := iOracleError;

END aircraftSwap;

END FlightInterface;
/

--changeSet MX-27658:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY PREP_DEADLINE_PKG IS

/* Subtypes internal to the package. */
TYPE typrec_SchedulingRule IS RECORD (

   DataTypeDbId      NUMBER,
   DataTypeId        NUMBER,
   IntervalQt        FLOAT(22),
   InitialQt         FLOAT(22),
   DeviationQt       FLOAT(22),
   PrefixedQt        FLOAT(22),
   PostfixedQt       FLOAT(22)
);

TYPE typtabrec_SchedulingRuleTable IS TABLE OF typrec_SchedulingRule INDEX BY binary_integer;


TYPE typrec_ScheduleDetails IS RECORD (

   -- Stask details
   STaskDbId NUMBER,
   STaskId   NUMBER,
   FirstInstanceBool BOOLEAN,
   PreviousSTaskDbId NUMBER,
   PreviousSTaskId   NUMBER,
   HInvNoDbId        NUMBER,
   HInvNoId          NUMBER,
   PartNoDbId        NUMBER,
   PartNoId          NUMBER,

   -- Task definition details
   ActiveTaskTaskDbId   NUMBER,
   ActiveTaskTaskId     NUMBER,
   RevisionTaskTaskDbId NUMBER,
   RevisionTaskTaskId   NUMBER,
   EffectiveDt          DATE,
   ReschedFromCd        VARCHAR2(8),
   RelativeBool         NUMBER,
   RecurringBool        NUMBER,
   SchedFromReceivedDtBool NUMBER,
   TaskClassCd          VARCHAR2(8),
   ScheduleToLast       NUMBER
);


/********************************************************************************
*
* Procedure: GetTwoLastRevisions
* Arguments:
*            an_TaskDbId task definition pk
*            an_TaskId   -- // --
*
* Return:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_PrevTaskDbId   - previous task definition
*            an_PrevTaskId     -- // --
*            on_Return         - 1 is success
*
* Description: This procedure returns two latest revisions for a task definition.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/

PROCEDURE GetTwoLastRevisions(
            an_TaskDbId IN task_task.task_db_id%TYPE,
            an_TaskId IN task_task.task_id%TYPE,
            an_LatestTaskDbId OUT task_task.task_db_id%TYPE,
            an_LatestTaskId OUT task_task.task_id%TYPE,
            an_PrevTaskDbId OUT task_task.task_db_id%TYPE,
            an_PrevTaskId OUT task_task.task_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


/* get the deadline */
CURSOR lcur_TwoLastRevisions  IS
 SELECT
       prev_task_def.task_db_id as prev_task_db_id,
       prev_task_def.task_id as prev_task_id,
       new_task_def.task_db_id as new_task_db_id,
       new_task_def.task_id as new_task_id,
       task_defn.task_defn_db_id,
       task_defn.task_defn_id

 FROM task_defn,
       task_task new_task_def,
       task_task prev_task_def,
       task_task start_task_def
 WHERE
      -- get main task definition
      start_task_def.task_db_id=an_TaskDbId AND
      start_task_def.task_id=an_TaskId
      AND
      task_defn.task_defn_db_id=start_task_def.task_defn_db_id AND
      task_defn.task_defn_id=start_task_def.task_defn_id
      AND
      -- get task with previous task definition
      prev_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      prev_task_def.task_defn_id=task_defn.task_defn_id AND
      prev_task_def.revision_ord = task_defn.last_revision_ord-1
      AND
      new_task_def.task_defn_db_id=task_defn.task_defn_db_id AND
      new_task_def.task_defn_id=task_defn.task_defn_id AND
      new_task_def.revision_ord = task_defn.last_revision_ord;
      lrec_TwoLastRevisions  lcur_TwoLastRevisions%ROWTYPE;
BEGIN


   on_Return := icn_NoProc;

   OPEN  lcur_TwoLastRevisions();
   FETCH lcur_TwoLastRevisions INTO lrec_TwoLastRevisions;
   CLOSE lcur_TwoLastRevisions;


   an_LatestTaskDbId:= lrec_TwoLastRevisions.new_task_db_id;
   an_LatestTaskId  := lrec_TwoLastRevisions.new_task_id ;
   an_PrevTaskDbId  := lrec_TwoLastRevisions.prev_task_db_id;
   an_PrevTaskId  := lrec_TwoLastRevisions.prev_task_id;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTwoLastRevisions@@@'||SQLERRM);
      RETURN;

END GetTwoLastRevisions;

/********************************************************************************
*
* Procedure:    GetActualDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
* Return:
*           as_Sched_FromCd (char) - scheduled from refterm
*           an_IntervalQt   (float) - deadline interval qt
*           an_NotifyQt     (float) - deadline notify qt
*           an_DeviationQt  (float) - deadline deviation qt
*           an_PrefixedQt   (float) - deadline prefixed qt
*           an_PostfixedQt  (float) - deadline postfixed qt
*           ad_StartDate    (date)  - start date
*           an_StartQt      (float) - start qt
*           an_InitialIntervalBool (boolean) -true if using def_inital_interval
*           on_Return       (long) - 1 Success/ Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetActualDeadline(
            an_DataTypeDbId        IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId          IN task_sched_rule.data_type_id%TYPE,
            an_SchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_SchedId             IN sched_stask.sched_id%TYPE,
            an_TaskDbId            IN task_task.task_db_id%TYPE,
            an_TaskId              IN task_task.task_id%TYPE,
            ab_RefreshMe           IN BOOLEAN,
            as_Sched_FromCd        OUT evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt          OUT task_sched_rule.def_interval_qt%TYPE,
            an_NotifyQt            OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt         OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt          OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt         OUT task_sched_rule.def_postfixed_qt%TYPE,
            ad_StartDt             OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt             OUT evt_sched_dead.start_qt%TYPE,
            an_DeadlineExists      OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return              OUT typn_RetCode
   ) IS

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;


/* get the deadline */
CURSOR lcur_ActualsDeadlines  IS
      SELECT
           evt_sched_dead.interval_qt,
           evt_sched_dead.notify_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_sched_dead.sched_from_cd,
           evt_sched_dead.start_dt,
           evt_sched_dead.start_qt
      FROM
          evt_sched_dead
      WHERE
           evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
           evt_sched_dead.data_type_id = an_DataTypeId
           AND
           evt_sched_dead.event_db_id =  an_SchedDbId AND
           evt_sched_dead.event_id = an_SchedId;
           lrec_ActualsDeadlines  lcur_ActualsDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* get actual deadline of a task */
   OPEN  lcur_ActualsDeadlines();
   FETCH lcur_ActualsDeadlines INTO lrec_ActualsDeadlines;
   IF NOT lcur_ActualsDeadlines%FOUND THEN
      -- no actual deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_ActualsDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_ActualsDeadlines;


   an_IntervalQt   := lrec_ActualsDeadlines.interval_qt;
   an_NotifyQt     := lrec_ActualsDeadlines.notify_qt ;
   an_DeviationQt  := lrec_ActualsDeadlines.deviation_qt;
   an_PrefixedQt   := lrec_ActualsDeadlines.prefixed_qt;
   an_PostfixedQt  := lrec_ActualsDeadlines.postfixed_qt;
   as_Sched_FromCd :=lrec_ActualsDeadlines.sched_from_cd;
   ad_StartDt      :=lrec_ActualsDeadlines.start_dt;
   an_StartQt      :=lrec_ActualsDeadlines.start_qt;
   /* Measurement scheduling rules*/
   IF ab_RefreshMe THEN
        /* If this task satisfies the ME sched rules condition then use the baseline values*/
        GetMESchedRuleDeadline(
                               an_SchedDbId,
                               an_SchedId,
                               an_TaskDbId,
                               an_TaskId,
                               an_DataTypeDbId,
                               an_DataTypeId,
                               ln_IntervalQt_me,
                               ln_NotifyQt_me,
                               ln_DeviationQt_me,
                               ln_PrefixedQt_me,
                               ln_PostfixedQt_me,
                               ln_Return_me
        );

        IF ln_Return_me > 0
           AND  -- only if the measurement deadline has changed due to JIC measurement changing. We do not want to reset the deviation
                -- unless we have to
           NOT ( an_IntervalQt  = ln_IntervalQt_me AND
                 an_NotifyQt    = ln_NotifyQt_me   AND
                 an_PrefixedQt  = ln_PrefixedQt_me AND
                 an_PostfixedQt = ln_PostfixedQt_me    ) THEN

           an_IntervalQt   := ln_IntervalQt_me;
           an_NotifyQt     := ln_NotifyQt_me;
           an_DeviationQt  := ln_DeviationQt_me;
           an_PrefixedQt   := ln_PrefixedQt_me;
           an_PostfixedQt  := ln_PostfixedQt_me;
        END IF;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetActualDeadline@@@'||SQLERRM);
      RETURN;

END GetActualDeadline;

/********************************************************************************
*
* Procedure:    GetBaselineDeadline
* Arguments:
*           an_DataTypeDbId (long) - The primary key of deadline data type
*           an_DataTypeId   (long) --//--
*           an_SchedDbId    (long) - The primary key of the newly created task
*           an_SchedId      (long) --//--
*           an_TaskDbId     (long) - The primary key of the task's definition key
*           an_TaskId       (long) --//--
*           an_PartNoDbId   (long) - The primary key of the main inventory part no
*           an_PartNoId     (long) --//--
* Return:
*           an_IntervalQt   (float) - deadline interval qt
*           an_InitialQt    (float) - deadline initial qt
*           an_NotifyQt     (float) - dedline notify qt
*           an_DeviationQt  (float) - dedline deviation qt
*           an_PrefixedQt   (float) - dedline prefixed qt
*           an_PostfixedQt  (float) - dedline postfixed qt
*           an_DeadlineExists  (long) - 1 if baseline deadline exists
*           on_Return       (long) - Success/Failure
*
* Description:  This procedure is used to get deadline from task_sched_rule table.
*               Baselined deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetBaselineDeadline(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            an_TaskDbId     IN task_interval.task_db_id%TYPE,
            an_TaskId       IN task_interval.task_id%TYPE,
            an_SchedDbId    IN sched_stask.task_db_id%TYPE,
            an_SchedId      IN sched_stask.task_id%TYPE,
            an_PartNoDbId   IN task_interval.part_no_db_id%TYPE,
            an_PartNoId     IN task_interval.part_no_id%TYPE,
            an_HInvNoDbId   IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId     IN task_ac_rule.inv_no_id%TYPE,
            an_IntervalQt      OUT task_sched_rule.def_interval_qt%TYPE,
            an_InitialQt       OUT task_sched_rule.def_initial_qt%TYPE,
            an_NotifyQt        OUT task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt     OUT task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt      OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt     OUT task_sched_rule.def_postfixed_qt%TYPE,
            an_DeadlineExists  OUT task_sched_rule.def_postfixed_qt%TYPE,
            on_Return          OUT typn_RetCode
   ) IS

    /*
   || Cursor used to get BASELINED scheduling rules for the task.  This
   || will only return deadlines for tasks with task definitions, since adhoc
   || task cannot have baselines deadlines.
   || It  will bring back the part-specific interval_qt if it exists.
   ||
   || When determine the deadline date for an actual task, the scheduling rules should be taken in this order:
   ||    Measurement Specifc
   ||    Part Sepcific
   ||    Tail Specific
   ||    Standard
   */
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id%TYPE,
            cn_TaskId      task_interval.task_id%TYPE,
            cn_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            cn_DataTypeId IN task_sched_rule.data_type_id%TYPE,
            cn_PartNoDbId  task_interval.part_no_db_id%TYPE,
            cn_PartNoId    task_interval.part_no_id%TYPE
         ) IS
         -- Standard, if no Part or Tail rules
         SELECT
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_notify_qt      notify_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId       AND
            task_sched_rule.data_type_db_id = cn_DataTypeDbId AND
            task_sched_rule.data_type_id    = cn_DataTypeId

            -- No Part Specific exists
            AND NOT EXISTS
             ( SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId )

            -- No Tail Specific exists
            AND NOT EXISTS
               (  SELECT 1
                  FROM
                     task_ac_rule
                  WHERE
                     task_ac_rule.task_db_id      = cn_TaskDbId     AND
                     task_ac_rule.task_id         = cn_TaskId       AND
                     task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
                     task_ac_rule.data_type_id    = cn_DataTypeId   AND
                     task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
                     task_ac_rule.inv_no_id       = an_HInvNoId )

        UNION ALL

         -- Tail Specific rules
         SELECT
            task_ac_rule.interval_qt,
            task_ac_rule.initial_qt,
            task_ac_rule.notify_qt,
            task_ac_rule.deviation_qt,
            task_ac_rule.prefixed_qt,
            task_ac_rule.postfixed_qt
         FROM
            task_ac_rule
         WHERE
            task_ac_rule.task_db_id      = cn_TaskDbId     AND
            task_ac_rule.task_id         = cn_TaskId       AND
            task_ac_rule.data_type_db_id = cn_DataTypeDbId AND
            task_ac_rule.data_type_id    = cn_DataTypeId   AND
            task_ac_rule.inv_no_db_id    = an_HInvNoDbId AND
            task_ac_rule.inv_no_id       = an_HInvNoId
            -- No Part Specific exists
            AND NOT EXISTS
            (
               SELECT 1
               FROM task_interval
               WHERE
                   task_interval.task_db_id      = cn_TaskDbId     AND
                   task_interval.task_id         = cn_TaskId       AND
                   task_interval.data_type_db_id = cn_DataTypeDbId AND
                   task_interval.data_type_id    = cn_DataTypeId   AND
                   task_interval.part_no_db_id   = cn_PartNoDbId   AND
                   task_interval.part_no_id      = cn_PartNoId
            )
        UNION ALL

         -- Part Specific Rules
         SELECT
            task_interval.interval_qt,
            task_interval.initial_qt,
            task_interval.notify_qt,
            task_interval.deviation_qt,
            task_interval.prefixed_qt,
            task_interval.postfixed_qt
         FROM
            task_interval
         WHERE
            task_interval.task_db_id      = cn_TaskDbId     AND
            task_interval.task_id         = cn_TaskId       AND
            task_interval.data_type_db_id = cn_DataTypeDbId AND
            task_interval.data_type_id    = cn_DataTypeId   AND
            task_interval.part_no_db_id   = cn_PartNoDbId   AND
            task_interval.part_no_id      = cn_PartNoId;

            /* ME sched rules parms */
            ln_IntervalQt_me          task_sched_rule.def_interval_qt%TYPE;
            ln_NotifyQt_me            task_sched_rule.def_notify_qt%TYPE;
            ln_DeviationQt_me         task_sched_rule.def_deviation_qt%TYPE;
            ln_PrefixedQt_me          task_sched_rule.def_prefixed_qt%TYPE;
            ln_PostfixedQt_me         task_sched_rule.def_postfixed_qt%TYPE;
            ln_Return_me              typn_RetCode;

            lrec_BaselineDeadlines  lcur_BaselineDeadlines%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* If Measurement scheduling rules exist, then use them*/
   GetMESchedRuleDeadline(
                          an_SchedDbId,
                          an_SchedId,
                          an_TaskDbId,
                          an_TaskId,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_IntervalQt_me,
                          ln_NotifyQt_me,
                          ln_DeviationQt_me,
                          ln_PrefixedQt_me,
                          ln_PostfixedQt_me,
                          ln_Return_me
      );

   IF ln_Return_me > 0 THEN
      an_IntervalQt   := ln_IntervalQt_me;
      an_NotifyQt     := ln_NotifyQt_me;
      an_DeviationQt  := ln_DeviationQt_me;
      an_PrefixedQt   := ln_PrefixedQt_me;
      an_PostfixedQt  := ln_PostfixedQt_me;
      an_InitialQt    := 0;

      an_DeadlineExists :=1;
      on_Return         := icn_Success;
      RETURN;
   END IF;

    /* get the baseline deadline for this task definition.*/
   OPEN  lcur_BaselineDeadlines(
            an_TaskDbId,
            an_TaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            an_PartNoDbId,
            an_PartNoId );
   FETCH lcur_BaselineDeadlines INTO lrec_BaselineDeadlines;
   IF NOT lcur_BaselineDeadlines%FOUND THEN
      -- no baseline deadline for this datatype
      an_DeadlineExists :=0;
      CLOSE lcur_BaselineDeadlines;
      on_Return := icn_Success;
      RETURN;
   ELSE
      an_DeadlineExists :=1;
   END IF;
   CLOSE lcur_BaselineDeadlines;

   /* initialize the out variables */
   an_IntervalQt      := lrec_BaselineDeadlines.interval_qt;
   an_InitialQt       := lrec_BaselineDeadlines.initial_qt;
   an_NotifyQt        := lrec_BaselineDeadlines.notify_qt ;
   an_DeviationQt     := lrec_BaselineDeadlines.deviation_qt;
   an_PrefixedQt      := lrec_BaselineDeadlines.prefixed_qt;
   an_PostfixedQt     := lrec_BaselineDeadlines.postfixed_qt;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetBaselineDeadline@@@'||SQLERRM);
      RETURN;

END GetBaselineDeadline;

/********************************************************************************
*
* Procedure: BaselineDeadlinesChanged
* Arguments:
*            an_LatestTaskDbId - the latest task definition
*            an_LatestTaskId   -- // --
*            an_OrigTaskDbId   - the task definition revision before the synch
*            an_OrigTaskId     -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_HInvNoDbId    -- the highest inventory of the main inventory
*            an_HInvNoId      -- // --
*            an_PartNoDbId    -- the part number of the main inventory
*            an_PartNoId      -- // --
*
* Return:
*            an_BaselineDeadlineChanged - 1 if deadline changed between two baseline revisions
*            on_Return                  - 1 is success
*
* Description: This procedure returns
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  BaselineDeadlinesChanged(
            an_LatestTaskDbId IN task_task.task_db_id%TYPE,
            an_LatestTaskId   IN task_task.task_id%TYPE,
            an_OrigTaskDbId   IN task_task.task_db_id%TYPE,
            an_OrigTaskId     IN task_task.task_id%TYPE,
            an_DataTypeDbId   IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId     IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId     IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     IN task_interval.part_no_db_id%TYPE,
            an_PartNoId       IN task_interval.part_no_id%TYPE,
            an_BaselineDeadlineChanged OUT NUMBER,
            on_Return       OUT typn_RetCode
   ) IS

   ln_orig_exist   NUMBER;
   ln_latest_exist NUMBER;


   /* task rules */
   CURSOR lcur_Rule(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_sched_rule.def_initial_qt,
                        NULL,
                        -999999,
                        task_sched_rule.def_initial_qt
                ) AS def_initial_qt,
                task_sched_rule.def_interval_qt,
                task_sched_rule.def_deviation_qt,
                task_sched_rule.def_prefixed_qt,
                task_sched_rule.def_postfixed_qt
          FROM
                task_sched_rule
          WHERE
                task_sched_rule.task_db_id      = cl_TaskDbId     AND
                task_sched_rule.task_id         = cl_TaskId       AND
                task_sched_rule.data_type_db_id = an_DataTypeDbId AND
                task_sched_rule.data_type_id    = an_DataTypeId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId )
                -- No Tail Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_ac_rule
                  WHERE  task_ac_rule.task_db_id      = cl_TaskDbId     AND
                         task_ac_rule.task_id         = cl_TaskId       AND
                         task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                         task_ac_rule.data_type_id    = an_DataTypeId   AND
                         task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                         task_ac_rule.inv_no_id       = an_HInvNoId);

   /* tail rules*/
   CURSOR lcur_Tail(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_ac_rule.initial_qt,
                        NULL,
                        -999999,
                        task_ac_rule.initial_qt
                 ) AS initial_qt,
                 task_ac_rule.interval_qt,
                 task_ac_rule.deviation_qt,
                 task_ac_rule.prefixed_qt,
                 task_ac_rule.postfixed_qt
          FROM
                 task_ac_rule
          WHERE
                 task_ac_rule.task_db_id      = cl_TaskDbId     AND
                 task_ac_rule.task_id         = cl_TaskId       AND
                 task_ac_rule.data_type_db_id = an_DataTypeDbId AND
                 task_ac_rule.data_type_id    = an_DataTypeId   AND
                 task_ac_rule.inv_no_db_id    = an_HInvNoDbId   AND
                 task_ac_rule.inv_no_id       = an_HInvNoId
                -- No Part Specific exists
                AND NOT EXISTS
                ( SELECT 1
                  FROM   task_interval
                  WHERE  task_interval.task_db_id      = cl_TaskDbId     AND
                         task_interval.task_id         = cl_TaskId       AND
                         task_interval.data_type_db_id = an_DataTypeDbId AND
                         task_interval.data_type_id    = an_DataTypeId   AND
                         task_interval.part_no_db_id   = an_PartNoDbId   AND
                         task_interval.part_no_id      = an_PartNoId );

   /* part rules*/
   CURSOR lcur_Part(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                DECODE( task_interval.initial_qt,
                        NULL,
                        -999999,
                        task_interval.initial_qt
                 ) AS initial_qt,
                 task_interval.interval_qt,
                 task_interval.deviation_qt,
                 task_interval.prefixed_qt,
                 task_interval.postfixed_qt
          FROM
                 task_interval
          WHERE
                 task_interval.task_db_id      = cl_TaskDbId     AND
                 task_interval.task_id         = cl_TaskId       AND
                 task_interval.data_type_db_id = an_DataTypeDbId AND
                 task_interval.data_type_id    = an_DataTypeId   AND
                 task_interval.part_no_db_id   = an_PartNoDbId   AND
                 task_interval.part_no_id      = an_PartNoId;

   /* Measurement scheduling rules*/
   CURSOR lcur_ME(
          cn_PrevTaskDbId      task_task.task_db_id%TYPE,
          cn_PrevTaskId        task_task.task_id%TYPE,
          cn_LatestTaskDbId    task_task.task_db_id%TYPE,
          cn_LatestTaskId      task_task.task_id%TYPE
   )
   IS
   (--post minus pre
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt

     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  )
  UNION
  (--pre minus post
     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_PrevTaskDbId AND
        task_task.task_id    = cn_PrevTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id

     MINUS

     SELECT
        DECODE(task_me_rule_interval.rule_data_type_db_id, NULL, NULL, task_me_rule_interval.rule_data_type_db_id || ':' || task_me_rule_interval.rule_data_type_id ) AS me_rule_data_type_key,
        DECODE(task_me_rule_interval.me_data_type_db_id, NULL, NULL, task_me_rule_interval.me_data_type_db_id  || ':' || task_me_rule_interval.me_data_type_id) AS me_me_data_type_key,
        task_me_rule_interval.me_ord,
        task_me_rule_interval.me_qt,
        task_me_rule_interval.interval_qt,
        task_me_rule_interval.deviation_qt,
        task_me_rule_interval.notification_qt,
        task_me_rule_interval.prefix_qt,
        task_me_rule_interval.postfix_qt
     FROM
        task_me_rule_interval,
        task_me_rule,
        task_task

     WHERE
        task_task.task_db_id = cn_LatestTaskDbId AND
        task_task.task_id    = cn_LatestTaskId
        AND
        task_me_rule.task_db_id (+)= task_task.task_db_id AND
        task_me_rule.task_id    (+)= task_task.task_id
        AND
        task_me_rule_interval.task_db_id           (+)= task_me_rule.task_db_id            AND
        task_me_rule_interval.task_id              (+)= task_me_rule.task_id               AND
        task_me_rule_interval.rule_data_type_db_id (+)= task_me_rule.rule_data_type_db_id  AND
        task_me_rule_interval.rule_data_type_id    (+)= task_me_rule.rule_data_type_id     AND
        task_me_rule_interval.me_data_type_db_id   (+)= task_me_rule.me_data_type_db_id    AND
        task_me_rule_interval.me_data_type_id      (+)= task_me_rule.me_data_type_id
  );

   /* task_task details */
   CURSOR lcur_Task(
          cl_TaskDbId   task_task.task_db_id%TYPE,
          cl_TaskId     task_task.task_id%TYPE
          ) IS
          SELECT
                 task_task.recurring_task_bool,
                 task_task.relative_bool,
                 task_task.sched_from_received_dt_bool,
                 task_task.last_sched_dead_bool,
                 task_task.effective_gdt,
                 task_task.resched_from_cd
          FROM
                 task_task
          WHERE
                 task_task.task_db_id = cl_TaskDbId AND
                 task_task.task_id    = cl_TaskId;

   /* CURSORS */
   lrec_MERule    lcur_ME%ROWTYPE;

   lrec_OrigRule   lcur_Rule%ROWTYPE;
   lrec_LatestRule lcur_Rule%ROWTYPE;

   lrec_OrigPart   lcur_Part%ROWTYPE;
   lrec_LatestPart lcur_Part%ROWTYPE;

   lrec_OrigTail   lcur_Tail%ROWTYPE;
   lrec_LatestTail lcur_Tail%ROWTYPE;

   lrec_OrigTask   lcur_Task%ROWTYPE;
   lrec_LatestTask lcur_Task%ROWTYPE;

BEGIN
   on_Return := icn_NoProc;
   an_BaselineDeadlineChanged:=0;

   /*
   * VERIFY FOR DIFFERENCES IN MEASUREMENT SCHEDULING RULES
   */
   OPEN lcur_ME(an_OrigTaskDbId, an_OrigTaskId, an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_ME INTO lrec_MERule;
   IF lcur_ME%FOUND THEN
      an_BaselineDeadlineChanged:=1;
      CLOSE lcur_ME;
      on_Return := icn_Success;
      RETURN;
   END IF;
   CLOSE lcur_ME;

   /*
   * VERIFY FOR DIFFERENCES IN TASK RULES
   */

   /* get original rule information */
   OPEN lcur_Rule(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Rule INTO lrec_OrigRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   /* get latest rule information */
   OPEN lcur_Rule(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Rule INTO lrec_LatestRule;
   IF NOT lcur_Rule%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Rule;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigRule.Def_Interval_Qt  = lrec_LatestRule.Def_Interval_Qt)  OR
           NOT(lrec_OrigRule.Def_Deviation_Qt = lrec_LatestRule.Def_Deviation_Qt) OR
           NOT(lrec_OrigRule.Def_Initial_Qt   = lrec_LatestRule.Def_Initial_Qt)   OR
           NOT(lrec_OrigRule.Def_Prefixed_Qt  = lrec_LatestRule.Def_Prefixed_Qt)  OR
           NOT(lrec_OrigRule.Def_Postfixed_Qt = lrec_LatestRule.Def_Postfixed_Qt)
          ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN PART RULES
   */

   /* get original rule information */
   OPEN lcur_Part(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Part INTO lrec_OrigPart;
   IF NOT lcur_Part%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Part;

   /* get latest rule information */
   OPEN lcur_Part(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Part INTO lrec_LatestPart;
   IF NOT lcur_Part%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Part;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigPart.Interval_Qt  = lrec_LatestPart.Interval_Qt)      OR
           NOT(lrec_OrigPart.Deviation_Qt = lrec_LatestPart.Deviation_Qt)     OR
           NOT(lrec_OrigPart.Initial_Qt   = lrec_LatestPart.Initial_Qt)       OR
           NOT(lrec_OrigPart.Prefixed_Qt  = lrec_LatestPart.Prefixed_Qt)      OR
           NOT(lrec_OrigPart.Postfixed_Qt = lrec_LatestPart.Postfixed_Qt)
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TAIL RULES
   */

   /* get original rule information */
   OPEN lcur_Tail(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Tail INTO lrec_OrigTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   /* get latest rule information */
   OPEN lcur_Tail(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Tail INTO lrec_LatestTail;
   IF NOT lcur_Tail%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Tail;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both deadlines exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 ) AND
         ( NOT(lrec_OrigTail.Interval_Qt  = lrec_LatestTail.Interval_Qt)      OR
           NOT(lrec_OrigTail.Deviation_Qt = lrec_LatestTail.Deviation_Qt)     OR
           NOT(lrec_OrigTail.Initial_Qt   = lrec_LatestTail.Initial_Qt)       OR
           NOT(lrec_OrigTail.Prefixed_Qt  = lrec_LatestTail.Prefixed_Qt)      OR
           NOT(lrec_OrigTail.Postfixed_Qt = lrec_LatestTail.Postfixed_Qt)
         ) THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   /*
   * VERIFY FOR DIFFERENCES IN TASK SCHEDULING DETAILS
   */

   /* get original task information */
   OPEN lcur_Task(an_OrigTaskDbId, an_OrigTaskId);
   FETCH lcur_Task INTO lrec_OrigTask;
   IF NOT lcur_Task%FOUND THEN
      ln_orig_exist :=0;
   ELSE
      ln_orig_exist :=1;
   END IF;
   CLOSE lcur_Task;

   /* get latest task information */
   OPEN lcur_Task(an_LatestTaskDbId, an_LatestTaskId);
   FETCH lcur_Task INTO lrec_LatestTask;
   IF NOT lcur_Task%FOUND THEN
      ln_latest_exist :=0;
   ELSE
      ln_latest_exist :=1;
   END IF;
   CLOSE lcur_Task;

   -- if one exists and the other doesn't
   IF ( NOT(ln_orig_exist = ln_latest_exist) ) THEN
      an_BaselineDeadlineChanged:=1;
      on_Return := icn_Success;
      RETURN;
   -- if both task revisions exist, verify for any differences
   ELSIF ( ln_orig_exist = 1 AND ln_latest_exist = 1 )
         AND
         (
            (lrec_OrigTask.recurring_task_bool           != lrec_LatestTask.recurring_task_bool)             OR
            (lrec_OrigTask.relative_bool                 != lrec_LatestTask.relative_bool)                   OR
            (lrec_OrigTask.sched_from_received_dt_bool   != lrec_LatestTask.sched_from_received_dt_bool)
            OR
            NOT(lrec_OrigTask.effective_gdt          = lrec_LatestTask.effective_gdt)                     OR
            (lrec_OrigTask.effective_gdt IS NULL     AND lrec_LatestTask.effective_gdt IS NOT NULL)       OR
            (lrec_OrigTask.effective_gdt IS NOT NULL AND lrec_LatestTask.effective_gdt IS NULL)
            OR
            NOT(lrec_OrigTask.resched_from_cd          = lrec_LatestTask.resched_from_cd )                OR
            (lrec_OrigTask.resched_from_cd IS NULL     AND lrec_LatestTask.resched_from_cd IS NOT NULL)   OR
            (lrec_OrigTask.resched_from_cd IS NOT NULL AND lrec_LatestTask.resched_from_cd IS NULL)
         )
      THEN
           an_BaselineDeadlineChanged:=1;
           on_Return := icn_Success;
           RETURN;
   END IF;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@BaselineDeadlinesChanged@@@'||SQLERRM);
      RETURN;

END BaselineDeadlinesChanged;


/********************************************************************************
*
* Procedure: GetValuesAndActionForSynch
* Arguments: an_OrigTaskDbId  -definition revision of the task before synch ran
*            an_OrigTaskId    -- // --
*            an_TaskDbId      -task definition pk
*            an_TaskId        -- // --
*            an_SchedDbId     - the actual task pk
*            an_SchedId       -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*            an_PartNoDbId    - the main inventory part number pk
*            an_PartNoId      -- // --
* Return:
*            an_DeleteActualDealine   - true if actual deadline should be deleted
*            an_UpdateActualDeadline  - true if actual deadline should be updated to match baseline
*            an_InsertActualDeadline  - true if actual deadlin should be created as per baseline
*            as_sched_from_cd         - value of actual sched_from_cd
*            ad_start_dt              - value of actuals start_dt (used for UPDATE action)
*            an_start_qt              - value of actuals start_qt (used for UPDATE action)
*            an_BaselineIntervalQt    - value of baseline interval
*            an_BaselineInitialQt     - value of baseline initial interval
*            an_BaselineNotifyQt      - value of baseline notification quantity
*            an_BaselineDeviationQt   - value of baseline deviation
*            an_BaselinePrefixedQt    - value of baseline prefix
*            an_BaselinePostfixedQt   - value of baseline postfix
*            on_Return                - 1 is success
*
* Description: This procedue will retrieve the action to perform on the actual deadline, either
*              DELETE, CREATE or UPDATE the actual to match the baseline
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    September 29, 2008
*
*********************************************************************************
*
* Copyright 2000-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE  GetValuesAndActionForSynch(
            an_OrigTaskDbId            IN task_task.task_db_id%TYPE,
            an_OrigTaskId              IN task_task.task_id%TYPE,
            an_TaskDbId                IN task_task.task_db_id%TYPE,
            an_TaskId                  IN task_task.task_id%TYPE,
            an_PrevSchedDbId           IN sched_stask.sched_db_id%TYPE,
            an_PrevSchedId             IN sched_stask.sched_id%TYPE,
            an_SchedDbId               IN sched_stask.task_db_id%TYPE,
            an_SchedId                 IN sched_stask.task_id%TYPE,
            an_DataTypeDbId            IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId              IN task_sched_rule.data_type_id%TYPE,
            an_HInvNoDbId              IN task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId                IN task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId              IN task_interval.part_no_db_id%TYPE,
            an_PartNoId                IN task_interval.part_no_id%TYPE,
            an_RecurringBool           IN task_task.recurring_task_bool%TYPE,
            an_EventsOnSameInv         IN NUMBER,

            -- OUT actions
            an_DeleteActualDealine    OUT NUMBER,
            an_UpdateActualDeadline   OUT NUMBER,
            an_InsertActualDeadline   OUT NUMBER,

            -- OUT actual values
            as_sched_from_cd          OUT evt_sched_dead.sched_from_cd%TYPE,
            ad_start_dt               OUT evt_sched_dead.start_dt%TYPE,
            an_start_qt               OUT evt_sched_dead.start_qt%TYPE,

            -- OUT baseline values
            an_BaselineIntervalQt     OUT task_sched_rule.def_interval_qt%TYPE,
            an_BaselineNotifyQt       OUT task_sched_rule.def_notify_qt%TYPE,
            an_BaselineDeviationQt    OUT task_sched_rule.def_deviation_qt%TYPE,
            an_BaselinePrefixedQt     OUT task_sched_rule.def_prefixed_qt%TYPE,
            an_BaselinePostfixedQt    OUT task_sched_rule.def_postfixed_qt%TYPE,

            on_Return                 OUT typn_RetCode
   ) IS

      -- actual information
      ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
      ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
      ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
      ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
      ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
      ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
      ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
      ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
      ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

      -- original baseline information
      ln_OrigIntervalQt          task_sched_rule.def_Interval_Qt%TYPE;
      ln_OrigInitialQt           task_sched_rule.def_initial_qt%TYPE;
      ln_OrigNotifyQt            task_sched_rule.def_notify_qt%TYPE;
      ln_OrigDeviationQt         task_sched_rule.def_deviation_qt%TYPE;
      ln_OrigPrefixedQt          task_sched_rule.def_prefixed_qt%TYPE;
      ln_OrigPostfixedQt         task_sched_rule.def_postfixed_qt%TYPE;
      ln_OrigDeadlineExists      task_sched_rule.def_postfixed_qt%TYPE;

      ln_BaselineInitialQt       task_sched_rule.def_initial_qt%TYPE;
      ln_BaselineDeadlineChanged task_sched_rule.def_postfixed_qt%TYPE;
      ln_BaselineDeadlineExists  task_sched_rule.def_postfixed_qt%TYPE;
      ln_SameTaskDefn            NUMBER;
BEGIN
   -- initialize variables
   on_Return               := icn_NoProc;
   an_DeleteActualDealine  := 0;
   an_UpdateActualDeadline := 0;
   an_InsertActualDeadline := 0;

   /* see if the two tasks are on the same definition */
   SELECT COUNT(*)
   INTO   ln_SameTaskDefn
   FROM   sched_stask prev_sched,
          task_task   prev_task,
          task_task
   WHERE  prev_sched.sched_db_id = an_PrevSchedDbId AND
          prev_sched.sched_id    = an_PrevSchedId
          AND
          -- task definition of the previous task
          prev_task.task_db_id = prev_sched.task_db_id AND
          prev_task.task_id    = prev_sched.task_id
          AND
          -- task definition of the current task
          task_task.task_db_id = an_TaskDbId AND
          task_task.task_id    = an_TaskId
          AND
          task_task.assmbl_db_id  = prev_task.assmbl_db_id AND
          task_task.assmbl_cd     = prev_task.assmbl_cd    AND
          task_task.assmbl_bom_id = prev_task.assmbl_bom_id
          AND
          ( --check if they are the same
            (prev_task.task_defn_db_id = task_task.task_defn_db_id AND
            prev_task.task_defn_id    = task_task.task_defn_id)
            OR
            (prev_task.block_chain_sdesc = task_task.block_chain_sdesc)
            OR
            ( EXISTS (SELECT 1
                      FROM   task_task all_prev,
                             task_task all_post
                      WHERE  all_post.block_chain_sdesc = task_task.block_chain_sdesc
                             AND
                             all_prev.block_chain_sdesc = prev_task.block_chain_sdesc
                             AND
                             all_post.task_defn_db_id = all_post.task_defn_db_id AND
                             all_post.task_defn_id    = all_post.task_defn_id ) )
          );

   /* get original deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_OrigTaskDbId,
            an_OrigTaskId,
            an_SchedDbId,
            an_SchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            ln_OrigIntervalQt,
            ln_OrigInitialQt,
            ln_OrigNotifyQt,
            ln_OrigDeviationQt,
            ln_OrigPrefixedQt,
            ln_OrigPostfixedQt,
            ln_OrigDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* get current deadline baseline information */
   GetBaselineDeadline(
            an_DataTypeDbId ,
            an_DataTypeId ,
            an_TaskDbId,
            an_TaskId,
            an_SchedDbId,
            an_SchedId,
            an_PartNoDbId,
            an_PartNoId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_BaselineIntervalQt,
            ln_BaselineInitialQt,
            an_BaselineNotifyQt,
            an_BaselineDeviationQt,
            an_BaselinePrefixedQt,
            an_BaselinePostfixedQt,
            ln_BaselineDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   -- if the first task in the chain or first on this inventory or first task of this definition, and the task is recurring, use initial interval
   IF (an_PrevSchedId =-1 OR an_EventsOnSameInv = 0 OR ln_SameTaskDefn = 0 ) AND an_RecurringBool = 1 AND ln_BaselineInitialQt IS NOT NULL THEN
      an_BaselineIntervalQt := ln_BaselineInitialQt;
   END IF;

      /* get existing deadline , do not refresh measurement rules */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            an_TaskDbId,
            an_TaskId,
            FALSE,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   -- If the baseline doesn't exist and the actual doesn't exists, then do nothing
   IF  ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 0 THEN
      on_Return := icn_Success;
      RETURN;

   -- If the baseline exists and the actual doesn't, then create the actual
   ELSIF ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 0 THEN
      an_InsertActualDeadline := 1;
      on_Return := icn_Success;
      RETURN;

   -- If the baseline doesn't exist and the actual does, then delete the actual
   ELSIF ln_BaselineDeadlineExists = 0 AND ln_ActualDeadlineExists = 1 THEN
      an_DeleteActualDealine := 1;
      on_Return := icn_Success;
      RETURN;

   -- if both deadlines exist, update to match the baseline
   END IF;


   --Now we know that both exist: ln_BaselineDeadlineExists = 1 AND ln_ActualDeadlineExists = 1
  BaselineDeadlinesChanged(
            an_TaskDbId,
            an_TaskId,
            an_OrigTaskDbId,
            an_OrigTaskId ,
            an_DataTypeDbId,
            an_DataTypeId,
            an_HInvNoDbId,
            an_HInvNoId,
            an_PartNoDbId,
            an_PartNoId,
            ln_BaselineDeadlineChanged,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   IF ln_BaselineDeadlineChanged = 1 THEN
      an_UpdateActualDeadline := 1;
      as_sched_from_cd        := ls_ActualSchedFromCd;
      ad_start_dt             := ld_ActualStartDt;
      an_start_qt             := ln_ActualStartQt;

      --if the user modified the actual, keep their changes:
      IF ln_OrigDeadlineExists = 1 THEN
         -- if the user extended the actual deadline, keep the extension
         IF ln_ActualDeviationQt <> ln_OrigDeviationQt THEN
            an_BaselineDeviationQt := ln_ActualDeviationQt;
         END IF;
      END IF;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetValuesAndActionForSynch@@@'||SQLERRM || '--' || an_TaskDbId ||':' ||  an_TaskId || '--' || an_SchedDbId || ':' || an_SchedId || '--' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;

END GetValuesAndActionForSynch;
/********************************************************************************
*
* Procedure: DeleteDeadline
* Arguments:
*
*            an_SchedDbId - the actual task pk
*            an_SchedTaskId   -- // --
*            an_DataTypeDbId  --  deadline data type pk
*            an_DataTypeId    -- // --
*
* Return:
*
*            on_Return         - 1 is success
*
* Description: This procedue deletes a deadline form actual task
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:
* Recent Date:    May 8, 2006
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE DeleteDeadline(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            on_Return       OUT typn_RetCode
   ) IS
   BEGIN
      /* delete the deadline */
      DELETE
      FROM evt_sched_dead
      WHERE  event_db_id= an_SchedDbId AND
          event_id= an_SchedId AND
          evt_sched_dead.data_type_db_id=an_DataTypeDbId AND
          evt_sched_dead.data_type_id=an_DataTypeId;
   EXCEPTION

   WHEN OTHERS THEN
   -- Unexpected error
   on_Return := icn_Error;
   APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@DeleteDeadline@@@'||SQLERRM);
   RETURN;

END DeleteDeadline;

/********************************************************************************
*
* Procedure:    GetUsageParmInfo
* Arguments:
*           an_DataTypeDbId (long) - The primary key of mim data type
*           an_DataTypeId   (long) --//--
* Return:
*           as_DomainTypeCd (char) - domain type of the data type
*           al_RefMultQt    (float)- multiplier for the data type
*           as_EngUnitCd    (char) - eng unit for the data type
*           as_DataTypeCd   (char) - data type code
*           on_Return       (long) - 1 Success/Failure
*
* Description:  This procedure is used to get deadline from evt_sched_dead table.
*               Actual task deadline info.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetUsageParmInfo(
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            as_DomainTypeCd   OUT mim_data_type.domain_type_cd%TYPE,
            al_RefMultQt     OUT ref_eng_unit.ref_mult_qt%TYPE,
            as_EngUnitCd  OUT mim_data_type.eng_unit_cd%TYPE,
            as_DataTypeCd   OUT mim_data_type.data_type_cd%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* select info about the mim datatype */
    SELECT mim_data_type.domain_type_cd,
             ref_eng_unit.ref_mult_qt,
             mim_data_type.eng_unit_cd,
             mim_data_type.data_type_cd
        INTO as_DomainTypeCd,
             al_RefMultQt,
             as_EngUnitCd,
             as_DataTypeCd
        FROM mim_data_type,
             ref_eng_unit
       WHERE ( mim_data_type.data_type_db_id = an_DataTypeDbId ) AND
             ( mim_data_type.data_type_id    = an_DataTypeId )
             AND
             ( ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id ) AND
             ( ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetUsageParmInfo@@@'||SQLERRM);
      RETURN;

END GetUsageParmInfo;


/********************************************************************************
*
* Procedure:    InsertDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*           an_InitialIntervalBool (boolean) -true if using task defn initial interval
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InsertDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN task_sched_rule.data_type_db_id%TYPE,
            an_DataTypeId   IN task_sched_rule.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            an_IntervalQt   IN task_sched_rule.def_interval_qt%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_NotifyQt     IN task_sched_rule.def_notify_qt%TYPE,
            an_DeviationQt  IN task_sched_rule.def_deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /*  insert row into the table */
    INSERT INTO evt_sched_dead(
             event_db_id,
             event_id,
             data_type_db_id,
             data_type_id,
             start_qt,
             start_dt,
             sched_from_db_id,
             sched_from_cd,
             sched_dead_qt,
             sched_dead_dt,
             interval_qt,
             notify_qt,
             deviation_qt,
             prefixed_qt,
             postfixed_qt)
      VALUES(
             an_SchedDbId,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             ad_StartQt,
             ad_StartDt,
             0,
             as_SchedFromCd,
             al_NewDeadlineQt,
             ad_NewDeadlineDt,
             an_IntervalQt,
             an_NotifyQt,
             an_DeviationQt,
             an_PrefixedQt,
             an_PostfixedQt
        );

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@InsertDeadlineRow@@@'||SQLERRM);
      RETURN;

END InsertDeadlineRow;

/********************************************************************************
*
* Procedure:    UpdateDeadlineRow
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*           an_DataTypeDbId  (long) - data type primary key
*           an_DataTypeId    (long) --//--
*           ad_StartQt       (float)- deadline start qt
*           ad_StartDt       (Date) - deadline start dt
*           as_SchedFromCd   (Char) - deadline scheduled from ref value
*           an_IntervalQt    (float)- deadline interval qt
*           al_NewDeadlineQt (float)- deadline due qt value
*           ad_NewDeadlineDt (Date) - deadline due date
*           an_NotifyQt      (float)- deadline notify qt
*           an_DeviationQt   (float)- deadline deviation qt
*           an_PrefixedQt    (float)- deadline prefixed qt
*           an_PostfixedQt   (float)- deadline post fixed qt
*
* Return:
*           on_Return       (long) -  1 Success/Failure
*
* Description:  This procedure inserts new row into the evt_sched_stask table.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   jbajer
* Recent Date:    July 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDeadlineRow(
            an_SchedDbId IN sched_stask.sched_db_id%TYPE,
            an_SchedId IN sched_stask.sched_db_id%TYPE,
            an_DataTypeDbId IN evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN evt_sched_dead.data_type_id%TYPE,
            ad_StartQt     IN  evt_sched_dead.start_qt%TYPE,
            ad_StartDt     IN  evt_sched_dead.start_dt%TYPE,
            as_SchedFromCd  IN evt_sched_dead.sched_from_cd%TYPE,
            al_NewDeadlineQt IN evt_sched_dead.sched_dead_qt%TYPE,
            ad_NewDeadlineDt IN evt_sched_dead.sched_dead_dt%TYPE,
            an_IntervalQt   IN evt_sched_dead.interval_qt%TYPE,
            an_NotifyQt     IN evt_sched_dead.notify_qt%TYPE,
            an_DeviationQt  IN evt_sched_dead.deviation_qt%TYPE,
            an_PrefixedQt   IN task_sched_rule.def_prefixed_qt%TYPE,
            an_PostfixedQt  IN task_sched_rule.def_postfixed_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    /* update the deadline */
    UPDATE evt_sched_dead
         SET
            sched_dead_qt              = al_NewDeadlineQt,
            sched_dead_dt              = ad_NewDeadlineDt,
            sched_dead_dt_last_updated = SYSDATE,
            start_qt                   = ad_StartQt,
            start_dt                   = ad_StartDt,
            sched_from_db_id           = 0,
            sched_from_cd              = as_SchedFromCd,
            prefixed_qt                = an_PrefixedQt,
            postfixed_qt               = an_PostfixedQt,
            interval_qt                = an_IntervalQt,
            sched_driver_bool          = 0,
            deviation_qt               = an_DeviationQt,
            notify_qt                  = an_NotifyQt
         WHERE
            event_db_id     = an_SchedDbId AND
            event_id        = an_SchedId AND
            data_type_db_id = an_DataTypeDbId AND
            data_type_id    = an_DataTypeId;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDeadlineRow@@@'||SQLERRM);
      RETURN;

END UpdateDeadlineRow;

/********************************************************************************
*
* Procedure:    GetCorrectiveFaultInfo
* Arguments:
*           an_SchedDbId     (long) - task primary key
*           an_SchedId       (long) --//--
*
*
* Return:
*           an_FaultDbId   (long) - fault primary key
*           an_FaultId     (long) --//-
*           ad_FoundOnDate (Date) - fault raised date
*           on_Return      (long) -  1 Success/Failure
*
* Description:  This procedure returns found on date and fault associated with this task.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCorrectiveFaultInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_FaultDbId    OUT  sd_fault.fault_db_id%TYPE,
            an_FaultId      OUT  sd_fault.fault_id%TYPE,
            ad_FoundOnDate       OUT  evt_event.actual_start_dt%TYPE,
            ad_StartDt           OUT  evt_sched_dead.start_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

     /* fault info */
     CURSOR lcur_corrective_fault IS
     SELECT evt_event_rel.event_db_id,
            evt_event_rel.event_id,
            evt_event.actual_start_dt
     FROM   evt_event_rel,
            evt_event
     WHERE  evt_event_rel.rel_event_db_id = an_SchedDbId AND
            evt_event_rel.rel_event_id    = an_SchedId   AND
            evt_event_rel.rel_type_cd     = 'CORRECT'
            AND
            evt_event.event_db_id = evt_event_rel.event_db_id AND
            evt_event.event_id    = evt_event_rel.event_id
            AND
            evt_event.rstat_cd  = 0;
      lrec_corrective_fault lcur_corrective_fault%ROWTYPE;

     CURSOR lcur_custom_deadline IS
     SELECT evt_sched_dead.start_dt
     FROM   evt_sched_dead
     WHERE  evt_sched_dead.event_db_id   = an_SchedDbId AND
            evt_sched_dead.event_id      = an_SchedId   AND
            evt_sched_dead.sched_from_cd = 'CUSTOM'     AND
            evt_sched_dead.start_dt      IS NOT NULL;
      lrec_custom_deadline lcur_custom_deadline%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get fault info */
   OPEN  lcur_corrective_fault();
   FETCH lcur_corrective_fault INTO lrec_corrective_fault;
   CLOSE lcur_corrective_fault;

   an_FaultDbId:= lrec_corrective_fault.event_db_id;
   an_FaultId  := lrec_corrective_fault.event_id ;
   ad_FoundOnDate:=lrec_corrective_fault.actual_start_dt;

   OPEN  lcur_custom_deadline();
   FETCH lcur_custom_deadline INTO lrec_custom_deadline;
   IF    lcur_custom_deadline%FOUND THEN
         -- if the found on date is not the same as the custom start date
         IF TRUNC(lrec_custom_deadline.start_dt) <> TRUNC(lrec_corrective_fault.actual_start_dt) THEN
            ad_StartDt:=lrec_custom_deadline.start_dt;
         END IF;
   END IF;
   CLOSE lcur_custom_deadline;
   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCorrectiveFaultInfo@@@'||SQLERRM);
      IF lcur_custom_deadline%ISOPEN THEN CLOSE lcur_custom_deadline; END IF;
      RETURN;

END GetCorrectiveFaultInfo;


/********************************************************************************
*
* Procedure:    FindCalendarDeadlineVariables
* Arguments:
*            an_DataTypeDbId  (long)  - data type primary key
*            an_DataTypeId    (long)  --//--
*            ad_StartDt       (date)  - deadline start date
*            an_Interval      (float) - deadline interval
*            ad_NewDeadlineDt (date)  - deadline date
*
* Return:
*            ad_StartDt       (date)  - new deadline start date
*            an_Interval      (float) - new deadline interval
*            ad_NewDeadlineDt (date)  - mew deadline date
*            on_Return        (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartDt, an_Interval, ad_NewDeadlineDt
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Jonathan Clarkin
* Recent Date:    May 5, 2006
*
*********************************************************************************
*
* Copyright 2000-2006 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindCalendarDeadlineVariables(
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            ad_StartDt       IN OUT evt_sched_dead.start_dt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_dt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /*local variables */
   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   lb_EndOfDay                BOOLEAN;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* get deadline parm info*/
   GetUsageParmInfo(
         an_DataTypeDbId,
         an_DataTypeId,
         ls_DomainTypeCd,
         ll_RefMultQt,
         ls_EngUnitCd,
         ls_DataTypeCd,
         on_Return   );
   IF on_Return < 0 THEN
     RETURN;
   END IF;

   /* Only round non Calendar Hour based dates */
   IF (NOT UPPER(ls_DataTypeCd) = 'CHR') THEN

      IF (ad_StartDt IS NOT NULL) THEN
         ad_StartDt := TO_DATE(TO_CHAR(ad_StartDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;


      /* round the all calendar deadlines to the end of the day */
      IF (ad_NewDeadlineDt IS NOT NULL)  AND lb_EndOfDay THEN
         ad_NewDeadlineDt := TO_DATE(TO_CHAR(ad_NewDeadlineDt, 'DD-MON-YYYY ')||'23:59:59', 'DD-MON-YYYY HH24:MI:SS');
      END IF;

   END IF;

   /* -- Calculate Deadline Date -- */
   IF (ad_NewDeadlineDt is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         ad_NewDeadlineDt := ADD_MONTHS( ad_StartDt, an_Interval ) + (an_Interval - TRUNC(an_Interval)) * ll_RefMultQt;

      /* if the data type is month then use months to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_StartDt:= LAST_DAY(ad_StartDt);
         ad_NewDeadlineDt :=  ADD_MONTHS( ad_StartDt, an_Interval );

      /* if the data type is year then use years to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
     ad_NewDeadlineDt := ADD_MONTHS( ad_StartDt, an_Interval*12 );

      /* If it is a Calendar Hour, do not truncate */
      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         ad_NewDeadlineDt := ad_StartDt + (an_Interval * ll_RefMultQt);

      /* add the correct # of days to the start date */
      ELSE
         ad_NewDeadlineDt := ad_StartDt + TRUNC(an_Interval * ll_RefMultQt);
      END IF;

   /* -- Calculate Start Date -- */
   ELSIF (ad_StartDt is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' THEN
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval ) - (an_Interval - TRUNC(an_Interval)) * ll_RefMultQt;

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval );

      /* if the data type is year then use years to calculate new deadline (not days) */
      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
          ad_StartDt:= ADD_MONTHS( ad_NewDeadlineDt, -an_Interval*12 );

      /* add the correct # of days to the start date */
      ELSE
         ad_StartDt := ad_NewDeadlineDt  - (an_Interval * ll_RefMultQt);
      END IF;


   /* -- Calculate Interval -- */
   ELSIF (an_Interval is NULL) THEN

      /* if the data type is month then use months to calculate new deadline (not days) */
      IF UPPER(ls_DataTypeCd) = 'CMON' OR UPPER(ls_DataTypeCd) = 'CLMON' THEN
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CLMON' THEN
         ad_NewDeadlineDt:= LAST_DAY(ad_NewDeadlineDt);
         ad_StartDt:= LAST_DAY(ad_StartDt);
         an_Interval := MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt );

      ELSIF UPPER(ls_DataTypeCd) = 'CYR' THEN
         an_Interval := TRUNC( MONTHS_BETWEEN( ad_NewDeadlineDt, ad_StartDt )/12);

      ELSIF UPPER(ls_DataTypeCd) = 'CHR' THEN
         an_Interval :=  (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt;

      /* add the correct # of days to the start date */
      ELSE
         an_Interval :=  TRUNC( (ad_NewDeadlineDt - ad_StartDt) / ll_RefMultQt );
      END IF;

   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindCalendarDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindCalendarDeadlineVariables;


/********************************************************************************
*
* Procedure: FindUsageDeadlineVariables
* Arguments:
*            ad_StartQt        (float)  - deadline start qt
*            an_Interval       (float)  - deadline interval
*            ad_NewDeadlineTSN (float)  - deadline TSN due value
*
* Return:
             ad_StartDt      (date)  - new deadline start date
*            an_Interval     (float) - new deadline interval
*            ad_NewDeadlineDt (date) - mew deadline TSN due value
*            on_Return       (long)  - 1 if success
*
* Description:  This procedure reaculates ad_StartQt, an_Interval, ad_NewDeadlineTSN
*               if any is null.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE   FindUsageDeadlineVariables(
            ad_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            ad_NewDeadlineTSN IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT typn_RetCode
   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

       /* recalculate deadline TSN */
       IF (ad_NewDeadlineTSN is NULL) THEN
            ad_NewDeadlineTSN := NVL( ad_StartQt, 0 ) + NVL( an_Interval, 0 );
       END IF;
       IF (ad_StartQt is NULL) THEN
            ad_StartQt := ad_NewDeadlineTSN  - NVL( an_Interval, 0 );
       END IF;
       IF (an_Interval is NULL) THEN
            an_Interval :=  ad_NewDeadlineTSN - ad_StartQt;
       END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindUsageDeadlineVariables@@@'||SQLERRM);
      RETURN;

END FindUsageDeadlineVariables;

/********************************************************************************
*
* Procedure: GetOldEvtInvUsage
* Arguments:
*           an_EventDbId    (long)  - event primary key
*           an_EventId      (long)  --//--
*           an_DataTypeDbId (long)  - deadline data type primary key
*           an_DataTypeId   (long)  --//--
*
* Return:
*           an_SnapshotTSN (date) - TSN snapshot
*           on_Return       - 1 is success
*
* Description: This procedure returns due and completion dates.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetOldEvtInvUsage(
            an_EventDbId    IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId      IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_SnapshotTSN OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

  CURSOR lcur_old_evt_inv_usage (
         cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
         cn_EventId        evt_sched_dead.event_id%TYPE,
         cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
         cn_DataTypeId     evt_sched_dead.data_type_id%TYPE

      ) IS
         SELECT
         evt_inv_usage.tsn_qt,
         evt_inv_usage.event_inv_id
      FROM
         evt_inv_usage
      WHERE
         evt_inv_usage.event_db_id     (+)= cn_EventDbId AND
         evt_inv_usage.event_id        (+)= cn_EventId AND
         evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId AND
         evt_inv_usage.data_type_id    (+)= cn_DataTypeId
         ORDER BY  evt_inv_usage.event_inv_id DESC;
         lrec_old_evt_inv_usage lcur_old_evt_inv_usage%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /*get snapshot for the event*/
   OPEN  lcur_old_evt_inv_usage(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_old_evt_inv_usage INTO lrec_old_evt_inv_usage;
   CLOSE lcur_old_evt_inv_usage;


   an_SnapshotTSN:= lrec_old_evt_inv_usage.tsn_qt;


   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetOldEvtInvUsage@@@'||SQLERRM);
      RETURN;

END GetOldEvtInvUsage;

/********************************************************************************
*
* Procedure: GetMainInventoryBirthInfo
* Arguments:
*             an_SchedDbId (long) - task primary key
*             an_SchedId   (long) --//--
*
* Return:
*             ad_ManufactDt   (date) --  manufacturing date of the inventory
*             ad_ReceivedDt   (date) --  received date of the inventory
*             on_Return       - 1 is success
*
* Description: This procedure returns manufacturing and received dates of the inventory.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMainInventoryBirthInfo(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            ad_ManufactDt   OUT  inv_inv.received_dt%TYPE,
            ad_ReceivedDt OUT inv_inv.received_dt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

  SELECT inv_inv.received_dt,
                 inv_inv.manufact_dt
          INTO ad_ReceivedDt,
               ad_ManufactDt
          FROM inv_inv,
               evt_inv
          WHERE inv_inv.inv_no_db_id (+) = evt_inv.inv_no_db_id
            AND inv_inv.inv_no_id    (+) = evt_inv.inv_no_id
            AND inv_inv.rstat_cd     (+) = 0
            AND evt_inv.main_inv_bool    = 1
            AND evt_inv.event_db_id      = an_SchedDbId
            AND evt_inv.event_id         = an_SchedId;

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetMainInventoryBirthInfo@@@'||SQLERRM);
      RETURN;

END GetMainInventoryBirthInfo;

/********************************************************************************
*
* Procedure: AreTasksOnTheSameInventory
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_PrevSchedDbId  (long) - previous task primary key
*            an_PrevSchedId    (long) --//--
* Return:
*             ll_EventsOnSameInv  (long) -- 1 if tasks are on the same inventory, 0 if not.
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns 1 if tasks are on the same inventory 0 if they are not.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AreTasksOnTheSameInventory(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_PrevSchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_PrevSchedId      IN  sched_stask.sched_id%TYPE,
            ll_EventsOnSameInv   OUT  NUMBER,
            on_Return      OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT COUNT(*)
              INTO ll_EventsOnSameInv
              FROM evt_inv    new_evt_inv,
                   evt_inv    previous_evt_inv
             WHERE ( new_evt_inv.event_db_id = an_SchedDbId ) AND
                   ( new_evt_inv.event_id    = an_SchedId ) AND
                   ( new_evt_inv.main_inv_bool = 1 )
                   AND
                   ( previous_evt_inv.event_db_id = an_PrevSchedDbId ) AND
                   ( previous_evt_inv.event_id    = an_PrevSchedId ) AND
                   ( previous_evt_inv.main_inv_bool = 1 )
                   AND
                   ( new_evt_inv.inv_no_db_id = previous_evt_inv.inv_no_db_id ) AND
                   ( new_evt_inv.inv_no_id    = previous_evt_inv.inv_no_id );

    -- Return success
    on_Return := icn_Success;

   EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@AreTasksOnTheSameInventory@@@'||SQLERRM);
      RETURN;

END AreTasksOnTheSameInventory;


/********************************************************************************
*
* Procedure: GetCurrentInventoryUsage
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
*            an_DataTypeDbId   (long) data type primary key
             an_DataTypeId      (long) --//--
* Return:
*             an_TsnQt (float) -- TSN value from the current inventory
*             on_Return       -   (long) 1 is success
*
* Description: This procedure returns current inventory usage TSN value.
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetCurrentInventoryUsage(
            an_SchedDbId    IN  sched_stask.sched_db_id%TYPE,
            an_SchedId      IN  sched_stask.sched_id%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_TsnQt OUT inv_curr_usage.tsn_qt%TYPE,
            on_Return      OUT typn_RetCode
   ) IS
     xc_DataTypeNotOnInv  EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

     SELECT inv_curr_usage.tsn_qt
                INTO an_TsnQt
                FROM evt_inv,
                     inv_curr_usage
                WHERE ( evt_inv.event_db_id = an_SchedDbId ) AND
                      ( evt_inv.event_id    = an_SchedId )   AND
                      ( evt_inv.main_inv_bool = 1 )
                       AND
                      ( inv_curr_usage.data_type_db_id = an_DataTypeDbId )AND
                      ( inv_curr_usage.data_type_id = an_DataTypeId )    AND
                      ( inv_curr_usage.inv_no_db_id = evt_inv.inv_no_db_id )AND
                      ( inv_curr_usage.inv_no_id    = evt_inv.inv_no_id );

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
               RAISE xc_DataTypeNotOnInv;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetCurrentInventoryUsage@@@'||SQLERRM);
      RETURN;

END GetCurrentInventoryUsage;



/********************************************************************************
*
* Procedure: GetTaskDetails
* Arguments:
*            an_SchedDbId      (long) - task priamry key
*            an_SchedId        (long) --//--
* Return:
*           an_PrevSchedDbId  (long) - previous task primary key
*           an_PrevSchedId    (long) --//--
*           an_TaskDbId       (long) - this task task definition primary key
*           an_TaskId         (long) --//--
*           an_PartNoDbId     (long) - this tasks main inventory part primary key
*           an_PartNoId       (long) --//--
*           an_RelativeBool   (long) - 1 if this task is relative
*           ad_EffectiveDt    (date) - effective date of the task definition
*           on_Return       -   (long) 1 is success
*
* Description: This procedure returns task details
*
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetTaskDetails(
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE,
            an_PrevSchedDbId  OUT sched_stask.sched_db_id%TYPE,
            an_PrevSchedId    OUT sched_stask.sched_id%TYPE,
            an_TaskDbId       OUT task_interval.task_db_id%TYPE,
            an_TaskId         OUT task_interval.task_id%TYPE,
            an_HInvNoDbId     OUT task_ac_rule.inv_no_db_id%TYPE,
            an_HInvNoId       OUT task_ac_rule.inv_no_id%TYPE,
            an_PartNoDbId     OUT task_interval.part_no_db_id%TYPE,
            an_PartNoId       OUT task_interval.part_no_id%TYPE,
            an_RelativeBool   OUT task_task.relative_bool%TYPE,
            ad_EffectiveDt    OUT task_task.effective_dt%TYPE,
            av_ReschedFromCd  OUT task_task.resched_from_cd%TYPE,
            an_RecurringBool  OUT task_task.recurring_task_bool%TYPE,
            an_SchedFromReceivedDtBool OUT task_task.sched_from_received_dt_bool%TYPE,
            as_TaskClassCd    OUT task_task.task_class_cd%TYPE,
            on_Return         OUT typn_RetCode

   ) IS


BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   SELECT
         sched_stask.task_db_id,
         sched_stask.task_id,
         DECODE( evt_event_rel.event_db_id, NULL, -1, evt_event_rel.event_db_id ) AS prev_sched_db_id,
         DECODE( evt_event_rel.event_id,    NULL, -1, evt_event_rel.event_id )    AS prev_sched_id,
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         evt_inv.part_no_db_id,
         evt_inv.part_no_id,
         task_task.relative_bool,
         task_task.effective_dt,
         task_task.resched_from_cd,
         istaskdefnrecurring(sched_stask.task_db_id, sched_stask.task_id) AS recurring_task_bool,
         task_task.sched_from_received_dt_bool,
         task_task.task_class_cd
      INTO
         an_TaskDbId,
         an_TaskId,
         an_PrevSchedDbId,
         an_PrevSchedId,
         an_HInvNoDbId,
         an_HInvNoId,
         an_PartNoDbId,
         an_PartNoId,
         an_RelativeBool,
         ad_EffectiveDt,
         av_ReschedFromCd,
         an_RecurringBool,
         an_SchedFromReceivedDtBool,
         as_TaskClassCd
      FROM
         sched_stask,
         evt_event_rel,
         evt_inv,
         evt_event,
         task_task
      WHERE
         sched_stask.sched_db_id = an_SchedDbId AND
         sched_stask.sched_id    = an_SchedId
         AND
         sched_stask.rstat_cd   = 0
         AND
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id
         AND
         evt_event_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
         evt_event_rel.rel_event_id    (+)= sched_stask.sched_id    AND
         evt_event_rel.rel_type_cd     (+)= 'DEPT'
         AND
         evt_inv.event_db_id   = sched_stask.sched_db_id AND
         evt_inv.event_id      = sched_stask.sched_id    AND
         evt_inv.main_inv_bool = 1
         AND
         task_task.task_db_id (+)= sched_stask.task_db_id AND
         task_task.task_id    (+)= sched_stask.task_id;

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetTaskDetails@@@'||SQLERRM);
      RETURN;

END GetTaskDetails;



/********************************************************************************
*
* Procedure:    UpdateDependentDeadlinesTree
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the given task and its children plus forecasted tasks that follow them.
*
* Orig.Coder:   Michal Bajer
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlinesTree(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   /* SQL to retrieve all children of the task */
   CURSOR lcur_TaskTree (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         event_db_id sched_db_id,
         event_id    sched_id
      FROM
         evt_event
      WHERE rstat_cd = 0
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId
      CONNECT BY
         nh_event_db_id = PRIOR event_db_id AND
         nh_event_id    = PRIOR event_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the children of the task*/
   FOR lrec_TasksTree IN lcur_TaskTree( an_StartSchedDbId, an_StartSchedId )
   LOOP

      UpdateDependentDeadlines( lrec_TasksTree.sched_db_id, lrec_TasksTree.sched_id, on_Return );
      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlinesTree@@@: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlinesTree;


/********************************************************************************
*
* Procedure: GetRescheduleFromDtValues
* Arguments:an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*
* Return:
*           ad_StartDt     (date)    - current task's start date
*           av_SchedFromCd (varchar) - actual task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromDtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_sched_dead.sched_dead_dt AS task_due_dt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           ref_eng_unit.ref_mult_qt,
           evt_event.event_dt AS task_end_dt,
           evt_event.hist_bool AS task_hist_bool,
           root_event.actual_start_gdt,
           root_event.sched_start_gdt,
           root_event.event_gdt,
           root_event.sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           sched_stask.task_class_cd
        FROM
           evt_sched_dead,
           evt_event,
           evt_event root_event,
           sched_stask,
           mim_data_type,
           ref_eng_unit
        WHERE
           evt_event.event_db_id =  cn_EventDbId AND
           evt_event.event_id    =  cn_EventId AND
           evt_event.rstat_cd      = 0
           AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
           mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
           AND
           ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
           ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id
           AND
           sched_stask.sched_db_id = root_event.event_db_id AND
           sched_stask.sched_id    = root_event.event_id;
   lrec_task lcur_task%ROWTYPE;

   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;

   ld_DueDate          evt_sched_dead.sched_dead_dt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ld_EndDate          evt_event.event_gdt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;
   ll_RefMultQt        ref_eng_unit.ref_mult_qt%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   lv_TaskClassCd      sched_stask.task_class_cd%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;

   ld_BeginWindowDt    evt_sched_dead.sched_dead_dt%TYPE;
   ld_EndWindowDt      evt_sched_dead.sched_dead_dt%TYPE;
BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ld_StartDt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ld_DueDate          := lrec_task.task_due_dt;
      ll_RefMultQt        := lrec_task.ref_mult_qt;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ld_EndDate          := lrec_task.task_end_dt;
      lb_HistBool         := lrec_task.task_hist_bool;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_TaskClassCd      := lrec_task.task_class_cd;
      ld_RootEndDt        := lrec_task.event_gdt;
      ld_RootStartDt      := lrec_task.actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.sched_start_gdt;
   CLOSE lcur_task;

   -- the previous task is ACTV, so use the extended due date if given
   IF lb_HistBool = 0 AND ld_DueDate IS NOT NULL THEN
      lv_ReschedFromCd := 'LASTDUE';
      ld_StartDt     := ld_DueDate + (ln_Deviation * ll_RefMultQt);

   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF NOT (lv_TaskClassCd = 'CHECK' OR lv_TaskClassCd = 'RO') THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      IF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedStartDt;
      ELSIF ld_EndDate IS NOT NULL THEN
         ld_StartDt       := ld_EndDate;
         lv_ReschedFromCd := 'LASTEND';
      END IF;

      /* (now consider the scheduling window)
       * if the completion fell within the window, use scheduled completion information */
      IF ld_StartDt IS NOT NULL AND ld_DueDate IS NOT NULL THEN
         -- build window
         ld_BeginWindowDt := ld_DueDate - (ln_PrefixedQt * ll_RefMultQt);
         ld_EndWindowDt   := ld_DueDate + (ln_PostfixedQt * ll_RefMultQt);
         IF ( ld_StartDt >= ld_BeginWindowDt ) AND
            ( ld_StartDt <= ld_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ld_StartDt     := ld_DueDate;
         END IF;
      END IF;
  END IF;

  /* if we were able to determin the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ld_StartDt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     ad_StartDt     := ld_StartDt;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     ad_StartDt     := SYSDATE;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromDtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromDtValues;

/********************************************************************************
*
* Procedure:    FindDeadlineStartDate
* Arguments:     an_TaskDbId     (long)     - task definition primary key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_RelativeBool (int)      - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                an_FromRcvdDt   (int)      - boolean, schedule from received date
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                av_TaskClassCd  (varchar) - task class code
* Return:
*                ad_StartDt      (long)    - new deadline start dt
*                av_SchedFromCd  (long)    - new deadline sched from refterm
*                on_Return       (long)    - succss/failure of procedure
*
* Description:  This procedure looks up the start date for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:  November 2010
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartDate (
      an_TaskDbId         IN task_task.task_db_id%TYPE,
      an_TaskId           IN task_task.task_id%TYPE,
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId    IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId      IN sched_stask.sched_id%TYPE,
      ab_RelativeBool     IN task_task.relative_bool%TYPE,
      ad_EffectiveDt      IN task_task.effective_dt%TYPE,
      av_ReschedFromCd    IN task_task.resched_from_cd%TYPE,
      an_FromRcvdDt       IN task_task.sched_from_received_dt_bool%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      av_TaskClassCd      IN task_task.task_class_cd%TYPE,
      ad_StartDt          IN OUT evt_sched_dead.start_dt%TYPE,
      av_SchedFromCd      IN OUT evt_sched_dead.sched_from_cd%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

   /* local variables */

   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ld_CustomDate              DATE;
   ln_FaultDbId   sd_fault.fault_db_id%TYPE;
   ln_FaultId sd_fault.fault_id%TYPE;
   ld_FoundOnDate evt_event.actual_start_dt%TYPE;
   ld_StartDt     evt_sched_dead.start_dt%TYPE;

BEGIN

   /* if this task is not a first task */
   IF NOT an_PrevSchedDbId =-1 THEN
      /* set reschedule_from */
      GetRescheduleFromDtValues(
            an_PrevSchedDbId  ,
            an_PrevSchedId ,
            an_DataTypeDbId,
            an_DataTypeId ,
            av_ReschedFromCd,
            ad_StartDt,
            av_SchedFromCd,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

   /* if there is no previous task */
   ELSE
      ld_CustomDate := TO_DATE('1950-01-01 23:59:59', 'YYYY-MM-DD HH24:MI:SS');

      /* check if this task has a corrective fault */
      GetCorrectiveFaultInfo(
            an_SchedDbId,
            an_SchedId,
            ln_FaultDbId,
            ln_FaultId,
            ld_FoundOnDate,
            ld_StartDt,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      /* if there is a corrective fault */
      IF (ln_FaultId IS NOT NULL) THEN
            IF ld_StartDt IS NOT NULL THEN
                ad_StartDt:=ld_StartDt;
            ELSE
                ad_StartDt:=ld_FoundOnDate;
            END IF;
            av_SchedFromCd:='CUSTOM';
      /* if this task is adhoc or REPL or CORR */
      ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
            (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
            ad_StartDt     := ld_CustomDate;
            av_SchedFromCd := 'CUSTOM';
      /* if this task is based on the task definition */
      ELSE
         /* get main inventory manufacturing and received date */
        GetMainInventoryBirthInfo(
          an_SchedDbId,
          an_SchedId,
          ld_ManufactDt,
          ld_ReceivedDt,
          on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        /* if this task should not be scheduled from birth */
        IF (ab_RelativeBool=1) THEN
          /** if we should schedule from date provided */
          IF ad_PreviousCompletionDt IS NOT NULL THEN
             ad_StartDt     := ad_PreviousCompletionDt;
             av_SchedFromCd := 'CUSTOM';
          /* if there is effective date for the task definition and it is not before the manufactured date */
          ELSIF ad_EffectiveDt IS NOT NULL AND ( ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt)) THEN
            ad_StartDt     := ad_EffectiveDt;
            av_SchedFromCd := 'EFFECTIV';

           /* if the effective date is before the manufactured date, use the manufactured date */
           ELSIF ad_EffectiveDt IS NOT NULL AND ld_ManufactDt IS NOT NULL AND ld_ManufactDt > ad_EffectiveDt THEN
            ad_StartDt     := ld_ManufactDt;
            av_SchedFromCd := 'CUSTOM';

           ELSE
            ad_StartDt     := ld_CustomDate;
            av_SchedFromCd := 'CUSTOM';
           END IF;
        /* if the task should be scheduled from birth*/
        ELSE

          /* if the task is a create_on_install task, and a completion date of the
             installation task was provided, then use that date */
            IF ad_PreviousCompletionDt IS NOT NULL THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
            ELSIF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL THEN
               /* schedule from received date */
               ad_StartDt     := ld_ReceivedDt;
               av_SchedFromCd := 'BIRTH';
            ELSIF an_FromRcvdDt=0 AND ld_ManufactDt IS NOT NULL THEN
               /* schedule from manufacturer date */
               ad_StartDt     := ld_ManufactDt;
               av_SchedFromCd := 'BIRTH';
            ELSE
               ad_StartDt     := ld_CustomDate;
               av_SchedFromCd := 'CUSTOM';
            END IF;
        END IF;
      END IF;
     END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartDate@@@ '||SQLERRM);
      RETURN;
END FindDeadlineStartDate;

/********************************************************************************
*
* Procedure: GetHistoricUsageAtDt
* Arguments:
*            ad_TargetDate     (Date) - usage at this date
*            an_DataTypeDbId   (long) data type primary key
*            an_DataTypeId     (long) --//--
*            an_InvNoDbId      (long) inventory primary key
*            an_InvNoId        (long) --//--
* Return:
*             an_TsnQt         (float) - TSN value at the target date
*             on_Return        (long) 1 is success
*
* Description: This procedure returns the TSN value of the specified inventory
*              at the specified target date.
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    March 13, 2008
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetHistoricUsageAtDt(
            ad_TargetDate   IN  evt_sched_dead.start_dt%TYPE,
            an_DataTypeDbId IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId   IN  evt_sched_dead.data_type_id%TYPE,
            an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
            on_TsnQt        OUT evt_inv_usage.tsn_qt%TYPE,
            on_Return       OUT typn_RetCode
   ) IS

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    SELECT
        tsn_qt INTO on_TsnQt
    FROM
    (

        SELECT
            tsn_qt
        FROM
        (
            SELECT
                evt_inv_usage.tsn_qt,
                evt_event.event_dt,
                evt_event.creation_dt
            FROM
                evt_inv_usage,
                inv_inv,
                inv_inv component_inv,
                evt_inv,
                evt_event
            WHERE
                inv_inv.inv_no_db_id = an_InvNoDbId AND
                inv_inv.inv_no_id    = an_InvNoId AND
                inv_inv.rstat_cd     = 0
                AND
                (
                        -- for SYS or TRK, use assembly to get usage
                    (
                        inv_inv.inv_class_cd IN ('SYS','TRK' ) AND
                        component_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                        component_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
                    )
                    OR
                        -- for ACFT or ASSY, use itself
                    (
                        inv_inv.inv_class_cd IN ('ACFT', 'ASSY') AND
                        component_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                        component_inv.inv_no_id    = inv_inv.inv_no_id
                    )
                )
                AND
                evt_inv.inv_no_db_id = component_inv.inv_no_db_id AND
                evt_inv.inv_no_id    = component_inv.inv_no_id
                AND
                evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
                evt_inv_usage.event_id     = evt_inv.event_id AND
                evt_inv_usage.event_inv_id = evt_inv.event_inv_id AND
                evt_inv_usage.data_type_db_id = an_DataTypeDbId AND
                evt_inv_usage.data_type_id    = an_DataTypeId AND
                evt_inv_usage.negated_bool    = 0
                AND
                evt_event.event_db_id = evt_inv.event_db_id AND
                evt_event.event_id    = evt_inv.event_id AND
                evt_event.hist_bool   = 1 AND
                evt_event.event_dt <= ad_TargetDate AND
                evt_event.event_type_cd = 'FG'

            UNION

            SELECT
                usg_usage_data.tsn_qt,
                usg_usage_record.usage_dt as event_dt,
                usg_usage_record.creation_dt as creation_dt
            FROM
                inv_inv,
                inv_inv component_inv,
                usg_usage_data,
                usg_usage_record
            WHERE
                inv_inv.inv_no_db_id = an_InvNoDbId AND
                inv_inv.inv_no_id    = an_InvNoId AND
                inv_inv.rstat_cd     = 0
                AND
                (
                    -- for SYS or TRK, use assembly to get usage
                    (
                    inv_inv.inv_class_cd IN ('SYS','TRK' ) AND
                    component_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                    component_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
                    )
                    OR
                    -- for ACFT or ASSY, use itself
                    (
                    inv_inv.inv_class_cd IN ('ACFT', 'ASSY') AND
                    component_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                    component_inv.inv_no_id    = inv_inv.inv_no_id
                    )
                )
                AND
                usg_usage_data.inv_no_db_id = component_inv.inv_no_db_id AND
                usg_usage_data.inv_no_id    = component_inv.inv_no_id
                AND
                usg_usage_data.data_type_db_id = an_DataTypeDbId AND
                usg_usage_data.data_type_id    = an_DataTypeId AND
                usg_usage_data.negated_bool    = 0
                AND
                usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
                AND
                usg_usage_record.usage_dt <= ad_TargetDate
            )
            ORDER BY event_dt DESC,creation_dt DESC
        )
    WHERE
        rownum = 1;

   -- Return success
   on_Return := icn_Success;

  EXCEPTION
   WHEN NO_DATA_FOUND THEN
      on_TsnQt := 0;
   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetHistoricUsageAtDt@@@'||SQLERRM);
   RETURN;

END GetHistoricUsageAtDt;


/********************************************************************************
*
* Procedure: GetRescheduleFromQtValues
* Arguments:
*           an_EventDbId     (long)    - previous task's primary key
*           an_EventId       (long)    --//--
*           an_DataTypeDbId  (long)    - deadline data type primary key
*           an_DataTypeId    (long)    --//--
*           av_ReschedFromCd (varchar) - current task's baseline's reschedule_from_cd
*           an_InvNoDbId     (long)    - current task's main inventory primary key
*           an_InvNoId       (long)    --//--
* Return:
*           ad_StartDt     (date)    - current task's start date
*           ad_StartQt     (date)    - current task's start quantity
*           av_SchedFromCd (varchar) - current task's reschedule from code
*           on_Return                - 1 is success
*
* Description: This procedure returns the start date and schedule from code for the next task
*
*
* Orig.Coder:     Julie Bajer
* Recent Coder:
* Recent Date:    November 2010
*
*********************************************************************************
*
* Copyright 2000-2004 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetRescheduleFromQtValues(
            an_EventDbId     IN  evt_sched_dead.event_db_id%TYPE,
            an_EventId       IN  evt_sched_dead.event_id%TYPE,
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            av_ReschedFromCd IN  task_task.resched_from_cd%TYPE,
            an_InvNoDbId     IN  inv_inv.inv_no_db_id%TYPE,
            an_InvNoId       IN  inv_inv.inv_no_id%TYPE,
            ad_StartDt       OUT evt_sched_dead.start_dt%TYPE,
            an_StartQt       OUT evt_sched_dead.start_qt%TYPE,
            av_SchedFromCd   OUT evt_sched_dead.sched_from_cd%TYPE,
            on_Return        OUT typn_RetCode
   ) IS

   /* previous task information */
   CURSOR lcur_task (
           cn_EventDbId      evt_sched_dead.event_db_id%TYPE,
           cn_EventId        evt_sched_dead.event_id%TYPE,
           cn_DataTypeDbId   evt_sched_dead.data_type_db_id%TYPE,
           cn_DataTypeId     evt_sched_dead.data_type_id%TYPE
        ) IS
        SELECT
           evt_inv_usage.tsn_qt,
           evt_sched_dead.sched_dead_qt,
           evt_sched_dead.deviation_qt,
           evt_sched_dead.prefixed_qt,
           evt_sched_dead.postfixed_qt,
           evt_event.hist_bool AS task_hist_bool,
           root_event.actual_start_gdt,
           root_event.sched_start_gdt,
           root_event.event_gdt,
           root_event.sched_end_gdt,
           root_event.hist_bool AS root_hist_bool,
           sched_stask.task_class_cd
        FROM
           evt_sched_dead,
           evt_event,
           evt_inv_usage,
           evt_event root_event,
           sched_stask
        WHERE
           evt_event.event_db_id =  cn_EventDbId AND
           evt_event.event_id    =  cn_EventId   AND
           evt_event.rstat_cd      = 0
           AND
           evt_sched_dead.event_db_id     (+)= evt_event.event_db_id AND
           evt_sched_dead.event_id        (+)= evt_event.event_id    AND
           evt_sched_dead.data_type_db_id (+)= cn_DataTypeDbId       AND
           evt_sched_dead.data_type_id    (+)= cn_DataTypeId
           AND
           evt_inv_usage.event_db_id     (+)= evt_event.event_db_id AND
           evt_inv_usage.event_id        (+)= evt_event.event_id    AND
           evt_inv_usage.data_type_db_id (+)= cn_DataTypeDbId       AND
           evt_inv_usage.data_type_id    (+)= cn_DataTypeId
           AND
           root_event.event_db_id = evt_event.h_event_db_id AND
           root_event.event_id    = evt_event.h_event_id
           AND
           sched_stask.sched_db_id = root_event.event_db_id AND
           sched_stask.sched_id    = root_event.event_id;
   lrec_task lcur_task%ROWTYPE;


   lv_ReschedFromCd    evt_sched_dead.sched_from_cd%TYPE;
   ld_StartDt          evt_sched_dead.start_dt%TYPE;
   ln_StartQt          evt_sched_dead.start_qt%TYPE;

   ln_DueQt            evt_sched_dead.sched_dead_qt%TYPE;
   ln_Deviation        evt_sched_dead.deviation_qt%TYPE;
   ln_PrefixedQt       evt_sched_dead.prefixed_qt%TYPE;
   ln_PostfixedQt      evt_sched_dead.postfixed_qt%TYPE;
   ln_EndQt            evt_inv_usage.tsn_qt%TYPE;
   lb_HistBool         evt_event.hist_bool%TYPE;

   lb_RootHistBool     evt_event.hist_bool%TYPE;
   lv_TaskClassCd      sched_stask.task_class_cd%TYPE;
   ld_RootEndDt        evt_event.event_gdt%TYPE;
   ld_RootStartDt      evt_event.actual_start_gdt%TYPE;
   ld_RootSchedEndDt   evt_event.sched_end_gdt%TYPE;
   ld_RootSchedStartDt evt_event.sched_start_gdt%TYPE;

   ln_BeginWindowDt    evt_sched_dead.sched_dead_qt%TYPE;
   ln_EndWindowDt      evt_sched_dead.sched_dead_qt%TYPE;
BEGIN

   -- Initialize the return value
   on_Return        := icn_NoProc;
   lv_ReschedFromCd := av_ReschedFromCd;
   ln_StartQt       := NULL;

    /* extract the previous task's information for readability */
   OPEN  lcur_task(an_EventDbId, an_EventId, an_DataTypeDbId, an_DataTypeId);
   FETCH lcur_task INTO lrec_task;
      ln_DueQt            := lrec_task.sched_dead_qt;
      ln_Deviation        := lrec_task.deviation_qt;
      ln_PrefixedQt       := lrec_task.prefixed_qt;
      ln_PostfixedQt      := lrec_task.postfixed_qt;
      ln_EndQt            := lrec_task.tsn_qt;
      lb_HistBool         := lrec_task.task_hist_bool;
      lb_RootHistBool     := lrec_task.root_hist_bool;
      lv_TaskClassCd      := lrec_task.task_class_cd;
      ld_RootEndDt        := lrec_task.event_gdt;
      ld_RootStartDt      := lrec_task.actual_start_gdt;
      ld_RootSchedEndDt   := lrec_task.sched_end_gdt;
      ld_RootSchedStartDt := lrec_task.sched_start_gdt;
   CLOSE lcur_task;

   -- the previous task is ACTV, so use the extended due date if given
   IF lb_HistBool = 0 AND ln_DueQt IS NOT NULL THEN
      lv_ReschedFromCd := 'LASTDUE';
      ln_StartQt       := ln_DueQt + ln_Deviation;

   ELSIF lb_HistBool = 1 THEN -- the previous task is COMPLETED

      -- if the previous task is not assigned to a CHECK/RO, we must reschedule from EXECUTE
      IF NOT (lv_TaskClassCd = 'CHECK' OR lv_TaskClassCd = 'RO') THEN
         lv_ReschedFromCd := 'EXECUTE';
      END IF;

      -- reschedule according to the LASTEND (ignore the scheduling window for now)
      IF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 1 AND ld_RootEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootEndDt;
      ELSIF lv_ReschedFromCd = 'WPEND' AND lb_RootHistBool = 0 AND ld_RootSchedEndDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedEndDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 1 AND ld_RootStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootStartDt;
      ELSIF lv_ReschedFromCd = 'WPSTART' AND lb_RootHistBool = 0 AND ld_RootSchedStartDt IS NOT NULL THEN
         ld_StartDt := ld_RootSchedStartDt;
      ELSIF ln_EndQt IS NOT NULL THEN
         ln_StartQt       := ln_EndQt;
         lv_ReschedFromCd := 'LASTEND';
      END IF;

      -- lookup the usage at the date
      IF ln_StartQt IS NULL AND ld_StartDt IS NOT NULL THEN
         GetHistoricUsageAtDt ( ld_StartDt,
                                an_DataTypeDbId,
                                an_DataTypeId,
                                an_InvNoDbId,
                                an_InvNoId,
                                ln_StartQt,
                                on_Return );
      END IF;

       /* (now consider the scheduling window)
       * if the completion fell within the window, use scheduled completion information */
      IF ln_StartQt IS NOT NULL AND ln_DueQt IS NOT NULL THEN
         -- build window
         ln_BeginWindowDt := ln_DueQt - ln_PrefixedQt;
         ln_EndWindowDt   := ln_DueQt + ln_PostfixedQt;
         IF ( ln_StartQt >= ln_BeginWindowDt ) AND
            ( ln_StartQt <= ln_EndWindowDt ) THEN
            -- was within window
            lv_ReschedFromCd := 'LASTDUE';
            ln_StartQt       := ln_DueQt;
         END IF;
      END IF;
   END IF;

  /* if we were able to determin the rescheduling values, use them.
     Otherwise we are missing information so use CUSTOM */
  IF ln_StartQt IS NOT NULL THEN
     av_SchedFromCd := lv_ReschedFromCd;
     an_StartQt     := ln_StartQt;
     -- a future start date indicates that we want to use the forecasting engine,
     -- and in this case we do not.
     IF ld_StartDt IS NOT NULL AND ld_StartDt <= SYSDATE THEN
        ad_StartDt     := ld_StartDt;
     END IF;
  ELSE
     av_SchedFromCd := 'CUSTOM';
     GetCurrentInventoryUsage(
           an_EventDbId,
           an_EventId,
           an_DataTypeDbId,
           an_DataTypeId,
           an_StartQt,
           on_Return );
     IF on_Return < 0 THEN
        RETURN;
     END IF;
  END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      IF lcur_task%ISOPEN THEN CLOSE lcur_task; END IF;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@GetRescheduleFromQtValues@@@'||SQLERRM);
      RETURN;

END GetRescheduleFromQtValues;

/********************************************************************************
*
* Procedure:    FindDeadlineStartQt
* Arguments:     an_TaskDbId     (long)     - task definition key
*                an_TaskId       (long)     --//--
*                an_SchedDbId    (long)     - task primary key
*                an_SchedId      (long)     --//--
*                an_DataTypeDbId (long)     - data type primary key
*                an_DataTypeId   (long)     --//--
*                an_PrevSchedDbId(long)     - previous task's primary key
*                an_PrevSchedId  (long)     --//--
*                ab_RelativeBool (int)      - boolean
*                ad_EffectiveDt  (date)     - effective date of the task definition
*                av_ReschedFromCd(varchar)  - baseline's reschedule_from_cd
*                an_FromRcvdDt   (int)      - boolean, schedule from received date
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
*                an_EventsOnSameInv (int)   - true if current and previous tasks are on the same inventory.
*                av_TaskClassCd  (varchar)  - task class code
* Return:
*                ad_StartQt      (long)     - new deadline start qt
*                av_SchedFromCd  (long)     - new deadline sched from refterm
*                ad_StartDt      (long)     - new deadline start dt
*                on_Return       (long)     - succss/failure of procedure
*
* Description:  This procedure looks up the start qt and sched_from_cd for the deadline based on
*               many conditions.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:  November 2010
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE FindDeadlineStartQt (
      an_TaskDbId             IN task_task.task_db_id%TYPE,
      an_TaskId               IN task_task.task_id%TYPE,
      an_SchedDbId            IN sched_stask.sched_db_id%TYPE,
      an_SchedId              IN sched_stask.sched_id%TYPE,
      an_DataTypeDbId         IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId           IN task_sched_rule.data_type_id%TYPE,
      an_PrevSchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_PrevSchedId          IN sched_stask.sched_id%TYPE,
      an_RelativeBool         IN task_task.relative_bool%TYPE,
      ad_EffectiveDt          IN task_task.effective_dt%TYPE,
      av_ReschedFromCd        IN task_task.resched_from_cd%TYPE,
      an_FromRcvdDt           IN task_task.sched_from_received_dt_bool%TYPE,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      an_EventsOnSameInv      IN NUMBER,
      av_TaskClassCd          IN task_task.task_class_cd%TYPE,
      an_StartQt              IN OUT evt_sched_dead.start_qt%TYPE,
      av_SchedFromCd          IN OUT evt_sched_dead.sched_from_cd%TYPE,
      ad_StartDt              OUT evt_sched_dead.start_dt%TYPE,
      on_Return               OUT typn_RetCode
   ) IS


   /* local variables */
   ln_InvNoDbId               inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId                 inv_inv.inv_no_id%TYPE;
   ld_ReceivedDt              inv_inv.received_dt%TYPE;
   ld_ManufactDt              inv_inv.manufact_dt%TYPE;
   ln_FaultDbId               sd_fault.fault_db_id%TYPE;
   ln_FaultId                 sd_fault.fault_id%TYPE;
   ld_FoundOnDate             evt_event.actual_start_dt%TYPE;
   ld_StartDt                 evt_sched_dead.start_dt%TYPE;
   ln_CustomQt                NUMBER;
   lIsInstalledOnAircraft number;
   lAircraftDbId number;
   lAircraftId number;
BEGIN

   /* set ln_InvNoPk for later use */
   EVENT_PKG.GetMainInventory(
         an_SchedDbId,
         an_SchedId,
         ln_InvNoDbId,
         ln_InvNoId,
         on_Return);
   IF on_Return < 0 THEN
         RETURN;
   END IF;

   /* if this task is a first task being create */
   IF NOT an_PrevSchedId =-1 THEN
       /*if this task is not on the same inventory */
       IF an_EventsOnSameInv = 0 THEN

           /* get the current inventory usage */
           GetCurrentInventoryUsage(
                an_SchedDbId,
                an_SchedId,
                an_DataTypeDbId,
                an_DataTypeId,
                an_StartQt,
                on_Return );
           IF on_Return < 0 THEN
              RETURN;
           END IF;

           av_SchedFromCd:='CUSTOM';

       /* if the task is on the same inventory as the previous task */
       ELSE

          /* set reschedule_from */
          GetRescheduleFromQtValues(
                an_PrevSchedDbId,
                an_PrevSchedId,
                an_DataTypeDbId,
                an_DataTypeId,
                av_ReschedFromCd,
                ln_InvNoDbId,
                ln_InvNoId,
                ad_StartDt,
                an_StartQt,
                av_SchedFromCd,
                on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;
       END IF;

    /*if this task is a first task being created */
    ELSE
        ln_CustomQt := 0;

        /* find fault if this task is associated with one */
        GetCorrectiveFaultInfo(
                an_SchedDbId,
                an_SchedId,
                ln_FaultDbId,
                ln_FaultId,
                ld_FoundOnDate,
                ld_StartDt,
                on_Return );
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        /* if fault is found */
        IF (ln_FaultId IS NOT NULL) THEN
           /* schedule from custom start date if exists a CUSTOM deadline already */
           IF ld_StartDt IS NOT NULL THEN
              ad_StartDt     := ld_StartDt;
              av_SchedFromCd := 'CUSTOM';
              /* if the start date is in the past, get the usage at that time */
              IF ld_StartDt< TRUNC(SYSDATE+1) THEN
                   GetHistoricUsageAtDt(
                         ld_StartDt,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         ln_InvNoDbId,
                         ln_InvNoId,
                         an_StartQt,
                         on_Return);
              ELSE
              /* the date is in the future, use the current usage or predict it if installed on aircraft*/
                   GetCurrentInventoryUsage(
                         an_SchedDbId,
                         an_SchedId,
                         an_DataTypeDbId,
                         an_DataTypeId,
                         an_StartQt,
                         on_Return );
                   IF on_Return < 0 THEN
                      RETURN;
                   END IF;

                   lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft( ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                   IF lIsInstalledOnAircraft=1 THEN
                           EVENT_PKG.PredictUsageBetweenDt( lAircraftDbId,
                                                            lAircraftId,
                                                            an_DataTypeDbId,
                                                            an_DataTypeId,
                                                            SYSDATE,
                                                            ld_StartDt,
                                                            an_StartQt,
                                                            an_StartQt,
                                                            on_Return);
                   END IF;
              END IF;
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           ELSE
              ad_StartDt     := ld_FoundOnDate;
              av_SchedFromCd := 'CUSTOM';

              /* get usage snapshot of this fault */
              GetOldEvtInvUsage( ln_FaultDbId,
                                 ln_FaultId,
                                 an_DataTypeDbId,
                                 an_DataTypeId,
                                 an_StartQt,
                                 on_Return );
              IF on_Return < 0 THEN
                 RETURN;
              END IF;
           END IF;

        /* if task is adhoc or REPL or CORR */
        ELSIF (an_TaskDbId IS NULL AND an_TaskId IS NULL) OR
              (av_TaskClassCd = 'CORR' OR av_TaskClassCd = 'REPL') THEN
                av_SchedFromCd := 'CUSTOM';
                an_StartQt     := ln_CustomQt;

        /* if task is based on task definition */
        ELSE
           /* get main inventory manufacturing and received date */
          GetMainInventoryBirthInfo(
            an_SchedDbId,
            an_SchedId,
            ld_ManufactDt,
            ld_ReceivedDt,
            on_Return );
          IF on_Return < 0 THEN
             RETURN;
          END IF;

         /* if the task is scheduled from install date */
          IF ( ad_PreviousCompletionDt IS NOT NULL) THEN
               ad_StartDt     := ad_PreviousCompletionDt;
               av_SchedFromCd := 'CUSTOM';
               /* get the usage at the installation snapshot */
               GetHistoricUsageAtDt(
                      ad_PreviousCompletionDt,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      ln_InvNoDbId,
                      ln_InvNoId,
                      an_StartQt,
                      on_Return);

          /* if the task should be scheduled from birth */
          ELSIF (an_RelativeBool = 0 ) THEN

              IF an_FromRcvdDt=1 AND ld_ReceivedDt IS NOT NULL AND ld_ReceivedDt < SYSDATE THEN
                   ad_StartDt     := ld_ReceivedDt;
                   av_SchedFromCd := 'BIRTH';
                   /* get the usage at received date */
                   GetHistoricUsageAtDt(
                          ld_ReceivedDt,
                          an_DataTypeDbId,
                          an_DataTypeId,
                          ln_InvNoDbId,
                          ln_InvNoId,
                          an_StartQt,
                          on_Return);
               ELSIF an_FromRcvdDt=0  THEN
                   -- we do not enforce a manufactured date in order to schedule from BIRTH for usage rules
                   av_SchedFromCd := 'BIRTH';
                   an_StartQt     := 0;
               ELSE
                   av_SchedFromCd := 'CUSTOM';
                   an_StartQt     := ln_CustomQt;
               END IF;

          /* if the task should not be scheduled from birth */
          ELSIF ad_EffectiveDt IS NOT NULL AND (ld_ManufactDt IS NULL OR (ld_ManufactDt IS NOT NULL AND ld_ManufactDt <= ad_EffectiveDt ) ) THEN
                 ad_StartDt     := ad_EffectiveDt;
                 av_SchedFromCd := 'EFFECTIV';

                 /* That's where we need to define the TSN value at the specified date */
                 IF ad_EffectiveDt<SYSDATE THEN
                   GetHistoricUsageAtDt(
                      ad_EffectiveDt,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      ln_InvNoDbId,
                      ln_InvNoId,
                      an_StartQt,
                      on_Return);

                 ELSE
                   GetCurrentInventoryUsage(
                      an_SchedDbId,
                      an_SchedId,
                      an_DataTypeDbId,
                      an_DataTypeId,
                      an_StartQt,
                      on_Return );
                   IF on_Return < 0 THEN
                      RETURN;
                   END IF;
                    lIsInstalledOnAircraft := EVENT_PKG.IsInstalledOnAircraft(ln_InvNoDbId, ln_InvNoId, lAircraftDbId, lAircraftId);
                    IF lIsInstalledOnAircraft=1 THEN
                      EVENT_PKG.PredictUsageBetweenDt(
                                                      lAircraftDbId,
                                                      lAircraftId,
                                                      an_DataTypeDbId,
                                                      an_DataTypeId,
                                                      SYSDATE ,
                                                      ad_EffectiveDt,
                                                      an_StartQt,
                                                      an_StartQt,
                                                      on_Return);
                     ELSE
                       GetCurrentInventoryUsage(
                            an_SchedDbId,
                            an_SchedId,
                            an_DataTypeDbId,
                            an_DataTypeId,
                            an_StartQt,
                            on_Return );
                     END IF;
                  END IF;
                  IF on_Return < 0 THEN
                     RETURN;
                  END IF;
          /* if the effective date is before the manufactured date, use the manufactured date */
          ELSIF ad_EffectiveDt IS NOT NULL AND ld_ManufactDt IS NOT NULL AND ld_ManufactDt > ad_EffectiveDt THEN
               an_StartQt     := 0;
               av_SchedFromCd := 'CUSTOM';

          ELSE
               av_SchedFromCd := 'CUSTOM';
               an_StartQt     := ln_CustomQt;
          END IF;
        END IF;
    END IF;

   /* return success */
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@FindDeadlineStartQt@@@: '||SQLERRM);
      RETURN;
END FindDeadlineStartQt;


/********************************************************************************
*
* Procedure:    PrepareCalendarDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                           a component that fired the create_on_install
*                                           logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares calednar deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder:   Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareCalendarDeadline (
      an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
      an_SchedId           IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId  IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId    IN task_task.task_id%TYPE,
      an_DataTypeDbId      IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId        IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      ad_PreviousCompletionDt  IN evt_event.sched_end_gdt%TYPE,
      on_Return            OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;

      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ld_NewDeadlineDt         evt_sched_dead.sched_dead_dt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ld_NewDeadlineDt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ld_EffectiveDt,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the original task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE
      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );

      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;


   /* find the start date of the deadline */
   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      FindDeadlineStartDate(
         ln_TaskDbId,
         ln_TaskId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_RelativeBool,
         ld_EffectiveDt,
         lv_ReschedFromCd,
         ln_SchedFromReceivedDtBool,
         ad_PreviousCompletionDt,
         ls_TaskClassCd,
         ld_StartDt,
         ls_SchedFromCd,
         on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find the new deadline date */
   FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_NewDeadlineDt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
               an_SchedDbId,
               an_SchedId,
               an_DataTypeDbId,
               an_DataTypeId,
               NULL,
               ld_StartDt,
               ls_SchedFromCd,
               NULL,
               ld_NewDeadlineDt,
               ln_IntervalQt,
               ln_NotifyQt,
               ln_DeviationQt,
               ln_PrefixedQt,
               ln_PostfixedQt,
               on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;

    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
                 an_SchedDbId,
                 an_SchedId,
                 an_DataTypeDbId,
                 an_DataTypeId,
                 NULL,
                 ld_StartDt,
                 ls_SchedFromCd,
                 ln_IntervalQt,
                 NULL,
                 ld_NewDeadlineDt,
                 ln_NotifyQt,
                 ln_DeviationQt,
                 ln_PrefixedQt,
                 ln_PostfixedQt,
                 on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
    END IF;

   -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
   UPDATE evt_sched_dead
   SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
   WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
          evt_sched_dead.data_type_id    = an_DataTypeId   AND
          evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
          (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
          IN
          (
           SELECT evt_event_rel.rel_event_db_id,
                  evt_event_rel.rel_event_id
           FROM   evt_event,
                  evt_event_rel
           WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                  evt_event_rel.event_id    = an_SchedId   AND
                  evt_event_rel.rel_type_cd = 'DEPT'
                  AND
                  evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                  evt_event.event_id        = evt_event_rel.rel_event_id    AND
                  evt_event.event_status_cd = 'FORECAST' );

    -- Return success
    on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareCalendarDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareCalendarDeadline;


/********************************************************************************
*
* Procedure:    PrepareUsageDeadline
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                an_DataTypeDbId (long) - data type primary key
*                an_DataTypeId   (long) --//--
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares usage deadline of a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2005 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareUsageDeadline (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      an_DataTypeDbId     IN task_sched_rule.data_type_db_id%TYPE,
      an_DataTypeId       IN task_sched_rule.data_type_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      ls_TaskClassCd    task_task.task_class_cd%TYPE;
      ln_PrevSchedDbId  sched_stask.sched_db_id%TYPE;
      ln_PrevSchedId    sched_stask.sched_id%TYPE;
      ln_OrigTaskDbId   task_task.task_db_id%TYPE;
      ln_OrigTaskId     task_task.task_id%TYPE;
      ln_TaskDbId       task_interval.task_db_id%TYPE;
      ln_TaskId         task_interval.task_id%TYPE;
      ln_PartNoDbId     task_interval.part_no_db_id%TYPE;
      ln_PartNoId       task_interval.part_no_id%TYPE;
      ln_RelativeBool   task_task.relative_bool%TYPE;
      ld_EffectiveDt    task_task.effective_dt%TYPE;
      lv_ReschedFromCd  task_task.resched_from_cd%TYPE;
      ln_RecurringBool  task_task.recurring_task_bool%TYPE;
      ln_HInvNoDbId     task_ac_rule.inv_no_db_id%TYPE;
      ln_HInvNoId       task_ac_rule.inv_no_id%TYPE;
      ln_IntervalQt     evt_sched_dead.interval_qt%TYPE;
      ln_NotifyQt       evt_sched_dead.notify_qt%TYPE;
      ln_DeviationQt    evt_sched_dead.deviation_qt%TYPE;
      ln_PrefixedQt     evt_sched_dead.prefixed_qt%TYPE;
      ln_PostfixedQt    evt_sched_dead.postfixed_qt%TYPE;
      ln_ActualDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;
      ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
      --synch action
      ln_DeleteActualDealine    NUMBER;
      ln_UpdateActualDeadline   NUMBER;
      ln_InsertActualDeadline   NUMBER;

      -- actuals information
      ls_SchedFromCd           evt_sched_dead.sched_from_cd%TYPE;
      ld_StartDt               evt_sched_dead.start_dt%TYPE;
      ln_StartQt               evt_sched_dead.start_qt%TYPE;
      ln_NewDeadlineQt         evt_sched_dead.sched_dead_qt%TYPE;
      ln_EventsOnSameInv       NUMBER;
BEGIN

   -- Initialize the return value
   on_Return               := icn_NoProc;
   ln_DeleteActualDealine  := 0;
   ln_UpdateActualDeadline := 0;
   ln_InsertActualDeadline := 0;
   ln_NewDeadlineQt        := NULL;

   /* get task details */
   GetTaskDetails(
            an_SchedDbId,
            an_SchedId,
            ln_PrevSchedDbId,
            ln_PrevSchedId,
            ln_TaskDbId,
            ln_TaskId,
            ln_HInvNoDbId,
            ln_HInvNoId,
            ln_PartNoDbId,
            ln_PartNoId,
            ln_RelativeBool,
            ld_EffectiveDt,
            lv_ReschedFromCd,
            ln_RecurringBool,
            ln_SchedFromReceivedDtBool,
            ls_TaskClassCd,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   AreTasksOnTheSameInventory(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_EventsOnSameInv,
         on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   /* only if baseline syncronization was requested */
   IF abSyncWithBaseline AND ls_TaskClassCd <> 'CORR' AND ls_TaskClassCd <> 'REPL' THEN
      --if we provided a key for the previous task_task, use it.
      IF(an_OrigTaskTaskDbId IS NOT NULL) THEN
        ln_OrigTaskDbId := an_OrigTaskTaskDbId;
        ln_OrigTaskId   := an_OrigTaskTaskId;
      ELSE
        ln_OrigTaskDbId := ln_TaskDbId;
        ln_OrigTaskId   := ln_TaskId;
      END IF;

      GetValuesAndActionForSynch(
         ln_OrigTaskDbId,
         ln_OrigTaskId,
         ln_TaskDbId,
         ln_TaskId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         an_SchedDbId,
         an_SchedId,
         an_DataTypeDbId,
         an_DataTypeId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RecurringBool,
         ln_EventsOnSameInv,
         ln_DeleteActualDealine,
         ln_UpdateActualDeadline,
         ln_InsertActualDeadline,
         ls_SchedFromCd,
         ld_StartDt,
         ln_StartQt,
         ln_IntervalQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         on_Return
      );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      -- if delete, then delete the actual deadline
      IF ln_DeleteActualDealine = 1 THEN
         DeleteDeadline(
             an_SchedDbId ,
             an_SchedId,
             an_DataTypeDbId,
             an_DataTypeId,
             on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

   ELSE

      /* get existing deadline */
      GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            an_SchedDbId,
            an_SchedId,
            ln_TaskDbId,
            ln_TaskId,
            TRUE,
            ls_SchedFromCd,
            ln_IntervalQt,
            ln_NotifyQt,
            ln_DeviationQt,
            ln_PrefixedQt,
            ln_PostfixedQt,
            ld_StartDt,
            ln_StartQt,
            ln_ActualDeadlineExists,
            on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      IF ln_ActualDeadlineExists = 0 THEN
         -- Return success
         on_Return := icn_Success;
         RETURN;
      END IF;

      ln_UpdateActualDeadline := 1;

   END IF;

      /* find the start date of the deadline */
   IF ls_SchedFromCd IS NULL OR NOT ls_SchedFromCd = 'CUSTOM' THEN
      /* find deadline start qt */
      FindDeadlineStartQt (
          ln_TaskDbId,
          ln_TaskId,
          an_SchedDbId,
          an_SchedId,
          an_DataTypeDbId,
          an_DataTypeId,
          ln_PrevSchedDbId,
          ln_PrevSchedId,
          ln_RelativeBool,
          ld_EffectiveDt,
          lv_ReschedFromCd,
          ln_SchedFromReceivedDtBool,
          ad_PreviousCompletionDt,
          ln_EventsOnSameInv,
          ls_TaskClassCd,
          ln_StartQt,
          ls_SchedFromCd,
          ld_StartDt,
          on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   /* find deadline qt valur */
   FindUsageDeadlineVariables(
            ln_StartQt,
            ln_IntervalQt,
            ln_NewDeadlineQt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

    /* if we need to update the existing deadline */
    IF ln_UpdateActualDeadline = 1 THEN
       UpdateDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_NewDeadlineQt,
              NULL,
              ln_IntervalQt,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
    /* if it doesn't exist, insert it */
    ELSIF ln_InsertActualDeadline = 1 THEN

       InsertDeadlineRow(
              an_SchedDbId,
              an_SchedId,
              an_DataTypeDbId,
              an_DataTypeId,
              ln_StartQt,
              ld_StartDt,
              ls_SchedFromCd,
              ln_IntervalQt,
              ln_NewDeadlineQt,
              NULL,
              ln_NotifyQt,
              ln_DeviationQt,
              ln_PrefixedQt,
              ln_PostfixedQt,
              on_Return );
       IF on_Return < 0 THEN
          RETURN;
       END IF;
     END IF;

     -- see if the next FORECAST task has the rule scheduled from CUSTOM, if so, update to LASTDUE
     UPDATE evt_sched_dead
     SET    evt_sched_dead.sched_from_cd = 'LASTDUE'
     WHERE  evt_sched_dead.data_type_db_id = an_DataTypeDbId AND
            evt_sched_dead.data_type_id    = an_DataTypeId   AND
            evt_sched_dead.sched_from_cd   = 'CUSTOM'        AND
            (evt_sched_dead.event_db_id, evt_sched_dead.event_id)
            IN
            (
             SELECT evt_event_rel.rel_event_db_id,
                    evt_event_rel.rel_event_id
             FROM   evt_event,
                    evt_event_rel
             WHERE  evt_event_rel.event_db_id = an_SchedDbId AND
                    evt_event_rel.event_id    = an_SchedId   AND
                    evt_event_rel.rel_type_cd = 'DEPT'
                    AND
                    evt_event.event_db_id     = evt_event_rel.rel_event_db_id AND
                    evt_event.event_id        = evt_event_rel.rel_event_id    AND
                    evt_event.event_status_cd = 'FORECAST' );

     -- Return success
     on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareUsageDeadline@@@'||SQLERRM || '-SchedPk-' || an_SchedDbId ||':' ||  an_SchedId || '-DataTypePk-' || an_DataTypeDbId ||':' || an_DataTypeId);
      RETURN;
END PrepareUsageDeadline;



/********************************************************************************
*
* Procedure:    PrepareSchedDeadlines
* Arguments:     an_SchedDbId    (long) - task primary key
*                an_SchedId      (long) --//--
*                an_OrigTaskTaskDbId (long) -previous task task key, if NULL use the from sched
*                an_OrigTaskTaskId   (long) --//--
*                ad_PreviousCompletionDt (date) -completion date of the installation of
*                                            a component that fired the create_on_install
*                                            logic. Otherwise this value is NULL
* Return:
*                on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure prepares all deadlines for a task.
* Orig.Coder:   Michal Bajer
* Recent Coder: Julie Bajer
* Recent Date:    November 2008
*
*********************************************************************************
*
* Copyright 2008 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareSchedDeadlines (
      an_SchedDbId        IN sched_stask.sched_db_id%TYPE,
      an_SchedId          IN sched_stask.sched_id%TYPE,
      an_OrigTaskTaskDbId IN task_task.task_db_id%TYPE,
      an_OrigTaskTaskId   IN task_task.task_id%TYPE,
      abSyncWithBaseline  IN BOOLEAN,
      ad_PreviousCompletionDt IN evt_event.sched_end_gdt%TYPE,
      on_Return           OUT typn_RetCode
   ) IS

      -- retrieves deadlines
      CURSOR lcur_ActualsDeadlines  IS
      (  SELECT
            evt_sched_dead.data_type_db_id as data_type_db_id,
            evt_sched_dead.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            evt_sched_dead,
            mim_data_type
         WHERE
            evt_sched_dead.event_db_id = an_SchedDbId AND
            evt_sched_dead.event_id    = an_SchedId
            AND
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
      UNION
         SELECT
            task_sched_rule.data_type_db_id as data_type_db_id,
            task_sched_rule.data_type_id    as data_type_id,
            mim_data_type.domain_type_cd
         FROM
            sched_stask,
            task_sched_rule,
            mim_data_type
         WHERE
            sched_stask.sched_db_id = an_SchedDbId AND
            sched_stask.sched_id    = an_SchedId
            AND
            sched_stask.rstat_cd    = 0
            AND
            task_sched_rule.task_db_id = sched_stask.task_db_id AND
            task_sched_rule.task_id    = sched_stask.task_id
            AND
            mim_data_type.data_type_db_id = task_sched_rule.data_type_db_id AND
            mim_data_type.data_type_id    = task_sched_rule.data_type_id
      );

BEGIN
   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all the baseline, and actual deadlines */
   FOR lrec_ActualsDeadlines IN lcur_ActualsDeadlines
   LOOP

      /* Calendar Deadline */
      IF (lrec_ActualsDeadlines.domain_type_cd='CA') THEN

         -- prepare calendar deadline
         PrepareCalendarDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
            ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;

      /* Usage Deadline */
      ELSE
         PrepareUsageDeadline (
            an_SchedDbId,
            an_SchedId,
            an_OrigTaskTaskDbId,
            an_OrigTaskTaskId,
            lrec_ActualsDeadlines.data_type_db_id,
            lrec_ActualsDeadlines.data_type_id,
            abSyncWithBaseline,
             ad_PreviousCompletionDt,
            on_Return
         );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END IF;

   END LOOP;

   /* Update the Deadlines for this Task */
   EVENT_PKG.UpdateDeadline( an_SchedDbId, an_SchedId, on_Return );
   IF on_Return < 1 THEN
      RETURN;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@PrepareSchedDeadlines@@@: '||SQLERRM);
      RETURN;
END PrepareSchedDeadlines;

/********************************************************************************
*
* Procedure:    UpdateDependentDeadlines
* Arguments:    an_StartSchedDbId (long) - the task whose deadlines will be prepared
                an_StartSchedId   (long) - ""
* Return:       on_Return    (long) - succss/failure of procedure
*
* Description:  This procedure will update the deadlines
                of the tasks that have been forecasted to follow it.
*
* Orig.Coder:   A. Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
**********************************************s***********************************
*
* Copyright ? 2002 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateDependentDeadlines(
      an_StartSchedDbId  IN sched_stask.sched_db_id%TYPE,
      an_StartSchedId    IN sched_stask.sched_id%TYPE,
      on_Return          OUT typn_RetCode
   ) IS

   ln_TaskTaskDbId  task_task.task_db_id%TYPE;
   ln_TaskTaskId    task_task.task_id%TYPE;

   /* SQL to retrieve all of the forecasted tasks starting with this one */
   CURSOR lcur_DepTasks (
         cn_StartSchedDbId sched_stask.sched_db_id%TYPE,
         cn_StartSchedId   sched_stask.sched_id%TYPE
      ) IS
      SELECT
         LEVEL,
         rel_event_db_id sched_db_id,
         rel_event_id    sched_id
      FROM
         evt_event_rel
      START WITH
         event_db_id = cn_StartSchedDbId AND
         event_id    = cn_StartSchedId AND
         rel_type_cd = 'DEPT'
      CONNECT BY
         event_db_id = PRIOR rel_event_db_id AND
         event_id    = PRIOR rel_event_id AND
         rel_type_cd = 'DEPT'
      ORDER BY 1;

   /* EXCEPTIONS */
   xc_UnkCUSTOMnSQLError EXCEPTION;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

    SELECT
        sched_stask.task_db_id,
        sched_stask.task_id
    INTO
        ln_TaskTaskDbId,
        ln_TaskTaskId
    FROM
        sched_stask
    WHERE
         sched_stask.sched_db_id = an_StartSchedDbId AND
         sched_stask.sched_id    = an_StartSchedId
         AND
         sched_stask.rstat_cd   = 0;

   /* loop dependant tasks, and prepare their deadlines*/
   FOR lrec_DepTasks IN lcur_DepTasks( an_StartSchedDbId, an_StartSchedId )
   LOOP

      PrepareSchedDeadlines( lrec_DepTasks.sched_db_id,
                             lrec_DepTasks.sched_id,
                             ln_TaskTaskDbId,
                             ln_TaskTaskId,
                             false,
                             NULL,
                             on_Return );

      IF on_Return < 1 THEN
         RETURN;
      END IF;

   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      -- Unexpected error
     on_Return := icn_Error;
     APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','prep_deadline_pkg@@@UpdateDependentDeadlines@@@: '||SQLERRM);
     RETURN;
END UpdateDependentDeadlines;



/********************************************************************************
*
* Procedure:      PrepareDeadlineForInv
* Arguments:      al_InvNoDbId (IN NUMBER): The inventory to update
*                 al_InvNoId (IN NUMBER): ""
* Description:    Finds all the tasks with dedlines and runs
*                 prepare deadlines on them.
*
* Orig.Coder:     Michal Bajer
* Recent Coder:   cjb
* Recent Date:    February 27, 2005
*
*********************************************************************************
*
* Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrepareDeadlineForInv(
      an_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
      an_InvNoId      IN  inv_inv.inv_no_id%TYPE,
      as_SchedFrom    IN evt_sched_dead.sched_from_cd%TYPE,
      abSyncWithBaseline   IN BOOLEAN,
      on_Return        OUT NUMBER
   ) IS

  /* cursor declarations */
   /* return all non-historic events in the inventory tree that have
      scheduled deadlines */
   CURSOR lcur_DeadlineTask (cn_InvNoDbId    IN  inv_inv.inv_no_db_id%TYPE,
                     cn_InvNoId      IN  inv_inv.inv_no_id%TYPE)
   IS
      SELECT
             evt_event.event_db_id,
             evt_event.event_id
        FROM
             (
             SELECT     inv_inv.inv_no_db_id, inv_inv.inv_no_id
             FROM       inv_inv
             START WITH inv_inv.inv_no_db_id    = cn_InvNoDbId AND
                        inv_inv.inv_no_id       = cn_InvNoId
             CONNECT BY inv_inv.nh_inv_no_db_id = PRIOR inv_inv.inv_no_db_id AND
                        inv_inv.nh_inv_no_id    = PRIOR inv_inv.inv_no_id
             )inv_tree,
             evt_inv,
             evt_event
       WHERE
             evt_inv.inv_no_db_id = inv_tree.inv_no_db_id AND
             evt_inv.inv_no_id    = inv_tree.inv_no_id    AND
             evt_inv.main_inv_bool=1
             AND
             evt_event.event_db_id     = evt_inv.event_db_id AND
             evt_event.event_id        = evt_inv.event_id    AND
             evt_event.event_type_cd   = 'TS'                AND
             evt_event.hist_bool       = 0                   AND
             evt_event.event_status_cd = 'ACTV'              AND
             evt_event.rstat_cd   = 0
             AND EXISTS
             (  SELECT 1
                FROM   evt_sched_dead
                WHERE  evt_sched_dead.event_db_id   = evt_event.event_db_id AND
                       evt_sched_dead.event_id      = evt_event.event_id    AND
                       evt_sched_dead.sched_from_cd = as_SchedFrom);
   lrec_DeadlineTask lcur_DeadlineTask%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all of the tasks which need updating */
   FOR lrec_DeadlineTask IN lcur_DeadlineTask(an_InvNoDbId, an_InvNoId) LOOP

      PrepareSchedDeadlines( lrec_DeadlineTask.event_db_id,
                             lrec_DeadlineTask.event_id,
                             NULL,
                             NULL,
                             abSyncWithBaseline,
                             NULL,
                             on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;


      /* prepare deadlines forecasted tasks*/
      UpdateDependentDeadlines( lrec_DeadlineTask.event_db_id,
                                lrec_DeadlineTask.event_id,
                                on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -100;
      APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'prep_deadline_pkg@@@PrepareDeadlineForInv@@@' || SQLERRM);
      RETURN;
END PrepareDeadlineForInv;


FUNCTION GetScheduleDetails(
            an_TaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_TaskTaskId     IN task_task.task_id%TYPE,
            an_SchedDbId      IN sched_stask.sched_db_id%TYPE,
            an_SchedId        IN sched_stask.sched_id%TYPE
         ) RETURN typrec_ScheduleDetails
IS
   -- Local stask and task definition variables
   ln_PrevSchedDbId       sched_stask.sched_db_id%TYPE;
   ln_PrevSchedId         sched_stask.sched_id%TYPE;
   ln_ActiveTaskTaskDbId  task_interval.task_db_id%TYPE;
   ln_ActiveTaskTaskId    task_interval.task_id%TYPE;
   ln_HInvNoDbId          task_ac_rule.inv_no_db_id%TYPE;
   ln_HInvNoId            task_ac_rule.inv_no_id%TYPE;
   ln_PartNoDbId          task_interval.part_no_db_id%TYPE;
   ln_PartNoId            task_interval.part_no_id%TYPE;
   ln_RelativeBool        task_task.relative_bool%TYPE;
   ld_EffectiveDt         task_task.effective_dt%TYPE;
   lv_ReschedFromCd       task_task.resched_from_cd%TYPE;
   ln_SchedFromReceivedDtBool task_task.sched_from_received_dt_bool%TYPE;
   ln_RecurringBool       task_task.recurring_task_bool%TYPE;
   ls_TaskClassCd         task_task.task_class_cd%TYPE;
   ln_Return              typn_RetCode;

   orec_ScheduleDetails  typrec_ScheduleDetails;

BEGIN

   GetTaskDetails(
         an_SchedDbId,
         an_SchedId,
         ln_PrevSchedDbId,
         ln_PrevSchedId,
         ln_ActiveTaskTaskDbId,
         ln_ActiveTaskTaskId,
         ln_HInvNoDbId,
         ln_HInvNoId,
         ln_PartNoDbId,
         ln_PartNoId,
         ln_RelativeBool,
         ld_EffectiveDt,
         lv_ReschedFromCd,
         ln_RecurringBool,
         ln_SchedFromReceivedDtBool,
         ls_TaskClassCd,
         ln_Return );

   IF ln_Return < 0 THEN
      RETURN NULL;
   END IF;

   -- Populate task details
   orec_ScheduleDetails.STaskDbId         := an_SchedDbId;
   orec_ScheduleDetails.STaskId           := an_SchedId;
   orec_ScheduleDetails.FirstInstanceBool := ( ln_PrevSchedDbId = -1 );
   orec_ScheduleDetails.PreviousSTaskDbId := ln_PrevSchedDbId;
   orec_ScheduleDetails.PreviousSTaskID   := ln_PrevSchedId;
   orec_ScheduleDetails.HInvNoDbId        := ln_HInvNoDbId;
   orec_ScheduleDetails.HInvNoId          := ln_HInvNoId;
   orec_ScheduleDetails.PartNoDbId        := ln_PartNoDbId;
   orec_ScheduleDetails.PartNoId          := ln_PartNoId;

   -- Populate task definition details
   -- Because this function is related to forecasting deadlines, the revised task definition
   -- details are used instead of the stask's actual task definition details.
   orec_ScheduleDetails.RevisionTaskTaskDbId  := an_TaskTaskDbId;
   orec_ScheduleDetails.RevisionTaskTaskID    := an_TaskTaskId;
   orec_ScheduleDetails.ActiveTaskTaskDbId    := ln_ActiveTaskTaskDbId;
   orec_ScheduleDetails.ActiveTaskTaskId      := ln_ActiveTaskTaskId;

   SELECT
      task_task.relative_bool,
      task_task.effective_dt,
      task_task.resched_from_cd,
      istaskdefnrecurring(an_TaskTaskDbId, an_TaskTaskID ),
      task_task.sched_from_received_dt_bool,
      task_task.last_sched_dead_bool,
      task_task.task_class_cd
   INTO
      orec_ScheduleDetails.RelativeBool,
      orec_ScheduleDetails.EffectiveDt,
      orec_ScheduleDetails.ReschedFromCd,
      orec_ScheduleDetails.RecurringBool,
      orec_ScheduleDetails.SchedFromReceivedDtBool,
      orec_ScheduleDetails.ScheduleToLast,
      orec_ScheduleDetails.TaskClassCd
   FROM
      task_task
   WHERE
      task_task.task_db_id = an_TaskTaskDbId AND
      task_task.task_id    = an_TaskTaskId;


   RETURN orec_ScheduleDetails;

END GetScheduleDetails;



/********************************************************************************
*
* Function:     GetTaskRuleStartDate
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartDate(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

         ) RETURN evt_sched_dead.start_dt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   ls_SchedFromCd          evt_sched_dead.sched_from_cd%TYPE;
   ln_Return               typn_RetCode;
   -- Return value
   ld_StartDt              DATE;

BEGIN

   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF (
         lcur_CustomDeadline%FOUND AND
         lrec_CustomDeadline.sched_from_cd = 'CUSTOM' AND
         lrec_CustomDeadline.start_dt IS NOT NULL )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_dt;
      END IF;

      CLOSE lcur_CustomDeadline;
      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartDate (
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ReschedFromCd,
         arec_Schedule.SchedFromReceivedDtBool,
         ld_PreviousCompletionDt,
         arec_Schedule.TaskClassCd,
         ld_StartDt,                                 -- This sets the startDt
         ls_SchedFromCd,
         ln_Return
      );

   RETURN ld_StartDt;

END GetTaskRuleStartDate;



/********************************************************************************
*
* Function:     GetTaskRuleStartUsage
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               an_PrefixedQt            start of the completion date window
*               an_PostfixedQt           end of the completion date window
*
* Return:       Start usage for the task's scheduling rule with the given data type.
*
* Description:  Calculates the start usage for a task definition's scheduling rule/date type.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskRuleStartUsage(
            arec_Schedule            IN typrec_ScheduleDetails,
            an_DataTypeDbId          IN mim_data_type.data_type_db_id%TYPE,
            an_DataTypeId            IN mim_data_type.data_type_id   %TYPE

         ) RETURN evt_sched_dead.start_qt%TYPE
IS

   -- Find the driving CUSTOM deadline for an stask.
   CURSOR lcur_CustomDeadline IS
      SELECT
         evt_sched_dead.start_dt,
         evt_sched_dead.start_qt,
         evt_sched_dead.sched_from_cd
      FROM
         evt_sched_dead
         INNER JOIN ref_sched_from     ON ref_sched_from.sched_from_db_id = evt_sched_dead.sched_from_db_id AND
                                          ref_sched_from.sched_from_cd    = evt_sched_dead.sched_from_cd
      WHERE
         evt_sched_dead.event_db_id       = arec_Schedule.STaskDbId AND
         evt_sched_dead.event_id          = arec_Schedule.STaskId   AND
         evt_sched_dead.data_type_db_id   = an_DataTypeDbId         AND
         evt_sched_dead.data_type_id      = an_DataTypeId           AND
         evt_sched_dead.sched_driver_bool = 1
         AND
         ref_sched_from.sched_from_db_id  = 0        AND
         ref_sched_from.sched_from_cd     = 'CUSTOM';

   lrec_CustomDeadline  lcur_CustomDeadline%ROWTYPE;

   -- Not sure what this is for, yet, or how to obtain it.
   ld_PreviousCompletionDt evt_event.sched_end_gdt%TYPE := NULL;

   -- For the moment, assume the event inventory matches.
   ln_EventsOnSameInv      NUMBER := 1;

   ls_SchedFromCd          ref_sched_from.sched_from_cd%TYPE;
   ld_StartDt              evt_sched_dead.start_dt%TYPE;
   ln_StartQt              evt_sched_dead.start_qt%TYPE;
   ln_Return               typn_RetCode;


BEGIN
   -- If the stask is the first instance of the requirement and has a CUSTOM date, return the custom deadline's date.
   IF ( arec_Schedule.FirstInstanceBool ) THEN

      OPEN lcur_CustomDeadline;
      FETCH lcur_CustomDeadline INTO lrec_CustomDeadline;

      IF ( lcur_CustomDeadline%FOUND AND
           lrec_CustomDeadline.start_qt IS NOT NULL AND
           lrec_CustomDeadline.start_qt != 0 )
      THEN
         CLOSE lcur_CustomDeadline;

         RETURN lrec_CustomDeadline.start_qt;

      END IF;

      CLOSE lcur_CustomDeadline;

      -- No CUSTOM deadline found.  Continue with deadline calculations.
   END IF;

   FindDeadlineStartQt(
         arec_schedule.RevisionTaskTaskDbId,
         arec_schedule.RevisionTaskTaskID,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.PreviousSTaskDbId,
         arec_Schedule.PreviousSTaskId,
         arec_Schedule.RelativeBool,
         arec_Schedule.EffectiveDt,
         arec_Schedule.ReschedFromCd,
         arec_Schedule.SchedFromReceivedDtBool,
         ld_PreviousCompletionDt,
         ln_EventsOnSameInv,
         arec_Schedule.TaskClassCd,
         ln_StartQt,
         ls_SchedFromCd,
         ld_StartDt,
         ln_Return
      );

   RETURN ln_StartQt;

END GetTaskRuleStartUsage;



/********************************************************************************
*
* Function:      getCustomDeviation
* Arguments:
*                ar_ScheduleDetails   task definition and stask information.
*
*
* Description:   Obtains custom deadline information for a task definition revision, or scheduled task.
*                If a task_deadline_ext has been defined, its deviation is returned.
*                If the scheduled task has an extension, its deviation is returned.
*                Otherwise NULL is returned.
*
* Orig.Coder:    Alexander Nazarian
* Recent Coder:  Alexander Nazarian
* Recent Date:   July 2009
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getCustomDeviation(
      arec_ScheduleDetails    IN   typrec_ScheduleDetails,
      an_DataTypeDbId         IN   mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId           IN   mim_data_type.data_type_id   %TYPE
   )  RETURN task_deadline_ext.deviation_qt%TYPE

IS
   ln_Deviation                NUMBER;
   lb_RecFound                 BOOLEAN;

   --First check if the user has setup an extension using the Extend Deadline page from the View Impact Report page.
   --If there is an extension, use it.
   CURSOR lCur_TaskDeadlineExt
   (
      cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
      cn_SchedId      IN sched_stask.sched_id%TYPE,
      cn_DataTypeDbId IN mim_data_type.data_type_db_id%TYPE,
      cn_DataTypeId   IN mim_data_type.data_type_id%TYPE
   )
   IS
      SELECT
         task_deadline_ext.deviation_qt
      FROM
         task_deadline_ext
      WHERE
         task_deadline_ext.sched_db_id        = cn_SchedDbId AND
         task_deadline_ext.sched_id           = cn_SchedId   AND
         task_deadline_ext.data_type_db_id    = cn_DataTypeDbId AND
         task_deadline_ext.data_type_id       = cn_DataTypeId;

   --Cursor variable for the task deadline extension
   lRec_TaskDeadlineExt lCur_TaskDeadlineExt%ROWTYPE;

   -- TODO: Not sure where to get this from
   ln_EventsOnSameInv         NUMBER;

   ln_DeleteActualDeadline   NUMBER;
   ln_UpdateActualDeadline   NUMBER;
   ln_InsertActualDeadline   NUMBER;

   ls_sched_from_cd          evt_sched_dead.sched_from_cd%TYPE;
   ld_start_dt               evt_sched_dead.start_dt%TYPE;
   ln_start_qt               evt_sched_dead.start_qt%TYPE;

   -- OUT baseline values
   ln_ActiveIntervalQt       task_sched_rule.def_interval_qt%TYPE;
   ln_ActiveNotifyQt         task_sched_rule.def_notify_qt%TYPE;
   ln_ActiveInitialQt        task_sched_rule.def_deviation_qt%TYPE;
   ln_ActiveDeviationQt      task_sched_rule.def_deviation_qt%TYPE;
   ln_ActivePrefixedQt       task_sched_rule.def_prefixed_qt%TYPE;
   ln_ActivePostfixedQt      task_sched_rule.def_postfixed_qt%TYPE;
   ln_ActiveDeadlineExists   task_sched_rule.def_postfixed_qt%TYPE;

   ls_ActualSchedFromCd         evt_sched_dead.sched_from_cd%TYPE;
   ln_ActualIntervalQt          evt_sched_dead.Interval_Qt%TYPE;
   ln_ActualNotifyQt            evt_sched_dead.notify_qt%TYPE;
   ln_ActualDeviationQt         evt_sched_dead.deviation_qt%TYPE;
   ln_ActualPrefixedQt          evt_sched_dead.prefixed_qt%TYPE;
   ln_ActualPostfixedQt         evt_sched_dead.postfixed_qt%TYPE;
   ld_ActualStartDt             evt_sched_dead.start_dt%TYPE;
   ln_ActualStartQt             evt_sched_dead.start_qt%TYPE;
   ln_ActualDeadlineExists task_sched_rule.def_postfixed_qt%TYPE;

   ln_BaselineIntervalQt     task_sched_rule.def_interval_qt%TYPE;
   ln_BaselineNotifyQt       task_sched_rule.def_notify_qt%TYPE;
   ln_BaselineDeviationQt    task_sched_rule.def_deviation_qt%TYPE;
   ln_BaselinePrefixedQt     task_sched_rule.def_prefixed_qt%TYPE;
   ln_BaselinePostfixedQt    task_sched_rule.def_postfixed_qt%TYPE;

   ln_Return                 typn_RetCode;

BEGIN
   -- First, check for an extension set up by the user using the extend deadline page.
   OPEN lCur_TaskDeadlineExt
   (
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId
   ) ;

   FETCH lCur_TaskDeadlineExt INTO lRec_TaskDeadlineExt;

   lb_RecFound := lCur_TaskDeadlineExt%FOUND;
   CLOSE lCur_TaskDeadlineExt;

   IF ( lb_RecFound AND
        lrec_TaskDeadlineExt.deviation_qt IS NOT NULL AND
        lrec_taskDeadlineExt.deviation_qt > 0 )
   THEN
      ln_Deviation := lRec_TaskDeadlineExt.deviation_qt;
      RETURN ln_Deviation;
   END IF;

   /*
    * No impact report extension has been defined, so check for an extension on the actual stask.
    *
    * Note that this code repeats queries that will be performed by GetValuesAndActionForSynch in the following block of code.
    * This is because GetValuesAndActionForSynch does not return the stask's custom deviation if scheduling rules have not changed.
    * Because of this, we cannot rely on the deviation returned by it, and have to explicitly check the actual deadline first.
    * Ideally, GetValuesAndActionForSynch's behaviour will be updated when we can be sure it will not break baseline synch.
    */

   -- Get the deviation for the active task def.
   GetBaselineDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            arec_ScheduleDetails.PartNoDbId,
            arec_ScheduleDetails.PartNoId,
            arec_ScheduleDetails.HInvNoDbId,
            arec_ScheduleDetails.HInvNoId,
            ln_ActiveIntervalQt,
            ln_ActiveInitialQt,
            ln_ActiveNotifyQt,
            ln_ActiveDeviationQt,
            ln_ActivePrefixedQt,
            ln_ActivePostfixedQt,
            ln_ActiveDeadlineExists,
            ln_Return );

   -- If available, get the actual stask deviation for this data type.
   GetActualDeadline(
            an_DataTypeDbId,
            an_DataTypeId,
            arec_ScheduleDetails.STaskDbId,
            arec_ScheduleDetails.STaskId,
            arec_ScheduleDetails.ActiveTaskTaskDbId,
            arec_ScheduleDetails.ActiveTaskTaskId,
            TRUE,
            ls_ActualSchedFromCd,
            ln_ActualIntervalQt,
            ln_ActualNotifyQt,
            ln_ActualDeviationQt,
            ln_ActualPrefixedQt,
            ln_ActualPostfixedQt,
            ld_ActualStartDt,
            ln_ActualStartQt,
            ln_ActualDeadlineExists,
            ln_Return );

   -- If both exist, and the active baseline deviation != stask deviation then a custom deviation has been defined.
   IF ln_ActualDeadlineExists = 1 AND ln_ActiveDeadlineExists = 1 THEN
      IF ln_ActualDeviationQt <> ln_ActiveDeviationQt THEN
         -- Return the stask's custom deviation.
         RETURN ln_ActualDeviationQt;
      END IF;
   END IF;

   /*
    * No custom deviations exist from the impact report, or the actual stask.  Figure out the new baseline's deviation for this data type.
    */
   GetValuesAndActionForSynch(
      arec_ScheduleDetails.ActiveTaskTaskDbId,
      arec_ScheduleDetails.ActiveTaskTaskId,
      arec_ScheduleDetails.RevisionTaskTaskDbId,
      arec_ScheduleDetails.RevisionTaskTaskId,
      arec_ScheduleDetails.PreviousSTaskDbId,
      arec_ScheduleDetails.PreviousSTaskId,
      arec_ScheduleDetails.STaskDbId,
      arec_ScheduleDetails.STaskId,
      an_DataTypeDbId,
      an_DataTypeId,
      arec_ScheduleDetails.HInvNoDbId,
      arec_ScheduleDetails.HInvNoId,
      arec_ScheduleDetails.PartNoDbId,
      arec_ScheduleDetails.PartNoId,
      arec_ScheduleDetails.RecurringBool,
      ln_EventsOnSameInv,
      ln_DeleteActualDeadline,
      ln_UpdateActualDeadline,
      ln_InsertActualDeadline,
      ls_sched_from_cd,
      ld_start_dt,
      ln_start_qt,
      ln_BaselineIntervalQt,
      ln_BaselineNotifyQt,
      ln_BaselineDeviationQt,
      ln_BaselinePrefixedQt,
      ln_BaselinePostfixedQt,

      ln_Return
   );

   IF ( ln_InsertActualDeadline = 1 OR ln_UpdateActualDeadline = 1 ) THEN
      RETURN ln_BaselineDeviationQt;
   ELSE

      -- The revision has not updated scheduling rules, or the rule has been deleted.  Either way, no custom deviation exists.
      RETURN NULL;
   END IF;

END getCustomDeviation;



/********************************************************************************
*
* Function:     getTaskRuleDeadline
* Arguments:
*               arec_Schedule            Scheduling information for a task definition and stask.
*               an_DataTypeDbId          Primary key of the scheduling rule's data type
*               an_DataTypeId            -- // --
*               as_DomainTypeCd          Code of the data type's domain.
*               as_EngUnitCd             Code of the data type's engineering unit.
*               an_MultiplierQt          Data type's multiplier.
*
* Return:       Deadline information for the task definition's scheduling rule.
*
* Description:  Calculates deadline information for calendar and usage data types.
*
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION getTaskRuleDeadline(
      arec_Schedule    typrec_ScheduleDetails,
      an_DataTypeDbId  mim_data_type.data_type_db_id%TYPE,
      an_DataTypeId    mim_data_type.data_type_id   %TYPE,
      as_DomainTypeCd  mim_data_type.domain_type_cd %TYPE,
      as_EngUnitCd     mim_data_type.eng_unit_cd    %TYPE,
      an_MultiplierQt  ref_eng_unit.ref_mult_qt     %TYPE
   ) RETURN DeadlineRecord
IS

   ln_IntervalQt        task_sched_rule.def_interval_qt%TYPE;
   ln_InitialQt         task_sched_rule.def_initial_qt%TYPE;
   ln_NotifyQt          task_sched_rule.def_notify_qt%TYPE;
   ln_DeviationQt       task_sched_rule.def_deviation_qt%TYPE;
   ln_PrefixedQt        task_sched_rule.def_prefixed_qt%TYPE;
   ln_PostfixedQt       task_sched_rule.def_postfixed_qt%TYPE;
   ln_DeadlineExists    task_sched_rule.def_postfixed_qt%TYPE;
   ln_Return            typn_RetCode;

   ln_CustomDeviationQt task_sched_rule.def_deviation_qt%TYPE;

   ln_CurrentUsage      inv_curr_usage.tsn_qt%TYPE;
   ln_UsageRemaining    inv_curr_usage.tsn_qt%TYPE;
   ld_StartDt           evt_sched_dead.start_dt%TYPE;
   ln_StartQt           evt_sched_dead.start_qt%TYPE;
   ld_DeadlineDt        evt_sched_dead.start_dt%TYPE;

   lrec_Deadline        DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);

BEGIN
   lrec_Deadline.DeadlineDt := SYSDATE;

   -- Get the most relevant scheduling rule and interval information for the task definition revision + data type.

   GetBaselineDeadline(
         an_DataTypeDbId,
         an_DataTypeId,
         arec_Schedule.RevisionTaskTaskDbId,
         arec_Schedule.RevisionTaskTaskId,
         arec_Schedule.STaskDbId,
         arec_Schedule.STaskId,
         arec_Schedule.PartNoDbId,
         arec_Schedule.PartNoId,
         arec_Schedule.HInvNoDbId,
         arec_Schedule.HInvNoId,
         ln_IntervalQt,
         ln_InitialQt,
         ln_NotifyQt,
         ln_DeviationQt,
         ln_PrefixedQt,
         ln_PostfixedQt,
         ln_DeadlineExists,
         ln_Return
      );

   IF ( ln_Return < 0 ) THEN
      -- No deadline information found.  This should never happen.
      RETURN lrec_Deadline;
   END IF;

   ln_CustomDeviationQt := getCustomDeviation( arec_Schedule, an_DataTypeDbId, an_DataTypeId );

   IF ( ln_CustomDeviationQt IS NOT NULL AND ln_customDeviationQt > 0 ) THEN
      -- If a custom extension has been defined, override the scheduling rule deviation.
      ln_DeviationQt := ln_CustomDeviationQt;
   END IF;


   IF ( arec_Schedule.FirstInstanceBool AND ln_InitialQt IS NOT NULL ) THEN
      -- Use the rule's initial interval for its first stask instance.
      ln_IntervalQt := ln_InitialQt;
   END IF;

   -- Get the deadline start date or usage
   IF ( as_DomainTypeCd = 'CA' ) THEN -- Calendar

      ld_StartDt := GetTaskRuleStartDate(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId
         );

      -- Calculate the new deadline date.
      FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            ln_IntervalQt,
            ld_DeadlineDt,
            ln_Return
         );

      -- Assign default value for usage remaining - clients should ignore this for calendar deadlines.
      lrec_Deadline.UsageRemainingQt := 0;

   ELSE -- Usage

      -- Get the start usage for this scheduling rule.
      -- This obtains custom start usage defined for the task def revision, or an actual extension assigned to the stask.
      -- If no extensions are found, the usage is calculated using FindDeadlineStartQt.
      ln_StartQt := GetTaskRuleStartUsage(
            arec_Schedule,
            an_DataTypeDbId,
            an_DataTypeId
         );

      ld_StartDt := SYSDATE;

      -- Get the main inventory's current usage.
      GetCurrentInventoryUsage(
            arec_Schedule.STaskDbId,
            arec_Schedule.STaskId,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_CurrentUsage,
            ln_Return
         );

      ln_UsageRemaining := (ln_StartQt + ln_IntervalQt ) - ln_CurrentUsage;

      -- Calculate the usage remaining.
      EVENT_PKG.findForecastedDeadDt(
            arec_Schedule.HInvNoDbId,
            arec_Schedule.HInvNoId,
            an_DataTypeDbId,
            an_DataTypeId,
            ln_UsageRemaining + ln_DeviationQt,
            ld_StartDt,
            ld_DeadlineDt, -- assigned a value by this procedure call.
            ln_Return
         );
      IF ( ln_UsageRemaining IS NULL ) THEN
         ln_UsageRemaining := 0;
      END IF;


      lrec_Deadline.StartUsageQt     := ln_StartQt;
      lrec_Deadline.CurrentUsageQt   := ln_CurrentUsage;
      lrec_Deadline.UsageRemainingQt := ln_UsageRemaining;
   END IF;

   lrec_Deadline.DataTypeDbId := an_DataTypeDbId;
   lrec_Deadline.DataTypeId   := an_DataTypeId;
   lrec_Deadline.DomainTypeCd := as_DomainTypeCd;
   lrec_Deadline.EngUnitCd    := as_EngUnitCd;
   lrec_Deadline.DeviationQt  := ln_DeviationQt;
   lrec_Deadline.DeadlineDt   := ld_DeadlineDt;
   lrec_Deadline.MultiplierQt := an_MultiplierQt;
   lrec_Deadline.IntervalQt   := ln_IntervalQt;

   -- determine the extended deadline date
   lrec_Deadline.ExtendedDeadlineDt :=
      getExtendedDeadlineDt(
         lrec_Deadline.DeviationQt,
         lrec_Deadline.DeadlineDt,
         lrec_Deadline.DomainTypeCd,
         lrec_Deadline.MultiplierQt
      );

   RETURN lrec_Deadline;

END getTaskRuleDeadline;



/********************************************************************************
*
* Function:     getForecastedDrivingDueDate
* Arguments:
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // --
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // --
*               an_AircraftInvNoDbId      IN inv_inv.inv_no_db_id%TYPE,
*               an_AircraftInvNoId        IN inv_inv.inv_no_id%TYPE
*
* Return:       A string with * delimited deadline information.
*
* Description:  Obtains the driving deadline due date for a revised task definition.
*               All scheduling rules for the new revision have their deadline/extended deadline calculated.
*               The deadline returned will be the earliest, or latest depending on the
*               revised requirement's last_sched_dead_bool value.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetForecastedDrivingDueDate(
            an_STaskDbId              IN sched_stask.sched_db_id%TYPE,
            an_STaskId                IN sched_stask.sched_id%TYPE,
            an_RevisionTaskTaskDbId   IN task_task.task_db_id%TYPE,
            an_RevisionTaskTaskId     IN task_task.task_id%TYPE

   ) RETURN VARCHAR2
IS

   lrec_Schedule    typrec_ScheduleDetails;
   lrec_CurrentDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);
   lrec_DrivingDeadline    DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);


   -- Get the new baseline interval and deviation for the REV requirement
   CURSOR lcur_NewBaselines (
      crec_Schedule typrec_ScheduleDetails
   ) IS
      SELECT
         task_sched_rule.data_type_db_id,
         task_sched_rule.data_type_id
      FROM
         task_sched_rule
      WHERE
         task_sched_rule.task_db_id    = an_RevisionTaskTaskDbId AND
         task_sched_rule.task_id       = an_RevisionTaskTaskId;

   lrec_NewBaseline           lcur_NewBaselines%ROWTYPE;
   ln_Return                  typn_RetCode;

   ls_DomainTypeCd            mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt               ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd               mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd              mim_data_type.data_type_cd%TYPE;

BEGIN

   lrec_Schedule := GetScheduleDetails( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId, an_STaskDbId, an_STaskId );
   lrec_DrivingDeadline.DomainTypeCd := NULL;

   FOR lrec_NewBaseline IN lcur_NewBaselines( lrec_Schedule ) LOOP

      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return   );
      IF ln_Return < 0 THEN
        RETURN NULL;
      END IF;

      lrec_CurrentDeadline := getTaskRuleDeadline(
            lrec_Schedule,
            lrec_NewBaseline.data_type_db_id,
            lrec_NewBaseline.data_type_id,
            ls_DomainTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      IF ( lrec_DrivingDeadline.DomainTypeCd IS NULL OR
         ( lrec_Schedule.ScheduleToLast = 1 AND lrec_CurrentDeadline.ExtendedDeadlineDt > lrec_DrivingDeadline.ExtendedDeadlineDt ) OR
         ( lrec_Schedule.ScheduleToLast = 0 AND lrec_CurrentDeadline.ExtendedDeadlineDt < lrec_DrivingDeadline.ExtendedDeadlineDt )
         )
      THEN
         lrec_DrivingDeadline := lrec_CurrentDeadline;
      END IF;
   END LOOP;

   IF ( lrec_DrivingDeadline.DataTypeDbId = -1 ) THEN
      -- No deadlines found.  Return empty data.
      RETURN '******';
   END IF;

   RETURN lrec_DrivingDeadline.DomainTypeCd || '*' || lrec_DrivingDeadline.UsageRemainingQt || '*' || lrec_DrivingDeadline.EngUnitCd || '*' ||
          lrec_DrivingDeadline.MultiplierQt || '*' || TO_CHAR( lrec_DrivingDeadline.DeadlineDt, 'DD-MON-YYYY' ) || ' 23:59 *' || lrec_DrivingDeadline.DeviationQt || '*' ||
          lrec_DrivingDeadline.ExtendedDeadlineDt || ' 23:59';

END GetForecastedDrivingDueDate;



/********************************************************************************
*
* Function:     GetTaskDefnRules
* Arguments:
*               an_TaskTaskDbId    Task definition primary key
*               an_TaskTaskId      -- // --
*               an_STaskDbId       Active stask primary key
*               an_STaskId         -- // --
*
* Return:       A table of scheduling rule information for the task definition.
*               Note that *only* "basic" rule information is returned - i.e.
*               tail, part, and measurement specific information is not included.
*
* Description:  Obtains all basic scheduling rule information for a task definition.
*               Each data type has exactly one row of rule information returned.
*               Because this is a helper function, part, tail, and measurement
*               information is not returned.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDefnRules(
            an_TaskTaskDbId     IN task_task.task_db_id   %TYPE,
            an_TaskTaskId       IN task_task.task_id      %TYPE
         )
         RETURN typtabrec_SchedulingRuleTable
IS

   -- Cursor that gets basic scheduling rules for the task.
   CURSOR lcur_BaselineDeadlines(
            cn_TaskDbId    task_interval.task_db_id   %TYPE,
            cn_TaskId      task_interval.task_id      %TYPE
         )
      IS
         SELECT
            task_sched_rule.data_type_db_id    data_type_db_id,
            task_sched_rule.data_type_id       data_type_id,
            task_sched_rule.def_interval_qt    interval_qt,
            task_sched_rule.def_initial_qt     initial_qt,
            task_sched_rule.def_deviation_qt   deviation_qt,
            task_sched_rule.def_prefixed_qt    prefixed_qt,
            task_sched_rule.def_postfixed_qt   postfixed_qt
         FROM

            task_sched_rule
         WHERE
            task_sched_rule.task_db_id      = cn_TaskDbId     AND
            task_sched_rule.task_id         = cn_TaskId
      ;

   lrec_BaselineDeadline   lcur_BaselineDeadlines%ROWTYPE;

   ltab_Rules              typtabrec_SchedulingRuleTable;

   ln_Index                NUMBER := 0;

BEGIN
   FOR lrec_BaselineDeadline IN lcur_BaselineDeadlines(
         an_TaskTaskDbId,
         an_TaskTaskId
      )
   LOOP
      ltab_Rules( ln_Index ).DataTypeDbId := lrec_BaselineDeadline.data_type_db_id;
      ltab_Rules( ln_Index ).DataTypeId   := lrec_BaselineDeadline.data_type_id;
      ltab_Rules( ln_index ).IntervalQt  := lrec_BaselineDeadline.interval_qt;
      ltab_Rules( ln_index ).InitialQt   := lrec_BaselineDeadline.initial_qt;
      ltab_Rules( ln_index ).DeviationQt := lrec_BaselineDeadline.deviation_qt;
      ltab_Rules( ln_index ).PrefixedQt  := lrec_BaselineDeadline.prefixed_qt;
      ltab_Rules( ln_index ).PostfixedQt := lrec_BaselineDeadline.postfixed_qt;

      ln_Index := ln_Index + 1;
   END LOOP;

   RETURN ltab_Rules;

END GetTaskDefnRules;


/********************************************************************************
*
* Function:     isTaskDefnSchedulingChanged
* Arguments:
*               an_ActiveTaskTaskDbId    Active task definition primary key
*               an_ActiveTaskTaskId      -- // --
*               an_RevisionTaskTaskDbId  Revised task definition primary key
*               an_RevisionTaskTaskId    -- // --
*               an_STaskDbId             Active stask primary key
*               an_STaskId               -- // --
*
* Return:       0 if task definition information and scheduling rules are identical.
*               1 if the task definition has changed (recurring, schedule from, schedule to, etc.)
                1 if any scheduling rules have been added/removed/updated.
*
* Description:  Checks for differences between two revisions of a task definition.
*               Examines the basic task def information (recurring, schedule from, etc)
*               and checks all scheduling rules for differences.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isTaskDefnSchedulingChanged(
            an_ActiveTaskTaskDbId    task_task.task_db_id   %TYPE,
            an_ActiveTaskTaskId      task_task.task_id      %TYPE,
            an_RevisionTaskTaskDbId  task_task.task_db_id   %TYPE,
            an_RevisionTaskTaskId    task_task.task_id      %TYPE,
            an_STaskDbId             sched_stask.sched_db_id%TYPE,
            an_STaskId               sched_stask.sched_id   %TYPE
   ) RETURN NUMBER
IS

   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task defs.
   ltabrec_Rules         typtabrec_SchedulingRuleTable;
   ltabrec_RevisedRules  typtabrec_SchedulingRuleTable;

   -- Loop variable.
   lrec_Rule          typrec_SchedulingRule;

   ln_BaselineDeadlineChanged  INT;
   ln_Return                   INT;

BEGIN

   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_ActiveTaskTaskDbId, an_ActiveTaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules        := GetTaskDefnRules( an_ActiveTaskTaskDbId,   an_ActiveTaskTaskId );
   ltabrec_RevisedRules := GetTaskDefnRules( an_RevisionTaskTaskDbId, an_RevisionTaskTaskId );

   IF ( ltabrec_Rules.COUNT != ltabrec_RevisedRules.COUNT ) THEN
      -- There's a mismatch in rule counts.  Something must have changed.
      RETURN 1;
   END IF;

   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );

      BaselineDeadlinesChanged(
            an_RevisionTaskTaskDbId,
            an_RevisionTaskTaskId,
            an_ActiveTaskTaskDbId,
            an_ActiveTaskTaskId,
            lrec_Rule.DataTypeDbId,
            lrec_Rule.DataTypeId,
            lrec_Schedule.HInvNoDbId,
            lrec_Schedule.HInvNoId,
            lrec_Schedule.PartNoDbId,
            lrec_Schedule.PartNoId,
            ln_BaselineDeadlineChanged,
            ln_Return
         );

      IF ( ln_BaselineDeadlineChanged > 0 ) THEN
         -- A rule has changed.  We can stop checking.
         RETURN 1;
      END IF;
   END LOOP;

   -- No differences found.
   RETURN 0;

END isTaskDefnSchedulingChanged;


/********************************************************************************
*
* Function:     GetTaskDeadlines
* Arguments:
*               an_TaskTaskDbId   Task definition primary key
*               an_TaskTaskId     -- // --
*               an_STaskDbId      Stask primary key
*               an_STaskId        -- // --
*
* Return:       Deadline information for every scheduling rule associated with the task definition.
*
* Description:  Returns one row for each scheduling rule in a task.
*               Part, tail, and measurement information will overwrite basic rule values.
*
* Orig.Coder:   Gord Pearson
* Recent Coder:
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION GetTaskDeadlines(
            an_TaskTaskDbId   IN task_task.task_db_id   %TYPE,
            an_TaskTaskId     IN task_task.task_id      %TYPE,
            an_STaskDbId      IN sched_stask.sched_db_id%TYPE,
            an_STaskId        IN sched_stask.sched_id   %TYPE

   ) RETURN DeadlineTable PIPELINED

IS
   -- Task definition information.
   lrec_Schedule      typrec_ScheduleDetails;

   -- Scheduling rules for the task def.
   ltabrec_Rules      typtabrec_SchedulingRuleTable;

   -- Used in the loop to store a scheduling rule's data type information.
   ls_DomainTypeCd    mim_data_type.domain_type_cd%TYPE;
   ll_RefMultQt       ref_eng_unit.ref_mult_qt%TYPE;
   ls_EngUnitCd       mim_data_type.eng_unit_cd%TYPE;
   ls_DataTypeCd      mim_data_type.data_type_cd%TYPE;

   -- Used in the loop to temporarily store scheduling rule and deadline data.
   lrec_Rule          typrec_SchedulingRule;
   lrec_DeadlineInfo  DeadlineRecord := DeadlineRecord(-1, -1, '', '', -1, -1, -1, null, null, -1, -1, -1);

   ln_Return          typn_RetCode;

BEGIN
   -- Get basic requirement information
   lrec_Schedule := GetScheduleDetails( an_TaskTaskDbId, an_TaskTaskId, an_STaskDbId, an_STaskId );

   -- Get all scheduling rules for this requirement
   ltabrec_Rules := GetTaskDefnRules( an_TaskTaskDbId, an_TaskTaskId );

   -- Loop over the scheduling rules, and return a row of deadline information for each of them.
   FOR i IN 0..ltabrec_Rules.COUNT-1 LOOP
      lrec_Rule         := ltabrec_Rules( i );

      -- Populate usage parameter details.
      GetUsageParmInfo(
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId,
            ls_DomainTypeCd,
            ll_RefMultQt,
            ls_EngUnitCd,
            ls_DataTypeCd,
            ln_Return
         );

      -- Get the deadline information for the current data type.
      lrec_DeadlineInfo := getTaskRuleDeadline(
            lrec_Schedule,
            lrec_Rule.DataTypeDbId, lrec_Rule.DataTypeId,
            ls_DomainTypeCd,
            ls_EngUnitCd,
            ll_RefMultQt
         );

      -- Output the deadline information.
      PIPE ROW( lrec_DeadlineInfo );
   END LOOP;

END GetTaskDeadlines;


END PREP_DEADLINE_PKG;
/

--changeSet MX-27658:86 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FLIGHT_LEG_STATUS"
** 0-Level
** DATE: 2010-09-30
*********************************************/
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXPLAN', 0, 80, 'Planned', 'Flight has been planned', 'PLAN', 10, '0:FLPLAN',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXPLAN' );

--changeSet MX-27658:87 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXOUT', 0, 80, 'Left Gate', 'Flight time starts', 'OUT', 20, '0:FLOUT',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXOUT' );

--changeSet MX-27658:88 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXOFF', 0, 80, 'Airborne', 'Air time starts', 'OFF', 30, '0:FLOFF',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXOFF' );

--changeSet MX-27658:89 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXON', 0, 80, 'On Ground', 'Air time stops', 'ON', 40, '0:FLON',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXON' );

--changeSet MX-27658:90 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXIN', 0, 80, 'At Gate', 'Flight time stops', 'IN', 50, '0:IN',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXIN' );

--changeSet MX-27658:91 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXCMPLT', 0, 83, 'Complete', 'Flight complete', 'COMPLETE', 60, '0:FLCMPLT',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXCMPLT' );

--changeSet MX-27658:92 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXDELAY', 0, 83, 'Delay', 'Flight delay', 'DELAY', 65, '0:FLDELAY',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXDELAY' );

--changeSet MX-27658:93 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXCANCEL', 0, 83, 'Cancelled', 'Flight cancellation', 'CANCEL', 75, '0:FLCANCEL',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXCANCEL' );

--changeSet MX-27658:94 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXERR', 0, 83, 'Error', 'Error was found in record', 'ERROR', 95, '0:FLERR',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXERR' );

--changeSet MX-27658:95 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXEDIT', 0, 83, 'Edit', 'Flight was edited', 'EDIT', 95, '0:FLEDIT',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXEDIT' );

--changeSet MX-27658:96 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXDIVERT', 0, 83, 'Divert', 'Flight has diverted to a station other than the scheduled arrival station', 'DIVERT', 35, '0:DIVERT',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXDIVERT' );

--changeSet MX-27658:97 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXRETURN', 0, 83, 'Return', 'Aircraft has returned to gate before taking-off', 'RETURN', 25, '0:RETURN',  0, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXRETURN' );

--changeSet MX-27658:98 stripComments:false
INSERT INTO
   ref_flight_leg_status
   (
      ctrl_db_id, flight_leg_status_cd, bitmap_db_id, bitmap_tag, display_name, display_desc, display_code, display_ord, legacy_key, rstat_cd, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user, revision_no
   )
   SELECT 0, 'MXBLKOUT', 0, 1, 'N/A', 'N/A', 'BLKOUT', 1, '0:FLBLKOUT', 3, sysdate, 0, sysdate, 0, 'MXI', 1
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM ref_flight_leg_status WHERE ref_flight_leg_status.flight_leg_status_cd = 'MXBLKOUT' );

--changeSet MX-27658:99 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_USAGE_TYPE"
** 0-Level
** DATE: 2010-09-30
*********************************************/
INSERT INTO
    ref_usage_type
    (
    usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id,    bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
    )
    SELECT 'MXACCRUAL', 'ACCRUAL', 'Usage Accrual', 'Usage Accrual', 100, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI'
    FROM
    dual
    WHERE
    NOT EXISTS (SELECT 1 FROM ref_usage_type WHERE ref_usage_type.usage_type_cd = 'MXACCRUAL');

--changeSet MX-27658:100 stripComments:false
INSERT INTO
    ref_usage_type
    (
    usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id,    bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
    )
    SELECT 'MXADJUSTMENT', 'ADJUSTMENT', 'Usage Adjustment', 'Usage Adjustment', 200, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI'
    FROM
    dual
    WHERE
    NOT EXISTS (SELECT 1 FROM ref_usage_type WHERE ref_usage_type.usage_type_cd = 'MXADJUSTMENT');

--changeSet MX-27658:101 stripComments:false
INSERT INTO
    ref_usage_type
    (
    usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id,    bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
    )
    SELECT 'MXCORRECTION', 'CORRECTION', 'Usage Correction', 'Usage Correction', 300, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI'
    FROM
    dual
    WHERE
    NOT EXISTS (SELECT 1 FROM ref_usage_type WHERE ref_usage_type.usage_type_cd = 'MXCORRECTION');

--changeSet MX-27658:102 stripComments:false
INSERT INTO
    ref_usage_type
    (
    usage_type_cd, display_code, display_name, display_desc, display_ord, bitmap_db_id,    bitmap_tag, ctrl_db_id, revision_no, creation_db_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
    )
    SELECT 'MXFLIGHT', 'FLIGHT', 'Flight', 'Flight', 400, 0, 1, 0, 1, 0, 0, sysdate, sysdate, 0, 'MXI'
    FROM
    dual
    WHERE
    NOT EXISTS (SELECT 1 FROM ref_usage_type WHERE ref_usage_type.usage_type_cd = 'MXFLIGHT');

--changeSet MX-27658:103 stripComments:false
-- - migration script for ref_usage_type
INSERT INTO ref_usage_type
   (
      usage_type_cd,
      display_code,
      display_name,
      display_desc,
      display_ord,
      bitmap_db_id,
      bitmap_tag,
      ctrl_db_id,
      revision_no,
      creation_db_id
   )
SELECT
   'MXACCRUAL',
   'ACCRUAL',
   'Usage Accrual',
   'Usage Accrual',
   100,
   0,
   1,
   0,
   1,
   0
FROM
   dual
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         ref_usage_type
      WHERE
         ref_usage_type.usage_type_cd = 'MXACCRUAL'
   )
;

--changeSet MX-27658:104 stripComments:false
INSERT INTO ref_usage_type
   (
      usage_type_cd,
      display_code,
      display_name,
      display_desc,
      display_ord,
      bitmap_db_id,
      bitmap_tag,
      ctrl_db_id,
      revision_no,
      creation_db_id
   )
SELECT
   'MXADJUSTMENT',
   'ADJUSTMENT',
   'Usage Adjustment',
   'Usage Adjustment',
   200,
   0,
   1,
   0,
   1,
   0
FROM
   dual
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         ref_usage_type
      WHERE
         ref_usage_type.usage_type_cd = 'MXADJUSTMENT'
   )
;

--changeSet MX-27658:105 stripComments:false
INSERT INTO ref_usage_type
   (
      usage_type_cd,
      display_code,
      display_name,
      display_desc,
      display_ord,
      bitmap_db_id,
      bitmap_tag,
      ctrl_db_id,
      revision_no,
      creation_db_id
   )
SELECT
   'MXCORRECTION',
   'CORRECTION',
   'Usage Correction',
   'Usage Correction',
   300,
   0,
   1,
   0,
   1,
   0
FROM
   dual
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         ref_usage_type
      WHERE
         ref_usage_type.usage_type_cd = 'MXCORRECTION'
   )
;

--changeSet MX-27658:106 stripComments:false
INSERT INTO ref_usage_type
   (
      usage_type_cd,
      display_code,
      display_name,
      display_desc,
      display_ord,
      bitmap_db_id,
      bitmap_tag,
      ctrl_db_id,
      revision_no,
      creation_db_id
   )
SELECT
   'MXFLIGHT',
   'FLIGHT',
   'Flight',
   'Flight',
   400,
   0,
   1,
   0,
   1,
   0
FROM
   dual
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         ref_usage_type
      WHERE
         ref_usage_type.usage_type_cd = 'MXFLIGHT'
   )
;

--changeSet MX-27658:107 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'REF_USAGE_TYPE');
END;
/

--changeSet MX-27658:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: INSERT INTO USG_USAGE_RECORD: USAGE CORRECTIONS
-- Add temporary columns to USG_USAGE_RECORD
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE USG_USAGE_RECORD ADD EVENT_DB_ID NUMBER(10)
');
END;
/

--changeSet MX-27658:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
  ALTER TABLE USG_USAGE_RECORD ADD EVENT_ID NUMBER (10)
');
END;
/

--changeSet MX-27658:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   -- Enable parallel DML on usg_usage_record
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('USG_USAGE_RECORD');

   -- Migrate data for USG_USAGE_RECORD: usage corrections
   INSERT /*+ append parallel */ INTO usg_usage_record
      (
         usage_record_id,
         usage_type_cd,
         usage_name,
         usage_desc,
         document_ref,
         ext_key,
         inv_no_db_id,
         inv_no_id,
         usage_dt,
         negated_bool,
         record_dt,
         record_hr_db_id,
         record_hr_id,
         applied_bool,
         legacy_key,
         ctrl_db_id,
         event_db_id,
         event_id,
         recorded_dt,
         rstat_cd,
         revision_no,
         creation_db_id,
         creation_dt,
         revision_db_id,
         revision_dt,
         revision_user
      )
   SELECT
      mx_key_pkg.new_uuid(),
      'MXCORRECTION',
      evt_event.event_sdesc,
      evt_event.event_ldesc,
      evt_event.doc_ref_sdesc,
      evt_event.ext_key_sdesc,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS usage_dt,
      NVL(
      (
         SELECT 1
         FROM dual
         WHERE EXISTS
         (
           SELECT 1
            FROM evt_inv_usage
           WHERE
            evt_inv_usage.negated_bool = 1 AND
            evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
            evt_inv_usage.event_id     = evt_inv.event_id AND
            evt_inv_usage.event_inv_id = evt_inv.event_inv_id
         )
      ),
      0
      ) AS negated_bool,
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt,
      NVL(evt_stage.hr_db_id, NVL(evt_event.editor_hr_db_id, 0))  AS hr_db_id,
      NVL(evt_stage.hr_id, NVL(evt_event.editor_hr_id, 3)) AS hr_id,
      1,
      evt_event.event_db_id || ':' || evt_event.event_id AS legacy_key,
      evt_event.ctrl_db_id,
      evt_event.event_db_id,
      evt_event.event_id,
      evt_event.creation_dt,
      evt_event.rstat_cd,
      1,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getuser
   FROM
      evt_event
      INNER JOIN evt_inv ON evt_inv.event_db_id   = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1
      LEFT OUTER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
                    evt_event.event_id        = evt_stage.event_id
                   AND
                    evt_stage.event_status_db_id = 0 AND
                   evt_stage.event_status_cd    = 'UCPEND'
   WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'UC'
      AND
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd    = 'UCCOMPLETE';
   
   -- MX27658: INSERT INTO USG_USAGE_RECORD: USAGE ACCRUALS
   -- Migrate data for USG_USAGE_RECORD: usage accruals
   INSERT /*+ append parallel */ INTO usg_usage_record
      (
         usage_record_id,
         usage_type_cd,
         usage_name,
         usage_desc,
         document_ref,
         ext_key,
         inv_no_db_id,
         inv_no_id,
         usage_dt,
         negated_bool,
         record_dt,
         record_hr_db_id,
         record_hr_id,
         applied_bool,
         legacy_key,
         ctrl_db_id,
         event_db_id,
         event_id,
         recorded_dt,
         rstat_cd,
         revision_no,
         creation_db_id,
         creation_dt,
         revision_db_id,
         revision_dt,
         revision_user
      )
   SELECT
      mx_key_pkg.new_uuid(),
      'MXACCRUAL',
      evt_event.event_sdesc,
      evt_event.event_ldesc,
      evt_event.doc_ref_sdesc,
      evt_event.ext_key_sdesc,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS usage_dt,
      NVL(
      (
         SELECT 1
         FROM dual
         WHERE EXISTS
         (
           SELECT 1
            FROM evt_inv_usage
           WHERE
            evt_inv_usage.negated_bool = 1 AND
            evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
            evt_inv_usage.event_id     = evt_inv.event_id AND
            evt_inv_usage.event_inv_id = evt_inv.event_inv_id
         )
      ),
      0
      ) AS negated_bool,
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt,
      NVL(evt_stage.hr_db_id, NVL(evt_event.editor_hr_db_id, 0)) AS hr_db_id,
      NVL(evt_stage.hr_id, NVL(evt_event.editor_hr_id, 3)) AS hr_id ,
      1,
      evt_event.event_db_id || ':' || evt_event.event_id AS legacy_key,
      evt_event.ctrl_db_id,
      evt_event.event_db_id,
      evt_event.event_id,
      evt_event.creation_dt,
      evt_event.rstat_cd,
      1,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getuser
   FROM
      evt_event
      INNER JOIN evt_inv ON evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1
      LEFT OUTER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
                    evt_event.event_id        = evt_stage.event_id
                   AND
                    evt_stage.event_status_db_id = 0 AND
                   evt_stage.event_status_cd = 'URPEND'
   WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'UR'
      AND
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd = 'URCOMPLETE'
   ;
   
   -- MX27658: INSERT INTO USG_USAGE_RECORD: FLIGHT USAGE
   -- Migrate data for USG_USAGE_RECORD: flight usage
   INSERT /*+ append parallel */ INTO usg_usage_record
      (
         usage_record_id,
         usage_type_cd,
         usage_name,
         usage_desc,
         document_ref,
         ext_key,
         inv_no_db_id,
         inv_no_id,
         usage_dt,
         negated_bool,
         record_dt,
         record_hr_db_id,
         record_hr_id,
         applied_bool,
         legacy_key,
         ctrl_db_id,
         event_db_id,
         event_id,
         recorded_dt,
         rstat_cd,
         revision_no,
         creation_db_id,
         creation_dt,
         revision_db_id,
         revision_dt,
         revision_user
      )
   SELECT
      mx_key_pkg.new_uuid(),
      'MXFLIGHT',
      evt_event.event_sdesc,
      evt_event.event_ldesc,
      evt_event.doc_ref_sdesc,
      evt_event.ext_key_sdesc,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      evt_event.event_dt AS usage_dt,
      0 AS negated_bool,
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt,
      NVL(evt_stage.hr_db_id, NVL(evt_event.editor_hr_db_id, 0)) AS hr_db_id,
      NVL(evt_stage.hr_id, NVL(evt_event.editor_hr_id, 3)) AS hr_id ,
      CASE WHEN evt_event.event_status_cd = 'FLCMPLT'
         THEN 1
         ELSE 0
      END as applied_bool,
      evt_event.event_db_id || ':' || evt_event.event_id AS legacy_key,
      evt_event.ctrl_db_id,
      evt_event.event_db_id,
      evt_event.event_id,
      evt_event.creation_dt,
      evt_event.rstat_cd,
      1,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getuser
   FROM
      evt_event
      INNER JOIN evt_inv ON evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1
      INNER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
               evt_event.event_id        = evt_stage.event_id
               AND
               evt_event.event_status_db_id = evt_stage.event_status_db_id AND
               evt_event.event_status_cd    = evt_stage.event_status_cd
   WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'FL'
      AND
      (evt_stage.event_db_id, evt_stage.event_id, evt_stage.stage_dt, evt_stage.stage_id) IN
      (
      SELECT
         max_evt_stage.event_db_id, max_evt_stage.event_id, MAX(max_evt_stage.stage_dt) AS stage_dt, MAX(max_evt_stage.stage_id) AS stage_id
      FROM
         evt_stage max_evt_stage
      WHERE
         max_evt_stage.event_status_db_id = 0 AND
         max_evt_stage.event_status_cd    = 'FLCMPLT'
      GROUP BY
         max_evt_stage.event_db_id,
         max_evt_stage.event_id
      )
   ;
   
   -- Disable parallel DML on usg_usage_record
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('USG_USAGE_RECORD', TRUE);

END;
/

--changeSet MX-27658:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints on USG_USAGE_RECORD
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_RECORD" add Constraint "FK_INVINV_USGUSREC" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_RECORD" add Constraint "FK_MIMDB_USGUSREC" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_RECORD" add Constraint "FK_ORGHR_USGUSREC" foreign key ("RECORD_HR_DB_ID","RECORD_HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_RECORD" add Constraint "FK_MIMRSTAT_USGUSREC" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_RECORD" add Constraint "FK_REFUSTYPE_USGUSREC" foreign key ("USAGE_TYPE_CD") references "REF_USAGE_TYPE" ("USAGE_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes on USG_USAGE_RECORD
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVT_USGUSREC" ON "USG_USAGE_RECORD" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-27658:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSAGERECORD" ON "USG_USAGE_RECORD" ("INV_NO_ID","INV_NO_DB_ID","USAGE_DT")
');
END;
/

--changeSet MX-27658:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSREC_REFUSGTYPE_FK" ON "USG_USAGE_RECORD" ("USAGE_TYPE_CD")
');
END;
/

--changeSet MX-27658:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSREC_INVINV_FK" ON "USG_USAGE_RECORD" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-27658:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSREC_ORGHR_FK" ON "USG_USAGE_RECORD" ("RECORD_HR_DB_ID","RECORD_HR_ID")
');
END;
/

--changeSet MX-27658:121 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSREC_MIMDB" ON "USG_USAGE_RECORD" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers on USG_USAGE_RECORD
CREATE OR REPLACE TRIGGER "TIBR_USG_USAGE_RECORD" BEFORE INSERT
   ON "USG_USAGE_RECORD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_USG_USAGE_RECORD" BEFORE UPDATE
   ON "USG_USAGE_RECORD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USG_USAGE_RECORDED_DT" BEFORE INSERT
   ON "USG_USAGE_RECORD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet MX-27658:125 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'USG_USAGE_RECORD');
END;
/

--changeSet MX-27658:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: INSERT INTO USG_USAGE_DATA
BEGIN

   -- Enable parallel DML on usg_usage_data
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('USG_USAGE_DATA');

   -- Migrate data for USG_USAGE_DATA
   INSERT /*+ append parallel */ INTO usg_usage_data
      (
         usage_data_id,
         usage_record_id,
         inv_no_db_id,
         inv_no_id,
         data_type_db_id,
         data_type_id,
         data_ord,
         assmbl_inv_no_db_id,
         assmbl_inv_no_id,
         assmbl_db_id,
         assmbl_cd,
         assmbl_bom_id,
         assmbl_pos_id,
         tsn_qt,
         tso_qt,
         tsi_qt,
         tsn_delta_qt,
         tso_delta_qt,
         tsi_delta_qt,
         negated_bool,
         legacy_key,
         ctrl_db_id,
         rstat_cd,
         revision_no,
         creation_db_id,
         creation_dt,
         revision_db_id,
         revision_dt,
         revision_user
      )
   SELECT
      mx_key_pkg.new_uuid(),
      usg_usage_record.usage_record_id,
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      inv_num_data.data_type_db_id,
      inv_num_data.data_type_id,
      NVL( inv_num_data.data_ord, 1 ) AS data_ord,
      evt_inv.assmbl_inv_no_db_id,
      evt_inv.assmbl_inv_no_id,
      evt_inv.assmbl_db_id,
      evt_inv.assmbl_cd,
      evt_inv.assmbl_bom_id,
      evt_inv.assmbl_pos_id,
      evt_inv_usage.tsn_qt,
      evt_inv_usage.tso_qt,
      evt_inv_usage.tsi_qt,
      inv_num_data.tsn_delta_qt,
      inv_num_data.tso_delta_qt,
      inv_num_data.tsi_delta_qt,
      evt_inv_usage.negated_bool,
      evt_inv_usage.event_db_id || ':' || evt_inv_usage.event_id || ':' || evt_inv_usage.event_inv_id || ':' || evt_inv_usage.data_type_db_id || ':' || evt_inv_usage.data_type_id AS legacy_key,
      evt_event.ctrl_db_id,
      inv_num_data.rstat_cd,
      1,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getdbid,
      application_object_pkg.gettimestamp,
      application_object_pkg.getuser
   FROM
      usg_usage_record
      INNER JOIN evt_event ON
          evt_event.event_db_id = usg_usage_record.event_db_id AND
          evt_event.event_id = usg_usage_record.event_id
      INNER JOIN evt_inv ON evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id    = evt_event.event_id
      INNER JOIN evt_inv_usage ON evt_inv_usage.event_db_id = evt_inv.event_db_id AND
                   evt_inv_usage.event_id    = evt_inv.event_id AND
                   evt_inv_usage.event_inv_id = evt_inv.event_inv_id
      INNER JOIN inv_num_data ON inv_num_data.event_db_id    = evt_inv_usage.event_db_id AND
                  inv_num_data.event_id       = evt_inv_usage.event_id AND
                  inv_num_data.event_inv_id   = evt_inv_usage.event_inv_id AND
                  inv_num_data.data_type_db_id = evt_inv_usage.data_type_db_id AND
                  inv_num_data.data_type_id    = evt_inv_usage.data_type_id;

   -- Disable parallel DML on usg_usage_data
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('USG_USAGE_DATA', TRUE);

END;
/

--changeSet MX-27658:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints on USG_USAGE_DATA
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_ASSMBLINVINV_USGUSDATA" foreign key ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_INVINV_USGUSDATA" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_MIMDB_USGUSDATA" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_MIMRSTAT_USGUSDATA" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_MIMDATTYPE_USGUSDATA" foreign key ("DATA_TYPE_DB_ID","DATA_TYPE_ID") references "MIM_DATA_TYPE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_EQPASSMBLPOS_USGUSAGEDATA" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "USG_USAGE_DATA" add Constraint "FK_USGUSREC_USGUSDATA" foreign key ("USAGE_RECORD_ID") references "USG_USAGE_RECORD" ("USAGE_RECORD_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes on USG_USAGE_DATA
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_USGUSAGEDATA_NK" ON "USG_USAGE_DATA" ("USAGE_RECORD_ID","INV_NO_ID","INV_NO_DB_ID","DATA_TYPE_ID","DATA_TYPE_DB_ID")
');
END;
/

--changeSet MX-27658:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSDATA_USGREC_FK" ON "USG_USAGE_DATA" ("USAGE_RECORD_ID")
');
END;
/

--changeSet MX-27658:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSDATA_INVINV_FK" ON "USG_USAGE_DATA" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-27658:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSDATA_MIMDATATYPE_FK" ON "USG_USAGE_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-27658:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSDATA_MIMDB_FK" ON "USG_USAGE_DATA" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "USGUSDATA_ASSMBLINVINV_FK" ON "USG_USAGE_DATA" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-27658:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_USGUSAGEDATA" ON "USG_USAGE_DATA" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MX-27658:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers on USG_USAGE_DATA
CREATE OR REPLACE TRIGGER "TIBR_USG_USAGE_DATA" BEFORE INSERT
   ON "USG_USAGE_DATA" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_USG_USAGE_DATA" BEFORE UPDATE
   ON "USG_USAGE_DATA" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:143 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'USG_USAGE_DATA');
END;
/

--changeSet MX-27658:144 stripComments:false
-- MX27658: REF_FLIGHT_REASON
-- --  migration script for ref_flight_reason -- -- -- -- -- -- -
INSERT INTO ref_flight_reason
    (
        flight_reason_cd,
        display_code,
        display_name,
        display_desc,
        display_ord,
        bitmap_db_id,
        bitmap_tag,
        legacy_key,
        ctrl_db_id,
        revision_no,
        creation_db_id
    )
SELECT
    ref_event_reason.event_reason_cd,
    ref_event_reason.user_reason_cd,
    ref_event_reason.desc_sdesc,
    NVL(ref_event_reason.desc_ldesc, ref_event_reason.desc_sdesc ),
    100,
    ref_event_reason.bitmap_db_id,
    ref_event_reason.bitmap_tag,
    ref_event_reason.event_reason_db_id || ':' || ref_event_reason.event_reason_cd,
    ref_event_reason.event_reason_db_id,
    1,
    ref_event_reason.event_reason_db_id
FROM
    ref_event_reason
WHERE
    ref_event_reason.event_type_db_id = 0 AND
   ref_event_reason.event_type_cd    = 'FL';


--changeSet MX-27658:145 stripComments:false
UPDATE
   ref_flight_reason
SET
   ref_flight_reason.flight_reason_cd = REPLACE(ref_flight_reason.flight_reason_cd,  'FL', 'MX' )
WHERE
   ref_flight_reason.creation_db_id = 0 AND
   ref_flight_reason.rstat_cd = 0
;

--changeSet MX-27658:146 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'REF_FLIGHT_REASON');
END;
/

--changeSet MX-27658:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg_disrupt ADD flight_disrupt_db_id NUMBER(10)
');
END;
/

--changeSet MX-27658:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg_disrupt ADD flight_disrupt_id NUMBER(10)
');
END;
/

--changeSet MX-27658:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: INSERT INTO FL_LEG
-- Create temporary columns for FL_LEG
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg ADD event_db_id NUMBER(10)
');
END;
/

--changeSet MX-27658:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg ADD event_id NUMBER (10)
');
END;
/

--changeSet MX-27658:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   -- Enable parallel DML on fl_leg
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('FL_LEG');

   -- Migrate data into FL_LEG
   INSERT /*+ append parallel */ INTO fl_leg
       (
           leg_id,
           leg_no,
           leg_desc,
           master_flight_no,
           ext_key,
           hist_bool,
           flight_leg_status_cd,
           flight_reason_cd,
           flight_type_db_id,
           flight_type_cd,
           etops_bool,
           aircraft_db_id,
           aircraft_id,
           inv_capability_db_id,
           inv_capability_cd,
           logbook_ref,
           usage_record_id,
           departure_loc_db_id,
           departure_loc_id,
           departure_gate_cd,
           sched_departure_dt,
           actual_departure_dt,
           off_dt,
           arrival_loc_db_id,
           arrival_loc_id,
           arrival_gate_cd,
           sched_arrival_dt,
           actual_arrival_dt,
           on_dt,
           plan_assmbl_db_id,
           plan_assmbl_cd,
           legacy_key,
           ctrl_db_id,
           event_db_id,
           event_id,
           recorded_dt,
           rstat_cd,
           revision_no,
           creation_db_id,
           creation_dt,
           revision_db_id,
           revision_dt,
           revision_user
       )
   SELECT
       mx_key_pkg.new_uuid(),
       evt_event.event_sdesc AS leg_no,
       evt_event.event_ldesc AS leg_sdec,
       jl_flight.master_flight_sdesc AS master_flight_no,
       evt_event.ext_key_sdesc AS ext_key,
       evt_event.hist_bool AS hist_bool,
       CASE
          WHEN
             evt_event.event_status_db_id = 0
          THEN
             (
                CASE
                   WHEN
                      substr( evt_event.event_status_cd, 0, 2) = 'FL'
                   THEN
                      'MX' || substr( evt_event.event_status_cd, 3)
                   ELSE
                      'MX' || evt_event.event_status_cd
                END
             )
          ELSE
             evt_event.event_status_cd
       END AS flight_leg_status_cd,
       evt_event.event_reason_cd AS flight_reason_cd,
       jl_flight.flight_type_db_id AS flight_type_db_id,
       jl_flight.flight_type_cd AS flight_type_cd,
       jl_flight.etops_bool AS etops_bool,
       evt_inv.inv_no_db_id AS aircraft_db_id,
       evt_inv.inv_no_id AS aircraft_id,
       jl_flight.inv_capability_db_id AS inv_capability_db_id,
       jl_flight.inv_capability_cd AS inv_capability_cd,
       evt_event.doc_ref_sdesc AS logbook_ref,
       usg_usage_record.usage_record_id AS usage_record_id,
       departure_event_loc.loc_db_id AS departure_loc_db_id,
       departure_event_loc.loc_id AS departure_loc_id,
       jl_flight.departure_gate_cd AS departure_gate_cd,
       evt_event.sched_start_dt AS sched_departure_dt,
      NVL(
          evt_event.actual_start_dt,
          evt_event.sched_start_dt
       ) AS actual_departure_dt,
       jl_flight.up_dt AS off_dt,
       arrival_event_loc.loc_db_id as arrival_loc_db_id,
       arrival_event_loc.loc_id AS arrival_loc_id,
       jl_flight.arrival_gate_cd AS arrival_gate_cd,
       evt_event.sched_end_dt AS sched_arrival_dt,
       NVL(
          evt_event.event_dt,
          evt_event.sched_end_dt
       ) AS actual_arrival_dt,
       jl_flight.down_dt AS on_dt,
       jl_flight.plan_assmbl_db_id,
       jl_flight.plan_assmbl_cd,
       evt_event.event_db_id || ':' || evt_event.event_id AS legacy_key,
       evt_event.ctrl_db_id,
       evt_event.event_db_id,
       evt_event.event_id,
       evt_event.creation_dt,
       evt_event.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       jl_flight
       INNER JOIN evt_event ON
          evt_event.event_db_id = jl_flight.flight_db_id AND
          evt_event.event_id    = jl_flight.flight_id
       LEFT OUTER JOIN evt_inv ON
          evt_inv.event_db_id   = evt_event.event_db_id AND
          evt_inv.event_id      = evt_event.event_id AND
          evt_inv.main_inv_bool = 1
       LEFT OUTER JOIN evt_loc departure_event_loc ON
          departure_event_loc.event_db_id  = evt_event.event_db_id AND
          departure_event_loc.event_id     = evt_event.event_id AND
          departure_event_loc.event_loc_id = 1
       LEFT OUTER JOIN evt_loc arrival_event_loc ON
          arrival_event_loc.event_db_id  = evt_event.event_db_id AND
          arrival_event_loc.event_id     = evt_event.event_id AND
          arrival_event_loc.event_loc_id = 2
       LEFT OUTER JOIN usg_usage_record ON
          usg_usage_record.event_db_id = evt_event.event_db_id AND
          usg_usage_record.event_id    = evt_event.event_id;

   -- Disable parallel DML on fl_leg
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('FL_LEG', TRUE);

END;
/

--changeSet MX-27658:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints for FL_LEG
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_EQPASSMBL_FLLEG" foreign key ("PLAN_ASSMBL_DB_ID","PLAN_ASSMBL_CD") references "EQP_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_INVACREG_FLLEG" foreign key ("AIRCRAFT_DB_ID","AIRCRAFT_ID") references "INV_AC_REG" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_DEPINVLOC_FLLEG" foreign key ("DEPARTURE_LOC_DB_ID","DEPARTURE_LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_ARRINVLOC_FLLEG" foreign key ("ARRIVAL_LOC_DB_ID","ARRIVAL_LOC_ID") references "INV_LOC" ("LOC_DB_ID","LOC_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_MIMDB_FLLEG" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_REFFLTYPE_FLLEG" foreign key ("FLIGHT_TYPE_DB_ID","FLIGHT_TYPE_CD") references "REF_FLIGHT_TYPE" ("FLIGHT_TYPE_DB_ID","FLIGHT_TYPE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_REFINVCAP_FLLEG" foreign key ("INV_CAPABILITY_DB_ID","INV_CAPABILITY_CD") references "REF_INV_CAPABILITY" ("INV_CAPABILITY_DB_ID","INV_CAPABILITY_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_MIMRSTAT_FLLEG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_USGUSREC_FLLEG" foreign key ("USAGE_RECORD_ID") references "USG_USAGE_RECORD" ("USAGE_RECORD_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_REFFLREASON_FLLEG" foreign key ("FLIGHT_REASON_CD") references "REF_FLIGHT_REASON" ("FLIGHT_REASON_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG" add Constraint "FK_REFFLSTATUS_FLEG" foreign key ("FLIGHT_LEG_STATUS_CD") references "REF_FLIGHT_LEG_STATUS" ("FLIGHT_LEG_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes for FL_LEG
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG" ON "FL_LEG" ("LEG_NO","AIRCRAFT_ID","AIRCRAFT_DB_ID","DEPARTURE_LOC_ID","DEPARTURE_LOC_DB_ID","ACTUAL_DEPARTURE_DT")
');
END;
/

--changeSet MX-27658:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_REFFLSTATUS_FK" ON "FL_LEG" ("FLIGHT_LEG_STATUS_CD")
');
END;
/

--changeSet MX-27658:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_FLLEGREASON_FK" ON "FL_LEG" ("FLIGHT_REASON_CD")
');
END;
/

--changeSet MX-27658:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_REFFLTYPE_FK" ON "FL_LEG" ("FLIGHT_TYPE_DB_ID","FLIGHT_TYPE_CD")
');
END;
/

--changeSet MX-27658:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_INVACREG_FK" ON "FL_LEG" ("AIRCRAFT_DB_ID","AIRCRAFT_ID")
');
END;
/

--changeSet MX-27658:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_REFINVCAP_FK" ON "FL_LEG" ("INV_CAPABILITY_DB_ID","INV_CAPABILITY_CD")
');
END;
/

--changeSet MX-27658:169 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_USGUSAGEREC_FK" ON "FL_LEG" ("USAGE_RECORD_ID")
');
END;
/

--changeSet MX-27658:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_INVLOC_DEP_FK" ON "FL_LEG" ("DEPARTURE_LOC_DB_ID","DEPARTURE_LOC_ID")
');
END;
/

--changeSet MX-27658:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_INVLOC_ARR_FK" ON "FL_LEG" ("ARRIVAL_LOC_DB_ID","ARRIVAL_LOC_ID")
');
END;
/

--changeSet MX-27658:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_MIMDB_FK" ON "FL_LEG" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_EQP_ASSMBL" ON "FL_LEG" ("PLAN_ASSMBL_DB_ID","PLAN_ASSMBL_CD")
');
END;
/

--changeSet MX-27658:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEG_EVTEVENT_FK" ON "FL_LEG" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-27658:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FL_LEG_LEGACY_KEY" ON "FL_LEG" (LEGACY_KEY)
');
END;
/

--changeSet MX-27658:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers for FL_LEG
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG" BEFORE INSERT
   ON "FL_LEG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG" BEFORE UPDATE
   ON "FL_LEG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_RECORDED_DT" BEFORE INSERT
   ON "FL_LEG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet MX-27658:179 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG');
END;
/

--changeSet MX-27658:180 stripComments:false
-- MX27658: INSERT FL_LEG_DISRUPT
-- -- -- -- -migration script for fl_leg_disrupt-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
INSERT INTO fl_leg_disrupt
    (
        leg_disrupt_id,
        leg_id,
        ext_ref,
        flight_stage_db_id,
        flight_stage_cd,
        sched_db_id,
        sched_id,
        delay_code_db_id,
        delay_code_cd,
        maint_delay_time_qt,
        tech_delay_bool,
        disruption_desc,
        disruption_note,
        legacy_key,
        ctrl_db_id,
        flight_disrupt_db_id,
        flight_disrupt_id
    )
SELECT
    mx_key_pkg.new_uuid(),
    fl_leg.leg_id,
    jl_flight_disrupt.ext_ref_sdesc,
    jl_flight_disrupt.flight_stage_db_id,
    jl_flight_disrupt.flight_stage_cd,
    jl_flight_disrupt.sched_db_id,
    jl_flight_disrupt.sched_id,
    jl_flight_disrupt.delay_code_db_id,
    jl_flight_disrupt.delay_code_cd,
    jl_flight_disrupt.maint_delay_time_qt,
    jl_flight_disrupt.tech_delay_bool,
    jl_flight_disrupt.disrupt_sdesc,
    jl_flight_disrupt.disrupt_notes,
    jl_flight_disrupt.flight_disrupt_db_id || ':' || jl_flight_disrupt.flight_disrupt_id AS legacy_key,
    fl_leg.ctrl_db_id,
    jl_flight_disrupt.flight_disrupt_db_id,
    jl_flight_disrupt.flight_disrupt_id
FROM
    fl_leg
    INNER JOIN jl_flight_disruption_map ON
       jl_flight_disruption_map.flight_db_id = fl_leg.event_db_id AND
       jl_flight_disruption_map.flight_id    = fl_leg.event_id
    INNER JOIN jl_flight_disrupt ON
      jl_flight_disrupt.flight_disrupt_db_id = jl_flight_disruption_map.flight_disrupt_db_id AND
       jl_flight_disrupt.flight_disrupt_id    = jl_flight_disruption_map.flight_disrupt_id;


--changeSet MX-27658:181 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_DISRUPT');
END;
/

--changeSet MX-27658:182 stripComments:false
-- MX27658: INSERT FL_LEG_DISRUPT_TYPE
-- -- -- -- -- -- -- -migration script for fl_leg_disrupt_type -- -- -- -- -- -- -- -- -
INSERT INTO fl_leg_disrupt_type
    (
        leg_disrupt_type_id,
        leg_disrupt_id,
        disrupt_type_db_id,
        disrupt_type_cd,
        legacy_key,
        ctrl_db_id
    )
SELECT
    mx_key_pkg.new_uuid(),
    fl_leg_disrupt.leg_disrupt_id,
    jl_flight_disrupt_type.disrupt_type_db_id,
    jl_flight_disrupt_type.disrupt_type_cd,
    jl_flight_disrupt_type.flight_disrupt_db_id || ':' || jl_flight_disrupt_type.flight_disrupt_id || ':' ||  jl_flight_disrupt_type.   disrupt_type_db_id || ':' || jl_flight_disrupt_type.disrupt_type_cd AS legacy_key,
    fl_leg_disrupt.ctrl_db_id
FROM
    jl_flight_disrupt_type
    INNER JOIN fl_leg_disrupt ON
       fl_leg_disrupt.flight_disrupt_db_id = jl_flight_disrupt_type.flight_disrupt_db_id AND
       fl_leg_disrupt.flight_disrupt_id    = jl_flight_disrupt_type.flight_disrupt_id;


--changeSet MX-27658:183 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_DISRUPT_TYPE');
END;
/

--changeSet MX-27658:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: INSERT INTO FL_LEG_MEASUREMENT
BEGIN

   -- Enable parallel DML on fl_leg_measurement
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('FL_LEG_MEASUREMENT');

   -- Migrate data for FL_LEG_MEASUREMENT
   INSERT /*+ parallel */ INTO fl_leg_measurement
       (
           leg_measurement_id,
           leg_id,
           inv_no_db_id,
           inv_no_id,
           data_type_db_id,
           data_type_id,
           data_ord,
           data_value_db_id,
           data_value_cd,
           data_qt,
           data_dt,
           data_text,
           data_bool,
           legacy_key,
           ctrl_db_id,
           rstat_cd,
           revision_no,
           creation_db_id,
           creation_dt,
           revision_db_id,
           revision_dt,
           revision_user
       )
   SELECT
       mx_key_pkg.new_uuid(),
       fl_leg.leg_id,
       inv_parm_data.inv_no_db_id,
       inv_parm_data.inv_no_id,
       inv_parm_data.data_type_db_id,
       inv_parm_data.data_type_id,
       NVL(inv_parm_data.data_ord, 1),
       inv_parm_data.data_value_db_id,
       inv_parm_data.data_value_cd,
       inv_parm_data.parm_qt,
       inv_parm_data.parm_dt,
       inv_parm_data.parm_text,
       ( CASE
            WHEN inv_parm_data.parm_qt IS NULL
            THEN 0 ELSE 1
         END
        ) AS data_bool,
       inv_parm_data.event_db_id || ':' || inv_parm_data.event_id || ':' || inv_parm_data.event_inv_id || ':' ||
       inv_parm_data.data_type_db_id || ':' || inv_parm_data.data_type_id || ':' || inv_parm_data.inv_no_db_id || ':' || inv_parm_data.inv_no_id AS    legacy_key,
       fl_leg.ctrl_db_id,
       inv_parm_data.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       fl_leg
       INNER JOIN inv_parm_data ON
          inv_parm_data.event_db_id = fl_leg.event_db_id AND
          inv_parm_data.event_id    = fl_leg.event_id;

   -- Disable parallel DML on fl_leg_measurement
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('FL_LEG_MEASUREMENT', TRUE);

END;
/

--changeSet MX-27658:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints for FL_LEG_MEASUREMENT
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_INVINV_FLLEGMEAS" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_MIMDB_FLLEGMEAS" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:187 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_MIMRSTAT_FLLEGMEAS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_MIMDTTYP_FLLEGMEAS" foreign key ("DATA_TYPE_DB_ID","DATA_TYPE_ID") references "MIM_DATA_TYPE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_REFDATVAL_FLLEGMEAS" foreign key ("DATA_VALUE_DB_ID","DATA_VALUE_CD") references "REF_DATA_VALUE" ("DATA_VALUE_DB_ID","DATA_VALUE_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_MEASUREMENT" add Constraint "FK_FLLEG_FLLEGMEAS" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes for FL_LEG_MEASUREMENT
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_FLLEGMEASUREMENT_NK" ON "FL_LEG_MEASUREMENT" ("LEG_ID","INV_NO_ID","INV_NO_DB_ID","DATA_TYPE_ID","DATA_TYPE_DB_ID")
');
END;
/

--changeSet MX-27658:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGMEAS_FLLEG" ON "FL_LEG_MEASUREMENT" ("LEG_ID")
');
END;
/

--changeSet MX-27658:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGMEAS_INVINV_FK" ON "FL_LEG_MEASUREMENT" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-27658:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGMEAS_MIMDATTYP_FK" ON "FL_LEG_MEASUREMENT" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-27658:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGMEAS_REFDATVAL_FK" ON "FL_LEG_MEASUREMENT" ("DATA_VALUE_DB_ID","DATA_VALUE_CD")
');
END;
/

--changeSet MX-27658:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGMEAS_MIMDB_FK" ON "FL_LEG_MEASUREMENT" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers for FL_LEG_MEASUREMENT
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_MEASUREMENT" BEFORE INSERT
   ON "FL_LEG_MEASUREMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_MEASUREMENT" BEFORE UPDATE
   ON "FL_LEG_MEASUREMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:199 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_MEASUREMENT');
END;
/

--changeSet MX-27658:200 stripComments:false
-- MX27658: INSERT FL_LEG_FAIL_EFFECT
-- -- -- -- -- -- -- migration script for fl_leg_fail_effect-- -- -- -- -- -- -- -- -
INSERT INTO fl_leg_fail_effect
    (
        leg_fail_effect_id,
        leg_id,
        fail_effect_type_db_id,
        fail_effect_type_cd,
        fail_effect_db_id,
        fail_effect_id,
        effect_dt,
        effect_desc,
        flight_stage_db_id,
        flight_stage_cd,
        legacy_key,
        ctrl_db_id
    )
SELECT
    mx_key_pkg.new_uuid(),
    fl_leg.leg_id,
    evt_fail_effect.fail_effect_type_db_id,
    evt_fail_effect.fail_effect_type_cd,
    evt_fail_effect.fail_effect_db_id,
    evt_fail_effect.fail_effect_id,
    evt_fail_effect.effect_dt,
    evt_fail_effect.info_sdesc,
    NVL(evt_fail_effect.flight_stage_db_id, 10),
    NVL(evt_fail_effect.flight_stage_cd, 'NR'),
    evt_fail_effect.event_db_id || ':' || evt_fail_effect.event_id || ':' || evt_fail_effect.event_effect_id AS legacy_key,
    fl_leg.ctrl_db_id
FROM
    evt_fail_effect
    INNER JOIN fl_leg ON
       fl_leg.event_db_id = evt_fail_effect.event_db_id AND
       fl_leg.event_id    = evt_fail_effect.event_id;


--changeSet MX-27658:201 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_FAIL_EFFECT');
END;
/

--changeSet MX-27658:202 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: INSERT INTO FL_LEG_NOTE
-- Create temporary columns on FL_LEG_NOTE
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg_note ADD event_db_id NUMBER(10)
');
END;
/

--changeSet MX-27658:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg_note ADD event_id NUMBER(10)
');
END;
/

--changeSet MX-27658:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE fl_leg_note ADD stage_id NUMBER(10)
');
END;
/

--changeSet MX-27658:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   -- Enable parallel DML on fl_leg_note
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('FL_LEG_NOTE');

   -- Migrate data for FL_LEG_NOTE
   INSERT /*+ append parallel */ INTO fl_leg_note
       (
           flight_note_id,
           leg_id,
           entry_dt,
           entry_ord,
           system_bool,
           hr_db_id,
           hr_id,
           entry_note,
           legacy_key,
           ctrl_db_id,
           event_db_id,
           event_id,
           stage_id,
           rstat_cd,
           revision_no,
           creation_db_id,
           creation_dt,
           revision_db_id,
           revision_dt,
           revision_user
       )
   SELECT
       mx_key_pkg.new_uuid(),
       fl_leg.leg_id,
       evt_stage.stage_dt,
       evt_stage.stage_id,
       0,
       NVL( evt_stage.hr_db_id, 0 ),
       NVL( evt_stage.hr_id, 3 ),
       evt_stage.user_stage_note,
       evt_stage.event_db_id || ':' || evt_stage.event_id || ':' || evt_stage.stage_id AS legacy_key,
       fl_leg.ctrl_db_id,
       evt_stage.event_db_id,
       evt_stage.event_id,
       evt_stage.stage_id,
       evt_stage.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       evt_stage
       INNER JOIN fl_leg ON
          fl_leg.event_db_id = evt_stage.event_db_id AND
          fl_leg.event_id    = evt_stage.event_id
   WHERE
       evt_stage.user_stage_note IS NOT NULL
   UNION ALL
   SELECT
       mx_key_pkg.new_uuid(),
       fl_leg.leg_id,
       evt_stage.stage_dt,
       evt_stage.stage_id,
       1,
       NVL( evt_stage.hr_db_id, 0 ),
       NVL( evt_stage.hr_id, 3 ),
       evt_stage.system_stage_note AS stage_note,
       evt_stage.event_db_id || ':' || evt_stage.event_id || ':' || evt_stage.stage_id AS legacy_key,
       fl_leg.ctrl_db_id,
       evt_stage.event_db_id,
       evt_stage.event_id,
       evt_stage.stage_id,
       evt_stage.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       evt_stage
       INNER JOIN fl_leg ON
          fl_leg.event_db_id = evt_stage.event_db_id AND
          fl_leg.event_id    = evt_stage.event_id
   WHERE
       evt_stage.system_stage_note IS NOT NULL;

   -- Disable parallel DML on fl_leg_note
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('FL_LEG_NOTE', TRUE);

END;
/

--changeSet MX-27658:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints on FL_LEG_NOTE
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_NOTE" add Constraint "FK_MIMDB_FLLEGNOTE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_NOTE" add Constraint "FK_ORGHR_FLLEGNOTE" foreign key ("HR_DB_ID","HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_NOTE" add Constraint "FK_MIMRSTAT_FLLEGNOTE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_NOTE" add Constraint "FK_FLLEG_FLLEGNOTE" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes on FL_LEG_NOTE
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGNOTE_EVTEVT_FK" ON "FL_LEG_NOTE" ("EVENT_DB_ID","EVENT_ID","STAGE_ID")
');
END;
/

--changeSet MX-27658:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_FLLEGNOTE_NK" ON "FL_LEG_NOTE" ("LEG_ID","ENTRY_DT","ENTRY_ORD","SYSTEM_BOOL")
');
END;
/

--changeSet MX-27658:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGNOTE_FLLEG_FK" ON "FL_LEG_NOTE" ("LEG_ID")
');
END;
/

--changeSet MX-27658:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGNOTE_ORGHR_FK" ON "FL_LEG_NOTE" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-27658:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGNOTE_MIMDB_FK" ON "FL_LEG_NOTE" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers on FL_LEG_NOTE
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_NOTE" BEFORE INSERT
   ON "FL_LEG_NOTE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_NOTE" BEFORE UPDATE
   ON "FL_LEG_NOTE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:217 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_NOTE');
END;
/

--changeSet MX-27658:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   -- Enable parallel DML on fl_leg_status_log
   UTL_PARALLEL_PKG.PARALLEL_INSERT_BEGIN('FL_LEG_STATUS_LOG');

   -- MX27658: INSERT INTO FL_LEG_STATUS_LOG: SYSTEM NOTES
   -- Migrate data for FL_LEG_STATUS_LOG: System notes
   INSERT /*+ append parallel */ INTO fl_leg_status_log
       (
           leg_status_log_id,
           leg_id,
           log_dt,
           flight_leg_status_cd,
           system_bool,
           hr_db_id,
           hr_id,
           user_note_id,
           system_note_id,
           legacy_key,
           ctrl_db_id,
           rstat_cd,
           revision_no,
           creation_db_id,
           creation_dt,
           revision_db_id,
           revision_dt,
           revision_user
       )
   SELECT
       mx_key_pkg.new_uuid(),
       fl_leg_note.leg_id,
       evt_stage.stage_dt,
       CASE
           WHEN evt_stage.event_status_db_id IS NULL
            THEN 'MXEDIT'
          WHEN evt_stage.event_status_db_id = 0
              THEN (
                  CASE WHEN substr( evt_stage.event_status_cd, 0, 2) = 'FL'
                  THEN 'MX' || substr( evt_stage.event_status_cd, 3)
                  ELSE 'MX' || evt_stage.event_status_cd
                  END
                  )
            ELSE evt_stage.event_status_cd
       END AS flight_leg_status_cd,
       evt_stage.system_bool,
       NVL( evt_stage.hr_db_id, 0 ),
       NVL( evt_stage.hr_id, 3 ),
       NULL,
       fl_leg_note.flight_note_id,
       (evt_stage.event_db_id || ':' || evt_stage.event_id || ':' || evt_stage.stage_id ) AS legacy_key,
       fl_leg_note.ctrl_db_id,
       fl_leg_note.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       evt_stage
       INNER JOIN fl_leg_note ON
          fl_leg_note.event_db_id = evt_stage.event_db_id AND
          fl_leg_note.event_id    = evt_stage.event_id AND
          fl_leg_note.stage_id    = evt_stage.stage_id
   WHERE
       evt_stage.system_bool = 1;
   
   -- MX27658: INSERT INTO FL_LEG_STATUS_LOG: USER NOTES
   -- Migrate data for FL_LEG_STATUS_LOG: User notes
   INSERT /*+ append parallel */ INTO fl_leg_status_log
       (
           leg_status_log_id,
           leg_id,
           log_dt,
           flight_leg_status_cd,
           system_bool,
           hr_db_id,
           hr_id,
           user_note_id,
           system_note_id,
           legacy_key,
           ctrl_db_id,
           rstat_cd,
           revision_no,
           creation_db_id,
           creation_dt,
           revision_db_id,
           revision_dt,
           revision_user
       )
   SELECT
       mx_key_pkg.new_uuid(),
       fl_leg_note.leg_id,
       evt_stage.stage_dt,
       CASE
           WHEN evt_stage.event_status_db_id IS NULL
            THEN 'MXEDIT'
          WHEN evt_stage.event_status_db_id = 0
            THEN (
                  CASE WHEN substr( evt_stage.event_status_cd, 0, 2) = 'FL'
                  THEN 'MX' || substr( evt_stage.event_status_cd, 3)
                  ELSE 'MX' || evt_stage.event_status_cd
                  END
                  )
            ELSE evt_stage.event_status_cd
       END AS flight_leg_status_cd,
       evt_stage.system_bool,
       NVL( evt_stage.hr_db_id, 0 ),
       NVL( evt_stage.hr_id, 3 ),
       fl_leg_note.flight_note_id,
       NULL,
       (evt_stage.event_db_id || ':' || evt_stage.event_id || ':' || evt_stage.stage_id ) AS legacy_key,
       fl_leg_note.ctrl_db_id,
       fl_leg_note.rstat_cd,
       1,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getdbid,
       application_object_pkg.gettimestamp,
       application_object_pkg.getuser
   FROM
       evt_stage
       INNER JOIN fl_leg_note ON
          fl_leg_note.event_db_id = evt_stage.event_db_id AND
          fl_leg_note.event_id    = evt_stage.event_id AND
          fl_leg_note.stage_id    = evt_stage.stage_id
   WHERE
       evt_stage.system_bool = 0
   ;

   -- Disable parallel DML on fl_leg_status_log
   UTL_PARALLEL_PKG.PARALLEL_INSERT_END('FL_LEG_STATUS_LOG', TRUE);

END;
/

--changeSet MX-27658:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create constraints for FL_LEG_STATUS_LOG
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_MIMDB_FLLEGSTATUSLOG" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_ORGHR_FLLEGSTATLOG" foreign key ("HR_DB_ID","HR_ID") references "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_MIMRSTAT_FLLEGSTATLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:222 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_FLLEG_FLLEGSTATLOG" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:223 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_USRFLLEGNOTE_FLLEGSTALOG" foreign key ("USER_NOTE_ID") references "FL_LEG_NOTE" ("FLIGHT_NOTE_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_SYSFLLEGNOTE_FLLEGSTALOG" foreign key ("SYSTEM_NOTE_ID") references "FL_LEG_NOTE" ("FLIGHT_NOTE_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "FL_LEG_STATUS_LOG" add Constraint "FK_REFFLLEGSTAT_FLLEGSTALOG" foreign key ("FLIGHT_LEG_STATUS_CD") references "REF_FLIGHT_LEG_STATUS" ("FLIGHT_LEG_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet MX-27658:226 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create indexes for FL_LEG_STATUS_LOG
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGSTATUSLOG_NK" ON "FL_LEG_STATUS_LOG" ("LEG_ID","LOG_DT" Desc,"FLIGHT_LEG_STATUS_CD","SYSTEM_BOOL")
');
END;
/

--changeSet MX-27658:227 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTATUSLOG_FLLEG_FK" ON "FL_LEG_STATUS_LOG" ("LEG_ID")
');
END;
/

--changeSet MX-27658:228 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTAT_REFFLLEGSTAT_FK" ON "FL_LEG_STATUS_LOG" ("FLIGHT_LEG_STATUS_CD")
');
END;
/

--changeSet MX-27658:229 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTATUS_ORGHR_FK" ON "FL_LEG_STATUS_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-27658:230 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTAT_FLFLNOTEUSER_FK" ON "FL_LEG_STATUS_LOG" ("USER_NOTE_ID")
');
END;
/

--changeSet MX-27658:231 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTAT_FLFLNOTESYS_FK" ON "FL_LEG_STATUS_LOG" ("SYSTEM_NOTE_ID")
');
END;
/

--changeSet MX-27658:232 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FLLEGSTAT_MIMDB_FK" ON "FL_LEG_STATUS_LOG" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-27658:233 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create triggers for FL_LEG_STATUS_LOG
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_STATUS_LOG" BEFORE INSERT
   ON "FL_LEG_STATUS_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet MX-27658:234 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_FL_LEG_STATUS_LOG" BEFORE UPDATE
   ON "FL_LEG_STATUS_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet MX-27658:235 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'FL_LEG_STATUS_LOG');
END;
/

--changeSet MX-27658:236 stripComments:false
-- MX27658: MERGE SD_FAULT.LEG_ID
-- -- -- migration script to populate sd_fault.leg_id column -- -- -- -- -- -- -- -- -- 
MERGE INTO
  sd_fault
USING
  (
    SELECT
      sd_fault.fault_db_id,
      sd_fault.fault_id,
      fl_leg.leg_id
    FROM
      sd_fault
    INNER JOIN evt_event_rel ON
      sd_fault.fault_db_id = evt_event_rel.rel_event_db_id AND
      sd_fault.fault_id    = evt_event_rel.rel_event_id
    INNER JOIN fl_leg ON
      fl_leg.event_db_id = evt_event_rel.event_db_id AND
      fl_leg.event_id    = evt_event_rel.event_id
    WHERE
      evt_event_rel.rel_type_db_id = 0 AND
      evt_event_rel.rel_type_cd    = 'ENCF'
  ) encf
ON
  (
    sd_fault.fault_db_id = encf.fault_db_id AND
    sd_fault.fault_id    = encf.fault_id
  )
WHEN MATCHED THEN
  UPDATE SET
    sd_fault.leg_id = encf.leg_id;


--changeSet MX-27658:237 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SD_FAULT" add Constraint "FK_FLLEG_SDFAULT" foreign key ("LEG_ID") references "FL_LEG" ("LEG_ID")  DEFERRABLE
');
END;
/

--changeSet MX-27658:238 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'SD_FAULT');
END;
/

--changeSet MX-27658:239 stripComments:false
-- MX27658: UPDATE EVT_FAIL_EFFECT.LEG_ID
-- -- -- migration script to populate evt_fail_effect.leg_id column -- -- -- -- -- -- -- -- -- 
UPDATE
  evt_fail_effect
SET
  evt_fail_effect.leg_id = (
      SELECT
         fl_leg.leg_id
      FROM
         fl_leg
      WHERE
         fl_leg.event_db_id = evt_fail_effect.flight_db_id AND
         fl_leg.event_id    = evt_fail_effect.flight_id
   );


--changeSet MX-27658:240 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'EVT_FAIL_EFFECT');
END;
/

--changeSet MX-27658:241 stripComments:false
-- MX27658: UPDATE UTL_ALERT_PARM.PARM_VALUE
-- -- -- migration script to populate utl_alert_parm.parm_value -- -- -- -- -- -- -- -- -- 
UPDATE
  utl_alert_parm t
SET
  t.parm_value = (
    SELECT
      rawtohex( fl_leg.leg_id )
    FROM
      fl_leg
    WHERE
      fl_leg.legacy_key = t.parm_value
  )
WHERE
  t.parm_type = 'FLIGHT'
  AND EXISTS (
    SELECT
      1
    FROM
      fl_leg
    WHERE
      fl_leg.legacy_key = t.parm_value
);

--changeSet MX-27658:242 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_ALERT_PARM');
END;
/

--changeSet MX-27658:243 stripComments:false
-- MX27658: MERGE AND DELETE FROM INV_AC_FLIGHT_PLAN
-- -- --  migration script to populate inv_ac_flight_plan -- -- -- 
-- Populate the arrival flight leg
MERGE INTO
  inv_ac_flight_plan
USING
  (
    SELECT
      fl_leg.leg_id,
      fl_leg.event_db_id,
      fl_leg.event_id
    FROM
      fl_leg
  ) flight
ON
  (
    inv_ac_flight_plan.arr_flight_db_id = flight.event_db_id AND
    inv_ac_flight_plan.arr_flight_id    = flight.event_id
  )
WHEN MATCHED THEN
  UPDATE SET
    inv_ac_flight_plan.arr_leg_id = flight.leg_id;


--changeSet MX-27658:244 stripComments:false
-- Populate the departure flight leg
MERGE INTO
  inv_ac_flight_plan
USING
  (
    SELECT
      fl_leg.leg_id,
      fl_leg.event_db_id,
      fl_leg.event_id
    FROM
      fl_leg
  ) flight
ON
  (
    inv_ac_flight_plan.dep_flight_db_id = flight.event_db_id AND
    inv_ac_flight_plan.dep_flight_id    = flight.event_id
  )
WHEN MATCHED THEN
  UPDATE SET
    inv_ac_flight_plan.dep_leg_id = flight.leg_id
;

--changeSet MX-27658:245 stripComments:false
-- Clean up flight plan entries referring to non-existent flights (if any)
DELETE FROM inv_ac_flight_plan WHERE inv_ac_flight_plan.arr_flight_db_id IS NOT NULL
AND inv_ac_flight_plan.arr_leg_id IS NULL;

--changeSet MX-27658:246 stripComments:false
DELETE FROM inv_ac_flight_plan WHERE inv_ac_flight_plan.dep_flight_db_id IS NOT NULL
AND inv_ac_flight_plan.dep_leg_id IS NULL;

--changeSet MX-27658:247 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'INV_AC_FLIGHT_PLAN');
END;
/

--changeSet MX-27658:248 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DROP MIGRATION SCAFFOLDING: INDEXES AND COLUMNS
-- --  drop migration scaffolding: indexes and columns -- -- -- -- -- -- -- -- -- -- -- -
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('FL_LEG_LEGACY_KEY');
END;
/

--changeSet MX-27658:249 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('USG_USAGE_RECORD', 'EVENT_DB_ID');
END;
/

--changeSet MX-27658:250 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('USG_USAGE_RECORD', 'EVENT_ID');
END;
/

--changeSet MX-27658:251 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG', 'EVENT_DB_ID');
END;
/

--changeSet MX-27658:252 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG', 'EVENT_ID');
END;
/

--changeSet MX-27658:253 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG_DISRUPT', 'FLIGHT_DISRUPT_DB_ID');
END;
/

--changeSet MX-27658:254 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG_DISRUPT', 'FLIGHT_DISRUPT_ID');
END;
/

--changeSet MX-27658:255 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG_NOTE', 'EVENT_DB_ID');
END;
/

--changeSet MX-27658:256 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG_NOTE', 'EVENT_ID');
END;
/

--changeSet MX-27658:257 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('FL_LEG_NOTE', 'STAGE_ID');
END;
/

--changeSet MX-27658:258 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_STAGE
BEGIN

   -- Enable parallel DML on evt_stage
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_STAGE');

   -- --  Delete data in existing tables migrated to the new tables -- -- -- -- -- -- -
   DELETE /*+ parallel index(evt_stage ix_evtevent_evtstage2) */ FROM
      evt_stage
   WHERE
      (evt_stage.event_db_id, evt_stage.event_id) IN
      (
         SELECT /*+ index(evt_event ix_evt_event_typestatus) */
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      );

   -- Disable parallel DML on evt_stage
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_STAGE', TRUE);

END;
/

--changeSet MX-27658:259 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_INV_USAGE
BEGIN

   -- Enable parallel DML on evt_inv_usage
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_INV_USAGE');

   DELETE /*+ parallel index(evt_inv_usage ix_evtinv_evtinvusage) */ FROM
      evt_inv_usage
   WHERE
      (evt_inv_usage.event_db_id, evt_inv_usage.event_id, evt_inv_usage.event_inv_id) IN
      (
         SELECT /*+ index(evt_inv pk_evt_inv) index(evt_event ix_evt_event_typestatus) */
            evt_inv.event_db_id,
            evt_inv.event_id,
            evt_inv.event_inv_id
         FROM
            evt_inv
            INNER JOIN evt_event ON
               evt_event.event_db_id = evt_inv.event_db_id AND
               evt_event.event_id    = evt_inv.event_id
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      );

   -- Disable parallel DML on evt_inv_usage
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_INV_USAGE', TRUE);

END;
/

--changeSet MX-27658:260 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM INV_NUM_DATA
BEGIN

   -- Enable parallel DML on inv_num_data
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('INV_NUM_DATA');

   DELETE /*+ parallel index(inv_num_data ix_evtinv_invnumdata) */ FROM
      inv_num_data
   WHERE
   (inv_num_data.event_db_id, inv_num_data.event_id, inv_num_data.event_inv_id) IN
   (
      SELECT /*+ index(evt_inv pk_evt_inv) index(evt_event ix_evt_event_typestatus) */
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_inv.event_inv_id
      FROM
         evt_inv
         INNER JOIN evt_event ON
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id
      WHERE
         (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
            (0, 'UC'),
            (0, 'UR'),
            (0, 'FL')
         )
   );

   -- Disable parallel DML on inv_num_data
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('INV_NUM_DATA', TRUE);

END;
/

--changeSet MX-27658:261 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the indexes on the INV_PARM_DATA table to improve the performance of the delete
-- (the IX_EVTINV_INVPARMDATA index is not being dropped because it is used by the delete)
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INVINV_INVPARMDATA');
END;
/

--changeSet MX-27658:262 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM INV_PARM_DATA
BEGIN

   -- Enable parallel DML on inv_parm_data
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('INV_PARM_DATA');

   DELETE /*+ parallel index(inv_parm_data ix_evtinv_invparmdata) */ FROM
      inv_parm_data
   WHERE
      (inv_parm_data.event_db_id, inv_parm_data.event_id, inv_parm_data.event_inv_id) IN
      (
         SELECT /*+ index(evt_inv pk_evt_inv) index(evt_event ix_evt_event_typestatus) */
            evt_inv.event_db_id,
            evt_inv.event_id,
            evt_inv.event_inv_id
         FROM
            evt_inv
            INNER JOIN evt_event ON
               evt_event.event_db_id = evt_inv.event_db_id AND
               evt_event.event_id    = evt_inv.event_id
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      );

   -- Disable parallel DML on inv_parm_data
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('INV_PARM_DATA', TRUE);

END;
/

--changeSet MX-27658:263 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreate the indexes on the INV_PARM_DATA table that were dropped to improve the performance of the delete
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVPARMDATA" ON "INV_PARM_DATA" ("INV_NO_DB_ID", "INV_NO_ID")
');
END;
/

--changeSet MX-27658:264 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Gather statistics on the table since the indexes were recreated
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'INV_PARM_DATA');
END;
/

--changeSet MX-27658:265 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the indexes on the EVT_INV table to improve the performance of the delete
-- (the IX_EVTEVENT_EVTINV index is not being dropped because it is used by the delete)
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EQPBOMPART_EVTINV_FK');
END;
/

--changeSet MX-27658:266 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EQPPARTNO_EVTINV_FK');
END;
/

--changeSet MX-27658:267 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EQPASSMBLPOS_EVTINV');
END;
/

--changeSet MX-27658:268 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_ININV_EVTINV');
END;
/

--changeSet MX-27658:269 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INVINV_EVTINV');
END;
/

--changeSet MX-27658:270 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INVIN_EVTINV');
END;
/

--changeSet MX-27658:271 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INV_INV_EVENTINV_IE');
END;
/

--changeSet MX-27658:272 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_IVINV_EVTINV');
END;
/

--changeSet MX-27658:273 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_MAIN_EVT_EVTINV');
END;
/

--changeSet MX-27658:274 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_MAIN_INV_EVTINV');
END;
/

--changeSet MX-27658:275 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_MAIN_INV_EVT_EVTINV');
END;
/

--changeSet MX-27658:276 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_INV
BEGIN

   -- Enable parallel DML on evt_inv
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_INV');

   DELETE /*+ parallel index(evt_inv pk_evt_inv) */ FROM
      evt_inv
   WHERE
      (evt_inv.event_db_id,evt_inv.event_id) IN
      (
         SELECT /*+ index(evt_event ix_evt_event_typestatus) */
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      );

   -- Disable parallel DML on evt_inv
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_INV', TRUE);

END;
/

--changeSet MX-27658:277 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreate the indexes on the EVT_INV table that were dropped to improve the performance of the delete
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPBOMPART_EVTINV_FK" ON "EVT_INV" ("BOM_PART_DB_ID", "BOM_PART_ID")
');
END;
/

--changeSet MX-27658:278 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPPARTNO_EVTINV_FK" ON "EVT_INV" ("PART_NO_DB_ID", "PART_NO_ID")
');
END;
/

--changeSet MX-27658:279 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_EVTINV" ON "EVT_INV" ("ASSMBL_DB_ID", "ASSMBL_CD", "ASSMBL_BOM_ID", "ASSMBL_POS_ID")
');
END;
/

--changeSet MX-27658:280 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ININV_EVTINV" ON "EVT_INV" ("ASSMBL_INV_NO_DB_ID", "ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-27658:281 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EVTINV" ON "EVT_INV" ("H_INV_NO_DB_ID", "H_INV_NO_ID")
');
END;
/

--changeSet MX-27658:282 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVIN_EVTINV" ON "EVT_INV" ("INV_NO_DB_ID", "INV_NO_ID")
');
END;
/

--changeSet MX-27658:283 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_INV_EVENTINV_IE" ON "EVT_INV" ("INV_NO_DB_ID", "INV_NO_ID", "EVENT_DB_ID", "EVENT_ID")
');
END;
/

--changeSet MX-27658:284 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IVINV_EVTINV" ON "EVT_INV" ("NH_INV_NO_DB_ID", "NH_INV_NO_ID")
');
END;
/

--changeSet MX-27658:285 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAIN_EVT_EVTINV" ON "EVT_INV" ("EVENT_DB_ID", "EVENT_ID", "MAIN_INV_BOOL")
');
END;
/

--changeSet MX-27658:286 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAIN_INV_EVTINV" ON "EVT_INV" ("INV_NO_DB_ID", "INV_NO_ID", "MAIN_INV_BOOL")
');
END;
/

--changeSet MX-27658:287 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAIN_INV_EVT_EVTINV" ON "EVT_INV" ("EVENT_DB_ID", "EVENT_ID", "INV_NO_DB_ID", "INV_NO_ID", "MAIN_INV_BOOL")
');
END;
/

--changeSet MX-27658:288 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Gather statistics on the table since the indexes were recreated
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'EVT_INV');
END;
/

--changeSet MX-27658:289 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DROP TABLES THAT HAVE BEEN REPLACED OR ARE NO LONGER NEEDED
-- --  drop tables that have been replaced or are no longer needed -- -- -- -- -- -
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('JL_FLIGHT_DISRUPT_TYPE');
END;
/

--changeSet MX-27658:290 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('JL_FLIGHT_DISRUPTION_MAP');
END;
/

--changeSet MX-27658:291 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('JL_FLIGHT_DISRUPT');
END;
/

--changeSet MX-27658:292 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('JL_FLIGHT_LOAD');
END;
/

--changeSet MX-27658:293 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INV_AC_FLIGHT_PLAN', 'FK_JLFLIGHT_INVACFLIGHTPLAN');
END;
/

--changeSet MX-27658:294 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INV_AC_FLIGHT_PLAN', 'FK_JLFLIGHT_INVACFLIGHTPLAN2');
END;
/

--changeSet MX-27658:295 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_AC_FLIGHT_PLAN', 'ARR_FLIGHT_DB_ID');
END;
/

--changeSet MX-27658:296 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_AC_FLIGHT_PLAN', 'ARR_FLIGHT_ID');
END;
/

--changeSet MX-27658:297 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_AC_FLIGHT_PLAN', 'DEP_FLIGHT_DB_ID');
END;
/

--changeSet MX-27658:298 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('INV_AC_FLIGHT_PLAN', 'DEP_FLIGHT_ID');
END;
/

--changeSet MX-27658:299 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('EVT_FAIL_EFFECT', 'FK_JL_FLIGHT_EVTFAILEFFECT');
END;
/

--changeSet MX-27658:300 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EVT_FAIL_EFFECT', 'FLIGHT_DB_ID');
END;
/

--changeSet MX-27658:301 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('EVT_FAIL_EFFECT', 'FLIGHT_ID');
END;
/

--changeSet MX-27658:302 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('JL_FLIGHT');
END;
/

--changeSet MX-27658:303 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the indexes on the EVT_LOC table to improve the performance of the delete
-- (the IX_EVTEVENT_EVTLOC index is not being dropped because it is used by the delete)
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INVLOC_EVTLOC');
END;
/

--changeSet MX-27658:304 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_LOC
BEGIN

   -- Enable parallel DML on evt_loc
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_LOC');

   DELETE /*+ parallel index(evt_loc ix_evtevent_evtloc) */ FROM
      evt_loc
   WHERE
      (evt_loc.event_db_id, evt_loc.event_id) IN
      (
         SELECT /*+ index(evt_event ix_evt_event_typestatus) */
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      );

   -- Disable parallel DML on evt_loc
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_LOC', TRUE);

END;
/

--changeSet MX-27658:305 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreate the indexes on the EVT_LOC table that were dropped to improve the performance of the delete
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_EVTLOC" ON "EVT_LOC" ("LOC_DB_ID", "LOC_ID")
');
END;
/

--changeSet MX-27658:306 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Gather statistics on the table since the indexes were recreated
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'EVT_LOC');
END;
/

--changeSet MX-27658:307 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_EVENT_REL
BEGIN

   -- Enable parallel DML on evt_event_rel
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_EVENT_REL');

   DELETE /*+ parallel index(evt_event_rel ix_evtevent_evteventrel) */ FROM
      evt_event_rel
   WHERE
      (evt_event_rel.event_db_id, evt_event_rel.event_id ) IN
      (
         SELECT /*+ index(evt_event ix_evt_event_typestatus) */
            evt_event.event_db_id,
            evt_event.event_id
         FROM
            evt_event
         WHERE
            (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
               (0, 'UC'),
               (0, 'UR'),
               (0, 'FL')
            )
      )
      AND
      evt_event_rel.rel_type_cd = 'ENCF';

   -- Disable parallel DML on evt_event_rel
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_EVENT_REL', TRUE);

END;
/

--changeSet MX-27658:308 stripComments:false
-- MX27658: DELETE FROM EVT_FAIL_EFFECT
DELETE FROM
   evt_fail_effect
WHERE
   (evt_fail_effect.event_db_id, evt_fail_effect.event_id) IN
   (
      SELECT /*+ index(evt_event ix_evt_event_typestatus) */
         evt_event.event_db_id,
         evt_event.event_id
      FROM
          evt_event
      WHERE
         (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
            (0, 'UC'),
            (0, 'UR'),
            (0, 'FL')
         )
   );

--changeSet MX-27658:309 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MX27658: DELETE FROM EVT_EVENT
BEGIN

   -- Enable parallel DML on evt_event
   UTL_PARALLEL_PKG.PARALLEL_DELETE_BEGIN('EVT_EVENT');

   DELETE /*+ parallel index(evt_event ix_evt_event_typestatus) */ FROM
      evt_event
   WHERE
      (evt_event.event_type_db_id, evt_event.event_type_cd) IN (
         (0, 'UC'),
         (0, 'UR'),
         (0, 'FL')
      );

   -- Disable parallel DML on evt_event
   UTL_PARALLEL_PKG.PARALLEL_DELETE_END('EVT_EVENT', TRUE);

END;
/

--changeSet MX-27658:310 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop flight-related sequences
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('JL_FLIGHT_DISRUPT_SEQ');
END;
/

--changeSet MX-27658:311 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'JL_FLIGHT_DISRUPT_SEQ';
