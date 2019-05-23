--liquibase formatted sql


--changeSet DEV-2516:1 stripComments:false
-- Migration for Jasper report
UPDATE utl_config_parm set parm_value='JASPER_SSO', default_value='JASPER_SSO', allow_value_desc = 'BOE, JASPER_REST, JASPER_SSO'  where parm_name = 'DEFAULT_REPORT_ENGINE' and parm_type='REPORT_ENGINE';

--changeSet DEV-2516:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "UTL_REPORT_TYPE" (
	"REPORT_NAME" Varchar2 (500) NOT NULL DEFERRABLE ,
	"REPORT_ENGINE_TYPE" Varchar2 (40) NOT NULL DEFERRABLE ,
	"REPORT_PATH" Varchar2 (1000),
	"REPORT_DESC" Varchar2 (4000),
	"ACTIVE_BOOL" Number(1,0) Default 1 NOT NULL DEFERRABLE  Check (ACTIVE_BOOL IN (0, 1) ) DEFERRABLE ,
	"SYSTEM_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SYSTEM_BOOL IN (0, 1) ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_UTL_REPORT_TYPE" primary key ("REPORT_NAME") 
)  
');
END;
/

--changeSet DEV-2516:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_REPORT_TYPE" add Constraint "FK_MIMDB_CTRLUTLREPORTTYPE" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2516:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_REPORT_TYPE" add Constraint "FK_MIMDB_CREATEUTLREPORTTYPE" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2516:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_REPORT_TYPE" add Constraint "FK_MIMDB_REVUTLREPORTTYPE" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2516:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "UTL_REPORT_TYPE" add Constraint "FK_MIMRSTAT_UTLREPORTTYPE" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2516:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add insert trigger for UTL_REPORT_TYPE
CREATE OR REPLACE TRIGGER "TIBR_UTL_REPORT_TYPE" BEFORE INSERT
   ON "UTL_REPORT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-2516:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add update trigger for UTL_REPORT_TYPE
CREATE OR REPLACE TRIGGER "TUBR_UTL_REPORT_TYPE" BEFORE UPDATE
   ON "UTL_REPORT_TYPE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-2516:9 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'esignature.SignatureCorrection', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/esignature/SignatureCorrection', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'esignature.SignatureCorrection');

--changeSet DEV-2516:10 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.CondemnedPartTag', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/CondemnedPartTag', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.CondemnedPartTag');

--changeSet DEV-2516:11 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.DetailInvFncLog', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/inventory/DetailInvFncLog', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.DetailInvFncLog');

--changeSet DEV-2516:12 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.InspReqServiceablePartTag', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/InspReqServiceablePartTag', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.InspReqServiceablePartTag');

--changeSet DEV-2516:13 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.IssuedCsgnInventory', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/IssuedCsgnInventory', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.IssuedCsgnInventory');

--changeSet DEV-2516:14 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.KitAssemblyTicket', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/KitAssemblyTicket', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.KitAssemblyTicket');

--changeSet DEV-2516:15 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.NoInspReqServiceablePartTag', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/NoInspReqServiceablePartTag', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.NoInspReqServiceablePartTag');   

--changeSet DEV-2516:16 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.RotableAdjustment', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/inventory/RotableAdjustment', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.RotableAdjustment');   

--changeSet DEV-2516:17 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.SummaryInvFncLog', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/inventory/SummaryInvFncLog', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.SummaryInvFncLog');   

--changeSet DEV-2516:18 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'inventory.UnserviceablePartTag', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/inventory/UnserviceablePartTag', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'inventory.UnserviceablePartTag');   

--changeSet DEV-2516:19 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'license.LicenseCard', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/license/LicenseCard', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'license.LicenseCard');   

--changeSet DEV-2516:20 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'location.LabourSummary', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/location/LabourSummary', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'location.LabourSummary');   

--changeSet DEV-2516:21 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'lrp.PlanComparisonReport', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/lrp/PlanComparisonReport', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'lrp.PlanComparisonReport');   

