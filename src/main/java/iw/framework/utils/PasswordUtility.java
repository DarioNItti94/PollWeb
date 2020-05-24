package iw.framework.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Primiano Medugno
 * @since 03/02/2020
 */
public class PasswordUtility {
  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String DIGIT = "0123456789";
  private static final String SPECIAL_CHAR = "!@#$%&*()_+-=[]?";
  private static final String PASSWORD_BASE_STRING = CHAR_LOWER + CHAR_UPPER + DIGIT + SPECIAL_CHAR;
  private static final SecureRandom random = new SecureRandom();

  /**
   * Metodo che genera una password casuale di 10 caratteri
   *
   * @return la password generata
   */
  public static String generateRandomPassword () {
    StringBuilder stringBuilder = new StringBuilder();
    String shuffledString = shuffleString();
    int randomCharAt;
    char randomChar;

    for (int i = 0; i < 10; i++) {
      randomCharAt = random.nextInt(shuffledString.length());
      randomChar = shuffledString.charAt(randomCharAt);
      stringBuilder.append(randomChar);
    }
    return stringBuilder.toString();
  }

  /**
   * Mischia in maniera casuale i caratteri nella stringa base
   * contenente i caratteri accettati per la composizione della password
   *
   * @return stringa mischiata
   */
  private static String shuffleString () {
    List<String> letters = Arrays.asList(PASSWORD_BASE_STRING.split(""));
    Collections.shuffle(letters);
    return String.join("", letters);
  }

  /**
   * Genera lo SHA-256 della stringa data in input
   *
   * @param inputString la password da offuscare
   * @return SHA-256 calcolato sull'input
   */
  public static String getSHA256 (String inputString) {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest(inputString.getBytes(StandardCharsets.UTF_8));

      for (byte b : hash) {
        stringBuilder.append(String.format("%02x", b));
      }
    } catch (NoSuchAlgorithmException ex) {
      //TODO gestire l'eccezione
    }
    return stringBuilder.toString();
  }
}
