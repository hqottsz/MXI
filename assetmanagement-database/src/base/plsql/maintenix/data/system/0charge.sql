--liquibase formatted sql


--changeSet 0charge:1 stripComments:false
/****************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE "CHARGE"
****************************************************/
INSERT INTO charge
 (
       charge_id,
       charge_code,
       charge_name,
       recoverable_bool,
       archive_bool,
       rstat_cd,
       revision_no,
       ctrl_db_id,
       creation_dt,
       creation_db_id,
       revision_dt,
       revision_db_id,
       revision_user
  )
  SELECT
        mx_key_pkg.new_uuid(),
        'BORROW',
        'BORROW CHARGES',
        0 AS recoverable_bool,
        0 AS archive_bool,
        0,
        0,
        0,
        SYSDATE,
        0,
        SYSDATE,
        0,
        USER
  FROM
      dual
  WHERE
      NOT EXISTS( SELECT 1 FROM charge WHERE charge_code = 'BORROW');              