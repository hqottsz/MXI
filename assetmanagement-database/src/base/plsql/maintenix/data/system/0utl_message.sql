--liquibase formatted sql


--changeSet 0utl_message:1 stripComments:false
-- INSERT SCRIPT FOR TABLE UTL_MESSAGE
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10001, 'ArgumentTooLongException', 'ARGLONG', 'The argument is too long',0);

--changeSet 0utl_message:2 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10002, 'MissingEndDateException', 'MENDDATE', 'The end date is missing',0);

--changeSet 0utl_message:3 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10003, 'MissingStartDateException', 'MSTRDATE', 'The start date is missing',0);

--changeSet 0utl_message:4 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10004, 'InvalidDateException', 'INVDATE', 'The date is invalid',0);

--changeSet 0utl_message:5 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10005, 'InvalidRefTermException', 'INVREFT', 'The refterm is invalid',0);

--changeSet 0utl_message:6 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10006, 'InvalidStringException', 'INVSTR', 'The string is invalid',0);

--changeSet 0utl_message:7 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10007, 'InvalidTaskException', 'INVTASK', 'The task is invalid',0);

--changeSet 0utl_message:8 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10008, 'InvalidUsernameAndPasswordException', 'INVUP', 'Invalid username and password',0);

--changeSet 0utl_message:9 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10009, 'StartDateAfterEndDateException', 'STAFTEND', 'The start date is after the end date',0);

--changeSet 0utl_message:10 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TITLE, MSG_TYPE_CD, MSG_LDESC, UTL_ID)
VALUES (10010, 'StringMissingException', 'STRMISS', 'The string is missing',0);

--changeSet 0utl_message:11 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10011, 'LOGIC', 'LooseTaskException', 'This task %1 is not assigned to check or work order.', 0);

--changeSet 0utl_message:12 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10012, 'LOGIC', 'NotScheduledTaskException', 'This operation can not be executed on task <font color=''RED''>%1 </font> since the task was not scheduled.', 0);

--changeSet 0utl_message:13 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10013, 'LOGIC', 'InvalidHrSignatureRequiredException', 'You have attempted to complete task %2.  Task %2 requires a sign off signature.  <br><br> The task must have a signature before it can be signed off.', 0);

--changeSet 0utl_message:14 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10014, 'LOGIC', 'NonRootTaskOperationException', 'The %1 operation can not be executed on task %2 of class CHECK or RO.', 0);

--changeSet 0utl_message:15 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10015, 'LOGIC', 'DifferentLocationException', 'The %1 inventory is at %2 location, it is different than expected %3 location, happened during %4.', 0);

--changeSet 0utl_message:16 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10016, 'LOGIC', 'DuplicateSerialNumberException', 'Inventory %1 serial number can not be set, the %2 serial number already exists in the database and is associated with inventory %3.', 0);

--changeSet 0utl_message:17 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10017, 'LOGIC', 'IncompatiblePartNoException', 'You have attempted to install %1 part. <br><br> %1 part is incompatible with inventory %2, which is already installed. <br><br> Items which are incompatible with currently installed inventory cannot be installed.', 0);

--changeSet 0utl_message:18 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10018, 'LOGIC', 'InvalidBomItemStructureException', 'The %1 inventory has a BOM item structure that is not compatible with the target %2 BOM item structure.', 0);

--changeSet 0utl_message:19 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10019, 'LOGIC', 'InvalidPartNoStructureException', 'The %1 inventory structure''s part numbers are not compatible with the target %2 BOM item structure.', 0);

--changeSet 0utl_message:20 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10020, 'LOGIC', 'LooseInventoryException', 'The %1 inventory is %2.', 0);

--changeSet 0utl_message:21 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10021, 'LOGIC', 'InvalidModificationPartNoException', 'Inventory''s %1 part no is not listed for the given %2 Task Definition.', 0);

--changeSet 0utl_message:22 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10022, 'LOGIC', 'MustCompleteWhenUninstalledException', 'You have attempted to complete %1.  This task is marked as ''Complete When Uninstalled'', meaning that in order to complete the task, the %3 must be removed. <br><br> You have not removed %3.', 0);

--changeSet 0utl_message:23 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10023, 'LOGIC', 'UnserviceableInventoryException', 'You have attempted to install inventory item %1. <br><br> Inventory item %1 is currently unserviceable. <br><br> Unserviceable items cannot be installed.', 0);

--changeSet 0utl_message:24 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10024, 'LOGIC', 'InvalidPartStatusForInstallException', 'The part %1 is not ACTV', 0);

--changeSet 0utl_message:25 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10025, 'LOGIC', 'LockedInventoryException', 'The %1 inventory is locked, operation could not be performed.', 0);

--changeSet 0utl_message:26 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10026, 'LOGIC', 'InvalidOperationForTaskStatusException', 'This operation %1 can not be executed on task %2 with %3 status.', 0);

--changeSet 0utl_message:27 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10027, 'LOGIC', 'DuplicateRoleCodeException', 'The ''%1'' you have entered is already assigned to an existing role.<br><br>Please enter a different ''%1''.', 0);

--changeSet 0utl_message:28 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10028, 'LOGIC', 'DuplicateRoleAssignmentException', 'The %1 (%2) role is already assigned to user %3.', 0);

