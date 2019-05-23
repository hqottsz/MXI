@CSI @FinanceAPI
Feature: Block Order Inventory Returned - Inventory NOT Marked as Serviceable - Inventory Marked as Serviceable With PO Closed

  Background:
    Given I am a superuser
    Given that the Create Order page has been navigated to

	@RefactorNonSmoke
  Scenario: Receive a TRACKED part which DOES require inspection, Create and complete RTV shipment
    Given that I have filled in a tracked part
      | Tracked Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | TRK-MISMATCH    | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No      | Receive Shipment Quantity | Manufactured Date | Part Type |
      | TRK-MISMATCH |                         1 | 01-JAN-2016       | Tracked   |
    Given that I have NOT inspected the inventory as serviceable
    Given that I have created a Return to Vendor shipment
    When I complete the Return to Vendor shipment
    Then Order Inventory Returned message is NOT sent

	@RefactorNonSmoke
  Scenario: Receive, Inspect as Serviceable a BATCH part which DOES require inspection, Create and complete RTV shipment
    Given that I have filled in a batch part
      | Batch Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | B000001       | AIRPORT1/DOCK    |                 5 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type |
      | B000001 |                         5 | 01-JAN-2016       | Batch     |
    Given that I have inspected the inventory as serviceable
    Given that I have created a partial Return to Vendor shipment
      | Part No | RTV Quantity |
      | B000001 |            3 |
    Given that I have closed the PO
    When I complete the Return to Vendor shipment
    Then Order Inventory Returned message is NOT sent
