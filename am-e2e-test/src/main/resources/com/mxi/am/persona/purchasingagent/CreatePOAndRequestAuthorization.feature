@PurchaseAgent
Feature: Create Purchase Order and Request Authorization

	@E2ESmokeBuild1
	Scenario: PO is created and Request Authorization for the PO
     Given I am a purchasing agent
       And I have an adhoc part request
      When I create a purchase order
      Then the purchase order is created
      When I request authorization for the purchase order
      Then the Authorization Status of the PO changes to REQUESTED