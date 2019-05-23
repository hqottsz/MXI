--liquibase formatted sql


--changeSet BaselineSyntaxRuleChecker_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY BaselineSyntaxRuleChecker IS

/***********************************************************************************
*
* Package:       BaselineSyntaxRuleChecker
*
* Description: This package runs through the list of rules, executing the equivalent
*               SQL statement (on a rule by rule basis) to generate "positive" result
*               list indicating rule failure.
*
* Orig.Coder:   T. Abbott
* Recent Coder:   T. Abbott
* Recent Date:  April 11, 2002
*
************************************************************************************
*
* Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
***********************************************************************************/



/********************************************************************************
*
*  Procedure:     CheckBaseline
*
*  Arguments:    on_ReturnStatus    OUT  number
*
*  Description:   This procedure tests all the rule SQL statements and then logs new error
*                 messages for the rules that fail.
*
*  Orig. Coder:   T. Abbott
*  Recent Coder:  T. Abbott
*  Recent Date:   April 11, 2002
*
*  Version:        1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckBaseline    (  an_AllSQLFlag       IN  number,
                              on_ReturnStatus    OUT  number
                                 )
IS

   -- Declaration of variables

   ln_Rule#            number;                  -- Current rule number
   ln_ReturnStatus     number;                  -- Procedural return code
   X                   number;                  -- Position of error information in ErrorList table
   ln_Total#ofRules    number;                  -- Total number of rules in the utl_rule table

BEGIN

  -- initialize message list
  BaselineSyntaxErrorList.InitializeMessageList(ln_ReturnStatus);

  X := 0;

  -- retrieve the total number of rules in the utl_rule table
  select count(*)
    into ln_Total#ofRules
    from utl_rule;

  -- loop through each rule
  FOR counter in 1..ln_Total#ofRules LOOP

    ln_Rule# := counter;

    -- Execute each rules sql statement
    BaselineSyntaxRuleChecker.ExecuteSQL (ln_Rule#, ln_ReturnStatus);

    -- Check the return code from the procedure
    If ln_ReturnStatus <> icn_Success or an_AllSQLFlag = 1 Then
      -- fail
      on_ReturnStatus := ln_ReturnStatus;

      X := X + 1;

      -- log new error message
      BaselineSyntaxErrorList.AddMessageToList (X, ln_Rule#, ln_ReturnStatus);

    Else
      -- pass
      on_ReturnStatus := BaselineRules.icn_Success;

    End If;
  END LOOP;

  on_ReturnStatus := ln_ReturnStatus;

END CheckBaseline;



/********************************************************************************
*
*  Procedure:     ExecuteSQL
*
*  Arguments:    an_Rule#          IN  number
*                 on_ReturnStatus  OUT  number
*
*  Description:   This procedure runs a test SQL statement to determine if any
*                 rows exist that will invalidate the corresponding rule.
*
*  Orig. Coder:   T. Abbott
*  Recent Coder:  ysotozaki
*  Recent Date:   January 3, 2007
*
*********************************************************************************
*
*  Copyright © 2002-2007 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE ExecuteSQL    ( an_RuleId          IN  STRING,
                          on_ReturnStatus  OUT  number
                         )
IS

   lv_Rule        varchar2(32679);     -- SQL to be executed
   li_SQLHandle   number(10);          -- Handle for Dynamic SQL cursor
   li_return      number(10);          -- Return value from EXECUTE and FETCH
   li_SqlErrorCd  varchar2(32679);     -- SQL error code

 BEGIN

   -- retrieve the rule code and sql for the rule to be executed
   SELECT utl_rule.rule_sql
     INTO lv_Rule
     FROM utl_rule
    WHERE utl_rule.rule_id = an_RuleId;

      -- Get the Dynamic SQL Handle
      li_SQLHandle := DBMS_SQL.OPEN_CURSOR;

      -- Execute the SQL
      DBMS_SQL.PARSE(li_SQLHandle, lv_Rule, DBMS_SQL.V7);
      li_Return := DBMS_SQL.EXECUTE_AND_FETCH (li_SQLHandle);

    -- Indicate if data found, therefore Failure!
    If li_Return <> 0 Then
         on_ReturnStatus := icn_Error;
    Else
        on_ReturnStatus := icn_Success;
    End if;


      -- Clean up the Dynamic SQL
      DBMS_SQL.CLOSE_CURSOR(li_SQLHandle);


   -- Trap all errors and send the error message to be displayed
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         -- Close the cursor
         DBMS_SQL.CLOSE_CURSOR(li_SQLHandle);

         -- Indicate no data found, therefore Success!
        on_ReturnStatus := icn_Success;

      WHEN OTHERS
      THEN
         -- Close the cursor
         DBMS_SQL.CLOSE_CURSOR(li_SQLHandle);
      li_SqlErrorCd := SQLCODE;
      If  li_SqlErrorCd =0 Then
            on_ReturnStatus := icn_Error;
      Else
            on_ReturnStatus := icn_SQLError;
      End If;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'BaselineSyntaxRuleChecker.ExecuteSQL');

 END ExecuteSQL;


END BaselineSyntaxRuleChecker;
/