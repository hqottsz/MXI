--liquibase formatted sql


--changeSet MX-17704:1 stripComments:false
DELETE FROM utl_help WHERE help_context = '/maintenix/web/user/CreateEditUser.jsp' and help_topic = 'htmlhelp.htm#U_Users,htm#proc-create_user';

--changeSet MX-17704:2 stripComments:false
INSERT INTO
   utl_help
   (
      HELP_CONTEXT, HELP_TOPIC, UTL_ID
   )
   SELECT '/maintenix/web/user/CreateEditUser.jsp', 'htmlhelp.htm#U_Users.htm#proc-create_user',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_help WHERE help_context = '/maintenix/web/user/CreateEditUser.jsp' );