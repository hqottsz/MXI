--liquibase formatted sql

--changeSet OPER-9449:1 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete existing roles.' WHERE parm_name = 'ACTION_DELETE_ROLE';


--changeSet OPER-9449:2 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete a new status board.' WHERE parm_name = 'ACTION_DELETE_STATUS_BOARD';


--changeSet OPER-9449:3 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete To-Do List button that''s required to create to-do lists for roles.' WHERE parm_name = 'ACTION_DELETE_TODO_LIST';


--changeSet OPER-9449:4 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Usage Definition button that''s required to remove usage definitions  from assemblies.' WHERE parm_name = 'ACTION_DELETE_USAGE_DEFINITION';


--changeSet OPER-9449:5 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete user button. (Users that have performed tasks in Maintenix cannot be deleted).' WHERE parm_name = 'ACTION_DELETE_USER';


--changeSet OPER-9449:6 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Vendor icon.' WHERE parm_name = 'ACTION_DELETE_VENDOR';


--changeSet OPER-9449:7 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete button that''s required to delete warranty contracts that are in build status.' WHERE parm_name = 'ACTION_DELETE_WARRANTY_CONTRACT';


--changeSet OPER-9449:8 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to disposed alert.' WHERE parm_name = 'ACTION_DISPOSITION_ALERT';


--changeSet OPER-9449:9 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Duplicate Job Card button that''s required to create copies of job cards (baseline definitions).' WHERE parm_name = 'ACTION_DUPLICATE_JIC';


--changeSet OPER-9449:10 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Duplicate MPC button that''s required to create copies of master panel cards.' WHERE parm_name = 'ACTION_DUPLICATE_MASTER_PANEL_CARD';


--changeSet OPER-9449:11 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Duplicate Reference button that''s required to create copies of reference documents.' WHERE parm_name = 'ACTION_DUPLICATE_REF_DOC';


--changeSet OPER-9449:12 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Duplicate Requirement button that''s required to create copies of requirements.' WHERE parm_name = 'ACTION_DUPLICATE_REQ';


--changeSet OPER-9449:13 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Duplicate Technical Reference button that''s required to create copies of technical references.' WHERE parm_name = 'ACTION_DUPLICATE_TECHNICAL_REFERENCE';


--changeSet OPER-9449:14 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Authority icon.' WHERE parm_name = 'ACTION_EDIT_AUTHORITY';


--changeSet OPER-9449:15 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor icon that''s required to modify part exchange vendor details.' WHERE parm_name = 'ACTION_EDIT_EXCHANGE_VENDOR_DETAILS';


--changeSet OPER-9449:16 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Inventory button to edit general information for inventory items such as serial number, applicability code, registration code, etc. There are four steps with links to pages for editing inventory. This permission is for Step 1. The parameters for the other three steps are:  Step 2: Edit subcomponent details -  ACTION_EDIT_INV_SUBITEMS.   Step 3: Edit inventory usage -  ACTION_EDIT_INV_USAGE.  Step 4: Edit task list for inventory - ACTION_EDIT_INV_TASKS.                                           Note, that on the Edit Inventory Step 1 page, to edit the received date and the manufactured date, users require another permission,  ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY.' WHERE parm_name = 'ACTION_EDIT_INV_DETAILS';


--changeSet OPER-9449:17 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the details of the subcomponents of inventory items. With this permission you can edit part numbers, serial numbers, OEM info, and ICN numbers. There are four steps or links to pages for editing inventory. This permission is for Step 2 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_INV_DETAILS.  Step 3: Edit inventory usage -  ACTION_EDIT_INV_USAGE.  Step 4: Edit task list for inventory - ACTION_EDIT_INV_TASKS.' WHERE parm_name = 'ACTION_EDIT_INV_SUBITEMS';


--changeSet OPER-9449:18 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the details of the tasks of inventory items. With this permission you can create tasks from task definitions or cancel  historical tasks (if applicable) and set tasks as not applicable. There are four steps or links to pages for editing inventory. This permission is for Step 4 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_INV_DETAILS.  Step 2: Edit subcomponent details -  ACTION_EDIT_INV_SUBITEMS.   Step 3: Edit inventory usage -  ACTION_EDIT_INV_USAGE.' WHERE parm_name = 'ACTION_EDIT_INV_TASKS';


--changeSet OPER-9449:19 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the usage values of  the subcomponents of inventory items. With this permission you can edit TSO (time since overhaul) and TSN (time since new) at install, TSO and TSN of highest component, and current TSO, TSI (time since install), and TSN. There are four steps or links to pages for editing inventory. This permission is for Step 3 . The parameters for the other three steps are:  Step 1: Edit inventory details - ACTION_EDIT_INV_DETAILS.  Step 2: Edit subcomponent details -  ACTION_EDIT_INV_SUBITEMS.   Step 4: Edit task list for inventory - ACTION_EDIT_INV_TASKS.' WHERE parm_name = 'ACTION_EDIT_INV_USAGE';


--changeSet OPER-9449:20 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor icon that''s required to modify repair vendor details.' WHERE parm_name = 'ACTION_EDIT_PART_REPAIR_VENDOR';


--changeSet OPER-9449:21 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to modify part specific constants.' WHERE parm_name = 'ACTION_EDIT_PART_SPEC_CONSTANTS';


--changeSet OPER-9449:22 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Scheduling Rule icon that''s required to removed scheduling rules from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_SCHEDULING_RULE';


--changeSet OPER-9449:23 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Step icon that''s required to remove steps from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_STEP';


--changeSet OPER-9449:24 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference icon that''s required to remove technical references from reference document definition.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_TECHNICAL_REFERENCE';


--changeSet OPER-9449:25 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Tool Requirement icon that''s required to remove tool requirements from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_TOOL_REQUIREMENT';


--changeSet OPER-9449:26 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Zone icon that''s required to remove zones from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_ZONE';


--changeSet OPER-9449:27 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Tail Number Specific Scheduling Rule icon that''s required to remove the selected rules from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_TAIL_NO_SPECIFIC_SCHED_RULE';


--changeSet OPER-9449:28 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a removed part to a part requirement.' WHERE parm_name = 'ACTION_REMOVE_REMOVED_PART';


--changeSet OPER-9449:29 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove repairable part.' WHERE parm_name = 'ACTION_REMOVE_REP_PART';


--changeSet OPER-9449:30 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachment button that''s required to remove attachments from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_REMOVE_RFQ_ATTACHMENT';


--changeSet OPER-9449:31 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Line buttone to remove lines from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_REMOVE_RFQ_LINE';


--changeSet OPER-9449:32 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference button that''s required to remove technical references from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_REMOVE_RFQ_TECH_REF';


--changeSet OPER-9449:33 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachment icon that''s required to remove attachments from shipments.' WHERE parm_name = 'ACTION_REMOVE_SHIPMENT_ATTACHMENT';


--changeSet OPER-9449:34 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Shipment Line button that''s required to remove line items from shipments.' WHERE parm_name = 'ACTION_REMOVE_SHIPMENT_LINE';


--changeSet OPER-9449:35 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference icon that''s required to removed topics (technical references) from shipments.' WHERE parm_name = 'ACTION_REMOVE_SHIPMENT_TECH_REF';


--changeSet OPER-9449:36 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit deadline for ad-hoc task or fault.' WHERE parm_name = 'ACTION_REMOVE_TASK_DEADLINE';


--changeSet OPER-9449:37 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachment button that''s required to remove attachments from user details.' WHERE parm_name = 'ACTION_REMOVE_USER_ATTACHMENT';


--changeSet OPER-9449:38 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Vendor icon that''s required to remove authorized repair and purchase vendors from warranty contracts (terms & conditions).' WHERE parm_name = 'ACTION_REMOVE_VENDOR';


--changeSet OPER-9449:39 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Account icon that''s required to remove accounts from vendors.' WHERE parm_name = 'ACTION_REMOVE_VENDOR_ACCOUNT';


--changeSet OPER-9449:40 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Airport icon that''s required to remove airport locations from vendors.' WHERE parm_name = 'ACTION_REMOVE_VENDOR_AIRPORT';


--changeSet OPER-9449:41 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachments icon that''s required to remove attachments from vendor details.' WHERE parm_name = 'ACTION_REMOVE_VENDOR_ATTACHMENT';


--changeSet OPER-9449:42 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Vendor button that''s required to remove vendors from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_REMOVE_VENDOR_FROM_RFQ';


--changeSet OPER-9449:43 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference icon that''s required to remove technical references from vendor details.' WHERE parm_name = 'ACTION_REMOVE_VENDOR_TECH_REF';


--changeSet OPER-9449:44 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachment icon that''s required to remove attachments from warranty contracts.' WHERE parm_name = 'ACTION_REMOVE_WARRANTY_ATTACHMENT';


--changeSet OPER-9449:45 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Reorder Measurements button that''s required to change the order of flight measurements on assemblies.' WHERE parm_name = 'ACTION_REORDER_MEASUREMENT';


--changeSet OPER-9449:46 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Reorder Parameters button that''s required to change the order of usage parameters in usage definitions for assemblies.' WHERE parm_name = 'ACTION_REORDER_USAGE_PARMS';


--changeSet OPER-9449:47 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Request approval for block.' WHERE parm_name = 'ACTION_REQUEST_APPROVAL_BLOCK';


--changeSet OPER-9449:48 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Request approval for JIC.' WHERE parm_name = 'ACTION_REQUEST_APPROVAL_JIC';


--changeSet OPER-9449:49 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Request approval for MPC.' WHERE parm_name = 'ACTION_REQUEST_APPROVAL_MPC';


--changeSet OPER-9449:50 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Request Approval button that''s required to request approval for reference document definitions.' WHERE parm_name = 'ACTION_REQUEST_APPROVAL_REFDOC';


--changeSet OPER-9449:51 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Request approval for requirement.' WHERE parm_name = 'ACTION_REQUEST_APPROVAL_REQ';


--changeSet OPER-9449:52 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to request engineering to analyze a fault.' WHERE parm_name = 'ACTION_REQUEST_ENGINEERING';


--changeSet OPER-9449:53 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to request parts for check/work order.' WHERE parm_name = 'ACTION_REQUEST_PARTS';


--changeSet OPER-9449:54 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Run Warranty Evaluation button that''s required to manually check whether the parts and labor in work packages are eligible for warranties.' WHERE parm_name = 'ACTION_REQUEST_WARRANTY_EVALUATION';


--changeSet OPER-9449:55 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to require RII-INSP labor to analyze a fault.' WHERE parm_name = 'ACTION_REQUIRE_RII';


--changeSet OPER-9449:56 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to resolve the reliability note on the inventory.' WHERE parm_name = 'ACTION_RESOLVE_INVENTORY_RELIABILITY_NOTE';


--changeSet OPER-9449:57 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Restart approval for block.' WHERE parm_name = 'ACTION_RESTART_APPROVAL_BLOCK';


--changeSet OPER-9449:58 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Restart approval for JIC.' WHERE parm_name = 'ACTION_RESTART_APPROVAL_JIC';


--changeSet OPER-9449:59 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Restart MPC approval.' WHERE parm_name = 'ACTION_RESTART_APPROVAL_MPC';


--changeSet OPER-9449:60 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Restart approval for reference doc.' WHERE parm_name = 'ACTION_RESTART_APPROVAL_REFDOC';


--changeSet OPER-9449:61 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Restart approval for requirement.' WHERE parm_name = 'ACTION_RESTART_APPROVAL_REQ';


--changeSet OPER-9449:62 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Return To Vendor button that''s required to return a piece of inventory to its original vendor.' WHERE parm_name = 'ACTION_RETURN_TO_VENDOR';


--changeSet OPER-9449:63 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to review the work captured by the technician while finishing or job stopping the labor requirement.' WHERE parm_name = 'ACTION_REVIEW_WORK_CAPTURED';


--changeSet OPER-9449:64 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to reroute a piece of inventory.' WHERE parm_name = 'ACTION_SCHEDULE_RESCHEDULE_CHECK';


--changeSet OPER-9449:65 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to scrap a piece of inventory.' WHERE parm_name = 'ACTION_SCRAP_INVENTORY';


--changeSet OPER-9449:66 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search for an attachment.' WHERE parm_name = 'ACTION_SEARCH_ATTACHMENT';


--changeSet OPER-9449:67 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the search icon that''s required to search for authorities.' WHERE parm_name = 'ACTION_SEARCH_AUTHORITY';


--changeSet OPER-9449:68 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new Part Catalog entries.' WHERE parm_name = 'ACTION_CREATE_PART';


--changeSet OPER-9449:69 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new inventory associated with a part number by clicking the Create Inventory button. This button appears on various pages (including Inventory Search By Type, Inventory Search, Part Details). After you click the button, the Create Inventory page opens and you can enter inventory details.' WHERE parm_name = 'ACTION_CREATE_INVENTORY';


--changeSet OPER-9449:70 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create an operator.' WHERE parm_name = 'ACTION_CREATE_OPERATOR';


--changeSet OPER-9449:71 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new Owner records.' WHERE parm_name = 'ACTION_CREATE_OWNER';


--changeSet OPER-9449:72 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new Part Catalog entries and assign them to task part requirements.' WHERE parm_name = 'ACTION_CREATE_PART_FOR_TASK';


--changeSet OPER-9449:73 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create kit part request for task part requirement.' WHERE parm_name = 'ACTION_CREATE_PART_REQUEST_FOR_KIT';


--changeSet OPER-9449:74 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create planning types.' WHERE parm_name = 'ACTION_CREATE_PLANNING_TYPE';


--changeSet OPER-9449:75 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create PO for Quote button that''s required to create a purchase order from an request for quote (RFQ) line in the list.' WHERE parm_name = 'ACTION_CREATE_PO_FROM_RFQ_LINE';


--changeSet OPER-9449:76 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Put Away button that''s required to create bin locations when putting away serviceable inventory items that don''t have an existing put-away storage locations. (For items that are not assigned to part requests and are therefore stored in the warehouse.)' WHERE parm_name = 'ACTION_CREATE_PUTAWAY';


--changeSet OPER-9449:77 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Reference Document button.' WHERE parm_name = 'ACTION_CREATE_REF_DOC';


--changeSet OPER-9449:78 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Reference Revision button that''s required to revise reference document definitions.' WHERE parm_name = 'ACTION_CREATE_REF_DOC_REVISION';


--changeSet OPER-9449:79 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create a removal task specifically to return inventory that was received via an exchange order. If the inventory item is faulty and cannot be repaired, so that the inventory can be sent back (returned) to the original owner.' WHERE parm_name = 'ACTION_CREATE_REMOVAL_TASK_FOR_RETURN_INVENTORY';


--changeSet OPER-9449:80 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create repetitive tasks for faults by clicking the Create Repetitive Task button on the Task Details page. If you defer a MEL fault, you might be required to create a repetitive inspection or servicing task to monitor faulty equipment until it''s fixed. You might also add a repetitive task to faults that were addressed if you want to verify that the corrective actions were taken, assess whether the problem has reoccurred, or evaluate when a permanent corrective action should be performed (after a temporary measure was performed).  This parameter is typically enabled for users that have permission to defer MEL faults.' WHERE parm_name = 'ACTION_CREATE_REPETITIVE_TASK';


--changeSet OPER-9449:81 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create RFQ button that''s required to create requests for quotes (RFQs).' WHERE parm_name = 'ACTION_CREATE_RFQ';


--changeSet OPER-9449:82 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Shipment button that''s required to move inventory items to and from supply locations or vendors.' WHERE parm_name = 'ACTION_CREATE_SHIPMENT';


--changeSet OPER-9449:83 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create a new status board.' WHERE parm_name = 'ACTION_CREATE_STATUS_BOARD';


--changeSet OPER-9449:84 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create stock numbers.' WHERE parm_name = 'ACTION_CREATE_STOCK';


--changeSet OPER-9449:85 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Task Warranty Contract button.' WHERE parm_name = 'ACTION_CREATE_TASK_WARRANTY_CONTRACT';


--changeSet OPER-9449:86 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create To Do List button that''s required to create to-do lists for roles.' WHERE parm_name = 'ACTION_CREATE_TODO_LIST';


--changeSet OPER-9449:87 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Transfer button that''s required to move inventory items between sub-locations within the same supply location.' WHERE parm_name = 'ACTION_CREATE_TRANSFER';


--changeSet OPER-9449:88 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create an unassigned adhoc task.' WHERE parm_name = 'ACTION_CREATE_UNASSIGNED_ADHOC_TASK';


--changeSet OPER-9449:89 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create unassigned faults by clicking the Raise Logbook Fault button on the Inventory Details, Open Faults tab. Unassigned faults do not belong to a work package. Users that don''t have this permission can still raise faults if they have the ACTION_CREATE_ASSIGNED_FAULT permission, but they can only do so within a work package.' WHERE parm_name = 'ACTION_CREATE_UNASSIGNED_FAULT';


--changeSet OPER-9449:90 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create an unassigned task from a definition.' WHERE parm_name = 'ACTION_CREATE_UNASSIGNED_TASK_FROM_DEFN';


--changeSet OPER-9449:91 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Usage Definition that''s required to add new usage definitions to assemblies.' WHERE parm_name = 'ACTION_CREATE_USAGE_DEFINITION';


--changeSet OPER-9449:92 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Parameter button that''s required to add new usage parameters to assemblies.' WHERE parm_name = 'ACTION_CREATE_USAGE_PARM';


--changeSet OPER-9449:93 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create a Usage Record for an Inventory item.' WHERE parm_name = 'ACTION_CREATE_USAGE_RECORD';


--changeSet OPER-9449:94 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create User button that''s required to create new users in Maintenix.' WHERE parm_name = 'ACTION_CREATE_USER';


--changeSet OPER-9449:95 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Vendor button.' WHERE parm_name = 'ACTION_CREATE_VENDOR';


--changeSet OPER-9449:96 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the De-Activate Warranty Contract button.' WHERE parm_name = 'ACTION_DEACTIVATE_WARRANTY_CONTRACT';


--changeSet OPER-9449:97 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to defer a fault with a severity of MEL.' WHERE parm_name = 'ACTION_DEFER_MEL_TASK';


--changeSet OPER-9449:98 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delay a check/work order.' WHERE parm_name = 'ACTION_DELAY_CHECK';


--changeSet OPER-9449:99 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delet alert.' WHERE parm_name = 'ACTION_DELETE_ALERT';


--changeSet OPER-9449:100 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Authority button that''s required to remove an authority. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_DELETE_AUTHORITY';


--changeSet OPER-9449:101 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Calculated Parameter button that''s required to delete the calculated parm.' WHERE parm_name = 'ACTION_DELETE_CALCULATED_PARM';


--changeSet OPER-9449:102 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Config Slot button that''s required to delete config slot.' WHERE parm_name = 'ACTION_DELETE_CONFIG_SLOT';


--changeSet OPER-9449:103 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Deferral Reference button.' WHERE parm_name = 'ACTION_DELETE_DEFER_REF';


--changeSet OPER-9449:104 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete a failure record.' WHERE parm_name = 'ACTION_DELETE_FAILURE_EFFECT';


--changeSet OPER-9449:105 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete fault definition.' WHERE parm_name = 'ACTION_DELETE_FAULT_DEFINITION';


--changeSet OPER-9449:106 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete fault threshold.' WHERE parm_name = 'ACTION_DELETE_FAULT_THRESHOLD';


--changeSet OPER-9449:107 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete an ietm.' WHERE parm_name = 'ACTION_DELETE_IETM';


--changeSet OPER-9449:108 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete a Manufacturer record.' WHERE parm_name = 'ACTION_DELETE_MANUFACTURER';


--changeSet OPER-9449:109 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete a menu group.' WHERE parm_name = 'ACTION_DELETE_MENU';


--changeSet OPER-9449:110 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete operator.' WHERE parm_name = 'ACTION_DELETE_OPERATOR';


--changeSet OPER-9449:111 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete an Owner record.' WHERE parm_name = 'ACTION_DELETE_OWNER';


--changeSet OPER-9449:112 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to delete a Part Catalog entry (from BUILD).' WHERE parm_name = 'ACTION_DELETE_PART';


--changeSet OPER-9449:113 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Delete Reference Document button.' WHERE parm_name = 'ACTION_DELETE_REF_DOC';


--changeSet OPER-9449:114 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to generate workscopes for work packages.  Generating a workscope does not request materials or commit the work package.' WHERE parm_name = 'ACTION_GENERATE_WORKSCOPE';


--changeSet OPER-9449:115 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to generate user certificates for use when recording electonic signatures.' WHERE parm_name = 'ACTION_GENERATE_CERTIFICATE';


--changeSet OPER-9449:116 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Print Shipping Form button that''s required to print shipping reports that are attached to shipping packages.' WHERE parm_name = 'ACTION_GENERATE_SHIPPING_FORM';


--changeSet OPER-9449:117 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Print Transfer Ticket, Print Issue Ticket, and Print Put Away Ticket buttons. These buttons are used to print reports that are attached to inventory items that are moved between locations within the same supply location.' WHERE parm_name = 'ACTION_GENERATE_TRANSFER_TICKET';


--changeSet OPER-9449:118 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add historic custom part requirements.' WHERE parm_name = 'ACTION_HIST_ADD_CUST_PART_REQUIREMENT';


--changeSet OPER-9449:119 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit historic task cust part requirements.' WHERE parm_name = 'ACTION_HIST_EDIT_CUST_PART_REQUIREMENT';


--changeSet OPER-9449:120 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove historic cust part requirements.' WHERE parm_name = 'ACTION_HIST_RMV_CUST_PART_REQUIREMENT';


--changeSet OPER-9449:121 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to ignore count discrepancies.  When a discrepancy is ignored, it no longer appears on the discrepancy list.' WHERE parm_name = 'ACTION_IGNORE_DISCREPANCY';


--changeSet OPER-9449:122 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to ignore tasks with a scheduling rule of ''Next Shop Visit'' during execution of work packages.  Normally, these tasks would need to be performed during the next visit, but users that have this permission, can override the rule.' WHERE parm_name = 'ACTION_IGNORE_NSV_TASKS_DURING_EXECUTION';


--changeSet OPER-9449:123 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Initialize Block button that''s required to initialize blocks and push them out to the fleet.' WHERE parm_name = 'ACTION_INITIALIZE_BLOCK';


--changeSet OPER-9449:124 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Initialize Warranty Contract button that''s required to manually initialize warranties on specific inventory items.' WHERE parm_name = 'ACTION_INITIALIZE_INVENTORY_WARRANTY';


--changeSet OPER-9449:125 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Initialize Reference button that''s required to initialize reference documents. Initializing pushes reference documents out to the fleet.' WHERE parm_name = 'ACTION_INITIALIZE_REF_DOC';


--changeSet OPER-9449:126 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the initialize requirements. Initializing pushes requirements out to the fleet.' WHERE parm_name = 'ACTION_INITIALIZE_REQ';


--changeSet OPER-9449:127 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to initialize task definitions.' WHERE parm_name = 'ACTION_INITIALIZE_TASK_DEFINITION';


--changeSet OPER-9449:128 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Initialize Warranty Contract button that''s required to initialize task warranties.' WHERE parm_name = 'ACTION_INITIALIZE_TASK_WARRANTY';


--changeSet OPER-9449:129 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to inspect an Inventory item as serviceable.' WHERE parm_name = 'ACTION_INSPECT_AS_SERVICEABLE';


--changeSet OPER-9449:130 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to record pass or rejection result of task inspections.' WHERE parm_name = 'ACTION_INSPECT_TASK';


--changeSet OPER-9449:131 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Turn In button that''s required to manually transfer inventory items from the maintenance floor to the storeroom.' WHERE parm_name = 'ACTION_INV_TURN_IN';


--changeSet OPER-9449:132 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Ad Hoc Issue Inventory button that''s required to issue inventory to ad hoc part requests.' WHERE parm_name = 'ACTION_ISSUE_ADHOC';


--changeSet OPER-9449:133 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Issue Inventory button that''s required to transfer reserved inventory items from the storeroom to the locations where they will be used.' WHERE parm_name = 'ACTION_ISSUE_INVENTORY';


--changeSet OPER-9449:134 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Print Vendor Reliability button that''s required to see and print vendor reliability reports.' WHERE parm_name = 'ACTION_PRINT_VENDOR_RELIABILITY';


--changeSet OPER-9449:135 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the status of a task definition to Do Not Execute by clicking the Prevent Execution button on the Requirement Details page. Tasks with Do Not Execute status cannot be moved into committed work packages (or, work packages that contain a task with this status cannot be committed). If a task is in a committed work package when the task status is changed to Do Not Execute, then the task can be performed. Users that try to create a task based on a task definition with Do Not Execute status, see a warning. Prevent Execution is a toggle button whose opposite, Allow Execution, is controlled with the ACTION_ALLOW_TASK_DEFINITION_EXECUTION parameter.' WHERE parm_name = 'ACTION_PREVENT_TASK_DEFINITION_EXECUTION';


--changeSet OPER-9449:136 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Print Pick List button that''s required to print the pick list of items that must be collected for a shipment.' WHERE parm_name = 'ACTION_PRINT_PICK_LIST';


--changeSet OPER-9449:137 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Print License Card button that''s required to print users'' license cards.' WHERE parm_name = 'ACTION_PRINT_USER_LICENSE';


--changeSet OPER-9449:138 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Put Away Inventory button that''s required to manually store serviceable inventory items that arrive in the dock and need to be stored in the warehouse because they are not assigned to part requests.' WHERE parm_name = 'ACTION_PUTAWAY_INVENTORY';


--changeSet OPER-9449:139 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Link To Task icon that''s required to remove linked tasks from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_LINK_FROM_REF_DOC';


--changeSet OPER-9449:140 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference icon that''s required to remove references job card definitions.' WHERE parm_name = 'ACTION_REMOVE_JIC_TECHNICAL_REFERENCE';


--changeSet OPER-9449:141 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove kit content.' WHERE parm_name = 'ACTION_REMOVE_KIT_CONTENT';


--changeSet OPER-9449:142 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a labour requirement from a task.' WHERE parm_name = 'ACTION_REMOVE_LABOUR_REQUIREMENT';


--changeSet OPER-9449:143 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Location icon that''s required to remove authorized internal repair locations from warranty contract details (terms & conditions).' WHERE parm_name = 'ACTION_REMOVE_LOCATION';


--changeSet OPER-9449:144 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove non-repairable part.' WHERE parm_name = 'ACTION_REMOVE_NON_REP_PART';


--changeSet OPER-9449:145 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove part.' WHERE parm_name = 'ACTION_REMOVE_PART';


--changeSet OPER-9449:146 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove the BOM applicability of a Part No.' WHERE parm_name = 'ACTION_REMOVE_PART_APPLICABILITY';


--changeSet OPER-9449:147 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Repair Vendor button that''s required to remove vendors from the list of vendors that can do repairs for a part.' WHERE parm_name = 'ACTION_REMOVE_PART_REPAIR_VENDOR';


--changeSet OPER-9449:148 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a part requirement from a task.' WHERE parm_name = 'ACTION_REMOVE_PART_REQUIREMENT';


--changeSet OPER-9449:149 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove planning types.' WHERE parm_name = 'ACTION_REMOVE_PLANNING_TYPE';


--changeSet OPER-9449:150 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove an attachment from a po invoice.' WHERE parm_name = 'ACTION_REMOVE_PO_INVOICE_ATTACHMENT';


--changeSet OPER-9449:151 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a technical reference from a po invoice.' WHERE parm_name = 'ACTION_REMOVE_PO_INVOICE_TECH_REF';


--changeSet OPER-9449:152 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Purchase Vendor button that''s required to remove vendors that are listed for part purchases.' WHERE parm_name = 'ACTION_REMOVE_PURCHASE_VENDOR';


--changeSet OPER-9449:153 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a query.' WHERE parm_name = 'ACTION_REMOVE_QUERY';


--changeSet OPER-9449:154 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Attachment icon that''s required to remove attachments from  reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_ATTACHMENT';


--changeSet OPER-9449:155 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Following Task icon that''s required to remove following tasks from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_FOLLOWING_TASK';


--changeSet OPER-9449:156 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Impacts icon that''s required to remove impacts from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_IMPACT';


--changeSet OPER-9449:157 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Labor Requirement icon that''s required to remove labor requirements from  reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_LABOUR_REQUIREMENT';


--changeSet OPER-9449:158 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Measurement icon that''s required to unassign measurements from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_MEASUREMENT';


--changeSet OPER-9449:159 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Measurement Specific Scheduling Rule icon that''s required to remove rules from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_MEASURE_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:160 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Panel icon that''s required to remove panels from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_PANEL';


--changeSet OPER-9449:161 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Part Specific Scheduling Rule icon that''s required to remove rules from reference document definitions.' WHERE parm_name = 'ACTION_REMOVE_REF_DOC_PART_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:162 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to select the After The Fact Receipt radio button that''s required to bypass shipping warnings and receive inventory. This button may be used when materials support was not available and tracked or serialized parts were physically received and installed before receipt of the items was processed in Maintenix.  To select the Maintenix Record Error radio button on the same page, roles require the ACTION_RECEIVE_INV_RECORD_ERROR permission.' WHERE parm_name = 'ACTION_RECEIVE_INV_AFTER_THE_FACT';


--changeSet OPER-9449:163 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Maintenix Record Error radio button that''s required to bypass shipping warnings and receive inventory. This warning appears when shipping clerks attempt to process receipt of an item in Maintenix, but an inventory item with the exact same part number and serial number already exists in Maintenix.  To select the After the Fact Receipt radio button on the same page, roles require the  ACTION_RECEIVE_INV_AFTER_THE_FACT permission.' WHERE parm_name = 'ACTION_RECEIVE_INV_RECORD_ERROR';


--changeSet OPER-9449:164 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Receive Shipment button that''s required to receive shipments of inventory. To see the button on the PO Details page, the purchase order must be issued.' WHERE parm_name = 'ACTION_RECEIVE_SHIPMENT';


--changeSet OPER-9449:165 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Lock Reference Document button that''s required if you want to lock documents while they are in build or revision status. Lock Reference Document is a toggle button whose opposite, Unlock Reference Document, is controlled with the ACTION_REF_DOC_UNLOCK permission.' WHERE parm_name = 'ACTION_REF_DOC_LOCK';


--changeSet OPER-9449:166 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unlock Reference Document button.  This is a toggle button whose opposite, Lock Reference Document, is controlled with the ACTION_REF_DOC_LOCK permission.' WHERE parm_name = 'ACTION_REF_DOC_UNLOCK';


--changeSet OPER-9449:167 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'This parameter is obsolete.' WHERE parm_name = 'ACTION_REINDUCT_INVENTORY';


--changeSet OPER-9449:168 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Reject Warranty button that''s required to reject warranties against assemblies, components and work packages.' WHERE parm_name = 'ACTION_REJECT_WARRANTY';


--changeSet OPER-9449:169 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove asynchronous action records (such as task batch complete job) from the system file utl_async_action without logging into the database, i.e. from the GUI. Typically enabled for system configuration and user administrator roles.' WHERE parm_name = 'ACTION_REMOVE_ASYNC_ACTION';


--changeSet OPER-9449:170 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to issue and raise fault for inventory.' WHERE parm_name = 'ACTION_ISSUE_RAISE_FAULT';


--changeSet OPER-9449:171 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Issue Inventory to Task button.' WHERE parm_name = 'ACTION_ISSUE_TO_TASK';


--changeSet OPER-9449:172 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to lock and unlock the records of Inventory items.' WHERE parm_name = 'ACTION_LOCK_INVENTORY';


--changeSet OPER-9449:173 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'permission required for locking down the actual costs on a work package.' WHERE parm_name = 'ACTION_LOCK_COST_LINE_ITEMS_ACTUAL';


--changeSet OPER-9449:174 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'permission required for locking down the expected costs on a work package.' WHERE parm_name = 'ACTION_LOCK_COST_LINE_ITEMS_EXP';


--changeSet OPER-9449:175 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'permission required for required for locking down the initial expected costs on a work package.' WHERE parm_name = 'ACTION_LOCK_COST_LINE_ITEMS_INIT_EXP';


--changeSet OPER-9449:176 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unlock User and Lock User buttons that are required to set or remove locks on users'' accounts. (Locked users cannot access Maintenix.) This is a toggle button.' WHERE parm_name = 'ACTION_LOCK_UNLOCK_USER';


--changeSet OPER-9449:177 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to log fault and close/fix immediately.' WHERE parm_name = 'ACTION_LOG_FAULT_AND_CLOSE';


--changeSet OPER-9449:178 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to log fault and leave open.' WHERE parm_name = 'ACTION_LOG_FAULT_AND_LEAVE_OPEN';


--changeSet OPER-9449:179 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to log fault and schedule work immediately.' WHERE parm_name = 'ACTION_LOG_FAULT_AND_SCHEDULE';


--changeSet OPER-9449:180 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to setup deadline extensions.' WHERE parm_name = 'ACTION_MAINT_PROGRAM_SETUP_DEADLINE_EXTENSION';


--changeSet OPER-9449:181 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to  Mark Adjusted For Billing.' WHERE parm_name = 'ACTION_MARK_AS_ADJUST_BILLING';


