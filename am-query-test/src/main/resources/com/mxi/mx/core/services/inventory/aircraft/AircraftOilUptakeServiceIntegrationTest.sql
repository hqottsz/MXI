-- Baseline

-- Assemblies
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, rstat_cd)
	VALUES (4650, 'ACFT1', 0);
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, rstat_cd)
	VALUES (4650, 'ENG1', 0);

-- Config Slots
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_bom_cd, bom_class_db_id, bom_class_cd, mandatory_bool, rstat_cd)
	VALUES (4650, 'ACFT1', 0, '1-0-0', 0, 'ROOT', 1, 0);
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_bom_cd, bom_class_db_id, bom_class_cd, pos_ct, mandatory_bool, rstat_cd)
	VALUES (4650, 'ACFT1', 1, '1-1-0', 0, 'SUBASSY', 2, 1, 0);
INSERT INTO eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd, mandatory_bool, rstat_cd)
	VALUES (4650, 'ENG1', 0, 0, 'ROOT', 1, 0);

-- Config Slot Positions
INSERT INTO eqp_assmbl_pos (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, rstat_cd)
	VALUES (4650, 'ACFT1', 0, 1, 0);
INSERT INTO eqp_assmbl_pos (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, nh_assmbl_pos_id, rstat_cd)
	VALUES (4650, 'ACFT1', 1, 1, 4650, 'ACFT1', 0, 1, 0);
INSERT INTO eqp_assmbl_pos (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, nh_assmbl_db_id, nh_assmbl_cd, nh_assmbl_bom_id, nh_assmbl_pos_id, rstat_cd)
	VALUES (4650, 'ACFT1', 1, 2, 4650, 'ACFT1', 0, 1, 0);

-- Oil Consumption Rates
INSERT INTO eqp_assmbl_bom_oil (assmbl_db_id, assmbl_cd, oil_data_type_db_id, oil_data_type_id, time_data_type_id, rstat_cd)
	VALUES (4650, 'ENG1', 4650, 10004, 0, 0);
INSERT INTO mim_data_type (data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, domain_type_db_id, domain_type_cd, entry_prec_qt, data_type_cd, rstat_cd)
    VALUES (4650, 10004, 0, 'QT', 0, 'ME', 2, 'QUARTS', 0);

-- Parts
INSERT INTO eqp_part_no (part_no_db_id, part_no_id, inv_class_db_id, inv_class_cd, rstat_cd)
	VALUES (4650, 1, 0, 'ACFT', 0);
INSERT INTO eqp_part_no (part_no_db_id, part_no_id, inv_class_db_id, inv_class_cd, rstat_cd)
	VALUES (4650, 2, 0, 'ASSY', 0);

-- Part Groups
INSERT INTO eqp_bom_part (bom_part_db_id, bom_part_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, bom_part_cd, rstat_cd)
	VALUES (4650, 1, 4650, 'ACFT1', 0, 'PG-1', 0);
INSERT INTO eqp_bom_part (bom_part_db_id, bom_part_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, bom_part_cd, rstat_cd)
	VALUES (4650, 2, 4650, 'ACFT1', 1, 'PG-2', 0);

-- Data Type
INSERT INTO mim_data_type (data_type_db_id, data_type_id, entry_prec_qt, data_type_cd, alt_id, rstat_cd)
	VALUES (4650, 1, 2, 'TEST_VOL', 'ABCDEF0123456789ABCDEF0123456789', 0);

-- Locations
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 1, 0, 'AIRPORT', NULL, NULL, 4650, 1, 1);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 2, 0, 'LINE', 4650, 1, 4650, 1, 0);

-- Airport with company location marked as supply location
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 3, 0, 'AIRPORT', NULL, NULL, NULL, NULL, 0);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 4, 0, 'COMPANY', 4650, 3, 4650, 4, 1);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 5, 0, 'LINE', 4650, 4, 4650, 4, 0);

-- Airport with company location not marked as supply location
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 6, 0, 'AIRPORT', NULL, NULL, NULL, NULL, 0);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id, supply_bool)
	VALUES (4650, 7, 0, 'COMPANY', 4650, 6, NULL, NULL, 0);

-- Organzation
INSERT INTO org_org (org_db_id, org_id, org_sdesc)
	VALUES (4650, 1, 'Air Canada');
--Organization assigned to location (4650, 3)
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 4, 4650, 1);

-- Actual

