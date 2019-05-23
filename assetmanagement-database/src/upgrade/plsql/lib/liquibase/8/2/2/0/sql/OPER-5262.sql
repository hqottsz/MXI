--liquibase formatted sql


--changeSet OPER-5262:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_EMBEDDED_VIEW_REPORT',
   'Permission to execute webapp/embedded/ViewReport.jsp',
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

--changeSet OPER-5262:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_EMBEDDED_JASPER_VIEW_REPORT',
   'Permission to execute webapp/embedded/jasper/ViewReport.jsp',
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

--changeSet OPER-5262:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_CHANGE_REPORT_NAME_FORM',
   'Permission to execute webapp/enterprise/changeReportNameForm.jsp',
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

--changeSet OPER-5262:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_REPORT_DESCRIPTION',
   'Permission to execute webapp/enterprise/reportDescription.jsp',
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

--changeSet OPER-5262:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_REPORTS',
   'Permission to execute webapp/enterprise/reports.jsp',
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

--changeSet OPER-5262:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_SCHEDULE_FORM',
   'Permission to execute webapp/enterprise/scheduleForm.jsp',
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

--changeSet OPER-5262:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_SEARCH',
   'Permission to execute webapp/enterprise/search.jsp',
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

--changeSet OPER-5262:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_SEARCH_FORM',
   'Permission to execute webapp/enterprise/searchForm.jsp',
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

--changeSet OPER-5262:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_ENTERPRISE_VIEW_REPORT2',
   'Permission to execute webapp/enterprise/viewReport2.jsp',
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

--changeSet OPER-5262:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_SHARED_REPORT_PARAMETERS',
   'Permission to execute webapp/shared/ReportParameters.jsp',
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

--changeSet OPER-5262:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_SHARED_JASPER_REPORT_PARAMETERS',
   'Permission to execute webapp/shared/jasper/ReportParameters.jsp',
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

--changeSet OPER-5262:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ADVISORY_ADD_EDIT_ADVISORY',
   'Permission to execute webapp/web/advisory/AddEditAdvisory.jsp',
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

--changeSet OPER-5262:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ADVISORY_ADVISORY_DETAILS',
   'Permission to execute webapp/web/advisory/AdvisoryDetails.jsp',
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

--changeSet OPER-5262:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ADVISORY_CLEAR_ADVISORY',
   'Permission to execute webapp/web/advisory/ClearAdvisory.jsp',
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

--changeSet OPER-5262:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ADVISORY_OUTSTANDING_ADVSRY_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/advisory/OutstandingAdvsrySearchByType.jsp',
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

--changeSet OPER-5262:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_ADD_NOTE',
   'Permission to execute webapp/web/alert/AddNote.jsp',
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

--changeSet OPER-5262:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_ALERT_DETAILS',
   'Permission to execute webapp/web/alert/AlertDetails.jsp',
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

--changeSet OPER-5262:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_ALERT_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/alert/AlertSearchByType.jsp',
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

--changeSet OPER-5262:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_ALERT_SETUP',
   'Permission to execute webapp/web/alert/AlertSetup.jsp',
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

--changeSet OPER-5262:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_DISPOSITION_ALERT',
   'Permission to execute webapp/web/alert/DispositionAlert.jsp',
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

--changeSet OPER-5262:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ALERT_EDIT_ROLE_ASSIGNMENTS',
   'Permission to execute webapp/web/alert/EditRoleAssignments.jsp',
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

--changeSet OPER-5262:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_ASSEMBLY_DETAILS',
   'Permission to execute webapp/web/assembly/AssemblyDetails.jsp',
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

--changeSet OPER-5262:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_DEFERRAL_DETAILS_DEFER_REF_DETAILS',
   'Permission to execute webapp/web/assembly/deferral/details/DeferRefDetails.jsp',
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

--changeSet OPER-5262:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_CREATE_EDIT_MEASUREMENT',
   'Permission to execute webapp/web/assembly/measurements/CreateEditMeasurement.jsp',
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

--changeSet OPER-5262:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_MEASUREMENT_SEARCH',
   'Permission to execute webapp/web/assembly/measurements/MeasurementSearch.jsp',
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

--changeSet OPER-5262:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_REORDER_MEASUREMENTS',
   'Permission to execute webapp/web/assembly/measurements/ReorderMeasurements.jsp',
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

--changeSet OPER-5262:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_EDIT_ASSEMBLY_THRESHOLD',
   'Permission to execute webapp/web/assembly/oilconsumption/EditAssemblyThreshold.jsp',
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

--changeSet OPER-5262:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_EDIT_OPERATOR_SPECIFIC_THRESHOLD',
   'Permission to execute webapp/web/assembly/oilconsumption/EditOperatorSpecificThreshold.jsp',
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

--changeSet OPER-5262:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_OIL_CONSUMPTION_RATE_DEFINITION',
   'Permission to execute webapp/web/assembly/oilconsumption/OilConsumptionRateDefinition.jsp',
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

--changeSet OPER-5262:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ASSEMBLY_SUBTYPES_CREATE_EDIT_SUBTYPE',
   'Permission to execute webapp/web/assembly/subtypes/CreateEditSubtype.jsp',
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

--changeSet OPER-5262:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ATTACH_ADD_ATTACHMENT',
   'Permission to execute webapp/web/attach/AddAttachment.jsp',
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

--changeSet OPER-5262:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ATTACH_EDIT_ATTACHMENT',
   'Permission to execute webapp/web/attach/EditAttachment.jsp',
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

--changeSet OPER-5262:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_ADD_FAILURE_PRIORITY',
   'Permission to execute webapp/web/authority/AddFailurePriority.jsp',
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

--changeSet OPER-5262:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_ASSIGN_INVENTORY',
   'Permission to execute webapp/web/authority/AssignInventory.jsp',
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

--changeSet OPER-5262:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_AUTHORITY_DETAILS',
   'Permission to execute webapp/web/authority/AuthorityDetails.jsp',
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

--changeSet OPER-5262:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_AUTHORITY_SEARCH',
   'Permission to execute webapp/web/authority/AuthoritySearch.jsp',
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

--changeSet OPER-5262:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_CREATE_EDIT_AUTHORITY',
   'Permission to execute webapp/web/authority/CreateEditAuthority.jsp',
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

--changeSet OPER-5262:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_AUTHORITY_EDIT_FAILURE_PRIORITY',
   'Permission to execute webapp/web/authority/EditFailurePriority.jsp',
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

--changeSet OPER-5262:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_ADD_CONFIG_SLOT',
   'Permission to execute webapp/web/bom/AddConfigSlot.jsp',
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

--changeSet OPER-5262:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_CONFIG_SLOT_DETAILS',
   'Permission to execute webapp/web/bom/ConfigSlotDetails.jsp',
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

--changeSet OPER-5262:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_CREATE_EDIT_ASSEMBLY',
   'Permission to execute webapp/web/bom/CreateEditAssembly.jsp',
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

--changeSet OPER-5262:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_CREATE_EDIT_CONFIG_SLOT',
   'Permission to execute webapp/web/bom/CreateEditConfigSlot.jsp',
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

--changeSet OPER-5262:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_EDIT_ASSIGNED_PART_DETAILS',
   'Permission to execute webapp/web/bom/EditAssignedPartDetails.jsp',
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

--changeSet OPER-5262:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_EDIT_PART_GROUP_APPLICABILITY',
   'Permission to execute webapp/web/bom/EditPartGroupApplicability.jsp',
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

--changeSet OPER-5262:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_EDIT_POSITIONS',
   'Permission to execute webapp/web/bom/EditPositions.jsp',
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

--changeSet OPER-5262:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_SELECT_CONFIG_SLOT',
   'Permission to execute webapp/web/bom/SelectConfigSlot.jsp',
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

--changeSet OPER-5262:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_SELECT_PARENT_CONFIG_SLOT',
   'Permission to execute webapp/web/bom/SelectParentConfigSlot.jsp',
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

--changeSet OPER-5262:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_BOM_FAULTTHRESHOLD_FAULT_THRESHOLD_DETAILS',
   'Permission to execute webapp/web/bom/faultthreshold/FaultThresholdDetails.jsp',
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

--changeSet OPER-5262:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CAPABILITY_CAPABILITY_DETAILS',
   'Permission to execute webapp/web/capability/CapabilityDetails.jsp',
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

--changeSet OPER-5262:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CAPABILITY_CAPABILITY_SEARCH',
   'Permission to execute webapp/web/capability/CapabilitySearch.jsp',
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

--changeSet OPER-5262:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CAPABILITY_CREATE_EDIT_CAPABILITY',
   'Permission to execute webapp/web/capability/CreateEditCapability.jsp',
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

--changeSet OPER-5262:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CANCEL_CLAIM',
   'Permission to execute webapp/web/claim/CancelClaim.jsp',
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

--changeSet OPER-5262:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CLAIM_DETAILS',
   'Permission to execute webapp/web/claim/ClaimDetails.jsp',
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

--changeSet OPER-5262:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CLAIM_SEARCH',
   'Permission to execute webapp/web/claim/ClaimSearch.jsp',
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

--changeSet OPER-5262:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CLOSE_CLAIM',
   'Permission to execute webapp/web/claim/CloseClaim.jsp',
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

--changeSet OPER-5262:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CREATE_EDIT_CLAIM',
   'Permission to execute webapp/web/claim/CreateEditClaim.jsp',
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

--changeSet OPER-5262:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_LABOR_LINE_NOTE',
   'Permission to execute webapp/web/claim/EditClaimLaborLineNote.jsp',
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

--changeSet OPER-5262:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_PART_LINE_NOTE',
   'Permission to execute webapp/web/claim/EditClaimPartLineNote.jsp',
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

--changeSet OPER-5262:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_PART_LINES',
   'Permission to execute webapp/web/claim/EditClaimPartLines.jsp',
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

--changeSet OPER-5262:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CLAIM_CLAIMDETAILS_EDIT_CLAIM_LABOR_LINES',
   'Permission to execute webapp/web/claim/claimdetails/EditClaimLaborLines.jsp',
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

--changeSet OPER-5262:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CYCLECOUNT_LOAD_CYCLE_COUNT_RESULTS',
   'Permission to execute webapp/web/cyclecount/LoadCycleCountResults.jsp',
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

