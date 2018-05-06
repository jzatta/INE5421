
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string>

#include "gr.h"

int main(int argc, char **argv) {
  std::string s;
  
  s += "S -> aS | bB | a | b\nB->a|aS";
//   s += "S->aS|bB|a|b\nB->a|aS";
  std::cout << s << std::endl;
  
  try {
    GR::parse(s);
  } catch (const char *s) {
    std::cout << "ERROR: " << s << std::endl;
  }
  
  return 0;
}
