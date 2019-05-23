--liquibase formatted sql


--changeSet 0utl_config_parm:1 stripComments:false
-- Cache Parameters.
/*************************************
*  Insert script for UTL_CONFIG_PARM *
**************************************/
/*************** Common **************/
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'USER_CACHE_TTL', '120000', 'SECURITY_CACHE', 0, 'This parameter is used to set the cache time-to-live in milliseconds for the list of users in the UTL_USER table.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 12000 , 1, 'Cache Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:2 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ROLE_CACHE_TTL', '120000', 'SECURITY_CACHE', 0, 'This parameter is used to set the cache time-to-live in milliseconds for the list of roles in the UTL_ROLE table.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', 'Number', 12000 , 1, 'Cache Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:3 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'USER_ROLE_ASSIGNMENT_CACHE_TTL', '120000', 'SECURITY_CACHE', 0, 'This parameter is used to set the cache time-to-live in milliseconds for the user-to-role mapping in the UTL_USER_ROLE table.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 12000 , 1, 'Cache Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:4 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'TRIGGER_CACHE_TTL', '0', 'TRIGGER_CACHE', 0, 'This parameter is used to set the cache time-to-live in milliseconds for the trigger definitions in UTL_TRIGGER.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 0 , 1, 'Cache Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:5 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'USER_RESOURCE_AUTH_CACHE_TTL', '60000', 'SECURITY_CACHE', 0, 'This parameter is used to set the cache time-to-live in milliseconds for the user-to-resource authorization mapping in the UTL_CONFIG_PARM and UTL_USER_PARM tables.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 60000 , 1, 'Cache Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:6 stripComments:false
-- Work Item Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'COMPLETED_WORK_ITEM_TTL', '1', 'WORK_MANAGER', 0, 'This parameter is used to set the time-to-live in hours for completed Work Items in the UTL_WORK_ITEM table.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 1 , 1, 'Work Manager Parameter', '6.8.12', 0  );

--changeSet 0utl_config_parm:7 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'REALTIME_ACFTDEADLINE_WORKITEM_DELAY', '10', 'WORK_MANAGER', 0, 'The minutes the work item will wait before execution. Used to eliminate excessive processing for the same aircrafts.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL',  'Number', 10, 1, 'Work Manager Parameter', '6.8.12', 0  );

--changeSet 0utl_config_parm:8 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MAX_WORK_ITEMS_SCHEDULED_AT_ONCE', '50', 'WORK_MANAGER', 0, 'This parameter limits the number of work items of a type that can be scheduled at any given iteration of the WorkItemDispatcher.  It is used to allow for better load-balancing of work items across multiple application servers.', 'GLOBAL',  'Number', 50 , 1, 'Work Manager Parameter', '7.2 alpha', 0  );

--changeSet 0utl_config_parm:9 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WORK_ITEM_IDLE_SLEEP_TIME_MILLISECONDS', '10000', 'WORK_MANAGER', 0, 'The amount of time in milliseconds that the work item scheduler sleeps if there was nothing to do in the previous iteration.', 'GLOBAL',  'Non-negative integer.', '10000' , 1, 'Work Manager Parameter', '8.0', 0  );

--changeSet 0utl_config_parm:10 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WORK_ITEM_BATCH_SLEEP_TIME_MILLISECONDS', '500', 'WORK_MANAGER', 0, 'The amount of time in milliseconds that the scheduler sleeps between scheduling batches of work items.  This value should be 0 or small in a single server environment. It should be large enough in a multi-server environment to give all servers a fair chance of scheduling work. Starvation will occur in a multi-server environment as this value approaches zero or if inactive sleep time is significantly larger than sleep time between batches', 'GLOBAL',  'Non-negative integer.', '500' , 1, 'Work Manager Parameter', '8.0', 0  );

--changeSet 0utl_config_parm:11 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WORK_ITEM_ERROR_SLEEP_TIME_MILLISECONDS', '10000', 'WORK_MANAGER', 0, 'The amount of time in milliseconds that the scheduler sleeps in case of an infrastructure error such as temporary db outage.', 'GLOBAL',  'Non-negative integer.', '10000' , 1, 'Work Manager Parameter', '8.0', 0  );

--changeSet 0utl_config_parm:12 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WORK_ITEM_MAX_PROCESSING_TIME_IN_MINUTES', '60', 'WORK_MANAGER', 0, 'Maximum time in minutes that may be spent processing a work item before the server is deemed off line.', 'GLOBAL',  'Non-negative integer.', '60' , 1, 'Work Manager Parameter', '8.1-SP2', 0  );

--changeSet 0utl_config_parm:13 stripComments:false
-- Integration Parameters
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INTEGRATION_MSG_RETRY','INTEGRATION','2', 0,'The number of attempts to emit a web service notification message.','GLOBAL', 'Number', '2', 1, 'Integration', '0712', 0);

--changeSet 0utl_config_parm:14 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INTEGRATION_MSG_RETRY_INTERVAL','INTEGRATION','30', 0,'The interval (in seconds) between attempts at emitting a web service notification message.','GLOBAL', 'Number', '30', 1, 'Integration', '0712', 0);

--changeSet 0utl_config_parm:15 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INTEGRATION_MSG_TIMEOUT','INTEGRATION','10000', 0,'The duration (in seconds) at the sending of a web service notification message will timeout.','GLOBAL', 'Number', '10000', 1, 'Integration', '0712', 0);

--changeSet 0utl_config_parm:16 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MESSAGE_ORDER_TIMEOUT','INTEGRATION','60', 0,'The maximum amount of time (in seconds) that the Message Ordering Processor will wait before skipping the next expected message.','GLOBAL', 'Number', '60', 1, 'Integration', '6.9.1', 0);

--changeSet 0utl_config_parm:17 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MESSAGE_ORDER_SLEEP_TIME','INTEGRATION','500', 0,'This value represents the amount of time (in milliseconds) that the Message Ordering Processor will wait before repeating its cycle of acquiring a database lock.','GLOBAL', 'Number', '500', 1, 'Integration', '6.9.1', 0);

--changeSet 0utl_config_parm:18 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('OUTBOUND_MESSAGE_QUEUE','INTEGRATION','com.mxi.mx.jms.OutboundIntegrationQueue', 0,'This value represents the JNDI name of the Outbound Message Queue to be used.','GLOBAL', 'JNDI name', 'com.mxi.mx.jms.OutboundIntegrationQueue', 1, 'Integration', '6.9.1', 0);

--changeSet 0utl_config_parm:19 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ASB_ADAPTER_NOTIFICATION_MESSAGE_QUEUE','ASB','com.mxi.mx.jms.AsbAdapterNotificationQueue', 0,'This value represents the JNDI name of the asb adapter notifcation  message queue to be used.','GLOBAL', 'JNDI name', 'com.mxi.mx.jms.AsbAdapterNotificationQueue', 1, 'ASB', '8.0', 0);

--changeSet 0utl_config_parm:20 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'INCOMING_ADAPTER_XML_VALIDATION', 'LOGIC', 'TRUE', 0, 'This parameter is used enforce validation of incoming requests.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Integration', '8.0', 0 );

--changeSet 0utl_config_parm:21 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'OUTGOING_ADAPTER_XML_VALIDATION', 'LOGIC', 'TRUE', 0, 'This parameter is used enforce validation of outgoing responses.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Integration', '8.0', 0 );

--changeSet 0utl_config_parm:22 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALLOW_DUPLICATE_HISTORIC_FLIGHT_MESSAGES', 'LOGIC', 'FALSE', 0, 'Determines whether historic flight messages for the same flight are permitted within the flight adaptor.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Integration', '8.0-SP2', 0 );

--changeSet 0utl_config_parm:23 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'INT_MSG_WAIT_TIMEOUT', '10', 'INTEGRATION', 0, 'This parameter is used set a time limit for waiting for a database commit before delivering an integration message.', 'GLOBAL', 'Number', '10', 1, 'Integration', '8.1-SP1', 0  );

--changeSet 0utl_config_parm:24 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MEASUREMENT_UPDATE_MODE', 'LOGIC', 'SYNCHRONIZE', 0, 'This parameter is used to determine how measurements are processed: SYNCHRONIZE which is a flush and fill operation or ADDITIVE which adds measurements that do not exist and updates existing measurements.', 'GLOBAL', 'SYNCHRONIZE/ADDITIVE', 'SYNCHRONIZE', 1, 'Integration', '8.1-SP1', 0 );

--changeSet 0utl_config_parm:25 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'FLIGHT_ADAPTER_PUBLISH_MEL_ONLY', 'LOGIC','This parameter is used to control publishing the fault of MEL severity type.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'Flight Adapter', '8.2', 0);

--changeSet 0utl_config_parm:26 stripComments:false
INSERT INTO utl_config_parm (parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER', 'LOGIC', 'TRUE', 0, 'Determines whether to add additional wrapper to outbound message.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Integration Message', '8.2-SP2', 0 );

--changeSet 0utl_config_parm:27 stripComments:false
INSERT INTO utl_config_parm (parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id )
VALUES ('SPEC2000_AEROBUY_OUTBOUND_MESSAGE_WRAPPER_ENVELOP', 'LOGIC', '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><soapenv:Body><aer:receiveRequest xmlns:aer="aeroxchange.com"><inXMLData><![CDATA[{0}]]></inXMLData></aer:receiveRequest></soapenv:Body></soapenv:Envelope>', 0, 'Aerobuy outbound messsage wrapper envelop.', 'GLOBAL', 'Aerobuy outbound messsage wrapper envelop.', '<aer:receiveRequest xmlns:aer="aeroxchange.com"><inXMLData><![CDATA[{0}]]></inXMLData></aer:receiveRequest>', 1, 'Integration Message', '8.2-SP2', 0);

--changeSet 0utl_config_parm:28 stripComments:false
INSERT INTO utl_config_parm (parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id )
VALUES ('SPEC2000_AEROBUY_ORDER_EXCEPTIONS_INDICATOR_POLLING', 'LOGIC', '5000', 0, 'Determines the refresh frequency (ms) for the Order Exceptions Indicator.', 'GLOBAL', 'NUMBER', '5000', 1, 'Integration', '8.2-SP2', 0);

--changeSet 0utl_config_parm:29 stripComments:false
INSERT INTO utl_config_parm (parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, allow_value_desc, default_value, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'SPEC2000_ACN_PROCESSING', 'INTEGRATION', 'NONE', 0, 'Determines a proceesing action for ACN in ATA_SparesOrder SPEC2000 message.', 'GLOBAL', 'LEFT_5_ALPHA_NUMERIC_CHARS - Uses 5 alphanum chars from left and removes other chars like dashes, RIGHT_5_ALPHA_NUMERIC_CHARS - Uses 5 alphanum chars from right and removes other chars like dashes, NUMERIC_CHARS - Uses only numeric chars, NONE - Does not change the ACN', 'NONE', 1, 'Integration', '8.2-SP2', 0);

--changeSet 0utl_config_parm:30 stripComments:false
-- Security Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'PASSWORD_EXPIRY_DAYS', '90', 'SECURITY', 0, 'This parameter is used set a time limit on how long passwords have until they expire.  This parameter cannot be overridden by user and/or role values. Set this value to -1 to disable password expiration.', 'GLOBAL', 'Number', NULL, 1, 'Security Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:31 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'FORCE_CHANGE_PASSWORD', 'FALSE', 'SECURITY', 0, 'This parameter is used enforce users to change their password when the account is newly created.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', 'TRUE/FALSE', NULL, 1, 'Security Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:32 stripComments:false
-- Database Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'DB_USERNAME', '${db_username}', 'DBINFO_SERVLET', 0, 'This parameter is used to set the database username that thick-client applications use to connect to the Maintenix database.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', '', 'none', 1, 'Database Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:33 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'DB_PASSWORD', '${db_password}', 'DBINFO_SERVLET', 1, 'This parameter is used to set the database password that thick-client applications use to connect to the Maintenix database.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', '', 'none', 1, 'Database Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:34 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'DB_DATABASE', '${db_database}', 'DBINFO_SERVLET', 0, 'This parameter is used to set the database OCI connect string that non-Java thick-client applications use to connect to the Maintenix database.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', '', 'none', 1, 'Database Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:35 stripComments:false
-- SMTP Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'SMTP_USERNAME', NULL, 'EMAIL_ENGINE', 0, 'This parameter is used to set the SMTP login username required to connect to the SMTP server used by the e-mail engine.  If the SMTP server does not require authentication the value should be left blank.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', '', 'none', 0, 'SMTP Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:36 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc , CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'SMTP_PASSWORD', NULL, 'EMAIL_ENGINE', 1, 'This parameter is used to set the SMTP login password required to connect to the SMTP server used by the e-mail engine.  If the SMTP server does not require authentication the value should be left blank.  This parameter cannot be overridden by user and/or role values.' , 'GLOBAL', '', 'none', 0, 'SMTP Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:37 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ADMIN_EMAIL_ADDR', 'admin@maintenix.com', 'SYSTEM', 0, 'This parameter is used to set the System Administrator e-mail address to send system notifications to.  This parameter cannot be overridden by user and/or role values.' , 'GLOBAL', '', 'none', 1 , 'SMTP Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:38 stripComments:false
-- Alert Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALERT_ENGINE_ACTIVE', 'true', 'ALERT_ENGINE', 0, 'This parameter is used to determine whether or not the alert engine is currently active and should generate fs.  This parameter cannot be overridden by user and/or role values.  Possible values are:' || chr(10) || 'true - Alert engine is active' || chr(10) || 'false - Alert engine is not active' , 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Alert Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:39 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALERT_REPLY_EMAIL_ADDR', 'alertnotifier@maintenix.com', 'ALERT_ENGINE', 0, 'This parameter is used to set the e-mail address that will appear as the reply-to e-mail address in all Maintenix alert e-mail messages.  This parameter cannot be overridden by user and/or role values.' , 'GLOBAL', 'Valid email address', 'none', 1, 'Alert Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:40 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'SEND_EMAIL_ALERTS', 'true', 'ALERT_ENGINE', 0, 'This parameter is used to set whether e-mail alerts should be sent to users when alerts are triggered.  This parameter cannot be overridden by user and/or role values.  Possible values are:' || chr(10) || 'true - Users will be alerted by e-mail' || chr(10) || 'false - Users will not be alerted by e-mail', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Alert Parameter', '5.1', 0  );

--changeSet 0utl_config_parm:41 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALERT_USER_FILTER', '0', 'ALERT_FILTER', 0, 'Priority filter. User will see only alert types with ALERT_PRIORITY higher or equal to this value', 'USER', 'Number', '0', 1, 'Alert Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:42 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MAT_ADAPT_ALERT_ROLE', '', 'LOGIC', 0, 'The Role defined by UTL_ROLE.ROLE_CD to notify with alerts relating to the Materials Adapter.', 'GLOBAL', 'String', '', 1, 'Alert Parameter', '5.1.5.4', 0 );

--changeSet 0utl_config_parm:43 stripComments:false
-- FSR
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_FLIGHT_STAGE_MANDATORY_ON_SAVE','LOGIC','false', 0,'Whether the Flight Stage is mandatory','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:44 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_FLIGHT_STAGE_MANDATORY_ON_CLOSE','LOGIC','false', 0,'Whether the Flight Stage is mandatory before the Event may be Closed','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:45 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_EVENT_LOC_MANDATORY_ON_SAVE','LOGIC','false', 0,'Whether the Event Location is mandatory','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:46 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_SUBJECT_MANDATORY_ON_SAVE','LOGIC','false', 0,'Whether the Subject is mandatory','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:47 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_CATEGORY_MANDATORY_ON_SAVE','LOGIC','false', 0,'Whether the Category is mandatory','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:48 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_SUBJECT_MANDATORY_ON_CLOSE','LOGIC','false', 0,'Whether the Subject is mandatory for the Event to be Closed','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:49 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_CATEGORY_MANDATORY_ON_CLOSE','LOGIC','false', 0,'Whether the Category is mandatory for the Event to be Closed','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:50 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EE_FLIGHT_MANDATORY_ON_SAVE','LOGIC','false', 0,'Whether flight information ( To and From location) is mandatory saving event','GLOBAL', 'true/false', 'false', 1, 'FSR', '0803', 0);

--changeSet 0utl_config_parm:51 stripComments:false
-- HTML parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ENCRYPT_PARAMETERS', 'true', 'MXCOMMONWEB', 0, 'This parameter is used to set whether the web application URL parameter encryption should be used.  This parameter cannot be overridden by user and/or role values.  Possible values are:' || chr(10) || 'true - Protected URL parameters will be encrypted and will not be re-useable' || chr(10) || 'false - Protected URL parameters will be shown in plaintext and will be re-useable', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '5.1', 0 );

--changeSet 0utl_config_parm:52 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WELCOME_PAGE', 'index.jsp', 'MXCOMMONWEB', 0, 'This parameter is used to set the default start page for the desktop web application.  This parameter may be overridden by user and/or role values.' , 'USER', '', 'none', 1 , 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:53 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MOBILE_WELCOME_PAGE', 'mobile.jsp', 'MXCOMMONWEB', 0, 'This parameter is used to set the default start page for mobile Maintenix clients.  This parameter may be overridden by user and/or role values.' , 'USER', '', 'mobile.jsp', 1 , 'HTML Parameter', '8.1-SP1', 0 );

--changeSet 0utl_config_parm:54 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'LOGOUT_PAGE', '/common/security/logout.jsp', 'MXCOMMONWEB', 0, 'This parameter is used to set the default logout page for the desktop web application.  This parameter cannot be overridden by user and/or role values.  If you are referring to a page within the current webapp do not include the context path.  Instead simply start with a slash.  If you are referring to a page outside the current webapp, construct a full proper URL.' , 'GLOBAL', '', 'none', 1 ,'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:55 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'POCKET_WELCOME_PAGE', '/maintenix/common/PocketToDoList.jsp', 'MXCOMMONWEB', 0, 'This parameter is used to set the default start page for the pocket browser web application.  This parameter may be overridden by user and/or role values.' , 'USER', '', 'none', 1, 'HTML Parameter', '0609', 0 );

--changeSet 0utl_config_parm:56 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'USE_CUSTOM_START_PAGE', 'true', 'MXCOMMONWEB', 0, 'This parameter is used to set whether a user is redirected to their custom start page when navigating to the web application home.  This parameter cannot be overridden by user and/or role values.  Possible values are:' || chr(10) || 'true - Users will be redirected to the WELCOME_PAGE as defined in this table when navigating to the web application home' || chr(10) || 'false - Users will be redirected to the default home page (index.jsp) when navigating to the web application home', 'USER', '', 'none', 1, 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:57 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'TREE_TABLE_SIZE_LIMIT', '200', 'MXCOMMONWEB', 0, 'This parameter is used to set the row size limit on the TreeTable.  If the number of rows returned is greater than this value, then the flat table, with parent/child grouping, will be used instead.', 'GLOBAL', '', 200, 1, 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:58 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MAX_TREE_TABLE_SIZE_LIMIT', '500', 'MXCOMMONWEB', 0, 'This parameter is used to set the maximum row size limit on the TreeTable. If the number of rows returned is greater than this value, then the Table class should be used instead. Otherwise the system may fail to display TreeTable as a flat list.', 'GLOBAL', '', 500, 1, 'HTML Parameter', '6.8.6', 0 );

--changeSet 0utl_config_parm:59 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ERROR_EMAIL_LIST', NULL, 'MXCOMMONWEB', 0, 'This parameter is used to setup email address that Maintenix Error Reports will be emailed to when uncaught exceptions occur. The value should contain a comma-separated list of valid email addresses.', 'GLOBAL', '', 'none', 1, 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:60 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'sAutoRefreshMode', 'AUTO', 'SESSION', 0, 'This parameter is used to set the auto-refresh mode for web application pages that have the refresh option turned on.  This parameter may be overridden by user and/or role values.  Possible values are:' || chr(10) || 'ON - Pages will always auto-refresh if they have this option turned on' || chr(10) || 'OFF - Pages will never auto-refresh even if they have this option turned on' || chr(10) || 'AUTO - Pages will always auto-refresh based on the user cookie setting for each page' , 'USER', '', 'none', 1, 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:61 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ('MAX_NUMBER_REGISTERED_OBJECTS', '500', 'SYSTEM', 0, 'This parameter is used to set the maximum number of objects registered on the session.  This parameter cannot be overridden by user and/or role values.', 'GLOBAL', '', 'none', 1, 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:62 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'ENFORCE_SIGNATURES', 'LOGIC','If true,  turns on the "Password Authorization" window for all actions that are configured to prompt user for password.','USER', 'TRUE/FALSE', 'TRUE', 0, 'HTML Parameter', '0706', 0);

--changeSet 0utl_config_parm:63 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALLOW_CHANGE_PASSWORD', 'true', 'MXCOMMONWEB', 0, 'This parameter is used to control the users ability to change their password.  This parameter cannot be overridden by user and/or role values.  Possible values are:' || chr(10) || 'true - Users will be allowed to change their password' || chr(10) || 'false - No Change Password link will appear in the Options menu dropdown', 'GLOBAL', 'true/false', 'true', 1 , 'HTML Parameter', '5.1', 0 );

--changeSet 0utl_config_parm:64 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MAXIMUM_POST_DATA_SIZE', '10485760', 'MXCOMMONWEB', 0, 'This parameter specifies the maximum value for uploaded files.', 'GLOBAL', 'Number in bytes', 10485760, 1, 'System', '5.1.4', 0  );

--changeSet 0utl_config_parm:65 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'SCROLLABLE_TABLE_ROWS', 20, 'MXCOMMONWEB', 0, 'This parameter specifies the number of rows a table needs before it will become scrollable/resizable.', 'GLOBAL', 'Number', 20, 1, 'System', '0609', 0  );

--changeSet 0utl_config_parm:66 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'SCROLLABLE_TABLE_DEFAULT_HEIGHT', 400, 'MXCOMMONWEB', 0, 'This parameter specifies the default height of scrollable tables.', 'GLOBAL', 'Number', 400, 1, 'System', '0609', 0  );

--changeSet 0utl_config_parm:67 stripComments:false
INSERT INTO utl_config_parm (parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('HTTP_CACHE_EXPIRY', null, 'MXCOMMONWEB', 0, 'This parameter specifies the value in seconds for the HTTP header "Cache-Control: max-age=kit
" for image, JS, and CSS files.', 'GLOBAL', null, 'Number', 1, 'System', '0609', 0);

--changeSet 0utl_config_parm:68 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MOBILE_REGEXP', '.*(iPad).*', 'MXCOMMONWEB', 0, 'This parameter is used to identify httprequest from mobile device.' , 'USER', '', '.*(iPad).*', 1 , 'HTML Parameter', '8.0-SP1', 0 );

--changeSet 0utl_config_parm:69 stripComments:false
-- FileUpload validation checks
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FILE_UPLOAD_FILENAME_LENGTH', '80', 'MXCOMMONWEB', 'Determines the valid length for uploaded file names.', 'GLOBAL', '80', 'Number', 1, 'HTML Parameter', '8.2-SP2', 0);

--changeSet 0utl_config_parm:70 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FILE_UPLOAD_ALLOWED_CHARACTERS', '^[a-zA-Z0-9_()+\\-\\s]+$', 'MXCOMMONWEB', 'Determines the valid characters for uploaded file names.', 'GLOBAL', '^[a-zA-Z0-9_()+\\-\\s]+$', 'A Java regular expression pattern (java.util.regex.Pattern) that will match the allowed characters in the filename that Maintenix should accept for upload.', 1, 'HTML Parameter', '8.2-SP2', 0);

--changeSet 0utl_config_parm:71 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FILE_UPLOAD_ALLOWED_TYPES', '(?i)^(bmp|csv|doc|docx|gif|jpeg|jpg|msg|pdf|png|ppt|pptx|rtf|txt|xls|xlsx)$', 'MXCOMMONWEB', 'The list of allowable file extensions for files uploaded to Maintenix, where each file extension is separated by the pipe character ("|").', 'GLOBAL', '(?i)^(bmp|csv|doc|docx|gif|jpeg|jpg|msg|pdf|png|ppt|pptx|rtf|txt|xls|xlsx)$', 'A Java regular expression pattern (java.util.regex.Pattern) that will match the file extension of files that Maintenix should accept for upload. A value of ".*" will accept any file extension.', 1, 'HTML Parameter', '8.2-SP2', 0);

--changeSet 0utl_config_parm:72 stripComments:false
-- Report parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MIN_REMAINING_LABOUR_HOURS_FOR_TASK', '1', 'REPORT', 0, 'This parameter is used to set the minimum number of remaining labour hours in a task, below which the tasks will not be shown in the Production Planning reports.' , 'GLOBAL', 'Number', '1', 1 , 'Report Parameter', '0609', 0 );

--changeSet 0utl_config_parm:73 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'IETM_OVERLAY_REPORT', null, 'REPORT', 0, 'Overlay report name for IETMs attached to task cards.' , 'GLOBAL', 'String', null, 1 , 'Report Parameter', '6.2.6', 0 );

--changeSet 0utl_config_parm:74 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('Detail', 'sReportExcelBaseAreaType', 'SESSION', 'Identifies the base area in a report when exporting to Excel.', 'USER', '', 'Detail', 0, 'Report Export Option', '0712', 0);

--changeSet 0utl_config_parm:75 stripComments:false
-- Notification Area Parameters
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'NOTIFICATION_AREA_UPDATE_INTERVAL', '0', 'MXCOMMONWEB', 0, 'Sets the polling interval (in milliseconds) between updates of the alert notification area, except for the following special cases: if the value is negative, the notification area is never updated (and thus never visible); if zero, the notification area is updated just once, on page load; and if between 0 and 5000 exclusive, the notification area is updated at the minimum interval of every 5000.' , 'USER', 'NUMBER', '0', 1, 'System', '6.8.12-SP1', 0 );