--changeSet 0utl_message:29 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10029, 'LOGIC', 'NoSuchRoleAssignmentException', 'The %1 (%2) role is currently not assigned to user %3.', 0);

--changeSet 0utl_message:30 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10030, 'LOGIC', 'UsersAssignedToRoleException', 'The %1 (%2) role is currently assigned to at least one user.', 0);

--changeSet 0utl_message:31 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10031, 'LOGIC', 'DuplicateButtonAssignmentException', 'The %1 button is already assigned to %2 todo list.', 0);

--changeSet 0utl_message:32 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10032, 'LOGIC', 'DuplicateTabAssignmentException', 'The %1 tab is already assigned to %2 todo list.', 0);

--changeSet 0utl_message:33 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10033, 'LOGIC', 'NoSuchButtonAssignmentException', 'The %1 button is currently not assigned to the %2 todo list.', 0);

--changeSet 0utl_message:34 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10034, 'LOGIC', 'NoSuchTabAssignmentException', 'The %1 tab is currently not assigned to the %2 todo list.', 0);

--changeSet 0utl_message:35 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10035, 'LOGIC', 'DuplicateStockCodeException', 'The stock code %1 already exists in the system.', 0);

--changeSet 0utl_message:36 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10036, 'LOGIC', 'OneOrMoreRequiredException', 'No %1 were specified. Please select at least one %2.', 0);

--changeSet 0utl_message:37 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10037, 'LOGIC', 'BreakExistsException', 'The %1 menu item already has a break after it.<br><br>You can only add breaks to menu items that don''t already have break.', 0);

--changeSet 0utl_message:38 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10038, 'LOGIC', 'BreakDoesntExistException', 'The %1 menu item does not have a break after it.<br><br>You can only remove breaks from menu items that have a break.', 0);

--changeSet 0utl_message:39 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10039, 'LOGIC', 'OwnerHasInventoryException', 'The %1 (%2) owner is assigned to an inventory.<br><br>You can only delete owners that are not assigned to an inventory.', 0);

--changeSet 0utl_message:40 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10040, 'LOGIC', 'ManufacturerHasPartNoException', 'The %1 (%2) manufacturer is assigned to a part no.<br><br>You can only delete manufacturers that are not assigned to a part no.', 0);

--changeSet 0utl_message:41 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10041, 'LOGIC', 'BinLevelAlreadyExists', 'The Location chosen: %1 already has a bin level for this Stock: %2', 0 );

--changeSet 0utl_message:42 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10042, 'LOGIC', 'StockLevelAlreadyExistsException', 'The Location chosen: %1 already has a stock level for this Stock: %2', 0 );

--changeSet 0utl_message:43 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10043, 'LOGIC', 'StockAllocationPctNotZeroException', 'The Stock Level (Location : %1) chosen to be removed must have an Allocation % of 0. If it is the only stock level the Allocation % must be 100.', 0 );

--changeSet 0utl_message:44 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10044, 'LOGIC', 'NotSupplyLocationException', 'The Location : %1 chosen is not a supply location.  Please choose a supply location to create a Stock Level at.', 0 );

--changeSet 0utl_message:45 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10045, 'LOGIC', 'MultipleSupplyLocationsException', 'This action cannot be taken since it will result in more than 1 supply location in the resulting location chain.', 0 );

--changeSet 0utl_message:46 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10046, 'LOGIC', 'NotBinBnchStckLocationException', 'The location chosen is not of type BIN or BNCHSTK.', 0 );

--changeSet 0utl_message:47 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10047, 'LOGIC', 'RemovePreferredVendorException', 'The %1 vendor is the preferred vendor for part %2.<br><br>  You can only delete the preferred vendor if it is the only vendor available for this part.',0);

--changeSet 0utl_message:48 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10048, 'LOGIC', 'DuplicateLocationCode', 'The location code %1 already exists in the system.', 0);

--changeSet 0utl_message:49 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10049, 'LOGIC', 'CircularTreeReferenceException', 'The location cannot be assigned, since it will result in a circular reference.', 0);

--changeSet 0utl_message:50 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10050, 'LOGIC', 'DuplicateUsernameException', 'The username ''%1'' is already assigned to an existing user.<br><br>Please enter a different username.', 0);

--changeSet 0utl_message:51 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10051, 'LOGIC', 'DuplicateHrCodeException', 'The ''%1'' you have entered is already assigned to an existing user.<br><br>Please enter a different ''%1''.', 0);

--changeSet 0utl_message:52 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10052, 'LOGIC', 'LocationHasEventException', 'The selected location has events assigned to it.<br><br>Please select a different location.', 0);

--changeSet 0utl_message:53 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10053, 'LOGIC', 'LocationHasInventoryException', 'The selected location has inventory assigned to it.<br><br>Please select a different location.', 0);

--changeSet 0utl_message:54 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10054, 'LOGIC', 'LocationHasSubLocationException', 'The selected location has sub locations assigned to it.<br><br>Please select a different location.', 0);

--changeSet 0utl_message:55 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10055, 'LOGIC', 'DepartmentHasEventException', 'The %1 department is assigned to an event.<br><br>You can only delete departments that are not assigned to an event.', 0);

