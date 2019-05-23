-- Disable all
UPDATE ref_sensitivity SET active_bool = 0;

-- Enable 3 sensitive systems
UPDATE ref_sensitivity SET active_bool = 1 WHERE sensitivity_cd IN ('CAT_III', 'ETOPS', 'RII');

-- ASSEMBLIES
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'TEST1' FROM dual;
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'PROP' FROM dual;

-- ASSEMBLY SENSITIVE SYSTEMS
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST1', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST1', 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST1', 'RII' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST1', 'RVSM' FROM dual;

INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'PROP', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'PROP', 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'PROP', 'RII' FROM dual;

-- CONFIG SLOTS
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'TEST1', 'TEST1', 0 FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'TEST1', 'TEST1-1', 1 FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id) SELECT 4650, 'TEST1', 'TEST1-2', 2 FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd) SELECT 4650, 'TEST1', 'TEST1-3', 3, 0, 'SYS' FROM dual;

-- CONFIG SLOT TREE FOR PROPAGATION TESTS
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', 'PROP-0', 0, 0, 'ROOT' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', '61', 1, 4650, 'PROP', 0, 0, 'SYS' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', '61-10', 2, 4650, 'PROP', 1, 0, 'SYS' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', '61-10-05', 3, 4650, 'PROP', 2, 0, 'SYS' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', '61-20', 4, 4650, 'PROP', 1, 0, 'SYS' FROM dual;


-- CONFIG SLOT SENSITIVE SYSTEMS
-- 3 CAT_III, 2 ETOPS, 1 RII, 1 RVSM (deactivated)
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 1, 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 1, 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 1, 'RII' FROM dual;

INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 2, 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 2, 'ETOPS' FROM dual;

INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 3, 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'TEST1', 3, 'RVSM' FROM dual;

-- Sensitive system settings for propagation testing
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'PROP', 2, 'CAT_III' FROM dual;

INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'PROP', 3, 'ETOPS' FROM dual;

INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd)
	SELECT 4650, 'PROP', 4, 'RII' FROM dual;