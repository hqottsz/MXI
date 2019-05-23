--liquibase formatted sql

--changeSet OPER-30404-003:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(274,'core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_name','core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_description','PRIVATE',NULL,'INVENTORY','core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(275,'core.alert.SYNC_USAGE_PARAMETERS_FAILED_name','core.alert.SYNC_USAGE_PARAMETERS_FAILED_description','PRIVATE',NULL,'INVENTORY','core.alert.SYNC_USAGE_PARAMETERS_FAILED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(276,'core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_name','core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_description','PRIVATE',NULL,'LICENSEDEFN','core.alert.EVALUATE_LICENSE_DEFN_COMPLETED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(277,'core.alert.EVALUATE_LICENSE_DEFN_FAILED_name','core.alert.EVALUATE_LICENSE_DEFN_FAILED_description','PRIVATE',NULL,'LICENSEDEFN','core.alert.EVALUATE_LICENSE_DEFN_FAILED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
END;
/

--changeSet OPER-30404-003:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment remove the previous async action alert data for generic async actions
BEGIN
   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert alert parm data
   DELETE FROM
      utl_alert_parm
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_parm.alert_id AND
           utl_alert.alert_type_id IN (66,67)
       );
     
   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert alert log data
   DELETE FROM
      utl_alert_log  
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_log.alert_id AND
           utl_alert.alert_type_id IN (66,67)
       );
    
   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert alert status log data
   DELETE FROM 
      utl_alert_status_log
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_status_log.alert_id AND
           utl_alert.alert_type_id IN (66,67)
       );

   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert assigned to users data
   DELETE FROM
      utl_user_alert
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_user_alert.alert_id AND
           utl_alert.alert_type_id IN (66,67)
       );
    
   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert alert data
   DELETE FROM
      utl_alert
   WHERE utl_alert.alert_type_id IN (66,67);

   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert from any assigned roles
   DELETE FROM
      utl_alert_type_role
   WHERE
      utl_alert_type_role.alert_type_id IN (66,67);

   -- Delete AsyncActionCompleteAlert, AsyncActionErrorAlert alert type
   DELETE FROM 
      utl_alert_type
   WHERE utl_alert_type.alert_type_id IN (66,67);
END;
/

--changeSet OPER-30404-003:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment remove the async action menu item
BEGIN
   -- Remove any menu group mapping making use of the Async Action Menu
   DELETE FROM
      utl_menu_group_item
   WHERE EXISTS
         (SELECT
             NULL
          FROM
             utl_menu_item
          WHERE
             utl_menu_group_item.menu_id = utl_menu_item.menu_id AND
             utl_menu_item.menu_name     = 'web.menuitem.ASYNCHRONOUS_ACTIONS');
              

   -- Remove any menu items referencing the Async Actions Page
   DELETE FROM
      utl_menu_item
   WHERE utl_menu_item.menu_name = 'web.menuitem.ASYNCHRONOUS_ACTIONS';
END;
/