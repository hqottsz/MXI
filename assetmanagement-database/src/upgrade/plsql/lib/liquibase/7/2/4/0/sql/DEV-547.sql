--liquibase formatted sql


--changeSet DEV-547:1 stripComments:false
-- HTTP Compression parameters
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'HTTP_COMPRESSION_URLS', '.*', 'HTTP', 'A comma-separated list of regular expressions (java.util.regex.Pattern) which match the URLs of the content to compress.', 'GLOBAL', 'Java Regex List', '.*', 1, 'HTTP COMPRESSION', '7.5', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'HTTP_COMPRESSION_URLS');

--changeSet DEV-547:2 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET ALLOW_VALUE_DESC = 'Java Regex List',
       DEFAULT_VALUE = '.*',
       MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'HTTP_COMPRESSION_URLS' AND PARM_TYPE = 'HTTP';

--changeSet DEV-547:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'HTTP_COMPRESSION_CONTENT_TYPES', 'text/html,text/xml,text/plain,text/javascript,application/javascript,application/x-javascript,text/css,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/msexcel,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/mspowerpoint', 
'HTTP', 'A comma-separated list of compressible MIME types', 'GLOBAL', 'Content Type List', 'text/html,text/xml,text/plain,text/javascript,application/javascript,application/x-javascript,text/css,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/msexcel,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/mspowerpoint', 1, 'HTTP COMPRESSION', '7.5', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'HTTP_COMPRESSION_CONTENT_TYPES');

--changeSet DEV-547:4 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET ALLOW_VALUE_DESC = 'Content Type List', 
       DEFAULT_VALUE = 'text/html,text/xml,text/plain,text/javascript,application/javascript,application/x-javascript,text/css,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel,application/msexcel,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.ms-powerpoint,application/mspowerpoint',
       MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'HTTP_COMPRESSION_CONTENT_TYPES' AND PARM_TYPE = 'HTTP';

--changeSet DEV-547:5 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'HTTP_COMPRESSION_DEBUG', 'false', 'HTTP', 'Enables debug logging for HTTP compression operations', 'GLOBAL', 'TRUE/FALSE', 'FALSE', 1, 'HTTP COMPRESSION', '7.5', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'HTTP_COMPRESSION_DEBUG');

--changeSet DEV-547:6 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'HTTP_COMPRESSION_DEBUG' AND PARM_TYPE = 'HTTP';

--changeSet DEV-547:7 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'HTTP_COMPRESSION_ENABLED', 'true', 'HTTP', 'Enables HTTP compression', 'GLOBAL', 'TRUE/FALSE', 'TRUE', 1, 'HTTP COMPRESSION', '7.5', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'HTTP_COMPRESSION_ENABLED');

--changeSet DEV-547:8 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET PARM_VALUE = 'true',
       DEFAULT_VALUE = 'TRUE',
       MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'HTTP_COMPRESSION_ENABLED' AND PARM_TYPE = 'HTTP';

--changeSet DEV-547:9 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_NAME, PARM_VALUE, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'HTTP_COMPRESSION_MIN_SIZE', '1024', 'HTTP', 'Size in bytes of the smallest response that will be compressed.Responses smaller than this size will not be compressed.', 'GLOBAL', 'Integer', '1024', 1, 'HTTP COMPRESSION', '7.5', 0
FROM DUAL WHERE NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'HTTP_COMPRESSION_MIN_SIZE');

--changeSet DEV-547:10 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET ALLOW_VALUE_DESC = 'Integer',
       DEFAULT_VALUE = '1024',
       MODIFIED_IN = '7.5'
WHERE PARM_NAME = 'HTTP_COMPRESSION_MIN_SIZE' AND PARM_TYPE = 'HTTP';