#!/bin/bash

echo "Starting Docker Compose services..."
docker compose up -d

if [ $? -eq 0 ]; then
    echo "Services started successfully!"
else
    echo "Failed to start services."
    exit 1
fi
