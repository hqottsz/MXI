-- CONFIG SLOTS
-- Valid root slot
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd) SELECT 4650, 'TEST', 'TEST', 0, 0, 'SYS' FROM dual;
-- Tracked child slot
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd) SELECT 4650, 'TEST', 'TEST-1', 1, 0, 'TRK' FROM dual;

-- Assemblies
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) SELECT 4650, 'TEST' FROM dual;

-- Assembly Sensitive Systems
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'RVSM' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'RII' FROM dual;
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) SELECT 4650, 'TEST', 'FCBS' FROM dual;


-- CONFIG SLOT SENSITIVE SYSTEMS
-- 3 CAT_III, 2 ETOPS, 1 RII, 1 RVSM (deactivated)
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'TEST', 0, 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'TEST', 0, 'ETOPS' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'TEST', 0, 'RVSM' FROM dual;

INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'TEST', 1, 'CAT_III' FROM dual;
INSERT INTO eqp_assmbl_bom_sens (assmbl_db_id, assmbl_cd, assmbl_bom_id, sensitivity_cd) SELECT 4650, 'TEST', 1, 'ETOPS' FROM dual;

-- Activate 3 sensitive systems
UPDATE ref_sensitivity SET active_bool = 0;
UPDATE ref_sensitivity SET active_bool = 1 WHERE sensitivity_cd IN ('CAT_III', 'ETOPS', 'RVSM');