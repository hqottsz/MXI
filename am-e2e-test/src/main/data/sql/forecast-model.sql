
INSERT INTO fc_model ( model_db_id, model_id, desc_sdesc, default_bool )
VALUES( 4650, 100000, 'Forecast1', 1 );

INSERT INTO fc_range ( model_db_id, model_id, range_id, start_month, start_day )
SELECT
   model_db_id, model_id, 1, 1, 1
FROM
   fc_model
WHERE
   desc_sdesc = 'Forecast1';

-- create all of the HOURS forecast rates
INSERT INTO fc_rate ( model_db_id, model_id, range_id, data_type_db_id, data_type_id, forecast_rate_qt )
SELECT DISTINCT
   fc_range.model_db_id,
   fc_range.model_id,
   fc_range.range_id,
   0,
   1,
   1
FROM
   fc_range
   INNER JOIN fc_model ON
      fc_model.model_db_id = fc_range.model_db_id AND
      fc_model.model_id   = fc_range.model_id;

-- create all of the CYCLES forecast rates
INSERT INTO fc_rate ( model_db_id, model_id, range_id, data_type_db_id, data_type_id, forecast_rate_qt )
SELECT DISTINCT
   fc_range.model_db_id,
   fc_range.model_id,
   fc_range.range_id,
   0,
   10,
   1
FROM
   fc_range
   INNER JOIN fc_model ON
      fc_model.model_db_id = fc_range.model_db_id AND
      fc_model.model_id   = fc_range.model_id;
