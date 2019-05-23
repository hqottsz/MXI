INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES ( 4650, 100000, 10, 'COUNTS', 0, 'US', 1, 'USAGE1', 'Usage1' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES ( 4650, 100001, 10, 'COUNTS', 0, 'US', 1, 'USAGE2', 'Usage2' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES( 4650, 100002, 10, 'COUNTS', 0, 'US', 1, 'USAGE3', 'Usage3' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES( 4650, 100003, 0, 'HOURS', 0, 'US', 2, 'APUH_AT_READING', 'APUH_AT_READING' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES ( 4650, 100004, 10, 'COUNTS', 0, 'ME', 1, 'MEASUREMENT1', 'Measurement1' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc )
VALUES( 4650, 100005, 10, 'COUNTS', 0, 'ME', 1, 'MEASUREMENT2', 'Measurement2' );

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc, data_type_mdesc, rstat_cd )
VALUES( 4650, 1000, 0, 'DAY', 0, 'CH', 1, 'TSIG', 'Take Off Signal', 'TSIG description', 0);

INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc, data_type_mdesc, rstat_cd )
VALUES( 4650, 1001, 10, 'COUNTS', 0, 'CME', 2, 'CHAR', 'Character Measurement', 'Character Measurement Test', 0 );

-- Uncomment after  OPER-31298 --
--INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc, data_type_mdesc, rstat_cd )
--VALUES(4650, 3003, null, null, 0, 'CHK', 0, 'QTS', 'Check Box', 'Check Box Test', 0 );

--INSERT INTO mim_data_type ( data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, data_type_sdesc, data_type_mdesc, rstat_cd )
--VALUES( 4650, 3004, 0, 'DAY', 0, 'CME', 0, 'DAY', 'Date', 'Number of days', 0 );