package com.gabchak.example.constants;

import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {
  public static final int DEFAULT_NUMBER_ITEMS_PER_PAGE = 10;
  public static final int FIRST_PAGE = 0;

  public static final String DATE_MONTH_AGO = LocalDate.now().minus(Period.ofMonths(1)).toString();
  public static final String DATE_NOW = LocalDate.now().toString();
}