--changeSet OPER-9449:182 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark the inventory as found when it has been marked as Not In Stock.' WHERE parm_name = 'ACTION_MARK_AS_FOUND';


--changeSet OPER-9449:183 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark event error.' WHERE parm_name = 'ACTION_MARK_EVENT_ERROR';


--changeSet OPER-9449:184 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to  Mark Fault In Error.' WHERE parm_name = 'ACTION_MARK_FAULT_AS_ERROR';


--changeSet OPER-9449:185 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark a piece of inventory as repair required.' WHERE parm_name = 'ACTION_MARK_INV_AS_REPAIR_REQUIRED';


--changeSet OPER-9449:186 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Mark As No Warranty button that''s required to mark lines in purchase orders.' WHERE parm_name = 'ACTION_MARK_NO_WARRANTY';


--changeSet OPER-9449:187 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to  Mark Task In Error.' WHERE parm_name = 'ACTION_MARK_TASK_AS_ERROR';


--changeSet OPER-9449:188 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Mark Vendor Account As Default icon.' WHERE parm_name = 'ACTION_MARK_VENDOR_ACCOUNT_AS_DEFAULT';


--changeSet OPER-9449:189 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the Mark Airport as Default icon that''s required to select one of multiple airports that are assigned to vendors, as the default airport.' WHERE parm_name = 'ACTION_MARK_VENDOR_AIRPORT_AS_DEFAULT';


--changeSet OPER-9449:190 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to merge possible faults together.' WHERE parm_name = 'ACTION_MERGE_POSSIBLE_FAULTS';


--changeSet OPER-9449:191 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to modify aircraft capability when raising and deferring a fault.' WHERE parm_name = 'ACTION_MODIFY_ACFT_CAPABILITY';


--changeSet OPER-9449:192 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to Move Down the workscope order.' WHERE parm_name = 'ACTION_MOVE_DOWN_WORKSCOPE_ORDER';


--changeSet OPER-9449:193 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to move Inventory items to quarantine area.' WHERE parm_name = 'ACTION_MOVE_INV_TO_QUARANTINE';


--changeSet OPER-9449:194 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Move Reference Document button that''s required to move reference documents from one config slot to another.' WHERE parm_name = 'ACTION_MOVE_REF_DOC';


--changeSet OPER-9449:195 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Move to Dock button that''s required to move inventory items for selected lines in shipments to the dock location.' WHERE parm_name = 'ACTION_MOVE_SHIPMENT_LINE_INVENTORY';


--changeSet OPER-9449:196 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to transfer inventory from the unserviceable staging to the work location.' WHERE parm_name = 'ACTION_MOVE_TO_SHOP';


--changeSet OPER-9449:197 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to Move Up the  workscope order.' WHERE parm_name = 'ACTION_MOVE_UP_WORKSCOPE_ORDER';


--changeSet OPER-9449:198 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark a corrective task as No Fault Found.' WHERE parm_name = 'ACTION_NO_FAULT_FOUND';


--changeSet OPER-9449:199 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Obsolete button that''s required to mark vendor part prices as obsolete/historic.' WHERE parm_name = 'ACTION_OBSOLESCE_VENDOR_PART_PRICE';


--changeSet OPER-9449:200 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Obsolete Block button.' WHERE parm_name = 'ACTION_OBSOLETE_BLOCK';


--changeSet OPER-9449:201 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Obsolete Job Card button.' WHERE parm_name = 'ACTION_OBSOLETE_JIC';


--changeSet OPER-9449:202 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the status of a license definition to OBSOLETE.' WHERE parm_name = 'ACTION_OBSOLETE_LICENSE';


--changeSet OPER-9449:203 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the status of a master panel card to OBSOLETE.' WHERE parm_name = 'ACTION_OBSOLETE_MASTER_PANEL_CARD';


--changeSet OPER-9449:204 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Reject Part button that''s required to change the status of a Part Catalog entry from ACTIVE to OBSOLETE.' WHERE parm_name = 'ACTION_OBSOLETE_PART';


--changeSet OPER-9449:205 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Obsolete Reference button that''s required to make reference documents that are in REVISION status obsolete.' WHERE parm_name = 'ACTION_OBSOLETE_REF_DOC';


--changeSet OPER-9449:206 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Obsolete Requirement button.' WHERE parm_name = 'ACTION_OBSOLETE_REQ';


--changeSet OPER-9449:207 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit assembly thresholds.' WHERE parm_name = 'ACTION_OIL_CONSUMPTION_ASSEMBLY_THRESHOLD';


--changeSet OPER-9449:208 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to define oil consumption rate definitions.' WHERE parm_name = 'ACTION_OIL_CONSUMPTION_RATE_DEFINITION';


--changeSet OPER-9449:209 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to order columns.' WHERE parm_name = 'ACTION_ORDER_COLUMN';


--changeSet OPER-9449:210 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to package and complete tasks by clicking the Package And Complete Task button on the Open Tasks tab of the Inventory Details page. When you click the button, the Complete Task page opens and you can enter location, date, and usage information for the parent and all sub-assemblies. Visibility of the button depends on the context (aircraft or uninstalled component) and the status of the  ACTION_CREATE_COMPONENT_WORK_ORDER and ACTION_CREATE_BLANK_CHECK permissions.' WHERE parm_name = 'ACTION_PACKAGE_AND_COMPLETE_TASK';


--changeSet OPER-9449:211 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to set the parts not ready flag for tasks.' WHERE parm_name = 'ACTION_PARTS_NOT_READY';


--changeSet OPER-9449:212 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to set the parts ready flag for tasks.' WHERE parm_name = 'ACTION_PARTS_READY';


--changeSet OPER-9449:213 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to pause tasks.' WHERE parm_name = 'ACTION_PAUSE_TASK';


--changeSet OPER-9449:214 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to clear faults from fault lists.' WHERE parm_name = 'ACTION_PDA_CLEAR_FAULT';


--changeSet OPER-9449:215 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to clear tasks from task lists.' WHERE parm_name = 'ACTION_PDA_CLEAR_TASK';


--changeSet OPER-9449:216 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to download tasks to a PDA (Personal Ditigal or Data Assitant device).' WHERE parm_name = 'ACTION_PDA_DOWNLOAD_TASK';


--changeSet OPER-9449:217 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to synchronize the baseline between a PDA  (Personal Ditigal or Data Assitant device) and Maintenix.' WHERE parm_name = 'ACTION_PDA_SYNCHRONIZE_BASELINE';


--changeSet OPER-9449:218 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to uncomplete tasks in a PDA (Personal Ditigal or Data Assitant device) that are not yet uploaded to Maintenix.' WHERE parm_name = 'ACTION_PDA_UNCOMPLETE_TASK';


--changeSet OPER-9449:219 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to upload tasks and faults from a PDA (Personal Ditigal or Data Assitant device) to Maintenix.' WHERE parm_name = 'ACTION_PDA_UPLOAD';


--changeSet OPER-9449:220 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to perform the corrections when viewing recount results of a count discrepency.' WHERE parm_name = 'ACTION_PERFORM_CORRECTIONS';


--changeSet OPER-9449:221 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create, cancel, and perform corrective actions by clicking the Corrective Actions button on the Task Details page of a non-historic fault. When you click the button, you can then create subtasks, unassign subtasks, and mark subtasks as successful. If you unassign subtasks, the subtask is cancelled.' WHERE parm_name = 'ACTION_PERFORM_CORRECTIVE_ACTIONS';


--changeSet OPER-9449:222 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to perform cycle counts.' WHERE parm_name = 'ACTION_PERFORM_COUNT';


--changeSet OPER-9449:223 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to perform a recount on a count discrepancy.' WHERE parm_name = 'ACTION_PERFORM_RECOUNT';


--changeSet OPER-9449:224 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to pick items for kits.' WHERE parm_name = 'ACTION_PICK_ITEMS_FOR_KIT';


--changeSet OPER-9449:225 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Pick Items For Shipment button that''s required to select items for outbound shipments.' WHERE parm_name = 'ACTION_PICK_SHIPMENT';


--changeSet OPER-9449:226 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to plan the upcoming shifts within a check/work order.' WHERE parm_name = 'ACTION_PLAN_SHIFT';


--changeSet OPER-9449:227 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to prevent an ACFT or ASSY to be allowed to use baseline sync.' WHERE parm_name = 'ACTION_PREVENT_BASELINE_SYNCHRONIZATION';


--changeSet OPER-9449:228 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to prevent airdraft from being scheduled by Line Planning Automation.' WHERE parm_name = 'ACTION_PREVENT_LPA_AIRCRAFT';


--changeSet OPER-9449:229 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to prevent work packages from being hidden from Line Planning Automation.' WHERE parm_name = 'ACTION_PREVENT_LPA_WORK_PACKAGE';


--changeSet OPER-9449:230 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for parts.' WHERE parm_name = 'ACTION_SEARCH_PART';


--changeSet OPER-9449:231 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the seach button that''s required to search for part requests.' WHERE parm_name = 'ACTION_SEARCH_PART_REQ';


--changeSet OPER-9449:232 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for purchase orders.' WHERE parm_name = 'ACTION_SEARCH_PO';


--changeSet OPER-9449:233 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for requirements.' WHERE parm_name = 'ACTION_SEARCH_REQUIRIMENT';


--changeSet OPER-9449:234 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for roles.' WHERE parm_name = 'ACTION_SEARCH_ROLE';


--changeSet OPER-9449:235 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for shipments.' WHERE parm_name = 'ACTION_SEARCH_SHIPMENT';


--changeSet OPER-9449:236 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for stock.' WHERE parm_name = 'ACTION_SEARCH_STOCK';


--changeSet OPER-9449:237 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for tasks.' WHERE parm_name = 'ACTION_SEARCH_TASK';


--changeSet OPER-9449:238 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the button that''s required to search for task definitions.' WHERE parm_name = 'ACTION_SEARCH_TASK_DEFINITION';


--changeSet OPER-9449:239 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for technical references.' WHERE parm_name = 'ACTION_SEARCH_TECHNICAL_REFERENCE';


--changeSet OPER-9449:240 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search for a tool by using a barcode on Check Out/In Tool page.' WHERE parm_name = 'ACTION_SEARCH_TOOLS_W_BARCODE';


--changeSet OPER-9449:241 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for transfers.' WHERE parm_name = 'ACTION_SEARCH_TRANSFER';


--changeSet OPER-9449:242 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for open part requests.' WHERE parm_name = 'ACTION_SEARCH_UNRSRV_RSRV';


--changeSet OPER-9449:243 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the search button that''s required to search for users.' WHERE parm_name = 'ACTION_SEARCH_USER';


--changeSet OPER-9449:244 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission see the Search icon that''s required to search for vendors.' WHERE parm_name = 'ACTION_SEARCH_VENDOR';


--changeSet OPER-9449:245 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Send RFQ button that''s required to send inventory items for external repair.' WHERE parm_name = 'ACTION_SEND_INV_EXTERNAL';


--changeSet OPER-9449:246 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the OK button that''s required to send integration messages.' WHERE parm_name = 'ACTION_SEND_MESSAGE';


--changeSet OPER-9449:247 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Send RFQ button that''s required to send requests for quotes to vendors.' WHERE parm_name = 'ACTION_SEND_RFQ';


--changeSet OPER-9449:248 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Send Shipment button.' WHERE parm_name = 'ACTION_SEND_SHIPMENT';


--changeSet OPER-9449:249 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the View Impact Report button that''s required to set up deadline extensions for actual tasks associated with requirement definitions that are in revision status.' WHERE parm_name = 'ACTION_SETUP_DEADLINE_EXTENSION';


--changeSet OPER-9449:250 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Alert Priority button that''s required to set the priority of alert types.' WHERE parm_name = 'ACTION_SET_ALERT_TYPE_PRIORITY';


--changeSet OPER-9449:251 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Default Printer icon that''s required to set the default printer for locations. If you want to print task cards, you set the default printer details in the location details for the task.' WHERE parm_name = 'ACTION_SET_AS_DEFAULT_PRINTER';


--changeSet OPER-9449:252 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set As Preferred Vendor button that''s required to set preferred vendors for part exchange orders.  There are separate permissions for setting preferred repair and purchase vendors. See ACTION_SET_PREF_PART_REPAIR_VENDOR and ACTION_SET_PREF_PURCHASE_VENDOR.' WHERE parm_name = 'ACTION_SET_AS_PREFERRED_VENDOR';


--changeSet OPER-9449:253 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Issue to Accounts button that''s required to select the account that will be charged when inventory is issued to tasks in work packages. Related access permissions for servlets and pages are required.' WHERE parm_name = 'ACTION_SET_ISSUE_TO_ACCOUNTS';


--changeSet OPER-9449:254 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Next Count Date button that''s required to specify when to count inventory that''s located in selected bins.' WHERE parm_name = 'ACTION_SET_NEXT_COUNT_DATE';


--changeSet OPER-9449:255 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Plan By Date button that''s required to set a different date than the due date for active tasks.' WHERE parm_name = 'ACTION_SET_PLAN_BY_DATE';


--changeSet OPER-9449:256 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Plan By Dates button that''s required to set a different date than the due date for active tasks associated with requirement definitions.' WHERE parm_name = 'ACTION_SET_PLAN_BY_DATES';


--changeSet OPER-9449:257 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Preferred Vendor button that''s required to set preferred vendors for part repair orders.  There are separate permissions for setting preferred part exhcange vendors (ACTION_SET_AS_PREFERRED_VENDOR) and preferred purchase vendors (ACTION_SET_PREF_PURCHASE_VENDOR.' WHERE parm_name = 'ACTION_SET_PREF_PART_REPAIR_VENDOR';


--changeSet OPER-9449:258 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Preferred Vendor button that''s required to set preferred vendors for part purchase orders.  There are separate permissions for setting preferred part exhcange vendors (ACTION_SET_AS_PREFERRED_VENDOR) and preferred repair vendors (ACTION_SET_PREF_PART_REPAIR_VENDOR).' WHERE parm_name = 'ACTION_SET_PREF_PURCHASE_VENDOR';


--changeSet OPER-9449:259 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to set the repair order line numbers for a task.' WHERE parm_name = 'ACTION_SET_REPAIR_ORDER_LINE_NUMBERS';


--changeSet OPER-9449:260 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Set Task Priority button that''s required to set the priority level of a task.  Note that if a user has the ACTION_EDIT_TASK_INFORMATION permission, they can set task priority without having the ACTION_SET_TASK_PRIORITY permission.' WHERE parm_name = 'ACTION_SET_TASK_PRIORITY';


--changeSet OPER-9449:261 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Start Work Package button that''s required to start work packages (check or work order).' WHERE parm_name = 'ACTION_START_CHECK';


--changeSet OPER-9449:262 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign tasks from work packages.' WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_CHECK';


--changeSet OPER-9449:263 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see applicability range data that is displayed on task definition Detail pages > Applicability tab.' WHERE parm_name = 'ACTION_SHOW_APPLICABILITY_RANGE';


--changeSet OPER-9449:264 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Show Differences button that''s required to view a highlighted comparison of the differences between revisions of task definitions.' WHERE parm_name = 'ACTION_SHOW_DIFFERENCES';


--changeSet OPER-9449:265 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view task labor summaries.' WHERE parm_name = 'ACTION_SHOW_TASK_LABOUR_SUMMARY';


--changeSet OPER-9449:266 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to sign off a check or work order.' WHERE parm_name = 'ACTION_SIGN_CHECK_OR_WO';


--changeSet OPER-9449:267 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Start button that''s required to start labor rows in tasks.' WHERE parm_name = 'ACTION_START_TASK';


--changeSet OPER-9449:268 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Submit Claim button that''s required to submit warranty claims.' WHERE parm_name = 'ACTION_SUBMIT_CLAIM';


--changeSet OPER-9449:269 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Suspend User License icon that''s required to suspend active licenses that are assigned to users.' WHERE parm_name = 'ACTION_SUSPEND_USER_LICENSE';


--changeSet OPER-9449:270 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Synchronize Maintenance button that''s required to flag all of an aircraft''s ACFT or ASSY subconfig slots as SYNCH REQUIRED. The next time the baseline synchronization job runs, ACFT or ASSY subconfig slot tasks are canceled, updated, or initialized to match changes made to the baseline. This button is mainly required when first inducting an aircraft.' WHERE parm_name = 'ACTION_SYNCHRONIZE_MAINTENANCE';


--changeSet OPER-9449:271 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Synchronize Usage Parameter button that''s required to apply new baseline usage parameters to existing inventory.' WHERE parm_name = 'ACTION_SYNCH_USAGE_PARMS';


--changeSet OPER-9449:272 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Tag Requirement button that''s required to tag task definitions.' WHERE parm_name = 'ACTION_TAG_TASK_DEFINITION';


--changeSet OPER-9449:273 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Terminate Task button.' WHERE parm_name = 'ACTION_TERMINATE_TASK';


--changeSet OPER-9449:274 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Toggle All Authority button that''s required to enable or remove all authority for selected users. This button is a toggle. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_TOGGLE_ALL_AUTHORITY';


--changeSet OPER-9449:275 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign and to unassign requests.' WHERE parm_name = 'ACTION_TOGGLE_ASSIGN_REQUEST';


--changeSet OPER-9449:276 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to enable/disable Enforce Workscope Order.' WHERE parm_name = 'ACTION_TOGGLE_ENF_WORKSCOPE_ORDER';


--changeSet OPER-9449:277 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit soft deadlines for ad-hoc tasks or faults.' WHERE parm_name = 'ACTION_TOGGLE_SOFT_DEADLINE';


--changeSet OPER-9449:278 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Tools Not Ready button that''s required to clear the Tools Ready check box for tasks. (This is a toggle button whose opposite is Tools Ready).' WHERE parm_name = 'ACTION_TOOLS_NOT_READY';


--changeSet OPER-9449:279 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Tools Ready button that''s required to select the Tools Ready check box for tasks. (This is a toggle button whose opposite is Tools Not Ready).' WHERE parm_name = 'ACTION_TOOLS_READY';


--changeSet OPER-9449:280 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unapprove Alternate Parts button to disapprove a part for the part-group.  The ACTION_APPROVE_PART_FOR_GROUP is required for the opposite action.' WHERE parm_name = 'ACTION_UNAPPROVE_PART_FOR_GROUP';


--changeSet OPER-9449:281 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unapprove Vendor button that''s required to indicate that vendors are not approved for orders, such as purchase, repair, exchange. (The same vendor can be unapproved for repairs, but approved for exchange).' WHERE parm_name = 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE';


--changeSet OPER-9449:282 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unapprove Vendor button that''s required to indicate that services performed by vendors are unacceptable. The same vendor can be approved for one type of service, but unapproved for another type of service.' WHERE parm_name = 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE';


--changeSet OPER-9449:283 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unarchive Message button that''s required to unarchive an integration message.' WHERE parm_name = 'ACTION_UNARCHIVE_MESSAGE';


--changeSet OPER-9449:284 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Airport button that''s required to unassign airports from vendor-part exchange relationships.  The opposite action is controlled with the ACTION_ASSIGN_AIRPORT permission.' WHERE parm_name = 'ACTION_UNASSIGN_AIRPORT';


--changeSet OPER-9449:285 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Alert Role button that''s required to unassign roles that are alerted when faults are deferred.' WHERE parm_name = 'ACTION_UNASSIGN_ALERT_ROLE';


--changeSet OPER-9449:286 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Approval Level button that''s required to unassign an approval level from a task definition approval workflow.' WHERE parm_name = 'ACTION_UNASSIGN_APPROVAL_LEVEL';


--changeSet OPER-9449:287 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Authority button that''s required to unassign authorities from users and restrict their access to data. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)  The opposite action is controlled with the ACTION_ASSIGN_AUTHORITY permission.' WHERE parm_name = 'ACTION_UNASSIGN_AUTHORITY';


--changeSet OPER-9449:288 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign a capability from a location.' WHERE parm_name = 'ACTION_UNASSIGN_CAPABILITY';


--changeSet OPER-9449:289 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign companies from users.' WHERE parm_name = 'ACTION_UNASSIGN_COMPANY';


--changeSet OPER-9449:290 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Data Value button that''s required to remove data values from measurements in baseline definitions.' WHERE parm_name = 'ACTION_UNASSIGN_DATA_VALUE';


--changeSet OPER-9449:291 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Department button that''s required to unassign departments from locations.' WHERE parm_name = 'ACTION_UNASSIGN_DEPARTMENT';


--changeSet OPER-9449:292 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Location button that''s required to unassign locations from departments.' WHERE parm_name = 'ACTION_UNASSIGN_DEPARTMENT_LOCATIONS';


--changeSet OPER-9449:293 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign User button that''s required to unassign users from departments.' WHERE parm_name = 'ACTION_UNASSIGN_DEPARTMENT_USERS';


--changeSet OPER-9449:294 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Exchange Vendor button that''s required to unassign vendors that can be used for part exchanges.' WHERE parm_name = 'ACTION_UNASSIGN_EXCHANGE_PART_VENDOR';


--changeSet OPER-9449:295 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign failure effects from fault definitions that were created for an assembly. The failure effects assigned to a fault definition represent the symptoms that would indicate that the associated fault has occurred. (When an aircraft experiences a set of failures matching those in a fault definition, a possible fault is automatically created in Maintenix.)' WHERE parm_name = 'ACTION_UNASSIGN_FAILURE_EFFECT';


--changeSet OPER-9449:296 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Fault Definition button that''s required to unassign fault definitions from failure effects.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_DEFINITION';


--changeSet OPER-9449:297 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Task Definition button that''s required to unassign task definitions from fault definitions.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_DEFINITION_TASK_DEFINITION';


--changeSet OPER-9449:298 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign faults from work packages.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_CHECK';


--changeSet OPER-9449:299 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign faults from completed work packages.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPLETED_CHECK';


--changeSet OPER-9449:300 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign faults from work orders on sub-components.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_FROM_COMPONENT_WORK_ORDER';


--changeSet OPER-9449:301 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Fault Suppression button that''s required to unassign a fault that is currently suppressed by its parent fault.' WHERE parm_name = 'ACTION_UNASSIGN_FAULT_SUPPRESSION';


--changeSet OPER-9449:302 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Forecast Model button that''s required to unassign forecast models from authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_UNASSIGN_FCMODEL_AUTHORITY';


--changeSet OPER-9449:303 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign From Me button that''s required to unassign alerts that you currently receive.' WHERE parm_name = 'ACTION_UNASSIGN_FROM_ME_ALERT';


--changeSet OPER-9449:304 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Assemblies button that''s required to unassign assemblies from IETMs.' WHERE parm_name = 'ACTION_UNASSIGN_IETM_ASSEMBLY';


--changeSet OPER-9449:305 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign tasks from completed work packages.' WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPLETED_CHECK';


--changeSet OPER-9449:306 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign tasks from a work order on a sub-component.' WHERE parm_name = 'ACTION_UNASSIGN_TASK_FROM_COMPONENT_WORK_ORDER';


--changeSet OPER-9449:307 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Tool icon that''s required on actual tasks to remove tools from the list of tools required.' WHERE parm_name = 'ACTION_UNASSIGN_TASK_TOOL';


--changeSet OPER-9449:308 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Button button that''s required to remove buttons from to-do lists.' WHERE parm_name = 'ACTION_UNASSIGN_TODO_BUTTON';


--changeSet OPER-9449:309 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Tab button that''s required to remove tabs from to-do lists.' WHERE parm_name = 'ACTION_UNASSIGN_TODO_TAB';


--changeSet OPER-9449:310 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign from Waybill Group button that''s required to unassign shipments from waybill groups.' WHERE parm_name = 'ACTION_UNASSIGN_FROM_WAYBILL_GROUP';


--changeSet OPER-9449:311 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Install Kit From Part Group icon.' WHERE parm_name = 'ACTION_UNASSIGN_INSTALL_KIT_FROM_PART_GROUP';


--changeSet OPER-9449:312 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Inventory button that''s required to unassign inventory items from authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_UNASSIGN_INV_AUTHORITY';


--changeSet OPER-9449:313 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign parts from job card definitions.' WHERE parm_name = 'ACTION_UNASSIGN_JIC_PART';


--changeSet OPER-9449:314 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign licenses from users. User also requires the ACTION_UNASSIGN_USER_LICENSE permission to view the Unassign License icon.' WHERE parm_name = 'ACTION_UNASSIGN_LICENSE';


--changeSet OPER-9449:315 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Organization button that''s required to unassign organizations from locations.  The opposite action is controlled by the ACTION_ASSIGN_LOCATION_ORGANIZATION permission.' WHERE parm_name = 'ACTION_UNASSIGN_LOCATION_ORGANIZATION';


--changeSet OPER-9449:316 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Measure button that''s required to unassign baseline flight measurements from assemblies.' WHERE parm_name = 'ACTION_UNASSIGN_MEASUREMENT';


--changeSet OPER-9449:317 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Menu Item button that''s required to unassign menu items from menu groups.' WHERE parm_name = 'ACTION_UNASSIGN_MENU_ITEM';


--changeSet OPER-9449:318 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Warranty button that''s required to unassign warranties from lines in purchase and repair orders.' WHERE parm_name = 'ACTION_UNASSIGN_ORDER_WARRANTY';


--changeSet OPER-9449:319 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Organization button that''s required to unassign organizations from users.' WHERE parm_name = 'ACTION_UNASSIGN_ORGANIZATION';


--changeSet OPER-9449:320 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Parts button that''s required to unassign part numbers from stock numbers.' WHERE parm_name = 'ACTION_UNASSIGN_PART_FROM_STOCK';


--changeSet OPER-9449:321 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Part Group button that''s required to unassign part groups from install kits.' WHERE parm_name = 'ACTION_UNASSIGN_PART_GROUP_FROM_INSTALL_KIT';


--changeSet OPER-9449:322 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Repair Location icon that''s required to unassign repair locations from parts.' WHERE parm_name = 'ACTION_UNASSIGN_PART_REPAIR_LOCATION';


--changeSet OPER-9449:323 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign From Me button that''s required to unassign a part request from yourself.' WHERE parm_name = 'ACTION_UNASSIGN_PART_REQUEST_FROM_ME';


--changeSet OPER-9449:324 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Part icon that''s required to unassign parts from part-type reference documents.' WHERE parm_name = 'ACTION_UNASSIGN_REF_DOC_PART';


--changeSet OPER-9449:325 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Part button that''s required to unassign repairable parts from locations.' WHERE parm_name = 'ACTION_UNASSIGN_REPAIRABLE_PART';


--changeSet OPER-9449:326 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Requirement from Maintenance Program button.' WHERE parm_name = 'ACTION_UNASSIGN_REQ_FROM_MAINT_PROGRAM';


--changeSet OPER-9449:327 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Part Request button that''s required to unassign part requests from existing RFQs.' WHERE parm_name = 'ACTION_UNASSIGN_REQ_TO_RFQ';


--changeSet OPER-9449:328 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Roles button that''s required to unassign roles from users.' WHERE parm_name = 'ACTION_UNASSIGN_ROLE';


--changeSet OPER-9449:329 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Skill button that''s required to unassign skills from users.' WHERE parm_name = 'ACTION_UNASSIGN_SKILL';


--changeSet OPER-9449:330 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Parameter button that''s required to unassign usage parameters from usage definitions for assemblies.' WHERE parm_name = 'ACTION_UNASSIGN_USAGE_PARM';


--changeSet OPER-9449:331 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Parameter button that''s required to unassign usage parameters from config slots.' WHERE parm_name = 'ACTION_UNASSIGN_USAGE_PARMS_CONFIG_SLOT';


--changeSet OPER-9449:332 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign User button that''s required to unassign users from roles.' WHERE parm_name = 'ACTION_UNASSIGN_USER';


--changeSet OPER-9449:333 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign User button that''s required to unassign users from authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_UNASSIGN_USER_AUTHORITY';


--changeSet OPER-9449:334 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign User button that''s required to unassign users from task definition approval workflow levels.' WHERE parm_name = 'ACTION_UNASSIGN_USER_LEVEL';


--changeSet OPER-9449:335 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view the Unassign License icon that''s required to unassign licenses from users.  Users also require the ACTION_UNASSIGN_LICENSE permission to perform the action.' WHERE parm_name = 'ACTION_UNASSIGN_USER_LICENSE';


--changeSet OPER-9449:336 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Vendor button that''s required to unassign vendors from organizations.' WHERE parm_name = 'ACTION_UNASSIGN_VENDOR_FROM_ORGANIZATION';


--changeSet OPER-9449:337 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Remove Technical Reference icon that''s required to remove technical references from warranty contracts.' WHERE parm_name = 'ACTION_UNASSIGN_WARRANTY_DEFN_TECH_REF';


--changeSet OPER-9449:338 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Warranty button that''s required to unassign warranty contracts from lines in purchase orders.' WHERE parm_name = 'ACTION_UNASSIGN_WARRANTY_CONTRACT';


--changeSet OPER-9449:339 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Part No icon that''s required to remove part numbers from warranty contracts with a warranty type of assembly or component.' WHERE parm_name = 'ACTION_UNASSIGN_WARRANTY_DEFN_PART';


--changeSet OPER-9449:340 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unassign Task Definitions button that''s required to remove requirement definitions or reference document definitions from warranty contracts.' WHERE parm_name = 'ACTION_UNASSIGN_WARRANTY_DEFN_TASK_DEFN';


--changeSet OPER-9449:341 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unauthorize Order button that''s required to reject purchase orders.' WHERE parm_name = 'ACTION_UNAUTHORIZE_PO';


--changeSet OPER-9449:342 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Uncascade Parameter button to uncascade usage parameters from config slots.' WHERE parm_name = 'ACTION_UNCASCADE_USAGE_PARMS_CONFIG_SLOT';


--changeSet OPER-9449:343 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark job cards as uncollected.' WHERE parm_name = 'ACTION_UNCOLLECT_JIC';


--changeSet OPER-9449:344 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark historic job cards as uncollected.' WHERE parm_name = 'ACTION_UNCOLLECT_JIC_HISTORIC';


--changeSet OPER-9449:345 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Uncommit Scope button that''s required to uncommit workscopes for work packages.' WHERE parm_name = 'ACTION_UNCOMMIT_SCOPE';


--changeSet OPER-9449:346 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unlock Maintenance Program button.' WHERE parm_name = 'ACTION_UNLOCK_MAINT_PROGRAM';


--changeSet OPER-9449:347 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the following buttons that are required to unlock task definitions: Unlock Job Card, Unlock Reference Document, Unlock Requirement, Unlock Block.' WHERE parm_name = 'ACTION_UNLOCK_TASK';


--changeSet OPER-9449:348 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unmark as Supply Location button.' WHERE parm_name = 'ACTION_UNMARK_AS_SUPPLY';


--changeSet OPER-9449:349 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unseal Kit button that''s required to unseal kits before you can remove inventory from them.' WHERE parm_name = 'ACTION_UNSEAL_KIT';


--changeSet OPER-9449:350 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Undo Start button that''s required to change the status of a Check/Work Order back to PLAN from IN WORK.' WHERE parm_name = 'ACTION_UNSTART_CHECK';


--changeSet OPER-9449:351 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Unsuspend Licenses icon that''s required to manually unsuspend inactive licenses that users hold.' WHERE parm_name = 'ACTION_UNSUSPEND_USER_LICENSE';


--changeSet OPER-9449:352 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to un-tag task definitions.' WHERE parm_name = 'ACTION_UNTAG_TASK_DEFINITION';


--changeSet OPER-9449:353 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Update button that''s required to update config slot, repairable parts, and non-repairable lists in the terms and conditions for warranty contracts.' WHERE parm_name = 'ACTION_UPDATE';


--changeSet OPER-9449:354 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Update Aircraft Deadlines button that''s required to launch a background task to update deadlines on aircraft.' WHERE parm_name = 'ACTION_UPDATE_ACFT_DEADLINES';


--changeSet OPER-9449:355 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Update Job Card to Latest Revision button to update job cards in committed work packages with changes that were made to job card definitions.  To view job cards that were revised, users need ACTION_VIEW_JOB_CARDS_WITH_NEWER_REVISION.' WHERE parm_name = 'ACTION_UPDATE_JOB_CARD_TO_LATEST_REVISION';


--changeSet OPER-9449:356 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to update users'' license information.' WHERE parm_name = 'ACTION_UPDATE_LICENSE';


--changeSet OPER-9449:357 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the search icon/button that''s required to search for users.' WHERE parm_name = 'ACTION_USER_SEARCH';


--changeSet OPER-9449:358 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Validate For Payment button that''s required to mark purchase order invoices for payment.' WHERE parm_name = 'ACTION_VALIDATE_FOR_PAYMENT';


--changeSet OPER-9449:359 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view user details for users other than yourself.' WHERE parm_name = 'ACTION_VIEW_ALL_USERS_DETAILS';


--changeSet OPER-9449:360 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Open Reliability Notes sub-tab on inventory details pages.' WHERE parm_name = 'ACTION_VIEW_INVENTORY_RELIABILITY_NOTES';


--changeSet OPER-9449:361 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the View Job Cards With Newer Version button that''s required to view new revisions of job cards in committed work packages. Typically done for large, ongoing work packages because baseline synchronization does not update committed work packages when job cards are revised. To update job cards to the new revision, users require the ACTION_UPDATE_JOB_CARD_TO_LATEST_REVISION permission.' WHERE parm_name = 'ACTION_VIEW_JOB_CARDS_WITH_NEWER_REVISION';


