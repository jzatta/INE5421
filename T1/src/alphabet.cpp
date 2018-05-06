
#include "alphabet.h"

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

bool Terminal::isTerminal(char c) {
  return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '&';
}

NonTerminal::NonTerminal(char c) {
  if (c >= 'A' && c <= 'Z')
    index = c - 'A';
  else if (c == '&')
    index = 26;
  else
    throw "Dont belongs to Non Terminal";
}

bool NonTerminal::isNonTerminal(char c) {
  return (c >= 'A' && c <= 'Z') || c == '&';
}
