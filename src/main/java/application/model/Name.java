package application.model;

import java.util.Locale;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Name {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Locale language;
  private String value;

  public Name() {
  }

  public Name(Long id, Locale language, String value) {
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
    if (!(o instanceof Name)) {
      return false;
    }
    Name name = (Name) o;
    return Objects.equals(id, name.id) &&
        Objects.equals(language, name.language) &&
        Objects.equals(value, name.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, language, value);
  }

  @Override
  public String toString() {
    return "Name{" +
        "id=" + id +
        ", language=" + language +
        ", value='" + value + '\'' +
        '}';
  }
}
