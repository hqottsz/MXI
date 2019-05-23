--liquibase formatted sql


--changeSet 0utl_pb_message_mxfc:1 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'DEV-99999',
   'DEV',
   'Development Error',
   'A development error has occurred, please contact Mxi Technical Support.
Object: %s
Function: %s
Error: %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:2 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00001',
   'PFF',
   'Mail Recipient Required',
   'At least one mail recipient is required for the mail UTL_MESSAGE.
Please select a recipient from your Address Book.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:3 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00002',
   'PFF',
   'Mail Information Missing',
   'The %s is missing from the mail UTL_MESSAGE.
Please enter a value',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:4 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00003',
   'PFF',
   'Mail Attachment Create Error',
   'Unable to create the file attachment for the mail UTL_MESSAGE.
Please verify that the TEMP directory is valid for your user configuration.
The mail UTL_MESSAGE will not be sent',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:5 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00004',
   'PFF',
   'Mail Send Successful',
   'The mail UTL_MESSAGE has been successfully sent.',
   'Information',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:6 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00005',
   'PFF',
   'Cascading Delete',
   'The table %s is missing the field %s',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:7 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00006',
   'PFF',
   'Deletion Error',
   'Could not delete the reference term because it is being used.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:8 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00007',
   'PFF',
   'Save error',
   'Could not save because mandatory fields are not filled in.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:9 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00008',
   'PFF',
   'Save Error',
   'Could not save because the reference term code "%s" already exists in the database.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:10 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00009',
   'PFF',
   'Must Save Changes',
   'You must save any changes before selecting a new reference term. Press Apply and try again.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:11 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00010',
   'PFF',
   'Row Selection Error',
   'Can only select ''%s'' rows from the search output datawindow.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:12 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00011',
   'PFF',
   'Properties Error',
   'You cannot modify the selected item because it is a "Shadow Entry". The actual properties of this item exist in a different database.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:13 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00012',
   'PFF',
   'Development error',
   'An invalid menu item name was passed into inv_EnableAction.of_RegisterMenuItem()',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:14 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00013',
   'PFF',
   'Database Error',
   'Unable to successfully delete the selected  item.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:15 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00014',
   'PFF',
   'Retrieval Error',
   'Error in retrieving contents of detail datastore ''%s'' for the action service.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:16 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00015',
   'PFF',
   'Error',
   'The selected or created item cannot be applied to the field.   ',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:17 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00016',
   'PFF',
   'Utility Error',
   'The row could not be found. Search Discontinued.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:18 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00017',
   'PFF',
   'Validation Error',
   'The following mandatory column(s) is missing: %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:19 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00018',
   'PFF',
   'Utility Error',
   'Database information collected does not match treeview hierarchy.
Search Discontinued!',
   'StopSign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:20 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00020',
   'PFF',
   'Delete Confirmation',
   'Are you sure that you want to delete the selected %s?',
   'Question',
   'YesNo',
   2,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:21 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00021',
   'PFF',
   'Assignment Error',
   'The assign_name ''%s'' could not be found in the utl_assignment table.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:22 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00022',
   'PFF',
   'Utility Error',
   'The report_name ''%s'' could not be found in the utl_report table.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:23 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00023',
   'PFF',
   'String Error',
   'The as_ColNames[] and as_ColValues[] arrays are of different lengths in the function n_cst_string.of_CreateFindExpr()',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:24 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00024',
   'PFF',
   'Utility Error',
   'The number of elements in il_ListCols[] does not match the number of elements in il_AssignCols[] in w_r_assignment.pff_PostOpen()',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:25 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00025',
   'PFF',
   'Development',
   'Unknown data type (%s) in %s',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:26 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00026',
   'PFF',
   'Selection Error',
   'You may not perform this action while more than one row is selected. Select a single row, and try again.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:27 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00027',
   'PFF',
   'Authorization Not Approved',
   'The selected Human Resource can not authorize this change.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:28 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00028',
   'PFF',
   'Invalid Password',
   'The password entered is invalid.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:29 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00029',
   'PFF',
   'Retrieval Error',
   'Error in retrieving contents of datawindow (%s.)',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:30 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00030',
   'PFF',
   'Retrieval Error',
   'There is no code to set the as_action to another value',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:31 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00031',
   'PFF',
   'Search Error',
   'Search values do not exist.  Please re-enter the search values.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:32 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00032',
   'PFF',
   'Search Utility',
   'The following datawindow object could not be found. %s.        ',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:33 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00033',
   'PFF',
   'Invalid License',
   'This license code is invalid.  Please try again.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:34 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00034',
   'PFF',
   'Expired License',
   'This license has expired.  Please contact Mxi Technologies to renew the license.
Thank you.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:35 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00035',
   'PFF',
   'License Renewal Warning',
   'This license has expired.   Please contact Mxi Technologies to renew the license within the next %s days.
Thank you.',
   'Information',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:36 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00036',
   'PFF',
   'License Decode Error',
   'An error occurred while attempting to decode the license code.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:37 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00037',
   'PFF',
   'License Encode Error',
   'An error occurred while attempting to encode the license code.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:38 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00038',
   'PFF',
   'Retrieve',
   '0 Rows Retrieved',
   'Information',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:39 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00039',
   'PFF',
   'Retrieve',
   'You have hit the retrieve limit of %s rows.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:40 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00040',
   'PFF',
   'Error',
   'A database error has occurred.  Please re-try your last action.  If the problem persists contact your System Administrator.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:41 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00041',
   'PFF',
   'Development',
   'The PLUO datastore %s already exists.',
   'Exclamation',
   'Ok',
   1,
   0,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:42 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00043',
   'PFF',
   'Nested Reports',
   'Nested Reports can not be print previewed.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:43 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00044',
   'PFF',
   'No System Printer',
   'Unable to detect a system printer.  Please confirm system settings.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:44 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00045',
   'PFF',
   'Cancel',
   'Are you sure you want to Cancel without saving changes?',
   'Question',
   'YesNo',
   2,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:45 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00046',
   'PFF',
   'Soft Delete',
   'This reference term ''%s'' is being used.  Do you wish to Soft Delete it?',
   'Question',
   'YesNo',
   2,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:46 stripComments:false
