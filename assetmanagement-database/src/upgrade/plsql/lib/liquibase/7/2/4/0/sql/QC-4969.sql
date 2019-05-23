--liquibase formatted sql


--changeSet QC-4969:1 stripComments:false
DELETE FROM UTL_USER_PARM WHERE PARM_NAME = 'sDeferralReferenceOperator';

--changeSet QC-4969:2 stripComments:false
DELETE FROM UTL_ROLE_PARM WHERE PARM_NAME = 'sDeferralReferenceOperator';

--changeSet QC-4969:3 stripComments:false
DELETE FROM DB_TYPE_CONFIG_PARM WHERE PARM_NAME = 'sDeferralReferenceOperator';

--changeSet QC-4969:4 stripComments:false
DELETE FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'sDeferralReferenceOperator';