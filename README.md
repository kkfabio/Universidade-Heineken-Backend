# 🍺 Universidade Heineken (UHNK) — Backend

API REST da plataforma de e-learning corporativo da Heineken, desenvolvida com Spring Boot, JWT e Supabase.

## 🔗 Links

- **Produção:** https://universidade-heineken-backend-production.up.railway.app
- **Frontend:** https://github.com/kkfabio/Universidade-Heineken-Front

---

## 🚀 Tecnologias

- [Java 21](https://www.oracle.com/java/)
- [Spring Boot 3.4.6](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA + Hibernate](https://spring.io/projects/spring-data-jpa)
- [JWT (jjwt 0.12.6)](https://github.com/jwtk/jjwt)
- [BCrypt](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
- [PostgreSQL + Supabase](https://supabase.com/)
- [Railway](https://railway.app/) (deploy)

---

## ✨ Funcionalidades

- **Login** com validação de credenciais e geração de token JWT
- **Recuperação de senha** com validação de CPF e senha temporária (expira em 5 minutos)
- **Troca de senha** com verificação da senha atual via BCrypt
- **Restauração automática** da senha original após expiração da senha temporária
- **Proteção de rotas** via Spring Security stateless

---

## 🔐 Arquitetura de Autenticação

```
Usuário → Vercel (Next.js) → Railway (Spring Boot) → Supabase (PostgreSQL)
```

- Senhas armazenadas com **BCrypt**
- Autenticação via **JWT stateless** (sem sessão no servidor)
- Token com expiração de **24 horas**
- Senha temporária com expiração de **5 minutos**

---

## 📁 Estrutura do Projeto

```
src/main/java/com/heineken/auth/
├── controller/
│   ├── AuthController.java
│   └── PasswordController.java
├── service/
│   ├── AuthService.java
│   ├── JwtService.java
│   ├── PasswordService.java
│   └── impl/
│       ├── AuthServiceImpl.java
│       ├── JwtServiceImpl.java
│       └── PasswordServiceImpl.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── infra/config/
│   └── SecurityConfig.java
└── util/
```

---

## 🔌 Endpoints

### Auth
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Login com e-mail e senha |

### Password
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/password/forgot` | Recuperação de senha com CPF |
| POST | `/api/password/change` | Troca de senha autenticada |

---

## ⚙️ Rodando localmente

### Pré-requisitos

- Java 21+
- Maven
- Conta no Supabase

### Variáveis no `application.properties`

```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://<host>:5432/postgres
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=validate
jwt.secret=<seu-secret>
jwt.expiration-ms=86400000
```

### Rodando

```bash
./mvnw spring-boot:run
```

---

## 👤 Usuário de teste

```
Email: teste@heineken.com
Senha: Joao@!2021
CPF:   123.456.789-00
```

---

## 📄 Licença

Projeto desenvolvido para fins educacionais.
