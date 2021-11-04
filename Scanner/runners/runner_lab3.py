from Scanner import Scanner

if __name__ == '__main__':
    scanner = Scanner()

    try:
        st, pif, message = scanner.scan_by_line("/Users/anamaria/Documents/Semester5/LFTC/mini-language-compiler"
                               "/inputFiles/p1.txt")
        print("st: ", st)
        print("pif: ", pif)
        print(message)
    except ValueError as ve:
        print(ve)