--changeSet 0utl_config_parm:76 stripComments:false
/*************** Core **************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_PO_PRIORITY_MAP', 'AOG-10,WSP-20,USR-40', 'LOGIC', 0, 'This parameter specifies the PO priority order to the SPEC200 PRI element code.  The format is a comma delimeted list of code, dash, then maximum order for that code.  The list must be specified in an incrementing order.', 'GLOBAL', 'String pattern', 'AOG-10,WSP-20,USR-40', 1, 'Core Logic', '5.1.5', 0);

--changeSet 0utl_config_parm:77 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_FAULT_RECURRENCE_DAYS', '2000', 'LOGIC', 0,'The number of days in history that will be searched for a latest recurring fault instance that is related to a newly created possible fault. Allowable Values: Number', 'GLOBAL', 'Number', 2000, 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:78 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ADHOC_CORRTASK_LABOUR', 'LBR', 'LOGIC', 0, 'If provided labour skill will be assigned to Faults. Allowable Values: valid labour_skill_cd','GLOBAL', 'valid labour_skill_cd', 'LBR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:79 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ADHOC_TASK_LABOUR', 'LBR', 'LOGIC', 0,'If provided labour skill will be assigned to ad-hoc tasks (excluding adhoc corrective tasks). Allowable Values: valid labour_skill_cd','GLOBAL', 'valid labour_skill_cd', 'LBR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:80 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, CONFIG_TYPE, PARM_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLANK_CHECK_SIGNATURE', 'AET,INSP', 'LOGIC', 0, 'GLOBAL', 'If provided labour skill will be assigned to blank check tasks (excluding  auto complete tasks ex WorkPkg, QuickCheck). Allowable Values: valid labour_skill_cd', 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:81 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, CONFIG_TYPE, PARM_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLANK_RO_SIGNATURE', 'AET,INSP', 'LOGIC', 0, 'GLOBAL', 'If provided labour skill will be assigned to blank work order tasks (excluding  auto complete tasks ex WorkPkg, QuickCheck). Allowable Values: valid labour_skill_cd','Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:82 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, CONFIG_TYPE, PARM_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLANK_OPEN_MPC_SIGNATURE', 'INSP', 'LOGIC', 0, 'GLOBAL', 'If provided labour skill will be assigned to MPC tasks. Allowable Values: valid labour_skill_cd', 'Core Logic', '0803', 0);

--changeSet 0utl_config_parm:83 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, CONFIG_TYPE, PARM_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLANK_CLOSE_MPC_SIGNATURE', 'INSP', 'LOGIC', 0, 'GLOBAL', 'If provided labour skill will be assigned to MPC tasks. Allowable Values: valid labour_skill_cd', 'Core Logic', '0803', 0);

--changeSet 0utl_config_parm:84 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_AOG_FAULT','LOGIC','ERROR', 0,'Severity of the validation for AOG faults on the aircraft/component or any of its sub-components that are not assigned to any check/work order on that are currently IN WORK or INSPREQ.  Allowable Values OK, WARN, ERROR (case sensitive)','USER', 'WARN/ERROR/OK', 'ERROR', 1,  'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:85 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_LOW_SEV_OPEN_FAULT','LOGIC','WARN', 0,'Severity of the validation for low severity open faults on the aircraft/component or any of its sub-components that are not assigned to any check/work order on that are currently IN WORK or INSPREQ.  Allowable Values OK, WARN, ERROR (case sensitive)','USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:86 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_MISSING_COMPONENT','LOGIC','ERROR', 0,'If you try to close a work package for an aircraft or component that is missing mandatory sub-components, the ERROR or WARN setting for this parameter determines whether you can proceed. ERROR: you cannot complete the work package (unless there is another IN WORK work package against the aircraft or component). WARN: you see a warning, but can complete the work package. OK: do not use. For incomplete components, use the complete work package as unserviceable or ready for build workflows.','USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '8.2-SP5', 0);

--changeSet 0utl_config_parm:87 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_NEAR_DUE_FAULT','LOGIC','WARN', 0,'Severity of the validation for non-historic faults on the aircraft/component and any of sub-components that are not assigned to any check/work order that are currently IN WORK or INSPREQ.See: RELEASE_NOTIFY_INTERVAL Allowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'WARN', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:88 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_NEAR_DUE_TASK','LOGIC','WARN', 0, 'Severity of the validation for non-historic task on the aircraft/component and any of sub-components that are not assigned to any check/work order that are currently IN WORK or INSPREQ.See: RELEASE_NOTIFY_INTERVALAllowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'WARN', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:89 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_OVERDUE_FAULT','LOGIC','ERROR', 0,'Severity of the validation for non-historic faults on the aircraft/component or any of its sub-components that are not assigned to any check/work order that are currently IN WORK or INSPREQ and whose deadline is overdue.Allowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:90 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_OVERDUE_TASK','LOGIC','ERROR', 0,'Severity of the validation for non-historic tasks on the aircraft/component or any of its sub-components that are not assigned to any check/work order that are currently IN WORK or INSPREQ and whose deadline is overdue.Allowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:91 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_UNCOLLECTED_JOB_CARDS','LOGIC','WARN', 0,'Severity of the validation for completing a check/work order that has uncollected job cards remaining.Allowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '6.2.1', 0);

--changeSet 0utl_config_parm:92 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('RELEASE_NOTIFY_INTERVAL','LOGIC', 5, 0,'Only tasks or faults that have predicted days remaining less or equal to this value, will be used in validation described for parametersRELEASE_NEAR_DUE_FAULT,RELEASE_NEAR_DUE_TASKAllowable Values: Number', 'USER', 'number', 5, 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:93 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENFORCE_QUALIFICATION','LOGIC', 'FALSE', 0,'If true, we will enforce labour qualification. when labour is assigned, or when the qualification changes. Allowable Values: TRUE, FALSE (Case sensitive)', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:94 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENFORCE_LICENSE','LOGIC', 'FALSE', 0,'If true, we will enforce labour to have valid license when labour is assigned, or when the license changes.Allowable Values: TRUE, FALSE (Case sensitive)','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:95 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('NEXT_DUE_INTERVAL', '10', 'LOGIC', 0,'The number of days in the future to look for upcoming due scheduled maintenance. Allowable Values: Number', 'GLOBAL', 'Number', 10, 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:96 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BASELINESYNCH_HR_SIGNOFF_HR_CD', 'LOGIC', 'SYSTEM', 0, 'Indicates the HR user that should be used to sign off task cancellations, stage notes, etc.  This should already exist-setup by the services implementation baseline setup', 'GLOBAL', 'Username', 'SYSTEM', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:97 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MONTHLY_DEMAND_START_DT', 'LOGIC', '1999-01-01 3:19:15 PM', 0, 'Start date monthly demand parameter for demand consumption calculation. ', 'GLOBAL', '', 'Date format: YYYY-MM-DD HH12:MI:SS AM', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:98 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_PASSWORD', 'LOGIC', 'password', 1, 'This is the default password used when creating a new user and resetting a users password.', 'GLOBAL', '', 'String', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:99 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TODO_LIST_URL', 'LOGIC', '/maintenix/common/ToDoList.jsp', 0, 'URL to the todo list page in maintenix.', 'GLOBAL', '/maintenix/common/ToDoList.jsp', 'String', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:100 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PRINT_TICKET_INTERVAL', '24', 'LOGIC', 0,'Represents the number of hours before the part request due date to lock the part request and print the issue ticket. The part will no longer be able to be stolen after it is locked.', 'GLOBAL', 'Number', 24, 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:101 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('REAUTHORIZE_PO', 'FALSE', 'LOGIC', 0,'This config parameter turns on logic that forces a purchase order to be re-authorized if certain properties are changed after the purchase order has already been authorized.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:102 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SHOW_CHARGE_TO_ACCOUNT_FOR_NON_LOCAL_PO', 'FALSE', 'LOGIC', 0, 'This config parameter turns on logic that forces an expense account to be chosen when create purchase order or purchase order line for an organization with non-local owner.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.2', 0);

--changeSet 0utl_config_parm:103 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_SILENT_PRINTING', 'FALSE', 'LOGIC', 0,'This config parameter turns on logic that performs automated ticket printing.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:104 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('REAUTHORIZE_PO_PRICE_TOLERANCE', '0', 'LOGIC', 0,'Difference, in percentage, between the previous price and the new price of a modified purchase order, above which Maintenix assesses whether the purchase order must be reapproved. To specify a price difference of 10% as the value, type 0.1.', 'GLOBAL', 'Number', '0', 0, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:105 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENFORCE_MIN_PO_QTY', 'FALSE', 'LOGIC', 0, 'When set to true, will automatically set purchase order quantity for new orders to the minimum quantity defined in the vendor prices.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.1-SP2', 0);

--changeSet 0utl_config_parm:106 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_REASON_FOR_REMOVAL', 'IMSCHD', 'LOGIC', 0,'The default reason for removal when creating part requests.', 'GLOBAL', 'Remove reason code.', 'IMSCHD', 1, 'Core Logic', '0706', 0);

--changeSet 0utl_config_parm:107 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_RSRV_MAX_TRY_COUNT', '5', 'LOGIC', 0, 'The maximum number of times processing of an autoreservation should be attempted. Allowable Values: Number', 'GLOBAL', 'Number', 5, 0, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:108 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_RSRV_ROW_PROCESS_COUNT', '20', 'LOGIC', 0, 'The maximum number of rows in auto_rsrv_queue for which processing of autoreservation will be attempted. Allowable Values: Number', 'GLOBAL', 'Number', 20, 0, 'Core Logic', '6.8.9', 0);

--changeSet 0utl_config_parm:109 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAINTENIX_IDENTIFICATION_CODE', 'MX-4650', 'LOGIC', 0, 'The identification code for the current Maintenix installation.', 'GLOBAL', 'STRING', NULL, 0, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:110 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('END_DATE_COMPLETION_THRESHOLD', '-1', 'LOGIC', 0,'Controls the number of hours in the future that a tasks end date is allowed to be upon completion of the task. Negative number disables this validation.', 'GLOBAL', 'Number', '0', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:112 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'AUTO_ISSUE_INVENTORY', 'LOGIC','Controls auto-issue of inventory part requests at task completion time.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'SYSTEM', '5.1.2', 0);

--changeSet 0utl_config_parm:113 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('50', 'PERCENT_SHELF_LIFE_WARNING', 'LOGIC','Allowable percentage of shelf life remaining when receiving new inventory items or picking the inventory item for kit. If the percentage of shelf life remaining is less than this percentage, the item is automatically quarantined.','GLOBAL', 'Number', null, 1,'SYSTEM', '5.1.4',0);

--changeSet 0utl_config_parm:114 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'PERCENT_SHELF_LIFE_OVERAGE_WARNING', 'LOGIC','Allowable percentage of shelf life remaining when receiving new inventory items or picking the inventory item for kit. If the percentage of shelf life remaining is greater than this percentage it is displayed in red to warn the user.','GLOBAL', 'Number', null, 1,'SYSTEM', '8.2-SP1',0);

--changeSet 0utl_config_parm:115 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME,PARM_TYPE,PARM_VALUE,ENCRYPT_BOOL,PARM_DESC,CONFIG_TYPE,DEFAULT_VALUE,ALLOW_VALUE_DESC,MAND_CONFIG_BOOL,CATEGORY,MODIFIED_IN,REPL_APPROVED,UTL_ID)
VALUES ('ALLOW_BAD_MANUFACT_AND_SHELF_LIFE_EXPIRY_DATES','LOGIC','false',0,'Determines whether manufacturing date and the shelf expiry date are to be consistent with each other or not.','GLOBAL','TRUE','TRUE/FALSE',1,'Supply Inventory','7.0',0,0);

--changeSet 0utl_config_parm:116 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'RESEND_PO_ISSUE_REQUEST', 'LOGIC','Indicates wether or not to resend a PO issue request to the vendor if the PO has already been issued.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'SYSTEM', '5.1.4',0);

--changeSet 0utl_config_parm:117 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_RAISE_FUTURE_FAULT', 'TRUE', 'LOGIC', 0,'True - if the user is allowed to raise a fault with a future found on date, false-otherwise', 'USER', 'true/false', 'TRUE', 1, 'Core Logic', '5.1.4', 0);

--changeSet 0utl_config_parm:118 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MARK_ALL_RECEIPT_INSP_AS_INSPREQ', 'FALSE', 'LOGIC', 0,'True - if items that require inspection upon receipt are to be marked as INSPREQ upon receipt in a shipment, false - otherwise', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '5.1.4', 0);

--changeSet 0utl_config_parm:119 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MOVE_ACFT_TO_WORK_LOCATION', 'FALSE', 'LOGIC', 0,'This config parameter turns on logic that moves the aircraft to the work location.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1.5', 0);

--changeSet 0utl_config_parm:120 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ATTACH_INVENTORY_FROM_DIFFERENT_LOCATION', 'TRUE', 'LOGIC', 0,'If set to true it allows inventory to be attached and its parent inventory to be in different locations.', 'USER', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '8.1-SP2', 0);

--changeSet 0utl_config_parm:121 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('VALIDATE_SCHEDULE_LOCATION_CAPABILITY', 'FALSE', 'LOGIC', 0,'This config parameter turns on logic that validates the scheduled work location for the Check.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '5.1.5', 0);

--changeSet 0utl_config_parm:122 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SCHEDULE_LOCATIONS_CHECK', 'LINE,TRACK', 'LOGIC', 0, 'Location types where a CHECK may be scheduled. Allowable Values: valid loc_type_cd','GLOBAL', 'valid loc_type_cd', 'LINE,TRACK', 1, 'Core Logic', '5.1.5', 0);

--changeSet 0utl_config_parm:123 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SCHEDULE_LOCATIONS_RO', 'SHOP', 'LOGIC', 0, 'Location types where a WORK ORDER may be scheduled. Allowable Values: valid loc_type_cd','GLOBAL', 'valid loc_type_cd', 'SHOP', 1, 'Core Logic', '5.1.5', 0);

--changeSet 0utl_config_parm:124 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SCHEDULE_LOCATIONS_ASSEMBLY', 'LINE,TRACK,SHOP', 'LOGIC', 0, 'Location types where a assembly WORK ORDER may be scheduled. Allowable Values: valid loc_type_cd','GLOBAL', 'valid loc_type_cd', 'LINE,TRACK,SHOP', 1, 'Core Logic', '7.1.0', 0);

--changeSet 0utl_config_parm:125 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MANUAL_AUP_CALCULATION', 'FALSE', 'LOGIC', 0, 'Flag denoting whether the average unit price for parts should be calculated manually or not.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '0609', 0);

--changeSet 0utl_config_parm:126 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('INVOICE_TOLERANCE_PCT', 'LOGIC', '0', 0, 'Acceptable percentage difference between the purchase order price and the invoiced price.', 'USER', '0', 'Number', 0, 'Core Logic', '0609', 0, 0);

--changeSet 0utl_config_parm:127 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('INVOICE_ALLOW_OVERINV', 'LOGIC', 'FALSE', 0, 'Allow invoiced price greater or less than received price on invoices.', 'USER', 'FALSE', 'TRUE/FALSE', 0, 'Core Logic', '6.8.5', 0, 0);

--changeSet 0utl_config_parm:128 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INVOICE_ALLOW_UNMAPPED_LINES','LOGIC','ERROR', 0,'Allow/Warn/Error for unmapped invoice lines. Allowable Values OK, WARN, ERROR (case sensitive)','USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '0609', 0);

--changeSet 0utl_config_parm:129 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INVOICE_ALLOW_OVERINV_ISSUE','LOGIC','ERROR', 0,'Warn/Error for invoicing more than is issued. Allowable Values WARN, ERROR (case sensitive)','USER', 'WARN/ERROR', 'ERROR', 1, 'Core Logic', '0712', 0);

--changeSet 0utl_config_parm:130 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MATADAPTER_DEFAULT_FINANCIAL_CLASS_CD','LOGIC','', 0,'Default financial class code to be used when creating a new part definition throught material adpter','GLOBAL', 'valid ref_financial_class.financial_class_cd value', 'DEFAULTCD', 1, 'Material Adapter', '0609', 0);

--changeSet 0utl_config_parm:131 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MATADAPTER_DEFAULT_ABC_CLASS_CD','LOGIC','C', 0,'Default ABC class code to be used when creating a new part definition throught material adpter','GLOBAL', 'valid ref_abc_class.abc_class_cd value', 'C', 1, 'Material Adapter', '0609', 0);

--changeSet 0utl_config_parm:132 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MATADAPTER_DEFAULT_ASSET_ACCOUNT_CD','LOGIC','', 0,'Default ASSET ACCOUNT code to be used when creating a new part definition throught material adpter','GLOBAL', 'valid FNC_ACCOUNT.ACCOUNT_CD value', 'DEFAULTCD', 1, 'Material Adapter', '0609', 0);

--changeSet 0utl_config_parm:133 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('CREATE_SERVICEABLE_TURN_INS', 'TRUE', 'LOGIC', 0, 'Determines if serviceable turn ins should be created.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '5.1.5.4', 0);

--changeSet 0utl_config_parm:134 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('CORRECTIVE_ACTION_MANDATORY_EXCEPTION', 'TRUE', 'LOGIC', 0, 'Determines if corrective action mandatory exception should be checked.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '0703', 0);

--changeSet 0utl_config_parm:135 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ADD_SHIPPING_PROCESSING_TIME', 'false', 'LOGIC', 0,'Whether the Est. Arrival Date should include shipping and processing time', 'GLOBAL', 'true/false', 'false', 1, 'Core Logic', '0706', 0);

--changeSet 0utl_config_parm:136 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_UNASSIGN_JIC', 'FALSE', 'LOGIC', 0,'True - if the user is allowed to remove a JIC from a work package, false-otherwise', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '5.1.5.6', 0);

--changeSet 0utl_config_parm:137 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'DEFAULT_ASSIGN_TO_ME_ON_PAUSE_WORK', 'MXWEB','Default state for the Assign to Me check box on the pause work page.','USER', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '5.1.5.6', 0);

--changeSet 0utl_config_parm:138 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'DEFAULT_BATCH_REMOVAL', 'MXWEB','AddPartRequirement page: Indicates whether to default the Add Part Requirements / Requirement / RM checkbox for batch parts.','GLOBAL', 'TRUE/FALSE', 'false', 1, 'MXWEB', '5.1.5', 0);

--changeSet 0utl_config_parm:139 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_DUPLICATE_LABOUR_SKILL_ASSIGNMENT', 'TRUE', 'LOGIC', 0,'True - the same person is allowed to assign to multiple labour rows for the same skill, false-otherwise', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '5.1.5.6', 0);

--changeSet 0utl_config_parm:140 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_ENFORCE_PART_NO_COMPLIANCE', 'FALSE', 'LOGIC', 0, 'If true, part numbers must be SPEC2000 compliant.', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '7.5', 0);

--changeSet 0utl_config_parm:141 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_VENDOR_CD_ENABLE', 'FALSE', 'LOGIC', 0,'Enable regex pattern for vendor', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:142 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_MANUFACT_CD_ENABLE', 'TRUE', 'LOGIC', 0,'Enable regex pattern for manufacturer', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:143 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_ASSMBL_CD', 'TRUE', 'LOGIC', 0,'Assmbly Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:144 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_ASSMBL_BOM_CD', 'TRUE', 'LOGIC', 0,'Config-slot code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:145 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_BOM_PART_CD', 'TRUE', 'LOGIC', 0,'Part Group code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:146 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_MANUFACT_CD', 'TRUE', 'LOGIC', 0,'Manufacturer code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:147 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_PART_NO_OEM', 'TRUE', 'LOGIC', 0,'OEM Part Number must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:148 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_STOCK_NO_CD', 'TRUE', 'LOGIC', 0,'Stock Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:149 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_PANEL_CD', 'TRUE', 'LOGIC', 0,'Panel Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:150 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_ZONE_CD', 'TRUE', 'LOGIC', 0,'Zone Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:151 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_ACCOUNT_CD', 'TRUE', 'LOGIC', 0,'Account Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:152 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_IETM_CD', 'TRUE', 'LOGIC', 0,'IETM Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:153 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_LOC_CD', 'TRUE', 'LOGIC', 0,'Location Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:154 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_VENDOR_CD', 'TRUE', 'LOGIC', 0,'Vendor Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:155 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_LABOUR_SKILL_CD', 'TRUE', 'LOGIC', 0,'Labour Skill Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:156 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_LICENSE_TYPE_CD', 'TRUE', 'LOGIC', 0,'License Type Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:157 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_UPPERCASE_TASK_CD', 'TRUE', 'LOGIC', 0,'Task Code must be upper-case', 'GLOBAL', 'true/false', 'TRUE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:158 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_CHECK_DUPLICATES', 'TRUE', 'LOGIC', 0,'Enable duplicates check', 'GLOBAL', 'true/false', 'FALSE', 1, 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:159 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_CHANGE_CHECK_BARCODE', 'TRUE', 'LOGIC', 0,'Whether to allow the barcode of a check to be changed', 'GLOBAL', 'true/false', 'true', 1, 'Core Logic', '6.2.2', 0);

--changeSet 0utl_config_parm:160 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_CHANGE_TASK_FAULT_BARCODE', 'true', 'LOGIC', 0,'Whether to allow the barcode of a task or a fault to be changed', 'USER', 'true/false', 'true', 1, 'Core Logic', '0712', 0);

--changeSet 0utl_config_parm:161 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_DIFFERENT_USER_LABOUR_SIGNOFF', 'LOGIC','Whether to allow the logged in user to be able to sign off on labour rows for a different user. This value cannot be true if e-signatures is turned on.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'Core Logic', '0709', 0);

--changeSet 0utl_config_parm:162 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_MINIMUM_GROUND_TIME','LOGIC', '4', 0,'Global configuration parameter sets minimum time required by an aircraft to visit a line maintenance location for maintenance (hours)', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:163 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_SCHEDULING_RANGE','LOGIC', '5', 0,'Global configuration parameter sets scheduling range for maintenance opportunities and maintenance tasks used by line planning automation (days)', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:164 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TASK_MINIMUM_PLANNING_YIELD','LOGIC', '0.8', 0,'Global configuration parameter sets minimum planning yield for all maintenance tasks to be used for scheduling by line planning automation (percentage ex: 1 representing 100%)', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:165 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TASK_MIN_USAGE_FOR_RELEASE','LOGIC', '0.0', 0,'Global configuration parameter sets minimum usage for release percentage for all requirement task definitions (percentage ex: 1 representing 100%)', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Planning', '7.0', 0);

--changeSet 0utl_config_parm:166 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_IGNORE_LABOUR_SHORTAGE','LOGIC', 'FALSE', 0,'When set to true line planning automation will ignore labour resource shortage.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:167 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('CERTIFICATION_REQUIRED_DEFAULT','LOGIC', 'FALSE', 0,'If true,  all new task labour requirements request certification by default.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint - Planning', '7.0', 0);

--changeSet 0utl_config_parm:168 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_IGNORE_TOOLS_SHORTAGE','LOGIC', 'FALSE', 0,'When set to true line planning automation will ignore tools shortage.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:169 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_IGNORE_PARTS_SHORTAGE','LOGIC', 'FALSE', 0,'When set to true line planning automation will ignore parts shortage.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:170 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_IGNORE_WORKTYPE_SHORTAGE','LOGIC', 'FALSE', 0,'When set to true line planning automation will ignore work type shortage.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:171 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LINE_PLANNING_WORK_PACKAGE_NAME','LOGIC', 'inv_loc.loc_cd || '', '' ', 0,'This value is used to generate prefix for work package name created by line planning automation. Any columns from inv_ac_reg (aircraft), inv_inv (aircraft) , inv_loc (airport) are allowed.', 'GLOBAL', 'STRING', '0' , 1, 'Maint - Planning', '706', 0);

--changeSet 0utl_config_parm:172 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TASK_DEFN_REV_REASON_MANDATORY','LOGIC','false', 0,'Makes BLOCK/REQ/JIC revision reason and notes mandatory.','GLOBAL', 'true/false', 'false', 1, 'Maint Program - Task Definitions', '0712', 0);

--changeSet 0utl_config_parm:173 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'TASK_DEFN_DISCRETE_UI', 'LOGIC','Whether the user sees the new (discrete) UI','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '0712', 0);

--changeSet 0utl_config_parm:174 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('UNASSIGN_RECREATE_JIC', 'true', 'LOGIC', 0,'True - completed and error jics are re-created when parent req is unassigned, false- completed and error jics are not re-created.', 'GLOBAL', 'true/false', 'true', 1, 'Core Logic', '6.3.1', 0);

--changeSet 0utl_config_parm:175 stripComments:false
INSERT INTO utl_config_parm ( PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ALLOW_ACTIVATING_TD_BEFORE_APPROVAL', 'TRUE', 'LOGIC', 0, 'This parameter is used to determine whether activating task definition is allowed before approval.' , 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '8.0', 0 );

--changeSet 0utl_config_parm:176 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ADD_FOUND_IN_LABOUR_TO_FAULT', 'false', 'LOGIC', 0, 'Whether to add the labour from the found in task to the corrective task.', 'GLOBAL', 'true/false', 'true', 1, 'Core Logic', '0712', 0);

--changeSet 0utl_config_parm:177 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SUPPRESS_DUPLICATE_TASKS', 'true', 'LOGIC', 0,'True - Duplicate task suppression logic is active, false- Duplicate task suppression logic is disabled', 'GLOBAL', 'true/false', 'true', 1, 'Core Logic', '6.3.1', 0);

--changeSet 0utl_config_parm:178 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_BLOCK_CHAIN_LENGTH', '50', 'LOGIC', 0,'The maximum size for a Block Chain Task Definition', 'GLOBAL', 'NUMBER', '50', 1, 'Core Logic', '0803', 0);

--changeSet 0utl_config_parm:179 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_MANUAL_LOGISTICS','LOGIC', 'FALSE', 0,'Whether user can set and identify tasks that are parts and/or tools ready.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint', '0803', 0);

--changeSet 0utl_config_parm:180 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENFORCE_OPEN_CLOSE_PANEL_COMPLETE_TASK_ORDER','LOGIC', 'FALSE', 0,'Whether or not ensure the order of Open Panel -> Do Task -> Close panel.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint', '0803', 0);

--changeSet 0utl_config_parm:181 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_CHECK_MPC_PANELS_ON_COMPLETE_CHECK','LOGIC', 'FALSE', 0,'Whether or not allow to check for open and close panels status to complete a work package', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint', '0803', 0);

--changeSet 0utl_config_parm:182 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_MPC_UPDATES_AFTER_COMMIT','LOGIC', 'FALSE', 0,'Whether or not allow mpc task updates after a work package is committed.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'Maint', '0803', 0);

--changeSet 0utl_config_parm:183 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BSYNC_IGNORE_ZIPPING_DATE_MISALIGNMENT','LOGIC', 'FALSE', 0,'When set to true, baseline synchronization of Block / Requirement mappings will allow assigning requirements to blocks that have later deadlines.', 'GLOBAL', 'BOOLEAN', '0' , 1, 'BSync - Zipping', '6.8.5', 0);

--changeSet 0utl_config_parm:184 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC,CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FORCE_INSP_REPAIR_RELEASE','LOGIC', 'FALSE', 0,'Parameter that forces INSPREQ inventory status for inventory released from internal shops ','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '6.8.6', 0);

--changeSet 0utl_config_parm:185 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PREVENT_SYNCH_ON_INV_CREATED_AS_INSPREQ', 'TRUE', 'LOGIC', 0, 'TRUE-Inventory creation logic will mark new inventory that are created as INSPREQ as Prevent Sync.FALSE-Inventory creation logic will not mark new inventory that are created as INSPREQ as Prevent Sync.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '0812', 0);

--changeSet 0utl_config_parm:186 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_ACTIVATE_TASK_DEFN_W_INV_MISSING_MANUFACT_RECEIVE_DATE','LOGIC', 'FALSE', 0,'This confguration parameter dictates whether or not the user is allowed to activate a task definition scheduled from manufactured date or received date even though there are applicable inventory records missing these values.', 'GLOBAL', 'TRUE/FALSE', 'FALSE' , 1, 'Maint Program - Task Definitions', '8.0', 0);

--changeSet 0utl_config_parm:187 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INVOICE_TOLERANCE_FIXED',0, 'LOGIC', 0,'Acceptable fixed dollar difference between the fixed dollar and the invoice price.', 'USER', 'Number', 0, 0, 'Core Logic', '8.0', 0);

--changeSet 0utl_config_parm:188 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INVOICE_VALIDATE_JOB_DELAY',24, 'LOGIC', 0,'The delay (in hours) between invoice creation and automated verification of invoice to be marked for payment.', 'USER', 'Number', 24, 0, 'Core Logic', '8.0', 0);

--changeSet 0utl_config_parm:189 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('WORK_TYPE_REQUIRED_FOR_WP','LOGIC', 'FALSE', 0,'Whether work type is mandatory for a work order.', 'GLOBAL', 'TRUE/FALSE', 'FALSE' , 1, 'Maint - Work Packages', '8.0-SP1', 0);

--changeSet 0utl_config_parm:190 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ISSUE_PART_REQUEST_PRINTER_LOCATION', 'WAREHOUSE', 'LOGIC', 0, 'Controls the location that part request print jobs go. WAREHOUSE will print at the inventory location. WHERE_NEEDED will print at the where-needed location.', 'GLOBAL', 'WAREHOUSE/WHERE_NEEDED', 'WAREHOUSE', 1, 'Core Logic', '8.0-SP2', 0);

--changeSet 0utl_config_parm:191 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_ON_CONDITION_REQ_IN_MANDATORY_BLOCK', 'FALSE', 'LOGIC', 0, 'This will allow on condition requirements in mandatory blocks. Enabling this feature will have a performance impact during baseline synchronization.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.0-SP2', 0);

--changeSet 0utl_config_parm:192 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_STANDARD_AD_SB_MANAGEMENT', 'FALSE', 'LOGIC', 0, 'Controls whether the Standard AD/SB solution is enabled or not.  If a custom AD/SB solution is being used then this parameter must be set to FALSE (the Standard AD/SB solution and a custom AD/SB solution may not be used simultaneously).', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.1-SP3', 0);

--changeSet 0utl_config_parm:193 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY', 'TRUE', 'LOGIC', 0, 'Controls the way that part number applicability checks are performed. Part applicability checks are executed during the creation of a part requirement, reservation, issue and installation of a component. When set to FALSE, the sub-inventory tree is validated against the aircraft applicability code it is installed on or reserved for. Validations on parts to be attached to loose sub-assemblies will pass automatically. Validations on parts to be attached to a sub-assembly of an aircraft will be done against the aircraft applicability code. When set to TRUE, each sub-inventory in the sub-inventory tree is only validated against its next-highest parent assembly applicability code.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '8.2-SP2', 0);

--changeSet 0utl_config_parm:194 stripComments:false
-- Order Budget
INSERT INTO utl_config_parm(PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_ORDER_BUDGET_INTEGRATION', 'FALSE', 'LOGIC', 0, 'Controls whether the order budget integration feature is enabled. When true, the order approval workflow will include a budget check prior to allowing individual authorizations, and Maintenix will emit and receive order budget related messages.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Orders - Budget', '8.0', 0);

--changeSet 0utl_config_parm:195 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INSTALLED_INVENTORY_NOT_APPLICABLE','LOGIC', 'ERROR', 0,'Specifies the severity of the validation for any non-applicable installed inventory. If the specified applicability code does not apply to the installed inventory, the code will either be processed without a warning, processed with a warning, or not processed with an error. Allowable values are OK, WARNING, ERROR (case sensitive)','USER', 'WARNING/ERROR/OK', 'ERROR', 1,  'Core Logic', '8.1-sp2', 0);

--changeSet 0utl_config_parm:196 stripComments:false
-- LOGIC Config parameters
/*************** Web **************/
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENFORCE_RAISE_FAULT_AGAINST_SYSTEM', 'LOGIC','When raising a fault where the root inventory is an aircraft, this parameter can force users to raise faults only against systems.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 0, 'Web Logic', '5.1', 0);

