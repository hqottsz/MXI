@QualityInspector
Feature: Inspecting Inventory

   The quality inspector is responsible for making sure all inventories are in a good condition to be installed in an aircraft.

	@SmokeTest
   	Scenario: A quality inspector receives an inventory in a good condition

     Given I am a superuser
       And I have a new inventory to inspect
      When I inspect the inventory
       And I agree with the conditions
      Then the inventory is available to use
