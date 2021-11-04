from SymbolTable import SymbolTable

if __name__ == '__main__':
    st = SymbolTable()
    print(st.add("ana"))
    print(st.add("ana"))
    print(st.add("octavian"))
    print(st.add("flaviu"))
    print(st.add(2))
    print(st.add("2"))
    print(st)

    print(st.get("ana"))
    print(st.get(2))
    print(st.get("2"))