--changeSet OPER-5262:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CYCLECOUNT_RECOUNT_RESULTS',
   'Permission to execute webapp/web/cyclecount/RecountResults.jsp',
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

--changeSet OPER-5262:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_CYCLECOUNT_SET_NEXT_COUNT_DATE',
   'Permission to execute webapp/web/cyclecount/SetNextCountDate.jsp',
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

--changeSet OPER-5262:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DBRULECHECKER_CREATE_EDIT_DATABASE_RULE',
   'Permission to execute webapp/web/dbrulechecker/CreateEditDatabaseRule.jsp',
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

--changeSet OPER-5262:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_CHECK_RESULTS',
   'Permission to execute webapp/web/dbrulechecker/DatabaseRuleCheckResults.jsp',
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

--changeSet OPER-5262:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_DETAILS',
   'Permission to execute webapp/web/dbrulechecker/DatabaseRuleDetails.jsp',
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

--changeSet OPER-5262:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_SEARCH',
   'Permission to execute webapp/web/dbrulechecker/DatabaseRuleSearch.jsp',
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

--changeSet OPER-5262:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DEPARTMENT_DEPARTMENT_DETAILS',
   'Permission to execute webapp/web/department/DepartmentDetails.jsp',
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

--changeSet OPER-5262:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DEPARTMENT_DEPARTMENT_SEARCH',
   'Permission to execute webapp/web/department/DepartmentSearch.jsp',
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

--changeSet OPER-5262:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DEPLOYED_DEPLOYED_OPS_IMPORT_SEARCH',
   'Permission to execute webapp/web/deployed/DeployedOpsImportSearch.jsp',
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

--changeSet OPER-5262:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DEPLOYED_IMPORT_FILE_DETAILS',
   'Permission to execute webapp/web/deployed/ImportFileDetails.jsp',
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

--changeSet OPER-5262:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_DEVELOPER_REST_API',
   'Permission to execute webapp/web/developer/RestAPI.jsp',
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

--changeSet OPER-5262:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ER_CREATE_EDIT_EXTRACTION_RULE',
   'Permission to execute webapp/web/er/CreateEditExtractionRule.jsp',
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

--changeSet OPER-5262:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ER_EXTRACTION_RULE_DETAILS',
   'Permission to execute webapp/web/er/ExtractionRuleDetails.jsp',
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

--changeSet OPER-5262:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ESIGNER_ELECTRONIC_SIGNATURE',
   'Permission to execute webapp/web/esigner/ElectronicSignature.jsp',
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

--changeSet OPER-5262:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ESIGNER_GENERATE_CERTIFICATE',
   'Permission to execute webapp/web/esigner/GenerateCertificate.jsp',
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

--changeSet OPER-5262:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_EVENT_CREATE_EDIT_EVENT',
   'Permission to execute webapp/web/event/CreateEditEvent.jsp',
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

--changeSet OPER-5262:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_EVENT_EVENT_SEARCH',
   'Permission to execute webapp/web/event/EventSearch.jsp',
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

--changeSet OPER-5262:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAILEFFECT_ASSIGN_FAULT_DEFINITION',
   'Permission to execute webapp/web/faileffect/AssignFaultDefinition.jsp',
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

--changeSet OPER-5262:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAILEFFECT_CREATE_EDIT_FAIL_EFFECT',
   'Permission to execute webapp/web/faileffect/CreateEditFailEffect.jsp',
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

--changeSet OPER-5262:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAILEFFECT_FAIL_EFFECT_DETAILS',
   'Permission to execute webapp/web/faileffect/FailEffectDetails.jsp',
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

--changeSet OPER-5262:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAILEFFECT_FAILURE_EFFECT_SEARCH',
   'Permission to execute webapp/web/faileffect/FailureEffectSearch.jsp',
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

--changeSet OPER-5262:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_CORRECTIVE_ACTIONS',
   'Permission to execute webapp/web/fault/CorrectiveActions.jsp',
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

--changeSet OPER-5262:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_EDIT_FAULT_DESCRIPTION',
   'Permission to execute webapp/web/fault/EditFaultDescription.jsp',
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

--changeSet OPER-5262:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_FAULT_DEFINITION_SEARCH',
   'Permission to execute webapp/web/fault/FaultDefinitionSearch.jsp',
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

--changeSet OPER-5262:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_FAULT_PAGE',
   'Permission to execute webapp/web/fault/FaultPage.jsp',
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

--changeSet OPER-5262:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_FAULT_SEARCH',
   'Permission to execute webapp/web/fault/FaultSearch.jsp',
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

--changeSet OPER-5262:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_FAULT_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/fault/FaultSearchByType.jsp',
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

--changeSet OPER-5262:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_FIND_RECURRING_FAULT',
   'Permission to execute webapp/web/fault/FindRecurringFault.jsp',
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

--changeSet OPER-5262:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_POSSIBLE_FAULT_DETAILS',
   'Permission to execute webapp/web/fault/PossibleFaultDetails.jsp',
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

--changeSet OPER-5262:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_DEFERRAL_CREATE_EDIT_FAULT_DEFER_REF',
   'Permission to execute webapp/web/fault/deferral/CreateEditFaultDeferRef.jsp',
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

--changeSet OPER-5262:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULT_THRESHOLD_CREATE_EDIT_FAULT_THRESHOLD',
   'Permission to execute webapp/web/fault/threshold/CreateEditFaultThreshold.jsp',
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

--changeSet OPER-5262:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_ADD_ATTACHMENT',
   'Permission to execute webapp/web/faultdefn/AddAttachment.jsp',
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

--changeSet OPER-5262:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_ADD_TECHNICAL_REFERENCE',
   'Permission to execute webapp/web/faultdefn/AddTechnicalReference.jsp',
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

--changeSet OPER-5262:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_FAILURE_EFFECT',
   'Permission to execute webapp/web/faultdefn/AssignFailureEffect.jsp',
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

--changeSet OPER-5262:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_FAULT_SUPPRESSION',
   'Permission to execute webapp/web/faultdefn/AssignFaultSuppression.jsp',
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

--changeSet OPER-5262:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_TASK_DEFINITION',
   'Permission to execute webapp/web/faultdefn/AssignTaskDefinition.jsp',
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

--changeSet OPER-5262:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_CREATE_EDIT_FAULT_DEFINITION',
   'Permission to execute webapp/web/faultdefn/CreateEditFaultDefinition.jsp',
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

--changeSet OPER-5262:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_EDIT_FAILURE_EFFECT_ORDER',
   'Permission to execute webapp/web/faultdefn/EditFailureEffectOrder.jsp',
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

--changeSet OPER-5262:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_EDIT_TECHNICAL_REFERENCE_ORDER',
   'Permission to execute webapp/web/faultdefn/EditTechnicalReferenceOrder.jsp',
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

--changeSet OPER-5262:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_FAULT_DEFINITION_DETAILS',
   'Permission to execute webapp/web/faultdefn/FaultDefinitionDetails.jsp',
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

--changeSet OPER-5262:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FAULTDEFN_FAULT_DEFINITION_SEARCH',
   'Permission to execute webapp/web/faultdefn/FaultDefinitionSearch.jsp',
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

--changeSet OPER-5262:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_ADD_NOTES',
   'Permission to execute webapp/web/flight/AddNotes.jsp',
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

--changeSet OPER-5262:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_COMPLETE_FLIGHT',
   'Permission to execute webapp/web/flight/CompleteFlight.jsp',
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

--changeSet OPER-5262:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_EDIT_FLIGHT',
   'Permission to execute webapp/web/flight/EditFlight.jsp',
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

--changeSet OPER-5262:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_EDIT_FLIGHT_MEASUREMENTS',
   'Permission to execute webapp/web/flight/EditFlightMeasurements.jsp',
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

--changeSet OPER-5262:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_FLIGHT_DETAILS',
   'Permission to execute webapp/web/flight/FlightDetails.jsp',
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

--changeSet OPER-5262:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHT_FLIGHT_SEARCH',
   'Permission to execute webapp/web/flight/FlightSearch.jsp',
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

--changeSet OPER-5262:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHTDISRUPTION_FLIGHT_DISRUPTION_DETAILS',
   'Permission to execute webapp/web/flightdisruption/FlightDisruptionDetails.jsp',
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

--changeSet OPER-5262:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FLIGHTDISRUPTION_FLIGHT_DISRUPTION_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/flightdisruption/FlightDisruptionSearchByType.jsp',
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

--changeSet OPER-5262:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FNC_ACCOUNT_SEARCH',
   'Permission to execute webapp/web/fnc/AccountSearch.jsp',
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

--changeSet OPER-5262:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FNC_TCODE_SEARCH',
   'Permission to execute webapp/web/fnc/TCodeSearch.jsp',
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

--changeSet OPER-5262:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FNC_TRANSACTION_SEARCH',
   'Permission to execute webapp/web/fnc/TransactionSearch.jsp',
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

--changeSet OPER-5262:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FORECAST_ASSIGN_FORECAST_MODEL',
   'Permission to execute webapp/web/forecast/AssignForecastModel.jsp',
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

--changeSet OPER-5262:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FORECAST_CREATE_EDIT_FORECAST_MODEL',
   'Permission to execute webapp/web/forecast/CreateEditForecastModel.jsp',
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

--changeSet OPER-5262:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FORECAST_FC_MODEL_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/forecast/FcModelSearchByType.jsp',
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

--changeSet OPER-5262:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_FORECAST_FORECAST_MODEL_DETAILS',
   'Permission to execute webapp/web/forecast/ForecastModelDetails.jsp',
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

--changeSet OPER-5262:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_ADD_TECHNICAL_REFERENCE',
   'Permission to execute webapp/web/ietm/AddTechnicalReference.jsp',
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

--changeSet OPER-5262:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_CREATE_EDIT_IETM',
   'Permission to execute webapp/web/ietm/CreateEditIetm.jsp',
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

--changeSet OPER-5262:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_CREATE_EDIT_IETM_TOPIC',
   'Permission to execute webapp/web/ietm/CreateEditIetmTopic.jsp',
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

--changeSet OPER-5262:121 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_IETM_DETAILS',
   'Permission to execute webapp/web/ietm/IetmDetails.jsp',
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

--changeSet OPER-5262:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_IETM_SEARCH',
   'Permission to execute webapp/web/ietm/IetmSearch.jsp',
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

