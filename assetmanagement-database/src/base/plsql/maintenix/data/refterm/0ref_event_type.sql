--liquibase formatted sql


--changeSet 0ref_event_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_EVENT_TYPE"
** 0-Level
** DATE: 04-OCT-04 TIME: 16:24:27
*********************************************/
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'QC', 0, 1,  'Quantity Correction', 'Quantity Correction', 0,  0, '29-SEP-04', '29-SEP-04', 100, 'MXI');

--changeSet 0ref_event_type:2 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FL', 0, 95,  'Flight', 'Flight', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:3 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'FG', 0, 1,  'Configuration Change', 'Configuration Change', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:4 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CF', 0, 118,  'Fault', 'Component Fault', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:5 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IX', 0, 12,  'Shipment', 'Remote/External Shipment', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:6 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UC', 0, 54,  'Usage Correction', 'Usage Correction', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:7 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'UR', 0, 6,  'Usage Record', 'Usage Record', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:8 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IL', 0, 144,  'Inventory Lock', 'Inventory Lock', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:9 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'AC', 0, 56,  'Change Inventory Condition', 'Change Inventory Condition', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:10 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CO', 0, 56,  'Change Aircraft Operating Status', 'Change Aircraft Operating Status', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:11 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CC', 0, 56,  'Change Aircraft Capability', 'Change Aircraft Capability', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:12 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TS', 0, 56,  'Task', 'Maintenance Task', 0,  0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_event_type:13 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IPN', 0, 1,  'Inventory Part Number Change', 'Inventory Part Number Change', 0,  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_type:14 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ISN', 0, 1,  'Inventory Serial Number Change', 'Inventory Serial Number Change', 0,  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_type:15 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IMD', 0, 1,  'Inventory Manufacture Date Change', 'Inventory Manufacture Date Change', 0,  0, '16-SEP-03', '16-SEP-03', 100, 'MXI');

--changeSet 0ref_event_type:16 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IMS', 0, 1,  'Inventory Modification Status Note Change', 'Inventory Modification Status Note Change', 0,  0, '12-JUL-07', '12-JUL-07', 100, 'MXI');

--changeSet 0ref_event_type:17 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LX', 0, 2,  'Transfer', 'Local/Internal Transfer', 0,  0, '27-OCT-04', '27-OCT-04', 100, 'MXI');

--changeSet 0ref_event_type:18 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PR', 0, 1,  'Part Request', 'This is a standard purchase order used to acquire parts or services', 0,  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_type:19 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PO', 0, 1,  'Purchase Order', 'This is a repair purchase order used to purchase repair services', 0,  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_type:20 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PI', 0, 1,  'Purchase Invoice', 'This is a Purchase Invoice', 0,  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_type:21 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'DCC', 0, 1,  'Distributed Control Change', 'Distributed operation event that marks when data (ex. an aircraft) has been deployed to a satellite site', 0,  0, '28-MAR-05', '28-MAR-05', 100, 'MXI');

--changeSet 0ref_event_type:22 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'TCO', 0, 1,  'Tool Checkout', 'This is the Tool Checkout event.', 0,  0, '20-JAN-06', '20-JAN-06', 100, 'MXI');

--changeSet 0ref_event_type:23 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'IC', 0, 1,  'Inventory Changed', 'This is the Inventory Details Changed event.', 0,  0, '10-MAY-06', '10-MAY-06', 100, 'MXI');

--changeSet 0ref_event_type:24 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICC', 0, 1,  'Inventory Custody Change', 'This is the Inventory custody change event.', 0,  0, '24-MAY-06', '24-MAY-06', 100, 'MXI');

--changeSet 0ref_event_type:25 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'VN', 0, 1,  'Vendor', 'Vendor', 0,  0, '30-JUN-06', '30-JUN-06', 100, 'MXI');

--changeSet 0ref_event_type:26 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PE', 0, 1,  'Part Edit', 'Part Edit', 0,  0, '06-JUL-06', '06-JUL-06', 100, 'MXI');

--changeSet 0ref_event_type:27 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'BLK', 0, 1,  'Blackout', 'Blackout', 0,  0, '01-DEC-06', '01-DEC-06', 100, 'MXI');

--changeSet 0ref_event_type:28 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'RFQ', 0, 1,  'Request For Quote', 'Request For Quote', 0,  0, '28-NOV-06', '28-NOV-06', 100, 'MXI');

--changeSet 0ref_event_type:29 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'OC', 0, 1,  'Owner Change', 'Owner Change', 0,  0, '17-MAR-07', '17-MAR-07', 100, 'MXI');

--changeSet 0ref_event_type:30 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'EO', 0, 1,  'Exchange Order', 'This is an exchange order used to exchange parts', 0,  0, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_type:31 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LD', 0, 1,  'License Defition', 'This is a license definition historic event', 0,  0, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_type:32 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'HR', 0, 1,  'Human Resource', 'This is a human resource historic event', 0,  0, '09-APR-07', '09-APR-07', 100, 'MXI');

--changeSet 0ref_event_type:33 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'PRA', 0, 1,  'Part Request Assignment', 'This is an assignment of a part request', 0,  0, '16-APR-08', '16-APR-08', 100, 'MXI');

--changeSet 0ref_event_type:34 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CL', 0, 1,  'Warranty Claim', 'This is a warranty claim', 0,  0, '16-APR-08', '16-APR-08', 100, 'MXI');

--changeSet 0ref_event_type:35 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'CA', 0, 1,  'Change Authority', 'Change Authority', 0,  0, '02-DEC-09', '02-DEC-09', 100, 'MXI');

--changeSet 0ref_event_type:36 stripComments:false
insert into ref_event_type(event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_def_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'ICR', 0, 1,  'Inventory Creation', 'This is an inventory creation event.', 0,  0, '02-FEB-15', '02-FEB-15', 100, 'MXI');