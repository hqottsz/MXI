--liquibase formatted sql


--changeSet BaselineSyntaxErrorList_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY BaselineSyntaxErrorList IS

/*--------------------- Package Instance Variables -----------------------------*/


   itab_ErrorList  typtabrec_MessageTable;    -- Table with failed rules

/***********************************************************************************
*
* Package:       BaselineSyntaxErrorList
*
* Description: This package is a storage location that contains a compiled
*               list of error message entries as a result of running the
*               BaselineSyntaxRuleChecker package.
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
*  Procedure:    InitializeMessageList
*
*  Arguments:   on_ReturnStatus  OUT  number  -- return status
*
*  Description:  This procedure deletes all entries in the PL/SQL table.
*
*  Orig. Coder:  T. Abbott
*  Recent Coder: T. Abbott
*  Recent Date:  February 26, 2002
*
*  Version:      1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InitializeMessageList    ( on_ReturnStatus  OUT number
                                 )
IS

BEGIN

    -- deletes ErrorList table
    itab_ErrorList.DELETE;

    on_ReturnStatus := icn_Success;

END InitializeMessageList;



/********************************************************************************
*
*  Procedure:    AddMessageToList
*
*  Arguments:   X                 IN  number    -- Position in ErrorList table
*                an_Rule#          IN  number    -- Rule number (position in SQLRules table)
*                on_ReturnStatus  OUT  number    -- return status code
*
*  Description:  This procedure adds a new message entry to end of the PL/SQL table.
*
*  Orig. Coder:  T. Abbott
*  Recent Coder: T. Abbott
*  Recent Date:  April 11, 2002
*
*  Version:      1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AddMessageToList    (X                 IN number,  -- Position in ErrorList table
                               an_RuleId         IN STRING,
                               on_ReturnStatus  OUT number)  -- return status code
IS

  lv_Title        varchar2(500);          -- Message title
  lv_Text         varchar2(32679);        -- Message text (Rule logic)
  ln_Severity     number;                 -- Message severity code (0, 1, 5, 20)


BEGIN

-- Selects the MsgTitle, MsgText, and MsgSeverity corresponding to the MsgId (code)
--     from the UTL_RULE table.
BEGIN
 select rule_name,
        rule_ldesc,
        rule_severity_cd
   into lv_Title,
        lv_Text,
        ln_Severity
   from utl_rule
  where rule_id = an_RuleId;
EXCEPTION
-- For each MsgId that is not found in the UTL_RULE table there is a statement
--     outputted indicating that the Message code could not be found in the database.
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE ('/* Message Code ' || an_RuleId || ' Not Found in Database */');

END;


  -- insert the message properties of the failed rule into the Error table
    itab_ErrorList(X).mtabn_MessageCode      :=  an_RuleId;       -- Short message number
    itab_ErrorList(X).mtabn_MessageTitle     :=  lv_Title;      -- Short message name
    itab_ErrorList(X).mtabn_MessageText      :=  lv_Text;       -- Long message name
    itab_ErrorList(X).mtabn_MessageSeverity  :=  ln_Severity;   -- Seriousness of the message (Informational, Warning, Error)

    on_ReturnStatus := icn_Success;

END AddMessageToList;



