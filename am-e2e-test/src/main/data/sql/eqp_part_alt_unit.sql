DECLARE
   PART_DB_ID eqp_part_baseline.part_no_db_id%TYPE;
   PART_ID eqp_part_baseline.part_no_id%TYPE;
BEGIN
-- get the part_db_id and part_no_id based upon the part number and manufacturer unique combination...
-- ...since the part may have been given any part_db_id and part_id based upon when the respective configurator csv files were ran
   SELECT
      eqp_part_baseline.part_no_db_id, eqp_part_baseline.part_no_id INTO  PART_DB_ID, PART_ID
   FROM
      eqp_bom_part,
      eqp_part_baseline
   WHERE
      eqp_bom_part.bom_part_cd = 'S000001 - 1234567890' AND
      eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      eqp_part_baseline.bom_part_id = eqp_bom_part.bom_part_id; 

-- insert a new alternate unit of measure for the specified part number and manufacturer unique combination (if it does not already exist)
   INSERT INTO 
      eqp_part_alt_unit (part_no_db_id, part_no_id, qty_unit_db_id, qty_unit_cd, qty_convert_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
   SELECT
      PART_DB_ID, PART_ID, PART_DB_ID,'BOX_5DEC', 6, 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM 
      dual
   WHERE NOT EXISTS
      ( SELECT 
           1 
        FROM 
           eqp_part_alt_unit 
        WHERE 
           part_no_id = PART_ID
        AND 
        qty_unit_cd = 'BOX_5DEC'
      );
END;
/