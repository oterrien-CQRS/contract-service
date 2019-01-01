# Mandate Domain Services

Exemple:

POST http://192.168.99.100:9999/mandate-api/v1/mandates 
{
	"id": "5",
	"bankName": "Socgen",
	"contractor": {
		"name": "the_contractor"
	}
}

PUT http://192.168.99.100:9999/mandate-api/v1/mandates/5/notary
{
	"name": "the_notary"
}