/********************************************************************************
*
*  Procedure:    GetMessageCount
*
*  Arguments:   on_ErrorCount    OUT number
*
*  Description:  This procedure returns the number of messages in the message list.
*
*  Orig. Coder:  T. Abbott
*  Recent Coder: T. Abbott
*  Recent Date:  February 26, 2002
*
*  Version:      1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMessageCount    ( on_ErrorCount    OUT number
                                )
IS

BEGIN

  -- Retrieve the number of entries in the table
  on_ErrorCount := itab_ErrorList.count;

END GetMessageCount;


/********************************************************************************
*
*  Procedure:    GetMessage
*
*  Arguments:   an_Rule#          IN  number
*                lrec_Message     OUT  typrec_MessageType
*
*  Description:  This procedure returns the details of a specific message in
*                the PL/SQL array.
*
*  Orig. Coder:   T. Abbott
*  Recent Coder:  T. Abbott
*  Recent Date:   January 31, 2002
*
*  Version:       1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMessage    (an_Rule#          IN number,
                         lrec_Message     OUT typrec_MessageType )
IS

BEGIN

     lrec_Message := itab_ErrorList(an_Rule#);

END GetMessage;


/********************************************************************************
*
*  Procedure:    GetMessageCd
*
*  Arguments:   an_Rule#          IN  number
*                on_ReturnStatus  OUT  number
*                ov_MessageCd     OUT  typv_ShortStr
*
*  Description:  This procedure returns the Message String Code value for a specific message in
*                the PL/SQL array.
*
*  Orig. Coder:   B. Smith
*  Recent Coder:  B. Smith
*  Recent Date:   March 13, 2002
*
*  Version:       1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetMessageCD          (an_Rule#          IN number,
                                 on_ReturnStatus  OUT number,
                                 ov_MessageCd     OUT typv_ShortStr
                                 )
IS

BEGIN

    ov_MessageCd := itab_ErrorList(an_Rule#).mtabn_MessageCode;

    on_ReturnStatus := icn_success;

    EXCEPTION
        WHEN OTHERS THEN
            on_ReturnStatus := icn_error;

END GetMessageCd;


/********************************************************************************
*
*  Procedure:    PrintAllMessages
*
*  Arguments:   on_ReturnStatus  OUT  number
*
*  Description:  This procedure outputs all the messages using the DBMS_OUTPUT.
*                the PL/SQL array.
*
*  Orig. Coder:   T. Abbott
*  Recent Coder:  T. Abbott
*  Recent Date:   April 11, 2002
*
*  Version:       1.0
*
*********************************************************************************
*
*  Copyright © 2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE PrintAllMessages    ( on_ReturnStatus  OUT number
                                 )

IS
  lv_summary       varchar2(60);     -- Summary statement indicating the total number of failed rules.
  ln_SQLlength     number;           -- Length of remainging string.
  lv_SQLstring     varchar2(32679);  -- Remaining string of failed rule.
  lv_Output        varchar2(240);    -- Portion of rule printed.


BEGIN

   DBMS_OUTPUT.NEW_LINE;
   DBMS_OUTPUT.PUT_LINE (' /************************  Error Message List  ************************/ ');
   DBMS_OUTPUT.NEW_LINE;


 -- loop through and print out all messages in Error table
   FOR N in 1..itab_ErrorList.count LOOP


     -- for each failed rule print out the identifying information and the sql code
     DBMS_OUTPUT.PUT_LINE ('/*');
     DBMS_OUTPUT.PUT_LINE ('Code:     ' || itab_ErrorList(N).mtabn_MessageCode);
     DBMS_OUTPUT.PUT_LINE ('Title:    ' || itab_ErrorList(N).mtabn_MessageTitle);
     DBMS_OUTPUT.PUT_LINE ('Text:     ' || itab_ErrorList(N).mtabn_MessageText);
     DBMS_OUTPUT.PUT_LINE ('Severity: ' || itab_ErrorList(N).mtabn_MessageSeverity);
     DBMS_OUTPUT.PUT_LINE ('SQL Code: ' );
     DBMS_OUTPUT.PUT_LINE ('*/');


     select utl_rule.rule_sql
       into lv_SQLstring
       from utl_rule
      where utl_rule.rule_id = itab_ErrorList(N).mtabn_MessageCode;

     -- portion off the code so that when printed it is readable.
     WHILE length (lv_SQLstring) > 0 LOOP

       IF (instr (lv_SQLstring, chr (10)) <> 0) THEN
           ln_SQLLength := instr (lv_SQLstring, chr (10));
       ELSIF (instr (lv_SQLstring, ' ', 60, 1) <> 0) THEN
           ln_SQLLength := instr(lv_SQLstring, ' ', 60, 1);
       ELSE
               ln_SQLLength := length (lv_SQLstring);
       END IF;

       lv_Output    := substr(lv_SQLstring, 1, ln_SQLlength - 1 ) || ' ';
       lv_SQLstring := substr(lv_SQLstring, ln_SQLlength + 1);

       DBMS_OUTPUT.PUT_LINE ('   ' || lv_Output);

     END LOOP;
    -- add a semicolon and spaces inbetween rules
     DBMS_OUTPUT.PUT_LINE (';');
     DBMS_OUTPUT.PUT_LINE (chr (10));
     DBMS_OUTPUT.PUT_LINE (chr (10));

     DBMS_OUTPUT.NEW_LINE;

   END LOOP;

   DBMS_OUTPUT.NEW_LINE;


  -- Create summary statement
  lv_summary := '           The total number of rules that fail is '||itab_ErrorList.count||'.';

   DBMS_OUTPUT.PUT_LINE ('/*');
   DBMS_OUTPUT.PUT_LINE (' ******************************************************************** ');
   DBMS_OUTPUT.PUT_LINE (lv_summary);
   DBMS_OUTPUT.PUT_LINE (' ******************************************************************** ');
   DBMS_OUTPUT.PUT_LINE ('*/');


    on_ReturnStatus := icn_Success;


END PrintAllMessages;


END BaselineSyntaxErrorList;
/