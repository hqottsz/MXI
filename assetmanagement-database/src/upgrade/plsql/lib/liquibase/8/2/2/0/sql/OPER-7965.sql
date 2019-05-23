--liquibase formatted sql


--changeSet OPER-7965:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN
   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ACTION_WEB_VIEW_ACTION',
   'Permission to execute com.mxi.mx.web.servlet.action.WebViewActionServlet servlet',
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

--changeSet OPER-7965:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ADVISORY_ADD_EDIT_ADVISORY',
   'Permission to execute com.mxi.mx.web.servlet.advisory.AddEditAdvisory servlet',
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

--changeSet OPER-7965:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ALERT_WEB_ALERT',
   'Permission to execute com.mxi.mx.web.servlet.alert.WebAlert servlet',
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

--changeSet OPER-7965:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ATTACH_VIEW_ATTACHMENT',
   'Permission to execute com.mxi.mx.web.servlet.attach.ViewAttachment servlet',
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

--changeSet OPER-7965:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_EDIT_ASSIGNED_PART_DETAILS',
   'Permission to execute com.mxi.mx.web.servlet.bom.EditAssignedPartDetails servlet',
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

--changeSet OPER-7965:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_CREATE_EDIT_CONFIG_SLOT',
   'Permission to execute com.mxi.mx.web.servlet.bom.CreateEditConfigSlot servlet',
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

--changeSet OPER-7965:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_ASSIGN_USAGE_PARAMETER',
   'Permission to execute com.mxi.mx.web.servlet.bom.AssignUsageParameter servlet',
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

--changeSet OPER-7965:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_EDIT_CONFIG_SLOT_PARENT',
   'Permission to execute com.mxi.mx.web.servlet.bom.EditConfigSlotParent servlet',
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

--changeSet OPER-7965:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_CREATE_EDIT_ASSEMBLY',
   'Permission to execute com.mxi.mx.web.servlet.bom.CreateEditAssembly servlet',
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

--changeSet OPER-7965:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_EDIT_PART_GROUP_APPLICABILITY',
   'Permission to execute com.mxi.mx.web.servlet.bom.EditPartGroupApplicability servlet',
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

--changeSet OPER-7965:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_BOM_EDIT_POSITIONS',
   'Permission to execute com.mxi.mx.web.servlet.bom.EditPositions servlet',
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

--changeSet OPER-7965:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CALC_EDIT_CONSTANTS',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.calc.EditConstants servlet',
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

--changeSet OPER-7965:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CALC_EDIT_SYMBOLS',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.calc.EditSymbols servlet',
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

--changeSet OPER-7965:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CALC_EDIT_PART_SPEC_CONSTANTS',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.calc.EditPartSpecConstants servlet',
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

--changeSet OPER-7965:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CALC_ADD_CONSTANT',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.calc.AddConstant servlet',
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

--changeSet OPER-7965:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CAPABILITY_DELETE_CAPABILITY',
   'Permission to execute com.mxi.mx.web.servlet.capability.DeleteCapability servlet',
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

--changeSet OPER-7965:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CAPABILITY_CREATE_EDIT_CAPABILITY',
   'Permission to execute com.mxi.mx.web.servlet.capability.CreateEditCapability servlet',
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

--changeSet OPER-7965:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CLAIM_CLOSE_CLAIM',
   'Permission to execute com.mxi.mx.web.servlet.claim.CloseClaim servlet',
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

--changeSet OPER-7965:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CLAIM_EDIT_CLAIM_LABOR_LINE_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.claim.EditClaimLaborLineNote servlet',
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

--changeSet OPER-7965:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CLAIM_CREATE_EDIT_CLAIM',
   'Permission to execute com.mxi.mx.web.servlet.claim.CreateEditClaim servlet',
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

--changeSet OPER-7965:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CLAIM_CANCEL_CLAIM',
   'Permission to execute com.mxi.mx.web.servlet.claim.CancelClaim servlet',
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

--changeSet OPER-7965:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_COMMON_MESSAGE',
   'Permission to execute com.mxi.mx.web.servlet.common.Message servlet',
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

--changeSet OPER-7965:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CONFIGUTILS_DELETE',
   'Permission to execute com.mxi.mx.web.servlet.configutils.DeleteServlet servlet',
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

--changeSet OPER-7965:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CONFIGUTILS_LAUNCH',
   'Permission to execute com.mxi.mx.web.servlet.configutils.LaunchServlet servlet',
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

--changeSet OPER-7965:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CYCLECOUNT_RECOUNT_RESULTS',
   'Permission to execute com.mxi.mx.web.servlet.cyclecount.RecountResults servlet',
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

--changeSet OPER-7965:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_CYCLECOUNT_LOAD_CYCLE_COUNT_RESULTS',
   'Permission to execute com.mxi.mx.web.servlet.cyclecount.LoadCycleCountResults servlet',
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

--changeSet OPER-7965:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_DAO_QUERY',
   'Permission to execute com.mxi.mx.web.servlet.dao.QueryServlet servlet',
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

--changeSet OPER-7965:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_DBRULECHECKER_VALIDATE_SQL',
   'Permission to execute com.mxi.mx.web.servlet.dbrulechecker.ValidateSql servlet',
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

--changeSet OPER-7965:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_DEPARTMENT_CREATE_EDIT_DEPARTMENT',
   'Permission to execute com.mxi.mx.web.servlet.department.CreateEditDepartment servlet',
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

--changeSet OPER-7965:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ENTITY_ENTITY_REDIRECTOR',
   'Permission to execute com.mxi.mx.web.servlet.entity.EntityRedirector servlet',
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

--changeSet OPER-7965:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ESIGNER_ELECTRONIC_SIGNATURE',
   'Permission to execute com.mxi.mx.web.servlet.esigner.ElectronicSignature servlet',
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

--changeSet OPER-7965:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_ADD_EVENT_TECH_REF',
   'Permission to execute com.mxi.mx.web.servlet.event.AddEventTechRef servlet',
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

--changeSet OPER-7965:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_CREATE_EDIT_EVENT_DATA',
   'Permission to execute com.mxi.mx.web.servlet.event.CreateEditEventData servlet',
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

--changeSet OPER-7965:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_SCAN_BARCODE',
   'Permission to execute com.mxi.mx.web.servlet.event.ScanBarcode servlet',
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

--changeSet OPER-7965:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_CREATE_EDIT_EVENT',
   'Permission to execute com.mxi.mx.web.servlet.event.CreateEditEvent servlet',
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

--changeSet OPER-7965:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_EVENT_DETAILS_REDIRECTOR',
   'Permission to execute com.mxi.mx.web.servlet.event.EventDetailsRedirector servlet',
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

--changeSet OPER-7965:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_CANCEL_TASKS',
   'Permission to execute com.mxi.mx.web.servlet.event.CancelTasks servlet',
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

--changeSet OPER-7965:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_EVENT_REMOVE_EVENT_TECH_REF',
   'Permission to execute com.mxi.mx.web.servlet.event.RemoveEventTechRef servlet',
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

--changeSet OPER-7965:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAILEFFECT_CREATE_EDIT_FAIL_EFFECT',
   'Permission to execute com.mxi.mx.web.servlet.faileffect.CreateEditFailEffect servlet',
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

--changeSet OPER-7965:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_REASSIGN_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.fault.ReassignFault servlet',
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

--changeSet OPER-7965:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_CREATE_EDIT_FAULT_DEFER_REF',
   'Permission to execute com.mxi.mx.web.servlet.fault.CreateEditFaultDeferRef servlet',
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

--changeSet OPER-7965:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_EDIT_RESULT_EVENT',
   'Permission to execute com.mxi.mx.web.servlet.fault.EditResultEvent servlet',
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

--changeSet OPER-7965:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_MERGE_POSSIBLE_FAULTS',
   'Permission to execute com.mxi.mx.web.servlet.fault.MergePossibleFaults servlet',
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

--changeSet OPER-7965:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_CALC_FAULT_PRIORITY',
   'Permission to execute com.mxi.mx.web.servlet.fault.CalcFaultPriority servlet',
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

--changeSet OPER-7965:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_MARK_FAULT_AS_NO_FAULT_FOUND',
   'Permission to execute com.mxi.mx.web.servlet.fault.MarkFaultAsNoFaultFound servlet',
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

--changeSet OPER-7965:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_POCKET_RAISE_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.fault.PocketRaiseFault servlet',
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

--changeSet OPER-7965:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_RAISE_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.fault.RaiseFault servlet',
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

--changeSet OPER-7965:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_EDIT_POSSIBLE_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.fault.EditPossibleFault servlet',
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

--changeSet OPER-7965:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULT_CREATE_EDIT_FAULT_THRESHOLD',
   'Permission to execute com.mxi.mx.web.servlet.fault.CreateEditFaultThreshold servlet',
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

--changeSet OPER-7965:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULTDEFN_CREATE_EDIT_FAULT_DEFINITION',
   'Permission to execute com.mxi.mx.web.servlet.faultdefn.CreateEditFaultDefinition servlet',
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

--changeSet OPER-7965:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FAULTDEFN_FAULT_DEFERRAL_DATA',
   'Permission to execute com.mxi.mx.web.servlet.faultdefn.FaultDeferralData servlet',
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

--changeSet OPER-7965:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FLIGHT_ADD_FLIGHT_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.flight.AddFlightNote servlet',
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

--changeSet OPER-7965:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_FLIGHT_ABSTRACT_FLIGHT',
   'Permission to execute com.mxi.mx.web.servlet.flight.AbstractFlightServlet servlet',
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

--changeSet OPER-7965:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_IETM_CREATE_EDIT_IETM',
   'Permission to execute com.mxi.mx.web.servlet.ietm.CreateEditIetm servlet',
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

--changeSet OPER-7965:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_IETM_VIEW_HYPER_LINK',
   'Permission to execute com.mxi.mx.web.servlet.ietm.ViewHyperLink servlet',
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

--changeSet OPER-7965:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INCLUDE_GET_PICKED_ITEMS_INFO',
   'Permission to execute com.mxi.mx.web.servlet.include.GetPickedItemsInfo servlet',
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

--changeSet OPER-7965:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INCLUDE_GET_PICKED_ITEMS_WARNINGS',
   'Permission to execute com.mxi.mx.web.servlet.include.GetPickedItemsWarnings servlet',
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

--changeSet OPER-7965:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INCLUDE_PART_NO_DATA_FOR_BULK_CREATE_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.include.PartNoDataForBulkCreateInventory servlet',
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

--changeSet OPER-7965:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_GENERATE_SERIAL_NUMBER',
   'Permission to execute com.mxi.mx.web.servlet.inventory.GenerateSerialNumber servlet',
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

--changeSet OPER-7965:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_MARK_INVENTORY_AS_REPAIR_REQUIRED',
   'Permission to execute com.mxi.mx.web.servlet.inventory.MarkInventoryAsRepairRequired servlet',
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

--changeSet OPER-7965:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_ADD_INVENTORY_TECH_REF',
   'Permission to execute com.mxi.mx.web.servlet.inventory.AddInventoryTechRef servlet',
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

--changeSet OPER-7965:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_MARK_INVENTORY_AS_INSP_REQUIRED',
   'Permission to execute com.mxi.mx.web.servlet.inventory.MarkInventoryAsInspRequired servlet',
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

--changeSet OPER-7965:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_CHANGE_CUSTODY',
   'Permission to execute com.mxi.mx.web.servlet.inventory.ChangeCustody servlet',
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

--changeSet OPER-7965:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_PREVENT_LINE_PLANNING_AUTOMATION_AIRCRAFT',
   'Permission to execute com.mxi.mx.web.servlet.inventory.PreventLinePlanningAutomationAircraft servlet',
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

--changeSet OPER-7965:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_ISSUE_INVENTORY_TO_NEW_REQUEST',
   'Permission to execute com.mxi.mx.web.servlet.inventory.IssueInventoryToNewRequest servlet',
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

--changeSet OPER-7965:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_RE_INDUCT_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.inventory.ReInductInventory servlet',
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

--changeSet OPER-7965:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_ISSUE_INVENTORY_TO_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.inventory.IssueInventoryToFault servlet',
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

--changeSet OPER-7965:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_ISSUE_INVENTORY_TO_TASK',
   'Permission to execute com.mxi.mx.web.servlet.inventory.IssueInventoryToTask servlet',
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

--changeSet OPER-7965:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_INVENTORY_SEARCH_REDIRECTOR',
   'Permission to execute com.mxi.mx.web.servlet.inventory.InventorySearchRedirector servlet',
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

--changeSet OPER-7965:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVENTORY_TRANSFER_TO_QUARANTINE',
   'Permission to execute com.mxi.mx.web.servlet.inventory.TransferToQuarantine servlet',
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

--changeSet OPER-7965:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVOICING_LOCK_ESTIMATE_COST_LINE_ITEM',
   'Permission to execute com.mxi.mx.web.servlet.task.invoicing.LockEstimateCostLineItem servlet',
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

--changeSet OPER-7965:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVOICING_EDIT_ESTIMATE_COST_LINE_ITEM',
   'Permission to execute com.mxi.mx.web.servlet.task.invoicing.EditEstimateCostLineItem servlet',
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

