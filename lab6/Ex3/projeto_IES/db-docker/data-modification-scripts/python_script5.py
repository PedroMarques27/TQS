

import numpy as np

coords = np.random.rand(10000, 2) * 2

print(coords)


from random import randint


file1 = open('users.sql', 'r') 
Lines = file1.readlines() 

newlines = []

for line in Lines: 
    if "INSERT INTO" in line:
        newlines.append(line)
    else:
        option = randint(0,len(coords)-1)

        x,y = coords[option]
        data = str(x)+","+str(y)
        data_without_par = line.replace("'),", "', '"+data+"'),")
        newlines.append(data_without_par)


print(newlines)
f = open('users.sql','w')
f.writelines(newlines)