--liquibase formatted sql


--changeSet isAuthorizedForInv:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForInv
* Arguments:     aHrDbId    - human resource db id
*                aHrId      - human resource id
*                aInvNoDbId - inventory db id
*                aInvNoId   - inventory id
* Description:   This function determines if the human resource has authority
*                over the specified inventory.
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  jbajer
* Recent Date:   April 29, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForInv
(
   aHrDbId    IN org_hr.hr_db_id%TYPE,
   aHrId      IN org_hr.hr_id%TYPE,
   aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
   aInvNoId   IN inv_inv.inv_no_id%TYPE
)  RETURN NUMBER
IS
   ls_InvAuthorityDbId  org_authority.authority_db_id%TYPE;
   ls_InvAuthorityId    org_authority.authority_id%TYPE;
   ln_IsAuthorized      NUMBER;
   ln_AllAuthorityBool  NUMBER;
   ln_InvNoDbId inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId inv_inv.inv_no_id%TYPE;

BEGIN

   -- if the hr or inventory is null, return false (0)
   IF ( aHrDbId IS NULL ) OR ( aHrId IS NULL ) OR ( aInvNoDbId IS NULL ) OR ( aInvNoId IS NULL ) THEN
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

   --  Check if inventory is attached to the air craft.

      SELECT
          inv_inv.h_inv_no_db_id,
          inv_inv.h_inv_no_id
      INTO
          ln_InvNoDbId,
          ln_InvNoId
      FROM
         inv_inv
      WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId;

  --  If inventory is attached to the aircraft, then authority should be retrieved for parent aircraft.
  IF ln_InvNoDbId IS NOT NULL AND ln_InvNoId IS NOT NULL
  THEN
   -- Retrieve the authority for the air craft.
   SELECT
      inv_inv.authority_db_id,
      inv_inv.authority_id
   INTO
      ls_InvAuthorityDbId,
      ls_InvAuthorityId
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = ln_InvNoDbId AND
      inv_inv.inv_no_id    = ln_InvNoId;

  ELSE

   -- Retrieve the authority for the inventory
   SELECT
      inv_inv.authority_db_id,
      inv_inv.authority_id
   INTO
      ls_InvAuthorityDbId,
      ls_InvAuthorityId
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId;

  END IF;


   -- If the authority is null, then return true (1)
   IF ls_InvAuthorityDbId IS NULL THEN
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
      org_hr_authority.authority_db_id = ls_InvAuthorityDbId AND
      org_hr_authority.authority_id    = ls_InvAuthorityId
      AND
      org_hr_authority.hr_db_id = aHrDbId AND
      org_hr_authority.hr_id    = aHrId;

   -- Return true if the user has the authority, otherwise return false
   IF ln_IsAuthorized > 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isAuthorizedForInv;
/