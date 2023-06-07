import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

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

  @Override
  public String toString() {
    return "Product{name=" + name + ",price=" + price + "}";
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;

    Product other = (Product) obj;
    return name.equals(other.getName()) && price == other.getPrice();
  }
}