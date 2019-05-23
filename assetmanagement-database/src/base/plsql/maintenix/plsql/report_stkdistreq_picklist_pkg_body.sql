CREATE OR REPLACE PACKAGE BODY report_stkdistreq_picklist_pkg AS

   /************************************************************************
   ** Date:        October 23, 2018
   ** Author:      Libin Cai
   *************************************************************************
   **
   ** Confidential, proprietary and/or trade secret information of
   ** Mxi Technologies.
   **
   ** Copyright 2005-2018 Mxi Technologies. All Rights Reserved.
   **
   ** Except as expressly provided by written license signed by a duly appointed
   ** officer of Mxi Technologies. any disclosure, distribution,
   ** reproduction, compilation, modification, creation of derivative works and/or
   ** other use of the Mxi source code is strictly prohibited.  Inclusion of a
   ** copyright notice shall not be taken to indicate that the source code has
   ** been published.
   ***************************************************************************/
   
   gk_row_limit              CONSTANT PLS_INTEGER := 100;
 
   -----------------------------------------------------------------------------------------------------------
   -- Description  : Get the stock dist request id ordered by bin location
   -- Return       : DistReqTable (Type Table) - collection of data
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetDistReq (
      aReqKeys IN VARCHAR2
   ) RETURN DistReqTable
   PIPELINED
   IS
      CURSOR lcur_dist_req IS
      WITH dist_req AS
      (
         SELECT
            stock_dist_req.stock_dist_req_db_id,
            stock_dist_req.stock_dist_req_id,
            stock_dist_req.stock_no_db_id,
            stock_dist_req.stock_no_id,
            stock_dist_req.request_id,
            stock_dist_req.needed_qty,
            stock_dist_req.supplier_loc_db_id,
            stock_dist_req.supplier_loc_id,
            stock_dist_req.owner_db_id,
            stock_dist_req.owner_id,
            needed_loc.loc_cd AS needed_loc_cd,
            sup_loc.loc_cd    AS sup_loc_cd,
            inv_owner.owner_cd
         FROM
            stock_dist_req 
            INNER JOIN inv_loc needed_loc ON
               stock_dist_req.needed_loc_db_id = needed_loc.loc_db_id AND
               stock_dist_req.needed_loc_id    = needed_loc.loc_id
            INNER JOIN inv_loc sup_loc ON
               stock_dist_req.supplier_loc_db_id = sup_loc.loc_db_id AND
               stock_dist_req.supplier_loc_id    = sup_loc.loc_id 
            LEFT JOIN inv_owner ON
               stock_dist_req.owner_db_id = inv_owner.owner_db_id AND
               stock_dist_req.owner_id    = inv_owner.owner_id                          
         WHERE
            stock_dist_req.stock_dist_req_id || ':' || stock_dist_req.stock_dist_req_db_id
            IN
            -- split the request key to in clause
            ( 
               SELECT regexp_substr( aReqKeys, '[^,]+', 1, LEVEL ) FROM dual
               CONNECT BY regexp_substr( aReqKeys, '[^,]+', 1, LEVEL ) IS NOT NULL 
            )                  
      ),  
      dist_req_with_part AS
      (
         SELECT
            dist_req.*,
            eqp_part_no.part_no_db_id,
            eqp_part_no.part_no_id,
            eqp_part_no.qty_unit_cd
         FROM
            dist_req 
            -- one stock may have multiple parts
            INNER JOIN eqp_part_no ON
               dist_req.stock_no_db_id = eqp_part_no.stock_no_db_id AND
               dist_req.stock_no_id    = eqp_part_no.stock_no_id
      ),        
      dist_req_needed_qty AS
      (
         SELECT
            dist_req.stock_dist_req_db_id,
            dist_req.stock_dist_req_id,
            dist_req.needed_qty - SUM( NVL( xfer.xfer_qt, 0 ) ) AS new_needed_qty
         FROM
            dist_req
            LEFT JOIN stock_dist_req_picked_item ON
               dist_req.stock_dist_req_db_id = stock_dist_req_picked_item.stock_dist_req_db_id AND
               dist_req.stock_dist_req_id    = stock_dist_req_picked_item.stock_dist_req_id
            LEFT JOIN 
            (
               SELECT 
                  inv_xfer.xfer_db_id,
                  inv_xfer.xfer_id,
                  inv_xfer.xfer_qt
               FROM 
                  inv_xfer
                  INNER JOIN evt_event xfer_event ON
                     inv_xfer.xfer_db_id = xfer_event.event_db_id AND
                     inv_xfer.xfer_id    = xfer_event.event_id
               WHERE
                  ( xfer_event.event_status_db_id, xfer_event.event_status_cd ) 
                  IN
                  ( ( 0, 'LXPEND' ), ( 0, 'LXCMPLT' ) )               
            ) xfer ON
               stock_dist_req_picked_item.xfer_db_id = xfer.xfer_db_id AND
               stock_dist_req_picked_item.xfer_id    = xfer.xfer_id
         GROUP BY
            dist_req.stock_dist_req_db_id,
            dist_req.stock_dist_req_id,
            dist_req.needed_qty        
      ),    
      -- for each supplier location, get its bin locations
      supplier_bin AS
      (
         SELECT
            -- keep the root location at each location row
            CONNECT_BY_ROOT inv_loc.loc_db_id AS supplier_loc_db_id,
            CONNECT_BY_ROOT inv_loc.loc_id    AS supplier_loc_id,
            inv_loc.loc_db_id,
            inv_loc.loc_id,
            inv_loc.loc_cd
          FROM
             inv_loc
          WHERE
             ( inv_loc.loc_db_id, inv_loc.loc_id ) 
             NOT IN
             ( SELECT supplier_loc_db_id, supplier_loc_id FROM dist_req )
          START WITH
             -- get sub locations for both supplier and warehouse    
             ( inv_loc.loc_db_id, inv_loc.loc_id )
             IN
             ( SELECT supplier_loc_db_id, supplier_loc_id FROM dist_req )
          CONNECT BY
             inv_loc.nh_loc_db_id = PRIOR inv_loc.loc_db_id  AND
             inv_loc.nh_loc_id    = PRIOR inv_loc.loc_id
      ),
      supplier_bin_inv AS
      (
         SELECT
            dist_req_with_part.stock_dist_req_db_id,
            dist_req_with_part.stock_dist_req_id,
            dist_req_with_part.part_no_db_id,
            dist_req_with_part.part_no_id,     
            supplier_bin.loc_db_id,
            supplier_bin.loc_id,
            supplier_bin.loc_cd,
            SUM( NVL( inv_inv.bin_qt, 1 ) ) AS bin_total     
         FROM
            dist_req_with_part     
            INNER JOIN supplier_bin ON
               dist_req_with_part.supplier_loc_db_id = supplier_bin.supplier_loc_db_id AND 
               dist_req_with_part.supplier_loc_id    = supplier_bin.supplier_loc_id            
            INNER JOIN inv_inv ON
               dist_req_with_part.part_no_db_id = inv_inv.part_no_db_id AND
               dist_req_with_part.part_no_id    = inv_inv.part_no_id            
               AND
               supplier_bin.loc_db_id = inv_inv.loc_db_id AND
               supplier_bin.loc_id    = inv_inv.loc_id
         WHERE
            (
               inv_inv.nh_inv_no_db_id IS NULL OR
               inv_inv.nh_inv_no_id    IS NULL
            )
            AND
            inv_inv.issued_bool = 0
            AND
            inv_inv.not_found_bool = 0
            AND
            inv_inv.inv_cond_db_id = 0 AND
            inv_inv.inv_cond_cd    = 'RFI'  
            AND
            (          
               -- the owner must be the same if it is not null
               dist_req_with_part.owner_db_id IS NULL
               OR
               (
                  dist_req_with_part.owner_db_id = inv_inv.owner_db_id AND
                  dist_req_with_part.owner_id    = inv_inv.owner_id
               )
            )
         GROUP BY
            dist_req_with_part.stock_dist_req_db_id,
            dist_req_with_part.stock_dist_req_id,         
            dist_req_with_part.part_no_db_id,
            dist_req_with_part.part_no_id,          
            supplier_bin.loc_db_id,
            supplier_bin.loc_id,
            supplier_bin.loc_cd  
      )      
      -- do not use distinct to get the unique distribution request because its order is different than the original order
      SELECT 
         DistReqTableRow(
            ranked_req.stock_dist_req_db_id,
            ranked_req.stock_dist_req_id,
            ranked_req.request_id,
            ranked_req.needed_loc_cd,
            ranked_req.sup_loc_cd,   
            -- if the new needed qty does not exist (no picked item), use the stock dist request needed quantity      
            NVL( dist_req_needed_qty.new_needed_qty, ranked_req.needed_qty ),
            ranked_req.owner_cd,
            ranked_req.qty_unit_cd
         )
      FROM
         (
            SELECT 
               dist_req_with_part.stock_dist_req_db_id,
               dist_req_with_part.stock_dist_req_id,               
               dist_req_with_part.request_id,
               dist_req_with_part.needed_loc_cd,
               dist_req_with_part.sup_loc_cd,
               dist_req_with_part.needed_qty,
               dist_req_with_part.owner_cd,
               dist_req_with_part.qty_unit_cd,  
               inv_loc_zone.route_order,
               supplier_bin_inv.loc_cd, 
               -- for each dist request, order by zone route and bin location code and set the rn               
               row_number() OVER ( 
                  PARTITION BY  
                     dist_req_with_part.stock_dist_req_db_id,
                     dist_req_with_part.stock_dist_req_id 
                  ORDER BY
                     inv_loc_zone.route_order,
                     supplier_bin_inv.loc_cd 
               ) AS rn                       
            FROM 
               dist_req_with_part
               LEFT JOIN supplier_bin_inv ON
                  dist_req_with_part.stock_dist_req_db_id = supplier_bin_inv.stock_dist_req_db_id AND
                  dist_req_with_part.stock_dist_req_id    = supplier_bin_inv.stock_dist_req_id
                  AND               
                  dist_req_with_part.part_no_db_id = supplier_bin_inv.part_no_db_id AND
                  dist_req_with_part.part_no_id    = supplier_bin_inv.part_no_id 
                  AND
                  supplier_bin_inv.bin_total > 0       
               LEFT JOIN inv_loc_zone ON
                  supplier_bin_inv.loc_db_id = inv_loc_zone.loc_db_id AND
                  supplier_bin_inv.loc_id    = inv_loc_zone.loc_id
         ) ranked_req
         INNER JOIN dist_req_needed_qty ON
            ranked_req.stock_dist_req_db_id = dist_req_needed_qty.stock_dist_req_db_id AND
            ranked_req.stock_dist_req_id    = dist_req_needed_qty.stock_dist_req_id
      WHERE
         -- for each distribution request, get the row with rn as 1 which means get the first one with router order and bin location code
         ranked_req.rn = 1 
         AND
         -- remove the totally picked and over picked whose new_needed_qty <= 0
         dist_req_needed_qty.new_needed_qty > 0 
      ORDER BY
         ranked_req.route_order,
         ranked_req.loc_cd                  
      ;

      ltable_dist_req  DistReqTable;

   BEGIN

       OPEN lcur_dist_req;
       
       LOOP
          FETCH lcur_dist_req
          BULK COLLECT INTO ltable_dist_req
          LIMIT gk_row_limit;

          EXIT WHEN ltable_dist_req.COUNT = 0;
          FOR i IN 1..ltable_dist_req.COUNT LOOP
             PIPE ROW (ltable_dist_req(i));
          END LOOP;
       END LOOP;
       
       CLOSE lcur_dist_req;

       RETURN;

   EXCEPTION
      WHEN OTHERS THEN
         RAISE;

   END GetDistReq;
   
   -----------------------------------------------------------------------------------------------------------
   -- Description  : Get the part list to pick
   -- Return       : PickListTable (Type Table) - collection of data
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetPickList (
      ain_stock_dist_req_id     IN stock_dist_req.stock_dist_req_id%TYPE,   
      ain_stock_dist_req_db_id  IN stock_dist_req.stock_dist_req_db_id%TYPE
   ) RETURN DistReqPickListTable
   PIPELINED
   IS
      CURSOR lcur_pick_list IS
      WITH dist_req AS
      (
         SELECT
            -- request info
            stock_dist_req.stock_no_db_id,
            stock_dist_req.stock_no_id,
            stock_dist_req.owner_db_id,
            stock_dist_req.owner_id,
            -- location info
            sup_loc.loc_db_id AS sup_loc_db_id,
            sup_loc.loc_id    AS sup_loc_id,
            wh_loc.loc_db_id  AS wh_loc_db_id,
            wh_loc.loc_id     AS wh_loc_id                                   
         FROM
            stock_dist_req
            INNER JOIN inv_loc wh_loc ON
               stock_dist_req.needed_loc_db_id = wh_loc.loc_db_id AND
               stock_dist_req.needed_loc_id    = wh_loc.loc_id
            INNER JOIN inv_loc sup_loc ON
               stock_dist_req.supplier_loc_db_id = sup_loc.loc_db_id AND
               stock_dist_req.supplier_loc_id    = sup_loc.loc_id
         WHERE
            stock_dist_req.stock_dist_req_db_id = ain_stock_dist_req_db_id AND
            stock_dist_req.stock_dist_req_id    = ain_stock_dist_req_id
      ),
      dist_req_with_part AS
      (
         SELECT
            dist_req.*,
            -- part info
            eqp_part_no.part_no_db_id,
            eqp_part_no.part_no_id,
            eqp_part_no.part_no_oem,
            eqp_part_no.part_no_sdesc,
            eqp_part_no.qty_unit_cd,
            eqp_part_no.part_type_cd,
            eqp_part_no.hazmat_cd                                    
         FROM
            dist_req
            -- one stock may have multiple parts 
            INNER JOIN eqp_part_no ON
               dist_req.stock_no_db_id = eqp_part_no.stock_no_db_id AND
               dist_req.stock_no_id    = eqp_part_no.stock_no_id               
      ),      
      -- get all supplier location and warehouse location
      root_loc AS (
         SELECT 
            sup_loc_db_id AS loc_db_id, 
            sup_loc_id    AS loc_id 
         FROM 
            dist_req 
         UNION ALL
         SELECT 
            wh_loc_db_id AS loc_db_id, 
            wh_loc_id    AS loc_id
         FROM 
            dist_req 
      ),
      -- for each supplier or warehouse location, get its bin locations including the root
      bin_loc AS
      (
         SELECT
            -- keep the root location at each location row
            CONNECT_BY_ROOT inv_loc.loc_db_id AS root_loc_db_id,
            CONNECT_BY_ROOT inv_loc.loc_id    AS root_loc_id,
            inv_loc.loc_db_id,
            inv_loc.loc_id,
            inv_loc.loc_cd
          FROM
             inv_loc
          START WITH
             -- get sub locations for both supplier and warehouse    
             ( inv_loc.loc_db_id, inv_loc.loc_id )
             IN
             ( SELECT loc_db_id, loc_id FROM root_loc )
          CONNECT BY
             inv_loc.nh_loc_db_id = PRIOR inv_loc.loc_db_id  AND
             inv_loc.nh_loc_id    = PRIOR inv_loc.loc_id
      ),    
      -- get the alternative part and supplier bin location for each distribution request
      req_part_supl_bin AS
      (
         SELECT
            dist_req_with_part.part_no_db_id,
            dist_req_with_part.part_no_id,
            dist_req_with_part.qty_unit_cd,
            dist_req_with_part.owner_db_id,
            dist_req_with_part.owner_id,               
            bin_loc.loc_db_id,
            bin_loc.loc_id,
            inv_loc.loc_cd
         FROM
            dist_req_with_part
            -- get the bin whose root is the supplier       
            INNER JOIN bin_loc ON
               dist_req_with_part.sup_loc_db_id = bin_loc.root_loc_db_id AND
               dist_req_with_part.sup_loc_id    = bin_loc.root_loc_id
            INNER JOIN inv_loc ON
               bin_loc.loc_db_id = inv_loc.loc_db_id AND 
               bin_loc.loc_id    = inv_loc.loc_id                
      ),
      -- get all inventories in the supplier and its bin locations
      req_part_bin_inv AS
      (
         SELECT
            req_part_supl_bin.part_no_db_id,
            req_part_supl_bin.part_no_id,     
            req_part_supl_bin.loc_db_id,
            req_part_supl_bin.loc_id,
            SUM( NVL( inv_inv.bin_qt, 1 ) ) AS bin_total     
         FROM
            req_part_supl_bin                   
            INNER JOIN inv_inv ON
               req_part_supl_bin.loc_db_id = inv_inv.loc_db_id AND
               req_part_supl_bin.loc_id    = inv_inv.loc_id
               AND
               req_part_supl_bin.part_no_db_id = inv_inv.part_no_db_id AND
               req_part_supl_bin.part_no_id    = inv_inv.part_no_id
         WHERE
            (
               inv_inv.nh_inv_no_db_id IS NULL OR
               inv_inv.nh_inv_no_id    IS NULL
            )
            AND
            inv_inv.issued_bool = 0
            AND
            inv_inv.not_found_bool = 0
            AND
            inv_inv.inv_cond_db_id = 0 AND
            inv_inv.inv_cond_cd    = 'RFI'  
            AND
            (          
               -- the owner must be the same if it is not null
               req_part_supl_bin.owner_db_id IS NULL
               OR
               (
                  req_part_supl_bin.owner_db_id = inv_inv.owner_db_id AND
                  req_part_supl_bin.owner_id    = inv_inv.owner_id
               )
            )
         GROUP BY
            req_part_supl_bin.part_no_db_id,
            req_part_supl_bin.part_no_id,          
            req_part_supl_bin.loc_db_id,
            req_part_supl_bin.loc_id  
      ),
      -- get the supplier bin and its total quantity for each pair of distribution request and part
      supplier AS
      (
         SELECT
            req_part_supl_bin.part_no_db_id,
            req_part_supl_bin.part_no_id,
            -- put html code here because it is hard to merge multiple rows in the from column for one part row in jasper report studio
            LISTAGG(req_part_supl_bin.loc_cd || '<br>(' 
               -- owner
               || NVL2( inv_owner.owner_cd, inv_owner.owner_cd || ', ', '')
               -- quantity
               || req_part_bin_inv.bin_total || ' ' || req_part_supl_bin.qty_unit_cd || ')', '<br>')
               WITHIN GROUP ( ORDER BY inv_loc_zone.route_order, req_part_supl_bin.loc_cd ) AS from_loc  
         FROM
            req_part_supl_bin
            INNER JOIN req_part_bin_inv ON
               req_part_supl_bin.part_no_db_id = req_part_bin_inv.part_no_db_id AND
               req_part_supl_bin.part_no_id    = req_part_bin_inv.part_no_id
               AND
               req_part_supl_bin.loc_db_id = req_part_bin_inv.loc_db_id AND
               req_part_supl_bin.loc_id    = req_part_bin_inv.loc_id
            LEFT JOIN inv_owner ON
               req_part_supl_bin.owner_db_id = inv_owner.owner_db_id AND
               req_part_supl_bin.owner_id    = inv_owner.owner_id
            LEFT JOIN inv_loc_zone ON
               req_part_bin_inv.loc_db_id = inv_loc_zone.loc_db_id AND
               req_part_bin_inv.loc_id    = inv_loc_zone.loc_id               
         WHERE
            req_part_bin_inv.bin_total > 0               
         GROUP BY          
            req_part_supl_bin.part_no_db_id,
            req_part_supl_bin.part_no_id   
      ), 
      -- get the warehouse bin for each pair of distribution request and part     
      warehouse AS
      (
         SELECT
            dist_req_with_part.part_no_db_id,
            dist_req_with_part.part_no_id,
            -- put html code here because it is hard to merge multiple rows in the to_bin column for one part row in jasper report studio
            LISTAGG(inv_loc.loc_cd, '<br>')
               WITHIN GROUP ( ORDER BY inv_loc.loc_cd ) AS to_bin         
         FROM         
            dist_req_with_part
            INNER JOIN bin_loc ON
               dist_req_with_part.wh_loc_db_id = bin_loc.root_loc_db_id AND
               dist_req_with_part.wh_loc_id    = bin_loc.root_loc_id                
            INNER JOIN inv_loc_bin ON
               dist_req_with_part.part_no_db_id = inv_loc_bin.part_no_db_id AND
               dist_req_with_part.part_no_id    = inv_loc_bin.part_no_id  
               AND
               bin_loc.loc_db_id = inv_loc_bin.loc_db_id AND 
               bin_loc.loc_id    = inv_loc_bin.loc_id 
            INNER JOIN inv_loc ON
               bin_loc.loc_db_id = inv_loc.loc_db_id AND 
               bin_loc.loc_id    = inv_loc.loc_id                        
         WHERE
            dist_req_with_part.owner_db_id IS NULL
            OR
            inv_loc_bin.owner_db_id IS NULL
            OR
            (
               dist_req_with_part.owner_db_id = inv_loc_bin.owner_db_id AND
               dist_req_with_part.owner_id    = inv_loc_bin.owner_id
            )
         GROUP BY     
            dist_req_with_part.part_no_db_id,
            dist_req_with_part.part_no_id                    
      )
      SELECT  
         DistReqPickListTableRow(
            dist_req_with_part.part_no_oem,
            dist_req_with_part.part_no_sdesc,
            dist_req_with_part.part_type_cd,
            dist_req_with_part.qty_unit_cd,
            dist_req_with_part.hazmat_cd,
            SUBSTR( supplier.from_loc, 1, 3500 ),
            SUBSTR( warehouse.to_bin, 1, 3500 )
         )
      FROM
         dist_req_with_part
         LEFT JOIN supplier ON
            dist_req_with_part.part_no_db_id = supplier.part_no_db_id AND
            dist_req_with_part.part_no_id    = supplier.part_no_id            
         LEFT JOIN warehouse ON
            dist_req_with_part.part_no_db_id = warehouse.part_no_db_id AND
            dist_req_with_part.part_no_id    = warehouse.part_no_id
      ORDER BY
         dist_req_with_part.part_no_oem
      ;

      ltable_pick_list   DistReqPickListTable;

   BEGIN

       OPEN lcur_pick_list;
       
       LOOP
          FETCH lcur_pick_list
          BULK COLLECT INTO ltable_pick_list
          LIMIT gk_row_limit;

          EXIT WHEN ltable_pick_list.COUNT = 0;
          FOR i IN 1..ltable_pick_list.COUNT LOOP
             PIPE ROW (ltable_pick_list(i));
          END LOOP;
       END LOOP;
       
       CLOSE lcur_pick_list;

       RETURN;

   EXCEPTION
      WHEN OTHERS THEN
         RAISE;

   END GetPickList;

END report_stkdistreq_picklist_pkg;
/
