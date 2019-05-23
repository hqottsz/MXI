--liquibase formatted sql


--changeSet 0ref_inv_cond:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INV_COND"
** 0-Level
** DATE: 11-MAR-05
*********************************************/
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFI', 0, 47, 1,1, 'Ready for Issue', 'The asset is in operational condition, ready for issue.', 'RFI', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:2 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'INSRV', 0, 39, 1,0, 'In Service', 'The asset is in service', 'INSRV', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:3 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REPREQ', 0, 124, 0,1, 'Repair Required', 'Asset requires repair', 'REPREQ', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:4 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SCRAP', 0, 31, 0,0, 'Scrap Asset', 'This asset has been scrapped, a certificate of destruction should exist','SCRAP',0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:5 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QUAR', 0, 41, 0,0, 'Quarantined', 'This asset has been taken out of service, awaiting evaluation', 'QUAR', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:6 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ARCHIVE', 0, 114, 0,0, 'Archive', 'Archive', 'ARCHIVE', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:7 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'INREP', 0, 132, 0,0, 'In Repair', 'Asset is in repair.  The workorder has already been initiated.', 'INREP', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_inv_cond:8 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'INSPREQ', 0, 1, 0,1, 'Inspection Required', 'Inspection Required', 'INSPREQ', 0, 0, '13-DEC-04', '13-DEC-04', 100, 'MXI');

--changeSet 0ref_inv_cond:9 stripComments:false
insert into ref_inv_cond(inv_cond_db_id, inv_cond_cd, bitmap_db_id, bitmap_tag, srv_bool, create_bool, desc_sdesc, desc_ldesc, user_cd, wo_complete_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CONDEMN', 0, 1, 0,0, 'Condemned', 'Condemned', 'CONDEMN', 0, 0, '13-DEC-04', '13-DEC-04', 100, 'MXI');