--liquibase formatted sql


--changeSet OPER-8482:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- comment Check if csv is part of the allowed file upload types. 
/**
* The cycle count buttons allow a user to upload .csv files to update the cycle count results. 
* The config param FILE_UPLOAD_ALLOWED_TYPES was added as a security measure to prevent unrecognized file types from being uploaded. 
* However, "csv" was not in the default value of the config param and if the parm value does not contain it, 
* then a user will be prevented from uploading csv files which will break the cycle count functionality. 
* This upgrade script adds the file type "csv" to the allowed file types if at least one user has permission to use the cycle count buttons 
* (ACTION_LOAD_COUNT_RESULTS and/or ACTION_LOAD_FULL_COUNT_RESULTS) and it is necessary to add it.
**/

DECLARE 

   ln_csv_in_parm_value                INTEGER := 0;
   ln_user_has_cycle_count             INTEGER; 
   lv_parm_value                       utl_config_parm.parm_name%type;

BEGIN 

   -- find out if a user has permission to see cycle count buttons 
   SELECT
      NVL(SUM(COUNT(username)), 0)
   INTO 
      ln_user_has_cycle_count
   FROM 
      (
      SELECT 
         utl_user.username 
      FROM 
         utl_action_user_parm
      INNER JOIN utl_user ON 
         utl_user.user_id = utl_action_user_parm.user_id
      WHERE 
         (
            utl_action_user_parm.parm_name = 'ACTION_LOAD_COUNT_RESULTS' AND 
            utl_action_user_parm.parm_value LIKE 'true'
         ) 
         OR 
         (
            utl_action_user_parm.parm_name = 'ACTION_LOAD_FULL_COUNT_RESULTS' AND 
            utl_action_user_parm.parm_value LIKE 'true'
         ) 
      UNION 
      -- OR a user is assigned a role with the permission 
      SELECT 
         utl_user.username 
      FROM 
         utl_action_role_parm
      INNER JOIN utl_role ON   
         utl_role.role_id = utl_action_role_parm.role_id 
      INNER JOIN utl_user_role ON 
         utl_user_role.role_id = utl_role.role_id
      INNER JOIN utl_user ON 
         utl_user.user_id = utl_user_role.user_id
      WHERE 
         (
            utl_action_role_parm.parm_name = 'ACTION_LOAD_COUNT_RESULTS' AND 
            utl_action_role_parm.parm_value LIKE 'true'
         ) 
         OR 
         (
            utl_action_role_parm.parm_name = 'ACTION_LOAD_FULL_COUNT_RESULTS' AND 
            utl_action_role_parm.parm_value LIKE 'true'
         ) 
      )
   GROUP BY username;

   -- if a user has permission for the cycle count buttons
   -- then check if the allowed file types includes csv
   IF (ln_user_has_cycle_count > 0) THEN

      SELECT 
         parm_value 
      INTO
         lv_parm_value
      FROM 
         utl_config_parm
      WHERE 
         parm_name = 'FILE_UPLOAD_ALLOWED_TYPES';

      -- if the parm value is null, we use the default value which includes csv (since 8.2SP2u5)
      IF (lv_parm_value IS NULL) THEN 
         ln_csv_in_parm_value := 1;
      -- if the parm value is .* this allows any file type, therefore including csv
      ELSIF (lv_parm_value = '.*') THEN 
         ln_csv_in_parm_value := 1;
      -- otherwise, check if the parm value contains csv already 
      ELSIF (INSTR( lv_parm_value, 'csv') > 0) THEN
         ln_csv_in_parm_value := 1;
      END IF;

      -- if necessary, concatenate "csv" on to the parm value 
      IF (ln_csv_in_parm_value = 0) THEN
         UPDATE 
            utl_config_parm
         SET 
            parm_value = lv_parm_value || '|csv'
         WHERE 
            parm_name = 'FILE_UPLOAD_ALLOWED_TYPES';
      END IF;

   END IF;
   
END;
/
