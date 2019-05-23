-- Set up 5 sensitive systems
DELETE FROM ref_sensitivity;
INSERT INTO ref_sensitivity (sensitivity_cd, active_bool, desc_sdesc, ord_id, warning_ldesc) SELECT 'CAT_III', 1, 'CAT III', 10, 'This system is CAT III compliance sensitive - the aircraft may require recertification.'  FROM dual;
INSERT INTO ref_sensitivity (sensitivity_cd, active_bool, desc_sdesc, ord_id, warning_ldesc) SELECT 'ETOPS', 1, 'ETOPS', 20, 'This system is ETOPS compliance sensitive - the aircraft may require recertification.' FROM dual;
INSERT INTO ref_sensitivity (sensitivity_cd, active_bool, desc_sdesc, ord_id, warning_ldesc) SELECT 'RII', 1, 'RII', 30, 'This system is RII compliance sensitive - the aircraft may require recertification.' FROM dual;
INSERT INTO ref_sensitivity (sensitivity_cd, active_bool, desc_sdesc, ord_id, warning_ldesc) SELECT 'RVSM', 0, 'RVSM', 40, 'This system is RVSM compliance sensitive - the aircraft may require recertification.' FROM dual;
INSERT INTO ref_sensitivity (sensitivity_cd, active_bool, desc_sdesc, ord_id, warning_ldesc) SELECT 'FCBS', 0, 'FCBS', 50, 'This system is FCBS compliance sensitive - the aircraft may require recertification.' FROM dual;

-- Create an assembly
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'TEST' FROM dual;
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'NOSENS' FROM dual;

-- Enable 3 sensitive systems for assembly: 2 that are globally active 1 that is not
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'RVSM' FROM dual;