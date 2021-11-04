def get_tokens(file_name):
    tokens = []
    with open(file_name, 'r') as file:
        lines = file.readlines()

        for line in lines:
            tokens.append(line.strip())

    return tokens
