--liquibase formatted sql


--changeSet isAuthorizedForResource:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isAuthorizedForResource
* Arguments:     aUserId   - user id
*                aParmName - config parm name
* Description:   This function determines if the user has authority
*                over the config parm.
*
* Orig.Coder:    Wayne Leroux
* Recent Coder:  Jonathan Clarkin
* Recent Date:   Sept 24, 2014
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForResource
(
   aUserId      utl_user.user_id%TYPE,
   aParmName    utl_config_parm.parm_name%TYPE
)  RETURN NUMBER
IS

   lParmValue utl_config_parm.parm_value%TYPE;
   lAuthorizedForResource NUMBER;

BEGIN
   lParmValue := NULL;

   SELECT getUserParmValue( aUserId, aParmName )
   INTO lParmValue
   FROM DUAL;

   IF upper(lParmValue) = 'TRUE' THEN
      lAuthorizedForResource := 1;
   ELSIF lParmValue = '1' THEN
      lAuthorizedForResource := 1;
   ELSE
      lAuthorizedForResource := 0;
   END IF;

   RETURN lAuthorizedForResource;

END isAuthorizedForResource;
/