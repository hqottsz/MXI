SELECT
   'AL_WORK_PACKAGE' AS "TABLE",
   AL_WORK_PACKAGE.WKP_NAME,
   al_proc_result.result_cd,
   dl_ref_message.user_desc
FROM
       AL_WORK_PACKAGE
       INNER JOIN al_proc_result ON
          al_proc_result.record_id = al_work_package.record_id
       INNER JOIN dl_ref_message ON
          al_proc_result.result_cd = dl_ref_message.result_cd
UNION ALL
SELECT
   'AL_WORK_PACKAGE_TASK' AS "TABLE",
   AL_WORK_PACKAGE_TASK.WKP_NAME,
   al_proc_result.result_cd,
   dl_ref_message.user_desc
FROM
       AL_WORK_PACKAGE_TASK
       INNER JOIN al_proc_result ON
          al_proc_result.record_id = al_work_package_task.record_id
       INNER JOIN dl_ref_message ON
          al_proc_result.result_cd = dl_ref_message.result_cd
UNION ALL
SELECT
   'AL_WORK_PACKAGE_TASK_ATA' AS "TABLE",
   AL_WORK_PACKAGE_TASK_ATA.WKP_NAME,
   al_proc_result.result_cd,
   dl_ref_message.user_desc
FROM
       AL_WORK_PACKAGE_TASK_ATA
       INNER JOIN al_proc_result ON
          al_proc_result.record_id = AL_WORK_PACKAGE_TASK_ATA.record_id
       INNER JOIN dl_ref_message ON
          al_proc_result.result_cd = dl_ref_message.result_cd
