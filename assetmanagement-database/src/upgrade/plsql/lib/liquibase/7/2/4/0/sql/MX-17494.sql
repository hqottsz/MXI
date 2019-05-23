--liquibase formatted sql


--changeSet MX-17494:1 stripComments:false
-- Remove the EditLabourRequirement Menu Item and related FKs
DELETE utl_menu_group_item WHERE menu_id = 10071;

--changeSet MX-17494:2 stripComments:false
DELETE utl_menu_item WHERE menu_id = 10071;

--changeSet MX-17494:3 stripComments:false
-- Remove the EditLabourRequirement Menu Item and related FKs
UPDATE utl_role_parm
SET parm_value = '/maintenix/common/ToDoList.jsp'
WHERE parm_value = '/maintenix/web/labour/EditLabourRequirements.jsp';