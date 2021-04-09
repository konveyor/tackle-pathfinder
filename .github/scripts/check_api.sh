#!/bin/sh -x
set -e

minikube_ip=$(minikube ip)
minikube_ip=localhost:8085
keycloak_ip=localhost:8180

echo '>>> Obtaining the keycloack token for the operations'
access_token=$(curl -X POST "http://$keycloak_ip/auth/realms/quarkus/protocol/openid-connect/token" \
            --user backend-service:secret \
            -H 'content-type: application/x-www-form-urlencoded' \
            -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token')
echo 
echo
echo '>>> Given a NOT assessed app When Get Assessments ThenResult Empty body with 200 http code'
req_not_existing_app=$(curl -X GET "http://$minikube_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_not_existing_app" = "[]200"

echo 
echo
echo '>>> Given a NOT assessed app When Create Assessment ThenResult AssessmentHeader body with 201 http code'
curl "http://$minikube_ip/pathfinder/assessments" \
            -H 'Content-Type: application/json' -H 'Accept: application/json' -H "Authorization: Bearer $access_token" \
            -d '{ "applicationId": 100 }' -w "%{http_code}" |
    grep '"applicationId":100,"status":"STARTED"}201'

echo 
echo
echo '>>>Given an assessed app When Get AssessmentHeader ThenResult AssessmentHeader body with 200 http code'
req_get_assessment=$(curl -X GET "http://$minikube_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
echo $req_get_assessment | grep '"applicationId":100,"status":"STARTED"}]200' 
req_get_assessment=$(echo $req_get_assessment | sed 's/]200/]/g')

echo 
echo
echo '>>> Given an assessed app When Get Assessment ThenResult Assessment body with 200 http code'
assessmentId=$(echo $req_get_assessment | jq '.[0].id')
curl -X GET "http://$minikube_ip/pathfinder/assessments/$assessmentId" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}" | \
    grep '"order":5,"option":"Application containerisation not attempted as yet"'
