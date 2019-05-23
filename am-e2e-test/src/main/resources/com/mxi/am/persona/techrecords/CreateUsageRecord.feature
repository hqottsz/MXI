@RunAMTechnicalRecords
Feature: Create Usage Record

    @SmokeTest
    @Create-Usage-Record-Success
	Scenario: Create Usage Record for an assembly
		Given I am a technical records clerk
		When I create the usage record for an assembly
		Then the usage record is created persistently
