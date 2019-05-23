--liquibase formatted sql


--changeSet OPER-285:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*******************************************************************************************************************************
     Added new column to hold spec 2000 mapping values for ref_fault_source, ref_flight_type and ref_remove_reason
     Added new values for ref_flight_stage
     OPER-285 OPER-287 OPER-288 OPER-289 OPER-290
*******************************************************************************************************************************/
BEGIN
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE REF_FAULT_SOURCE ADD (
               SPEC2K_FAULT_SOURCE_CD Varchar2(8)
       )'
     );     
END;
/

--changeSet OPER-285:2 stripComments:false
UPDATE ref_fault_source
   SET spec2k_fault_source_cd = 'PL'
 WHERE fault_source_cd = 'PILOT';

--changeSet OPER-285:3 stripComments:false
UPDATE ref_fault_source
   SET spec2k_fault_source_cd = 'ML'
 WHERE fault_source_cd = 'MECH';

--changeSet OPER-285:4 stripComments:false
UPDATE ref_fault_source
   SET spec2k_fault_source_cd = 'PL'
 WHERE fault_source_cd = 'MESSAGE'; 

--changeSet OPER-285:5 stripComments:false
UPDATE ref_fault_source
   SET spec2k_fault_source_cd = 'CL'
 WHERE fault_source_cd = 'CABIN';

--changeSet OPER-285:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE REF_FLIGHT_TYPE ADD (
               SPEC2K_FLIGHT_TYPE_CD Varchar2(8)
       )'
     );     
END;
/

--changeSet OPER-285:7 stripComments:false
UPDATE ref_flight_type
   SET spec2k_flight_type_cd = 'F'
 WHERE flight_type_cd = 'FERRY';

--changeSet OPER-285:8 stripComments:false
UPDATE ref_flight_type
   SET spec2k_flight_type_cd = 'N'
 WHERE flight_type_cd = 'TEST'; 

--changeSet OPER-285:9 stripComments:false
UPDATE ref_flight_type
   SET spec2k_flight_type_cd = 'R'
 WHERE flight_type_cd = 'RGEN'; 

--changeSet OPER-285:10 stripComments:false
UPDATE ref_flight_type
   SET spec2k_flight_type_cd = 'R'
 WHERE flight_type_cd not in ('FERRY', 'TEST', 'RGEN');

--changeSet OPER-285:11 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'APC', 0, 1, 'Circuit Pattern', 'Circuit Pattern', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:12 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'APF', 0, 1, ' Final Approach', ' Final Approach', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:13 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'API', 0, 1, 'Initial Approach', 'Initial Approach', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:14 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CRC', 0, 1, 'Cruise Climb', 'Cruise Climb', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:15 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CRD', 0, 1, 'Cruise Descent', 'Cruise Descent', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:16 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LNF', 0, 1, 'Flare', 'Flare', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:17 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LNR', 0, 1, 'Landing Roll', 'Landing Roll', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:18 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXP', 0, 1, 'Taxi to Take-off Position', 'Taxi to Take-off Position', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:19 stripComments:false
insert into ref_flight_stage (flight_stage_db_id, flight_stage_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TXR', 0, 1, 'Taxi to Runway', 'Taxi to Runway', 0, TO_DATE('03-02-14','DD-MM-YY'), TO_DATE('03-02-14','DD-MM-YY'), 0, 'MXI');

--changeSet OPER-285:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE REF_REMOVE_REASON ADD (
               SPEC2K_REMOVE_REASON_CD Varchar2(8)
       )'
     );     
END;
/

--changeSet OPER-285:21 stripComments:false
UPDATE ref_remove_reason
   SET spec2k_remove_reason_cd = 'U'
 WHERE remove_reason_cd = 'IMSCHD';

--changeSet OPER-285:22 stripComments:false
UPDATE ref_remove_reason
   SET spec2k_remove_reason_cd = 'S'
 WHERE remove_reason_cd = 'VENDRET'; 

--changeSet OPER-285:23 stripComments:false
UPDATE ref_remove_reason
   SET spec2k_remove_reason_cd = 'R'
 WHERE remove_reason_cd = 'ROB'; 

--changeSet OPER-285:24 stripComments:false
UPDATE ref_remove_reason
   SET spec2k_remove_reason_cd = 'O'
 WHERE remove_reason_cd NOT IN ('ROB', 'VENDRET', 'IMSCHD');