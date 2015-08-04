package org.example;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

public class Example {

  @Autowired
  Service service;

  @Inject
  Service service2;

  static class Service {
  }
}
