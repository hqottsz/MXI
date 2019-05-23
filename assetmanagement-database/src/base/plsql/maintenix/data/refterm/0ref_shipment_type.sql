--liquibase formatted sql


--changeSet 0ref_shipment_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SHIPMENT_TYPE"
** 0-Level
** DATE: 15-MAR-2005
*********************************************/
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'REPAIR', 0, 76, 'Repair Order', 'Contracting repair order', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:2 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PURCHASE', 0, 45, 'Purchase Order', 'Purchase parts', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:3 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'STKTRN', 0, 12, 'Stock Transfer', 'Transfers btw stores for levelling purposes', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:4 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RTNVEN', 0, 75, 'Return to Vendor', 'Loaner return', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:5 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SENDREP', 0, 76, 'Send For Repair', 'Send For Repair', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:6 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'SENDXCHG', 0, 76, 'Send For Exchange', 'Send For Exchange', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_shipment_type:7 stripComments:false
insert into ref_shipment_type(shipment_type_db_id, shipment_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RTNWAR', 0, 76, 'Return Warranty', 'Return Warranty', 0, '06-AUG-08', '06-AUG-08', 100, 'MXI');

--changeSet 0ref_shipment_type:8 stripComments:false
insert into REF_SHIPMENT_TYPE (SHIPMENT_TYPE_DB_ID, SHIPMENT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 'N/A', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');