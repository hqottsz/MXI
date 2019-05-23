--liquibase formatted sql


--changeSet QC-6832:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_INV_POS_DESC_QUEUE_DEL" BEFORE DELETE
   ON "INV_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  -- remove any row in the INV_POS_DESC_DEL before deleting from INV_INV.
  DELETE FROM INV_POS_DESC_QUEUE WHERE :OLD.inv_no_db_id=INV_POS_DESC_QUEUE.inv_no_db_id AND :OLD.inv_no_id = INV_POS_DESC_QUEUE.inv_no_id;

  EXCEPTION
      WHEN OTHERS THEN
           APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'TDBR_INV_POS_DESC_QUEUE_DEL:' || SQLERRM);
           RAISE;
end;
/