--changeSet OPER-9449:362 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view opportunistic tasks.' WHERE parm_name = 'ACTION_VIEW_OPPORTUNISTIC_TASKS';


--changeSet OPER-9449:363 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the view the Vendor List tab on organization details pages.' WHERE parm_name = 'ACTION_VIEW_ORGANIZATION_VENDOR_LIST';


--changeSet OPER-9449:364 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view the overnight station capacity.' WHERE parm_name = 'ACTION_VIEW_OVERNIGHT_CAPACITY';


--changeSet OPER-9449:365 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view the project plan applet.' WHERE parm_name = 'ACTION_VIEW_PROJECT_PLAN';


--changeSet OPER-9449:366 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view recount results for count discrepencies.' WHERE parm_name = 'ACTION_VIEW_RECOUNT_RESULTS';


--changeSet OPER-9449:367 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view the scheduled hours multiplier field.' WHERE parm_name = 'ACTION_VIEW_SCHED_HR_MULT';


--changeSet OPER-9449:368 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Price button that''s required to edit current vendor part prices.' WHERE parm_name = 'ACTION_EDIT_VENDOR_PART_PRICE';


--changeSet OPER-9449:369 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit user shift pattern day shifts.' WHERE parm_name = 'ACTION_EDIT_USER_SHIFT_PATTER_DAY_SHIFT';


--changeSet OPER-9449:370 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor icon that''s required to modify vendor records.' WHERE parm_name = 'ACTION_EDIT_VENDOR';


--changeSet OPER-9449:371 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Attachment icon that''s required to edit attachments to vendor details.' WHERE parm_name = 'ACTION_EDIT_VENDOR_ATTACHMENT';


--changeSet OPER-9449:372 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor Quote button that''s required to modify vendor quotes on requests for quotes (RFQs).' WHERE parm_name = 'ACTION_EDIT_VENDOR_QUOTE';


--changeSet OPER-9449:373 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Warranty Contract icon.' WHERE parm_name = 'ACTION_EDIT_WARRANTY';


--changeSet OPER-9449:374 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Attachment icon that''s required to edit the details of attachments to warranty contracts.' WHERE parm_name = 'ACTION_EDIT_WARRANTY_ATTACHMENT';


--changeSet OPER-9449:375 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Warranty Contract icon that''s required to edit warranty definitions.' WHERE parm_name = 'ACTION_EDIT_WARRANTY_CONTRACT';


--changeSet OPER-9449:376 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to Edit workscope order.' WHERE parm_name = 'ACTION_EDIT_WORKSCOPE_ORDER';


--changeSet OPER-9449:377 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the work captured by the technician while finishing or job stopping the labor requirement.' WHERE parm_name = 'ACTION_EDIT_WORK_CAPTURED';


--changeSet OPER-9449:378 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to close event.' WHERE parm_name = 'ACTION_EE_CLOSE_EVENT';


--changeSet OPER-9449:379 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete event.' WHERE parm_name = 'ACTION_EE_COMPLETE_EVENT';


--changeSet OPER-9449:380 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create event.' WHERE parm_name = 'ACTION_EE_CREATE_EVENT';


--changeSet OPER-9449:381 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Attachments for the Event.' WHERE parm_name = 'ACTION_EE_EDIT_ATTACHMENTS';


--changeSet OPER-9449:382 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit completed event.' WHERE parm_name = 'ACTION_EE_EDIT_COMPLETED_EVENT';


--changeSet OPER-9449:383 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Component Changes Band.' WHERE parm_name = 'ACTION_EE_EDIT_COMPONENT_CHANGES';


--changeSet OPER-9449:384 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to save event.' WHERE parm_name = 'ACTION_EE_EDIT_EVENT';


--changeSet OPER-9449:385 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Failure Effects for the Event.' WHERE parm_name = 'ACTION_EE_EDIT_FAILURE_EFFECTS';


--changeSet OPER-9449:386 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Findings for the Event.' WHERE parm_name = 'ACTION_EE_EDIT_FINDINGS';


--changeSet OPER-9449:387 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Incorporated Tasks for the Event.' WHERE parm_name = 'ACTION_EE_EDIT_INCORPORATED_TASKS';


--changeSet OPER-9449:388 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Other Actions for the Event.' WHERE parm_name = 'ACTION_EE_EDIT_OTHER_ACTIONS';


--changeSet OPER-9449:389 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark event error.' WHERE parm_name = 'ACTION_EE_MARK_EVENT_ERROR';


--changeSet OPER-9449:390 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to send Alert to engineering.' WHERE parm_name = 'ACTION_EE_NOTIFY_ENG';


--changeSet OPER-9449:391 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to send Alert to FSR.' WHERE parm_name = 'ACTION_EE_NOTIFY_FIELD_REP';


--changeSet OPER-9449:392 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Attachments for the Event.' WHERE parm_name = 'ACTION_EE_VIEW_ATTACHMENTS';


--changeSet OPER-9449:393 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Component Changes Band.' WHERE parm_name = 'ACTION_EE_VIEW_COMPONENT_CHANGES';


--changeSet OPER-9449:394 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Failure Effects for the Event.' WHERE parm_name = 'ACTION_EE_VIEW_FAILURE_EFFECTS';


--changeSet OPER-9449:395 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Findings for the Event.' WHERE parm_name = 'ACTION_EE_VIEW_FINDINGS';


--changeSet OPER-9449:396 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Incorporated Tasks for the Event.' WHERE parm_name = 'ACTION_EE_VIEW_INCORPORATED_TASKS';


--changeSet OPER-9449:397 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view Other Actions for the Event.' WHERE parm_name = 'ACTION_EE_VIEW_OTHER_ACTIONS';


--changeSet OPER-9449:398 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Toggle Enforce Workscope Order button that''s required to make it compulsory  that tasks in a component work package are started and finished in the order that''s defined in the workscope.' WHERE parm_name = 'ACTION_ENF_WORKSCOPE_ORDER_REQ';


--changeSet OPER-9449:399 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Escalate Oil Consumption Status button that''s required to manually escalate the rate status for inventory items with an oil consumption value that''s greater than the consumption rate threshold for the item.' WHERE parm_name = 'ACTION_ESCALATE_OIL_CONSUMPTION_STATUS';


--changeSet OPER-9449:400 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Expire Warranty button that''s required to manually select and expire an initialized warranty.' WHERE parm_name = 'ACTION_EXPIRE_WARRANTY';


--changeSet OPER-9449:401 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to extend deadline for actual tasks based on a requirement definition.' WHERE parm_name = 'ACTION_EXTEND_DEADLINE';


--changeSet OPER-9449:402 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to extend deadlines on maintenance Tasks.' WHERE parm_name = 'ACTION_EXTEND_DEADLINES';


--changeSet OPER-9449:403 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Finish Evaluation button that''s required to record that faults were evaluated to determine what corrective action is required to fix them.' WHERE parm_name = 'ACTION_FINISH_EVALUATION';


--changeSet OPER-9449:404 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the work captured by the technician while finishing or job stopping the labor requirement on behalf of.' WHERE parm_name = 'ACTION_EDIT_WORK_CAPTURED_ON_BEHALF_OF';


--changeSet OPER-9449:405 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Finish button to finish a basic labor requirement (not a certification or inspection) for an executable task.' WHERE parm_name = 'ACTION_FINISH_TASK';


--changeSet OPER-9449:406 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Claim Search page that''s required to find warranty claims.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CLAIM_SEARCH';


--changeSet OPER-9449:407 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Capability and Edit Capability pages.' WHERE parm_name = 'ACTION_JSP_WEB_CAPABILITY_CREATE_EDIT_CAPABILITY';


--changeSet OPER-9449:408 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Cancel Claim page that''s required to cancel a warranty claim.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CANCEL_CLAIM';


--changeSet OPER-9449:409 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Claim Details page.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CLAIM_DETAILS';


--changeSet OPER-9449:410 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Close Claim page that''s required to close a warranty claim.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CLOSE_CLAIM';


--changeSet OPER-9449:411 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Claim and Edit Claim pages.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CREATE_EDIT_CLAIM';


--changeSet OPER-9449:412 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Note page that''s required to add or edit the notes for labor lines on a warranty claim.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_LABOR_LINE_NOTE';


--changeSet OPER-9449:413 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Notes page that''s required to add or edit the notes for part lines in a warranty claim.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_PART_LINE_NOTE';


--changeSet OPER-9449:414 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Claim Part Lines page that''s required to edit the details of part lines in warranty claims.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_EDIT_CLAIM_PART_LINES';


--changeSet OPER-9449:415 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Claim Labor Lines page that''s required to edit details of labor lines in warrantly claims.' WHERE parm_name = 'ACTION_JSP_WEB_CLAIM_CLAIMDETAILS_EDIT_CLAIM_LABOR_LINES';


--changeSet OPER-9449:416 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Load Cycle Count Results and the Load Full Count Results pages.' WHERE parm_name = 'ACTION_JSP_WEB_CYCLECOUNT_LOAD_CYCLE_COUNT_RESULTS';


--changeSet OPER-9449:417 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Recount Results page.' WHERE parm_name = 'ACTION_JSP_WEB_CYCLECOUNT_RECOUNT_RESULTS';


--changeSet OPER-9449:418 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Next Count Date page that''s required to indicate when the next physical count of inventory at specific location is to occur.' WHERE parm_name = 'ACTION_JSP_WEB_CYCLECOUNT_SET_NEXT_COUNT_DATE';


--changeSet OPER-9449:419 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Database Rule and Edit Database Rule Details page.' WHERE parm_name = 'ACTION_JSP_WEB_DBRULECHECKER_CREATE_EDIT_DATABASE_RULE';


--changeSet OPER-9449:420 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Database Rule Check:Results page.' WHERE parm_name = 'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_CHECK_RESULTS';


--changeSet OPER-9449:421 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Database Rule Details page.' WHERE parm_name = 'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_DETAILS';


--changeSet OPER-9449:422 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Database Rule Search page.' WHERE parm_name = 'ACTION_JSP_WEB_DBRULECHECKER_DATABASE_RULE_SEARCH';


--changeSet OPER-9449:423 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Department Details page.' WHERE parm_name = 'ACTION_JSP_WEB_DEPARTMENT_DEPARTMENT_DETAILS';


--changeSet OPER-9449:424 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Department Search page.' WHERE parm_name = 'ACTION_JSP_WEB_DEPARTMENT_DEPARTMENT_SEARCH';


--changeSet OPER-9449:425 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Deployed Ops Import Search page.' WHERE parm_name = 'ACTION_JSP_WEB_DEPLOYED_DEPLOYED_OPS_IMPORT_SEARCH';


--changeSet OPER-9449:426 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Import File Details page that''s required in Deployed Operations.' WHERE parm_name = 'ACTION_JSP_WEB_DEPLOYED_IMPORT_FILE_DETAILS';


--changeSet OPER-9449:427 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Send HTTP Request page.' WHERE parm_name = 'ACTION_JSP_WEB_DEVELOPER_REST_API';


--changeSet OPER-9449:428 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the following pages: Create Seasonal Extraction Rule, Create Weekly Extraction Rule, Copy Extraction Rule, Edit Seasonal Extraction Rule, and  Edit Weekly Extraction Rule.' WHERE parm_name = 'ACTION_JSP_WEB_ER_CREATE_EDIT_EXTRACTION_RULE';


--changeSet OPER-9449:429 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Weekly Extraction Rule and Seasonal Extraction Rule pages that are requires to see the details of extraction rules defined in the system.' WHERE parm_name = 'ACTION_JSP_WEB_ER_EXTRACTION_RULE_DETAILS';


--changeSet OPER-9449:430 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Electronic Signature:  Sign Document page.' WHERE parm_name = 'ACTION_JSP_WEB_ESIGNER_ELECTRONIC_SIGNATURE';


--changeSet OPER-9449:431 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Generate Certificate page that''s required for users to obtain a certificate for electronic signatures.' WHERE parm_name = 'ACTION_JSP_WEB_ESIGNER_GENERATE_CERTIFICATE';


--changeSet OPER-9449:432 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Event Details page that''s required to create and edit events.' WHERE parm_name = 'ACTION_JSP_WEB_EVENT_CREATE_EDIT_EVENT';


--changeSet OPER-9449:433 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Event Search page.' WHERE parm_name = 'ACTION_JSP_WEB_EVENT_EVENT_SEARCH';


--changeSet OPER-9449:434 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Failure Effect Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FAILEFFECT_FAILURE_EFFECT_SEARCH';


--changeSet OPER-9449:435 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Fault Definition page that''s used to assign fault definitions to failure effects.' WHERE parm_name = 'ACTION_JSP_WEB_FAILEFFECT_ASSIGN_FAULT_DEFINITION';


--changeSet OPER-9449:436 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Failure Effect and Edit Failure Effect pages.' WHERE parm_name = 'ACTION_JSP_WEB_FAILEFFECT_CREATE_EDIT_FAIL_EFFECT';


--changeSet OPER-9449:437 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Failure Effect Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FAILEFFECT_FAIL_EFFECT_DETAILS';


--changeSet OPER-9449:438 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to Assign Capacity page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_ASSIGN_CAPACITY';


--changeSet OPER-9449:439 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the License Definition Search page.' WHERE parm_name = 'ACTION_JSP_WEB_LICENSEDEFN_LICENSE_DEFN_SEARCH';


--changeSet OPER-9449:440 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to execute webapp/web/licensedefn/ObsoleteLicenseDefn.jsp' WHERE parm_name = 'ACTION_JSP_WEB_LICENSEDEFN_OBSOLETE_LICENSE_DEFN';


--changeSet OPER-9449:441 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Capability page that''s required to identify the work types that a maintenance location can perform, and for which assemblies.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_ASSIGN_CAPABILITY';


--changeSet OPER-9449:442 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Organization page that''s required to associate an organization with a location.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_ASSIGN_ORGANIZATION_TO_LOCATION';


--changeSet OPER-9449:443 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Contact and Edit Contact pages that are required to identify the contact at a location.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_CREATE_EDIT_CONTACT';


--changeSet OPER-9449:444 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Printer and Edit Printer pages that are required to set up a printer at a location.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_CREATE_EDIT_PRINTER';


--changeSet OPER-9449:445 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Capacity page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_EDIT_CAPACITY';


--changeSet OPER-9449:446 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Location Details page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_LOCATION_DETAILS';


--changeSet OPER-9449:447 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Location Search page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_LOCATION_SEARCH';


--changeSet OPER-9449:448 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Station Capacity page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_STATION_CAPACITY';


--changeSet OPER-9449:449 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Stock Level Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_LOCATION_STOCK_LEVEL_INVENTORY';


--changeSet OPER-9449:450 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Line Planning Automation: Completed page.' WHERE parm_name = 'ACTION_JSP_WEB_LPA_LPARESULT';


--changeSet OPER-9449:451 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Line Planning Automation Run Details page.' WHERE parm_name = 'ACTION_JSP_WEB_LPA_RUN_DETAILS';


--changeSet OPER-9449:452 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Line Planning Automation Run page.' WHERE parm_name = 'ACTION_JSP_WEB_LPA_RUN_LPA';


--changeSet OPER-9449:453 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Report Parameters: Plan Comparison Report page.' WHERE parm_name = 'ACTION_JSP_WEB_LRP_LRPREPORT_PARAMETERS';


--changeSet OPER-9449:454 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Activate Maintenance Program page.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_ACTIVATE_MAINT_PROGRAM';


--changeSet OPER-9449:455 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Requirement page that''s required to add a requirement definition to a maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_ASSIGN_REQ';


--changeSet OPER-9449:456 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Maintenance Program and Edit Maintenance Program pages.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_CREATE_EDIT_MAINT_PROGRAM';


--changeSet OPER-9449:457 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Impact Report page.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_DEADLINE_EXTENSIONS';


--changeSet OPER-9449:458 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Issue Numbers page that''s required to change the issue number assigned to a requirement definition inside a maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_EDIT_ISSUE_NUMBERS';


--changeSet OPER-9449:459 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Reason and Notes page that''s required when setting up or editing a maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_EDIT_REASONS_NOTES';


--changeSet OPER-9449:460 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Maintenance Program Details page.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_MAINT_PROGRAM_DETAILS';


--changeSet OPER-9449:461 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Requirements page that required to select one or more requirement definitions to assign to a maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_SELECT_REQ';


--changeSet OPER-9449:462 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Unassign Requirement page that''s required to remove a requirement definition from a maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_MAINT_UNASSIGN_REQ';


--changeSet OPER-9449:463 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Manufacturer and Edit Manufacturer pages.' WHERE parm_name = 'ACTION_JSP_WEB_MANUFACTURER_CREATE_EDIT_MANUFACTURER';


--changeSet OPER-9449:464 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Manufacturer Details page.' WHERE parm_name = 'ACTION_JSP_WEB_MANUFACTURER_MANUFACTURER_DETAILS';


--changeSet OPER-9449:465 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Manufacturer Search page that''s required to find manufacturers in the database.' WHERE parm_name = 'ACTION_JSP_WEB_MANUFACTURER_MANUFACTURER_SEARCH';


--changeSet OPER-9449:466 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Menu and Edit Menu pages.' WHERE parm_name = 'ACTION_JSP_WEB_MENU_CREATE_EDIT_MENU_GROUP';


--changeSet OPER-9449:467 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Menu Item Order page that''s required to change the order in which the menu items appear in a menu.' WHERE parm_name = 'ACTION_JSP_WEB_MENU_EDIT_MENU_ITEM_ORDER';


--changeSet OPER-9449:468 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Menu Order page that''s required to change the order in which the menus are listed for a role.' WHERE parm_name = 'ACTION_JSP_WEB_MENU_EDIT_MENU_ORDER';


--changeSet OPER-9449:469 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Menu Details page that''s required to modify the menu items that appear for a role.' WHERE parm_name = 'ACTION_JSP_WEB_MENU_MENU_GROUP_DETAILS';


--changeSet OPER-9449:470 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Menu Item Search page that''s required to assign menu items to the menu of a role.' WHERE parm_name = 'ACTION_JSP_WEB_MENU_MENU_ITEM_SEARCH';


--changeSet OPER-9449:471 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Organization page.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_ASSIGN_ORGANIZATION';


--changeSet OPER-9449:472 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Skill page that''s required to assign skills to an organization.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_ASSIGN_SKILL_TO_ORGANIZATION';


--changeSet OPER-9449:473 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Organization and Edit Organization pages.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_CREATE_EDIT_ORGANIZATION';


--changeSet OPER-9449:474 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Skill Order page.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_EDIT_SKILL_ORDER';


--changeSet OPER-9449:475 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access to the Organization page that''s required to see the details of an organization.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_ORGANIZATION_DETAILS';


--changeSet OPER-9449:476 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Organization Search page that provides restricted search criteria for finding organizations.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_ORGANIZATION_SEARCH_BY_TYPE';


--changeSet OPER-9449:477 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Organization page' WHERE parm_name = 'ACTION_JSP_WEB_ORG_PICK_ORGANIZATION';


--changeSet OPER-9449:478 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Organization Approval Details page.' WHERE parm_name = 'ACTION_JSP_WEB_ORG_ORGDETAILS_APPROVAL_DETAILS';


--changeSet OPER-9449:479 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Owner and Edit Owner pages.' WHERE parm_name = 'ACTION_JSP_WEB_OWNER_CREATE_EDIT_OWNER';


--changeSet OPER-9449:480 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Owner Details page.' WHERE parm_name = 'ACTION_JSP_WEB_OWNER_OWNER_DETAILS';


--changeSet OPER-9449:481 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Owner Search page.' WHERE parm_name = 'ACTION_JSP_WEB_OWNER_OWNER_SEARCH';


--changeSet OPER-9449:482 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Panel Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PANEL_PANEL_DETAILS';


--changeSet OPER-9449:483 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Content To Kit page that''s required to define the part numbers to include as items in a kit part number.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADD_CONTENTS_TO_KIT';


--changeSet OPER-9449:484 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Externally Controlled Part page that''s required to add part requirements for inventory that is not controlled in Maintenix to actual tasks.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADD_EXTERNALLY_CONTROLLED_PART';


--changeSet OPER-9449:485 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Part Requirement page that''s required to add required parts to a task or fault.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADD_PART_REQUIREMENT_SEARCH';


--changeSet OPER-9449:486 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Task Incompatibility page that''s required to indicate a part number cannot be used on inventory for which a specific task is completed or open.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADD_TASK_INCOMPATIBILITY';


--changeSet OPER-9449:487 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Adjust Average Unit Price page that''s required to edit the AUP for a part number.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADJUST_AVG_UNIT_PRICE';


--changeSet OPER-9449:488 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Adjust Total Spares page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_ADJUST_TOTAL_SPARES';


--changeSet OPER-9449:489 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Group Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_BOMPART_DETAILS';


--changeSet OPER-9449:490 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Group Search page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_BOMPART_SEARCH';


--changeSet OPER-9449:491 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Approve Part No, which is required to make part numbers active and allow their use, and the Reject Part Number page, which is required to make part numbers obsolete, thus preventing maintenance from installing or using such parts.' WHERE parm_name = 'ACTION_JSP_WEB_PART_CHANGE_PART_STATUS';


--changeSet OPER-9449:492 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Alternate Part page that''s required to create part numbers for parts that are valid alternatives to existing part numbers.' WHERE parm_name = 'ACTION_JSP_WEB_PART_CREATE_ALTERNATE_PART';


--changeSet OPER-9449:493 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Bin Level page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_CREATE_BIN_LEVEL';


--changeSet OPER-9449:494 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Part and Edit Part pages that are required to create part numbers and edit them.' WHERE parm_name = 'ACTION_JSP_WEB_PART_CREATE_EDIT_PART';


--changeSet OPER-9449:495 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Part For Task page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_CREATE_PART_FOR_TASK';


--changeSet OPER-9449:496 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Bin Levels page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_BIN_LEVELS';


--changeSet OPER-9449:497 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Externally Controlled Part Requirements page that''s required to edit actual tasks'' part requirements that are for inventory not controlled in Maintenix.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_EXTERNAL_PART_REQUIREMENTS';


--changeSet OPER-9449:498 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Install Kit Map page that''s required to edit which part groups, and which alternate parts within the groups, are associated with an Install Kit.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_INSTALL_KIT_MAP';


--changeSet OPER-9449:499 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Install Kits page that''s required to identify which of the alternate parts in a part group can use the selected Install Kit.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_INSTALL_KITS';


--changeSet OPER-9449:500 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Kit Contents page that''s required to change the quantity of part numbers included in a kit part, or the percentage of the value of the kit parts represent.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_KIT_CONTENTS';


--changeSet OPER-9449:501 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Part Financials page that''s required to see the financial details about  part numbers.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_PART_FINANCIALS';


--changeSet OPER-9449:502 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Part Requirements page that''s required to edit part requirements in actual tasks.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_PART_REQUIREMENTS';


--changeSet OPER-9449:503 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Purchase Vendor page that''s required to identify the vendors from which a part can be purchased.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_PURCHASE_VENDOR';


--changeSet OPER-9449:504 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Repair Vendor page that''s required to identify the vendors who can repair inventory of a specific part number.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EDIT_REPAIR_VENDOR';


--changeSet OPER-9449:505 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Exchange Vendor Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_EXCHANGE_VENDOR_DETAILS';


--changeSet OPER-9449:506 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Alternate Part page that''s required to add an equivalent part number to a part group.' WHERE parm_name = 'ACTION_JSP_WEB_PART_MANAGE_ALTERNATE_PART';


--changeSet OPER-9449:507 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Search page that provides restricted search criteria for finding part numbers.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PART_SEARCH_BY_TYPE';


--changeSet OPER-9449:508 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PART_DETAILS';


--changeSet OPER-9449:509 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Note page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PART_NOTE';


--changeSet OPER-9449:510 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Search page that''s required to search for part number details in the system.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PART_SEARCH';


--changeSet OPER-9449:511 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Approve Alternate Part and Unapprove Alternate Part pages that are required to approve/unapprove a part number as a valid alternative to other parts in a part group, and to the Unassign Part page that''s required to remove an alternate part from a part group.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PART_STATUS_CHANGE';


--changeSet OPER-9449:512 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Part Price Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PURCHASE_VENDOR_PRICE_DETAILS';


--changeSet OPER-9449:513 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Search page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_SELECT_PART_GROUP';


--changeSet OPER-9449:514 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Part Incompatibility page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_SELECT_PART_INCOMPATIBILITY';


--changeSet OPER-9449:515 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access ther Edit Note page that''s required to add notes to the Parts Details page.' WHERE parm_name = 'ACTION_JSP_WEB_PART_PARTDETAILS_RECEIVING_NOTES';


--changeSet OPER-9449:516 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Permission Matrix Viewer page.' WHERE parm_name = 'ACTION_JSP_WEB_PERMATRIX_PERM_MATRIX_SECURITY';


--changeSet OPER-9449:517 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Miscellaneous PO Invoice Line page.' WHERE parm_name = 'ACTION_JSP_WEB_PI_ADD_MISC_POINVOICE_LINE';


--changeSet OPER-9449:518 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Part PO Invoice Line page.' WHERE parm_name = 'ACTION_JSP_WEB_PI_ADD_PART_POINVOICE_LINE';


--changeSet OPER-9449:519 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Order Invoice ad Edit Order Invoice pages.' WHERE parm_name = 'ACTION_JSP_WEB_PI_CREATE_EDIT_POINVOICE';


--changeSet OPER-9449:520 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit PO Invoice Line Notes page.' WHERE parm_name = 'ACTION_JSP_WEB_PI_EDIT_POINVOICE_LINE_NOTES';


--changeSet OPER-9449:521 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit PO Invoice Lines page.' WHERE parm_name = 'ACTION_JSP_WEB_PI_EDIT_POINVOICE_LINES';


--changeSet OPER-9449:522 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Map PO Invoice Line.' WHERE parm_name = 'ACTION_JSP_WEB_PI_MAP_POINVOICE_LINE';


--changeSet OPER-9449:523 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the PO Invoice Search page.' WHERE parm_name = 'ACTION_JSP_WEB_PI_POINVOICE_SEARCH';


--changeSet OPER-9449:524 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Miscellaneous Line page that''s required to add a line for items, other than those with part numbers, to an order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_ADD_MISC_ORDER_LINE';


--changeSet OPER-9449:525 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the AOG Authorization Override page.' WHERE parm_name = 'ACTION_JSP_WEB_PO_AOGAUTHORIZE_ORDER';


--changeSet OPER-9449:526 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Authorize RO and Authorize PO pages.' WHERE parm_name = 'ACTION_JSP_WEB_PO_AUTHORIZE_ORDER';


--changeSet OPER-9449:527 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access pages for creating and editing various types of orders. The specific pages are: Create PO, Edit PO, Create Borrow Order, Edit Borrow Order, Create Exchange Order, Edit Exchange Order, Create Consignment Order, Edit Consignment Order, Create Consignment Exchange Order, Edit Consignment Exchange Order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_CREATE_EDIT_ORDER';


--changeSet OPER-9449:528 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Order Lines page that''s required to edit the details of a line item on an order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_EDIT_ORDER_LINES';


--changeSet OPER-9449:529 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Receiver Note page that''s required to add or edit notes pertaining to the receiver for a line in an order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_EDIT_PURCHASE_ORDER_LINE_RECEIVER_NOTE';


--changeSet OPER-9449:530 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Vendor Note page that''s required to add or edit notes pertaining to the vendor for a line in an order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_EDIT_PURCHASE_ORDER_LINE_VENDOR_NOTE';


--changeSet OPER-9449:531 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Issue PO page that''s required to issue different types of orders.' WHERE parm_name = 'ACTION_JSP_WEB_PO_ISSUE_ORDER';


--changeSet OPER-9449:532 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the details page for different types of orders. The specific pages are: PO Details, Borrow Order Details, Consignment Order Details, Exchange Order Details, Consignment Exchange Order Details, and RO Details.' WHERE parm_name = 'ACTION_JSP_WEB_PO_ORDER_DETAILS';


--changeSet OPER-9449:533 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Order Search page that provides restricted search criteria.' WHERE parm_name = 'ACTION_JSP_WEB_PO_ORDER_SEARCH_BY_TYPE';


--changeSet OPER-9449:534 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Order Search page.' WHERE parm_name = 'ACTION_JSP_WEB_PO_POSEARCH';


--changeSet OPER-9449:535 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Request Authorization page that''s required to have different types of orders authorized.' WHERE parm_name = 'ACTION_JSP_WEB_PO_REQUEST_ORDER_AUTHORIZATION';


--changeSet OPER-9449:536 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Account page.' WHERE parm_name = 'ACTION_JSP_WEB_PO_SELECT_CHARGE_TO_ACCOUNT';


--changeSet OPER-9449:537 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_PO_SELECT_INVENTORY';


--changeSet OPER-9449:538 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create borrow orders for part requests.' WHERE parm_name = 'ACTION_CREATE_BO_FOR_PART_REQUEST';


--changeSet OPER-9449:539 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create calc parms.' WHERE parm_name = 'ACTION_CREATE_CALC_PARM';


--changeSet OPER-9449:540 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create capacity patterns.' WHERE parm_name = 'ACTION_CREATE_CAPACITY_PATTERN';


--changeSet OPER-9449:541 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create work packages for faults or tasks. Visibility of the button depends on the context (aircraft or uninstalled component) and the status of the ACTION_CREATE_COMPONENT_WORK_ORDER and ACTION_CREATE_BLANK_CHECK permissions.' WHERE parm_name = 'ACTION_CREATE_CHECK_FOR_FAULT_OR_TASK';


--changeSet OPER-9449:542 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create claims.' WHERE parm_name = 'ACTION_CREATE_CLAIM';


--changeSet OPER-9449:543 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Component Warranty Contract button.' WHERE parm_name = 'ACTION_CREATE_COMPNT_WARRANTY_CONTRACT';


--changeSet OPER-9449:544 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create work packages on component/subassembly.' WHERE parm_name = 'ACTION_CREATE_COMPONENT_WORK_ORDER';


--changeSet OPER-9449:545 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the create configuration slots.' WHERE parm_name = 'ACTION_CREATE_CONFIG_SLOT';


--changeSet OPER-9449:546 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create consignment exchange orders.' WHERE parm_name = 'ACTION_CREATE_CONSIGN_EXCHANGE_ORDER';


--changeSet OPER-9449:547 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create consignment orders.' WHERE parm_name = 'ACTION_CREATE_CONSIGN_ORDER';


--changeSet OPER-9449:548 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create consignment orders for part requests.' WHERE parm_name = 'ACTION_CREATE_CONSIGN_ORDER_FOR_PART_REQUEST';


--changeSet OPER-9449:549 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create consignment requests from a stock low.' WHERE parm_name = 'ACTION_CREATE_CONSIGN_REQUEST_FOR_STOCK';


--changeSet OPER-9449:550 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create contacts for locations.' WHERE parm_name = 'ACTION_CREATE_CONTACT';


--changeSet OPER-9449:551 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new database rules.' WHERE parm_name = 'ACTION_CREATE_DATABASE_RULE';


--changeSet OPER-9449:552 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create deferral references.' WHERE parm_name = 'ACTION_CREATE_DEFER_REF';


--changeSet OPER-9449:553 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create departments.' WHERE parm_name = 'ACTION_CREATE_DEPARTMENT';


--changeSet OPER-9449:554 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create or edit a menu groups.' WHERE parm_name = 'ACTION_CREATE_EDIT_MENU';


--changeSet OPER-9449:555 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create exchange orders.' WHERE parm_name = 'ACTION_CREATE_EO';


--changeSet OPER-9449:556 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create exchange orders for inventory.' WHERE parm_name = 'ACTION_CREATE_EO_FOR_INV';


--changeSet OPER-9449:557 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create exchange orders for part requests.' WHERE parm_name = 'ACTION_CREATE_EO_FOR_PART_REQUEST';


--changeSet OPER-9449:558 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create events.' WHERE parm_name = 'ACTION_CREATE_EVENT';


--changeSet OPER-9449:559 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create failure records.' WHERE parm_name = 'ACTION_CREATE_FAILURE_EFFECT';


--changeSet OPER-9449:560 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create faults against checks or WOs.' WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_CHECK_OR_WO';


--changeSet OPER-9449:561 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create faults against tasks.' WHERE parm_name = 'ACTION_CREATE_FAULT_AGAINST_TASK';


--changeSet OPER-9449:562 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create fault definitions.' WHERE parm_name = 'ACTION_CREATE_FAULT_DEFINITION';


--changeSet OPER-9449:563 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create fault thresholds.' WHERE parm_name = 'ACTION_CREATE_FAULT_THRESHOLD';


--changeSet OPER-9449:564 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new flights.' WHERE parm_name = 'ACTION_CREATE_FLIGHT';


--changeSet OPER-9449:565 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create a new flight disruption.' WHERE parm_name = 'ACTION_CREATE_FLIGHT_DISRUPTION';


--changeSet OPER-9449:566 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create Forecast Models.' WHERE parm_name = 'ACTION_CREATE_FORECAST_MODEL';


