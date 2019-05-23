--liquibase formatted sql


--changeSet 0ref_rcv_routing:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_RCV_ROUTING"
** 0-Level
** DATE: 29-JUL-03
*********************************************/
insert into ref_rcv_routing(rcv_routing_db_id, rcv_routing_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'QUAR',0, 1, 'Quarantine', 'When the part is missing documentation or is damaged, or if did not know what to do with the part', 0, '23-MAR-01', '23-MAR-01', 20, 'MXI');

--changeSet 0ref_rcv_routing:2 stripComments:false
insert into ref_rcv_routing(rcv_routing_db_id, rcv_routing_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'U/S', 0, 1, 'Unserviceable', 'Unserviceable staging area (part requires repair)', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rcv_routing:3 stripComments:false
insert into ref_rcv_routing(rcv_routing_db_id, rcv_routing_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SRVC', 0, 1, 'Serviceable', 'Serviceable staging area (to be inspected)', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_rcv_routing:4 stripComments:false
insert into ref_rcv_routing(rcv_routing_db_id, rcv_routing_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'STORES', 0, 1, 'Storeroom', 'Storeroom (if part is marked as no inspection required)', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');