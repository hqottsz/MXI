--liquibase formatted sql

--changeSet OPER-12358-SP4:1 stripComments:false
--comment Update Config Param description
UPDATE
   utl_config_parm
SET
   parm_desc = 'Controls the way that part number applicability checks are performed. Part applicability checks are executed during the creation of a part requirement, reservation, issue and installation of a component. When set to FALSE, the sub-inventory tree is validated against the aircraft applicability code it is installed on or reserved for. Validations on parts to be attached to loose sub-assemblies will pass automatically. Validations on parts to be attached to a sub-assembly of an aircraft will be done against the aircraft applicability code. When set to TRUE, each sub-inventory in the sub-inventory tree is only validated against its next-highest parent assembly applicability code.',
   modified_in = '8.2-SP2'
WHERE
   parm_name = 'APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY';