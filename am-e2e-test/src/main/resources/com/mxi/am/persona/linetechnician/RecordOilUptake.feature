@RunAMLine
@Angular
Feature: Record Oil Uptake

    As a Line Technician
    I would like to record oil uptake for assemblies on an aircraft
    So that consumption patterns can be monitored

    Background:
        Given I am a line technician

	@E2ESmokeBuild1
    Scenario: Line Technician records oil uptake
        Given I want to record oil uptake for an aircraft
        When I submit the oil uptake values for the assemblies
        Then the oil uptake records are saved
