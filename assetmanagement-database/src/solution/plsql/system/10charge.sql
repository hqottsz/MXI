/****************************************************
** 10-LEVEL INSERT SCRIPT FOR TABLE "CHARGE"
****************************************************/

INSERT INTO charge
 (
       charge_id,
       charge_code,
       charge_name,  
       recoverable_bool, 
       archive_bool,
       rstat_cd,
       ctrl_db_id,
       creation_db_id,
       revision_db_id
  )
  SELECT
        mx_key_pkg.new_uuid(),
        'CUSTOMS',
        'CUSTOMS', 
        0 AS recoverable_bool,
        0 AS archive_bool, 
        0,
        10,
        10,
        0
  FROM
      dual
  WHERE
      NOT EXISTS( SELECT 1 FROM charge WHERE charge_code = 'CUSTOMS')
      
      
 INSERT INTO charge
  (
        charge_id,
        charge_code,
        charge_name,  
        recoverable_bool, 
        archive_bool,
        rstat_cd,
        ctrl_db_id,
        creation_db_id,
        revision_db_id
   )
   SELECT
         mx_key_pkg.new_uuid(),
         'SHIPPING',
         'SHIPPING', 
         0 AS recoverable_bool,
         0 AS archive_bool, 
         0,
         10,
         10,
         0
   FROM
       dual
   WHERE
      NOT EXISTS( SELECT 1 FROM charge WHERE charge_code = 'SHIPPING')
      
  