--changeSet 0utl_message:56 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10056, 'LOGIC', 'DuplicateAuthorityCodeException', 'The ''%1'' code you have entered is already assigned an existing authority.<br><br>Please enter a different code.', 0);

--changeSet 0utl_message:57 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10057, 'LOGIC', 'CannotDeleteUserException', 'The user account for %1 %2 cannot be deleted because they have performed tasks in Maintenix. <br><br> If the user account is no longer required, it is preferable to lock the account.', 0);

--changeSet 0utl_message:58 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10058, 'LOGIC', 'InventoryAlreadyAssignedException', 'The inventory chosen: ''%1'' is already assigned to the authority: ''%2''.<br><br>Please select a different inventory.', 0);

--changeSet 0utl_message:59 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10059, 'LOGIC', 'HrAlreadyAssignedException', 'The HR: ''%1'' is already assigned to the authority: ''%2''.<br><br>Please select a different HR.', 0);

--changeSet 0utl_message:60 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10060, 'LOGIC', 'MissingButtonAssignmentException', 'All buttons assigned to the ''%1'' to do list must be included in a new button ordering. The following assigned buttons are missing: %2.', 0);

--changeSet 0utl_message:61 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10061, 'LOGIC', 'MissingTabAssignmentException', 'All tabs assigned to the ''%1'' to do list must be included in a new tab ordering. The following assigned tabs are missing: %2.', 0);

--changeSet 0utl_message:62 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10062, 'LOGIC', 'LocationAlreadyAssignedException', 'The location %1 is already assigned to the department %2.', 0);

--changeSet 0utl_message:63 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10063, 'LOGIC', 'UserAlreadyAssignedException', 'The user %1 is already assigned to the department %2.', 0);

--changeSet 0utl_message:64 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10064, 'LOGIC', 'DuplicateDepartmentCode', 'The department code %1 already exists in the system.', 0);

--changeSet 0utl_message:65 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10065, 'LOGIC', 'CheckWorkOrderExistsException', 'An active Check or Work Order exists with the vendor %1 that you have selected to delete.<br><br>Please complete all Checks or Work Orders associated with %1 before deleting it.',0);

--changeSet 0utl_message:66 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10066, 'LOGIC', 'InventoryExistsException', 'The vendor %1 that you have selected to delete has inventory.<br><br>Please remove all inventory for this vendor before deleting the vendor.',0);

--changeSet 0utl_message:67 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10067, 'LOGIC', 'ShipmentExistsException', 'There is a shipment with the vendor %1 that you have selected to delete.<br><br>Please remove all shipments for this vendor before deleting the vendor.',0);

--changeSet 0utl_message:68 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10068, 'LOGIC', 'AuthorityAssignedToInventoryException', 'The authority: ''%1'' cannot be removed. Please unassign all inventory from this authority before removing the authority.',0);

--changeSet 0utl_message:69 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10069, 'LOGIC', 'DuplicateDepartmentAssignmentException', 'The ''%1 (%2)'' department is already assigned to ''%3 %4''.',0);

--changeSet 0utl_message:70 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10070, 'LOGIC', 'FailFactorAlreadyAssignedException', 'The failure factor %1 is already assigned to the authority %2.', 0);

--changeSet 0utl_message:71 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10071, 'LOGIC', 'DuplicateAuthorityAssignmentException', 'The ''%1 (%2)'' authority is already assigned to ''%3 %4''.',0);

--changeSet 0utl_message:72 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10072, 'LOGIC', 'DepartmentAlreadyAssignedException', 'The department %1 is already assigned to the location %2.', 0);

--changeSet 0utl_message:73 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10073, 'LOGIC', 'InvalidAuthorityAssignmentException', 'The ''%1 (%2)'' authority is not assigned to ''%3 %4''.',0);

--changeSet 0utl_message:74 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10074, 'LOGIC', 'DuplicateVendorAccountException', 'The ''%1'' vendor already has an account with the account number ''%2''.<br><br>Please select a new account number and try again.', 0);

--changeSet 0utl_message:75 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10075, 'LOGIC', 'DuplicateVendorCodeException', 'The vendor code ''%1'' is already assigned to another vendor.  Please enter a unique vendor code.', 0);

--changeSet 0utl_message:76 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10076, 'LOGIC', 'MaxLevelLowerThanReorderLevelException', 'The Max Level must be greater than the Re-Order Level for Location %1.', 0);

--changeSet 0utl_message:77 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10077, 'LOGIC', 'ApprovalExpiryAfterCertificateExpiryException', 'The approval expiry date specified occurs after the vendor''s certificate expiry date.<br><br>The approval expiry date must be set to a date that occurs before the vendor''s certificate expiry date.', 0);

--changeSet 0utl_message:78 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10078, 'LOGIC', 'LocationNotExistsException', 'The location ''%1'' does not exist.<br><br>Please select a valid local ''%2'' and try again.', 0);

--changeSet 0utl_message:79 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10080, 'LOGIC', 'InvalidTransferQuantityException', 'The ''Quantity'' must be less than or equal to the total quantity available.', 0);