-- Complete Aircraft at location (4650, 1)
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, loc_db_id, loc_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, inv_cond_db_id, inv_cond_cd, alt_id, complete_bool, rstat_cd)
	VALUES (4650, 1, 4650, 1, 0, 'ACFT', 4650, 1, 4650, 1, 4650, 1, 4650, 'ACFT1', 0, 1, 0, 'INSRV', '1234567890ABCDEF1234567890ABCDEF', 1, 0);
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, inv_oper_db_id, inv_oper_cd , rstat_cd) VALUES (4650, 1, 0, 'NORM', 0);

-- Complete Aircraft at location (4650, 3)
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, loc_db_id, loc_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, inv_cond_db_id, inv_cond_cd, alt_id, complete_bool, rstat_cd)
	VALUES (4650, 6, 4650, 6, 0, 'ACFT', 4650, 6, 4650, 3, 4650, 1, 4650, 'ACFT1', 0, 1, 0, 'INSRV', '1987654321ABCDEF1234567890ABCDEF', 1, 0);
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, inv_oper_db_id, inv_oper_cd , rstat_cd) VALUES (4650, 6, 0, 'NORM', 0);

-- Complete Aircraft at location (4650, 6)
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, loc_db_id, loc_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, inv_cond_db_id, inv_cond_cd, alt_id, complete_bool, rstat_cd)
	VALUES (4650, 7, 4650, 7, 0, 'ACFT', 4650, 7, 4650, 6, 4650, 1, 4650, 'ACFT1', 0, 1, 0, 'INSRV', '2987654321ABCDEF1234567890ABCDEF', 1, 0);
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, inv_oper_db_id, inv_oper_cd , rstat_cd) VALUES (4650, 7, 0, 'NORM', 0);

-- Incomplete Aircraft
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, loc_db_id, loc_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, inv_cond_db_id, inv_cond_cd, alt_id, complete_bool, rstat_cd)
	VALUES (4650, 4, 4650, 4, 0, 'ACFT', 4650, 4, 4650, 1, 4650, 1, 4650, 'ACFT1', 0, 1, 0, 'INREP', '0987654321ABCDEF1234567890ABCDEF', 0, 0);
INSERT INTO inv_ac_reg (inv_no_db_id, inv_no_id, inv_oper_db_id, inv_oper_cd, rstat_cd) VALUES (4650, 4, 0, 'NORM', 0);

-- Engines
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, alt_id, orig_assmbl_db_id, orig_assmbl_cd, loc_db_id, loc_id, rstat_cd)
	VALUES (4650, 2, 4650, 1, 0, 'ASSY', 4650, 1, 4650, 2, 4650, 'ACFT1', 1, 1, '2234567890ABCDEF1234567890ABCDEF', 4650, 'ENG1', 4650, 1, 0);

INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, alt_id, orig_assmbl_db_id, orig_assmbl_cd, loc_db_id, loc_id, rstat_cd)
	VALUES (4650, 3, 4650, 1, 0, 'ASSY', 4650, 1, 4650, 2, 4650, 'ACFT1', 1, 2, '3234567890ABCDEF1234567890ABCDEF', 4650, 'ENG1', 4650, 1, 0);

INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, alt_id, orig_assmbl_db_id, orig_assmbl_cd, loc_db_id, loc_id, rstat_cd)
	VALUES (4650, 5, 4650, 4, 0, 'ASSY', 4650, 4, 4650, 2, 4650, 'ACFT1', 1, 1, '4234567890ABCDEF1234567890ABCDEF', 4650, 'ENG1', 4650, 1, 0);

INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, alt_id, orig_assmbl_db_id, orig_assmbl_cd, loc_db_id, loc_id, rstat_cd)
	VALUES (4650, 8, 4650, 6, 0, 'ASSY', 4650, 8, 4650, 2, 4650, 'ACFT1', 1, 1, '5234567890ABCDEF1234567890ABCDEF', 4650, 'ENG1', 4650, 3, 0);

INSERT INTO inv_inv (inv_no_db_id, inv_no_id, h_inv_no_db_id, h_inv_no_id, inv_class_db_id, inv_class_cd, assmbl_inv_no_db_id, assmbl_inv_no_id, part_no_db_id, part_no_id, assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, alt_id, orig_assmbl_db_id, orig_assmbl_cd, loc_db_id, loc_id, rstat_cd)
	VALUES (4650, 9, 4650, 7, 0, 'ASSY', 4650, 9, 4650, 2, 4650, 'ACFT1', 1, 1, '6234567890ABCDEF1234567890ABCDEF', 4650, 'ENG1', 4650, 6, 0);