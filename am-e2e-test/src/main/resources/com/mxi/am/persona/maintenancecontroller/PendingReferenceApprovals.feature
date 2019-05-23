@RunAMLine @Angular
@MaintenanceController
Feature: Pending Reference Approvals

	As a Maintenance Controller
	I want to be able to see all pending requests for repairs & deferrals from a Line Technicians
	So that I can authorize or reject the request

	@CreatePendingReferenceRequest @E2ESmokeBuild1
	Scenario: Maintenance Controller authorizes a Pending Reference Request
		Given I am a maintenance controller
		When I authorize a pending request
		Then the pending request should be authorized successfully