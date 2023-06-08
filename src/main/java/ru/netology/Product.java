package ru.netology;

public class Product {
  private final String name;
  private final long price;

  Product(String name, int price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }
  public long getPrice() {
    return price;
  }
}
