
#include "alphabet.h"

const char Terminal::CHARACTERS[COUNT] = {
  '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
  'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
  'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
  '&'};

Terminal::Terminal(char c) {
  if (c >= '0' && c <= '9')
    index = c - '0';
  else if (c >= 'a' && c <= 'z')
    index = c - 'a' + 10;
  else if (c == '&')
    index = 36;
  else
    throw "Dont belongs to Terminal";
}

Terminal::Terminal() {
  index = 36;
}

bool Terminal::isTerminal(char c) {
  return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '&';
}




const char NonTerminal::CHARACTERS[COUNT] = {
  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
  'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
  '&'};

NonTerminal::NonTerminal(char c) {
  if (c >= 'A' && c <= 'Z')
    index = c - 'A';
  else if (c == '&')
    index = 26;
  else
    throw "Dont belongs to Non Terminal";
}

NonTerminal::NonTerminal() {
  index = 26;
}

bool NonTerminal::isNonTerminal(char c) {
  return (c >= 'A' && c <= 'Z') || c == '&';
}

bool NonTerminal::isEpsilon(char c) {
  return c == 26;
}
