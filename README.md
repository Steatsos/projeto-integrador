# Projeto Integrador

#### 🏥 CHSP - Clínica Hospitalar de São Paulo
Projeto para conclusão do curso técnico em Informática para Internet integrado ao Ensino Médio, no IFSP de São Carlos, em 2024.

#### 🗒️ Descrição
Sistema WEB que cadastra consultas para pacientes em um hospital, e especialistas podem visualizar e confirmar o agendamento essas consultas.

#### 🔨 Sistema e Execução
Feito com `Java Spring Boot`. Usa Java 21+, Maven 3.9+ e um navegador.<br>
Para execução do sistema, usa-se uma IDE e executa-se a classe `ChspApplication.java` (também é possível executá-lo baixando o repositório e usando comandos `mvn`). Após isso, basta acessar `http://localhost:8080` no navegador.

#### 🔌 Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Spring Boot | 3.3.6 | Framework web |
| Spring Data JPA | 3.3.6 | Persistência de dados |
| Spring Security | 6.x | Autenticação e autorização |
| Thymeleaf | 3.1.x | Template engine |
| H2 Database | 2.x | Banco de dados |
| Bootstrap | 5.x | Framework CSS |
| Lombok | 1.18.x | Redução de boilerplate |

#### 🐛 Algumas Limitações

Devido ao propósito educacional e estudantil, o projeto possui características que não poderiam estar em uma versão usada comercialmente, principalmente relacionados à segurança. Estes erros não foram corrigidos para facilitar e simplificar o desenvolvimento. Principais exemplos:
- H2 Console aberto em `/h2-console`
- Upload de arquivos sem validação de tipo
- Sem rate limiting
- Sem HTTPS
- Validação de CPF não é chamada no cadastro

#### Sugestões para o Desenvolvimento

- [ ] API REST (com Spring Web)
- [ ] Paginação nas listas
- [ ] Filtros avançados de consultas
- [ ] Relatórios em PDF
- [ ] Notificações por email (lembretes de consultas)
- [ ] Dashboard com gráficos
- [ ] Integração com calendário (Google Calendar, etc)
- [ ] Sistema de avaliações de profissionais
- [ ] Prescrição digital

#### 📚 Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [H2 Database](https://h2database.com/)

#### 👨‍💻 Autoria e Créditos
- Esta aplicação foi desenvolvida em 2024 em um trabalho em dupla e está no GitHub com autorização dos dois membros, um deles sou eu.
- Projeto atualizado e revisado por Felipe Rabello
- Agradecimentos aos professores das matérias de PJI e DSW3 de 2024 do Instituto Federal de São Paulo, Campus São Carlos, que nos ensinaram, acompanharam e apoiaram no processo de desenvolvimento.
