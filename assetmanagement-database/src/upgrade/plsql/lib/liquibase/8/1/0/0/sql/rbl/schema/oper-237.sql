--liquibase formatted sql


--changeSet oper-237:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_CALENDAR_MONTH (
   YEAR_CODE Varchar2 (25) NOT NULL ,
   MONTH_CODE Varchar2 (25) NOT NULL ,
   start_date Date NOT NULL ,
   end_date Date NOT NULL ,
 Constraint PK_OPR_CALENDAR_MONTH primary key (YEAR_CODE,MONTH_CODE) 
)
') ;
END;
/

--changeSet oper-237:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_MONTHLY_RELIABILITY (
   YEAR_CODE Varchar2 (25) NOT NULL ,
   MONTH_CODE Varchar2 (25) NOT NULL ,
   OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   OPERATOR_CODE Varchar2 (8) NOT NULL ,
   FLEET_TYPE Varchar2 (8) NOT NULL ,
   CYCLES Integer Default 0  NOT NULL ,
   FLIGHT_HOURS Number(10,5) Default 0  NOT NULL ,
   DAYS_OUT_OF_SERVICE Number(10,5) Default 0  NOT NULL ,
   COMPLETED_DEPARTURES Integer Default 0  NOT NULL ,
   DELAYED_DEPARTURES Integer Default 0  NOT NULL ,
   DELAYED_DEPARTURES_GT_15 Integer Default 0  NOT NULL ,
   DELAY_TIME Number(10,5) Default 0  NOT NULL ,
   CANCELLED_DEPARTURES Integer Default 0  NOT NULL ,
   DIVERTED_DEPARTURES Integer Default 0  NOT NULL ,
   AOG_DELAYED_DEPARTURES Integer Default 0  NOT NULL ,
   MEL_DELAYED_DEPARTURES Integer Default 0  NOT NULL ,
   AIR_TURNBACKS Integer Default 0  NOT NULL ,
   ABORTED_DEPARTURES Integer Default 0  NOT NULL ,
   RETURN_TO_GATE Integer Default 0  NOT NULL ,
   EMERGENCY_DESCENT Integer Default 0  NOT NULL ,
   INFLIGHT_SHUTDOWNS Integer Default 0  NOT NULL ,
   TAIR_INCIDENTS Integer Default 0 NOT NULL ,
   TINR_INCIDENTS Integer Default 0 NOT NULL ,
   ABORTED_APPROACHES Integer Default 0 NOT NULL ,
   SOURCE_SYSTEM Varchar2 (15) NOT NULL ,
   AIRCRAFT_ID Raw(16),
   OPERATOR_ID Raw(16),
 Constraint PK_OPR_MONTHLY_RELIABILITY primary key (YEAR_CODE,MONTH_CODE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE) 
)
') ;
END;
/

--changeSet oper-237:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_MONTHLY_DELAY (
   YEAR_CODE Varchar2 (25) NOT NULL ,
   MONTH_CODE Varchar2 (25) NOT NULL ,
   OPERATOR_REGISTRATION_CODE Varchar2 (10) NOT NULL ,
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   OPERATOR_CODE Varchar2 (8) NOT NULL ,
   DEPARTURE_AIRPORT_CODE Varchar2 (200) NOT NULL ,
   CHAPTER_CODE Varchar2 (50) NOT NULL ,
   DELAY_CATEGORY_CODE Varchar2 (10) Default ''0''  NOT NULL ,
   FLEET_TYPE Varchar2 (8) NOT NULL ,
   DELAYED_DEPARTURES Integer Default 0  NOT NULL ,
   DELAY_TIME Integer Default 0  NOT NULL ,
   SOURCE_SYSTEM Varchar2 (15) NOT NULL ,
   AIRCRAFT_ID Raw(16),
   OPERATOR_ID Raw(16),
 Constraint PK_OPR_MONTHLY_DELAY primary key (YEAR_CODE,MONTH_CODE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE,DEPARTURE_AIRPORT_CODE,CHAPTER_CODE,DELAY_CATEGORY_CODE) 
)
') ;
END;
/

--changeSet oper-237:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_MONTHLY_INCIDENT (
   YEAR_CODE Varchar2 (25) NOT NULL ,
   MONTH_CODE Varchar2 (25) NOT NULL ,
   OPERATOR_REGISTRATION_CODE Varchar2 (8) NOT NULL ,
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   OPERATOR_CODE Varchar2 (8) NOT NULL ,
   AIRPORT_CODE Varchar2 (200) NOT NULL ,
   CHAPTER_CODE Varchar2 (50) NOT NULL ,
   INCIDENT_CODE Varchar2 (16) NOT NULL ,
   NUMBER_OF_INCIDENTS Integer Default 0  NOT NULL ,
   FLEET_TYPE Varchar2 (8) NOT NULL ,
   AIRCRAFT_ID Raw(16),
   OPERATOR_ID Raw(16),
 Constraint PK_OPR_MONTHLY_INCIDENT primary key (YEAR_CODE,MONTH_CODE,OPERATOR_REGISTRATION_CODE,SERIAL_NUMBER,OPERATOR_CODE,AIRPORT_CODE,CHAPTER_CODE,INCIDENT_CODE) 
)
') ;
END;
/

