@MaterialController
Feature: Inventory Count

	@Ignore @E2ESmokeBuild1
	Scenario: Able to submit report of unexpected part found
		Given I am a material controller
		And I locate a bin location on Inventory Count page
		When An unexpected part is found
		And I am able to add it to the list
		Then I submit the quantity for this part

	@Ignore @E2ESmokeBuild1
	Scenario: Able to submit cycle count
		Given I am a material controller
		When I count parts for a bin location on Inventory Count | Cycle Count tab
		Then I submit the quantity for these parts