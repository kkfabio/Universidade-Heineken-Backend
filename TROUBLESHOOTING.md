# 🔧 Guia de Troubleshooting - Refatoração de Segurança

## ❓ Problemas Comuns e Soluções

### 1. "No qualifying bean of type 'AuthService' found"

**Problema:**
```
Error creating bean with name 'authController': 
No qualifying bean of type 'com.heineken.auth.service.AuthService' found
```

**Causa:**
A interface `AuthService` não tem implementação com `@Service`.

**Solução:**
```java
// ❌ Errado
public class AuthServiceImpl implements AuthService {
}

// ✅ Correto
@Service
public class AuthServiceImpl implements AuthService {
}
```

**Verificação:**
- [ ] `AuthServiceImpl` tem `@Service`
- [ ] `JwtServiceImpl` tem `@Service`
- [ ] `PasswordServiceImpl` tem `@Service`

---

### 2. "Circular dependency detected"

**Problema:**
```
BeanCurrentlyInCreationException: Requested bean is currently in creation
```

**Causa:**
`AuthServiceImpl` injeta `PasswordService` que injeta `AuthService`.

**Solução:**
Use `@Lazy` em uma das injeções:
```java
@Inject
@Lazy
private AuthService authService;
```

**Verificação:**
```
AuthServiceImpl
  ├─ UserRepository ✓
  ├─ PasswordEncoder ✓
  ├─ JwtService ✓ (interface)
  └─ PasswordService ✓ (interface)

PasswordServiceImpl
  ├─ UserRepository ✓
  └─ PasswordEncoder ✓

JwtServiceImpl
  └─ Nenhuma injeção ✓
```

---

### 3. "Method extractEmail is never used" (Warning)

**Problema:**
```
Method 'extractEmail(java.lang.String)' is never used
```

**Causa:**
O método está definido na interface mas não é usado no AuthServiceImpl atualmente.

**Solução:**
Ignore o warning (é para extensão futura) ou adicione `@SuppressWarnings("unused")`:

```java
public interface JwtService {
    String generateToken(String email);
    
    @SuppressWarnings("unused")
    String extractEmail(String token);
    
    @SuppressWarnings("unused")
    boolean isTokenValid(String token);
}
```

---

### 4. "Senha temporária not in response"

**Problema:**
O cliente espera a senha em `ForgotPasswordResponse` mas recebe `null`.

**Solução:**
Esta é uma **mudança de segurança intencional**:
```java
// ❌ Antes (inseguro)
return new ForgotPasswordResponse(message, temporaryPassword); // Expõe senha

// ✅ Depois (seguro)
return new ForgotPasswordResponse(message, null); // Não expõe
// A senha deve ser enviada por email
```

**Migração do Client:**
```javascript
// ❌ Antes
const { temporaryPassword } = await response.json();
console.log("Sua senha é: " + temporaryPassword);

// ✅ Depois
const { message } = await response.json();
console.log(message); // "Senha temporária foi enviada para o email registrado."
```

---

### 5. "InvalidPasswordException: Senha deve conter..."

**Problema:**
Usuário recebe erro ao tentar mudar para senha fraca.

**Causa:**
`PasswordValidator` está rejeitando a senha.

**Solução:**
Verificar requisitos de força:
```
✓ Mínimo 8 caracteres
✓ Máximo 128 caracteres
✓ Pelo menos 1 MAIÚSCULA (A-Z)
✓ Pelo menos 1 minúscula (a-z)
✓ Pelo menos 1 dígito (0-9)
✓ Pelo menos 1 especial (!@#$%^&*()_+...)
```

**Exemplos válidos:**
- `ValidPass!123`
- `Secure@Pass456`
- `MyNewPass!2024`

**Exemplos inválidos:**
- `password123` (sem maiúscula, sem especial)
- `Pass@1` (menos de 8 chars)
- `PASSWORD@123` (sem minúscula)

---

### 6. "InvalidCredentialsException: Email ou CPF inválido"

**Problema:**
CPF correto mas rejeitado.

**Causa:**
Formatação diferente entre request e banco de dados.

**Solução:**
`CpfValidator.normalizeCpf()` trata isso:
```java
// Ambos funcionam:
resetPassword({ email: "user@example.com", cpf: "123.456.789-00" });
resetPassword({ email: "user@example.com", cpf: "12345678900" });
```

**Verificação:**
```java
CpfValidator.normalizeCpf("123.456.789-00"); // → "12345678900"
CpfValidator.normalizeCpf("12345678900");    // → "12345678900"
```

---

### 7. "UserNotFoundException com mensagem genérica"

**Problema:**
Erro mostra "Email ou CPF inválido. Tente novamente." mesmo quando email existe.

