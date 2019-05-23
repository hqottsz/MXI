@Ignore
@RunAMPlanning
Feature: Raise Logbook Fault

   Background:
       Given I am a superuser

	@RefactorNonSmoke
   Scenario: User raises historic fault using log fault and close option
       When I raise a historic logbook fault for aircraft "Aircraft Part 2 - OPER-4185"
       Then the fault is complete

	@RefactorNonSmoke
	Scenario: User raises historic fault using log fault and close option with configuration changes
       When I raise a historic logbook fault with configuration changes for aircraft "Aircraft Part 2 - OPER-4185"
       And I remove part number "A0000001" serial number "OPER-4185-PO1" and install part number "A0000001" serial number "OPER-4185-P1"
       Then the fault is complete has usage at completion of 1002 cycles and 1010 hours
	   And the installed parts usage is 9999 cycles and 9999 hours
	   And the removed parts usage is 9999 cycles and 9999 hours
	   And I revert configuration parameter SHOW_PARTS_ON_OFF