--changeSet OPER-9449:567 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create assigned historic faults.' WHERE parm_name = 'ACTION_CREATE_HISTORIC_ASSIGNED_FAULT';


--changeSet OPER-9449:568 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create historic faults.' WHERE parm_name = 'ACTION_CREATE_HISTORIC_FAULT';


--changeSet OPER-9449:569 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view the entired linked chain of a task definition.' WHERE parm_name = 'ACTION_VIEW_TASK_HIERARCHY';


--changeSet OPER-9449:570 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Vendor Part Price List button that''s required to view the vendor part price details.' WHERE parm_name = 'ACTION_VIEW_VENDOR_PART_PRICE_DETAILS';


--changeSet OPER-9449:571 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the advanced search page.' WHERE parm_name = 'ACTION_ENABLE_ADVANCED_SEARCH';


--changeSet OPER-9449:572 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Approve With Warning button that''s required to indicate that vendors are acceptable as suppliers for orders, but there are restrictions or limitations.' WHERE parm_name = 'ACTION_WARNING_VENDOR_ORDER_TYPE';


--changeSet OPER-9449:573 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Approve With Warning button that''s required to indicate that vendor performance for services is acceptable, but there are restrictions or limitations.' WHERE parm_name = 'ACTION_WARNING_VENDOR_SERVICE_TYPE';


--changeSet OPER-9449:574 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Program Definition application.' WHERE parm_name = 'APP_PROGRAM_DEFINITION';


--changeSet OPER-9449:575 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Maint - Long Range Planning application.' WHERE parm_name = 'APP_LONG_RANGE_PLANNER';


--changeSet OPER-9449:576 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Portable Detached Adapter application.' WHERE parm_name = 'APP_PORTABLE_DETACHED_ADAPTER';


--changeSet OPER-9449:577 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Production Planning and Control application.' WHERE parm_name = 'APP_PRODUCTION_PLAN_CONTROLLER';


--changeSet OPER-9449:578 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Records Induction application.' WHERE parm_name = 'APP_RECORDS_INDUCTION';


--changeSet OPER-9449:579 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow Optimization action from the Plan menu.' WHERE parm_name = 'ACTION_PPC_OPTIMIZE_PLAN';


--changeSet OPER-9449:580 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create from Existing button that''s required to create vendor part prices from existing part prices.' WHERE parm_name = 'ACTION_CREATE_FROM_EXISTING_VENDOR_PART_PRICE';


--changeSet OPER-9449:581 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor Tax button.' WHERE parm_name = 'ACTION_EDIT_VENDOR_TAX';


--changeSet OPER-9449:582 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor Charge button that''s required to modify charges such as customs or shipping charges.' WHERE parm_name = 'ACTION_EDIT_VENDOR_CHARGE';


--changeSet OPER-9449:583 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to flag the block definition as requiring the manual input of the scheduling interval on each actual task that is based on this definition.' WHERE parm_name = 'ACTION_SET_BLOCK_MANUAL_SCHEDULING';


--changeSet OPER-9449:584 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to flag the requirement definition as requiring the manual input of the scheduling interval on each actual task that is based on this definition.' WHERE parm_name = 'ACTION_SET_REQ_MANUAL_SCHEDULING';


--changeSet OPER-9449:585 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to flag the Reference definition as requiring the manual input of the scheduling interval on each actual task that is based on this definition.' WHERE parm_name = 'ACTION_SET_REF_DOC_MANUAL_SCHEDULING';


--changeSet OPER-9449:586 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit the Failed System field on the Raise Fault page for historic faults.' WHERE parm_name = 'ACTION_EDIT_FAILED_SYS_ON_HISTORIC_FAULT';


--changeSet OPER-9449:587 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to print an ordered tally sheet for an in-work or completed check.' WHERE parm_name = 'ACTION_PRINT_ORDERED_TALLY_SHEET';


--changeSet OPER-9449:588 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow get inventory calls.' WHERE parm_name = 'ACTION_GETINVENTORY';


--changeSet OPER-9449:589 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to request locales via the API.' WHERE parm_name = 'API_LOCALE_REQUEST';


--changeSet OPER-9449:590 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow Asset Record Coordination (ARC)  API calls to load technical records for assets into Maintenix from external systems.' WHERE parm_name = 'API_ARC';


--changeSet OPER-9449:591 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval calls for location.' WHERE parm_name = 'API_LOCATION_REQUEST';


--changeSet OPER-9449:592 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow get part calls.' WHERE parm_name = 'ACTION_GETPART';


--changeSet OPER-9449:593 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow create aircraft calls.' WHERE parm_name = 'API_CREATE_ACFT_REQUEST';


--changeSet OPER-9449:594 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow create assembly, tracked and serialized inventory calls.' WHERE parm_name = 'API_CREATE_SERIALIZED_REQUEST';


--changeSet OPER-9449:595 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow request user menu via API.' WHERE parm_name = 'API_MENU_REQUEST';


--changeSet OPER-9449:596 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API aircraft retrieval calls.' WHERE parm_name = 'API_AIRCRAFT_REQUEST';


--changeSet OPER-9449:597 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval call for aircraft work packages.' WHERE parm_name = 'API_AIRCRAFT_WORK_PACKAGE_REQUEST';


--changeSet OPER-9449:598 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval call for vendors.' WHERE parm_name = 'API_VENDOR_REQUEST';


--changeSet OPER-9449:599 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API planning item retrieval calls.' WHERE parm_name = 'API_PLANNING_ITEM_REQUEST';


--changeSet OPER-9449:600 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API Planning Item unassign calls.' WHERE parm_name = 'API_PLANNING_ITEM_UNASSIGN';


--changeSet OPER-9449:601 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API Planning Item assign calls.' WHERE parm_name = 'API_PLANNING_ITEM_ASSIGN';


--changeSet OPER-9449:602 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval call for work packages.' WHERE parm_name = 'API_WORK_PACKAGE_REQUEST';


--changeSet OPER-9449:603 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API asset retrieval calls.' WHERE parm_name = 'API_ASSET_REQUEST';


--changeSet OPER-9449:604 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API operator retrieval calls.' WHERE parm_name = 'API_OPERATOR_REQUEST';


--changeSet OPER-9449:605 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API search and update part definition calls.' WHERE parm_name = 'API_PART_DEFINITION_REQUEST';


--changeSet OPER-9449:606 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval calls for aspects of the Maintenance Engineering Program.' WHERE parm_name = 'API_MAINT_ENG_PROGRAM_REQUEST';


--changeSet OPER-9449:607 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API asset retrieval calls.' WHERE parm_name = 'API_TRACKED_CORRECT_USAGE_REQUEST';


--changeSet OPER-9449:608 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API raise adhoc alerts.' WHERE parm_name = 'API_RAISE_ADHOC_ALERT_REQUEST';


--changeSet OPER-9449:609 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API asset retrieval calls.' WHERE parm_name = 'API_CREATE_USAGE_REQUEST';


--changeSet OPER-9449:610 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow create and attach tracked asset calls.' WHERE parm_name = 'API_ATTACH_TRACKED_REQUEST';


--changeSet OPER-9449:611 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API asset retrieval calls.' WHERE parm_name = 'API_AIRCRAFT_CORRECT_USAGE_REQUEST';


--changeSet OPER-9449:612 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API user account requests.' WHERE parm_name = 'API_USER_ACCOUNT_REQUEST';


--changeSet OPER-9449:613 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Production Planning and Control release control functionality.' WHERE parm_name = 'ACTION_PRODUCTION_PLAN_RELEASE_CONTROL';


--changeSet OPER-9449:614 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow detach tracked asset calls.' WHERE parm_name = 'API_DETACH_TRACKED_REQUEST';


--changeSet OPER-9449:615 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow archive tracked asset calls.' WHERE parm_name = 'API_ARCHIVE_TRACKED_REQUEST';


--changeSet OPER-9449:616 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow update aircraft calls.' WHERE parm_name = 'API_UPDATE_ACFT_REQUEST';


--changeSet OPER-9449:617 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow create and attach SER asset calls.' WHERE parm_name = 'API_ATTACH_SER_REQUEST';


--changeSet OPER-9449:618 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access/see the Production Planning and Control delete baseline button.' WHERE parm_name = 'ACTION_PRODUCTION_PLAN_DELETE_BASELINE';


--changeSet OPER-9449:619 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission allowing API to delete baselines for PPC production plans.' WHERE parm_name = 'API_PRODUCTION_DELETE_BASELINE_REQUEST';


--changeSet OPER-9449:620 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission allowing API to retrieve PPC baselines.' WHERE parm_name = 'API_PRODUCTION_BASELINE_REQUEST';


--changeSet OPER-9449:621 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Production Planning and Control delete plan/template functionality.' WHERE parm_name = 'ACTION_PRODUCTION_PLAN_DELETE_PLAN_TEMPLATE';


--changeSet OPER-9449:622 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API delete action calls for PPC production plans and templates.' WHERE parm_name = 'API_PRODUCTION_DELETE_PLAN_TEMPLATE_REQUEST';


--changeSet OPER-9449:623 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to remove a part requirement from a task even when the part that was requested by the part requirement was already issued to the work order.' WHERE parm_name = 'ACTION_REMOVE_PART_REQUIREMENT_FOR_ISSUED_PART_REQUEST';


--changeSet OPER-9449:624 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API induction plan retrieval calls.' WHERE parm_name = 'API_INDUCTION_PLAN_REQUEST';


--changeSet OPER-9449:625 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create historic requirement tasks.' WHERE parm_name = 'API_CREATE_HISTORIC_REQ_REQUEST';


--changeSet OPER-9449:626 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create historic block tasks.' WHERE parm_name = 'API_CREATE_HISTORIC_BLOCK_REQUEST';


--changeSet OPER-9449:627 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create historic adhoc tasks.' WHERE parm_name = 'API_CREATE_HISTORIC_ADHOC_REQUEST';


--changeSet OPER-9449:628 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval call for owners.' WHERE parm_name = 'API_OWNER_REQUEST';


--changeSet OPER-9449:629 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval call for forecast models.' WHERE parm_name = 'API_FORECAST_MODEL_REQUEST';


--changeSet OPER-9449:630 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval calls for finance account.' WHERE parm_name = 'API_FINANCE_ACCOUNT_REQUEST';


--changeSet OPER-9449:631 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API retrieval calls for asset condition.' WHERE parm_name = 'API_ASSET_CONDITION_REQUEST';


--changeSet OPER-9449:632 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow API position retrieval calls.' WHERE parm_name = 'API_POSITION_REQUEST';


--changeSet OPER-9449:633 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unassign assemblies from measurements.' WHERE parm_name = 'ACTION_UNASSIGN_ASSEMBLY';


--changeSet OPER-9449:634 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create logbook faults.' WHERE parm_name = 'API_CREATE_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:635 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to request information about logbook faults by using the API.' WHERE parm_name = 'API_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:636 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel logbook faults.' WHERE parm_name = 'API_CANCEL_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:637 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to update open logbook faults.' WHERE parm_name = 'API_UPDATE_OPEN_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:638 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search last done for block, executable requirment and requirment.' WHERE parm_name = 'API_TASK_REQUEST';


--changeSet OPER-9449:639 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow complete put away API call.' WHERE parm_name = 'API_COMPLETE_PUTAWAY_REQUEST';


--changeSet OPER-9449:640 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search fault source.' WHERE parm_name = 'API_FAULT_SOURCE_REQUEST';


--changeSet OPER-9449:641 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search fault severity.' WHERE parm_name = 'API_FAULT_SEVERITY_REQUEST';


--changeSet OPER-9449:642 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to search priority.' WHERE parm_name = 'API_PRIORITY_REQUEST';


--changeSet OPER-9449:643 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow pre-draw asset API call.' WHERE parm_name = 'API_PREDRAW_ASSET_REQUEST';


--changeSet OPER-9449:644 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to enable manual scheduling for tasks based on task definitions. When manual scheduling is enabled, users can modify the scheduling rules for actual tasks that are based on task definitions.' WHERE parm_name = 'ACTION_ENABLE_MANUAL_SCHEDULING';


--changeSet OPER-9449:645 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to defer faults that have a severity type of MEL.' WHERE parm_name = 'API_DEFER_MEL_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:646 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to update logbook faults that have a severity type of MEL.' WHERE parm_name = 'API_UPDATE_MEL_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:647 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to extend logbook fault deadlines.' WHERE parm_name = 'API_EXTEND_DEADLINE_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:648 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view roles.' WHERE parm_name = 'API_ROLE_VIEW_REQUEST';


--changeSet OPER-9449:649 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to update roles.' WHERE parm_name = 'API_ROLE_UPDATE_REQUEST';


--changeSet OPER-9449:650 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'API permission to view permission sets. Permission sets are predefined groups of user permissions for a specific business function.' WHERE parm_name = 'API_PERM_SET_VIEW_REQUEST';


--changeSet OPER-9449:651 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow update logbook faults that have a severity type of MINOR.' WHERE parm_name = 'API_UPDATE_MINOR_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:652 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to defer faults that have a severity type of MINOR.' WHERE parm_name = 'API_DEFER_MINOR_LOGBOOK_FAULT_REQUEST';


--changeSet OPER-9449:653 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to raise predefined alerts.' WHERE parm_name = 'API_RAISE_ALERT_REQUEST';


--changeSet OPER-9449:654 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to view APIs (Application Program Interface).' WHERE parm_name = 'API_VIEW_REQUEST';


--changeSet OPER-9449:655 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to retrieve flight information.' WHERE parm_name = 'API_FLIGHT_REQUEST';


--changeSet OPER-9449:656 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke  theGetPickedItemsInfoCommon servlet.' WHERE parm_name = 'ACTION_ALERT_GET_PICKED_ITEMS_INFO_COMMON';


--changeSet OPER-9449:657 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign assemblies to measurements.' WHERE parm_name = 'ACTION_ASSIGN_ASSEMBLY';


--changeSet OPER-9449:658 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddEditAdvisory servlet.' WHERE parm_name = 'ACTION_ADVISORY_ADD_EDIT_ADVISORY';


--changeSet OPER-9449:659 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateLoginAlert servlet.' WHERE parm_name = 'ACTION_ALERT_CREATE_LOGIN_ALERT';


--changeSet OPER-9449:660 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DefaultAlert servlet.' WHERE parm_name = 'ACTION_ALERT_DEFAULT_ALERT';


--changeSet OPER-9449:661 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the WebViewAction servlet to support alert management.' WHERE parm_name = 'ACTION_ACTION_WEB_VIEW_ACTION';


--changeSet OPER-9449:662 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the WebAlert servlet.' WHERE parm_name = 'ACTION_ALERT_WEB_ALERT';


--changeSet OPER-9449:663 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditConfigSlot servlet.' WHERE parm_name = 'ACTION_BOM_CREATE_EDIT_CONFIG_SLOT';


--changeSet OPER-9449:664 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ViewAttachment servlet.' WHERE parm_name = 'ACTION_ATTACH_VIEW_ATTACHMENT';


--changeSet OPER-9449:665 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignUsageParameter servlet.' WHERE parm_name = 'ACTION_BOM_ASSIGN_USAGE_PARAMETER';


--changeSet OPER-9449:666 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditAssembly servlet.' WHERE parm_name = 'ACTION_BOM_CREATE_EDIT_ASSEMBLY';


--changeSet OPER-9449:667 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditAssignedPartDetails servlet.' WHERE parm_name = 'ACTION_BOM_EDIT_ASSIGNED_PART_DETAILS';


--changeSet OPER-9449:668 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditConfigSlotParent servlet.' WHERE parm_name = 'ACTION_BOM_EDIT_CONFIG_SLOT_PARENT';


--changeSet OPER-9449:669 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPartGroupApplicability servlet.' WHERE parm_name = 'ACTION_BOM_EDIT_PART_GROUP_APPLICABILITY';


--changeSet OPER-9449:670 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPositions servlet.' WHERE parm_name = 'ACTION_BOM_EDIT_POSITIONS';


--changeSet OPER-9449:671 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddConstant servlet that''s required to add constants to Calculated Usage Parameters.' WHERE parm_name = 'ACTION_CALC_ADD_CONSTANT';


--changeSet OPER-9449:672 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditConstants servlet that''s required to edit constants for Calculated Usage Parameters.' WHERE parm_name = 'ACTION_CALC_EDIT_CONSTANTS';


--changeSet OPER-9449:673 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPartSpecConstants servlet. This servlet controls the Part Specific Constants for Calculated Usage Parameters.' WHERE parm_name = 'ACTION_CALC_EDIT_PART_SPEC_CONSTANTS';


--changeSet OPER-9449:674 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditSymbols servlet. This servelet controls access to the Symbols required to create a Calculated Usage Parameter.' WHERE parm_name = 'ACTION_CALC_EDIT_SYMBOLS';


--changeSet OPER-9449:675 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditCapability servlet.' WHERE parm_name = 'ACTION_CAPABILITY_CREATE_EDIT_CAPABILITY';


--changeSet OPER-9449:676 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DeleteCapability servlet.' WHERE parm_name = 'ACTION_CAPABILITY_DELETE_CAPABILITY';


--changeSet OPER-9449:677 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CancelClaim servlet.' WHERE parm_name = 'ACTION_CLAIM_CANCEL_CLAIM';


--changeSet OPER-9449:678 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CloseClaim servlet.' WHERE parm_name = 'ACTION_CLAIM_CLOSE_CLAIM';


--changeSet OPER-9449:679 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditClaim servlet.' WHERE parm_name = 'ACTION_CLAIM_CREATE_EDIT_CLAIM';


--changeSet OPER-9449:680 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditClaimLaborLineNote servlet.' WHERE parm_name = 'ACTION_CLAIM_EDIT_CLAIM_LABOR_LINE_NOTE';


--changeSet OPER-9449:681 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Message servlet. This servlet controls the display of the message page, and should be enabled for all roles.' WHERE parm_name = 'ACTION_COMMON_MESSAGE';


--changeSet OPER-9449:682 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DeleteServlet servlet that''s required to remove selected messages from the Configurator, c_message_log table.' WHERE parm_name = 'ACTION_CONFIGUTILS_DELETE';


--changeSet OPER-9449:683 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the configutils.LaunchServlet servlet.' WHERE parm_name = 'ACTION_CONFIGUTILS_LAUNCH';


--changeSet OPER-9449:684 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the LoadCycleCountResults servlet.' WHERE parm_name = 'ACTION_CYCLECOUNT_LOAD_CYCLE_COUNT_RESULTS';


--changeSet OPER-9449:685 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RecountResults servlet.' WHERE parm_name = 'ACTION_CYCLECOUNT_RECOUNT_RESULTS';


--changeSet OPER-9449:686 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DAO.Query servlet. This generic servelet performs SQL queries and should be enabled for all roles.' WHERE parm_name = 'ACTION_DAO_QUERY';


--changeSet OPER-9449:687 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the dbrulechecker.ValidateSql servlet.' WHERE parm_name = 'ACTION_DBRULECHECKER_VALIDATE_SQL';


--changeSet OPER-9449:688 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditDepartment servlet.' WHERE parm_name = 'ACTION_DEPARTMENT_CREATE_EDIT_DEPARTMENT';


--changeSet OPER-9449:689 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EntityRedirector servlet.' WHERE parm_name = 'ACTION_ENTITY_ENTITY_REDIRECTOR';


--changeSet OPER-9449:690 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ElectronicSignature servlet.' WHERE parm_name = 'ACTION_ESIGNER_ELECTRONIC_SIGNATURE';


--changeSet OPER-9449:691 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddEventTechRef servlet that''s used to add IETMs to tasks, shipments, purchase orders, invoices, and RFQs.' WHERE parm_name = 'ACTION_EVENT_ADD_EVENT_TECH_REF';


--changeSet OPER-9449:692 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CancelTasks servlet.' WHERE parm_name = 'ACTION_EVENT_CANCEL_TASKS';


--changeSet OPER-9449:693 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditEvent servlet that supports the creation of Events.' WHERE parm_name = 'ACTION_EVENT_CREATE_EDIT_EVENT';


--changeSet OPER-9449:694 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditEventData servlet that''s used to create and edit events, including event findings.' WHERE parm_name = 'ACTION_EVENT_CREATE_EDIT_EVENT_DATA';


--changeSet OPER-9449:695 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EventDetailsRedirector servlet.This generic servlet should always be enabled.' WHERE parm_name = 'ACTION_EVENT_EVENT_DETAILS_REDIRECTOR';


--changeSet OPER-9449:696 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveEventTechRef servlet that''s used to remove IETMs from tasks, shipments, purchase orders, invoices, and RFQs.' WHERE parm_name = 'ACTION_EVENT_REMOVE_EVENT_TECH_REF';


--changeSet OPER-9449:697 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ScanBarcode servlet.' WHERE parm_name = 'ACTION_EVENT_SCAN_BARCODE';


--changeSet OPER-9449:698 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditFailEffect servlet.' WHERE parm_name = 'ACTION_FAILEFFECT_CREATE_EDIT_FAIL_EFFECT';


--changeSet OPER-9449:699 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CalcFaultPriority servlet.' WHERE parm_name = 'ACTION_FAULT_CALC_FAULT_PRIORITY';


--changeSet OPER-9449:700 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditFaultDeferRef servlet.' WHERE parm_name = 'ACTION_FAULT_CREATE_EDIT_FAULT_DEFER_REF';


--changeSet OPER-9449:701 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditFaultThreshold servlet.' WHERE parm_name = 'ACTION_FAULT_CREATE_EDIT_FAULT_THRESHOLD';


--changeSet OPER-9449:702 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPossibleFault servlet.' WHERE parm_name = 'ACTION_FAULT_EDIT_POSSIBLE_FAULT';


--changeSet OPER-9449:703 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Fault.EditResultEvent servlet that supports the addition and removal of fault resulting events.' WHERE parm_name = 'ACTION_FAULT_EDIT_RESULT_EVENT';


--changeSet OPER-9449:704 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MarkFaultAsNoFaultFound servlet.' WHERE parm_name = 'ACTION_FAULT_MARK_FAULT_AS_NO_FAULT_FOUND';


--changeSet OPER-9449:705 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MergePossibleFaults servlet.' WHERE parm_name = 'ACTION_FAULT_MERGE_POSSIBLE_FAULTS';


--changeSet OPER-9449:706 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PocketRaiseFault servlet. This servlet is obsolete.' WHERE parm_name = 'ACTION_FAULT_POCKET_RAISE_FAULT';


--changeSet OPER-9449:707 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RaiseFault servlet.' WHERE parm_name = 'ACTION_FAULT_RAISE_FAULT';


--changeSet OPER-9449:708 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReassignFault servlet.' WHERE parm_name = 'ACTION_FAULT_REASSIGN_FAULT';


--changeSet OPER-9449:709 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditFaultDefinition servlet.' WHERE parm_name = 'ACTION_FAULTDEFN_CREATE_EDIT_FAULT_DEFINITION';


--changeSet OPER-9449:710 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the FaultDeferralData servlet.' WHERE parm_name = 'ACTION_FAULTDEFN_FAULT_DEFERRAL_DATA';


--changeSet OPER-9449:711 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AbstractFlight servlet. This servlet is used to complete flights and to create and edit historical flights.' WHERE parm_name = 'ACTION_FLIGHT_ABSTRACT_FLIGHT';


--changeSet OPER-9449:712 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddFlightNote servlet.' WHERE parm_name = 'ACTION_FLIGHT_ADD_FLIGHT_NOTE';


--changeSet OPER-9449:713 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditIETM servlet. This servlet is used to create and edit technical references.' WHERE parm_name = 'ACTION_IETM_CREATE_EDIT_IETM';


--changeSet OPER-9449:714 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ViewHyperlink servlet.' WHERE parm_name = 'ACTION_IETM_VIEW_HYPER_LINK';


--changeSet OPER-9449:715 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetPickedItemsInfo servlet.' WHERE parm_name = 'ACTION_INCLUDE_GET_PICKED_ITEMS_INFO';


--changeSet OPER-9449:716 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetPickedItemsWarnings servlet.' WHERE parm_name = 'ACTION_INCLUDE_GET_PICKED_ITEMS_WARNINGS';


--changeSet OPER-9449:717 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PartNoDataForBulkCreateInventory servlet that''s used to support the creation of inventory.' WHERE parm_name = 'ACTION_INCLUDE_PART_NO_DATA_FOR_BULK_CREATE_INVENTORY';


--changeSet OPER-9449:718 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the integration.MessageData servlet.' WHERE parm_name = 'ACTION_INTEGRATION_MESSAGE_DATA';


--changeSet OPER-9449:719 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddInventoryTechRef servlet.' WHERE parm_name = 'ACTION_INVENTORY_ADD_INVENTORY_TECH_REF';


--changeSet OPER-9449:720 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ChangeCustody servlet.' WHERE parm_name = 'ACTION_INVENTORY_CHANGE_CUSTODY';


--changeSet OPER-9449:721 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateSerialNumber servlet.' WHERE parm_name = 'ACTION_INVENTORY_GENERATE_SERIAL_NUMBER';


--changeSet OPER-9449:722 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the InventorySearchRedirector servlet. This servlet supports inventory searches.' WHERE parm_name = 'ACTION_INVENTORY_INVENTORY_SEARCH_REDIRECTOR';


--changeSet OPER-9449:723 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the IssueInventoryToFault servlet.' WHERE parm_name = 'ACTION_INVENTORY_ISSUE_INVENTORY_TO_FAULT';


--changeSet OPER-9449:724 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the IssueInventoryToNewRequest servlet.' WHERE parm_name = 'ACTION_INVENTORY_ISSUE_INVENTORY_TO_NEW_REQUEST';


--changeSet OPER-9449:725 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the IssueInventoryToTask servlet.' WHERE parm_name = 'ACTION_INVENTORY_ISSUE_INVENTORY_TO_TASK';


--changeSet OPER-9449:726 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MarkInventoryAsInspRequired servlet.' WHERE parm_name = 'ACTION_INVENTORY_MARK_INVENTORY_AS_INSP_REQUIRED';


--changeSet OPER-9449:727 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MarkInventoryAsRepairRequired servlet.' WHERE parm_name = 'ACTION_INVENTORY_MARK_INVENTORY_AS_REPAIR_REQUIRED';


--changeSet OPER-9449:728 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PreventLinePlanningAutomationAircraft servlet.' WHERE parm_name = 'ACTION_INVENTORY_PREVENT_LINE_PLANNING_AUTOMATION_AIRCRAFT';


--changeSet OPER-9449:729 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReInductInventory servlet.' WHERE parm_name = 'ACTION_INVENTORY_RE_INDUCT_INVENTORY';


--changeSet OPER-9449:730 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TransferToQuarantine servlet.' WHERE parm_name = 'ACTION_INVENTORY_TRANSFER_TO_QUARANTINE';


--changeSet OPER-9449:731 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Invoicing.CreateEditEstimateCostLineItemNote servlet.' WHERE parm_name = 'ACTION_INVOICING_CREATE_EDIT_ESTIMATE_COST_LINE_ITEM_NOTE';


--changeSet OPER-9449:732 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Invoicing.CreateEstimateCostLineItem servlet.' WHERE parm_name = 'ACTION_INVOICING_CREATE_ESTIMATE_COST_LINE_ITEM';


--changeSet OPER-9449:733 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Invoicing.EditEstimateCostLineItem servlet.' WHERE parm_name = 'ACTION_INVOICING_EDIT_ESTIMATE_COST_LINE_ITEM';


--changeSet OPER-9449:734 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Invoicing.LockEstimateCostLineItem servlet that supports the invoice creation and editing process.' WHERE parm_name = 'ACTION_INVOICING_LOCK_ESTIMATE_COST_LINE_ITEM';


--changeSet OPER-9449:735 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Invoicing.RemoveEstimateCostLineItem servlet. This servelet is used to remove the Cost Line Item from the work package.' WHERE parm_name = 'ACTION_INVOICING_REMOVE_ESTIMATE_COST_LINE_ITEM';


--changeSet OPER-9449:736 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the jasper.DirectReport servlet. This servlet handles directing request to jasper server to generate reports to a non-HTML format such as PDF, Excel, and Word.  Permissions for the individual reports are required.' WHERE parm_name = 'ACTION_JASPER_DIRECT_REPORT';


--changeSet OPER-9449:737 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the jasper.EmbeddedReport servlet. This servlet connects to the Report Application Server and retrieves the report. Permissions for the individual reports are required.' WHERE parm_name = 'ACTION_JASPER_EMBEDDED_REPORT';


--changeSet OPER-9449:738 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the jasper.ExportReport servlet. This servlet handles exporting of reports to a non-HTML format such as PDF, Excel, Word. Permissions for the indvidual reports are required.' WHERE parm_name = 'ACTION_JASPER_EXPORT_REPORT';


--changeSet OPER-9449:739 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke jasper.SSOReport servlet. This servlet supports single sign-on between Maintenix and Jaspersoft. Permissions for the individual reports are required.' WHERE parm_name = 'ACTION_JASPER_SSO_REPORT';


--changeSet OPER-9449:740 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ValidateKitBarcode servlet.' WHERE parm_name = 'ACTION_KIT_VALIDATE_KIT_BARCODE';


--changeSet OPER-9449:741 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddLabourToTask servlet.' WHERE parm_name = 'ACTION_LABOUR_ADD_LABOUR_TO_TASK';


--changeSet OPER-9449:742 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveLabourFromTask servlet.' WHERE parm_name = 'ACTION_LABOUR_REMOVE_LABOUR_FROM_TASK';


--changeSet OPER-9449:743 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the location.AssignCapability servlet.' WHERE parm_name = 'ACTION_LOCATION_ASSIGN_CAPABILITY';


--changeSet OPER-9449:744 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the location.AssignDepartment servlet.' WHERE parm_name = 'ACTION_LOCATION_ASSIGN_DEPARTMENT';


--changeSet OPER-9449:745 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignParentLocation servlet.' WHERE parm_name = 'ACTION_LOCATION_ASSIGN_PARENT_LOCATION';


--changeSet OPER-9449:746 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the location.AssignRepairablePart servlet.' WHERE parm_name = 'ACTION_LOCATION_ASSIGN_REPAIRABLE_PART';


--changeSet OPER-9449:747 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateContact servlet.' WHERE parm_name = 'ACTION_LOCATION_CREATE_CONTACT';


--changeSet OPER-9449:748 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatePrinter servlet.' WHERE parm_name = 'ACTION_LOCATION_CREATE_PRINTER';


--changeSet OPER-9449:749 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditCapacity servlet.' WHERE parm_name = 'ACTION_LOCATION_EDIT_CAPACITY';


--changeSet OPER-9449:750 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditContact servlet.' WHERE parm_name = 'ACTION_LOCATION_EDIT_CONTACT';


--changeSet OPER-9449:751 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPrinter servlet.' WHERE parm_name = 'ACTION_LOCATION_EDIT_PRINTER';


--changeSet OPER-9449:752 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MarkAsDefaultDock servlet.' WHERE parm_name = 'ACTION_LOCATION_MARK_AS_DEFAULT_DOCK';


--changeSet OPER-9449:753 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the MarkAsSupply servlet that''s required to set locations as supply locations.' WHERE parm_name = 'ACTION_LOCATION_MARK_AS_SUPPLY';


--changeSet OPER-9449:754 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the location.UnassignDepartment servlet.' WHERE parm_name = 'ACTION_LOCATION_UNASSIGN_DEPARTMENT';


--changeSet OPER-9449:755 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnmarkAsSupply servlet that''s required to remove locations as supply locations.' WHERE parm_name = 'ACTION_LOCATION_UNMARK_AS_SUPPLY';


--changeSet OPER-9449:756 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CompleteLPAAnalysis servlet.' WHERE parm_name = 'ACTION_LPA_COMPLETE_LPAANALYSIS';


--changeSet OPER-9449:757 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the LPAStatus servlet.' WHERE parm_name = 'ACTION_LPA_LPASTATUS';


--changeSet OPER-9449:758 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the LPA.MarkAsResolved servlet.' WHERE parm_name = 'ACTION_LPA_MARK_AS_RESOLVED';


--changeSet OPER-9449:759 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the lpa.RunLPA servlet.' WHERE parm_name = 'ACTION_LPA_RUN_LPA';


--changeSet OPER-9449:760 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ActivateMaintProgram servlet.' WHERE parm_name = 'ACTION_MAINT_ACTIVATE_MAINT_PROGRAM';


--changeSet OPER-9449:761 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignReqInfo servlet. This servlet is used in Maintenance Program management.' WHERE parm_name = 'ACTION_MAINT_ASSIGN_REQ_INFO';


--changeSet OPER-9449:762 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DeleteMaintProgram servlet.' WHERE parm_name = 'ACTION_MAINT_DELETE_MAINT_PROGRAM';


--changeSet OPER-9449:763 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditReasonsNotes servlet that''s required to edit reasons and notes in a Maintenance Program.' WHERE parm_name = 'ACTION_MAINT_EDIT_REASONS_NOTES';


--changeSet OPER-9449:764 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RevertToPreviousRevision servlet. This servelet is used in the workflow of Maintenance Programs.' WHERE parm_name = 'ACTION_MAINT_REVERT_TO_PREVIOUS_REVISION';


