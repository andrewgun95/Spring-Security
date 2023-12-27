## What is Application Security ?

Securing application which involves technology solutions, adopt best practices, 
person and physical security 

Analyzing the risk -> implementing mitigating actions

Mitigation Actions
- Don't use common password
- Firewalls
- Prevent direct sign-on to super account
- Use OS Level Security
- 2FA (Two-Factor Authentication)
- Don't use personal email for official business
- Threat scanning of incoming emails
- Password expiration policies (but only on business email)
- Use state department email (consult with expert)

Security Audit Frameworks / Certification
- PCS-DSS
- SOX
- HIPAA (Medical Industry)
- SSAE 16

Common Terminology
- PII : Personally Identifiable Information (name, address, email, tax ids, etc)
- Encryption at Rest : Sensitive data to be encrypted when stored (password encryption)
- Encryption at Flight : Sensitive data to be encrypted when transmitted (https, ssh)
- Segregation of Duties : Roles management with limited access
- Processes and Controls : Why is changing?

PCI DSS Requirement
- Protect System with Firewalls (avoid everything exposing into the internet)
- Don't use default for configure Passwords and Settings
- Use industry accepted algorithm (bycrypt, md5, etc)
- Implement logging and log management
- Vulnerability scans and penetration tests
- Documentation and risk assessments

Best Practices

1. Use **OS Service Accounts** for Application (minimal access)
2. Use **Database Service Accounts** for Application (minimal access)
3. Use layer of network security to protect internal systems
   - VPC, VPN, multiple physical network

Spring Security focuses on **Application Security**

Security Key Term
1. Identity : unique actor
2. Credentials : user id and password
3. Authentication : how application verify the identity of the requestor 
   - User gives credentials and then user got validated
4. Authorization : can user perform an action

### Authentication Providers
(verify user identities)

Spring Security Support
1. In Memory
2. JDBC / Database
3. Custom (Using JWT)
4. LDAP / Active Directory
5. Keycloak
6. ACL (Access Control List)
7. OpenID
8. CAS

### Password Storage

Spring Security Support
1. NoOp
2. BCrypt
3. Argon2
4. Pbkdf2
5. SCrypt
6. Custom

Spring Security Core Modules
1. Core
2. Remoting (RMI operations)
3. Web
4. Config
5. LDAP
6. OAuth 2.0 Core (OAuth 2.0 and OpenID)
7. OAuth 2.0 Client (Client support for OAuth 2.0 and OpenID)
8. OAuth 2.0 JOSE (Javascript Object Signing and Encryption)
9. OAuth 2.0 Resource Server (OAuth 2.0 Resource Server)
10. ACL
11. CAS
12. OpenID
13. Test

OWASP

Spring Security Support
1. XSS
2. CSRF
3. Security HTTP Response Headers
4. Redirect to HTTPS

XSS (Cross Site Scripting)

*Javascript Injection*
- Do some particular action, ex : add a friend on social media and might be spread

Mitigation :
Set Header `X-XSS-Protection` into `1;mode=block`

On Web Client/Browser there is a CSP (Content Security Policy)
1. Placing in meta tag
2. Set attribute Content-Security-Policy at the response header

Common Scenarios
- User enters text in an input field within text is Javascript code
- Server accepts text without encoding or sanitizing
- User enter text displayed to user on page then Javascript code executed

CSRF (Cross Site Request Forgery)

*Fake Request*

Untrusted sites trying to send a fake request by include a session token on that request

Common Scenarios
- User Log-in into Trusted Site
- Trusted Site establish session and sends token in cookie
- Redirect to untrusted sites
- Inject establish session and sends token when try performing fake request to Trusted site

Abusing credit card usage or unexpected transactions

Mitigation :
CSRF Token

*Synchronizer Token on Request*

SomeSite Cookie Attribute
Tell the browser to not send cookie when request is coming from other sites

When to Use CSRF Protection?
CSRF request is coming from normal user (not programmatic clients like RestTemplate or WebClient)

Spring Security Filter

Incoming Request --> Web Client -> Filter 1 -> Filter 2 -> Filter n -> Servlet
                                -------------- Filter Chain --------->

### Hash?
Is one way *Encoding Function*

1. Generate the password into hash value (password can't generate from hash value)
2. Verify the password (ex 'password1' will always hash to 'abc')

Hash with Salt

Problem  : dictionary attack can be used to guess passwords from hash value
Solution : using salt value

*Additional data added to the value being hashed*

ex 'password1' with salt become 'password1{ThisIsMySaltValue}'

Spring Delegating Password Encoder

- Allow storage of password hashes in multiple formats
- Password hashes stored as - {encodername}<somepasswordvalue>

Password Encoder Recommendation
1. BCrypt
2. Pbkdf2
3. SCrypt

### Spring Security Filters

All Spring Security Filters implement the **Filter** interface.

Filter is part of **Java Servlet API** and accept *Servlet Request*, *Servlet Response*, and *Filter Chain*

Ex : BasicAuthenticationFilter

What it does? Inspect Request for HTTP Basic Credentials and perform Authentication

#### Can have more than one authentication filter

Common Flow :
1. If authentication is there and fails will throw an exception
2. If authentication is not there and will pass through next filter (without issues)

### Database Authentication

1. Provide implementation of UserDetailsService using Spring Data JPA

    *UserDetailsService is implementation of how getting information about the user*

2. Provide implementation of UserDetails using Spring Data JPA Entities (User and Authorities)

    *UserDetails is implementation of the user which store the information (username, password, etc)*

#### Spring JPA

Unless you've initialized a transaction in your code, Spring Data JPA will implicitly create a transaction when repository methods are called