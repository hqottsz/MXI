-- Activate CAT_III, ETOPS
UPDATE ref_sensitivity SET active_bool = 0;
UPDATE ref_sensitivity SET active_bool = 1 WHERE sensitivity_cd IN ('CAT_III', 'ETOPS');

-- Assemblies
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'CONFIG' FROM dual;
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'PROP' FROM dual;

-- Sensitive System Settings for Assemblies
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'CONFIG', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'CONFIG', 'ETOPS' FROM dual;

INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'PROP', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'PROP', 'ETOPS' FROM dual;

-- Config Slots
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd) SELECT 4650, 'CONFIG', 'CONFIG', 0, 0, 'ROOT' FROM dual;

INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd, assmbl_bom_name)
	SELECT 4650, 'PROP', 'PROP-0', 0, 0, 'ROOT', 'Propagation' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', 'PROP-1', 1, 4650, 'PROP', 0, 0, 'SYS' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', 'PROP-2', 2, 4650, 'PROP', 1, 0, 'SYS' FROM dual;
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, bom_class_db_id, bom_class_cd)
	SELECT 4650, 'PROP', 'PROP-3', 3, 4650, 'PROP', 0, 0, 'SUBASSY' FROM dual;


-- Sensitive System Settings for Config Slots
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'CONFIG', 0, 'ETOPS' FROM dual;