--liquibase formatted sql


--changeSet OPER-315:1 stripComments:false
INSERT INTO ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 
   0,'AGI','AOG','Aircraft On the Ground','Aircraft On the Ground.',0, TO_DATE('2014-02-19', 'YYYY-MM-DD'), TO_DATE('2014-02-19', 'YYYY-MM-DD'), 100, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   ref_result_event
                WHERE
                  result_event_db_id = 0 AND
                  result_event_cd    = 'AGI'
              );   