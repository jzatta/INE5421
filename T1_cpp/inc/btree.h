
#pragma once

// Binary tree
template<typename T>
class BTree {
  
  BTree(T &_data, BTree *_father = NULL) {
    data  = _data;
    left  = NULL;
    right = NULL;
    father = _father;
  }
  
  virtual ~BTree()  {
    if (left != NULL)
      delete left;
    if (right != NULL)
      delete right;
  }
  
  void setLeft(T &data) {
    if (left != NULL)
      delete left;
    left = new BTree(data, this);
  }
  
  BTree *getLeft() {
    return left;
  }
  
  void setRight(T &data) {
    if (right != NULL)
      delete right;
    right = new BTree(data, this);
  }
  
  BTree *getRight() {
    return right;
  }
  
  BTree *getFather() {
    return father;
  }
  
  T &getData() {
    return data;
  }
  
  void setData(T &_data) {
    data = _data;
  }
  
  bool isLeaf() {
    if ((left == NULL) && (right == NULL))
      return true;
    else
      return false;
  }
  
private:
  BTree *father;
  BTree *left;
  BTree *right;
  T data;
};
