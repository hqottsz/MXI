Feature: Certify Completed Work

  Persona - Heavy Maintenance Certifier
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Certifying Work
    Given I have task labor awaiting certification
     When I certiy the labor
     Then the labor is certified and task is COMPLETE