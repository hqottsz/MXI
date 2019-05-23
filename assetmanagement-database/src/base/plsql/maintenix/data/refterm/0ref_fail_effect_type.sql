--liquibase formatted sql


--changeSet 0ref_fail_effect_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_EFFECT_TYPE"
** 0-Level
** DATE: 20-DEC-02 00:00:00
*********************************************/
insert into ref_fail_effect_type (fail_effect_type_db_id, fail_effect_type_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FREEFORM', 0, 6,  'Freeform', 'The failure effect was not defined in the baseline', 0, '20-DEC-02', '20-DEC-02', 0, 'MXI');