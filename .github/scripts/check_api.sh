#!/bin/sh -x
set -e

minikube_ip=$(minikube ip)

# Obtaining the keycloack token for the operations
access_token=$(curl -X POST "http://$minikube_ip/auth/realms/quarkus/protocol/openid-connect/token" \
            --user backend-service:secret \
            -H 'content-type: application/x-www-form-urlencoded' \
            -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token')

# Given a NOT assessed app When Get Assessments ThenResult Empty body with 200 http code
req_not_existing_app=$(curl -X GET "http://$minikube_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_not_existing_app" = "[]200"

# Given a NOT assessed app When Create Assessment ThenResult AssessmentHeader body with 201 http code
req_create_assessment=$(curl "http://$minikube_ip/pathfinder/assessments" \
            -H 'Content-Type: application/json' -H 'Accept: application/json' -H "Authorization: Bearer $access_token" \
            -d '{ "applicationId": 100 }' -s -w "%{http_code}")
test "$req_create_assessment" = "{\"id\":1,\"applicationId\":100,\"status\":\"STARTED\"}201"

# Given an assessed app When Get Assessment ThenResult AssessmentHeader body with 200 http code
req_existing_app=$(curl -X GET "http://$minikube_ip/pathfinder/assessments?applicationId=100" -H 'Accept: application/json' \
            -H "Authorization: Bearer $access_token" -s -w "%{http_code}")
test "$req_existing_app" = "[{\"id\":1,\"applicationId\":100,\"status\":\"STARTED\"}]200"
