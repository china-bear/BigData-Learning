import sys

for line in sys.stdin:
    values = line.strip().split("\t")
    if len(values) == 1:
        list_ = eval(values[0])
        for value in list_:
            print("\t".join([str(value)]))