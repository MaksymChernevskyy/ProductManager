package application.model;

import java.util.Locale;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Description {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Locale language;
  private String value;

  public Description() {
  }

  public Description(Long id, Locale language, String value) {
    this.id = id;
    this.language = language;
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Locale getLanguage() {
    return language;
  }

  public void setLanguage(Locale language) {
    this.language = language;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Description)) {
      return false;
    }
    Description that = (Description) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(language, that.language) &&
        Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, language, value);
  }

  @Override
  public String toString() {
    return "Description{" +
        "id=" + id +
        ", language=" + language +
        ", value='" + value + '\'' +
        '}';
  }
}
