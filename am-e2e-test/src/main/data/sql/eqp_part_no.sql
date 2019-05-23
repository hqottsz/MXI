-- update eqp_part_no for specific part number in regards to activating receipt_insp_bool
UPDATE 
   eqp_part_no 
SET 
   receipt_insp_bool = 1
WHERE 
   part_no_oem = 'B000001';
   
   
UPDATE 
   eqp_part_no 
SET 
   receipt_insp_bool = 1
WHERE 
   part_no_oem = 'S000003';