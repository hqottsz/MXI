--liquibase formatted sql


--changeSet isApplicable:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isApplicable
* Arguments:     as_range - applicability range
*                as_code  - applicability code
* Description:   This function determines if the applicability code is present
*                within the applicability range
*
* Orig.Coder:    Sylvain Charette
* Recent Coder:  edo
* Recent Date:   July 2, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isApplicable
(
    as_range STRING,
    as_code  STRING
)  RETURN NUMBER
IS
   ln_Applicable NUMBER;
   ln_Comma      NUMBER;
   ln_Hyphen     NUMBER;
   ls_Token      VARCHAR2(4000);
   ls_String     VARCHAR2(4000);
   ls_Min        VARCHAR2(4000);
   ls_Max        VARCHAR2(4000);
BEGIN
   -- initialize variables
   ln_Applicable := 0;
   ln_Comma  := 1;


   -- if the range is N/A then consider it is not applicable
   IF ( as_range = 'N/A') THEN
      RETURN 0;
   END IF;

   -- if the range or code is null, then consider it applicable
   IF ( as_range IS NULL ) OR ( as_range = '' ) OR ( as_code IS NULL ) OR ( as_code = '' ) THEN
      RETURN 1;
   END IF;
   

   -- remove any new line characters from the range
   ls_String := replace( replace( replace(as_range, CHR(10)), CHR(13)), CHR(09));

   WHILE ln_Comma > 0 LOOP
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

      -- find the index of the hyphen in the first token
      ln_Hyphen := instr( ls_Token, '-' );

      IF ln_Hyphen > 0 THEN
         -- separate the minimum value from the maximum value
         ls_Min := substr( ls_Token, 1, ln_Hyphen - 1 );
         ls_Max := substr( ls_Token, ln_Hyphen + 1 );

         -- if the code is between these two values, the code is applicable
         IF ( length( as_code ) = length( ls_Min ) ) AND ( as_code >= ls_Min ) AND ( as_code <= ls_Max ) THEN
            ln_Applicable := 1;
         END IF;
      ELSE
         -- if the code matches the entire token, the code is applicable
         IF ( as_code = ls_Token ) THEN
            ln_Applicable := 1;
         END IF;
      END IF;

      -- if the code is found to be applicable, exit the loop
      EXIT WHEN ln_Applicable = 1;
   END LOOP;

   -- return the applicability
   RETURN ln_Applicable;
END isApplicable;
/