--changeSet 0utl_message:80 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10083, 'LOGIC', 'DuplicateInventoryBarcodeException', 'The Barcode ''%1'' is already assigned to another inventory item.  Please enter a unique Inventory Barcode value.', 0);

--changeSet 0utl_message:81 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10086, 'LOGIC', 'InvalidInventoryBarcodeException', 'The inventory barcode ''%1'' was not found in the system.', 0);

--changeSet 0utl_message:82 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10088, 'LOGIC', 'ScrapLocationNotFoundException', 'There are no SCRAP locations setup in the system.',0);

--changeSet 0utl_message:83 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10089, 'LOGIC', 'ArchiveLocationNotFoundException', 'There are no ARCHIVE locations setup in the system.',0);

--changeSet 0utl_message:84 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10090, 'LOGIC', 'InvalidTransferBarcodeException', 'The Transfer ID %1 was not found in the system.',0);

--changeSet 0utl_message:85 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10091, 'LOGIC', 'InvalidUsernameException', 'Username ''%1'' is not a valid username.',0);

--changeSet 0utl_message:86 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10092, 'LOGIC', 'InventoryLocationServiceabilityMismatchException', 'You are attempting to move an unserviceable item of inventory to a serviceable location. This is not permitted.',0);

--changeSet 0utl_message:87 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10093, 'LOGIC', 'InvalidShipmentQuantityException', 'The shipment quantity for serialized inventory can only be one. Please set the shipment quantity for the ''%1'' inventory to one.',0);

--changeSet 0utl_message:88 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10094, 'LOGIC', 'InvalidShipmentQuantityException', 'The shipment quantity for the ''%1'' inventory (%2) exceeds the quantity currently available (%3). Please set the shipment quantity for the inventory to a number less than or equal to the number currently available.', 0);

--changeSet 0utl_message:89 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10095, 'LOGIC', 'InvalidShipToLocationException', 'The shipment''s ship-to location is not of type DOCK or VENDOR. Please select a new location that is of type DOCK or VENDOR.', 0);

--changeSet 0utl_message:90 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10096, 'LOGIC', 'ReceiptBeforeShipmentException', 'The receive date (%1) is before the shipment date (%2). Please change the receive date so that it occurs after the shipment date.', 0);

--changeSet 0utl_message:91 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10097, 'LOGIC', 'DuplicateTaskBarcodeException', 'The Barcode ''%1'' is already assigned to another task.  Please enter a unique Task Barcode value.', 0);

--changeSet 0utl_message:92 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10098, 'LOGIC', 'DuplicatePendingTransferException', 'A pending transfer already exists for the inventory ''%1''.', 0);

--changeSet 0utl_message:93 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10099, 'LOGIC', 'InvalidExpectedQuantityException', 'You have entered an expected quantity of %1.  There are currently %2 item(s) in stock, of which %3 are allocated to pending shipments.<br><br>There are currently %4 item(s) available for shipment.', 0);

--changeSet 0utl_message:94 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (10100, 'LOGIC', 'InvalidStatusException', 'The operation cannot be completed because the %1 is %2.', 0);

--changeSet 0utl_message:95 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10101, 'LOGIC', 'AutoCompleteTaskPartException', 'The task %1 can not be auto completed since it has at least one part requirement.', 0);

--changeSet 0utl_message:96 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10102, 'LOGIC', 'HoleNotEmptyException', 'The parent inventory %1 has an inventory item %2 installed at assembly %3 position %4.', 0);

--changeSet 0utl_message:97 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10103, 'LOGIC', 'InsufficientQuantityException', 'There is not enough items left in the BATCH or QTY controlled item %1, for the desired operation.', 0);

--changeSet 0utl_message:98 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10104, 'LOGIC', 'InventoryBomItemPositionMismatchException', 'The inventory at bom item %1 position %2 could not be attached since no parent inventory was found at bom item %3 position %4 under %5 inventory.', 0);

--changeSet 0utl_message:99 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10105, 'LOGIC', 'InvalidReceivedQuantityException', 'You have entered a negative received quantity for Line No %1. Please enter a positive quantity.', 0);

--changeSet 0utl_message:100 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10106, 'LOGIC', 'InvalidReceivedQuantityException', 'You have entered an invalid received quantity for Line No %1. As the inventory is serialized, please enter a received quantity of either zero or one.', 0);

--changeSet 0utl_message:101 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10107, 'LOGIC', 'InvalidReceivedQuantityException', 'You have entered a received quantity for Line No %1 that exceeds the expected quantity. Please enter a valid received quantity that is equal to or less than %2.', 0);

--changeSet 0utl_message:102 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10108, 'LOGIC', 'InventoryNotInShipFromLocationException', 'Currently, the %1 is located in %2, while the shipment is set to originate in %3.<br><br>All items in this shipment must be currently located at %3.', 0);

--changeSet 0utl_message:103 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10109, 'LOGIC', 'To be installed inventory was not found.', 'You have identified that you installed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 could not be found. Please confirm that the part number and serial number of the installed inventory is correct.', 0);

--changeSet 0utl_message:104 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10110, 'LOGIC', 'To be installed inventory is scrapped or condemned.', 'You have identified that you installed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is scrapped or condemned. Please confirm that the part number and serial number of the installed inventory is correct.', 0);

