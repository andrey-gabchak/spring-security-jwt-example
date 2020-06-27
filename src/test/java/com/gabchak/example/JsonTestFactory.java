package com.gabchak.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonTestFactory {
  public static final ObjectWriter OBJECT_WRITER = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .writer();
}
