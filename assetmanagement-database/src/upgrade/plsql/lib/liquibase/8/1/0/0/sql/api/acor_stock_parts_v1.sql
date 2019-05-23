--liquibase formatted sql


--changeSet acor_stock_parts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_stock_parts_v1
AS 
SELECT
    eqp_stock_no.alt_id              AS stock_id,
    eqp_part_no.alt_id               AS part_id,
    eqp_part_no.part_no_sdesc        AS part_number_name,
    eqp_part_no.part_no_ldesc        AS part_number_description,
    eqp_part_no.part_no_oem          AS part_number,
    eqp_part_no.part_status_cd       AS part_status_code,
    eqp_manufact.alt_id              AS manufacture_id,
    eqp_manufact.manufact_cd         AS manufacture_code,
    eqp_manufact.manufact_name       AS manufacture_name,
    eqp_part_no.model_name,
    eqp_part_no.rev_no_name          AS revision_number_name,
    ref_inv_class.traceable_bin_bool AS traceable_bin_flag,
    ref_part_use.part_use_cd         AS part_use_code,
    ref_part_use.desc_sdesc          AS part_use_name,
    ref_part_type.part_type_cd       AS part_type_code,
    ref_part_type.desc_sdesc         AS part_type_name,
    eqp_part_no.repair_bool          AS repair_flag,
    ref_inv_class.serial_bool        AS serial_flag,
    eqp_part_no.receipt_insp_bool    AS inspection_upon_receipt_flag,
    eqp_part_no.doc_req_ldesc        AS doc_required_description,
    ref_qty_unit.qty_unit_cd         AS std_quantity_unit_code,
    ref_qty_unit.desc_sdesc          AS std_quantity_unit_name,
    eqp_part_no.stock_pct            AS stock_percentage
FROM
    eqp_stock_no
    INNER JOIN eqp_part_no ON
       eqp_part_no.stock_no_db_id = eqp_stock_no.stock_no_db_id AND
       eqp_part_no.stock_no_id    = eqp_stock_no.stock_no_id
    INNER JOIN eqp_manufact ON
       eqp_manufact.manufact_db_id = eqp_part_no.manufact_db_id AND
       eqp_manufact.manufact_cd    = eqp_part_no.manufact_cd
    INNER JOIN ref_inv_class ON
       ref_inv_class.inv_class_db_id = eqp_stock_no.inv_class_db_id AND
       ref_inv_class.inv_class_cd    = eqp_stock_no.inv_class_cd
    LEFT JOIN ref_part_use ON
       eqp_part_no.part_use_db_id = ref_part_use.part_use_db_id AND
       eqp_part_no.part_use_cd    = ref_part_use.part_use_cd
    LEFT JOIN ref_part_type ON
       eqp_part_no.part_type_db_id = ref_part_type.part_type_db_id AND
       eqp_part_no.part_type_cd    = ref_part_type.part_type_cd
    LEFT JOIN ref_qty_unit ON
       eqp_part_no.qty_unit_db_id = ref_qty_unit.qty_unit_db_id AND
       eqp_part_no.qty_unit_cd    = ref_qty_unit.qty_unit_cd;