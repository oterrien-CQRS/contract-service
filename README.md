# Contract CQRS

Given DOCKER_MACHINE_IP is ```192.168.99.100```

Given file ```fabio.properties``` has been copied into ```C:/Users/data/docker/fabio```

Start docker-compose.yml  
* Consul UI : http://${DOCKER_MACHINE_IP}:8500/ui/
* Fabio UI : http://${DOCKER_MACHINE_IP}/9998/routes

Start 2 instances of ServerApplication
* port 8080
* port 8081


Calling ```http://${DOCKER_MACHINE_IP}:9999/server/test``` should call ServerApplication on 8080 or 8081 (depending on the load balancing history)   