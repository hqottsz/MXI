/********************************************
** INSERT SCRIPT FOR TABLE "REF_FLIGHT_TYPE"
** 10-Level
** DATE: 09/30/1998 TIME: 16:56:27
*********************************************/
insert into ref_flight_type(flight_type_db_id, flight_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, spec2k_flight_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'NORM', 0, 95, 'Normal Flight', 'This is a normal point to point flight for this aircraft.', 'R', 0, TO_DATE('23-MAR-01','DD-MON-YY'), TO_DATE('23-MAR-01','DD-MON-YY'), 100, 'MXI');
insert into ref_flight_type(flight_type_db_id, flight_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, spec2k_flight_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'FERRY', 0, 99, 'Ferry Flight', 'Special flight permit required.  Special AW rules apply', 'F', 0, TO_DATE('23-MAR-01','DD-MON-YY'), TO_DATE('23-MAR-01','DD-MON-YY'), 100, 'MXI');
insert into ref_flight_type(flight_type_db_id, flight_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, spec2k_flight_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'TEST', 0, 121, 'Test Flight', 'Flight for the purpose of returning ac to AW status', 'N', 0, TO_DATE('23-MAR-01','DD-MON-YY'), TO_DATE('23-MAR-01','DD-MON-YY'), 100, 'MXI');
