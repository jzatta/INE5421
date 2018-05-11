

public class Terminal {
  private int symbol;
  public static final int COUNT = 37;
  public static final char CHARACTERS[] = {
  '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
  'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
  'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
  '&'};
  
  public Terminal(char c) {
    if (c >= '0' && c <= '9')
      symbol = c - '0';
    else if (c >= 'a' && c <= 'z')
      symbol = c - 'a' + 10;
    else /*if (c == '&')*/
      symbol = 36;
//     else
//       throw "Dont belongs to Terminal";
  }
  
  public static boolean belongs(char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '&';
  }
  
  public String toString() {
    return Character.toString(CHARACTERS[symbol]);
  }
}
