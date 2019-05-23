--liquibase formatted sql


--changeSet vw_kit_baseline:1 stripComments:false
/**
This view displays the part baseline for kits.  It differentiates the details of the kit (kit) with the parts underneath it (kit_item).
*/
/********************************************************************************
*
* View:           vw_kit_baseline
*
* Description:    This view displays the part baseline for kits.  It differentiates the details of the kit (kit) with the parts underneath it (kit_item).
*
* Orig.Coder:    cdaley
* Recent Coder:
* Recent Date:   Jun 23rd, 2009
*
*********************************************************************************/
CREATE OR REPLACE VIEW vw_kit_baseline AS
SELECT
    eqp_part_no.part_no_db_id          as kit_part_no_db_id,
    eqp_part_no.part_no_id             as kit_part_no_id,
    eqp_kit_part_groups.kit_qt         as kit_qt,
    eqp_kit_part_groups.value_pct      as value_pct,
    eqp_kit_part_groups.bom_part_db_id as kit_bom_part_db_id,
    eqp_kit_part_groups.bom_part_id    as kit_bom_part_id,
    kit_part.part_no_db_id             as kit_item_part_no_db_id,
    kit_part.part_no_id                as kit_item_part_no_id
FROM
    -- get the kits and join them to the mapping table that contains the part groups in the kit
    eqp_part_no INNER JOIN eqp_kit_part_group_map ON
               eqp_part_no.part_no_db_id = eqp_kit_part_group_map.kit_part_no_db_id AND
               eqp_part_no.part_no_id = eqp_kit_part_group_map.kit_part_no_id
    INNER JOIN eqp_kit_part_groups ON
               eqp_kit_part_group_map.eqp_kit_part_group_db_id = eqp_kit_part_groups.eqp_kit_part_group_db_id AND
               eqp_kit_part_group_map.eqp_kit_part_group_id = eqp_kit_part_groups.eqp_kit_part_group_id
    -- join in the parts/alternates for the part groups assigned to the kit
    INNER JOIN eqp_kit_part_map ON
               eqp_kit_part_groups.eqp_kit_part_group_db_id = eqp_kit_part_map.eqp_kit_part_group_db_id AND
               eqp_kit_part_groups.eqp_kit_part_group_id = eqp_kit_part_map.eqp_kit_part_group_id
    INNER JOIN eqp_part_no kit_part ON
               eqp_kit_part_map.part_no_db_id = kit_part.part_no_db_id AND
               eqp_kit_part_map.part_no_id = kit_part.part_no_id
WHERE
    -- only get details for Kit inventory class
    eqp_part_no.inv_class_db_id = 0 AND
    eqp_part_no.inv_class_cd = 'KIT';