Feature: Basic Arithmetic

  Background: A Calculator
    Given Calculator Initialized

  Scenario: Addition
    When Add 4 5
    Then = 9

  Scenario: Subtract
    When Sub 7 2
    Then = 5

  Scenario: Multiply
    When Multi 7 2
    Then = 14

  Scenario:
    When Div 6 2
    Then = 3
