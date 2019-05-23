SELECT
   'AL_PROC_OPEN_DEFERRED_FAULT' AS "TABLE",
   al_proc_open_deferred_fault.assmbl_cd,
   al_proc_open_deferred_fault.part_no_oem,
   al_proc_open_deferred_fault.ata_sys_cd,
   dl_ref_message.result_cd,
   dl_ref_message.user_desc
FROM
       AL_PROC_OPEN_DEFERRED_FAULT
       INNER JOIN al_proc_result ON
       al_proc_open_deferred_fault.record_id = al_proc_result.record_id
       INNER JOIN dl_ref_message ON
       al_proc_result.result_cd = dl_ref_message.result_cd
WHERE severity_cd in ('CRITICAL', 'DEPENDENCY')
UNION ALL
SELECT
   'AL_PROC_ODF_PART_REQUIREMENT' AS "TABLE",
   al_proc_odf_part_requirement.prt_assmbl_cd as assmbl_cd,
   al_proc_odf_part_requirement.prt_part_no_oem as part_no_oem,
   al_proc_odf_part_requirement.prt_ipc_ref_cd as ata_sys_cd,
   dl_ref_message.result_cd as result_cd,
   dl_ref_message.result_cd as user_desc
FROM
       al_proc_odf_part_requirement
       INNER JOIN al_proc_result ON
       al_proc_odf_part_requirement.record_id = al_proc_result.record_id
       INNER JOIN dl_ref_message ON
       al_proc_result.result_cd = dl_ref_message.result_cd
WHERE severity_cd in ('CRITICAL', 'DEPENDENCY')