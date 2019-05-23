/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_EFFECT_TYPE"
** 10-Level
** DATE: 20-DEC-02 00:00:00
*********************************************/
insert into ref_fail_effect_type (fail_effect_type_db_id, fail_effect_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'VISIBLE', 0, 31,  'Visible/Feelable symptoms', 'Visual, kinestetic, auditory observations', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_fail_effect_type (fail_effect_type_db_id, fail_effect_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'FDE', 0, 31,  'Flight Deck Effect', 'FDE''s are effects manifest through flight deck notification', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
