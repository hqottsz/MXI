--liquibase formatted sql


--changeSet convertToUserFriendly:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION convertToUserFriendly
(
   lRawSql task_task.task_appl_sql_ldesc%TYPE
) RETURN VARCHAR
IS
   lReturn task_task.task_appl_sql_ldesc%TYPE;    
BEGIN
 
      lReturn := REPLACE(lRawSql, 'rootpart.part_no_oem', '[Aircraft Part No]');
      lReturn := REPLACE(lReturn, 'ac_inv.serial_no_oem', '[Aircraft Serial No]');
      lReturn := REPLACE(lReturn, 'inv_ac_reg.fin_no_cd', '[Aircraft Fin No]');
      lReturn := REPLACE(lReturn, 'inv_ac_reg.var_no_oem', ' [Aircraft Var No]');
      lReturn := REPLACE(lReturn, 'inv_ac_reg.line_no_oem', '[Aircraft Line No]');
      lReturn := REPLACE(lReturn, 'org_carrier.carrier_cd', '[Aircraft Operator]');
      lReturn := REPLACE(lReturn, 'asspart.part_no_oem', '[Assembly Part No]');
      lReturn := REPLACE(lReturn, 'assembly.serial_no_oem', '[Assembly Serial No]');
      lReturn := REPLACE(lReturn, 'ass_carrier.carrier_cd', '[Assembly Operator]');
      lReturn := REPLACE(lReturn, 'ass_owner.owner_name', '[Assembly Owner Name]');
      lReturn := REPLACE(lReturn, 'ass_owner.owner_cd', '[Assembly Owner Code]');
      lReturn := REPLACE(lReturn, 'eqp_part_no.part_no_oem', '[Component Part No]');
      lReturn := REPLACE(lReturn, 'inv_inv.serial_no_oem', '[Component Serial No]');
      lReturn := REPLACE(lReturn, 'inv_inv.lot_oem_tag', '[Component Lot No]');
      lReturn := REPLACE(lReturn, 'eqp_manufact.manufact_name', '[Component Manufacturer]');
      lReturn := REPLACE(lReturn, 'inv_owner.owner_name', '[Component Owner Name]');
      lReturn := REPLACE(lReturn, 'inv_owner.owner_cd', '[Component Owner Code]');
      
   RETURN lReturn;

END convertToUserFriendly;
/