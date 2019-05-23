--liquibase formatted sql


--changeSet DEV-2421:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*************** Report Engine Settings *************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.KitAssemblyTicket',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.KitAssemblyTicket report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*************** Report Engine Path Settings *************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'esignature.SignatureCorrection',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/esignature/SignatureCorrection',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.CondemnedPartTag',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/CondemnedPartTag',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.DetailInvFncLog',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/DetailInvFncLog',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.InspReqServiceablePartTag',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/InspReqServiceablePartTag',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.IssuedCsgnInventory',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/IssuedCsgnInventory',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.KitAssemblyTicket',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/KitAssemblyTicket',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.NoInspReqServiceablePartTag',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/NoInspReqServiceablePartTag',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.RotableAdjustment',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/RotableAdjustment',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.SummaryInvFncLog',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/SummaryInvFncLog',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'inventory.UnserviceablePartTag',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/inventory/UnserviceablePartTag',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'license.LicenseCard',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/license/LicenseCard',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'location.LabourSummary',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/location/LabourSummary',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'lpr.PlanComparisionReport',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/lpr/PlanComparisionReport',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgramDiff',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgramDiff',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgramImpact',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgramImpact',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgram',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgram',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'oilconsumption.OilConsumptionReport',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/oilconsumption/OilConsumptionReport',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'po.PurchaseOrder',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/po/PurchaseOrder',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'po.RepairOrConvertedRepairOrder',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/po/RepairOrConvertedRepairOrder',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'rfq.RFQ',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/rfq/RFQ',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'shipment.PickList',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/shipment/PickList',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'shipment.ShippingForm',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/shipment/ShippingForm',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'tag.TagTaskDefinitions',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/tag/TagTaskDefinitions',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.FoundFault',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/FoundFault',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.MaintenanceRelease',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/MaintenanceRelease',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.OrderedTallySheet',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/OrderedTallySheet',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.TallySheet',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/TallySheet',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.TaskCard',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/TaskCard',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'task.WorkCapture',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/task/WorkCapture',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.ReqImpactReport',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/taskdefinition/ReqImpactReport',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.TaskDefnCard',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/taskdefinition/TaskDefnCard',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'transfer.IssueList',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/transfer/IssueList',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'transfer.IssueTicket',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/transfer/IssueTicket',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'transfer.PutAwayTicket',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/transfer/PutAwayTicket',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'transfer.TransferTicket',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/transfer/TransferTicket',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.DepartmentMemberList',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/DepartmentMemberList',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionAccessRole',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/FunctionAccessRole',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionAccess',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/FunctionAccess',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionActionAccessRole',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/FunctionActionAccessRole',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionActionAccess',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/FunctionActionAccess',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.SystemInformation',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/SystemInformation',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.UIandLogicSettings',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/UIandLogicSettings',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.UserAction',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/UserAction',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.UserDepartmentList',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/UserDepartmentList',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'utility.UserRole',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/utility/UserRole',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/

--changeSet DEV-2421:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'vendor.VendorReliability',
      'REPORT_PATH_MAPPING',
      'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
      'GLOBAL',
      'Use path from the report properties in Jasper',
      '/organizations/Maintenix/Reports/Core/vendor/VendorReliability',
      0,
      'Report Parameter',
      '8.1',
      0      
   );
END;
/