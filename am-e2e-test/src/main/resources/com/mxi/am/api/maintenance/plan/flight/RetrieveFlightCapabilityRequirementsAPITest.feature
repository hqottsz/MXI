@RunAMPlanning @AircraftCapabilities @MaintenanceController
Feature: Retrieve Flight Required Capabilities API Test

	@RefactorNonSmoke
	@Retrieve-Capability-Success
	Scenario: Retrieve Flight Required Capabilities for given aircraft
	Given a planned flight exists with required capabilities
	When a retrieve required flight capabilities API request is sent to Maintenix
	Then the list of capabilities required for the flight is returned

	@RefactorNonSmoke
	Scenario: Retrieving Flight Required Capabilities for Unknown Aircraft
	When a retrieve flight required capabilities API request is sent to Maintenix for an unknown aircraft
	Then the retrieve flight required capabilities response returns a 2XX with an empty list
