--liquibase formatted sql

--changeSet OPER-21455:1 stripComments:false
--comment add event relation type FAULTREL into table ref_rel_type
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
SELECT
   0, 'FAULTREL', 'The relationships between a fault and all its related tasks.', 
'When a fault is raised, it may either be deferred (with repeating tasks) or completed (triggering changes to the inspection and repair program). \
To provide tractability, it is important to show a relationship between the originating fault and all related tasks. \
This includes recurring tasks created upon the deferral, and tasks created when the fault has a requirement sub-task \
that has CREATE links to other tasks, and when the tasks created by the CREATE link have CREATE links to other tasks. \
This relationship does not show sub-tasks of the fault (either ad-hoc or baselined) that were completed during the execution \
of the fault. This relationship also does not show child tasks of any related task if a requirement is linked via a CREATE link, \
and the requirement has child job cards, the child job cards will not appear. This relationship is created when the related task is created.',  
   0, TO_DATE('2018-04-06','YYYY-MM-DD'), TO_DATE('2018-04-06','YYYY-MM-DD'), 100, 'MXI'
FROM
   DUAL
WHERE
   NOT EXISTS
      (
        SELECT 1 FROM ref_rel_type WHERE rel_type_db_id =0 AND rel_type_cd ='FAULTREL'
      );
