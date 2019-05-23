--liquibase formatted sql


--changeSet 0utl_pb_message_baseliner:1 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00016', 'BUS', 'Cannot Perform Action', 'You cannot assign or unassign usage parms from the root Bom Item. You must add or remove them from the usage definition instead.', 'Stopsign', 'OK', 2, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:2 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00128', 'BUS', 'Unassign', 'Unable to Unassign %s %s' || chr(10) || '%s', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:3 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00145', 'BUS', 'Invalid Database ID', 'The specified database id is invalid. The application could not be started. Tell the system administrator to check the mim_local_db table.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:4 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00149', 'BUS', 'Assembly', 'An Assembly already exists with the code %s' || chr(10) || 'Please enter a new code.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:5 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00150', 'BUS', 'BOM Item', 'There must be at least one position for this item', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:6 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00152', 'BUS', 'Baseliner Drag and Drop', 'The baseline configuration cannot be changed: moving the selected component will have adverse effects on existing inventory items.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:7 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00153', 'BUS', 'Cannot Delete', 'The selected reference term cannot be deleted because it is required system data.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:8 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00172', 'BUS', 'Validation Error', 'The status'' "Status Order" must be between %s and %s.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:9 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00175', 'BUS', 'BOM Item', 'This BOM Item has child BOM Items that are mandatory.' || chr(10) || 'This BOM Item must be mandatory.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:10 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00176', 'BUS', 'BOM Item', 'This BOM Item has a parent BOM Item that is not mandatory.' || chr(10) || 'You cannot set this BOM Item to mandatory.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:11 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00182', 'BUS', 'Usage Parameter', 'The Usage Parameter %s has the same code as an existing one.' || chr(10) || 'Are you sure you wish to modify this Usage Parameter?', 'Question', 'YesNo', 2, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:12 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00183', 'BUS', 'BOM Item', 'The specified %s is already being used by another BOM Item on this Assembly. Do you want to save this non-unique value?', 'Question', 'YesNo', 2, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:13 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00206', 'BUS', 'Validation Error', 'A Usage Definition with the same Data Source already exists on the selected Assembly. The Usage Definition could not be saved.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:14 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00210', 'BUS', 'Deletion Error', 'The Usage Definition cannot be deleted because there are Task Definitions that use one of the Usage Parms for scheduling.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:15 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00212', 'BUS', 'Deletion Error', 'The Usage Parameter cannot be deleted because it is being used in a Usage Definition', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:16 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00213', 'BUS', 'Deletion Error', 'The Usage Parameter cannot be deleted because it is recorded against a piece of inventory', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:17 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00214', 'BUS', 'Deletion Error', 'The assembly could not deleted because it has been created in inventory.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:18 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00215', 'BUS', 'Deletion Error', 'The assembly could not be deleted because there are usage definitions on it.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:19 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00216', 'BUS', 'Deletion Error', 'The assembly could not be deleted because there are calculations on it', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:20 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00218', 'BUS', 'Deletion Error', 'The assembly could not be deleted because there are task definitions on it.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:21 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00219', 'BUS', 'Validation Error', 'The usage parm ''%s'' is a calculation, therefore it cannot be collected as part of this usage definition', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:22 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00220', 'BUS', 'Assignment Error', 'You can only assign part-specific values for "Constant" inputs.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:23 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00224', 'BUS', 'Part No Assignment', 'The following Part No could not be assigned because it is assigned to another assembly: %s', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:24 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00309', 'BUS', 'Deletion Error', 'There are BOM entries attached to this BOM', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:25 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00310', 'BUS', 'Deletion Error', 'There is Inventory associated with this BOM item', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:26 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00311', 'BUS', 'Deletion Error', 'There are task(s) defined against this BOM item', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:27 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00314', 'BUS', 'Deletion Error', 'There are Assemblies that use this Part', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:28 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00315', 'BUS', 'Deletion Error', 'There is Inventory for this Part', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:29 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00316', 'BUS', 'Deletion Error', 'There is a Baseline Task that depends upon this Part', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:30 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00320', 'BUS', 'Deletion Error', 'This Part No is used by part no specific task scheduling rules.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:31 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00323', 'BUS', 'Deletion Error', 'There is Tool for this Part', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:32 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00376', 'BUS', 'Save Error', 'Could not save because the reference term code "%s" already exists in the database.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:33 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00395', 'BUS', 'Deletion Error', 'This Usage Parm cannot be unassigned because there is a Task Definition that uses it for a scheduling interval', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:34 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00398', 'BUS', 'Deletion Error', 'This Part No is assigned to a Part Transformation (MOD) Task Definition', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:35 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00399', 'BUS', 'Baseliner Drag and Drop', 'Are you sure you want to change the configuration of the baseline by moving the selected component?', 'Stopsign', 'YesNo', 2, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:36 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00403', 'BUS', 'BOM Position Delete', 'Could not delete position. BOM item must to have at least one position', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:37 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00404', 'BUS', 'BOM Position Delete', 'Could not delete soft-deleted positions or positions that have inventories installed or historic events recorded', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:38 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00423', 'BUS', 'Save Error', 'Could not save because the "%s" status order "%s" already exists in the database.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:39 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00424', 'BUS', 'Error Launching IETM', 'The specified IETM cannot be launched.  Please ensure that the command line is correct.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:40 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00431', 'BUS', 'Deletion Error', 'Cannot delete the failure effect because it is used by an actual event.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:41 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00432', 'BUS', 'Deletion Error', 'Cannot delete the failure effect because it is assigned to a fault definition.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:42 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00435', 'BUS', 'Deletion Error', 'Cannot delete the fault definition because it is assigned to a component fault.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:43 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00440', 'BUS', 'Select IETM Topic', 'You must select an IETM topic in order to assign a task definition or fault definition to this IETM.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:44 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00446', 'BUS', 'Saving Error', 'MTBF value must be greater than 0.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:45 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00447', 'BUS', 'Saving Error', 'MTBR value must be greater than 0.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:46 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00448', 'BUS', 'Saving Error', 'A usage parm must be entered as there is  either an MTBF or an MTBR.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:47 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00480', 'BUS', 'Deletion Error', 'The assembly could not be deleted because there are fault definitions on it.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:48 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00481', 'BUS', 'Deletion Error', 'The assembly could not be deleted because there are IETMs on it.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:49 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL', 'BUS-00487','BUS', 'No IETM defined','There is no IETM defined for this bom part.','Exclamation','OK', 1,1,'N','N' ,0);

