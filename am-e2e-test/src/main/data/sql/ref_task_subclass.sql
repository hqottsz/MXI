INSERT INTO
ref_task_subclass
( 
 task_subclass_db_id, task_subclass_cd, task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_subclass_cd, rstat_cd
)
SELECT 
 application_object_pkg.getdbid,'REQSUB', 0, 'REQ', 0, 120, 'Requirement Subclass', 'Requirement Subclass', 'REQSUB', 0 
FROM
 dual
WHERE 
NOT EXISTS (SELECT 1 FROM ref_task_subclass WHERE ref_task_subclass.task_subclass_db_id = application_object_pkg.getdbid AND ref_task_subclass.task_subclass_cd = 'REQSUB');