--changeSet DEV-2516:22 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'maintprgm.MaintenanceProgramDiff', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgramDiff', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'maintprgm.MaintenanceProgramDiff');   

--changeSet DEV-2516:23 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'maintprgm.MaintenanceProgramImpact', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgramImpact', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'maintprgm.MaintenanceProgramImpact');   

--changeSet DEV-2516:24 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'maintprgm.MaintenanceProgram', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/maintprgm/MaintenanceProgram', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'maintprgm.MaintenanceProgram');   

--changeSet DEV-2516:25 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'oilconsumption.OilConsumptionReport', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/oilconsumption/OilConsumptionReport', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'oilconsumption.OilConsumptionReport');   

--changeSet DEV-2516:26 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'po.PurchaseOrder', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/po/PurchaseOrder', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'po.PurchaseOrder');   

--changeSet DEV-2516:27 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'po.RepairOrConvertedRepairOrder', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/po/RepairOrConvertedRepairOrder', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'po.RepairOrConvertedRepairOrder');   

--changeSet DEV-2516:28 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'rfq.RFQ', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/rfq/RFQ', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'rfq.RFQ');   

--changeSet DEV-2516:29 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'shipment.PickList', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/shipment/PickList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'shipment.PickList');     

--changeSet DEV-2516:30 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'shipment.ShippingForm', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/shipment/ShippingForm', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'shipment.ShippingForm');   

--changeSet DEV-2516:31 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'tag.TagTaskDefinitions', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/tag/TagTaskDefinitions', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'tag.TagTaskDefinitions');   

--changeSet DEV-2516:32 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.FoundFault', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/FoundFault', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.FoundFault');   

--changeSet DEV-2516:33 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.MaintenanceRelease', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/MaintenanceRelease', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.MaintenanceRelease');   

--changeSet DEV-2516:34 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.OrderedTallySheet', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/OrderedTallySheet', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.OrderedTallySheet');   

--changeSet DEV-2516:35 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.TallySheet', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/TallySheet', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.TallySheet');   

--changeSet DEV-2516:36 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.TaskCard', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/TaskCard', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.TaskCard');   

--changeSet DEV-2516:37 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'task.WorkCapture', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/task/WorkCapture', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'task.WorkCapture');   

--changeSet DEV-2516:38 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'taskdefinition.ReqImpactReport', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/taskdefinition/ReqImpactReport', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'taskdefinition.ReqImpactReport');   

--changeSet DEV-2516:39 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'taskdefinition.TaskDefnCard', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/taskdefinition/TaskDefnCard', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'taskdefinition.TaskDefnCard');   

--changeSet DEV-2516:40 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'taskdefinition.TaskLaborSummary', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/taskdefinition/TaskLaborSummary', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'taskdefinition.TaskLaborSummary');   

--changeSet DEV-2516:41 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'transfer.IssueList', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/transfer/IssueList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'transfer.IssueList');   

--changeSet DEV-2516:42 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'transfer.IssueTicket', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/transfer/IssueTicket', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'transfer.IssueTicket');   

--changeSet DEV-2516:43 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'transfer.PutAwayTicket', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/transfer/PutAwayTicket', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'transfer.PutAwayTicket');   

--changeSet DEV-2516:44 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'transfer.TransferTicket', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/transfer/TransferTicket', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'transfer.TransferTicket');   

--changeSet DEV-2516:45 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.DepartmentMemberList', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/DepartmentMemberList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.DepartmentMemberList');   

--changeSet DEV-2516:46 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.FunctionAccessRole', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/FunctionAccessRole', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.FunctionAccessRole');   

--changeSet DEV-2516:47 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.FunctionAccess', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/FunctionAccess', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.FunctionAccess');   

--changeSet DEV-2516:48 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.FunctionActionAccessRole', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/FunctionActionAccessRole', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.FunctionActionAccessRole');   

--changeSet DEV-2516:49 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.FunctionActionAccess', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/FunctionActionAccess', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.FunctionActionAccess');      

