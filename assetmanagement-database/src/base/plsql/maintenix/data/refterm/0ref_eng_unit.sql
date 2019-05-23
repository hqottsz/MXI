--liquibase formatted sql


--changeSet 0ref_eng_unit:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_ENG_UNIT"
** 0-Level
** DATE: 21-JUL-2004 TIME: 16:56:27
*********************************************/
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'DAY', 0, 'TIME', 0, 63, 'Days', 'Base unit of days, all other calendar units are factors of a day', 0, 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:2 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'HOUR', 0, 'TIME', 0, 63, 'Hours', 'Hours fraction of a day', 0, 0.041667, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:3 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'MONTH', 0, 'TIME', 0, 63, 'Months', 'Average calendar days in a month', 0, 30, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:4 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'WEEK', 0, 'TIME', 0, 63, 'Weeks', 'Rounded calendar days in a week', 0, 7, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:5 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'YEAR', 0, 'TIME', 0, 63, 'Years', 'Rounded calendar days in a year', 0, 365, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:6 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CYCLES', 0, 'COUNT', 0, 60, 'Cycles', 'Cycle returns to original state', 0, 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:7 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LNDG', 0, 'COUNT', 0, 60, 'Landings', 'Number of landing', 0, 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:8 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'HOURS', 0, 'COUNT', 0, 60, 'Operating Hours', 'Operating hours not based on calendar parameters', 0, 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_eng_unit:9 stripComments:false
-- Electronic Logbook
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LI', 0, 'VOLUME', 0, 1, 'Liter', 'A unit of metric capacity equal to 1.0 cubic decimeter, 61.02 cubic inches, 1.06 quarts (U.S.) or 0.88 quart (Imperial).', 0, 1, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_eng_unit:10 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QT', 0, 'VOLUME', 0, 1, 'Quart (U.S.)', 'A unit of liquid capacity equal to 1/4 (.25) gallon (U.S.), 57.75 cubic inches, 0.946 liter or 0.833 quart (Imperial).', 0, 0.946, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_eng_unit:11 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PT', 0, 'VOLUME', 0, 1, 'Pint (U.S.)', 'A measure of liquid capacity equal to 1/8 (.125) gallon (U.S.), 28.875 cubic inches, 0.473 liter or 0.833 pint (Imperial).', 0, 0.473, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_eng_unit:12 stripComments:false
insert into ref_eng_unit(eng_unit_db_id, eng_unit_cd, ref_unit_db_id, ref_unit_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, ref_offset_qt, ref_mult_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QI', 0, 'VOLUME', 0, 1, 'Quart (Imperial)', 'A unit of liquid capacity equal to 1/4 (.25) gallon (Imperial), 69.355 cubic inches, 1.136 liters or 1.201 quart (U.S.).', 0, 1.136, 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');