--changeSet OPER-9449:765 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnassignReq servlet.' WHERE parm_name = 'ACTION_MAINT_UNASSIGN_REQ';


--changeSet OPER-9449:766 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditManufacturer servlet.' WHERE parm_name = 'ACTION_MANUFACTURER_CREATE_EDIT_MANUFACTURER';


--changeSet OPER-9449:767 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the assembly.AssignMeasurement servlet.' WHERE parm_name = 'ACTION_MEASUREMENTS_ASSIGN_MEASUREMENT';


--changeSet OPER-9449:768 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the assembly.CreateEditMeasurement servlet.' WHERE parm_name = 'ACTION_MEASUREMENTS_CREATE_EDIT_MEASUREMENT';


--changeSet OPER-9449:769 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the assembly.ReorderMeasurements servlet.' WHERE parm_name = 'ACTION_MEASUREMENTS_REORDER_MEASUREMENTS';


--changeSet OPER-9449:770 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the oilconsumption.EditSerialNoSpecificThreshold servlet.' WHERE parm_name = 'ACTION_OILCONSUMPTION_EDIT_SERIAL_NO_SPECIFIC_THRESHOLD';


--changeSet OPER-9449:771 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignOrgSkill servlet. This servlet is used to assign organization-specific labour skills so that each organization can have a different set of available skills.' WHERE parm_name = 'ACTION_ORG_ASSIGN_ORG_SKILL';


--changeSet OPER-9449:772 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditOrganization servlet.' WHERE parm_name = 'ACTION_ORG_CREATE_EDIT_ORGANIZATION';


--changeSet OPER-9449:773 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditSkill servlet.' WHERE parm_name = 'ACTION_ORG_CREATE_EDIT_SKILL';


--changeSet OPER-9449:774 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnassignOrgSkill servlet.' WHERE parm_name = 'ACTION_ORG_UNASSIGN_ORG_SKILL';


--changeSet OPER-9449:775 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatEditOwner servlet.' WHERE parm_name = 'ACTION_OWNER_CREAT_EDIT_OWNER';


--changeSet OPER-9449:776 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddEditExchangeVendorPart servlet.' WHERE parm_name = 'ACTION_PART_ADD_EDIT_EXCHANGE_VENDOR_PART';


--changeSet OPER-9449:777 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddPartAttachment servlet.' WHERE parm_name = 'ACTION_PART_ADD_PART_ATTACHMENT';


--changeSet OPER-9449:778 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddPartKitContents servlet.' WHERE parm_name = 'ACTION_PART_ADD_PART_KIT_CONTENTS';


--changeSet OPER-9449:779 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ApprovalToggle servlet that supports the approval of parts.' WHERE parm_name = 'ACTION_PART_APPROVAL_TOGGLE';


--changeSet OPER-9449:780 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditPart servlet.' WHERE parm_name = 'ACTION_PART_CREATE_EDIT_PART';


--changeSet OPER-9449:781 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatePartForTask servlet.' WHERE parm_name = 'ACTION_PART_CREATE_PART_FOR_TASK';


--changeSet OPER-9449:782 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditBinLevels servlet.' WHERE parm_name = 'ACTION_PART_EDIT_BIN_LEVELS';


--changeSet OPER-9449:783 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPartRequirements servlet.' WHERE parm_name = 'ACTION_PART_EDIT_PART_REQUIREMENTS';


--changeSet OPER-9449:784 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPurchaseVendor servlet.' WHERE parm_name = 'ACTION_PART_EDIT_PURCHASE_VENDOR';


--changeSet OPER-9449:785 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditRepairVendor servlet.' WHERE parm_name = 'ACTION_PART_EDIT_REPAIR_VENDOR';


--changeSet OPER-9449:786 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditUnitOfMeasure servlet.' WHERE parm_name = 'ACTION_PART_EDIT_UNIT_OF_MEASURE';


--changeSet OPER-9449:787 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReceivingNotes servlet.' WHERE parm_name = 'ACTION_PART_RECEIVING_NOTES';


--changeSet OPER-9449:788 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemovePartRequirement servlet.' WHERE parm_name = 'ACTION_PART_REMOVE_PART_REQUIREMENT';


--changeSet OPER-9449:789 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveVendor servlet.' WHERE parm_name = 'ACTION_PART_REMOVE_VENDOR';


--changeSet OPER-9449:790 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ViewPurchaseVendorPriceDetails servlet.' WHERE parm_name = 'ACTION_PART_VIEW_PURCHASE_VENDOR_PRICE_DETAILS';


--changeSet OPER-9449:791 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateLicenseCardPdf servlet.' WHERE parm_name = 'ACTION_PDF_ABSTRACT_GENERATE_LICENSE_CARD_PDF';


--changeSet OPER-9449:792 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GeneratePartTagPdf servlet.' WHERE parm_name = 'ACTION_PDF_ABSTRACT_GENERATE_PART_TAG_PDF';


--changeSet OPER-9449:793 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateTaskCardPdf servlet.' WHERE parm_name = 'ACTION_PDF_ABSTRACT_GENERATE_TASK_CARD_PDF';


--changeSet OPER-9449:794 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateCondemnedTagPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_CONDEMNED_TAG_PDF';


--changeSet OPER-9449:795 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GeneratePlanComparisonReportPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_PLAN_COMPARISON_REPORT_PDF';


--changeSet OPER-9449:796 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GeneratePurchaseOrderPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_PURCHASE_ORDER_PDF';


--changeSet OPER-9449:797 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateRepairOrConvertRepairOrderPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_REPAIR_OR_CONVERT_REPAIR_ORDER_PDF';


--changeSet OPER-9449:798 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateServiceableTagPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_SERVICEABLE_TAG_PDF';


--changeSet OPER-9449:799 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateShippingFormPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_SHIPPING_FORM_PDF';


--changeSet OPER-9449:800 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateTallySheetPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_TALLY_SHEET_PDF';


--changeSet OPER-9449:801 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateTaskDefinitionCardsPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_TASK_DEFINITION_CARDS_PDF';


--changeSet OPER-9449:802 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateUnserviceableTagPdf servlet.' WHERE parm_name = 'ACTION_PDF_GENERATE_UNSERVICEABLE_TAG_PDF';


--changeSet OPER-9449:803 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintIssueListPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_ISSUE_LIST_PDF';


--changeSet OPER-9449:804 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintIssueTicketPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_ISSUE_TICKET_PDF';


--changeSet OPER-9449:805 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintPickListPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_PICK_LIST_PDF';


--changeSet OPER-9449:806 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintPutAwayTicketPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_PUT_AWAY_TICKET_PDF';


--changeSet OPER-9449:807 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintQuarantineTagPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_QUARANTINE_TAG_PDF';


--changeSet OPER-9449:808 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintRFQPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_RFQPDF';


--changeSet OPER-9449:809 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintTransferTicketPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_TRANSFER_TICKET_PDF';


--changeSet OPER-9449:810 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintVendorReliabilityPdf servlet.' WHERE parm_name = 'ACTION_PDF_PRINT_VENDOR_RELIABILITY_PDF';


--changeSet OPER-9449:811 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ViewEsigDocument servlet.' WHERE parm_name = 'ACTION_PDF_VIEW_ESIG_DOCUMENT';


--changeSet OPER-9449:812 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PermMatrixSecurity servlet. This servelt is used to allow modification of the permission matrix.' WHERE parm_name = 'ACTION_PERMMATRIX_PERM_MATRIX_SECURITY';


--changeSet OPER-9449:813 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddMiscPOInvoiceLine servlet.' WHERE parm_name = 'ACTION_PI_ADD_MISC_POINVOICE_LINE';


--changeSet OPER-9449:814 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPOInvoiceLineNotes servlet.' WHERE parm_name = 'ACTION_PI_EDIT_POINVOICE_LINE_NOTES';


--changeSet OPER-9449:815 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetKitPickedItemsInfo servlet.' WHERE parm_name = 'ACTION_PICK_GET_KIT_PICKED_ITEMS_INFO';


--changeSet OPER-9449:816 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the LoadPlanningViewer servlet.' WHERE parm_name = 'ACTION_PLANNINGVIEWER_LOAD_PLANNING_VIEWER';


--changeSet OPER-9449:817 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TriggerUpdatePredictedDeadlineDate servlet. This servlet is used in the planning viewer to update deadlines on aircraft.' WHERE parm_name = 'ACTION_PLANNINGVIEWER_TRIGGER_UPDATE_PREDICTED_DEADLINE_DATE';


--changeSet OPER-9449:818 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPurchaseOrderLineReceiverNote servlet.' WHERE parm_name = 'ACTION_PO_EDIT_PURCHASE_ORDER_LINE_RECEIVER_NOTE';


--changeSet OPER-9449:819 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditPurchaseOrderLineVendorNote servlet.' WHERE parm_name = 'ACTION_PO_EDIT_PURCHASE_ORDER_LINE_VENDOR_NOTE';


--changeSet OPER-9449:820 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Cyclecount.FoundUnexpectedPart servlet.' WHERE parm_name = 'ACTION_PORTABLE_FOUND_UNEXPECTED_PART';


--changeSet OPER-9449:821 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Cyclecount.FoundUnexpectedPartAction servlet.' WHERE parm_name = 'ACTION_PORTABLE_FOUND_UNEXPECTED_PART_ACTION';


--changeSet OPER-9449:822 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Cyclecount.ItemRecount servlet.' WHERE parm_name = 'ACTION_PORTABLE_ITEM_RECOUNT';


--changeSet OPER-9449:823 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddRemoveTaxChargePOInvoiceRFQ servlet.' WHERE parm_name = 'ACTION_PROCUREMENT_ADD_REMOVE_TAX_CHARGE_POINVOICE_RFQ';


--changeSet OPER-9449:824 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditVendorTaxCharge servlet.' WHERE parm_name = 'ACTION_PROCUREMENT_EDIT_VENDOR_TAX_CHARGE';


--changeSet OPER-9449:825 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaxCharge servlet.' WHERE parm_name = 'ACTION_PROCUREMENT_TAX_CHARGE';


--changeSet OPER-9449:826 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Quarantine.AddNotes servlet.' WHERE parm_name = 'ACTION_QUARANTINE_ADD_ACTION_NOTE';


--changeSet OPER-9449:827 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Quarantine.AddNotes servlet.' WHERE parm_name = 'ACTION_QUARANTINE_ADD_NOTE';


--changeSet OPER-9449:828 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PreviewTaskCards servlet.' WHERE parm_name = 'ACTION_REPORT_ABSTRACT_PREVIEW_TASK_CARDS';


--changeSet OPER-9449:829 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintLicenseCards servlet.' WHERE parm_name = 'ACTION_REPORT_ABSTRACT_PRINT_LICENSE_CARDS';


--changeSet OPER-9449:830 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PrintTaskCards servlet.' WHERE parm_name = 'ACTION_REPORT_ABSTRACT_PRINT_TASK_CARDS';


--changeSet OPER-9449:831 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EmbeddedReport servlet. This servlet connects to the Crystal Report Application Server, or logs in to Crystal Enterprise. It then retrieves the report and redirects to a page displaying the report. Permissions for individual reports are required.' WHERE parm_name = 'ACTION_REPORT_EMBEDDED_REPORT';


--changeSet OPER-9449:832 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ExportReport servlet.' WHERE parm_name = 'ACTION_REPORT_EXPORT_REPORT';


--changeSet OPER-9449:833 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GeneratePlanComparisonReport servlet.' WHERE parm_name = 'ACTION_REPORT_GENERATE_PLAN_COMPARISON_REPORT';


--changeSet OPER-9449:834 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateReport servlet. This servlet handles requests to generate reports. Based on the configuration, it redirects to the appropriate reporting engine servlet. Permissions for individual reports are required.' WHERE parm_name = 'ACTION_REPORT_GENERATE_REPORT';


--changeSet OPER-9449:835 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RenameReport servlet.' WHERE parm_name = 'ACTION_REPORT_RENAME_REPORT';


--changeSet OPER-9449:836 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ScheduleReport servlet.' WHERE parm_name = 'ACTION_REPORT_SCHEDULE_REPORT';


--changeSet OPER-9449:837 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatePartRequest servlet.' WHERE parm_name = 'ACTION_REQ_CREATE_PART_REQUEST';


--changeSet OPER-9449:838 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatePartRequestForTask servlet.' WHERE parm_name = 'ACTION_REQ_CREATE_PART_REQUEST_FOR_TASK';


--changeSet OPER-9449:839 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreatePurchaseRequestsForPartRequests servlet.' WHERE parm_name = 'ACTION_REQ_CREATE_PURCHASE_REQUESTS_FOR_PART_REQUESTS';


--changeSet OPER-9449:840 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the IssueInventory servlet.' WHERE parm_name = 'ACTION_REQ_ISSUE_INVENTORY';


--changeSet OPER-9449:841 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReserveLocalInventory servlet.' WHERE parm_name = 'ACTION_REQ_RESERVE_LOCAL_INVENTORY';


--changeSet OPER-9449:842 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReserveOnOrderInventory servlet.' WHERE parm_name = 'ACTION_REQ_RESERVE_ON_ORDER_INVENTORY';


--changeSet OPER-9449:843 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ValidateIssueBarcode servlet.' WHERE parm_name = 'ACTION_REQ_VALIDATE_ISSUE_BARCODE';


--changeSet OPER-9449:844 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveRFQLines servlet.' WHERE parm_name = 'ACTION_RFQ_REMOVE_RFQLINES';


--changeSet OPER-9449:845 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SelectAlternatePartForQuote servlet.' WHERE parm_name = 'ACTION_RFQ_SELECT_ALTERNATE_PART_FOR_QUOTE';


--changeSet OPER-9449:846 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AdvancedSearch servlet.' WHERE parm_name = 'ACTION_SEARCH_ADVANCED_SEARCH';


--changeSet OPER-9449:847 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the servlet.Logic servlet. The ancestor-class servlet should be enabled for all roles.' WHERE parm_name = 'ACTION_SERVLET_LOGIC';


--changeSet OPER-9449:848 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ValidatingLogic servlet. This generic servlet provides a framework for validation and should be enabled for all roles.' WHERE parm_name = 'ACTION_SERVLET_VALIDATING_LOGIC';


--changeSet OPER-9449:849 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CapacityPatternDetails servlet.' WHERE parm_name = 'ACTION_SHIFT_CAPACITY_PATTERN_DETAILS';


--changeSet OPER-9449:850 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditCapacityPattern servlet.' WHERE parm_name = 'ACTION_SHIFT_CREATE_EDIT_CAPACITY_PATTERN';


--changeSet OPER-9449:851 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DuplicateCapacityPattern servlet.' WHERE parm_name = 'ACTION_SHIFT_DUPLICATE_CAPACITY_PATTERN';


--changeSet OPER-9449:852 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditShipmentLineNotes servlet.' WHERE parm_name = 'ACTION_SHIPMENT_EDIT_SHIPMENT_LINE_NOTES';


--changeSet OPER-9449:853 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetSubKitPartDetails servlet.' WHERE parm_name = 'ACTION_SHIPMENT_GET_SUB_KIT_PART_DETAILS';


--changeSet OPER-9449:854 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PickShipment servlet.' WHERE parm_name = 'ACTION_SHIPMENT_PICK_SHIPMENT';


--changeSet OPER-9449:855 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SSO.BusinessIntelligenceRedirect servlet. This servlet supports single sign-on.' WHERE parm_name = 'ACTION_SSO_BUSINESS_INTELLIGENCE_REDIRECT';


--changeSet OPER-9449:856 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SSO.ValidateTicket servlet. This servlet supports single sign-on.' WHERE parm_name = 'ACTION_SSO_VALIDATE_TICKET';


--changeSet OPER-9449:857 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the StationMonitoring servlet.' WHERE parm_name = 'ACTION_STATIONMONITORING_STATION_MONITORING';


--changeSet OPER-9449:858 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AdjustStockPercentage servlet.' WHERE parm_name = 'ACTION_STOCK_ADJUST_STOCK_PERCENTAGE';


--changeSet OPER-9449:859 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetSubtypeByAssemblyAjax servlet.  This servlet is used to get subtypes of an assembly invoked using AJAX.  The results are returned to the client-side as XML.' WHERE parm_name = 'ACTION_SUBTYPES_GET_SUBTYPE_BY_ASSEMBLY_AJAX';


--changeSet OPER-9449:860 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddDeadlineValidateAsync servlet. This servlet is used to perform server side validations for the Add Task Deadlines workflow that''s invoked using AJAX. The results are returned to the client-side as XML.' WHERE parm_name = 'ACTION_TASK_ADD_DEADLINE_VALIDATE_ASYNC';


--changeSet OPER-9449:861 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.AddMeasurements servlet.' WHERE parm_name = 'ACTION_TASK_ADD_MEASUREMENTS';


--changeSet OPER-9449:862 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddTaskAttachment servlet.' WHERE parm_name = 'ACTION_TASK_ADD_TASK_ATTACHMENT';


--changeSet OPER-9449:863 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CancelPartsFromTask servlet.' WHERE parm_name = 'ACTION_TASK_CANCEL_PARTS_FROM_TASK';


--changeSet OPER-9449:864 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CancelTasks servlet.' WHERE parm_name = 'ACTION_TASK_CANCEL_TASKS';


--changeSet OPER-9449:865 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CommitWorkscope servlet.' WHERE parm_name = 'ACTION_TASK_COMMIT_WORKSCOPE';


--changeSet OPER-9449:866 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ConfirmCorrectiveAction servlet.' WHERE parm_name = 'ACTION_TASK_CONFIRM_CORRECTIVE_ACTION';


--changeSet OPER-9449:867 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateNewCheck servlet.' WHERE parm_name = 'ACTION_TASK_CREATE_NEW_CHECK';


--changeSet OPER-9449:868 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateReplacementTaskForInventory servlet.' WHERE parm_name = 'ACTION_TASK_CREATE_REPLACEMENT_TASK_FOR_INVENTORY';


--changeSet OPER-9449:869 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the DeferTask servlet.' WHERE parm_name = 'ACTION_TASK_DEFER_TASK';


--changeSet OPER-9449:870 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditCheck servlet.' WHERE parm_name = 'ACTION_TASK_EDIT_CHECK';


--changeSet OPER-9449:871 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.EditMeasurements servlet.' WHERE parm_name = 'ACTION_TASK_EDIT_MEASUREMENTS';


--changeSet OPER-9449:872 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditTaskAttachment servlet.' WHERE parm_name = 'ACTION_TASK_EDIT_TASK_ATTACHMENT';


--changeSet OPER-9449:873 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the FindPredictedDeadline servlet.' WHERE parm_name = 'ACTION_TASK_FIND_PREDICTED_DEADLINE';


--changeSet OPER-9449:874 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetCurrUsageAsync servlet that''s used to get the current usage for task scheduling.' WHERE parm_name = 'ACTION_TASK_GET_CURR_USAGE_ASYNC';


--changeSet OPER-9449:875 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetVendorByLocationAjax servlet. This servlet is used to get the vendor by using its location key for an inventory item that''s invoked using AJAX. The results are returned to the client-side as XML.' WHERE parm_name = 'ACTION_TASK_GET_VENDOR_BY_LOCATION_AJAX';


--changeSet OPER-9449:876 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the task.HandlingMoveSelectedItems servlet. This servlet is used to update the tally line order for work orders.' WHERE parm_name = 'ACTION_TASK_HANDLING_MOVE_SELECTED_ITEMS';


--changeSet OPER-9449:877 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PackageAndComplete servlet.' WHERE parm_name = 'ACTION_TASK_PACKAGE_AND_COMPLETE';


--changeSet OPER-9449:878 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the task.PartNote servlet that''s used to create or edit part notes.' WHERE parm_name = 'ACTION_TASK_PART_NOTE';


--changeSet OPER-9449:879 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the PreventLinePlanningAutomationWorkPackage servlet.' WHERE parm_name = 'ACTION_TASK_PREVENT_LINE_PLANNING_AUTOMATION_WORK_PACKAGE';


--changeSet OPER-9449:880 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.RemoveMeasurements servlet.' WHERE parm_name = 'ACTION_TASK_REMOVE_MEASUREMENTS';


--changeSet OPER-9449:881 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemovePartsFromTask servlet.' WHERE parm_name = 'ACTION_TASK_REMOVE_PARTS_FROM_TASK';


--changeSet OPER-9449:882 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ROLineNumbers servlet.' WHERE parm_name = 'ACTION_TASK_ROLINE_NUMBERS';


--changeSet OPER-9449:883 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SaveTask servlet.' WHERE parm_name = 'ACTION_TASK_SAVE_TASK';


--changeSet OPER-9449:884 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.SchedAddPanel servlet.' WHERE parm_name = 'ACTION_TASK_SCHED_ADD_PANEL';


--changeSet OPER-9449:885 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.SchedAddZone servlet.' WHERE parm_name = 'ACTION_TASK_SCHED_ADD_ZONE';


--changeSet OPER-9449:886 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the Task.SetPartsToolsStatus servlet.' WHERE parm_name = 'ACTION_TASK_SET_PARTS_TOOLS_STATUS';


--changeSet OPER-9449:887 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskPriorities servlet.' WHERE parm_name = 'ACTION_TASK_TASK_PRIORITIES';


--changeSet OPER-9449:888 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnassignDoAtNextInstall servlet.' WHERE parm_name = 'ACTION_TASK_UNASSIGN_DO_AT_NEXT_INSTALL';


--changeSet OPER-9449:889 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnassignTask servlet.' WHERE parm_name = 'ACTION_TASK_UNASSIGN_TASK';


--changeSet OPER-9449:890 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UncommitWorkscope servlet.' WHERE parm_name = 'ACTION_TASK_UNCOMMIT_WORKSCOPE';


--changeSet OPER-9449:891 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UndoStart servlet.' WHERE parm_name = 'ACTION_TASK_UNDO_START';


--changeSet OPER-9449:892 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddEditMeasureSpecificSchedulingRules servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_EDIT_MEASURE_SPECIFIC_SCHEDULING_RULES';


--changeSet OPER-9449:893 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddJICToREQ servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_JIC_TO_REQ';


--changeSet OPER-9449:894 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddJICToREQInfo servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_JIC_TO_REQ_INFO';


--changeSet OPER-9449:895 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddLabourRequirement servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_LABOUR_REQUIREMENT';


--changeSet OPER-9449:896 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddLinkedTask servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_LINKED_TASK';


--changeSet OPER-9449:897 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddPanel servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_PANEL';


--changeSet OPER-9449:898 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddPossibleFault servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_POSSIBLE_FAULT';


--changeSet OPER-9449:899 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddStep servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_STEP';


--changeSet OPER-9449:900 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddTaskDefnAttachment servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_TASK_DEFN_ATTACHMENT';


--changeSet OPER-9449:901 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddTaskDefnTechRef servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_TASK_DEFN_TECH_REF';


--changeSet OPER-9449:902 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddToolRequirement servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_TOOL_REQUIREMENT';


--changeSet OPER-9449:903 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddWeightAndBalance servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_WEIGHT_AND_BALANCE';


--changeSet OPER-9449:904 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.AddZone servlet.' WHERE parm_name = 'ACTION_TASKDEFN_ADD_ZONE';


--changeSet OPER-9449:905 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ApproveTaskDefn servlet.' WHERE parm_name = 'ACTION_TASKDEFN_APPROVE_TASKDEFN';


--changeSet OPER-9449:906 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditBlock servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_BLOCK';


--changeSet OPER-9449:907 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditBlockData servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_BLOCK_DATA';


--changeSet OPER-9449:908 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.CreateEditJobCard servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_JOB_CARD';


--changeSet OPER-9449:909 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditRefDocData servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_REF_DOC_DATA';


--changeSet OPER-9449:910 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.CreateEditReq servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_REQ';


--changeSet OPER-9449:911 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.CreateEditReqData servlet.' WHERE parm_name = 'ACTION_TASKDEFN_CREATE_EDIT_REQ_DATA';


--changeSet OPER-9449:912 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditLabourRequirements servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_LABOUR_REQUIREMENTS';


--changeSet OPER-9449:913 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditMeasurementOrder servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_MEASUREMENT_ORDER';


--changeSet OPER-9449:914 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditPartRequirements servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_PART_REQUIREMENTS';


--changeSet OPER-9449:915 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditPartSpecificSchedulingRules servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_PART_SPECIFIC_SCHEDULING_RULES';


--changeSet OPER-9449:916 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditPlanningValuesData servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_PLANNING_VALUES_DATA';


--changeSet OPER-9449:917 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditReqMap servlet. This servlet supports the mapping of requirements to blocks.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_REQ_MAP';


--changeSet OPER-9449:918 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditSchedulingRules servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_SCHEDULING_RULES';


--changeSet OPER-9449:919 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditStepOrder servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_STEP_ORDER';


--changeSet OPER-9449:920 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditSteps servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_STEPS';


--changeSet OPER-9449:921 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditSubtaskOrder servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_SUBTASK_ORDER';


--changeSet OPER-9449:922 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditTaskApplicability servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_TASK_APPLICABILITY';


--changeSet OPER-9449:923 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditTaskDefnAttachmentOrder servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_TASK_DEFN_ATTACHMENT_ORDER';


--changeSet OPER-9449:924 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditTaskDefnTechRefOrder servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_TASK_DEFN_TECH_REF_ORDER';


--changeSet OPER-9449:925 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditToolRequirements servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_TOOL_REQUIREMENTS';


--changeSet OPER-9449:926 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.EditWeightAndBalance servlet.' WHERE parm_name = 'ACTION_TASKDEFN_EDIT_WEIGHT_AND_BALANCE';


--changeSet OPER-9449:927 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RejectTaskDefn servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REJECT_TASK_DEFN';


--changeSet OPER-9449:928 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveLinkedTask servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_LINKED_TASK';


--changeSet OPER-9449:929 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveMeasureSpecificSchedulingRules servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_MEASURE_SPECIFIC_SCHEDULING_RULES';


--changeSet OPER-9449:930 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RemoveMeasurement servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_MEASUREMENT';


--changeSet OPER-9449:931 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemovePartSpecificSchedulingRule servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_PART_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:932 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemovePartTransformation servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_PART_TRANSFORMATION';


--changeSet OPER-9449:933 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RemovePossibleFault servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_POSSIBLE_FAULT';


--changeSet OPER-9449:934 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RemoveSchedulingRule servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_SCHEDULING_RULE';


--changeSet OPER-9449:935 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RemoveWeightAndBalance servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_WEIGHT_AND_BALANCE';


--changeSet OPER-9449:936 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RemoveZone servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REMOVE_ZONE';


--changeSet OPER-9449:937 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.RequestRestartApproval servlet.' WHERE parm_name = 'ACTION_TASKDEFN_REQUEST_RESTART_APPROVAL';


--changeSet OPER-9449:938 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinitionRedirector servlet.  This servlet directs users to the appropriate Task Definition Details page based on the task definition class mode.' WHERE parm_name = 'ACTION_TASKDEFN_TASK_DEFINITION_REDIRECTOR';


--changeSet OPER-9449:939 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.ValidateAddEditMeasurementData servlet.' WHERE parm_name = 'ACTION_TASKDEFN_VALIDATE_ADD_EDIT_MEASUREMENT_DATA';


--changeSet OPER-9449:940 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.VerifyApplicabilityRule servlet.' WHERE parm_name = 'ACTION_TASKDEFN_VERIFY_APPLICABILITY_RULE';


--changeSet OPER-9449:941 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the TaskDefinition.VerifyApplicabilityRuleAjax servlet. This servlet supports the Ajax servlet that''s used to verify applicability rules.' WHERE parm_name = 'ACTION_TASKDEFN_VERIFY_APPLICABILITY_RULE_AJAX';


--changeSet OPER-9449:942 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetDateTimeForTimeZoneAjax servlet. This servlet supports the Ajax call to convert time to a local time.' WHERE parm_name = 'ACTION_TIMEZONE_GET_DATE_TIME_FOR_TIME_ZONE_AJAX';


--changeSet OPER-9449:943 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetTimeZoneByDateAjax servlet. This AJAX servlet returns time zone information from server configurations on the basis of the date provided.' WHERE parm_name = 'ACTION_TIMEZONE_GET_TIME_ZONE_BY_DATE_AJAX';


--changeSet OPER-9449:944 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetTimeZoneByKeyAjax servlet. This AJAX servlet returns time zone information for a given time zone key.' WHERE parm_name = 'ACTION_TIMEZONE_GET_TIME_ZONE_BY_KEY_AJAX';


--changeSet OPER-9449:945 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GetTimeZoneByLocationAjax servlet. This AJAX servlet returns time zone information for a given location key.' WHERE parm_name = 'ACTION_TIMEZONE_GET_TIME_ZONE_BY_LOCATION_AJAX';


--changeSet OPER-9449:946 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditToBeReturnedPart servlet.' WHERE parm_name = 'ACTION_TODOLIST_EDIT_TO_BE_RETURNED_PART';


--changeSet OPER-9449:947 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the IncompleteKitsTab servlet.' WHERE parm_name = 'ACTION_TODOLIST_INCOMPLETE_KITS_TAB';


--changeSet OPER-9449:948 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CompletePutAway servlet.' WHERE parm_name = 'ACTION_TRANSFER_COMPLETE_PUT_AWAY';


--changeSet OPER-9449:949 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the InvTurnInData servlet.' WHERE parm_name = 'ACTION_TRANSFER_INV_TURN_IN_DATA';


--changeSet OPER-9449:950 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateUsageRecord servlet.' WHERE parm_name = 'ACTION_USAGE_CREATE_USAGE_RECORD';


--changeSet OPER-9449:951 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditUsageDefinition servlet.' WHERE parm_name = 'ACTION_USAGEDEFN_CREATE_EDIT_USAGE_DEFINITION';


--changeSet OPER-9449:952 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the ReorderUsageParms servlet.' WHERE parm_name = 'ACTION_USAGEDEFN_REORDER_USAGE_PARMS';


--changeSet OPER-9449:953 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignDataValue servlet.' WHERE parm_name = 'ACTION_USAGEPARM_ASSIGN_DATA_VALUE';


--changeSet OPER-9449:954 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddTimeOff servlet.' WHERE parm_name = 'ACTION_USER_ADD_TIME_OFF';


--changeSet OPER-9449:955 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditTimeOff servlet.' WHERE parm_name = 'ACTION_USER_EDIT_TIME_OFF';


--changeSet OPER-9449:956 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditUserLicense servlet.' WHERE parm_name = 'ACTION_USER_EDIT_USER_LICENSE';


--changeSet OPER-9449:957 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the User.RemoveTimeOff servlet.' WHERE parm_name = 'ACTION_USER_REMOVE_TIME_OFF';


--changeSet OPER-9449:958 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveUserShifts servlet.' WHERE parm_name = 'ACTION_USER_REMOVE_USER_SHIFTS';


--changeSet OPER-9449:959 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SuspendLicense servlet.' WHERE parm_name = 'ACTION_USER_SUSPEND_LICENSE';


--changeSet OPER-9449:960 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the User.UnSuspendLicense servlet.' WHERE parm_name = 'ACTION_USER_UN_SUSPEND_LICENSE';


--changeSet OPER-9449:961 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UserPkiXml servlet that''s used to return XML data for message viewing and sending.' WHERE parm_name = 'ACTION_USER_USER_PKI_XML';


--changeSet OPER-9449:962 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AddUserShiftPatternDayShift servlet.' WHERE parm_name = 'ACTION_USERSHIFTPATTERN_ADD_USER_SHIFT_PATTERN_DAY_SHIFT';


--changeSet OPER-9449:963 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the EditUserShiftPatternDayShifts servlet.' WHERE parm_name = 'ACTION_USERSHIFTPATTERN_EDIT_USER_SHIFT_PATTERN_DAY_SHIFTS';


--changeSet OPER-9449:964 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the RemoveUserShiftPatternDayShifts servlet.' WHERE parm_name = 'ACTION_USERSHIFTPATTERN_REMOVE_USER_SHIFT_PATTERN_DAY_SHIFTS';


--changeSet OPER-9449:965 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the AssignVendorAirport servlet.' WHERE parm_name = 'ACTION_VENDOR_ASSIGN_VENDOR_AIRPORT';


--changeSet OPER-9449:966 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the GenerateVendorCode servlet.' WHERE parm_name = 'ACTION_VENDOR_GENERATE_VENDOR_CODE';


--changeSet OPER-9449:967 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the SetDefaultVendorAirport servlet.' WHERE parm_name = 'ACTION_VENDOR_SET_DEFAULT_VENDOR_AIRPORT';


--changeSet OPER-9449:968 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the UnassignVendorAirport servlet.' WHERE parm_name = 'ACTION_VENDOR_UNASSIGN_VENDOR_AIRPORT';


--changeSet OPER-9449:969 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditWarrantyContract servlet.' WHERE parm_name = 'ACTION_WARRANTY_CREATE_EDIT_WARRANTY_CONTRACT';


--changeSet OPER-9449:970 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the InitEditWarranty servlet.' WHERE parm_name = 'ACTION_WARRANTY_INIT_EDIT_WARRANTY';


