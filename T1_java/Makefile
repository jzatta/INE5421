
MAINCLASS=LRApp

BINDIR = bin
LIBDIR = lib
SRCDIR = src

SRC  := $(shell find $(SRCDIR)/ -name "*.java")
OBJ  := $(subst $(SRCDIR)/,$(BINDIR)/,$(SRC))
OBJ  := $(subst .java,.class,$(OBJ))

JAVA  = java
JAVAC = javac

.PHONY: all run clean

fast:
	@make -j 8 all

all: $(OBJ)

run: all
	$(JAVA) -cp $(BINDIR) $(MAINCLASS)

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $< -d $(BINDIR) -sourcepath $(SRCDIR)


clean:
	rm -rf bin/*

test:
	@echo $(SRC)
	@echo $(OBJ)
