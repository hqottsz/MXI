--liquibase formatted sql


--changeSet QC-6047:1 stripComments:false
UPDATE UTL_CONFIG_PARM 
   SET PARM_DESC='Allow invoiced price greater or less than received price on invoices.'
   WHERE PARM_NAME = 'INVOICE_ALLOW_OVERINV' AND PARM_TYPE = 'LOGIC';