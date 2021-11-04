import re

INT_CONSTANT_REGEX = re.compile(r'0|^[-+]?[1-9]\d*$')
STRING_CONSTANT_REGEX = re.compile(r'\"[a-zA-Z0-9:;?!\.]+\"')
IDENTIFIER_REGEX = re.compile(r'^[a-zA-Z][a-zA-Z0-9]*')
