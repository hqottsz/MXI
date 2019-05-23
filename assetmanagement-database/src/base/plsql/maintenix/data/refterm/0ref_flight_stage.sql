--liquibase formatted sql


--changeSet 0ref_flight_stage:1 stripComments:false
-- Electronic Logbook project
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FLIGHT_STAGE"
** 0-Level
*********************************************/
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FLP', 0, 1, 'Flight Planning', 'Flight Planning', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:2 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRF', 0, 1, 'Pre-flight', 'Pre-flight', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:3 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ESD', 0, 1, 'Engine Start/Depart', 'Engine Start/Depart', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:4 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXO', 0, 1, 'Taxi-out', 'Taxi-out', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:5 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RTO', 0, 1, 'Rejected Take-off', 'Rejected Take-off', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:6 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TOF', 0, 1, 'Take-off', 'Take-off', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:7 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICL', 0, 1, 'Initial Climb', 'Initial Climb', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:8 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TOC', 0, 1, 'Top Of Climb', 'Top Of Climb', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:9 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CRZ', 0, 1, 'Cruise', 'Cruise', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:10 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ECL', 0, 1, 'En Route Climb', 'En Route Climb', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:11 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TOD', 0, 1, 'Top Of Descent', 'Top Of Descent', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:12 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DST', 0, 1, 'Descent', 'Descent', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:13 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'APR', 0, 1, 'Approach', 'Approach', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:14 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'GOA', 0, 1, 'Go-around', 'Go-around', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:15 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LND', 0, 1, 'Landing', 'Landing', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:16 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXI', 0, 1, 'Taxi-in', 'Taxi-in', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:17 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'AES', 0, 1, 'Arrival/Engine Shutdown', 'Arrival/Engine Shutdown', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:18 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FLC', 0, 1, 'Flight Close', 'Flight Close', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:19 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PSF', 0, 1, 'Post-flight', 'Post-flight', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:20 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'GDS', 0, 1, 'Ground Servicing', 'Ground Servicing', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_flight_stage:21 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'APC', 0, 1, 'Circuit Pattern', 'Circuit Pattern', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:22 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'APF', 0, 1, ' Final Approach', ' Final Approach', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:23 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'API', 0, 1, 'Initial Approach', 'Initial Approach', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:24 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CRC', 0, 1, 'Cruise Climb', 'Cruise Climb', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:25 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CRD', 0, 1, 'Cruise Descent', 'Cruise Descent', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:26 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LNF', 0, 1, 'Flare', 'Flare', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:27 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LNR', 0, 1, 'Landing Roll', 'Landing Roll', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:28 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXP', 0, 1, 'Taxi to Take-off Position', 'Taxi to Take-off Position', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');

--changeSet 0ref_flight_stage:29 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXR', 0, 1, 'Taxi to Runway', 'Taxi to Runway', 0, '03-FEB-14', '03-FEB-14', 0, 'MXI');