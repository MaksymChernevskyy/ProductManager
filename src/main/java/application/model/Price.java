package application.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal price;
  private Currency currency;

  public Price() {
  }

  public Price(Long id, BigDecimal price, Currency currency) {
    this.id = id;
    this.price = price;
    this.currency = currency;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Price)) {
      return false;
    }
    Price price1 = (Price) o;
    return Objects.equals(id, price1.id) &&
        Objects.equals(price, price1.price) &&
        Objects.equals(currency, price1.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, price, currency);
  }

  @Override
  public String toString() {
    return "Price{" +
        "id=" + id +
        ", price=" + price +
        ", currency=" + currency +
        '}';
  }
}
