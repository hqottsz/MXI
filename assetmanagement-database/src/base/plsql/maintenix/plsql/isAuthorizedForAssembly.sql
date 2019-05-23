--liquibase formatted sql


--changeSet isAuthorizedForAssembly:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForAssembly
* Arguments:     aHrDbId    - human resource db id
*                aHrId      - human resource id
*                aAssemblyDbId - assembly db id
*                aAssemblyCd   - assembly cd
* Description:   This function determines if the human resource has authority
*                over the specified assembly.
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  jbajer
* Recent Date:   April 29, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForAssembly
(
   aHrDbId    IN org_hr.hr_db_id%TYPE,
   aHrId      IN org_hr.hr_id%TYPE,
   aAssemblyDbId IN eqp_assmbl.assmbl_db_id%TYPE,
   aAssemblyCd   IN eqp_assmbl.assmbl_cd%TYPE
)  RETURN NUMBER
IS
   ls_AssemblyAuthorityDbId  org_authority.authority_db_id%TYPE;
   ls_AssemblyAuthorityId    org_authority.authority_id%TYPE;
   ln_IsAuthorized      NUMBER;
   ln_AllAuthorityBool  NUMBER;
BEGIN

   -- if the hr or inventory is null, return false (0)
   IF ( aHrDbId IS NULL ) OR ( aHrId IS NULL ) OR ( aAssemblyDbId IS NULL ) OR ( aAssemblyCd IS NULL ) THEN
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
      org_hr.hr_id    = aHrId;

   IF ln_AllAuthorityBool = 1 THEN
      RETURN 1;
   END IF;

   -- Retrieve the authority for the inventory
   SELECT
      eqp_assmbl.authority_db_id,
      eqp_assmbl.authority_id
   INTO
      ls_AssemblyAuthorityDbId,
      ls_AssemblyAuthorityId
   FROM
      eqp_assmbl
   WHERE
      eqp_assmbl.assmbl_db_id = aAssemblyDbId AND
      eqp_assmbl.assmbl_cd    = aAssemblyCd;

   -- If the authority is null, then return true (1)
   IF ls_AssemblyAuthorityDbId IS NULL THEN
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
      org_hr_authority.authority_db_id = ls_AssemblyAuthorityDbId AND
      org_hr_authority.authority_id    = ls_AssemblyAuthorityId
      AND
      org_hr_authority.hr_db_id = aHrDbId AND
      org_hr_authority.hr_id    = aHrId;

   -- Return true if the user has the authority, otherwise return false
   IF ln_IsAuthorized > 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isAuthorizedForAssembly;
/