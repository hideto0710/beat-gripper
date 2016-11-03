BeatGripper
===========

## What is it
- AWS Lambda Function for Gripper

## How to use it
1. Compile this code. `$ sbt assembly`
2. Upload JAR file to AWS Lambda.
3. Request

## How to request
### Attend
```
{
  "user": "user",
  "password": "password",
  "status": 1
}
```

### Leave
```
{
  "user": "user",
  "password": "password",
  "status": -1
}
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
  "status": 401,
  "detail": "Could not login."
}
```

### Invalid status
```
{
  "status": 400,
  "detail": "Inputted 2 not supported."
}
```
