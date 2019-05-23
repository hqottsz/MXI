--liquibase formatted sql


--changeSet MTX-923:1 stripComments:false
/**********************************************************************
* Set the default value of the location configuration parameter 
* to the code of the location from INV_LOC table
**********************************************************************/
UPDATE utl_config_parm SET
    parm_value = (SELECT loc_cd FROM inv_loc WHERE loc_type_cd='AIRPORT' and rownum=1)
 WHERE
    parm_name = 'ARC_DEFAULT_LOCATION';