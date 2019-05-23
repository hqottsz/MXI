-- create a new alternate unit of measure (if it does not already exist)
INSERT INTO 
   ref_qty_unit (qty_unit_db_id, qty_unit_cd, desc_sdesc, decimal_places_qt, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   4650, 'BOX_5DEC','BOX_5DEC', 5, 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
   dual
WHERE NOT EXISTS
   ( SELECT 
        1 
     FROM 
        ref_qty_unit 
     WHERE 
        qty_unit_cd = 'BOX_5DEC'
     AND 
        desc_sdesc = 'BOX_5DEC'
   )
;