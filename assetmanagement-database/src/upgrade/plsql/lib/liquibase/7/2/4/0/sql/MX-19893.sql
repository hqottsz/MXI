--liquibase formatted sql


--changeSet MX-19893:1 stripComments:false
-- Delete the types assigned to the roles
DELETE FROM utl_alert_type_role t
 WHERE t.alert_type_id = 212;

--changeSet MX-19893:2 stripComments:false
-- Delete the alerts assigned to the users
DELETE FROM utl_user_alert t
 WHERE t.alert_id IN (SELECT utl_alert.alert_id
                        FROM utl_alert
                       WHERE utl_alert.alert_type_id = 212);

--changeSet MX-19893:3 stripComments:false
-- Delete the alert parms
DELETE FROM utl_alert_parm t
 WHERE t.alert_id IN (SELECT utl_alert.alert_id
                        FROM utl_alert
                       WHERE utl_alert.alert_type_id = 212);

--changeSet MX-19893:4 stripComments:false
-- Delete the alert log
DELETE FROM utl_alert_log t
 WHERE t.alert_id IN (SELECT utl_alert.alert_id
                        FROM utl_alert
                       WHERE utl_alert.alert_type_id = 212);

--changeSet MX-19893:5 stripComments:false
-- Delete the alert status log
DELETE FROM utl_alert_status_log t
 WHERE t.alert_id IN (SELECT utl_alert.alert_id
                        FROM utl_alert
                       WHERE utl_alert.alert_type_id = 212);

--changeSet MX-19893:6 stripComments:false
-- Delete the alerts
DELETE FROM utl_alert t
 WHERE t.alert_type_id = 212;

--changeSet MX-19893:7 stripComments:false
-- Delete the types
DELETE FROM utl_alert_type t
 WHERE t.alert_type_id = 212;