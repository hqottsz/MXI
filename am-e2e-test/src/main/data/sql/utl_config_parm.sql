-- Spec 2000 priority map configuration for testing Spec 2000 messages
UPDATE utl_config_parm
SET parm_value = 'AOG-10,WSP-20,USR-40,RTN-100'
WHERE parm_name = 'SPEC2000_PO_PRIORITY_MAP';

-- Set SHOW_SCHED_LABOUR_HOURS to TRUE in order to show labor rows
UPDATE utl_config_parm
SET parm_value = 'TRUE'
WHERE parm_name = 'SHOW_SCHED_LABOUR_HOURS';

-- Enable the Ready for Build status
UPDATE utl_config_parm
SET parm_value = 'TRUE'
WHERE parm_name = 'ENABLE_READY_FOR_BUILD';

-- Set the expected tool checkout duration to 24 hours
UPDATE utl_config_parm
SET parm_value = '24'
WHERE parm_name = 'EXPECTED_TOOL_CHECKOUT_DURATION_HRS';

-- Make reference field visible for part requirements
UPDATE utl_config_parm
SET parm_value = 'TRUE'
WHERE parm_name = 'SHOW_PART_REQUIREMENT_REFERENCE';
