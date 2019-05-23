--liquibase formatted sql

--changeSet OPER-30456:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--mark the record as historic in INV_LOC_PART_COUNT table when no bin level exists and no inventory exist for the part and bin location combination
BEGIN
    UPDATE inv_loc_part_count
    SET hist_bool = 1
    WHERE ROWID
    IN (
         SELECT             
             ROWID AS rid
          FROM
             inv_loc_part_count    
          WHERE
             inv_loc_part_count.hist_bool = 0 
             AND
             -- no bin levels exist for part and bin location
             NOT EXISTS (
                          SELECT 1
                          FROM
                             inv_loc_bin
                          WHERE
                             inv_loc_bin.loc_db_id     = inv_loc_part_count.loc_db_id AND
                             inv_loc_bin.loc_id        = inv_loc_part_count.loc_id AND
                             inv_loc_bin.part_no_db_id = inv_loc_part_count.part_no_db_id AND
                             inv_loc_bin.part_no_id    = inv_loc_part_count.part_no_id
            )
            AND
            -- no inventory exists for part at the bin location
            NOT EXISTS (
                          SELECT 1
                          FROM
                             inv_inv
                          WHERE
                             inv_inv.loc_db_id     = inv_loc_part_count.loc_db_id AND
                             inv_inv.loc_id        = inv_loc_part_count.loc_id AND
                             inv_inv.part_no_db_id = inv_loc_part_count.part_no_db_id AND
                             inv_inv.part_no_id    = inv_loc_part_count.part_no_id
           )
           
        );
                
    COMMIT;
END;
/