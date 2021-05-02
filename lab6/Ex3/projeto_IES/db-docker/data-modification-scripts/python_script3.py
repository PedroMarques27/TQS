file1 = open('clients.sql', 'r') 
Lines = file1.readlines() 

newlines = []
# Strips the newline character 
for line in Lines: 
    if "INSERT INTO" in line:
        newlines.append(line)
    else:
        data_without_par = line.replace("(","")
        data_without_par = line.replace(")","")
        data_split = line.split("',")
        profile_pic = data_split[9].replace("'","").strip()
        gender =data_split[3].replace("'","").strip()

        new_profile_pic = None
        if gender=="F":
            new_profile_pic="DEFAULT_F"
        elif gender=="M":
            new_profile_pic="DEFAULT_M"
        else:
            new_profile_pic="DEFAULT_O"
        newline = line.replace(profile_pic,new_profile_pic)
        newlines.append(newline)


file1 = open('clients.sql', 'w') 
file1.writelines(newlines) 
file1.close() 