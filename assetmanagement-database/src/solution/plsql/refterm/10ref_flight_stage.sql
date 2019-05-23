/********************************************
** INSERT SCRIPT FOR TABLE "REF_FLIGHT_STAGE"
** 10-Level
** DATE: 09/30/1998 TIME: 16:56:27
*********************************************/
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'AP', 0, 1, 'Approach', 'Occurred between final approach fix and runway decision height.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CL', 0, 1, 'Climb', 'Occurred after decision speed, before leveling at flight planned altitiude', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CR', 0, 1, 'Cruise', 'Occurred in cruise phase, before initial descent', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'DE', 0, 1, 'Descent', 'Occurred durring descent, before decision height.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'HO', 0, 1, 'Hovering (helicopter)', 'Occurred in hover flight', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'IN', 0, 1, 'Inspection/maintenance', 'Occurred durring inspecion/maintenance', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'LD', 0, 1, 'Landing', 'Occurred at or below decision height for runway in use', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'NR', 0, 1, 'Not reported', 'Not reported', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'ST', 0, 1, 'Engine start', 'Occurred between spoolup and idle phase of engine start.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'TO', 0, 1, 'Take-off', 'Occurred from stage of runway entrance to takeoff decision speed.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_flight_stage(flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'TX', 0, 1, 'Taxi/ground handling', 'Occurred in stage between gate departure and runway entry.', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
