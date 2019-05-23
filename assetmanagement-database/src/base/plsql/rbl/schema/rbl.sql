--liquibase formatted sql


--changeSet rbl:1 stripComments:false
-- Create Types section
-- Create Tables section
/*
Created		7/25/2013
Modified		10/31/2014
Project		SMOS
Model		Maintenix
Company		Mxi Technologies
Author		
Version		1306
Database		Oracle 10g 
*/
Create table OPR_REPORT (
	REPORT_CODE Varchar2 (100 CHAR) NOT NULL ,
	REPORT_NAME Varchar2 (200 CHAR),
	PROGRAM_NAME Varchar2 (200 CHAR) NOT NULL ,
	PROGRAM_TYPE_CODE Varchar2 (10 CHAR) NOT NULL  Constraint OPR_PROGRAMTYPECD_CHK Check (PROGRAM_TYPE_CODE IN ('XTRACT','XFORM','OTHER') ) ,
	START_DATE Date NOT NULL ,
	END_DATE Date NOT NULL ,
	LAST_START_DATE Date,
	LAST_SUCCESS_END_DATE Date,
	ROW_PROCESSED Integer,
	LAST_ERROR_END_DATE Date,
	LAST_ERROR Varchar2 (4000 CHAR),
	STATUS_CODE Varchar2 (10 CHAR) Default 'IDLE',
	EXEC_PARALLEL_FLAG Number(1,0),
	ACTIVE_FLAG Number(1,0),
	EXECUTION_ORDER Number,
	CUTOFF_DATE Date,
 Constraint PK_OPR_REPORT primary key (REPORT_CODE)
)
;

--changeSet rbl:2 stripComments:false
Create table OPR_REPORT_REGEN (
	REPORT_CODE Varchar2 (100) NOT NULL ,
	START_DATE Date NOT NULL ,
	END_DATE Date NOT NULL ,
	SUBMITTED_BY Varchar2 (40) NOT NULL ,
	SUBMITTED_DATE Date NOT NULL ,
	EXEC_START_DATE Date,
	EXEC_END_DATE Date,
	STATUS_CODE Varchar2 (20),
	ERROR Varchar2 (4000),
 Constraint pk_OPR_REPORT_REGEN primary key (REPORT_CODE,START_DATE,END_DATE)
)
;

--changeSet rbl:3 stripComments:false
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
)
;

--changeSet rbl:4 stripComments:false
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
	CYCLES Number,
	FAULT_CNT Number,
 Constraint pk_OPR_RBL_FAULT_MONTHLY primary key (OPERATOR_ID,AIRCRAFT_ID,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YEAR_MONTH)
)
;

--changeSet rbl:5 stripComments:false
Create table OPR_CALENDAR_MONTH (
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	START_DATE Date NOT NULL ,
	END_DATE Date NOT NULL ,
 Constraint PK_OPR_CALENDAR_MONTH primary key (YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:6 stripComments:false
Create table OPR_RBL_MONTHLY_DELAY (
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	DEPARTURE_AIRPORT_CODE Varchar2 (200) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	DELAY_CATEGORY_CODE Varchar2 (10) Default '0'  NOT NULL ,
	DELAYED_DEPARTURES Integer Default 0  NOT NULL ,
	DELAY_TIME Integer Default 0  NOT NULL ,
	MAINTENANCE_DELAY_TIME Integer Default 0 NOT NULL ,
	AIRCRAFT_ID Raw(16),
	OPERATOR_ID Raw(16),
 Constraint PK_OPR_RBL_MONTHLY_DELAY primary key (YEAR_CODE,MONTH_CODE,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE,DEPARTURE_AIRPORT_CODE,CHAPTER_CODE,DELAY_CATEGORY_CODE)
)
;

--changeSet rbl:7 stripComments:false
Create table OPR_RBL_MONTHLY_INCIDENT (
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	DEPARTURE_AIRPORT_CODE Varchar2 (200) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	INCIDENT_CODE Varchar2 (16) NOT NULL ,
	NUMBER_OF_INCIDENTS Integer Default 0  NOT NULL ,
	AIRCRAFT_ID Raw(16),
	OPERATOR_ID Raw(16),
 Constraint PK_OPR_RBL_MONTHLY_INCIDENT primary key (YEAR_CODE,MONTH_CODE,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE,DEPARTURE_AIRPORT_CODE,CHAPTER_CODE,INCIDENT_CODE)
)
;

--changeSet rbl:8 stripComments:false
Create table OPR_RBL_FAULT (
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	FOUND_ON_DATE Date NOT NULL ,
	FAULT_ID Raw(16) NOT NULL ,
	FAULT_DESCRIPTION Varchar2 (4000),
	FAULT_SEVERITY Varchar2 (8) NOT NULL ,
	DEFERRAL_CLASS Varchar2 (8) NOT NULL ,
 Constraint PK_OPR_RBL_FAULT primary key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID)
)
;

--changeSet rbl:9 stripComments:false
Create table OPR_RBL_FLIGHT_DISRUPTION (
	FLIGHT_ID Raw(16) NOT NULL ,
	FLIGHT_NUMBER Varchar2 (500) NOT NULL ,
	SCHEDULED_DEPARTURE_DATE Date NOT NULL ,
	DEPARTURE_AIRPORT Varchar2 (200) NOT NULL ,
	DISRUPTION_TYPE_CODE Varchar2 (8) NOT NULL ,
	DISRUPTION_ID Raw(16) NOT NULL ,
	DELAY_CODE Varchar2 (8) NOT NULL ,
	MAINTENANCE_DELAY_TIME Number(10,5) Default 0 ,
	FAULT_ID Raw(16),
	SERIAL_NUMBER Varchar2 (50),
	CHAPTER_CODE Varchar2 (50),
	FOUND_ON_DATE Date,
 Constraint PK_OPR_RBL_FLIGHT_DISRUPTION primary key (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT,DISRUPTION_TYPE_CODE,DISRUPTION_ID)
)
;

--changeSet rbl:10 stripComments:false
Create table OPR_RBL_FLIGHT (
	FLIGHT_ID Raw(16) NOT NULL ,
	FLIGHT_NUMBER Varchar2 (500) NOT NULL ,
	SCHEDULED_DEPARTURE_DATE Date NOT NULL ,
	DEPARTURE_AIRPORT Varchar2 (200) NOT NULL ,
	ACTUAL_DEPARTURE_DATE Date NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	FLIGHT_STATUS Varchar2 (16) NOT NULL ,
	FLIGHT_TYPE Varchar2 (8) Default 'RGEN' NOT NULL ,
	AIRCRAFT_ID Raw(16) NOT NULL ,
	USAGE_RECORD_ID Raw(16) NOT NULL ,
	ACTUAL_ARRIVAL_DATE Date,
	SCHEDULED_ARRIVAL_DATE Date,
 Constraint PK_OPR_RBL_FLIGHT primary key (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT)
)
;

--changeSet rbl:11 stripComments:false
Create table OPR_RBL_FAULT_INCIDENT (
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	FOUND_ON_DATE Date NOT NULL ,
	FAULT_ID Raw(16) NOT NULL ,
	INCIDENT_CODE Varchar2 (16) NOT NULL ,
 Constraint PK_OPR_RBL_FAULT_INCIDENT primary key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID,INCIDENT_CODE)
)
;

