

public class NonTerminal {
  private int symbol;
  public static final int COUNT = 27;
  public static final char CHARACTERS[] = {
  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
  'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
  '&'};
  
  public NonTerminal(char c) {
    if (c >= 'A' && c <= 'Z')
      symbol = c - 'A';
    else /*if (c == '&')*/
      symbol = 26;
//     else
//       throw "Dont belongs to Terminal";
  }
  
  public static boolean belongs(char c) {
    return (c >= 'A' && c <= 'Z') || c == '&';
  }
  
  public String toString() {
    return Character.toString(CHARACTERS[symbol]);
  }
}
