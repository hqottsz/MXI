Feature: Create and update Purchase order with re-expedite location
	As a Purchasing Agent,
	I want to create and revise orders with re-expedite locations
	So that the routing of my shipments accurately reflects the path of the ordered items.

	@SmokeTest
	Scenario: The Purchasing Agent creates a PO with a re-expedite location and edits the ordered quantity
		Given I am a purchasing agent
		  And a Purchase order is created with Serialized part and has a re-expedite location
		  And two other batched parts are added to the order
		  And the order is received at the re-expedite location by a Storeroom Clerk
		 When the PO lines are edited with decrease of quantity for one batched part and increase of quantities for other parts
		 Then only one pending shipment exists for both the Purchasing Agent and Storeroom Clerk
		  And the quantities of open shipment lines for the order equal the quantities of order lines