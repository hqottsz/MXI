Feature: Create Adhoc Task

  Persona - Production Planner
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Create Adhoc Task from Work Package
    Given I have a work package
     When I create an Adhoc Task in the work package
     Then I can see the Adhoc Task in the work package