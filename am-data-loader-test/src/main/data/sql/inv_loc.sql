insert into INV_LOC (loc_db_id, loc_id, loc_type_db_id, loc_type_cd,  supply_bool, default_dock_bool, no_hub_auto_rsrv_bool, track_capacity_qt, inbound_flights_qt, loc_cd, loc_name, timezone_cd,  rstat_cd)
values (0, (SELECT MAX(LOC_ID)+1  FROM inv_loc), 0, 'ARCHIVE', 0, 0, 0, 0,0, 'ATARC','ATARC','America/New_York', 0);
insert into INV_LOC (loc_db_id, loc_id, loc_type_db_id, loc_type_cd,  supply_bool, default_dock_bool, no_hub_auto_rsrv_bool, track_capacity_qt, inbound_flights_qt, loc_cd, loc_name, timezone_cd,  rstat_cd)
values (0, (SELECT MAX(LOC_ID)+1  FROM inv_loc), 0, 'SCRAP', 0, 0, 0, 0,0, 'ATSCP','ATSCP','America/New_York', 0);
insert into INV_LOC (loc_db_id, loc_id, loc_type_db_id, loc_type_cd,  supply_bool, default_dock_bool, no_hub_auto_rsrv_bool, track_capacity_qt, inbound_flights_qt, loc_cd, loc_name, timezone_cd,  rstat_cd)
values (0, (SELECT MAX(LOC_ID)+1  FROM inv_loc), 0, 'ARCHIVE', 0, 0, 0, 0,0, 'ATDUP','ATDUPLICATE','America/New_York', 0);
insert into INV_LOC (loc_db_id, loc_id, loc_type_db_id, loc_type_cd,  supply_bool, default_dock_bool, no_hub_auto_rsrv_bool, track_capacity_qt, inbound_flights_qt, loc_cd, loc_name, timezone_cd,  rstat_cd)
values (0, (SELECT MAX(LOC_ID)+1  FROM inv_loc), 0, 'SCRAP', 0, 0, 0, 0,0, 'atdup','atduplicate','America/New_York', 0);