--liquibase formatted sql


--changeSet BaselineSyntaxErrorList_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BaselineSyntaxErrorList

IS

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


/**********************************************************************************
* SUBTYPE DECLARATIONS
***********************************************************************************/

   -- Variables used for SUBTYPE Declarations
   iv_LongString   VARCHAR2(32679);   -- size of strings used for SQL
   iv_MedString    VARCHAR2(500);     -- size of strings for table names
   iv_ShortString  VARCHAR2(30);      -- size of strings for table names

   -- Declarations
   SUBTYPE typv_LongStr   IS iv_LongString%TYPE;  -- Subtype for long strings
   SUBTYPE typv_MedStr    IS iv_MedString%TYPE;   -- Subtype for medium strings
   SUBTYPE typv_ShortStr  IS iv_ShortString%TYPE; -- Subtype for Short strings


/**********************************************************************************
* RECORD/TABLE DECLARATIONS
***********************************************************************************/

   TYPE typrec_MessageType IS RECORD
   (
      mtabn_MessageCode       typv_ShortStr,   -- Short message number
      mtabn_MessageTitle      typv_MedStr,     -- Medium message name
      mtabn_MessageText       typv_LongStr,    -- Long message name
      mtabn_MessageSeverity   typv_ShortStr    -- Seriousness of the message
   );

   TYPE typtabrec_MessageTable IS TABLE OF typrec_MessageType index by binary_integer;


   TYPE typrec_SQLType IS RECORD
   (
      mtabn_MessageCode       typv_ShortStr   -- Short message number
   );


   TYPE typtabrec_SQLTable     IS TABLE OF typrec_SQLType     index by binary_integer;

   otab_SQLRules  typtabrec_SQLTable;


/**********************************************************************************
* PACKAGE CONSTANTS
***********************************************************************************/

   icn_Success    CONSTANT NUMBER :=  0;

   icn_Error      CONSTANT NUMBER :=  1;

   icn_SQLError   CONSTANT NUMBER := -1;





/**********************************************************************************
* PROCEDURE DECLARATIONS
***********************************************************************************/

   PROCEDURE InitializeMessageList (on_ReturnStatus  OUT number
                                    );

   PROCEDURE AddMessageToList      (X                 IN number,
                                    an_RuleId         IN STRING,
                                    on_ReturnStatus  OUT number );

   PROCEDURE GetMessageCount       (on_ErrorCount    OUT number
                                    );

   PROCEDURE GetMessage            (an_Rule#          IN number,
                                    lrec_Message     OUT typrec_MessageType
                                    );

   PROCEDURE GetMessageCD          (an_Rule#          IN number,
                                    on_ReturnStatus  OUT number,
                                    ov_MessageCd     OUT typv_ShortStr
                                    );

   PROCEDURE PrintAllMessages      (on_ReturnStatus  OUT number
                                   );



END BaselineSyntaxErrorList;
/