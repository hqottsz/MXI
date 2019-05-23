@CSI @FinanceAPI
Feature: Send Order Inventory Returned - Inventory Marked as Serviceable

  Background:
    Given I am a superuser
    Given that the Create Order page has been navigated to

	@RefactorNonSmoke
  Scenario: Receive, Inspect as Serviceable a SER part which DOES require inspection, Create and complete RTV shipment
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
    Then Order Inventory Returned message is sent
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number | Manufacturer Code |
      | S000003 |                 1 | EA            |               1 | EA          |                 1 |        1234567890 |
