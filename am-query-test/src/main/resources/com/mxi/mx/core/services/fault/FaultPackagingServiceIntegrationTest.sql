INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, supply_loc_db_id, supply_loc_id, supply_bool) VALUES (4650, 200, 0, 'AIRPORT', 4650, 200, 1);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, supply_loc_db_id, supply_loc_id, supply_bool) VALUES (4650, 113, 0, 'AIRPORT', 4650, 113, 1);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd ) VALUES (4650, 300, 0, 'AIRPORT');

INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id)
	VALUES (4650, 400, 0, 'LINE', 4650, 200, 4650, 200);
INSERT INTO inv_loc (loc_db_id, loc_id, loc_type_db_id, loc_type_cd, nh_loc_db_id, nh_loc_id, supply_loc_db_id, supply_loc_id)
	VALUES (4650, 26, 0, 'LINE', 4650, 113, 4650, 113);

INSERT INTO ref_labour_skill (labour_skill_db_id, labour_skill_cd) VALUES (0, 'AET');
INSERT INTO ref_labour_skill (labour_skill_db_id, labour_skill_cd) VALUES (0, 'INSP');
