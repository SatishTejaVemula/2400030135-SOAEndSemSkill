# JWT Validation and Claims Extraction

A Java 17 and Spring Boot REST API that validates signed JWTs, checks expiration, extracts user claims, and protects backend endpoints. Swagger UI is available for interactive API testing.

## Requirements

- JDK 17
- Maven 3.6.3 or later

## Run in VS Code (PowerShell)

1. Open the `END_LAB` folder in VS Code.
2. Install the **Extension Pack for Java** if it is not already installed.
3. Open a PowerShell terminal in the project folder.
4. Generate a fresh Base64-encoded HMAC secret and set it for this terminal:

   ```powershell
   $bytes = New-Object byte[] 32
   [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
   $env:JWT_SECRET = [Convert]::ToBase64String($bytes)
   ```

   Keep this key private. The application refuses to start without a valid secret of at least 32 bytes; no signing secret is embedded in the Java source.

5. Start the application:

   ```powershell
   mvn spring-boot:run
   ```

   On startup, the application logs a sample JWT for `john` (`john@example.com`, role `admin`). The token expires after one hour. The environment variable and generated token are local to this development session.

## Test with Swagger UI

1. Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
2. Copy the sample JWT printed in the application terminal.
3. Select **Authorize**, enter `Bearer ` followed by the token, and select **Authorize** again.
4. Run `GET /api/validate`. A valid token returns:

   ```json
   {
     "message": "Token validated successfully",
     "username": "john",
     "email": "john@example.com",
     "role": "admin"
   }
   ```

5. Run `GET /api/protected`. A valid token returns `{"message":"Access granted"}`.
6. To test rejection, use a modified token, an expired token, or clear the authorization value. The protected endpoints return HTTP `401 Unauthorized`.

Swagger's API documentation is also available at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

## API

| Method | Path | Authorization | Result |
|---|---|---|---|
| `GET` | `/api/validate` | `Authorization: Bearer <JWT>` | Validation message and username, email, and role claims |
| `GET` | `/api/protected` | `Authorization: Bearer <JWT>` | Access granted message |

Both endpoints require a valid signed token containing `sub`, `email`, and `role` claims. The validator verifies the signature with the configured secret and rejects expired tokens.
