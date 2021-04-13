#!/bin/sh -x
set -e

# Usage : check_api.sh api_ip keycloak_api , both arguments optional defaulted to $(minikube ip)
# ./check_api.sh localhost:8085 localhost:8180
# ./check_api.sh

api_ip=${1:-$(minikube ip)}
keycloak_ip=${2:-$api_ip}

echo "Using API IP: $api_ip   &   Keycloak IP: $keycloak_ip"
echo
echo '>>> Obtaining the keycloack token for the operations'
access_token=$(curl -X POST "http://$keycloak_ip/auth/realms/quarkus/protocol/openid-connect/token" \
            --user backend-service:secret \
            -H 'content-type: application/x-www-form-urlencoded' \
            -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token')
echo 
echo
echo '>>> Given a NOT assessed app When Get Assessments ThenResult Empty body with 200 http code'
req_not_existing_app=$(curl -X GET "http://$api_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_not_existing_app" = "[]200"

echo 
echo
echo '>>> Given a NOT assessed app When Create Assessment ThenResult AssessmentHeader body with 201 http code'
curl "http://$api_ip/pathfinder/assessments" \
            -H 'Content-Type: application/json' -H 'Accept: application/json' -H "Authorization: Bearer $access_token" \
            -d '{ "applicationId": 100 }' -w "%{http_code}" |
    grep '"applicationId":100,"status":"STARTED"}201'

echo 
echo
echo '>>>Given an assessed app When Get AssessmentHeader ThenResult AssessmentHeader body with 200 http code'
req_get_assessment=$(curl -X GET "http://$api_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
echo $req_get_assessment | grep '"applicationId":100,"status":"STARTED"}]200' 
req_get_assessment=$(echo $req_get_assessment | sed 's/]200/]/g')

echo 
echo
echo '>>> Given an assessed app When Get Assessment ThenResult Assessment body with 200 http code'
assessmentId=$(echo $req_get_assessment | jq '.[0].id')
curl -X GET "http://$api_ip/pathfinder/assessments/$assessmentId" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}" | \
    grep '"order":5,"option":"Application containerisation not attempted as yet"'
    
echo 
echo
echo '>>> Given a NOT assessed app When Get Assessment ThenResult 404 http code'
assessmentId="1000"
req_get_not_assessed=$(curl -X GET "http://$api_ip/pathfinder/assessments/$assessmentId" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -w "%{http_code}")
test "$req_get_not_assessed" = "404"
