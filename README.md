<h1>RESTful API - Gerenciamento de Locadoras de Veículos</h1>

<p>
O sistema foi desenvolvido para gerenciar <strong>locadoras de veículos</strong>, permitindo o cadastro de <em>marcas</em>, <em>carros</em> (CRUD completo), <em>locadores</em> e <em>aluguéis</em>.  
Além disso, conta com integrações externas para otimizar a experiência do usuário e garantir a integridade dos dados.
</p>

<h2>Destaques do Projeto</h2>
<ul>
  <li><strong>API FIPE:</strong> Automatiza o cadastro de marcas e modelos de veículos.</li>
  <li><strong>ViaCEP:</strong> Preenchimento automático de endereços pelo CEP.</li>
  <li><strong>Segurança:</strong> Autenticação e autorização com JWT, com perfis <code>ADMIN</code> e <code>USER</code>.</li>
  <li><strong>Testes:</strong> JUnit &amp; Mockito para validação de funcionalidades.</li>
  <li><strong>Front-end:</strong> Angular (SPA reativa e intuitiva, com configuração de CORS).</li>
  <li><strong>Deploy:</strong> Aplicação hospedada na AWS EC2.</li>
</ul>

<h2>Tecnologias Utilizadas</h2>
<ul>
  <li>Java + Spring Boot</li>
  <li>Angular</li>
  <li>MySQL + JPA/Hibernate</li>
  <li>APIs externas (FIPE, ViaCEP)</li>
  <li>JUnit &amp; Mockito</li>
  <li>AWS EC2</li>
</ul>

<h2>Como Executar o Projeto Localmente</h2>

<h3>Pré-requisitos</h3>
<ul>
  <li>Java 21+ (JDK)</li>
  <li>Maven 3.8+</li>
  <li>Node.js 18+</li>
  <li>Angular CLI 19+</li>
  <li>MySQL</li>
  <li>Git</li>
</ul>

<h3>1. Back-end</h3>
<h4>Clone o repositório</h4>
<pre><code>$ git clone https://github.com/Tributino03/restful-api-car-rental.git</code></pre>

<h4>Navegue até a pasta do back-end</h4>
<pre><code>$ cd restful-api-car-rental/backend</code></pre>

<h4>Configure o <code>application.properties</code></h4>
<pre><code># src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/cars
spring.datasource.username=root
spring.datasource.password=sua-senha
jwt.secret=seu-segredo-super-secreto
jwt.expiration=86400000
</code></pre>

<h4>Instale as dependências e execute</h4>
<pre><code>$ mvn spring-boot:run</code></pre>
<p>O servidor estará disponível em: <a href="http://localhost:8080">http://localhost:8080</a></p>

<h3>2. Front-end</h3>
<h4>Navegue até a pasta do front-end</h4>
<pre><code>$ cd restful-api-car-rental/frontend</code></pre>

<h4>Instale as dependências</h4>
<pre><code>$ npm install</code></pre>

<h4>Execute a aplicação</h4>
<pre><code>$ ng serve</code></pre>
<p>A aplicação estará acessível em: <a href="http://localhost:4200">http://localhost:4200</a></p>

<h2>Rodando os Testes</h2>
<pre><code>$ mvn test</code></pre>

<h2>API Endpoints &amp; Segurança</h2>
<p>A API utiliza JWT para autenticação. Para acessar endpoints protegidos, envie o token no cabeçalho <code>Authorization</code>.</p>

<ul>
  <li><code>/login (POST)</code>: Gera o token de autenticação.</li>
</ul>

<h4>/api/cars</h4>
<ul>
  <li><strong>GET</strong>: ADMIN e USER</li>
  <li><strong>POST, PUT, DELETE</strong>: apenas ADMIN</li>
</ul>

<h4>/api/landlords</h4>
<ul>
  <li><strong>GET</strong>: ADMIN e USER</li>
  <li><strong>POST, PUT, DELETE</strong>: apenas ADMIN</li>
</ul>
