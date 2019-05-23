--liquibase formatted sql


--changeSet QC-6014:1 stripComments:false
UPDATE utl_menu_item
SET menu_link_url = '/maintenix/'||substr(menu_link_url, instr(menu_link_url, '/servlet/', -1) + 1)
WHERE menu_link_url like '/mxreport/servlet/%';