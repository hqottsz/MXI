@RunAMPlanning @AircraftCapabilities @MaintenanceController
Feature: Save Degraded Capabilities By Deferral Reference API Test

	@RefactorNonSmoke
  @Save-Degraded-Capability-Tear-Down
  Scenario: Save Degraded Capability To Deferral Reference
    Given a deferral reference exists
    When I send an API message to add a degraded capability to the deferral reference
    Then the degraded capability is added to the deferral reference

	@RefactorNonSmoke
  @Save-Degraded-Capability-Tear-Down
  Scenario: Remove Degraded Capability From Deferral Reference
    Given a deferral reference exists with a degraded capability
	When I send an API message to remove a degraded capability from a deferral reference
	Then the degraded capability is removed from the deferral reference

	@RefactorNonSmoke
  @Save-Degraded-Capability-Tear-Down
  Scenario: Update Degraded Capability In Deferral Reference
    Given a deferral reference exists with a degraded capability
    When I send an API message to change a degraded capability on a deferral reference
    Then the degraded capability is updated for the deferral reference

	@RefactorNonSmoke
  Scenario: Error When Saving Degraded Capability To Deferral Reference
    When I send an API message to add a degraded capability to an unknown deferral reference
    Then the save degraded capability response returns a 4XX error