--changeSet 0utl_message:105 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10111, 'LOGIC', 'To be installed inventory is not in maintenance location.', 'You have identified that you installed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is not in maintenance location. Please confirm that the part number and serial number of the installed inventory is correct.', 0);

--changeSet 0utl_message:106 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10112, 'LOGIC', 'To be installed inventory is attached.', 'You have identified that you installed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is attached. Please confirm that the part number and serial number of the installed inventory is correct.', 0);

--changeSet 0utl_message:107 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10113, 'LOGIC', 'Part no has not been approved yet.', 'You have identified that you installed part no %1 during the execution of task %2. According to Maintenix records, part no %1 has not been approved yet. Please confirm that the part number of the installed inventory is correct.', 0);

--changeSet 0utl_message:108 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10114, 'LOGIC', 'To be removed inventory is scrapped or condemned.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is scrapped or condemned, and the currently installed item has part no %3 and serial number %4. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:109 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10115, 'LOGIC', 'To be removed inventory does not match the installed inventory.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 does not match the currently installed item with part no %4 and serial number %5. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:110 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10116, 'LOGIC','To be removed inventory is could not be found.' ,'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 could not be found. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:111 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10117, 'LOGIC', 'To be removed inventory is not found and there is installed inventory.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 does not exist, and the currently installed item has part no %4 and serial number %5. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:112 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10118, 'LOGIC', 'To be removed inventory is not found and there is no installed inventory.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 could not be found, and an inventory is not installed. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:113 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10119, 'LOGIC', 'To be removed inventory is loose.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 already exists somewhere else, and an inventory is not installed. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:114 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10120, 'LOGIC', 'To be removed inventory is loose and scrapped or condemned.', 'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is loose and scrapped or condemned, and an inventory is not installed. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:115 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10121, 'LOGIC','To be removed serialized inventory is loose and scrapped or condemned.' ,'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is scrapped or condemned. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:116 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10122, 'LOGIC','To be removed serialized inventory is loose.' ,'You have identified that you removed part no %1 with serial number %2 during the execution of task %3. According to Maintenix records, part no %1 with serial number %2 is already loose. Please confirm that the part number and serial number of the removed inventory is correct.', 0);

--changeSet 0utl_message:117 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10123, 'LOGIC', 'Installed and removed part total quantity does not match.', 'You have identified that you removed %2 and installed %3 %1 parts during the execution of task %4. Please confirm that the installed and removed quantity numbers are correct.', 0);

--changeSet 0utl_message:118 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10124, 'LOGIC', 'Installed and removed parts are not interchangeable.','You have identified that you removed %1 part and installed %2 part during the execution of task %3. The two parts are not interchangeable. Please confirm that this is an action you wish to perform.', 0);

--changeSet 0utl_message:119 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10125, 'LOGIC', 'InvalidTransferLocationException', 'The transfer from location ''%1'' is not under the same airport as the transfer to location ''%2''.', 0);

--changeSet 0utl_message:120 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10126, 'LOGIC', 'DuplicateShipmentAssignmentException', 'The inventory item ''%1'' is currently assigned to an active shipment ''%2''.  Please either cancel or complete the shipment before creating another shipment for this inventory item.', 0);

--changeSet 0utl_message:121 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10127, 'LOGIC', 'NoDockLocationException', 'The ''%1'' location is either not a dock location or it''s supply location does not have a dock location.  Please specify a new dock location and try again.', 0);

--changeSet 0utl_message:122 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10128, 'LOGIC', 'NoDockLocationForInventoryException', 'The inventory item ''%1'' is currently located at ''%2'' which does not have a supply location which has a dock location.  Please move the inventory to an appropriate location with a dock location and then try again.', 0);

--changeSet 0utl_message:123 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10129, 'LOGIC', 'DuplicateBarcodeException', 'The barcode ''%1'' already exists for another shipment.  Please change your barcode to a unique barcode and try again.', 0);

--changeSet 0utl_message:124 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10130, 'LOGIC', 'OnlySingleTrackedItemException', 'This part requirement is for a tracked part and therefore, only one %1 can be assigned.', 0);

--changeSet 0utl_message:125 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10131, 'LOGIC', 'QtyCanNotBeRemovedException', 'You have attempted to removed a quantity controlled part.<br><br>Quantity controlled parts cannot be removed.', 0);

--changeSet 0utl_message:126 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10132, 'LOGIC', 'InvalidBomPartException', 'You have attempted to schedule a part requirement against a bom part that is on a different assembly than the task.<br><br>Please select a different bom part and try again.', 0);

--changeSet 0utl_message:127 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10133, 'LOGIC', 'InvalidBomPositionException', 'The bom position is not valid for the specified bom part. <br><br>Please select a different bom part or position and try again.', 0);

--changeSet 0utl_message:128 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10134, 'LOGIC', 'InvalidQuantityForTrackedItemException', 'For tracked items, the scheduled quantity must be 1. <br><br>Please update the scheduled quantity and try again.', 0);

--changeSet 0utl_message:129 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10135, 'LOGIC', 'TrackedItemMissingAssemblyInfoException', 'You have not specified the assembly information.<br><br>Please specify the assembly information and try again.', 0);

