# Mandate Domain Services

Bind properties : https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/#_configuration_options

Example:

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
