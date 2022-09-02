#!/bin/bash

##
# grafana api calls
##

# DEV NOTE: set environments here
APIKEY="eyJrIjoiUWFMWVQ1aUoyanh3eVZzVnlnd3lLalJxMUFsdTdJWGciLCJuIjoidGVzdCIsImlkIjoxfQ=="
HOST="http://51.178.165.36:3000"


# utility used by other functions

function post-json {
  curl -X POST "$HOST$1" -H "Content-Type: application/json" -H "Authorization: Bearer $APIKEY" -d "@$2"
}

function get {
  curl -X GET "$HOST$1" -H "Accept: application/json" -H "Content-Type: application/json" -H "Authorization: Bearer $APIKEY"
}

function send {
  curl -X $1 "$HOST$2" -H "Accept: application/json" -H "Content-Type: application/json" -H "Authorization: Bearer $APIKEY"
}

# api calls

function get_dashboard_by_uid {
  get "/api/dashboards/uid/$1"
}

# post-json "/api/dashboards/db" "body.txt"

# get_dashboard_by_uid "G9axKCWVz"

# send GET /api/datasources

# send GET /api/org