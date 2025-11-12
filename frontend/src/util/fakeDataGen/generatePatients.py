import json
import random
import os
from faker import Faker
from datetime import datetime



fake = Faker('de_AT')

genders = ['MALE', 'FEMALE', 'OTHER']
blood_types = ['A_POSITIVE', 'A_NEGATIVE', 'B_POSITIVE', 'B_NEGATIVE', 'O_POSITIVE', 'O_NEGATIVE', 'AB_POSITIVE', 'AB_NEGATIVE']
marital_statuses = ['SINGLE', 'MARRIED']
status = 'ACTIVE'
sample_allergies = ['Pollen', 'Dust', 'Nuts', 'Penicillin', 'Latex']
sample_diseases = ['Diabetes', 'Hypertension', 'Asthma', 'Arthritis']
sample_medications = ['Metformin', 'Lisinopril', 'Albuterol', 'Ibuprofen']
sample_immunizations = ['COVID-19', 'Tetanus', 'Hepatitis B', 'Influenza']
sample_surgeries = ['Appendectomy', 'Cataract surgery', 'Hernia repair']

def random_subset(lst):
    return random.sample(lst, random.randint(0, min(3, len(lst))))

patients = []

for _ in range(100):
    gender = random.choice(genders)
    first_name = fake.first_name_male() if gender == 'MALE' else fake.first_name_female()
    last_name = fake.last_name()
    dob = fake.date_of_birth(minimum_age=1, maximum_age=90)

    patient = {
        "firstName": first_name,
        "lastName": last_name,
        "gender": gender,
        "dateOfBirth": dob.isoformat(),
        "bloodGroup": random.choice(blood_types),
        "maritalStatus": random.choice(marital_statuses),
        "phoneNumber": fake.phone_number(),
        "email": fake.email(),
        "emergencyContactName": fake.name(),
        "emergencyContactNumber": fake.phone_number(),
        "relationshipToEmergencyContact": random.choice(['Mother', 'Father', 'Spouse', 'Sibling', 'Friend']),
        "addressLine1": fake.street_address(),
        "addressLine2": fake.street_name() if random.random() < 0.5 else "",
        "city": fake.city(),
        "state": fake.state(),
        "country": "Austria",
        "postalCode": fake.postcode(),
        "status": status,
        "patientId": "",
        "referredBy": fake.name(),
        "knownAllergies": random_subset(sample_allergies),
        "pastMedicalHistory": random_subset(sample_diseases),
        "chronicDiseases": random_subset(sample_diseases),
        "currentMedications": random_subset(sample_medications),
        "immunizationStatus": random_subset(sample_immunizations),
        "surgicalHistory": random_subset(sample_surgeries),
        "insuranceProvider": "ÖGK",
        "insurancePolicyNumber": ''.join([str(random.randint(0, 9)) for _ in range(12)]),
        "insuranceExpiryDate": fake.date_between(start_date='today', end_date='+5y').isoformat()
    }

    patients.append(patient)



script_dir = os.path.dirname(os.path.abspath(__file__))
timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
file_path = os.path.join(script_dir, f'patients_{timestamp}.json')

with open(file_path, 'w', encoding='utf-8') as f:
    json.dump(patients, f, ensure_ascii=False, indent=2)

print("✅ JSON file 'patients.json' created.")