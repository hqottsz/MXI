--liquibase formatted sql

--changeSet OPER-13105:1 stripComments:false 
UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to delete alerts.'
  WHERE
      parm_name = 'ACTION_DELETE_ALERT'; 

--changeSet OPER-13105:2 stripComments:false 
UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to see the search button that''s required to search for transfers.'
    
  WHERE
      parm_name = 'ACTION_SEARCH_TRANSFER'; 
      
--changeSet OPER-13105:3 stripComments:false  
UPDATE
     utl_action_config_parm
     SET
     parm_desc = 'Permission to see the search button that''s required to search for tasks.'
  WHERE
     parm_name = 'ACTION_SEARCH_TASK'; 
     
     
--changeSet OPER-13105:4 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for parts.'
WHERE
     parm_name = 'ACTION_SEARCH_PART'; 
     

--changeSet OPER-13105:5 stripComments:false    
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for vendors.'
WHERE
     parm_name = 'ACTION_SEARCH_VENDOR'; 
     
     
--changeSet OPER-13105:6 stripComments:false  
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for shipments.'
WHERE
     parm_name = 'ACTION_SEARCH_SHIPMENT'; 
     
  
--changeSet OPER-13105:7 stripComments:false 
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for stock.'
WHERE
     parm_name = 'ACTION_SEARCH_STOCK'; 
     
     
 --changeSet OPER-13105:8 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for roles.'
WHERE
     parm_name = 'ACTION_SEARCH_ROLE'; 


--changeSet OPER-13105:9 stripComments:false    
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for users.'
WHERE
     parm_name = 'ACTION_SEARCH_USER'; 
     
     
--changeSet OPER-13105:10 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for part requests.'
WHERE
     parm_name = 'ACTION_SEARCH_PART_REQ'; 
   
   
--changeSet OPER-13105:11 stripComments:false          
 UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to see the search button that''s required to search for open part requests.'
  WHERE
      parm_name = 'ACTION_SEARCH_UNRSRV_RSRV'; 
  
  
--changeSet OPER-13105:12 stripComments:false 
UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to see the search button that''s required to search for purchase orders.'
    
  WHERE
      parm_name = 'ACTION_SEARCH_PO'; 
   
   
 --changeSet OPER-13105:13 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for technical references.'
WHERE
     parm_name = 'ACTION_SEARCH_TECHNICAL_REFERENCE'; 
     
     
--changeSet OPER-13105:14 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the button that''s required to search for task definitions.'
WHERE
     parm_name = 'ACTION_SEARCH_TASK_DEFINITION'; 
     
    
--changeSet OPER-13105:15 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the search button that''s required to search for requirements.'
WHERE
     parm_name = 'ACTION_SEARCH_REQUIRIMENT'; 
     
     
--changeSet OPER-13105:16 stripComments:false   
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the Complete Transfer button that''s required to complete transfers.'
WHERE
     parm_name = 'ACTION_COMPLETE_TRANSFER'; 
   
   
--changeSet OPER-13105:17 stripComments:false   
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to search last done for block, executable requirement and requirement.'
WHERE
     parm_name = 'API_TASK_REQUEST'; 
     
     
 --changeSet OPER-13105:18 stripComments:false    
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to change the authorities that are assigned to inventory items.'
WHERE
     parm_name = 'ACTION_CHANGE_AUTHORITY'; 
     

--changeSet OPER-13105:19 stripComments:false     
    
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to allow an aircraft to be scheduled by Line Planning Automation.'
WHERE
     parm_name = 'ACTION_ALLOW_LPA_AIRCRAFT'; 
     

--changeSet OPER-13105:20 stripComments:false     
     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the Set Preferred Vendor button that''s required to set preferred vendors for part purchase orders. There are separate permissions for setting preferred part exchange vendors (ACTION_SET_AS_PREFERRED_VENDOR) and preferred repair vendors (ACTION_SET_PREF_PART_REPAIR_VENDOR).'
WHERE
     parm_name = 'ACTION_SET_PREF_PURCHASE_VENDOR'; 
   

