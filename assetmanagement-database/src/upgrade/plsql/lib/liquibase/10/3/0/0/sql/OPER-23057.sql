--liquibase formatted sql

--changeSet OPER-23057:1 stripComments:false
UPDATE
   utl_menu_item
SET
   menu_name = 'web.menuitem.IFS_WEBSITE',
   menu_link_url = 'http://www.ifsworld.com'
WHERE
   menu_name = 'web.menuitem.MXI_WEBSITE';