--changeSet 0utl_message:130 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10136, 'LOGIC', 'AircraftPartRequirementNotPossibleException', 'You may not schedule a part requirement for an entire aircraft.<br><br>Please select a different part and try again.', 0);

--changeSet 0utl_message:131 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10137, 'LOGIC', 'IncompatibleBomPartAndPartNoException', 'The specified part is not compatible with the part requirement''s bom part.<br><br>Please select a different part and try again.', 0);

--changeSet 0utl_message:132 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10138, 'LOGIC', 'InvalidQuantityForSerializedItemException', 'For serialized parts, the scheduled quantity must be 1.<br><br>Please update the scheduled quantity and try again.', 0);

--changeSet 0utl_message:133 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10139, 'LOGIC', 'SerialNoForUnserializedException', 'The serial number cannot be specified for quantity controlled parts.<br><br>Please remove the serial number and try again.', 0);

--changeSet 0utl_message:134 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10140, 'LOGIC', 'MandatorySerialNoForSerializedException', 'The serial number must be specified for serialized items.<br><br>Please specify a serial number and try again.', 0);

--changeSet 0utl_message:135 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10141, 'LOGIC', 'MandatoryPartNoForInstPartReqException', 'Part number is mandatory for %1 inventory when performing part installation. Please provide part no for installed part and try again.', 0);

--changeSet 0utl_message:136 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10142, 'LOGIC', 'MandatorySerialNoForInstPartReqException', 'Serial number is mandatory for %1 inventory when performing part installation. Please provide serial number for installed part and try again.', 0);

--changeSet 0utl_message:137 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10143, 'LOGIC', 'MandatoryPartNoForRmvdPartReqException', 'Part number is mandatory for %1 inventory when performing part removal. Please provide part no for removed part and try again.', 0);

--changeSet 0utl_message:138 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10144, 'LOGIC', 'MandatorySerialNoForRmvdPartReqException', 'Serial number is mandatory for %1 inventory when performing part removal. Please provide serial number for removed part and try again.', 0);

--changeSet 0utl_message:139 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10145, 'LOGIC', 'InvalidLocationException', 'The location code ''%1'' does not represent a valid location.  Please change your location code and try again.', 0);

--changeSet 0utl_message:140 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10146, 'LOGIC', 'InvalidInventoryForRootTaskClassException', 'The root task %1 of class %2 can not be created on inventory %3 with inventory class %4.  Please use a different inventory or change the class of the root task.', 0);

--changeSet 0utl_message:141 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10147, 'LOGIC', 'InvalidShipmentBarcodeException', 'The shipment barcode %1 was not found in the system.', 0);

--changeSet 0utl_message:142 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10148, 'LOGIC', 'WorkOrderOperationException', 'The operation is intended for a work order task only.', 0);

--changeSet 0utl_message:143 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10149, 'LOGIC', 'InvalidInventoryClassException', 'Task %2 currently has a status of %3. <br><br> Tasks with a status of %3 cannot be completed.', 0);

--changeSet 0utl_message:144 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10150, 'LOGIC', 'UnserviceableInventoryToServiceableLocationException', 'The unserviceable inventory %1 cannot be transferred to serviceable location %2.', 0);

--changeSet 0utl_message:145 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10151, 'LOGIC', 'InventoryNotInShipFromLocationException', 'A new shipment line for the %1 could not be created, because the inventory is located in %2, while the duplicate shipment will originate in %3.', 0);

--changeSet 0utl_message:146 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10152, 'LOGIC', 'SingleWorkOrderException', 'Inventory %1 already has a work order with name %2. Only one active work order per inventory is allowed. Please cancel or complete existing work order.', 0);

--changeSet 0utl_message:147 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10153, 'LOGIC', 'DuplicateBomPartException', 'A Bom Part with assembly code ''%1'' and bom part code ''%2'' already exists. Please enter a unique bom part code value.', 0);

--changeSet 0utl_message:148 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10154, 'LOGIC', 'InvalidAircraftRegCdException', 'The aircraft ''%1'' could not be found.  Please enter a difference aircraft reference and try again.', 0);

--changeSet 0utl_message:149 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10155, 'LOGIC', 'InvalidFinancialAccountCodeException', 'The account code ''%1'' is not valid.', 0);

--changeSet 0utl_message:150 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10156, 'LOGIC', 'InvalidFinancialAccountTypeException', 'The account code ''%1'' is not of type ''%2''.', 0);

--changeSet 0utl_message:151 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10157, 'LOGIC', 'IssuedInventoryException', 'The %1 operation cannot be performed since the inventory %2 is already issued to part request %3.', 0);

--changeSet 0utl_message:152 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10158, 'LOGIC', 'LocationNotExistsException', 'A ''%1'' location type could not be found in the supply location tree of the location ''%2''.', 0);

--changeSet 0utl_message:153 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10159, 'LOGIC', 'InvalidConditionException', 'The inventory ''%1'' has a condition of ''%2'' but should be ''%3''.', 0);

--changeSet 0utl_message:154 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10160, 'LOGIC', 'InventoryNotIssuedException', 'The inventory ''%1'' cannot be turned in because it has not been issued.', 0);

