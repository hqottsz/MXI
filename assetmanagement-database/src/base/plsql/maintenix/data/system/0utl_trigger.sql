--liquibase formatted sql


--changeSet 0utl_trigger:1 stripComments:false
-- Insert script for UTL_TRIGGER
-- NOTE: trigger_id column counts down from the last inserted row
--       this effectively reserves ids 100001 - 1 for internal use
--       To ensure that keys do not conflict, reserve a number from
--       the trunk database.
--
--       You will usually want to add a new trigger_id rather than
--       updating an existing row, for example, when adding a
--       notification API trigger class version.

INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (100001, 'RO_NUM_GEN', 1, 'xxx', 'Repair Order Number Generator', 'com.mxi.mx.core.plugin.ordernumber.RepairOrderNumberGenerator', 1, 0);

--changeSet 0utl_trigger:2 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (100000, 'MX_REPORT_PARM_SET', 1, 'COMPONENT', 'Report Parameter Set', 'com.mxi.mx.common.trigger.report.MxReportParmSetTrigger', 0, 0);

--changeSet 0utl_trigger:3 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99999, 'MX_FNC_SEND_ORDER_INFORMATION', 1, 'COMPONENT', 'Send Order Information', 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById30', 0, 0);

--changeSet 0utl_trigger:4 stripComments:false
-- flight adapter triggers for work order
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99998, 'MX_TS_CREATE', 1, 'COMPONENT', 'plan work order request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:5 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99997, 'MX_TS_COMMIT', 1, 'COMPONENT', 'commit work order request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:6 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99996, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'uncommit work order request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:7 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99995, 'MX_TS_STARTWORK', 1, 'COMPONENT', 'start work order request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:8 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99994, 'MX_TS_UNDOSTARTWORK', 1, 'COMPONENT', 'unstart work order request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:9 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99993, 'MX_TS_DELAY', 1, 'COMPONENT', 'estimate return to service request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:10 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99992, 'MX_TS_COMPLETE', 1, 'COMPONENT', 'return to service request', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:11 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99991, 'MX_TS_CANCEL', 1, 'COMPONENT', 'cancel the task', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:12 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99990, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'schedule the work order external', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:13 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99989, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'schedule the work order internal', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.WOStatus12PublishTrigger', 0, 0);

--changeSet 0utl_trigger:14 stripComments:false
-- flight adapter triggers for faults
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99988, 'MX_TS_DEFER', 1, 'COMPONENT', 'TASK DEFER', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.OutstandingFaultPublishTrigger', 0, 0);

--changeSet 0utl_trigger:15 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99987, 'MX_CF_CERTIFY', 1, 'COMPONENT', 'FAULT CERTIFICATION', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.CompleteFaultPublishTrigger', 0, 0);

--changeSet 0utl_trigger:16 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99986, 'MX_CF_NFF', 1, 'COMPONENT', 'FAULT NFF', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.CompleteFaultPublishTrigger', 0, 0);

--changeSet 0utl_trigger:17 stripComments:false
-- Integration Framework Message ID Generator trigger
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99985, 'MX_INT_ID_GENERATOR', 1, 'COMPONENT', 'Integration message ID generator', 'com.mxi.mx.integration.trigger.id.DefaultMsgIdGenerator', 1, 0);

--changeSet 0utl_trigger:18 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99984, 'MX_RFQ_SEND', 1, 'COMPONENT', 'Send Request For Quotes', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForQuotes', 0, 0);

--changeSet 0utl_trigger:19 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99983, 'RO_REL_NUM_GEN', 1, 'xxx', 'Repair Order Release Number Generator', 'com.mxi.mx.core.plugin.releasenumber.RepairOrderReleaseNumberGenerator', 1, 0);

--changeSet 0utl_trigger:20 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99982, 'MX_FNC_SEND_DETAILED_FINANCIAL_LOG', 1, 'COMPONENT', 'Send Detailed Financial Log', 'com.mxi.mx.core.adapter.finance.trg.TrgDetailedFinancialLogById20', 0, 0);

--changeSet 0utl_trigger:21 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99981, 'MX_FNC_SEND_TO_BE_PAID_INVOICE', 1, 'COMPONENT', 'Send Detailed To Be Paid Invoice', 'com.mxi.mx.core.adapter.finance.trg.TrgToBePaidInvoiceById20', 0, 0);

--changeSet 0utl_trigger:22 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99980, 'MX_IN_AUTO_RESERVATION', 1, 'COMPONENT', 'Auto Reservation Request Handler', 'com.mxi.mx.core.services.inventory.reservation.AutoReservationRequestHandler', 1, 0);

--changeSet 0utl_trigger:23 stripComments:false
-- material adapter triggers
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99979, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package ', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:24 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99978, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:25 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99977, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:26 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99976, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'part request by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:27 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99975, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'part request by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:28 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99974, 'MX_TS_CREATE', 1, 'COMPONENT', 'part request by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:29 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99973, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:30 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99972, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:31 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99966, 'MX_TS_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:32 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99965, 'MX_PR_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:33 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99964, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:34 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99963, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'update part request by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:35 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99962, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'update part request by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0);

