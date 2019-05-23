
Feature: Edit a non-historic Fault

	As a line technician
	I would like to edit an open fault by adding a zone.

    @JspSmokeTest
	Scenario: Editing an open fault by adding a zone will result in not synchronizing the associated panels
	    Given I am a line technician
	    When I edit an open fault by adding a zone
	    Then I see the zone was added to the corrective task without any associated panels