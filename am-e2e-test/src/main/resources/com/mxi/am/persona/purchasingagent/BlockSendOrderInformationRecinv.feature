@CSI @FinanceAPI
Feature: Block Send Order Information - Upon Receipt and Inspection as Serviceable

  Background:
    Given I am a superuser
      And that the Create Order page has been navigated to

	@SmokeTest
	Scenario: Receive a BATCH part which does NOT require inspection, Confirm no Send Order Information Message
	    Given that I have filled in a batch part
	      | Batch Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
	      | B000002       | AIRPORT1/DOCK    |                 5 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
	     And that I have requested authorization and issued the order
	    When I receive the order
	      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type |
	      | B000002 |                         5 | 01-JAN-2016       | Batch     |
	    Then Send Order Inventory message version four with action "recinv" is NOT sent
	      | On Shipment Page |
	      | TRUE             |

	@RefactorNonSmoke
	Scenario: Receive and Inspect as Serviceable a SER part which DOES require inspection, Confirm no Send Order Information Message
	    Given that I have filled in a serialized part
	      | Serialized Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
	      | S000003            | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
	    Given that I have requested authorization and issued the order
	    Given that I have received the order
	      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type  |
	      | S000003 |                         1 | 01-JAN-2016       | Serialized |
	    When I inspect the inventory as serviceable
	    Then Send Order Inventory message version four with action "recinv" is NOT sent
	      | On Shipment Page |
	      | FALSE            |