--changeSet 0utl_config_parm:197 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('4', 'MO_FLAG_THRESHOLD', 'LOGIC','Number of hours required for a Maintenance Opportunity','GLOBAL', 'Number', 4, 0 , 'Web Logic', '5.1', 0);

--changeSet 0utl_config_parm:198 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'MO_LOC_CAPABILITY', 'LOGIC','Work types required for a Maintenance Opportunity','GLOBAL', 'Comma seperated list of work types', null, 0 , 'Web Logic', '5.1.1', 0);

--changeSet 0utl_config_parm:199 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'CAPACITY_OFFSET_HOURS', 'LOGIC','Number of hours after midnight that a day "begins"', 'GLOBAL', 'Number between zero and 23', 0, 0, 'Web Logic', '5.1', 0);

--changeSet 0utl_config_parm:200 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PO_SETUP_COST', 'LOGIC', 0, 0, 'Enterprise wide average PO setup cost.','GLOBAL', 0, 'Number', 1, 'Web Logic', '5.1', 0);

--changeSet 0utl_config_parm:201 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0.8', 'CAPACITY_WARNING_PERCENT', 'LOGIC','Station Capacity page: the percentage of total capacity that the scheduled labor for a check must be above to indicate a warning','GLOBAL', '', '0.8', 1, 'Org - Locations', '706', 0);

--changeSet 0utl_config_parm:202 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('4', 'VENDOR_RELIABILITY_MONTHS', 'LOGIC','Vendor Reliability Report: this is the numbers of months vendor reliability calculation is based on.','GLOBAL', 'Number', '4', 1, 'Vendor Details', '709', 0);

--changeSet 0utl_config_parm:203 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('6', 'TASK_LABOUR_SUMMARY_MONTHS', 'LOGIC','The number of months the system calculates the actual hours spent on workscope tasks.','GLOBAL', 'Number', '6', 1, 'Web Logic', '712', 0);

--changeSet 0utl_config_parm:204 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ENABLE_UNRELATED_PART_WARNING', 'LOGIC','Tasks and Task Defns: Enables a warning when a user adds a part that is not in the BOM Tree or COMHW','GLOBAL', 'TRUE/FALSE', 'false', 1, 'Tasks - Part Requirements', '6.9', 0);

--changeSet 0utl_config_parm:205 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_AUTO_COMPLETION', 'LOGIC','Allow the auto-completion of open tasks, faults, and work packages that are associated with an inventory item when a user inspects the inventory item as serviceable. If set to FALSE, users cannot inspect the inventory as serviceable until the tasks and work package are manually completed. Auto-completion is not applicable to inspecting inventory as unserviceable.','USER', 'TRUE/FALSE', 'false', 1, 'Inspect -Servicable', '8.2sp3', 0);

--changeSet 0utl_config_parm:206 stripComments:false
-- MXWEB Config parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'ALLOW_RAISE_FAULT_LEAVE_OPEN_FROM_INV_OR_FLIGHT', 'MXWEB','Whether to allow the user to raise a fault and leave it open when raising a fault from a flight.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '5.1.5.3', 0);

--changeSet 0utl_config_parm:207 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'SHOW_TASK_EXECUTION', 'MXWEB','Whether to force the showing of the task execution sections on Task Details.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '5.1.4', 0);

--changeSet 0utl_config_parm:208 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'EDIT_INV_REASON_NOTES', 'MXWEB','Whether to force the user to submit reason and note for editing inventory.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '5.1.5', 0);

--changeSet 0utl_config_parm:209 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'FLIGHT_ENTRY_DEFAULT_AIRPORT', 'MXWEB','Indicates whether the departure and arrival airports will default to the aircraft''s current location when creating a flight.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:210 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'FLIGHT_ENTRY_DEFAULT_DATE', 'MXWEB','Indicates whether the departure and arrival dates will default to the current date when creating a flight. The times will default to 00:00.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:211 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, default_value, allow_value_desc, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'SELECT_WINDOW_POPUP_MAX_LOCATIONS', 'MXWEB', '200', 0, 'Indicates the maximum number of locations that may be displayed in the Select Location popup; if this value is exceeded, the Location Search page  will be displayed instead.', 'GLOBAL', '200', 'Number', 1, 'MXWEB', '5.1.0.1', 0 );

--changeSet 0utl_config_parm:212 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, default_value, allow_value_desc, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'SELECT_WINDOW_POPUP_MAX_LOCAL_LOCATIONS', 'MXWEB', '1000', 0, 'Indicates the maximum number of locations that may be displayed in the Select Local Location popup; if this value is exceeded, only the number of locations set with this parameter will be displayed, together with a warning message for a user to refine the search.', 'GLOBAL', '1000', 'Number', 1, 'MXWEB', '6.9', 0 );

--changeSet 0utl_config_parm:213 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('REP', 'COMPLETE_WO_DEFAULT_CONDITION', 'MXWEB','Default condition for the release inventory condition dropdown.', 'GLOBAL', 'valid inv_cond_cd', 'REP', 1 ,'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:214 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TDAWM', 'DEFAULT_DELAY_REASON', 'MXWEB','Default Delay Reason for delaying a check.','GLOBAL', 'valid event_stage_cd for TaskDeferal', 'TDAWM', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:215 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AWTD', 'DEFAULT_EDIT_REASON', 'MXWEB','Default Reason for edit estimated end date of a check.','GLOBAL', 'valid event_stage_cd for Edit Estimated End Date of a check', 'AWTD', 1, 'MXWEB', '7.1', 0);

--changeSet 0utl_config_parm:216 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'LABOUR_SCHEDULED_TIME_AS_DEFAULT', 'MXWEB','Indicates whether to use the current date or the scheduled date for the labour context.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 ,'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:217 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'CHECK_SCHEDULED_TIME_AS_DEFAULT', 'MXWEB','Indicates whether to use the current date or the scheduled date for task contexts','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:218 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0, 0, 0', 'DEFAULT_WARN_COLOR', 'MXWEB','The foreground colour of the aircraft cd in the applets (tile view and station monitoring).','GLOBAL', 'RBG color: 0,0,0 -> 255,255,255', '0,0,0', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:219 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('idTabDetails', 'DEFAULT_INV_DETAILS_TAB', 'SESSION','Default tab to display when rendering the Inventory Details page.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:220 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'DEFAULT_SCHED_LOCATION_WORK_TYPE', 'MXWEB','Default scheduled location for a work type.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:221 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'DEFAULT_ALERT_TECHREC_SRVC', 'MXWEB','Default values used to indicate if technical records will be alerted.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:222 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'DEFAULT_GEN_SRVC_TAG', 'MXWEB','Default generation of Serviceability tag.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:223 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'DEFAULT_SHOW_TASKS_ON_SUBCOMPONENTS', 'MXWEB','Dictates whether tasks against subcomponents are displayed on the Edit Inventory Tasks page.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:224 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('50', 'TODOLIST_LABOUR_BATCH_SIZE', 'MXWEB','Defines the number of maximum number of rows that will appear when the Heavy Maintenance To Do List is opened.  If there is more than the specified maximum, additional pages will be created, and the user can tab through them using the sub list feature.','GLOBAL', '', '50', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:225 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'DEFAULT_USAGE_DEADLINE_INTERVAL', 'MXWEB','Default interval used when creating usage deadlines.','GLOBAL', 'Number', 100, 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:226 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'DEFAULT_CALENDAR_DEADLINE_INTERVAL', 'MXWEB','Default interval used when creating calendar deadlines.','GLOBAL', '', '100', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:227 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'ALLOW_REPLICA_INV_CREATION', 'MXWEB','Indicates whether a user may create inventory at a replicated site.','USER', 'TRUE/FALSE', 'true', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:228 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'SHOW_PARTS_ON_OFF', 'MXWEB','Indicates if we show the removed and installed parts table when raising and close a fault.','USER', 'TRUE/FALSE', 'false', 1, 'MXWEB', '5.1', 0);

--changeSet 0utl_config_parm:229 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ISSUE_INV_RECEIVED_BY_REQUIRED_AND_AUTH', 'MXWEB','IssueInventory page: Indicates whether Received By field is mandatory and requires authentification.','GLOBAL', 'TRUE/FALSE', 'false', 1, 'InventoryIssue', '5.1.4', 0);

--changeSet 0utl_config_parm:230 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'CREATE_LOGIN_ALERT_MESSAGE_TITLE_REQUIRED', 'MXWEB','CreateLoginAlert page: Indicates whether Message Title field is mandatory.','GLOBAL', 'TRUE/FALSE', 'false', 1, 'CreateLoginAlert', '0609', 0);

--changeSet 0utl_config_parm:231 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'CREATE_LOGIN_ALERT_MESSAGE_REQUIRED', 'MXWEB','CreateLoginAlert page: Indicates whether Message field is mandatory.','GLOBAL', 'TRUE/FALSE', 'false', 1, 'CreateLoginAlert', '0609', 0);

--changeSet 0utl_config_parm:232 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'USAGE_HOURS_AS_DECIMAL', 'MXWEB','Indicates whether the HOUR usages will be shown as decimals or HH:MM format. The HH:MM format is supported on the following pages: Create/Edit Flight, Inventory Details, Work Package Details, and Task Details.','GLOBAL', 'TRUE/FALSE', 'true', 1, 'Ops - Flights', '10.2.0.0', 0);

--changeSet 0utl_config_parm:233 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'PERFORM_CAPACITY_CHECK_ON_ASSIGN', 'MXWEB','Whether to run the capacity validation logic when assigning a task to a check.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '6.1', 0);

--changeSet 0utl_config_parm:234 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'EXPORT_TO_EXCEL', 'MXWEB','Whether to run export to excel logic in the TableTag.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '0703', 0);

--changeSet 0utl_config_parm:235 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'FAULT_DESCRIPTION_MANDATORY', 'MXWEB','Whether the fault description field is a mandatory field','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '0703', 0);

--changeSet 0utl_config_parm:236 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'SHOW_WO_COMPLETION_WARNINGS', 'MXWEB','Whether or not to show completion warnings on the Inspect as Serviceable page.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '0703', 0);

--changeSet 0utl_config_parm:237 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'REASON_FOR_PAUSE_WORK_MANDATORY', 'MXWEB','If the user is required to enter a Reason on the Capture Work page','USER', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '6.2.2.5', 0);

--changeSet 0utl_config_parm:238 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'STATUS_BOARD_VIEW_ALL_AUTHORITIES', 'MXWEB','If the user is allowed to view all authorities in the status board','USER', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '0712', 0);

--changeSet 0utl_config_parm:239 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'TURN_IN_WARNING_FOR_BATCH', 'SECURED_RESOURCE','It will control if warnings will be generated for BATCH parts on turn in.','USER', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Turn Ins', '6.9', 0);

--changeSet 0utl_config_parm:240 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_CREATE_INV', 'SECURED_RESOURCE','Indicates whether a user may auto-create Inventory when trying to create a Turn In.','USER', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:241 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_ARCHIVED_INV', 'SECURED_RESOURCE','Indicates whether a user may unarchive Inventory when trying to create a Turn In.','USER', 'TRUE/FALSE', 'false', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:242 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_INSTALLED_INV', 'SECURED_RESOURCE','Indicates whether a user may auto-uninstall Inventory when trying to create a Turn In.','USER', 'TRUE/FALSE', 'false', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:243 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_MOVE_INV', 'SECURED_RESOURCE','Indicates whether a user may auto-move Inventory when trying to create a Turn In.','USER', 'TRUE/FALSE', 'false', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:244 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_CHANGE_INV_CONDITION', 'SECURED_RESOURCE','Indicates whether a user may auto-modify Inventory Condition when trying to create a Turn In.','USER', 'TRUE/FALSE', 'false', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:245 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ALLOW_TURN_IN_NOT_ISSUED_INV', 'SECURED_RESOURCE','Indicates whether a user may turn in not issued Inventory.','USER', 'TRUE/FALSE', 'false', 1, 'Supply - Turn Ins', '0806', 0);

--changeSet 0utl_config_parm:246 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_CREATE_REMOTE_TURN_IN_REPREQ_BATCH','LOGIC','ERROR', 0,'WARNING/ERROR for allowing to create remote REPREQ batch.', 'USER', 'WARNING/ERROR', 'ERROR', 1, 'Supply - Turn Ins', '8.1-SP3', 0);

--changeSet 0utl_config_parm:247 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'SHOW_CREATE_PART_REQUEST', 'MXWEB','Determines whether to display the RQ checkbox on the Add Part Requirements page.','USER', 'TRUE/FALSE', 'true', 1, 'MXWEB', '0806', 0);

--changeSet 0utl_config_parm:248 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAKE_THIS_STANDARD_BIN', 'DEFAULT_MAKE_STANDARD_BIN', 'MXWEB','The default value of the radio button to make a bin the standard bin or make it the standard for an owner on the Create Put Away page.','GLOBAL', 'DO_NOT_CREATE_BIN/MAKE_THIS_STANDARD_BIN/MAKE_THIS_STANDARD_BIN_FOR_OWNER', 'MAKE_THIS_STANDARD_BIN', 1, 'Supply - Transfers', '6.8', 0);

--changeSet 0utl_config_parm:249 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('24', 'PRE_DRAW_MONITOR_NEXT_HOURS', 'MXWEB','Next X houses for Ready For Picking.','GLOBAL', 'Number', '24', 1, 'MXWEB', '6.8', 0);

--changeSet 0utl_config_parm:250 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('24', 'PRE_DRAW_MONITOR_PAST_HOURS', 'MXWEB','Past Y houses for Complete, Issued pre-draws.','GLOBAL', 'Number', '24', 1, 'MXWEB', '6.8', 0);

--changeSet 0utl_config_parm:251 stripComments:false
INSERT INTO utl_config_parm ( PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'APPLET_CLASSID', 'clsid:CAFEEFAC-0017-0000-FFFF-ABCDEFFEDCBA', 'MXWEB', 0, 'Specifies the value of the CLASSID attribute for applet''s OBJECT tags; the CLASSID identifies the minimum required Java version to launch an applet.', 'GLOBAL',  'See http://docs.oracle.com/javase/7/docs/technotes/guides/jweb/applet/using_tags.html#classidattrib',  'clsid:CAFEEFAC-0017-0000-FFFF-ABCDEFFEDCBA', 1, 'Client - Java', '8.2', 0  );

--changeSet 0utl_config_parm:252 stripComments:false
INSERT INTO utl_config_parm ( PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'CLIENT_JAVA_VERSION', '1.7+', 'MXWEB', 0, 'Specifies the Java version required on the client to run Java applets and applications.', 'GLOBAL',  'See https://docs.oracle.com/javase/7/docs/technotes/guides/jweb/applet/applet_deployment.html#JAVA_VERSION',  '1.7+', 1, 'Client - Java', '8.2', 0  );

--changeSet 0utl_config_parm:253 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'SCHEDULE_DEFERRAL_FROM_RECURRENCE', 'MXWEB','If true, the fault deferral start date will be defaulted to the start date of the recurrence fault, if one exists.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'MXWEB', '6.9', 0);

--changeSet 0utl_config_parm:254 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'ENABLE_ALT_MEASUREMENT_UNIT', 'MXWEB', 'Permission to display Alternate UOM on Edit Measurements page.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'Measurements', '7.0', 0);

--changeSet 0utl_config_parm:255 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, default_value, allow_value_desc, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'SELECT_WINDOW_POPUP_MAX_ACCOUNTS', 'MXWEB', '1000', 0, 'Indicates the maximum number of accounts that may be displayed in the Select Account popup; if this value is exceeded, only the number of accounts set with this parameter will be displayed, together with a warning message for a user to refine the search.', 'GLOBAL', '1000', 'Number', 1, 'MXWEB', '8.0', 0 );

--changeSet 0utl_config_parm:256 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1000', 'SUBCOMPONENTS_THRESHOLD_ON_INVENTORY_TASKS', 'MXWEB','When editing tasks on the edit inventory tasks page, the number of subcomponents can cause performance issues. When the number of subcomponents exceeds this threshold, the user will be warned of the performance impact.','GLOBAL', 'NUMBER', '1000', 1, 'MXWEB', '8.0', 0);

