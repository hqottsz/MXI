@RunAMEngineering
Feature: Create and Delete Usage Definition
	As a system engineer
	I want to accurately record usage on assemblies, using create/delete usage definitions
	So that I can identify which usage parameters are tracked against each assembly

	Background:
	    Given I am an engineer

	@E2ESmokeBuild1
	@Usage-Definition-Delete
	Scenario: Create an usage definition
	   Given an aircraft assembly
       When I create a new usage definition for the assembly
       Then the new usage definition is created

   @E2ESmokeBuild1
   @Usage-Definition-Create
	Scenario: Delete an usage definition
	   Given an aircraft assembly with a usage definition
       When I delete the usage definition
       Then the usage definition is deleted