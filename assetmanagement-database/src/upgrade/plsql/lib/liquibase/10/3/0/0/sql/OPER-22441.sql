--liquibase formatted sql

--changeSet OPER-22441:1 stripComments:false
--comment add event relation type RECSRCRP into table ref_rel_type
INSERT INTO ref_rel_type(rel_type_db_id, rel_type_cd, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
SELECT
   0, 'RECSRCRP', 'Replaced repetitive task.', 'The relationship between a fault and repetitive task when that task has been replaced by another repetitive task.', 
   0, TO_DATE('2018-05-30','YYYY-MM-DD'), TO_DATE('2018-05-30','YYYY-MM-DD'), 100, 'MXI'
FROM
   DUAL
WHERE
   NOT EXISTS
      (
        SELECT 1 FROM ref_rel_type WHERE rel_type_db_id =0 AND rel_type_cd ='RECSRCRP'
      );
