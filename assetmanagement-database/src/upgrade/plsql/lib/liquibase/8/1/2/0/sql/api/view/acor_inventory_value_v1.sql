--liquibase formatted sql


--changeSet acor_inventory_value_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW ACOR_INVENTORY_VALUE_V1 AS
WITH
   rvw_asset
AS
   (
      SELECT
         account.ID                         AS account_id,
         part.part_id,
         account.account_cd,
         inventory.owner_code,
         part.manufact_cd                   AS manufacturer_code,
         part.part_number,
         SUM(inventory.bin_quantity)        AS quantity,
         part.average_unit_price            AS avg_unit_price
      FROM
         (
            SELECT
               inventory_id,
               h_inventory_id,
               part_id,
               owner_code,
               oem_serial_number,
               inventory_condition,
               1 as bin_quantity
            FROM
               acor_inv_serial_cntrl_asset_v1
            UNION ALL
            SELECT
               inventory_id,
               h_inventory_id,
               part_id,
               owner_code,
               NVL(oem_batch_number,oem_serial_number),
               inventory_condition,
               bin_quantity
            FROM
               acor_inv_batch_v1
            UNION ALL
            SELECT
               inventory_id,
               h_inventory_id,
               part_id,
               owner_code,
               oem_batch_number,
               inventory_condition,
               bin_quantity
            FROM
               acor_inv_batch_tool_v1
         ) inventory
         INNER JOIN acor_part_number_v1 part ON
            inventory.part_id = part.part_id
         INNER JOIN acor_accounts_v1 account ON
            part.assetaccount_id = account.ID
         INNER JOIN inv_inv ON
            inv_inv.alt_id = inventory.inventory_id              
      WHERE
         -- off-wing
         inventory.inventory_id = inventory.h_inventory_id
         AND
         inventory.inventory_condition NOT IN ('SCRAP','ARCHIVE')
         AND
         part.finance_type_cd IN ('ROTABLE','CONSUM')
         -- Exclude the received inventory that has not been inspected as serviceable because it is still owned by the vendor.
         -- More specific: a row with inv_cond_cd('INSPREQ'), receive_cond_cd('NEW') and owner_type_cd('LOCAL') is excluded,
         -- while a row with inv_cond_cd('INSPREQ'), receive_cond_cd(NULL) and owner_type_cd('LOCAL') is still included.
         AND NOT
         (
            inv_inv.inv_cond_db_id = 0 AND
            inv_inv.inv_cond_cd    = 'INSPREQ'              
            AND
            -- receive_cond_db_id/id is nullable. For this condition, we only want receive_cond_db_id:id=(0, 'NEW'). 
            -- Because this is sub condition of the NOT condition, we need to declare receive_cond_db_id is not null as well.
            -- For example, if we have two rows of data, one with null value in Column1, and another one is xxx. Without
            -- declaring the null value, system returns nothing with the "NOT (Column1 = xxx)" condition. The correct one is
            -- "NOT (Column1 IS NOT NULL AND Column1 = xxx)". In this way, system can show the row with null value in Column1.
            (
              inv_inv.receive_cond_db_id IS NOT NULL 
              AND
              inv_inv.receive_cond_db_id = 0 AND
              inv_inv.receive_cond_cd    = 'NEW'
            )
            AND
            inv_inv.owner_type_db_id = 0 AND
            inv_inv.owner_type_cd    = 'LOCAL'
         )         
      GROUP BY
         account.ID,
         part.part_id,
         account.account_cd,
         inventory.owner_code,
         part.manufact_cd,
         part.part_number,
         part.average_unit_price
     )
SELECT
   account_id,
   part_id,
   account_cd,
   owner_code,
   manufacturer_code,
   part_number,
   quantity,
   avg_unit_price,
   (quantity * avg_unit_price) total
FROM
   rvw_asset;