--changeSet OPER-5262:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_IETM_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/ietm/IetmSearchByType.jsp',
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

--changeSet OPER-5262:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IETM_IETM_TOPIC_DETAILS',
   'Permission to execute webapp/web/ietm/IetmTopicDetails.jsp',
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

--changeSet OPER-5262:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_IMPACTS_ADD_EDIT_IMPACTS',
   'Permission to execute webapp/web/impacts/AddEditImpacts.jsp',
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

--changeSet OPER-5262:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INCLUDE_FAULT_FAULT_INFO',
   'Permission to execute webapp/web/include/fault/FaultInfo.jsp',
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

--changeSet OPER-5262:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INCLUDE_INVENTORY_ISSUEINVENTORY_ISSUE_INVENTORY_TO_FAULT',
   'Permission to execute webapp/web/include/inventory/IssueInventory/IssueInventoryToFault.jsp',
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

--changeSet OPER-5262:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INCLUDE_INVENTORY_ISSUEINVENTORY_ISSUE_INVENTORY_TO_TASK',
   'Permission to execute webapp/web/include/inventory/IssueInventory/IssueInventoryToTask.jsp',
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

--changeSet OPER-5262:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_ADD_BLACKOUT',
   'Permission to execute webapp/web/inventory/AddBlackout.jsp',
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

--changeSet OPER-5262:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_ADJUST_QUANTITY',
   'Permission to execute webapp/web/inventory/AdjustQuantity.jsp',
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

--changeSet OPER-5262:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_AIRCRAFT_REPORTS',
   'Permission to execute webapp/web/inventory/AircraftReports.jsp',
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

--changeSet OPER-5262:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_AIRCRAFT_SELECTION',
   'Permission to execute webapp/web/inventory/AircraftSelection.jsp',
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

--changeSet OPER-5262:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CHANGE_BOM_ITEM',
   'Permission to execute webapp/web/inventory/ChangeBomItem.jsp',
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

--changeSet OPER-5262:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CHANGE_CUSTODY',
   'Permission to execute webapp/web/inventory/ChangeCustody.jsp',
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

--changeSet OPER-5262:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CHANGE_OWNER',
   'Permission to execute webapp/web/inventory/ChangeOwner.jsp',
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

--changeSet OPER-5262:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CHECK_REPORTS',
   'Permission to execute webapp/web/inventory/CheckReports.jsp',
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

--changeSet OPER-5262:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CONFIRM_INCOMPLETE_INVENTORY',
   'Permission to execute webapp/web/inventory/ConfirmIncompleteInventory.jsp',
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

--changeSet OPER-5262:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_CREATE_INVENTORY',
   'Permission to execute webapp/web/inventory/CreateInventory.jsp',
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

--changeSet OPER-5262:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_DETACH_INVENTORY',
   'Permission to execute webapp/web/inventory/DetachInventory.jsp',
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

--changeSet OPER-5262:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_EDIT_INVENTORY',
   'Permission to execute webapp/web/inventory/EditInventory.jsp',
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

--changeSet OPER-5262:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_INVENTORY_DETAILS',
   'Permission to execute webapp/web/inventory/InventoryDetails.jsp',
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

--changeSet OPER-5262:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_INVENTORY_SEARCH',
   'Permission to execute webapp/web/inventory/InventorySearch.jsp',
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

--changeSet OPER-5262:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_INVENTORY_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/inventory/InventorySearchByType.jsp',
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

--changeSet OPER-5262:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_INVENTORY_SELECTION',
   'Permission to execute webapp/web/inventory/InventorySelection.jsp',
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

--changeSet OPER-5262:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_LOCK_INVENTORY',
   'Permission to execute webapp/web/inventory/LockInventory.jsp',
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

--changeSet OPER-5262:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_RELEASE_INVENTORY',
   'Permission to execute webapp/web/inventory/ReleaseInventory.jsp',
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

--changeSet OPER-5262:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_RETURN_TO_VENDOR',
   'Permission to execute webapp/web/inventory/ReturnToVendor.jsp',
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

--changeSet OPER-5262:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_SELECT_REMOVED_ITEM',
   'Permission to execute webapp/web/inventory/SelectRemovedItem.jsp',
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

--changeSet OPER-5262:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_SPLIT_BIN',
   'Permission to execute webapp/web/inventory/SplitBin.jsp',
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

--changeSet OPER-5262:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_UNARCHIVE_INVENTORY',
   'Permission to execute webapp/web/inventory/UnarchiveInventory.jsp',
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

--changeSet OPER-5262:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_BULKCREATEINVENTORY_BULK_CREATE_INVENTORY',
   'Permission to execute webapp/web/inventory/bulkcreateinventory/BulkCreateInventory.jsp',
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

--changeSet OPER-5262:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_BULKCREATEINVENTORY_BULK_CREATE_INV_SUMMARY',
   'Permission to execute webapp/web/inventory/bulkcreateinventory/BulkCreateInvSummary.jsp',
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

--changeSet OPER-5262:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_OILCONSUMPTION_ESCALATE_OIL_CONSUMPTION_STATUS',
   'Permission to execute webapp/web/inventory/oilconsumption/EscalateOilConsumptionStatus.jsp',
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

--changeSet OPER-5262:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_INVENTORY_RELIABILITY_RELIABILITY_NOTE_SEARCH',
   'Permission to execute webapp/web/inventory/reliability/ReliabilityNoteSearch.jsp',
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

--changeSet OPER-5262:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_KIT_PICK_ITEMS_FOR_KIT',
   'Permission to execute webapp/web/kit/PickItemsForKit.jsp',
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

--changeSet OPER-5262:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_ADD_SIGNOFF_REQUIREMENT',
   'Permission to execute webapp/web/labour/AddSignoffRequirement.jsp',
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

--changeSet OPER-5262:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_CREATE_LABOUR',
   'Permission to execute webapp/web/labour/CreateLabour.jsp',
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

--changeSet OPER-5262:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_HISTORY',
   'Permission to execute webapp/web/labour/EditLabourHistory.jsp',
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

--changeSet OPER-5262:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_REQUIREMENTS',
   'Permission to execute webapp/web/labour/EditLabourRequirements.jsp',
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

--changeSet OPER-5262:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_EDIT_WPLABOUR_HISTORY',
   'Permission to execute webapp/web/labour/EditWPLabourHistory.jsp',
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

--changeSet OPER-5262:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_FIND_HUMAN_RESOURCE',
   'Permission to execute webapp/web/labour/FindHumanResource.jsp',
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

--changeSet OPER-5262:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LABOUR_LABOUR_ASSIGNMENT',
   'Permission to execute webapp/web/labour/LabourAssignment.jsp',
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

--changeSet OPER-5262:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LICENSEDEFN_ACTIVATE_LICENCE_DEFN',
   'Permission to execute webapp/web/licensedefn/ActivateLicenceDefn.jsp',
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

--changeSet OPER-5262:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LICENSEDEFN_CREATE_EDIT_LICENSE_DEFN',
   'Permission to execute webapp/web/licensedefn/CreateEditLicenseDefn.jsp',
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

--changeSet OPER-5262:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LICENSEDEFN_LICENSE_DEFN_DETAILS',
   'Permission to execute webapp/web/licensedefn/LicenseDefnDetails.jsp',
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

--changeSet OPER-5262:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LICENSEDEFN_LICENSE_DEFN_SEARCH',
   'Permission to execute webapp/web/licensedefn/LicenseDefnSearch.jsp',
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

--changeSet OPER-5262:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LICENSEDEFN_OBSOLETE_LICENSE_DEFN',
   'Permission to execute webapp/web/licensedefn/ObsoleteLicenseDefn.jsp',
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

--changeSet OPER-5262:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_ASSIGN_CAPABILITY',
   'Permission to execute webapp/web/location/AssignCapability.jsp',
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

--changeSet OPER-5262:169 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_ASSIGN_CAPACITY',
   'Permission to execute webapp/web/location/AssignCapacity.jsp',
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

--changeSet OPER-5262:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_ASSIGN_ORGANIZATION_TO_LOCATION',
   'Permission to execute webapp/web/location/AssignOrganizationToLocation.jsp',
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

--changeSet OPER-5262:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_CREATE_EDIT_CONTACT',
   'Permission to execute webapp/web/location/CreateEditContact.jsp',
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

--changeSet OPER-5262:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_CREATE_EDIT_PRINTER',
   'Permission to execute webapp/web/location/CreateEditPrinter.jsp',
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

--changeSet OPER-5262:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_EDIT_CAPACITY',
   'Permission to execute webapp/web/location/EditCapacity.jsp',
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

--changeSet OPER-5262:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_LOCATION_DETAILS',
   'Permission to execute webapp/web/location/LocationDetails.jsp',
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

--changeSet OPER-5262:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_LOCATION_SEARCH',
   'Permission to execute webapp/web/location/LocationSearch.jsp',
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

--changeSet OPER-5262:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_STATION_CAPACITY',
   'Permission to execute webapp/web/location/StationCapacity.jsp',
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

--changeSet OPER-5262:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LOCATION_STOCK_LEVEL_INVENTORY',
   'Permission to execute webapp/web/location/StockLevelInventory.jsp',
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

--changeSet OPER-5262:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LPA_LPARESULT',
   'Permission to execute webapp/web/lpa/LPAResult.jsp',
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

--changeSet OPER-5262:179 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LPA_RUN_DETAILS',
   'Permission to execute webapp/web/lpa/RunDetails.jsp',
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

--changeSet OPER-5262:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LPA_RUN_LPA',
   'Permission to execute webapp/web/lpa/RunLPA.jsp',
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

--changeSet OPER-5262:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_LRP_LRPREPORT_PARAMETERS',
   'Permission to execute webapp/web/lrp/LRPReportParameters.jsp',
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

--changeSet OPER-5262:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_ACTIVATE_MAINT_PROGRAM',
   'Permission to execute webapp/web/maint/ActivateMaintProgram.jsp',
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

--changeSet OPER-5262:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_ASSIGN_REQ',
   'Permission to execute webapp/web/maint/AssignReq.jsp',
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

--changeSet OPER-5262:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_CREATE_EDIT_MAINT_PROGRAM',
   'Permission to execute webapp/web/maint/CreateEditMaintProgram.jsp',
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

