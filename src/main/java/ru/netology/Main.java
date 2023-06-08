package ru.netology;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static PrintStream SYSOUT = System.out;
  public static List<Product> products = new ArrayList<>();

  public static void main(String[] args) {
    products.add(0, new Product("Молоко", 50));
    products.add(1, new Product("Хлеб", 14));
    products.add(2, new Product("Гречневая крупа", 80));

    File basketFileJson = new File("basket.json");
    File logFile = new File("log.csv");
    ClientLog log = new ClientLog();

    Basket basket = new Basket();
    try {
      basket = loadFromJson(basketFileJson);
    } catch (IOException | ParseException ignored) {}

    Scanner scanner = new Scanner(System.in);
    StringBuilder productsList = new StringBuilder();
    for(Product product: products) {
      productsList.append(products.indexOf(product)).append(" ").append(product.getName()).append(" ").append(product.getPrice()).append(" руб/шт\n");
    }
    SYSOUT.println("Список возможных товаров для покупки:");
    SYSOUT.println(productsList);

    SYSOUT.println("Напишите номер товара и его кол-во, для завершения покупок введите \"end\"");
    while (true) {
      String inputString = scanner.nextLine();
      if (inputString.equals("end")) {
        break;
      }

      String[] inputArray = inputString.split(" ");
      if (inputArray.length != 2) {
        SYSOUT.println("Правильный формат: \"id_товара кол-во_товара\"");
        continue;
      }

      int id; long count;
      try {
        id = Integer.parseInt(inputArray[0]);
        count = Long.parseLong(inputArray[1]);
      } catch (NumberFormatException ex) {
        SYSOUT.println(inputArray.length + "Правильный формат: \"id_товара кол-во_товара\"");
        continue;
      }
      log.log(id, count);

      if (count <= 0) {
        SYSOUT.println("Количество товара должно быть больше 0");
        continue;
      }

      if (!basket.addToCart(id, count)) {
        SYSOUT.println("Нет товара с ID \"" + id + "\"");
        SYSOUT.println("Список возможных товаров для покупки:");
        SYSOUT.println(productsList);
        continue;
      }

      SYSOUT.println("Успешно добавили \"" + products.get(id).getName() + "\" в корзину");
    }

    SYSOUT.println("Ваша корзина: ");
    basket.printCart();
    try {
      //basket.saveTxt(basketFileTxt);
      saveAsJson(basketFileJson, basket);
      log.exportAsCSV(logFile);
    } catch (IOException ex) {
      SYSOUT.println(ex);
    }
  }

  public static Basket loadFromJson(File jsonFile) throws IOException, ParseException, ClassCastException, IndexOutOfBoundsException {
    JSONParser parser = new JSONParser();
    HashMap<Product, Long> productLongHashMap = new HashMap<>();
    try {
      Object obj = parser.parse(new FileReader(jsonFile));
      JSONArray jsonArray = (JSONArray) obj;
      jsonArray.forEach(o -> {
        JSONObject object = (JSONObject) o;
        int productName = Math.toIntExact((long) object.get("productName"));
        long amount = (long) object.get("amount");
        productLongHashMap.put(products.get(productName), amount);
      });
    } catch (IOException | ParseException ignored) {}

    return new Basket(productLongHashMap);
  }

  public static void saveAsJson(File jsonFile, Basket basket) throws IOException {
    JSONArray array = new JSONArray();
    basket.getBasket().forEach((product, aLong) -> {
      JSONObject object = new JSONObject();
      object.put("productName", products.indexOf(product));
      object.put("amount", aLong);
      array.add(object);
    });

    try (FileWriter file = new FileWriter(jsonFile)) {
      file.write(array.toJSONString());
      file.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}