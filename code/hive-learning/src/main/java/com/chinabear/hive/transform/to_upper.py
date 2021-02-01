import sys

for line in sys.stdin:
    values = line.strip().split("\t")
    print("\t".join(list(map(lambda x: x.upper(), values))))