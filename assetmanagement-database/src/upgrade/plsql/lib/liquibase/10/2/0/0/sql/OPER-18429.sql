--liquibase formatted sql

-- Update script to improve clarity/accuracy of sensitivity warning messages.

--changeSet OPER-18429:1 stripComments:false
UPDATE
    ref_sensitivity
SET
    warning_ldesc = 'CAT III Sensitive - Refer to MPM for Details'
WHERE
    warning_ldesc = 'This system is CAT III compliance sensitive - the aircraft may require recertification.';

--changeSet OPER-18429:2 stripComments:false
UPDATE
    ref_sensitivity
SET
    warning_ldesc = 'ETOPS Sensitive - Refer to MPM for Details'
WHERE
    warning_ldesc = 'This system is ETOPS compliance sensitive - the aircraft may require recertification.';

--changeSet OPER-18429:3 stripComments:false
UPDATE
    ref_sensitivity
SET
    warning_ldesc = 'RII Sensitive - Refer to MPM for Details'
WHERE
    warning_ldesc = 'This system is RII compliance sensitive - the aircraft may require recertification.';

--changeSet OPER-18429:4 stripComments:false
UPDATE
    ref_sensitivity
SET
    warning_ldesc = 'FCBS Sensitive - Refer to MPM for Details'
WHERE
    warning_ldesc = 'This system is FCBS compliance sensitive - the aircraft may require recertification.';

--changeSet OPER-18429:5 stripComments:false
UPDATE
    ref_sensitivity
SET
    warning_ldesc = 'RVSM Sensitive - Refer to MPM for Details'
WHERE
    warning_ldesc = 'This system is RVSM compliance sensitive - the aircraft may require recertification.';
