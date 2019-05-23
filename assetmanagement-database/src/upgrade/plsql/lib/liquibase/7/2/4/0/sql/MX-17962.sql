--liquibase formatted sql


--changeSet MX-17962:1 stripComments:false
-- Reset Transfer-on-bool for all contracts which accept non-OEM repairables
UPDATE 
       warranty_defn_assembly assy_contract
SET
       -- reset transfer_bool
       assy_contract.transfer_bool = 0
WHERE 
      assy_contract.transfer_bool = 1
      AND
      EXISTS(
      -- for all warranties where the core contract is not restricted to OEM parts
      SELECT 
             1
      FROM
          warranty_defn
      WHERE
          warranty_defn.warranty_defn_db_id = assy_contract.warranty_defn_db_id AND
          warranty_defn.warranty_defn_id    = assy_contract.warranty_defn_id
          AND
          warranty_defn.oem_parts_only_bool = 0             
);