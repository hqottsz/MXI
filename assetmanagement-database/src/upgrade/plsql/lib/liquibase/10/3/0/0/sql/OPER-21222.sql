--liquibase formatted sql

--changeset OPER-21222:1 stripComments:false
--comment update columns utl_report_type.report_name and utl_report_type.report_path
UPDATE
   utl_report_type
SET
   report_name = 'task.CompleteWorkPackage',
   report_path = '/organizations/Maintenix/Reports/Core/task/CompleteWorkPackage'
WHERE
   report_name = 'task.MaintenanceRelease';


--changeset OPER-21222:2 stripComments:false
--comment update columns utl_plugin.plugin_class and utl_plugin.interface_class
UPDATE
   utl_plugin
SET
   plugin_class    = 'com.mxi.mx.web.plugin.esigner.CompleteWorkPackageDocumentGenerator',
   interface_class = 'com.mxi.mx.web.services.esigner.trigger.CompleteWorkPackageDocumentGenerator'
WHERE
   plugin_id = 99999;
   