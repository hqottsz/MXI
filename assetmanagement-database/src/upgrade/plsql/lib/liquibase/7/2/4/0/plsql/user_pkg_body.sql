--liquibase formatted sql


--changeSet user_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


END USER_PKG;
/