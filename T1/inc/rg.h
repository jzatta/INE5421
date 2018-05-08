
#pragma once

#include <iostream>
#include <stdlib.h>
#include <string>
#include <map>
#include <list>

#include "alphabet.h"

class RG;
class FA;
class RE;

using namespace std;

class RG {
public:
  static RG * parse(std::string &);
  
  RG();
  
 friend ostream &operator<<(ostream &os, const RG &rg) {
    os << "Initial: " << NonTerminal::CHARACTERS[(NonTerminal)rg.initial] << endl;
    os << "Productions:" << endl;
    Production::const_iterator it_map = rg.productions.begin();
    for (; it_map != rg.productions.end(); it_map++) {
      os << NonTerminal::CHARACTERS[it_map->first] << "->";
      list<pair<int, int> >::iterator it_list = it_map->second->begin();
      while (it_list != it_map->second->end()) {
        os << Terminal::CHARACTERS[it_list->first];
        if (!NonTerminal::isEpsilon(it_list->second)) {
          os << NonTerminal::CHARACTERS[it_list->second];
        }
        it_list++;
        if (it_list != it_map->second->end())
          os << "|";
      }
      os << endl;
   }
    return os;
  }
  
private:
  static bool lexicalAnalysis(std::string &);
  static bool sintaxAnalysis(std::string &);
  
  bool parseProd(const std::string &);
  
private:
  typedef map<int,
            list<
              pair<int, int>
            > *
          > Production;
  Production productions;
  NonTerminal initial;
  bool hasEpsilon;
  bool hasRecurtionInitial;
  int countProds;
};
