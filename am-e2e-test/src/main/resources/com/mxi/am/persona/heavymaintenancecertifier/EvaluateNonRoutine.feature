Feature: Evaluate Non-Routine

  Persona - Heavy Maintenance Certifier
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Evaluate Non-Routine
    Given I have an Aircraft with an Open Fault
     When I mark the Fault as Evaluated
     Then the Fault is marked as being evaluated