--changeSet OPER-9449:971 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the WarrantyType servlet.' WHERE parm_name = 'ACTION_WARRANTY_WARRANTY_TYPE';


--changeSet OPER-9449:972 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the WorkItemAction servlet.' WHERE parm_name = 'ACTION_WORK_WORK_ITEM_ACTION';


--changeSet OPER-9449:973 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to invoke the CreateEditApprovalLevel servlet.' WHERE parm_name = 'ACTION_WORKFLOW_CREATE_EDIT_APPROVAL_LEVEL';


--changeSet OPER-9449:974 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the View Report page.' WHERE parm_name = 'ACTION_JSP_EMBEDDED_VIEW_REPORT';


--changeSet OPER-9449:975 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Jasper View Report page.' WHERE parm_name = 'ACTION_JSP_EMBEDDED_JASPER_VIEW_REPORT';


--changeSet OPER-9449:976 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_CHANGE_REPORT_NAME_FORM';


--changeSet OPER-9449:977 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports: Report Details page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_REPORT_DESCRIPTION';


--changeSet OPER-9449:978 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports: Report Listing page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_REPORTS';


--changeSet OPER-9449:979 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports: Schedule Reports page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_SCHEDULE_FORM';


--changeSet OPER-9449:980 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports: Search Results page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_SEARCH';


--changeSet OPER-9449:981 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Crystal Reports: Search Reports page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_SEARCH_FORM';


--changeSet OPER-9449:982 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Jasper View Report page.' WHERE parm_name = 'ACTION_JSP_ENTERPRISE_VIEW_REPORT2';


--changeSet OPER-9449:983 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Report Parameters page.' WHERE parm_name = 'ACTION_JSP_SHARED_REPORT_PARAMETERS';


--changeSet OPER-9449:984 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Jasper Reports Parameters page.' WHERE parm_name = 'ACTION_JSP_SHARED_JASPER_REPORT_PARAMETERS';


--changeSet OPER-9449:985 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Advisory and Edit Advisory pages that are required to warn users about problems with specific part numbers, serials numbers, or batch numbers.' WHERE parm_name = 'ACTION_JSP_WEB_ADVISORY_ADD_EDIT_ADVISORY';


--changeSet OPER-9449:986 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Advisory Details page that''s required to see the details of an advisory about parts, including the affected inventory.' WHERE parm_name = 'ACTION_JSP_WEB_ADVISORY_ADVISORY_DETAILS';


--changeSet OPER-9449:987 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Clear Advisory page that''s required to clear the advisory assigned to a part number.' WHERE parm_name = 'ACTION_JSP_WEB_ADVISORY_CLEAR_ADVISORY';


--changeSet OPER-9449:988 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Outstanding Advisory Search page that''s required to find inventory items that have advisories against them.' WHERE parm_name = 'ACTION_JSP_WEB_ADVISORY_OUTSTANDING_ADVSRY_SEARCH_BY_TYPE';


--changeSet OPER-9449:989 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Note To Alert page.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_ADD_NOTE';


--changeSet OPER-9449:990 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Alert Details page.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_ALERT_DETAILS';


--changeSet OPER-9449:991 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Alert Search page.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_ALERT_SEARCH_BY_TYPE';


--changeSet OPER-9449:992 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Alert Setup page.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_ALERT_SETUP';


--changeSet OPER-9449:993 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Disposition Alert page.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_DISPOSITION_ALERT';


--changeSet OPER-9449:994 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Alert Setup: Role Assignments page that''s required to assign an alert to user roles.' WHERE parm_name = 'ACTION_JSP_WEB_ALERT_EDIT_ROLE_ASSIGNMENTS';


--changeSet OPER-9449:995 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assembly Details page.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_ASSEMBLY_DETAILS';


--changeSet OPER-9449:996 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Deferral Reference Details page.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_DEFERRAL_DETAILS_DEFER_REF_DETAILS';


--changeSet OPER-9449:997 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the following pages: Create Measurement, Edit Measurement, Create Parameter.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_CREATE_EDIT_MEASUREMENT';


--changeSet OPER-9449:998 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Measurement Search page.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_MEASUREMENT_SEARCH';


--changeSet OPER-9449:999 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reorder Measurements page.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_MEASUREMENTS_REORDER_MEASUREMENTS';


--changeSet OPER-9449:1000 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Assembly Thresholds page that''s required to define oil consumption rate values for Maintenix to use to monitor oil consumption.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_EDIT_ASSEMBLY_THRESHOLD';


--changeSet OPER-9449:1001 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Operator Specific Thresholds page that''s required to define oil consumption monitoring threshold values that apply to a single operator.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_EDIT_OPERATOR_SPECIFIC_THRESHOLD';


--changeSet OPER-9449:1002 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Oil Consumption Rate Definition and the Edit Oil Consumption Rate Definition pages.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_OILCONSUMPTION_OIL_CONSUMPTION_RATE_DEFINITION';


--changeSet OPER-9449:1003 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Subtype and Edit Subtype pages that are used to create and modify assembly subtypes.' WHERE parm_name = 'ACTION_JSP_WEB_ASSEMBLY_SUBTYPES_CREATE_EDIT_SUBTYPE';


--changeSet OPER-9449:1004 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Attachment page that''s required to attach documents to many different types of records in Maintenix.' WHERE parm_name = 'ACTION_JSP_WEB_ATTACH_ADD_ATTACHMENT';


--changeSet OPER-9449:1005 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Attachment page that''s required to modify the information about an existing attachment.' WHERE parm_name = 'ACTION_JSP_WEB_ATTACH_EDIT_ATTACHMENT';


--changeSet OPER-9449:1006 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Failure Priority page that''s required to add a failure priority to an authority.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_ADD_FAILURE_PRIORITY';


--changeSet OPER-9449:1007 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Inventory page that''s required to assign inventory items to an authority.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_ASSIGN_INVENTORY';


--changeSet OPER-9449:1008 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Authority Details page.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_AUTHORITY_DETAILS';


--changeSet OPER-9449:1009 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Authority Search page.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_AUTHORITY_SEARCH';


--changeSet OPER-9449:1010 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Authority and Edit Authority pages.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_CREATE_EDIT_AUTHORITY';


--changeSet OPER-9449:1011 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Failure Priority page.' WHERE parm_name = 'ACTION_JSP_WEB_AUTHORITY_EDIT_FAILURE_PRIORITY';


--changeSet OPER-9449:1012 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Config Slot page.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_ADD_CONFIG_SLOT';


--changeSet OPER-9449:1013 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Config Slot Details page.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_CONFIG_SLOT_DETAILS';


--changeSet OPER-9449:1014 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Assembly and Edit Assembly pages.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_CREATE_EDIT_ASSEMBLY';


--changeSet OPER-9449:1015 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Config Slot and Edit Config Slot pages.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_CREATE_EDIT_CONFIG_SLOT';


--changeSet OPER-9449:1016 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Assigned Part Details page that''s required to add conditional part applicability details to a part group.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_EDIT_ASSIGNED_PART_DETAILS';


--changeSet OPER-9449:1017 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Applicability page that''s required to specify the applicability of a part group and of the alternate part numbers included in the part group.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_EDIT_PART_GROUP_APPLICABILITY';


--changeSet OPER-9449:1018 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Positions page that''s required to edit the name of positions of configuration slots.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_EDIT_POSITIONS';


--changeSet OPER-9449:1019 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Config Slot page that''s required when assigning a new parent configuration slot, or when moving a part group to a different configuration slot.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_SELECT_CONFIG_SLOT';


--changeSet OPER-9449:1020 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Parent Config Slot page that''s used when converting a component from tracked to serialized.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_SELECT_PARENT_CONFIG_SLOT';


--changeSet OPER-9449:1021 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Fault Threshold Details page.' WHERE parm_name = 'ACTION_JSP_WEB_BOM_FAULTTHRESHOLD_FAULT_THRESHOLD_DETAILS';


--changeSet OPER-9449:1022 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Capability Details page.' WHERE parm_name = 'ACTION_JSP_WEB_CAPABILITY_CAPABILITY_DETAILS';


--changeSet OPER-9449:1023 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Capability Search page.' WHERE parm_name = 'ACTION_JSP_WEB_CAPABILITY_CAPABILITY_SEARCH';


--changeSet OPER-9449:1024 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment button that''s required to add attachments to requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_RFQ_ATTACHMENT';


--changeSet OPER-9449:1025 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Note button that''s required to add a note to an RFQ.' WHERE parm_name = 'ACTION_ADD_RFQ_NOTE';


--changeSet OPER-9449:1026 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference button that''s required to add technical references to requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_RFQ_TECH_REF';


--changeSet OPER-9449:1027 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment icon that''s required to add attachments to shipments.' WHERE parm_name = 'ACTION_ADD_SHIPMENT_ATTACHMENT';


--changeSet OPER-9449:1028 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference icon that''s required to add topics (technical references) to shipments.' WHERE parm_name = 'ACTION_ADD_SHIPMENT_TECH_REF';


--changeSet OPER-9449:1029 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add panels to tasks and faults.' WHERE parm_name = 'ACTION_ADD_TASK_PANEL';


--changeSet OPER-9449:1030 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add job card steps to tasks and faults.' WHERE parm_name = 'ACTION_ADD_TASK_STEP';


--changeSet OPER-9449:1031 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new technical references and to add technical references to tasks and faults.' WHERE parm_name = 'ACTION_ADD_TASK_TECH_REF';


--changeSet OPER-9449:1032 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add zones to tasks and faults.' WHERE parm_name = 'ACTION_ADD_TASK_ZONE';


--changeSet OPER-9449:1033 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment button that''s required to add attachments to user records.' WHERE parm_name = 'ACTION_ADD_USER_ATTACHMENT';


--changeSet OPER-9449:1034 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Technical Reference button that''s required to assign technical references to parts.' WHERE parm_name = 'ACTION_ASSIGN_PART_TECH_REF';


--changeSet OPER-9449:1035 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Parts button that''s required to assign part numbers to stock numbers.' WHERE parm_name = 'ACTION_ASSIGN_PART_TO_STOCK';


--changeSet OPER-9449:1036 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Part Request button that''s required to assign part requests to lines in purchase orders.' WHERE parm_name = 'ACTION_ASSIGN_PO_PART_REQUEST';


--changeSet OPER-9449:1037 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Part icon that''s required to assign parts to part number type reference documents.' WHERE parm_name = 'ACTION_ASSIGN_REF_DOC_PART';


--changeSet OPER-9449:1038 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Request to RFQ button that''s required to assign part requests to existing requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ASSIGN_REQ_TO_RFQ';


--changeSet OPER-9449:1039 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Quote to Existing PO button that''s required to assign lines in requests for quotes (RFQs) to existing purchase orders.' WHERE parm_name = 'ACTION_ASSIGN_RFQ_LINE_TO_PO';


--changeSet OPER-9449:1040 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign existing technical references to tasks and faults. (A separate permission is required to create technical references).' WHERE parm_name = 'ACTION_ASSIGN_TASK_TECH_REF';


--changeSet OPER-9449:1041 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Tool icon that''s required on actual tasks to add tools to the list of tools required.' WHERE parm_name = 'ACTION_ASSIGN_TASK_TOOL';


--changeSet OPER-9449:1042 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign a task into a work order on a sub-component.' WHERE parm_name = 'ACTION_ASSIGN_TASK_TO_COMPONENT_WORK_ORDER';


--changeSet OPER-9449:1043 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Button button that''s required to add buttons to to-do lists.' WHERE parm_name = 'ACTION_ASSIGN_TODO_BUTTON';


--changeSet OPER-9449:1044 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Tab button that''s required to assign tabs to to-do lists.' WHERE parm_name = 'ACTION_ASSIGN_TODO_TAB';


--changeSet OPER-9449:1045 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign alert to me.' WHERE parm_name = 'ACTION_ASSIGN_TO_ME_ALERT';


--changeSet OPER-9449:1046 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign to User button that''s required to assign alerts to users.' WHERE parm_name = 'ACTION_ASSIGN_TO_USER';


--changeSet OPER-9449:1047 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign To Waybill Group button that''s required to group separate shipments that are going to the same destination.' WHERE parm_name = 'ACTION_ASSIGN_TO_WAYBILL_GROUP';


--changeSet OPER-9449:1048 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Parameter button that''s required to assign usage parameters to usage definitions for assemblies.  The ACTION_ASSIGN_USAGE_PARMS_CONFIG_SLOT permission is required to assign usage parameters to config slots.' WHERE parm_name = 'ACTION_ASSIGN_USAGE_PARM';


--changeSet OPER-9449:1049 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Parameter button that''s required to assign usage parameters to config slots.  The ACTION_ASSIGN_USAGE_PARM permission is required to assign usage parameters to assemblies.' WHERE parm_name = 'ACTION_ASSIGN_USAGE_PARMS_CONFIG_SLOT';


--changeSet OPER-9449:1050 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Parameter button that''s required to assign usage parameters to calculated usage parameters.' WHERE parm_name = 'ACTION_ASSIGN_USAGE_PARM_CALC';


--changeSet OPER-9449:1051 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign User button that''s required to assign users to roles.' WHERE parm_name = 'ACTION_ASSIGN_USER';


--changeSet OPER-9449:1052 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign User button that''s required to assign users to authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_ASSIGN_USER_AUTHORITY';


--changeSet OPER-9449:1053 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the xx button that''s required to  assign users to a particular level in the context of task definition approval workflows.' WHERE parm_name = 'ACTION_ASSIGN_USER_LEVEL';


--changeSet OPER-9449:1054 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign License icon that''s required to assign licenses to users. To perform the action, users also require the ACTION_ASSIGN_LICENSE permission.' WHERE parm_name = 'ACTION_ASSIGN_USER_LICENSE';


--changeSet OPER-9449:1055 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the search icon that''s required to change the authorities that are assigned to inventory items. [Is this true that it''s the search icon here?] This action is part of the edit inventory details workflow, so users also require the Edit Inventory Step 1 permission also. [True? If yes, will name perm here.]' WHERE parm_name = 'ACTION_CHANGE_AUTHORITY';


--changeSet OPER-9449:1056 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'This governs the ''Change to Serialized'' button on the Part Details page of a BATCH part.  This will change the inventory class of the part from BATCH to SER.   In order to receive the item properly, the material controller will have to change the part type of the part from BATCH to SER.  The user cannot change the inventory class if there are other parts assigned to the same part group.  The alternate parts must be unassigned first, as a part group should only have items with the same inventory class.  The user also cannot change class if there are open part requests with a quantity greater than one.  These part requests must be closed before converting.' WHERE parm_name = 'ACTION_CHANGE_BATCH_TO_SERIALIZED';


--changeSet OPER-9449:1057 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the attachments on block definitions.' WHERE parm_name = 'ACTION_CHANGE_BLOCK_ATTACHMENT_ORDER';


--changeSet OPER-9449:1058 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the subtasks on block definitions.' WHERE parm_name = 'ACTION_CHANGE_BLOCK_SUBTASK_ORDER';


--changeSet OPER-9449:1059 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the technical references on block definitions.' WHERE parm_name = 'ACTION_CHANGE_BLOCK_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1060 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the BOM Item (role) of inventory items.' WHERE parm_name = 'ACTION_CHANGE_BOM_ITEM';


--changeSet OPER-9449:1061 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change custody to a piece of inventory.' WHERE parm_name = 'ACTION_CHANGE_CUSTODY';


--changeSet OPER-9449:1062 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the failed system when raising faults.' WHERE parm_name = 'ACTION_CHANGE_FAILED_SYSTEM';


--changeSet OPER-9449:1063 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of failure effects.' WHERE parm_name = 'ACTION_CHANGE_FAILURE_EFFECT_ORDER';


--changeSet OPER-9449:1064 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of attachements on fault definitions.' WHERE parm_name = 'ACTION_CHANGE_FAULT_DEFINITION_ATTACHMENT_ORDER';


--changeSet OPER-9449:1065 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of technical references on fault definitions.' WHERE parm_name = 'ACTION_CHANGE_FAULT_DEFINITION_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1066 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the attachments on job card definitions.' WHERE parm_name = 'ACTION_CHANGE_JIC_ATTACHMENT_ORDER';


--changeSet OPER-9449:1067 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the measurements on job card definitions.' WHERE parm_name = 'ACTION_CHANGE_JIC_MEASUREMENT_ORDER';


--changeSet OPER-9449:1068 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the job steps on job card definitions.' WHERE parm_name = 'ACTION_CHANGE_JIC_STEP_ORDER';


--changeSet OPER-9449:1069 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Technical Reference Order icon that''s required to change the order of technical references on job card definitions.' WHERE parm_name = 'ACTION_CHANGE_JIC_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1070 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the attachments on master panel card definitions.' WHERE parm_name = 'ACTION_CHANGE_MPC_ATTACHMENT_ORDER';


--changeSet OPER-9449:1071 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the technical references on a Master Panel Card (MPC) definition.' WHERE parm_name = 'ACTION_CHANGE_MPC_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1072 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Change Owner button that''s required to change the owner of inventory items.' WHERE parm_name = 'ACTION_CHANGE_OWNER';


--changeSet OPER-9449:1073 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Change Parent Config Slot button that''s required to (change the parent of a config slot.)' WHERE parm_name = 'ACTION_CHANGE_PARENT_CONFIG_SLOT';


--changeSet OPER-9449:1074 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the attachments on reference document definitions. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REF_DOC_ATTACHMENT_ORDER';


--changeSet OPER-9449:1075 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of measurements on reference document definitions by clicking the Change Measurement Order button. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REF_DOC_MEASUREMENT_ORDER';


--changeSet OPER-9449:1076 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of steps in reference document definitions by clicking the Change Step Order button. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REF_DOC_STEP_ORDER';


--changeSet OPER-9449:1077 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the technical reference documents on reference document definitions. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REF_DOC_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1078 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the attachments on requirement definitions. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REQ_ATTACHMENT_ORDER';


--changeSet OPER-9449:1079 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of measurements on requirement definitions by clicking the Change Measurement Order button. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REQ_MEASUREMENT_ORDER';


--changeSet OPER-9449:1080 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of steps on requirement definitions by clicking the Change Step Order button. Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_REQ_STEP_ORDER';


--changeSet OPER-9449:1081 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of the technical references on requirement definitions.' WHERE parm_name = 'ACTION_CHANGE_REQ_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1082 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert serial-controlled parts to batch-controlled parts by clicking the Change to Batch button. This applies to baseline parts (part numbers). Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_SERIALIZED_TO_BATCH';


--changeSet OPER-9449:1083 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert serial parts to tracked parts by clicking the Change to Tracked button. This applies to baseline parts (part numbers). Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_SERIALIZED_TO_TRACKED';


--changeSet OPER-9449:1084 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the names of tasks and faults.' WHERE parm_name = 'ACTION_CHANGE_TASK_NAME';


--changeSet OPER-9449:1085 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the order of job steps for actual (not baseline) tasks.' WHERE parm_name = 'ACTION_CHANGE_TASK_STEP_ORDER';


--changeSet OPER-9449:1086 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert tracked parts to serial-controlled parts by clicking the Change to Serialized button. This applies to baseline parts (part numbers). Typically enabled for engineer and technical records roles.' WHERE parm_name = 'ACTION_CHANGE_TRACKED_TO_SERIALIZED';


--changeSet OPER-9449:1087 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to execute custom labour allocation packages.' WHERE parm_name = 'ACTION_CHECK_AUTO_ASSIGN_LABOUR';


--changeSet OPER-9449:1088 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to check in tools.' WHERE parm_name = 'ACTION_CHECK_IN_TOOL';


--changeSet OPER-9449:1089 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to check out tools.' WHERE parm_name = 'ACTION_CHECK_OUT_TOOL';


--changeSet OPER-9449:1090 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to check stock levels.' WHERE parm_name = 'ACTION_CHECK_STOCK_LEVEL';


--changeSet OPER-9449:1091 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to clear the advisory from a part no.' WHERE parm_name = 'ACTION_CLEAR_ADVISORY';


--changeSet OPER-9449:1092 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to clear advisory on the inventory.' WHERE parm_name = 'ACTION_CLEAR_INVENTORY_ADVISORY';


--changeSet OPER-9449:1093 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to clear the workscope of a check/work order.' WHERE parm_name = 'ACTION_CLEAR_WORKSCOPE';


--changeSet OPER-9449:1094 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to close claims.' WHERE parm_name = 'ACTION_CLOSE_CLAIM';


--changeSet OPER-9449:1095 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to close events.' WHERE parm_name = 'ACTION_CLOSE_EVENT';


--changeSet OPER-9449:1096 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'When enabled, this parameter displays the Close Order button on the Order Details page. If a user manually closes an order, the assumption is that no more invoices are expected for the order. Normally an order is automatically closed once all lines have been matched with an invoice.' WHERE parm_name = 'ACTION_CLOSE_PO';


--changeSet OPER-9449:1097 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Close RFQ button that''s required to close requests for quotes.' WHERE parm_name = 'ACTION_CLOSE_RFQ';


--changeSet OPER-9449:1098 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark job cards as collected.' WHERE parm_name = 'ACTION_COLLECT_JIC';


--changeSet OPER-9449:1099 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to mark historic job cards as collected.' WHERE parm_name = 'ACTION_COLLECT_JIC_HISTORIC';


--changeSet OPER-9449:1100 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to commit workscopes for checks and /work orders.' WHERE parm_name = 'ACTION_COMMIT_SCOPE';


--changeSet OPER-9449:1101 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete checks and work orders.' WHERE parm_name = 'ACTION_COMPLETE_CHECK';


--changeSet OPER-9449:1102 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete work packages and all tasks in a work package by clicking the Batch Complete All Tasks button. This button is on the Work Package Details page.' WHERE parm_name = 'ACTION_COMPLETE_CHECK_AND_SUBTASKS';


--changeSet OPER-9449:1103 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete events.' WHERE parm_name = 'ACTION_COMPLETE_EVENT';


--changeSet OPER-9449:1104 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete flights.' WHERE parm_name = 'ACTION_COMPLETE_FLIGHT';


--changeSet OPER-9449:1105 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Complete Put Away button that''s required to verify that put-away details are correct as a final step when serviceable inventory items are stored in the warehouse.' WHERE parm_name = 'ACTION_COMPLETE_PUTAWAY';


--changeSet OPER-9449:1106 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete component work orders as unserviceable. This means that after the work order is completed, the status of the inventory item will be unserviceable.' WHERE parm_name = 'ACTION_COMPLETE_RO_UNSERVICEABLE';


--changeSet OPER-9449:1107 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete tasks by clicking the Complete Task button. When you complete a task by using this method, part changes, measurements, tools, job steps, etc. are not recorded.' WHERE parm_name = 'ACTION_COMPLETE_TASK';


--changeSet OPER-9449:1108 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Complete Transfer button that''s required to .' WHERE parm_name = 'ACTION_COMPLETE_TRANSFER';


--changeSet OPER-9449:1109 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to complete turn ins.' WHERE parm_name = 'ACTION_COMPLETE_TURNIN';


--changeSet OPER-9449:1110 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to condemn inventory items.' WHERE parm_name = 'ACTION_CONDEMN_INVENTORY';


--changeSet OPER-9449:1111 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Confirm Receipt button that''s required to confirm that shipments to component repair vendors were received by the vendors.' WHERE parm_name = 'ACTION_CONFIRM_RECEIPT';


--changeSet OPER-9449:1112 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert borrow orders to purchase orders.' WHERE parm_name = 'ACTION_CONVERT_BORROW_TO_PO';


--changeSet OPER-9449:1113 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert borrow orders to exchange orders.' WHERE parm_name = 'ACTION_CONVERT_BORROW_TO_XCHG';


--changeSet OPER-9449:1114 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert  purchase orders to exchange orders.' WHERE parm_name = 'ACTION_CONVERT_PO_TO_XCHG';


--changeSet OPER-9449:1115 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert repair orders to exchange orders.' WHERE parm_name = 'ACTION_CONVERT_RO_TO_XCHG';


--changeSet OPER-9449:1116 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to convert exchange orders to purchase orders.' WHERE parm_name = 'ACTION_CONVERT_XCHG_TO_PO';


--changeSet OPER-9449:1117 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to copy extraction rules.' WHERE parm_name = 'ACTION_COPY_EXTRACTION_RULE';


--changeSet OPER-9449:1118 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to copy Forecast Models.' WHERE parm_name = 'ACTION_COPY_FORECAST_MODEL';


--changeSet OPER-9449:1119 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new financial accounts.' WHERE parm_name = 'ACTION_CREATE_ACCOUNT';


--changeSet OPER-9449:1120 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create ad hoc part requests.' WHERE parm_name = 'ACTION_CREATE_ADHOC_PART_REQUEST';


--changeSet OPER-9449:1121 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create adhoc purchase requests.' WHERE parm_name = 'ACTION_CREATE_ADHOC_PURCHASE_REQUEST';


--changeSet OPER-9449:1122 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Ad-Hoc RFQ button on a To Do List. (The button must assigned to a To Do List for a role that the user is assigned).' WHERE parm_name = 'ACTION_CREATE_ADHOC_RFQ';


--changeSet OPER-9449:1123 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create ad hoc purchase orders.' WHERE parm_name = 'ACTION_CREATE_AD_HOC_PO';


--changeSet OPER-9449:1124 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create alternate part numbers by clicking the Create Alternate Part button from the Part Search page or the Part Details page. When an alternate part number is created it is assigned to every part group of the original part number.' WHERE parm_name = 'ACTION_CREATE_ALTERNATE_PART';


--changeSet OPER-9449:1125 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create approval levels.' WHERE parm_name = 'ACTION_CREATE_APPROVAL_LEVEL';


--changeSet OPER-9449:1126 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create approval workflows.' WHERE parm_name = 'ACTION_CREATE_APPROVAL_WORKFLOW';


--changeSet OPER-9449:1127 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new assemblies.' WHERE parm_name = 'ACTION_CREATE_ASSEMBLY';


--changeSet OPER-9449:1128 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create and assign tasks to work packages by clicking the Create New Task button (from the Work Package Details page). These tasks are created against specific inventory and can be created based on task definitions or as ad-hoc tasks.' WHERE parm_name = 'ACTION_CREATE_ASSIGNED_TASK';


--changeSet OPER-9449:1129 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Assembly Warranty Contract button.' WHERE parm_name = 'ACTION_CREATE_ASSMBL_WARRANTY_CONTRACT';


--changeSet OPER-9449:1130 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Authority button. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_CREATE_AUTHORITY';


--changeSet OPER-9449:1131 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create bin locations.' WHERE parm_name = 'ACTION_CREATE_BIN_LOCATION';


--changeSet OPER-9449:1132 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create aircraft work packages.' WHERE parm_name = 'ACTION_CREATE_BLANK_CHECK';


--changeSet OPER-9449:1133 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Block button.' WHERE parm_name = 'ACTION_CREATE_BLOCK';


--changeSet OPER-9449:1134 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Create Block Revision button.' WHERE parm_name = 'ACTION_CREATE_BLOCK_REVISION';


--changeSet OPER-9449:1135 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create borrow orders.' WHERE parm_name = 'ACTION_CREATE_BO';


--changeSet OPER-9449:1136 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to create new bom parts.' WHERE parm_name = 'ACTION_CREATE_BOM_PART';


--changeSet OPER-9449:1137 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign a user to a quarantine corrective action.' WHERE parm_name = 'ACTION_ASSIGN_USER_TO_QUARANTINE_ACTION';


--changeSet OPER-9449:1138 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Vendor button that''s required to assign vendors to organizations.' WHERE parm_name = 'ACTION_ASSIGN_VENDOR_TO_ORGANIZATION';


--changeSet OPER-9449:1139 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Warranty button that''s required to assign warranty contracts to lines in purchase orders.' WHERE parm_name = 'ACTION_ASSIGN_WARRANTY_CONTRACT';


--changeSet OPER-9449:1140 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Part No icon that''s required to assign parts to component or assembly type warranty contracts.' WHERE parm_name = 'ACTION_ASSIGN_WARRANTY_DEFN_PART';


--changeSet OPER-9449:1141 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Reference Document Definition icon that''s require to assign reference document definitions to warranty contracts.' WHERE parm_name = 'ACTION_ASSIGN_WARRANTY_DEFN_REF_DOC';


--changeSet OPER-9449:1142 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Requirement Definition icon that''s required to assign requirement definitions to warranty contracts.' WHERE parm_name = 'ACTION_ASSIGN_WARRANTY_DEFN_REQ_DEFN';


--changeSet OPER-9449:1143 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference icon that''s required to add technical references to warranty contracts.' WHERE parm_name = 'ACTION_ASSIGN_WARRANTY_DEFN_TECH_REF';


--changeSet OPER-9449:1144 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign locations to workscope tasks.' WHERE parm_name = 'ACTION_ASSIGN_WS_TASK_LOCATION';


--changeSet OPER-9449:1145 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to use the attach/detach functions for inventory.' WHERE parm_name = 'ACTION_ATTACH_DETACH_INVENTORY';


--changeSet OPER-9449:1146 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to authorize purchase orders.' WHERE parm_name = 'ACTION_AUTHORIZE_PO';


--changeSet OPER-9449:1147 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to set the oil consumption rate status to automatic.' WHERE parm_name = 'ACTION_AUTO_UPDATE_ESCALATE_OIL_CONSUMPTION_STATUS';


--changeSet OPER-9449:1148 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to batch complete tasks.' WHERE parm_name = 'ACTION_BATCH_COMPLETE_TASK';


--changeSet OPER-9449:1149 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to lock blocks.' WHERE parm_name = 'ACTION_BLOCK_LOCK';


--changeSet OPER-9449:1150 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to unlock blocks.' WHERE parm_name = 'ACTION_BLOCK_UNLOCK';


--changeSet OPER-9449:1151 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to calculate values for stock levels.' WHERE parm_name = 'ACTION_CALC_STOCK_LEVELS';


--changeSet OPER-9449:1152 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel tasks based on task definitions.' WHERE parm_name = 'ACTION_CANCEL_BL_TASK';


--changeSet OPER-9449:1153 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel checks or work orders.' WHERE parm_name = 'ACTION_CANCEL_CHECK';


--changeSet OPER-9449:1154 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel claims.' WHERE parm_name = 'ACTION_CANCEL_CLAIM';


--changeSet OPER-9449:1155 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel part requests.' WHERE parm_name = 'ACTION_CANCEL_PART_REQUEST';


--changeSet OPER-9449:1156 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel purchase orders.' WHERE parm_name = 'ACTION_CANCEL_PO';


--changeSet OPER-9449:1157 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel purchase order invoices.' WHERE parm_name = 'ACTION_CANCEL_PO_INVOICE';


--changeSet OPER-9449:1158 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel put away tickets.' WHERE parm_name = 'ACTION_CANCEL_PUTAWAY';


--changeSet OPER-9449:1159 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Cancel RFQ button that''s required to cancel requests for quotes.' WHERE parm_name = 'ACTION_CANCEL_RFQ';


--changeSet OPER-9449:1160 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Cancel Shipment button.' WHERE parm_name = 'ACTION_CANCEL_SHIPMENT';


--changeSet OPER-9449:1161 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel tasks or faults.' WHERE parm_name = 'ACTION_CANCEL_TASK_OR_FAULT';


--changeSet OPER-9449:1162 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to cancel transfers.' WHERE parm_name = 'ACTION_CANCEL_TRANSFER';


--changeSet OPER-9449:1163 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Cancel Turn In button that''s required to cancel turns ins and return inventory items to the location they were issued to.' WHERE parm_name = 'ACTION_CANCEL_TURNIN';


--changeSet OPER-9449:1164 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Cascade Parameter button that''s required to assign a usage parameter to all sub-configuration slots. If there are multiple layers of sub-configurator slots, the cascading applies to all layers.' WHERE parm_name = 'ACTION_CASCADE_USAGE_PARMS_CONFIG_SLOT';


--changeSet OPER-9449:1165 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to sign off on work completed to indicate that the work is certified as being performed correctly.' WHERE parm_name = 'ACTION_CERTIFY_TASK';


--changeSet OPER-9449:1166 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit certification after a task is completed (but before the work package is completed).' WHERE parm_name = 'ACTION_CERTIFY_TASK_AFTER_TASK_COMPLETE';


--changeSet OPER-9449:1167 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Vendor icon that''s required to add authorized repair or purchase vendors to warranty contract details (terms & conditions).' WHERE parm_name = 'ACTION_ADD_VENDOR';


--changeSet OPER-9449:1168 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission required to see the Add Account icon that''s required to add accounts that are associated with vendors to vendor details.' WHERE parm_name = 'ACTION_ADD_VENDOR_ACCOUNT';


--changeSet OPER-9449:1169 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Airport icon that''s required to add airport locations to vendor details.' WHERE parm_name = 'ACTION_ADD_VENDOR_AIRPORT';


--changeSet OPER-9449:1170 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment icon that''s required to add attachments to vendor details.' WHERE parm_name = 'ACTION_ADD_VENDOR_ATTACHMENT';


--changeSet OPER-9449:1171 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add vendor part price.' WHERE parm_name = 'ACTION_ADD_VENDOR_PART_PRICE';


