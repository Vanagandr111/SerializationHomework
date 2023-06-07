import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static PrintStream SYSOUT = System.out;
  public static List<Product> products = new ArrayList<>();

  public static void main(String[] args) {
    products.add(0, new Product("Молоко", 50));
    products.add(1, new Product("Хлеб", 14));
    products.add(2, new Product("Гречневая крупа", 80));

    File basketFile = new File("basket.txt");
    Basket basket = new Basket();
    try {
      basket = Basket.loadFromTxtFile(basketFile);
    } catch (IOException | NumberFormatException ignored) {}

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

      int id; int count;
      try {
        id = Integer.parseInt(inputArray[0]);
        count = Integer.parseInt(inputArray[1]);
      } catch (NumberFormatException ex) {
        SYSOUT.println(inputArray.length + "Правильный формат: \"id_товара кол-во_товара\"");
        continue;
      }
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
      basket.saveTxt(basketFile);
    } catch (IOException ex) {
      SYSOUT.println(ex);
    }
  }
}