--changeSet OPER-5262:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_DEADLINE_EXTENSIONS',
   'Permission to execute webapp/web/maint/DeadlineExtensions.jsp',
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

--changeSet OPER-5262:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_EDIT_ISSUE_NUMBERS',
   'Permission to execute webapp/web/maint/EditIssueNumbers.jsp',
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

--changeSet OPER-5262:187 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_EDIT_REASONS_NOTES',
   'Permission to execute webapp/web/maint/EditReasonsNotes.jsp',
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

--changeSet OPER-5262:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_MAINT_PROGRAM_DETAILS',
   'Permission to execute webapp/web/maint/MaintProgramDetails.jsp',
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

--changeSet OPER-5262:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_SELECT_REQ',
   'Permission to execute webapp/web/maint/SelectReq.jsp',
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

--changeSet OPER-5262:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MAINT_UNASSIGN_REQ',
   'Permission to execute webapp/web/maint/UnassignReq.jsp',
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

--changeSet OPER-5262:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MANUFACTURER_CREATE_EDIT_MANUFACTURER',
   'Permission to execute webapp/web/manufacturer/CreateEditManufacturer.jsp',
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

--changeSet OPER-5262:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MANUFACTURER_MANUFACTURER_DETAILS',
   'Permission to execute webapp/web/manufacturer/ManufacturerDetails.jsp',
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

--changeSet OPER-5262:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MANUFACTURER_MANUFACTURER_SEARCH',
   'Permission to execute webapp/web/manufacturer/ManufacturerSearch.jsp',
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

--changeSet OPER-5262:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MENU_CREATE_EDIT_MENU_GROUP',
   'Permission to execute webapp/web/menu/CreateEditMenuGroup.jsp',
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

--changeSet OPER-5262:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MENU_EDIT_MENU_ITEM_ORDER',
   'Permission to execute webapp/web/menu/EditMenuItemOrder.jsp',
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

--changeSet OPER-5262:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MENU_EDIT_MENU_ORDER',
   'Permission to execute webapp/web/menu/EditMenuOrder.jsp',
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

--changeSet OPER-5262:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MENU_MENU_GROUP_DETAILS',
   'Permission to execute webapp/web/menu/MenuGroupDetails.jsp',
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

--changeSet OPER-5262:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_MENU_MENU_ITEM_SEARCH',
   'Permission to execute webapp/web/menu/MenuItemSearch.jsp',
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

--changeSet OPER-5262:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_ASSIGN_ORGANIZATION',
   'Permission to execute webapp/web/org/AssignOrganization.jsp',
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

--changeSet OPER-5262:200 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_ASSIGN_SKILL_TO_ORGANIZATION',
   'Permission to execute webapp/web/org/AssignSkillToOrganization.jsp',
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

--changeSet OPER-5262:201 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_CREATE_EDIT_ORGANIZATION',
   'Permission to execute webapp/web/org/CreateEditOrganization.jsp',
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

--changeSet OPER-5262:202 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_EDIT_SKILL_ORDER',
   'Permission to execute webapp/web/org/EditSkillOrder.jsp',
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

--changeSet OPER-5262:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_ORGANIZATION_DETAILS',
   'Permission to execute webapp/web/org/OrganizationDetails.jsp',
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

--changeSet OPER-5262:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_ORGANIZATION_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/org/OrganizationSearchByType.jsp',
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

--changeSet OPER-5262:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_PICK_ORGANIZATION',
   'Permission to execute webapp/web/org/PickOrganization.jsp',
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

--changeSet OPER-5262:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ORG_ORGDETAILS_APPROVAL_DETAILS',
   'Permission to execute webapp/web/org/orgdetails/ApprovalDetails.jsp',
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

--changeSet OPER-5262:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_OWNER_CREATE_EDIT_OWNER',
   'Permission to execute webapp/web/owner/CreateEditOwner.jsp',
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

--changeSet OPER-5262:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_OWNER_OWNER_DETAILS',
   'Permission to execute webapp/web/owner/OwnerDetails.jsp',
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

--changeSet OPER-5262:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_OWNER_OWNER_SEARCH',
   'Permission to execute webapp/web/owner/OwnerSearch.jsp',
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

--changeSet OPER-5262:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PANEL_PANEL_DETAILS',
   'Permission to execute webapp/web/panel/PanelDetails.jsp',
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

--changeSet OPER-5262:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADD_CONTENTS_TO_KIT',
   'Permission to execute webapp/web/part/AddContentsToKit.jsp',
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

--changeSet OPER-5262:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADD_EXTERNALLY_CONTROLLED_PART',
   'Permission to execute webapp/web/part/AddExternallyControlledPart.jsp',
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

--changeSet OPER-5262:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADD_PART_REQUIREMENT_SEARCH',
   'Permission to execute webapp/web/part/AddPartRequirementSearch.jsp',
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

--changeSet OPER-5262:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADD_TASK_INCOMPATIBILITY',
   'Permission to execute webapp/web/part/AddTaskIncompatibility.jsp',
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

--changeSet OPER-5262:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADJUST_AVG_UNIT_PRICE',
   'Permission to execute webapp/web/part/AdjustAvgUnitPrice.jsp',
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

--changeSet OPER-5262:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_ADJUST_TOTAL_SPARES',
   'Permission to execute webapp/web/part/AdjustTotalSpares.jsp',
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

--changeSet OPER-5262:217 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_BOMPART_DETAILS',
   'Permission to execute webapp/web/part/BOMPartDetails.jsp',
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

--changeSet OPER-5262:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_BOMPART_SEARCH',
   'Permission to execute webapp/web/part/BOMPartSearch.jsp',
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

--changeSet OPER-5262:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_CHANGE_PART_STATUS',
   'Permission to execute webapp/web/part/ChangePartStatus.jsp',
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

--changeSet OPER-5262:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_CREATE_ALTERNATE_PART',
   'Permission to execute webapp/web/part/CreateAlternatePart.jsp',
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

--changeSet OPER-5262:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_CREATE_BIN_LEVEL',
   'Permission to execute webapp/web/part/CreateBinLevel.jsp',
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

--changeSet OPER-5262:222 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_CREATE_EDIT_PART',
   'Permission to execute webapp/web/part/CreateEditPart.jsp',
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

--changeSet OPER-5262:223 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_CREATE_PART_FOR_TASK',
   'Permission to execute webapp/web/part/CreatePartForTask.jsp',
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

--changeSet OPER-5262:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_BIN_LEVELS',
   'Permission to execute webapp/web/part/EditBinLevels.jsp',
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

--changeSet OPER-5262:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_EXTERNAL_PART_REQUIREMENTS',
   'Permission to execute webapp/web/part/EditExternalPartRequirements.jsp',
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

--changeSet OPER-5262:226 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_INSTALL_KIT_MAP',
   'Permission to execute webapp/web/part/EditInstallKitMap.jsp',
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

--changeSet OPER-5262:227 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_INSTALL_KITS',
   'Permission to execute webapp/web/part/EditInstallKits.jsp',
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

--changeSet OPER-5262:228 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_KIT_CONTENTS',
   'Permission to execute webapp/web/part/EditKitContents.jsp',
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

--changeSet OPER-5262:229 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_PART_FINANCIALS',
   'Permission to execute webapp/web/part/EditPartFinancials.jsp',
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

--changeSet OPER-5262:230 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_PART_REQUIREMENTS',
   'Permission to execute webapp/web/part/EditPartRequirements.jsp',
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

--changeSet OPER-5262:231 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_PURCHASE_VENDOR',
   'Permission to execute webapp/web/part/EditPurchaseVendor.jsp',
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

--changeSet OPER-5262:232 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EDIT_REPAIR_VENDOR',
   'Permission to execute webapp/web/part/EditRepairVendor.jsp',
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

--changeSet OPER-5262:233 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_EXCHANGE_VENDOR_DETAILS',
   'Permission to execute webapp/web/part/ExchangeVendorDetails.jsp',
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

--changeSet OPER-5262:234 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_MANAGE_ALTERNATE_PART',
   'Permission to execute webapp/web/part/ManageAlternatePart.jsp',
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

--changeSet OPER-5262:235 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PART_DETAILS',
   'Permission to execute webapp/web/part/PartDetails.jsp',
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

--changeSet OPER-5262:236 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PART_NOTE',
   'Permission to execute webapp/web/part/PartNote.jsp',
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

--changeSet OPER-5262:237 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PART_SEARCH',
   'Permission to execute webapp/web/part/PartSearch.jsp',
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

--changeSet OPER-5262:238 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PART_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/part/PartSearchByType.jsp',
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

--changeSet OPER-5262:239 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PART_STATUS_CHANGE',
   'Permission to execute webapp/web/part/PartStatusChange.jsp',
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

--changeSet OPER-5262:240 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PURCHASE_VENDOR_PRICE_DETAILS',
   'Permission to execute webapp/web/part/PurchaseVendorPriceDetails.jsp',
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

--changeSet OPER-5262:241 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_SELECT_PART_GROUP',
   'Permission to execute webapp/web/part/SelectPartGroup.jsp',
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

--changeSet OPER-5262:242 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_SELECT_PART_INCOMPATIBILITY',
   'Permission to execute webapp/web/part/SelectPartIncompatibility.jsp',
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

--changeSet OPER-5262:243 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PART_PARTDETAILS_RECEIVING_NOTES',
   'Permission to execute webapp/web/part/partdetails/ReceivingNotes.jsp',
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

--changeSet OPER-5262:244 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PERMATRIX_PERM_MATRIX_SECURITY',
   'Permission to execute webapp/web/permatrix/PermMatrixSecurity.jsp',
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

--changeSet OPER-5262:245 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_ADD_MISC_POINVOICE_LINE',
   'Permission to execute webapp/web/pi/AddMiscPOInvoiceLine.jsp',
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

--changeSet OPER-5262:246 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_ADD_PART_POINVOICE_LINE',
   'Permission to execute webapp/web/pi/AddPartPOInvoiceLine.jsp',
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

--changeSet OPER-5262:247 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_CREATE_EDIT_POINVOICE',
   'Permission to execute webapp/web/pi/CreateEditPOInvoice.jsp',
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

