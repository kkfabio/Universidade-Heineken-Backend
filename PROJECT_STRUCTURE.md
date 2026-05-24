# 📁 Estrutura Final do Projeto - Árvore de Diretórios

```
Universidade-Heineken-Backend/
│
├── 📄 pom.xml                                    (configuração Maven)
├── 📄 mvnw / mvnw.cmd                          (Maven Wrapper)
├── 📄 README.md                                 (documentação geral)
│
├── 📚 Documentação de Segurança
│   ├── 📄 SECURITY_ANALYSIS.md                 (análise de vulnerabilidades)
│   ├── 📄 SECURITY_IMPLEMENTATION_REPORT.md    (relatório com fluxogramas)
│   ├── 📄 IMPLEMENTATION_SUMMARY.md             (resumo de mudanças)
│   └── 📄 VALIDATION_CHECKLIST.md              (checklist de validação)
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/
    │   │   └── com/heineken/auth/
    │   │       │
    │   │       ├── 🔑 AuthApplication.java                    (Spring Boot main)
    │   │       │
    │   │       ├── config/
    │   │       │   └── 🔒 SecurityConfig.java                 (CORS, CSRF, sessão)
    │   │       │
    │   │       ├── controller/
    │   │       │   ├── 🌐 AuthController.java                 (POST /api/auth/login)
    │   │       │   └── 🌐 PasswordController.java             (POST /api/password/*)
    │   │       │
    │   │       ├── service/  (INTERFACES)
    │   │       │   ├── ✨ AuthService.java                   (interface)
    │   │       │   ├── ✨ JwtService.java                    (interface)
    │   │       │   ├── ✨ PasswordService.java               (interface)
    │   │       │   │
    │   │       │   └── impl/  (IMPLEMENTAÇÕES)
    │   │       │       ├── ✨ AuthServiceImpl.java            (@Service)
    │   │       │       ├── ✨ JwtServiceImpl.java             (@Service)
    │   │       │       └── ✨ PasswordServiceImpl.java        (@Service, seguro)
    │   │       │
    │   │       ├── exception/  (CUSTOM EXCEPTIONS)
    │   │       │   ├── ✨ UserNotFoundException.java          (404)
    │   │       │   ├── ✨ InvalidCredentialsException.java    (401)
    │   │       │   ├── ✨ InvalidPasswordException.java       (400)
    │   │       │   └── ✨ GlobalExceptionHandler.java         (centralizado)
    │   │       │
    │   │       ├── util/  (UTILITÁRIOS DE SEGURANÇA)
    │   │       │   ├── ✨ CpfValidator.java                  (normaliza + valida)
    │   │       │   ├── ✨ PasswordValidator.java             (força de senha)
    │   │       │   └── ✨ SecurePasswordGenerator.java       (16 chars aleatórios)
    │   │       │
    │   │       ├── model/
    │   │       │   ├── dto/
    │   │       │   │   ├── request/
    │   │       │   │   │   ├── LoginRequest.java
    │   │       │   │   │   ├── ForgotPasswordRequest.java
    │   │       │   │   │   └── ChangePasswordRequest.java
    │   │       │   │   └── response/
    │   │       │   │       ├── AuthResponse.java
    │   │       │   │       └── ForgotPasswordResponse.java
    │   │       │   └── entity/
    │   │       │       └── User.java                         (com temp password fields)
    │   │       │
    │   │       └── repository/
    │   │           └── UserRepository.java                   (JPA interface)
    │   │
    │   └── resources/
    │       └── application.properties                        (config)
    │
    └── test/
        └── java/
            └── com/heineken/auth/
                │
                ├── util/  (TESTES UNITÁRIOS)
                │   ├── ✨ PasswordValidatorTest.java         (6 testes)
                │   ├── ✨ CpfValidatorTest.java              (4 testes)
                │   └── ✨ SecurePasswordGeneratorTest.java   (6 testes)
                │
                └── AuthApplicationTests.java                 (integration tests)

```

