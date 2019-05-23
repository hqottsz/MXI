INSERT
INTO
   stock_dist_req
   (
      stock_dist_req_id,
      stock_dist_req_db_id,
      request_id,
      stock_no_db_id,
      stock_no_id,
      status_cd,
      status_db_id,
      needed_qty,
      needed_loc_db_id,
      needed_loc_id, 
      supplier_loc_db_id,
      supplier_loc_id,
      qty_unit_db_id,
      qty_unit_cd
   )
SELECT
   9999,
   4650,
   'SR99Z999999Z',
   4650,
   (select stock_no_id from eqp_stock_no where eqp_stock_no.stock_no_cd = 'Warehouse_SER1_STOCK1'),
   'OPEN',
   0,
   1,
   4650,
   (select loc_id from inv_loc where inv_loc.loc_cd = 'SUPPLY2/STORE1'),
   4650,
   (select loc_id from inv_loc where inv_loc.loc_cd = 'SUPPLY2/STORE2'),
   0,
   'EA'
FROM
   dual
WHERE
   NOT EXISTS (SELECT 1 FROM stock_dist_req WHERE stock_dist_req_id = 9999 AND request_id = 'SR99Z999999Z');