--changeSet 0utl_trigger:36 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99960, 'MX_PR_UPDATE_REQ_BY_DT', 1, 'COMPONENT', 'Triggered when the req_update_queue entry is processed.', 'com.mxi.mx.core.services.req.DefaultUpdateReqByDateTrigger', 0, 0);

--changeSet 0utl_trigger:37 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99959, 'MX_EDIT_FAULT', 1, 'COMPONENT', 'EDIT FAULT', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 0, 0);

--changeSet 0utl_trigger:38 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99958, 'MX_CF_SET_OP_RESTRICTION', 1, 'COMPONENT', 'SET OPER RESTRICTION', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 0, 0);

--changeSet 0utl_trigger:39 stripComments:false
-- flight adapter trigger for inventory deadline update
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99957, 'MX_FL_DEADLINE_UPDATED', 1, 'COMPONENT', 'Inventory Deadlines Updated', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.DeadlinesUpdatedTrigger', 0, 0);

--changeSet 0utl_trigger:40 stripComments:false
-- Procurement Adapter trigger for vendor creation message
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99956, 'MX_VENDOR_CREATE', 1, 'COMPONENT', 'Vendor Creation', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorCreation', 0, 0);

--changeSet 0utl_trigger:41 stripComments:false
-- Procurement Adapter trigger for vendor approval update message
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99955, 'MX_VENDOR_APPROVAL_UPDATE', 1, 'COMPONENT', 'Vendor Approval Update', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorApprovalUpdate', 0, 0);

--changeSet 0utl_trigger:42 stripComments:false
-- Procurement Adapter trigger for potential turn in message
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99954, 'MX_MAT_POTENTIAL_TURN_IN', 1, 'COMPONENT', 'Send Potential Turn In', 'com.mxi.mx.core.adapter.services.material.potentialturnin.PotentialTurnInService11', 0, 0);

--changeSet 0utl_trigger:43 stripComments:false
-- Material Adapter trigger for part definition created outoing message
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99953, 'MX_MAT_PART_DEF_CREATED', 1, 'COMPONENT', 'Send Part Definition Created', 'com.mxi.mx.core.adapter.services.material.partdefinitioncreated.PartDefinitionCreatedService11', 0, 0);

--changeSet 0utl_trigger:44 stripComments:false
-- Aircraft work package release number generator
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99952, 'WO_REL_NUM_GEN', 1, 'xxx', 'Work Order Release Number Generator', 'com.mxi.mx.core.plugin.releasenumber.WorkOrderReleaseNumberGenerator', 1, 0);

--changeSet 0utl_trigger:45 stripComments:false
-- Aircraft work package order number generator
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99951, 'WO_NUM_GEN', 1, 'xxx', 'Work Order Number Generator', 'com.mxi.mx.core.plugin.ordernumber.WorkOrderNumberGenerator', 1, 0);

--changeSet 0utl_trigger:46 stripComments:false
-- Materials adapter V1_2 triggers
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99950, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package ', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:47 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99949, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:48 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99948, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:49 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99947, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'part request by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:50 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99946, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'part request by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:51 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99945, 'MX_TS_CREATE', 1, 'COMPONENT', 'part request by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:52 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99944, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:53 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99943, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:54 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99942, 'MX_TS_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:55 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99941, 'MX_PR_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:56 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99940, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:57 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99939, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'update part request by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:58 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99938, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'update part request by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTriggerV1_2', 0, 0);

--changeSet 0utl_trigger:59 stripComments:false
-- Flight leg name generator
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99937, 'MX_FLIGHT_GENERATE_NAME', 1, 'xxx', 'Flight Leg Name Generator', 'com.mxi.mx.web.trigger.flight.DefaultFlightLegNameGenerator', 1, 0);

--changeSet 0utl_trigger:60 stripComments:false
-- Materials adapter part request publish external supply chain triggers
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99936, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'part request by work order after internally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:61 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99935, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:62 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99934, 'MX_TS_COMMIT', 1, 'COMPONENT', 'part request by work order after commit work work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:63 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99933, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'part request by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:64 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99932, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'part request by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:65 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99931, 'MX_TS_CREATE', 1, 'COMPONENT', 'part request by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:66 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99930, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'part request by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:67 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99929, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'part request by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:68 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99928, 'MX_TS_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishExtSupChainTrigger', 0, 0);

--changeSet 0utl_trigger:69 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99927, 'MX_PR_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishExtSupChainTrigger', 0, 0);

--changeSet 0utl_trigger:70 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99926, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:71 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99925, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'update part request by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:72 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99924, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'update part request by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishExternalSupplyChainTrigger', 0, 0);

--changeSet 0utl_trigger:73 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99923, 'MX_TS_SEND_WP_TO_MRO', 1, 'COMPONENT', 'request to send work package message to MRO for integration', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendWPToMROTrigger', 0, 0);

