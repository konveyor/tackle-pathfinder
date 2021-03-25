#!/bin/sh -x

while [[ ! "$(kubectl rollout status deployment tackle-pathfinder -n tackle)" =~ "successfully" ]]; do
  kubectl rollout status deployment tackle-pathfinder -n tackle
  sleep 15
done