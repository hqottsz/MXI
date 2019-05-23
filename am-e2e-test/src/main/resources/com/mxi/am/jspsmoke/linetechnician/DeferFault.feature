Feature: Defer Fault

	As a line technician
	I would like to defer an open fault.
	If I forget to enter any mandatory information while deferring the fault and an error message is thrown,
	the information I entered is preserved after the error message is acknowledge.


   @JspSmokeTest
	Scenario: The information entered while deferring a fault remains in the session after acknowledging an error message.
	    Given I am a line technician
	    And I attempt to defer the fault with MEL severity and missing deferral reference information
	    When I receive an error message about the missing deferral reference information and I acknowledge it
	    Then the previous entered information remains in the session