--changeSet OPER-9449:1172 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference icon that''s required to add tech references to vendor details.' WHERE parm_name = 'ACTION_ADD_VENDOR_TECH_REF';


--changeSet OPER-9449:1173 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Vendor button that''s required to add vendors to requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_VENDOR_TO_RFQ';


--changeSet OPER-9449:1174 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment icon that''s required to add attachments to warranty contracts.' WHERE parm_name = 'ACTION_ADD_WARRANTY_ATTACHMENT';


--changeSet OPER-9449:1175 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow an airdraft to be scheduled by Line Planning Automation.' WHERE parm_name = 'ACTION_ALLOW_LPA_AIRCRAFT';


--changeSet OPER-9449:1176 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to allow a Work Package to be hidden from Line Planning Automation.' WHERE parm_name = 'ACTION_ALLOW_LPA_WORK_PACKAGE';


--changeSet OPER-9449:1177 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to change the status of a task definition from Do Not Execute by clicking the Allow Execution'' button on the Task Definition Details page. Allowing execution changes the status for the task definition and for all corresponding actual tasks. Allow execution changes do not require Baseline Synch to run before they go into effect - the changes are effected immediately. Allow Execution is a toggle button whose opposite, Prevent Execution, is controlled with the ACTION_PREVENT_TASK_DEFINITION_EXECUTION parameter.' WHERE parm_name = 'ACTION_ALLOW_TASK_DEFINITION_EXECUTION';


--changeSet OPER-9449:1178 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Approve block.' WHERE parm_name = 'ACTION_APPROVE_BLOCK';


--changeSet OPER-9449:1179 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Approve JIC.' WHERE parm_name = 'ACTION_APPROVE_JIC';


--changeSet OPER-9449:1180 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Approve MPC.' WHERE parm_name = 'ACTION_APPROVE_MPC';


--changeSet OPER-9449:1181 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to approve a Part Catalog entry (from BUILD).' WHERE parm_name = 'ACTION_APPROVE_PART';


--changeSet OPER-9449:1182 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Approve reference document.' WHERE parm_name = 'ACTION_APPROVE_REFDOC';


--changeSet OPER-9449:1183 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Approve requirement.' WHERE parm_name = 'ACTION_APPROVE_REQ';


--changeSet OPER-9449:1184 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Approve Vendor button that''s required to indicate that vendors are  approved for orders, such as purchase, repair, exchange. (The same vendor can be unapproved for repairs, but approved for exchange).' WHERE parm_name = 'ACTION_APPROVE_VENDOR_ORDER_TYPE';


--changeSet OPER-9449:1185 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Approve Vendor button that''s required to indicate that services performed by vendors are acceptable. The same vendor can be approved for one type of service, but unapproved for another type of service.' WHERE parm_name = 'ACTION_APPROVE_VENDOR_SERVICE_TYPE';


--changeSet OPER-9449:1186 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to archive alert.' WHERE parm_name = 'ACTION_ARCHIVE_ALERT';


--changeSet OPER-9449:1187 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to archive an Inventory item.' WHERE parm_name = 'ACTION_ARCHIVE_INVENTORY';


--changeSet OPER-9449:1188 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Airport button that''s required to assign airports to vendor-part exchange relationships.  The opposite action is controlled with the ACTION_UNASSIGN_AIRPORT permission.' WHERE parm_name = 'ACTION_ASSIGN_AIRPORT';


--changeSet OPER-9449:1189 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Alert Role button that''s required to add roles that will be alerted when faults are deferred.' WHERE parm_name = 'ACTION_ASSIGN_ALERT_ROLE';


--changeSet OPER-9449:1190 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Authority button that''s required to assign authorities to users and permit them to access data. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)  The opposite action is controlled with the ACTION_UNASSIGN_AUTHORITY permission.' WHERE parm_name = 'ACTION_ASSIGN_AUTHORITY';


--changeSet OPER-9449:1191 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Data Value button that''s required to assign a data value' WHERE parm_name = 'ACTION_ASSIGN_DATA_VALUE';


--changeSet OPER-9449:1192 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Mark As Primary button that''s required to assign default organizations to users.' WHERE parm_name = 'ACTION_ASSIGN_DEFAULT_ORGANIZATION';


--changeSet OPER-9449:1193 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Department button that''s required to assign departments to locations.' WHERE parm_name = 'ACTION_ASSIGN_DEPARTMENT';


--changeSet OPER-9449:1194 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Location button that''s required to assign locations to departments.' WHERE parm_name = 'ACTION_ASSIGN_DEPARTMENT_LOCATIONS';


--changeSet OPER-9449:1195 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign User button that''s required to assign users to departments.' WHERE parm_name = 'ACTION_ASSIGN_DEPARTMENT_USERS';


--changeSet OPER-9449:1196 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Exchange Vendor button that''s required to assign exchange vendors to parts.' WHERE parm_name = 'ACTION_ASSIGN_EXCHANGE_VENDOR';


--changeSet OPER-9449:1197 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Fault Definition button that''s required to assign a fault definition to a failure effect OR to assign a fault' WHERE parm_name = 'ACTION_ASSIGN_FAULT_DEFINITION';


--changeSet OPER-9449:1198 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign a fault into a work order on a sub-component.' WHERE parm_name = 'ACTION_ASSIGN_FAULT_TO_COMPONENT_WORK_ORDER';


--changeSet OPER-9449:1199 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Forecast Model that''s required to assign forecast models to authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_ASSIGN_FCMODEL_AUTHORITY';


--changeSet OPER-9449:1200 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Install Kit To Part Group icon.' WHERE parm_name = 'ACTION_ASSIGN_INSTALL_KIT_TO_PART_GROUP';


--changeSet OPER-9449:1201 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Inventory button that''s required to assign inventory items to authorities. (Items are grouped by authorities to restrict access to users that have corresponding authorities.)' WHERE parm_name = 'ACTION_ASSIGN_INV_AUTHORITY';


--changeSet OPER-9449:1202 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to assign licenses to users. The ACTION_ASSIGN_USER_LICENSE permission is also required to see the Assign Licesnes icon.' WHERE parm_name = 'ACTION_ASSIGN_LICENSE';


--changeSet OPER-9449:1203 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Parent Location button that''s required to assign sub-locations as parents.' WHERE parm_name = 'ACTION_ASSIGN_LOCATION';


--changeSet OPER-9449:1204 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Organization button that''s required to assign organizations to locations.' WHERE parm_name = 'ACTION_ASSIGN_LOCATION_ORGANIZATION';


--changeSet OPER-9449:1205 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Menu Item button that''s required to assign menu items to roles.' WHERE parm_name = 'ACTION_ASSIGN_MENU_ITEM';


--changeSet OPER-9449:1206 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the View Opportunistic Tasks button that''s required to see a list of tasks that could be added to a work package. (Tasks in work packages can have opportunistic task links to blocks or requirements that aren''t in the work package).' WHERE parm_name = 'ACTION_ASSIGN_OPPRTNSTC_TASK_TO_PACKAGE';


--changeSet OPER-9449:1207 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Warranty button that''s required to assign pre-defined warranty contracts to lines in purchase orders.' WHERE parm_name = 'ACTION_ASSIGN_ORDER_WARRANTY';


--changeSet OPER-9449:1208 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Organization button that''s required to assign organizations to users.' WHERE parm_name = 'ACTION_ASSIGN_ORGANIZATION';


--changeSet OPER-9449:1209 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Repair Location icon  that''s required to assign a repair location to a part.' WHERE parm_name = 'ACTION_ASSIGN_PART_REPAIR_LOCATION';


--changeSet OPER-9449:1210 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Vendor icon that''s required to modify part purchase vendor details.' WHERE parm_name = 'ACTION_EDIT_PURCHASE_VENDOR';


--changeSet OPER-9449:1211 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit a quarantine corrective action.' WHERE parm_name = 'ACTION_EDIT_QUARANTINE_ACTION';


--changeSet OPER-9449:1212 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit query.' WHERE parm_name = 'ACTION_EDIT_QUERY';


--changeSet OPER-9449:1213 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Reference Document  button.  Not all reference document information can be edited with this permission. See other permissions in the Maint Program - Reference Documents  category.' WHERE parm_name = 'ACTION_EDIT_REF_DOC';


--changeSet OPER-9449:1214 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Impacts icon that''s required to edit impacts in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_IMPACT';


--changeSet OPER-9449:1215 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Labor Requirement icon that''s required to edit labor requirements in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_LABOUR_REQUIREMENT';


--changeSet OPER-9449:1216 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Measurement Specific Scheduling Rule icon that''s required to edit the rules in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_MEASURE_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:1217 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Part Requirement icon that''s required to edit part requirements in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_PART_REQUIREMENT';


--changeSet OPER-9449:1218 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Part Specific Scheduling Rule icon that''s required to edit the  rules in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_PART_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:1219 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Scheduling Rule icon that''s required to edit scheduling rules in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_SCHEDULING_RULES';


--changeSet OPER-9449:1220 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Step icon that''s required to edit steps in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_STEP';


--changeSet OPER-9449:1221 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Tool Requirement icon that''s required to edit tool requirements in  reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_DOC_TOOL_REQUIREMENT';


--changeSet OPER-9449:1222 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Tail Number Specific Scheduling Rule icon that''s required to edit the rules in reference document definitions.' WHERE parm_name = 'ACTION_EDIT_REF_TAIL_NO_SPECIFIC_SCHED_RULE';


--changeSet OPER-9449:1223 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit RFQ icon that''s required edit requests for quotes (RFQs).' WHERE parm_name = 'ACTION_EDIT_RFQ';


--changeSet OPER-9449:1224 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit  Attachment icon that''s required to edit request for quote (RFQ) attachments.' WHERE parm_name = 'ACTION_EDIT_RFQ_ATTACHMENT';


--changeSet OPER-9449:1225 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit RFQ Lines button that''s required to edit the lines of requests for quotes (RFQs).' WHERE parm_name = 'ACTION_EDIT_RFQ_LINES';


--changeSet OPER-9449:1226 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Attachment icon that''s required to edit details about documents that are attached to shipments.' WHERE parm_name = 'ACTION_EDIT_SHIPMENT_ATTACHMENT';


--changeSet OPER-9449:1227 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Shipment icon that''s required to modify shipment details.' WHERE parm_name = 'ACTION_EDIT_SHIPMENT_DETAILS';


--changeSet OPER-9449:1228 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Note icon that''s required to modify the notes for individual line items in shipments.' WHERE parm_name = 'ACTION_EDIT_SHIPMENT_LINE_NOTES';


--changeSet OPER-9449:1229 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Routing icon that''s required to open the Edit Routing page where you can modify shipping segments for shipments that pass through multiple destinations before reaching their final destinations.' WHERE parm_name = 'ACTION_EDIT_SHIPMENT_ROUTING';


--changeSet OPER-9449:1230 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Button Order button that''s required to reorder menus for a to-do list.' WHERE parm_name = 'ACTION_EDIT_TODO_BUTTON_ORDER';


--changeSet OPER-9449:1231 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit To-Do List icon.' WHERE parm_name = 'ACTION_EDIT_TODO_LIST';


--changeSet OPER-9449:1232 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Tab Order button that''s required to reorder tabs on to-do lists.' WHERE parm_name = 'ACTION_EDIT_TODO_TAB_ORDER';


--changeSet OPER-9449:1233 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit Parameter icon that''s required to edit assembly usage parameters.' WHERE parm_name = 'ACTION_EDIT_USAGE_PARM';


--changeSet OPER-9449:1234 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to edit Usage Snapshots on maintenance Tasks.' WHERE parm_name = 'ACTION_EDIT_USAGE_SNAPSHOT';


--changeSet OPER-9449:1235 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit User icon that''s required to edit users'' details.' WHERE parm_name = 'ACTION_EDIT_USER';


--changeSet OPER-9449:1236 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Edit User icon that''s required to edit users'' license information.' WHERE parm_name = 'ACTION_EDIT_USER_LICENSE';


--changeSet OPER-9449:1237 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to accept possible faults by clicking a button on the Inventory Details page. Possible faults are created in Maintenix by the diagnostics adapter.' WHERE parm_name = 'ACTION_ACCEPT_POSSIBLE_FAULTS';


--changeSet OPER-9449:1238 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to click the Accept Suggested Value Pct button on the Edit Kit Contents page. This button sets value percentages for all line items in a kit based on Maintenix''s evaluation of the unit price of the standard part numbers or the average unit price. (The value percentage for a kit line item is the percentage of the total kit contents that an item should comprise.) Without this permission, users set the value percentages manually.' WHERE parm_name = 'ACTION_ACCEPT_SUGGESTED_VALUE_PCT';


--changeSet OPER-9449:1239 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to acknowledge alert  by clicking the Acknowledge button on the Alert List tab. Acknowledging alerts indicates to users or user groups that the alert was processed.' WHERE parm_name = 'ACTION_ACKNOWLEDGE_ALERT';


--changeSet OPER-9449:1240 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Disposition Reference Document button that''s required to activate a reference document definition that''s in build or revision status.' WHERE parm_name = 'ACTION_ACTIVATE_REF_DOC';


--changeSet OPER-9449:1241 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the  Activate Requirement button.' WHERE parm_name = 'ACTION_ACTIVATE_REQ';


--changeSet OPER-9449:1242 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Activate button that''s required to change the status of warranty contracts from build to active status.' WHERE parm_name = 'ACTION_ACTIVATE_WARRANTY_CONTRACT';


--changeSet OPER-9449:1243 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the xx button to add advisories to requirements. Related access permissions for servlets and pages are required.' WHERE parm_name = 'ACTION_ADD_ADVISORY';


--changeSet OPER-9449:1244 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Op Restriction icon that''s required to add operational restrictions to historic faults. To use this permission, roles also require the ACTION_ADD_OP_RESTRICTION permission.' WHERE parm_name = 'ACTION_ADD_HISTORIC_OP_RESTRICTION';


--changeSet OPER-9449:1245 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Inventory Shipment Line button that''s required to add inventory items to shipments.' WHERE parm_name = 'ACTION_ADD_INVENTORY_SHIPMENT_LINE';


--changeSet OPER-9449:1246 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference icon that''s required to add references to job card definitions.' WHERE parm_name = 'ACTION_ADD_JIC_TECHNICAL_REFERENCE';


--changeSet OPER-9449:1247 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Link To Task icon that''s required to add linked tasks to reference document definitions.' WHERE parm_name = 'ACTION_ADD_LINK_TO_REF_DOC';


--changeSet OPER-9449:1248 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Location icon that''s required to add authorized internal repair locations from warranty contract details (terms & conditions).' WHERE parm_name = 'ACTION_ADD_LOCATION';


--changeSet OPER-9449:1249 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Misc Line button that''s required to add miscellaneous lines to requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_MISC_RFQ_LINE';


--changeSet OPER-9449:1250 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add non-repairable part.' WHERE parm_name = 'ACTION_ADD_NON_REP_PART';


--changeSet OPER-9449:1251 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add notes to a historic task.' WHERE parm_name = 'ACTION_ADD_NOTES_TO_HISTORIC_TASK';


--changeSet OPER-9449:1252 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Note button that''s required to add notes that are retained in shipment histories.' WHERE parm_name = 'ACTION_ADD_NOTES_TO_SHIPMENT';


--changeSet OPER-9449:1253 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add notes to a task.' WHERE parm_name = 'ACTION_ADD_NOTES_TO_TASK';


--changeSet OPER-9449:1254 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Note button that''s required to add notes that are retained in transfer histories.' WHERE parm_name = 'ACTION_ADD_NOTES_TO_TRANSFER';


--changeSet OPER-9449:1255 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add the non routine labor estimation to a task.' WHERE parm_name = 'ACTION_ADD_NR_LABOR_ESTIMATION';


--changeSet OPER-9449:1256 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Op Restriction icon that''s required to add operational restrictions to faults.' WHERE parm_name = 'ACTION_ADD_OP_RESTRICTION';


--changeSet OPER-9449:1257 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add part.' WHERE parm_name = 'ACTION_ADD_PART';


--changeSet OPER-9449:1258 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Repair Part and Assign Repair Vendor buttons that are required to add parts to vendor lists and vendors to part lists.' WHERE parm_name = 'ACTION_ADD_PART_REPAIR_VENDOR';


--changeSet OPER-9449:1259 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add part requirements to a task.' WHERE parm_name = 'ACTION_ADD_PART_REQUIREMENT';


--changeSet OPER-9449:1260 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Part Line button that''s required to add part lines to requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_PART_RFQ_LINE';


--changeSet OPER-9449:1261 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Part Shipment Line button that''s required to add parts to shipments.' WHERE parm_name = 'ACTION_ADD_PART_SHIPMENT_LINE';


--changeSet OPER-9449:1262 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Purchase Part and Assign Purchase Vendor buttons that are required to add parts to vendor lists and vendors to part lists.' WHERE parm_name = 'ACTION_ADD_PURCHASE_VENDOR';


--changeSet OPER-9449:1263 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to add a price for a part that is specific to a purchase vendor.' WHERE parm_name = 'ACTION_ADD_PURCHASE_VENDOR_PRICE';


--changeSet OPER-9449:1264 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Attachment icon that''s required to add attachments to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_ATTACHMENT';


--changeSet OPER-9449:1265 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Following Task icon that''s required to add following tasks to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_FOLLOWING_TASK';


--changeSet OPER-9449:1266 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Impacts icon that''s required to add impacts to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_IMPACT';


--changeSet OPER-9449:1267 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Labor Requirement icon that''s required to add labor requirements to  reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_LABOUR_REQUIREMENT';


--changeSet OPER-9449:1268 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Assign Measurement icon that''s required to assign measurements to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_MEASUREMENT';


--changeSet OPER-9449:1269 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Measurement Specific Scheduling Rule icon that''s required to add rules to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_MEASURE_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:1270 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Panel icon that''s required to add panels to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_PANEL';


--changeSet OPER-9449:1271 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add a Part Requirement icon that''s required to add part requirements to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_PART_REQUIREMENT';


--changeSet OPER-9449:1272 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Part Specific Scheduling Rule icon that''s required to add rules to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_PART_SPECIFIC_SCHEDULING_RULE';


--changeSet OPER-9449:1273 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Scheduling Rule icon that''s required to add scheduling rules to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_SCHEDULING_RULE';


--changeSet OPER-9449:1274 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Step icon that''s required to add steps to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_STEP';


--changeSet OPER-9449:1275 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Technical Reference icon that''s required to add technical references to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_TECHNICAL_REFERENCE';


--changeSet OPER-9449:1276 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Tool Requirement icon that''s required to add tool requirements to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_TOOL_REQUIREMENT';


--changeSet OPER-9449:1277 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Zone icon that''s required to add zones to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_DOC_ZONE';


--changeSet OPER-9449:1278 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Tail Number Specific Scheduling Rule to reference document definitions.' WHERE parm_name = 'ACTION_ADD_REF_TAIL_NO_SPECIFIC_SCHED_RULE';


--changeSet OPER-9449:1279 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add/Remove Charge button that''s required to add or remove charges from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_REMOVE_RFQ_CHARGE';


--changeSet OPER-9449:1280 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add/Remove Tax button that''s required to add or remove taxes from requests for quotes (RFQs).' WHERE parm_name = 'ACTION_ADD_REMOVE_RFQ_TAX';


--changeSet OPER-9449:1281 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Pick Items For Kit page that''s required to select the inventory items that are to be included in a kit.' WHERE parm_name = 'ACTION_JSP_WEB_KIT_PICK_ITEMS_FOR_KIT';


--changeSet OPER-9449:1282 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Signoff Requirement page.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_ADD_SIGNOFF_REQUIREMENT';


--changeSet OPER-9449:1283 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Labor Requirement page that''s required to add a skill that is required to complete a task or fault.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_CREATE_LABOUR';


--changeSet OPER-9449:1284 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor History page.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_HISTORY';


--changeSet OPER-9449:1285 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor Requirements page that''s required to modify existing labor requirements in actual tasks.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_EDIT_LABOUR_REQUIREMENTS';


--changeSet OPER-9449:1286 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor History page.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_EDIT_WPLABOUR_HISTORY';


--changeSet OPER-9449:1287 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Find Human Resource page.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_FIND_HUMAN_RESOURCE';


--changeSet OPER-9449:1288 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Labor Details page that''s required to assign labor requirements to resources.' WHERE parm_name = 'ACTION_JSP_WEB_LABOUR_LABOUR_ASSIGNMENT';


--changeSet OPER-9449:1289 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Activate License Definition page.' WHERE parm_name = 'ACTION_JSP_WEB_LICENSEDEFN_ACTIVATE_LICENCE_DEFN';


--changeSet OPER-9449:1290 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create License Definition and Edit License Definition pages.' WHERE parm_name = 'ACTION_JSP_WEB_LICENSEDEFN_CREATE_EDIT_LICENSE_DEFN';


--changeSet OPER-9449:1291 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the License Definition Details page.' WHERE parm_name = 'ACTION_JSP_WEB_LICENSEDEFN_LICENSE_DEFN_DETAILS';


--changeSet OPER-9449:1292 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to modify the received date or manufactured date when editing inventory details. The ACTION_EDIT_INV_DETAILS permission is also required.' WHERE parm_name = 'ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY';


--changeSet OPER-9449:1293 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to see the Add Note icon  that''s required to add notes to part requests.' WHERE parm_name = 'ACTION_ADD_PART_REQUEST_NOTE';


--changeSet OPER-9449:1294 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Return Inventory page that''s required to select the inventory to return for an exchange order.' WHERE parm_name = 'ACTION_JSP_WEB_PO_SELECT_RETURN_INVENTORY';


--changeSet OPER-9449:1295 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Archive Tax and Archive Charge pages.' WHERE parm_name = 'ACTION_JSP_WEB_PROCUREMENT_ARCHIVE_TAX_CHARGE';


--changeSet OPER-9449:1296 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Quarantine Details page.' WHERE parm_name = 'ACTION_JSP_WEB_QUARANTINE_QUARANTINE_DETAILS';


--changeSet OPER-9449:1297 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Compliance Report page.' WHERE parm_name = 'ACTION_JSP_WEB_REPORT_COMPLIANCE_REPORT';


--changeSet OPER-9449:1298 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Report Configuration and Edit Report Configuration pages.' WHERE parm_name = 'ACTION_JSP_WEB_REPORT_CREATE_EDIT_REPORT_CONFIG';


--changeSet OPER-9449:1299 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Report Configuration page.' WHERE parm_name = 'ACTION_JSP_WEB_REPORT_REPORT_CONFIG';


--changeSet OPER-9449:1300 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Purchase Request page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_CREATE_PURCHASE_REQUEST';


--changeSet OPER-9449:1301 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Consignment Request and Create Purchase Request pages.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_CREATE_PURCHASE_REQUEST_FOR_STOCK';


--changeSet OPER-9449:1302 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit External Reference page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_EDIT_EXTERNAL_REFERENCE';


--changeSet OPER-9449:1303 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Request Priority page that''s required to change the priority of a part request.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_EDIT_REQUEST_PRIORITY';


--changeSet OPER-9449:1304 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Issue Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_ISSUE_INVENTORY';


--changeSet OPER-9449:1305 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Request Details page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_PART_REQUEST_DETAILS';


--changeSet OPER-9449:1306 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Part Request Search page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_PART_REQUEST_SEARCH';


--changeSet OPER-9449:1307 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reserve Local Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_RESERVE_LOCAL_INVENTORY';


--changeSet OPER-9449:1308 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reserve On Order Inventory.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_RESERVE_ON_ORDER_INVENTORY';


--changeSet OPER-9449:1309 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reserve Remote Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_RESERVE_REMOTE_INVENTORY';


--changeSet OPER-9449:1310 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Requested Part page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_SELECT_REQUESTED_PART';


--changeSet OPER-9449:1311 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Steal Remote Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_STEAL_REMOTE_RESERVATION';


--changeSet OPER-9449:1312 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Review Unexpected Turn Ins page.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_UNEXPECTED_TURN_INS';


--changeSet OPER-9449:1313 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Note page that''s required to add or edit notes in a part request.' WHERE parm_name = 'ACTION_JSP_WEB_REQ_PARTREQUESTDETAILS_EDIT_NOTE';


--changeSet OPER-9449:1314 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Miscellaneous RFQ Line page.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_ADD_MISC_RFQLINE';


--changeSet OPER-9449:1315 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create RFQ and Edit RFQ pages.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_CREATE_EDIT_RFQ';


--changeSet OPER-9449:1316 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit RFQ Lines page.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_EDIT_RFQLINES';


--changeSet OPER-9449:1317 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Vendor Quote page.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_EDIT_VENDOR_QUOTES';


--changeSet OPER-9449:1318 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the RFQ Details page.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_RFQDETAILS';


--changeSet OPER-9449:1319 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the RFQ Search page.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_RFQSEARCH';


--changeSet OPER-9449:1320 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select PO page that''s required to add a vendor quote to an open purchase order.' WHERE parm_name = 'ACTION_JSP_WEB_RFQ_SELECT_PO';


--changeSet OPER-9449:1321 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Role and Edit Role pages.' WHERE parm_name = 'ACTION_JSP_WEB_ROLE_CREATE_EDIT_ROLE';


--changeSet OPER-9449:1322 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Role Details page.' WHERE parm_name = 'ACTION_JSP_WEB_ROLE_ROLE_DETAILS';


--changeSet OPER-9449:1323 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Role Search page.' WHERE parm_name = 'ACTION_JSP_WEB_ROLE_ROLE_SEARCH';


--changeSet OPER-9449:1324 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Role Permission Editor page.' WHERE parm_name = 'ACTION_JSP_WEB_ROLE_ROLE_SECURITY';


--changeSet OPER-9449:1325 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Advanced Search Page.' WHERE parm_name = 'ACTION_JSP_WEB_SEARCH_ADVANCED_SEARCH';


--changeSet OPER-9449:1326 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Capacity Pattern and Edit Capacity Pattern pages that are required to define nominal capacity of maintenance locations.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_CREATE_EDIT_CAPACITY_PATTERN';


--changeSet OPER-9449:1327 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Duplicate Capacity Pattern page that''s used to make a copy of an existing capacity pattern.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_DUPLICATE_CAPACITY_PATTERN';


--changeSet OPER-9449:1328 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor Profile page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_EDIT_LABOR_PROFILE';


--changeSet OPER-9449:1329 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor Skill page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_EDIT_LABOR_SKILLS';


--changeSet OPER-9449:1330 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Shifts for Day page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_EDIT_SHIFTS_FOR_DAY';


--changeSet OPER-9449:1331 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create User Shift Pattern and Edit User Shift Pattern pages.' WHERE parm_name = 'ACTION_JSP_WEB_SHIFT_USERSHIFTPATTERN_CREATE_EDIT_USER_SHIFT_PATTERN';


--changeSet OPER-9449:1332 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Shipment Line page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_ADD_SHIPMENT_LINE';


--changeSet OPER-9449:1333 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign To Waybill Group page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_ASSIGN_TO_WAYBILL_GROUP';


--changeSet OPER-9449:1334 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Shipment and Edit Shipment pages.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_CREATE_EDIT_SHIPMENT';


--changeSet OPER-9449:1335 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Shipment Line Notes page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_EDIT_SHIPMENT_LINE_NOTES';


--changeSet OPER-9449:1336 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Install Kits Available page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_INSTALL_KITS_AVAILABLE';


--changeSet OPER-9449:1337 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Receive Shipment page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_RECEIVE_SHIPMENT';


--changeSet OPER-9449:1338 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Send Shipment page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_SEND_SHIPMENT';


--changeSet OPER-9449:1339 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Shipment Details page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_SHIPMENT_DETAILS';


--changeSet OPER-9449:1340 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Shipment Search page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_SHIPMENT_SEARCH';


--changeSet OPER-9449:1341 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Routing page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_ROUTING_EDIT_SHIPMENT_ROUTING';


--changeSet OPER-9449:1342 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Receive Shipment page.' WHERE parm_name = 'ACTION_JSP_WEB_SHIPMENT_ROUTING_RECEIVE_SHIPMENT_SEGMENT';


--changeSet OPER-9449:1343 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Column and Edit Column pages that are required when designing a status board.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_CREATE_EDIT_STATUS_BOARD_COLUMN';


--changeSet OPER-9449:1344 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Assembly page that''s required to add an assembly to a status board.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_ADD_STATUS_BOARD_ASSMBL';


--changeSet OPER-9449:1345 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Column Group page that''s required when designing a status board.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_ADD_STATUS_BOARD_COLUMN_GROUP';


--changeSet OPER-9449:1346 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Status Board and Edit Status Board pages.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_CREATE_EDIT_STATUS_BOARD';


--changeSet OPER-9449:1347 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Note page that''s required to add a note next to an asset that is listed on a status board.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_EDIT_ASSET_NOTE';


--changeSet OPER-9449:1348 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Column Order page that''s require when designing a status board.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_EDIT_STATUS_BOARD_COLUMN_ORDER';


--changeSet OPER-9449:1349 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Status Board Details page.' WHERE parm_name = 'ACTION_JSP_WEB_STATUSBOARD_STATUS_BOARD_DETAILS';


--changeSet OPER-9449:1350 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Adjust Stock Percentage page.' WHERE parm_name = 'ACTION_JSP_WEB_STOCK_ADJUST_STOCK_PERCENTAGE';


--changeSet OPER-9449:1351 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Stock and Edit Stock pages.' WHERE parm_name = 'ACTION_JSP_WEB_STOCK_CREATE_EDIT_STOCK';


--changeSet OPER-9449:1352 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Stock Levels page.' WHERE parm_name = 'ACTION_JSP_WEB_STOCK_EDIT_STOCK_LEVELS';


--changeSet OPER-9449:1353 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Stock Details page.' WHERE parm_name = 'ACTION_JSP_WEB_STOCK_STOCK_DETAILS';


--changeSet OPER-9449:1354 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Stock Search page.' WHERE parm_name = 'ACTION_JSP_WEB_STOCK_STOCK_SEARCH';


--changeSet OPER-9449:1355 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Stock Level page.' WHERE parm_name = 'ACTION_JSP_WEB_STOCKLEVEL_CREATE_STOCK_LEVEL';


--changeSet OPER-9449:1356 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Measurements page that''s required to add measurement parameters to an actual task and record measurement values.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_ADD_MEASUREMENTS';


--changeSet OPER-9449:1357 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Step page that''s required to add job steps to an actusal task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_ADD_STEP';


--changeSet OPER-9449:1358 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Adjust Hours for Billing page that''s required to edit the number of hours for billing purposes.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_ADJUST_HOURS_FOR_BILLING';


--changeSet OPER-9449:1359 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Cancel Task page that''s required to cancel a single instance of a task so that it is not executed.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CANCEL_TASKS';


--changeSet OPER-9449:1360 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Package Delay page that''s required to change the estimated end date of a work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CHECK_DELAY';


--changeSet OPER-9449:1361 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Package Delay Details page that is required to see information about the delay of the end of a work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CHECK_DELAY_DETAILS';


--changeSet OPER-9449:1362 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Package Details page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CHECK_DETAILS';


--changeSet OPER-9449:1363 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Tasks to Work Package and Assign Faults to Work Package pages.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CHECK_SELECTION';


--changeSet OPER-9449:1364 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to acces the Complete Task Summary page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_COMPLETE_TASK_SUMMARY';


--changeSet OPER-9449:1365 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Work Package page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CREATE_OR_EDIT_CHECK';


--changeSet OPER-9449:1366 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Repetitive Task page that''s required to create recurring ad hoc tasks, such as regular inspections, required to monitor a fault.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CREATE_REPETITIVE_TASK';


--changeSet OPER-9449:1367 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Task page that''s required to manually create an actual task that is based on a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_CREATE_TASK_FROM_DEFINITION';


--changeSet OPER-9449:1368 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Defer Fault page that''s required to postpone resolving a fault and remove the fault from the active work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_DEFER_TASKS';


--changeSet OPER-9449:1369 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Estimated End Date that''s required to change the estimated end date for work packages.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_ESTIMATED_END_DATE';


--changeSet OPER-9449:1370 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Intervals page that''s required to change the scheduling interval in an actual task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_INTERVALS';


--changeSet OPER-9449:1371 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Measurements page for an actual task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_MEASUREMENTS';


--changeSet OPER-9449:1372 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Line Numbers page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_ROLINE_NUMBERS';


--changeSet OPER-9449:1373 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Start Values page that''s required to change the value from which a task''s deadline is calculated.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_START_VALUES';


--changeSet OPER-9449:1374 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Step Order page that''s required to change the order of job steps in an actual task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_STEP_ORDER';


--changeSet OPER-9449:1375 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Steps page that''s required to edit the description of job steps in an actual task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_STEPS';


--changeSet OPER-9449:1376 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Information page that''s required to edit some details about tasks.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_TASK';


--changeSet OPER-9449:1377 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Workscope Order page that''s required to change the order in which  task are to be executed in component work packages.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EDIT_WORKSCOPE_ORDER';


--changeSet OPER-9449:1378 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Job Cards with Newer Revision page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_JICS_WITH_NEWER_REV';


--changeSet OPER-9449:1379 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Complete Task page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_PACKAGE_AND_COMPLETE';


--changeSet OPER-9449:1380 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Preview Release page that''s required to complete a work package and release the asset back to operations.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_PREVIEW_RELEASE';


