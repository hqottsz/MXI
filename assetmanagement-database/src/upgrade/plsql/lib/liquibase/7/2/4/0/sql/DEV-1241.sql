--liquibase formatted sql


--changeSet DEV-1241:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'INVOICE_TOLERANCE_FIXED', 0, 'LOGIC', 0, 'Acceptable fixed dollar difference between the fixed dollar and the invoice price.' , 'USER', 'Number', 0, 0, 'Core Logic', '8.0', 0 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'INVOICE_TOLERANCE_FIXED' );              

--changeSet DEV-1241:2 stripComments:false
 INSERT INTO
    UTL_CONFIG_PARM
    (
       parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
    )
    SELECT 'INVOICE_VALIDATE_JOB_DELAY', 24, 'LOGIC', 0, 'The delay (in hours) between invoice creation and automated verification of invoice to be marked for payment.' , 'USER', 'Number', 24, 0, 'Core Logic', '8.0', 0 
    FROM
       dual
    WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'INVOICE_VALIDATE_JOB_DELAY' );            

--changeSet DEV-1241:3 stripComments:false
 INSERT INTO 
   UTL_JOB (
   		job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id
   	   )
   SELECT 'MX_CORE_VALIDATE_OPEN_INVOICE', 'Evaluates all open invoices and when applicable marks them as ready for payment.', '00:00', null, 86400, 0, 0
   FROM
          dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_job WHERE job_cd = 'MX_CORE_VALIDATE_OPEN_INVOICE' );