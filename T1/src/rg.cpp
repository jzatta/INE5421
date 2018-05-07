
#include <iostream>
#include "gr.h"

RG::RG() {
  
}

RG *RG::parse(std::string &s) {
  if (!lexicalAnalysis(s)) {
    throw "Unrecognized Symbols";
  }
  
  if (!sintaxAnalysis(s)) {
    throw "Not an RG";
  }
  
  
  
  return NULL;
}

bool RG::lexicalAnalysis(std::string &s) {
  int length = s.length();
  int index;
  char c;
  
  for (index = 0; index < length; index++) {
    c = s[index];
    if (c == ' ') {
      s.erase(index, 1);
      index--;
      length--;
      continue;
    }
    if (c == '-' || c == '>' || c == '|' || c == '\n')
      continue;
    if (Terminal::isTerminal(c))
      continue;
    if (NonTerminal::isNonTerminal(c))
      continue;
    return false;
  }
  return true;
}

bool RG::sintaxAnalysis(std::string &s) {
  int length = s.length();
  int index;
  char c;
  
  for (index = 0; index < length;) {
    c = s[index++];
    if (!NonTerminal::isNonTerminal(c)) {
      return false;
    }
    if (s[index++] != '-') {
      return false;
    }
    if (s[index++] != '>') {
      return false;
    }
    while (index < length) {
      c = s[index++];
      if (!Terminal::isTerminal(c)) {
        return false;
      }
      c = s[index++];
      if (c == '|') {
        continue;
      }
      else if (c == '\n' || c == '\0') {
        break;
      }
      else if (!NonTerminal::isNonTerminal(c)) {
        return false;
      }
      c = s[index++];
      if (c == '|') {
        continue;
      }
      else if (c == '\n' || c == '\0')
        break;
      return false;
    }
  }
  return true;
}