--changeSet 0utl_config_parm:257 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('FALSE', 'FLIGHT_ENTRY_COPY_ACTUAL_DT', 'MXWEB','Copies the actual departure dates to all other date fields.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '1209', 0, 0);

--changeSet 0utl_config_parm:258 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('L', 'BARCODE_SHORTCUT', 'MXWEB','Indicates the barcode shortcut','GLOBAL', 'An uppercase alphanumeric character. No special characters are allowed.', 'L', 1, 'MXWEB', '8.0', 0, 0);

--changeSet 0utl_config_parm:259 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_PART_AVAILABILITY', 'SECURED_RESOURCE','Permission to access availability in part details page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Parts - Part Numbers', '8.0',0);

--changeSet 0utl_config_parm:260 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_PART_BIN_LEVELS', 'SECURED_RESOURCE','Permission to access bin levels in part details page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Parts - Part Numbers', '8.0',0);

--changeSet 0utl_config_parm:261 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_LOCATION_STOCK_LEVELS', 'SECURED_RESOURCE','Permission to access stock levels in location details page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Locations', '8.0',0);

--changeSet 0utl_config_parm:262 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_LOCATION_BIN_LEVELS', 'SECURED_RESOURCE','Permission to access bin levels in location details page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Locations', '8.0',0);

--changeSet 0utl_config_parm:263 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_STOCK_PAGE', 'SECURED_RESOURCE','Permission to access stock page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Parts - Stock Numbers', '8.0',0);

--changeSet 0utl_config_parm:264 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_AUTHORITY_PAGE', 'SECURED_RESOURCE','Permission to access authority page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Org - Authorities', '8.0',0);

--changeSet 0utl_config_parm:265 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_FAULT_DEFN_ON_RAISE_FAULT_PAGE', 'MXWEB','Controls the visibility of the Fault Definition field on Raise Fault page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:266 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_FAULT_DETAILS_ON_RAISE_FAULT_PAGE', 'MXWEB','Controls the visibility of the Fault Details group box on Raise Fault page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:267 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_CREATE_WORKPACKAGE_SUBTYPE', 'MXWEB','Controls the visibility of the Subtype field on Create Work Package page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:268 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_CREATE_WORKPACKAGE_DOCREF', 'MXWEB','Controls the visibility of the Doc. Ref field on Create Work Package page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:269 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_CREATE_WORKPACKAGE_HEAVY_MAINT', 'MXWEB','Controls the visibility of the Heavy Maintenance checkbox on Create Work Package page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:270 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_CREATE_WORKPACKAGE_ISSUE_TO_ACCOUNT', 'MXWEB','Controls the visibility of the Issue To Account field on Create Work Package page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:271 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'DEFAULT_ISSUE_TO_ACCOUNT_CREATE_WORKPACKAGE', 'MXWEB','The default value to be used for the Issue To Account field on Create Work Package page.','GLOBAL', 'STRING', null, 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:272 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'DEFAULT_HEAVY_MAINTENANCE_CREATE_WORKPACKAGE', 'MXWEB','The default value to be used for the Heavy Maintenance checkbox on Create Work Package page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:273 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_INV_CREATE_OWNER', 'MXWEB','Controls the visibility of the Owner field on Create Inventory page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:274 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HIDE_INV_CREATE_CREDIT_ACCOUNT', 'MXWEB','Controls the visibility of the Credit Account field on Create Inventory page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:275 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'DEFAULT_OWNER_INV_CREATE', 'MXWEB','The default value to be used in the Owner field on Create Inventory page.','GLOBAL', 'STRING', null, 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:276 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'DEFAULT_CREDIT_ACCOUNT_INV_CREATE', 'MXWEB','The default value to be used in the Credit Account field on Create Inventory page.','GLOBAL', 'STRING', null, 1, 'MXWEB', '8.0',  0);

--changeSet 0utl_config_parm:278 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_REF_DOC_EDIT_INVENTORY_TASK_LIST', 'SECURED_RESOURCE','Permission to show reference document task definition on the Edit Inventory Step 4: Task List page.','USER', 'TRUE/FALSE', 'FALSE', 1,'Supply - Inventory', '8.1-SP1',0);

--changeSet 0utl_config_parm:279 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('SHOW_PART_NAME', 'MXWEB', 'FALSE', 0, 'Controls display of the Part Name column on the Task Details page, Task Execution tab, Part Requirements area. The default value is FALSE. Set to TRUE to display the Part Name column.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'MXWEB', '8.1-SP2', 0, 0);

--changeSet 0utl_config_parm:280 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('SHOW_EXTRA_DUE_INFO', 'MXWEB', 'FALSE', 0, 'Controls display of the Driving, Calendar, Hours, and Cycles columns under the Due column on the Inventory Details page, Open tab, Open Tasks sub-tab.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'MXWEB', '8.1-SP2', 0, 0);

--changeSet 0utl_config_parm:281 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('CHECK_DUPLICATE_SERIAL_NO', 'MXWEB', 'FALSE', 0, 'Checks if there are existing serial numbers that match the one being created. Special characters and spaces are ignored and only alphanumeric characters (a-z, regardless of case and 0-9) are considered for the comparison.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'MXWEB', '8.1-SP2', 0, 0);

--changeSet 0utl_config_parm:282 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('OPEN_TASKS_DUE_USAGES', '0:1,0:10', 'LOGIC', 0, 'DataTypes to display on the Open Tasks list.','GLOBAL', 'valid data_type_pk', '0:1,0:10', 1, 'MXWEB', '8.1-SP4', 0);

--changeSet 0utl_config_parm:283 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ALLOW_DEFAULT_PART_NUMBER_REPAIR_ORDER_ACCOUNT', 'MXWEB','Indicates whether to allow the input of default part number repair account.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.2-SP3', 0);

--changeSet 0utl_config_parm:284 stripComments:false
-- Session parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('60', 'sDayCount', 'SESSION','Stores the last value entered in any of the search forms searching by number of days.','USER', 'Number', '60', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:285 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'sFleetDueListDayCount', 'SESSION','Stores the last value entered in Fleet Due List number of days','USER', 'Number', '60', 0, 'SESSION', '6.8.6', 0);

--changeSet 0utl_config_parm:286 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('24', 'sHourCount', 'SESSION','Stores the last value of hours entered in pre-draws form.','USER', 'Number', '24', 0, 'SESSION', '6.8', 0);

--changeSet 0utl_config_parm:287 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWorkType', 'SESSION','Stores the last value of work type index selected on pre-draw select work type page.','USER', 'STRING', '', 0, 'SESSION', '6.8', 0);

--changeSet 0utl_config_parm:288 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sLocationKey', 'SESSION','Stores the last value of location key selected on pre-draw select warehouse page.','USER', 'STRING', '', 0, 'SESSION', '6.8', 0);

--changeSet 0utl_config_parm:289 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sPredrawMonitorLocation', 'SESSION','Stores the last value of selected location on Predraw Monitoring page.','USER', 'STRING', '', 0, 'SESSION', '6.8', 0);

--changeSet 0utl_config_parm:290 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'sRaiseFaultWithinXDays', 'SESSION','Stores the last value entered in the fault definition search form on the Raise Fault page (within X days).','USER', 'Number', '10', 0 ,'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:291 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'sRecurringFaultWithinXDays', 'SESSION','Stores the last value entered in the fault definition search form on the Raise Fault page. (within X days)','USER', 'Number', '10', 0 , 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:292 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'sHistoricFlightWithinXDays', 'SESSION','Stores the last value entered in the search form on the Historic Flight Search page (within X days).','USER', 'Number', '10', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:293 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'sWithinXHours', 'SESSION','Stores the last value entered when search for aircraft arriving with X hours, and possible fault occurring within X hours.','USER', 'Number', '8', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:294 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'sWithinNextXHours', 'SESSION','Stores the last value entered when search for aircraft arriving within next X hours.','USER', 'Number', '8', 0, 'SESSION', '0703', 0);

--changeSet 0utl_config_parm:295 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'NoTaskAuthReqd', 'SESSION','Whether or not a user must enter their password when either marking a fault as "No Fault Found", canceling a task, or deferring a task.','USER', 'Number', 0, 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:297 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'sFlightTimeThreshold', 'SESSION','The threshold to determine whether an aircraft is late/early or on time. (in seconds) AC is early if ( current time - threshold ) is less than the arrival time.','USER', 'Number', '10', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:298 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'sFlightFilter', 'SESSION','Filter used for the Possible Faults list on the Inventory Details page.','USER', 'Number', '1', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:299 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sShowAssignedFaults', 'SESSION','Whether assigned faults are shown in the open faults list on the Inventory Details page.','USER', 'Number', '0', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:300 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sShowAssignedTasks', 'SESSION','Whether assigned tasks are shown in the open tasks list on the Inventory Details page.','USER',  'Number', '0', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:301 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sHideNonExcecutableTasks', 'SESSION','Hide non executable  taks on  open tasks list on the Inventory Details and CheckDetail page.','USER', 'Number', '0', 0, 'SESSION', '7.0', 0);

--changeSet 0utl_config_parm:302 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sHideNonExcecutableFaults', 'SESSION','Hide non executable  faults on  open faults list on the Inventory Details and CheckDetail page.','USER',  'Number', '0', 0, 'SESSION', '7.0', 0);

--changeSet 0utl_config_parm:303 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sLocationDetailsShowInventoryInSublocations', 'SESSION','Whether inventory in sublocations are shown on the Inventory tab on the Location Details page.','USER',  'TRUE/FALSe', 'FALSE', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:304 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sLocationDetailsShowDepartmentsInSublocations', 'SESSION','Whether departments in sublocations are shown on the Departments tab on the Location Details page.','USER',  'TRUE/FALSe', 'FALSE', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:305 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sLocationDetailsShowRepairablePartsInSublocations', 'SESSION','Whether repairable parts in sublocations are shown on the Departments tab on the Location Details page.','USER',  'TRUE/FALSe', 'FALSE', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:306 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShowTask', 'SESSION','show task.','USER', '', '', 0, 'SESSION', '6.9', 0);

--changeSet 0utl_config_parm:307 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sLocationDetailsShowBinLevelsInSublocations', 'SESSION','Whether bin levels in sublocations are shown on the Departments tab on the Location Details page.','USER',  'TRUE/FALSe', 'FALSE', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:308 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sMyPartRequestsShowPartRequestsCreatedByDept', 'SESSION','Whether part requests created by people in the users department are shown on the My Part Requests tab on the Material Control To Do List.','USER',  'TRUE/FALSE', 'FALSE', 0, 'SESSION', '5.1', 0);

--changeSet 0utl_config_parm:309 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, ENCRYPT_BOOL, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVisibleAssemblies', 'SESSION','Assemblies that are visible on the Fleet List page.','USER', 'STRING', null, 0, 0, 'SESSION', '8.1', 0);

--changeSet 0utl_config_parm:310 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, ENCRYPT_BOOL, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVisibleLocations', 'SESSION','Locations that are visible on the assigned to crew todo list.','USER', 'STRING', null, 0, 0, 'SESSION', '8.1', 0);

--changeSet 0utl_config_parm:311 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sShowOnlyPartRequestsAssignedToHr', 'SESSION','Whether to hide part requests not assigned to the current user on the Purchase Requests todo list.','USER',  'Number', '0', 0, 'SESSION', '0806', 0);

--changeSet 0utl_config_parm:312 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sShowSoftDeadlines', 'SESSION','Whether to hide tasks that are due with a Soft Deadline (ie can go over with no issue).','USER',  'TRUE/FALSE', 'FALSE', 0, 'SESSION', '6.8', 0);

--changeSet 0utl_config_parm:313 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sShowNsvTasks', 'SESSION', 'Whether to hide next shop visit tasks.', 'USER',  'TRUE/FALSE', 'FALSE', 0, 'SESSION', '8.0', 0);

--changeSet 0utl_config_parm:314 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUSSTGPartType', 'SESSION','Stores the last selected value for Part Type filter on the Unserviceable Staging todo list.','USER', 'STRING', '', 0, 'SESSION', '6.8.12', 0);

--changeSet 0utl_config_parm:315 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUSSTGOptionalColumns', 'SESSION', 'Unserviceable Staging todo list hidden columns', 'USER', 'STRING', null, 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:316 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sUSSTGShowOnlyVendorOwnedParts', 'SESSION','Whether to show only vendor owned parts on the Unserviceable Staging todo list.','USER',  'Number', '0', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:317 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'sOnlyShowOrdersRequireYourAttention', 'SESSION','Whether to show only orders that require your attention on the Authorization Required POs To Do List.','USER',  'TRUE/FALSE', 'TRUE', 0, 'SESSION', '6.8.12', 0);

--changeSet 0utl_config_parm:318 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('30', 'sTaskDueListDayCount', 'SESSION', 0, 'Stores the last value entered in the "Show tasks due within the next" options filter on the Storage Maintenance to-do list.','USER', 'Number', '30', 0, 'SESSION', '8.1-SP2', 0);

--changeSet 0utl_config_parm:319 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sSTMTPartType', 'SESSION', 0, 'Stores the last selected value for the part type options filter on the Storage Maintenance to-do list.','USER', 'STRING', '', 0, 'SESSION', '8.1-SP2', 0);

--changeSet 0utl_config_parm:320 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'sShowRequestsAssignedToOthers', 'SESSION','Whether to show requests not assigned to the current user on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:321 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'sShowPartRequests', 'SESSION','Whether to show all part requests on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:322 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'sShowStockRequests', 'SESSION','Whether to show all stock requests on the Open Part Requests todo list.','USER',  'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:323 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALL', 'sPartRequestPurchaseType', 'SESSION','Stores the last selected value of the Request Type dropdown  on the Open Part Requests todo list.','USER',  'STRING', 'ALL', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:324 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALL', 'sPartRequestPartProvider', 'SESSION','Stores the last selected value of the Part Provider dropdown on the Open Part Requests todo list.','USER',  'STRING', 'ALL', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:325 stripComments:false
-- PO Invoice Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchPOInvoiceNumber', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:326 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchPOInvoiceStatus', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:327 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchPONumber', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:328 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchVendor', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:329 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchMaxResults', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:330 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchContactHr', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '0609', 0);

--changeSet 0utl_config_parm:331 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sPOInvoiceSearchExternalKey', 'SESSION', null, 0, 'PO Invoice search parameters', 'USER', null, 'String', 0, 'PO Invoice Search', '6.8', 0);

--changeSet 0utl_config_parm:332 stripComments:false
-- Tool Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchManufacturerCd', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:333 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchPartNoOem', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:334 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchAssemblyCd', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:335 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchBomItem', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:336 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchBomPart', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:337 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchBatchSerialNo', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:338 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchCalibrationDue', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'Date', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:339 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchMaxResults', 'SESSION', '100', 0, 'Tool search parameters', 'USER', '100', 'Number', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:340 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchMethod', 'SESSION', 'STARTS_WITH', 0, 'Tool search parameters', 'USER', 'STARTS_WITH', 'String', 0, 'Tool Search', '5.1', 0);

--changeSet 0utl_config_parm:341 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchLocationCd', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:342 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchInvCondCd', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:343 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchCheckOutUserName', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:344 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sToolSearchIncludeSublocations', 'SESSION', null, 0, 'Tool search parameters', 'USER', null, 'String', 0, 'Tool Search', '5.1.5', 0);

--changeSet 0utl_config_parm:345 stripComments:false
-- Task Definition Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sTaskDefnSearchMaxResults', 'SESSION', 'Task Definition search parameters', 'USER', 'Number', 100, 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:346 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sTaskDefnSearchMethod', 'SESSION', 'Task Definition search parameters.', 'USER', 'String', 'STARTS_WITH', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:347 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskCd', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:348 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskName', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:349 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskClass', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:350 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskSubClass', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:351 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchAssmbl', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:352 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchAssmblName', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:353 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchAssmblCd', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:354 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchAssmblBomCd', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:355 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskStatus', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:356 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchTaskOriginator', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1', 0);

--changeSet 0utl_config_parm:357 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchWorkType', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '5.1.5.4', 0);

--changeSet 0utl_config_parm:358 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchSchedParm', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '0703', 0);

--changeSet 0utl_config_parm:359 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskDefnSearchSchedInterval', 'SESSION', 'Task Definition search parameters.', 'USER', '', '', 0, 'Task Definition Search', '0703', 0);

--changeSet 0utl_config_parm:360 stripComments:false
-- Task Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchAircraft', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:361 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchAircraftName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:362 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchInventory', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:363 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchInventoryName', 'SESSION','Task search parameters.','USER', '', '', 0,'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:364 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchAssembly', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:365 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchAssemblyName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:366 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchBomItem', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:367 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchZoneName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:368 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchZoneCd', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:369 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchPanel', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:370 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchPanelName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:371 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'sTaskSearchIncludeArchivedInventory', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '6.4.1', 0);

--changeSet 0utl_config_parm:372 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:373 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchFaultSeverity', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:374 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchTaskClass', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:375 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchTaskSubclass', 'SESSION','Task search parameters.','USER', 'Number', '1', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:376 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchOriginator', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:377 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchZone', 'SESSION','Task search parameters.','USER', '', '', 0,'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:378 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchWorkType', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:379 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchDueBefore', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:380 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchDueAfter', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:381 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchSchedulePriority', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:382 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'sTaskSearchUnassignedTasksOnly', 'SESSION','Task search parameters.','USER', 'Number', 0, 0, 'Task Search', '5.1.4', 0);

--changeSet 0utl_config_parm:383 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1000', 'sTaskSearchMaxResults', 'SESSION','Task search parameters.','USER', '', '1000', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:384 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchHistoric', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:385 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchCompletedAfter', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:386 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchCompletedBefore', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:387 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchWOName', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:388 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchRONumber', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:389 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchWONumber', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:390 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchROVendor', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:391 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'sTaskSearchIncludeForecastedTasks', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:392 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null,'sTaskSearchDocumentReference','SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:393 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchWOLineNumber','SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:394 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchROLineNumber','SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:395 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'STARTS_WITH', 'sTaskSearchMethod', 'SESSION','Task search parameters.','USER','String', 'STARTS_WITH', 0, 'Task Search', '5.1', 0);

--changeSet 0utl_config_parm:396 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchTaskPriority', 'SESSION','Task search parameters.','USER', '', '', 0, 'Task Search', '0609', 0);

--changeSet 0utl_config_parm:397 stripComments:false
-- Event Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchDueBefore', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:398 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchDueAfter', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:399 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchDays', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:400 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchFailType', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:401 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchEventStatus', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:402 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchAuthority', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:403 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchAssembly', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:404 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchAcftSerialNo', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:405 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchAcftRegn', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:406 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchMethod', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:407 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sEventSearchMaxResults', 'SESSION','Event search parameters.','USER', '', '', 0, 'Maint - Events', '0803', 0);

--changeSet 0utl_config_parm:408 stripComments:false
-- Fault Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchAircraft', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:409 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchAircraftName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:410 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchInventory', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:411 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchInventoryName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:412 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchAssembly', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:413 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchAssemblyName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:414 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchBomItem', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:415 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchZoneName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:416 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchZoneCd', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:417 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchPanel', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:418 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchPanelName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:419 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchFaultName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:420 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchFaultSeverity', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:421 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchTaskPriority', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:422 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null,'sFaultSearchFaultDocReference','SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:423 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null,'sFaultSearchFlightLogReference','SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:424 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null,'sFaultSearchTaskDocReference','SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:425 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HIST_OPEN', 'sFaultSearchHistoric', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:426 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchCompletedAfter', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:427 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchCompletedBefore', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:428 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchDueAfter', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:429 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchDueBefore', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:430 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchSchedulePriority', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:431 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchUnassignedFaultsOnly', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:432 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchWOName', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:433 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchRONumber', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:434 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchWONumber', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:435 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchROVendor', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:436 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchWOLineNumber','SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:437 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchROLineNumber','SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:438 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sFaultSearchMaxResults', 'SESSION','Fault search parameters.','USER', 'Number', 100, 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:439 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'STARTS_WITH', 'sFaultSearchMethod', 'SESSION','Fault search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:440 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchZone', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:441 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultSearchForMyCrew', 'SESSION','Fault search parameters.','USER', '', '', 0, 'Fault Search', '5.1', 0);

--changeSet 0utl_config_parm:442 stripComments:false
-- Fault Definition Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultDefnSearchFaultCd', 'SESSION','Fault Definition search parameters.','USER', '', '', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:443 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultDefnSearchFaultName', 'SESSION','Fault Definition search parameters.','USER', '', '', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:444 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultDefnSearchAssmbly', 'SESSION','Fault Definition search parameters.','USER', '', '', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:445 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultDefnSearchAssmblyName', 'SESSION','Fault Definition search parameters.','USER', '', '', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:446 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultDefnSearchAssmblBomCd', 'SESSION','Fault Definition search parameters.','USER', '', '', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:447 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sFaultDefnSearchMethod', 'SESSION','Fault Definition search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:448 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sFaultDefnSearchMaxResults', 'SESSION','Fault Definition search parameters.','USER', 'Number', '100', 0, 'Fault Definition Search', '0703', 0);

--changeSet 0utl_config_parm:449 stripComments:false
-- Role Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sRoleSearchMaxResults', 'SESSION','Role search parameters.','USER', 'Number', 100, 0, 'Role Search', '5.1', 0);

--changeSet 0utl_config_parm:450 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sRoleSearchMethod', 'SESSION','Role search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Role Search', '5.1', 0);

--changeSet 0utl_config_parm:451 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sRoleSearchRoleName','SESSION','Role search parameters.','USER', '', '', 0, 'Role Search', '5.1', 0);

--changeSet 0utl_config_parm:452 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sRoleSearchRoleCode','SESSION','Role search parameters.','USER', '', '', 0, 'Role Search', '5.1', 0);

--changeSet 0utl_config_parm:453 stripComments:false
-- Capability Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sCapSearchMaxResults', 'SESSION','Capability search parameters.','USER', 'Number', 100, 0, 'Capability Search', '5.1.5', 0);

--changeSet 0utl_config_parm:454 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sCapSearchMethod', 'SESSION','Capability search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Capability Search', '5.1.5', 0);

--changeSet 0utl_config_parm:455 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sCapSearchCapName','SESSION','Capability search parameters.','USER', '', '', 0, 'Capability Search', '5.1.5', 0);

--changeSet 0utl_config_parm:456 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sCapSearchCapCode','SESSION','Capability search parameters.','USER', '', '', 0, 'Capability Search', '5.1.5', 0);

--changeSet 0utl_config_parm:457 stripComments:false
-- Inventory To Issue Search Criteria
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sInventoryToIssueSearchMethod','SESSION','Inventory To Issue search parameters.','USER','String', 'STARTS_WITH', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:458 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sInventoryToIssueSearchMaxResults','SESSION','Inventory To Issue search parameters.','USER', 'Number', '100', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:459 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventoryToIssueSearchPartName', 'SESSION','Inventory To Issue search parameters.','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:460 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventoryToIssueSearchPartNoOem','SESSION','Inventory To Issue search parameters.','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:461 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventoryToIssueSearchAlternateParts','SESSION','Inventory To Issue search parameters.','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:462 stripComments:false
-- User Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sUserSearchMaxResults', 'SESSION','User search parameters.','USER', 'Number', 100, 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:463 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sUserSearchMethod', 'SESSION','User search parameters.','USER', 'String', 'STARTS_WITH', 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:464 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchFirstName','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:465 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchLastName','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:466 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchHrCode','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:467 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchUsername','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '5.1', 0);

--changeSet 0utl_config_parm:468 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchDepartment','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '0612', 0);

--changeSet 0utl_config_parm:469 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchOrganization','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '6.8', 0);

--changeSet 0utl_config_parm:470 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sUserSearchSkill','SESSION','User search parameters.','USER', '', '', 0, 'User Search', '0612', 0);

--changeSet 0utl_config_parm:471 stripComments:false
-- Manufacturer Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sManufactSearchName', 'SESSION','Manufacturer search parameters.','USER', '', '', 0, 'Manufact Search', '5.1', 0);

--changeSet 0utl_config_parm:472 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sManufactSearchCd', 'SESSION','Manufacturer search parameters.','USER', '', '', 0, 'Manufact Search', '5.1', 0);

--changeSet 0utl_config_parm:473 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sManufactSearchMaxResults', 'SESSION','Manufacturer search parameters.','USER', 'Number', 100, 0, 'Manufact Search', '5.1', 0);

--changeSet 0utl_config_parm:474 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sManufactSearchMethod', 'SESSION','Manufacturer search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Manufact Search', '5.1', 0);

--changeSet 0utl_config_parm:475 stripComments:false
-- Vendor Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchName', 'SESSION','Vendor search parameters.','USER', '', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:476 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchCd', 'SESSION','Vendor search parameters.','USER', '', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:477 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sVendorSearchMaxResults', 'SESSION','Vendor search parameters.','USER', 'Number', '100', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:478 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sVendorSearchMethod', 'SESSION','Vendor search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:479 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchAccountNumber', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:480 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchStatus', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:481 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchCountry', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '0609', 0);

--changeSet 0utl_config_parm:482 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchState', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '0609', 0);

--changeSet 0utl_config_parm:483 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchCity', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '0609', 0);

--changeSet 0utl_config_parm:484 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchAirportCd', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '0806', 0);

--changeSet 0utl_config_parm:485 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchType', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:486 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'sVendorSearchExpiredOnly', 'SESSION','Vendor search parameters.','USER', 'String', '', 0, 'Vendor Search', '5.1', 0);

--changeSet 0utl_config_parm:487 stripComments:false
-- Vendor Search By Type Parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchVendorCdByVendorCode', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:488 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchVendorTypeByVendorCode', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:489 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchOrderTypeByOrderTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:490 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchOrgCdByOrderTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:491 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchStatusByOrderTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:492 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchOrgCdByServiceTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:493 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchServiceTypeByServiceTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:494 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVendorSearchStatusByServiceTypeStatus', 'SESSION','Vendor search by type parameters.','USER', 'STRING', '', 0, 'Vendor Search By Type', '8.1-SP2', 0);

--changeSet 0utl_config_parm:495 stripComments:false
-- Owner Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOwnerSearchName', 'SESSION','Owner search parameters.','USER', '', '', 0, 'Owner Search', '5.1', 0);

--changeSet 0utl_config_parm:496 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOwnerSearchCd', 'SESSION','Owner search parameters.','USER', '', '', 0, 'Owner Search', '5.1', 0);

--changeSet 0utl_config_parm:497 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sOwnerSearchMaxResults', 'SESSION','Owner search parameters.','USER', 'Number', 100, 0, 'Owner Search', '5.1', 0);

--changeSet 0utl_config_parm:498 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sOwnerSearchMethod', 'SESSION','Owner search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Owner Search', '5.1', 0);

--changeSet 0utl_config_parm:499 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sOwnerSearchLocal', 'SESSION','Owner search parameters','USER', 'TRUE/FALSE', 'false', 0, 'Owner Search', '0706', 0);

--changeSet 0utl_config_parm:500 stripComments:false
-- Part Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchPartName', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:501 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchOEMPartNo', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:502 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchPartModel', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '8.0', 0);

--changeSet 0utl_config_parm:503 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchPartStatus', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:504 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchBomItem', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:505 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchBomPart', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:506 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchPartUse', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:507 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchPartType', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:508 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sPartSearchMaxResults', 'SESSION','Part search parameters','USER', 'Number', 100, 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:509 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sPartSearchMethod', 'SESSION','Part search parameters','USER', 'String', 'STARTS_WITH', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:510 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchManufacturerCd', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:511 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchAssemblyCd', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:512 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchBomPosition', 'SESSION','Part search parameters','USER', '', '', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:513 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchVendor', 'SESSION','Stores the last value entered as the Vendor on the Part Search page.','USER',  'Number', 'null', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:514 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartSearchVendorPartNo', 'SESSION','Stores the last value entered as the Vendor part no. on the Part Search page.','USER',  'Number', 'null', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:515 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartStockNo', 'SESSION','Stores the last value entered as the stock no. on the Part Search page.','USER',  'Number', 'null', 0, 'Part Search', '5.1', 0);

--changeSet 0utl_config_parm:516 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPMAPart', 'SESSION','Stores the PMA Part checked value on the Part Search page.','USER',  'TRUE/FALSE', 'FALSE', 0, 'Part Search', '6.8', 0);

--changeSet 0utl_config_parm:517 stripComments:false
-- Shipment Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchNumber', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:518 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchType', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:519 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchStatus', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:520 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchWaybill', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:521 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchRMANumber', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1.5.2', 0);

--changeSet 0utl_config_parm:522 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchPONumber', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:523 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchPOExternalReference', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:524 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchPOVendor', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:525 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentFinalDestination', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '6.8', 0);

--changeSet 0utl_config_parm:526 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchFrom', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:527 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchTo', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:528 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchShippedBefore', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:529 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchShippedAfter', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:530 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchReceivedBefore', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:531 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchReceivedAfter', 'SESSION','Shipment search parameters','USER', '', '', 0,'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:532 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchPartName', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:533 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchSerialNo', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:534 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sShipmentSearchPartNo', 'SESSION','Shipment search parameters','USER', '', '', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:535 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sShipmentSearchMaxResults', 'SESSION','Shipment search parameters','USER', 'Number', 100, 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:536 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sShipmentSearchMethod', 'SESSION','Shipment search parameters','USER', 'String', 'STARTS_WITH', 0, 'Shipment Search', '5.1', 0);

--changeSet 0utl_config_parm:537 stripComments:false
-- Flight Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchAircraft', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:538 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchAircraftName', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:539 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchFlightName', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:540 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchMasterFlightNo', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:541 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchLogReference', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:542 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchOperationType', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:543 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchDepAirport', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:544 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchArrAirport', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:545 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchDepBefore', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:546 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchDepAfter', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:547 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchArrBefore', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:548 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchArrAfter', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:549 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HIST_OPEN', 'sFlightSearchHistoric', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:550 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sFlightSearchMaxResults', 'SESSION','Flight search parameters','USER', 'Number', 100, 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:551 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sFlightSearchMethod', 'SESSION','Flight search parameters','USER', 'String', 'STARTS_WITH', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:552 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFlightSearchFlightReason', 'SESSION','Flight search parameters','USER', '', '', 0, 'Flight Search', '5.1', 0);

--changeSet 0utl_config_parm:553 stripComments:false
-- Inventory Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchPartName', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:554 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchHighestInventory', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:555 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchOemPartNo', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:556 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchAssembly', 'SESSION','Inventory search parameters','USER', 'String', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:557 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchOemSerialNo', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:558 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchBomItem', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:559 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchIcn', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:560 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchBomPart', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:561 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchLocation', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:562 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INSTALLED_UNINSTALLED', 'sInventorySearchInstalled', 'SESSION','Inventory search parameters','USER', 'STRING', 'INSTALLED_UNINSTALLED', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:563 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sInventorySearchIncludeSublocations', 'SESSION','Inventory search parameters','USER', 'TRUE/FALSE', 'TRUE', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:564 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sInventorySearchIncludeIssued', 'SESSION','Inventory search parameters','USER', 'TRUE/FALSE', 'FALSE', 0, 'Inventory Search', '5.1.5', 0);

--changeSet 0utl_config_parm:565 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchOwner', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:566 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchReceivedBefore', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:567 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchReceivedAfter', 'SESSION','Inventory search parameters','USER', '', '', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:568 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sInventorySearchIncludeArchived', 'SESSION','Inventory search parameters','USER', 'TRUE/FALSE', 'FALSE', 0 ,'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:569 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sInventorySearchIncludeScrapped', 'SESSION','Inventory search parameters','USER', 'TRUE/FALSE', 'FALSE', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:570 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sInventorySearchMaxResults', 'SESSION','Inventory search parameters','USER', 'Number', 100, 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:571 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sInventorySearchMethod', 'SESSION','Inventory search parameters','USER', 'String', 'STARTS_WITH', 0, 'Inventory Search', '5.1', 0);

--changeSet 0utl_config_parm:572 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchInventoryClass', 'SESSION','Inventory search parameters','USER', 'STRING', '', 0, 'Inventory Search', '0609', 0);

--changeSet 0utl_config_parm:573 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchFinancialClass', 'SESSION','Inventory search parameters','USER', 'STRING', '', 0, 'Inventory Search', '0609', 0);

--changeSet 0utl_config_parm:574 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInventorySearchCondition', 'SESSION','Inventory search parameters','USER', 'STRING', '', 0, 'Inventory Search', '0609', 0);

--changeSet 0utl_config_parm:575 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sInventorySearchIncludeZeroQty', 'SESSION','Inventory search parameters','USER', 'TRUE/FALSE', 'FALSE', 0, 'Inventory Search', '0609', 0);

--changeSet 0utl_config_parm:576 stripComments:false
-- Aircraft Planner Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sAircraftPlannerAircraftKey', 'SESSION','Aircraft Planner search parameters','USER', 'String', '', 0, 'Aircraft Planner Search', '5.1', 0);

--changeSet 0utl_config_parm:577 stripComments:false
-- Menu Item Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sMenuItemSearchMethod', 'SESSION','Menu item search parameters','USER', 'String', 'STARTS_WITH', 0, 'Menu Item Search', '5.1', 0);

--changeSet 0utl_config_parm:578 stripComments:false
-- Location Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sLocationSearchMaxResults', 'SESSION','Location search parameters','USER', 'Number', 100, 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:579 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sLocationSearchMethod', 'SESSION','Location search parameters','USER', 'String', 'STARTS_WITH', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:580 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchName', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:581 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchCode', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:582 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchCity', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:583 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchZip', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:584 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchAddress1', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:585 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchAddress2', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:586 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchOrganization', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '6.8', 0);