--changeSet rbl:12 stripComments:false
Create table OPR_RBL_USAGE (
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	USAGE_DATE Date NOT NULL ,
	DATA_TYPE Varchar2 (80) NOT NULL ,
	USAGE_TYPE Varchar2 (12) NOT NULL ,
	USAGE_RECORD_ID Raw(16) NOT NULL ,
	USAGE_VALUE Number(10,5) Default 0  NOT NULL ,
 Constraint PK_OPR_RBL_USAGE primary key (SERIAL_NUMBER,USAGE_DATE,DATA_TYPE,USAGE_TYPE,USAGE_RECORD_ID)
)
;

--changeSet rbl:13 stripComments:false
Create table OPR_FLEET_MOVEMENT (
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	PHASE_IN_DATE Date NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	SOURCE_SYSTEM Varchar2 (15) NOT NULL ,
	PHASE_OUT_DATE Date,
	FIN_NUMBER Varchar2 (40),
	AIRCRAFT_ID Raw(16),
	OPERATOR_ID Raw(16),
 Constraint PK_OPR_FLEET_MOVEMENT primary key (OPERATOR_REGISTRATION_CODE,PHASE_IN_DATE,OPERATOR_CODE,SERIAL_NUMBER)
)
;

--changeSet rbl:14 stripComments:false
Create table OPR_RBL_WORK_PACKAGE (
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	ACTUAL_START_DATE Date NOT NULL ,
	WORK_PACKAGE_BARCODE Varchar2 (80) NOT NULL ,
	SCHEDULED_START_DATE Date,
	WORK_PACKAGE_NAME Varchar2 (500),
	SCHEDULED_END_DATE Date,
	ACTUAL_END_DATE Date,
 Constraint PK_OPR_RBL_WORK_PACKAGE primary key (SERIAL_NUMBER,ACTUAL_START_DATE,WORK_PACKAGE_BARCODE)
)
;

--changeSet rbl:15 stripComments:false
Create table OPR_RBL_DELAY_CATEGORY (
	DELAY_CATEGORY_CODE Varchar2 (10) Default '0'  NOT NULL ,
	LOW_RANGE Integer Default 0  NOT NULL ,
	HIGH_RANGE Integer Default 0 ,
	DELAY_CATEGORY_NAME Varchar2 (30) NOT NULL ,
	DISPLAY_ORDER Integer NOT NULL ,
 Constraint PK_OPR_RBL_DELAY_CATEGORY primary key (DELAY_CATEGORY_CODE)
)
;

