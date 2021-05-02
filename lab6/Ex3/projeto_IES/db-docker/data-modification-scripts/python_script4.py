
from random import randint


file1 = open('promotions.sql', 'r') 

Lines = file1.readlines() 

newlines = []
# Strips the newline character 
bus = ["DEFAULT_F","DEFAULT_F2"] +["DEFAULT_R","DEFAULT_R2"]

for line in Lines: 
    if "INSERT INTO" in line:
        newlines.append(line)
    else:

        data_without_par = line.replace("(","")
        data_without_par = line.replace(")","")
        data_split = line.split("',")
        profile_pic = data_split[4].replace("'","").strip()
        option = randint(0,3)
            
        new_profile_pic = bus[option]
        clicks = randint(200,1000)
        newline = line.replace(profile_pic,"'"+new_profile_pic+"', "+str(clicks)+"),")
        newlines.append(newline)


file1 = open('promotions.sql', 'w') 
file1.writelines(newlines) 
file1.close() 