--changeSet 0utl_config_parm:587 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchType', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:588 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sLocationSearchIncludeSublocations', 'SESSION','Location search parameters','USER', 'TRUE/FALSE', 'true', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:589 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sLocationSearchMaintenance', 'SESSION','Location search parameters','USER', 'TRUE/FALSE', 'false', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:590 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sLocationSearchOnlyShowRoot', 'SESSION','Location search parameters','USER', 'TRUE/FALSE', 'false', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:591 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchCountry', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:592 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sLocationSearchState', 'SESSION','Location search parameters','USER', 'String', '', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:593 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALL', 'sLocationSearchLocationVisibility', 'SESSION','Location search parameters','USER', 'String', 'ALL', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:594 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'sLocationSearchShopRepair', 'SESSION','Location search parameters','USER', 'TRUE/FALSE', 'false', 0, 'Location Search', '5.1', 0);

--changeSet 0utl_config_parm:595 stripComments:false
-- Stock Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchMaxResults', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', 100, 'Number', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:596 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchStockCd', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:597 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchStockName', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:598 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchMethod', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', 'STARTS_WITH', 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:599 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchLocationCode', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:600 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchOEMPartNo', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:601 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchManufacturerCode', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:602 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchAssemblyCode', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:603 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchIncludeSublocations', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', 'TRUE', 'TRUE/FALSE', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:604 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchBomItem', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:605 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchBomPart', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:606 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchUom', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:607 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sStockSearchAbc', 'SESSION', NULL, 0, 'Stock search parameters', 'USER', NULL, 'String', 0, 'Stock Search', '5.1', 0);

--changeSet 0utl_config_parm:608 stripComments:false
-- Transfer Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sTransferSearchMaxResults', 'SESSION','Transfer search parameters.','USER', 'Number', 100, 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:609 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sTransferSearchMethod', 'SESSION','Transfer search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:610 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchSupplyLocation','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:611 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferType','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:612 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferStatus','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:613 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferFrom','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:614 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferTo','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:615 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferredBefore','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:616 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchTransferredAfter','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:617 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchPartName','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:618 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchOEMSerialNo','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:619 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTransferSearchOEMPartNo','SESSION','Transfer search parameters.','USER', '', '', 0, 'Transfer Search', '5.1', 0);

--changeSet 0utl_config_parm:620 stripComments:false
-- Department Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sDepartmentSearchMaxResults', 'SESSION','Department search parameters','USER', 'Number', 100, 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:621 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sDepartmentSearchMethod', 'SESSION','Department search parameters','USER', 'String', 'STARTS_WITH', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:622 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDepartmentSearchName', 'SESSION','Department search parameters','USER', 'String', '', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:623 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDepartmentSearchCode', 'SESSION','Department search parameters','USER', 'String', '', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:624 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDepartmentSearchType', 'SESSION','Department search parameters','USER', 'String', '', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:625 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sDepartmentSearchIncludeSublocations', 'SESSION','Department search parameters','USER', 'TRUE/FALSE', 'true', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:626 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDepartmentSearchLocation', 'SESSION','Department search parameters','USER', 'String', '', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:627 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDepartmentSearchUser', 'SESSION','Department search parameters','USER', 'String', '', 0, 'Department Search', '5.1', 0);

--changeSet 0utl_config_parm:628 stripComments:false
-- Authority Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sAuthoritySearchMaxResults', 'SESSION', 100, 0, 'Authority search parameters', 'USER', '100', 'Number', 0, 'Authority Search', '5.1', 0);

--changeSet 0utl_config_parm:629 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sAuthoritySearchMethod', 'SESSION', 'STARTS_WITH', 0, 'Authority search parameters', 'USER', 'STARTS_WITH', 'String', 0, 'Authority Search', '5.1', 0);

--changeSet 0utl_config_parm:630 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sAuthoritySearchName', 'SESSION', null, 0, 'Authority search parameters', 'USER', '', 'String', 0, 'Authority Search', '5.1', 0);

--changeSet 0utl_config_parm:631 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sAuthoritySearchCode', 'SESSION', null, 0, 'Authority search parameters', 'USER', '', 'String', 0, 'Authority Search', '5.1', 0);

--changeSet 0utl_config_parm:632 stripComments:false
-- Part Request Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sPartRequestSearchMaxResults', 'SESSION','Part Request search parameters.','USER', 'Number', 100, 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:633 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sPartRequestSearchMethod', 'SESSION','Part Request search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:634 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchRequestID','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:635 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchExternalReference','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:636 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchRequestedPartNo','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:637 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchNeededByTask','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:638 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchAccountCode','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:639 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchRequestedByHr','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:640 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchStatus','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:641 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchAircraft','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:642 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchAircraftName','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1', 0);

--changeSet 0utl_config_parm:643 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchWhereNeeded','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1.3', 0);

--changeSet 0utl_config_parm:644 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchType','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1.3', 0);

--changeSet 0utl_config_parm:645 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPartRequestSearchPriority','SESSION','Part Request search parameters.','USER', '', '', 0, 'Part Req Search', '5.1.3', 0);

--changeSet 0utl_config_parm:646 stripComments:false
-- Purchase Order Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sPOSearchMaxResults', 'SESSION','PO search parameters','USER', 'Number', 100, 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:647 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sPOSearchMethod', 'SESSION','PO search parameters','USER', 'String', 'STARTS_WITH', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:648 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchBarcode', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:649 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchStatus', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:650 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchType', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:651 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchPriority', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '0609', 0);

--changeSet 0utl_config_parm:652 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchContactHr', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '0609', 0);

--changeSet 0utl_config_parm:653 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchVendorCd', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:654 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchRequestID', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:655 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchExternalReference', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:656 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchPartName', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet 0utl_config_parm:657 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchOEMPartNo', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '5.1', 0);

--changeSet OPER-21304:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchPartGroupPurchaseType', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '8.3', 0);

--changeSet  OPER-23054:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPOSearchAuthorizationStatusCd', 'SESSION','PO search parameters','USER', 'String', '', 0, 'PO Search', '8.3', 0);

--changeSet 0utl_config_parm:659 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchVendorByDateRange', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:660 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchTypeByPartNo', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:661 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchOrderNoByOrderNumber', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:662 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchExternalRefByOrderNumber', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:663 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchStatusByPartNo', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:664 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchOEMPartNoByPartNo', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:665 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchPromisedAfterByAOGAuthor', 'SESSION','Order search parameters.','USER', 'Date', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:666 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchTypeByAOGAuthor', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:667 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchPromisedAfterByDateRange', 'SESSION','Order search parameters.','USER', 'Date', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:668 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchTypeByDateRange', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:669 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchStatusByDateRange', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:670 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchPriorityByDateRange', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:671 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchContactByContact', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:672 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchTypeByContact', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:673 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchStatusByContact', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0709', 0);

--changeSet 0utl_config_parm:674 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchStatusByAircraft', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0806', 0);

--changeSet 0utl_config_parm:675 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchPriorityByAircraft', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '0806', 0);

--changeSet 0utl_config_parm:676 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchOrgCodeByReceiptOrg', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '7.5', 0);

--changeSet OPER-23208:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOrderSearchPartGroupPurchaseTypeByContact', 'SESSION','Order search parameters.','USER', 'String', '', 0, 'Order Search', '8.3', 0);

--changeSet 0utl_config_parm:677 stripComments:false
-- Purchase Requests Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPurchaseRequestAssmblCd', 'SESSION','Purchase Request search parameters.','USER', 'String', '', 0, 'Purchase Requests', '6.8.5', 0);

--changeSet 0utl_config_parm:678 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPurchaseRequestPurchType', 'SESSION','Purchase Request search parameters.','USER', 'String', '', 0, 'Purchase Requests', '6.8.5', 0);

--changeSet 0utl_config_parm:679 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sPurchaseRequestWONumber', 'SESSION','Purchase Request search parameters.','USER', 'String', '', 0, 'Purchase Requests', '6.8.5', 0);

--changeSet 0utl_config_parm:680 stripComments:false
-- FleetTaskLabourSummarySearch Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskLabourSummarySearchAssembl', 'SESSION','Task Labour Summary search parameters.','USER', 'String', '', 0, 'Task Labour Summary Search', '0712', 0);

--changeSet 0utl_config_parm:681 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskLabourSummarySearchDeviation', 'SESSION','Task Labour Summary search parameters.','USER', 'Integer', '', 0, 'Task Labour Summary Search', '8.3-SP1', 0);

--changeSet 0utl_config_parm:682 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskLabourSummarySearchLabourSkill', 'SESSION','Task Labour Summary search parameters.','USER', 'String', '', 0, 'Task Labour Summary Search', '0712', 0);

--changeSet 0utl_config_parm:683 stripComments:false
-- Database Rule Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDbRuleSearchRuleId', 'SESSION','Database Rule Search parameters','USER', 'String', null, 0, 'Database Rule Search', '0612_baseedit', 0);

--changeSet 0utl_config_parm:684 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDbRuleSearchTypeCd', 'SESSION','Database Rule Search parameters','USER', 'String', null, 0, 'Database Rule Search', '0612_baseedit', 0);

--changeSet 0utl_config_parm:685 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDbRuleSearchSeverityCd', 'SESSION','Database Rule Search parameters','USER', 'String', null, 0, 'Database Rule Search', '0612_baseedit', 0);

--changeSet 0utl_config_parm:686 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDbRuleSearchName', 'SESSION','Database Rule Search parameters','USER', 'String', null, 0, 'Database Rule Search', '0612_baseedit', 0);

--changeSet 0utl_config_parm:687 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDbRuleSearchDescription', 'SESSION','Database Rule Search parameters','USER', 'String', null, 0, 'Database Rule Search', '0612_baseedit', 0);

--changeSet 0utl_config_parm:688 stripComments:false
-- Issue Tab Search Criteria
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sRequestIssueTabSearchMethod','SESSION','Issue Tab search parameters.','USER','String', 'STARTS_WITH', 0, 'Issue Tab Search', '5.1', 0);

--changeSet 0utl_config_parm:689 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE,PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sRequestIssueTabSearchMaxResults','SESSION','Issue Tab search parameters.','USER', 'Number', '100', 0, 'Issue Tab Search', '5.1', 0);

--changeSet 0utl_config_parm:690 stripComments:false
-- IETM Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sIetmSearchMaxResults', 'SESSION','Ietm search parameters.','USER', 'Number', 100, 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:691 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sIetmSearchMethod', 'SESSION','Ietm search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:692 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sIetmSearchName', 'SESSION','Ietm search name parameters.','USER', 'String', '', 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:693 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sIetmSearchIetmName', 'SESSION','Ietm search name parameters.','USER', 'String', '', 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:694 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sIetmSearchReference', 'SESSION','Ietm search code parameters.','USER', 'String', '', 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:695 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sIetmSearchType', 'SESSION','Ietm search type parameters.','USER', 'String', '', 0, 'Ietm Search', '5.1', 0);

--changeSet 0utl_config_parm:696 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAttachType', 'SESSION','Ietm search Attachment type parameters.','USER', 'String', '', 0, 'Ietm Search', '0806', 0);

--changeSet 0utl_config_parm:697 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sIetmSearchAssembly', 'SESSION','Ietm search assembly parameters.','USER', 'String', '', 0, 'Ietm Search', '7.0', 0);

--changeSet 0utl_config_parm:698 stripComments:false
-- Account Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchAccountCd', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:699 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchAccountName', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:700 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchAccountType', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:701 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchExternalRef', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:702 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchTCodeCd', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:703 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sAccountSearchMaxResults', 'SESSION','Account search parameters.','USER', 'String', '100', 0, 'Account Search', '0609', 0);

--changeSet 0utl_config_parm:704 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAccountSearchAccountStatus', 'SESSION','Account search parameters.','USER', 'String', null, 0, 'Account Search', '8.0-SP2', 0);

--changeSet 0utl_config_parm:705 stripComments:false
-- Failure Effect Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchAssembly', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:706 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchAssemblyName', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:707 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchCode', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:708 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchName', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:709 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchSeverity', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:710 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchType', 'SESSION','Failure Effect search parameters.','USER', 'String', null, 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:711 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFailEffectSearchMethod', 'SESSION','Failure Effect search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:712 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sFailEffectSearchMaxResults', 'SESSION','Failure Effect search parameters.','USER', 'String', '100', 0, 'Failure Effect Search', '0703', 0);

--changeSet 0utl_config_parm:713 stripComments:false
-- Measurement Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchAssembly', 'SESSION','Measurement search parameters.','USER', 'String', null, 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:714 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchAssemblyName', 'SESSION','Measurement search parameters.','USER', 'String', null, 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:715 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchCode', 'SESSION','Measurement search parameters.','USER', 'String', null, 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:716 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchName', 'SESSION','Measurement search parameters.','USER', 'String', null, 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:717 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchDomainType', 'SESSION','Measurement search parameters.','USER', 'String', null, 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:718 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sMeasurementSearchMethod', 'SESSION','Measurement search parameters.','USER', 'String', 'STARTS_WITH', 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:719 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sMeasurementSearchMaxResults', 'SESSION','Measurement search parameters.','USER', 'String', '100', 0, 'Measurement Search', '0706', 0);

--changeSet 0utl_config_parm:720 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMeasurementSearchType', 'SESSION','Measurement search parameters.','USER', 'TRUE/FALSE', '', 0,'Measurement Search', '7.0',0);

--changeSet 0utl_config_parm:721 stripComments:false
-- T-Code Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sTCodeSearchTCodeCd', 'SESSION','T-Code search parameters.','USER', 'String', null, 0, 'T-Code Search', '0609', 0);

--changeSet 0utl_config_parm:722 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sTCodeSearchTCodeName', 'SESSION','T-Code search parameters.','USER', 'String', null, 0, 'T-Code Search', '0609', 0);

--changeSet 0utl_config_parm:723 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'sTCodeSearchMaxResults', 'SESSION','T-Code search parameters.','USER', 'String', '100', 0, 'T-Code Search', '0609', 0);

--changeSet 0utl_config_parm:724 stripComments:false
-- Slow Moving Stock Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('12', 'sSlowMovingStockMonths', 'SESSION', 'Slow Moving Stock todo list parameters', 'USER', 'Number', '12', 0, 'Slow Moving Stock', '0609',0);

--changeSet 0utl_config_parm:725 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('50', 'sSlowMovingStockPercentage', 'SESSION', 'Slow Moving Stock todo list parameters', 'USER', 'Number', '50', 0, 'Slow Moving Stock', '0609',0);

--changeSet 0utl_config_parm:726 stripComments:false
-- Surplus Stock Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('200', 'sSurplusStockPercentage', 'SESSION', 'Surplus Stock todo list parameters', 'USER', 'Number', '200', 0, 'Surplus Stock', '0609', 0);

--changeSet 0utl_config_parm:727 stripComments:false
-- Surplus Stock Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchOriginator', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', null, 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:728 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchTaskName', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', null, 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:729 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchTaskSubtype', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', null, 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:730 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchTaskType', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', null, 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:731 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchNoIssueAccount', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', '0', 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:732 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchWorkType', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'STRING', null, 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:733 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSetIssueAccountSearchMaxResults', 'SESSION', 'Set Issue Accounts parameters', 'USER', 'INTEGER', '100', 0, 'Set Issue Account Search', '0709', 0);

--changeSet 0utl_config_parm:734 stripComments:false
-- Status Board Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('NONE', 'sStatusBoardAuthority', 'SESSION', 'Status Board Authority Parameter', 'USER', 'STRING', 'NONE', 0, 'Status Board', '0712', 0);

--changeSet 0utl_config_parm:735 stripComments:false
-- Integration Message Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sOnlyFailures', 'SESSION', 'Integration Message Search Parameter', 'USER', 'TRUE/FALSE', 'FALSE', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:736 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sHistoric', 'SESSION', 'Integration Message Search Parameter', 'USER', 'TRUE/FALSE', 'FALSE', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:737 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sSearchMode', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:738 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFromDate', 'SESSION', 'Integration Message Search Parameter', 'USER', 'Date', '', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:739 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sToDate', 'SESSION', 'Integration Message Search Parameter', 'USER', 'Date', '', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:740 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sMsgId', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:741 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAllStreams', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Integration Message Search', '7.0', 0);

--changeSet 0utl_config_parm:742 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sStreamCd', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Integration Message Search', '7.0', 0);

--changeSet 0utl_config_parm:743 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sPeriodLength', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'days', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:744 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sPeriod', 'SESSION', 'Integration Message Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Integration Message Search', '6.8.5', 0);

--changeSet 0utl_config_parm:745 stripComments:false
-- Reliability Note Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sReliabilityNoteSearchType', 'SESSION','Reliability Note Search Parameters','USER', 'String', '', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:746 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sReliabilityNoteSearchRecordedDate', 'SESSION', 'Reliability Note Search  Parameter', 'USER', 'Date', '', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:747 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchRecordedDateType', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:748 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sReliabilityNoteSearchResolvedDate', 'SESSION', 'Reliability Note Search  Parameter', 'USER', 'Date', '', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:749 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchResolvedDateType', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:750 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchPartNoByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:751 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchAircraftByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:752 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchAircraftNameByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:753 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchNoteTypeByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:754 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchRecordedByByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:755 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchResolvedByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:756 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchResolvedByByPartNo', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:757 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchAssemblyByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:758 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchConfigSlotByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:759 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'sReliabilityNoteSearchIncludeSubconfigSlotsByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'TRUE/FALSE', 'TRUE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:760 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchAircraftByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:761 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchAircraftNameByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:762 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchNoteTypeByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:763 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchRecordedByByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:764 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchResolvedByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:765 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sReliabilityNoteSearchResolvedByByConfigSlot', 'SESSION', 'Reliability Note Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Reliability Note Search', '7.0', 0);

--changeSet 0utl_config_parm:766 stripComments:false
-- MyOpenOrders Filter Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sMyOpenOrdersViewType', 'SESSION','My Open Orders options parameter.','USER', 'String', '', 0, 'Purchasing Agent To Do List', '6.9', 0);

--changeSet 0utl_config_parm:767 stripComments:false
-- InboundShipments Filter Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sInboundShipmentsViewType', 'SESSION','Inbound Shipments options parameter.','USER', 'String', '', 0, 'Storeroom Clerk To Do List', '6.9', 0);

--changeSet 0utl_config_parm:768 stripComments:false
-- OutboundShipments Filter Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sOutboundShipmentsViewType', 'SESSION','Outbound Shipments options parameter.','USER', 'String', '', 0, 'Storeroom Clerk To Do List', '6.9', 0);

--changeSet 0utl_config_parm:769 stripComments:false
-- Fault Evaluation Tab Filter Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultEvalWorkPackage', 'SESSION','Fault Evaluation Tab search parameters.','USER', '', '', 0, 'SESSION', '6.6.9', 0);

--changeSet 0utl_config_parm:770 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFaultEvalZone', 'SESSION','Fault Evaluation Tab search parameters.','USER',  '', '', 0, 'SESSION', '6.6.9', 0);

--changeSet 0utl_config_parm:771 stripComments:false
-- FleetSettings page
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sFleet', 'SESSION','Selected Fleet parameter','USER',  '', '', 0, 'Maint - Planning', '7.2 SP1', 0);

--changeSet 0utl_config_parm:772 stripComments:false
-- Spec 2000 Standards
/**************************/
/* Secured resource codes */
/**************************/
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'SPEC2000_ALLOW_DUPLICATES', 'SECURED_RESOURCE','Permission to allow duplicate values.','USER', 'TRUE/FALSE', 'FALSE', 1,'Admin - Spec 2000', '0709', 0);

--changeSet 0utl_config_parm:773 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'EXPECTED_TURN_IN_DELAY', 'LOGIC','Expected number of days between an issue and a corresponding turn-in.','GLOBAL', 'NUMBER', '1', 1,'Supply - Transfers', '6.7',0);

--changeSet 0utl_config_parm:774 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'PRE_DRAW_ENABLE_PRINT_ISSUE', 'LOGIC','Controls the visibility of the re-print issue ticket checkbox on Pre-Draw Inventory page. ','GLOBAL', 'TRUE/FALSE', 'TRUE', 1,'SYSTEM', '6.8', 0);

--changeSet 0utl_config_parm:775 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'PRE_DRAW_DEFAULT_PRINT_ISSUE', 'LOGIC','Controls whether the re-print issue ticket checkbox on Pre-Draw Inventory page is checked by default.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1,'SYSTEM', '6.8', 0);

--changeSet 0utl_config_parm:776 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'AUTO_COMPLETE_PUTAWAY', 'LOGIC','Controls the automatic put away behavior','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'Supply - Transfers', '6.8.5', 0);

--changeSet 0utl_config_parm:777 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'AUTO_PRINT_PUTAWAY_TICKET', 'LOGIC','Controls the automatic printing of the put away ticket','GLOBAL', 'TRUE/FALSE', 'FALSE', 1 , 'Supply - Transfers', '6.8.10', 0);

--changeSet 0utl_config_parm:778 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'UNEXPECTED_TURN_IN_SEARCH_DAYS', 'LOGIC', 'When issuing inventory, the number of days before the issue date that will be checked for unexpected turn ins.', 'USER', 'NUMBER', '1', 1, 'Supply - Transfers', '7.0', 0);

--changeSet 0utl_config_parm:779 stripComments:false
-- Task
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ENABLE_NEGATIVE_USAGES', 'LOGIC','Allow the user to input a negative usage parm for a check','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'Maint - Work Packages', '0806',0);

--changeSet 0utl_config_parm:780 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ENABLE_PART_REQ_JOB_FOR_NONBASELINED', 'LOGIC','Enables the Part Request Auto Generation Job to retrieve non-baselined tasks','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'Maint - Work Packages', '0806',0);

--changeSet 0utl_config_parm:781 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_AUP_ON_ADD_PART_REQUIREMENT', 'LOGIC','Controls the visibility of AUP column while adding part requirement for task','GLOBAL', 'TRUE/FALSE', 'TRUE', 1 , 'MXWEB', '6.8.5', 0);

--changeSet 0utl_config_parm:782 stripComments:false
INSERT INTO utl_config_parm ( PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ('TRUE', 'EFFECTIVE_FROM_TIME_END_OF_DAY', 'LOGIC', 'This parameter is used to determine the default time value for effective from date field in create requirement/block/reference document pages', 'GLOBAL', 'TRUE/FALSE' , 'TRUE', 1, 'Maint Program - Requirements/Blocks/Reference Documents', '7.0', 0);

--changeSet 0utl_config_parm:783 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ERROR', 'LABOR_IN_WORK_IN_COMPLETE_TASK', 'LOGIC', 'The labor is in work on a completed task.', 'USER', 'ERROR', 'ERROR', 1, 'Maint - Tasks', '7.2-SP1', 0);

--changeSet 0utl_config_parm:784 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INFO', 'TASK_USAGE_SNAPSHOT_EXCEEDS_CURRENT_USAGE', 'LOGIC','Validation severity level.  Task usage snapshot edited to values exceeding inventory''s current usage.','USER', 'WARNING/ERROR/INFO', 'INFO', 1,'Maint - Tasks', '8.0',0);

--changeSet 0utl_config_parm:785 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'AUTO_COMPLETE_TASK_WHEN_CANCEL_LAST_JIC', 'LOGIC','Permission to auto complete the task when cancel the last JIC and there is at least one complete JIC.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'Maint - Tasks', '8.0-SP1',0);

--changeSet 0utl_config_parm:786 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ALLOW_UNRELATED_PART_GROUP_IN_PART_REQUIREMENT', 'LOGIC', 'Allow part requirements to be scheduled to a fault/task when the config slot of the part is unrelated to the assembly of the fault.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Tasks - Part Requirements', '8.0-SP2', 0);

--changeSet 0utl_config_parm:787 stripComments:false
-- Check
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('10', 'MAX_FLEET_DUE_LIST_DAY_COUNT', 'MXWEB','The Maximum number of days which can be searched for on the Fleet Due List','GLOBAL', 'Number', '10', 0, 'Maint - Tasks', '6.8.6', 0);

--changeSet 0utl_config_parm:788 stripComments:false
-- Labour
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sLabourAssignmentSearchShowAllHr', 'SESSION','Labour assignment search parameters for show all HR','USER', 'TRUE/FALSE', 'true', 0, 'Labour Assignment Search', '6.9', 0);

--changeSet 0utl_config_parm:789 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'sLabourAssignmentSearchShowOnlySchedHr', 'SESSION','Labour assignment search parameters for show only scheduled HR','USER', 'TRUE/FALSE', 'true', 0, 'Labour Assignment Search', '6.9', 0);

--changeSet 0utl_config_parm:790 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'sLabourAssignmentSearchNextXHours', 'SESSION','Labour assignment search parameters for next x hours','USER', 'NUMBER', '8', 0, 'Labour Assignment Search', '6.9', 0);

--changeSet 0utl_config_parm:791 stripComments:false
-- Inventory
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('MAXIMUM_EDITABLE_SUBITEMS', 'MXWEB', 500, 0, 'The maximum number of rows that will be shown on the EditIventorySubItems page before showing a warning', 'GLOBAL', 500, 'integers', 1, 'Supply - Inventory', '6.4.2', 0, 0);

--changeSet 0utl_config_parm:792 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_PRINT_SRV_TAG_INSPREQ', 'TRUE', 'LOGIC', 0,'Determines if the user is allowed to print a serviceable part tag for the INSPREQ inventory', 'GLOBAL', 'true/false', 'true', 1, 'Core Logic', '6.9', 0);

--changeSet 0utl_config_parm:793 stripComments:false
-- Fault
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('WARN', 'FAULT_DEFERRAL_REFERENCE_VALIDATION', 'LOGIC','Controls Validation Logic for Invalid Deferral References in the Defer Fault workflow','USER', 'OFF/WARN/ERROR', 'WARN', 1,'Maint - Faults', '7.5',0);

--changeSet 0utl_config_parm:794 stripComments:false
-- Electronic Logbook Adapter
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'ELA_USERNAME', 'ELA' , 'mxintegration', 0, 'The utl_user.username of the designated ELA authorization system user', 'GLOBAL' ,'String', '',  1, 'ELA', '7.2', 0);

--changeSet 0utl_config_parm:795 stripComments:false
-- Earliest Deadlines Adapter
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALL', 'EARLIEST_DEADLINE_TASK_ORIGINATOR_CODES', 'LOGIC', 0, 'The list of originator codes that should be considered when getting the earliest deadlines.','GLOBAL', 'ALL/list of originator codes', 'ALL', 1,'Adapters - Earliest Deadlines', '8.0', 0);

