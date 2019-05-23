@RunAMLine
Feature: Edit Part Requirement

	@E2ESmokeBuild1
   Scenario: User edits a part requirement
       Given I am a line technician
       And there exists a part requirement on a fault
       When I edit the part requirement
       Then the part requirement gets updated
