@Engineer @RunAMEngineering
Feature: Configure assigned assembly sensitivities

	As an engineer
	I would like to assign sensitivities to an assembly
	So that I may configure those sensitivities on systems and part groups on that assembly

	Background:
	    Given I am an engineer

	@E2ESmokeBuild1 @AssignAssemblySensitivitiesSetupData
	@Ignore
    Scenario: System Engineer assigns sensitivities to an assembly
       Given I am assigning sensitivities to an assembly
       When I modify the sensitivities for the assembly
       Then I should see the sensitivities tab with the updated list of sensitivities