--liquibase formatted sql


--changeSet MX-19986:1 stripComments:false
UPDATE utl_alert_type_role t
SET    t.utl_id  = 10
WHERE  t.utl_id  = 0      AND
       t.role_id = 19000  AND
       t.alert_type_id    IN (93, 94, 95, 96, 97, 98, 100, 101, 104);