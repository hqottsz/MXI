DELETE FROM ref_sensitivity;
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, ord_id, warning_ldesc, active_bool) SELECT 'CAT_III', 'CAT III', 10, 'This system is CAT III compliance sensitive - the aircraft may require recertification.', 1 FROM dual;

-- ASSEMBLIES
INSERT INTO eqp_assmbl ( assmbl_db_id, assmbl_cd ) SELECT 4650, 'ENABLED' FROM dual;
INSERT INTO eqp_assmbl ( assmbl_db_id, assmbl_cd ) SELECT 4650, 'DISABLED' FROM dual;

-- Sensitive system assembly applicability
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'ENABLED', 'CAT_III' FROM dual;

-- CONFIG SLOTS
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'ENABLED', 'TEST1', 0 FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'ENABLED', 'TEST1-1', 1 FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'DISABLED', 'TEST1', 0 FROM dual;
