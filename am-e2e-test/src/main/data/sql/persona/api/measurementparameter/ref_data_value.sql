/**************************************************
******** INSERT SCRIPT FOR TABLE REF_DATA_VALUE *******
***************************************************/
-- Create a ref_data_value record.
INSERT
   INTO
      ref_data_value
         (
            data_value_db_id,
            data_value_cd,
            desc_sdesc
        )
   SELECT
      4650,
      'BUSY',
      'BUSY Signal'
   FROM
      dual;

-- Create a ref_data_value record.
INSERT
   INTO
      ref_data_value
         (
            data_value_db_id,
            data_value_cd,
            desc_sdesc
        )
   SELECT
      4650,
      'UNCLEAR',
      'UNCLEAR Signal'
   FROM
      dual;

-- Create a ref_data_value record.
INSERT
   INTO
      ref_data_value
         (
            data_value_db_id,
            data_value_cd,
            desc_sdesc
        )
   SELECT
      4650,
      'TEST',
      'Test'
   FROM
      dual;