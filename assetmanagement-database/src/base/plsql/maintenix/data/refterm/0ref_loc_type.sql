--liquibase formatted sql


--changeSet 0ref_loc_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_LOC_TYPE"
** 0-Level
** DATE: 28-04-05 TIME: 16:56:27
********************************************/
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SYSTEM', 0, 01, 'System', 'This location type is used as an administrative location.  There should only be one System location in your organization. The System location will be part of 0-level data.  ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:2 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'REGION', 0, 01, 'Region', 'This location is used to represent your world view.  Regions can be organized in any way, including Continent/Country/Province/etc.  Essentially, the purpose of the region location type is to organize your airports in some meaningful way. ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:3 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'OPS', 0, 01, 'Operation', 'This is the location, which holds aircraft that are currently in flight as well as inventory that are being shipped between remote locations. The OPS location will be part of 0-level data.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:4 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'ARCHIVE', 0, 01, 'Archive', 'This location is used to hold archived inventory records.  All archived inventory records will be held in a single location. The Archive location will be part of 0-level data', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:5 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'VENDOR', 0, 01, 'Vendor', 'This location type is used to represent a vendor.  Each vendor has an individual location, and all inventories, which are currently being repaired/loaned to the vendor, are shown here.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:6 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'AIRPORT', 0, 49, 'Airport', 'This location type (the children of which are detailed below) identifies an airport.  Every location below an airport is considered local, and will be generally in one geographical location. ', 0, '17-JAN-02', '17-JAN-02', 100, 'MXI');

--changeSet 0ref_loc_type:7 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SHIPRCV', 0, 01, 'Shipping Receiving', 'This location is used to group all of the shipping and receiving sub-locations together', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:8 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SRVSTORE', 0, 39, 'Serviceable Stores', 'This location type represents the desk at the front of the warehouse.  Inventory that is routed to the storage is deposited at this desk and put away by warehouse personnel. In some cases the inventory is picked from the shelves and placed at the SRVSTORE location it is then issued to Delivery personnel or technicians.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:9 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'USSTORE', 0, 01, 'Unserviceable Stores', 'This location type is the same as the SRVSTORE. The only difference is that it deals with unserviceable inventory.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:10 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'REPAIR', 0, 01, 'Repair', 'This location type represents the repair shop.  ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:11 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'LINE', 0, 01, 'Line', 'This location type is used for line maintenance. This is one of the location types where a check can be executed.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:12 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'HGR', 0, 50, 'Hangar', 'This location type is the parent for all of the heavy maintenance tracks.  ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:13 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'INTRANS', 0, 01, 'In Transit', 'This location holds the inventories that are currently being moved around one supply location (an airport). ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:14 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SRVSTG', 0, 39, 'Serviceable Staging', 'This location represents the serviceable staging area.  Serviceable inventory that arrives at the dock and is not immediately moved to another location will be placed here, pending disposition (issue to maintenance, store in warehouse, ship to different location, sell, etc).  Each SRVSTG location will be able to handle certain types of inventory, which can be modeled via Bin Levels.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:15 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'USSTG', 0, 41, 'Unserviceable Staging', '', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:16 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'QUAR', 0, 117, 'Quarantine','This location represents the quarantine location, where inventory is placed in quarantine.  Inventory can be routed to quarantine from any location, not just from SandR related locations. ', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:17 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'DOCK', 0, 01, 'Dock', 'This location represents the loading dock, where inventory is shipped and received. There could be only one dock per an airport.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:18 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'STORE', 0, 01, 'Store', 'This location type provides a way to represent the organization of the warehouse.  The top level STORE locations can be used for buildings or sections, while lower level items can be for aisles, or shelves, and so on.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:19 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SHOP', 0, 5, 'Local Repair Shop', 'This location type represents a repair location in a repair shop.  Each repair shop will generally perform a different type of repair (Hydraulics, Avionics, Electronics, Breakdown, etc). Work Orders can be scheduled only into this type of location.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:20 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'BNCHSTK', 0, 01, 'BenchStock Location', 'This location type represents a benchstock location.  Benchstock is inventory that is stored in the maintenance area.  Benchstock is often general-purpose inventory, often consumables and expendables.  If a BNCHSTK is directly under the REPAIR or HGR location, the items at that location are available to each SHOP or TRACK.  If the BNCHSTK is under a SHOP or TRACK location, the items at that location are only available to the parent.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:21 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'TRACK', 0, 1, 'Track', 'This location type represents the heavy maintenance track where the work is performed.  There will be one location for each track. This is one of the location types where a check can be executed.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:22 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'BIN', 0, 01, 'Bin', 'This location type is used to represent the actual storage locations for inventory items.  Many BIN locations will have Bin Levels defined against them, identifying which inventory items can be stored at each location, as well as the maximum number of items which can be stored.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:23 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'PREDRAW', 0, 01, 'Predraw Location', 'This location type is used when a predraw is being compiled. Predraw locations will be associated with a Work Order number, representing the upcoming check which for which the inventory is being assembled.  When the check has completed, or when all of the inventory has been moved to the appropriate hanger, the WO number is unassigned, and the PREDRAW location is available for a new WO.', 0, '20-OCT-04', '20-OCT-04', 100, 'MXI');

--changeSet 0ref_loc_type:24 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'OUTSRCE', 0, 1, 'Out Source', 'Container for records for items that have left the Customer System (external overhaul, etc.)', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_loc_type:25 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'SCRAP', 0, 1, 'Scrap', 'Scrap', 0, '2-NOV-01', '2-NOV-01', 100, 'MXI');

--changeSet 0ref_loc_type:26 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'LOAN', 0, 47, 'Loaner', 'Loaner', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_loc_type:27 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'RECEIVE', 0, 74, 'Receiving Dock', 'Receiving Dock', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_loc_type:28 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'PREDRBIN', 0, 01, 'Predraw Bin', 'Location in which pre-drawn inventory is to be placed for a specific work order number.', 0, '05-JUN-06', '05-JUN-06', 100, 'MXI');

--changeSet 0ref_loc_type:29 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'CSGNPOOL', 0, 01, 'Consignment Pool', 'This location type is used to represent the third party locations for returning consigned inventory.', 0, '09-Oct-07', '09-Oct-07', 100, 'MXI');

--changeSet 0ref_loc_type:30 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'VENHGR', 0, 50, 'Vendor Hangar', 'This location type is the parent for all of the heavy maintenance vendor tracks.', 0, '16-Jun-08', '16-Jun-08', 100, 'MXI');

--changeSet 0ref_loc_type:31 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'VENLINE', 0, 1, 'Vendor Line', 'This location type is used for vendor line maintenance. This is one of the location types where a check can be executed.', 0, '16-Jun-08', '16-Jun-08', 100, 'MXI');

--changeSet 0ref_loc_type:32 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'VENTRK', 0, 1, 'Vendor Track', 'This location type represents the heavy maintenance vendor track where the work is performed.  There will be one location for each track. This is one of the location types where a check can be executed.', 0, '16-Jun-08', '16-Jun-08', 100, 'MXI');

--changeSet 0ref_loc_type:33 stripComments:false
insert into REF_LOC_TYPE (LOC_TYPE_DB_ID, LOC_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 0, 1, 'N/A', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_loc_type:34 stripComments:false
insert into ref_loc_type(loc_type_db_id, loc_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0, 'COMPANY', 0, 01, 'Company', 'This location type represents a company location. The company locations will be used to model the organizations that have location at an airport.', 0, to_date('28-02-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('28-02-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');