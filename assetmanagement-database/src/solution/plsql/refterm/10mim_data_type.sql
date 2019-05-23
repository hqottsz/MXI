/********************************************
** INSERT SCRIPT FOR TABLE "MIM_DATA_TYPE"
** 10-Level
** DATE: 07-OCT-04 13:45:24
*********************************************/
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 5, 10, 'COUNTS', 0, 'US', 'FDAY', 'Flight Days', NULL, 0,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 25, 0, 'HOUR', 0, 'US', 'EOT', 'Engine Operating Time', NULL, 2, 0, '27-JUN-01', '27-JUN-01', 100, 'MXI');
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 26, 0, 'HOUR', 0, 'US', 'AOT', 'APU Operating Time ', NULL, 2,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 27, 0, 'CYCLES', 0, 'US', 'ECYC', 'Engine Cycles', 'Engine Cycles', 0,  0, '07-OCT-04', '07-OCT-04', 100, 'MXI');
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (10, 28, 0, 'CYCLES', 0, 'US', 'ACYC', 'APU Cycles', 'Engine Cycles', 0,  0, '07-OCT-04', '07-OCT-04', 100, 'MXI');