--changeSet 0utl_config_parm:796 stripComments:false
-- Flight
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_COMPLETE_FUTURE_FLIGHT', 'LOGIC','true', 0, 'Whether flights may be completed with actual up/down/out/in dates in the future.', 'USER', 'true/false', 'true', 1, 'Ops - Flights', '6.9', 0);

--changeSet 0utl_config_parm:797 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DAYS_IN_THE_PAST', 'INFO', 'LOGIC', 0, 'Severity of the validation for creation/completion of a flight further in the past than allowed.', 'USER', 'WARNING/ERROR/INFO', 'INFO', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:798 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DAYS_IN_THE_PAST_VALUE', '3', 'LOGIC', 0, 'How many days in the past the planning and completion of a flight is allowed.', 'USER', 'Number', '3', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:799 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DAYS_IN_THE_FUTURE', 'INFO', 'LOGIC', 0, 'Severity of the validation for creation/update of a flight further in the future than allowed.', 'USER', 'WARNING/ERROR/INFO', 'INFO', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:800 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DAYS_IN_THE_FUTURE_VALUE', '60', 'LOGIC', 0, 'How many days in the future the planning and completion of a flight is allowed.', 'USER', 'Number', '60', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:801 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DURATION', 'INFO', 'LOGIC', 0, 'Severity of the validation for flight duration more than allowed.', 'USER', 'WARNING/ERROR/INFO', 'INFO', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:802 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MAX_FLIGHT_DURATION_VALUE', '', 'LOGIC', 0, 'How many hours maximum a flight can last, configurable by assembly.', 'USER', 'String of format: assmbl_bom_cd=hours,assmbl_bom_cd=hours...Hours value has to be positive whole number', '', 1, 'Ops - Flights', '7.1-SP2', 0);

--changeSet 0utl_config_parm:803 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_EDIT_TSN_ON_USAGE_ENTRY', 'TRUE', 'MXWEB', 0, 'Allows users to edit usage TSN/TSO/TSI values on the create and edit flight pages.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '8.2-SP3', 0);

--changeSet 0utl_config_parm:804 stripComments:false
-- Purchasing - Prices
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'SHOW_COST', 'SECURED_RESOURCE','Indicates if cost information is displayed to the user.','USER', 'TRUE/FALSE', 'false', 1, 'Purchasing - Prices', '8.0', 0);

--changeSet 0utl_config_parm:805 stripComments:false
-- Tools
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'TOOL_CHECKOUT_USER_REQUIRES_AUTH', 'MXWEB', 'ToolCheckout page: Indicates whether Checkout User field of the Tool check in/out page requires authentication.', 'GLOBAL', 'TRUE/FALSE', 'false', 1, 'Supply - Tools', '6.8.6', 0);

--changeSet 0utl_config_parm:806 stripComments:false
-- HR - Shift Setup
INSERT INTO utl_config_parm ( parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, default_value, allow_value_desc, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'MAXIMUM_CAPACITY_SUMMARY_DAY_COUNT', 'MXWEB', '10', 0, 'Indicates the maximum number of days that are allowed in the Capacity Summary tab page.', 'GLOBAL', '10', 'Number', 1, 'HR - Shifts And Schedules', '6.8.6', 0 );

