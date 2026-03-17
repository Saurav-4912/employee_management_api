$baseUrl = "http://localhost:8081/api"
$username = "testuser_api_" + (Get-Date -Format "yyyyMMddHHmmss")
$email = $username + "@example.com"

Write-Host "1. Registering user..."
$registerBody = @{
    username = $username
    email = $email
    password = "password123"
} | ConvertTo-Json
$registerResponse = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
Write-Host "Register Response: $($registerResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n2. Logging in user..."
$loginBody = @{
    username = $username
    password = "password123"
} | ConvertTo-Json
$loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
$token = $loginResponse.token
Write-Host "Login Response: $($loginResponse | ConvertTo-Json -Depth 5)"

$headers = @{
    Authorization = "Bearer $token"
}

Write-Host "`n3. Creating employee..."
$employeeBody = @{
    name = "Test Employee"
    email = "emp_$username@example.com"
    department = "Engineering"
    position = "Developer"
    salary = 80000.00
    dateOfJoining = "2023-01-01"
} | ConvertTo-Json

$createResponse = Invoke-RestMethod -Uri "$baseUrl/employees" -Method Post -Body $employeeBody -Headers $headers -ContentType "application/json"
$employeeId = $createResponse.id
Write-Host "Create Response: $($createResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n4. Getting all employees..."
$getAllResponse = Invoke-RestMethod -Uri "$baseUrl/employees" -Method Get -Headers $headers
Write-Host "Get All Response items count: $($getAllResponse.content.Count)"

Write-Host "`n5. Getting employee by ID..."
$getByIdResponse = Invoke-RestMethod -Uri "$baseUrl/employees/$employeeId" -Method Get -Headers $headers
Write-Host "Get By ID Response: $($getByIdResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n6. Updating employee..."
$employeeBodyUpdate = @{
    name = "Test Employee Updated"
    email = "emp_$username@example.com"
    department = "Engineering"
    position = "Senior Developer"
    salary = 95000.00
    dateOfJoining = "2023-01-01"
} | ConvertTo-Json

$updateResponse = Invoke-RestMethod -Uri "$baseUrl/employees/$employeeId" -Method Put -Body $employeeBodyUpdate -Headers $headers -ContentType "application/json"
Write-Host "Update Response: $($updateResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n7. Deleting employee..."
Invoke-RestMethod -Uri "$baseUrl/employees/$employeeId" -Method Delete -Headers $headers
Write-Host "Delete successful."

Write-Host "`n8. Verifying deletion..."
try {
    Invoke-RestMethod -Uri "$baseUrl/employees/$employeeId" -Method Get -Headers $headers
    Write-Host "FAIL: Employee should have been deleted!"
} catch {
    Write-Host "PASS: Employee not found as expected. Error: $($_.Exception.Message)"
}
