INSERT INTO org_carrier ( CARRIER_DB_ID, CARRIER_ID, CARRIER_CD, ORG_DB_ID, ORG_ID, IATA_CD, ICAO_CD, CALLSIGN_SDESC, AUTHORITY_DB_ID, AUTHORITY_ID, EXTRN_CTRL_BOOL, SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD )
VALUES( 0, 1001, 'MXI', 0, 1, 'MXI', 'MXI', null, null, null, 0, 0, 'DEFAULT' );
INSERT INTO org_carrier (carrier_db_id, carrier_id,carrier_cd, org_db_id, org_id, iata_cd, icao_cd,supply_chain_db_id, supply_chain_cd)
VALUES (0, (select max(carrier_id)+1 from ORG_CARRIER), 'ATLD', 0, 1, 'MXI', 'MXI',0, 'DEFAULT');