--changeSet OPER-7965:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVOICING_REMOVE_ESTIMATE_COST_LINE_ITEM',
   'Permission to execute com.mxi.mx.web.servlet.task.invoicing.RemoveEstimateCostLineItem servlet',
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

--changeSet OPER-7965:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVOICING_CREATE_EDIT_ESTIMATE_COST_LINE_ITEM_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.task.invoicing.CreateEditEstimateCostLineItemNote servlet',
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

--changeSet OPER-7965:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_INVOICING_CREATE_ESTIMATE_COST_LINE_ITEM',
   'Permission to execute com.mxi.mx.web.servlet.task.invoicing.CreateEstimateCostLineItem servlet',
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

--changeSet OPER-7965:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JASPER_DIRECT_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.jasper.DirectReportServlet servlet',
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

--changeSet OPER-7965:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JASPER_EMBEDDED_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.jasper.EmbeddedReportServlet servlet',
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

--changeSet OPER-7965:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JASPER_EXPORT_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.jasper.ExportReportServlet servlet',
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

--changeSet OPER-7965:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_JASPER_SSO_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.jasper.SsoReportServlet servlet',
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

--changeSet OPER-7965:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_KIT_VALIDATE_KIT_BARCODE',
   'Permission to execute com.mxi.mx.web.servlet.kit.ValidateKitBarcode servlet',
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

--changeSet OPER-7965:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LABOUR_REMOVE_LABOUR_FROM_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.labour.RemoveLabourFromTask servlet',
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

--changeSet OPER-7965:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LABOUR_ADD_LABOUR_TO_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.labour.AddLabourToTask servlet',
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

--changeSet OPER-7965:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_ASSIGN_REPAIRABLE_PART',
   'Permission to execute com.mxi.mx.web.servlet.location.AssignRepairablePart servlet',
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

--changeSet OPER-7965:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_CREATE_PRINTER',
   'Permission to execute com.mxi.mx.web.servlet.location.CreatePrinter servlet',
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

--changeSet OPER-7965:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_UNASSIGN_DEPARTMENT',
   'Permission to execute com.mxi.mx.web.servlet.location.UnassignDepartment servlet',
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

--changeSet OPER-7965:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_EDIT_CONTACT',
   'Permission to execute com.mxi.mx.web.servlet.location.EditContact servlet',
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

--changeSet OPER-7965:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_UNMARK_AS_SUPPLY',
   'Permission to execute com.mxi.mx.web.servlet.location.UnmarkAsSupply servlet',
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

--changeSet OPER-7965:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_MARK_AS_SUPPLY',
   'Permission to execute com.mxi.mx.web.servlet.location.MarkAsSupply servlet',
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

--changeSet OPER-7965:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_ASSIGN_PARENT_LOCATION',
   'Permission to execute com.mxi.mx.web.servlet.location.AssignParentLocation servlet',
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

--changeSet OPER-7965:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_ASSIGN_DEPARTMENT',
   'Permission to execute com.mxi.mx.web.servlet.location.AssignDepartment servlet',
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

--changeSet OPER-7965:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_MARK_AS_DEFAULT_DOCK',
   'Permission to execute com.mxi.mx.web.servlet.location.MarkAsDefaultDock servlet',
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

--changeSet OPER-7965:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_CREATE_CONTACT',
   'Permission to execute com.mxi.mx.web.servlet.location.CreateContact servlet',
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

--changeSet OPER-7965:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_EDIT_PRINTER',
   'Permission to execute com.mxi.mx.web.servlet.location.EditPrinter servlet',
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

--changeSet OPER-7965:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_EDIT_CAPACITY',
   'Permission to execute com.mxi.mx.web.servlet.location.EditCapacity servlet',
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

--changeSet OPER-7965:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LOCATION_ASSIGN_CAPABILITY',
   'Permission to execute com.mxi.mx.web.servlet.location.AssignCapability servlet',
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

--changeSet OPER-7965:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LPA_RUN_LPA',
   'Permission to execute com.mxi.mx.web.servlet.lpa.RunLPA servlet',
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

--changeSet OPER-7965:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LPA_MARK_AS_RESOLVED',
   'Permission to execute com.mxi.mx.web.servlet.lpa.MarkAsResolved servlet',
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

--changeSet OPER-7965:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LPA_COMPLETE_LPAANALYSIS',
   'Permission to execute com.mxi.mx.web.servlet.lpa.CompleteLPAAnalysis servlet',
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

--changeSet OPER-7965:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_LPA_LPASTATUS',
   'Permission to execute com.mxi.mx.web.servlet.lpa.LPAStatus servlet',
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

--changeSet OPER-7965:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_ASSIGN_REQ_INFO',
   'Permission to execute com.mxi.mx.web.servlet.maint.AssignReqInfo servlet',
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

--changeSet OPER-7965:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_EDIT_REASONS_NOTES',
   'Permission to execute com.mxi.mx.web.servlet.maint.EditReasonsNotes servlet',
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

--changeSet OPER-7965:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_UNASSIGN_REQ',
   'Permission to execute com.mxi.mx.web.servlet.maint.UnassignReq servlet',
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

--changeSet OPER-7965:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_DELETE_MAINT_PROGRAM',
   'Permission to execute com.mxi.mx.web.servlet.maint.DeleteMaintProgram servlet',
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

--changeSet OPER-7965:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_ACTIVATE_MAINT_PROGRAM',
   'Permission to execute com.mxi.mx.web.servlet.maint.ActivateMaintProgram servlet',
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

--changeSet OPER-7965:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MAINT_REVERT_TO_PREVIOUS_REVISION',
   'Permission to execute com.mxi.mx.web.servlet.maint.RevertToPreviousRevision servlet',
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

--changeSet OPER-7965:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MANUFACTURER_CREATE_EDIT_MANUFACTURER',
   'Permission to execute com.mxi.mx.web.servlet.manufacturer.CreateEditManufacturer servlet',
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

--changeSet OPER-7965:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MEASUREMENTS_ASSIGN_MEASUREMENT',
   'Permission to execute com.mxi.mx.web.servlet.assembly.measurements.AssignMeasurement servlet',
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

--changeSet OPER-7965:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MEASUREMENTS_CREATE_EDIT_MEASUREMENT',
   'Permission to execute com.mxi.mx.web.servlet.assembly.measurements.CreateEditMeasurement servlet',
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

--changeSet OPER-7965:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_MEASUREMENTS_REORDER_MEASUREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.assembly.measurements.ReorderMeasurements servlet',
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

--changeSet OPER-7965:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_OILCONSUMPTION_EDIT_SERIAL_NO_SPECIFIC_THRESHOLD',
   'Permission to execute com.mxi.mx.web.servlet.assembly.oilconsumption.EditSerialNoSpecificThreshold servlet',
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