--changeSet OPER-5262:248 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_EDIT_POINVOICE_LINE_NOTES',
   'Permission to execute webapp/web/pi/EditPOInvoiceLineNotes.jsp',
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

--changeSet OPER-5262:249 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_EDIT_POINVOICE_LINES',
   'Permission to execute webapp/web/pi/EditPOInvoiceLines.jsp',
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

--changeSet OPER-5262:250 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_MAP_POINVOICE_LINE',
   'Permission to execute webapp/web/pi/MapPOInvoiceLine.jsp',
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

--changeSet OPER-5262:251 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PI_POINVOICE_SEARCH',
   'Permission to execute webapp/web/pi/POInvoiceSearch.jsp',
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

--changeSet OPER-5262:252 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_ADD_MISC_ORDER_LINE',
   'Permission to execute webapp/web/po/AddMiscOrderLine.jsp',
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

--changeSet OPER-5262:253 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_AOGAUTHORIZE_ORDER',
   'Permission to execute webapp/web/po/AOGAuthorizeOrder.jsp',
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

--changeSet OPER-5262:254 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_AUTHORIZE_ORDER',
   'Permission to execute webapp/web/po/AuthorizeOrder.jsp',
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

--changeSet OPER-5262:255 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_CREATE_EDIT_ORDER',
   'Permission to execute webapp/web/po/CreateEditOrder.jsp',
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

--changeSet OPER-5262:256 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_EDIT_ORDER_LINES',
   'Permission to execute webapp/web/po/EditOrderLines.jsp',
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

--changeSet OPER-5262:257 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_EDIT_PURCHASE_ORDER_LINE_RECEIVER_NOTE',
   'Permission to execute webapp/web/po/EditPurchaseOrderLineReceiverNote.jsp',
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

--changeSet OPER-5262:258 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_EDIT_PURCHASE_ORDER_LINE_VENDOR_NOTE',
   'Permission to execute webapp/web/po/EditPurchaseOrderLineVendorNote.jsp',
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

--changeSet OPER-5262:259 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_ISSUE_ORDER',
   'Permission to execute webapp/web/po/IssueOrder.jsp',
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

--changeSet OPER-5262:260 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_ORDER_DETAILS',
   'Permission to execute webapp/web/po/OrderDetails.jsp',
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

--changeSet OPER-5262:261 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_ORDER_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/po/OrderSearchByType.jsp',
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

--changeSet OPER-5262:262 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_POSEARCH',
   'Permission to execute webapp/web/po/POSearch.jsp',
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

--changeSet OPER-5262:263 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_REQUEST_ORDER_AUTHORIZATION',
   'Permission to execute webapp/web/po/RequestOrderAuthorization.jsp',
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

--changeSet OPER-5262:264 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_SELECT_CHARGE_TO_ACCOUNT',
   'Permission to execute webapp/web/po/SelectChargeToAccount.jsp',
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

--changeSet OPER-5262:265 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_SELECT_INVENTORY',
   'Permission to execute webapp/web/po/SelectInventory.jsp',
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

--changeSet OPER-5262:266 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PO_SELECT_RETURN_INVENTORY',
   'Permission to execute webapp/web/po/SelectReturnInventory.jsp',
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

--changeSet OPER-5262:267 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_PROCUREMENT_ARCHIVE_TAX_CHARGE',
   'Permission to execute webapp/web/procurement/ArchiveTaxCharge.jsp',
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

--changeSet OPER-5262:268 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_QUARANTINE_QUARANTINE_DETAILS',
   'Permission to execute webapp/web/quarantine/QuarantineDetails.jsp',
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

--changeSet OPER-5262:269 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REPORT_COMPLIANCE_REPORT',
   'Permission to execute webapp/web/report/ComplianceReport.jsp',
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

--changeSet OPER-5262:270 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REPORT_CREATE_EDIT_REPORT_CONFIG',
   'Permission to execute webapp/web/report/CreateEditReportConfig.jsp',
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

--changeSet OPER-5262:271 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REPORT_REPORT_CONFIG',
   'Permission to execute webapp/web/report/ReportConfig.jsp',
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

--changeSet OPER-5262:272 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_CREATE_PURCHASE_REQUEST',
   'Permission to execute webapp/web/req/CreatePurchaseRequest.jsp',
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

--changeSet OPER-5262:273 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_CREATE_PURCHASE_REQUEST_FOR_STOCK',
   'Permission to execute webapp/web/req/CreatePurchaseRequestForStock.jsp',
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

--changeSet OPER-5262:274 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_EDIT_EXTERNAL_REFERENCE',
   'Permission to execute webapp/web/req/EditExternalReference.jsp',
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

--changeSet OPER-5262:275 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_EDIT_REQUEST_PRIORITY',
   'Permission to execute webapp/web/req/EditRequestPriority.jsp',
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

--changeSet OPER-5262:276 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_ISSUE_INVENTORY',
   'Permission to execute webapp/web/req/IssueInventory.jsp',
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

--changeSet OPER-5262:277 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_PART_REQUEST_DETAILS',
   'Permission to execute webapp/web/req/PartRequestDetails.jsp',
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

--changeSet OPER-5262:278 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_PART_REQUEST_SEARCH',
   'Permission to execute webapp/web/req/PartRequestSearch.jsp',
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

--changeSet OPER-5262:279 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_RESERVE_LOCAL_INVENTORY',
   'Permission to execute webapp/web/req/ReserveLocalInventory.jsp',
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

--changeSet OPER-5262:280 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_RESERVE_ON_ORDER_INVENTORY',
   'Permission to execute webapp/web/req/ReserveOnOrderInventory.jsp',
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

--changeSet OPER-5262:281 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_RESERVE_REMOTE_INVENTORY',
   'Permission to execute webapp/web/req/ReserveRemoteInventory.jsp',
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

--changeSet OPER-5262:282 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_SELECT_REQUESTED_PART',
   'Permission to execute webapp/web/req/SelectRequestedPart.jsp',
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

--changeSet OPER-5262:283 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_STEAL_REMOTE_RESERVATION',
   'Permission to execute webapp/web/req/StealRemoteReservation.jsp',
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

--changeSet OPER-5262:284 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_UNEXPECTED_TURN_INS',
   'Permission to execute webapp/web/req/UnexpectedTurnIns.jsp',
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

--changeSet OPER-5262:285 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_REQ_PARTREQUESTDETAILS_EDIT_NOTE',
   'Permission to execute webapp/web/req/partrequestdetails/EditNote.jsp',
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

--changeSet OPER-5262:286 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_ADD_MISC_RFQLINE',
   'Permission to execute webapp/web/rfq/AddMiscRFQLine.jsp',
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

--changeSet OPER-5262:287 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_CREATE_EDIT_RFQ',
   'Permission to execute webapp/web/rfq/CreateEditRFQ.jsp',
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

--changeSet OPER-5262:288 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_EDIT_RFQLINES',
   'Permission to execute webapp/web/rfq/EditRFQLines.jsp',
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

--changeSet OPER-5262:289 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_EDIT_VENDOR_QUOTES',
   'Permission to execute webapp/web/rfq/EditVendorQuotes.jsp',
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

--changeSet OPER-5262:290 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_RFQDETAILS',
   'Permission to execute webapp/web/rfq/RFQDetails.jsp',
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

--changeSet OPER-5262:291 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_RFQSEARCH',
   'Permission to execute webapp/web/rfq/RFQSearch.jsp',
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

--changeSet OPER-5262:292 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_RFQ_SELECT_PO',
   'Permission to execute webapp/web/rfq/SelectPO.jsp',
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

--changeSet OPER-5262:293 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ROLE_CREATE_EDIT_ROLE',
   'Permission to execute webapp/web/role/CreateEditRole.jsp',
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

--changeSet OPER-5262:294 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ROLE_ROLE_DETAILS',
   'Permission to execute webapp/web/role/RoleDetails.jsp',
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

--changeSet OPER-5262:295 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ROLE_ROLE_SEARCH',
   'Permission to execute webapp/web/role/RoleSearch.jsp',
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

--changeSet OPER-5262:296 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_ROLE_ROLE_SECURITY',
   'Permission to execute webapp/web/role/RoleSecurity.jsp',
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

--changeSet OPER-5262:297 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SEARCH_ADVANCED_SEARCH',
   'Permission to execute webapp/web/search/AdvancedSearch.jsp',
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

--changeSet OPER-5262:298 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_CREATE_EDIT_CAPACITY_PATTERN',
   'Permission to execute webapp/web/shift/CreateEditCapacityPattern.jsp',
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

--changeSet OPER-5262:299 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_DUPLICATE_CAPACITY_PATTERN',
   'Permission to execute webapp/web/shift/DuplicateCapacityPattern.jsp',
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

--changeSet OPER-5262:300 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_EDIT_LABOR_PROFILE',
   'Permission to execute webapp/web/shift/EditLaborProfile.jsp',
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

--changeSet OPER-5262:301 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_EDIT_LABOR_SKILLS',
   'Permission to execute webapp/web/shift/EditLaborSkills.jsp',
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

--changeSet OPER-5262:302 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_EDIT_SHIFTS_FOR_DAY',
   'Permission to execute webapp/web/shift/EditShiftsForDay.jsp',
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

--changeSet OPER-5262:303 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIFT_USERSHIFTPATTERN_CREATE_EDIT_USER_SHIFT_PATTERN',
   'Permission to execute webapp/web/shift/usershiftpattern/CreateEditUserShiftPattern.jsp',
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

--changeSet OPER-5262:304 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_ADD_SHIPMENT_LINE',
   'Permission to execute webapp/web/shipment/AddShipmentLine.jsp',
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

--changeSet OPER-5262:305 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_ASSIGN_TO_WAYBILL_GROUP',
   'Permission to execute webapp/web/shipment/AssignToWaybillGroup.jsp',
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

--changeSet OPER-5262:306 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_CREATE_EDIT_SHIPMENT',
   'Permission to execute webapp/web/shipment/CreateEditShipment.jsp',
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

--changeSet OPER-5262:307 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_EDIT_SHIPMENT_LINE_NOTES',
   'Permission to execute webapp/web/shipment/EditShipmentLineNotes.jsp',
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

--changeSet OPER-5262:308 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_INSTALL_KITS_AVAILABLE',
   'Permission to execute webapp/web/shipment/InstallKitsAvailable.jsp',
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

