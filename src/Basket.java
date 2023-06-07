import java.io.*;
import java.util.HashMap;

public class Basket implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private HashMap<Product, Long> basket = new HashMap<>();

  public Basket(HashMap<Product, Long> productsInBasket) {
    this.basket = productsInBasket;
  }

  public Basket() {}

  public boolean addToCart(int productNum, long amount) {
    try {
      Product product = Main.products.get(productNum);
      if(!basket.containsKey(product)) {
        basket.put(product, 0L);
      }
      basket.put(product, basket.get(product) + amount);
    } catch (IndexOutOfBoundsException ex) {
      return false;
    }
    return true;
  }

  public void printCart() {
    long[] totalPrice = new long[1];
    basket.forEach((product, count) -> {
      Main.SYSOUT.println(product.getName() + ", количество: " + count + ", цена: " + product.getPrice() + " руб/шт.");
      totalPrice[0] = totalPrice[0] + count * product.getPrice();
    });
    Main.SYSOUT.println("Итого: " + totalPrice[0] + " руб");
  }

  public void saveBin(File file) throws IOException {
    try (FileOutputStream outputStream = new FileOutputStream(file);
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
      objectOutputStream.writeObject(this);
    }
  }

  public static Basket loadFromBinFile(File textFile) throws Exception {
    try (FileInputStream inputStream = new FileInputStream(textFile);
         ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
      return (Basket) objectInputStream.readObject();
    }
  }

  public void saveTxt(File textFile) throws IOException {
    if(!textFile.exists()) {
      if(textFile.getParentFile() != null) textFile.getParentFile().mkdirs();
      textFile.createNewFile();
    }
    try (PrintWriter out = new PrintWriter(textFile)) {
      basket.forEach((product, count) -> out.println(Main.products.indexOf(product) + " " + count));
    }
  }

  public static Basket loadFromTxtFile(File textFile) throws IOException, NumberFormatException {
    try (Reader reader = new FileReader(textFile); BufferedReader bufreader = new BufferedReader(reader)) {
      String configLine;
      HashMap<Product, Long> productsInBasket = new HashMap<>();
      while((configLine = bufreader.readLine()) != null) {
        String[] args = configLine.split(" ");
        int product = Integer.parseInt(args[0]);
        long countOf = Long.parseLong(args[1]);
        productsInBasket.put(Main.products.get(product), countOf);
      }
      return new Basket(productsInBasket);
    }
  }

  @Override
  public String toString() {
    return "Basket[" + basket.toString() + "]";
  }
}
