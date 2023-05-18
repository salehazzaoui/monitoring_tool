@echo off && setlocal enabledelayedexpansion

set LKEY=%~1
set LEASE=%~2

if "%LKEY%"=="" (
  echo Expecting a license key and lease hours as arguments. For example: 
  echo %0 12345678-9abc-def0-1234-567890abcdef 24 
  exit /b 1
)

if "%LEASE%"=="" (
  set LEASE=1
  echo The lease duration is not provided, using value "!LEASE!" instead.
)

if "%CURL_PROXY%"=="" (
  set CURL_PROXY=--proxy-anyauth
  echo CURL_PROXY is not set, using value "!CURL_PROXY!" instead.
  echo The following can be used to setup NTLM proxy:
  echo set CURL_PROXY=--proxy-ntlm --proxy-user username:password --proxy server:port
)

echo Generating a lease request...

set REQUEST=
for /f %%i in ('.\verifyta --key %LKEY% --lease %LEASE% --lease-request') do set REQUEST=!REQUEST!%%i
if not defined REQUEST (
  echo Failed generating lease request. 1>&2
  exit /b 1
)

set RESPONSE=
for /f "tokens=*" %%i in ('curl --silent --show-error %CURL_PROXY% -XPOST --data-urlencode "license_key=%LKEY%" --data-urlencode "request=%REQUEST%" http://www.uppaal.com/lease/lease.php') do set RESPONSE=!RESPONSE! %%i
if not defined RESPONSE goto :RESPONSE_ERROR
echo "%RESPONSE:"='%" | findstr /i "<title>" && goto :RESPONSE_ERROR
echo %RESPONSE% | findstr /b "Error" && goto :RESPONSE_ERROR

echo %RESPONSE% | .\verifyta --lease %LEASE% --lease-install

if %ERRORLEVEL% NEQ 0 (
  echo Failed installing the lease. 1>&2
  exit /b %ERRORLEVEL%
) else (
  echo Success.
  exit /b 0
)

:RESPONSE_ERROR
echo Failed fetching the lease. 1>&2
exit /b 1