--changeSet rbl:16 stripComments:false
Create table OPR_RBL_COMP_RMVL_RAW (
	AIRCRAFT_ID Raw(16) NOT NULL ,
	INVENTORY_ID Raw(16) NOT NULL ,
	CONFIG_SLOT_ID Raw(16) NOT NULL ,
	SCHED_ID Raw(16) NOT NULL ,
	SCHED_PART_ID Number NOT NULL ,
	SCHED_RMVD_PART_ID Number NOT NULL ,
	FLEET_TYPE Varchar2 (8),
	REGISTRATION_CODE Varchar2 (10),
	ACFT_SERIAL_NUMBER Varchar2 (40),
	BOM_CLASS_CD Varchar2 (8),
	CONFIG_SLOT Varchar2 (50),
	QUANTITY_PER_ACFT Number,
	MANUFACTURER_CODE Varchar2 (16),
	PART_NUMBER Varchar2 (40),
	PART_NAME Varchar2 (80),
	INVENTORY_CLASS Varchar2 (8),
	REMOVED_SERIAL_NUMBER Varchar2 (40),
	REMOVAL_REASON Varchar2 (8),
	REMOVAL_DATE Date,
	YEAR_MONTH Number,
	REMOVAL_TYPE Varchar2 (1),
	JUSTIFIED_FAILURE Number,
 Constraint PK_OPR_RBL_COMP_RMVL_RAW primary key (AIRCRAFT_ID,INVENTORY_ID,CONFIG_SLOT_ID,SCHED_ID,SCHED_PART_ID,SCHED_RMVD_PART_ID)
)
;

--changeSet rbl:17 stripComments:false
Create table OPR_RBL_COMP_RMVL_FLEET_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT Varchar2 (2 CHAR) NOT NULL ,
	PART_NUMBER Varchar2 (40 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
	PART_NAME Varchar2 (80 CHAR),
	INVENTORY_CLASS Varchar2 (8) NOT NULL ,
	CYCLES Integer,
	FLIGHT_HOURS Number,
	QUANTITY_PER_ACFT Number,
	NUMBER_OF_ACFT Number,
	REMOVAL_COUNT Number,
	SCHED_REMOVAL Number,
	UNSCHED_REMOVAL Number,
	JUSTIFIED_FAILURE Number,
 Constraint PK_OPR_RBL_COMP_RMVL_FLEET_MON primary key (OPERATOR_ID,FLEET_TYPE,CONFIG_SLOT,PART_NUMBER,YEAR_MONTH,INVENTORY_CLASS)
)
;

--changeSet rbl:18 stripComments:false
Create table OPR_RBL_HIST_USAGE (
	YEAR Varchar2 (4) NOT NULL ,
	MONTH Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	FLIGHT_HOURS Number(10,5) NOT NULL ,
	CYCLES Integer NOT NULL ,
	REVENUE_FLIGHT_HOURS Number(10,5) NOT NULL ,
	REVENUE_CYCLES Integer NOT NULL ,
	DAYS_OUT_OF_SERVICE Number(10,5) NOT NULL ,
	COMPLETED_FLIGHTS Integer NOT NULL ,
	CANCELLED_FLIGHTS Integer NOT NULL ,
 Constraint PK_OPR_RBL_HIST_USAGE primary key (YEAR,MONTH,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,OPERATOR_CODE,SERIAL_NUMBER)
)
;

--changeSet rbl:19 stripComments:false
Create table OPR_RBL_FAULT_FLEET_MONTHLY (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8 CHAR),
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT_CODE Varchar2 (10 CHAR) NOT NULL ,
	FAULT_SOURCE_CODE Varchar2 (10 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	FLIGHT_HOURS Number,
	CYCLES Number,
	FAULT_CNT Number,
 Constraint pk_OPR_RBL_FAULT_FLEET_MONTHLY primary key (OPERATOR_ID,FLEET_TYPE,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YEAR_MONTH)
)
;

