
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string>

#include "rg.h"

int main(int argc, char **argv) {
  std::string s;
  
//   s += "S -> aS | bB | a | b\nB->a|aS\nA->aA|a";
  
  s += "S -> & | aB | bB | a | b\nB->a|aB\nA->aA|a";
//   s += "S->aS|bB|a|b\nB->a|aS";
  std::cout << s << std::endl;
  
  try {
    RG::parse(s);
  } catch (const char *s) {
    std::cout << "ERROR: " << s << std::endl;
  }
  
  return 0;
}
