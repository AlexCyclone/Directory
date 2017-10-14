Directory API (ver 1.0.2):
auth none
-----------------------------------------------------------------------------
Department object format

Minimum filling of the object:
{
    "id": 1,
    "name": "c",           - null and "" not support (further for String object - not null)
    "contact": null,       - nullable
    "hasPositions": false, - property
    "hasChild": true       - property
}

Maximum filling of the object:
{
    "id": 1,
    "name": "Company name",
    "contact": {
        "addresses": [
            {
                "name": "primary",
                "zipCode": "00000",
                "country": "country",
                "region": "region",
                "district": "district",
                "city": "city",
                "street": "street",
                "house": "house",
                "office": "office",
                "common": true
            }
        ],
        "emails": [
            {
                "name": "E-mail",
                "email": "e@e",
                "common": true
            }
        ],
        "phones": [
            {
                "name": "tel",
                "number": "00000",
                "common": true
            },
            {
                "name": "tel/fax",
                "number": "00001",
                "common": true
            }
        ],
        "others": [
            {
                "name": "www",
                "value": "http://www.company.com",
                "common": true
            }
        ]
    },
    "hasPositions": false,
    "hasChild": true
}
-----------------------------------------------------------------------------
Contact object format

Minimum filling of the object:
{
    "addresses": [],  - Address object array
    "emails": [],     - Email object array
    "phones": [],     - Phone object array
    "others": []      - OtherInfo object array
}

Maximum filling of the object:
{
    "addresses": [
        {
            "name": "primary",          - not null
            "zipCode": "00000",         - nullable |
            "country": "country",       - nullable |
            "region": "region",         - nullable | Can't
            "district": "district",     - nullable | be
            "city": "city",             - nullable | clear
            "street": "street",         - nullable | together
            "house": "house",           - nullable |
            "office": "office",         - nullable |
            "common": true              - not null
        }
    ],
    "emails": [
        {
            "name": "E-mail",           - not null
            "email": "e@e",             - not null | unique within a single contact
            "common": true              - not null
        }
    ],
    "phones": [
        {
            "name": "tel",              - not null
            "number": "00000",          - not null | unique within a single contact
            "common": true              - not null
        }
    ],
    "others": [
        {
            "name": "www",                       - not null
            "value": "http://www.company.com",   - not null | unique within a single contact
            "common": true                       - not null
        }
    ]
}


(GET) http://localhost:8060/restapi/department
Возвращает обьект JSON соответствующий корневому департаменту

(PUT) http://localhost:8060/restapi/department
Сохраняет корневой департамент (производит изменение в случае существования корневого департамента)