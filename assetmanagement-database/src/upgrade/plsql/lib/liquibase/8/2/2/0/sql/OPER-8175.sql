--liquibase formatted sql
 

--changeSet OPER-8175:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************
** CREATE OR REPLACE SCRIPT FOR NEW FUNCTION getLocationCapabilities
** DATE: 27-OCTOBER-2016
********************************************************************/
CREATE OR REPLACE FUNCTION getLocationCapabilities
(
   aLocDbId inv_loc.loc_db_id%TYPE,
   aLocId inv_loc.loc_id%TYPE
) RETURN VARCHAR2
IS
   lCapabilityString VARCHAR2(32767);
BEGIN
   SELECT
     CONCAT_DATA(
       CURSOR(
         SELECT
            location_tree_capabilities.assmbl_cd || '$' || location_tree_capabilities.work_type_cd AS location_capabilities
         FROM
           (
             SELECT DISTINCT
                all_capabilities.assmbl_cd,
                all_capabilities.work_type_cd
             FROM
               (
                 SELECT
                    inv_loc.loc_db_id,
                    inv_loc.loc_id,
                    inv_loc.loc_cd,
                    inv_loc.nh_loc_db_id,
                    inv_loc.nh_loc_id,
                    inv_loc.loc_type_db_id,
                    inv_loc.loc_type_cd,
                    inv_loc_capability.assmbl_db_id,
                    inv_loc_capability.assmbl_cd,
                    inv_loc_capability.work_type_cd
                 FROM
                    inv_loc
                 LEFT OUTER JOIN inv_loc_capability ON
                    inv_loc.loc_db_id = inv_loc_capability.loc_db_id AND
                    inv_loc.loc_id    = inv_loc_capability.loc_id
               )all_capabilities
             START WITH
               all_capabilities.loc_db_id = aLocDbId AND
               all_capabilities.loc_id    = aLocId
             CONNECT BY
               all_capabilities.nh_loc_db_id = PRIOR all_capabilities.loc_db_id AND
               all_capabilities.nh_loc_id    = PRIOR all_capabilities.loc_id
           )location_tree_capabilities
         WHERE
            location_tree_capabilities.work_type_cd IS NOT NULL
           )) AS capability_list
   INTO
     lCapabilityString
   FROM
     dual;

   RETURN lCapabilityString;

END getLocationCapabilities;
/