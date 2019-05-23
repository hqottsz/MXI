--liquibase formatted sql

--changeSet OPER-26964:1 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '512' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;
--changeSet OPER-26964:2 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '513' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;

--changeSet OPER-26964:3 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '514' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;

--changeSet OPER-26964:4 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '266' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;

--changeSet OPER-26964:5 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '267' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;

--changeSet OPER-26964:6 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '268' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;

--changeSet OPER-26964:7 stripComments:false
DELETE FROM utl_alert_type
WHERE
   utl_alert_type.alert_type_id = '610' AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert
      WHERE utl_alert.alert_type_id = utl_alert_type.alert_type_id
   ) AND
   NOT EXISTS (
      SELECT 1
      FROM utl_alert_type_role
      WHERE utl_alert_type_role.alert_type_id = utl_alert_type.alert_type_id
   )
;