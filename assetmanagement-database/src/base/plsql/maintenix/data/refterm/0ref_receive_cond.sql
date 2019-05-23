--liquibase formatted sql


--changeSet 0ref_receive_cond:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_RECEIVE_COND"
** 0-Level
** DATE: 28-SEPT-2006 TIME: 00:00:00
*********************************************/
insert into ref_receive_cond(receive_cond_db_id, receive_cond_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'UNKNOWN', 'Unknown', 'Unknown', 0, '27-MAR-06', '27-MAR-06', 0, 'MXI' );

--changeSet 0ref_receive_cond:2 stripComments:false
insert into ref_receive_cond(receive_cond_db_id, receive_cond_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'NEW', 'New', 'New', 0, '18-AUG-08', '18-AUG-08', 0, 'MXI' );

--changeSet 0ref_receive_cond:3 stripComments:false
insert into ref_receive_cond(receive_cond_db_id, receive_cond_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'REP', 'Repaired', 'Repaired', 0, '18-AUG-08', '18-AUG-08', 0, 'MXI' );