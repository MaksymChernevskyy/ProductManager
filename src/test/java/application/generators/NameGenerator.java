package application.generators;

import application.model.Name;
import java.util.Locale;

public class NameGenerator {

  public static Name getRandomName(){
    long id = IdGenerator.getNextId();
    Locale language = LanguageGenerator.getLanguage();
    String name = WordGenerator.getRandomWord();
    return new Name(id, language, name);
  }
}
