@QualityInspector
Feature: Inspecting Inventory as Unserviceable from Exchange Order

    As an Inspector, I need to deal with quarantined received inventory from Exchange order because it is unserviceable,
    I inspect it as unserviceable then repair router can start to repair it.

	@E2ESmokeBuild1
    Scenario: Select item(s) from Quarantine To Do List
      Given multiple inventories received from an Exchange order are put under quarantine
      And Inspect Unserviceable button is not activated when we select multiple items
	  When a single item is selected
	  Then Inspects Unserviceable button is activated
	   And Marks as Repair Required is not operated

