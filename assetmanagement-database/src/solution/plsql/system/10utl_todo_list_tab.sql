/**********************************************
** 10-Level INSERT SCRIPT FOR UTL_TODO_LIST_TAB
***********************************************/

/***********************
** Maintenance Controller To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10001, 1, 10);

-- Capacity Summary Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10049, 10001, 3, 10);

-- Shift Schedule Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10048, 10001, 4, 10);

-- Fleet Due List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10050, 10001, 5, 10);

-- Forecast Models Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10051, 10001, 6, 10);

/***********************
** Line Technician To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10004, 1, 10);

/***********************
** Production Controller To Do List
************************/
-- Assigned Work List
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10001, 10002, 2, 10);

-- Planning Types Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11021, 10002, 3, 10);

/***********************
** Line Supervisor To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10003, 1, 10);

/***********************
** Shop Controller To Do List
************************/
-- Assigned Work List
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10001, 10005, 1, 10);

/***********************
** Engineer To Do List
************************/
-- System Engineering Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10013, 10006, 1, 10);

-- Assembly List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10035, 10006, 2, 10);

-- Fleet Task Labour Summary Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10057, 10006, 3, 10);

-- Tags Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10061, 10006, 4, 10);

-- Approval Workflows Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11010, 10006, 5, 10);

-- Approval Level Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11011, 10006, 6, 10);

-- My Task Definitions Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11012, 10006, 7, 10);

-- Task Definitions To Approve Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11013, 10006, 8, 10);

/***********************
** Progress Monitor To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10007, 1, 10);

/***********************
** Heavy Technician To Do List
************************/
-- My Tasks Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10003, 10008, 1, 10);

-- Assigned To Your Crew Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10004, 10008, 2, 10);

-- Fleet Due List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10050, 10008, 3, 10);

/***********************
** Technical Records To Do List
************************/
-- Users Alert List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11000, 10011, 1, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11021, 10011, 2, 10);

-- Assembly List tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10035, 10011, 3, 10);

-- Item tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11005, 10011, 5, 10);

/***********************
** Quality Control Inspector To Do List
************************/
-- Inspection Required Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10010, 10010, 1, 10);

-- Quarantine Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10012, 10010, 2, 10);

/***********************
** Repair Router To Do List
************************/
-- Unserviceable Staging To Do Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10005, 10014, 1, 10);

-- Out For Repair Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10033, 10014, 2, 10);

-- Condemned Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10021, 10014, 3, 10);

-- Storage Maintenance To Do Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10006, 10014, 4, 10);

/***********************
** Material Controller To Do List
************************/
-- Open Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10030, 10015, 1, 10);

-- Stock Lows Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10020, 10015, 2, 10);

-- Pending Expiry Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10017, 10015, 3, 10);

-- Condemned Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10021, 10015, 4, 10);

-- My Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10027, 10015, 5, 10)

-- Open MEL Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10037, 10015, 6, 10);

-- Cycle Count Discrepancies Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10038, 10015, 7, 10);



-- To Be Returned Consignment Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10056, 10015, 9, 10);

-- Incomplete Kits
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11018, 10015, 10, 10);

--  Overcomplete Kits
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11019, 10015, 11, 10);

--  Stock Distribution Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11026, 10015, 12, 10);

/***********************
** Storeroom Clerk To Do List
************************/
-- Issue Inventory Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10031, 10017, 1, 10);

-- To Be Shelved Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10016, 10017, 2, 10);

-- Cycle Count Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10042, 10017, 3, 10);  

-- Inbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10018, 10017, 4, 10);

-- Outbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10019, 10017, 5, 10);

-- Turn Ins Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10009, 10017, 6, 10);

-- My Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10027, 10017, 7, 10);

-- Stock transfer
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11014, 10017, 8, 10);

-- Incomplete Kits
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11018, 10017, 9, 10);

--  Overcomplete Kits
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11019, 10017, 10, 10);


/***********************
** Purchasing Agent To Do List
************************/
-- Purchase Request Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10022, 10018, 1, 10);

