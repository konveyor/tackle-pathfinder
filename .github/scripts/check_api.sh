#!/bin/sh -x
export access_token=$(curl -X POST "http://$(minikube ip)/auth/realms/quarkus/protocol/openid-connect/token" \
            --user backend-service:secret \
            -H 'content-type: application/x-www-form-urlencoded' \
            -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token')

req_not_existing_app=$(curl -X GET "http://$(minikube ip)/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_not_existing_app" = "[]200"
echo $?

req_create_assessment=$(curl "http://$(minikube ip)/pathfinder/assessments" \
            -H 'Content-Type: application/json' -H 'Accept: application/json' -H "Authorization: Bearer $access_token" \
            -d '{ "applicationId": 20 }' -s -w "%{http_code}")
test "$req_create_assessment" = "{\"id\":1,\"applicationId\":200,\"status\":\"STARTED\"}201"
echo $?

req_existing_app=$(curl -X GET "http://$(minikube ip)/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_existing_app" = "[{\"id\":1,\"applicationId\":100,\"status\":\"STARTED\"}]200"
echo $?