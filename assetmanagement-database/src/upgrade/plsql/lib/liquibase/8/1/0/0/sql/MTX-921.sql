--liquibase formatted sql


--changeSet MTX-921:1 stripComments:false
--
-- Add entry to utl_trigger
--
-- MTX-872
-- Flight Name TRIGGER needed when creating Flight Record
-- 
INSERT INTO 
	utl_trigger(
	trigger_id,
	trigger_cd,
	exec_order,
	type_cd,
	trigger_name,
	class_name,
	active_bool,
	utl_id
	)
SELECT
	99937,
	'MX_FLIGHT_GENERATE_NAME',
	1,
	'xxx',
	'Flight Leg Name Generator',
	'com.mxi.mx.web.trigger.flight.DefaultFlightLegNameGenerator',
	1,
	0
FROM
	dual
WHERE
	NOT EXISTS
	(
	SELECT *
	FROM
		utl_trigger
	WHERE
		trigger_id = 99937 AND
		trigger_cd = 'MX_FLIGHT_GENERATE_NAME'
	);