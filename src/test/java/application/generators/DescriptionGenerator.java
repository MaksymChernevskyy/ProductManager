package application.generators;

import application.model.Description;
import java.util.Locale;

public class DescriptionGenerator {
  public static Description getRandomDescription(){
    long id = IdGenerator.getNextId();
    Locale language = LanguageGenerator.getLanguage();
    String description = WordGenerator.getRandomWord();
    return new Description(id, language, description);
  }

}