--changeSet 0utl_config_parm:807 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( '', 'sShiftScheduleSearchLocation', 'SESSION', 'Last shift schedule search value used for the Location dropdown list.', 'USER', 'String', '', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:808 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( '', 'sShiftScheduleSearchShift', 'SESSION', 'Last shift schedule search value used for the Shift dropdown list.', 'USER', 'String', '', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:809 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( '', 'sCapacitySummaryLocation', 'SESSION', 'Last capacity summary search value used for the Station dropdown list.', 'USER', 'String', '', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:810 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'FALSE', 'sCapacitySummaryWarnings', 'SESSION', 'Last capacity summary search value used for the warnings checkbox.', 'USER', 'TRUE/FALSE', 'FALSE', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:811 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( '3', 'sCapacitySummaryDayCount', 'SESSION', 'Last capacity summary search value used for the day count.', 'USER', 'NUMBER', '3', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:812 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( '5', 'sStationCapacityUnassignedDayCount', 'SESSION', 'Day count for the unassigned tab on station capacity details.', 'USER', 'NUMBER', '5', 1, 'HR - Shifts And Schedules', '0612',0);

--changeSet 0utl_config_parm:813 stripComments:false
-- Entries for oil consumption
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('HOC_BOOL_DURATION_DAYS', 'LOGIC', '30', 0, 'Days between oil consumption escalations required to trigger ''Repeat'' flag on high oil consumption list.', 'GLOBAL', '30', 'Number', 1, 'Assembly - Oil Monitoring', '7.0', 0, 0);

--changeSet 0utl_config_parm:814 stripComments:false
/*************** FRC Integration **************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ( 'MX_FRC_USER_ID', 'MX_FRC_INTEGRATION' , 10, 0, 'The user_id of the designated MxFrc system level user', 'GLOBAL' ,'Number', '',  1, 'FRC Integration', '5.1', 0);

--changeSet 0utl_config_parm:815 stripComments:false
/************** External Maintenance Adapter **************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SIGNOFF_HR_CD', 'EXTERNAL_MAINTENANCE_ADAPTER','SYSTEM', 0, 'Default HR_CD used for processing messages where the HR_CD is not specified.', 'GLOBAL', 'SYSTEM', 'Any valid value from ORG_HR.HR_CD', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:816 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_CALC_USAGE_SNAPSHOT', 'EXTERNAL_MAINTENANCE_ADAPTER','true', 0, 'Set Up Auto Calculate function for task and fault.', 'GLOBAL', 'false', 'true/false', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:817 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_CREATE_PART', 'EXTERNAL_MAINTENANCE_ADAPTER','true', 0, 'Set Up Auto Create Consumable Part for the Install Part.', 'GLOBAL', 'false', 'true/false', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:818 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SHOW_PROCESS_TIME', 'EXTERNAL_MAINTENANCE_ADAPTER','false', 0, 'Show the process time in the Log file.', 'GLOBAL', 'false', 'true/false', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:819 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PROCESS_VALIDATION', 'EXTERNAL_MAINTENANCE_ADAPTER','true', 0, 'If true, the validation for reference in ref table will be turned on.', 'GLOBAL', 'false', 'true/false', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:820 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PROCESS_VALIDATION_XML', 'EXTERNAL_MAINTENANCE_ADAPTER','false', 0, 'If true, the schema validation for the XML file will be turned on .', 'GLOBAL', 'false', 'true/false', 0, 'Customer Software','5.1.4', 0);

--changeSet 0utl_config_parm:821 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_CONSUM_FINANCIAL_CLASS', 'EXTERNAL_MAINTENANCE_ADAPTER','', 0, 'Default Financial Class used for processing messages where the new consumable part is created.', 'GLOBAL', 'DEFAULTCD', 'Valid value for the consumable part from  REF_FINANCIAL_CLASS', 0, 'Customer Software','0612', 0);

--changeSet 0utl_config_parm:822 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_CONSUM_ASSET_ACCOUNT', 'EXTERNAL_MAINTENANCE_ADAPTER','', 0, 'Default Asset Account used for processing messages where the new consumable part is created.', 'GLOBAL', 'DEFAULTCD', 'Valid value for the consumable part from  FNC_ACCOUNT', 0, 'Customer Software','0612', 0);

--changeSet 0utl_config_parm:823 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_EMA_REMOVE_REASON', 'EXTERNAL_MAINTENANCE_ADAPTER','IMSCHD', 0, 'Default removal reason when automatically creating rmvl_part segments.', 'GLOBAL', 'IMSCHD', 'Valid code from ref_remove_reason', 1, 'Customer Software','6.6.1', 0);

--changeSet 0utl_config_parm:824 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('AUTO_CREATE_RMVL_SEGMENT', 'EXTERNAL_MAINTENANCE_ADAPTER','FALSE', 0, 'If true, rmvl_part segments will be created to remove the currently installed inventory when not present.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'Customer Software','6.6.1', 0);

--changeSet 0utl_config_parm:825 stripComments:false
/************** Planning Viewer Applet **************/
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('5', 'DAY_COUNT', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'Number', '15', 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:826 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ONLY_SHOW_UNPACKAGED_TASKS', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'TRUE/FALSE', 'FALSE', 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:827 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ONLY_SHOW_SCHEDULING_CONFLICTS', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'TRUE/FALSE', 'FALSE', 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:828 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ONLY_SHOW_UNSCHEDULED_CHECKS', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'TRUE/FALSE', 'FALSE', 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:829 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHOW_SOFT_DEADLINES', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'TRUE/FALSE', 'FALSE', 0, 'Planning Viewer', '6.8', 0);

--changeSet 0utl_config_parm:830 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (NULL, 'START_DATE', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'DATE', NULL, 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:831 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (NULL, 'END_DATE', 'PLANNING_VIEWER','Planning viewer parameters.','USER', 'DATE', NULL, 0, 'Planning Viewer', '0612', 0);

--changeSet 0utl_config_parm:832 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('30', 'MAX_DURATION', 'PLANNING_VIEWER','The maximum duration in days that the planning viewer can be set to.','GLOBAL', 'NUMBER', '30', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:833 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'ASSEMBLY_CODE', 'PLANNING_VIEWER','Planning viewer parameters','USER', 'STRING', '', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:834 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('5', 'START_FLIGHT_BUFFER', 'PLANNING_VIEWER','Planning viewer parameters','USER', 'NUMBER', '5', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:835 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('5', 'END_FLIGHT_BUFFER', 'PLANNING_VIEWER','Planning viewer parameters','USER', 'NUMBER', '5', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:836 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('60', 'MAX_START_FLIGHT_BUFFER', 'PLANNING_VIEWER','The maximum buffer in minutes between a flight and a work package.','GLOBAL', 'NUMBER', '60', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:837 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('60', 'MAX_END_FLIGHT_BUFFER', 'PLANNING_VIEWER','The maximum buffer in minutes between a flight and a work package.','GLOBAL', 'NUMBER', '60', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:838 stripComments:false
/************** Production Planning and Control Applet **************/
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_MAX_AVG_NR_TASK_DURATION', 'LOGIC', 3, 0, 'Default value in hours for maximum average non routine task duration.', 'GLOBAL', 3, 'NUMBER', 1, 'Maint - Production Planning and Control', '7.5', 0);

--changeSet 0utl_config_parm:839 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_PPC_PLAN_HORIZON', 'PRODUCTION_PLANNING_CONTROL', 30, 0, 'Default horizon value', 'GLOBAL', 30, 'NUMBER', 1, 'Maint - Production Planning and Control', '7.5', 0);

--changeSet 0utl_config_parm:840 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PPC_PUBLISH_TASK_CREWHR_RANGE', 'PRODUCTION_PLANNING_CONTROL', 72, 0, 'When production plan is published for a work package, system will assign crews and/or human resources only to tasks scheduled in the next set period.', 'GLOBAL', 72, 'NUMBER', 1, 'Production Planning Control', '7.5', 0);

--changeSet 0utl_config_parm:841 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PPC_PLAN_HORIZON_LIMIT', 'PRODUCTION_PLANNING_CONTROL', 90, 0, 'The horizon value limit', 'GLOBAL', 90, 'NUMBER', 1, 'Production Planning and Control', '7.5', 0);

--changeSet 0utl_config_parm:842 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC,  MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
VALUES ('PPC_CAPACITY_BUFFER', 'PRODUCTION_PLANNING_CONTROL', 30, 0, 'The number of days as a buffer for the capacity loading graph.', 'GLOBAL', 30, 'NUMBER', 1, 'Production Planning and Control', '8.0', 0, 0);

--changeSet 0utl_config_parm:843 stripComments:false
/*************** ESIGNER **************/
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('ENABLE_JC_ESIG', 'LOGIC', 'false', 0, 'Parameter that allows for job card signature.', 'GLOBAL', 'false', 'TRUE/FALSE', 1, 'Esigner', '0706', 0, 0);

--changeSet 0utl_config_parm:844 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('ENABLE_MR_ESIG', 'LOGIC', 'false', 0, 'Parameter that allows for maintenance release signature.', 'GLOBAL', 'false', 'TRUE/FALSE', 1, 'Esigner', '0706', 0, 0);

--changeSet 0utl_config_parm:845 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('DEFAULT_ESIG_MODE', 'LOGIC', 'SERVER', 0, 'Defines where the private key is kept.', 'GLOBAL', 'CLIENT', 'SERVER/CLIENT', 1, 'Esigner', '0706', 0, 0);

--changeSet 0utl_config_parm:846 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_KEY_LENGTH', 1024, 'LOGIC', 0, 'The default PKI key size for ESIGNATURE Certificates.','GLOBAL', '512/1024', '1024', 1, 'Core Logic', '0706', 0);

--changeSet 0utl_config_parm:847 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DEFAULT_CERT_DAYS', 720, 'LOGIC', 0, 'The number of days a PKI ESIGNATURE certificate is valid.','GLOBAL', 'Number', 720, 1, 'Core Logic', '0706', 0);

--changeSet 0utl_config_parm:848 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PKI_FILE_PATH', 'Z:/', 'LOGIC', 0, 'The file path to private key files ESIGNATURE configuration.','GLOBAL', 'Z:/', 'String', 1, 'Core Logic', '0706', 0);

--changeSet 0utl_config_parm:849 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ENFORCE_LAST_LABOUR_REQ_ESIG', 'LOGIC', 'FALSE', 0, 'Controls enforce the E-signature requirement when a user is finishing the last labor requirement in a task.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'Esigner', '8.2', 0, 0);

--changeSet 0utl_config_parm:850 stripComments:false
/*************** MOVE INVENTORY *************/
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_ARCHIVED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is archived', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:851 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_SCRAPPED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is scrapped', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:852 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_CONDITION_IS_DIFFERENT', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory condition is different', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:853 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_REMOTE', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is remote', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:854 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_NOT_ISSUED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is not issued', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:855 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_ISSUED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is issued', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:856 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_INSTALLED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is installed', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:857 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_DOES_NOT_EXIST', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory does not exist', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:858 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_BATCH_INSUFFICIENT_QUANTITY', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the bin quantity is insufficient ', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:859 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_BATCH_DOES_NOT_EXIST', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the batch does not exist', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:860 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INVENTORY_NOT_SERVICEABLE', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is not serviceable', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0709', 0);

--changeSet 0utl_config_parm:861 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_REQUIRES_CONTROLLED_RESERVATION', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the part of the inventory requires a controlled reservation and the inventory specified does not match the reserved inventory.', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '0806', 0);

--changeSet 0utl_config_parm:862 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_MIN_USAGE_FOR_RELEASE', 'SECURED_RESOURCE', 'FALSE', 0, 'Permission to allow user to accept a warning when the inventory has tasks due within the minimum usage for release', 'USER', 'FALSE', 'TRUE/FALSE', 1, 'Supply - Inventory', '7.0', 0);

--changeSet 0utl_config_parm:863 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'PREVENT_SYNCH_ON_EXISTING_INV_RECEIVED_AS_INSPREQ', 'LOGIC','When set to true, existing inventory items received as inspection required will be marked Prevent Synchronization.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1,'BSync - Task Initialization', '7.1-SP1',0);

--changeSet 0utl_config_parm:864 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_INVENTORY_IS_NOT_INSTALLED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory to be issued is different than the one installed on the task.', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Inventory', '8.2', 0);

--changeSet 0utl_config_parm:865 stripComments:false
/*************** JIC_PRINTING_CONFIG *************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('JIC_SOURCE', 'JIC_PRINTING_CONFIG', 'MAINTENIX',0, 'Indicates if maintenix or customer jics should be used', 'GLOBAL', 'MAINTENIX', 'MAINTENIX/CUSTOMER', 1, 'JIC Printing', '6.4.1',0,0);

--changeSet 0utl_config_parm:866 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('JIC_BASE_URL', 'JIC_PRINTING_CONFIG', null,0, 'Prefix of jic url if JIC_SOURCE == CUSTOMER', 'GLOBAL', null, 'Valid url prefix format http://...', 0, 'JIC Printing', '6.4.1',0,0);

--changeSet 0utl_config_parm:867 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('JIC_KEY', 'JIC_PRINTING_CONFIG', 'TASK_BARCODE',0, 'Jic identifier used in batch print file names', 'GLOBAL', null, 'TASK_BARCODE/TASK_EXTERNAL_KEY', 1, 'JIC Printing', '6.4.1',0,0);

--changeSet 0utl_config_parm:868 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('USE_FOLDER', 'JIC_PRINTING_CONFIG', 'true',0, 'Use folder for batch printing?', 'GLOBAL', 'true', 'true | false', 0, 'JIC Printing', '6.4.1',0,0);

--changeSet 0utl_config_parm:869 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('USE_TAIL_NO_FOR_AIRCRAFT_FOLDER', 'JIC_PRINTING_CONFIG', 'true',0, 'Use aircraft tail number as folder name for batch printing?', 'GLOBAL', 'true', 'true | false', 0, 'JIC Printing', '6.4.1',0,0);

--changeSet 0utl_config_parm:870 stripComments:false
/*************** PROXY_CONFIG *************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('PROXY_HOST', 'PROXY_CONFIG', null,0, 'Proxy Server Host Address - PROXY_CONFIG parameters influence all http requests that use the com.mxi.mx.core.clients.HttpProxyClient (as of 6.4.1 only JICs and IETMs)', 'GLOBAL', null, 'Valid host address', 0, 'Proxy Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:871 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('PROXY_PORT', 'PROXY_CONFIG', null,0, 'Proxy Server Host Port - PROXY_CONFIG parameters influence all http requests that use the com.mxi.mx.core.clients.HttpProxyClient (as of 6.4.1 only JICs and IETMs)', 'GLOBAL', null, 'Valid host port', 0, 'Proxy Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:872 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('PROXY_USER', 'PROXY_CONFIG', null,0, 'Proxy Server User Name - PROXY_CONFIG parameters influence all http requests that use the com.mxi.mx.core.clients.HttpProxyClient (as of 6.4.1 only JICs and IETMs)', 'GLOBAL', null, 'Valid user name', 0, 'Proxy Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:873 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('PROXY_PASSWORD', 'PROXY_CONFIG', null,0, 'Proxy Server User Password - PROXY_CONFIG parameters influence all http requests that use the com.mxi.mx.core.clients.HttpProxyClient (as of 6.4.1 only JICs and IETMs)', 'GLOBAL', null, 'Valid user password', 0, 'Proxy Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:874 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('PROXY_REALM', 'PROXY_CONFIG', null,0, 'Proxy Server Realm - PROXY_CONFIG parameters influence all http requests that use the com.mxi.mx.core.clients.HttpProxyClient (as of 6.4.1 only JICs and IETMs)', 'GLOBAL', null, 'Valid realm', 0, 'Proxy Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:875 stripComments:false
/*************** PDF_CONFIG *************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE,ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES('MAX_MERGE_SIZE', 'PDF_CONFIG', 41943040,0, 'Merging large PDF documents may cause OutOfMemoryError(s), this is the maximum total file size (in bytes) that should be attempted using the AbstractTaskCard.mergePDFDocuments operation.', 'GLOBAL', 41943040, 'number of bytes', 0, 'PDF Configuration', '6.4.1',0,0);

--changeSet 0utl_config_parm:876 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_KIT_LINE_IS_COMPLETE', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the kit line is already complete', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:877 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_PICKED_QT_MORE_THAN_REQUIRED', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the picked part quantity is more than required quantity', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:878 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INVENTORY_IS_IN_A_KIT', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow user to accept a warning when the inventory is already in a kit', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:879 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INSUFFICIENT_SHELF_LIFE', 'SECURED_RESOURCE', 'false', 0, 'KITS - Permission to allow user to accept a warning when the inventory has less shelf life remaining', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:880 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_NOT_LOCALLY_OWNED', 'SECURED_RESOURCE', 'false', 0, 'KITS - Permission to allow user to accept a warning when the inventory is not locally owned', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:881 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INVENTORY_RESERVED_FOR_PART_REQUEST', 'SECURED_RESOURCE', 'false', 0, 'KITS - Permission to allow user to accept a warning when the inventory is reserved for part request', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:882 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INVALID_COMBINATION_OEM_PART_NO_AND_SERIAL_NO', 'SECURED_RESOURCE', 'false', 0, 'Permission to allow invalid combination of part no oem and serial no.', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:883 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_INVENTORY_KIT_IS_INCOMPLETE', 'SECURED_RESOURCE', 'false', 0, 'KITS - Permission to allow user to accept a warning when the inventory kit is not complete', 'USER', 'false', 'TRUE/FALSE', 1, 'Supply - Kits', '7.0', 0);

--changeSet 0utl_config_parm:884 stripComments:false
insert into utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'MESSAGE_KIT_SUBINVENTORY_MISSING', 'SECURED_RESOURCE', 'false', 0, 'KITS - Permission to allow user to accept a warning when the KIT is issued with missing inventoriess', 'USER', 'false', 'TRUE/FALSE', 1, 'Kits', '7.0', 0);

--changeSet 0utl_config_parm:885 stripComments:false
-- Master Panel Cards - MPC
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('OPEN_PANEL_FAULT','LOGIC','ERROR', 0,'Severity of the validation for open tasks on the aircraft/component or any of its sub-components that are open.  Allowable Values OK, WARN, ERROR (case sensitive)', 'USER', 'WARN/ERROR/OK', 'ERROR', 1, 'Core Logic', '5.1', 0);

--changeSet 0utl_config_parm:886 stripComments:false
-- License
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('30', 'sExpiryDays', 'SESSION','Expiry days','USER', 'Number', '30', 0, 'License', '6.9', 0);

--changeSet 0utl_config_parm:887 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('NOTIFY_LIC_EXPIRY_DAYS', '30', 'LOGIC', 0, 'Licensing pending expiry days', 'GLOBAL', 'Number', '30', 1, 'License', '6.9', 0);

--changeSet 0utl_config_parm:888 stripComments:false
-- Long Range Planner
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('5', 'LRP_DEFAULT_AUTOMATION_DURATION', 'LONG_RANGE_PLANNER','Controls both the default automation duration, and the default number of years over which the automation logic should be run on a long range plan.','GLOBAL', 'Number', '5', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:889 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('4', 'LRP_VISIT_UPDATE_TO_ACTUALS_VALUE', 'LONG_RANGE_PLANNER','Default value for update to actuals value.','GLOBAL', 'Number', '4', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:890 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('NUMBER_WORK_LIST_START_DATE','LONG_RANGE_PLANNER', '1', 0, 'Default number of unassinged Work list start date field in WorkScope - Long Range Planning.', 'GLOBAL', 'NUMBER', '0' , 1, 'WorkScope - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:891 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('NUMBER_WORK_LIST_END_DATE','LONG_RANGE_PLANNER', '2', 0, 'Default number of unassinged Work list end date field in WorkScope - Long Range Planning.', 'GLOBAL', 'NUMBER', '0' , 1, 'WorkScope - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:892 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'LRP_AUTOMATION_SHIFT_VALUE', 'LONG_RANGE_PLANNER','Minimum default value in hours to shift event start date by.','GLOBAL', 'Number', '8', 1,'Maint - Long Range Planning', '8.0',0);

--changeSet 0utl_config_parm:893 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1', 'LRP_AUTOMATION_SNAP_TIME', 'LONG_RANGE_PLANNER','Default gap to leave in milliseconds between two events when snapping events.','GLOBAL', 'Number', '1', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:894 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_MIN_YIELD','LONG_RANGE_PLANNER', '0', 0, 'Default min yield to use when scheduling in Maint - Long Range Planning. Acceptable values are any values between 0 and 1.', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:895 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_MAX_YIELD','LONG_RANGE_PLANNER', '1', 0, 'Default max yield to use when scheduling in Maint - Long Range Planning. Acceptable values are any values between 0 and 1.', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:896 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_MIN_DURATION','LONG_RANGE_PLANNER', '0', 0, 'Default min duration (days) to use when scheduling in Maint - Long Range Planning.', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:897 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_MAX_DURATION','LONG_RANGE_PLANNER', '9999', 0, 'Default max duration (days) to use when scheduling in Maint - Long Range Planning.', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:898 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_DEFAULT_DURATION','LONG_RANGE_PLANNER', '1', 0, 'Default duration (days) to use when scheduling in Maint - Long Range Planning.', 'GLOBAL', 'NUMBER', '0' , 1, 'Maint - Long Range Planning', '0806', 0);

--changeSet 0utl_config_parm:899 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('5', 'LRP_DEFAULT_EXPORT_DURATION', 'LONG_RANGE_PLANNER','The default number of years over which the export plan logic should be run on a long range plan.','GLOBAL', 'Number', '5', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:900 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('20', 'LRP_MAX_ENTRIES_TO_LIST_ON_REMOVE', 'LONG_RANGE_PLANNER','The default number of  entires to be listed on remove','GLOBAL', 'Number', '20', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:901 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LRP_TIME_UNIT_THRESHOLD', 'LONG_RANGE_PLANNER', 'THIRTY_MINUTES','The date threshold for rounding date value in automation and forecasting. Supported values: ONE_DAY, ONE_HOUR, THIRTY_MINUTES, ONE_MINUTE, VOID','GLOBAL', 'String', 'THIRTY_MINUTES', 0,'Maint - Long Range Planning', '8.0',0);

--changeSet 0utl_config_parm:902 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP', 'LOGIC', 'FALSE','Defines availability of the user to assign/unassign LRP items to/from an INWORK work package','USER', 'TRUE/FALSE', 'FALSE', 0,'Maint - Long Range Planning', '8.0 SP1', 0);

--changeSet 0utl_config_parm:903 stripComments:false
-- User Selected  fault Threshold
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sFaultThresholdAssembly', 'SESSION','Fault Threshold Assembly parameter','USER', 'String', null, 0, 'Fault Threshold', '0806', 0);

--changeSet 0utl_config_parm:904 stripComments:false
-- User Selected  fdeferral reference
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sDeferralReferenceAssembly', 'SESSION','Deferral Reference Assembly parameter','USER', 'String', null, 0, 'Deferral Reference', '0806', 0);

--changeSet 0utl_config_parm:905 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sDeferralReferenceOperator', 'SESSION','Deferral Reference Operator parameter','USER', 'String', null, 0, 'Deferral Reference', '7.5', 0);

--changeSet 0utl_config_parm:906 stripComments:false
-- User Selected  Show Type In Exchange Returna Todolist
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sExchangeReturnsShowFilter', 'SESSION','Show Type In Exchage Returns Tab','USER', 'String', null, 0, 'Exchange Returns', '0806', 0);

--changeSet 0utl_config_parm:907 stripComments:false
-- Permissions control
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'HYPERLINK_ENABLED', 'PERMISSION','Enable/disable entity hyperlink hiding. This feature should not be enabled in production.','USER', 'TRUE/FALSE', 'FALSE', 1,'Permissions', '8.1-SP1',0);

--changeSet 0utl_config_parm:908 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'BUTTON_ENABLED', 'PERMISSION','Enable/disable actions for entities. This feature should not be enabled in production.','USER', 'TRUE/FALSE', 'FALSE', 1,'Permissions', '8.1-SP1',0);

--changeSet 0utl_config_parm:909 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ROWFILTERING_ENABLED', 'PERMISSION','Enable/disable entity row filtering. This feature should not be enabled in production.','USER', 'TRUE/FALSE', 'FALSE', 1,'Permissions', '8.1-SP1',0);

--changeSet 0utl_config_parm:910 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'PAGE_ENABLED', 'PERMISSION','Enable/disable page security. This feature should not be enabled in production.','USER', 'TRUE/FALSE', 'FALSE', 1,'Permissions', '8.1-SP1',0);

--changeSet OPER-12007:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'PAGE_LEVEL_SECURITY_ENABLED', 'PERMISSION', 'Enable/disable the ability to configure page-level security through the Role Permissions editior. NOTE: This features is a TECH DEMONSTRATOR only. Not ready for production use.', 'USER', 'TRUE/FALSE', 'FALSE', 1, 'Permissions', '8.2-SP3', 0);

--changeSet 0utl_config_parm:911 stripComments:false
-- User Initialize Inventory,Task Warranty
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchType', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:912 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchSubType', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:913 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchMethod', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:914 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchAssembly', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:915 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchStatus', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:916 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (0, 'sShowExpiredOnly', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:917 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchVendorCd', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:918 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchOEMPartNo', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:919 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchTaskCd', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:920 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE,PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sWarrantyContractSearchAssemblyName', 'SESSION','Warranty Contract search parameters.','USER', 'String', null, 0, 'Warranty Contract Search', '6.8', 0);

--changeSet 0utl_config_parm:921 stripComments:false
-- Timezone
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SERVER', 'TIME_ZONE_DISPLAY_MODE', 'LOGIC', 'Defines how dates and times that are associated with a location should be displayed.  When SERVER, all dates and times are displayed relative to the server time zone.  When LOCAL, dates associated with a location are transformed to be relative to the location time zone', 'USER', 'SERVER/LOCAL', 'SERVER', 1, 'Time zones', '6.8', 0);

--changeSet 0utl_config_parm:922 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('30', 'TIME_ZONE_DISPLAY_WIDTH', 'MXCOMMONWEB', 'Defines the width of timezone input field in pixels', 'GLOBAL', 'Any Integer', '30', 1, 'Time zones', '6.8', 0);

--changeSet 0utl_config_parm:923 stripComments:false
-- Issue Order
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ENFORCE_WARRANTY_REP_ORDER_LINES', 'false', 'SECURED_RESOURCE', 0, 'If true, all purchase/repair/exchange order lines for repairable parts must have an associated warranty (or exemption) before the order can be issued.' , 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Purchasing - Purchase Orders', '7.5', 0 );

--changeSet 0utl_config_parm:924 stripComments:false
-- Flight Ops config. parms.
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_AUTO_COMPLETE_PREVIOUS_FLIGHT', 'TRUE', 'LOGIC', 0, 'Determines whether to auto-complete previous active flights on an aircraft', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Core Logic', '6.8.7', 0);

--changeSet 0utl_config_parm:925 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FAULT_DEFERRAL_AUTH_MANDATORY', 'FALSE', 'LOGIC', 0, 'Whether the Deferral Authorization field in Defer Faults is mandatory', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Faults', '6.8.5', 0);

--changeSet 0utl_config_parm:926 stripComments:false
INSERT INTO utl_config_parm ( PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'FLIGHT_LOGBOOK_REF_MANDATORY', 'FALSE', 'LOGIC', 0, 'Whether flight Log References are mandatory.' , 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Ops - Flights', '6.8.5', 0 );

--changeSet 0utl_config_parm:927 stripComments:false
INSERT INTO utl_config_parm ( PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'FAULT_LOGBOOK_REF_MANDATORY', 'FALSE', 'LOGIC', 0, 'Whether Logbook Reference is mandatory for faults.' , 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Faults', '6.8.5', 0 );

--changeSet 0utl_config_parm:928 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FLIGHT_COMPLETE_CALCULATE_DELTAS', 'TRUE', 'LOGIC', 0, 'Whether AFH usage delta should be recalculated after changing Up and Down dates on the Complete Flight page.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Ops - Flights', '6.8.5', 0);

--changeSet 0utl_config_parm:929 stripComments:false
-- End of Flight Ops config. parms.
-- Configurable Part Replacement Warnings
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HOLE_TOBE_RMVD_EXISTS','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to remove a tracked component, but Maintenix records indicate there is already a hole in the config slot position.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:930 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HOLE_TOBE_RMVD_NOTFOUND','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to remove a tracked component, but Maintenix records indicate there is already a hole in the config slot position and the entered part number / serial number does not exist.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:931 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_RMVD_NOTFOUND','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to remove a tracked component, but Maintenix records indicate that the entered part number / serial number does not exist.', 'USER','WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:932 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_RMVD_NOTMATCH','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to remove a tracked component, but Maintenix records indicate that a different part number / serial number is currently installed.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:933 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_RMVD_SER_LOOSE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to remove a serialized component, but Maintenix records indicate that the component is already loose.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:934 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_AUTO_RECEIVED_INSPECTED_ISSUED','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a component using the ?maintenance receipt? functionality, which bypasses the receive, inspect and issue actions.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:935 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_AUTO_RECEIVED_INSPECTED_ISSUED_BATCH','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a batch component using the maintenance receipt functionality, which bypasses the receive, inspect and issue actions.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '7.0', 0);

--changeSet 0utl_config_parm:936 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_ATTACHED','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a component, but Maintenix records indicate that the component is currently installed somewhere else (maybe on the same aircraft, maybe on a different aircraft).', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:937 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_COMP_NOT_PART_COMPATIBLE','LOGIC','ERROR', 0,'Severity of the validation for the user attempting to complete a task that is incompatible with an installed part.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '8.0', 0);

--changeSet 0utl_config_parm:938 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_BATCH_INCORRECT_QTY','LOGIC','ERROR', 0,'Severity of the validation for the user if attempting to install more batch pieces than previously scheduled.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '7.1-SP3', 0);

--changeSet 0utl_config_parm:939 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_BATCH_NOT_ISSUED','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a batch, but Maintenix records indicate that the batch has not been issued from supply.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:940 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_PART_NOT_APPLICABLE','LOGIC','ERROR', 0,'Severity of the validation that the alternate part is not applicable.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:941 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_PART_GROUP_NOT_APPLICABLE','LOGIC','ERROR', 0,'Severity of the validation that the part group is not applicable.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:942 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOTAPPROVED_FOR_GROUP','LOGIC','ERROR', 0,'Severity of the validation that the alternate part is not approved.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:943 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOTFOUND','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a component, but Maintenix records indicate that the part number / serial number does not exist.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:944 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOT_ISSUED','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a component, but Maintenix records indicate that the component has not been issued from supply.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:945 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOT_PART_COMPATIBLE','LOGIC','ERROR', 0,'Severity of the validation that the component is not compatible with another part on the aircraft.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:946 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOT_RFI','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install a component, but Maintenix records indicate that the component is serviceable.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:947 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('REPETITIVE_TASK_TO_MONITOR_FAULT','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete fault having repetitive task to monitor it.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '7.1-SP1', 0);

--changeSet 0utl_config_parm:948 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOT_TASK_COMPATIBLE','LOGIC','ERROR', 0,'Severity of the validation that the component is not compatible with open or complete tasks on the aircraft.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:949 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOTAPPROVED','LOGIC','ERROR', 0,'Severity of the validation that the part number is not approved.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:950 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('REPLACEMENT_MISMATCH_QTY','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to install and remove components during a task, but the installed quantity and the removed quantity does not match.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:951 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_PART_NOT_APPLICABLE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete a part transformation and the new part number is not an applicable alternate.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:952 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_PART_GROUP_NOT_APPLICABLE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete a part transformation and the new part number is not in an applicable part group.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:953 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_NOT_APPROVED_IN_GROUP','LOGIC','ERROR', 0,'Severity of the validation for the user user is attempting to complete a part transformation and the new part number is not an approved alternate part.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:954 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_NOT_PART_COMPATIBLE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete a part transformation and the new part number is not compatible with another part number on the aircraft', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:955 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_NOT_PART_INTERCHANGEABLE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete a part transformation and the new part number is not interchangeable with the current part number.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:956 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_NOT_TASK_COMPATIBLE','LOGIC','ERROR', 0,'Severity of the validation for the user is attempting to complete a part transformation and the new part number is not compatible with open / complete tasks on the aircraft.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:957 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_TRANSFORMED_NOT_APPROVED','LOGIC','ERROR', 0,'Severity of the validation for the user attempting to complete a part transformation and the new part number is not an approved part.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1,'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:958 stripComments:false
INSERT INTO utl_config_parm(PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TOBE_INST_NOT_PART_INTERCHANGEABLE','LOGIC','ERROR', 0,'Severity of the validation that the alternate part is not interchangeable with the currently installed part.', 'USER', 'WARNING/ERROR/INFO', 'ERROR', 1, 'Maint - Tasks', '6.9', 0);

--changeSet 0utl_config_parm:959 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('2', 'LRP_STALE_THRESHOLD', 'LONG_RANGE_PLANNER','Threshold in hours to consider published data as stale.','GLOBAL', 'Double', '2', 1,'Maint - Long Range Planning', '6.9',0);

--changeSet 0utl_config_parm:960 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ADHOC_REPEAT_TASK_FORECAST_QT', '1', 'LOGIC', 0,'This parameter dictates the number of forecasted task to be created when creating a repetitive task for a fault.', 'GLOBAL', 'NON-NEGATIVE INTEGER', '1', 1, 'Core Logic', '7.1-SP1', 0);

--changeSet 0utl_config_parm:961 stripComments:false
-- Advisory Search Criteria
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchType', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:962 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchName', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:963 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchOEMPartNo', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:964 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchOemSerialNo', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:965 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchVendorCd', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:966 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchLotNo', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:967 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'sAdvsrySearchOperator', 'SESSION', 'Advisory Search Parameter', 'USER', 'STRING', 'NONE', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:968 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sAdvsrySearchOnWingOnly', 'SESSION', null, 0, 'Advisory search parameters', 'USER', null, 'String', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:969 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('STARTS_WITH', 'sAdvsrySearchMethod', 'SESSION','Advisory search parameters','USER', 'String', 'STARTS_WITH', 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:970 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (100, 'sAdvsrySearchMaxResults', 'SESSION','Advisory search parameters','USER', 'Number', 100, 0, 'Advisory Search', '7.0', 0);

--changeSet 0utl_config_parm:971 stripComments:false
-- Deployed Operations
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchSourceLocation', 'SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:972 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchAircraft','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:973 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchAircraftName','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:974 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchFileName','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:975 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchExportId','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:976 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchStatus','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:977 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchImportedBefore','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:978 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchImportedAfter','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:979 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchMethod','SESSION', 'STARTS_WITH', 0, 'Deployed ops import search parameters', 'USER', 'STARTS_WITH', 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:980 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('sDpoImportSearchShipmentId','SESSION', null, 0, 'Deployed ops import search parameters', 'USER', null, 'String', 0, 'Deployed - Inventory Transfer Search', '6.9', 0);

--changeSet 0utl_config_parm:989 stripComments:false
-- Task Labour Hour Filtering
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'SHOW_SCHED_LABOUR_HOURS', 'false', 'SECURED_RESOURCE', 0, 'Permission to view scheduled labor information.' , 'USER', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Tasks', '8.2-SP3', 0 );

--changeSet 0utl_config_parm:990 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchSubAssembly', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:991 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchZone', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:992 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchLaborSkill', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:993 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchAwaitingInspection', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:994 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchAwaitingCertification', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:995 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchActiveAndInwork', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:996 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sWorkscopeSearchZoneName', 'SESSION','Workscope search parameters..','USER', 'STRING', '0', 0, 'SESSION', '7.1-SP1', 0);

--changeSet 0utl_config_parm:997 stripComments:false
-- Final Certification
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ENABLE_FINAL_TASK_CERT_CHECK_TASK', 'FALSE', 'LOGIC', 0, 'This configuration parameter enables/disables the final certification work flow for aircraft tasks.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Task-Final Certification', '7.1-SP2', 0 );

--changeSet 0utl_config_parm:998 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'ENABLE_FINAL_TASK_CERT_ASSEMBLY_TASK', 'FALSE', 'LOGIC', 0, 'This configuration parameter enables/disables the final certification work flow for component tasks.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Task-Final Certification', '7.1-SP2', 0 );

--changeSet 0utl_config_parm:999 stripComments:false
-- HTTP Compression parameters
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_URLS', '.*', 'HTTP', 'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs of the content to compress.', 'GLOBAL', 'Java Regex List', '.*', 1, 'HTTP COMPRESSION', '7.5', 0);

--changeSet 0utl_config_parm:1000 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_CONTENT_TYPES', 'text/html,text/xml,text/plain,text/javascript,application/javascript,application/x-javascript,text/css,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/msexcel,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/mspowerpoint',
'HTTP', 'A comma-separated list of compressible MIME types', 'GLOBAL', 'Content Type List', 'text/html,text/xml,text/plain,text/javascript,application/javascript,application/x-javascript,text/css,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/msexcel,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/mspowerpoint', 1, 'HTTP COMPRESSION', '7.5', 0);

--changeSet 0utl_config_parm:1001 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_DEBUG', 'false', 'HTTP', 'Enables debug logging for HTTP compression operations', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'HTTP COMPRESSION', '7.5', 0);

--changeSet 0utl_config_parm:1002 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_ENABLED', 'true', 'HTTP', 'Enables HTTP compression', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'HTTP COMPRESSION', '7.5', 0);

--changeSet 0utl_config_parm:1003 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_MIN_SIZE', '1024', 'HTTP', 'Size in bytes of the smallest response that will be compressed.Responses smaller than this size will not be compressed.', 'GLOBAL', 'Integer', '1024', 1, 'HTTP COMPRESSION', '7.5', 0);

--changeSet 0utl_config_parm:1004 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('HTTP_COMPRESSION_EXCLUDE_URLS', '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet,.*/lrp,.*/ppc,.*/StationMonitoringServlet,.*/LoadPlanningViewer,.*/ValidateTicket', 'HTTP', 'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs that need to be excluded from compression.', 'GLOBAL', 'Java Regex List', '.*/report.*,.*Pdf,.*/report/export,.*/ViewAttachment,.*/GenerateTallySheet,.*/lrp,.*/ppc,.*/StationMonitoringServlet,.*/LoadPlanningViewer,.*/ValidateTicket', 1, 'HTTP COMPRESSION', '8.0-SP1', 0);

--changeSet 0utl_config_parm:1005 stripComments:false
-- HTTP Caching Parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('.*\.css,.*\.gif,.*\.jpg,.*\.jpeg,.*\.png,.*\.html,.*\.js,.*/common/PleaseWaitPopup\.jsp,.*/common/resources/autosuggest_js\.jsp,.*/common/resources/CopyrightNotice\.jsp,.*/common/resources/formats_js\.jsp,.*/common/resources/calendar/ipopeng\.jsp', 'HTTP_CACHE_INCLUDE_URLS', 'HTTP', 'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs to cache', 'GLOBAL', 'Java Regex List', '.*\.css,.*\.gif,.*\.jpg,.*\.jpeg,.*\.png,.*\.html,.*\.js,.*/common/PleaseWaitPopup\.jsp,.*/common/resources/autosuggest_js\.jsp,.*/common/resources/CopyrightNotice\.jsp,.*/common/resources/formats_js\.jsp,.*/common/resources/calendar/ipopeng\.jsp', 1, 'HTTP CACHING', '7.5', 0);

--changeSet 0utl_config_parm:1006 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'HTTP_CACHE_EXCLUDE_URLS', 'HTTP', 'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs that should be excluded from caching', 'GLOBAL', 'Java Regex List', '', 1, 'HTTP CACHING', '7.5', 0);

--changeSet 0utl_config_parm:1007 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'HTTP_CACHE_DEBUG', 'HTTP', 'Enables debug logging for HTTP caching operations', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'HTTP CACHING', '7.5', 0);

--changeSet 0utl_config_parm:1008 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'HTTP_CACHE_ENABLED', 'HTTP', 'Enables HTTP caching', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'HTTP CACHING', '7.5', 0);

--changeSet 0utl_config_parm:1009 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('86400', 'HTTP_CACHE_EXPIRATION', 'HTTP', 'How many seconds content should be cached on a browser', 'GLOBAL', 'Integer', '86400', 1, 'HTTP CACHING', '7.5', 0);

--changeSet 0utl_config_parm:1010 stripComments:false
-- Integration Parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'INTEGRATION_ENABLE_PROCESS_LOGGING', 'INTEGRATION', 'Indicates whether the detailed process step logging will be recorded to the database (i.e. INT_PROCESS_STEP)', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Integration', '7.5', 0);

--changeSet 0utl_config_parm:1011 stripComments:false
-- Spec2000 Parameters
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('SPEC2000_ORDER_NUM_LIMIT', 'FALSE', 'LOGIC', 'The order number must not exceed 9 characters to comply with Spec2000 standards.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.3-SP1', 0);

--changeSet 0utl_config_parm:1012 stripComments:false
-- Maintenance Program details - REQs Filter options
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sMaintProgramReqTabFilterGroupCode', 'SESSION','Group Code. Used to store the filter option on Maintenance Program Details, Requirements tab.','USER',  'STRING', null, 0, 'Maint Program - Maint Program', '8.0', 0);

--changeSet 0utl_config_parm:1013 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sMaintProgramReqTabFilterConfigSlot', 'SESSION','Config Slot. Used to store the filter option on Maintenance Program Details, Requirements tab.','USER',  'STRING', null, 0, 'Maint Program - Maint Program', '8.0', 0);

--changeSet 0utl_config_parm:1014 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sMaintProgramReqTabFilterRevisedOnly', 'SESSION','Revized Only flag. Used to store the filter option on Maintenance Program Details, Requirements tab.','USER',  'TRUE/FALSE', 'FALSE', 0, 'Maint Program - Maint Program', '8.0', 0);

--changeSet 0utl_config_parm:1015 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sMaintProgramReqTabFilterApplicableOnly', 'SESSION','Applicable Only flag. Used to store the filter option on Maintenance Program Details, Requirements tab.','USER',  'TRUE/FALSE', 'FALSE', 0, 'Maint Program - Maint Program', '8.0', 0);

--changeSet 0utl_config_parm:1016 stripComments:false
-- Warranties & Claim
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('true', 'ACTION_SUBMIT_CLAIM_AUTHENTICATION', 'MXWEB','Authenticate user while submiting claim.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Warranty - Claims', '6.8', 0);

--changeSet 0utl_config_parm:1017 stripComments:false
-- Part
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('false', 'ACTION_ALLOW_OVERLAPPING_PART_PRICE_CONTRACTS', 'LOGIC','Indicates if we will allow or disallow the creation/editing of vendor part prices with a contract ref whose effective range overlapps with an existing contract price.','GLOBAL',  'TRUE/FALSE', 'FALSE', 0, 'Parts - Part Numbers', '8.0', 0);

--changeSet 0utl_config_parm:1018 stripComments:false
-- Line Planning Automation
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('LINE_PLANNING_AUTOMATION_MODE', 'ADVANCED',  'LOGIC', 0, 'Specifies the mode in which line planning automation will run. Advanced automation takes into account capacity, whereas basic automation does not.', 'GLOBAL', 'BASIC/ADVANCED', 'ADVANCED', 1, 'Maint - Planning', '7.2 SP1', 0, 0);

--changeSet 0utl_config_parm:1019 stripComments:false
-- Procurement
INSERT INTO utl_config_parm ( parm_name, parm_type, parm_value, encrypt_bool, parm_desc, config_type, default_value, allow_value_desc, mand_config_bool, category, modified_in, utl_id )
VALUES ( 'MAXIMUM_TAX_CHARGE_VENDOR_SELECT_OPTIONS', 'MXWEB', '20', 0, 'Indicates the maximum number of options to be displayed in the Tax, Charge, and Vendor dropdown lists. If exceeded, an AutoSuggest widget will be used instead.', 'GLOBAL', '20', 'Number', 1, 'MXWEB', '8.0', 0 );

--changeSet 0utl_config_parm:1020 stripComments:false
-- Default Work Package Scheduling
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('FALSE', 'SCHEDULE_WORK_PACK_EXTERNALLY_BY_DEFAULT', 'MXWEB','Indicates if a work package will always be scheduled externally.','USER', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Work Packages', '1209', 0, 0);

--changeSet 0utl_config_parm:1021 stripComments:false
-- Mobile Line Maintenance
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WF_MOBILE_LINE_MAINT', 'FALSE', 'LOGIC', 0, 'This parameter is used to enable user access to the Mobile Line Maintenance.', 'USER',  'TRUE/FALSE', 'FALSE' , 1, 'Workflow Access', '8.0-SP1', 0);

--changeSet 0utl_config_parm:1022 stripComments:false
-- Work Package Builder Configuration
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WF_WORK_PACKAGE_BUILDER', 'FALSE', 'LOGIC', 0, 'This parameter is used to enable user access to the Work Package Builder.', 'USER',  'TRUE/FALSE', 'FALSE' , 1, 'Maint - Work Package Builder', '8.0-SP1', 0);

--changeSet 0utl_config_parm:1023 stripComments:false
INSERT INTO utl_config_parm ( parm_name, parm_value, parm_type, encrypt_bool, parm_desc, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ( 'WPB_MAX_DUE_LIMIT', '14', 'MXWEB', 0, 'This parameter sets the maximum amount of days into the future a planning item can be due.', 'USER',  '14', '14' , 1, 'Maint - Work Package Builder', '8.0-SP1', 0);

--changeSet 0utl_config_parm:1024 stripComments:false
-- Report Engine Name Setting
/*************** Report Engine Name Setting *************/
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('DEFAULT_REPORT_ENGINE', 'REPORT_ENGINE', 'JASPER_SSO', 0, 'Default report engine name', 'GLOBAL', 'JASPER_SSO', 'BOE, JASPER_REST, JASPER_SSO', 0,  'Report Parameter',  '8.0-SP2', 0, 0);

--changeSet 0utl_config_parm:1025 stripComments:false
-- ARC
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_LOCATION', 'ARC', '', 0, 'The default location for new assets', 'GLOBAL', '', 'A location code', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet 0utl_config_parm:1026 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_CONDITION', 'ARC', 'INSPREQ', 0, 'The condition code for new assets', 'GLOBAL', 'INSPREQ', 'A condition code', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet 0utl_config_parm:1027 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_FORECAST_MODEL', 'ARC', '', 0, 'The default forecast model for new assets', 'GLOBAL', '', 'A forecast model name', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet 0utl_config_parm:1028 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_ISSUE_TO_ACCOUNT', 'ARC', '', 0, 'The default issue account for new assets',  'GLOBAL', '', 'A issue account code', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet 0utl_config_parm:1029 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_CREDIT_ACCOUNT', 'ARC', '', 0, 'The default credit account for new assets', 'GLOBAL', '', 'A credit account code', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet 0utl_config_parm:1030 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_DEFAULT_OWNER', 'ARC', '', 0, 'The default owner for new assets', 'GLOBAL', '', 'A owner code', 1, 'ARC - Configuration', '8.1', 0, 0);

--changeSet OPER-14723:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ARC_ALLOW_USAGE_UPDATE_FOR_INREP_ASSET', 'ARC', 'FALSE', 0, 'Indicator if ARC allows updating inventory usages for INREP aircraft', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'ARC - Configuration', '8.2', 0, 0);

--changeSet 0utl_config_parm:1031 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('CREATE_HISTORIC_TASK_MATCHING_INTERVAL', 'LOGIC', '6', 0, 'The matching interval in hours for comparing historic tasks.', 'GLOBAL', '6', 'Hours between 0-24', 1, 'Core Logic', '8.1-SP1', 0, 0);

--changeSet 0utl_config_parm:1032 stripComments:false
-- Data Volume Management
/*************** Report Engine Path Settings *************/
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLOB_RETENTION_PERIOD', 'LOGIC', 0, 'Indicates the minimum age of a BLOB, in months, so that the BLOB is copied to the monthly tables, and eventually copied to the BLOB storage medium. A value of 0 means that all BLOBs remain in the operational database permanently.','GLOBAL', 'Integer from 0 to 10', 0, 1, 'Data Volume Management - BLOB segregation', '8.1-SP1', 0);

--changeSet 0utl_config_parm:1033 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLOB_SEGREGATE_CHUNK_SIZE', 'LOGIC', 0, 'Indicates the maximum number of rows of BLOB records (satisfying the segregation criteria) that will be processed/copied in one Blob Segregation job run.','GLOBAL', 'Integer. 0 implies that the chunk size will be Integer.MAX_VALUE, which means all satisfying rows will be proccesed in the segregation job run.', 0, 1, 'Data Volume Management - BLOB segregation', '8.1-SP1', 0);

--changeSet 0utl_config_parm:1034 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLOB_PRIMARY_LOC', 'LOGIC', '', 'The root path to the primary BLOB storage medium,e.g.,M:/BLOB/','GLOBAL', 'The path must be accessible to Maintenix server and end with forward slash/', '', 1, 'Data Volume Management - BLOB segregation', '8.1-SP1', 0);

--changeSet 0utl_config_parm:1035 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('BLOB_SECONDARY_LOC', 'LOGIC', '', 'The root path to backup medium of segregated BLOBs,,e.g.,N:/BLOB/','GLOBAL', 'The path must be accessible to Maintenix server and end with forward slash/', '', 1, 'Data Volume Management - BLOB segregation', '8.1-SP1', 0);

--changeSet 0utl_config_parm:1036 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('DATASOURCE_INFO', 'LOGIC', '', 'The datasource information that Maintenix connects to','GLOBAL', 'The datasource information that Maintenix connects to (Oracle thin URL plus schema)', '', 1, 'Data Volume Management - BLOB segregation', '8.1-SP1', 0);

--changeSet 0utl_config_parm:1037 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('PPC_REAL_ROSTER_ENABLED', 'PRODUCTION_PLANNING_CONTROL', 'TRUE', 'Real Roster enabled flag','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Maint - Production Planning and Control', '8.1', 0);

--changeSet 0utl_config_parm:1038 stripComments:false
-- ProjectStatus (PPC)
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ProjectStatus', 'REPORT_PATH_MAPPING', '/organizations/Maintenix/Reports/Operator/Planning/ProjectStatus', 0, 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 'GLOBAL', '/organizations/Maintenix/Reports/Operator/Planning/ProjectStatus', 'Use path from the report properties in Jasper', 0, 'Report Parameter', '8.2', 0, 0);

--changeSet 0utl_config_parm:1039 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, REPL_APPROVED, UTL_ID)
VALUES ('ProjectStatus', 'REPORT_ENGINE', 'JASPER_SSO', 0, 'Report engine name for generating Project Status report', 'GLOBAL', 'JASPER_SSO', 'BOE, JASPER_SSO', 0,  'Report Parameter',  '8.2', 0, 0);

--changeSet 0utl_config_parm:1040 stripComments:false
-- Receive Shipment
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'FORCE_ENTER_ZERO_QUANTITY_ON_RECEIVE_SHIPMENT', 'LOGIC','If TRUE, the user must enter a valid quantity, including zero, for all shipment lines on the Receive Shipment page. If FALSE, Maintenix will auto-populate any empty shipment lines with zero after displaying a warning message.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1,'Supply - Shipments', '8.2-SP1',0);

--changeSet 0utl_config_parm:1041 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ALLOW_EDIT_RECEIVE_DATE_ON_RECEIVE_SHIPMENT', 'LOGIC','Controls if the received date on the receive shipment page can be modified by the user or not. If the parameter is TRUE, the user can select a different received date. If it is FALSE, the user cannot modify the received date and the system default date and time applies.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1,'Supply - Shipments', '8.2-SP2',0);

--changeSet 0utl_config_parm:1042 stripComments:false
-- Task Name Search
/*************** Task Search By Type Session Parameters *************/
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1043 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1044 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1045 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskStatus', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1046 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1047 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskAircraft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1048 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByTaskWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1049 stripComments:false
-- ID Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNumber', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1050 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByWPLineNo', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1051 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByVendorWPLineNo', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1052 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByWPName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1053 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1054 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1055 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1056 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1057 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1058 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1059 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByIDWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1060 stripComments:false
-- Aircraft Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1061 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1062 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1063 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1064 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1065 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1066 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1067 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1068 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1069 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1070 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1071 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1072 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1073 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByAcftWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1074 stripComments:false
-- Config Slot Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsAssembly', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1075 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1076 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1077 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1078 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1079 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1080 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1081 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1082 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1083 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1084 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1085 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1086 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1087 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1088 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByCsWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1089 stripComments:false
-- Serial Number Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialNum', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1090 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1091 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1092 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1093 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1094 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1095 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1096 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1097 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1098 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1099 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1100 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1101 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1102 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchBySerialWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1103 stripComments:false
-- Part Number Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartNum', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1104 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartHist', 'SESSION','Task Type search parameters','USER', 'BOOLEAN', '0', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1105 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartDueAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1106 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartDateRange', 'SESSION','Task Type search parameters','USER', 'Number', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1107 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartCmpltBefore', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1108 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartCmpltAfter', 'SESSION','Task Type search parameters','USER', 'Date', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1109 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartJICREQ', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1110 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartTaskName', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1111 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartTaskType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1112 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1113 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartTaskSubType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1114 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartPriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1115 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartRoutine', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1116 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByPartWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1117 stripComments:false
-- Do not execute Search
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNotExeRevDate', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1118 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNotExeAircraft', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1119 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNotExeOrig', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1120 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNotExePriority', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1121 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskTypeSearchByNotExeWorkType', 'SESSION','Task Type search parameters','USER', 'String', '', 0, 'Task Type Search', '8.2-SP2', 0);

--changeSet 0utl_config_parm:1122 stripComments:false
-- Task Weight balance
INSERT INTO utl_config_parm (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ('WEIGHT_BALANCE_WEIGHT_UNIT', 'LBS', 'MXWEB', 0, 'Unit of measurement for weight used by the Weight and Balance feature.', 'GLOBAL', '', 'LBS', 1, 'Maint - Tasks', '8.2-SP2', 0 );

--changeSet 0utl_config_parm:1123 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID )
VALUES ('WEIGHT_BALANCE_MOMENT_UNIT', 'IN-LBS', 'MXWEB', 0, 'Unit of measurement for moment used by the Weight and Balance feature.',  'GLOBAL', '', 'IN-LBS', 1, 'Maint - Tasks', '8.2-SP2', 0 );

--changeSet 0utl_config_parm:1124 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( null, 'sWorkScopeTabOptionalColumns', 'SESSION', 'Controls coulmn visibility on the Workscope Tab on Workpackage Details page', 'USER', 'STRING', null, 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:1125 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, ENCRYPT_BOOL, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sVisibleGroups', 'SESSION','Groups that are visible on the Fleet List page.','USER', 'STRING', null, 0, 0, 'SESSION', '8.1', 0);

--changeSet 0utl_config_parm:1126 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sUSSTGShowUSSTGLocations', 'SESSION', 'Whether to show items at USSTG locations on the Unserviceable Staging to-do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:1127 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sUSSTGShowOtherLocations', 'SESSION', 'Whether to show items at non-USSTG locations on the Unserviceable Staging to-do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:1128 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sUSSTGShowInventory', 'SESSION', 'Whether to show inventory items on the Unserviceable Staging to-do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet 0utl_config_parm:1129 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sUSSTGShowTools', 'SESSION', 'Whether to show tools on the Unserviceable Staging to-do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP3', 0);

--changeSet OPER-10634:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_ERROR_MESSAGE_LOGGING','SYSTEM', 'TRUE', 0,'Enable logging that records the following information in the UTL_ERROR_LOG table each time users hit error messages: the exception class name, error code, error message, servlet name, and time. Does not identify the user encountering the error.', 'GLOBAL', 'TRUE', 'TRUE/FALSE' , 1, 'SYSTEM', '8.2-SP3', 0);

--changeSet OPER-8830:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('', 'AIRCRAFT_GROUP', 'PLANNING_VIEWER','Planning viewer parameters','USER', 'STRING', '', 0, 'Planning Viewer', '8.0', 0);

--changeSet 0utl_config_parm:1132 stripComments:false
-- Session parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('14', 'sPADayCount', 'SESSION','Stores the last value entered in the search form searching planned attendance by number of days.', 'USER', 'Number', '14', 0, 'SESSION', '8.2-SP4', 0);

--changeSet OPER-10552:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'SHELF_LIFE_REMAINING_VALIDATED_FOR_INTERNAL_SHIPMENT', 'LOGIC','When false, you can receive a shipment of existing inventory from an internal location (not new inventory
received from a vendor) and the inventory will not be quarantined based on how much shelf life remains for the
item. When true, the percentage of remaining shelf life for an item is assessed and items can be quarantined
based on the value specified by the PERCENT_SHELF_LIFE_WARNING parameter.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Purchasing - Purchase Orders', '8.2-SP4', 0);


--changeSet OPER-15606:1135 stripComments:false
-- Session parameters
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('0', 'sFilterAircraftGroupsForMELPartReq', 'SESSION','Filter MEL Part requests by aircraft groups.','USER',  'Number', null, 0, 'SESSION', '8.2-SP4', 0);

--changeSet OPER-15636:1136 stripComments:false
-- Session parameters
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( null, 'sDFPRTOptionalColumns', 'SESSION', 'Deferred faults part requests todo list hidden columns', 'USER', 'STRING', null, 0, 'SESSION', '8.2-SP4', 0);

--changeSet OPER-15602:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDeferredPartRequestNeededByDateFromCount', 'SESSION', 0, 'Stores the last value entered in the "Needed by" options filter on the Deferred Fault Part Requests tab  - From date.','USER', 'Number', null, 0, 'SESSION', '8.2-SP5', 0);

--changeSet OPER-15602:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sDeferredPartRequestNeededByDateToCount', 'SESSION', 0, 'Stores the last value entered in the "Needed by" options filter on the Deferred Fault Part Requests tab - To date.','USER', 'Number', null, 0, 'SESSION', '8.2-SP5', 0);

--changeSet OPER-15638:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sShowAllPartRequestsForDeferredFaultPR', 'SESSION', 0, 'When this parameter is false, users see deferred fault part requests that are unassigned or assigned to themselves. But the value is overwritten by the true value stored in the UTL_USER_PARM table for individual users if they select the Show All Part Requests filter.','USER', 'TRUE/FALSE', 'FALSE', 0, 'SESSION', '8.2-SP4', 0);

--changeSet OPER-15580:1 stripComments:false
-- Session parameters
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( '*', 'sDFPRTPartRequestStatus', 'SESSION', 'Filter by deferred faults part requests status', 'USER', 'STRING', '*', 0, 'SESSION', '8.2-SP4', 0);

--changeSet OPER-15686:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('INCLUDE_IN_WORKING_CAPACITY', 'LOGIC', 'false', 'Flag indicating (by role) whether the role''s hours should be included for crew labour capacity calculations, and (globally) whether this feature should be active.  Set parm_value to TRUE to activate the feature.', 'GLOBAL', 'true', 'TRUE/FALSE', 'Capacity Planning', '8.2-SP5', 0);

--changeSet OPER-17635:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_READY_FOR_BUILD', 'LOGIC', 'FALSE', 'When enabled, inventory with a serviceable core or main body can be received and inspected as serviceable, even though there are missing, mandatory, tracked sub-components which must be added.  When inventory is marked Ready For Build, component work packages for the inventory can be closed and the inventory can be installed on an aircraft. (The aircraft cannot be released until the inventory condition is RFI).', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 'Core Logic', '8.2-SP5', 0);

--changeSet OPER-15334:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES(-1,'MAX_USAGE_PARM_DISPLAY','MXWEB','The maximum amount of usage parameters used to display usage values on the Inventory Historic Usage tab. If this number is exceeded, then usage TSN/Deltas will not be displayed. If left as -1 (default) then ALL values will be displayed','GLOBAL','INTEGER',-1,0,'Supply - Inventory','8.2-SP3u09',0);

--changeSet OPER-18057:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'DEFAULT_CREW_SHIFT_DAY_FOR_NEXT_HOURS', 'LOGIC','This parameter specifies the default search crew shift day for the next hours.','GLOBAL', 'Number', '8', 1, 'Maint - Tasks', '8.2-SP5', 0);

--changeSet OPER-14525:17 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('1800', 'SESSION_PARM_CACHE_INVALIDATION_INTERVAL', 'LOGIC','The interval (in seconds) between automatic flushing of user session parm data. A value of 0 disables this feature.','GLOBAL', 'Number', '1800', 1, 'System', '8.2-SP5', 0);

--changeSet OPER-18520:1 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sShowRepreq', 'SESSION', 'Stores the last value entered in the Repreq checkbox on the repair routing to do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP5', 0);

--changeSet OPER-18520:2 stripComments:false
INSERT INTO utl_config_parm(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES( 1, 'sShowRfb', 'SESSION', 'Stores the last value entered in the Rfb checkbox on the repair routing to do list.', 'USER', 'Number', '1', 0, 'SESSION', '8.2-SP5', 0);

--changeSet OPER-18397:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ERROR', 'ALLOW_BATCH_COMPLETE_BEFORE_WORK_PACKAGE_ACTUAL_START_DT', 'LOGIC', 'Determines the severity of the validation for the user when attempting to batch complete tasks with a complete date earlier than the work package actual start date.', 'GLOBAL', 'ERROR/WARN', 'ERROR', 1, 'Maint - Tasks', '8.2-SP3u11', 0);

--changeSet OPER-18603:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ALLOW_REQ_CORRECTIVE_ACTIONS', 'LOGIC', 'Enables the creation of sub-tasks based on requirement definitions for a fault.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Maint - Tasks', '8.2-SP5', 0);

--changeSet OPER-19052:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('MATCH_RECORDS_PG_MF_SN', 'LOGIC', 'TRUE', 'When true and inventory is received, if IFS Maintenix searches, but does not find a matching serial number and part number in the inventory records, it searches for a matching part group, serial number, and manufacturer. If a match is found, the received part is reinducted and its record is unarchived/or if record is active then reuse. When false, IFS Maintenix does not match parts that have the same serial numbers as inventory records, but whose part numbers were changed.', 'GLOBAL', 'TRUE', 'TRUE/FALSE', 'Core Logic', '8.2-SP5', 0);
--changeSet OPER-5329:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sHideComponentChecks', 'SESSION', 0, 'Determines if Component Work Packages are shown in the open Work package list on the Aircraft Details page.','USER', 'TRUE/FALSE', 'FALSE', 0, 'SESSION', '8.2-SP5', 0);

--changeSet OPER-21248:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('EXPECTED_TOOL_CHECKOUT_DURATION_HRS', '0', 'LOGIC', 0, 'When a tool is checked out, the hour value specified for this parameter determines the expected return date for the tool. The return date is calculated by adding the specified hour value to the system date and time.', 'GLOBAL', 'Number', 0, 0, 'Core Logic', '8.3', 0);

--changeSet OPER-20916:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('LOCAL', 'TIME_ZONE_DISPLAY_MODE_STATION_MONITORING', 'LOGIC', 'Defines how dates and times appear in the Station Monitoring application, as server time zone, or localized to the aicraft arrival airport.', 'USER', 'SERVER/LOCAL', 'LOCAL', 1, 'Time zones', '8.2-SP3', 0);

--changeSet OPER-18288:1 stripComments:false
INSERT INTO utl_config_parm (PARM_NAME, PARM_TYPE, PARM_VALUE, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ENABLE_MULTIPLE_SKILLS_SIGN_OFF_ON_SAME_STEP', 'LOGIC', 'FALSE', 'Allows user with multiple skills to sign off on more than one skill on the same step under multiple labor rows.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 0,'Maint - Tasks', '8.3', 0);

--changeSet OPER-21491:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_MARK_JOB_CARD_STEP_NOT_APPLICABLE', 'MXWEB','Allows users to mark job card steps as not applicable.','GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'MXWEB', '8.3',  0);

--changeSet OPER-21654:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'USE_BASELINE_USAGE_SORT_ORDER', 'MXWEB','When enabled, usage parameters displayed on certain pages are sorted by the order defined in their usage definition. When disabled, parameters are sorted alphanumerically.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'MXWEB', '8.3',  0);

--changeSet OPER-18604:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ENABLE_MANDATORY_MEASUREMENT_VALUES', 'LOGIC','When enabled, measurements values on tasks are mandatory.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'LOGIC', '8.3',  0);

