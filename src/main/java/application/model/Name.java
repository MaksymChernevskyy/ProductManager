package application.model;

import io.swagger.annotations.ApiModelProperty;
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
  @ApiModelProperty(value = "The id of the name.", position = -1)
  private Long id;

  @ApiModelProperty(value = "Setting the language.", example = "EN")
  private String language;

  @ApiModelProperty(value = "Name of the Product.", example = "Name")
  private String value;

  public Name() {
  }

  public Name(Long id, String language, String value) {
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

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
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
        ", language='" + language + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
