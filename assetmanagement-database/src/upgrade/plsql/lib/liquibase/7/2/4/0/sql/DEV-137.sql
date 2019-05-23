--liquibase formatted sql


--changeSet DEV-137:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   This script will add two Planning Types per Assembly, if they do not already exist:
    - CUSTOM
    - STDR
    
   This script must execute after the creation of
    - Table: EQP_PLANNING_TYPE
    - Sequence: PLANNING_TYPE_SEQ
*/  
DECLARE

  ln_PlanningTypeId       eqp_planning_type.planning_type_id%TYPE;
  
  -- Get all Assemblies
  CURSOR lCur_Assemblies IS
    SELECT
      eqp_assmbl.assmbl_db_id,
      eqp_assmbl.assmbl_cd,
      DECODE( cust_plan_type.planning_type_db_id, NULL, 1, 0) AS create_cust_bool,
      DECODE( stdr_plan_type.planning_type_db_id, NULL, 1, 0) AS create_stdr_bool
    FROM
       eqp_assmbl
       LEFT OUTER JOIN eqp_planning_type cust_plan_type ON 
          cust_plan_type.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
          cust_plan_type.assmbl_cd    = eqp_assmbl.assmbl_cd 
          AND
          cust_plan_type.planning_type_cd = 'CUSTOM'
       LEFT OUTER JOIN eqp_planning_type stdr_plan_type ON 
          stdr_plan_type.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
          stdr_plan_type.assmbl_cd    = eqp_assmbl.assmbl_cd 
          AND
          stdr_plan_type.planning_type_cd = 'STDR';    
      
  lRec_Assemblies lCur_Assemblies%ROWTYPE;     

BEGIN

  FOR lRec_Assemblies in lCur_Assemblies LOOP
  
     -- Create Custom Planning Type if needed
     IF(lRec_Assemblies.create_cust_bool = 1) THEN
        SELECT PLANNING_TYPE_SEQ.nextval INTO ln_PlanningTypeId FROM dual;
        INSERT INTO eqp_planning_type (
           planning_type_db_id, planning_type_id,
           assmbl_db_id, assmbl_cd,
           planning_type_cd,
           desc_sdesc,
           desc_ldesc,
           nr_factor
        )
        VALUES (
           (SELECT db_id FROM mim_local_db), ln_PlanningTypeId,
           lRec_Assemblies.assmbl_db_id, lRec_Assemblies.assmbl_cd,
           'CUSTOM',           
           'Custom non routine estimate',
           'Custom non routine estimate',
           0
        );
     END IF;
     
     -- Create Standard Planning Type if needed
     IF(lRec_Assemblies.create_stdr_bool = 1) THEN
        SELECT PLANNING_TYPE_SEQ.nextval INTO ln_PlanningTypeId FROM dual;
        INSERT INTO eqp_planning_type (
           planning_type_db_id, planning_type_id,
           assmbl_db_id, assmbl_cd,
           planning_type_cd,
           desc_sdesc,
           desc_ldesc,
           nr_factor
        )
        VALUES (
           (SELECT db_id FROM mim_local_db), ln_PlanningTypeId,
           lRec_Assemblies.assmbl_db_id, lRec_Assemblies.assmbl_cd,
           'STDR',           
           'Standard Routine',
           'Standard Routine',
           0
        );
      END IF;
   END LOOP;
END;
/