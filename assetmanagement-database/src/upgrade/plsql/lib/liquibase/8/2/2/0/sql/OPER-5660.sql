--liquibase formatted sql


--changeSet OPER-5660:1 stripComments:false
-- When a stock level has stock low action of SHIPREQ, the reordershipping quantity field is now required
-- Using the default shipping quantity from the stock as the reordershipping quantity of the stock level 
UPDATE
   inv_loc_stock
SET
   inv_loc_stock.batch_size =
   (SELECT
      eqp_stock_no.ship_qt
    FROM
      eqp_stock_no
    WHERE
      eqp_stock_no.stock_no_db_id  = inv_loc_stock.stock_no_db_id AND
      eqp_stock_no.stock_no_id     = inv_loc_stock.stock_no_id
   )
WHERE
   inv_loc_stock.stock_low_actn_db_id  = 0 AND
   inv_loc_stock.stock_low_actn_cd     = 'SHIPREQ'
   AND
   (
      inv_loc_stock.batch_size IS NULL OR
      inv_loc_stock.batch_size <= 0
   );