--changeSet rbl:20 stripComments:false
Create table OPR_RBL_HIST_FAULT_MONTHLY (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	ACFT_SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	TAIL_NUMBER Varchar2 (8 CHAR),
	CONFIG_SLOT_CODE Varchar2 (10 CHAR) NOT NULL ,
	FAULT_SOURCE_CODE Varchar2 (10 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	FLIGHT_HOURS Number NOT NULL ,
	CYCLES Number NOT NULL ,
	FAULT_CNT Number NOT NULL ,
 Constraint pk_OPR_RBL_HIST_FAULT_MONTHLY primary key (OPERATOR_CODE,FLEET_TYPE,ACFT_SERIAL_NUMBER,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YEAR_MONTH)
)
;

--changeSet rbl:21 stripComments:false
Create table OPR_RBL_HIST_FAULT_FLT_MONTHLY (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT_CODE Varchar2 (10 CHAR) NOT NULL ,
	FAULT_SOURCE_CODE Varchar2 (10 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	FLIGHT_HOURS Number NOT NULL ,
	CYCLES Number NOT NULL ,
	FAULT_CNT Number NOT NULL ,
 Constraint pk_OPR_RBL_HIST_FAULT_FLT_MONT primary key (OPERATOR_CODE,FLEET_TYPE,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YEAR_MONTH)
)
;

--changeSet rbl:22 stripComments:false
Create table OPR_RBL_HIST_ACFT_FAULT (
	FLEET Varchar2 (8) NOT NULL ,
	OPERATOR Varchar2 (8) NOT NULL ,
	AC_REG_CODE Varchar2 (10),
	AC_PN Varchar2 (40) NOT NULL ,
	AC_SN Varchar2 (40) NOT NULL ,
	LOGBOOK_REF Varchar2 (80),
	FAULT_DESCRIPTION Varchar2 (4000),
	ATA_CHAPTER Varchar2 (50) NOT NULL ,
	ATA_SECTION Varchar2 (50) NOT NULL ,
	FOUND_ON_DATE Date NOT NULL ,
	FAULT_TYPE Varchar2 (8) NOT NULL ,
	FAULT_SOURCE Varchar2 (8) NOT NULL ,
	SEVERITY_CODE Varchar2 (20),
	DEFERRAL_CLASS Varchar2 (8),
	CLOSED_DATE Date,
	CORRECTIVE_ACTION Varchar2 (4000),
	DEFERRAL_REF Varchar2 (80),
	FLIGHT_NUMBER Varchar2 (100),
	DEPARTURE_AIRPORT Varchar2 (50),
	ARRIVAL_AIRPORT Varchar2 (50),
	FAULT_CODE Varchar2 (80),
	JIC_CODE Varchar2 (200),
	JIC_NAME Varchar2 (200),
	REQ_CODE Varchar2 (200),
	REQ_NAME Varchar2 (200),
	BLOCK_CODE Varchar2 (200),
	BLOCK_NAME Varchar2 (200),
	REMOVAL_CODE Varchar2 (80),
	WORK_ORDER Varchar2 (80),
	DOC_REFERENCE Varchar2 (80),
	MECHANIC Varchar2 (1000),
	CERTIFIER Varchar2 (1000),
	INSPECTOR Varchar2 (1000),
	ZONE_LIST Varchar2 (1000)
)
;

--changeSet rbl:23 stripComments:false
Create table OPR_REPORT_MVIEW (
	CATEGORY Varchar2 (50) NOT NULL ,
	MVIEW_NAME Varchar2 (30) NOT NULL ,
	REFRESH_FREQUENCY_CODE Varchar2 (20),
	EXECUTION_ORDER Number Default 0 NOT NULL ,
	REFRESH_START_DATE Date,
	REFRESH_END_DATE Date,
 Constraint PK_OPR_REPORT_MVIEW primary key (CATEGORY,MVIEW_NAME)
)
;

--changeSet rbl:24 stripComments:false
Create table OPR_RBL_HIST_COMP_RMVL_FLT_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
	CONFIG_SLOT Varchar2 (2 CHAR) NOT NULL ,
	PART_NUMBER Varchar2 (40 CHAR) NOT NULL ,
	YEAR_MONTH Number NOT NULL ,
	OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
	PART_NAME Varchar2 (80 CHAR),
	INVENTORY_CLASS Varchar2 (8) NOT NULL ,
	CYCLES Integer,
	FLIGHT_HOURS Number,
	QUANTITY_PER_ACFT Number,
	NUMBER_OF_ACFT Number,
	REMOVAL_COUNT Number,
	SCHED_REMOVAL Number,
	UNSCHED_REMOVAL Number,
	JUSTIFIED_FAILURE Number,
 Constraint PK_OPR_RBL_HIST_COMP_RMVL_FLT_ primary key (OPERATOR_ID,FLEET_TYPE,CONFIG_SLOT,PART_NUMBER,YEAR_MONTH,INVENTORY_CLASS)
)
;

--changeSet rbl:25 stripComments:false
Create table OPR_RBL_HIST_COMP_REMOVAL (
	FLEET Varchar2 (8) NOT NULL ,
	OPERATOR Varchar2 (8) NOT NULL ,
	AC_REG Varchar2 (10),
	AC_PN Varchar2 (40),
	AC_SN Varchar2 (40) NOT NULL ,
	PN Varchar2 (40) NOT NULL ,
	VENDOR_CODE Varchar2 (16) NOT NULL ,
	SN Varchar2 (40) NOT NULL ,
	RMVL_DATE Date NOT NULL ,
	SCHEDULED Varchar2 (1) NOT NULL ,
	RMVL_REASON_DESCRIPTION Varchar2 (2000),
	JUSTIFIED_FAILURE Varchar2 (1) NOT NULL ,
	ROOT_PN Varchar2 (40),
	ROOT_SN Varchar2 (40) NOT NULL ,
	ATA_CHAPTER Varchar2 (50) NOT NULL ,
	ATA_SECTION Varchar2 (50) NOT NULL ,
	TSI Number,
	CSI Number,
	TSO Number,
	CSO Number,
	LAST_INSTALl_DATE Date,
	RO_NUMBER Varchar2 (50),
	LAST_RO Varchar2 (50),
	LAST_RECEIVED_CONDITION Varchar2 (20),
	PN_IN Varchar2 (40),
	SN_IN Varchar2 (40),
	PN_DESCRIPTION Varchar2 (100),
	FAULT_CODE Varchar2 (80),
	DAYS_SINCE_INSTALL Number,
	PO_NUMBER Varchar2 (80),
	FAULT_DESCRIPTION Varchar2 (4000),
	FAULT_CORRECTIVE_ACTION Varchar2 (4000),
	DEFERRAL_CLASS Varchar2 (8),
	DEFERRAL_REF Varchar2 (80),
	DEPARTURE_AIRPORT Varchar2 (50),
	ARRIVAL_AIRPORT Varchar2 (50),
	FLIGHT_NUMBER Varchar2 (100),
	MECHANIC Varchar2 (1000),
	INSPECTOR Varchar2 (1000),
	WO_ORDER Varchar2 (20),
	WO_ORDER_NAME Varchar2 (200),
	JIC_CODE Varchar2 (200)
)
;

--changeSet rbl:26 stripComments:false
Create table OPR_RBL_HIST_ENG_QTY_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	MODEL_NAME Varchar2 (100) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	IN_FLEET Number,
	IN_SERVICE Number,
 Constraint PK_OPR_RBL_HIST_ENG_QTY_MON primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:27 stripComments:false
Create table OPR_RBL_HIST_ENG_INDICATOR_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	MODEL_NAME Varchar2 (100) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	IFSD Number NOT NULL ,
	POWER_LOSS Number NOT NULL ,
	REJECTED_T_O Number NOT NULL ,
	FULL_POWER_TO Number NOT NULL ,
	FLIGHT_CANCELATION Number NOT NULL ,
 Constraint PK_OPR_RBL_HIST_ENG_INDICATOR_ primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:28 stripComments:false
Create table OPR_RBL_HIST_ENG_USAGE_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	MODEL_NAME Varchar2 (100) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	CYCLES Number,
	HOURS Number,
 Constraint PK_OPR_RBL_HIST_ENG_USAGE_MON primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:29 stripComments:false
Create table OPR_RBL_HIST_ENG_SHOPVISIT_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	MODEL_NAME Varchar2 (100) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	SHOP_VISIT Number,
	RATE_PER_1K_HOURS Number,
 Constraint PK_OPR_RBL_HIST_ENG_SHOPVISIT_ primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:30 stripComments:false
Create table OPR_RBL_HIST_ENG_URMVL_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	MODEL_NAME Varchar2 (100) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	REMOVAL Number,
	RATE_PER_1K_HOURS Number,
 Constraint PK_OPR_RBL_HIST_ENG_URMVL_MON primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:31 stripComments:false
Create table OPR_RBL_WORK_PACKAGE_DOS (
	YEAR_CODE Varchar2 (25) NOT NULL ,
	MONTH_CODE Varchar2 (25) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	WORK_PACKAGE_BARCODE Varchar2 (80) NOT NULL ,
	OUT_OF_SERVICE_DATE Date NOT NULL ,
	WORK_PACKAGE_NAME Varchar2 (500),
 Constraint PK_OPR_RBL_WORK_PACKAGE_DOS primary key (YEAR_CODE,MONTH_CODE,SERIAL_NUMBER,WORK_PACKAGE_BARCODE,OUT_OF_SERVICE_DATE)
)
;

--changeSet rbl:32 stripComments:false
Create table OPR_RBL_ENG_INSERVICE_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	IN_SERVICE Number,
 Constraint PK_OPR_RBL_ENG_INSERVICE_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:33 stripComments:false
Create table OPR_RBL_ENG_USAGE_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	DATA_TYPE_CODE Varchar2 (8) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	QUANTITY Number,
 Constraint PK_OPR_RBL_ENG_USAGE_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,DATA_TYPE_CODE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:34 stripComments:false
Create table OPR_RBL_ENG_USAGE_RAW (
	USAGE_RECORD_ID Raw(16) NOT NULL ,
	INVENTORY_ID Raw(16) NOT NULL ,
	ASSMBL_CODE Varchar2 (8) NOT NULL ,
	ORIGINAL_ASSMBL_CODE Varchar2 (8),
	SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	MANUFACTURER Varchar2 (16),
	PART_NUMBER Varchar2 (40),
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8),
	USAGE_TYPE_CODE Varchar2 (20),
	DATA_TYPE_CODE Varchar2 (80) NOT NULL ,
	USAGE_DATE Date NOT NULL ,
	TSN_QT Float,
	TSO_QT Float,
	TSI_QT Float,
	TSN_DELTA_QT Float,
	TSO_DELTA_QT Float,
	TSI_DELTA_QT Float,
	NEGATED_BOOL Number(1,0),
 Constraint PK_OPR_RBL_ENG_USAGE_RAW primary key (USAGE_RECORD_ID,INVENTORY_ID,ASSMBL_CODE,SERIAL_NUMBER,OPERATOR_ID,DATA_TYPE_CODE,USAGE_DATE)
)
;

