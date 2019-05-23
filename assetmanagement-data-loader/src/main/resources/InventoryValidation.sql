SELECT
   'C_RI_INVENTORY' AS "TABLE",
   al_proc_inventory.serial_no_oem,
   al_proc_inventory.part_no_oem,
   al_proc_inventory.manufact_cd,
   al_proc_inventory_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_inventory
   INNER JOIN al_proc_inventory_error ON
      al_proc_inventory.record_id = al_proc_inventory_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_inventory_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_INVENTORY_SUB' AS "TABLE",
   al_proc_inventory_sub.serial_no_oem,
   al_proc_inventory_sub.part_no_oem,
   al_proc_inventory_sub.manufact_cd,
   al_proc_inventory_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_inventory_sub
   INNER JOIN al_proc_inventory_error ON
      al_proc_inventory_sub.record_id = al_proc_inventory_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_inventory_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_INVENTORY_USAGE' AS "TABLE",
   al_proc_inventory_usage.serial_no_oem,
   al_proc_inventory_usage.part_no_oem,
   al_proc_inventory_usage.manufact_cd,
   al_proc_inventory_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_inventory_usage
   INNER JOIN al_proc_inventory_error ON
      al_proc_inventory_usage.record_id = al_proc_inventory_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_inventory_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_ATTACH' AS "TABLE",
   al_proc_attach.attach_serial_no_oem,
   al_proc_attach.attach_part_no_oem,
   al_proc_attach.attach_manufact_cd,
   al_proc_inventory_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_attach
   INNER JOIN al_proc_inventory_error ON
      al_proc_attach.record_id = al_proc_inventory_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_inventory_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'C_RI_INVENTORY_CAP_LEVELS' AS "TABLE",
   al_proc_inventory_cap_levels.serial_no_oem,
   al_proc_inventory_cap_levels.part_no_oem,
   al_proc_inventory_cap_levels.manufact_cd,
   al_proc_inventory_error.error_cd,
   dl_ref_message.severity_cd,
   dl_ref_message.user_desc
FROM
   al_proc_inventory_cap_levels
   INNER JOIN al_proc_inventory_error ON
      al_proc_inventory_cap_levels.record_id = al_proc_inventory_error.record_id
   INNER JOIN dl_ref_message ON
      dl_ref_message.result_cd = al_proc_inventory_error.error_cd
WHERE
   dl_ref_message.severity_cd in ('CRITICAL', 'DEPENDENCY')