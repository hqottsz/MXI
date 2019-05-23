--liquibase formatted sql


--changeSet 0ref_remove_reason:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_REMOVAL_REASON"
** 0-Level
** DATE: 17-JUN-11 TIME: 00:00:00
*********************************************/
insert into ref_remove_reason(remove_reason_db_id, remove_reason_cd, inv_cond_db_id, inv_cond_cd,  desc_sdesc, desc_ldesc, spec2k_remove_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'IMSCHD', 0, 'REPREQ', 'Unscheduled Failure', 'Unscheduled Failure', 'U', 0, '13-DEC-04', '13-DEC-04', 0, 'MXI' );

--changeSet 0ref_remove_reason:2 stripComments:false
insert into ref_remove_reason(remove_reason_db_id, remove_reason_cd, inv_cond_db_id, inv_cond_cd,  desc_sdesc, desc_ldesc, spec2k_remove_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'VENDRET', 0, 'RFI', 'Return to Vendor', 'Return to Vendor', 'S', 0, '13-DEC-04', '13-DEC-04', 0, 'MXI' );

--changeSet 0ref_remove_reason:3 stripComments:false
insert into ref_remove_reason(remove_reason_db_id, remove_reason_cd, inv_cond_db_id, inv_cond_cd,  desc_sdesc, desc_ldesc, spec2k_remove_reason_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'ROB', 0, 'RFI', 'Rob Serviceable Component', 'Rob Serviceable Component', 'R', 0, '17-JUN-11', '17-JUN-11', 0, 'MXI' );