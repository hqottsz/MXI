@MaterialController
Feature: Generate Stock Distribution Request
As a material controller,
I need to make sure a warehouse distribution request is generated when the warehouse stock is below the restock level

	Background:
	   Given I am a material controller
	   And I go to Stock Search

	@E2ESmokeBuild1
	Scenario: Stock Distribution Request is generated for Warehouse Stock Level
	   Given I have a Stock Number set up with a warehouse stock level and the stock low action is Distribution Request
		When the warehouse stock is below restock level
		Then a distribution request is generated for the warehouse stock level