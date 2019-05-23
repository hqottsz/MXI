--liquibase formatted sql


--changeSet 0ref_ref_unit:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REF_UNIT"
** 0-Level
** DATE: 17/11/2004 TIME: 16:56:27
*********************************************/
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CURRENT', 0, 01, 'CURRENT', 'CURRENT',  0, '25-OCT-04', '25-OCT-04', 100, 'MXI');

--changeSet 0ref_ref_unit:2 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VOLTAGE', 0, 01, 'VOLTAGE', 'VOLTAGE',  0, '25-OCT-04', '25-OCT-04', 100, 'MXI');

--changeSet 0ref_ref_unit:3 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FREQNCY', 0, 01, 'FREQUENCY', 'FREQUENCY',  0, '25-OCT-04', '25-OCT-04', 100, 'MXI');

--changeSet 0ref_ref_unit:4 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'POWER', 0, 01, 'POWER', 'POWER',  0, '25-OCT-04', '25-OCT-04', 100, 'MXI');

--changeSet 0ref_ref_unit:5 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TIME', 0, 109, 'Time', 'Time',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_ref_unit:6 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'WEIGHT', 0, 111, 'Weight', 'Weight (w/ mass or gravity)',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_ref_unit:7 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'COUNT', 0, 60, 'Counts', 'Unitless measurment',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_ref_unit:8 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LENGTH', 0, 60, 'Length', 'Unit of distance',  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_ref_unit:9 stripComments:false
insert into ref_ref_unit(ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VOLUME', 0, 60, 'Volume', 'Length ^3',  0, '26-NOV-09', '26-NOV-09', 100, 'MXI');