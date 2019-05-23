@RunAMPlanning @MaintenancePlanner @FlightCapabilityRequirement
Feature: Flight Capability Requirement

   Background:
      Given I am a line planner

	@E2ESmokeBuild1
   Scenario: Multiple Flight Capability Requirements can be added to a flight
	  When a flight message with capability requirements is received
	  Then the capability requirements are added to the flight

	@RefactorNonSmoke
   Scenario: Multiple Flight Capability Requirements can be updated to a plan flight
      When a plan flight updated message with capability requirements is received
      Then the capability requirements are updated to the flight

