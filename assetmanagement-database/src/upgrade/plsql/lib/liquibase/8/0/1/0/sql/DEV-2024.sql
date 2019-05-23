--liquibase formatted sql


--changeSet DEV-2024:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Report engine name configuration
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'DEFAULT_REPORT_ENGINE',
      'REPORT_ENGINE',
      'Default report engine name',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'esignature.SignatureCorrection',
      'REPORT_ENGINE',
      'Report engine name for generating esignature.SignatureCorrection report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.InspReqServiceablePartTag',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.InspReqServiceablePartTag report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.CondemnedPartTag',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.CondemnedPartTag report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.UnserviceablePartTag',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.UnserviceablePartTag report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.RotableAdjustment',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.RotableAdjustment report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.SummaryInvFncLog',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.SummaryInvFncLog report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.DetailInvFncLog',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.DetailInvFncLog report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.IssuedCsgnInventory',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.IssuedCsgnInventory report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'inventory.NoInspReqServiceablePartTag',
      'REPORT_ENGINE',
      'Report engine name for generating inventory.NoInspReqServiceablePartTag report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'license.LicenseCard',
      'REPORT_ENGINE',
      'Report engine name for generating license.LicenseCard report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'location.LabourSummary',
      'REPORT_ENGINE',
      'Report engine name for generating location.LabourSummary report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'lpr.PlanComparisionReport',
      'REPORT_ENGINE',
      'Report engine name for generating lpr.PlanComparisionReport report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgram',
      'REPORT_ENGINE',
      'Report engine name for generating maintprgm.MaintenanceProgram report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgramDiff',
      'REPORT_ENGINE',
      'Report engine name for generating maintprgm.MaintenanceProgramDiff report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'maintprgm.MaintenanceProgramImpact',
      'REPORT_ENGINE',
      'Report engine name for generating maintprgm.MaintenanceProgramImpact report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'oilconsumption.OilConsumptionReport',
      'REPORT_ENGINE',
      'Report engine name for generating oilconsumption.OilConsumptionReport report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'po.PurchaseOrder',
      'REPORT_ENGINE',
      'Report engine name for generating po.PurchaseOrder report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'po.RepairOrConvertedRepairOrder',
      'REPORT_ENGINE',
      'Report engine name for generating po.RepairOrConvertedRepairOrder report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'rfq.RFQ',
      'REPORT_ENGINE',
      'Report engine name for generating rfq.RFQ report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'shipment.PickList',
      'REPORT_ENGINE',
      'Report engine name for generating shipment.PickList report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'shipment.ShippingForm',
      'REPORT_ENGINE',
      'Report engine name for generating shipment.ShippingForm report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'tag.TagTaskDefinitions',
      'REPORT_ENGINE',
      'Report engine name for generating tag.TagTaskDefinitions report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'task.TaskCard',
      'REPORT_ENGINE',
      'Report engine name for generating task.TaskCard report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'task.FoundFault',
      'REPORT_ENGINE',
      'Report engine name for generating task.FoundFault report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );


   utl_migr_data_pkg.config_parm_insert(
      'task.OrderedTallySheet',
      'REPORT_ENGINE',
      'Report engine name for generating task.OrderedTallySheet report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );
   

   utl_migr_data_pkg.config_parm_insert(
      'task.TallySheet',
      'REPORT_ENGINE',
      'Report engine name for generating task.TallySheet report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'task.WorkCapture',
      'REPORT_ENGINE',
      'Report engine name for generating task.WorkCapture report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'task.MaintenanceRelease',
      'REPORT_ENGINE',
      'Report engine name for generating task.MaintenanceRelease report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.ReqImpactReport',
      'REPORT_ENGINE',
      'Report engine name for generating taskdefinition.ReqImpactReport report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'taskdefinition.TaskDefnCard',
      'REPORT_ENGINE',
      'Report engine name for generating taskdefinition.TaskDefnCard report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'transfer.IssueList',
      'REPORT_ENGINE',
      'Report engine name for generating transfer.IssueList report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'transfer.IssueTicket',
      'REPORT_ENGINE',
      'Report engine name for generating transfer.IssueTicket report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'transfer.PutAwayTicket',
      'REPORT_ENGINE',
      'Report engine name for generating transfer.PutAwayTicket report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'transfer.TransferTicket',
      'REPORT_ENGINE',
      'Report engine name for generating transfer.TransferTicket report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.DepartmentMemberList',
      'REPORT_ENGINE',
      'Report engine name for generating utility.DepartmentMemberList report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.UserAction',
      'REPORT_ENGINE',
      'Report engine name for generating utility.UserAction report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.UserDepartmentList',
      'REPORT_ENGINE',
      'Report engine name for generating utility.UserDepartmentList report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.UserRole',
      'REPORT_ENGINE',
      'Report engine name for generating utility.UserRole report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.SystemInformation',
      'REPORT_ENGINE',
      'Report engine name for generating utility.SystemInformation report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.UIandLogicSettings',
      'REPORT_ENGINE',
      'Report engine name for generating utility.UIandLogicSettings report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionAccess',
      'REPORT_ENGINE',
      'Report engine name for generating utility.FunctionAccess report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionAccessRole',
      'REPORT_ENGINE',
      'Report engine name for generating utility.FunctionAccessRole report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionActionAccess',
      'REPORT_ENGINE',
      'Report engine name for generating utility.FunctionActionAccess report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'utility.FunctionActionAccessRole',
      'REPORT_ENGINE',
      'Report engine name for generating utility.FunctionActionAccessRole report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );

   utl_migr_data_pkg.config_parm_insert(
      'vendor.VendorReliability',
      'REPORT_ENGINE',
      'Report engine name for generating vendor.VendorReliability report',
      'GLOBAL',
      'BOE, JASPER_REST',
      'BOE',
      0,
      'Report Parameter',
      '8.0-SP2',
      0
   );
   
END;
/