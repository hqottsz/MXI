@RunAMLine @Angular
@LineTechnician
Feature: Select Deferral Reference

	As a Line Technician
	I would like to be able to select a deferral reference for a fault
	So that I can link a reference to my fault

    @E2ESmokeBuild1
    Scenario: Line Technician selects a deferral reference
        Given I am a line technician
        And there is an open fault
        When I select a deferral reference
        Then the deferral reference is selected