--liquibase formatted sql


--changeSet MTX-852-5:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the EQP_ASSMBL.ASSMBL_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('EQP_ASSMBL', 'ASSMBL_CD');
END;
/

--changeSet MTX-852-5:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('EQP_ASSMBL', 'ASSMBL_CD');
END;
/

--changeSet MTX-852-5:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_ASSMBL modify (
"ASSMBL_CD" Varchar2 (8) NOT NULL UNIQUE
)
');
END;
/

--changeSet MTX-852-5:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the EQP_MANUFACT.MANUFACT_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('EQP_MANUFACT', 'MANUFACT_DB_ID');
END;
/

--changeSet MTX-852-5:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('EQP_MANUFACT', 'MANUFACT_DB_ID');
END;
/

--changeSet MTX-852-5:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_MANUFACT modify (
"MANUFACT_DB_ID" Number(10,0) NOT NULL  Check (MANUFACT_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the EQP_MANUFACT.MANUFACT_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('EQP_MANUFACT', 'MANUFACT_CD');
END;
/

--changeSet MTX-852-5:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('EQP_MANUFACT', 'MANUFACT_CD');
END;
/

--changeSet MTX-852-5:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_MANUFACT modify (
"MANUFACT_CD" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the UTL_ROLE.ROLE_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('UTL_ROLE', 'ROLE_CD');
END;
/

--changeSet MTX-852-5:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('UTL_ROLE', 'ROLE_CD');
END;
/

--changeSet MTX-852-5:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_ROLE modify (
"ROLE_CD" Varchar2 (8) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_VENDOR_TYPE.VENDOR_TYPE_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_VENDOR_TYPE', 'VENDOR_TYPE_DB_ID');
END;
/

--changeSet MTX-852-5:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_VENDOR_TYPE', 'VENDOR_TYPE_DB_ID');
END;
/

--changeSet MTX-852-5:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_VENDOR_TYPE modify (
"VENDOR_TYPE_DB_ID" Number(10,0) NOT NULL  Check (VENDOR_TYPE_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_VENDOR_TYPE.VENDOR_TYPE_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_VENDOR_TYPE', 'VENDOR_TYPE_CD');
END;
/

--changeSet MTX-852-5:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_VENDOR_TYPE', 'VENDOR_TYPE_CD');
END;
/

--changeSet MTX-852-5:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_VENDOR_TYPE modify (
"VENDOR_TYPE_CD" Varchar2 (8) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the SB_STATUS_BOARD.STATUS_BOARD_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('SB_STATUS_BOARD', 'STATUS_BOARD_CD');
END;
/

--changeSet MTX-852-5:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('SB_STATUS_BOARD', 'STATUS_BOARD_CD');
END;
/

--changeSet MTX-852-5:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SB_STATUS_BOARD modify (
"STATUS_BOARD_CD" Varchar2 (8) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the TAG_TAG.TAG_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('TAG_TAG', 'TAG_CD');
END;
/

--changeSet MTX-852-5:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('TAG_TAG', 'TAG_CD');
END;
/

--changeSet MTX-852-5:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TAG_TAG modify (
"TAG_CD" Varchar2 (40) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the WF_LEVEL_DEFN.WF_LEVEL_DEFN_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('WF_LEVEL_DEFN', 'WF_LEVEL_DEFN_CD');
END;
/

--changeSet MTX-852-5:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('WF_LEVEL_DEFN', 'WF_LEVEL_DEFN_CD');
END;
/

--changeSet MTX-852-5:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table WF_LEVEL_DEFN modify (
"WF_LEVEL_DEFN_CD" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_DISRUPT_TYPE.DISRUPT_TYPE_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_DISRUPT_TYPE', 'DISRUPT_TYPE_DB_ID');
END;
/

--changeSet MTX-852-5:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_DISRUPT_TYPE', 'DISRUPT_TYPE_DB_ID');
END;
/

--changeSet MTX-852-5:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_DISRUPT_TYPE modify (
"DISRUPT_TYPE_DB_ID" Number(10,0) NOT NULL  Check (DISRUPT_TYPE_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:31 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_DISRUPT_TYPE.DISRUPT_TYPE_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_DISRUPT_TYPE', 'DISRUPT_TYPE_CD');
END;
/

--changeSet MTX-852-5:32 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_DISRUPT_TYPE', 'DISRUPT_TYPE_CD');
END;
/

--changeSet MTX-852-5:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_DISRUPT_TYPE modify (
"DISRUPT_TYPE_CD" Varchar2 (8) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:34 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_DELAY_CODE.DELAY_CODE_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_DELAY_CODE', 'DELAY_CODE_DB_ID');
END;
/

--changeSet MTX-852-5:35 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_DELAY_CODE', 'DELAY_CODE_DB_ID');
END;
/

--changeSet MTX-852-5:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_DELAY_CODE modify (
"DELAY_CODE_DB_ID" Number(10,0) NOT NULL  Check (DELAY_CODE_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:37 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_DELAY_CODE.DELAY_CODE_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_DELAY_CODE', 'DELAY_CODE_CD');
END;
/

--changeSet MTX-852-5:38 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_DELAY_CODE', 'DELAY_CODE_CD');
END;
/

--changeSet MTX-852-5:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_DELAY_CODE modify (
"DELAY_CODE_CD" Varchar2 (8) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:40 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the SHIFT_SHIFT.SHIFT_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('SHIFT_SHIFT', 'SHIFT_CD');
END;
/

--changeSet MTX-852-5:41 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('SHIFT_SHIFT', 'SHIFT_CD');
END;
/

--changeSet MTX-852-5:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SHIFT_SHIFT modify (
"SHIFT_CD" Varchar2 (80) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:43 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the USER_SHIFT_PATTERN.USER_SHIFT_PATTERN_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('USER_SHIFT_PATTERN', 'USER_SHIFT_PATTERN_CD');
END;
/

--changeSet MTX-852-5:44 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('USER_SHIFT_PATTERN', 'USER_SHIFT_PATTERN_CD');
END;
/

--changeSet MTX-852-5:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table USER_SHIFT_PATTERN modify (
"USER_SHIFT_PATTERN_CD" Varchar2 (80) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the CAPACITY_PATTERN.CAPACITY_PATTERN_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('CAPACITY_PATTERN', 'CAPACITY_PATTERN_DB_ID');
END;
/

--changeSet MTX-852-5:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('CAPACITY_PATTERN', 'CAPACITY_PATTERN_DB_ID');
END;
/

--changeSet MTX-852-5:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CAPACITY_PATTERN modify (
"CAPACITY_PATTERN_DB_ID" Number(10,0) NOT NULL  Check (CAPACITY_PATTERN_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:49 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the CAPACITY_PATTERN.CAPACITY_PATTERN_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('CAPACITY_PATTERN', 'CAPACITY_PATTERN_CD');
END;
/

--changeSet MTX-852-5:50 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('CAPACITY_PATTERN', 'CAPACITY_PATTERN_CD');
END;
/

--changeSet MTX-852-5:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CAPACITY_PATTERN modify (
"CAPACITY_PATTERN_CD" Varchar2 (80) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:52 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_RESULT_EVENT.RESULT_EVENT_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_RESULT_EVENT', 'RESULT_EVENT_DB_ID');
END;
/

--changeSet MTX-852-5:53 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_RESULT_EVENT', 'RESULT_EVENT_DB_ID');
END;
/

--changeSet MTX-852-5:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_RESULT_EVENT modify (
"RESULT_EVENT_DB_ID" Number(10,0) NOT NULL  Check (RESULT_EVENT_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:55 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_RESULT_EVENT.RESULT_EVENT_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_RESULT_EVENT', 'RESULT_EVENT_CD');
END;
/

--changeSet MTX-852-5:56 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_RESULT_EVENT', 'RESULT_EVENT_CD');
END;
/

--changeSet MTX-852-5:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_RESULT_EVENT modify (
"RESULT_EVENT_CD" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:58 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_RESULT_EVENT.USER_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_RESULT_EVENT', 'USER_CD');
END;
/

--changeSet MTX-852-5:59 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_RESULT_EVENT', 'USER_CD');
END;
/

--changeSet MTX-852-5:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_RESULT_EVENT modify (
"USER_CD" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:61 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the MAINT_PRGM_LOG.MAINT_PRGM_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('MAINT_PRGM_LOG', 'MAINT_PRGM_DB_ID');
END;
/

--changeSet MTX-852-5:62 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('MAINT_PRGM_LOG', 'MAINT_PRGM_DB_ID');
END;
/

--changeSet MTX-852-5:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MAINT_PRGM_LOG modify (
"MAINT_PRGM_DB_ID" Number(10,0) NOT NULL  Check (MAINT_PRGM_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:64 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the MAINT_PRGM_LOG.MAINT_PRGM_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('MAINT_PRGM_LOG', 'MAINT_PRGM_ID');
END;
/

--changeSet MTX-852-5:65 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('MAINT_PRGM_LOG', 'MAINT_PRGM_ID');
END;
/

--changeSet MTX-852-5:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MAINT_PRGM_LOG modify (
"MAINT_PRGM_ID" Number(10,0) NOT NULL  Check (MAINT_PRGM_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:67 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the MAINT_PRGM_LOG.MAINT_LOG_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('MAINT_PRGM_LOG', 'MAINT_LOG_ID');
END;
/

--changeSet MTX-852-5:68 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('MAINT_PRGM_LOG', 'MAINT_LOG_ID');
END;
/

--changeSet MTX-852-5:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table MAINT_PRGM_LOG modify (
"MAINT_LOG_ID" Number(10,0) NOT NULL  UNIQUE  Check (MAINT_LOG_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:70 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the SCHED_EXT_PART.SCHED_DB_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('SCHED_EXT_PART', 'SCHED_DB_ID');
END;
/

--changeSet MTX-852-5:71 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('SCHED_EXT_PART', 'SCHED_DB_ID');
END;
/

--changeSet MTX-852-5:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_EXT_PART modify (
"SCHED_DB_ID" Number(10,0) NOT NULL  Check (SCHED_DB_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:73 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the SCHED_EXT_PART.SCHED_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('SCHED_EXT_PART', 'SCHED_ID');
END;
/

--changeSet MTX-852-5:74 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('SCHED_EXT_PART', 'SCHED_ID');
END;
/

--changeSet MTX-852-5:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_EXT_PART modify (
"SCHED_ID" Number(10,0) NOT NULL  Check (SCHED_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:76 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the SCHED_EXT_PART.SCHED_EXT_PART_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('SCHED_EXT_PART', 'SCHED_EXT_PART_ID');
END;
/

--changeSet MTX-852-5:77 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('SCHED_EXT_PART', 'SCHED_EXT_PART_ID');
END;
/

--changeSet MTX-852-5:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_EXT_PART modify (
"SCHED_EXT_PART_ID" Number(10,0) NOT NULL  UNIQUE  Check (SCHED_EXT_PART_ID BETWEEN 0 AND 4294967295 )
)
');
END;
/

--changeSet MTX-852-5:79 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the UTL_NOTIF_EVENT_TYPE.NOTIF_EVENT_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('UTL_NOTIF_EVENT_TYPE', 'NOTIF_EVENT_CD');
END;
/

--changeSet MTX-852-5:80 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('UTL_NOTIF_EVENT_TYPE', 'NOTIF_EVENT_CD');
END;
/

--changeSet MTX-852-5:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_NOTIF_EVENT_TYPE modify (
"NOTIF_EVENT_CD" Varchar2 (100) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:82 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the UTL_NOTIF_TYPE.NOTIF_TYPE_CD column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('UTL_NOTIF_TYPE', 'NOTIF_TYPE_CD');
END;
/

--changeSet MTX-852-5:83 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('UTL_NOTIF_TYPE', 'NOTIF_TYPE_CD');
END;
/

--changeSet MTX-852-5:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_NOTIF_TYPE modify (
"NOTIF_TYPE_CD" Varchar2 (100) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:85 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the TAX.TAX_CODE column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('TAX', 'TAX_CODE');
END;
/

--changeSet MTX-852-5:86 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('TAX', 'TAX_CODE');
END;
/

--changeSet MTX-852-5:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table TAX modify (
"TAX_CODE" Varchar2 (40) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:88 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the CHARGE.CHARGE_ID column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('CHARGE', 'CHARGE_ID');
END;
/

--changeSet MTX-852-5:89 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('CHARGE', 'CHARGE_ID');
END;
/

--changeSet MTX-852-5:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CHARGE modify (
"CHARGE_ID" Raw(16) NOT NULL
)
');
END;
/

--changeSet MTX-852-5:91 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the CHARGE.CHARGE_CODE column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('CHARGE', 'CHARGE_CODE');
END;
/

--changeSet MTX-852-5:92 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('CHARGE', 'CHARGE_CODE');
END;
/

--changeSet MTX-852-5:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table CHARGE modify (
"CHARGE_CODE" Varchar2 (40) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:94 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_LPA_ISSUE_TYPE.DISPLAY_CODE column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_LPA_ISSUE_TYPE', 'DISPLAY_CODE');
END;
/

--changeSet MTX-852-5:95 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_LPA_ISSUE_TYPE', 'DISPLAY_CODE');
END;
/

--changeSet MTX-852-5:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LPA_ISSUE_TYPE modify (
"DISPLAY_CODE" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:97 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REF_LPA_RUN_STATUS.DISPLAY_CODE column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REF_LPA_RUN_STATUS', 'DISPLAY_CODE');
END;
/

--changeSet MTX-852-5:98 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_UNQ_DROP('REF_LPA_RUN_STATUS', 'DISPLAY_CODE');
END;
/

--changeSet MTX-852-5:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_LPA_RUN_STATUS modify (
"DISPLAY_CODE" Varchar2 (16) NOT NULL  UNIQUE
)
');
END;
/

--changeSet MTX-852-5:100 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreating the constraints on the REPL_CTRL.GLOBAL_NAME column as non-deferrable
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('REPL_CTRL', 'GLOBAL_NAME');
END;
/

--changeSet MTX-852-5:101 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- This unique index was originally created with a name, therefore, we need to 
-- drop it and the related index by name.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REPL_CTRL', 'UQ_REPLCTRL_GLOBALNAME');
END;
/

--changeSet MTX-852-5:102 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('UQ_REPLCTRL_GLOBALNAME');
END;
/

--changeSet MTX-852-5:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REPL_CTRL modify (
"GLOBAL_NAME" Varchar2 (4000) Constraint "UQ_REPLCTRL_GLOBALNAME" UNIQUE
)
');
END;
/