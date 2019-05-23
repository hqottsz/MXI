--liquibase formatted sql


--changeSet MX-23805:1 stripComments:false
-- Set the interchangeability order to 1 for all parts that are not TRK, ASSY or SER
UPDATE
     eqp_part_baseline
SET
     eqp_part_baseline.interchg_ord = 1
WHERE
     -- get part with interchangeability order is not 1
     eqp_part_baseline.interchg_ord <> 1
     AND     
     -- make sure the part is not a TRK, ASSY or SER
     ( eqp_part_baseline.part_no_db_id, eqp_part_baseline.part_no_id )
     IN
     (
           SELECT
                eqp_part_no.part_no_db_id, 
                eqp_part_no.part_no_id
           FROM
                eqp_part_no
           WHERE   
                ( eqp_part_no.inv_class_db_id, eqp_part_no.inv_class_cd ) 
                NOT IN
                ( ( 0, 'TRK' ) , ( 0, 'ASSY') , ( 0, 'SER' ) )                  
     )
     AND
     -- make sure that part is assigned to a part group 
     ( eqp_part_baseline.bom_part_db_id, eqp_part_baseline.bom_part_id )
     IN
     (     
           SELECT
                eqp_bom_part.bom_part_db_id, eqp_bom_part.bom_part_id
           FROM
                eqp_bom_part
           WHERE
               ( eqp_bom_part.inv_class_db_id, eqp_bom_part.inv_class_cd ) 
               NOT IN
               ( ( 0, 'TRK' ) , ( 0, 'ASSY') , ( 0, 'SER' ) )
     );