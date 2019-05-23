Feature: Job Card Collection

  Persona - Production Controller
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @Ignore
  @E2ESmokeBuild1
  Scenario: Collection Complete Task
    Given a COMPLETE Task is uncollected
     When I collect the Task
     Then the Task is collected

  @Ignore
  @E2ESmokeBuild1
  Scenario: Uncollection Complete Task
    Given a COMPLETE Task is collected
     When I uncollect the Task
     Then the Task is uncollected