--changeSet rbl:35 stripComments:false
Create table OPR_RBL_ENG_INDICATOR_RAW (
	OPERATOR_ID Raw(16) NOT NULL ,
	INVENTORY_ID Raw(16) NOT NULL ,
	LEG_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8),
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	MANUFACTURER Varchar2 (16),
	PART_NUMBER Varchar2 (40),
	CONDITION_CODE Varchar2 (20),
	DATA_TYPE_CODE Varchar2 (80) NOT NULL ,
	OPER_INDICATOR Varchar2 (100) NOT NULL ,
	DATA_DATE Date,
	DATA_VALUE_CODE Varchar2 (8),
	DATA_QUANTITY Float,
	DATA_FLAG Number(1,0),
 Constraint PK_OPR_RBL_ENG_INDICATOR_RAW primary key (OPERATOR_ID,INVENTORY_ID,LEG_ID,FLEET_TYPE,ENGINE_TYPE,SERIAL_NUMBER,DATA_TYPE_CODE)
)
;

--changeSet rbl:36 stripComments:false
Create table OPR_RBL_ENG_INDICATOR_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	OPER_INDICATOR Varchar2 (100) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	QUANTITY Number,
 Constraint PK_OPR_RBL_ENG_INDICATOR_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,OPER_INDICATOR,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:37 stripComments:false
