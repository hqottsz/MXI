--liquibase formatted sql


--changeSet isInRange:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isInRange
* Arguments:     as_range - range to search
*                as_value - value to test
* Description:   This function determines if a value is within a range.
*                The range may be a collection of comma seperated ranges all of
*                which may contain a minimum and maximum range value seperated 
*                by a hyphen.
*                If the data being tested itself contains a hyphen, then the 
*                min and max range values must be enclosed in double quotes.
*
*                Note: this function is based on isApplicable.sql with the 
*                      addition of the support for double quoted enclosed  
*                      values that may contain hyphens
*
* Orig.Coder:    Al Hogan
* Recent Coder:  
* Recent Date:   Sep 2, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isInRange
(
    as_range  STRING,
    as_value  STRING
)  RETURN NUMBER
IS
   ln_InRange        NUMBER;
   ln_Comma          NUMBER;
   ln_Hyphen         NUMBER;
   ln_FirstDblQuote  NUMBER;
   ln_SecondDblQuote NUMBER;
   ls_Token          VARCHAR2(4000);
   ls_String         VARCHAR2(4000);
   ls_Min            VARCHAR2(4000);
   ls_Max            VARCHAR2(4000);
BEGIN
   -- initialize variables
   ln_InRange := 0;
   ln_Comma   := 1;
   ls_String  := as_range;

   -- if the range is N/A then consider the value as not in range
   IF ( as_range = 'N/A') THEN
      RETURN 0;
   END IF;

   -- if the range or value is null, then consider the value as in range
   IF ( as_range IS NULL ) OR ( as_range = '' ) OR ( as_value IS NULL ) OR ( as_value = '' ) THEN
      RETURN 1;
   END IF;

   WHILE ln_Comma > 0 LOOP
      ls_Min := NULL;
      ls_Max := NULL;

      -- find the index of the first comma
      ln_Comma := instr( ls_String, ',' );

      IF ln_Comma > 0 THEN
         -- separate the first token from the remaining tokens
         ls_Token  := substr( ls_String, 1, ln_Comma - 1 );
         ls_String := substr( ls_String, ln_Comma + 1 );
      ELSE
         -- entire string is a single token
         ls_Token := ls_String;
      END IF;

      ln_FirstDblQuote  := instr( ls_Token, '"' );
      ln_SecondDblQuote := instr( ls_Token, '"', 2 );

      IF ln_FirstDblQuote = 1 AND ln_SecondDblQuote > 1 THEN
         -- the min value is enclosed in double quotes
         ls_Min := substr( ls_Token, 2, (ln_SecondDblQuote - 2) );

         -- check for hyphen beyond closing double quote
         ln_Hyphen := instr( ls_Token, '-', ln_SecondDblQuote );

         IF ln_Hyphen > 0 THEN
            ls_Max := substr( ls_Token, ln_Hyphen + 1 );
         END IF;

      ELSE
         -- the min value is not enclosed in double quotes
         ln_Hyphen := instr( ls_Token, '-' );

         IF ln_Hyphen > 0 THEN
            -- separate the minimum value from the maximum value
	          ls_Min := substr( ls_Token, 1, ln_Hyphen - 1 );
            ls_Max := substr( ls_Token, ln_Hyphen + 1 );
         ELSE
            ls_Min := ls_Token;
         END IF;

      END IF;

      IF ls_Max IS NOT NULL THEN
         ln_FirstDblQuote  := instr( ls_Max, '"' );
         ln_SecondDblQuote := instr( ls_Max, '"', 2 );

         IF ln_FirstDblQuote = 1 AND ln_SecondDblQuote > 1 THEN
            -- the max value is enclosed in double quotes
            ls_Max := substr( ls_Max, 2, (ln_SecondDblQuote - 2) );
         END IF;

         -- check if the value is between the min and max
         IF ( length( as_value ) = length( ls_Min ) ) AND ( as_value >= ls_Min ) AND ( as_value <= ls_Max ) THEN
	          ln_InRange := 1;
         END IF;

      ELSE
         -- since no max, check if the value matches the min
         IF ( as_value = ls_Min ) THEN
	          ln_InRange := 1;
         END IF;

      END IF;

      -- if the code is found to be applicable, exit the loop
      EXIT WHEN ln_InRange = 1;
   END LOOP;

   RETURN ln_InRange;

END isInRange;
/