--changeSet OPER-7965:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ORG_UNASSIGN_ORG_SKILL',
   'Permission to execute com.mxi.mx.web.servlet.org.UnassignOrgSkill servlet',
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

--changeSet OPER-7965:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ORG_CREATE_EDIT_SKILL',
   'Permission to execute com.mxi.mx.web.servlet.org.CreateEditSkill servlet',
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

--changeSet OPER-7965:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ORG_CREATE_EDIT_ORGANIZATION',
   'Permission to execute com.mxi.mx.web.servlet.org.CreateEditOrganization servlet',
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

--changeSet OPER-7965:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_ORG_ASSIGN_ORG_SKILL',
   'Permission to execute com.mxi.mx.web.servlet.org.AssignOrgSkill servlet',
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

--changeSet OPER-7965:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_OWNER_CREAT_EDIT_OWNER',
   'Permission to execute com.mxi.mx.web.servlet.owner.CreatEditOwner servlet',
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

--changeSet OPER-7965:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_ADD_EDIT_EXCHANGE_VENDOR_PART',
   'Permission to execute com.mxi.mx.web.servlet.part.AddEditExchangeVendorPart servlet',
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

--changeSet OPER-7965:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_VIEW_PURCHASE_VENDOR_PRICE_DETAILS',
   'Permission to execute com.mxi.mx.web.servlet.part.ViewPurchaseVendorPriceDetails servlet',
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

--changeSet OPER-7965:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_ADD_PART_ATTACHMENT',
   'Permission to execute com.mxi.mx.web.servlet.part.AddPartAttachment servlet',
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

--changeSet OPER-7965:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_REMOVE_PART_REQUIREMENT',
   'Permission to execute com.mxi.mx.web.servlet.part.RemovePartRequirement servlet',
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

--changeSet OPER-7965:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_CREATE_PART_FOR_TASK',
   'Permission to execute com.mxi.mx.web.servlet.part.CreatePartForTask servlet',
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

--changeSet OPER-7965:121 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_APPROVAL_TOGGLE',
   'Permission to execute com.mxi.mx.web.servlet.part.ApprovalToggleServlet servlet',
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

--changeSet OPER-7965:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_RECEIVING_NOTES',
   'Permission to execute com.mxi.mx.web.servlet.part.ReceivingNotes servlet',
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

--changeSet OPER-7965:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_EDIT_PURCHASE_VENDOR',
   'Permission to execute com.mxi.mx.web.servlet.part.EditPurchaseVendor servlet',
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

--changeSet OPER-7965:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_ADD_PART_KIT_CONTENTS',
   'Permission to execute com.mxi.mx.web.servlet.part.AddPartKitContents servlet',
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

--changeSet OPER-7965:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_EDIT_UNIT_OF_MEASURE',
   'Permission to execute com.mxi.mx.web.servlet.part.EditUnitOfMeasure servlet',
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

--changeSet OPER-7965:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_EDIT_REPAIR_VENDOR',
   'Permission to execute com.mxi.mx.web.servlet.part.EditRepairVendor servlet',
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

--changeSet OPER-7965:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_EDIT_PART_REQUIREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.part.EditPartRequirements servlet',
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

--changeSet OPER-7965:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_EDIT_BIN_LEVELS',
   'Permission to execute com.mxi.mx.web.servlet.part.EditBinLevels servlet',
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

--changeSet OPER-7965:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_CREATE_EDIT_PART',
   'Permission to execute com.mxi.mx.web.servlet.part.CreateEditPart servlet',
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

--changeSet OPER-7965:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PART_REMOVE_VENDOR',
   'Permission to execute com.mxi.mx.web.servlet.part.RemoveVendor servlet',
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

--changeSet OPER-7965:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_TASK_DEFINITION_CARDS_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateTaskDefinitionCardsPdf servlet',
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

--changeSet OPER-7965:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_ISSUE_TICKET_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintIssueTicketPdf servlet',
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

--changeSet OPER-7965:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_ABSTRACT_GENERATE_PART_TAG_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.AbstractGeneratePartTagPdf servlet',
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

--changeSet OPER-7965:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_SHIPPING_FORM_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateShippingFormPdf servlet',
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

--changeSet OPER-7965:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_CONDEMNED_TAG_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateCondemnedTagPdf servlet',
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

--changeSet OPER-7965:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_PUT_AWAY_TICKET_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintPutAwayTicketPdf servlet',
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

--changeSet OPER-7965:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_RFQPDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintRFQPdf servlet',
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

--changeSet OPER-7965:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_PLAN_COMPARISON_REPORT_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GeneratePlanComparisonReportPdf servlet',
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

--changeSet OPER-7965:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_ABSTRACT_GENERATE_TASK_CARD_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.AbstractGenerateTaskCardPdf servlet',
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

--changeSet OPER-7965:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_ISSUE_LIST_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintIssueListPdf servlet',
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

--changeSet OPER-7965:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_VIEW_ESIG_DOCUMENT',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.ViewEsigDocument servlet',
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

--changeSet OPER-7965:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_UNSERVICEABLE_TAG_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateUnserviceableTagPdf servlet',
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

--changeSet OPER-7965:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_SERVICEABLE_TAG_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateServiceableTagPdf servlet',
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

--changeSet OPER-7965:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_TRANSFER_TICKET_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintTransferTicketPdf servlet',
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

--changeSet OPER-7965:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_VENDOR_RELIABILITY_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintVendorReliabilityPdf servlet',
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

--changeSet OPER-7965:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_ABSTRACT_GENERATE_LICENSE_CARD_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.AbstractGenerateLicenseCardPdf servlet',
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

--changeSet OPER-7965:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_PURCHASE_ORDER_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GeneratePurchaseOrderPdf servlet',
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

--changeSet OPER-7965:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_REPAIR_OR_CONVERT_REPAIR_ORDER_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateRepairOrConvertRepairOrderPdf servlet',
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

--changeSet OPER-7965:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_QUARANTINE_TAG_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintQuarantineTagPdf servlet',
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

--changeSet OPER-7965:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_GENERATE_TALLY_SHEET_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.GenerateTallySheetPdf servlet',
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

--changeSet OPER-7965:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PDF_PRINT_PICK_LIST_PDF',
   'Permission to execute com.mxi.mx.web.servlet.report.pdf.PrintPickListPdf servlet',
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

--changeSet OPER-7965:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PERMMATRIX_PERM_MATRIX_SECURITY',
   'Permission to execute com.mxi.mx.web.servlet.permmatrix.PermMatrixSecurity servlet',
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

