--liquibase formatted sql


--changeSet 0utl_menu_arg:1 stripComments:false
-- Insert script for ULT_MENU_ARG
-- These are deprecated argument names
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('an_eventdbid', 1, null , 0);

--changeSet 0utl_menu_arg:2 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('an_eventid', 1, null , 0);

--changeSet 0utl_menu_arg:3 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('AN_INVNODBID', 1, null , 0);

--changeSet 0utl_menu_arg:4 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('AN_INVNOID', 1, null , 0);

--changeSet 0utl_menu_arg:5 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aIsAircraft', 0, null , 0);

--changeSet 0utl_menu_arg:6 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aCheckDbId', 1, null , 0);

--changeSet 0utl_menu_arg:7 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aCheckId', 1, null , 0);

--changeSet 0utl_menu_arg:8 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskDbId', 1, null , 0);

--changeSet 0utl_menu_arg:9 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskId', 1, null , 0);

--changeSet 0utl_menu_arg:10 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aInvNoDbId', 1, null , 0);

--changeSet 0utl_menu_arg:11 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aInvNoId', 1, null , 0);

--changeSet 0utl_menu_arg:12 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFlightDbId', 1, null , 0);

--changeSet 0utl_menu_arg:13 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFlightId', 1, null , 0);

--changeSet 0utl_menu_arg:14 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartNoDbId', 1, null , 0);

--changeSet 0utl_menu_arg:15 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartNoId', 1, null , 0);

--changeSet 0utl_menu_arg:16 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aRFQDbId', 1, null , 0);

--changeSet 0utl_menu_arg:17 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aRFQId', 1, null , 0);

--changeSet 0utl_menu_arg:18 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aUsageRecordDbId', 1, null , 0);

--changeSet 0utl_menu_arg:19 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aUsageRecordId', 1, null , 0);

--changeSet 0utl_menu_arg:20 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskDefinitionDbId', 1, null , 0);

--changeSet 0utl_menu_arg:21 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskDefinitionId', 1, null , 0);

--changeSet 0utl_menu_arg:22 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aShipmentDbId', 1, null , 0);

--changeSet 0utl_menu_arg:23 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aShipmentId', 1, null , 0);

--changeSet 0utl_menu_arg:24 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aVendorDbId', 1, null , 0);

--changeSet 0utl_menu_arg:25 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aVendorId', 1, null , 0);

--changeSet 0utl_menu_arg:26 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aManufactDbId', 1, null , 0);

--changeSet 0utl_menu_arg:27 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aManufactCd', 1, null , 0);

--changeSet 0utl_menu_arg:28 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aOwnerDbId', 1, null , 0);

--changeSet 0utl_menu_arg:29 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aOwnerId', 1, null , 0);

--changeSet 0utl_menu_arg:30 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFaultDbId', 1, null , 0);

--changeSet 0utl_menu_arg:31 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFaultId', 1, null , 0);

--changeSet 0utl_menu_arg:32 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aIsPossibleFault', 0, null , 0);

--changeSet 0utl_menu_arg:33 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPurchaseOrderDbId', 1, null , 0);

--changeSet 0utl_menu_arg:34 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPurchaseOrderId', 1, null , 0);

--changeSet 0utl_menu_arg:35 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aStockNoDbId', 1, null , 0);

--changeSet 0utl_menu_arg:36 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aStockNoId', 1, null , 0);

--changeSet 0utl_menu_arg:37 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTransferDbId', 1, null , 0);

--changeSet 0utl_menu_arg:38 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTransferId', 1, null , 0);

--changeSet 0utl_menu_arg:39 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aLocationDbId', 1, null , 0);

--changeSet 0utl_menu_arg:40 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aLocationId', 1, null , 0);

--changeSet 0utl_menu_arg:41 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aDepartmentDbId', 1, null , 0);

--changeSet 0utl_menu_arg:42 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aDepartmentId', 1, null , 0);

--changeSet 0utl_menu_arg:43 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aRoleId', 1, null , 0);

--changeSet 0utl_menu_arg:44 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aUserId', 1, null , 0);

--changeSet 0utl_menu_arg:45 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartRequestDbId', 1, null , 0);