--changeSet OPER-9449:1381 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Schedule Work Package page that''s required to schedule a work package to an internal maintenance location or to an external vendor.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_SCHEDULE_CHECK';


--changeSet OPER-9449:1382 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Component page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_SCHEDULE_REMOVAL';


--changeSet OPER-9449:1383 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Find Work Package page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_SELECT_IN_WORK_CHECK';


--changeSet OPER-9449:1384 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Issue To Account page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_SET_ISSUE_TO_ACCOUNTS';


--changeSet OPER-9449:1385 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Plan By Date page that''s required to specify a date earlier than the task deadline by which it would be beneficial to complete the task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_SET_PLAN_BY_DATE';


--changeSet OPER-9449:1386 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Start Work Package page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_START_CHECK';


--changeSet OPER-9449:1387 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Details page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TASK_DETAILS';


--changeSet OPER-9449:1388 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access thre Task Priorities page that''s required to set or change the priority level of tasks.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TASK_PRIORITIES';


--changeSet OPER-9449:1389 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Search page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TASK_SEARCH';


--changeSet OPER-9449:1390 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Search page that provides restricted search criteria for finding actual tasks.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TASK_SEARCH_BY_TYPE';


--changeSet OPER-9449:1391 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Selection page that''s required to select a task definition on which to base a new task, or choose to create an ad hoc task.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TASK_SELECTION';


--changeSet OPER-9449:1392 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Terminate Task that''s required to cancel all instances of a task on a specific inventory item, meaning that the task will never be executed on that item.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_TERMINATE_TASKS';


--changeSet OPER-9449:1393 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Undo Start Work Package page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_UNDO_START';


--changeSet OPER-9449:1394 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the View Opportunistic Tasks page that''s required to see the tasks that can be performed conveniently at the same time as the tasks in the current work package, but that are not mandatory.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_VIEW_OPPORTUNISTIC_TASKS';


--changeSet OPER-9449:1395 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Extend Deadline page for Blocks.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EXTENDDEADLINE_EXTEND_BLOCK_DEADLINE';


--changeSet OPER-9449:1396 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Extend Deadline page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_EXTENDDEADLINE_EXTEND_DEADLINE';


--changeSet OPER-9449:1397 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Incorporated Tasks Search page that''s required to add a task to an event.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_INCORPORATEDTASK_INCORPORATED_TASK_SEARCH';


--changeSet OPER-9449:1398 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Note page that''s required to add or edit a note for a cost line items in a work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_INVOICING_CREATE_EDIT_ESTIMATE_COST_LINE_ITEM_NOTE';


--changeSet OPER-9449:1399 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Cost Line Item page that''s required to add cost to a work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_INVOICING_CREATE_ESTIMATE_COST_LINE_ITEM';


--changeSet OPER-9449:1400 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Expected vs Actual Cost for a cost line item added to a work package.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_INVOICING_EDIT_ESTIMATE_COST_LINE_ITEMS';


--changeSet OPER-9449:1401 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Capture and the Edit Work Capture pages.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_LABOUR_EDIT_WORK_CAPTURE';


--changeSet OPER-9449:1402 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Review Work Captured page.' WHERE parm_name = 'ACTION_JSP_WEB_TASK_LABOUR_WORK_CAPTURE';


--changeSet OPER-9449:1403 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Activate Block, Activate Requirement, Activate Job Card, and Activate Master Panel Card pages that are each required to make their respective type of task definition active, therefore usable for creating maintenance tasks.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ACTIVATE_TASK_DEFINITION';


--changeSet OPER-9449:1404 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Attachment page that''s required to attach documents to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_ATTACHMENT';


--changeSet OPER-9449:1405 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Measurement Specific Scheduling Rule and the Edit Measurement Specific Scheduling Rule pages. These pages are required to add and edit task definition scheduling rules that apply only when a specified measurement occurs.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_EDIT_MEASURE_SPECIFIC_SCHEDULING_RULES';


--changeSet OPER-9449:1406 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Following Task page that''s required to identify task dependencies between task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_FOLLOWING_TASK';


--changeSet OPER-9449:1407 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Job Card to Requirement page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_JIC_TO_REQ';


--changeSet OPER-9449:1408 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Labor Requirement page that is required to add required skills, and hours for each one, to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_LABOUR_REQUIREMENT';


--changeSet OPER-9449:1409 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Linked Task page to link two task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_LINKED_TASK';


--changeSet OPER-9449:1410 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Panel page that''s required to specify the aircraft panels to open to execute the work in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_PANEL';


--changeSet OPER-9449:1411 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Part Requirement page that''s required to add required parts to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_PART_REQUIREMENT';


--changeSet OPER-9449:1412 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Fault Definition page that''s required to associated fault definitions with a job card definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_POSSIBLE_FAULTS';


--changeSet OPER-9449:1413 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Requirement to Block page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_REQ_TO_BLOCK';


--changeSet OPER-9449:1414 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Scheduling Rule page that''s required to specify the scheduling information for task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_SCHEDULING_RULE';


--changeSet OPER-9449:1415 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Step page that is required to add job steps to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_STEP';


--changeSet OPER-9449:1416 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Technical Reference page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_TECHNICAL_REFERENCE';


--changeSet OPER-9449:1417 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Tool Requirement page that''s required to add the tools needed to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_TOOL_REQUIREMENT';


--changeSet OPER-9449:1418 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Zone page that''s required to specify the aircraft zone that is relevant to a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ADD_ZONE';


--changeSet OPER-9449:1419 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Block Details page for a block task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_BLOCK_DETAILS';


--changeSet OPER-9449:1420 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Block Search page that provides restricted search criteria for finding block task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_BLOCK_SEARCH_BY_TYPE';


--changeSet OPER-9449:1421 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Planning Type and Edit Planning Type pages.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_PLANNING_TYPE';


--changeSet OPER-9449:1422 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Reference Document and Edit Reference Document pages.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_REF_DOC';


--changeSet OPER-9449:1423 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Requirement and Edit Requirement pages that are required to work on the content of requirement task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_CREATE_EDIT_REQ';


--changeSet OPER-9449:1424 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Impact Report page for deadline extensions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_DEADLINE_EXTENSIONS';


--changeSet OPER-9449:1425 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Disposition Reference Document page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_DISPOSITION_REF_DOCUMENT';


--changeSet OPER-9449:1426 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Duplicate Requirement and Duplicate Job Card pages that are required to create copies of requirement or job card definitions, to use as the basis to create new task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_DUPLICATE_TASK_DEFINITION';


--changeSet OPER-9449:1427 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Labor Requirements page that''s required to edit the details of labor requirements in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_LABOUR_REQUIREMENTS';


--changeSet OPER-9449:1428 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Measurement Order page that''s required to change the order of measurements to record in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_MEASUREMENT_ORDER';


--changeSet OPER-9449:1429 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Part Requirements page that''s required to edit the details of parts required in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_PART_REQUIREMENTS';


--changeSet OPER-9449:1430 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Part Specific Scheduling Rules page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_PART_SPECIFIC_SCHEDULING_RULES';


--changeSet OPER-9449:1431 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Planning Values page that is required to assign planning types to task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_PLANNING_VALUES';


--changeSet OPER-9449:1432 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Scheduling Rules page that''s required to edit the scheduling rules for task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_SCHEDULING_RULES';


--changeSet OPER-9449:1433 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Step Order page that''s required to change the order of job steps in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_STEP_ORDER';


--changeSet OPER-9449:1434 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Steps page that''s required to edit the description of job steps in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_STEPS';


--changeSet OPER-9449:1435 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Subtask Order page that''s required to change the order in which job cards are listed in the parent requirement task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_SUBTASK_ORDER';


--changeSet OPER-9449:1436 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Tail Number Specific Scheduling rules page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_TAIL_NO_SPECIFIC_SCHED_RULES';


--changeSet OPER-9449:1437 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Task Applicability page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_TASK_APPLICABILITY';


--changeSet OPER-9449:1438 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Technical Reference Order page that''s required to change the order in which the technical references are listed on a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1439 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Tool Requirements page that''s required to edit the details of the tool requirements in a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_TOOL_REQUIREMENTS';


--changeSet OPER-9449:1440 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Work Conditions page that''s required to specify the aircraft conditions that are required for a job card definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_EDIT_WORK_CONDITIONS';


--changeSet OPER-9449:1441 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Initialize Requirement and Initialize Block pages that are required to create instances of actual tasks based on the selected requirement or block definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_INITIALIZE_TASK';


--changeSet OPER-9449:1442 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Install Kits Available page that''s required to find kits that apply to a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_INSTALL_KITS_AVAILABLE';


--changeSet OPER-9449:1443 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Issue Temporary Revision page that''s required to issue the new active revision of a requirement definition to an active maintenance program.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_ISSUE_TEMPORARY_REVISION';


--changeSet OPER-9449:1444 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Job Card Details page for a job card task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_JICDETAILS';


--changeSet OPER-9449:1445 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Job Card Search page that provides restricted search criteria for finding job card definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_JOB_CARD_SEARCH_BY_TYPE';


--changeSet OPER-9449:1446 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Move Block, Move Requirement, Move Job Card,  Move Reference Document pages that are required to move the different types of task definitions from one configuration slot to another.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_MOVE_TASK_DEFINITION';


--changeSet OPER-9449:1447 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Master Panel Card Details page for a master panel card task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_MPCDETAILS';


--changeSet OPER-9449:1448 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the MPC Search page that provides restricted search criteria for finding master panel card task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_MPCSEARCH_BY_TYPE';


--changeSet OPER-9449:1449 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Obsolete Block, Obsolete Requirement, Obsolete Job Card, and Obsolete Task Definition pages.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_OBSOLETE_TASK_DEFN';


--changeSet OPER-9449:1450 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Prevent Execution and Allow Execution pages. The first page is required to indicate that a task definition or an actual task should not be executed and why. The second page is required to remove the restriction from the task definition or task.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_PREVENT_ALLOW_REQ';


--changeSet OPER-9449:1451 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reference Document Details page for a reference document task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REF_DOC_DETAILS';


--changeSet OPER-9449:1452 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reference Document Search page that provides restricted search criteria for finding master panel card task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REF_DOC_SEARCH_BY_TYPE';


--changeSet OPER-9449:1453 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Requirement Details page for a requirement task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REQ_DETAILS';


--changeSet OPER-9449:1454 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Requirement Search page that provides restricted search criteria for finding requirement task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REQ_SEARCH_BY_TYPE';


--changeSet OPER-9449:1455 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Task Definition page that''s required when creating or revising task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_SELECT_TASK_DEFINITION';


--changeSet OPER-9449:1456 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Plan By Date page, for task definitions, that''s required to specify a date earlier than the task deadline by which it would be beneficial to complete the task.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_SET_PLAN_BY_DATES';


--changeSet OPER-9449:1457 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Show Differences page that''s required to select the revision against which to compare the current revision of a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_SHOW_HIDE_DIFFERENCES';


--changeSet OPER-9449:1458 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Definition Search page that''s required to find task definitions that match the user''s search criteria.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASK_DEFINITION_SEARCH';


--changeSet OPER-9449:1459 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Extend Deadline page that required to see the impact of extending the deadline of existing actual tasks that are based on a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASK_EXTEND_DEADLINE';


--changeSet OPER-9449:1460 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Summary: Labor page for a task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASK_LABOUR_SUMMARY';


--changeSet OPER-9449:1461 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Hierarchy page that shows the hierarchy of task definitions that applies to the selected task definition.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_COMMON_VIEW_DOCUMENT_CHAIN';


--changeSet OPER-9449:1462 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Advisory page that''s required to add an advisory to requirement task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_ADD_ADVISORY';


--changeSet OPER-9449:1463 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Weight and Balance page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_ADD_WEIGHT_AND_BALANCE';


--changeSet OPER-9449:1464 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Weight and Balance page.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_REQDETAILS_EDIT_WEIGHT_AND_BALANCE';


--changeSet OPER-9449:1465 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Approve page that''s required to approve task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_APPROVE_TASK_DEFN';


--changeSet OPER-9449:1466 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reject page that is required to reject task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_REJECT_TASK_DEFN';


--changeSet OPER-9449:1467 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Request Approval page and the Restart Approval page that are required to start (or restart) the approval workflow for task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_TASKDEFN_TASKDEFNAPPROVAL_REQUEST_RESTART_APPROVAL';


--changeSet OPER-9449:1468 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Button page that''s required to add a button to a user role''s to-do list tab.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_ASSIGN_TO_DO_BUTTON';


--changeSet OPER-9449:1469 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Tab page that''s required when working on to-do lists assigned to user roles.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_ASSIGN_TO_DO_TAB';


--changeSet OPER-9449:1470 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create To Do List and Edit To Do List pages that are required when working on user roles.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_CREATE_EDIT_TO_DO_LIST';


--changeSet OPER-9449:1471 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Button Order page that''s required to change the order in which buttons appear on a to-do list page.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_EDIT_TO_DO_BUTTON_ORDER';


--changeSet OPER-9449:1472 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Tab Order that''s required to change the order in which to-do list tabs appear for a user role.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_EDIT_TO_DO_TAB_ORDER';


--changeSet OPER-9449:1473 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the HR Shift Assignment page that''s required to assign individuals to shifts.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_HR_SHIFT_ASSIGNMENT';


--changeSet OPER-9449:1474 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the To Do List Details page.' WHERE parm_name = 'ACTION_JSP_WEB_TODOLIST_TO_DO_LIST_DETAILS';


--changeSet OPER-9449:1475 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Check Out: Tool page.' WHERE parm_name = 'ACTION_JSP_WEB_TOOL_TOOL_CHECKOUT';


--changeSet OPER-9449:1476 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Put Away Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_COMPLETE_PUT_AWAY';


--changeSet OPER-9449:1477 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Turn In Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_COMPLETE_TURN_IN';


--changeSet OPER-9449:1478 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Put Away Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_CREATE_PUT_AWAY';


--changeSet OPER-9449:1479 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Transfer page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_CREATE_TRANSFER';


--changeSet OPER-9449:1480 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Transfer Details page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_TRANSFER_DETAILS';


--changeSet OPER-9449:1481 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Transfer Search page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_TRANSFER_SEARCH';


--changeSet OPER-9449:1482 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Turn In Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_TRANSFER_TURN_IN';


--changeSet OPER-9449:1483 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Usage Record page.' WHERE parm_name = 'ACTION_JSP_WEB_USAGE_CREATE_USAGE_RECORD';


--changeSet OPER-9449:1484 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Usage Snapshot page.' WHERE parm_name = 'ACTION_JSP_WEB_USAGE_EDIT_USAGE_SNAPSHOT';


--changeSet OPER-9449:1485 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Usage Record Details page.' WHERE parm_name = 'ACTION_JSP_WEB_USAGE_USAGE_RECORD_DETAILS';


--changeSet OPER-9449:1486 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Usage Definition page.' WHERE parm_name = 'ACTION_JSP_WEB_USAGEDEFN_CREATE_USAGE_DEFINITION';


--changeSet OPER-9449:1487 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reorder Parameters page that''s required to change the order of parameters in a usage definition.' WHERE parm_name = 'ACTION_JSP_WEB_USAGEDEFN_REORDER_USAGE_PARMS';


--changeSet OPER-9449:1488 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Usage Definition Details page.' WHERE parm_name = 'ACTION_JSP_WEB_USAGEDEFN_USAGE_DEFINITION_DETAILS';


--changeSet OPER-9449:1489 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Measurement Details page for usage parameters. webapp/web/usagedefn/usageparm/UsageParameterDetails.jsp' WHERE parm_name = 'ACTION_JSP_WEB_USAGEDEFN_USAGEPARM_USAGE_PARAMETER_DETAILS';


--changeSet OPER-9449:1490 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Time Off page that''s required to identify time periods when a user is away from work.' WHERE parm_name = 'ACTION_JSP_WEB_USER_ADD_TIME_OFF';


--changeSet OPER-9449:1491 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Attachment page that''s required to attach documents to a user''s record.' WHERE parm_name = 'ACTION_JSP_WEB_USER_ADD_USER_ATTACHMENT';


--changeSet OPER-9449:1492 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign License page to assign licenses to users.' WHERE parm_name = 'ACTION_JSP_WEB_USER_ASSIGN_LICENSE';


--changeSet OPER-9449:1493 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Organization that''s required to associate an organization with a user.' WHERE parm_name = 'ACTION_JSP_WEB_USER_ASSIGN_ORGANIZATION_TO_USERS';


--changeSet OPER-9449:1494 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Skill page that''s required to assign skills to users.' WHERE parm_name = 'ACTION_JSP_WEB_USER_ASSIGN_SKILL';


--changeSet OPER-9449:1495 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access ther Create User and Edit User pages.' WHERE parm_name = 'ACTION_JSP_WEB_USER_CREATE_EDIT_USER';


--changeSet OPER-9449:1496 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit PO Authorization Levels page that''s required to assign or edit order authorization levels to users.' WHERE parm_name = 'ACTION_JSP_WEB_USER_EDIT_POAUTHORIZATION_LEVELS';


--changeSet OPER-9449:1497 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Role Order page that''s required to change the order in which a user''s roles are listed in their Maintenix menu.' WHERE parm_name = 'ACTION_JSP_WEB_USER_EDIT_ROLE_ORDER';


--changeSet OPER-9449:1498 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Time Off page that''s required to edit the time away from work that''s recorded for a user.' WHERE parm_name = 'ACTION_JSP_WEB_USER_EDIT_TIME_OFF';


--changeSet OPER-9449:1499 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Suspend User License page.' WHERE parm_name = 'ACTION_JSP_WEB_USER_SUSPEND_LICENSE';


--changeSet OPER-9449:1500 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Unsuspend User License page.' WHERE parm_name = 'ACTION_JSP_WEB_USER_UN_SUSPEND_LICENSE';


--changeSet OPER-9449:1501 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Update License and Enter License Info pages that are required to update user licenses.' WHERE parm_name = 'ACTION_JSP_WEB_USER_UPDATE_LICENSE';


--changeSet OPER-9449:1502 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the User Details page.' WHERE parm_name = 'ACTION_JSP_WEB_USER_USER_DETAILS';


--changeSet OPER-9449:1503 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the User Search page.' WHERE parm_name = 'ACTION_JSP_WEB_USER_USER_SEARCH';


--changeSet OPER-9449:1504 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Account page that''s required to add an account number to a vendor record.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_ADD_ACCOUNT';


--changeSet OPER-9449:1505 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Approval Status page for vendors.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_CHANGE_ORDER_TYPE_STATUS';


--changeSet OPER-9449:1506 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Vendor and Edit Vendor pages.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_CREATE_EDIT_VENDOR';


--changeSet OPER-9449:1507 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Details page.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_VENDOR_DETAILS';


--changeSet OPER-9449:1508 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Part Price Details page.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_VENDOR_PART_PRICE_DETAILS';


--changeSet OPER-9449:1509 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Search page.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_VENDOR_SEARCH';


--changeSet OPER-9449:1510 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Vendor Search page that provides restricted search criteria for finding vendors.' WHERE parm_name = 'ACTION_JSP_WEB_VENDOR_VENDOR_SEARCH_BY_TYPE';


--changeSet OPER-9449:1511 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Assembly Warranty Contract and Edit Assembly Warranty Contract pages.' WHERE parm_name = 'ACTION_JSP_WEB_WARRANTY_CREATE_EDIT_WARRANTY_CONTRACT';


--changeSet OPER-9449:1512 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Warranty Contract Details page.' WHERE parm_name = 'ACTION_JSP_WEB_WARRANTY_WARRANTY_CONTRACT_DETAILS';


--changeSet OPER-9449:1513 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Warranty Contract Search page.' WHERE parm_name = 'ACTION_JSP_WEB_WARRANTY_WARRANTY_CONTRACT_SEARCH';


--changeSet OPER-9449:1514 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Approval Level Details page.' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_APPROVAL_LEVEL_DETAILS';


--changeSet OPER-9449:1515 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Approval Workflow Details page' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_APP_WORKFLOW_DEFN_DETAILS';


--changeSet OPER-9449:1516 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Approval Level and Edit Approval Level pages that are required to create and edit levels of approval that are required in task definition approval workflows.' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_CREATE_EDIT_APPROVAL_LEVEL';


--changeSet OPER-9449:1517 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Approval Workflow and Edit Approval Workflow pages that are required to create and edit approval workflows for task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_CREATE_EDIT_APP_WORKFLOW_DEFN';


--changeSet OPER-9449:1518 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Approval Level Order page.' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_EDIT_APPROVAL_LEVEL_ORDER';


--changeSet OPER-9449:1519 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Approval Level page that''s required to assign approval levels to  approval workflows for task definitions.' WHERE parm_name = 'ACTION_JSP_WEB_WORKFLOW_WORKFLOWDEFNDETAILS_ASSIGN_APPROVAL_LEVEL';


--changeSet OPER-9449:1520 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Login Alert page.' WHERE parm_name = 'ACTION_JSP_COMMON_ALERT_CREATE_LOGIN_ALERT';


--changeSet OPER-9449:1521 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Login Alerts page that''s required to see current login alerts.' WHERE parm_name = 'ACTION_JSP_COMMON_ALERT_LOGIN_ALERT';


--changeSet OPER-9449:1522 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to manage Login Alerts via the Create Login Alert page.' WHERE parm_name = 'ACTION_JSP_COMMON_ALERT_PICKED_ITEMS';


--changeSet OPER-9449:1523 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the User Alerts page.' WHERE parm_name = 'ACTION_JSP_COMMON_ALERT_USER_ALERTS';


--changeSet OPER-9449:1524 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Business Process Viewer page.' WHERE parm_name = 'ACTION_JSP_COMMON_INTEGRATION_BUSINESS_PROCESS_VIEWER';


--changeSet OPER-9449:1525 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Message Search page that''s required to search for integration messages.' WHERE parm_name = 'ACTION_JSP_COMMON_INTEGRATION_LOG_SEARCH';


--changeSet OPER-9449:1526 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Message Details page that''s required to see the content of integration messages.' WHERE parm_name = 'ACTION_JSP_COMMON_INTEGRATION_MESSAGE_DETAILS';


--changeSet OPER-9449:1527 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Send Message page that''s required to edit and resend integration messages.' WHERE parm_name = 'ACTION_JSP_COMMON_INTEGRATION_SEND_MESSAGE';


--changeSet OPER-9449:1528 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Integratoin Message page.' WHERE parm_name = 'ACTION_JSP_COMMON_INTEGRATION_VIEW_RESPONSE';


--changeSet OPER-9449:1529 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Enable / Disable Work Item Type page.' WHERE parm_name = 'ACTION_JSP_COMMON_WORK_ENABLE_DISABLE_WORK_ITEM_TYPE';


--changeSet OPER-9449:1530 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Set Schedule Date page.' WHERE parm_name = 'ACTION_JSP_COMMON_WORK_SET_SCHEDULE_DATE';


--changeSet OPER-9449:1531 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Item Administration Console page that''s required to find work items.' WHERE parm_name = 'ACTION_JSP_COMMON_WORK_WORK_ITEM_ADMIN_CONSOLE';


--changeSet OPER-9449:1532 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Corrective Actions page that''s required to select corrective actions to initiate as subtasks to a fault.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_CORRECTIVE_ACTIONS';


--changeSet OPER-9449:1533 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Fault Description page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_EDIT_FAULT_DESCRIPTION';


--changeSet OPER-9449:1534 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Fault Definition Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_FAULT_DEFINITION_SEARCH';


--changeSet OPER-9449:1535 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Find Recurring Fault page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_FAULT_PAGE';


--changeSet OPER-9449:1536 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Fault Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_FAULT_SEARCH';


--changeSet OPER-9449:1537 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Fault Search page that provides restricted search criteria for finding faults.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_FAULT_SEARCH_BY_TYPE';


--changeSet OPER-9449:1538 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Find Recurring Fault: Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_FIND_RECURRING_FAULT';


--changeSet OPER-9449:1539 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Possible Fault Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_POSSIBLE_FAULT_DETAILS';


--changeSet OPER-9449:1540 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Deferral Reference and Edit Deferral Reference  pages.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_DEFERRAL_CREATE_EDIT_FAULT_DEFER_REF';


--changeSet OPER-9449:1541 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Fault Threshold and Edit Fault Threshold pages.' WHERE parm_name = 'ACTION_JSP_WEB_FAULT_THRESHOLD_CREATE_EDIT_FAULT_THRESHOLD';


--changeSet OPER-9449:1542 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Attachment page that''s required to attach a file or link to a fault definition.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_ADD_ATTACHMENT';


--changeSet OPER-9449:1543 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Technical Reference page that''s required to add a technical reference to a fault definition.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_ADD_TECHNICAL_REFERENCE';


--changeSet OPER-9449:1544 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Failure Effect page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_FAILURE_EFFECT';


--changeSet OPER-9449:1545 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Fault Suppression page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_FAULT_SUPPRESSION';


--changeSet OPER-9449:1546 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Task Definition page that''s required to add an initiating task definition to a fault definition.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_ASSIGN_TASK_DEFINITION';


--changeSet OPER-9449:1547 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Fault Definition and Edit Fault Definition pages.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_CREATE_EDIT_FAULT_DEFINITION';


--changeSet OPER-9449:1548 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Failure Effect Order page that''s required to change the order of the failure effects that are assigned to a fault definition.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_EDIT_FAILURE_EFFECT_ORDER';


--changeSet OPER-9449:1549 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Technical Reference Order page that''s required to change the order of technical references that are assigned to fault definitions.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_EDIT_TECHNICAL_REFERENCE_ORDER';


--changeSet OPER-9449:1550 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Fault Definition Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_FAULT_DEFINITION_DETAILS';


--changeSet OPER-9449:1551 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Fault Definition Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FAULTDEFN_FAULT_DEFINITION_SEARCH';


--changeSet OPER-9449:1552 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Note page that''s required to add notes to a flight''s history.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_ADD_NOTES';


--changeSet OPER-9449:1553 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Complete Flight page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_COMPLETE_FLIGHT';


--changeSet OPER-9449:1554 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Flight and Edit Flight pages.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_EDIT_FLIGHT';


--changeSet OPER-9449:1555 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Flight Measurements page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_EDIT_FLIGHT_MEASUREMENTS';


--changeSet OPER-9449:1556 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Flight Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_FLIGHT_DETAILS';


--changeSet OPER-9449:1557 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Flight Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHT_FLIGHT_SEARCH';


--changeSet OPER-9449:1558 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Flight Disruption Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHTDISRUPTION_FLIGHT_DISRUPTION_DETAILS';


--changeSet OPER-9449:1559 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Flight Disruption Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FLIGHTDISRUPTION_FLIGHT_DISRUPTION_SEARCH_BY_TYPE';


--changeSet OPER-9449:1560 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Account Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FNC_ACCOUNT_SEARCH';


--changeSet OPER-9449:1561 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the T-Code Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FNC_TCODE_SEARCH';


--changeSet OPER-9449:1562 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Transaction Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FNC_TRANSACTION_SEARCH';


--changeSet OPER-9449:1563 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Assign Forecast model that''s required to associate a forecast model with aircraft.' WHERE parm_name = 'ACTION_JSP_WEB_FORECAST_ASSIGN_FORECAST_MODEL';


--changeSet OPER-9449:1564 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Forecast Model and Edit Forecast Model pages.' WHERE parm_name = 'ACTION_JSP_WEB_FORECAST_CREATE_EDIT_FORECAST_MODEL';


--changeSet OPER-9449:1565 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Forecast Model Search page.' WHERE parm_name = 'ACTION_JSP_WEB_FORECAST_FC_MODEL_SEARCH_BY_TYPE';


--changeSet OPER-9449:1566 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Forecast Model Details page.' WHERE parm_name = 'ACTION_JSP_WEB_FORECAST_FORECAST_MODEL_DETAILS';


--changeSet OPER-9449:1567 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Technical Reference, Duplicate Technical Reference, and Add Technical Reference pages.' WHERE parm_name = 'ACTION_JSP_WEB_IETM_ADD_TECHNICAL_REFERENCE';


--changeSet OPER-9449:1568 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create IETM and Edit IETM pages.' WHERE parm_name = 'ACTION_JSP_WEB_IETM_CREATE_EDIT_IETM';


--changeSet OPER-9449:1569 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the following pages that are required to define IETM topics: Add Attachment, Edit IETM Attachment, Add Technical Reference, Edit IETM Technical Reference, and Duplicate Technical Reference .' WHERE parm_name = 'ACTION_JSP_WEB_IETM_CREATE_EDIT_IETM_TOPIC';


--changeSet OPER-9449:1570 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the IETM Details page.' WHERE parm_name = 'ACTION_JSP_WEB_IETM_IETM_DETAILS';


--changeSet OPER-9449:1571 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the IETM Search page.' WHERE parm_name = 'ACTION_JSP_WEB_IETM_IETM_SEARCH';


--changeSet OPER-9449:1572 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the IETM Search page that provides restricted search criteria for finding IETMs.' WHERE parm_name = 'ACTION_JSP_WEB_IETM_IETM_SEARCH_BY_TYPE';


--changeSet OPER-9449:1573 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Attachment Details and Technical Reference Details pages that are required to see the details of IETM topics' WHERE parm_name = 'ACTION_JSP_WEB_IETM_IETM_TOPIC_DETAILS';


--changeSet OPER-9449:1574 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Add Impacts and Edit Impacts pages.' WHERE parm_name = 'ACTION_JSP_WEB_IMPACTS_ADD_EDIT_IMPACTS';


--changeSet OPER-9449:1575 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Fault page.' WHERE parm_name = 'ACTION_JSP_WEB_INCLUDE_FAULT_FAULT_INFO';


--changeSet OPER-9449:1576 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Issue Inventory to New Fault page.' WHERE parm_name = 'ACTION_JSP_WEB_INCLUDE_INVENTORY_ISSUEINVENTORY_ISSUE_INVENTORY_TO_FAULT';


--changeSet OPER-9449:1577 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Issue Inventory to Task page.' WHERE parm_name = 'ACTION_JSP_WEB_INCLUDE_INVENTORY_ISSUEINVENTORY_ISSUE_INVENTORY_TO_TASK';


--changeSet OPER-9449:1578 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Blackout page that''s required to define date ranges during which an aircraft does not accrue any usage.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_ADD_BLACKOUT';


--changeSet OPER-9449:1579 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Adjust Quantity page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_ADJUST_QUANTITY';


--changeSet OPER-9449:1580 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Aircraft Monitoring page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_AIRCRAFT_REPORTS';


--changeSet OPER-9449:1581 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Aircraft page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_AIRCRAFT_SELECTION';


--changeSet OPER-9449:1582 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Change Config Slot page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CHANGE_BOM_ITEM';


--changeSet OPER-9449:1583 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Change Custody page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CHANGE_CUSTODY';


--changeSet OPER-9449:1584 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Change Owner page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CHANGE_OWNER';


--changeSet OPER-9449:1585 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Work Package Monitoring page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CHECK_REPORTS';


--changeSet OPER-9449:1586 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Confirm Release page that''s required to release an asset, that has missing mandatory components, from a work package.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CONFIRM_INCOMPLETE_INVENTORY';


--changeSet OPER-9449:1587 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create Inventory page that''s required to create inventory records.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_CREATE_INVENTORY';


--changeSet OPER-9449:1588 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Detach Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_DETACH_INVENTORY';


--changeSet OPER-9449:1589 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Edit Inventory page and all its sub-pages.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_EDIT_INVENTORY';


--changeSet OPER-9449:1590 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Inventory Details page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_INVENTORY_DETAILS';


--changeSet OPER-9449:1591 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Inventory Search page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_INVENTORY_SEARCH';


--changeSet OPER-9449:1592 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Inventory Search page that provides restricted search criteria for finding inventory items.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_INVENTORY_SEARCH_BY_TYPE';


--changeSet OPER-9449:1593 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Select Component page that''s required to select the aircraft system for which you want to create a task or raise a fault.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_INVENTORY_SELECTION';


--changeSet OPER-9449:1594 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Lock Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_LOCK_INVENTORY';


--changeSet OPER-9449:1595 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Complete Work Package page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_RELEASE_INVENTORY';


--changeSet OPER-9449:1596 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Return To Vendor page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_RETURN_TO_VENDOR';


--changeSet OPER-9449:1597 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Task Details page for removed items.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_SELECT_REMOVED_ITEM';


--changeSet OPER-9449:1598 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Split Bin page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_SPLIT_BIN';


--changeSet OPER-9449:1599 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Unarchive Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_UNARCHIVE_INVENTORY';


--changeSet OPER-9449:1600 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create and Store Inventory page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_BULKCREATEINVENTORY_BULK_CREATE_INVENTORY';


--changeSet OPER-9449:1601 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Create and Store Inventory Summary.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_BULKCREATEINVENTORY_BULK_CREATE_INV_SUMMARY';


--changeSet OPER-9449:1602 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Escalate Oil Consumption Status page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_OILCONSUMPTION_ESCALATE_OIL_CONSUMPTION_STATUS';


--changeSet OPER-9449:1603 stripComments:false 
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'Permission to access the Reliability Note Search page.' WHERE parm_name = 'ACTION_JSP_WEB_INVENTORY_RELIABILITY_RELIABILITY_NOTE_SEARCH';