/*************************************************
** 10-LEVEL INSERT SCRIPT FOR TABLE UTL_ALERT_TYPE_ROLE
**************************************************/

/***********************
** Common Alerts
************************/
-- JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 2, 10014, 10 );


/***********************
** Fault Alerts
************************/
-- POSSIBLE_FAULT_ARRIVAL / LINEMTC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 5, 10011, 10 );

-- POSSIBLE_FAULT_AUTHORITY / FLTCTRL
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 6, 10000, 10 );


/***********************
** Flight Alerts
************************/


/***********************
** Inventory Alerts
************************/
-- AUTO_RESERVATION_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 8, 10014, 10 );

-- EXPIRED_INVENTORY_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 9, 10014, 10 );

-- INVENTORY_COUNTED_CONDITION / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 10, 10014, 10 );

-- INVENTORY_INSPECTED_AS_SERVICEABLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 11, 10014, 10 );

-- INVENTORY_NOT_APPROVED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 69, 10014, 10 );

-- INVENTORY_NOT_ARCHIVED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 12, 10014, 10 );

-- INVENTORY_OOS_INSTALL / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 13, 10014, 10 );

-- INVENTORY_OOS_REMOVAL / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 14, 10014, 10 );

-- INVENTORY_REPLICA_CREATION / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 15, 10014, 10 );

-- INVENTORY_ROLE_TRANSFORMATION_CHANGE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 16, 10014, 10 );

-- INVENTORY_ROLE_TRANSFORMATION_SET / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 17, 10014, 10 );

-- REPLACEMENT_INVENTORY_CREATED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 71, 10014, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 76, 10014, 10 );

-- INVENTORY_NOT_INSPECTED_AS_SERVICEABLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 152, 10014, 10 );

-- INVENTORY_CREATED_IN_QUARANTINE 
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 244, 10009, 10 );

-- INVENTORY_INSPECTED_AS_UNSERVICEABLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 175, 10014, 10 );


/***********************
** Part Alerts
************************/
-- ACTIVATE_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 20, 10014, 10 );

-- ASSIGN_ALTERNATE_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 21, 10014, 10 );

-- ASSIGN_ALTERNATE_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 105, 10014, 10 );

-- CREATE_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 22, 10014, 10 );

-- PART_NOT_APPROVED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 70, 10014, 10 );


/***********************
** Purchase Order Alerts
************************/
-- CONVERT_BORROW_TO_PURCHASE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 24, 10014, 10 );

-- PURCHASE_ORDER_EXCEPTION / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 25, 10019, 10 );

-- PURCHASE_ORDER_AUTO_ISSUED / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 72, 10019, 10 );

-- PO_AUTO_GEN_INVALID_VENDOR / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 78, 10019, 10 );

-- ORDER_BELOW_MINIMUM_VENDOR_PART_QUANTITY / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 240, 10019, 10 );


/***********************
** Part Request Alerts
************************/
-- GENERATE_PART_REQUESTS_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 26, 10014, 10 );

-- PART_REQUEST_INVENTORY_ASSIGNMENT / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 27, 10014, 10 );

-- PART_REQUEST_NON_ARCHIVE_INVENTORY_ASSIGNMENT / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 68, 10014, 10 );

/***********************
** RFQ Alerts
************************/
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 93, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 94, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 95, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 96, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 97, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 98, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 100, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 101, 19000, 10 );

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 104, 19000, 10 );

/***********************
** Shipment Alerts
************************/
-- NO_MATCH_FOR_SHIPMENT_ADVISORY / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 28, 10019, 10 );

-- RECEIVED_INSTALLED_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 29, 10014, 10 );

-- RECEIVED_INSTALLED_PART_UNDEFINED_REPL / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 30, 10014, 10 );

-- RECEIVED_LOOSE_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 31, 10014, 10 );

-- RECEIVED_SCRAPPED_PART / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 32, 10014, 10 );

-- SHIPMENT_ADVISORY / PURCHMAN
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 33, 10019, 10 );