Create table OPR_RBL_ENG_SHOPVISIT_RAW (
	OPERATOR_ID Raw(16) NOT NULL ,
	INVENTORY_ID Raw(16) NOT NULL ,
	SCHED_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8),
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	MANUFACTURER Varchar2 (16),
	PART_NUMBER Varchar2 (40),
	CONDITION_CODE Varchar2 (20),
	LOCATION_CODE Varchar2 (100) NOT NULL ,
	LOCATION_TYPE_CODE Varchar2 (8) NOT NULL ,
	COMPLETED_DATE Date NOT NULL ,
 Constraint PK_OPR_RBL_ENG_SHOPVISIT_RAW primary key (OPERATOR_ID,INVENTORY_ID,SCHED_ID,FLEET_TYPE,ENGINE_TYPE,SERIAL_NUMBER,LOCATION_CODE,COMPLETED_DATE)
)
;

--changeSet rbl:38 stripComments:false
Create table OPR_RBL_ENG_SHOPVISIT_MON (
	OPERATOR_ID Raw(16),
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	QUANTITY Number,
 Constraint PK_OPR_RBL_ENG_SHOPVISIT_MON primary key (OPERATOR_CODE,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:39 stripComments:false
Create table OPR_RBL_ENG_URMVL_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	QUANTITY Number,
 Constraint PK_OPR_RBL_ENG_URMVL_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:40 stripComments:false
Create table OPR_RBL_ENG_DELAY_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	QUANTITY Number,
	CYCLES Number,
 Constraint PK_OPR_RBL_ENG_DELAY_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:41 stripComments:false
Create table OPR_RBL_ENG_INFLEET_MON (
	OPERATOR_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (50) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (20) NOT NULL ,
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	IN_FLEET Number,
 Constraint PK_OPR_RBL_ENG_INFLEET_MON primary key (OPERATOR_ID,FLEET_TYPE,ENGINE_TYPE,YEAR_CODE,MONTH_CODE)
)
;

--changeSet rbl:42 stripComments:false
Create table OPR_RBL_ENG_CANCELLATION_RAW (
	OPERATOR_ID Raw(16) NOT NULL ,
	FLIGHT_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8),
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (8) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	DELAY_CODE Varchar2 (8) NOT NULL ,
	DISRUPTION_TYPE_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	FOUND_ON_DATE Date NOT NULL ,
 Constraint PK_OPR_RBL_ENG_CANCELLATION_RA primary key (OPERATOR_ID,FLIGHT_ID,FLEET_TYPE,ENGINE_TYPE,CHAPTER_CODE,DELAY_CODE,DISRUPTION_TYPE_CODE,SERIAL_NUMBER,FOUND_ON_DATE)
)
;

--changeSet rbl:43 stripComments:false
Create table OPR_REPORT_JOB (
	JOB_CODE Varchar2 (30) NOT NULL ,
	JOB_DESCRIPTION Varchar2 (500) NOT NULL ,
	PROGRAM_NAME Varchar2 (200) NOT NULL ,
	FREQUENCY_CODE Varchar2 (20) NOT NULL ,
	INTERVAL Number,
	SCHEDULE_DATE Date NOT NULL ,
	NEXT_RUN_DATE Date,
	LAST_START_DATE Date,
	LAST_END_DATE Date,
 Constraint PK_OPR_REPORT_JOB primary key (JOB_CODE)
)
;

--changeSet rbl:44 stripComments:false
Create table OPR_RBL_ENG_IFDRTO_EVENT_RAW (
	OPERATOR_ID Raw(16) NOT NULL ,
	FAULT_ID Raw(16) NOT NULL ,
	OPERATOR_CODE Varchar2 (8),
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	ENGINE_TYPE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (40) NOT NULL ,
	CHAPTER_CODE Varchar2 (50) NOT NULL ,
	FOUND_ON_DATE Date NOT NULL ,
	INCIDENT_CODE Varchar2 (16) NOT NULL ,
 Constraint PK_OPR_RBL_ENG_IFDRTO_EVENT_RA primary key (OPERATOR_ID,FAULT_ID,FLEET_TYPE,ENGINE_TYPE,SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,INCIDENT_CODE)
)
;

--changeSet rbl:45 stripComments:false
Create table OPR_RBL_HIST_INCIDENT (
	YEAR Varchar2 (4) NOT NULL ,
	MONTH Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	ABORTED_APPROACHES Integer Default 0 NOT NULL ,
	AIR_TURNBACKS Integer Default 0 NOT NULL ,
	DIVERSIONS Integer Default 0 NOT NULL ,
	EMERGENCY_DESCENTS Integer Default 0 NOT NULL ,
	GROUND_TURNBACKS Integer Default 0 NOT NULL ,
	INFLIGHT_SHUTDOWNS Integer Default 0 NOT NULL ,
	ABORTED_TAKEOFFS Integer Default 0 NOT NULL ,
	TECHNICAL_INCIDENTS Integer Default 0 NOT NULL ,
	AIR_INTERRUPTIONS Integer Default 0 NOT NULL ,
	GROUND_INTERRUPTIONS Integer Default 0 NOT NULL ,
	AIRCRAFT_ON_GROUND Integer Default 0 NOT NULL ,
 Constraint PK_OPR_RBL_HIST_INCIDENT primary key (YEAR,MONTH,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,OPERATOR_CODE,SERIAL_NUMBER)
)
;

--changeSet rbl:46 stripComments:false
Create table OPR_RBL_HIST_DELAY (
	YEAR Varchar2 (4) NOT NULL ,
	MONTH Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	DEPARTURE_AIRPORT Varchar2 (4000) NOT NULL ,
	CHAPTER Varchar2 (50) NOT NULL ,
	DELAY_CATEGORY_CODE Varchar2 (10) NOT NULL ,
	NUMBER_OF_DELAYS Integer NOT NULL ,
	TOTAL_DELAY_TIME Integer NOT NULL ,
	TOTAL_MAINTENANCE_DELAY_TIME Integer NOT NULL ,
 Constraint PK_OPR_RBL_HIST_DELAY primary key (YEAR,MONTH,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,OPERATOR_CODE,SERIAL_NUMBER,DEPARTURE_AIRPORT,CHAPTER,DELAY_CATEGORY_CODE)
)
;

--changeSet rbl:47 stripComments:false
Create table OPR_RBL_MONTHLY_USAGE (
	YEAR_CODE Varchar2 (4) NOT NULL ,
	MONTH_CODE Varchar2 (2) NOT NULL ,
	FLEET_TYPE Varchar2 (8) NOT NULL ,
	OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
	SERIAL_NUMBER Varchar2 (50) NOT NULL ,
	OPERATOR_CODE Varchar2 (8) NOT NULL ,
	REVENUE_FLIGHT_HOURS Number(10,5) Default 0 NOT NULL ,
	REVENUE_CYCLES Integer Default 0 NOT NULL ,
	FLIGHT_HOURS Number(10,5) Default 0 NOT NULL ,
	CYCLES Integer Default 0 NOT NULL ,
	CANCELLED_DEPARTURES Integer Default 0 NOT NULL ,
	COMPLETED_DEPARTURES Integer Default 0 NOT NULL ,
	DAYS_OUT_OF_SERVICE Number(10,5) Default 0 NOT NULL ,
	MEL_DELAYED_DEPARTURES Integer Default 0 NOT NULL ,
	WORK_PACKAGE_LIST Varchar2 (4000),
	AIRCRAFT_ID Raw(16),
	OPERATOR_ID Raw(16),
 Constraint PK_OPR_RBL_MONTHLY_USAGE primary key (YEAR_CODE,MONTH_CODE,FLEET_TYPE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE)
)
;

--changeSet rbl:48 stripComments:false
Create table OPR_RBL_ENG_SHOPVISIT_LOC (
	LOCATION_ID Raw(16) NOT NULL ,
	LOCATION_CODE Varchar2 (2000) NOT NULL ,
	LOCATION_TYPE_CODE Varchar2 (8),
 Constraint PK_OPR_RBL_ENG_SHOPVISIT_LOC primary key (LOCATION_ID)
)
;

--changeSet rbl:49 stripComments:false
Create table OPR_RBL_ENG_INDICATOR_MEASURE (
	DATA_TYPE_ID Raw(16) NOT NULL ,
	DATA_TYPE_CODE Varchar2 (80) NOT NULL ,
	DATA_TYPE_DESCRIPTION Varchar2 (100) NOT NULL ,
 Constraint PK_OPR_RBL_ENG_INDICATOR_MEASU primary key (DATA_TYPE_ID)
)
;

--changeSet rbl:50 stripComments:false
-- Create Alternate keys section
Alter table OPR_CALENDAR_MONTH add Constraint UK_CALENDAR_MONTH unique (START_DATE,END_DATE) 
;

--changeSet rbl:51 stripComments:false
-- Create Indexes section
-- Create Foreign keys section
Create Index IX_FK_OPR_REPORTREGEN ON OPR_REPORT_REGEN (REPORT_CODE)
;

--changeSet rbl:52 stripComments:false
Alter table OPR_REPORT_REGEN add Constraint FK_OPR_REPORTREGEN foreign key (REPORT_CODE) references OPR_REPORT (REPORT_CODE) 
;

--changeSet rbl:53 stripComments:false
Create Index IX_FK_MONTHLY_DELAY_MONTH ON OPR_RBL_MONTHLY_DELAY (YEAR_CODE,MONTH_CODE)
;

--changeSet rbl:54 stripComments:false
Alter table OPR_RBL_MONTHLY_DELAY add Constraint FK_MONTHLY_DELAY_MONTH foreign key (YEAR_CODE,MONTH_CODE) references OPR_CALENDAR_MONTH (YEAR_CODE,MONTH_CODE) 
;

--changeSet rbl:55 stripComments:false
Create Index IX_FK_MONTHLY_USAGE_MONTH ON OPR_RBL_MONTHLY_USAGE (YEAR_CODE,MONTH_CODE)
;

--changeSet rbl:56 stripComments:false
Alter table OPR_RBL_MONTHLY_USAGE add Constraint FK_MONTHLY_USAGE_MONTH foreign key (YEAR_CODE,MONTH_CODE) references OPR_CALENDAR_MONTH (YEAR_CODE,MONTH_CODE) 
;

--changeSet rbl:57 stripComments:false
Create Index IX_FK_MONTHLY_DELAY_CATEGORY ON OPR_RBL_MONTHLY_DELAY (DELAY_CATEGORY_CODE)
;

--changeSet rbl:58 stripComments:false
Alter table OPR_RBL_MONTHLY_DELAY add Constraint FK_MONTHLY_DELAY_CATEGORY foreign key (DELAY_CATEGORY_CODE) references OPR_RBL_DELAY_CATEGORY (DELAY_CATEGORY_CODE) 
;

--changeSet rbl:59 stripComments:false
-- Create Object Tables section
-- Create Table comments section
Comment on table OPR_REPORT is 'Reliability report table'
;

--changeSet rbl:60 stripComments:false
Comment on table OPR_RBL_FAULT_RAW is 'This is the raw data'
;

--changeSet rbl:61 stripComments:false
Comment on table OPR_RBL_FAULT_MONTHLY is 'Monthly fault group by operator_id, aircraft_id, config_slot_code and year_month and the monthly flight hours'
;

--changeSet rbl:62 stripComments:false
Comment on table OPR_RBL_MONTHLY_DELAY is 'Represents a summary of technical delays resulting from maintenance activity for an aircraft operator during a calendar month'
;

--changeSet rbl:63 stripComments:false
Comment on table OPR_RBL_MONTHLY_INCIDENT is 'Represents a summary of technical incidents for an aircraft operator during a calendar month'
;

--changeSet rbl:64 stripComments:false
Comment on table OPR_RBL_FAULT is 'Represents faults found during a flight'
;

--changeSet rbl:65 stripComments:false
Comment on table OPR_RBL_FLIGHT_DISRUPTION is 'Represents delays/incidents captured during a flight'
;

--changeSet rbl:66 stripComments:false
Comment on table OPR_RBL_USAGE is 'Represents usage captured for an aircraft.   Includes Usage Records of type MXACCRUAL and MXFLIGHT'
;

--changeSet rbl:67 stripComments:false
Comment on table OPR_FLEET_MOVEMENT is 'Represents the movement of aircraft between operators.'
;

--changeSet rbl:68 stripComments:false
Comment on table OPR_RBL_WORK_PACKAGE is 'Represents heavy maintenance activity for an aircraft.'
;

--changeSet rbl:69 stripComments:false
Comment on table OPR_RBL_DELAY_CATEGORY is 'Represents a  delay category indicating the amount of time (in minutes) that a departure is delayed.'
;

--changeSet rbl:70 stripComments:false
Comment on table OPR_RBL_FAULT_FLEET_MONTHLY is 'Monthly fault group by operator_id, aircraft_id, config_slot_code and year_month and the monthly flight hours'
;

--changeSet rbl:71 stripComments:false
Comment on table OPR_RBL_HIST_FAULT_MONTHLY is 'Monthly fault group by operator_id, aircraft_id, config_slot_code and year_month and the monthly flight hours'
;

--changeSet rbl:72 stripComments:false
Comment on table OPR_RBL_HIST_FAULT_FLT_MONTHLY is 'Monthly fault group by operator_id, aircraft_id, config_slot_code and year_month and the monthly flight hours'
;

--changeSet rbl:73 stripComments:false
Comment on table OPR_RBL_WORK_PACKAGE_DOS is 'Represents heavy maintenance activity for an aircraft.'
;

--changeSet rbl:74 stripComments:false
-- Create Attribute comments section
Comment on column OPR_RBL_MONTHLY_DELAY.DELAYED_DEPARTURES is 'Total Number of Delayed Departures'
;

--changeSet rbl:75 stripComments:false
Comment on column OPR_RBL_MONTHLY_DELAY.DELAY_TIME is 'Total Number of Minutes, Departures have been delayed'
;

--changeSet rbl:76 stripComments:false
Comment on column OPR_RBL_MONTHLY_DELAY.MAINTENANCE_DELAY_TIME is 'Total Number of Minutes, Departures have been delayed because of maintenance activity'
;

--changeSet rbl:77 stripComments:false
Comment on column OPR_RBL_DELAY_CATEGORY.HIGH_RANGE is 'NULL indicates that the high range has no limit'
;

--changeSet rbl:78 stripComments:false
Comment on column OPR_RBL_ENG_INDICATOR_MEASURE.DATA_TYPE_DESCRIPTION is 'description that will be displayed in the report'
;