--changeSet OPER-5262:309 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_RECEIVE_SHIPMENT',
   'Permission to execute webapp/web/shipment/ReceiveShipment.jsp',
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

--changeSet OPER-5262:310 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_SEND_SHIPMENT',
   'Permission to execute webapp/web/shipment/SendShipment.jsp',
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

--changeSet OPER-5262:311 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_SHIPMENT_DETAILS',
   'Permission to execute webapp/web/shipment/ShipmentDetails.jsp',
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

--changeSet OPER-5262:312 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_SHIPMENT_SEARCH',
   'Permission to execute webapp/web/shipment/ShipmentSearch.jsp',
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

--changeSet OPER-5262:313 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_ROUTING_EDIT_SHIPMENT_ROUTING',
   'Permission to execute webapp/web/shipment/routing/EditShipmentRouting.jsp',
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

--changeSet OPER-5262:314 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_SHIPMENT_ROUTING_RECEIVE_SHIPMENT_SEGMENT',
   'Permission to execute webapp/web/shipment/routing/ReceiveShipmentSegment.jsp',
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

--changeSet OPER-5262:315 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_ADD_STATUS_BOARD_ASSMBL',
   'Permission to execute webapp/web/statusboard/AddStatusBoardAssmbl.jsp',
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

--changeSet OPER-5262:316 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_ADD_STATUS_BOARD_COLUMN_GROUP',
   'Permission to execute webapp/web/statusboard/AddStatusBoardColumnGroup.jsp',
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

--changeSet OPER-5262:317 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_CREATE_EDIT_STATUS_BOARD',
   'Permission to execute webapp/web/statusboard/CreateEditStatusBoard.jsp',
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

--changeSet OPER-5262:318 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_CREATE_EDIT_STATUS_BOARD_COLUMN',
   'Permission to execute webapp/web/statusboard/CreateEditStatusBoardColumn.jsp',
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

--changeSet OPER-5262:319 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_EDIT_ASSET_NOTE',
   'Permission to execute webapp/web/statusboard/EditAssetNote.jsp',
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

--changeSet OPER-5262:320 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_EDIT_STATUS_BOARD_COLUMN_ORDER',
   'Permission to execute webapp/web/statusboard/EditStatusBoardColumnOrder.jsp',
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

--changeSet OPER-5262:321 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STATUSBOARD_STATUS_BOARD_DETAILS',
   'Permission to execute webapp/web/statusboard/StatusBoardDetails.jsp',
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

--changeSet OPER-5262:322 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_ADJUST_STOCK_PERCENTAGE',
   'Permission to execute webapp/web/stock/AdjustStockPercentage.jsp',
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

--changeSet OPER-5262:323 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_CREATE_EDIT_STOCK',
   'Permission to execute webapp/web/stock/CreateEditStock.jsp',
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

--changeSet OPER-5262:324 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_EDIT_STOCK_LEVELS',
   'Permission to execute webapp/web/stock/EditStockLevels.jsp',
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

--changeSet OPER-5262:325 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_STOCK_DETAILS',
   'Permission to execute webapp/web/stock/StockDetails.jsp',
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

--changeSet OPER-5262:326 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCK_STOCK_SEARCH',
   'Permission to execute webapp/web/stock/StockSearch.jsp',
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

--changeSet OPER-5262:327 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_STOCKLEVEL_CREATE_STOCK_LEVEL',
   'Permission to execute webapp/web/stocklevel/CreateStockLevel.jsp',
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

--changeSet OPER-5262:328 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_ADD_MEASUREMENTS',
   'Permission to execute webapp/web/task/AddMeasurements.jsp',
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

--changeSet OPER-5262:329 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_ADD_STEP',
   'Permission to execute webapp/web/task/AddStep.jsp',
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

--changeSet OPER-5262:330 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_ADJUST_HOURS_FOR_BILLING',
   'Permission to execute webapp/web/task/AdjustHoursForBilling.jsp',
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

--changeSet OPER-5262:331 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CANCEL_TASKS',
   'Permission to execute webapp/web/task/CancelTasks.jsp',
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

--changeSet OPER-5262:332 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CHECK_DELAY',
   'Permission to execute webapp/web/task/CheckDelay.jsp',
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

--changeSet OPER-5262:333 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CHECK_DELAY_DETAILS',
   'Permission to execute webapp/web/task/CheckDelayDetails.jsp',
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

--changeSet OPER-5262:334 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CHECK_DETAILS',
   'Permission to execute webapp/web/task/CheckDetails.jsp',
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

--changeSet OPER-5262:335 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CHECK_SELECTION',
   'Permission to execute webapp/web/task/CheckSelection.jsp',
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

--changeSet OPER-5262:336 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_COMPLETE_TASK_SUMMARY',
   'Permission to execute webapp/web/task/CompleteTaskSummary.jsp',
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

--changeSet OPER-5262:337 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CREATE_OR_EDIT_CHECK',
   'Permission to execute webapp/web/task/CreateOrEditCheck.jsp',
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

--changeSet OPER-5262:338 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CREATE_REPETITIVE_TASK',
   'Permission to execute webapp/web/task/CreateRepetitiveTask.jsp',
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

--changeSet OPER-5262:339 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_CREATE_TASK_FROM_DEFINITION',
   'Permission to execute webapp/web/task/CreateTaskFromDefinition.jsp',
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

--changeSet OPER-5262:340 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_DEFER_TASKS',
   'Permission to execute webapp/web/task/DeferTasks.jsp',
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

--changeSet OPER-5262:341 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_ESTIMATED_END_DATE',
   'Permission to execute webapp/web/task/EditEstimatedEndDate.jsp',
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

--changeSet OPER-5262:342 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_INTERVALS',
   'Permission to execute webapp/web/task/EditIntervals.jsp',
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

--changeSet OPER-5262:343 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_MEASUREMENTS',
   'Permission to execute webapp/web/task/EditMeasurements.jsp',
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

--changeSet OPER-5262:344 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_ROLINE_NUMBERS',
   'Permission to execute webapp/web/task/EditROLineNumbers.jsp',
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

--changeSet OPER-5262:345 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_START_VALUES',
   'Permission to execute webapp/web/task/EditStartValues.jsp',
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

--changeSet OPER-5262:346 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_STEP_ORDER',
   'Permission to execute webapp/web/task/EditStepOrder.jsp',
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

--changeSet OPER-5262:347 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_STEPS',
   'Permission to execute webapp/web/task/EditSteps.jsp',
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

--changeSet OPER-5262:348 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_TASK',
   'Permission to execute webapp/web/task/EditTask.jsp',
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

--changeSet OPER-5262:349 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EDIT_WORKSCOPE_ORDER',
   'Permission to execute webapp/web/task/EditWorkscopeOrder.jsp',
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

--changeSet OPER-5262:350 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_JICS_WITH_NEWER_REV',
   'Permission to execute webapp/web/task/JicsWithNewerRev.jsp',
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

--changeSet OPER-5262:351 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_PACKAGE_AND_COMPLETE',
   'Permission to execute webapp/web/task/PackageAndComplete.jsp',
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

--changeSet OPER-5262:352 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_PREVIEW_RELEASE',
   'Permission to execute webapp/web/task/PreviewRelease.jsp',
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

--changeSet OPER-5262:353 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_SCHEDULE_CHECK',
   'Permission to execute webapp/web/task/ScheduleCheck.jsp',
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

--changeSet OPER-5262:354 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_SCHEDULE_REMOVAL',
   'Permission to execute webapp/web/task/ScheduleRemoval.jsp',
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

--changeSet OPER-5262:355 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_SELECT_IN_WORK_CHECK',
   'Permission to execute webapp/web/task/SelectInWorkCheck.jsp',
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

--changeSet OPER-5262:356 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_SET_ISSUE_TO_ACCOUNTS',
   'Permission to execute webapp/web/task/SetIssueToAccounts.jsp',
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

--changeSet OPER-5262:357 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_SET_PLAN_BY_DATE',
   'Permission to execute webapp/web/task/SetPlanByDate.jsp',
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

--changeSet OPER-5262:358 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_START_CHECK',
   'Permission to execute webapp/web/task/StartCheck.jsp',
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

--changeSet OPER-5262:359 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TASK_DETAILS',
   'Permission to execute webapp/web/task/TaskDetails.jsp',
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

--changeSet OPER-5262:360 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TASK_PRIORITIES',
   'Permission to execute webapp/web/task/TaskPriorities.jsp',
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

--changeSet OPER-5262:361 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TASK_SEARCH',
   'Permission to execute webapp/web/task/TaskSearch.jsp',
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

--changeSet OPER-5262:362 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TASK_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/task/TaskSearchByType.jsp',
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

--changeSet OPER-5262:363 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TASK_SELECTION',
   'Permission to execute webapp/web/task/TaskSelection.jsp',
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

--changeSet OPER-5262:364 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_TERMINATE_TASKS',
   'Permission to execute webapp/web/task/TerminateTasks.jsp',
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

--changeSet OPER-5262:365 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_UNDO_START',
   'Permission to execute webapp/web/task/UndoStart.jsp',
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

--changeSet OPER-5262:366 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_VIEW_OPPORTUNISTIC_TASKS',
   'Permission to execute webapp/web/task/ViewOpportunisticTasks.jsp',
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

--changeSet OPER-5262:367 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EXTENDDEADLINE_EXTEND_BLOCK_DEADLINE',
   'Permission to execute webapp/web/task/extenddeadline/ExtendBlockDeadline.jsp',
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

--changeSet OPER-5262:368 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_EXTENDDEADLINE_EXTEND_DEADLINE',
   'Permission to execute webapp/web/task/extenddeadline/ExtendDeadline.jsp',
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

--changeSet OPER-5262:369 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_INCORPORATEDTASK_INCORPORATED_TASK_SEARCH',
   'Permission to execute webapp/web/task/incorporatedtask/IncorporatedTaskSearch.jsp',
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

--changeSet OPER-5262:370 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_INVOICING_CREATE_EDIT_ESTIMATE_COST_LINE_ITEM_NOTE',
   'Permission to execute webapp/web/task/invoicing/CreateEditEstimateCostLineItemNote.jsp',
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

--changeSet OPER-5262:371 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_INVOICING_CREATE_ESTIMATE_COST_LINE_ITEM',
   'Permission to execute webapp/web/task/invoicing/CreateEstimateCostLineItem.jsp',
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

