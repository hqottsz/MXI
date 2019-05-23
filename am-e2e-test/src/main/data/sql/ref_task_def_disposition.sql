INSERT INTO ref_task_def_disposition
(
    task_def_disposition_db_id,
    task_def_disposition_cd,
    desc_sdesc,
    desc_ldesc,
    rstat_cd
)
SELECT
    application_object_pkg.getdbid,
    'EO',
    'Engineering Order Generation Required',
    'Engineering Order Generation Required',
    0
FROM
    dual
WHERE NOT EXISTS
(
    SELECT
        1
    FROM
        ref_task_def_disposition
    WHERE
        ref_task_def_disposition.task_def_disposition_db_id = application_object_pkg.getdbid AND
        ref_task_def_disposition.task_def_disposition_cd = 'EO'
);