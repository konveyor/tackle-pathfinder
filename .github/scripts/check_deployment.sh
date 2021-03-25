#!/bin/sh -x

while [[ ! "$(kubectl rollout status deployment tackle-pathfinder -n tackle)" =~ "successfully" ]]; do
  echo "$(kubectl rollout status deployment tackle-pathfinder -n tackle)"
  sleep 15
done

kubectl rollout status deployment tackle-pathfinder -n tackle
kubectl describe deployment tackle-pathfinder -n tackle

kubectl get pods -n tackle
kubectl get pods -n microcks
echo "---------------------------------------------------------------"
echo "---------------------------------------------------------------"
kubectl logs --tail=100 -l app.kubernetes.io/name=tackle-pathfinder -n tackle
echo "---------------------------------------------------------------"
echo "---------------------------------------------------------------"
kubectl logs --tail=100 -l app.kubernetes.io/name=keycloak -n tackle
