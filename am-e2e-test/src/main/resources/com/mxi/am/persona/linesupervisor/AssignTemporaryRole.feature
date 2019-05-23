@RunAMBaselineMaintenance
Feature: Assign Temporary HvyLead Role

	@E2ESmokeBuild1
	Scenario: Line supervisor temporarily assigns heavy lead to a line technician
		Given I am able to assign temporary roles
		And I am able to unassign temporary roles
		And I am a line supervisor
		When I find a line technician in my department
        And I assign a temporary role to the user for today
        Then the temporary role is assigned to the technician