/***********************
** Task Alerts
************************/
-- DELETE_ORPHANED_FORECASTED_TASKS_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 34, 10014, 10 );

-- GENERATE_FORECASTED_TASKS_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 35, 10014, 10 );

-- TASK_TRANSFORMATION_FOUND / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 38, 10014, 10 );

-- TASK_TRANSFORMATION_NOT_FOUND / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 39, 10014, 10 );

-- TASK_TRANSFORMATION_SCHED_RULE_MISMATCH / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 40, 10014, 10 );

-- INSTALL_BATCH_NOT_ISSUED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 44, 10014, 10 );

-- INSTALL_PART_ATTACHED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 45, 10014, 10 );

-- INSTALL_PART_ATTACHED_WITH_UNDEFINED_REPLACEMENT / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 75, 10014, 10 );

-- INSTALL_PART_NOT_APPLICABLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 46, 10014, 10 );

-- INSTALL_PART_NOT_APPROVED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 47, 10014, 10 );

-- INSTALL_PART_NOT_APPROVED_IN_GROUP / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 103, 10014, 10 );

-- INSTALL_PART_NOT_FOUND / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 48, 10014, 10 );

-- INSTALL_PART_NOT_ISSUED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 49, 10014, 10 );

-- INSTALL_PART_NOT_PART_COMPATIBLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 50, 10014, 10 );

-- INSTALL_PART_NOT_RFI / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 51, 10014, 10 );

-- INSTALL_PART_NOT_TASK_COMPATIBLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 52, 10014, 10 );

-- REMOVE_PART_DIFF_POS / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 53, 10014, 10 );

-- REMOVE_PART_HOLE_EXISTS / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 54, 10014, 10 );

-- REMOVE_PART_HOLE_EXISTS_UNDEFINED_REPL / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 55, 10014, 10 );

-- REMOVE_PART_LOOSE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 56, 10014, 10 );

-- REMOVE_PART_MISMATCH / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 57, 10014, 10 );

-- REMOVE_PART_MISMATCH_UNDEFINED_REPL / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 58, 10014, 10 );

-- REMOVE_PART_NOT_FOUND / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 59, 10014, 10 );

-- REMOVE_PART_NOT_FOUND_HOLE_EXISTS / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 60, 10014, 10 );

-- REPLACE_PART_NOT_INTERCHANGEABLE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 61, 10014, 10 );

-- REPLACE_PART_QTY_MISMATCH / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 62, 10014, 10 );

-- UNABLE_TO_SCHED_FROM_BIRT / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 112, 10014, 10 );

/***********************
** Transfer Alerts
************************/
-- GENERATE_ISSUE_TRANSFERS_JOB_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 63, 10014, 10 );


/***********************
** Baseline Synchronization Alerts
************************/
-- BASELINE_SYNCH_FAILED / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 108, 10014, 10 );

-- BASELINE_SYNCH_COMPLETE / TECHREC
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 109, 10014, 10 );

/**********************************
** Warranty Evaluation Alerts *****
***********************************/
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 124, 10014, 10 );

/**********************************
** Oracle Job Alerts *****
***********************************/

INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 210, 10031, 10 );


/**********************************
** Task Defn *****
***********************************/
-- TASK_DEFN_ACTV_FOR_INV_WITH_MISSING alert type
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 214, 10009, 10 );


/**********************************
** Electronic Logbook adapter ****
***********************************/
INSERT INTO utl_alert_type_role( alert_type_id, role_id, utl_id )
VALUES ( 170, 10014, 10 );

INSERT INTO utl_alert_type_role( alert_type_id, role_id, utl_id )
VALUES ( 172, 10014, 10 );

INSERT INTO utl_alert_type_role( alert_type_id, role_id, utl_id )
VALUES ( 173, 10014, 10 );

INSERT INTO utl_alert_type_role( alert_type_id, role_id, utl_id )
VALUES ( 174, 10014, 10 );

/*********************************
**Adhoc Alert*****
*********************************/
INSERT INTO utl_alert_type_role ( alert_type_id, role_id, utl_id )
VALUES ( 241, 19000, 10 );