--changeSet 0utl_message:155 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10161, 'LOGIC', 'Inventory Not Exact', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 is different than the requested part no %3. ', 0);

--changeSet 0utl_message:156 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10162, 'LOGIC', 'Inventory Not Compatible', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 is not compatible with aircraft part no %3 with serial number %4. ', 0);

--changeSet 0utl_message:157 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10163, 'LOGIC', 'Inventory Not Interchangeable', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 is not interchangeable with part no %3 with serial number %4. ', 0);

--changeSet 0utl_message:158 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10164, 'LOGIC', 'Inventory Will Expire', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 expires on date %3 and it is before part request %4 needed by date %5. ', 0);

--changeSet 0utl_message:159 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10165, 'LOGIC', 'Inventory has locked Reservation', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 has locked part reservation %3. ', 0);

--changeSet 0utl_message:160 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10166, 'LOGIC', 'Not Enough Quantity', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 does not have enough items left to fully fulfill the part request %3.', 0);

--changeSet 0utl_message:161 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10167, 'LOGIC', 'PartRequestExistsException', 'The installed part ''%1'' already has a part request.', 0);

--changeSet 0utl_message:162 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10168, 'LOGIC', 'HistoricalRecordException', 'The installed part ''%1'' is historic.  Please select an installed part with active status.', 0);

--changeSet 0utl_message:163 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10169, 'LOGIC', 'InventoryIsNotSpecificException', 'Inventory with serial number %1 and part no oem %2 that you wish to reserve does not match the part number %3 specified by the part request %4, and it is not known alternative.', 0);

--changeSet 0utl_message:164 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10170, 'LOGIC', 'OriginatingPurchaseOrderNotFoundException', 'Originating purchase order could not be found for inventory item %1.', 0);

--changeSet 0utl_message:165 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10171, 'LOGIC', 'VendorHasNoLocationException', 'The vendor, %1, does not have a location assigned.', 0);

--changeSet 0utl_message:166 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10172, 'LOGIC', 'DuplicatePartPOLineException', '%1 is already listed on the purchase order.  Please adjust the quantity to be ordered instead of adding a new line.', 0);

--changeSet 0utl_message:167 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10173, 'LOGIC', 'UnserviceableInventoryToBinLocationException', 'The unserviceable inventory %1 cannot be transferred to bin location %2.', 0);

--changeSet 0utl_message:168 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10174, 'LOGIC', 'HistoricTaskException', 'You cannot create a part request on the historic task ''%1''.', 0);

--changeSet 0utl_message:169 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10175, 'LOGIC', 'InvalidPartClassException', 'The inventory class of part %1 is invalid.<br><br>The following inventory classes are invalid: %2', 0);

--changeSet 0utl_message:170 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10176, 'LOGIC', 'InvalidPOBarcodeException', 'The purchase order barcode %1 was not found in the system.', 0);

--changeSet 0utl_message:171 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10177, 'LOGIC', 'HistoricPartRequestException', 'The specified part request %1 is historic.', 0);

--changeSet 0utl_message:172 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10178, 'LOGIC', 'IncompleteInventoryException', 'The %1 inventory is incomplete, operation could not be performed.', 0);

--changeSet 0utl_message:173 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10179, 'LOGIC', 'InvalidVendorCodeException', 'The vendor %1 does not exist.', 0);

--changeSet 0utl_message:174 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10180, 'LOGIC', 'InvalidScheduledLocationExceptionForWorkOrder', 'The location ''%1'' is not a SHOP location. Please enter a SHOP location.', 0);

--changeSet 0utl_message:175 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10181, 'LOGIC', 'InvalidScheduledLocationExceptionForCheck', 'The location ''%1'' is not a LINE or TRACK location and the location''s airport does not have a LINE location. Please enter a LINE or TRACK location or an airport with a single LINE location.', 0);

--changeSet 0utl_message:176 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10182, 'LOGIC', 'InvalidScheduledLocationExceptionForCheckWithMultipleLine', 'The location ''%1'' is not a LINE or TRACK location and the location''s airport has more than one LINE location. Please enter a LINE or TRACK location or an airport with a single LINE location.', 0);

--changeSet 0utl_message:177 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10183, 'LOGIC', 'DuplicatePONumberException', 'The PO Number %1 is already assigned to another Purchase Order.  Please enter a unique PO Number.', 0);

--changeSet 0utl_message:178 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10184, 'LOGIC', 'PartNoBomPartMismatchException', 'The  %1 part number is not assigned to the %2 BOM part.', 0);

--changeSet 0utl_message:179 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10185, 'LOGIC', 'InvalidPartRequestStatusException', 'The part request ''%1'' has a condition of ''%2'' but should be ''%3''.', 0);

--changeSet 0utl_message:180 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10186, 'LOGIC', 'ExpiredInventoryException', 'The inventory ''%1'' is expired as of ''%2''.', 0);

--changeSet 0utl_message:181 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10187, 'LOGIC', 'Not Enough Quantity', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 does not have enough items left to fully fulfill the requested quantity of %3.', 0);

--changeSet 0utl_message:182 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10188, 'LOGIC', 'DuplicateBomPartRequirement', 'There is already a part requirement scheduled for BOM Part %1 in task %2.', 0);

