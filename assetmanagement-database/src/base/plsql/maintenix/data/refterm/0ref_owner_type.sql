--liquibase formatted sql


--changeSet 0ref_owner_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_OWNER_TYPE"
** 0-Level
** DATE: 17-SEP-2007 TIME: 16:56:27
*********************************************/
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'LOCAL', 'Local', 'Local ownership type', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:2 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'EXCHRCVD', 'Exchange Receive', 'Non-local, received on Exchange Order', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:3 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'EXCHRTRN', 'Exchange Return', 'Non-local, return to vendor via Exchange Order', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:4 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'BORROW', 'Borrow', 'Non-local, received/installed on Borrow Order', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:5 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CSGNRCVD', 'Consignment Receive', 'Non-local, received on Consignment Order or Consignment Exchange Order', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:6 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'CSGNRTRN', 'Consignment Return', 'Non-local, return to vendor via Consignment Exchange Order', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');

--changeSet 0ref_owner_type:7 stripComments:false
insert into ref_owner_type(owner_type_db_id, owner_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'OTHER', 'Other', 'Non-local, catch all for all pooled, loaned or any other type', 0, '17-SEP-07', '17-SEP-07', 0, 'MXI');