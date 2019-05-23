-- create a pending put away event
INSERT INTO 
	evt_event 
(
	event_db_id, 
	event_id, 
	event_type_db_id, 
	event_type_cd, 
	event_status_db_id, 
	event_status_cd, 
	h_event_db_id, 
	h_event_id, 
	event_sdesc, 
	hist_bool
)
VALUES
(
	4650, 
	event_id_seq.nextval, 
	0, 
	'LX', 
	0, 
	'LXPEND', 
	4650, 
	event_id_seq.currval, 
	'APENDPUTAWAY', 
	1
);

-- create an evt_inv record for the pending put away inventory
INSERT INTO 
	evt_inv 
( 
	event_db_id, 
	event_id, 
	event_inv_id, 
	inv_no_db_id, 
	inv_no_id, 
	nh_inv_no_db_id, 
	nh_inv_no_id, 
	assmbl_inv_no_db_id, 
	assmbl_inv_no_id, 
	h_inv_no_db_id, 
	h_inv_no_id, 
	assmbl_db_id, 
	assmbl_cd, 
	assmbl_bom_id, 
	assmbl_pos_id, 
	part_no_db_id, 
	part_no_id, 
	main_inv_bool
) 
VALUES 
(
  4650, 
  event_id_seq.currval,  
  1, 
  4650, 
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-4188'), 
  4650, 
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-4188'), 
  4650, 
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-4188'), 
  4650, 
  (SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-4188'), 
  4650, 
  'ACFT_CD1', 
  18, 
  2, 	
  4650,
  (SELECT part_no_id FROM eqp_part_no WHERE part_no_oem = 'PART4188'),
  1
);

-- create an from-location event
INSERT INTO 
	evt_loc 
(
	event_db_id, 
	event_id, 
	event_loc_id, 
	loc_db_id, 
	loc_id
)
VALUES
(
	4650, 
	event_id_seq.currval, 
	1, 
	4650, 
	(SELECT loc_id FROM inv_loc WHERE loc_type_cd = 'DOCK' AND loc_cd = 'AIRPORT1/DOCK')
);

-- create an to-location event
INSERT INTO 
	evt_loc 
(
	event_db_id, 
	event_id, 
	event_loc_id, 
	loc_db_id, 
	loc_id
)
VALUES
(
	4650, 
	event_id_seq.currval, 
	2, 
	4650, 
	(SELECT loc_id FROM inv_loc WHERE loc_type_cd = 'BIN' AND loc_cd = 'AIRPORT1/STORE/BIN1-1')
);

-- create a put away transfer
INSERT INTO 
	inv_xfer 
(
	xfer_db_id, 
	xfer_id, 
	xfer_type_db_id, 
	xfer_type_cd, 
	inv_no_db_id, 
	inv_no_id, 
	xfer_qt, 
	printed_bool
)
VALUES
(
	4650, 
	event_id_seq.currval, 
	0, 
	'PUTAWAY', 
	4650, 
	(SELECT inv_no_id FROM inv_inv WHERE serial_no_oem = 'OPER-4188'), 
	1, 
	1
);

-- create an inv_loc_bin record
INSERT INTO 
	inv_loc_bin 
(
	loc_db_id, 
	loc_id, 
	part_no_db_id, 
	part_no_id, 
	min_qt, 
	max_qt
)
VALUES
(
	4650, 
	(SELECT loc_id FROM inv_loc WHERE loc_type_cd = 'BIN' AND loc_cd = 'AIRPORT1/STORE/BIN1-1'), 
	4650, 
	(SELECT part_no_id FROM eqp_part_no WHERE part_no_oem = 'PART4188'), 
	0, 
	1
);	  