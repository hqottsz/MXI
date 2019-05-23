-- assign ADMIN role to VENDOR alerts
INSERT INTO
   utl_alert_type_role ( alert_type_id, role_id, utl_id )
SELECT
   utl_alert_type.alert_type_id,
   utl_role.role_id,
   4650
FROM
   utl_alert_type
   CROSS JOIN utl_role
WHERE
   utl_alert_type.CATEGORY = 'VENDOR'
   AND
   utl_role.role_cd = 'ADMIN'
   AND
   NOT EXISTS (
      SELECT 1
        FROM utl_alert_type_role
       WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id AND
             utl_alert_type_role.role_id       = utl_role.role_id
   );


/**********************************
**Message Order Delivery Alert*****
***********************************/
INSERT INTO
   utl_alert_type_role
   (
      alert_type_id, role_id, utl_id
   )
SELECT
   602, 10019, 10
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM utl_alert_type_role WHERE utl_alert_type_role.alert_type_id = 602 AND utl_alert_type_role.role_id = 10019);


/*********************************
** For Flight APU Message - Usage Record Creation Failed - in GUI (Usage Record Message Error - in SQL) Alert Role - Assigns it to Tech Records Role*****
*********************************/
INSERT INTO
   utl_alert_type_role
   (
      alert_type_id, role_id, utl_id
   )
SELECT
   800, 10019, 10
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM utl_alert_type_role WHERE utl_alert_type_role.alert_type_id = 800 AND utl_alert_type_role.role_id = 10019);
