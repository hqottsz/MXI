--liquibase formatted sql


--changeSet OPER-2947:1 stripComments:false
/**********************************************************************
* Ensure the Java version used for applets and thick-clients is 1.7
**********************************************************************/
UPDATE 
   utl_config_parm 
SET
   parm_value = 'clsid:CAFEEFAC-0017-0000-FFFF-ABCDEFFEDCBA',
   allow_value_desc = 'See http://docs.oracle.com/javase/7/docs/technotes/guides/jweb/applet/using_tags.html#classidattrib',
   default_value = 'clsid:CAFEEFAC-0017-0000-FFFF-ABCDEFFEDCBA',
   modified_in = '8.2'
WHERE
   parm_name = 'APPLET_CLASSID';   

--changeSet OPER-2947:2 stripComments:false
UPDATE 
   utl_config_parm 
SET
   parm_value = '1.7+',
   allow_value_desc = 'See https://docs.oracle.com/javase/7/docs/technotes/guides/jweb/applet/applet_deployment.html#JAVA_VERSION',
   default_value = '1.7+',
   modified_in = '8.2'
WHERE
   parm_name = 'CLIENT_JAVA_VERSION';