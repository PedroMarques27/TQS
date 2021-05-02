f = open('users.sql', 'r')
data = f.read()
data = data.replace('Restaurant','Business')
f.close()

f = open('users.sql', 'w')
f.write(data)
f.close()