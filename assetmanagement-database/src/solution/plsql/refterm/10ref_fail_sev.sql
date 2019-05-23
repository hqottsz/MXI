/********************************************
** INSERT SCRIPT FOR TABLE "REF_FAIL_SEV"
** 10-Level
** DATE: 02/05/03 TIME: 16:56:27
*********************************************/
insert into ref_fail_sev(fail_sev_db_id, fail_sev_cd, bitmap_db_id, bitmap_tag,  desc_sdesc, desc_ldesc, fail_sev_ord, sev_type_db_id, sev_type_cd,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'SRM', 0, 151,  'SRM Failure', 'Fault can be deferred due to Structural Repair Manual.', 30,  0, 'MEL',   0, '23-MAR-01', '23-MAR-01', 100, 'MXI');