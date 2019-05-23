--liquibase formatted sql

--changeSet OPER-14748:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
       livingCount NUMBER;   
BEGIN
    --Clear all duplicate external references for flights.
    FOR lc_duplicate_keys IN (SELECT ext_key, total
                                  FROM ( SELECT COUNT(1) total, ext_key 
                                         FROM fl_leg 
                                         WHERE EXT_KEY IS NOT NULL GROUP BY fl_leg.ext_key
                                       ) 
                                 WHERE total > 1) LOOP
    
       SELECT COUNT(1) INTO livingCount 
       FROM fl_leg 
       WHERE EXT_KEY = lc_duplicate_keys.ext_key AND hist_bool=0;
       --If there is a living flight apart of the duplicates, clear all historic ones
       IF (livingCount > 0) THEN
          UPDATE fl_leg 
          SET ext_key = NULL
          WHERE ext_key = lc_duplicate_keys.ext_key AND 
                hist_bool=1;
       --Otherwise, we will clear the 'older' ones based on leg_id
       ELSE
          UPDATE fl_leg 
          SET ext_key = NULL
          WHERE ext_key = lc_duplicate_keys.ext_key AND 
                fl_leg.leg_id < (SELECT max(leg_id) 
                             FROM fl_leg WHERE ext_key = lc_duplicate_keys.ext_key);
       END IF;
       
    END LOOP;

   -- Add a new Unique constraint to ext_key in the fl_leg table
   upg_migr_schema_v1_pkg.table_constraint_add('ALTER TABLE fl_leg ADD CONSTRAINT IX_FLLEGEXTKEY_UNQ UNIQUE (EXT_KEY)');
END;
/