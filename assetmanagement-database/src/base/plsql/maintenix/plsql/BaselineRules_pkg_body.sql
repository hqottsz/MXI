--liquibase formatted sql


--changeSet BaselineRules_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY BaselineRules

IS

/***********************************************************************************
*
* Package:		  BaselineRules
*
* Description:	This package collects all the rule_id's from the utl_rule table and
*               puts them in the temporary table: otab_SQLRules. From the rule_ids it
*               finds the corresponding sql code for each rule.
*
* Orig.Coder:   T. Abbott
* Recent Coder:	T. Abbott
* Recent Date:  April 11, 2002
*
************************************************************************************
*
* Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
***********************************************************************************/

PROCEDURE BaselineRulesTable (an_Rule#       in number,    -- current rule number
                              ov_code       out varchar2,  -- current error message number
                              ov_sql        out varchar2)  -- current SQL code
IS

   ln_count  NUMBER (10);

   otab_SQLRules  typtabrec_SQLTable;

   -- Cursor that runs selects each rule id from the utl_rule table
   CURSOR  lcur_UtlRule IS
   SELECT  rule_id
     FROM  UTL_RULE
    ORDER
       BY  rule_id;

 BEGIN

    -- Load the rule message and SQL text from the database UTL_RULE table.

    ln_count := 1;

    FOR lrec_UtlRule IN lcur_UtlRule LOOP
        -- puts the rule_ids (Message Code) in the SQLRules temporary table
        otab_SQLRules(ln_count).mtabn_MessageCode      :=  lrec_UtlRule.rule_id;       -- Short message number

        ln_count := ln_count + 1;

    END LOOP;

    -- Message Code for the specified rule number
    ov_code := otab_SQLRules(an_Rule#).mtabn_MessageCode;

    -- Retrieves the corresponding sql code for the rule
    select utl_rule.rule_sql
      into ov_sql
      from utl_rule
     where utl_rule.rule_id = ov_code;

 END BaselineRulesTable;

END BaselineRules;

/