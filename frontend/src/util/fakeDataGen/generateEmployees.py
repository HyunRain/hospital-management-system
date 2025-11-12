import json
import random
import os
from faker import Faker
from datetime import datetime

fake = Faker('de_AT')

genders = ['MALE', 'FEMALE', 'OTHER']
roles = [ 'NURSE', 'RECEPTIONIST', 'PHARMACIST', 'RADIOLOGIST', 'SECURITY', 'ACCOUNTANT', 'DIETICIAN', 'ANESTHESIOLOGIST', 'CLEANING_STAFF', 'PHYSIOTHERAPIST', 'LAB_TECHNICIAN', 'SURGEON']

employees = []

for _ in range(100): 
    gender = random.choice(genders)
    first_name = fake.first_name_male() if gender == 'MALE' else fake.first_name_female()
    last_name = fake.last_name()
    dob = fake.date_of_birth(minimum_age=28, maximum_age=70)

    employee = {
        "role": random.choice(roles),
        "firstName": first_name,
        "lastName": last_name,
        "gender": gender,
        "dateOfBirth": dob.isoformat(),
        "phoneNumber": fake.phone_number(),
        "email": fake.unique.email(),
        "addressLine1": fake.street_address(),
        "addressLine2": fake.street_address() if random.random() < 0.5 else "",
        "city": fake.city(),
        "state": fake.state(),
        "department": "NonMedical",
        "country": "Austria",
        "postalCode": fake.postcode(),
        "password": "87943251",
        "repeatedPassword": "87943251"
    }

    employees.append(employee)

script_dir = os.path.dirname(os.path.abspath(__file__))
timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
file_path = os.path.join(script_dir, f'employees_{timestamp}.json')

with open(file_path, 'w', encoding='utf-8') as f:
    json.dump(employees, f, ensure_ascii=False, indent=2)

print(f"JSON file 'employees_{timestamp}.json' created.")
