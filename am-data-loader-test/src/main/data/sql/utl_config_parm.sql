-- Spec 2000 priority map configuration for testing Spec 2000 messages
UPDATE utl_config_parm
SET parm_value = 'AOG-10,WSP-20,USR-40,RTN-100'
WHERE parm_name = 'SPEC2000_PO_PRIORITY_MAP';
