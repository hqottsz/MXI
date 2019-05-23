--liquibase formatted sql


--changeSet oper-236:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create Tables section
/*
Created		25/07/2013
Modified		12/02/2014
Project		SMOS
Model		Maintenix
Company		Mxi Technologies
Author		
Version		1306
Database		Oracle 10g 
*/
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_REPORT (
	REPORT_CODE Varchar2 (100 CHAR) NOT NULL ,
	REPORT_NAME Varchar2 (200 CHAR),
	PROGRAM_NAME Varchar2 (200 CHAR) NOT NULL ,
	PROGRAM_TYPE_CODE Varchar2 (10 CHAR) NOT NULL  Constraint OPR_PROGRAMTYPECD_CHK Check (PROGRAM_TYPE_CODE IN (''XTRACK'',''XFORM'') ) ,
	START_DATE Date NOT NULL ,
	END_DATE Date NOT NULL ,
	LAST_START_DATE Date,
	LAST_SUCCESS_END_DATE Date,
	ROW_PROCESSED Integer,
	LAST_ERROR_END_DATE Date,
	LAST_ERROR Varchar2 (4000 CHAR),
	STATUS_CODE Varchar2 (10 CHAR) Default ''IDLE'',
	EXEC_PARALLEL_FLAG Number(1,0),
	ACTIVE_FLAG Number(1,0),
	EXECUTION_ORDER Number,
 Constraint PK_OPR_REPORT primary key (REPORT_CODE) 
)');
END;
/

--changeSet oper-236:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_REPORT_REGEN (
	REPORT_CODE Varchar2 (100) NOT NULL ,
	START_DATE Date NOT NULL ,
	END_DATE Date NOT NULL ,
	SUBMITTED_BY Varchar2 (40) NOT NULL ,
	SUBMITTED_DATE Date NOT NULL ,
	EXTRACTED_DATE Date,
	TRANSFORMED_DATE Date,
	ERROR Varchar2 (4000)
)');
END;
/

--changeSet oper-236:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_RBL_FAULT_RAW (
	AIRCRAFT_ID Raw(16) NOT NULL ,
	INVENTORY_ID Raw(16) NOT NULL ,
	CONFIG_SLOT_ID Raw(16) NOT NULL ,
	FAULT_ID Raw(16) NOT NULL ,
	LEG_ID Raw(16),
	FLEET_TYPE Varchar2 (8),
	CONFIG_SLOT_CODE Varchar2 (10 CHAR),
	FAULT_SOURCE_CODE Varchar2 (10 CHAR),
	FAULT_SEVERITY_CODE Varchar2 (8 CHAR),
	FOUND_ON_DATE Date,
	YEAR_MONTH Number,
 Constraint pk_OPR_RBL_FAULT_RAW primary key (AIRCRAFT_ID,INVENTORY_ID,CONFIG_SLOT_ID,FAULT_ID) 
)');
END;
/

--changeSet oper-236:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_RBL_FAULT_MONTHLY (
	OPERATOR_ID Raw(16) NOT NULL ,
	AIRCRAFT_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8 CHAR),
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	TAIL_NUMBER Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT_CODE Varchar2 (10 CHAR) NOT NULL ,
	FAULT_SOURCE_CODE Varchar2 (10 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	FLIGHT_HOURS Number,
	FLIGHT_CYCLES Number,
	FAULT_CNT Number,
 Constraint pk_OPR_RBL_FAULT_MONTHLY primary key (OPERATOR_ID,AIRCRAFT_ID,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YEAR_MONTH) 
)');
END;
/

--changeSet oper-236:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_RBL_FAULT_SUMMARY (
	OPERATOR_ID Raw(16) NOT NULL ,
	AIRCRAFT_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	TAIL_NUMBER Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT_CODE Varchar2 (10 CHAR) NOT NULL ,
	FAULT_SOURCE_CODE Varchar2 (10 CHAR) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	YM_1MON Number NOT NULL ,
	YM_2MON Number NOT NULL ,
	YM_3MON Number NOT NULL ,
	YM_6MON Number NOT NULL ,
	YM_12MON Number NOT NULL ,
	M1_QT Number NOT NULL ,
	M1_FH_QT Number NOT NULL ,
	M1_FC_QT Number NOT NULL ,
	M2_QT Number NOT NULL ,
	M2_FH_QT Number NOT NULL ,
	M2_FC_QT Number NOT NULL ,
	M3_QT Number NOT NULL ,
	M3_FH_QT Number NOT NULL ,
	M3_FC_QT Number NOT NULL ,
	M3_TOT_QT Number NOT NULL ,
	M3_FH_TOT_QT Number NOT NULL ,
	M3_FC_TOT_QT Number NOT NULL ,
	M6_TOT_QT Number NOT NULL ,
	M6_FH_TOT_QT Number NOT NULL ,
	M6_FC_TOT_QT Number NOT NULL ,
	M12_TOT_QT Number NOT NULL ,
	M12_FH_TOT_QT Number NOT NULL ,
	M12_FC_TOT_QT Number NOT NULL ,
 Constraint pk_OPR_RBL_FAULT_SUMMARY primary key (OPERATOR_ID,AIRCRAFT_ID,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YM_1MON) 
)');
END;
/

--changeSet oper-236:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create Indexes section
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index IX_RBLFAULTSUM02 ON OPR_RBL_FAULT_SUMMARY (FLEET_TYPE,YEAR_CODE,MONTH_CODE,FAULT_SOURCE_CODE) 
');
END;
/

--changeSet oper-236:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index IX_OPR_RBLFAULTFLTSUM02 ON OPR_RBL_FAULT_FLEET_SUMMARY (FLEET_TYPE,YEAR_CODE,MONTH_CODE,FAULT_SOURCE_CODE) 
');
END;
/

--changeSet oper-236:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create Foreign keys section
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index IX_OPER_REPORT_REGEN ON OPR_REPORT_REGEN (REPORT_CODE)
');
END;
/

--changeSet oper-236:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_REPORT_REGEN add Constraint FK_OPR_REPORTREGEN foreign key (REPORT_CODE) references OPR_REPORT (REPORT_CODE) 
');
END;
/