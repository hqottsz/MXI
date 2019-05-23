@MaterialController
Feature: Prepare RFB Inventory
As a material controller,
I need to monitor the availability of RFB inventory
So that I can plan build tasks if needed

	Background:
	   Given I am a QC inspector
	   And Ready For Build is enabled

	@E2ESmokeBuild1
	Scenario: Inspector inspects incomplete inventory as serviceable and the inventory is shown in availability tabs
	   Given a TRK inventory which is missing mandatory sub-components
		When I Inspect it as Serviceable
		Then the Inventory Search by Type page and the Part Number, Part Group and Stock Details availability tabs should show the inventory as RFB