--changeSet OPER-22908:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (1000, 'SELECT_WINDOW_POPUP_MAX_MANUFACTURERS', 'MXWEB','Indicates the maximum number of manufacturers that may be displayed in the Select Manufacturer popup; if this value is exceeded, only the number of manufacturers set with this parameter will be displayed, together with a warning message for a user to refine the search.','GLOBAL', 'Number', 1000, 1, 'MXWEB', '8.2-SP5u5',  0);

--changeSet OPER-22209:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ENABLE_MARK_MEASUREMENT_NA', 'LOGIC','When enabled, allows a user to explicitly mark a measurement as NA on the Work Capture page.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'LOGIC', '8.3',  0);

--changeSet OPER-23438:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('600', 'AUTH_PROVIDER_CACHE_INVALIDATION_INTERVAL', 'LOGIC','Defines how often to check Weblogic Authentication Provider Configuration','GLOBAL', 'Number', '600', 1, 'System', '8.3', 0);

--changeSet OPER-17925:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALLOW_REPL_SUPPRESSION', 'FALSE', 'LOGIC', 0,'Enables task suppression on duplicate subtasks of REPL tasks that have different Replacement Config Slots. Requires SUPPRESS_DUPLICATE_TASKS to also be enabled.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Core Logic', '8.3-SP1', 0);

--changeSet OPER-20979:1 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
values ('MESSAGE_SER_TRK_INVENTORY_IS_LOCKED', 'SECURED_RESOURCE', 'FALSE', 0, 'Permission to allow user to accept a warning when the serialized or tracked inventory is locked.', 'USER', 'FALSE', 'TRUE/FALSE', 1, 'Supply - Inventory', '8.3-SP1', 0);

--changeSet OPER-24790:1 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('JOB_STOP_REMAINING_HOURS', 'LOGIC', 'CALCULATED', 0, 'Controls the default value of the Remaining Hours field on the Work Capture page when a technician performs a job stop.  Configuration options are: calculate a default value and display it; calculate a value and hide it; or provide a null value as default.', 'GLOBAL', 'CALCULATED', 'CALCULATED/CALCULATED_HIDDEN/BLANK_REMAINING_HOURS', 1, 'Maint - Tasks', '8.3-SP1', 0, 0);

--changeSet OPER-25865:1 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('DEFER_ON_APPROVAL', 'LOGIC', 'TRUE', 0, 'Controls whether faults are automatically deferred after deferral reference requests are approved. If true, faults are deferred automatically after the deferral reference request is approved. If false, after the deferral reference request is approved, technicians must perform a Job Stop before faults are deferred.', 'GLOBAL', 'TRUE', 'TRUE/FALSE', 1, 'Core Logic', '8.3-SP1', 0, 0);

--changeSet OPER-18731:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchCreatedBefore', 'SESSION','Task search date range parameter for open tasks without scheduling.','USER', 'DATE', '', 0, 'Task Search', '8.3-SP1', 0);

--changeSet OPER-18731:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sTaskSearchCreatedAfter', 'SESSION','Task search date range parameter for open tasks without scheduling.','USER', 'DATE', '', 0, 'Task Search', '8.3-SP1', 0);

--changeSet OPER-18731-1:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('14', 'TASK_SEARCH_DEFAULT_DATE_RANGE', 'MXWEB','Task search date range used if user adds no date to scheduling fields. This will use the settings value to look X days in the future and X days in past for OPEN tasks and X days in past for HISTORICAL.','GLOBAL', 'Integer', '14', 0, 'Task Search', '8.3-SP1', 0);

--changeSet OOPER-26414:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'PRINT_OPEN_ISSUE_TRANSFERS', 'LOGIC','If enabled, issue transfer tickets will be printed when the part request status is in OPEN or AVAIL.','GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Supply - Inventory', '8.3-SP1', 0);

--changeSet OPER-25087:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('ALL', 'sStockDistReqStatus', 'SESSION','Stores the last selected value of the Status dropdown on the Stock Distribution Requests todo list.','USER',  'STRING', 'ALL', 0, 'SESSION', '8.3-SP1', 0);

--changeSet OPER-25087:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES (null, 'sStockDistReqWhereNeeded', 'SESSION','Stores the last selected value of the Where Needed location on the Stock Distribution Requests todo list.','USER',  'STRING', '', 0, 'SESSION', '8.3-SP1', 0);

--changeSet OPER-25087:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sStockDistReqOnlyHazmat', 'SESSION', 0, 'Stores the last selected value of the Only Hazmat requests checkbox on the Stock Distribution Requests todo list.','USER', 'TRUE/FALSE', 'FALSE', 0, 'SESSION', '8.3-SP1', 0);

--changeSet OPER-25087:4 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, encrypt_bool, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'sStockDistReqNonHazmat', 'SESSION', 0, 'Stores the last selected value of the Non Hazmat requests checkbox on the Stock Distribution Requests todo list.','USER', 'TRUE/FALSE', 'FALSE', 0, 'SESSION', '8.3-SP1', 0);

--changeSet OPER-27732:1 stripComments:false
insert into UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
values ('SHOW_PART_REQUIREMENT_REFERENCE', 'LOGIC', 'FALSE', 0, 'Controls whether the part requirement reference is visible in the UI. If true, the Reference field is visible when you add part requirements to tasks and faults and reference information is displayed on other pages (such as details and work capture pages).', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'Core Logic', '8.3-SP1', 0, 0);

--changeSet OPER-27733:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
VALUES ('PART_REQUIREMENT_REFERENCE_MANDATORY_INV_CLASSES', 'LOGIC', '', 0, 'Controls whether entering an IPC or other technical manual reference is mandatory for specific inventory classes when technicians add part requirements to tasks or faults in the user interface.', 'GLOBAL', '', 'BATCH/SER/KIT/TRK', 1, 'Tasks - Part Requirements', '8.3-SP1', 0, 0);

--changeSet OPER-27734:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
VALUES ('PART_REQUIREMENT_REFERENCE_MANDATORY_ACTIONS', 'LOGIC', '', 0, 'Controls whether entering an IPC or other technical manual reference is mandatory for specific Actions when technicians add part requirements to tasks or faults in the user interface.', 'GLOBAL', '', 'Any REF_REQ_ACTION', 1, 'Tasks - Part Requirements', '8.3-SP1', 0, 0);

--changeSet OPER-24801:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8.0', 'LABOR_ROW_ELAPSED_TIME_THRESHOLD', 'MXWEB', 'The default value used to indicate on the Task Supervision tab when the time between the start and current time on a labor row has elapsed. This is set as a configurable threshold in hour decimals.', 'GLOBAL',  'Number','8.0', 1, 'MXWEB', '8.3-SP1', 0);

--changeSet OPER-28790:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'BULK_LOAD_BATCH_SIZE', 'LOGIC', 'The batch size of a single write operation when inserting data loaded into Maintenix through CSVs to the database.', 'GLOBAL',  'Number','100', 1, 'Core Logic', '8.3-SP2', 0);

--changeSet OPER-23069:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_CONTENT_TYPE_OPTIONS_HEADER', 'HTTP', 'Controls if the X-Content-Type-Options header is added to each HTTP response. By default, the value of the header is set to "nosniff", which will cause browsers to enforce strict content type checks.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-23069:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_XSS_PROTECTION_HEADER', 'HTTP', 'Controls if the X-XSS-Protection header is added to each HTTP response. By default, the value of the header is set to "1; mode=block", which will cause browsers to block any pages where the browser detects a potential XSS attack.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-29772:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_FRAME_OPTIONS_HEADER', 'HTTP', 'Controls if the X-Frame-Options header is added to each HTTP response. By default, the value of the header is set to "sameorigin", which will protect Maintenix from being placed within an iframe element loaded from another site, while still allowing embedded iframes to display content.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-30102:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'ENABLE_AXON_FRAMEWORK_FAILSAFE', 'LOGIC', 'This parameter disables event publication for axon. This is to be used in extreme circumstances only.', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'Axon Framework', '8.3-SP2', 0);

--changeSet OPER-29660:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('100', 'AXON_QUERY_UPDATE_MESSAGE_INTERVAL', 'LOGIC', 'This parameter controls the interval (in milliseconds) for checking new Axon Subscription Update Messages.', 'GLOBAL', 'Number', '100', 1, 'Axon Framework', '8.3-SP2', 0);

--changeSet OPER-29660:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('600000', 'AXON_QUERY_UPDATE_SUBSCRIPTION_INACTIVE_DURATION', 'LOGIC', 'This parameter controls the duration (in milliseconds) for a query update subscription to be considered inactive.', 'GLOBAL', 'Number', '600000', 1, 'Axon Framework', '8.3-SP2', 0);

--changeSet OPER-27508:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_CONTENT_SECURITY_POLICY_HEADER', 'HTTP', 'Controls if the Content-Security-Policy header is added to each HTTP response. By default, the value of the header is set to "default-src ''self''; style-src ''self'' ''unsafe-inline'' ''unsafe-eval''; script-src ''self'' ''unsafe-inline'' ''unsafe-eval'';", which will allow only css, images, scripts, and other resources to be displayed only if they are present on the same host as Maintenix.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-25604:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'CONTENT_SECURITY_POLICY_MIXED_CONTENT_DIRECTIVE', 'HTTP', 'Declares which mixed-content directive to append to the Content-Security-Policy response header when HTTPS is used. A value of UPGRADE will tell the browser to transparently change HTTP resource URLs to HTTPS, while a value of BLOCK will tell the browser to prevent any resources from being fetched over HTTP.', 'GLOBAL', 'UPGRADE/BLOCK', 'UPGRADE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-25604:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'ENABLE_STRICT_TRANSPORT_SECURITY_HEADER', 'HTTP', 'Controls if the Strict-Transport-Security header is added to each HTTP response. This header tells the browser that it should only access Maintenix using HTTPS, instead of using HTTP.', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-25604:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('TRUE', 'STRICT_TRANSPORT_SECURITY_MAX_AGE', 'HTTP', 'The time, in seconds, that the browser should remember that a site is only to be accessed using HTTPS. This value is used to populate the max-age attribute of the Strict-Transport-Security header, the presence of which is controlled by the ENABLE_STRICT_TRANSPORT_SECURITY_HEADER parameter. The default value is 86400 seconds (24 hours).', 'GLOBAL', 'INTEGER', '86400', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-30125:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('8', 'WORK_ITEM_BSYNCVALIDATEACTUALS_THRESHOLD', 'WORK_MANAGER', 'Defines the number of baseline task dependencies that will trigger baseline sync validate task actuals work to be done in a separate work item.', 'GLOBAL', 'Non-negative integer.', '8', 1, 'BSync - Validate Task Actuals', '8.2-SP3', 0);

--changeSet OPER-30522:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('4000', 'JOB_STEP_DEFINITION_DESCRIPTION_LENGTH_LIMIT', 'LOGIC', 'The maximum character length of a job step description.', 'GLOBAL', 'Non-negative integer.', '4000', 1, 'Maint - Tasks', '8.3-SP2', 0);

--changeSet OPER-30521:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID, REPL_APPROVED)
VALUES ('FALSE', 'ALLOW_ADHOC_JOB_STEPS_ON_BASELINE_TASKS', 'LOGIC', 0, 'Controls visibility of the Add, Edit, and Remove Job Step buttons on the Task Details page for tasks that are based on a task definition. If this global parameter is true, then in addition to having access to the buttons on the details page of faults and ad hoc tasks, roles that have the ACTION_ADD_TASK_STEP, ACTION_EDIT_TASK_STEP, ACTION_REMOVE_TASK_STEP permissions can also access these buttons on the details page of tasks that are based on task definitions.', 'GLOBAL', 'FALSE', 'TRUE/FALSE', 1, 'Core Logic', '8.3-SP2', 0, 0);

--changeSet OPER-25623:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('480', 'ABSOLUTE_SESSION_TIMEOUT', 'HTTP', 'The maximum duration (in minutes) of a session, after which the session is terminated. A value less than one allows a session to remain active indefinitely.', 'GLOBAL', 'INTEGER', '480', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-25623:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('15', 'IDLE_SESSION_TIMEOUT', 'HTTP', 'The maximum duration (in minutes) allowed between requests made within the same session, after which the session is terminated. A value less than one allows an indefinite amount of time between requests.', 'GLOBAL', 'INTEGER', '15', 1, 'Security', '8.3-SP2', 0);

--changeSet OPER-31259:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'CHECK_SUP_LOC_STOCK_LEVELS_ON_IMPORT', 'LOGIC', 'Permission to automatically trigger the POREQ and SHIPREQ stock low actions on imported supply location stock levels.', 'GLOBAL', 'BOOLEAN', 'FALSE', 1, 'Org - Locations', '8.3-SP2', 0);

--changeSet OPER-31259:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
VALUES ('FALSE', 'CHECK_WAREHOUSE_LOC_STOCK_LEVELS_ON_IMPORT', 'LOGIC', 'Permission to automatically trigger the DISTREQ stock low action on imported warehouse location stock levels.', 'GLOBAL', 'BOOLEAN', 'FALSE', 1, 'Org - Locations', '8.3-SP2', 0);
