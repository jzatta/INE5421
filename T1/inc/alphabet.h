
#pragma once

#include <iostream>

class Terminal {
public:
  static const char COUNT = 37;
  static const char CHARACTERS[COUNT];
  
public:
  static bool isTerminal(char);
  Terminal(char);
  Terminal();
  
  operator char() {
    CHARACTERS[index];
  }
  
  operator int() {
    return index;
  }
  Terminal &operator=(Terminal i) {
    index = i;
  }
  Terminal &operator=(int i) {
    index = i;
  }
private:
  
private:
  int index;
};

class NonTerminal {
public:
  static const char COUNT = 27;
  static const char CHARACTERS[COUNT];
public:
  static bool isNonTerminal(char);
  static bool isEpsilon(char);
  NonTerminal(char);
  NonTerminal();
  
//   operator char() {
//     CHARACTERS[index];
//   }
  
  operator int() {
    return index;
  }
  
  NonTerminal &operator=(NonTerminal i) {
    index = i.index;
  }
  
  NonTerminal &operator=(int i) {
    index = i;
  }
private:
  int index;
};
