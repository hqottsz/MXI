@MaterialController
Feature: Pick Inventory for Stock Distribution Request
As a material controller,
I need to be able to pick inventory for a stock distribution request

	Background:
	   Given I am a material controller

	@E2ESmokeBuild1
	Scenario: Inventory is picked for a stock distribution request
	   Given I have a distribution request generated for a warehouse stock level
		When I pick item for the distribution request
		Then the request can be completed successfully