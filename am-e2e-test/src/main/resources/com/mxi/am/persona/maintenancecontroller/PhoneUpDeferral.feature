@RunAMLine @Angular
@MaintenanceController
Feature: Phone Up Deferral

   As a Maintenance Controller
   I want to be able to capture details of a fault over the phone from a Line Technician
   So that I can make a decision to raise an open fault or authorize a deferral


	@E2ESmokeBuild1 @AuthorizeDeferral @Ignore
	Scenario: Maintenance Controller authorizes a fault deferral using the Phone Up Deferral workflow
		Given I am a maintenance controller
		When I authorize a phone up deferral
        Then the deferred fault is authorized

	@E2ESmokeBuild1 @Ignore
	Scenario: Maintenance Controller raises an open fault using the Phone Up Deferral workflow
	    Given I am a maintenance controller
	    When I raise an open fault through a phone up deferral
	    Then the fault is raised successfully
