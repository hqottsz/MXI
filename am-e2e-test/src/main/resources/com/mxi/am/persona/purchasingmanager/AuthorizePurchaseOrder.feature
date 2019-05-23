@PurchaseOrder
Feature: Authorize a PO

    @E2ESmokeBuild1
	Scenario: A Purchase Order is authorized by Purchase Manager
		Given A Purchasing Agent unauthorizes and requests authorization for a PO
	      And I am a purchase manager
	      And I have a purchase order to be authorized
	     When I authorize the purchase order
	     Then the purchase order is authorized
