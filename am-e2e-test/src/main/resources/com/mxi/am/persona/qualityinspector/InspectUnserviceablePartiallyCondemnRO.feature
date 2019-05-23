@QualityInspector
Feature: Inspecting Inventory as Unserviceable from Batch Repair Order which has been partially condemned

    As an Inspector, I received a batched item for a Repair order, since it's partially unserviceable,
    I need to be able to inspect it partially unserviceable.

	@RefactorNonSmoke
    Scenario: Inspect serviceable part firstly and then inspect condemned part unserviceable
     Given the inventories received from a repair order are partially condemned and partially serviceable
      And the serviceable item is RFI
      When inspector is blocked for marking the condemned inventory as repair required
	   And he clicks Mark as Inspection Required
	  Then the inventory is Inspection Required
	  Then an Inspector goes to Inspection Required To Do List again and inspects the condemned inventory as unserviceable