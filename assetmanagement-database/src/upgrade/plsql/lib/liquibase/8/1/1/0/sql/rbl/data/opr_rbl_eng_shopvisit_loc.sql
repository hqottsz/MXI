--liquibase formatted sql


--changeSet opr_rbl_eng_shopvisit_loc:1 stripComments:false
-- location where engine visit is a shopvisit
INSERT
INTO
   opr_rbl_eng_shopvisit_loc
   (  
     location_id,
     location_code,
     location_type_code
   )   
SELECT
  alt_id,
  loc_cd,
  loc_type_cd
FROM 
   inv_loc
WHERE
   inv_loc.loc_type_cd IN ('OUTSRCE',
                           'VENHGR',
                           'VENLINE',
                           'VENTRK',
                           'VENDOR'
                          )
    AND
    NOT EXISTS (
                 SELECT
                    1
                 FROM 
                    opr_rbl_eng_shopvisit_loc
                 WHERE
                    location_id = alt_id
               );