**Solução:**
Esta é uma **mudança de segurança intencional** (enumeration prevention):
```java
// ❌ Antes (inseguro)
if (userOpt.isEmpty()) {
    throw new RuntimeException("Usuário não encontrado"); // Enumeration
}
if (!storedCpf.equals(normalizedCpf)) {
    throw new RuntimeException("CPF incorreto"); // Enumeration
}

// ✅ Depois (seguro)
if (userOpt.isEmpty()) {
    throw new UserNotFoundException("Email ou CPF inválido. Tente novamente.");
}
if (!storedCpf.equals(normalizedCpf)) {
    throw new InvalidCredentialsException("Email ou CPF inválido. Tente novamente.");
}
```

**Benefício:**
Atacante não consegue descobrir quais emails são válidos no sistema.

---

### 8. "GlobalExceptionHandler não funciona"

**Problema:**
Exceções não são tratadas pelo handler.

**Causa:**
`@RestControllerAdvice` não foi detectado.

**Solução:**
Verificar:
1. Classe tem `@RestControllerAdvice`
2. Classe está no pacote certo
3. Spring consegue fazer scan do pacote

```java
// ✅ Correto
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        // ...
    }
}
```

**Verificação:**
```bash
# Log do Spring deve incluir:
# Mapped "{...}" onto handler method in GlobalExceptionHandler
```

---

### 9. "Testes falhando com NullPointerException"

**Problema:**
```
java.lang.NullPointerException at PasswordValidatorTest.testValidPassword()
```

**Causa:**
Falta de inicialização de mocks.

**Solução:**
Usar `@RunWith(MockitoRunner.class)`:

```java
@RunWith(MockitoRunner.class)
class PasswordValidatorTest {

    @Test
    void testValidPassword() {
        assertTrue(PasswordValidator.isValid("ValidPass@123"));
    }
}
```

**Ou sem Mockito:**
```java
class PasswordValidatorTest {

    @Test
    void testValidPassword() {
        // PasswordValidator é classe com métodos estáticos
        assertTrue(PasswordValidator.isValid("ValidPass@123"));
    }
}
```

---

### 10. "SecurePasswordGenerator sempre gera mesma senha"

**Problema:**
Senhas geradas são todas iguais.

**Causa:**
`SecureRandom` pode estar usando seed fixa.

**Solução:**
A implementação já usa `new SecureRandom()` corretamente:

```java
private static final SecureRandom random = new SecureRandom();

public static String generateSecurePassword() {
    // random já está inicializado corretamente
    // cada chamada gerará senha diferente
    password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
}
```

**Teste:**
```java
Set<String> passwords = new HashSet<>();
for (int i = 0; i < 100; i++) {
    passwords.add(SecurePasswordGenerator.generateSecurePassword());
}
assertTrue(passwords.size() > 95); // Deve ter >95 únicas
```

---

## 🔍 Debugging

### Ativar logs de segurança

**application.properties:**
```properties
logging.level.com.heineken.auth=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Inspecionar injeção de dependência

**AuthApplication.java:**
```java
@SpringBootApplication
public class AuthApplication {
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthApplication.class);
        
        // Registrar listener para debug
        app.addListeners(event -> {
            if (event instanceof ApplicationReadyEvent) {
                System.out.println("✅ Application started successfully");
            }
        });
        
        app.run(args);
    }
}
```

### Testar beans no contexto

```java
@SpringBootTest
class AuthApplicationTests {
    
    @Autowired
    private ApplicationContext context;
    
    @Test
    void testBeansLoaded() {
        // Verificar se beans foram criados
        assertTrue(context.containsBean("authServiceImpl"));
        assertTrue(context.containsBean("jwtServiceImpl"));
        assertTrue(context.containsBean("passwordServiceImpl"));
    }
}
```

---

## 🚨 Checklist de Validação

### Antes de Deploy

- [ ] Todos os arquivos `.java` compilam sem erros
- [ ] Testes passam: `mvn test`
- [ ] Sem warnings de compilação relevantes
- [ ] `GlobalExceptionHandler` está no classpath
- [ ] Spring consegue injetar todas as dependências
- [ ] Controllers conseguem usar as interfaces
- [ ] Banco de dados tem `User` com campos temporários

### Testes de Funcionalidade

- [ ] Reset de senha com CPF normalizado
- [ ] Reset de senha com CPF formatado
- [ ] Mudança de senha com força adequada
- [ ] Mudança de senha rejeita força fraca
- [ ] Login com email/senha corretos
- [ ] Login rejeita email inválido
- [ ] Login rejeita senha inválida
- [ ] Mensagens de erro são genéricas

### Segurança

- [ ] Senhas temporárias não retornam em response
- [ ] Senhas são criptografadas com BCrypt
- [ ] JWTs são assinados e têm expiração
- [ ] CPF é normalizado antes de comparar
- [ ] Força de senha é validada

---

## 📞 Contato e Suporte

Se encontrar problemas:

1. Verificar logs com `DEBUG` level
2. Validar structure de diretórios
3. Confirmar anotações (`@Service`, `@RestControllerAdvice`)
4. Consultar `SECURITY_ANALYSIS.md` para detalhes de segurança
5. Executar testes: `mvn clean test`

---

**Última atualização:** 2026-05-23  
**Status:** ✅ Completo

