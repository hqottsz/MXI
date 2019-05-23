@CSI @FinanceAPI
Feature: Send Order Inventory Received - Quarantine

Background:
	Given I am a superuser
	Given that the Create Order page has been navigated to

	@RefactorNonSmoke
Scenario: Over receive BATCH part which does require inspection and therefore is sent to Quarantine
	Given that I have filled in a batch part
		| Batch Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions |   Freight On Board   | Vendor |
		|    B000001    |  AIRPORT1/DOCK   |         2         |       EA      |      AIR (AIR)      |     NET30 (NET30)  |   RECEIPT (RECEIPT)  | 30001  |
	Given that I have requested authorization and issued the order
	Given that I have received the order
		|  Part No  | Receive Shipment Quantity |  Manufactured Date  | Part Type |
		|  B000001  |           5               |     01-JAN-2016     |   Batch   |
	When I inspect the quarantine inventory
		|  Part No  | Update Edit Order Lines Quantity |
		|  B000001  |                5                 |
	Then Receipt Order message is sent and includes order information
		|  Part No  | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number |
		|  B000001  |        3.0        |      EA       |        3.0      |      EA     |         1         |