--changeSet OPER-7965:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PI_EDIT_POINVOICE_LINE_NOTES',
   'Permission to execute com.mxi.mx.web.servlet.pi.EditPOInvoiceLineNotes servlet',
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

--changeSet OPER-7965:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PI_ADD_MISC_POINVOICE_LINE',
   'Permission to execute com.mxi.mx.web.servlet.pi.AddMiscPOInvoiceLine servlet',
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

--changeSet OPER-7965:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PICK_GET_KIT_PICKED_ITEMS_INFO',
   'Permission to execute com.mxi.mx.web.servlet.kit.pick.GetKitPickedItemsInfo servlet',
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

--changeSet OPER-7965:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PLANNINGVIEWER_LOAD_PLANNING_VIEWER',
   'Permission to execute com.mxi.mx.web.servlet.planningviewer.LoadPlanningViewer servlet',
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

--changeSet OPER-7965:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PLANNINGVIEWER_TRIGGER_UPDATE_PREDICTED_DEADLINE_DATE',
   'Permission to execute com.mxi.mx.web.servlet.planningviewer.TriggerUpdatePredictedDeadlineDate servlet',
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

--changeSet OPER-7965:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PO_EDIT_PURCHASE_ORDER_LINE_RECEIVER_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.po.EditPurchaseOrderLineReceiverNote servlet',
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

--changeSet OPER-7965:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PO_EDIT_PURCHASE_ORDER_LINE_VENDOR_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.po.EditPurchaseOrderLineVendorNote servlet',
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

--changeSet OPER-7965:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PORTABLE_FOUND_UNEXPECTED_PART',
   'Permission to execute com.mxi.mx.web.servlet.cyclecount.portable.FoundUnexpectedPart servlet',
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

--changeSet OPER-7965:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PORTABLE_FOUND_UNEXPECTED_PART_ACTION',
   'Permission to execute com.mxi.mx.web.servlet.cyclecount.portable.FoundUnexpectedPartAction servlet',
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

--changeSet OPER-7965:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PORTABLE_ITEM_RECOUNT',
   'Permission to execute com.mxi.mx.web.servlet.cyclecount.portable.ItemRecount servlet',
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

--changeSet OPER-7965:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PROCUREMENT_TAX_CHARGE',
   'Permission to execute com.mxi.mx.web.servlet.procurement.TaxChargeServlet servlet',
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

--changeSet OPER-7965:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PROCUREMENT_EDIT_VENDOR_TAX_CHARGE',
   'Permission to execute com.mxi.mx.web.servlet.procurement.EditVendorTaxCharge servlet',
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

--changeSet OPER-7965:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_PROCUREMENT_ADD_REMOVE_TAX_CHARGE_POINVOICE_RFQ',
   'Permission to execute com.mxi.mx.web.servlet.procurement.AddRemoveTaxChargePOInvoiceRFQ servlet',
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

--changeSet OPER-7965:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_RENAME_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.RenameReportServlet servlet',
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

--changeSet OPER-7965:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_EMBEDDED_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.EmbeddedReportServlet servlet',
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

--changeSet OPER-7965:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_EXPORT_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.ExportReportServlet servlet',
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

--changeSet OPER-7965:169 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_GENERATE_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.GenerateReportServlet servlet',
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

--changeSet OPER-7965:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_ABSTRACT_PRINT_TASK_CARDS',
   'Permission to execute com.mxi.mx.web.servlet.report.AbstractPrintTaskCards servlet',
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

--changeSet OPER-7965:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_GENERATE_PLAN_COMPARISON_REPORT',
   'Permission to execute com.mxi.mx.web.servlet.report.GeneratePlanComparisonReport servlet',
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

--changeSet OPER-7965:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_ABSTRACT_PRINT_LICENSE_CARDS',
   'Permission to execute com.mxi.mx.web.servlet.report.AbstractPrintLicenseCards servlet',
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

--changeSet OPER-7965:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_ABSTRACT_PREVIEW_TASK_CARDS',
   'Permission to execute com.mxi.mx.web.servlet.report.AbstractPreviewTaskCards servlet',
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

--changeSet OPER-7965:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REPORT_SCHEDULE_REPORT',
   'Permission to execute com.mxi.mx.common.servlet.report.ScheduleReportServlet servlet',
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

--changeSet OPER-7965:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_ISSUE_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.req.IssueInventory servlet',
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

--changeSet OPER-7965:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_CREATE_PART_REQUEST_FOR_TASK',
   'Permission to execute com.mxi.mx.web.servlet.req.CreatePartRequestForTask servlet',
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

--changeSet OPER-7965:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_RESERVE_ON_ORDER_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.req.ReserveOnOrderInventory servlet',
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

--changeSet OPER-7965:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_RESERVE_LOCAL_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.req.ReserveLocalInventory servlet',
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

--changeSet OPER-7965:179 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_CREATE_PURCHASE_REQUESTS_FOR_PART_REQUESTS',
   'Permission to execute com.mxi.mx.web.servlet.req.CreatePurchaseRequestsForPartRequests servlet',
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

--changeSet OPER-7965:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_CREATE_PART_REQUEST',
   'Permission to execute com.mxi.mx.web.servlet.req.CreatePartRequest servlet',
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

--changeSet OPER-7965:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_REQ_VALIDATE_ISSUE_BARCODE',
   'Permission to execute com.mxi.mx.web.servlet.req.ValidateIssueBarcode servlet',
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

--changeSet OPER-7965:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_RFQ_SELECT_ALTERNATE_PART_FOR_QUOTE',
   'Permission to execute com.mxi.mx.web.servlet.rfq.SelectAlternatePartForQuote servlet',
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

--changeSet OPER-7965:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_RFQ_REMOVE_RFQLINES',
   'Permission to execute com.mxi.mx.web.servlet.rfq.RemoveRFQLines servlet',
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

--changeSet OPER-7965:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SEARCH_ADVANCED_SEARCH',
   'Permission to execute com.mxi.mx.web.servlet.search.AdvancedSearch servlet',
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

--changeSet OPER-7965:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SERVLET_LOGIC',
   'Permission to execute com.mxi.mx.web.servlet.LogicServlet servlet',
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

--changeSet OPER-7965:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SERVLET_VALIDATING_LOGIC',
   'Permission to execute com.mxi.mx.web.servlet.ValidatingLogicServlet servlet',
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

--changeSet OPER-7965:187 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIFT_DUPLICATE_CAPACITY_PATTERN',
   'Permission to execute com.mxi.mx.web.servlet.shift.DuplicateCapacityPattern servlet',
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

