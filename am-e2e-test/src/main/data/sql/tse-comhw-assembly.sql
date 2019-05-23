-- tools and equipment
INSERT INTO eqp_assmbl ( assmbl_db_id, assmbl_cd, assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag, assmbl_name, rstat_cd )
VALUES( 4650, 'TSE', 0, 'TSE', 0, 131, 'Tools and Service Equipment', 0 );

INSERT INTO eqp_assmbl_bom ( assmbl_db_id, assmbl_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd, pos_ct, assmbl_bom_cd, assmbl_bom_name, mandatory_bool, cfg_slot_status_db_id, cfg_slot_status_cd, rstat_cd )
VALUES( 4650, 'TSE', 0, 0, 'ROOT', 1, 'TSE', 'Tools and Service Equipment', 1, 0, 'ACTV', 0 );

INSERT INTO eqp_assmbl_pos ( assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, eqp_pos_cd, rstat_cd )
VALUES( 4650, 'TSE', 0, 1, 'TSE-POS-1', 0 );

-- common hardware
INSERT INTO eqp_assmbl ( assmbl_db_id, assmbl_cd, assmbl_class_db_id, assmbl_class_cd, bitmap_db_id, bitmap_tag, assmbl_name, rstat_cd )
VALUES( 4650, 'COMHW', 0, 'COMHW', 0, 19, 'Common Hardware', 0 );

INSERT INTO eqp_assmbl_bom ( assmbl_db_id, assmbl_cd, assmbl_bom_id, bom_class_db_id, bom_class_cd, pos_ct, assmbl_bom_cd, assmbl_bom_name, mandatory_bool, cfg_slot_status_db_id, cfg_slot_status_cd, rstat_cd )
VALUES(  4650,  'COMHW',  0,  0,  'ROOT',  1,  'Common HardwareCommon Hardware',  'Tools and Service Equipment',  1,  0,  'ACTV',  0 ); 

INSERT INTO eqp_assmbl_pos ( assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_pos_id, eqp_pos_cd, rstat_cd )
VALUES( 4650, 'COMHW', 0, 1, 'COMHW-POS-1', 0 );
