package com.gabchak.weather.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

  private HomeController controller;

  @BeforeEach
  void setUp() {
    controller = new HomeController();
  }

  @Test
  void index() {
    String actual = controller.index();
    Assertions.assertEquals(actual, "index");
  }
}