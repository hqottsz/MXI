-- Update the current usage for specified inventory
UPDATE inv_curr_usage
SET tsn_qt = 1000
WHERE
   (inv_no_db_id, inv_no_id) = (SELECT inv_no_db_id,
                                       inv_no_id 
                                FROM inv_inv 
                                WHERE serial_no_oem = 'CUR-001')      AND
   (data_type_db_id, data_type_id) = (SELECT data_type_db_id,
                                             data_type_id
                                      FROM mim_data_type
                                      WHERE data_type_cd = 'APUH_AT_READING');