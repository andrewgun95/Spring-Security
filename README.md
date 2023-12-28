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

```pre
Incoming Request --> Web Client -> Filter 1 -> Filter 2 -> Filter n -> Servlet
                                -------------- Filter Chain --------->
```

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

1. User try logged in
2. Authentication Filter will intercept incoming User Request
3. Authentication Manager will decide if User is authenticated
    1. If authentication is there and fails will throw an exception
    2. If authentication is not there and will pass through next filter (without issues)
4. Authentication Manager used Authentication Provider to validate User
    1. Do verification on UserDetailsService to get stored UserDetails
    2. Do password verification between stored UserDetails and User

### Database Authentication

1. Provide implementation of UserDetailsService using Spring Data JPA

   *UserDetailsService is implementation of how getting information about the user*

2. Provide implementation of UserDetails using Spring Data JPA Entities (User and Authorities)

   *UserDetails is implementation of the user which store the information (username, password, etc)*

#### Spring JPA

Unless you've initialized a transaction in your code, Spring Data JPA will implicitly create a transaction when
repository methods are called

### Authorization

User is being **AUTHENTICATED** and then :

- Authenticated User can have specific **ROLES** or **AUTHORITIES**
- Authenticated User with given **ROLES** or **AUTHORITIES** can perform specific **ACTION**

Common Flow :

1. User try logged in
2. Authentication Manager return an Authentication Object
3. Authentication Object store principle information and a list of GrantedAuthority Objects
   *Principle can be form as UserDetails*
4. AuthorizationManager make an authorization decision between Authentication Object and Secure Object
   *Secure Object is Actual Secure Object Invocation can be form as Method Invocation*
5. AuthorizationManager making a final access control decision
    1. If access is granted, AuthorizationDecision will return positive (true)
    2. If access is denied, AuthorizationDecision will return negative (false)
    3. AuthorizationDecision will return null, when abstaining from making a decision

### New Spring

Using AuthorizationManager and delegating AuthorizationManager

### Implementation

#### AuthorityAuthorizationManager

Configure with a given set of roles name (prefix "ROLE_")

### AuthenticatedAuthorizationManager

Differentiate between anonymously, remembered me and fully authenticated

### Old Spring

Using AccessDecisionManager and AccessDecisionVoter

### Voting-Based AccessDecisionManager Implementation

A series of AccessDecisionVoter implementations are polled on an authorization decision

AccessDecisionManager then decides whether or not to throw an AccessDeniedException based on its assessment of the votes

Each votes will return denied, granted or abstain

   - Consensus 

      Accept list of access decision voters 
   
      Access granted based on total allowed vs denied responses (pools each voter)
   
   - Affirmative
   
      Access granted based on if one or more granted votes were received
   
   - Unanimous

      Access granted based on if no denied votes were received

#### Role Voter

Configure with a given set of roles name (prefix "ROLE_")

### Authenticated Voter

Differentiate between anonymously, remembered me and fully authenticated

### Role Hierarchies

Examples

```pre
ROLE_USER
ROLE_ADMIN > ROLE_USER > ROLE_FOO
ROLE_ADMIN will have authorities and those of ROLE_USER and ROLE_FOO
```

#### Role & Authorities

Role is a group of one or more Authorities

Role and Authorities can have **SET OF RULES** to perform specific ACTION

### Authorization Rules

Attaching to request URIs or methods

Can be defined using RequestMatcher or using Class/Method Annotation

### Spring Role & Authorities

1. Role start with "ROLE_"

   Example : ROLE_ADMIN, ROLE_USER

2. Authorization are any string

Security Expression in Java Configuration

- permitAll : allow all access
- denyAll : denies all access
- isAnonymous : is authenticated anonymously
- isAuthenticated : is authenticated (fully or remembered)
- isRememberMe : is authenticated with Remember Me Cookie
- isFullyAuthenticated : is fully authenticated
- hasRole : has specific role
- hasAnyRole : have list of roles
- hasAuthority : has authority
- hasAnyAuthority : have list of authorities
- hasIpAddress : has specific IP address (range IP address)

Authorization Filter

Method Security using AOP

@EnableGlobalMethodSecurity

@Secured - accept list of roles or IS_AUTHENTICATED_ANONYMOUSLY

@PreAuthorize - accept Security Expression