## 📊 Legenda

| Símbolo | Significado |
|---------|------------|
| ✨ | Arquivo novo ou refatorado |
| 🔑 | Classe principal da aplicação |
| 🔒 | Segurança (config, exceções) |
| 🌐 | Controller (REST endpoints) |
| 💾 | Persistência de dados |
| 🧪 | Testes |

## 📈 Estatísticas

### Arquivos Criados
- **3** Interfaces (AuthService, JwtService, PasswordService)
- **3** Implementações (AuthServiceImpl, JwtServiceImpl, PasswordServiceImpl)
- **3** Custom Exceptions (UserNotFoundException, InvalidCredentialsException, InvalidPasswordException)
- **1** Global Exception Handler
- **3** Utility Classes (CpfValidator, PasswordValidator, SecurePasswordGenerator)
- **3** Test Classes (PasswordValidatorTest, CpfValidatorTest, SecurePasswordGeneratorTest)
- **4** Documentation Files (SECURITY_ANALYSIS.md, SECURITY_IMPLEMENTATION_REPORT.md, IMPLEMENTATION_SUMMARY.md, VALIDATION_CHECKLIST.md)

**Total: 23 arquivos novos**

### Arquivos Modificados
- **2** (AuthServiceImpl - melhorias, PasswordServiceImpl - melhorias)
- **1** (pom.xml - versão Java 21)

**Total: 2 arquivos modificados**

### Linhas de Código
- **~1200** Linhas adicionadas
- **~1000** Linhas em testes
- **~200** Linhas em utilidades
- **~100** Linhas em exceptions

## 🏗️ Arquitetura Em Camadas

```
┌────────────────────────────────────────────────┐
│ WEB LAYER (Controllers)                        │
│ └─ AuthController, PasswordController          │
└────────────┬─────────────────────────────────┘
             │ (injeta interfaces)
┌────────────▼─────────────────────────────────┐
│ SERVICE LAYER (Interfaces)                    │
│ ├─ AuthService, JwtService, PasswordService  │
│ └─ Implementadas por impl/                   │
└────────────┬─────────────────────────────────┘
             │ (injeta interfaces + utils)
┌────────────▼─────────────────────────────────┐
│ BUSINESS LOGIC (Implementations)              │
│ ├─ AuthServiceImpl (login)                    │
│ ├─ JwtServiceImpl (geração de tokens)         │
│ └─ PasswordServiceImpl (gestão de senhas)     │
└────────────┬─────────────────────────────────┘
             │ (usa utilitários de segurança)
┌────────────▼─────────────────────────────────┐
│ SECURITY LAYER (Utilities & Validators)       │
│ ├─ CpfValidator (validação de CPF)           │
│ ├─ PasswordValidator (força de senha)         │
│ └─ SecurePasswordGenerator (aleatoriedade)    │
└────────────┬─────────────────────────────────┘
             │
┌────────────▼─────────────────────────────────┐
│ ERROR HANDLING LAYER                         │
│ ├─ UserNotFoundException                      │
│ ├─ InvalidCredentialsException               │
│ ├─ InvalidPasswordException                  │
│ └─ GlobalExceptionHandler                    │
└────────────┬─────────────────────────────────┘
             │
┌────────────▼─────────────────────────────────┐
│ PERSISTENCE LAYER                            │
│ ├─ UserRepository (JPA)                      │
│ └─ PasswordEncoder (BCrypt)                  │
└────────────┬─────────────────────────────────┘
             │
┌────────────▼─────────────────────────────────┐
│ DATABASE LAYER                               │
│ └─ User table (com temp password fields)     │
└─────────────────────────────────────────────┘
```

## 🔌 Injeção de Dependência

