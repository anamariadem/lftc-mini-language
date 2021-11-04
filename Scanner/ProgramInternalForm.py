class PIF:
    def __init__(self):
        self._data = []

    def add(self, key, pos):
        return self._data.append((key, pos))

    def __str__(self):
        value = ''
        for i in self._data:
            value += str(i) + "\n"

        return value
