@RunSCMaterials
@ShippingClerk
Feature: Send Shipment

	As a Shipping Clerk,
	I want to be able to keep an accurate, integrated record of the inventory I send from my dock
	in order to minimize my organization's lead time on outgoing shipments.

   	@E2ESmokeBuild1
   	Scenario: The Shipping Clerk ships inventory from his or her dock
       Given I am a superuser
   	     And that an inventory is to be shipped from my dock
       When I ship the inventory
       Then the inventory is in transit