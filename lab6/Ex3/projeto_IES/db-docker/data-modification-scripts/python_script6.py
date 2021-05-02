

f = open('business.sql', 'r')
data = f.readlines()

count = 0
newlines=[]
for line in data:

    if count%3==0:
        line = line.replace("'Restaurant'","'Florist'")
    newlines.append(line)
    count+=1
f = open('business.sql', 'w')
f.writelines(newlines)
