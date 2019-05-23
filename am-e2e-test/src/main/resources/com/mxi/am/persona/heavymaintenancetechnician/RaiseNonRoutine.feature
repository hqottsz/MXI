Feature: Raise Non-Routine

  Persona - Heavy Maintenance Technician
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Raising a Fault from a Work Package
    Given I have an In Work Work Package
     When I raise a Logbook Fault
     Then the Fault is created

  @E2ESmokeBuild1
  Scenario: Raising a Fault from a Task
    Given I have an In Work Task
     When I raise a Fault
     Then the Fault is raised