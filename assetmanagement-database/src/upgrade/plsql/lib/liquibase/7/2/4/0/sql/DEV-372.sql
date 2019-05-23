--liquibase formatted sql


--changeSet DEV-372:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*	This package holds mxi utility pl/sql functions and procedures.
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PACKAGE MX_UTILS_PKG 
IS

/*********************        PACKAGE VARIABLES              *******************/
-- Constant declarations (return codes)
icn_Success			CONSTANT NUMBER := 1;  -- Success
icn_Failure			CONSTANT NUMBER := -1;  -- Unsuccessful

/*********************        PROCEDURES                     *******************/


/*********************        FUNCTIONS                     ********************/

/********************************************************************************
*	Function: toHtmlText
*	
*	Arguments:      aText        string     the string to convert to html
*	Return:         the string converted to html  (clob)
*	Description:    Converts special characters to html equivalents.
*                       May be used for displaying strings in crytal report fields that use html interperation.
*
*                       Note: this is a duplication of code found in com.mxi.mx.common.utils.HtmlUtils.toHtmlText.
*                       Note: we need to return a clob since the max size of a string is ~32k and we convert 
*                             a typical varchar2(4000) of space chars with nbsp it will exceed the string limit.
*   
*	Orig Coder: ahogan
*	Date:       Sept 17, 2009
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION toHtmlText
(
      as_text  STRING
) RETURN CLOB;


/********************************************************************************
*	Function: hashmd5
*	
*	Description:    Constructs a hash value for a text string using the MD5 hash
*                  algorithm
*	Arguments:      aText  VARCHAR2  the text to hash
*	Return:         an MD5 hash of the provided text as a hexadecimal string
* 
*   
*	Orig Coder: scharette
*	Date:       April 1, 2010
*********************************************************************************
*
*  Copyright 2010 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION hashmd5
(
      as_text  VARCHAR2
) RETURN VARCHAR2;



END MX_UTILS_PKG;
/

--changeSet DEV-372:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY MX_UTILS_PKG IS

/*********************        PROCEDURES                     *******************/


/*********************        FUNCTIONS                     ********************/

/********************************************************************************
*	Function: toHtmlText
*	
*	Arguments:      aText        string     the string to convert to html
*	Return:         the string converted to html  (clob)
*	Description:    Converts special characters to html equivalents.
*                       May be used for displaying strings in crytal report fields that use html interperation.
*
*                       Note: this is a duplication of code found in com.mxi.mx.common.utils.HtmlUtils.toHtmlText.
*                       Note: we need to return a clob since the max size of a string is ~32k and we convert 
*                             a typical varchar2(4000) of space chars with nbsp it will exceed the string limit.
*   
*	Orig Coder: ahogan
*	Date:       Sept 17, 2009
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION toHtmlText
(
      as_text  STRING
) RETURN CLOB 
IS
   ln_HtmlText  CLOB;
BEGIN

   ln_HtmlText := as_text;
   ln_HtmlText := replace( ln_HtmlText, '\r\n', '<br>' );
   ln_HtmlText := replace( ln_HtmlText, '\n'  , '<br>' );
   ln_HtmlText := replace( ln_HtmlText, '\t'  , '&' || 'nbsp;&' || 'nbsp;&' || 'nbsp;&' || 'nbsp;&' || 'nbsp;' );
   
   ln_HtmlText := replace( ln_HtmlText, ' '    , '&' || 'nbsp;' );

   -- <br/> do not appear to be interpreted by crystal reports interpretation of html
   ln_HtmlText := replace( ln_HtmlText, '<br/>', '<br>' );
   
   -- take into account oracle's carrage return 
   ln_HtmlText := replace( ln_HtmlText, chr(10), '<br>' );
   
   return ln_HtmlText;
   
END toHtmlText;


/********************************************************************************
*	Function: hashmd5
*	
*	Description:    Constructs a hash value for a text string using the MD5 hash
*                  algorithm
*	Arguments:      aText  VARCHAR2  the text to hash
*	Return:         an MD5 hash of the provided text as a hexadecimal string
* 
*   
*	Orig Coder: scharette
*	Date:       April 1, 2010
*********************************************************************************
*
*  Copyright 2010 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION hashmd5
(
	as_text  VARCHAR2
) RETURN VARCHAR2 
IS BEGIN

	RETURN rawtohex( dbms_obfuscation_toolkit.MD5( input => utl_raw.cast_to_raw( as_text ) ) );

END hashmd5;

   
END MX_UTILS_PKG;
/