@RunAMPlanning @AircraftCapabilities @MaintenanceController
Feature: Retrieve Degraded Capabilities By Deferral Reference API Test

	@RefactorNonSmoke
  @Retrieve-Capability-Success
  Scenario: Retrieve Degraded Capabilities for a Deferral Reference
     Given a deferral reference including some degraded capability exists
     When I send a retrieve degraded capabilities for deferral reference API request is sent to Maintenix
     Then the list of degraded capability is returned

	@RefactorNonSmoke
  Scenario: Retrieving Degraded Capabilities for Unknown Deferral Reference**
     When a retrieve degraded capabilities for deferral reference API request is sent to Maintenix for an unknown deferral reference
     Then the retrieve degraded capability response returns a 4XX error
