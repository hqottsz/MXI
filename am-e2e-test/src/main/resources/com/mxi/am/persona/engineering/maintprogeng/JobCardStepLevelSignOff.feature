@RunAMBaseMaintenance
Feature: Job Card Step Level Sign Off

  Persona - Maintenance Program Engineer
  Squad   - Operator Base Maintenance

  @E2ESmokeBuild1
  Scenario: Add Job Card Step
    Given I am a superuser
     When I add Job Card Step
     Then the Job Card Step is added