--changeSet oper-237:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_FAULT (
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   CHAPTER_CODE Varchar2 (50) NOT NULL ,
   FOUND_ON_DATE Date NOT NULL ,
   FAULT_ID Raw(16) NOT NULL ,
   FAULT_DESCRIPTION Varchar2 (4000),
   FAULT_SEVERITY Varchar2 (8) NOT NULL ,
   DEFERRAL_CLASS Varchar2 (8) NOT NULL ,
 Constraint PK_OPR_FAULT primary key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID) 
)
') ;
END;
/

--changeSet oper-237:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_FLIGHT_DISRUPTION (
   FLIGHT_ID Raw(16) NOT NULL ,
   FLIGHT_NUMBER Varchar2 (500) NOT NULL ,
   SCHEDULED_DEPARTURE_DATE Date NOT NULL ,
   DEPARTURE_AIRPORT_CODE Varchar2 (200) NOT NULL ,
   DISRUPTION_TYPE_CODE Varchar2 (8) NOT NULL ,
   DELAY_CODE Varchar2 (8) NOT NULL ,
   MAINTENANCE_DELAY_TIME Number(10,5) Default 0 ,
   FAULT_ID Raw(16),
   SERIAL_NUMBER Varchar2 (50),
   CHAPTER_CODE Varchar2 (50),
   FOUND_ON_DATE Date,
 Constraint PK_OPR_FLIGHT_DISRUPTION primary key (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT_CODE,DISRUPTION_TYPE_CODE) 
)
') ;
END;
/

--changeSet oper-237:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_FLIGHT (
   FLIGHT_ID Raw(16) NOT NULL ,
   FLIGHT_NUMBER Varchar2 (500) NOT NULL ,
   SCHEDULED_DEPARTURE_DATE Date NOT NULL ,
   DEPARTURE_AIRPORT_CODE Varchar2 (200) NOT NULL ,
   ACTUAL_DEPARTURE_DATE Date NOT NULL ,
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   FLIGHT_STATUS_CODE Varchar2 (16) NOT NULL ,
   AIRCRAFT_ID Raw(16) NOT NULL ,
   ACTUAL_ARRIVAL_DATE Date,
   SCHEDULED_ARRIVAL_DATE Date,
 Constraint PK_OPR_FLIGHT primary key (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT_CODE) 
)
') ;
END;
/

--changeSet oper-237:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_FAULT_INCIDENT (
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   CHAPTER_CODE Varchar2 (50) NOT NULL ,
   FOUND_ON_DATE Date NOT NULL ,
   FAULT_ID Raw(16) NOT NULL ,
   INCIDENT_CODE Varchar2 (16) NOT NULL ,
 Constraint PK_OPR_FAULT_INCIDENT primary key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID,INCIDENT_CODE) 
)
') ;
END;
/

--changeSet oper-237:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_USAGE (
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   USAGE_DATE Date NOT NULL ,
   DATA_TYPE_CODE Varchar2 (80) NOT NULL ,
   USAGE_TYPE_CODE Varchar2 (12) NOT NULL ,
   USAGE_VALUE Number(10,5) Default 0  NOT NULL ,
 Constraint PK_OPR_USAGE primary key (SERIAL_NUMBER,USAGE_DATE,DATA_TYPE_CODE,USAGE_TYPE_CODE) 
)
') ;
END;
/

--changeSet oper-237:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_TECHNICAL_INCIDENT (
   INCIDENT_CODE Varchar2 (16) NOT NULL ,
   INCIDENT_NAME Varchar2 (45) NOT NULL ,
   INCIDENT_TYPE Varchar2 (4) NOT NULL  Check (incident_type in (''TAIR'', ''TINR'') ) ,
   DISPLAY_ORDER Integer Default 0  NOT NULL ,
 Constraint PK_OPR_TECHNICAL_INCIDENT primary key (INCIDENT_CODE) 
)
') ;
END;
/

--changeSet oper-237:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
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
') ;
END;
/

--changeSet oper-237:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_MAINTENANCE (
   SERIAL_NUMBER Varchar2 (50) NOT NULL ,
   SCHEDULED_START_DATE Date NOT NULL ,
   WORK_PACKAGE_BARCODE Varchar2 (20) NOT NULL ,
   SCHEDULED_END_DATE Date NOT NULL ,
   ACTUAL_START_DATE Date,
   ACTUAL_END_DATE Date,
   DURATION Number Default 0,
 Constraint PK_OPR_MAINTENANCE primary key (SERIAL_NUMBER,SCHEDULED_START_DATE,WORK_PACKAGE_BARCODE) 
)
') ;
END;
/