--changeSet DEV-2516:50 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.SystemInformation', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/SystemInformation', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.SystemInformation');   

--changeSet DEV-2516:51 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.UIandLogicSettings', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/UIandLogicSettings', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.UIandLogicSettings');   

--changeSet DEV-2516:52 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.UserAction', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/UserAction', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.UserAction');   

--changeSet DEV-2516:53 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.UserDepartmentList', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/UserDepartmentList', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.UserDepartmentList');   

--changeSet DEV-2516:54 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'utility.UserRole', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/utility/UserRole', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'utility.UserRole');   

--changeSet DEV-2516:55 stripComments:false
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'vendor.VendorReliability', 'JASPER_REST', '/organizations/Maintenix/Reports/Core/vendor/VendorReliability', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM dual
   WHERE NOT EXISTS (SELECT * FROM utl_report_type WHERE report_name = 'vendor.VendorReliability');   

--changeSet DEV-2516:56 stripComments:false
-- Update report paths in utl_report_type table if changed in original utl_config_parm table
UPDATE utl_report_type 
SET utl_report_type.report_path =
   ( SELECT 
        utl_config_parm.parm_value
     FROM 
        utl_config_parm
     WHERE
        utl_report_type.report_name = utl_config_parm.parm_name
        AND 
        utl_config_parm.parm_type = 'REPORT_PATH_MAPPING'
    ) 
WHERE EXISTS
   ( SELECT 
        utl_config_parm.parm_value
     FROM 
        utl_config_parm
     WHERE
        utl_report_type.report_name = utl_config_parm.parm_name
        AND 
        utl_config_parm.parm_type = 'REPORT_PATH_MAPPING'
        AND
        utl_config_parm.parm_value <> utl_report_type.report_path
    );

--changeSet DEV-2516:57 stripComments:false
-- Copy custom rows from utl_config_parm to utl_report_type
INSERT INTO utl_report_type (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
SELECT 
    report_engine_parm.parm_name, 
    report_engine_parm.parm_value, 
    report_path_parm.report_path, 
    report_path_parm.report_desc, 
    1, 0, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI' 
FROM utl_config_parm report_engine_parm, 
    ( 
      SELECT 
           utl_config_parm.parm_name, 
           utl_config_parm.parm_value as report_path, 
           utl_config_parm.parm_desc as report_desc 
      FROM 
           utl_config_parm 
      WHERE 
           utl_config_parm.parm_type = 'REPORT_PATH_MAPPING' 
    ) report_path_parm 
WHERE NOT EXISTS 
    (SELECT * FROM utl_report_type WHERE utl_report_type.report_name = report_engine_parm.parm_name) 
    AND report_engine_parm.parm_type = 'REPORT_ENGINE' 
    AND report_engine_parm.parm_name <> 'DEFAULT_REPORT_ENGINE'
    AND report_engine_parm.parm_name = report_path_parm.parm_name ( + );   

--changeSet DEV-2516:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Delete config parms for REPORT_ENGINE and REPORT_PATH_MAPPING in utl_config_parm
BEGIN
FOR report_path_parm_list IN (
    SELECT 
       utl_config_parm.parm_name,
       utl_config_parm.parm_type
    FROM utl_config_parm
    WHERE 
       utl_config_parm.parm_type = 'REPORT_PATH_MAPPING'
    )LOOP

    utl_migr_data_pkg.config_parm_delete(report_path_parm_list.parm_name);    
END LOOP;

FOR report_engine_parm_list IN (
    SELECT 
       utl_config_parm.parm_name,
       utl_config_parm.parm_type
    FROM utl_config_parm
    WHERE 
       utl_config_parm.parm_type = 'REPORT_ENGINE'
       AND utl_config_parm.parm_name <> 'DEFAULT_REPORT_ENGINE'
    )LOOP

    utl_migr_data_pkg.config_parm_delete(report_engine_parm_list.parm_name);    
END LOOP;
END;
/