@RunAMPlanning @AircraftCapabilities @MaintenanceController
Feature: Retrieve Capabilities By Assembly API Test

	@RefactorNonSmoke
  Scenario: Retrieve capabilities list for an assembly
    Given a capability is added to assembly
    When a Retrieve capabilities by assembly API request is sent to Maintenix
    Then the list of capabilities for the assembly is returned

	@RefactorNonSmoke
	@AfterRetrieveCapabilitiesByAssemblyAPITest
  Scenario: Retrieve capabilities list for an unknown assembly
    When a Retrieve capabilities by assembly API request is sent to Maintenix for an unknown assembly
    Then the retrieve assembly capabilities response returns 4XX level error
