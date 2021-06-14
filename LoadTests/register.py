import csv

import requests


class Register:

    def __init__(self, url):
        self.url = url

    def createUser(self, count=1, name='user'):
        for i in range(0, count):
            session = requests.session()
            csrftoken = session.get(self.url + '/accounts/registration').cookies['csrftoken']
            session.headers.update({'X-CSRFToken': csrftoken})

            username = name + str(i)
            data = {'username': username,
                    'password1': 'qpwoeiru', 'password2': 'qpwoeiru', 'email': username + '@troickij.ru'}
            createUser = session.post(self.url + '/accounts/registration/', data=data)
            if str(createUser.status_code) == '200':
                print('Создан пользователь ' + username)
            else:
                print('Ошибка при создании')

    def generateFileUsers(self, name, count, is_superuser):
        with open('users.csv', 'a', newline='') as file:
            #fileColumn = ['username', 'password', 'is_superuser']
            writer = csv.writer(file)
            for i in range(0, count):
                writer.writerow([name + str(i), 'qpwoeiru', is_superuser])
        print('Файл создан')

    #update public.auth_user set is_active = true where is_active is false
    # update public.auth_user set is_superuser = true where username LIKE 'admin%'

# reg = Register('https://dimatroickij.site')
#
# reg.generateFileUsers('user', 100, 'true')