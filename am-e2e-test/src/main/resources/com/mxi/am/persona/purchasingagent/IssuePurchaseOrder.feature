@PurchaseAgent
Feature: Issue a PO

	@E2ESmokeBuild1
	Scenario: A Purchase Order is issued by Purchase Agent

	    Given that I am a purchase agent
		  And I have a purchase order to be issued
		 When I issue the purchase order
		 Then the purchase order is issued
