--liquibase formatted sql

--changeSet OPER-15686:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'INCLUDE_IN_WORKING_CAPACITY', 'LOGIC', 'false', 'Flag indicating (by role) whether the role''s hours should be included for crew labour capacity calculations, and (globally) whether this feature should be active.  Set parm_value to TRUE to activate the feature.', 'ROLE', 'true', 'TRUE/FALSE', 'Capacity Planning', '8.2-SP5', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'INCLUDE_IN_WORKING_CAPACITY' );
