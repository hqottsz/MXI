--liquibase formatted sql
--changeSet OPER-21269:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY USER_PKG
IS


/********************************************************************************
*
* Function:       is_user_auth_to_view_order
* Arguments:      an_PoDbId (IN NUMBER): order primary key
*                 an_PoId (IN NUMBER):   order primary key
*	          an_CurrUserId (IN NUMBER): user primary key
*
* Description:    Tests if an user is authorized to view an order.
*
*
* Orig.Coder:     Akash Motwani
* Recent Coder:   Natasa Subotic
* Recent Date:    July 28,2011
*
*********************************************************************************
*
* Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

FUNCTION is_user_auth_to_view_order(
                           an_PoDbId IN typn_Id,
                           an_PoId IN typn_Id,
                           an_CurrUserId IN typn_Id) RETURN NUMBER
 IS

   -- Local Variables
   v_count NUMBER;

BEGIN

   SELECT COUNT(*)
     INTO v_count

     FROM org_hr
    INNER JOIN org_org_hr ON org_org_hr.hr_db_id = org_hr.hr_db_id
                         AND org_org_hr.hr_id = org_hr.hr_id

    INNER JOIN org_org user_org ON user_org.org_db_id = org_org_hr.org_db_id
                               AND user_org.org_id = org_org_hr.org_id

    INNER JOIN org_org user_company ON user_company.org_db_id =
                                       user_org.company_org_db_id
                                   AND user_company.org_id =
                                       user_org.company_org_id
    WHERE org_hr.user_id = an_CurrUserId
      AND org_org_hr.default_org_bool = 1
      AND user_company.org_type_cd = 'ADMIN';

   IF v_count > 0
   THEN
      RETURN 1;
   ELSE
      SELECT COUNT(*)
        INTO v_count
        FROM org_hr
       INNER JOIN org_org_hr ON org_org_hr.hr_db_id = org_hr.hr_db_id
                            AND org_org_hr.hr_id = org_hr.hr_id

       INNER JOIN org_org user_org ON user_org.org_db_id = org_org_hr.org_db_id
                                  AND user_org.org_id = org_org_hr.org_id

       INNER JOIN po_header ON po_header.created_by_org_db_id =
                               user_org.company_org_db_id
                           AND po_header.created_by_org_id =
                               user_org.company_org_id
       WHERE org_hr.user_id = an_CurrUserId
         AND po_header.po_db_id = an_PoDbId
         AND po_header.po_id = an_PoId
         AND org_org_hr.default_org_bool = 1;

      IF v_count > 0
      THEN
         RETURN 1;
      ELSE
         RETURN 0;
      END IF;
   END IF;

END is_user_auth_to_view_order;


PROCEDURE updateAllUsersTempRoles
(
   aDate   IN DATE
) IS
   -- Cursor that runs selects each rule id from the utl_rule table
   CURSOR  lcur_AllUsers IS
   SELECT  user_id
     FROM  UTL_USER
    ORDER
       BY  user_id;
BEGIN
   FOR lrec_AllUsers IN lcur_AllUsers LOOP
      updateUserTempRoles(lrec_AllUsers.user_id, aDate);
   END LOOP;
END;

PROCEDURE updateUserTempRoles
(
   aUserId IN utl_user.user_id%TYPE,
   aDate   IN DATE
) IS

lUtlId NUMBER;
lChangeCount NUMBER;

BEGIN
   
   -- Check for changes
   SELECT 
      SUM(change_count)
   INTO 
      lChangeCount
   FROM (
        SELECT 
            COUNT(*) AS change_count
        
        FROM 
            utl_user_role
        WHERE
            utl_user_role.user_id = aUserId
            AND
            utl_user_role.temp_bool = 1
            AND
            utl_user_role.role_id NOT IN (
                           SELECT
                              utl_user_temp_role.role_id
                           FROM
                              utl_user_temp_role
                           WHERE
                              utl_user_temp_role.user_id = utl_user_role.user_id
                              AND
                              aDate BETWEEN start_dt AND end_dt
                              AND
                              utl_user_temp_role.unassigned_dt IS NULL
                        )
                  
         UNION ALL
                  
         SELECT COUNT(*) AS change_count
            FROM (
            SELECT
               utl_user_temp_role.user_id,
               utl_user_temp_role.role_id
            FROM
               utl_user_temp_role
            WHERE
               user_id = aUserId
               AND
               aDate BETWEEN start_dt AND end_dt
               AND
               unassigned_by IS NULL
               AND
               -- if there is no same role already assigned
               role_id NOT IN (
                  SELECT
                     role_id
                  FROM
                     utl_user_role
                  WHERE
                     utl_user_role.user_id = utl_user_temp_role.user_id
               )
         )
         
       );
   
   -- Dont execute any change statements against table if no changes exist
   IF lChangeCount = 0 THEN
      RETURN;
   END IF;
   
   -- Retrieving MIM DB ID
   SELECT
      db_id
   INTO
      lUtlId
   FROM
      mim_local_db;
   
   -- Remove expired temporary roles for given user by given date
   DELETE FROM
      utl_user_role
   WHERE
      utl_user_role.user_id = aUserId
      AND
      utl_user_role.temp_bool = 1
      AND
      utl_user_role.role_id NOT IN (
         SELECT
            utl_user_temp_role.role_id
         FROM
            utl_user_temp_role
         WHERE
            utl_user_temp_role.user_id = utl_user_role.user_id
            AND
            aDate BETWEEN start_dt AND end_dt
            AND
            utl_user_temp_role.unassigned_dt IS NULL
      );
      
   -- Adding temporary roles to UTL_USER_ROLE table
   MERGE INTO utl_user_role ur
   USING (
      SELECT DISTINCT
         utl_user_temp_role.user_id,
         utl_user_temp_role.role_id,
         (
            -- calculating next available Role Order
            SELECT
               MAX(utl_user_role.role_order)+1
            FROM
               utl_user_role
            WHERE
               utl_user_role.user_id = utl_user_temp_role.user_id
         ) role_order
      FROM
         utl_user_temp_role
      WHERE
         user_id = aUserId
         AND
         aDate BETWEEN start_dt AND end_dt
         AND
         unassigned_by IS NULL
         AND
         -- if there is no same role already assigned
         role_id NOT IN (
            SELECT
               role_id
            FROM
               utl_user_role
            WHERE
               utl_user_role.user_id = utl_user_temp_role.user_id
         )
      ) utr
   ON (
      ur.user_id = utr.user_id 
      AND
      ur.role_id = utr.role_id
   )
   WHEN NOT MATCHED THEN
      INSERT (user_id, role_id, role_order, utl_id, temp_bool)
      VALUES (utr.user_id, utr.role_id, utr.role_order, lUtlId, 1);
      
END updateUserTempRoles;

END USER_PKG;
/