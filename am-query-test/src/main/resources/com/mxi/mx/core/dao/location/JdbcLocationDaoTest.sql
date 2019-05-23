--Airport Locations
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 0, null, null, 0, 'AIRPORT');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 1, 4650, 0, 0, 'AIRPORT');

--Airport location marked as a supply location
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 9, 4650, 9, 1, null, null, 0, 'AIRPORT');
--Airport location with no children locations
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 17, null, null, 0, null, null, 0, 'AIRPORT');
--Airport location not marked as a supply location
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 18, null, null, 0, null, null, 0, 'AIRPORT');
--Airport location with child company locations that are marked as supply locations
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 21, null, null, 0, null, null, 0, 'AIRPORT');


--Company Locations

--Company locations under airport (4650, 18)
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 19, null, null, 0, 4650, 18, 0, 'COMPANY');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 20, null, null, 0, 4650, 18, 0, 'COMPANY');

-- Company locations under airport (4650, 21)
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 22, 4650, 22, 1, 4650, 21, 0, 'COMPANY');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 23, 4650, 23, 1, 4650, 21, 0, 'COMPANY');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 24, null, null, 0, 4650, 21, 0, 'COMPANY');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 25, 4650, 25, 1, 4650, 21, 0, 'COMPANY');


--Lines Locations

--Line locations under Airport (4650, 1)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 2, 4650, 1, 0, 'LINE');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 3, 4650, 1, 0, 'LINE');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 4, 4650, 1, 0, 'LINE');

--Line Location under Airport supply location (4650, 9)
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 10, 4650, 9, 0, 4650, 9, 0, 'LINE');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 11, 4650, 9, 0, 4650, 9, 0, 'LINE');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 12, 4650, 9, 0, 4650, 9, 0, 'LINE');


--Hangar Locations

--Hangar location under Aiport (4650, 1)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 5, 4650, 1, 0, 'HGR');
--Hangar location under Airport supply location (4650, 9)
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 13, 4650, 9, 0, 4650, 9, 0, 'HGR');


--Track Locations

--Track locations under Hanger location (4650, 5)
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 6, 4650, 5, 0, 'TRACK');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 7, 4650, 5, 0, 'TRACK');
INSERT INTO inv_loc (loc_db_id, loc_id, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 8, 4650, 5, 0, 'TRACK');

--Track locations under Hanger location (4650, 13)
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 14, 4650, 9, 0, 4650, 13,  0, 'TRACK');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 15, 4650, 9, 0, 4650, 13, 0, 'TRACK');
INSERT INTO inv_loc (loc_db_id, loc_id, supply_loc_db_id, supply_loc_id, supply_bool, nh_loc_db_id, nh_loc_id, loc_type_db_id, loc_type_cd)
	VALUES (4650, 16, 4650, 9, 0, 4650, 13, 0, 'TRACK');


--Organizations
INSERT INTO org_org (org_db_id, org_id, org_sdesc)
	VALUES (4650, 2000, 'Air Canada');
INSERT INTO org_org (org_db_id, org_id, org_sdesc)
	VALUES (4650, 2001, 'SouthWest');
INSERT INTO org_org (org_db_id, org_id, org_sdesc)
	VALUES (4650, 2002, 'Delta');


--Carriers
INSERT INTO org_carrier (carrier_db_id, carrier_id, carrier_cd, org_db_id, org_id)
	VALUES (4650, 3000, 'AC-ACA', 4650, 2000);
INSERT INTO org_carrier (carrier_db_id, carrier_id, carrier_cd, org_db_id, org_id)
	VALUES (4650, 3001, 'WN-SWA', 4650, 2001);
INSERT INTO org_carrier (carrier_db_id, carrier_id, carrier_cd, org_db_id, org_id)
	VALUES (4650, 3002, 'DL-DAL', 4650, 2002);


--Organizations assigned to locations
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 23, 4650, 2000);
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 23, 4650, 2001);
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 24, 4650, 2000);
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 24, 4650, 2001);
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 25, 4650, 2000);
INSERT INTO inv_loc_org (loc_db_id, loc_id, org_db_id, org_id)
	VALUES (4650, 25, 4650, 2002);


-- Aircraft Inventory
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, carrier_db_id, carrier_id, inv_class_db_id, inv_class_cd, loc_db_id, loc_id, alt_id)
	VALUES (4650, 1000, 4650, 3000, 0, 'ACFT', 4650, 21, '1234567890ABCDEF1234567890ABCDEF');
INSERT INTO inv_inv (inv_no_db_id, inv_no_id, carrier_db_id, carrier_id, inv_class_db_id, inv_class_cd, loc_db_id, loc_id, alt_id)
	VALUES (4650, 1001, 4650, 3001, 0, 'ACFT', 4650, 21, '1234567891ABCDEF1234567891ABCDEF');
