SELECT NULL = NULL;  -- output: NULL

SELECT NULL != NULL;  -- output: NULL

SELECT NULL IS NULL; -- output: true

SELECT TRUE IS NULL;  -- output: false

SELECT NULL IS NOT NULL; -- output: false

SELECT TRUE IS NOT NULL; -- output: true

-- For NULL-safe comparisons use IS DISTINCT / IS NOT DISTINCT:

SELECT NULL IS NOT DISTINCT FROM NULL; -- output: true

SELECT NULL IS NOT DISTINCT FROM TRUE; -- output: false
