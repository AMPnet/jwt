# jwt
[![](https://jitpack.io/v/AMPnet/jwt.svg)](https://jitpack.io/#AMPnet/jwt) [![](https://jitci.com/gh/AMPnet/jwt/svg)](https://jitci.com/gh/AMPnet/jwt) ![](https://github.com/AMPnet/jwt/workflows/Java%20CI/badge.svg?branch=master)

Spring Boot library for JWT used in Crowdfunding services.

## Usage
To install the library add: 
 
 ```gradle
 repositories { 
      jcenter()
      maven { url "https://jitpack.io" }
 }
 dependencies {
      implementation 'com.github.AMPnet:jwt:0.1.0'
  }
 ```  

## Example
Decode JWT payload: 
```json
{
  "sub": "e@mail.com",
  "user": "{\"uuid\":\"370a248e-cbd0-4af8-b1d5-0851a3d654db\",\"email\":\"e@mail.com\",\"name\":\"Name\",\"authorities\":[\"Auth\"],\"enabled\":true,\"verified\":true}",
  "iat": 1573050226,
  "exp": 1573050229
}
```
Encoded JWT: ```eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlQG1haWwuY29tIiwidXNlciI6IntcInV1aWRcIjpcIjM3MGEyNDhlLWNiZDAtNGFmOC1iMWQ1LTA4NTFhM2Q2NTRkYlwiLFwiZW1haWxcIjpcImVAbWFpbC5jb21cIixcIm5hbWVcIjpcIk5hbWVcIixcImF1dGhvcml0aWVzXCI6W1wiQXV0aFwiXSxcImVuYWJsZWRcIjp0cnVlLFwidmVyaWZpZWRcIjp0cnVlfSIsImlhdCI6MTU3MzIyNjEyMiwiZXhwIjoxNTczMjI2MTUyfQ.sXNTDuml_ztVNlrYwoplwz-4KRsyB9sxKCHjJdMz10zF53JWjVWAAAtqxFpR5G0lwtxtwMYVbIf6nJpsaf5bDHds87DVbYNuKCiIWVcoLLUbradTJms0DUPuF6RkYGBho4pjMQMZtPR2L1EO3Y3dh7xy23devsOmtMVv3P-Kf3g2yULbrh6WedYsQ2MxWOXQqmbXFF6W_EqVmTVFYHxpkzNDxxhmkYOp90JgPtv6VN9tdhzvwmOyZ-oonE5-CLBevgLAD2xdn4Bgeqjczvc0JGVRWXwEtWi-3FqGc548enUU03_cAprzLl6Cjn_VGIBJxsFD3TBOsYZ4xUOIH7j1xw```
