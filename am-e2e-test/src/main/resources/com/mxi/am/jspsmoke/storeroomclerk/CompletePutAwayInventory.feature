Feature: Complete Put Away Inventory

	As a storeroom clerk
	I would like to complete put away inventory

@JspSmokeTest
	Scenario: Validating barcode of inventory when completing the put away of the inventory
	    Given I am a storeroom clerk
	    When I enter an inventory barcode trying to validate for completing a put away
	    Then The barcode is properly validated