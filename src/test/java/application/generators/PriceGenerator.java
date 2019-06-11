package application.generators;

import application.model.Price;
import java.math.BigDecimal;
import java.util.Currency;


public class PriceGenerator {

  public static Price getRandomPrice(){
    long id = IdGenerator.getNextId();
    BigDecimal price = randomPrice(9999);
    Currency currency = Currency.getInstance("EUR");
    return new Price(id, price, currency);
  }

  private static BigDecimal randomPrice(int range) {
    BigDecimal max = new BigDecimal(range);
    BigDecimal randFromDouble = new BigDecimal(Math.random());
    BigDecimal actualRandomDec = randFromDouble.multiply(max);
    actualRandomDec = actualRandomDec
        .setScale(2, BigDecimal.ROUND_DOWN);
    return actualRandomDec;
  }
}
