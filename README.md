BeatGripper
===========

## What is it
- AWS Lambda Function for Gripper

## How to use it
1. Deploy this code. `$ ./scripts/deploy.sh`
2. Request

## How to request
### Attend
```
curl -X "POST" "https://<API Gateway Host>/<stage>/working" \
     -H "x-api-key: <API Key>" \
     -H "Content-Type: application/json; charset=utf-8" \
     -d "{\"user\":\"user\",\"password\":\"password\"}"

```

### Leave
```
curl -X "DELETE" "https://<API Gateway Host>/<stage>/working" \
     -H "x-api-key: <API Key>m" \
     -H "Content-Type: application/json; charset=utf-8" \
     -d "{\"user\":\"user\",\"password\":\"password\"}"

```

## Response
### Success
```
{
  "status": 200,
  "detail": ""
}
```

### Invalid user, password
```
{
  "errorMessage": "401: Could not login."
}
```

### Invalid status
```
{
  "errorMessage": "400: Inputted 2 not supported."
}
```
