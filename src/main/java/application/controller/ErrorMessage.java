package application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorMessage {
  private String message;
  private List<String> details;

  protected ErrorMessage() {
  }

  public ErrorMessage(String message) {
    this.message = message;
    details = new ArrayList<>();
  }

  public ErrorMessage(String message, List<String> details) {
    this.message = message;
    this.details = details;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorMessage that = (ErrorMessage) o;
    return Objects.equals(message, that.message)
        && Objects.equals(details, that.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, details);
  }
}
