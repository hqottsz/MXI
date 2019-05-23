/**************************************************
******** INSERT SCRIPT FOR TABLE MIM_DATA_VALUE *******
***************************************************/
-- Create a mim_data_value record.
INSERT
   INTO
      mim_data_value
         (
            data_type_db_id,
            data_type_id,
            data_value_db_id,
            data_value_cd
        )
   SELECT
      4650,
      1000,
      4650,
      'BUSY'
   FROM
      dual;

-- Create a mim_data_value record.
INSERT
   INTO
      mim_data_value
         (
            data_type_db_id,
            data_type_id,
            data_value_db_id,
            data_value_cd
        )
   SELECT
      4650,
      1000,
      4650,
      'UNCLEAR'
   FROM
      dual;

-- Create a mim_data_value record.
INSERT
   INTO
      mim_data_value
         (
            data_type_db_id,
            data_type_id,
            data_value_db_id,
            data_value_cd
        )
   SELECT
      4650,
      1001,
      4650,
      'TEST'
   FROM
      dual;