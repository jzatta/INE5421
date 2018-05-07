
#pragma once

class Terminal {
public:
  static const char COUNT = 37;
  static constexpr char CHARACTERS[COUNT] = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
    'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    '&'};
    
public:
  static bool isTerminal(char);
  Terminal(char);
  Terminal();
  
private:
  
private:
  int index;
};

class NonTerminal {
public:
  static const char COUNT = 27;
  static constexpr char CHARACTERS[COUNT] = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    '&'};
public:
  static bool isNonTerminal(char);
  NonTerminal(char);
  NonTerminal();
private:
  int index;
};