/* Modified for V4x */
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00047',
   'PFF',
   'Development',
   'The %s libraries could not be found.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:47 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00048',
   'PFF',
   'Error',
   'Please select an item from the list.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:48 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00049',
   'PFF',
   'System Mail Error',
   'The following mail system error occurred:
      %s
Check system settings or contact administrator.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:49 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00050',
   'PFF',
   'Mail Recipient Error',
   'Unable to resolve recipient:
   %s
Please re-enter mail recipient information',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:50 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00051',
   'PFF',
   'Refresh Treeview',
   'Please press the "Refresh" button on the main toolbar to update the inventory tree view.',
   'Information',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:51 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00052',
   'PFF',
   'Mandatory Column',
   'The Datastore %s is missing a value in column %s at row %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:52 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00054',
   'PFF',
   'Component Error',
   'The Datastore %s does not exist.',
   'Stopsign',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:53 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00063',
   'PFF',
   'Jaguar Error',
   'Distributed service error on Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:54 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00064',
   'PFF',
   'Jaguar Error',
   'Distributed communications error on Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:55 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00065',
   'PFF',
   'Jaguar Error',
   'Requested server not active. Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:56 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00066',
   'PFF',
   'Jaguar Error',
   'Server not accepting requests. Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:57 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00067',
   'PFF',
   'Jaguar Error',
   'Request terminated abnormally on Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:58 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00068',
   'PFF',
   'Jaguar Error',
   'Response to request incomplete on Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:59 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00069',
   'PFF',
   'Jaguar Error',
   'Not connected to Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:60 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00070',
   'PFF',
   'Jaguar Error',
   'Server busy. Server %s, Port %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:61 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00071',
   'PFF',
   'Jaguar Error',
   'Unknown Jaguar Error. Code = %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:62 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00073',
   'PFF',
   'Mandatory Positive columns',
   'The Datastore %s has a negative value in column %s at row %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:63 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00074',
   'PFF',
   'Validation Error',
   'The following columns must be positive: %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:64 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'pfc_closequery_failsvalidation',
   'PFC',
   'Application',
   'The information entered does not pass validation and must be corrected before changes can be saved.~r~n~r~nClose without saving changes?',
   'Exclamation',
   'YesNo',
   2,
   5,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:65 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'pfc_closequery_savechanges',
   'PFC',
   'Application',
   'Do you want to save changes?',
   'Exclamation',
   'YesNoCancel',
   1,
   0,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:66 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'pfc_dwdberror',
   'PFC',
   'Application',
   '%s',
   'StopSign',
   'Ok',
   1,
   20,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:67 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'pfc_requiredmissing',
   'PFC',
   'Application',
   'Required value missing for %s on row %s.  Please enter a value.',
   'Information',
   'Ok',
   1,
   5,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:68 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'pfc_systemerror',
   'PFC',
   'System Error',
   '%s',
   'StopSign',
   'Ok',
   1,
   20,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:69 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00075',
   'PFF',
   'Access Denied',
   'You do not have authority over the selected %s.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:70 stripComments:false
   insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
   VALUES (
      'MXFC',
      'PFF-00076',
      'PFF',
      'Error',
      'Please select a filter to delete.',
      'Stopsign',
      'OK',
      1,
      1,
      'N',
      'N',0);

--changeSet 0utl_pb_message_mxfc:71 stripComments:false
   insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
   VALUES (
      'MXFC',
      'PFF-00077',
      'PFF',
      'Delete Confirmation',
      'Are you sure you want to delete the selected filter?',
      'Question',
      'YesNo',
      1,
      1,
      'N',
      'N',0);

--changeSet 0utl_pb_message_mxfc:72 stripComments:false
   insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
    VALUES (
      'MXFC',
      'PFF-00078',
      'PFF',
      'Error',
      'Unable to succesfully delete the selected filter.',
      'Stopsign',
      'OK',
      1,
      1,
      'N',
      'N',0);

--changeSet 0utl_pb_message_mxfc:73 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00079',
   'PFF',
   'Validation Error',
   'The following string is too long: attribute=%s, max. length=%s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'Y',0);

--changeSet 0utl_pb_message_mxfc:74 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00080',
   'PFF',
   'Validation Error',
   'The following refterm does not exist, and is therefore invalid: %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:75 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00081',
   'PFF',
   'Validation Error',
   'The following key does not exist, and is therefore invalid: %s',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:76 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00082',
   'PFF',
   'Historic Event',
   'This event is historic and cannot be modified.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);

--changeSet 0utl_pb_message_mxfc:77 stripComments:false
insert into UTL_PB_MESSAGE (APP_CD, MSGID, MSG_TYPE_CD, MSGTITLE, MSGTEXT, MSGICON, MSGBUTTON, MSGDEFAULTBUTTON, MSGSEVERITY, MSGPRINT, MSGUSERINPUT, utl_id)
VALUES (
   'MXFC',
   'PFF-00083',
   'PFF',
   'Historic Event',
   'This event is historic and cannot be removed.',
   'Exclamation',
   'OK',
   1,
   1,
   'N',
   'N',0);