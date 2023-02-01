import sys, json, os

#Code copied from https://stackoverflow.com/questions/918154/relative-paths-in-python
# and https://stackoverflow.com/questions/1955505/parsing-json-with-unix-tools
# and https://www.geeksforgeeks.org/how-to-compare-json-objects-regardless-of-order-in-python/


# test.py 2.0 copied from https://www.quora.com/How-do-I-compare-two-JSON-files-in-Python


def compare_object(a,b):
	if type(a) != type (b):
		return False
	elif type(a) is dict:
		return compare_dict(a,b)
	elif type(a) is list:
		return compare_list(a,b)
	else:
		return a == b

def compare_dict(a,b):
	if len(a) != len(b):
		return False
	else:
		for k,v in a.items():
			if not k in b:
				return False
			else:
				if not compare_object(v, b[k]):
					return False
	return True
def compare_list(a,b):
	if len(a) != len(b):
		return False
	else:
		for i in range(len(a)):
			if not compare_object(a[i], b[i]):
				return False
	return True

project_num = sys.argv[1]
test_num = sys.argv[2]
actual = json.load(sys.stdin)
dirname = os.path.dirname(__file__)
filename = os.path.join(dirname, '../../' + project_num + '/Tests/' + test_num + '-out.json')
expected = json.load(open(filename))
test_result = compare_object(actual,expected)
print(f"Test {test_num} for Milestone {project_num}: {str(test_result)}")
if not test_result:
	print(f"Actual: {actual}")
	print(f"Expected: {expected}")