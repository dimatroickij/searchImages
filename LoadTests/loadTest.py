import csv
import os
import random
import uuid

from locust import task, between, HttpUser, tag


# locust -f loadTest.py --host=https://dimatroickij.site -u 1 -r 1

class LoadTest(HttpUser):

    oper = ['%2B', '%2D', '%2A', '%2F', '%26', '%7C', '%23%7C', '%23%2F']
    location = ['top', 'bottom', 'left', 'right', 'center', 'all']

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.classes = []
        self.users = []
        self.admin = []
        self.directory = 'C:\\Users\\dimatroickij\\Downloads\\val2017'
        with open('coco.csv', newline='') as File:
            reader = csv.reader(File)
            for row in reader:
                self.classes.append(row[0])

        with open('users.csv', 'r', newline='') as file:
            reader = csv.reader(file)
            for row in reader:
                if row[2] == 'true':
                    self.admin.append({'username': row[0], 'password': row[1]})
                else:
                    self.users.append({'username': row[0], 'password': row[1]})
        self.classes.append('all')

    wait_time = between(0, 0.5)

    def login(self):
        response = self.client.get('/accounts/login/')
        csrftoken = response.cookies['csrftoken']
        user = random.choice(self.users)
        self.client.post("/accounts/login/", {"username": user['username'], "password": user['password']},
                         headers={'X-CSRFToken': csrftoken})

    def logout(self):
        self.client.get("/accounts/logout/")

    @tag('search')
    @task
    def search(self):
        self.client.get("/search", params=self.generateRequest())

    @tag('addImage')
    @task
    def addImage(self):
        nameImage = os.listdir(self.directory)
        os.chdir(self.directory)
        image = open(random.choice(nameImage), 'rb')
        files = {'image': image,
                 'title': uuid.uuid4()}

        z = self.client.post('/upload', data=files, headers={'Content-Type': 'multipart/form-data'})
        # print(image.read().)

    def on_start(self):
        self.login()

    def on_stop(self):
        self.logout()

    def generateRequest(self):
        request = "%28" + random.choice(self.classes) + "%2C+" + random.choice(self.location) + "%29"
        return request