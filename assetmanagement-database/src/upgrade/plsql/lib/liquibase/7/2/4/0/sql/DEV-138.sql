--liquibase formatted sql


--changeSet DEV-138:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isAuthorizedForResource
(
   aUserId      utl_user.user_id%TYPE,
   aParmName    utl_config_parm.parm_name%TYPE
)  RETURN NUMBER
IS

  lParmValue utl_config_parm.parm_value%TYPE;
  lAuthorizedForResource NUMBER;
  
  CURSOR cur_user_parm(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
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

  CURSOR cur_role_parm(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
  SELECT
      utl_role_parm.parm_value
   FROM
      utl_user_role
      INNER JOIN utl_role_parm ON
         utl_role_parm.role_id = utl_user_role.role_id
   WHERE
      utl_user_role.user_id = aUserId
      AND
      utl_role_parm.parm_name = aParmName;
  
  CURSOR cur_config_parm(aParmName utl_config_parm.parm_name%TYPE) IS
  SELECT
     utl_config_parm.parm_value
  FROM
     utl_config_parm
  WHERE
     utl_config_parm.parm_name = aParmName;
BEGIN
  lParmValue := NULL;

  -- Get User Parm Value
  FOR r IN cur_user_parm( aUserId, aParmName ) LOOP
     lParmValue := r.parm_value;
  END LOOP;

  -- If User Parm Value is not set, use Role Parm Value
  IF lParmValue IS NULL THEN
    FOR r IN cur_role_parm(aUserId, aParmName) LOOP
       lParmValue := r.parm_value;
    END LOOP;
  END IF;
  
  -- If Role Parm Value is not set, use Default Parm Value
  IF lParmValue IS NULL THEN
    FOR r IN cur_config_parm(aParmName) LOOP
       lParmValue := r.parm_value;
    END LOOP;
  END IF;

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