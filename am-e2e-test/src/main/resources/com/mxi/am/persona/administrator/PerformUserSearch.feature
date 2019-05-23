@RunAMLine
Feature: Locate a User via User Search Page

	@E2ESmokeBuild1
    Scenario: Administrator locates a user via User Search
        Given I am a superuser
        When I search for a line technician
        Then I find the line technician