--changeSet OPER-5262:372 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_INVOICING_EDIT_ESTIMATE_COST_LINE_ITEMS',
   'Permission to execute webapp/web/task/invoicing/EditEstimateCostLineItems.jsp',
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

--changeSet OPER-5262:373 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_LABOUR_EDIT_WORK_CAPTURE',
   'Permission to execute webapp/web/task/labour/EditWorkCapture.jsp',
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

--changeSet OPER-5262:374 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASK_LABOUR_WORK_CAPTURE',
   'Permission to execute webapp/web/task/labour/WorkCapture.jsp',
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

--changeSet OPER-5262:375 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ACTIVATE_TASK_DEFINITION',
   'Permission to execute webapp/web/taskdefn/ActivateTaskDefinition.jsp',
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

--changeSet OPER-5262:376 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_ATTACHMENT',
   'Permission to execute webapp/web/taskdefn/AddAttachment.jsp',
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

--changeSet OPER-5262:377 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_EDIT_MEASURE_SPECIFIC_SCHEDULING_RULES',
   'Permission to execute webapp/web/taskdefn/AddEditMeasureSpecificSchedulingRules.jsp',
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

--changeSet OPER-5262:378 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_FOLLOWING_TASK',
   'Permission to execute webapp/web/taskdefn/AddFollowingTask.jsp',
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

--changeSet OPER-5262:379 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_JIC_TO_REQ',
   'Permission to execute webapp/web/taskdefn/AddJicToReq.jsp',
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

--changeSet OPER-5262:380 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_LABOUR_REQUIREMENT',
   'Permission to execute webapp/web/taskdefn/AddLabourRequirement.jsp',
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

--changeSet OPER-5262:381 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_LINKED_TASK',
   'Permission to execute webapp/web/taskdefn/AddLinkedTask.jsp',
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

--changeSet OPER-5262:382 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_PANEL',
   'Permission to execute webapp/web/taskdefn/AddPanel.jsp',
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

--changeSet OPER-5262:383 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_PART_REQUIREMENT',
   'Permission to execute webapp/web/taskdefn/AddPartRequirement.jsp',
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

--changeSet OPER-5262:384 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_POSSIBLE_FAULTS',
   'Permission to execute webapp/web/taskdefn/AddPossibleFaults.jsp',
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

--changeSet OPER-5262:385 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_REQ_TO_BLOCK',
   'Permission to execute webapp/web/taskdefn/AddReqToBlock.jsp',
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

--changeSet OPER-5262:386 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_SCHEDULING_RULE',
   'Permission to execute webapp/web/taskdefn/AddSchedulingRule.jsp',
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

--changeSet OPER-5262:387 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_STEP',
   'Permission to execute webapp/web/taskdefn/AddStep.jsp',
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

--changeSet OPER-5262:388 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_TECHNICAL_REFERENCE',
   'Permission to execute webapp/web/taskdefn/AddTechnicalReference.jsp',
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

--changeSet OPER-5262:389 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_TOOL_REQUIREMENT',
   'Permission to execute webapp/web/taskdefn/AddToolRequirement.jsp',
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

--changeSet OPER-5262:390 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ADD_ZONE',
   'Permission to execute webapp/web/taskdefn/AddZone.jsp',
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

--changeSet OPER-5262:391 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_BLOCK_DETAILS',
   'Permission to execute webapp/web/taskdefn/BlockDetails.jsp',
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

--changeSet OPER-5262:392 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_BLOCK_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/taskdefn/BlockSearchByType.jsp',
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

--changeSet OPER-5262:393 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_PLANNING_TYPE',
   'Permission to execute webapp/web/taskdefn/CreateEditPlanningType.jsp',
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

--changeSet OPER-5262:394 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_REF_DOC',
   'Permission to execute webapp/web/taskdefn/CreateEditRefDoc.jsp',
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

--changeSet OPER-5262:395 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_REQ',
   'Permission to execute webapp/web/taskdefn/CreateEditReq.jsp',
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

--changeSet OPER-5262:396 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_DEADLINE_EXTENSIONS',
   'Permission to execute webapp/web/taskdefn/DeadlineExtensions.jsp',
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

--changeSet OPER-5262:397 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_DISPOSITION_REF_DOCUMENT',
   'Permission to execute webapp/web/taskdefn/DispositionRefDocument.jsp',
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

--changeSet OPER-5262:398 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_DUPLICATE_TASK_DEFINITION',
   'Permission to execute webapp/web/taskdefn/DuplicateTaskDefinition.jsp',
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

--changeSet OPER-5262:399 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_LABOUR_REQUIREMENTS',
   'Permission to execute webapp/web/taskdefn/EditLabourRequirements.jsp',
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

--changeSet OPER-5262:400 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_MEASUREMENT_ORDER',
   'Permission to execute webapp/web/taskdefn/EditMeasurementOrder.jsp',
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

--changeSet OPER-5262:401 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_PART_REQUIREMENTS',
   'Permission to execute webapp/web/taskdefn/EditPartRequirements.jsp',
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

--changeSet OPER-5262:402 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_PART_SPECIFIC_SCHEDULING_RULES',
   'Permission to execute webapp/web/taskdefn/EditPartSpecificSchedulingRules.jsp',
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

--changeSet OPER-5262:403 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_PLANNING_VALUES',
   'Permission to execute webapp/web/taskdefn/EditPlanningValues.jsp',
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

--changeSet OPER-5262:404 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_SCHEDULING_RULES',
   'Permission to execute webapp/web/taskdefn/EditSchedulingRules.jsp',
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

--changeSet OPER-5262:405 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_STEP_ORDER',
   'Permission to execute webapp/web/taskdefn/EditStepOrder.jsp',
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

--changeSet OPER-5262:406 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_STEPS',
   'Permission to execute webapp/web/taskdefn/EditSteps.jsp',
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

--changeSet OPER-5262:407 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_SUBTASK_ORDER',
   'Permission to execute webapp/web/taskdefn/EditSubtaskOrder.jsp',
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

--changeSet OPER-5262:408 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_TAIL_NO_SPECIFIC_SCHED_RULES',
   'Permission to execute webapp/web/taskdefn/EditTailNoSpecificSchedRules.jsp',
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

--changeSet OPER-5262:409 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_TASK_APPLICABILITY',
   'Permission to execute webapp/web/taskdefn/EditTaskApplicability.jsp',
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

--changeSet OPER-5262:410 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_TECHNICAL_REFERENCE_ORDER',
   'Permission to execute webapp/web/taskdefn/EditTechnicalReferenceOrder.jsp',
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

--changeSet OPER-5262:411 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_TOOL_REQUIREMENTS',
   'Permission to execute webapp/web/taskdefn/EditToolRequirements.jsp',
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

--changeSet OPER-5262:412 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_EDIT_WORK_CONDITIONS',
   'Permission to execute webapp/web/taskdefn/EditWorkConditions.jsp',
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

--changeSet OPER-5262:413 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_INITIALIZE_TASK',
   'Permission to execute webapp/web/taskdefn/InitializeTask.jsp',
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

--changeSet OPER-5262:414 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_INSTALL_KITS_AVAILABLE',
   'Permission to execute webapp/web/taskdefn/InstallKitsAvailable.jsp',
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

--changeSet OPER-5262:415 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_ISSUE_TEMPORARY_REVISION',
   'Permission to execute webapp/web/taskdefn/IssueTemporaryRevision.jsp',
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

--changeSet OPER-5262:416 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_JICDETAILS',
   'Permission to execute webapp/web/taskdefn/JICDetails.jsp',
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

--changeSet OPER-5262:417 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_JOB_CARD_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/taskdefn/JobCardSearchByType.jsp',
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

--changeSet OPER-5262:418 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_MOVE_TASK_DEFINITION',
   'Permission to execute webapp/web/taskdefn/MoveTaskDefinition.jsp',
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

--changeSet OPER-5262:419 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_MPCDETAILS',
   'Permission to execute webapp/web/taskdefn/MPCDetails.jsp',
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

--changeSet OPER-5262:420 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_MPCSEARCH_BY_TYPE',
   'Permission to execute webapp/web/taskdefn/MPCSearchByType.jsp',
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

--changeSet OPER-5262:421 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_OBSOLETE_TASK_DEFN',
   'Permission to execute webapp/web/taskdefn/ObsoleteTaskDefn.jsp',
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

--changeSet OPER-5262:422 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_PREVENT_ALLOW_REQ',
   'Permission to execute webapp/web/taskdefn/PreventAllowReq.jsp',
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

--changeSet OPER-5262:423 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REF_DOC_DETAILS',
   'Permission to execute webapp/web/taskdefn/RefDocDetails.jsp',
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

--changeSet OPER-5262:424 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REF_DOC_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/taskdefn/RefDocSearchByType.jsp',
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

--changeSet OPER-5262:425 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REQ_DETAILS',
   'Permission to execute webapp/web/taskdefn/ReqDetails.jsp',
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

--changeSet OPER-5262:426 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REQ_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/taskdefn/ReqSearchByType.jsp',
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

--changeSet OPER-5262:427 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_SELECT_TASK_DEFINITION',
   'Permission to execute webapp/web/taskdefn/SelectTaskDefinition.jsp',
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

--changeSet OPER-5262:428 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_SET_PLAN_BY_DATES',
   'Permission to execute webapp/web/taskdefn/SetPlanByDates.jsp',
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

--changeSet OPER-5262:429 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_SHOW_HIDE_DIFFERENCES',
   'Permission to execute webapp/web/taskdefn/ShowHideDifferences.jsp',
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

--changeSet OPER-5262:430 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASK_DEFINITION_SEARCH',
   'Permission to execute webapp/web/taskdefn/TaskDefinitionSearch.jsp',
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

--changeSet OPER-5262:431 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASK_EXTEND_DEADLINE',
   'Permission to execute webapp/web/taskdefn/TaskExtendDeadline.jsp',
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

--changeSet OPER-5262:432 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASK_LABOUR_SUMMARY',
   'Permission to execute webapp/web/taskdefn/TaskLabourSummary.jsp',
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

--changeSet OPER-5262:433 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_COMMON_VIEW_DOCUMENT_CHAIN',
   'Permission to execute webapp/web/taskdefn/common/ViewDocumentChain.jsp',
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

