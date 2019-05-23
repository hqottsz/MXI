--liquibase formatted sql


--changeSet MX-2671:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- All values will be incremented by 1 if zero value is found for part group
DECLARE

   CURSOR lcur_FindZeroInterchgOrd IS
   SELECT DISTINCT
      bom_part_db_id,
      bom_part_id      
   FROM
      eqp_part_baseline
   WHERE 
      interchg_ord = 0;
      
   lrec_FindZeroInterchgOrd lcur_FindZeroInterchgOrd%ROWTYPE;
   
BEGIN
   FOR lrec_FindZeroInterchgOrd IN lcur_FindZeroInterchgOrd()
   LOOP
      UPDATE eqp_part_baseline
      SET
         interchg_ord = interchg_ord + 1
      WHERE 
         bom_part_db_id = lrec_FindZeroInterchgOrd.bom_part_db_id AND
         bom_part_id    = lrec_FindZeroInterchgOrd.bom_part_id;
   END LOOP;
END;
/   

--changeSet MX-2671:2 stripComments:false
-- Update null interchangeability order to 1
UPDATE eqp_part_baseline 
SET    interchg_ord = 1 
WHERE  interchg_ord IS NULL;     

--changeSet MX-2671:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PART_BASELINE modify (
   INTERCHG_ORD Number(4,0) DEFAULT 1 NOT NULL DEFERRABLE  Check (INTERCHG_ORD > 0 ) DEFERRABLE
)
');
END;
/       