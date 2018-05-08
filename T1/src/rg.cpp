
#include <iostream>
#include "rg.h"

RG::RG() {
  hasEpsilon = false;
  hasRecurtionInitial = false;
}

RG *RG::parse(std::string &s) {
  RG *gr;
  size_t posb, pose;
  if (!lexicalAnalysis(s)) {
    throw "Unrecognized Symbols";
  }
  
  if (!sintaxAnalysis(s)) {
    throw "Invalid Sintax";
  }
  
  gr = new RG();
  if (!gr->parseProd(s)) {
    delete gr;
    return NULL;
  }
  cout << *gr;
  
  return NULL;
}

bool RG::parseProd(const std::string &s) {
  int length = s.length();
  int index;
  countProds = 0;
  char c;
  for (index = 0; index < length;) {
    c = s[index++];
    if (!NonTerminal::isNonTerminal(c)) {
      return false;
    }
    NonTerminal state(c);
    if (s[index++] != '-') {
      return false;
    }
    if (s[index++] != '>') {
      return false;
    }
    if (initial == NonTerminal('&')) {
      initial = state;
    }
    productions[state] = new list<pair<int, int> >();
    while (index < length) {
      c = s[index++];
      if (c == '&') {
        if (state == initial && !hasRecurtionInitial) {
          hasEpsilon = true;
        } else {
throw "Is a type 0 grammar";
          return false;
        }
      }
      else if (!Terminal::isTerminal(c)) {
        return false;
      }
      Terminal alpha(c);
      c = s[index++];
      if (c == '|') {
        NonTerminal beta('&');
        std::pair<int, int> t(alpha, beta);
        productions[state]->push_back(t);
        continue;
      }
      else if (c == '\n' || c == '\0') {
        NonTerminal beta('&');
        std::pair<int, int> t(alpha, beta);
        productions[state]->push_back(t);
        break;
      }
      else if (!NonTerminal::isNonTerminal(c)) {
        return false;
      }
      NonTerminal beta(c);
      if (beta == initial) {
        hasRecurtionInitial = true;
        if (hasEpsilon) {
throw "Is a type 0 grammar";
          return false;
        }
      }
      c = s[index++];
      if (c == '|') {
        std::pair<int, int> t(alpha, beta);
        productions[state]->push_back(t);
        continue;
      }
      else if (c == '\n' || c == '\0') {
        std::pair<int, int> t(alpha, beta);
        productions[state]->push_back(t);
        break;
      }
      return false;
    }
    countProds++;
  }
  return true;
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
