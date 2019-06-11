package application.generators;

import application.model.Description;
import application.model.Name;
import application.model.Price;
import application.model.Product;

public class ProductGenerator {

  public static Product getRandomProduct(){
    long id = IdGenerator.getNextId();
    Name name = NameGenerator.getRandomName();
    Description description = DescriptionGenerator.getRandomDescription();
    Price price = PriceGenerator.getRandomPrice();
    return new Product(id, name, description, price);
  }
}
