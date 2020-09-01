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
      implementation 'com.github.AMPnet:jwt:0.1.5'
  }
 ```  

## Example
Decode JWT payload: 
```json
{
  "sub": "e@mail.com",
  "user": "{\"uuid\":\"370a248e-cbd0-4af8-b1d5-0851a3d654db\",\"email\":\"e@mail.com\",\"name\":\"Name\",\"authorities\":[\"Auth\"],\"enabled\":true,\"verified\":true,\"coop\":\"ampnet-demo\"}",
  "iat": 1598959052,
  "exp": 1598959082
}
```
Encoded JWT: ```eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlQG1haWwuY29tIiwidXNlciI6IntcInV1aWRcIjpcIjM3MGEyNDhlLWNiZDAtNGFmOC1iMWQ1LTA4NTFhM2Q2NTRkYlwiLFwiZW1haWxcIjpcImVAbWFpbC5jb21cIixcIm5hbWVcIjpcIk5hbWVcIixcImF1dGhvcml0aWVzXCI6W1wiQXV0aFwiXSxcImVuYWJsZWRcIjp0cnVlLFwidmVyaWZpZWRcIjp0cnVlLFwiY29vcFwiOlwiYW1wbmV0LWRlbW9cIn0iLCJpYXQiOjE1OTg5NTkwNTIsImV4cCI6MTU5ODk1OTA4Mn0.c-2GIMtUWPKl9ACTAci0_L1xmucEnuFQFYFAlDfjGsqvPhlZ1P6tR0X0dh6V0hk6W17si0RQMcATAQEd5HzKgk3U5OEisGITiLOxyqFciK6UTd2BLstgysp8Fnn282S1UOL7aPEG-mZ4ysElcITps6l-SUoiqS8w5hel_hclzG2efjsCmmKOIaBf5Zoe7OPh-A6-UjxxirfDbZk1TLdXdSs0p2lsIK33EJpLGOq-zOkJHKAvLNTaUS5C_Dnfk6viU4aTS0Pa2QFL3VFID0Bskw1mHUvm3aGsk8aWouT0RGGOGivZYyulIPlyYv8u9aMRfrtfh5d5Q4E1dNbWvRK0PA```
