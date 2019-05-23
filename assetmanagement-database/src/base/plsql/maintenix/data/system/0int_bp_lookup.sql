--liquibase formatted sql


--changeSet 0int_bp_lookup:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "INT_BP_LOOKUP"
** DATE: 21/03/2005 TIME: 11:53:54
*********************************************/
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/test', 'echo', 'EJB', 'com.mxi.mx.integration.ejb.Echo', 'echo', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:2 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/test', 'sleep', 'EJB', 'com.mxi.mx.integration.ejb.Sleep', 'sleep', 'FULL', 0, to_date('07-12-2006 11:31:19', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-12-2006 11:31:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:3 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/test', 'process', 'PROCESS', 'http://process.mxi.com/test', 'echoProcess', 'FULL', 0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:4 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'issueRequest', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'issueRequest', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:5 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'S1CPOACK', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'acknowledgePurchaseOrders', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:6 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'getissuedpurchaseorders', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'getIssuedPurchaseOrders', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:7 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'S1SPLSHP', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'processShipmentAdvisory', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:8 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'S1EXCXMT', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'recordPurchaseOrderExceptions', 'FULL', 0, to_date('05-10-2005 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-10-2005 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:9 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'sendRequestForQuotes', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendRequestForQuotes', 'FULL', 0, to_date('16-10-2005 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-10-2005 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:10 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/procurement/ATA_SparesQuoteInterim/1.0', 'ATA_SparesQuoteInterim', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'receiveInterimRequestForQuote', 'FULL', 0, to_date('16-10-2005 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-10-2005 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:11 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/procurement/ATA_SparesQuoteFinal/1.0', 'ATA_SparesQuoteFinal', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'receiveFinalRequestForQuote', 'FULL', 0, to_date('22-10-2007 04:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('22-10-2007 04:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:12 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'sendRequestForVendorCreation', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendVendorCreation', 'FULL', 0, to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:13 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.1', 'sendRequestForVendorCreation', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendVendorCreationV1_1', 'FULL', 0, to_date('30-09-2016 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-09-2016 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:14 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/1.0', 'sendRequestForVendorApprovalUpdate', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'sendVendorApprovalUpdated', 'FULL', 0, to_date('03-05-2012 08:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('03-05-2012 08:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:15 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/procurement/update_vendor_external_key/1.0', 'update_vendor_external_key', 'EJB', 'com.mxi.mx.core.ejb.ProcurementAdapter', 'processVendorExtKey', 'FULL', 0, to_date('27-04-2012 08:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-05-2012 14:00:52', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:16 stripComments:false
-- Material Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/ready_for_issue/1.0', 'ready_for_issue', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'readyForIssue', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:17 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/issue_inventory/1.0', 'issue_inventory', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:18 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/inventory-issued-to/1.0', 'inventory-issued-to', 'JAVA', 'com.mxi.mx.core.adapter.issueinventory.issueinventoryto.InventoryIssuedToEntryPoint', 'process', 'FULL', 0, to_date('10-07-2010 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-07-2010 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:19 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/activate_part_definition/1.0', 'activate_part_definition', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'activatePartDefinition', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:20 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_definition_activated/1.0', 'part_definition_activated', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:21 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/create_part_definition/1.0', 'create_part_definition', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'createPartDefinition', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:22 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_definition_created/1.0', 'part_definition_created', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:23 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_definition_created/1.1', 'part_definition_created', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('25-04-2013 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('25-04-2013 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:24 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/confirm_turnin/1.0', 'confirm_turnin', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'confirmTurnIn', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:25 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_request_status/1.0', 'part_request_status', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'partRequestStatus', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:26 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/potential_turnin/1.0', 'potential_turnin', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:27 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/turnin_inventory/1.0', 'turnin_inventory', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:28 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_request_quantity_update/1.0', 'part_request_quantity_update', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'partRequestQuantityUpdate', 'FULL', 0, to_date('26-10-2006 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('26-10-2006 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:29 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/send_issue_inventory/1.0', 'send_issue_inventory', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'sendIssueInventory', 'FULL', 0, to_date('10-06-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-06-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:30 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.0', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPoint', 'process', 'FULL', 0, to_date('07-06-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-06-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:31 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.0', 'update-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestEntryPoint', 'process', 'FULL', 0, to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:32 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/integration/hr-adapter/updateUser/1.0', 'updateUser', 'EJB', 'com.mxi.mx.core.ejb.HumanResource', 'updateUser', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:33 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ema/external_maintenance/2.0', 'external_maintenance', 'EJB', 'com.mxi.mx.core.adapter.ejb.ema.ExternalMaintenanceAdapter', 'processExternalMaintenance', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:34 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ema/external_maintenance/2.1', 'external_maintenance', 'EJB', 'com.mxi.mx.core.adapter.ejb.ema.ExternalMaintenanceAdapter', 'processExternalMaintenance', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:35 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ema/external_maintenance/2.2', 'external_maintenance', 'EJB', 'com.mxi.mx.core.adapter.ejb.ema.ExternalMaintenanceAdapter', 'processExternalMaintenance', 'FULL', 0, to_date('08-03-2006 10:58:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-03-2006 12:07:19', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:36 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_definition_obsolesced/1.0', 'part_definition_obsolesced', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('28-03-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('28-03-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:37 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part_definition_updated/1.0', 'part_definition_updated', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('02-04-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-04-2013 10:48:32', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:38 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/potential_turnin/1.1', 'potential_turnin', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'send', 'FULL', 0, to_date('04-04-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-04-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:39 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/create-part-definition/1.1', 'create-part-definition', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'createPartDefinitionV1_2', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:40 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/update-part-definition-status/1.1', 'update-part-definition-status', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'updatePartDefinitionStatusV1_2', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:41 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.1', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV1_2', 'process', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:42 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.2', 'part-request-status', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'partRequestStatusV1_2', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:43 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.3', 'part-request-status', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'partRequestStatusV1_3', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:44 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.4', 'part-request-status', 'EJB', 'com.mxi.mx.core.ejb.MaterialAdapter', 'partRequestStatusV1_4', 'FULL', 0, to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-12-2013 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:45 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.1', 'update-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestEntryPointV1_2', 'process', 'FULL', 0, to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:46 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.2', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV2_2', 'process', 'FULL', 0, to_date('06-05-2015 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('06-05-2015 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:47 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/part-request-request/2.3', 'part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestEntryPointV2_3', 'process', 'FULL', 0, to_date('02-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-02-2018 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:48 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/cancel-part-request-request/1.0', 'cancel-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestEntryPointV1_0', 'process', 'FULL', 0, to_date('20-11-2015 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-11-2015 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:49 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/matadapter/cancel-part-request-request/1.1', 'cancel-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestEntryPointV1_1', 'process', 'FULL', 0, to_date('08-02-2017 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-02-2017 11:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:50 stripComments:false
-- Finance Adapter
-- request_detailed_inventory_financial_log_by_id
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log_by_id/1.0', 'request_detailed_inventory_financial_log_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_detailed_inventory_financial_log_by_id', 'FULL', 0, to_date('12-06-2007 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-06-2007 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:51 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log_by_id/1.1','request_detailed_inventory_financial_log_by_id','PROCESS','http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_by_id_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:52 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log_by_id/2.0','request_detailed_inventory_financial_log_by_id','PROCESS','http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_by_id_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:53 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ( 'http://xml.mxi.com/xsd/core/finance/request-detailed-inventory-financial-log-by-id/3.0', 'request-detailed-inventory-financial-log-by-id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request-detailed-inventory-financial-log-by-id-30', 'FULL', 0, TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), TO_DATE('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:54 stripComments:false
-- request_detailed_inventory_financial_log
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log/1.0', 'request_detailed_inventory_financial_log', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_detailed_inventory_financial_log', 'FULL', 0, to_date('12-06-2007 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-06-2007 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:55 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log/1.1','request_detailed_inventory_financial_log','PROCESS','http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:56 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_detailed_inventory_financial_log/2.0','request_detailed_inventory_financial_log','PROCESS','http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:57 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request-detailed-inventory-financial-log/3.0','request-detailed-inventory-financial-log','PROCESS','http://www.mxi.com/mx/xml/finance','request-detailed-inventory-financial-log-30', 'FULL', 0, to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:58 stripComments:false
-- request_summary_inventory_financial_log
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_summary_inventory_financial_log/1.0', 'request_summary_inventory_financial_log', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_summary_inventory_financial_log', 'FULL', 0, to_date('11-07-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:59 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_summary_inventory_financial_log/1.1','request_summary_inventory_financial_log','PROCESS','http://www.mxi.com/mx/xml/finance','request_summary_inventory_financial_log_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:60 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_summary_inventory_financial_log/2.0','request_summary_inventory_financial_log','PROCESS','http://www.mxi.com/mx/xml/finance','request_summary_inventory_financial_log_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:61 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create-financial-accounts-request/1.0', 'create-financial-accounts-request', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'createFinancialAccountsRequest', 'FULL', 0, to_date('20-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:62 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/update_financial_accounts/1.0', 'update-financial-accounts-request', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'updateFinancialAccountsRequest', 'FULL', 0, to_date('28-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('28-03-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:63 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/close-financial-accounts-request/1.0', 'close-financial-accounts-request', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'closeFinancialAccountsRequest', 'FULL', 0, to_date('05-04-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-04-2013 19:03:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:64 stripComments:false
-- request_order_information
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information/1.0', 'request_order_information', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_order_information', 'FULL', 0, to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:65 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information/1.1','request_order_information','PROCESS','http://www.mxi.com/mx/xml/finance','request_order_information_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:66 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information/2.0','request_order_information','PROCESS','http://www.mxi.com/mx/xml/finance','request_order_information_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:67 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information/2.1','request_order_information','PROCESS','http://www.mxi.com/mx/xml/finance','request_order_information_21', 'FULL', 0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:68 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request-order-information/3.0','request-order-information','PROCESS','http://www.mxi.com/mx/xml/finance','request-order-information-30', 'FULL', 0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:69 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request-order-information/4.0','request-order-information','PROCESS','http://www.mxi.com/mx/xml/finance','request-order-information-40', 'FULL', 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:70 stripComments:false
-- request_order_information_by_id
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/1.0', 'request_order_information_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id', 'FULL', 0, to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:71 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/1.1', 'request_order_information_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_11', 'FULL', 0, to_date('29-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-01-2009 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:72 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/2.0', 'request_order_information_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:73 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/2.1', 'request_order_information_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_21', 'FULL', 0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:74 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/3.0', 'request-order-information-by-id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-30', 'FULL', 0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:75 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/4.0', 'request-order-information-by-id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-40', 'FULL', 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:76 stripComments:false
-- approve_order_budget
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/approve_order_budget/1.0', 'approve_order_budget', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'approve_order_budget', 'FULL', 0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:77 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/approve-order-budget/2.0', 'approve-order-budget', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'approve-order-budget-20', 'FULL', 0, to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:78 stripComments:false
-- reject_order_budget
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/reject_order_budget/1.0', 'reject_order_budget', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'reject_order_budget', 'FULL', 0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:79 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/reject-order-budget/2.0', 'reject-order-budget', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'reject-order-budget-20', 'FULL', 0, to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:80 stripComments:false
-- request_to_be_paid_invoices
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices/1.0', 'request_to_be_paid_invoices', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices', 'FULL', 0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:81 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices/1.1', 'request_to_be_paid_invoices', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:82 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices/2.0', 'request_to_be_paid_invoices', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:83 stripComments:false
-- request_to_be_paid_invoices_by_id
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices_by_id/1.0', 'request_to_be_paid_invoices_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_by_id', 'FULL', 0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:84 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices_by_id/1.1', 'request_to_be_paid_invoices_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_by_id_11', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:85 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/request_to_be_paid_invoices_by_id/2.0', 'request_to_be_paid_invoices_by_id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_by_id_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:86 stripComments:false
-- create_purchase_invoice
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create_purchase_invoice/1.0', 'create_purchase_invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice', 'FULL', 0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:87 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create_purchase_invoice/2.0', 'create_purchase_invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:88 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create_purchase_invoice/2.1', 'create_purchase_invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice_21', 'FULL', 0, to_date('11-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:89 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/3.0', 'create-purchase-invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-30', 'FULL', 0, to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:90 stripComments:false
-- update_currency_exchange_rate
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/update_currency_exchange_rate/1.0', 'update_currency_exchange_rate', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate', 'FULL', 0, to_date('07-08-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-08-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:91 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/update_currency_exchange_rate/1.1', 'update_currency_exchange_rate', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate_11', 'FULL', 0, to_date('27-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:92 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/update_currency_exchange_rate/2.0', 'update_currency_exchange_rate', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:93 stripComments:false
-- mark_invoice_as_paid
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/mark_invoice_as_paid/1.0', 'mark_invoice_as_paid', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'mark_invoice_as_paid', 'FULL', 0, to_date('15-09-2008 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('15-09-2008 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:94 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/mark_invoice_as_paid/2.0', 'mark_invoice_as_paid', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'mark_invoice_as_paid_20', 'FULL', 0, to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:95 stripComments:false
-- HR Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/create_user/1.0', 'create_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'create_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:96 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/update_user/1.0', 'update_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'update_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:97 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/delete_user/1.0', 'delete_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'delete_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:98 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/enable_user/1.0', 'enable_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'enable_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:99 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/disable_user/1.0', 'disable_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'disable_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:100 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/assign_user_to_department/1.0', 'assign_user_to_department', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'assign_user_to_department', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:101 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/unassign_user_from_department/1.0', 'unassign_user_from_department', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'unassign_user_from_department', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:102 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/assign_role_to_user/1.0', 'assign_role_to_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'assign_role_to_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:103 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/unassign_role_from_user/1.0', 'unassign_role_from_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'unassign_role_from_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:104 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/set_all_authority_for_user/1.0', 'set_all_authority_for_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'set_all_authority_for_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:105 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/assign_authority_to_user/1.0', 'assign_authority_to_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'assign_authority_to_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:106 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/unassign_authority_from_user/1.0', 'unassign_authority_from_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'unassign_authority_from_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:107 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/assign_skill_to_user/1.0', 'assign_skill_to_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'assign_skill_to_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:108 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/unassign_skill_from_user/1.0', 'unassign_skill_from_user', 'PROCESS', 'http://www.mxi.com/mx/xml/hr', 'unassign_skill_from_user', 'FULL', 0, to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-10-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:109 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/user-licenses/1.0', 'user-licenses', 'JAVA', 'com.mxi.mx.core.adapter.hr.userlicense.UserLicenseEntryPoint', 'process', 'FULL', 0, to_date('18-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('18-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:110 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/hr/user-planned-attendance/1.0', 'user-planned-attendance', 'JAVA', 'com.mxi.mx.core.adapter.hr.plannedattendance.PlannedAttendanceEntryPoint', 'process', 'FULL', 0, to_date('25-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('25-01-2010 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:111 stripComments:false
-- Shipment Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/shipment/get_pending_waybills/1.0', 'get_pending_waybills', 'PROCESS', 'http://www.mxi.com/mx/xml/shipment', 'get_pending_waybills', 'FULL', 0, to_date('10-09-2008 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-09-2008 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:112 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/shipment/update_waybill_details/1.0', 'update_waybill_details', 'PROCESS', 'http://www.mxi.com/mx/xml/shipment', 'update_waybill_details', 'FULL', 0, to_date('10-09-2008 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-09-2008 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:113 stripComments:false
-- Faults Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/faults/mark-as-obsolete/1.0', 'obsolete-failure-definitions', 'JAVA', 'com.mxi.mx.core.adapter.diagnostics.faults.markasobsolete.MarkAsObsoleteEntryPoint', 'process', 'FULL', 0, to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:114 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/faults/faults/1.0', 'faults', 'JAVA', 'com.mxi.mx.core.adapter.diagnostics.faults.fault.FaultEntryPoint', 'process', 'FULL', 0, to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:115 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/faults/fault-definition/1.0', 'fault-definitions', 'JAVA', 'com.mxi.mx.core.adapter.diagnostics.faults.faultdef.FaultDefEntryPoint', 'process', 'FULL', 0, to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:116 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/faults/define-failure-effect/1.0', 'define-failure-effects', 'JAVA', 'com.mxi.mx.core.adapter.diagnostics.faults.faileffectdef.FailEffectDefEntryPoint', 'process', 'FULL', 0, to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:117 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/faults/add-failure-effect/1.0', 'failure-effects', 'JAVA', 'com.mxi.mx.core.adapter.diagnostics.faults.faileffect.FailEffectEntryPoint', 'process', 'FULL', 0, to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-03-2009 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:118 stripComments:false
-- Flight Adapter
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/usage/1.0', 'usages', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.usage.adapter.UsageIntegrationInterface', 'process', 'FULL', 0, to_date('29-04-2009 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:119 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/usage/1.1', 'usages', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.usage.adapter.UsageIntegrationInterface11', 'process', 'FULL', 0, to_date('29-04-2009 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:120 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/usage/1.2', 'usages', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.usage.adapter.UsageIntegrationInterface12', 'process', 'FULL', 0, to_date('09-08-2016 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-08-2016 11:31:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:121 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/negate-usage/1.0', 'negate-usages', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.usage.adapter.NegateUsageIntegrationInterface', 'process', 'FULL', 0, to_date('13-11-2009 13:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('13-11-2009 13:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:122 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/measurement/measurement/1.0', 'flight-measurements', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.measurement.adapter.MeasurementIntegrationInterface', 'process', 'FULL', 0, to_date('29-04-2009 11:31:36', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:36', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:123 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/flight/1.0', 'flights', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.integrationinterface.v1.FlightIntegrationInterface', 'process', 'FULL', 0, to_date('29-04-2009 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:124 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/work-package-request/1.0', 'work-package-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:31:48', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:48', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:125 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/work-package-request/1.1', 'work-package-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint11', 'process', 'FULL', 0, to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:126 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/work-package-request/1.2', 'work-package-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.wostatus.WorkOrderStatusEntryPoint12', 'process', 'FULL', 0, to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-10-2012 13:40:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:127 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/fault-request/1.0', 'fault-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.faultstatus.FaultStatusEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:128 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/next-due-maintenance-task-request/1.0', 'next-due-maintenance-task-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.nextduemaintenance.NextDueMaintenanceEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:32:12', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:32:12', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:129 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/aircraft-status-request/1.0', 'aircraft-status-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftstatus.AircraftStatusEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:32:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:32:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:130 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/flight-identifier-request/1.0', 'flight-identifier-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.flightidentifier.FlightIdentifierEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:32:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:32:45', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:131 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/flight-identifier-request/1.1', 'flight-identifier-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.flightidentifier.FlightIdentifierEntryPoint11', 'process', 'FULL', 0, to_date('10-07-2013 12:12:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-07-2013 12:12:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:132 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.0', 'earliest-deadlines-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:133 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.1', 'earliest-deadlines-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint11', 'process', 'FULL', 0, to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:134 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.2', 'earliest-deadlines-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint12', 'process', 'FULL', 0, to_date('15-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('15-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:135 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/flight-disruptions/1.0', 'flight-disruptions', 'JAVA', 'com.mxi.mx.core.adapter.flightdisruption.FlightDisruptionEntryPoint', 'process', 'FULL', 0, to_date('10-07-2009 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-07-2009 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:136 stripComments:false
INSERT INTO int_bp_lookup (namespace,root_name,ref_type,ref_name,METHOD_NAME, INT_LOGGING_TYPE_CD,rstat_cd,creation_dt,revision_dt,revision_db_id,revision_user)
VALUES ('http://xml.mxi.com/xsd/core/matadapter/part_request_status/1.1','part_request_status','EJB','com.mxi.mx.core.ejb.MaterialAdapter','partRequestStatusV1_1', 'FULL', 0, to_date('04-07-2010 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-07-2010 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:137 stripComments:false
-- Flight Adapter: inventory deadlines updated notifications
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES ('http://xml.mxi.com/xsd/core/flight/inventory-deadlines-updated-request/1.0','inventory-deadlines-updated-request','JAVA','com.mxi.mx.core.adapter.flight.messages.flight.publish.deadlinesupdate.DeadlinesUpdatedEntryPoint10','process', 'FULL', 0, to_date('27-03-2012 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-03-2012 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:138 stripComments:false
-- PPC Optimize Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-request/1.0', 'optimize-production-plan-request', 'JAVA',
'com.mxi.mx.core.adapter.ppc.optimize.optimizeproductionplan.OptmizeProductionPlanEntryPoint', 'process',
'FULL', 0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:139 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-result/1.0', 'optimize-production-plan-result', 'JAVA',
'com.mxi.mx.core.adapter.ppc.optimize.productionplanresult.ProductionPlanResultEntryPoint', 'process',
'FULL', 0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:140 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/ppc/optimize/optimize-production-plan-status/1.0', 'optimize-production-plan-status', 'JAVA',
'com.mxi.mx.core.adapter.ppc.optimize.productionplanstatus.ProductionPlanStatusEntryPoint', 'process',
'FULL', 0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:141 stripComments:false
-- ATA Spares Invoice Adapter
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/procurement/ATA_SparesInvoice/1.0','ATA_SparesInvoice','JAVA', 'com.mxi.mx.core.adapter.procurement.atasparesinvoice.SparesInvoiceEntryPoint10', 'process', 'FULL', 0, to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-10-2010 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:142 stripComments:false
-- Part Price Management Adapter
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/procurement/1.0','MxPartPrices','JAVA', 'com.mxi.mx.core.adapter.procurement.partpricemanagement.PartPriceManagementEntryPoint10', 'process', 'FULL', 0, to_date('10-10-2011 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-10-2011 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:143 stripComments:false
-- ELA Project
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('noNamespace', 'ATA_DSE_Logbook', 'EJB', 'com.mxi.mx.core.ejb.ELA', 'coordinate', 'FULL', 0, to_date('04-05-2011 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-05-2011 10:30:30', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:144 stripComments:false
-- Timesheet Adapter
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.0','get-completed-work-hours-request','JAVA', 'com.mxi.mx.core.adapter.labor.getcompletedhours.GetCompletedHoursEntryPoint10', 'process', 'FULL', 0, to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:145 stripComments:false
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.1','get-completed-work-hours-request','JAVA', 'com.mxi.mx.core.adapter.labor.getcompletedhours.GetCompletedHoursEntryPoint11', 'process', 'FULL', 0, to_date('08-11-2013 16:50:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-11-2013 16:50:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:146 stripComments:false
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/labor/update-completed-work-hours-request/1.0','update-completed-work-hours-request','JAVA', 'com.mxi.mx.core.adapter.labor.updatecompletedhours.UpdateCompletedHoursEntryPoint10', 'process', 'FULL', 0, to_date('21-03-2013 11:01:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('21-03-2013 11:01:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:147 stripComments:false
-- Supply Chain Adapter
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/supplychain/get-upcoming-part-requests-request/1.0', 'get-upcoming-part-requests-request', 'JAVA',
'com.mxi.mx.core.adapter.supplychain.GetUpcomingPartRequestsEntryPoint', 'process',
'FULL', 0, to_date('20-03-2013 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:148 stripComments:false
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/hr/create-user/2.0','create-user','JAVA', 'com.mxi.mx.core.adapter.hr.createuser.CreateUserEntryPoint', 'process', 'FULL', 0, to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:149 stripComments:false
-- Integration with MRO
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/integration/work-package-coordination-request/1.0','work-package-coordination-request','JAVA', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendWPToMROEntryPoint', 'process', 'FULL', 0, to_date('17-07-2015 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2015 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:150 stripComments:false
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/integration/cancel-work-package-request/1.0','cancel-work-package-request','JAVA', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendCancelWPToMROEntryPoint', 'process', 'FULL', 0, to_date('29-07-2015 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-07-2015 10:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:151 stripComments:false
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES( 'http://xml.mxi.com/xsd/core/integration/assign-line-number-request/1.0','assign-line-number-request','JAVA', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.lineitem.SendAssignLineNumberToMROEntryPoint', 'process', 'FULL', 0, to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('06-11-2015 16:30:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:152 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flights/flight/2.0', 'flights', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.integrationinterface.v2.FlightIntegrationInterface', 'process', 'FULL', 0, to_date('08-12-2016 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-12-2016 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:153 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-updated-request/1.0', 'aircraft-capabilities-updated-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilitiesupdated.AircraftCapabilitiesUpdatedEntryPoint10', 'process', 'FULL', 0, to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-04-2009 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:154 stripComments:false
INSERT INTO INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES('http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-request/1.0', 'aircraft-capabilities-request', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilities.AircraftCapabilitiesEntryPoint10', 'process', 'FULL', 0, to_date('15-03-2017 11:32:28', 'dd-mm-yyyy hh24:mi:ss'), to_date('15-03-2017 11:32:28', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0int_bp_lookup:155 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/4.0', 'create-purchase-invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-40', 'FULL', 0, to_date('12-09-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-09-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');