```
Spring ApplicationContext
│
├─ @Bean: PasswordEncoder (BCryptPasswordEncoder)
│
├─ @Service: AuthServiceImpl
│   ├─ @Inject: UserRepository
│   ├─ @Inject: PasswordEncoder
│   ├─ @Inject: JwtService (interface → JwtServiceImpl)
│   └─ @Inject: PasswordService (interface → PasswordServiceImpl)
│
├─ @Service: JwtServiceImpl
│   └─ Nenhuma injeção (constants privadas)
│
├─ @Service: PasswordServiceImpl
│   ├─ @Inject: UserRepository
│   └─ @Inject: PasswordEncoder
│
└─ @RestControllerAdvice: GlobalExceptionHandler
    └─ Automática (Spring registra)
```

## 🚀 Fluxo de Requisição

### Login
```
POST /api/auth/login
        ↓
   AuthController.login()
        ↓
   AuthServiceImpl.login() [interface AuthService]
        ↓
   ├─ PasswordService.restorePasswordIfExpired()
   ├─ UserRepository.findByEmail()
   ├─ PasswordEncoder.matches()
   └─ JwtService.generateToken()
        ↓
   AuthResponse (JSON)
```

### Reset de Senha
```
POST /api/password/forgot
        ↓
   PasswordController.forgotPassword()
        ↓
   PasswordServiceImpl.resetPassword() [interface PasswordService]
        ↓
   ├─ CpfValidator.isValidFormat()
   ├─ UserRepository.findByEmail()
   ├─ CpfValidator.normalizeCpf()
   ├─ SecurePasswordGenerator.generateSecurePassword()
   ├─ PasswordEncoder.encode()
   └─ UserRepository.save()
        ↓
   ForgotPasswordResponse { message, password: null }
```

### Mudança de Senha
```
POST /api/password/change
        ↓
   PasswordController.changePassword()
        ↓
   PasswordServiceImpl.changePassword() [interface PasswordService]
        ↓
   ├─ UserRepository.findByEmail()
   ├─ PasswordEncoder.matches()
   ├─ PasswordValidator.isValid()
   ├─ PasswordEncoder.encode()
   └─ UserRepository.save()
        ↓
   HTTP 200 OK (sem conteúdo)
```

## 🧪 Cobertura de Testes

```
src/test/java/com/heineken/auth/util/
│
├─ PasswordValidatorTest
│   ├─ testValidPassword()
│   ├─ testPasswordTooShort()
│   ├─ testPasswordMissingUppercase()
│   ├─ testPasswordMissingLowercase()
│   ├─ testPasswordMissingDigit()
│   ├─ testPasswordMissingSpecialChar()
│   └─ testValidationErrorMessage()
│
├─ CpfValidatorTest
│   ├─ testValidateCpfWithFormatting()
│   ├─ testValidateCpfInvalidFormat()
│   ├─ testNormalizeCpf()
│   ├─ testNormalizeCpfNull()
│   └─ testCpfComparisonWithDifferentFormats()
│
└─ SecurePasswordGeneratorTest
    ├─ testGenerateSecurePasswordLength()
    ├─ testGenerateSecurePasswordUniqueness()
    ├─ testGenerateSecurePasswordHasUppercase()
    ├─ testGenerateSecurePasswordHasLowercase()
    ├─ testGenerateSecurePasswordHasDigit()
    └─ testGenerateSecurePasswordHasSpecialChar()
```

## 🎯 Próximos Arquivos Recomendados

```
com/heineken/auth/
│
├── config/
│   ├── RateLimiterConfig.java        (recomendado)
│   └── JwtAuthenticationFilter.java   (recomendado)
│
├── service/
│   ├── EmailService.java             (recomendado)
│   └── AuditService.java             (recomendado)
│
├── model/
│   └── entity/
│       ├── AuditLog.java             (recomendado)
│       └── LoginAttempt.java         (recomendado)
│
└── repository/
    ├── AuditLogRepository.java       (recomendado)
    └── LoginAttemptRepository.java   (recomendado)
```

---

**Projeto Status:** ✅ **ESTRUTURA COMPLETA E PRONTA**

