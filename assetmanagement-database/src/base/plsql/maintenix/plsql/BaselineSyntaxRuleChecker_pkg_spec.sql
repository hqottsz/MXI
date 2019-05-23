--liquibase formatted sql


--changeSet BaselineSyntaxRuleChecker_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BaselineSyntaxRuleChecker
/***********************************************************************************
*
* Package:       BaselineSyntaxRuleChecker
*
* Description: This package runs through the list of rules, executing the equivalent
*               SQL statement (on a rule by rule basis) to generate "positive" result
*               list indicating rule failure.
*
* Orig.Coder:     T. Abbott
* Recent Coder:   ysotozaki
* Recent Date:    January 4, 2007
*
************************************************************************************
*
* Copyright © 2002-2007 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
***********************************************************************************/

IS
   /*------------------------- Type declarations --------------------------------*/

   -- Variables used for SUBTYPE Declarations
   iv_LongString   VARCHAR2(32679);   -- size of strings used for SQL
   iv_ShortString  VARCHAR2(30);      -- size of strings for table names

   -- Declarations
   SUBTYPE typv_LongStr   IS iv_LongString%TYPE;  -- Subtype for long strings
   SUBTYPE typv_ShortStr  IS iv_ShortString%TYPE; -- Subtype for Short strings


   PROCEDURE CheckBaseline ( an_AllSQLFlag      IN  number,
                             on_ReturnStatus   OUT  number
                                 ) ;

   PROCEDURE ExecuteSQL    ( an_RuleId          IN  STRING,
                             on_ReturnStatus  OUT  number
                                 ) ;


   TYPE typrec_MessageType IS RECORD
   (
      mtabn_MessageCode       typv_ShortStr,   -- Short message number
      mtabn_MessageTitle      typv_ShortStr,   -- Short message name
      mtabn_MessageText       typv_LongStr,    -- Long message name
      mtabn_MessageSeverity   typv_ShortStr    -- Seriousness of the message (Informational, Warning, Error)
   );


   TYPE typrec_SQLType IS RECORD
   (
      mtabn_MessageCode       typv_ShortStr  -- Short message number
   );


   TYPE typtabrec_MessageTable IS TABLE OF typrec_MessageType index by binary_integer;

   TYPE typtabrec_SQLTable     IS TABLE OF typrec_SQLType     index by binary_integer;

   otab_SQLRules  typtabrec_SQLTable;


    icn_Success    CONSTANT NUMBER :=  0;

    icn_Error      CONSTANT NUMBER :=  1;

    icn_SQLError   CONSTANT NUMBER := -1;



END BaselineSyntaxRuleChecker;
/