--changeSet OPER-5262:434 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_ADD_ADVISORY',
   'Permission to execute webapp/web/taskdefn/reqdetails/AddAdvisory.jsp',
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

--changeSet OPER-5262:435 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_ADD_WEIGHT_AND_BALANCE',
   'Permission to execute webapp/web/taskdefn/reqdetails/AddWeightAndBalance.jsp',
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

--changeSet OPER-5262:436 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_EDIT_WEIGHT_AND_BALANCE',
   'Permission to execute webapp/web/taskdefn/reqdetails/EditWeightAndBalance.jsp',
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

--changeSet OPER-5262:437 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_APPROVE_TASK_DEFN',
   'Permission to execute webapp/web/taskdefn/taskdefnapproval/ApproveTaskDefn.jsp',
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

--changeSet OPER-5262:438 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_REJECT_TASK_DEFN',
   'Permission to execute webapp/web/taskdefn/taskdefnapproval/RejectTaskDefn.jsp',
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

--changeSet OPER-5262:439 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_REQUEST_RESTART_APPROVAL',
   'Permission to execute webapp/web/taskdefn/taskdefnapproval/RequestRestartApproval.jsp',
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

--changeSet OPER-5262:440 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_ASSIGN_TO_DO_BUTTON',
   'Permission to execute webapp/web/todolist/AssignToDoButton.jsp',
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

--changeSet OPER-5262:441 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_ASSIGN_TO_DO_TAB',
   'Permission to execute webapp/web/todolist/AssignToDoTab.jsp',
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

--changeSet OPER-5262:442 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_CREATE_EDIT_TO_DO_LIST',
   'Permission to execute webapp/web/todolist/CreateEditToDoList.jsp',
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

--changeSet OPER-5262:443 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_EDIT_TO_DO_BUTTON_ORDER',
   'Permission to execute webapp/web/todolist/EditToDoButtonOrder.jsp',
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

--changeSet OPER-5262:444 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_EDIT_TO_DO_TAB_ORDER',
   'Permission to execute webapp/web/todolist/EditToDoTabOrder.jsp',
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

--changeSet OPER-5262:445 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_HR_SHIFT_ASSIGNMENT',
   'Permission to execute webapp/web/todolist/HrShiftAssignment.jsp',
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

--changeSet OPER-5262:446 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TODOLIST_TO_DO_LIST_DETAILS',
   'Permission to execute webapp/web/todolist/ToDoListDetails.jsp',
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

--changeSet OPER-5262:447 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TOOL_TOOL_CHECKOUT',
   'Permission to execute webapp/web/tool/ToolCheckout.jsp',
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

--changeSet OPER-5262:448 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_COMPLETE_PUT_AWAY',
   'Permission to execute webapp/web/transfer/CompletePutAway.jsp',
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

--changeSet OPER-5262:449 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_COMPLETE_TURN_IN',
   'Permission to execute webapp/web/transfer/CompleteTurnIn.jsp',
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

--changeSet OPER-5262:450 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_CREATE_PUT_AWAY',
   'Permission to execute webapp/web/transfer/CreatePutAway.jsp',
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

--changeSet OPER-5262:451 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_CREATE_TRANSFER',
   'Permission to execute webapp/web/transfer/CreateTransfer.jsp',
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

--changeSet OPER-5262:452 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_TRANSFER_DETAILS',
   'Permission to execute webapp/web/transfer/TransferDetails.jsp',
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

--changeSet OPER-5262:453 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_TRANSFER_SEARCH',
   'Permission to execute webapp/web/transfer/TransferSearch.jsp',
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

--changeSet OPER-5262:454 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_TRANSFER_TURN_IN',
   'Permission to execute webapp/web/transfer/TurnIn.jsp',
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

--changeSet OPER-5262:455 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGE_CREATE_USAGE_RECORD',
   'Permission to execute webapp/web/usage/CreateUsageRecord.jsp',
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

--changeSet OPER-5262:456 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGE_EDIT_USAGE_SNAPSHOT',
   'Permission to execute webapp/web/usage/EditUsageSnapshot.jsp',
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

--changeSet OPER-5262:457 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGE_USAGE_RECORD_DETAILS',
   'Permission to execute webapp/web/usage/UsageRecordDetails.jsp',
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

--changeSet OPER-5262:458 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGEDEFN_CREATE_USAGE_DEFINITION',
   'Permission to execute webapp/web/usagedefn/CreateUsageDefinition.jsp',
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

--changeSet OPER-5262:459 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGEDEFN_REORDER_USAGE_PARMS',
   'Permission to execute webapp/web/usagedefn/ReorderUsageParms.jsp',
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

--changeSet OPER-5262:460 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGEDEFN_USAGE_DEFINITION_DETAILS',
   'Permission to execute webapp/web/usagedefn/UsageDefinitionDetails.jsp',
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

--changeSet OPER-5262:461 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USAGEDEFN_USAGEPARM_USAGE_PARAMETER_DETAILS',
   'Permission to execute webapp/web/usagedefn/usageparm/UsageParameterDetails.jsp',
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

--changeSet OPER-5262:462 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_ADD_TIME_OFF',
   'Permission to execute webapp/web/user/AddTimeOff.jsp',
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

--changeSet OPER-5262:463 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_ADD_USER_ATTACHMENT',
   'Permission to execute webapp/web/user/AddUserAttachment.jsp',
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

--changeSet OPER-5262:464 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_ASSIGN_LICENSE',
   'Permission to execute webapp/web/user/AssignLicense.jsp',
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

--changeSet OPER-5262:465 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_ASSIGN_ORGANIZATION_TO_USERS',
   'Permission to execute webapp/web/user/AssignOrganizationToUsers.jsp',
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

--changeSet OPER-5262:466 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_ASSIGN_SKILL',
   'Permission to execute webapp/web/user/AssignSkill.jsp',
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

--changeSet OPER-5262:467 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_CREATE_EDIT_USER',
   'Permission to execute webapp/web/user/CreateEditUser.jsp',
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

--changeSet OPER-5262:468 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_EDIT_POAUTHORIZATION_LEVELS',
   'Permission to execute webapp/web/user/EditPOAuthorizationLevels.jsp',
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

--changeSet OPER-5262:469 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_EDIT_ROLE_ORDER',
   'Permission to execute webapp/web/user/EditRoleOrder.jsp',
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

--changeSet OPER-5262:470 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_EDIT_TIME_OFF',
   'Permission to execute webapp/web/user/EditTimeOff.jsp',
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

--changeSet OPER-5262:471 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_SUSPEND_LICENSE',
   'Permission to execute webapp/web/user/SuspendLicense.jsp',
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

--changeSet OPER-5262:472 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_UN_SUSPEND_LICENSE',
   'Permission to execute webapp/web/user/UnSuspendLicense.jsp',
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

--changeSet OPER-5262:473 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_UPDATE_LICENSE',
   'Permission to execute webapp/web/user/UpdateLicense.jsp',
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

--changeSet OPER-5262:474 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_USER_DETAILS',
   'Permission to execute webapp/web/user/UserDetails.jsp',
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

--changeSet OPER-5262:475 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_USER_USER_SEARCH',
   'Permission to execute webapp/web/user/UserSearch.jsp',
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

--changeSet OPER-5262:476 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_ADD_ACCOUNT',
   'Permission to execute webapp/web/vendor/AddAccount.jsp',
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

--changeSet OPER-5262:477 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_CHANGE_ORDER_TYPE_STATUS',
   'Permission to execute webapp/web/vendor/ChangeOrderTypeStatus.jsp',
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

--changeSet OPER-5262:478 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_CREATE_EDIT_VENDOR',
   'Permission to execute webapp/web/vendor/CreateEditVendor.jsp',
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

--changeSet OPER-5262:479 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_VENDOR_DETAILS',
   'Permission to execute webapp/web/vendor/VendorDetails.jsp',
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

--changeSet OPER-5262:480 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_VENDOR_PART_PRICE_DETAILS',
   'Permission to execute webapp/web/vendor/VendorPartPriceDetails.jsp',
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

--changeSet OPER-5262:481 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_VENDOR_SEARCH',
   'Permission to execute webapp/web/vendor/VendorSearch.jsp',
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

--changeSet OPER-5262:482 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_VENDOR_VENDOR_SEARCH_BY_TYPE',
   'Permission to execute webapp/web/vendor/VendorSearchByType.jsp',
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

--changeSet OPER-5262:483 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WARRANTY_CREATE_EDIT_WARRANTY_CONTRACT',
   'Permission to execute webapp/web/warranty/CreateEditWarrantyContract.jsp',
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

--changeSet OPER-5262:484 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WARRANTY_WARRANTY_CONTRACT_DETAILS',
   'Permission to execute webapp/web/warranty/WarrantyContractDetails.jsp',
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

--changeSet OPER-5262:485 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WARRANTY_WARRANTY_CONTRACT_SEARCH',
   'Permission to execute webapp/web/warranty/WarrantyContractSearch.jsp',
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

--changeSet OPER-5262:486 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_APPROVAL_LEVEL_DETAILS',
   'Permission to execute webapp/web/workflow/ApprovalLevelDetails.jsp',
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

--changeSet OPER-5262:487 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_APP_WORKFLOW_DEFN_DETAILS',
   'Permission to execute webapp/web/workflow/AppWorkflowDefnDetails.jsp',
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

--changeSet OPER-5262:488 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_CREATE_EDIT_APPROVAL_LEVEL',
   'Permission to execute webapp/web/workflow/CreateEditApprovalLevel.jsp',
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

--changeSet OPER-5262:489 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_CREATE_EDIT_APP_WORKFLOW_DEFN',
   'Permission to execute webapp/web/workflow/CreateEditAppWorkflowDefn.jsp',
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

--changeSet OPER-5262:490 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_EDIT_APPROVAL_LEVEL_ORDER',
   'Permission to execute webapp/web/workflow/EditApprovalLevelOrder.jsp',
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

--changeSet OPER-5262:491 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JSP_WEB_WORKFLOW_WORKFLOWDEFNDETAILS_ASSIGN_APPROVAL_LEVEL',
   'Permission to execute webapp/web/workflow/workflowdefndetails/AssignApprovalLevel.jsp',
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