--changeSet OPER-7965:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIFT_CREATE_EDIT_CAPACITY_PATTERN',
   'Permission to execute com.mxi.mx.web.servlet.shift.CreateEditCapacityPattern servlet',
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

--changeSet OPER-7965:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIFT_CAPACITY_PATTERN_DETAILS',
   'Permission to execute com.mxi.mx.web.servlet.shift.CapacityPatternDetails servlet',
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

--changeSet OPER-7965:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIPMENT_GET_SUB_KIT_PART_DETAILS',
   'Permission to execute com.mxi.mx.web.servlet.shipment.GetSubKitPartDetails servlet',
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

--changeSet OPER-7965:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIPMENT_PICK_SHIPMENT',
   'Permission to execute com.mxi.mx.web.servlet.shipment.PickShipment servlet',
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

--changeSet OPER-7965:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SHIPMENT_EDIT_SHIPMENT_LINE_NOTES',
   'Permission to execute com.mxi.mx.web.servlet.shipment.EditShipmentLineNotes servlet',
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

--changeSet OPER-7965:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SSO_BUSINESS_INTELLIGENCE_REDIRECT',
   'Permission to execute com.mxi.mx.web.servlet.sso.BusinessIntelligenceRedirect servlet',
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

--changeSet OPER-7965:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SSO_VALIDATE_TICKET',
   'Permission to execute com.mxi.mx.web.servlet.sso.ValidateTicket servlet',
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

--changeSet OPER-7965:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_STATIONMONITORING_STATION_MONITORING',
   'Permission to execute com.mxi.mx.web.servlet.stationmonitoring.StationMonitoringServlet servlet',
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

--changeSet OPER-7965:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_STOCK_ADJUST_STOCK_PERCENTAGE',
   'Permission to execute com.mxi.mx.web.servlet.stock.AdjustStockPercentage servlet',
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

--changeSet OPER-7965:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_SUBTYPES_GET_SUBTYPE_BY_ASSEMBLY_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.assembly.subtypes.GetSubtypeByAssemblyAjax servlet',
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

--changeSet OPER-7965:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_GET_VENDOR_BY_LOCATION_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.task.GetVendorByLocationAjax servlet',
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

--changeSet OPER-7965:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_COMMIT_WORKSCOPE',
   'Permission to execute com.mxi.mx.web.servlet.task.CommitWorkscope servlet',
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

--changeSet OPER-7965:200 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_UNASSIGN_DO_AT_NEXT_INSTALL',
   'Permission to execute com.mxi.mx.web.servlet.task.UnassignDoAtNextInstall servlet',
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

--changeSet OPER-7965:201 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_TASK_PRIORITIES',
   'Permission to execute com.mxi.mx.web.servlet.task.TaskPriorities servlet',
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

--changeSet OPER-7965:202 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_EDIT_CHECK',
   'Permission to execute com.mxi.mx.web.servlet.task.EditCheck servlet',
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

--changeSet OPER-7965:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_ADD_TASK_ATTACHMENT',
   'Permission to execute com.mxi.mx.web.servlet.task.AddTaskAttachment servlet',
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

--changeSet OPER-7965:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_GET_CURR_USAGE_ASYNC',
   'Permission to execute com.mxi.mx.web.servlet.task.GetCurrUsageAsync servlet',
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

--changeSet OPER-7965:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_PART_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.task.PartNote servlet',
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

--changeSet OPER-7965:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_HANDLING_MOVE_SELECTED_ITEMS',
   'Permission to execute com.mxi.mx.web.servlet.task.HandlingMoveSelectedItems servlet',
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

--changeSet OPER-7965:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_ADD_MEASUREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.task.AddMeasurements servlet',
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

--changeSet OPER-7965:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_DEFER_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.DeferTask servlet',
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

--changeSet OPER-7965:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_ADD_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.task.AddNote servlet',
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

--changeSet OPER-7965:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_PREVENT_LINE_PLANNING_AUTOMATION_WORK_PACKAGE',
   'Permission to execute com.mxi.mx.web.servlet.task.PreventLinePlanningAutomationWorkPackage servlet',
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

--changeSet OPER-7965:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_ADD_DEADLINE_VALIDATE_ASYNC',
   'Permission to execute com.mxi.mx.web.servlet.task.AddDeadlineValidateAsync servlet',
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

--changeSet OPER-7965:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_UNCOMMIT_WORKSCOPE',
   'Permission to execute com.mxi.mx.web.servlet.task.UncommitWorkscope servlet',
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

--changeSet OPER-7965:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_PACKAGE_AND_COMPLETE',
   'Permission to execute com.mxi.mx.web.servlet.task.PackageAndComplete servlet',
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

--changeSet OPER-7965:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_CANCEL_PARTS_FROM_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.CancelPartsFromTask servlet',
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

--changeSet OPER-7965:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_SAVE_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.SaveTask servlet',
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

--changeSet OPER-7965:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_CREATE_NEW_CHECK',
   'Permission to execute com.mxi.mx.web.servlet.task.CreateNewCheck servlet',
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

--changeSet OPER-7965:217 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_CONFIRM_CORRECTIVE_ACTION',
   'Permission to execute com.mxi.mx.web.servlet.task.ConfirmCorrectiveAction servlet',
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

--changeSet OPER-7965:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_EDIT_MEASUREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.task.EditMeasurements servlet',
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

--changeSet OPER-7965:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_UNDO_START',
   'Permission to execute com.mxi.mx.web.servlet.task.UndoStart servlet',
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

--changeSet OPER-7965:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_SCHED_ADD_ZONE',
   'Permission to execute com.mxi.mx.web.servlet.task.SchedAddZone servlet',
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

--changeSet OPER-7965:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_ROLINE_NUMBERS',
   'Permission to execute com.mxi.mx.web.servlet.task.ROLineNumbers servlet',
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

--changeSet OPER-7965:222 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_UNASSIGN_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.UnassignTask servlet',
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

--changeSet OPER-7965:223 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_EDIT_TASK_ATTACHMENT',
   'Permission to execute com.mxi.mx.web.servlet.task.EditTaskAttachment servlet',
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

--changeSet OPER-7965:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_REMOVE_MEASUREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.task.RemoveMeasurements servlet',
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

--changeSet OPER-7965:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_SET_PARTS_TOOLS_STATUS',
   'Permission to execute com.mxi.mx.web.servlet.task.SetPartsToolsStatus servlet',
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

--changeSet OPER-7965:226 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_REMOVE_PARTS_FROM_TASK',
   'Permission to execute com.mxi.mx.web.servlet.task.RemovePartsFromTask servlet',
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

--changeSet OPER-7965:227 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_FIND_PREDICTED_DEADLINE',
   'Permission to execute com.mxi.mx.web.servlet.task.FindPredictedDeadline servlet',
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

