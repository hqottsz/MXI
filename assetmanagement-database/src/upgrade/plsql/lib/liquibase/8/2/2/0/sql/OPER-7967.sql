--liquibase formatted sql


--changeSet OPER-7967:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- mxcommon parameters
-- servlet parameters
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ALERT_DEFAULT_ALERT',
   'Permission to execute com.mxi.mx.common.servlet.alert.DefaultAlertServlet servlet with type ACTION_ALERT_DEFAULT_ALERT',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ALERT_CREATE_LOGIN_ALERT',
   'Permission to execute com.mxi.mx.common.servlet.alert.CreateLoginAlert servlet with type ACTION_ALERT_CREATE_LOGIN_ALERT',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ALERT_GET_PICKED_ITEMS_INFO_COMMON',
   'Permission to execute com.mxi.mx.common.servlet.alert.GetPickedItemsInfoCommon servlet with type ACTION_ALERT_GET_PICKED_ITEMS_INFO_COMMON',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INTEGRATION_MESSAGE_DATA',
   'Permission to execute com.mxi.mx.common.servlet.integration.MessageData servlet with type ACTION_INTEGRATION_MESSAGE_DATA',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_WORK_WORK_ITEM_ACTION',
   'Permission to execute com.mxi.mx.common.servlet.work.WorkItemAction servlet with type ACTION_WORK_WORK_ITEM_ACTION',
   'TRUE/FALSE',
   'TRUE',
   1,
   'Servlet Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- jsp parameters
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_ALERT_CREATE_LOGIN_ALERT',
   'Permission to execute webapp/common/alert/CreateLoginAlert.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_ALERT_LOGIN_ALERT',
   'Permission to execute webapp/common/alert/LoginAlert.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_ALERT_PICKED_ITEMS',
   'Permission to execute webapp/common/alert/PickedItems.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_ALERT_USER_ALERTS',
   'Permission to execute webapp/common/alert/UserAlerts.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_INTEGRATION_BUSINESS_PROCESS_VIEWER',
   'Permission to execute webapp/common/integration/BusinessProcessViewer.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_INTEGRATION_LOG_SEARCH',
   'Permission to execute webapp/common/integration/LogSearch.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_INTEGRATION_MESSAGE_DETAILS',
   'Permission to execute webapp/common/integration/MessageDetails.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_INTEGRATION_SEND_MESSAGE',
   'Permission to execute webapp/common/integration/SendMessage.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_INTEGRATION_VIEW_RESPONSE',
   'Permission to execute webapp/common/integration/ViewResponse.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_WORK_ENABLE_DISABLE_WORK_ITEM_TYPE',
   'Permission to execute webapp/common/work/EnableDisableWorkItemType.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_WORK_SET_SCHEDULE_DATE',
   'Permission to execute webapp/common/work/SetScheduleDate.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/

--changeSet OPER-7967:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_COMMON_WORK_WORK_ITEM_ADMIN_CONSOLE',
   'Permission to execute webapp/common/work/WorkItemAdminConsole.jsp',
   'TRUE/FALSE',
   'TRUE',
   1,
   'JSP Permission',
   '8.2-SP3',
   0,
   0,
   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
  );

END;
/