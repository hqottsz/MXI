Feature: Perform Independent Inspection

  Persona - Heavy Maintenance Inspector
  Squad - Operator Base Maintenance

  @E2ESmokeBuild1
  Scenario: Inspecting Work
    Given a task labor is awaiting independent inspection
    Given I am a QC inspector
     When I inspect the labor
     Then the labor is inspected
