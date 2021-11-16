class FiniteAutomata:
    def __init__(self, conf_file):
        self._states = []
        self._alphabet = []
        # there will be only 1 initial state
        self._initial_states = []
        self._final_states = []
        self._transitions = {}

        self._load_data(conf_file)

    def _classify_token(self, section, probe):
        if section == 'states':
            spec = probe.split(', ')
            self._states.extend(spec)
        elif section == 'alpha':
            spec = probe.split(', ')
            self._alphabet.extend(spec)
        elif section == 'initial':
            spec = probe.split(', ')
            self._initial_states.extend(spec)
        elif section == 'final':
            spec = probe.split(', ')
            self._final_states.extend(spec)
        elif section == "transitions":
            values = probe.split(", ")

            if (values[0], values[1]) in self._transitions.keys():
                if values[2] not in self._transitions[(values[0], values[1])]:
                    self._transitions[(values[0], values[1])].append(values[2])
            else:
                self._transitions[(values[0], values[1])] = [values[2]]

    def _load_data(self, conf_file):
        with open(conf_file, 'r') as file:
            lines = file.readlines()
            section = ''

            for line in lines:
                line = line.strip()
                if line[0] == '#':
                    section = line[1:]
                else:
                    self._classify_token(section, line)

    def is_deterministic(self):
        for key in self._transitions.keys():
            if len(self._transitions[key]) > 1:
                return False
        return True

    def is_accepted(self, sequence):
        if self.is_deterministic():
            current = self._initial_states[0]

            for symbol in sequence:
                if (current, symbol) in self._transitions.keys():
                    current = self._transitions[(current, symbol)][0]
                else:
                    return False

            return current in self._final_states
        return False

    def _print_menu(self):
        print("Display data regarding a finite automata")
        print("1: Display the states")
        print("2: Display the alphabet")
        print("3: Display the transitions")
        print("4: Display the final state")
        print("5: Check if given sequence is accepted")
        print("0: Exit")

    def display_data(self):
        while True:
            self._print_menu()
            user_input = input('>').strip()

            if user_input == '1':
                print('The states of the finite automata are: ', self._states)
            elif user_input == '2':
                print('The alphabet of the finite automata is: ', self._alphabet)
            elif user_input == '3':
                print('The transitions of the finite automata are: ', self._transitions)
            elif user_input == '4':
                print('The final state(s) of the finite automata are: ', self._final_states)
            elif user_input == '5':
                is_accepted = self.is_accepted(input("Sequence: ").strip())
                if is_accepted:
                    print('Accepted')
                else:
                    print('Not accepted')
            elif user_input == '0':
                break
            else:
                print('Wrong command')