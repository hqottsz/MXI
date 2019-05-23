CREATE OR REPLACE PACKAGE BODY AUTO_USAGE_HR IS /* general Usage*/

FUNCTION AUTO_HR_C (factor IN NUMBER,
		AT_HR_M IN NUMBER,
		AT_HR IN NUMBER,
		AT_HR_AT_HR_M IN NUMBER)

		RETURN NUMBER IS
		var_AFH NUMBER; /*Declare Assignable AFH*/

BEGIN

	IF (AT_HR < AT_HR_AT_HR_M) /* Manual reading is more accurate*/
	THEN var_AFH:=AT_HR_AT_HR_M;
	ELSE var_AFH:=AT_HR;
	END IF;

/*This is the calcualtion used to output the calculated parameter*/
RETURN (AT_HR_M + factor*(var_AFH - AT_HR_AT_HR_M));

END AUTO_HR_C;

END AUTO_USAGE_HR;
/