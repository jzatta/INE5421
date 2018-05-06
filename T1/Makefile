
APP = lr

SRCDIR = src
INCDIR = inc
LIBDIR = lib
OBJDIR = obj


SRCS := $(shell find $(SRCDIR) -name "*.cpp")
HEADERS := $(shell find $(INCDIR) -name "*.h")
OBJS := $(subst $(SRCDIR),$(OBJDIR),$(SRCS))
OBJS := $(subst .cpp,.o,$(OBJS))


CC = g++
LD = g++
MKDIR = mkdir
RM = rm


CFLAGS = -O2 -I$(INCDIR)/
# LDFLAGS =





# Rules:
.PHONY: all clean run

all: $(APP)

run: $(APP)
	./$<

test:
	@echo $(SRCS)
	@echo $(OBJS)

$(OBJDIR):
	$(MKDIR) $(OBJDIR)

$(OBJDIR)/%.o: $(SRCDIR)/%.cpp | $(OBJDIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(APP): $(OBJS)
	$(LD) $(LDFLAGS) $^ -o $@

clean:
	$(RM) -rf $(APP) $(OBJDIR)
