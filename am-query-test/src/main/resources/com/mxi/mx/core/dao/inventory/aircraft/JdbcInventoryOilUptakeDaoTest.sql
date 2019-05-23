-- Single previous oil uptake data
-- Aircraft assembly
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag) VALUES (1, 'B767-200', 'Boeing 767-200', 0, 'ACFT', 0, 26);

-- Enging assembly
INSERT INTO eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag) VALUES (1, 'CFM56', 'CFM56-3C', 0, 'ENG', 0, 26);

-- Data type
INSERT INTO mim_data_type (data_type_db_id, data_type_id, eng_unit_db_id, eng_unit_cd, entry_prec_qt, alt_id) VALUES (0, 999999, 0, 'QT', 2, 'ABCDEF0123456789ABCDEF0123456789');

-- Aircraft inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, alt_id) VALUES (0, 905359, 0, 'LOCAL', 'I199999', 0, 1000, 0, 'INSRV', 0, 905359, 0, 1000, 0, 'ACFT', '8417120C7429487EAF69CD31AA2F17B1');

-- Engine inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, orig_assmbl_db_id, orig_assmbl_cd, inv_no_sdesc, alt_id) VALUES (0, 905360, 0, 'LOCAL', 'I299999', 0, 1000, 0, 'INSRV', 0, 905359, 0, 1000, 0, 'ASSY', 1, 'CFM56', 'Engine 1', '7417120C7429487EAF69CD31AA2F17B1');

INSERT INTO eqp_assmbl_bom_oil (assmbl_db_id, assmbl_cd, oil_data_type_db_id, oil_data_type_id, time_data_type_db_id, time_data_type_id) VALUES(1, 'CFM56', 0, 999999, 0, 1);

-- oil uptake # 1
INSERT INTO inv_parm_data (event_db_id, event_id, inv_no_db_id, inv_no_id, event_inv_id, data_type_db_id, data_type_id, parm_qt, creation_dt) VALUES(0, 905360, 0, 905360, 1, 0, 999999, 2.4, SYSDATE-1 );

-- oil uptake # 2
INSERT INTO inv_parm_data (event_db_id, event_id, inv_no_db_id, inv_no_id, event_inv_id, data_type_db_id, data_type_id, parm_qt, creation_dt) VALUES(0, 905361, 0, 905360, 1, 0, 999999, 2.5, SYSDATE );


-- With no ASSY inventory and no previous oil uptake data
-- Aircraft inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, alt_id) VALUES (0, 905319, 0, 'LOCAL', 'I199991', 0, 1000, 0, 'INSRV', 0, 905359, 0, 1000, 0, 'ACFT', '8417120C7429487EAF69CD31AA2F17B2');


-- Multiple previous oil uptake data
-- Aircraft inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, alt_id) VALUES (0, 905329, 0, 'LOCAL', 'I199992', 0, 1000, 0, 'INSRV', 0, 905329, 0, 1000, 0, 'ACFT', '8417120C7429487EAF69CD31AA2F17B3');

-- Engine 1 inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, orig_assmbl_db_id, orig_assmbl_cd, inv_no_sdesc, alt_id) VALUES (0, 905330, 0, 'LOCAL', 'I299992', 0, 1000, 0, 'INSRV', 0, 905329, 0, 1000, 0, 'ASSY', 1, 'CFM56', 'Engine 2', '7417120C7429487EAF69CD31AA2F17B2');

-- Engine 2 inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, orig_assmbl_db_id, orig_assmbl_cd, inv_no_sdesc, alt_id) VALUES (0, 905331, 0, 'LOCAL', 'I299993', 0, 1000, 0, 'INSRV', 0, 905329, 0, 1000, 0, 'ASSY', 1, 'CFM56', 'Engine 3', '7417120C7429487EAF69CD31AA2F17B3');

-- oil uptake
INSERT INTO inv_parm_data (event_db_id, event_id, inv_no_db_id, inv_no_id, event_inv_id, data_type_db_id, data_type_id, parm_qt) VALUES(0, 905330, 0, 905330, 1, 0, 999999, 5.64);

INSERT INTO inv_parm_data (event_db_id, event_id, inv_no_db_id, inv_no_id, event_inv_id, data_type_db_id, data_type_id, parm_qt) VALUES(0, 905331, 0, 905331, 1, 0, 999999, 8.19);


-- Single inventory, no previous oil uptake data
-- Aircraft inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, alt_id) VALUES (0, 905339, 0, 'LOCAL', 'I199993', 0, 1000, 0, 'INSRV', 0, 905339, 0, 1000, 0, 'ACFT', '8417120C7429487EAF69CD31AA2F17B4');

-- Engine inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, owner_type_db_id, owner_type_cd, barcode_sdesc, owner_db_id, owner_id, inv_cond_db_id, inv_cond_cd, h_inv_no_db_id, h_inv_no_id, loc_db_id, loc_id, inv_class_db_id, inv_class_cd, orig_assmbl_db_id, orig_assmbl_cd, inv_no_sdesc, alt_id) VALUES (0, 905340, 0, 'LOCAL', 'I299993', 0, 1000, 0, 'INSRV', 0, 905339, 0, 1000, 0, 'ASSY', 1, 'CFM56', 'Engine 4', '7417120C7429487EAF69CD31AA2F17B4');
