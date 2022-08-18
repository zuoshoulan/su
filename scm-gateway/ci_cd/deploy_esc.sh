#!/bin/bash
mvn clean package
scp gateway-deploy/target/gateway-deploy.jar nodea:/data