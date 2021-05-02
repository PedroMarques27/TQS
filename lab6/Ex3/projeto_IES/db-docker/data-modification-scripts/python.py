'''
f = open("subscriptions.sql","r")

data = f.read()
data = data.replace("'Null'","Null");

f = open("subscriptions.sql","w")
f.write(data)

'''
import random

f = open("clients.sql","r")
data = f.read()
split_data = data.split("('")
emails = []

for string in split_data:
    string2  = string.split("',")
    emails+=[c for c in string2 if "@" in c]


matches = {}
print(len(emails))
for email in emails:
    matches[email]=[]

for email in emails:
    match_no = random.randint(10,40)
    for i in range(match_no):
        match_index = random.randint(0,999)
        print(match_index)
        if email != emails[match_index]  and emails[match_index] not in matches[email] and email not in matches[emails[match_index]]:
            matches[email].append(emails[match_index])


for em in matches:
    print(em,":",matches[em])

initial_string = "INSERT INTO matches (email1,email2, swipe_email1, swipe_email2, talking) VALUES"

for email in matches:
    for em in matches[email]:
        a = random.randint(0,1)
        b = random.randint(0,1)
        c=0
        if a==1 and a==b:
            c = random.randint(0,1)

        initial_string +="('"+email+"', '"+em+"',"+ str(a) + ", " + str(b) + ", "+ str(c)+"),\n"

initial_string = initial_string[:-1]+";"

out = open("matches.sql","w")
out.write(initial_string)
