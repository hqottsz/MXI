--liquibase formatted sql


--changeSet 0ref_price_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PRICE_TYPE"
** 0-Level
** DATE: 21-DEC-06
*********************************************/
INSERT INTO ref_price_type(price_type_db_id, price_type_cd, desc_sdesc, desc_ldesc, bitmap_db_id, bitmap_tag,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'QUOTE', 'Quoted Price', 'price from RFQ quote line', 0, 1, 0, '21-DEC-06', '21-DEC-06', 100, 'MXI');

--changeSet 0ref_price_type:2 stripComments:false
INSERT INTO ref_price_type(price_type_db_id, price_type_cd, desc_sdesc, desc_ldesc, bitmap_db_id, bitmap_tag,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'CNFRMD', 'Confirmed Price', 'Confirmed Unit Price with Vendor', 0, 1, 0, '29-JUL-08', '29-JUL-08', 100, 'MXI');

--changeSet 0ref_price_type:3 stripComments:false
INSERT INTO ref_price_type(price_type_db_id, price_type_cd, desc_sdesc, desc_ldesc, bitmap_db_id, bitmap_tag,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
VALUES (0, 'WARRANTY', 'Warranty Price', 'Warranty price', 0, 1, 0, '02-SEP-08', '02-SEP-08', 100, 'MXI');