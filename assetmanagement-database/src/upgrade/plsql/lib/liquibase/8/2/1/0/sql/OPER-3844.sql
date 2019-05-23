--liquibase formatted sql


--changeSet OPER-3844:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- modify the column PO_REVISION_NO and change the default value from 1 to 0
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE po_header MODIFY PO_REVISION_NO NUMBER DEFAULT 0
');
END;
/

--changeSet OPER-3844:2 stripComments:false
-- Add the comment on the column   
COMMENT ON COLUMN PO_HEADER.PO_REVISION_NO
IS
  'Indicates the issue number of the Order. When order is created and not issued, the initial value is 0.'; 

--changeSet OPER-3844:3 stripComments:false
ALTER TRIGGER TUBR_PO_HEADER DISABLE;

--changeSet OPER-3844:4 stripComments:false
-- update the PO_REVISION_NO value to 1 where Order is not issued yet.
UPDATE
   po_header
SET
   po_revision_no = 0
WHERE
   po_revision_no = 1 
   AND 
   issued_dt IS NULL;

--changeSet OPER-3844:5 stripComments:false
ALTER TRIGGER TUBR_PO_HEADER ENABLE;   