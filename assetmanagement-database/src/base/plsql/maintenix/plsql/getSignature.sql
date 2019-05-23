--liquibase formatted sql

--changeSet getSignature:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getSignature
* Arguments:     aEventDbId, aEventId, aToolId, PK for Tool (aEventDbId, aEventId 
*                is the tool's task)
* Description:   This function returns a concatinated list of user UUIDs that 
*                completed work on the provided task with the provided tool
*                
*                ex: 
*                E5E5C041DA604CCBBD4353EFDA9EC000|E5E5C041DA604CCBBD4353EFDA9EC000
*                 
*********************************************************************************/
CREATE OR REPLACE FUNCTION getSignature
(
   aEventDbId     IN NUMBER,
   aEventId       IN NUMBER,
   aToolId        IN NUMBER
) RETURN VARCHAR2
IS
   TYPE signatureRow IS RECORD
   (
        signature      VARCHAR2(64),
        maxStatustOrd  NUMBER(10)
   );
   
   lReturnString  VARCHAR2 (32000);
   lSignature     signatureRow;

   CURSOR cSignatures
   (
      iEventDbId  IN NUMBER,
      iEventId    IN NUMBER,
      iToolId     IN NUMBER
   )
   IS

      SELECT
         DISTINCT RAWTOHEX(tech_user.alt_id) AS signature,
         MAX(CASE WHEN tech_role_status.labour_role_status_cd = 'COMPLETE' then tech_role_status.status_ord else NULL END) 
                   OVER (PARTITION BY tech_role_status.labour_role_db_id, tech_role_status.labour_role_id) as max_status_ord
      FROM
         sched_labour_tool,
         sched_labour
         INNER JOIN sched_labour_role tech_role ON
            tech_role.labour_db_id = sched_labour.labour_db_id AND
            tech_role.labour_id    = sched_labour.labour_id
         AND
            tech_role.labour_role_type_cd = 'TECH'
         INNER JOIN sched_labour_role_status tech_role_status ON
            tech_role_status.labour_role_db_id = tech_role.labour_role_db_id AND
            tech_role_status.labour_role_id    = tech_role.labour_role_id
         INNER JOIN org_hr tech_hr ON
            tech_hr.hr_db_id = tech_role_status.hr_db_id AND
            tech_hr.hr_id    = tech_role_status.hr_id
         INNER JOIN utl_user tech_user ON
            tech_hr.user_id = tech_user.user_id
      WHERE
         sched_labour_tool.event_db_id = iEventDbId AND
         sched_labour_tool.event_id    = iEventId  AND
         sched_labour_tool.tool_id     = iToolId
         AND
         sched_labour.labour_db_id = sched_labour_tool.labour_db_id AND
         sched_labour.labour_id    = sched_labour_tool.labour_id
    ;
BEGIN
   OPEN  cSignatures (aEventDbId, aEventId, aToolId);
   LOOP
      FETCH cSignatures INTO lSignature;
      EXIT WHEN cSignatures%NOTFOUND;

      IF(cSignatures%ROWCOUNT<> 1)
      THEN
         lReturnString := lReturnString||'|';
      END IF;

      lReturnString := lReturnString||lSignature.signature;
   END LOOP;
   CLOSE cSignatures;
   RETURN (SUBSTR (lReturnString, 1, 4000));
END;
/