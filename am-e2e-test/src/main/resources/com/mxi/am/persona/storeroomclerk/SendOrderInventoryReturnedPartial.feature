@CSI @FinanceAPI
Feature: Send Order Inventory Returned - Partial Return

  Background:
    Given I am a superuser
    Given that the Create Order page has been navigated to

	@RefactorNonSmoke
  Scenario: Receive, Inspect as Serviceable a BATCH part which DOES require inspection, Create partial RTV and complete RTV shipment
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
    When I complete the Return to Vendor shipment
    Then Order Inventory Returned message is sent
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number | Manufacturer Code |
      | B000001 |                 3 | EA            |               3 | EA          |                 1 |        1234567890 |
