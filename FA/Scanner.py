import re

from SymbolTable import SymbolTable
from ProgramInternalForm import PIF
from FiniteAutomata import FiniteAutomata
from utils.regex import STRING_CONSTANT_REGEX


class Scanner:
    def __init__(self):
        self._operators = ['+', '-', '*', '/', '>', '<', '=', '<=', '>=', '==', '!=', '%']
        self._separators = ['[', ']', '{', '}', ';', ':', '(', ')', '\'', '\"']
        self._reserved_words = ['start', 'finish', 'def', 'int', 'char', 'string', 'log', 'read', 'if', 'then', 'else',
                                'while', 'execute', 'break', 'exit']
        # self._reserved_tokens = get_tokens("/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler"
        #                                    "/inputFiles/token.in")

    def needs_look_ahead(self, token):
        return token in ['>', '<', '=', '!']

    def is_reserved_token(self, token):
        return token in self._reserved_words or token in self._operators or token in self._separators

    def is_nr_const(self, token):
        integer_finite_automaton = FiniteAutomata('/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler'
                                                     '/inputFiles/fa/integer.in')

        return integer_finite_automaton.is_accepted(token)

    def is_str_const(self, token):
        result = STRING_CONSTANT_REGEX.search(token)

        return result is not None


    def is_identifier_const(self, token):
        identifier_finite_automaton = FiniteAutomata('/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler'
                                                     '/inputFiles/fa/identifier.in')

        return identifier_finite_automaton.is_accepted(token)


    def scan_by_line(self, file_name):
        symbol_table = SymbolTable()
        program_internal_form = PIF()

        # open file and read all lines
        program_file = open(file_name, 'r')
        lines = program_file.readlines()

        line_count = 0

        for line in lines:
            line_count += 1
            # get all word from file
            line_data = re.split('("[^a-zA-Z0-9\"\']")|([^a-zA-Z0-9\"\'])', line)

            # filter words and eliminate spaces
            line_data = list(filter(None, line_data))
            line_data = map(lambda e: e.strip(), line_data)
            line_data = list(filter(None, line_data))

            omit_next = False

            for i in range(len(line_data)):
                token = line_data[i]
                if not omit_next:
                    if self.needs_look_ahead(token) and line_data[i+1] == '=':
                        program_internal_form.add(token + line_data[i+1], 0)
                        omit_next = True
                    elif self.is_reserved_token(token):
                        program_internal_form.add(token, 0)
                    elif self.is_nr_const(token) or self.is_str_const(token):
                        position = symbol_table.add(token)
                        program_internal_form.add('CONST', position)
                    elif self.is_identifier_const(token):
                        position = symbol_table.add(token)
                        program_internal_form.add('IDENTIFIER', position)
                    else:
                        raise ValueError('Lexical error on token ' + token + ' at line: ' + str(line_count))
                else:
                    omit_next = False

        with open("/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler/outputFiles/ST.out", 'w') as file:
            file.write(str(symbol_table))

        with open("/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler/outputFiles/PIF.out", 'w') as file:
            file.write(str(program_internal_form))

        return symbol_table, program_internal_form, "Lexically correct"


    def scan(self, file_name):
        symbol_table = SymbolTable()
        program_internal_form = PIF()

        # open file and read all lines
        program_file = open(file_name, 'r')
        lines = program_file.read()

        # get all word from file
        line_data = re.split('("[^a-zA-Z0-9]")|([^a-zA-Z0-9])', lines)

        # filter words and eliminate spaces
        line_data = list(filter(None, line_data))
        line_data = map(lambda e: e.strip(), line_data)
        line_data = list(filter(None, line_data))

        print(list(line_data))


        for token in line_data:
            if self.is_reserved_token(token):
                program_internal_form.add(token, 0)
            elif self.is_nr_const(token) or self.is_identifier_const(token):
                position = symbol_table.add(token)
                program_internal_form.add(token, position)
            else:
                raise ValueError('Lexical error on token ' + token)

        return symbol_table, program_internal_form
