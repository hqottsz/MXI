Feature: Initialize On-condition Task

  Persona - Production Planner
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Create On-condition Task from Work Package
    Given a work package
     When I create an On-condition Task in the work package
     Then I can see the On-condition Task in the work package