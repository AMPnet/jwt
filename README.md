# jwt
[![](https://jitpack.io/v/AMPnet/jwt.svg)](https://jitpack.io/#AMPnet/jwt) [![](https://jitci.com/gh/AMPnet/jwt/svg)](https://jitci.com/gh/AMPnet/jwt)

Spring Boot library for JWT used in Crowdfunding services.

## Usage
To install the library add: 
 
 ```gradle
 repositories { 
      jcenter()
      maven { url "https://jitpack.io" }
 }
 dependencies {
      implementation 'com.github.AMPnet:jwt:0.0.3'
  }
 ```  

## Example
Encoded JWT: ```eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlQG1haWwuY29tIiwidXNlciI6IntcInV1aWRcIjpcIjM3MGEyNDhlLWNiZDAtNGFmOC1iMWQ1LTA4NTFhM2Q2NTRkYlwiLFwiZW1haWxcIjpcImVAbWFpbC5jb21cIixcIm5hbWVcIjpcIk5hbWVcIixcImF1dGhvcml0aWVzXCI6W1wiQXV0aFwiXSxcImVuYWJsZWRcIjp0cnVlLFwidmVyaWZpZWRcIjp0cnVlfSIsImlhdCI6MTU3MzA1MDIyNiwiZXhwIjoxNTczMDUwMjI5fQ.3RQKemUGZBQoKVg9P5qBmcAtkpwW6vomfJWVvfz3o94```

Decode JWT payload: 
```json
{
  "sub": "e@mail.com",
  "user": "{\"uuid\":\"370a248e-cbd0-4af8-b1d5-0851a3d654db\",\"email\":\"e@mail.com\",\"name\":\"Name\",\"authorities\":[\"Auth\"],\"enabled\":true,\"verified\":true}",
  "iat": 1573050226,
  "exp": 1573050229
}
```
