CREATE OR REPLACE PACKAGE BODY AUTO_USAGE_CYC IS /* general Usage*/

FUNCTION CYC_C (factor IN NUMBER,
		AT_CYC_M IN NUMBER,
		AT_CYC IN NUMBER,
		AT_CYC_AT_CYC_M IN NUMBER)

		RETURN NUMBER IS
		var_CYC NUMBER; /*Declare Assignable CYC*/

BEGIN

	IF (AT_CYC < AT_CYC_AT_CYC_M) /* Manual reading is more accurate*/
	THEN var_CYC:=AT_CYC_AT_CYC_M;
	ELSE var_CYC:=AT_CYC;
	END IF;

/*This is the calcualtion used to output the calculated parameter*/
RETURN (AT_CYC_M + factor*(var_CYC - AT_CYC_AT_CYC_M));

END CYC_C;

END AUTO_USAGE_CYC;
/