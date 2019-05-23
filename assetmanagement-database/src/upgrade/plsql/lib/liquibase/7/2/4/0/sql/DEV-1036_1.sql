--liquibase formatted sql


--changeSet DEV-1036_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove all Vendor Owned Stock Levels with parts of financial type EXPENSE
DECLARE
    
    ln_Count number;
    
    CURSOR lcur_VendorOwnedStockLevel IS
             
      -- Retrieve all vendor owned stock levels from the stock that has assigned part with financial type of EXPENSE
      SELECT DISTINCT
          inv_loc_stock.loc_db_id,
          inv_loc_stock.loc_id,
          inv_loc_stock.stock_no_db_id,
          inv_loc_stock.stock_no_id,
          inv_loc_stock.owner_db_id,
          inv_loc_stock.owner_id,
          eqp_stock_no.stock_no_cd || '( ' || eqp_stock_no.stock_no_name || ')' AS stock_cd_name
      FROM
          eqp_stock_no 
          INNER JOIN inv_loc_stock ON
                inv_loc_stock.stock_no_db_id = eqp_stock_no.stock_no_db_id AND
                inv_loc_stock.stock_no_id    = eqp_stock_no.stock_no_id
          INNER JOIN inv_owner ON
                inv_owner.owner_db_id = inv_loc_stock.owner_db_id AND
                inv_owner.owner_id    = inv_loc_stock.owner_id
      WHERE
          inv_owner.local_bool = 0
          AND
          EXISTS
          (
                SELECT 1
                FROM
                    eqp_part_no
                    INNER JOIN ref_financial_class ON
                          ref_financial_class.financial_class_db_id = eqp_part_no.financial_class_db_id AND
                          ref_financial_class.financial_class_cd    = eqp_part_no.financial_class_cd
                WHERE
                     eqp_part_no.stock_no_db_id = eqp_stock_no.stock_no_db_id AND
                     eqp_part_no.stock_no_id    = eqp_stock_no.stock_no_id
                     AND
                     ref_financial_class.finance_type_cd = 'EXPENSE'
                     
          );
           
    lrec_VendorOwnedStockLevel lcur_VendorOwnedStockLevel%ROWTYPE;       
             
BEGIN
     
    ln_Count := 0;
    
    -- Loop through all vendor owned stock levels
    FOR lrec_VendorOwnedStockLevel IN lcur_VendorOwnedStockLevel LOOP       
          
        DELETE 
              inv_loc_stock  
        WHERE 
              inv_loc_stock.stock_no_db_id = lrec_VendorOwnedStockLevel.stock_no_db_id AND
              inv_loc_stock.stock_no_id    = lrec_VendorOwnedStockLevel.stock_no_id
              AND
              inv_loc_stock.loc_db_id      = lrec_VendorOwnedStockLevel.loc_db_id AND
              inv_loc_stock.loc_id         = lrec_VendorOwnedStockLevel.loc_id
              AND
              inv_loc_stock.owner_db_id    = lrec_VendorOwnedStockLevel.owner_db_id AND
              inv_loc_stock.owner_id       = lrec_VendorOwnedStockLevel.owner_id;
  
         ln_Count := ln_Count + 1;
    END LOOP;    
    
    dbms_output.put_line( 'Number of records deleted:' || ln_Count  ); 

END;
/