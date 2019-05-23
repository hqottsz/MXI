--liquibase formatted sql


--changeSet BaselineRules_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BaselineRules

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

/**********************************************************************************
* SUBTYPE DECLARATIONS
***********************************************************************************/

   -- Variables used for SUBTYPE Declarations
   iv_LongString	 VARCHAR2(32679);   -- size of strings used for SQL
   iv_MedString	   VARCHAR2(100);     -- size of strings for table names
   iv_ShortString	 VARCHAR2(30);      -- size of strings for table names

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
      mtabn_MessageTitle      typv_MedStr,     -- Short message name
      mtabn_MessageText       typv_LongStr,    -- Long message name
      mtabn_MessageSeverity   typv_ShortStr    -- Seriousness of the message
   );


   TYPE typrec_SQLType IS RECORD
   (
      mtabn_MessageCode       typv_ShortStr   -- Short message number
   );


   TYPE typtabrec_MessageTable IS TABLE OF typrec_MessageType index by binary_integer;

   TYPE typtabrec_SQLTable     IS TABLE OF typrec_SQLType     index by binary_integer;




/**********************************************************************************
* PACKAGE CONSTANTS
***********************************************************************************/

    icn_Success    CONSTANT NUMBER :=  0;

    icn_Error      CONSTANT NUMBER :=  1;

    icn_SQLError   CONSTANT NUMBER := -1;




/**********************************************************************************
* PROCEDURE DECLARATIONS
***********************************************************************************/

PROCEDURE BaselineRulesTable(an_Rule#       in number,
                              ov_code       out varchar2,
                              ov_sql        out varchar2);

END BaselineRules;

/