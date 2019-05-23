--liquibase formatted sql

--changeSet OPER-28017:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIUBR_EXT_REF_ITEM" BEFORE INSERT OR UPDATE
   ON "EXT_REF_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  -- force the reference item name to uppercase 
  :new.reference_item_name := UPPER(:new.reference_item_name);
END;
/