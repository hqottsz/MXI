delete from inv_loc where loc_db_id = 777;

delete from lrp_loc;

delete from ref_lrp_duration_mode;

insert into ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
       values ( 'MX_DEFINITION', 'DEFINITION', 'DEFINITION', 'Work package duration is calculated using the default durations configured on the block or requirement definition.', 1, 0, 1, 100, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, 'MXI');

insert into ref_lrp_duration_mode ( LRP_DURATION_MODE_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
       values ( 'MX_LOCATION', 'LOCATION', 'Location', 'Work package duration is calculated using the location capacity configured on the Capacity tab.', 2 , 0, 1, 100, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, TO_DATE('20-05-2014', 'DD-MM-YYYY'), 100, 'MXI');

insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 1, 0, 'REGION', 'CA', 'CANADA', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 2, 0, 'AIRPORT', 'CA/OTT', 'OTTAWA', 777, 1, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 3, 0, 'LINE', 'CA/OTT/LN', 'LINE', 777, 2, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 4, 0, 'TRACK', 'CA/OTT/LN/TRK', 'TRACK', 777, 3, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 5, 0, 'VENLINE', 'CA/OTT/LN/VLINE', 'VENDOR LINE', 777, 4, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, loc_cd, loc_name, nh_loc_db_id, nh_loc_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
       values (777, 6, 0, 'VENTRK', 'CA/OTT/LN/VLINE/VTRK', 'VENDOR TRACK', 777, 5, 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');


insert into lrp_loc (LRP_DB_ID, LRP_ID, LOC_DB_ID, LOC_ID, DEFAULT_CAPACITY, LABOR_RATE, AH_EVT_CTRL_BOOL, DURATION_MODE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
       values (777, 1, 777, 6, 1, 2, 1, 'MX_DEFINITION', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');

insert into eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name)
       values (777, 'ASSMBL_1', 'LRP TEST ASSEMBLY');

insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) values (777, 'HIGH');
insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) values (777, 'MED');
insert into ref_lrp_priority (lrp_priority_db_id, lrp_priority_cd) values (777, 'LOW');

insert into inv_loc_capability (loc_db_id, loc_id, assmbl_db_id, assmbl_cd, work_type_db_id, work_type_cd)
       values (777, 6, 777, 'ASSMBL_1', 0, 'TURN');

insert into lrp_loc_capability (loc_db_id, loc_id, assmbl_db_id, assmbl_cd, work_type_db_id, work_type_cd, lrp_db_id, lrp_id, lrp_priority_db_id, lrp_priority_cd)
       values (777, 6, 777, 'ASSMBL_1', 0, 'TURN', 777, 1, 777, 'MED');