--changeSet 0utl_trigger:74 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99922, 'MX_TS_SEND_CANCEL_WP_TO_MRO', 1, 'COMPONENT', 'request to send cancel work package message to MRO for integration', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.SendCancelWPToMROTrigger', 0, 0);

--changeSet 0utl_trigger:75 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99921, 'MX_FAULT_DEADLINE_EXTENDED', 1, 'COMPONENT', 'EXTEND FAULT DEADLINES', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.FaultUpdatePublishTrigger', 0, 0);

--changeSet 0utl_trigger:76 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99920, 'MX_TS_SEND_LINE_NUMBER_TO_MRO', 1, 'COMPONENT', 'request to send assign line number message to MRO for integration', 'com.mxi.mx.core.adapter.maintenance.exec.work.pkg.lineitem.SendAssignLineNumberToMROTrigger', 0, 0);

--changeSet 0utl_trigger:77 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99919, 'MX_PO_ISSUE', 1, 'COMPONENT', 'Spec2000 Issue PO trigger', 'com.mxi.mx.integration.procurement.atasparesorder.triggers.IssueSparesOrderMessageTriggerV1_0', 0, 0);

--changeSet 0utl_trigger:78 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99918, 'MX_IN_INSPSRV', 1, 'COMPONENT', 'Send Order Inventory Received', 'com.mxi.mx.integration.finance.order.trigger.SendOrderInventoryReceivedTrigger1_0', 0, 0);

--changeSet 0utl_trigger:79 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99917, 'MX_PO_CANCEL', 1, 'COMPONENT', 'Spec2000 Cancel PO trigger', 'com.mxi.mx.integration.procurement.atasparesorder.triggers.CancelSparesOrderMessageTriggerV1_0', 0, 0);

--changeSet 0utl_trigger:80 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values( 99916, 'MX_IX_SEND', 1, 'COMPONENT', 'Send Order Inventory Returned', 'com.mxi.mx.integration.finance.order.trigger.SendOrderInventoryReturnedTrigger1_0', 0, 0);

--changeSet 0utl_trigger:81 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99915, 'MX_VENDOR_CREATE', 1, 'COMPONENT', 'Vendor Creation', 'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorCreationV1_1', 0, 0);

--changeSet 0utl_trigger:82 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
VALUES (99914, 'MX_AC_CAPABILITIES_UPDATED', 1, 'COMPONENT', 'Aircraft Capabilities Updated', 'com.mxi.mx.core.adapter.flight.messages.flight.trigger.AircraftCapabilitiesUpdatedPublishTrigger', 0, 0);

--changeSet 0utl_trigger:83 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99913, 'MX_FNC_SEND_ORDER_INFORMATION', 1, 'COMPONENT', 'Send Order Information', 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById40', 0, 0);

--changeSet 0utl_trigger:84 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99912, 'MX_FNC_SEND_DETAILED_FINANCIAL_LOG', 1, 'COMPONENT', 'Send Detailed Financial Log', 'com.mxi.mx.core.adapter.finance.trg.TrgDetailedFinancialLogById30', 0, 0);

--changeSet 0utl_trigger:85 stripComments:false
-- Materials adapter part request publish triggers
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99911, 'MX_TS_SCHEDULE_INTERNAL', 1, 'COMPONENT', 'publish part request message by work order after internally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:86 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99910, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'publish part request message by work order after externally schedule work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:87 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99909, 'MX_TS_COMMIT', 1, 'COMPONENT', 'publish part request message by work order after commit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:88 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99908, 'MX_TS_UNCOMMIT', 1, 'COMPONENT', 'publish part request message by work order after uncommit work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:89 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99907, 'MX_TS_REQUEST_PARTS', 1, 'COMPONENT', 'publish part request message by work order after request parts', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:90 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99906, 'MX_TS_CREATE', 1, 'COMPONENT', 'publish part request message by task after initialize task definition which creats task on inventory', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:91 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99905, 'MX_TS_TASKASSIGN', 1, 'COMPONENT', 'publish part request message by task after assign task to or unassign task from work package', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:92 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99904, 'MX_TS_ADD_PART_REQUIREMENT', 1, 'COMPONENT', 'publish part request message by part requirement after add part requirement to task', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:93 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99903, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'publish part request message by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:94 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99902, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'publish part request message by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:95 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99901, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'publish part request message by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTriggerV2_3', 0, 0);

--changeSet 0utl_trigger:96 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99900, 'MX_TS_CANCEL', 1, 'COMPONENT', 'publish cancel part request message by part requirement after cancel task', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishTriggerV1_1', 0, 0);

--changeSet 0utl_trigger:97 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
values (99899, 'MX_PR_CANCEL', 1, 'COMPONENT', 'publish cancel part request message by part requirement after cancel part requirement', 'com.mxi.mx.core.adapter.material.outgoing.cancelpartrequest.CancelPartRequestPublishTriggerV1_1', 0, 0);

