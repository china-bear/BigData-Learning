import sys


def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False


last_key = None
sum_ = 0
for line in sys.stdin:
    key, value = line.strip().split("\t")
    if last_key and last_key != key:
        print("\t".join([last_key, str(sum_)]))
        sum_ = 0
    sum_ += float(value) if is_number(value) else 0
    last_key = key

if last_key:
    print("\t".join([last_key, str(sum_)]))