--changeSet OPER-7965:228 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_CREATE_REPLACEMENT_TASK_FOR_INVENTORY',
   'Permission to execute com.mxi.mx.web.servlet.task.CreateReplacementTaskForInventory servlet',
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

--changeSet OPER-7965:229 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_CANCEL_TASKS',
   'Permission to execute com.mxi.mx.web.servlet.task.CancelTasks servlet',
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

--changeSet OPER-7965:230 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASK_SCHED_ADD_PANEL',
   'Permission to execute com.mxi.mx.web.servlet.task.SchedAddPanel servlet',
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

--changeSet OPER-7965:231 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_REQ_MAP',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditReqMap servlet',
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

--changeSet OPER-7965:232 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_PART_TRANSFORMATION',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemovePartTransformation servlet',
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

--changeSet OPER-7965:233 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_STEPS',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditSteps servlet',
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

--changeSet OPER-7965:234 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_REF_DOC_DATA',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditRefDocData servlet',
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

--changeSet OPER-7965:235 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_POSSIBLE_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemovePossibleFault servlet',
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

--changeSet OPER-7965:236 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_TASK_DEFN_TECH_REF',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddTaskDefnTechRef servlet',
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

--changeSet OPER-7965:237 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_TASK_DEFN_TECH_REF_ORDER',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditTaskDefnTechRefOrder servlet',
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

--changeSet OPER-7965:238 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_ZONE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveZone servlet',
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

--changeSet OPER-7965:239 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_REQ',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditReq servlet',
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

--changeSet OPER-7965:240 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_PART_SPECIFIC_SCHEDULING_RULE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemovePartSpecificSchedulingRule servlet',
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

--changeSet OPER-7965:241 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_JIC_TO_REQ_INFO',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddJicToReqInfo servlet',
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

--changeSet OPER-7965:242 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_SUBTASK_ORDER',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditSubtaskOrder servlet',
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

--changeSet OPER-7965:243 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_TASK_DEFN_ATTACHMENT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddTaskDefnAttachment servlet',
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

--changeSet OPER-7965:244 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_MEASUREMENT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveMeasurement servlet',
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

--changeSet OPER-7965:245 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_WEIGHT_AND_BALANCE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddWeightAndBalance servlet',
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

--changeSet OPER-7965:246 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_TASK_DEFINITION_REDIRECTOR',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.TaskDefinitionRedirector servlet',
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

--changeSet OPER-7965:247 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_BLOCK',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditBlock servlet',
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

--changeSet OPER-7965:248 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_WEIGHT_AND_BALANCE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveWeightAndBalance servlet',
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

--changeSet OPER-7965:249 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REJECT_TASK_DEFN',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RejectTaskDefn servlet',
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

--changeSet OPER-7965:250 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_PANEL',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddPanel servlet',
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

--changeSet OPER-7965:251 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_TASK_APPLICABILITY',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditTaskApplicability servlet',
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

--changeSet OPER-7965:252 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_TASK_DEFN_ATTACHMENT_ORDER',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditTaskDefnAttachmentOrder servlet',
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

--changeSet OPER-7965:253 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_APPROVE_TASKDEFN',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.ApproveTaskdefn servlet',
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

--changeSet OPER-7965:254 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_STEP_ORDER',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditStepOrder servlet',
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

--changeSet OPER-7965:255 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_LINKED_TASK',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddLinkedTask servlet',
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

--changeSet OPER-7965:256 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_TOOL_REQUIREMENT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddToolRequirement servlet',
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

--changeSet OPER-7965:257 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_MEASUREMENT_ORDER',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditMeasurementOrder servlet',
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

--changeSet OPER-7965:258 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_LINKED_TASK',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveLinkedTask servlet',
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

--changeSet OPER-7965:259 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_MEASURE_SPECIFIC_SCHEDULING_RULES',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveMeasureSpecificSchedulingRules servlet',
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

--changeSet OPER-7965:260 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_LABOUR_REQUIREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditLabourRequirements servlet',
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

--changeSet OPER-7965:261 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_VALIDATE_ADD_EDIT_MEASUREMENT_DATA',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.ValidateAddEditMeasurementData servlet',
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

--changeSet OPER-7965:262 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_POSSIBLE_FAULT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddPossibleFault servlet',
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

--changeSet OPER-7965:263 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REQUEST_RESTART_APPROVAL',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RequestRestartApproval servlet',
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

--changeSet OPER-7965:264 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_VERIFY_APPLICABILITY_RULE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.VerifyApplicabilityRule servlet',
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

--changeSet OPER-7965:265 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_PART_SPECIFIC_SCHEDULING_RULES',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditPartSpecificSchedulingRules servlet',
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

--changeSet OPER-7965:266 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_REQ_DATA',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditReqData servlet',
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

--changeSet OPER-7965:267 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_WEIGHT_AND_BALANCE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditWeightAndBalance servlet',
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

--changeSet OPER-7965:268 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_EDIT_MEASURE_SPECIFIC_SCHEDULING_RULES',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddEditMeasureSpecificSchedulingRules servlet',
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

--changeSet OPER-7965:269 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_SCHEDULING_RULES',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditSchedulingRules servlet',
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

--changeSet OPER-7965:270 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_LABOUR_REQUIREMENT',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddLabourRequirement servlet',
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

--changeSet OPER-7965:271 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_TOOL_REQUIREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditToolRequirements servlet',
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

--changeSet OPER-7965:272 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_PART_REQUIREMENTS',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditPartRequirements servlet',
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

--changeSet OPER-7965:273 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_STEP',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddStep servlet',
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

--changeSet OPER-7965:274 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_JOB_CARD',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditJobCard servlet',
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

--changeSet OPER-7965:275 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_ZONE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddZone servlet',
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

--changeSet OPER-7965:276 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_VERIFY_APPLICABILITY_RULE_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.VerifyApplicabilityRuleAjax servlet',
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

--changeSet OPER-7965:277 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_EDIT_PLANNING_VALUES_DATA',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.EditPlanningValuesData servlet',
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

--changeSet OPER-7965:278 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_ADD_JIC_TO_REQ',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.AddJicToReq servlet',
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

--changeSet OPER-7965:279 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_REMOVE_SCHEDULING_RULE',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.RemoveSchedulingRule servlet',
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

--changeSet OPER-7965:280 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TASKDEFN_CREATE_EDIT_BLOCK_DATA',
   'Permission to execute com.mxi.mx.web.servlet.taskdefn.CreateEditBlockData servlet',
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

