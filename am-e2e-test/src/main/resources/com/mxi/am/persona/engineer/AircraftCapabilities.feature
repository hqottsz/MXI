@RunAMEngineering @Engineer @AircraftCapabilities
Feature: Aircraft Capabilities Baseline

   Background:
       Given I am an engineer

	@E2ESmokeBuild1
   Scenario: Assign aircraft assembly capabilities
	   When I assign capabilities to an aircraft assembly
	   Then the capability assignment is applied to the aircraft assembly

	# Ignore temporarily due to WebLogic 12.2.1
	@E2ESmokeBuild1 @Ignore
   Scenario: Edit configured capability levels on an aircraft
	   When I edit the configured capability levels of an actual aircraft
	   Then the configured capability updates are applied to the aircraft

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
   Scenario: Edit aircraft assembly capabilities
	   When I edit the capabilities of an aircraft assembly
	   Then the capability updates are applied to the aircraft assembly
	   Then the assembly capability changes have been logged there