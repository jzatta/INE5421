
#pragma once

#include <stdlib.h>
#include <string>
#include <map>
#include <list>

#include "alphabet.h"

class RG;
class FA;
class RE;

class RG {
public:
  static RG * parse(std::string &);
  
  RG();
  
private:
  static bool lexicalAnalysis(std::string &);
  static bool sintaxAnalysis(std::string &);
  
private:
  typedef std::map<NonTerminal,
                    std::list<std::pair<Terminal, NonTerminal>>> Production;
  Production productions;
};