-- Consignment Stock Request Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10055, 10018, 2, 10);

-- Open Purchase Orders Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10023, 10018, 3, 10);

-- Issued Purchase Orders  Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10024, 10018, 4, 10);

-- Awaiting Issue POs Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10026, 10018, 5, 10);

-- Authorization Required POs Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10025, 10018, 6, 10);

-- Quarantine Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10012, 10018, 7, 10);

-- My RFQ Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10052, 10018, 8, 10);



-- To Be Returned Consignment Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10056, 10018, 10, 10);


/***********************
** Purchasing Manager To Do List
************************/
-- Authorization Required POs Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10025, 10019, 1, 10);


/***********************
** Crew Lead To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   values (10000, 10022, 1, 10);

-- Assigned to your Crew Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   values (10004, 10022, 2, 10);

-- Assigned Work List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID,TODO_LIST_ID,TAB_ORDER,UTL_ID)
   values (10001, 10022, 3, 10);

-- Fault Evaluation Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10034, 10022, 4, 10);

-- User Shift Pattern Setup
--insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
--values (11024, 10022, 4, 10);


/***********************
** Material Controller - Inspection To Do List
************************/
-- Inspection Required Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10010, 10023, 1, 10);

-- Quarantine Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10012, 10023, 2, 10);

/***********************
** Material Controller - Repair To Do List
************************/
-- Unserviceable Staging To Do Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10005, 10024, 1, 10);

-- Out For Repair Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10033, 10024, 2, 10);

/***********************
** Material Controller - Storeroom To Do List
************************/
-- Issue Inventory Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10031, 10025, 1, 10);

-- To Be Shelved Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10016, 10025, 2, 10);

-- Inbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10018, 10025, 3, 10);

-- Outbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10019, 10025, 4, 10);

-- Turn Ins Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10009, 10025, 5, 10);


/***********************
** Line Planner To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10026, 1, 10);

-- Forecast Models Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10051, 10026, 2, 10);


/***********************
** Heavy Planner To Do List
************************/
-- Fleet List Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10000, 10027, 1, 10);

-- My Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10027, 10027, 2, 10);

-- Forecast Models Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10051, 10027, 3, 10);

/***********************
** Storeroom Supervisor To Do List
************************/
-- Issue Inventory Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10031, 10028, 1, 10);

-- To Be Shelved Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10016, 10028, 2, 10);

-- Inbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10018, 10028, 3, 10);

-- Outbound Shipments Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10019, 10028, 4, 10);

-- Turn Ins Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10009, 10028, 5, 10);

-- My Part Requests Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10027, 10028, 6, 10);


/***********************
** Material Controller - Overstock To Do List
************************/
-- Obsolete Inventory Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10043, 10029, 1, 10);

-- Slow Moving Stock Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10044, 10029, 2, 10);

-- Surplus Stock Tab
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10045, 10029, 3, 10);


/***********************
** Administrative To Do List
************************/
-- Assembly List
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10035, 10016, 1, 10);

/***********************
** Fault Threshold To Do List
************************/
-- Fault Threshold List
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10064, 10003, 4, 10);

/***********************
** Deferral Reference To Do List
************************/
-- Deferral Reference List
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10065, 10003, 5, 10);


/***********************
** Borrow Retruns To Do List
************************/
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11003, 10018, 8, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11003, 10015,11, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11003, 10019, 2, 10);


/***********************
** Exchange Retruns To Do List
************************/
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11004, 10018, 9, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11004, 10019, 3, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11004, 10015, 12, 10);


/***********************
** Warranty Agent To Do List
************************/
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11006, 10034, 1, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10033, 10034, 2, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11007, 10034, 3, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11008, 10034, 4, 10);

insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11009, 10034, 5, 10);

/***********************
** Long Range Planner  To Do List
************************/
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (11015, 10033, 1, 10);
insert into UTL_TODO_LIST_TAB (TODO_TAB_ID, TODO_LIST_ID, TAB_ORDER, UTL_ID)
values (10051, 10033, 2, 10);