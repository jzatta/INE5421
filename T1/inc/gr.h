
#pragma once

#include <stdlib.h>
#include <string>
#include <map>
#include <list>

#include "alphabet.h"

class GR;
class AF;
class ER;

class GR {
public:
  static GR * parse(std::string &);
  
  GR();
  
private:
  static bool lexicalAnalysis(std::string &);
  static bool sintaxAnalysis(std::string &);
  
private:
  typedef std::map<NonTerminal,
                    std::list<std::pair<Terminal, NonTerminal>>> Production;
  Production productions;
};
