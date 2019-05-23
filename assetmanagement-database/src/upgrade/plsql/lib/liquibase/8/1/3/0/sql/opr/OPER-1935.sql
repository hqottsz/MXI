--liquibase formatted sql


--changeSet OPER-1935:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getUserParmValue
* Arguments:     aUserId   - user id
*                aParmName - config parm name
* Description:   This function retrieves the user parm value, compliant with role 
*                and global config parm rules.
*
* Orig.Coder:    Wayne Leroux
* Recent Coder:  Jonathan Clarkin
* Recent Date:   Sept 24, 2014
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getUserParmValue
(
   aUserId      utl_user.user_id%TYPE,
   aParmName    utl_config_parm.parm_name%TYPE
)  RETURN VARCHAR2
IS

   lParmValue utl_config_parm.parm_value%TYPE;
  
   CURSOR lUserParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      utl_user_parm.parm_value
   FROM
      utl_user
      INNER JOIN utl_user_parm ON
         utl_user_parm.user_id = utl_user.user_id
   WHERE
      utl_user.user_id = aUserId
      AND
     utl_user_parm.parm_name = aParmName;

   -- need to retrieve row with lowest role_order
   CURSOR lRoleParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      parm_value
   FROM
      (
         SELECT
            utl_role_parm.parm_value
         FROM
            utl_user_role
            INNER JOIN utl_role_parm ON
               utl_role_parm.role_id = utl_user_role.role_id
         WHERE
            utl_user_role.user_id = aUserId
            AND
            utl_role_parm.parm_name = aParmName
      )
   WHERE
      ROWNUM = 1;
  
   CURSOR lGlobalParmCursor(aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      utl_config_parm.parm_value
   FROM
      utl_config_parm
   WHERE
      utl_config_parm.parm_name = aParmName;
     
   CURSOR lActionUserParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_action_user_parm.parm_name%TYPE) IS
   SELECT
      utl_action_user_parm.parm_value
   FROM
      utl_user
      INNER JOIN utl_action_user_parm ON
         utl_action_user_parm.user_id = utl_user.user_id
   WHERE
      utl_user.user_id = aUserId
      AND
      utl_action_user_parm.parm_name = aParmName;

   -- need to retrieve row with lowest role_order
   CURSOR lActionRoleParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_action_role_parm.parm_name%TYPE) IS
   SELECT
      parm_value
   FROM
      (
         SELECT
            utl_action_role_parm.parm_value
         FROM
            utl_user_role
            INNER JOIN utl_action_role_parm ON
               utl_action_role_parm.role_id = utl_user_role.role_id
         WHERE
            utl_user_role.user_id = aUserId
            AND
            utl_action_role_parm.parm_name = aParmName
      )
   WHERE
      ROWNUM = 1;
  
   CURSOR lActionGlobalParmCursor(aParmName utl_action_config_parm.parm_name%TYPE) IS
   SELECT
      utl_action_config_parm.parm_value
   FROM
      utl_action_config_parm
   WHERE
      utl_action_config_parm.parm_name = aParmName;
     
BEGIN
   lParmValue := NULL;

   -- The parm should be in the action parm tables, so check them first
  
   -- Get Action User Parm Value
   FOR lRec IN lActionUserParmCursor( aUserId, aParmName ) LOOP
      lParmValue := lRec.parm_value;
   END LOOP;

   -- If Action User Parm Value is not set, use Action Role Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lActionRoleParmCursor(aUserId, aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
    END IF;
  
   -- If Action Role Parm Value is not set, use Default Action Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lActionGlobalParmCursor(aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;
  
  
   -- If parm was not found in the action parm tables 
   -- then check the config parm tables

   -- Get User Parm Value
   FOR lRec IN lUserParmCursor( aUserId, aParmName ) LOOP
      lParmValue := lRec.parm_value;
   END LOOP;

   -- If User Parm Value is not set, use Role Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lRoleParmCursor(aUserId, aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;
  
   -- If Role Parm Value is not set, use Default Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lGlobalParmCursor(aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;

   RETURN lParmValue;
  
END getUserParmValue;
/

--changeSet OPER-1935:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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