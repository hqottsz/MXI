@RunAMLine
@LineTechnician
Feature: Damage Record

   Background:
       Given I am a line technician

   @E2ESmokeBuild1
   Scenario: Line Technician adds a damage record to a fault
       When I add a damage record to a fault
       Then I can see the damage record is added to the fault