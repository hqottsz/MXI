/**************************************************
******** INSERT SCRIPT FOR TABLE REF_FAULT_SOURCE *******
***************************************************/
-- Create a ref_fault_source record.
INSERT
   INTO
      ref_fault_source
         (
            fault_source_db_id,
            fault_source_cd,
            desc_sdesc,
            desc_ldesc,
            spec2k_fault_source_cd,
            rstat_cd
        )
   SELECT
      4650,
      'MECH',
      'Mechanic',
      'Mechanic',
      'ML',
      0
   FROM
      dual;