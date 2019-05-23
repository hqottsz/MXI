delete from MIM_DATA_TYPE;
delete from REF_ENG_UNIT;
delete from FC_RATE;
delete from FC_RANGE;
delete from FC_MODEL;

-- REM INSERTING into MIM_DATA_TYPE
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,1,0,'HOUR',0,'US',2,'HOURS','Flying Hours','The time from the moment an aircraft leaves the surface until it comes in contact with the surface at the next point of landing.',1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('18-JAN-06','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,10,0,'CYCLES',0,'US',0,'CYCLES','Cycles',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,21,0,'DAY',0,'CA',0,'CDY','Calendar Day',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,22,0,'WEEK',0,'CA',0,'CWK','Calendar Week',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,23,0,'MONTH',0,'CA',0,'CMON','Calendar Month',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,24,0,'YEAR',0,'CA',0,'CYR','Calendar Year',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,25,0,'MONTH',0,'CA',0,'CLMON','Calendar Last Day of Month',null,1,0,to_date('18-APR-02','DD-MON-RR'),to_date('18-APR-02','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,30,0,'LNDG',0,'US',0,'LANDING','Aircraft Landings',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('27-JUN-01','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,31,0,'HOUR',0,'CA',0,'CHR','Calendar Hour',null,1,0,to_date('08-MAY-06','DD-MON-RR'),to_date('08-MAY-06','DD-MON-RR'),100,'MXI');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,5,10,'COUNTS',0,'US',0,'FDAY','Flight Days',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,25,0,'HOUR',0,'US',2,'EOT','Engine Operating Time',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,26,0,'HOUR',0,'US',2,'AOT','APU Operating Time ',null,1,0,to_date('27-JUN-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,27,0,'CYCLES',0,'US',0,'ECYC','Engine Cycles','Engine Cycles',1,0,to_date('07-OCT-04','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,28,0,'CYCLES',0,'US',0,'ACYC','APU Cycles','Engine Cycles',1,0,to_date('07-OCT-04','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,1,0,'HOUR',0,'US',0,'H','Hours','Hours',1,0,to_date('06-APR-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,2,0,'CYCLES',0,'US',0,'C','Cycles','Cycles',1,0,to_date('06-APR-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,3,0,'HOUR',0,'US',0,'AOH','APU Hours','APU Hours',1,0,to_date('06-APR-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,4,0,'CYCLES',0,'US',0,'AOC','APU Cycles','APU Cycles',1,0,to_date('06-APR-01','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,5,0,'HOURS',0,'US',0,'AOT','APU Operating Time','APU Operating Time',1,0,to_date('18-OCT-99','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (100,7,0,'CYCLES',0,'US',0,'ECYC','Engine Cycles','Engine Cycles',1,0,to_date('18-OCT-99','DD-MON-RR'),to_date('17-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1000,4650,'LBS',0,'ME',0,'TOWT','Take Off Weight',null,0,0,to_date('04-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1001,4650,'EA',0,'ME',0,'PASS','Number of Passengers',null,0,0,to_date('04-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1002,4650,'QTS',0,'ME',0,'OCON','Oil Consumption',null,0,0,to_date('04-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1003,null,null,0,'CH',0,'TSIG','Take Off Signal',null,0,0,to_date('04-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1004,4650,'LBS',0,'ME',0,'MAXTHR','Maximum Thrust',null,0,0,to_date('09-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1005,4650,'C',0,'ME',0,'MAXTMP','Maximum Temperature',null,0,0,to_date('09-MAY-04','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1006,null,null,0,'CME',0,'CAL','Calendar Measurement','Calendar Measurement Test',0,0,to_date('18-JUL-05','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1007,4650,'TEST',0,'ME',0,'NUM','Numeric Measurement','Numeric Measurement Test',0,0,to_date('15-JUN-05','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1008,null,null,0,'CH',0,'CHAR','Character Measurement','Character Measurement Test',0,0,to_date('15-JUN-05','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,1009,null,null,0,'US',0,'TN','TName','Test Description',1,0,to_date('09-FEB-07','DD-MON-RR'),to_date('09-FEB-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,2000,null,null,0,'US',0,'HOUR',null,null,1,0,to_date('09-FEB-07','DD-MON-RR'),to_date('20-FEB-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,2004,0,'CYCLES',0,'US',0,'TCal','Test Calculation',null,0,0,to_date('09-MAY-07','DD-MON-RR'),to_date('09-MAY-07','DD-MON-RR'),4650,'MX0706_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,2005,0,'CYCLES',0,'US',0,'TCal','Test Calculation',null,0,0,to_date('09-MAY-07','DD-MON-RR'),to_date('09-MAY-07','DD-MON-RR'),4650,'MX0706_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,2016,4650,'LBS',0,'ME',0,'TEMP','Engine Temperatue',null,0,0,to_date('09-MAY-07','DD-MON-RR'),to_date('09-MAY-07','DD-MON-RR'),4650,'MX0706_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3000,4650,'kg',0,'ME',0,'KG','Weight','Weight in KG',1,0,to_date('16-JAN-08','DD-MON-RR'),to_date('16-JAN-08','DD-MON-RR'),4650,'MX0803_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3001,4650,'Hz',0,'CH',0,'HZ','Drop Down','Cycles',1,0,to_date('16-JAN-08','DD-MON-RR'),to_date('16-JAN-08','DD-MON-RR'),4650,'MX0803_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3002,4650,'LBS',0,'TEXT',0,'LBS','Free Form Text','Proper Description',1,0,to_date('16-JAN-08','DD-MON-RR'),to_date('16-JAN-08','DD-MON-RR'),4650,'MX0803_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3003,4650,'QTS',0,'CHK',0,'QTS','Check Box','Quantity',1,0,to_date('16-JAN-08','DD-MON-RR'),to_date('16-JAN-08','DD-MON-RR'),4650,'MX0803_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3004,0,'DAY',0,'CME',0,'DAY','Date','The number of days',1,0,to_date('16-JAN-08','DD-MON-RR'),to_date('16-JAN-08','DD-MON-RR'),4650,'MX0803_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3005,null,null,0,'CH',2,'testCH',null,null,1,0,to_date('12-JUN-08','DD-MON-RR'),to_date('12-JUN-08','DD-MON-RR'),4650,'mxi');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,3006,null,null,0,'CME',2,'calCode',null,null,1,0,to_date('12-JUN-08','DD-MON-RR'),to_date('12-JUN-08','DD-MON-RR'),4650,'mxi');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,4,0,'CYCLES',0,'US',0,'ECYC','Equivalent Cycles','Equivalent Cycles',0,0,to_date('13-FEB-02','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,11,0,'CYCLES',0,'US',0,'B1','Thrust B1 Cycles','Thrust A Cycles',0,0,to_date('13-FEB-02','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,12,0,'CYCLES',0,'US',0,'B2','Thrust B2 Cycles','Thrust B Cycles',0,0,to_date('13-FEB-02','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,13,0,'CYCLES',0,'US',0,'C1','Thrust C1 Cycles','Thrust C Cycles',0,0,to_date('13-FEB-02','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,25,0,'CYCLES',0,'US',0,'TCSN','Total Engine Cycles','Engine Cycles',0,0,to_date('13-FEB-02','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (5001,2001,0,'CYCLES',0,'US',0,'TCal','Test Calculation',null,0,0,to_date('24-APR-07','DD-MON-RR'),to_date('24-APR-07','DD-MON-RR'),4650,'MX0706_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (9000100,1000,0,'HOUR',0,'US',0,'TSI','Time Since Install','Time Since Install',0,0,to_date('24-APR-01','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into MIM_DATA_TYPE (DATA_TYPE_DB_ID,DATA_TYPE_ID,ENG_UNIT_DB_ID,ENG_UNIT_CD,DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD,ENTRY_PREC_QT,DATA_TYPE_CD,DATA_TYPE_SDESC,DATA_TYPE_MDESC,FORECAST_BOOL,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (9000100,1001,0,'CYCLES',0,'US',0,'CSI','Cycles Since Install','Cycles Since Install',0,0,to_date('24-APR-01','DD-MON-RR'),to_date('16-JAN-07','DD-MON-RR'),4650,'MX0703_MASTER');


-- REM INSERTING into REF_ENG_UNIT
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'DAY',0,'TIME',0,0,63,'Days','Base unit of days, all other calendar units are factors of a day',0,1,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'HOUR',0,'TIME',0,0,63,'Hours','Hours fraction of a day',0,0.041667,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'MONTH',0,'TIME',0,0,63,'Months','Average calendar days in a month',0,30,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'WEEK',0,'TIME',0,0,63,'Weeks','Rounded calendar days in a week',0,7,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'YEAR',0,'TIME',0,0,63,'Years','Rounded calendar days in a year',0,365,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'CYCLES',0,'COUNT',0,0,60,'Cycles','Cycle returns to original state',0,1,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'LNDG',0,'COUNT',0,0,60,'Landings','Number of landing',0,1,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (0,'HOURS',0,'COUNT',0,0,60,'Operating Hours','Operating hours not based on calendar parameters',0,1,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (10,'COUNTS',0,'COUNT',0,0,60,'Unitless Count','Unit of unitary count',0,1,0,to_date('23-MAR-01','DD-MON-RR'),to_date('23-MAR-01','DD-MON-RR'),100,'MXI');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'AMPS',0,'CURRENT',0,0,111,'Amps','Amps',0,1,0,to_date('18-OCT-04','DD-MON-RR'),to_date('07-NOV-04','DD-MON-RR'),4650,'KRAMER_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'C',null,null,0,0,111,'Celsius',null,null,null,0,to_date('09-MAY-04','DD-MON-RR'),to_date('09-MAY-04','DD-MON-RR'),4650,'MASTER_MATRIX');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'CM',0,'LENGTH',0,0,111,'Centimeters','Centimeters',0,0.01,0,to_date('18-OCT-04','DD-MON-RR'),to_date('08-AUG-07','DD-MON-RR'),4650,'MX0709_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'EA',null,null,0,0,111,'Each',null,null,null,0,to_date('04-MAY-04','DD-MON-RR'),to_date('04-MAY-04','DD-MON-RR'),4650,'MASTER_MATRIX');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'GIGAWATZ',0,'POWER',0,0,111,'Gigawatts','Gigawatts',0,1,0,to_date('18-OCT-04','DD-MON-RR'),to_date('07-NOV-04','DD-MON-RR'),4650,'KRAMER_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'Hz',0,'FREQNCY',0,0,111,'Hertz','Hertz',0,1,0,to_date('18-OCT-04','DD-MON-RR'),to_date('07-NOV-04','DD-MON-RR'),4650,'KRAMER_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'LBS',0,'WEIGHT',0,0,111,'Pounds',null,null,0.45359237,0,to_date('04-MAY-04','DD-MON-RR'),to_date('08-AUG-07','DD-MON-RR'),4650,'MX0709_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'QTS',null,null,0,0,111,'Quarts',null,null,null,0,to_date('04-MAY-04','DD-MON-RR'),to_date('04-MAY-04','DD-MON-RR'),4650,'MASTER_MATRIX');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'TEST',0,'CURRENT',0,0,1,'Unit Test',null,0,1,2,to_date('20-JAN-05','DD-MON-RR'),to_date('20-JAN-05','DD-MON-RR'),4650,'KRAMER_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'V',0,'VOLTAGE',0,0,111,'Volts','Volts',0,1,0,to_date('18-OCT-04','DD-MON-RR'),to_date('07-NOV-04','DD-MON-RR'),4650,'KRAMER_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'kg',0,'WEIGHT',1,0,111,'Kilogram','Kilogram',0,1,0,to_date('08-AUG-07','DD-MON-RR'),to_date('08-AUG-07','DD-MON-RR'),4650,'MX0709_MASTER');
Insert into REF_ENG_UNIT (ENG_UNIT_DB_ID,ENG_UNIT_CD,REF_UNIT_DB_ID,REF_UNIT_CD,DEFAULT_BOOL,BITMAP_DB_ID,BITMAP_TAG,DESC_SDESC,DESC_LDESC,REF_OFFSET_QT,REF_MULT_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,'m',0,'LENGTH',1,0,111,'Meter','Meter',0,1,0,to_date('08-AUG-07','DD-MON-RR'),to_date('08-AUG-07','DD-MON-RR'),4650,'MX0709_MASTER');


-- REM INSERTING into FC_MODEL
Insert into FC_MODEL (MODEL_DB_ID,MODEL_ID,DESC_SDESC,DEFAULT_BOOL,AUTHORITY_DB_ID,AUTHORITY_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,'Boeing 767-232 - CR-JP',0,null,null,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_MODEL (MODEL_DB_ID,MODEL_ID,DESC_SDESC,DEFAULT_BOOL,AUTHORITY_DB_ID,AUTHORITY_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,'Boeing 767-232 - UNIT TEST',0,null,null,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_MODEL (MODEL_DB_ID,MODEL_ID,DESC_SDESC,DEFAULT_BOOL,AUTHORITY_DB_ID,AUTHORITY_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,'Boeing 767-232 - 13966',0,null,null,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_MODEL (MODEL_DB_ID,MODEL_ID,DESC_SDESC,DEFAULT_BOOL,AUTHORITY_DB_ID,AUTHORITY_ID,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,'Boeing 767-232 - DiagAdap1',0,null,null,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');


-- REM INSERTING into FC_RANGE
Insert into FC_RANGE (MODEL_DB_ID,MODEL_ID,RANGE_ID,START_MONTH,START_DAY,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,1,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RANGE (MODEL_DB_ID,MODEL_ID,RANGE_ID,START_MONTH,START_DAY,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,1,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RANGE (MODEL_DB_ID,MODEL_ID,RANGE_ID,START_MONTH,START_DAY,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,1,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RANGE (MODEL_DB_ID,MODEL_ID,RANGE_ID,START_MONTH,START_DAY,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,1,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');


-- REM INSERTING into FC_RATE
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,1,0,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,1,0,30,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,1,100,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100051,1,100,2,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,1,0,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,1,0,30,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,1,100,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100052,1,100,2,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,1,0,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,1,0,30,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,1,100,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100053,1,100,2,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,1,0,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,1,0,30,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,1,100,1,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');
Insert into FC_RATE (MODEL_DB_ID,MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID,DATA_TYPE_ID,FORECAST_RATE_QT,RSTAT_CD,CREATION_DT,REVISION_DT,REVISION_DB_ID,REVISION_USER)
values (4650,100054,1,100,2,1,0,to_date('28-DEC-06','DD-MON-RR'),to_date('28-DEC-06','DD-MON-RR'),4650,'MX0703_MASTER');

