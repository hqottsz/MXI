@RunAMLine @Angular
@MaintenanceController
Feature: Deferral Reference operations

    As a maintenance controller
    I want to manage deferral references
    So I can use them to defer faults.

    Background:
    Given I am a maintenance controller
        And I access the Deferral Reference Workflow

	# Ignore temporarily due to WebLogic 12.2.1
    @E2ESmokeBuild1 @Ignore
    Scenario: Maintenance Controller searches for deferral reference
        When I search for the deferral reference from the search results
	    Then I see details for the selected deferral reference

	# Ignore temporarily due to WebLogic 12.2.1
	@E2ESmokeBuild1 @Ignore
	Scenario: Maintenance Controller creates a deferral reference
    When I create a deferral reference
    Then I receive feedback indicating the deferral reference was saved
    	And the deferral reference has been saved in the system

 	@RefactorNonSmoke @CreateDeferralReference
	Scenario: Maintenance Controller edits an existing deferral reference
    When I edit the deferral reference with a custom deadline
	Then I should receive successful feedback of the updated deferral reference
        And the deferral reference has been updated in the system