--changeSet 0utl_menu_arg:46 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartRequestId', 1, null , 0);

--changeSet 0utl_menu_arg:47 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aBomPartDbId', 1, null , 0);

--changeSet 0utl_menu_arg:48 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aBomPartId', 1, null , 0);

--changeSet 0utl_menu_arg:49 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAuthorityDbId', 1, null, 0);

--changeSet 0utl_menu_arg:50 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAuthorityId', 1, null, 0);

--changeSet 0utl_menu_arg:51 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAssemblyDbId', 1, null, 0);

--changeSet 0utl_menu_arg:52 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAssemblyCd', 1, null, 0);

--changeSet 0utl_menu_arg:53 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAssemblyBomId', 1, null, 0);

--changeSet 0utl_menu_arg:54 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aMaintPrgmDbId', 1, null, 0);

--changeSet 0utl_menu_arg:55 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aMaintPrgmId', 1, null, 0);

--changeSet 0utl_menu_arg:56 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTagDbId', 1, null, 0);

--changeSet 0utl_menu_arg:57 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTagId', 1, null, 0);

--changeSet 0utl_menu_arg:58 stripComments:false
-- These are used by report
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aInventoryKey', 1, null , 0);

--changeSet 0utl_menu_arg:59 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskKey', 1, null , 0);

--changeSet 0utl_menu_arg:60 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aHumanResourceKey', 1, null , 0);

--changeSet 0utl_menu_arg:61 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aMaintProgramKey', 1, null , 0);

--changeSet 0utl_menu_arg:62 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPurchaseOrderKey', 1, null , 0);

--changeSet 0utl_menu_arg:63 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aRFQKey', 1, null , 0);

--changeSet 0utl_menu_arg:64 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aShipmentKey', 1, null , 0);

--changeSet 0utl_menu_arg:65 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTagKey', 1, null , 0);

--changeSet 0utl_menu_arg:66 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTaskDefinitionKey', 1, null , 0);

--changeSet 0utl_menu_arg:67 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aSchedTaskKey', 1, null , 0);

--changeSet 0utl_menu_arg:68 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aWorkPackageKey', 1, null , 0);

--changeSet 0utl_menu_arg:69 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartRequestKey', 1, null , 0);

--changeSet 0utl_menu_arg:70 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTransferKey', 1, null , 0);

--changeSet 0utl_menu_arg:71 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aVendorKey', 1, null , 0);

--changeSet 0utl_menu_arg:72 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aLocationKey', 1, null , 0);

--changeSet 0utl_menu_arg:73 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aBomPartKey', 1, null , 0);

--changeSet 0utl_menu_arg:74 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aRoleKey', 1, null , 0);

--changeSet 0utl_menu_arg:75 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aStockNumberKey', 1, null , 0);

--changeSet 0utl_menu_arg:76 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPOInvoiceKey', 1, null , 0);

--changeSet 0utl_menu_arg:77 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aOwnerKey', 1, null , 0);

--changeSet 0utl_menu_arg:78 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aManufacturerKey', 1, null , 0);

--changeSet 0utl_menu_arg:79 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aPartNumberKey', 1, null , 0);

--changeSet 0utl_menu_arg:80 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAssemblyKey', 1, null , 0);

--changeSet 0utl_menu_arg:81 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAuthorityKey', 1, null , 0);

--changeSet 0utl_menu_arg:82 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aDepartmentKey', 1, null , 0);

--changeSet 0utl_menu_arg:83 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFaultKey', 1, null , 0);

--changeSet 0utl_menu_arg:84 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aAccountKey', 1, null , 0);

--changeSet 0utl_menu_arg:85 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aTCodeKey', 1, null , 0);

--changeSet 0utl_menu_arg:86 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aEventKey', 1, null , 0);

--changeSet 0utl_menu_arg:87 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aFlightKey', 1, null , 0);

--changeSet 0utl_menu_arg:88 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aBomItemKey', 1, null , 0);

--changeSet 0utl_menu_arg:89 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aUsageRecordKey', 1, null , 0);

--changeSet 0utl_menu_arg:90 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aUserKey', 1, null , 0);

--changeSet 0utl_menu_arg:91 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) VALUES ('aSchedLabourKeys', 1, null , 0);