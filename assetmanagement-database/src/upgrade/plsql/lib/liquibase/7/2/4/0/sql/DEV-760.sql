--liquibase formatted sql


--changeSet DEV-760:1 stripComments:false
UPDATE utl_config_parm t 
    SET t.parm_value = 'clsid:CAFEEFAC-0016-0000-FFFF-ABCDEFFEDCBA',
        t.DEFAULT_VALUE = 'clsid:CAFEEFAC-0016-0000-FFFF-ABCDEFFEDCBA'
    WHERE t.parm_name = 'APPLET_CLASSID';

--changeSet DEV-760:2 stripComments:false
UPDATE utl_config_parm t 
    SET t.parm_value = '1.6+',
        t.DEFAULT_VALUE = '1.6+'
    WHERE t.parm_name = 'CLIENT_JAVA_VERSION';