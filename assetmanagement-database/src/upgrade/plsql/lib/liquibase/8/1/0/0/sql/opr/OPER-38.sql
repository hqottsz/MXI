--liquibase formatted sql
   

--changeSet OPER-38:1 stripComments:false
   UPDATE UTL_MENU_GROUP SET
   ALL_USERS_BOOL = 0
   WHERE
   GROUP_NAME = 'Compliance Reports' AND
   ROLE_ID = 19000
;   

--changeSet OPER-38:2 stripComments:false
   UPDATE UTL_MENU_GROUP SET
   ALL_USERS_BOOL = 0
   WHERE
   GROUP_NAME = 'Maintenance Reports' AND
   ROLE_ID = 19000
;