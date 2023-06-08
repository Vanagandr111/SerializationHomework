package ru.netology;

import java.util.Locale;

public enum BasketType {
  JSON,
  TEXT;

  public static BasketType getTypeFromString(String type) {
    if(type.toLowerCase(Locale.US).equals("json"))
      return JSON;
    else if(type.toLowerCase(Locale.US).equals("text"))
      return TEXT;

    return JSON;
  }
}
