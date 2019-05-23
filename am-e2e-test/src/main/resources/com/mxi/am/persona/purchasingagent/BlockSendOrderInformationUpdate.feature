@CSI @FinanceAPI
Feature: Block Send Order Information - Upon Inspection as Serviceable and Return to Vendor

  Background:
    Given I am a superuser
    Given that the Create Order page has been navigated to

	@RefactorNonSmoke
	Scenario: Receive, Inspect as Serviceable an inspection required SER part, Create and complete RTV shipment, No Send Order Information Message
	    Given that I have filled in a serialized part
	      | Serialized Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
	      | S000003            | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
	    Given that I have requested authorization and issued the order
	    Given that I have received the order
	      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type  |
	      | S000003 |                         1 | 01-JAN-2016       | Serialized |
	    Given that I have inspected the inventory as serviceable
	    Given that I have created a Return to Vendor shipment
	    When I complete the Return to Vendor shipment
	    Then Send Order Inventory message version four with action "update" is NOT sent
	      | On Shipment Page |
	      | TRUE             |