--changeSet oper-237:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_DELAY_CATEGORY (
   DELAY_CATEGORY_CODE Varchar2 (10) Default ''0''  NOT NULL ,
   LOW_RANGE Integer Default 0  NOT NULL ,
   HIGH_RANGE Integer Default 0 ,
   DELAY_CATEGORY_NAME Varchar2 (30) NOT NULL ,
   DISPLAY_ORDER Integer NOT NULL ,
 Constraint PK_OPR_DELAY_CATEGORY primary key (DELAY_CATEGORY_CODE) 
)
') ;
END;
/

--changeSet oper-237:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
   utl_migr_schema_pkg.table_create('
Create table OPR_RBL_FAULT_FLEET_SUMMARY (
   OPERATOR_ID Raw(16) NOT NULL ,
   OPERATOR_CODE Varchar2 (8 CHAR) NOT NULL ,
   FLEET_TYPE Varchar2 (8 CHAR) NOT NULL ,
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
 Constraint pk_OPR_RBL_FAULT_FLEET_SUMMARY primary key (OPERATOR_ID,FLEET_TYPE,CONFIG_SLOT_CODE,FAULT_SOURCE_CODE,YM_1MON) 
)
') ;
END;
/

--changeSet oper-237:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_CALENDAR_MONTH add Constraint UK_CALENDAR_MONTH unique (start_date,end_date) 
');
END;
/

--changeSet oper-237:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_OPR_DELAY_CALENDAR_MONTH ON OPR_MONTHLY_DELAY (YEAR_CODE,MONTH_CODE)
');
END;
/

--changeSet oper-237:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_MONTHLY_RELIABILITY add Constraint FK_OPR_RELIABILITY_MONTH foreign key (YEAR_CODE,MONTH_CODE) references OPR_CALENDAR_MONTH (YEAR_CODE,MONTH_CODE) 
');
END;
/

--changeSet oper-237:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_OPR_RELIABILITY_MONTH ON OPR_MONTHLY_RELIABILITY (YEAR_CODE,MONTH_CODE)
');
END;
/

--changeSet oper-237:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_OPR_DELAY_CALENDAR_MONTH ON OPR_MONTHLY_DELAY (YEAR_CODE,MONTH_CODE)
');
END;
/

--changeSet oper-237:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_MONTHLY_DELAY add Constraint FK_OPR_DELAY_CALENDAR_MONTH foreign key (YEAR_CODE,MONTH_CODE) references OPR_CALENDAR_MONTH (YEAR_CODE,MONTH_CODE) 
');
END;
/

--changeSet oper-237:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_OPR_FAULT_INCIDENT ON OPR_FAULT_INCIDENT (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID)
');
END;
/

--changeSet oper-237:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_FAULT_INCIDENT add Constraint FK_OPR_FAULT_INCIDENT_FAULT foreign key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID) references OPR_FAULT (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID) 
');
END;
/

--changeSet oper-237:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_OPR_FAULT_DISRUPTION ON OPR_FLIGHT_DISRUPTION (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID)
');
END;
/

--changeSet oper-237:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_FLIGHT_DISRUPTION add Constraint FK_FLIGHT_DISRUTION_FAULT foreign key (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID) references OPR_FAULT (SERIAL_NUMBER,CHAPTER_CODE,FOUND_ON_DATE,FAULT_ID)  DEFERRABLE
');
END;
/

--changeSet oper-237:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FLIGHT_DISRUPTION_FLIGHT ON OPR_FLIGHT_DISRUPTION (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT_CODE)
');
END;
/

--changeSet oper-237:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_FLIGHT_DISRUPTION add Constraint FK_FLIGHT_DISRUPTION_FLIGHT foreign key (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT_CODE) references OPR_FLIGHT (FLIGHT_ID,FLIGHT_NUMBER,SCHEDULED_DEPARTURE_DATE,DEPARTURE_AIRPORT_CODE)  DEFERRABLE
');
END;
/

--changeSet oper-237:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_INCIDENT_FAULT ON OPR_FAULT_INCIDENT (INCIDENT_CODE)
');
END;
/

--changeSet oper-237:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_FAULT_INCIDENT add Constraint FK_OPR_FAULT_INCIDENT foreign key (INCIDENT_CODE) references OPR_TECHNICAL_INCIDENT (INCIDENT_CODE) 
');
END;
/

--changeSet oper-237:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_INCIDENT_INCIDENT ON OPR_MONTHLY_INCIDENT (INCIDENT_CODE)
');
END;
/

--changeSet oper-237:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_MONTHLY_INCIDENT add Constraint FK_INCIDENT_INCIDENT foreign key (INCIDENT_CODE) references OPR_TECHNICAL_INCIDENT (INCIDENT_CODE)  DEFERRABLE
');
END;
/

--changeSet oper-237:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_OPR_DELAY_CATEGORY ON OPR_MONTHLY_DELAY (DELAY_CATEGORY_CODE)
');
END;
/

--changeSet oper-237:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table OPR_MONTHLY_DELAY add Constraint FK_OPR_DELAY_CATEGORY foreign key (DELAY_CATEGORY_CODE) references OPR_DELAY_CATEGORY (DELAY_CATEGORY_CODE) 
');
END;
/