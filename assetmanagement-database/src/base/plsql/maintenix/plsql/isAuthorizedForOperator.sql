--liquibase formatted sql


--changeSet isAuthorizedForOperator:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForAssembly
* Arguments:     aHrDbId    - human resource db id
*                aHrId      - human resource id
*                aCarrierDbId - carrier db id
*                aCarrierId   - carrier id
* Description:   This function determines if the human resource has authority
*                over the specified operator.
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  jbajer
* Recent Date:   April 29, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForOperator
(
   aHrDbId    IN org_hr.hr_db_id%TYPE,
   aHrId      IN org_hr.hr_id%TYPE,
   aCarrierDbId IN org_carrier.carrier_db_id%TYPE,
   aCarrierId   IN org_carrier.carrier_id%TYPE
)  RETURN NUMBER
IS
   ls_CarrierAuthorityDbId  org_authority.authority_db_id%TYPE;
   ls_CarrierAuthorityId    org_authority.authority_id%TYPE;
   ln_IsAuthorized      NUMBER;
   ln_AllAuthorityBool  NUMBER;
BEGIN

   -- if the hr or inventory is null, return false (0)
   IF ( aHrDbId IS NULL ) OR ( aHrId IS NULL ) THEN
      RETURN 0;
   END IF;

   IF ( aCarrierDbId IS NULL ) OR ( aCarrierId IS NULL ) THEN
      RETURN 1;
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
      org_carrier.authority_db_id,
      org_carrier.authority_id
   INTO
      ls_CarrierAuthorityDbId,
      ls_CarrierAuthorityId
   FROM
      org_carrier
   WHERE
      org_carrier.carrier_db_id = aCarrierDbId AND
      org_carrier.carrier_id    = aCarrierId;

   -- If the authority is null, then return true (1)
   IF ls_CarrierAuthorityDbId IS NULL THEN
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
      org_hr_authority.authority_db_id = ls_CarrierAuthorityDbId AND
      org_hr_authority.authority_id    = ls_CarrierAuthorityId
      AND
      org_hr_authority.hr_db_id = aHrDbId AND
      org_hr_authority.hr_id    = aHrId;

   -- Return true if the user has the authority, otherwise return false
   IF ln_IsAuthorized > 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;

END isAuthorizedForOperator;
/