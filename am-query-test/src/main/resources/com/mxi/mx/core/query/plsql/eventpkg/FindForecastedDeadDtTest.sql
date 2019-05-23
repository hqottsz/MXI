-- =======================================================================================================================================
-- Date for Test Cases #1-6
-- =======================================================================================================================================
-- FC_Range
insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (4650, 100000, 1,1,1,0);

-- FC_Rate for Hours
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100000, 1,0,1,1);

-- FC_Rate for Cycles
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100000, 1,0,10,1);

-- Evt_inv
insert into EVT_INV  (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID)
values (4650,300000,200000,4650,10737);
insert into EVT_INV  (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID)
values (4650,300001,200001,4650,10737);
insert into EVT_INV  (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID)
values (4650,300002,200002,4650,10737);

-- Evt_Event
insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_cd, event_sdesc,ACTUAL_START_DT, EVENT_DT)
values (4650, 300000, 0, 'BLK', 'ACTV', 'AUTOEVENT',trunc(sysdate+85), trunc(sysdate+87));
insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_cd, event_sdesc,ACTUAL_START_DT, EVENT_DT)
values (4650, 300001, 0, 'BLK', 'ACTV', 'AUTOEVENT',trunc(sysdate+88), trunc(sysdate+95));
insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_cd, event_sdesc,ACTUAL_START_DT, EVENT_DT)
values (4650, 300002, 0, 'BLK', 'ACTV', 'AUTOEVENT',trunc(sysdate+91), trunc(sysdate+93));
-- inv_ac_reg
insert into INV_AC_REG (INV_NO_DB_ID, INV_NO_ID, INV_OPER_DB_ID, INV_OPER_CD, REG_BODY_DB_ID, REG_BODY_CD, INV_CAPABILITY_DB_ID, INV_CAPABILITY_CD, COUNTRY_DB_ID, COUNTRY_CD, FORECAST_MODEL_DB_ID, FORECAST_MODEL_ID, AC_REG_CD, AIRWORTH_CD, PRIVATE_BOOL, PREVENT_LPA_BOOL, ISSUE_ACCOUNT_DB_ID, ISSUE_ACCOUNT_ID, VAR_NO_OEM, LINE_NO_OEM, FIN_NO_CD, INV_OPER_CHANGE_REASON, ETOPS_BOOL, RSTAT_CD)
values (4650, 10737, 0, 'NORM', null, null, 10, 'NORM', null, null, 4650, 100000, '001', null, 0, 1, 4650, 100003, null, null, null, null, 0, 0);

-- =======================================================================================================================================
-- Date for Test Cases #7-8
-- =======================================================================================================================================
-- FC_Range
insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (4650, 100001, 1,1,1,0);
insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (4650, 100001, 2,5,1,0);
insert into fc_range (MODEL_DB_ID, MODEL_ID,RANGE_ID, START_MONTH, START_DAY, RSTAT_CD) values (4650, 100001, 3,9,1,0);

-- FC_Rate for Hours
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 1,0,1,1);
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 2,0,1,3);
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 3,0,1,2);

-- FC_Rate for Cycles
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 1,0,10,2);
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 2,0,10,6);
insert into fc_rate (MODEL_DB_ID, MODEL_ID,RANGE_ID,DATA_TYPE_DB_ID, DATA_TYPE_ID,FORECAST_RATE_QT) values (4650, 100001, 3,0,10,4);

-- evt_inv
insert into EVT_INV  (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID)
values (4650,300003,200003,4650,10747);
insert into EVT_INV  (EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID)
values (4650,300004,200003,4650,10747);

-- Evt_Event
insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_cd, event_sdesc,ACTUAL_START_DT, EVENT_DT)
values (4650, 300003, 0, 'BLK', 'ACTV', 'AUTOEVENT',to_date('2018-09-01', 'YYYY-MM-DD'), to_date('2018-09-30', 'YYYY-MM-DD'));
insert into evt_event (event_db_id, event_id, event_type_db_id, event_type_cd, event_status_cd, event_sdesc,ACTUAL_START_DT, EVENT_DT)
values (4650, 300004, 0, 'BLK', 'ACTV', 'AUTOEVENT',to_date('2020-09-01', 'YYYY-MM-DD'), to_date('2020-09-30', 'YYYY-MM-DD'));

-- inv_ac_reg
insert into INV_AC_REG (INV_NO_DB_ID, INV_NO_ID, INV_OPER_DB_ID, INV_OPER_CD, REG_BODY_DB_ID, REG_BODY_CD, INV_CAPABILITY_DB_ID, INV_CAPABILITY_CD, COUNTRY_DB_ID, COUNTRY_CD, FORECAST_MODEL_DB_ID, FORECAST_MODEL_ID, AC_REG_CD, AIRWORTH_CD, PRIVATE_BOOL, PREVENT_LPA_BOOL, ISSUE_ACCOUNT_DB_ID, ISSUE_ACCOUNT_ID, VAR_NO_OEM, LINE_NO_OEM, FIN_NO_CD, INV_OPER_CHANGE_REASON, ETOPS_BOOL, RSTAT_CD)
values (4650, 10747, 0, 'NORM', null, null, 10, 'NORM', null, null, 4650, 100001, '001', null, 0, 1, 4650, 100003, null, null, null, null, 0, 0);
