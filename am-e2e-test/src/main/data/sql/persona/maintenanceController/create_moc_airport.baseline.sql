-- Create LINE location for airports.
 INSERT
    INTO
        INV_LOC(
            LOC_DB_ID,
            LOC_ID,
            LOC_TYPE_DB_ID,
            LOC_TYPE_CD,
            SUPPLY_LOC_DB_ID,
            SUPPLY_LOC_ID,
            SUPPLY_BOOL,
            DEFAULT_DOCK_BOOL,
            COUNTRY_DB_ID,
            COUNTRY_CD,
            STATE_CD,
            INBOUND_FLIGHTS_QT,
            LOC_CD,
            LOC_NAME,
            CITY_NAME,
            TIMEZONE_CD
        )
    SELECT
        4650,
        INV_LOC_ID.nextval,
        0,
        'LINE',
        4650,
        INV_LOC_ID.currval,
        1,
        0,
        10,
        'USA',
        'HI',
        100,
        'MOC LINE',
        'MOC Line Maintenance',
        'City C',
        'America/Fort_Wayne'
    FROM
        dual;

-- Create airports.

 INSERT
    INTO
        INV_LOC(
        	LOC_DB_ID,
        	LOC_ID,
            LOC_TYPE_DB_ID,
            LOC_TYPE_CD,
            SUPPLY_LOC_DB_ID,
            SUPPLY_LOC_ID,
            SUPPLY_BOOL,
            DEFAULT_DOCK_BOOL,
            COUNTRY_DB_ID,
            COUNTRY_CD,
            STATE_CD,
            INBOUND_FLIGHTS_QT,
            LOC_CD,
            LOC_NAME,
            CITY_NAME,
            TIMEZONE_CD
        )
    SELECT
    	4650,
    	INV_LOC_ID.nextval,
        0,
        'AIRPORT',
        (SELECT loc_db_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        (SELECT loc_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        1,
        0,
        10,
        'USA',
        'HI',
        100,
        'AIRPORTA',
        'MOC Airport A',
        'City A',
        'America/Fort_Wayne'
    FROM
        dual;

INSERT
    INTO
        INV_LOC(
        	LOC_DB_ID,
        	LOC_ID,
            LOC_TYPE_DB_ID,
            LOC_TYPE_CD,
            SUPPLY_LOC_DB_ID,
            SUPPLY_LOC_ID,
            SUPPLY_BOOL,
            DEFAULT_DOCK_BOOL,
            COUNTRY_DB_ID,
            COUNTRY_CD,
            STATE_CD,
            INBOUND_FLIGHTS_QT,
            LOC_CD,
            LOC_NAME,
            CITY_NAME,
            TIMEZONE_CD
        )
    SELECT
   		4650,
    	INV_LOC_ID.nextval,
        0,
        'AIRPORT',
        (SELECT loc_db_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        (SELECT loc_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        1,
        0,
        10,
        'USA',
        'HI',
        100,
        'AIRPORTB',
        'MOC Airport B',
        'City B',
        'America/Fort_Wayne'
    FROM
        dual;
 INSERT
    INTO
        INV_LOC(
        	LOC_DB_ID,
        	LOC_ID,
            LOC_TYPE_DB_ID,
            LOC_TYPE_CD,
            SUPPLY_LOC_DB_ID,
            SUPPLY_LOC_ID,
            SUPPLY_BOOL,
            DEFAULT_DOCK_BOOL,
            COUNTRY_DB_ID,
            COUNTRY_CD,
            STATE_CD,
            INBOUND_FLIGHTS_QT,
            LOC_CD,
            LOC_NAME,
            CITY_NAME,
            TIMEZONE_CD
        )
    SELECT
   		4650,
    	INV_LOC_ID.nextval,
        0,
        'AIRPORT',
        (SELECT loc_db_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        (SELECT loc_id FROM INV_LOC WHERE LOC_TYPE_DB_ID = 0 AND LOC_TYPE_CD = 'LINE' AND LOC_CD = 'MOC LINE'),
        1,
        0,
        10,
        'USA',
        'HI',
        100,
        'AIRPORTC',
        'MOC Airport C',
        'City C',
        'America/Fort_Wayne'
    FROM
        dual;
