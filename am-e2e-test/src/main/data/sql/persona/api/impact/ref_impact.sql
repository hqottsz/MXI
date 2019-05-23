/**************************************************
******** INSERT SCRIPT FOR TABLE REF_IMPACT *******
***************************************************/
-- Create a ref_impact record.
INSERT
   INTO
      ref_impact
         (
            impact_db_id,
            impact_cd,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      4650,
      'EL',
      'Electrical Load',
      'Electrical Load Description...',
      0
   FROM
      dual;

-- Create a ref_impact record.
INSERT
   INTO
      ref_impact
         (
            impact_db_id,
            impact_cd,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      4650,
      'ETOPS',
      'ETOPS',
      'ETOPS Description...',
      0
   FROM
      dual;

-- Create a ref_impact record.
INSERT
   INTO
      ref_impact
         (
            impact_db_id,
            impact_cd,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      4650,
      'WB',
      'Weight and Balance',
      'Weight and Balance Description...',
      0
   FROM
      dual;

-- Create a ref_impact record.
INSERT
   INTO
      ref_impact
         (
            impact_db_id,
            impact_cd,
            desc_sdesc,
            desc_ldesc,
            rstat_cd
        )
   SELECT
      30,
      'WB',
      'Weight and Balance',
      'Weight and Balance Description...',
      0
   FROM
      dual;
