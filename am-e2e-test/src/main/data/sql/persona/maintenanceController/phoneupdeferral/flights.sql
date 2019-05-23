-- Planned Flight PUD-DS-1 for Aircraft PUD-DS-1
INSERT
    INTO
        fl_leg(
            leg_id,
            leg_no,
            ext_key,
            hist_bool,
            flight_leg_status_cd,
            flight_type_db_id,
            etops_bool,
            aircraft_db_id,
            aircraft_id,
            departure_loc_db_id,
            departure_loc_id,
            sched_departure_dt,
            actual_departure_dt,
            arrival_loc_db_id,
            arrival_loc_id,
            sched_arrival_dt,
            actual_arrival_dt,
            recorded_dt,
            ctrl_db_id
        )
    SELECT
        (
            SELECT
                MX_KEY_PKG.new_uuid
            FROM
                dual
        ),
        'Planned Flight PUD-DS-1',
        'PlanFlight_PUD-DS-1',
        0,
        'MXPLAN',
        0,
        0,
        4650,
        (SELECT inv.inv_no_id FROM inv_inv inv INNER JOIN eqp_part_no epn ON inv.part_no_id = epn.part_no_id and inv.part_no_db_id = epn.part_no_db_id WHERE epn.PART_NO_OEM = 'ACFT_ASSY_MOC1' AND epn.MANUFACT_CD = '00001' AND inv.SERIAL_NO_OEM = 'PUD-DS-1'),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTA' AND LOC_NAME = 'MOC Airport A' AND CITY_NAME = 'City A'),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTB' AND LOC_NAME = 'MOC Airport B' AND CITY_NAME = 'City B'),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2016 9:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650
    FROM
        dual;

-- Attach Capabilities to Planned Flight PUD-DS-1
-- ======================================================
-- Autoland / CATI
-- Winglets (regular or Split Scimitar) MEL / SPLSCM
-- Second Observer Seat / YES

INSERT
	INTO
		fl_requirement(
			fl_leg_id,
			cap_db_id,
			cap_cd,
			level_db_id,
			level_cd
		)
	SELECT
		(SELECT LEG_ID FROM fl_leg WHERE leg_no ='Planned Flight PUD-DS-1' AND flight_leg_status_cd='MXPLAN'),
		10,
		'ALAND',
		10,
		'CATI'
	FROM
		dual;

INSERT
	INTO
		fl_requirement(
			fl_leg_id,
			cap_db_id,
			cap_cd,
			level_db_id,
			level_cd
		)
	SELECT
		(SELECT LEG_ID FROM fl_leg WHERE leg_no ='Planned Flight PUD-DS-1' AND flight_leg_status_cd='MXPLAN'),
		10,
		'WINGLET',
		10,
		'SPLSCM'
	FROM
		dual;

INSERT
	INTO
		fl_requirement(
			fl_leg_id,
			cap_db_id,
			cap_cd,
			level_db_id,
			level_cd
		)
	SELECT
		(SELECT LEG_ID FROM fl_leg WHERE leg_no ='Planned Flight PUD-DS-1' AND flight_leg_status_cd='MXPLAN'),
		10,
		'SECOBSVR',
		10,
		'YES'
	FROM
		dual;

-- Planned Flight PUD-AUTH-1 for Aircraft PUD-AUTH-1
INSERT
    INTO
        fl_leg(
            leg_id,
            leg_no,
            ext_key,
            hist_bool,
            flight_leg_status_cd,
            flight_type_db_id,
            etops_bool,
            aircraft_db_id,
            aircraft_id,
            departure_loc_db_id,
            departure_loc_id,
            sched_departure_dt,
            actual_departure_dt,
            arrival_loc_db_id,
            arrival_loc_id,
            sched_arrival_dt,
            actual_arrival_dt,
            recorded_dt,
            ctrl_db_id
        )
    SELECT
        (
            SELECT
                MX_KEY_PKG.new_uuid
            FROM
                dual
        ),
        'Planned Flight PUD-AUTH-1',
        'PlanFlight_PUD-AUTH-1',
        0,
        'MXPLAN',
        0,
        0,
        4650,
        (SELECT inv.inv_no_id FROM inv_inv inv INNER JOIN eqp_part_no epn ON inv.part_no_id = epn.part_no_id and inv.part_no_db_id = epn.part_no_db_id WHERE epn.PART_NO_OEM = 'ACFT_ASSY_MOC1' AND epn.MANUFACT_CD = '00001' AND inv.SERIAL_NO_OEM = 'PUD-AUTH-1'),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTA' AND LOC_NAME = 'MOC Airport A' AND CITY_NAME = 'City A'),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTB' AND LOC_NAME = 'MOC Airport B' AND CITY_NAME = 'City B'),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2016 9:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650
    FROM
        dual;

-- Planned Flight PUD-OPEN-1 for Aircraft PUD-OPEN-1
INSERT
    INTO
        fl_leg(
            leg_id,
            leg_no,
            ext_key,
            hist_bool,
            flight_leg_status_cd,
            flight_type_db_id,
            etops_bool,
            aircraft_db_id,
            aircraft_id,
            departure_loc_db_id,
            departure_loc_id,
            sched_departure_dt,
            actual_departure_dt,
            arrival_loc_db_id,
            arrival_loc_id,
            sched_arrival_dt,
            actual_arrival_dt,
            recorded_dt,
            ctrl_db_id
        )
    SELECT
        (
            SELECT
                MX_KEY_PKG.new_uuid
            FROM
                dual
        ),
        'Planned Flight PUD-OPEN-1',
        'PlanFlight_PUD-OPEN-1',
        0,
        'MXPLAN',
        0,
        0,
        4650,
        (SELECT inv.inv_no_id FROM inv_inv inv INNER JOIN eqp_part_no epn ON inv.part_no_id = epn.part_no_id and inv.part_no_db_id = epn.part_no_db_id WHERE epn.PART_NO_OEM = 'ACFT_ASSY_MOC1' AND epn.MANUFACT_CD = '00001' AND inv.SERIAL_NO_OEM = 'PUD-OPEN-1'),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTA' AND LOC_NAME = 'MOC Airport A' AND CITY_NAME = 'City A'),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 10:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650,
        (SELECT LOC_ID FROM inv_loc WHERE LOC_CD = 'AIRPORTB' AND LOC_NAME = 'MOC Airport B' AND CITY_NAME = 'City B'),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2017 11:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        TO_DATE( '10/12/2016 9:00:00', 'MM-DD-YYYY HH24:MI:SS' ),
        4650
    FROM
        dual;