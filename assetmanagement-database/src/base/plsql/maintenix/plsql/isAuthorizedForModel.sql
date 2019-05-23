--liquibase formatted sql


--changeSet isAuthorizedForModel:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForModel
* Arguments:     aHrDbId    - human resource db id
*                aHrId      - human resource id
*                aModelDbId - forecast model db id
*                aModelId   - forecast model id
* Description:   This function determines if the human resource has authority 
*                over the specified forecast model.
*
* Orig.Coder:    Julie Bajer
* Recent Coder:  
* Recent Date:   April 29, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForModel
(
   aHrDbId    IN org_hr.hr_db_id%TYPE,
   aHrId      IN org_hr.hr_id%TYPE,
   aModelDbId IN fc_model.model_db_id%TYPE,
   aModelId   IN fc_model.model_id%TYPE
)  RETURN NUMBER
IS
   ls_AuthorityDbId  org_authority.authority_db_id%TYPE;
   ls_AuthorityId    org_authority.authority_id%TYPE;
   ln_IsAuthorized      NUMBER;
   ln_AllAuthorityBool  NUMBER;
BEGIN

   -- if the hr or model is null, return false (0)
   IF ( aHrDbId IS NULL ) OR ( aHrId IS NULL ) OR ( aModelDbId IS NULL ) OR ( aModelId IS NULL ) THEN
      RETURN 0;
   END IF;

   -- Return true if the user has all_authority_bool set to true
   SELECT
      org_hr.all_authority_bool
   INTO
      ln_AllAuthorityBool
   FROM
      org_hr
   WHERE
      org_hr.hr_db_id = aHrDbId AND
      org_hr.hr_id    = aHrId
      AND
      org_hr.rstat_cd	= 0;

   IF ln_AllAuthorityBool = 1 THEN
      RETURN 1;
   END IF;

   -- Retrieve the authority for the model
   SELECT
      fc_model.authority_db_id,
      fc_model.authority_id
   INTO
      ls_AuthorityDbId,
      ls_AuthorityId
   FROM
      fc_model
   WHERE
      fc_model.model_db_id = aModelDbId AND
      fc_model.model_id    = aModelId;

   -- If the authority is null, then return true (1)
   IF ls_AuthorityDbId IS NULL THEN
      RETURN 1;
   END IF;

   -- If there is an authority, determine if the user has it
   SELECT
      COUNT(*)
   INTO
      ln_IsAuthorized
   FROM
      org_hr_authority
   WHERE
      org_hr_authority.authority_db_id = ls_AuthorityDbId AND
      org_hr_authority.authority_id    = ls_AuthorityId
      AND
      org_hr_authority.hr_db_id = aHrDbId AND
      org_hr_authority.hr_id    = aHrId;

   -- Return true if the user has the authority, otherwise return false
   IF ln_IsAuthorized > 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isAuthorizedForModel;
/