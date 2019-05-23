-- Activate CAT_III and ETOPS sensitivities
-- Activate CAT_III, ETOPS, RII sensitivities
DELETE FROM ref_sensitivity;
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool) VALUES ('CAT_III', 'CAT III', 'Desc', 1, 'Warning', 1);
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool) VALUES ('ETOPS', 'ETOPS', 'Desc', 2, 'Warning', 1);
INSERT INTO ref_sensitivity (sensitivity_cd, desc_sdesc, desc_ldesc, ord_id, warning_ldesc, active_bool) VALUES ('RVSM', 'RVSM', 'Desc', 3, 'Warning', 0);

-- Set up Assembly ASSY with assigned sensitivities CAT_III (active) RVSM (inactive)
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd) VALUES (4650, 'ASSY');
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) VALUES (4650, 'ASSY', 'CAT_III');
INSERT INTO eqp_assmbl_sens (assmbl_db_id, assmbl_cd, sensitivity_cd) VALUES (4650, 'ASSY', 'RVSM');
