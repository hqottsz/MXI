--changeset SWA-3811:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create function getLocationCapabilities
/********************************************************************************
*
* Function:      getLocationCapabilities
* Arguments:     p_cursor (sys_refcursor) -cursor/select of data
*                p_delimiter (varchar2)   -optional: delimiter to use between the data
* Description:   This function takes in a inv_loc db_id and loc_id
*                and returns a VARCHAR2 of capabilities for that location in
*                the format <alt_id>$<work_type>,<alt_id>$<work_type>,<alt_id>$<work_type>...
*                
*                ex. FF41799DE8F511E4B4548D9261740067$CABIN,
*                    FF41799DE8F511E4B4548D9261740067$ADMIN,
*                    FF41799DE8F511E4B4548D9261740067$CLEAN,
*                    FF41799DE8F511E4B4548D9261740067$TLI,
*                    FF41799DE8F511E4B4548D9261740067$DMI,
*                    FF41799DE8F511E4B4548D9261740067$TURN
* Version:       1.0
*
* Orig.Coder:    N Taitt
* Recent Coder:  C Atton
* Recent Date:   March 9, 2017
*
*********************************************************************************
*
* Confidential, proprietary and/or trade secret information of Mxi Technologies,
*    Ltd. Copyright 2005 Mxi Technologies, Ltd.  All Rights Reserved.
* Except as expressly provided by written license signed by a duly appointed 
*   officer of Mxi Technologies, Ltd., any disclosure, distribution, reproduction, 
*   compilation, modification, creation of derivative works and/or other use of 
*   the Mxi source code is strictly prohibited. 
* Inclusion of a copyright notice shall not be taken to indicate that the source
*   code has been published.
*
*********************************************************************************/
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
            location_tree_capabilities.alt_id || '$' || location_tree_capabilities.work_type_cd AS location_capabilities
         FROM
           (
             SELECT DISTINCT 
                all_capabilities.alt_id, 
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
                    inv_loc_capability.work_type_cd,
                    eqp_assmbl.alt_id
                 FROM
                    inv_loc
                 LEFT OUTER JOIN inv_loc_capability ON
                    inv_loc.loc_db_id = inv_loc_capability.loc_db_id AND
                    inv_loc.loc_id    = inv_loc_capability.loc_id
                 LEFT JOIN eqp_assmbl ON
                    inv_loc_capability.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
                    inv_loc_capability.assmbl_cd = eqp_assmbl.assmbl_cd 
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