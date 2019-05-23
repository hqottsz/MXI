--liquibase formatted sql


--changeSet 0mim_data_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "MIM_DATA_TYPE"
** 0-Level
** DATE: 07-OCT-04 13:45:24
*********************************************/
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 1, 0, 'HOUR', 0, 'US', 'HOURS', 'Flying Hours', 'The time from the moment an aircraft leaves the surface until it comes in contact with the surface at the next point of landing.' , 2,  0, '27-JUN-01', '18-JAN-06', 100, 'MXI');

--changeSet 0mim_data_type:2 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 10, 0, 'CYCLES', 0, 'US', 'CYCLES', 'Cycles', NULL,  0, 0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:3 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 21, 0, 'DAY', 0, 'CA', 'CDY', 'Calendar Day', NULL, 0, 0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:4 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 22, 0, 'WEEK', 0, 'CA', 'CWK', 'Calendar Week', NULL, 2,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:5 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 23, 0, 'MONTH', 0, 'CA', 'CMON', 'Calendar Month', NULL, 2,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:6 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 24, 0, 'YEAR', 0, 'CA', 'CYR', 'Calendar Year', NULL, 3,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:7 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 25, 0, 'MONTH', 0, 'CA', 'CLMON', 'Calendar Last Day of Month', NULL,  2, 0, '18-APR-02', '18-APR-02', 100, 'MXI');

--changeSet 0mim_data_type:8 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 30, 0, 'LNDG', 0, 'US', 'LANDING', 'Aircraft Landings', NULL, 0,  0, '27-JUN-01', '27-JUN-01', 100, 'MXI');

--changeSet 0mim_data_type:9 stripComments:false
insert into mim_data_type(data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, data_type_cd, data_type_sdesc, data_type_mdesc, entry_prec_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 31, 0, 'HOUR', 0, 'CA', 'CHR', 'Calendar Hour', NULL, 0, 0, '08-MAY-06', '08-MAY-06', 100, 'MXI');