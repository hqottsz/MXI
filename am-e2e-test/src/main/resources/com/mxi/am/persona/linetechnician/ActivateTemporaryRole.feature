@RunAMBaselineMaintenance
Feature: Assign Temporary HvyLead Role

	@RefactorNonSmoke
	Scenario: Line Technician temporarily has the role heavy lead
		Given Line technician has a temporary role assigned
		And I am a line technician
        Then Line technician has the active role