--changeSet OPER-13105:21 stripComments:false 
   
 UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to see the Set Preferred Vendor button that''s required to set preferred vendors for part repair orders.  There are separate permissions for setting preferred part exchange vendors (ACTION_SET_AS_PREFERRED_VENDOR) and preferred purchase vendors (ACTION_SET_PREF_PURCHASE_VENDOR.'
WHERE
     parm_name = 'ACTION_SET_PREF_PART_REPAIR_VENDOR'; 
    
 
--changeSet OPER-13105:22 stripComments:false  
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to uncomplete tasks in a PDA (Personal Digital or Data Assistant device) that are not yet uploaded to Maintenix.'
WHERE
     parm_name = 'ACTION_PDA_UNCOMPLETE_TASK'; 
     
     
--changeSet OPER-13105:23 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to upload tasks and faults from a PDA (Personal Digital or Data Assistant device) to Maintenix.'
WHERE
     parm_name = 'ACTION_PDA_UPLOAD'; 
   
   
 --changeSet OPER-13105:24 stripComments:false   
 UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to see the Mark Airport as Default icon that''s required to select one of multiple airports that are assigned to vendors, as the default airport.'
  WHERE
      parm_name = 'ACTION_MARK_VENDOR_AIRPORT_AS_DEFAULT'; 
      
      
 --changeSet OPER-13105:25 stripComments:false 
 UPDATE
     utl_action_config_parm
    SET
      parm_desc = 'Permission to perform the corrections when viewing recount results of a count discrepancy.'
    
  WHERE
      parm_name = 'ACTION_PERFORM_CORRECTIONS'; 
      
      
--changeSet OPER-13105:26 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to assign licenses to users. The ACTION_ASSIGN_USER_LICENSE permission is also required to see the Assign Licenses icon.'
WHERE
     parm_name = 'ACTION_ASSIGN_LICENSE'; 
     
     
--changeSet OPER-13105:27 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to generate user certificates for use when recording electronic signatures.'
WHERE
     parm_name = 'ACTION_GENERATE_CERTIFICATE'; 
    
    
--changeSet OPER-13105:28 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to view the entire linked chain of a task definition.'
WHERE
     parm_name = 'ACTION_VIEW_TASK_HIERARCHY'; 
     
     
--changeSet OPER-13105:29 stripComments:false      
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to access the button to add advisories to requirements.'
WHERE
     parm_name = 'ACTION_ADD_ADVISORY'; 
 
 
--changeSet OPER-13105:30 stripComments:false 
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to access the Integration Message page.'
WHERE
     parm_name = 'ACTION_JSP_COMMON_INTEGRATION_VIEW_RESPONSE'; 
     
     
--changeSet OPER-13105:31 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to access the Create Order Invoice and Edit Order Invoice pages.'
WHERE
     parm_name = 'ACTION_JSP_WEB_PI_CREATE_EDIT_POINVOICE'; 
 
 
--changeSet OPER-13105:32 stripComments:false 
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to invoke the EditSymbols servlet. This servlet controls access to the Symbols required to create a Calculated Usage Parameter.'
WHERE
     parm_name = 'ACTION_CALC_EDIT_SYMBOLS'; 
     
     
--changeSet OPER-13105:33 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to invoke the DAO.Query servlet. This generic servlet performs SQL queries and should be enabled for all roles.'
WHERE
     parm_name = 'ACTION_DAO_QUERY'; 
   

--changeSet OPER-13105:34 stripComments:false  
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to invoke the Invoicing.RemoveEstimateCostLineItem servlet. This servlet is used to remove the Cost Line Item from the work package.'
WHERE
     parm_name = 'ACTION_INVOICING_REMOVE_ESTIMATE_COST_LINE_ITEM'; 
     
    
--changeSet OPER-13105:35 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to invoke the jasper.ExportReport servlet. This servlet handles exporting of reports to a non-HTML format such as PDF, Excel, Word. Permissions for the individual reports are required.'
     
WHERE
     parm_name = 'ACTION_JASPER_EXPORT_REPORT'; 
     
     
     
--changeSet OPER-13105:36 stripComments:false     
UPDATE
     utl_action_config_parm
SET
     parm_desc = 'Permission to invoke the RevertToPreviousRevision servlet. This servlet is used in the workflow of Maintenance Programs.'
WHERE
     parm_name = 'ACTION_MAINT_REVERT_TO_PREVIOUS_REVISION'; 
         
   