--changeSet 0utl_message:183 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10189, 'LOGIC', 'PartRequestHasNoBomPartException', 'The part request ''%1'' requires a bom part to be promoted to a purchase request.', 0);

--changeSet 0utl_message:184 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10190, 'LOGIC', 'BomPartHasNoStandardPartException', 'The bom part ''%1'' does not have a standard part.', 0);

--changeSet 0utl_message:185 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10191, 'LOGIC', 'CannotDeleteVendorAccountException', 'The vendor account %1 cannot be deleted because it is associated with events in Maintenix.', 0);

--changeSet 0utl_message:186 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10192, 'LOGIC', 'InvalidQuantityForBatchOrQtyItemException', 'You have assigned %1 %3s to this tasks. The associated BOM slot can fit a maximum of %2 %3. Please select a maximum of %2 %3 to assign to this task.', 0);

--changeSet 0utl_message:187 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10193, 'LOGIC', 'InvalidAccountAssignmentException', 'A purchase order line cannot have its account modified if the line is currently expensed to an asset account.', 0);

--changeSet 0utl_message:188 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10194, 'LOGIC', 'InvalidPOLineDescriptionException', 'A purchase order line cannot have its description modified if the line item is a part or workorder.', 0);

--changeSet 0utl_message:189 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10195, 'LOGIC', 'NullVendorException', 'The inventory ''%1'' does not have a vendor associated with it.', 0);

--changeSet 0utl_message:190 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10196, 'LOGIC', 'CannotRemoveRepairPOLineException', 'Purchase order line %1 (%2) cannot be removed because it is associated with a repair.', 0);

--changeSet 0utl_message:191 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10197, 'LOGIC', 'InvalidPOLineAssignmentException', 'This action could not be completed because not all purchase order lines specified are assigned to the purchase order.', 0);

--changeSet 0utl_message:192 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10198, 'LOGIC', 'NoPOLineException', 'This action cannot be performed because there are no lines assigned to purchase order ''%1''.', 0);

--changeSet 0utl_message:193 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10199, 'LOGIC', 'InvalidVendorStatusException', 'The vendor %1 has a status of %2 and therefore cannot be used.<br><br>Please select a vendor that has a status other than %2.', 0);

--changeSet 0utl_message:194 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10200, 'LOGIC', 'InvalidLocationTypeException', 'The location %1 is not of the expected location type(s) %2.<br>Please change your location and try again.', 0);

--changeSet 0utl_message:195 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10201, 'LOGIC', 'MandatoryRemovalReasonException', 'A removal reason is mandatory when performing a part removal.<br><br>Please provide a removal reason for the removed part(s) and try again.', 0);

--changeSet 0utl_message:196 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10202, 'LOGIC', 'EndDateBeforeSubTaskEndDateException', 'The end date you have entered occurs before the end date of a sub-task.<br><br>Please enter an end date no earlier than %1.', 0);

--changeSet 0utl_message:197 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10203, 'LOGIC', 'CannotUnassignJobCardException', 'You are only allowed to unassign blocks and requirements; you cannot unassign job cards from a check.', 0);

--changeSet 0utl_message:198 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10204, 'PO_EXCEP', 'PurchaseOrderExceptionMessage', 'The vendor has made changes to purchase order %1.', 0);

--changeSet 0utl_message:199 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10205, 'SHIP_ADV', 'POShipmentAdvisoryMessage', 'The shipment %1 has been shipped containing parts that fulfill line items on purchase order %2.', 0);

--changeSet 0utl_message:200 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10206, 'LOGIC', 'ReplacementInventoryBomItemMismatchException', 'The inventory bom item ''%1'' must be equal to the task definition replacement bom item ''%2''.', 0);

--changeSet 0utl_message:201 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10207, 'LOGIC', 'ParentInventoryNotFoundException', 'The inventory item ''%1'' does not have a parent inventory with bom item ''%2''.', 0);

--changeSet 0utl_message:202 stripComments:false
INSERT INTO UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
VALUES (10208, 'LOGIC', 'Inventory Not Applicable', 'You have identified reservation of part no %1 with serial number %2. According to Maintenix records, part no %1 with serial number %2 is not applicable to part no %3 with serial number %4. ', 0);

--changeSet 0utl_message:203 stripComments:false
-- Insert scripts for common exceptions
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (20001, 'COMMON', 'StringTooLongException', 'The ''%1'' you have entered is too long.<br><br>Please shorten the ''%1'' to a maximum of %2 characters.', 0);

--changeSet 0utl_message:204 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (20002, 'COMMON', 'MandatoryArgumentException', 'The ''%1'' is a mandatory field.<br><br>Please enter a value for the ''%1''.', 0);

--changeSet 0utl_message:205 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (20003, 'COMMON', 'PositiveValueException', 'The ''%1'' must be positive.<br><br>Please enter a value greater than 0.', 0);

--changeSet 0utl_message:206 stripComments:false
insert into UTL_MESSAGE (MSG_ID, MSG_TYPE_CD, MSG_TITLE, MSG_LDESC, UTL_ID)
values (20004, 'COMMON', 'NegativeValueException', 'The ''%1'' cannot be negative.<br><br>Please enter a value greater than or equal to 0.', 0);