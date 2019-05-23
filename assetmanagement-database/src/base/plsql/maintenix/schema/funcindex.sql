--liquibase formatted sql


--changeSet funcindex:1 stripComments:false
-- TABLES
-- EQP_ASSMBL
  CREATE INDEX assmbl_cd_lower ON eqp_assmbl(lower(assmbl_cd));

--changeSet funcindex:2 stripComments:false
-- EQP_ASSMBL_BOM
  CREATE INDEX bom_assmbl_bom_cd_lower ON eqp_assmbl_bom(lower(assmbl_bom_cd));

--changeSet funcindex:3 stripComments:false
  CREATE INDEX bom_assmbl_cd_lower ON eqp_assmbl_bom(lower(assmbl_cd));

--changeSet funcindex:4 stripComments:false
-- EQP_BOM_PART
  CREATE INDEX assmbl_cd_bom_lower ON EQP_BOM_PART(lower(assmbl_cd));

--changeSet funcindex:5 stripComments:false
  CREATE INDEX bom_part_cd_lower ON eqp_bom_part(lower(bom_part_cd));

--changeSet funcindex:6 stripComments:false
-- EQP_MANUFACT
  CREATE INDEX manufact_cd_lower ON eqp_manufact(lower(manufact_cd));

--changeSet funcindex:7 stripComments:false
-- EQP_PART_NO
  CREATE INDEX part_no_oem_lower ON eqp_part_no(lower(part_no_oem));

--changeSet funcindex:8 stripComments:false
  CREATE INDEX part_manufact_cd_lower ON eqp_part_no(lower(manufact_cd));

--changeSet funcindex:9 stripComments:false
-- EQP_STOCK_NO
  CREATE INDEX stock_no_cd_lower ON EQP_STOCK_NO(lower(stock_no_cd));

--changeSet funcindex:10 stripComments:false
-- EVT_EVENT
  CREATE INDEX event_sdesc_lower ON evt_event(lower(event_sdesc));

--changeSet funcindex:11 stripComments:false
  CREATE INDEX ix_evt_event_histtypeschednh ON evt_event(HIST_BOOL, EVENT_TYPE_DB_ID, EVENT_TYPE_CD, SCHED_START_GDT, SCHED_END_GDT, NVL(NH_EVENT_DB_ID, -1));

--changeSet funcindex:12 stripComments:false
-- FL_LEG_DISRUPT
  CREATE INDEX ix_fllegdisrupt_disrdesc_upper ON fl_leg_disrupt (upper(disruption_desc));  

--changeSet funcindex:13 stripComments:false
-- INV_INV
  CREATE INDEX serial_no_oem_lower ON inv_inv(lower(serial_no_oem));

--changeSet funcindex:14 stripComments:false
  CREATE INDEX inv_barcode_sdesc_lower ON inv_inv(lower(barcode_sdesc));

--changeSet funcindex:15 stripComments:false
  CREATE INDEX ix_invinv_barcodesdesc_upper ON inv_inv (upper(barcode_sdesc));

--changeSet funcindex:16 stripComments:false
-- INV_LOC
  CREATE INDEX loc_cd_lower ON inv_loc(lower(loc_cd));

--changeSet funcindex:17 stripComments:false
-- ORG_HR
  create index hr_cd_lower ON org_hr(lower(hr_cd));

--changeSet funcindex:18 stripComments:false
-- ORG_VENDOR
  CREATE INDEX vendor_cd_lower ON org_vendor(lower(vendor_cd));

--changeSet funcindex:19 stripComments:false
-- ORG_WORK_DEPT
  CREATE INDEX dept_cd_lower ON org_work_dept(lower(dept_cd));  

--changeSet funcindex:20 stripComments:false
-- QUAR_QUAR
  CREATE INDEX ix_quarquar_barcodesdesc_upper ON quar_quar (upper(barcode_sdesc));

--changeSet funcindex:21 stripComments:false
-- REF_QTY_UNIT
  CREATE INDEX qty_unit_cd_lower ON ref_qty_unit(lower(qty_unit_cd));

--changeSet funcindex:22 stripComments:false
-- SCHED_STASK
  CREATE INDEX wo_ref_sdesc_lower ON sched_stask(lower(wo_ref_sdesc));

--changeSet funcindex:23 stripComments:false
  CREATE INDEX ix_schedstask_barcodesd_upper ON sched_stask (upper(barcode_sdesc));

--changeSet funcindex:24 stripComments:false
-- SHIP_SHIPMENT
  CREATE INDEX shpmnt_waybill_sdesc_upper ON ship_shipment(upper(waybill_sdesc));

--changeSet funcindex:25 stripComments:false
-- UTL_USER
  CREATE INDEX username_lower ON utl_user(lower(username));  

--changeSet funcindex:26 stripComments:false
-- VIEWS
-- MV_OPEN_PART_REQUESTS
  CREATE INDEX MV_OPEN_REQ_PK ON MV_OPEN_PART_REQUESTS(REQ_PART_DB_ID, REQ_PART_ID);

--changeSet funcindex:27 stripComments:false
-- MT_MATERIALS_REQUEST_STATUS  
  CREATE INDEX ix_mv_mc ON MT_MATERIALS_REQUEST_STATUS ("SCHED_DB_ID", "SCHED_ID");