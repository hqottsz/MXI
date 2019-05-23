--liquibase formatted sql

--changeSet OPER-13202:1 stripComments:false
UPDATE
   utl_config_parm
SET
   allow_value_desc = 'Number'
WHERE
   parm_name = 'FILE_UPLOAD_FILENAME_LENGTH';

