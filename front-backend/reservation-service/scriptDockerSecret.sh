#!/bin/bash
kubectl create secret docker-registry my-dockerhub-secret \
  --docker-server=https://index.docker.io/v1/ \
  --docker-username=$DOCKERHUB_USERNAME \
  --docker-password=$DOCKERHUB_TOKEN \
  --docker-email=chetouiiftikhar@gmail.com  # Optionnel, remplacez par votre email

echo "Secret Docker Hub créé avec succès !"