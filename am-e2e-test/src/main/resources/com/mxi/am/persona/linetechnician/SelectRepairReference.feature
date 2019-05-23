@RunAMLine @Angular
@LineTechnician
Feature: Select Reference

	As a Line Technician
	I would like to be able to select a repair reference for a fault
	So that I can link a reference to my fault

    @E2ESmokeBuild1
    Scenario: Line Technician selects a repair reference
        Given I am a line technician
        And there exists an open fault
        When I select a repair reference
        Then the repair reference gets linked