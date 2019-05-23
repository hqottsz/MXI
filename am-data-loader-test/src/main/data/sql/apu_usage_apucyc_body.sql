CREATE OR REPLACE PACKAGE BODY APU_USAGE_APUCYC IS

FUNCTION APUCYC_C (factor IN NUMBER,
		APUCYC_M IN NUMBER,
		ACYC IN NUMBER,
		ACYC_AT_APUCYC_M IN NUMBER)

		RETURN NUMBER IS
		var_ACYC NUMBER; /*Declare Assignable ACYC*/

BEGIN

	IF (ACYC < ACYC_AT_APUCYC_M) /* Manual reading is more accurate*/
	THEN var_ACYC:=ACYC_AT_APUCYC_M;
	ELSE var_ACYC:=ACYC;
	END IF;

/*This is the calcualtion used to output the calculated parameter*/
RETURN (APUCYC_M + factor*(var_ACYC - ACYC_AT_APUCYC_M));

END APUCYC_C;

END APU_USAGE_APUCYC;
/