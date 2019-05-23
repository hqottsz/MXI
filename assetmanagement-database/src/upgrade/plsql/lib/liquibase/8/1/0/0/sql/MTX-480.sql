--liquibase formatted sql


--changeSet MTX-480:1 stripComments:false
--
-- Remove entry from utl_work_item_type
--
-- MTX-480
-- Remove duplicate logic for sending update part request message
-- 
DELETE FROM
   utl_work_item
WHERE
   utl_work_item.type = 'REQ_PART_UPDATE_QUEUE';

--changeSet MTX-480:2 stripComments:false
DELETE FROM
   utl_work_item_type
WHERE 
   name = 'REQ_PART_UPDATE_QUEUE';   

--changeSet MTX-480:3 stripComments:false
DELETE FROM
   utl_trigger
WHERE 
   trigger_cd = 'MX_QUEUE_REQ_UPDATE' AND
   class_name = 'TRG_MX_QUEUE_REQ_UPDATE';   

--changeSet MTX-480:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PROCEDURE_DROP('TRG_MX_QUEUE_REQ_UPDATE');
END;
/