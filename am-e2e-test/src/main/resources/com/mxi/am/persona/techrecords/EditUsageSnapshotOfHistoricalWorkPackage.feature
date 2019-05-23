@RunAMTechnicalRecords
Feature: Edit Usage Snapshot of Historical Work Package

    @SmokeTest
	Scenario: Edit Usage Snapshot for Historic Work Package
		Given I am a technical records clerk
		And I have a historical work package
		When I edit the usage snapshot
		Then the usage snapshot is updated