--changeSet 0utl_pb_message_baseliner:50 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
values ('BL', 'BUS-00498', 'BUS', 'Rule Checker', 'This process may take a few minutes to complete.  Do you want to continue?', 'Question', 'YesNo', 1,  1,  'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:51 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL','BUS-00508','BUS','Save Error', 'Cannot delete the IETM as it is associated with an event.','Stopsign', 'OK', 1, 1, 'N','N',0);

--changeSet 0utl_pb_message_baseliner:52 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL','BUS-00509','BUS','Save Error', 'Cannot delete the IETM Topic as it is associated with an event.','Stopsign', 'OK', 1, 1, 'N','N',0);

--changeSet 0utl_pb_message_baseliner:53 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL','BUS-00512','BUS','Delete Confirmation', 'The selected Fault Definition has suppressed faults.  Are you sure you want to delete?','Question', 'YesNo', 1, 1, 'N','N',0);

--changeSet 0utl_pb_message_baseliner:54 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL','BUS-00513','BUS','Rule Checker', 'There are no broken rules within the database.','Exclamtion', 'OK', 1, 1, 'N','N',0);

--changeSet 0utl_pb_message_baseliner:55 stripComments:false
insert into utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint,msguserinput, utl_id)
values ('BL','BUS-00434','BUS','No IETM defined', 'Cannot launch the IETM as there is no IETM defined.','Stopsign', 'OK', 1, 1, 'N','N',0);

--changeSet 0utl_pb_message_baseliner:56 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00518', 'BUS', 'Part Assignment Error', 'BOM Parts and Parts must both have the same Inventory Class.', 'StopSign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:57 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00520', 'BUS', 'BOM Part Error', 'The BOM Part(s) %s is either tracked, an assembly or an aircraft and therefore must have a quantity of 1.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:58 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00526', 'BUS', 'Delete Error', 'The selected BOM is assigned to a Task and cannot be deleted.', 'Stopsign', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:59 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00532', 'BUS', 'Open Job Card Error', 'There was an error in obtaining the selected Job Card and it cannot be displayed.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:60 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00533', 'BUS', 'Open Job Card Error', 'There was an error in generating the selected Job Card and it cannot be displayed.', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);

--changeSet 0utl_pb_message_baseliner:61 stripComments:false
INSERT INTO utl_pb_message (app_cd, msgid, msg_type_cd, msgtitle, msgtext, msgicon, msgbutton, msgdefaultbutton, msgseverity, msgprint, MSGUSERINPUT, utl_id)
VALUES ('BL', 'BUS-00538', 'BUS', 'Delete Error', 'The Fault Definition cannot be deleted because it is associated with a Task Definition', 'Exclamation', 'OK', 1, 1, 'N', 'N',0);