--changeSet OPER-7965:281 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TIMEZONE_GET_TIME_ZONE_BY_KEY_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.timezone.GetTimeZoneByKeyAjax servlet',
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

--changeSet OPER-7965:282 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TIMEZONE_GET_TIME_ZONE_BY_LOCATION_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.timezone.GetTimeZoneByLocationAjax servlet',
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

--changeSet OPER-7965:283 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TIMEZONE_GET_TIME_ZONE_BY_DATE_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.timezone.GetTimeZoneByDateAjax servlet',
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

--changeSet OPER-7965:284 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TIMEZONE_GET_DATE_TIME_FOR_TIME_ZONE_AJAX',
   'Permission to execute com.mxi.mx.web.servlet.timezone.GetDateTimeForTimeZoneAjax servlet',
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

--changeSet OPER-7965:285 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TODOLIST_EDIT_TO_BE_RETURNED_PART',
   'Permission to execute com.mxi.mx.web.servlet.todolist.EditToBeReturnedPart servlet',
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

--changeSet OPER-7965:286 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TODOLIST_INCOMPLETE_KITS_TAB',
   'Permission to execute com.mxi.mx.web.servlet.todolist.IncompleteKitsTab servlet',
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

--changeSet OPER-7965:287 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TRANSFER_INV_TURN_IN_DATA',
   'Permission to execute com.mxi.mx.web.servlet.transfer.InvTurnInData servlet',
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

--changeSet OPER-7965:288 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_TRANSFER_COMPLETE_PUT_AWAY',
   'Permission to execute com.mxi.mx.web.servlet.transfer.CompletePutAway servlet',
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

--changeSet OPER-7965:289 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USAGE_CREATE_USAGE_RECORD',
   'Permission to execute com.mxi.mx.web.servlet.usage.CreateUsageRecord servlet',
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

--changeSet OPER-7965:290 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USAGEDEFN_CREATE_EDIT_USAGE_DEFINITION',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.CreateEditUsageDefinition servlet',
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

--changeSet OPER-7965:291 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USAGEDEFN_REORDER_USAGE_PARMS',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.ReorderUsageParms servlet',
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

--changeSet OPER-7965:292 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USAGEPARM_ASSIGN_DATA_VALUE',
   'Permission to execute com.mxi.mx.web.servlet.usagedefn.usageparm.AssignDataValue servlet',
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

--changeSet OPER-7965:293 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_SUSPEND_LICENSE',
   'Permission to execute com.mxi.mx.web.servlet.user.SuspendLicense servlet',
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

--changeSet OPER-7965:294 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_REMOVE_TIME_OFF',
   'Permission to execute com.mxi.mx.web.servlet.user.RemoveTimeOff servlet',
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

--changeSet OPER-7965:295 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_EDIT_TIME_OFF',
   'Permission to execute com.mxi.mx.web.servlet.user.EditTimeOff servlet',
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

--changeSet OPER-7965:296 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_REMOVE_USER_SHIFTS',
   'Permission to execute com.mxi.mx.web.servlet.user.RemoveUserShifts servlet',
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

--changeSet OPER-7965:297 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_EDIT_USER_LICENSE',
   'Permission to execute com.mxi.mx.web.servlet.user.EditUserLicense servlet',
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

--changeSet OPER-7965:298 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_UN_SUSPEND_LICENSE',
   'Permission to execute com.mxi.mx.web.servlet.user.UnSuspendLicense servlet',
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

--changeSet OPER-7965:299 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_USER_PKI_XML',
   'Permission to execute com.mxi.mx.web.servlet.user.UserPkiXml servlet',
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

--changeSet OPER-7965:300 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USER_ADD_TIME_OFF',
   'Permission to execute com.mxi.mx.web.servlet.user.AddTimeOff servlet',
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

--changeSet OPER-7965:301 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USERSHIFTPATTERN_REMOVE_USER_SHIFT_PATTERN_DAY_SHIFTS',
   'Permission to execute com.mxi.mx.web.servlet.shift.usershiftpattern.RemoveUserShiftPatternDayShifts servlet',
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

--changeSet OPER-7965:302 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USERSHIFTPATTERN_EDIT_USER_SHIFT_PATTERN_DAY_SHIFTS',
   'Permission to execute com.mxi.mx.web.servlet.shift.usershiftpattern.EditUserShiftPatternDayShifts servlet',
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

--changeSet OPER-7965:303 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_USERSHIFTPATTERN_ADD_USER_SHIFT_PATTERN_DAY_SHIFT',
   'Permission to execute com.mxi.mx.web.servlet.shift.usershiftpattern.AddUserShiftPatternDayShift servlet',
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

--changeSet OPER-7965:304 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_VENDOR_SET_DEFAULT_VENDOR_AIRPORT',
   'Permission to execute com.mxi.mx.web.servlet.vendor.SetDefaultVendorAirport servlet',
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

--changeSet OPER-7965:305 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_VENDOR_UNASSIGN_VENDOR_AIRPORT',
   'Permission to execute com.mxi.mx.web.servlet.vendor.UnassignVendorAirport servlet',
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

--changeSet OPER-7965:306 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_VENDOR_GENERATE_VENDOR_CODE',
   'Permission to execute com.mxi.mx.web.servlet.vendor.GenerateVendorCode servlet',
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

--changeSet OPER-7965:307 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_VENDOR_ASSIGN_VENDOR_AIRPORT',
   'Permission to execute com.mxi.mx.web.servlet.vendor.AssignVendorAirport servlet',
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

--changeSet OPER-7965:308 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_WARRANTY_WARRANTY_TYPE',
   'Permission to execute com.mxi.mx.web.servlet.warranty.WarrantyType servlet',
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

--changeSet OPER-7965:309 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_WARRANTY_INIT_EDIT_WARRANTY',
   'Permission to execute com.mxi.mx.web.servlet.warranty.InitEditWarranty servlet',
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

--changeSet OPER-7965:310 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_WARRANTY_CREATE_EDIT_WARRANTY_CONTRACT',
   'Permission to execute com.mxi.mx.web.servlet.warranty.CreateEditWarrantyContract servlet',
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

--changeSet OPER-7965:311 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_WORKFLOW_CREATE_EDIT_APPROVAL_LEVEL',
   'Permission to execute com.mxi.mx.web.servlet.workflow.CreateEditApprovalLevel servlet',
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

--changeSet OPER-7965:312 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_QUARANTINE_ADD_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.common.AddNotes servlet',
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

--changeSet OPER-7965:313 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add action parameter
BEGIN

   utl_migr_data_pkg.action_parm_insert(
   'ACTION_QUARANTINE_ADD_ACTION_NOTE',
   'Permission to execute com.mxi.mx.web.servlet.common.AddNotes servlet',
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