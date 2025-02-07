# Quarkus Project based ROLES 

Este projeto utiliza **Quarkus** para criar uma API REST resiliente e otimizada para **GraalVM** e execução nativa.

## 🚀 Tecnologias Utilizadas

- **Quarkus**: 3.18.1
- **Java**: 21
- **Auth0**: Plataforma de autenticação
- **RESTEasy**: Implementação JAX-RS
- **Smallrye JWT**: Manipulação de JWT tokens
- **Lombok**: Para reduzir o código boilerplate
- **Swagger (SmallRye OpenAPI)**: Documentação da API
- **HealthCheck (SmallRye Health)**: Monitoramento do status da aplicação

## Casos de Usos

- **Autenticação de Usuário**: Login e geração de tokens JWT via **Auth0**.
- **Refresh Token**: Renovação de tokens JWT sem necessidade de reautenticação.
- **Proteção de Endpoints**: Uso de **@RolesAllowed** para controle de acesso.
- **Suporte a REST API**: Implementação baseada no padrão REST com Quarkus.
- **Execução como Native Image**: Melhor desempenho e menor consumo de memória.
- **Documentação com Swagger**: Acessível via `/q/swagger-ui`
- **Monitoramento de HealthCheck**: Acessível via `/q/health`

## Configuração do projeto

O serviço utiliza as seguintes variáveis de configuração, que devem ser definidas no arquivo `application.properties`:

```properties
# Configuração do Auth0
auth0.client-id=Seu_Client_ID_no_Auth0
auth0.client-secret=Seu_Client_Secret_no_Auth0
auth0.audience=https://Seu_Domínio_Auth0/api/v2/

# Configuração do JWT
smallrye.jwt.path.groups=<<Namespace criado no Actions -> Triggers>>
smallrye.jwt.claims.groups=<<Namespace criado no Actions -> Triggers>>

# Chave pública do JWT
mp.jwt.verify.publickey.location=https://Seu_Domínio_Auth0/.well-known/jwks.json

# Configuração do Swagger/OpenAPI
quarkus.smallrye-openapi.path=/q/openapi
quarkus.swagger-ui.always-include=true

# Configuração do HealthCheck
quarkus.smallrye-health.root-path=/q/health
```

## Instalação e Execução

1. **Clone o repositório**:

   ```sh
   git clone https://github.com/seu-repositorio.git
   cd seu-repositorio
   ```

2. **Compile e execute o projeto**:
   ```sh
   ./mvnw clean package
   ```

3. **Executar em modo de desenvolvimento**:
   ```sh
   ./mvnw quarkus:dev
   ```

4. **Gerar imagem nativa**:
   ```sh
   ./mvnw clean package -Pnative "-Dquarkus.native.container-build=true"
   ```

## Endpoints

| Método | Endpoint              | Descrição |
|--------|-----------------------|-----------|
| POST   | `/auth/login`         | Realiza login e retorna access token |
| POST   | `/auth/refresh`       | Atualiza um access token com refresh token |
| GET    | `/protected/endpoint1` | Endpoint protegido (admin) |
| GET    | `/protected/endpoint2` | Endpoint protegido (admin, user_monster) |
| GET    | `/protected/endpoint3` | Endpoint protegido (user_monster) |
| GET    | `/q/openapi`          | Documentação OpenAPI |
| GET    | `/q/swagger-ui`       | Interface Swagger |
| GET    | `/q/health`           | HealthCheck da aplicação |

## Docker

Para construir e rodar a aplicação em um contêiner:

```sh
docker build -f src/main/docker/Dockerfile.native-micro -t quarkus/ms-quarkus-sec-jwt-auth0 .
docker run -i --rm -p 8080:8080 quarkus/ms-quarkus-sec-jwt-auth0
```

## Configuração Auth0 -> https://manage.auth0.com/dashboard

1. Applications -> Applications -> Settings: Recuperar Client Id e Client Secret
2. Applications -> Applications -> Settings -> Advanced Settings -> Grant Types: Marcar 5 grant types e deixar de fora MFA e Passwordless OTP.
3. Applications -> Applications -> APIs: Selecionar sua API e escolher os scopes para o projeto. (usei estas: read:users, read:data, read:connections , read:roles )
4. Applications -> APIs: Criar uma API e definir um "audience" (usada aqui: "audience-my-api")
5. Applications -> APIs -> Settings -> Access Settings: Habilitar Allow Offline Access (para funcionar o Refresh Token)
6. User Management -> Criar usuário e roles. Associar role ao usuário.
7. Actions -> Triggers: Criar Login / Post Login: Login / Post Login, Runtime: Node 22 (Recommended)

	```node
	exports.onExecutePostLogin = async (event, api) => {
	
	  const namespace = "rlr"; 
	  // este namespace deverá ser usado no application.properties 
	  // (smallrye.jwt.path.groups e smallrye.jwt.claims.groups)
	  
	  if (event.authorization && event.authorization.roles) {
	    // Adiciona as roles no ID Token e Access Token
	    api.idToken.setCustomClaim(`${namespace}`, event.authorization.roles);
	    api.accessToken.setCustomClaim(`${namespace}`, event.authorization.roles);
	  }
	};
	```

8. Monitoring -> Logs: Usei bastante para debugar, principalmente o flow criado no Actions

## Autor

Projeto desenvolvido por **RSDELIA**.

## Licença

Este projeto é distribuído sob a licença MIT.
