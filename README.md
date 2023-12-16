# MC426 - Engenharia de Software

Esse projeto terá como tema saúde pública.
Faremos um aplicativo que irá ajudar idosos.

## Diagrama C4



### Context (Nível 1)
![image](https://github.com/LucJRibas/MC426-Eng-Software/assets/101939969/c944fae6-945d-4fb1-8081-90825e4136c8)

### Containers (Nível 2)
![image](https://github.com/LucJRibas/MC426-Eng-Software/assets/101939969/61ea05ec-61ae-4c31-97a6-d03d34b4c2bc)

### Components (Nível 3)
![image](https://github.com/LucJRibas/MC426-Eng-Software/assets/101939969/c1511bf5-473b-4539-a149-44765959bc68)

### C4 Components
O estilo em camadas foi adotado para a criação da arquitetura da aplicação

#### Lembretio UI
Estabelece a comunicação com o usuário, definindo a aparência da interface e recebendo os comandos do usuário.

#### Activity
Executa os inputs do usuário, criando e definindo os objetos. Também interage com o AppRepostiory, se comunicando com o usuário e com a Database.

#### Notification Manager
Recebe os comandos da Activity e cria uma notificação.
 
#### Broadcast Receiver
Recebe o input do NotificationManager e faz a ponte entre o Sistema de Notificações do Android e a aplicação.

#### App Repository
Define a comunicação com as operações CRUD na Database.

#### Database
Armazena os dados de forma persistente.

## Estilo Arquitetural da Aplicação
  
### MVVM
A aplicação se utiliza a arquitetura Model View ViewModel, uma vez que:

1. O LembretioUi age como View, uma vez que faz a interface com o usuário.
2. A Activity  age como ViewModel, uma vez que recebe informações tanto da camada View, quanto da camada Model, realizando operações com as informações obtidas dessas duas camadas.
3. O AppRepository age como Model, uma vez que gerencia a lógica de operações na Database e envia as informações necessárias para a Activity.

### PUBSUB

O NotificationManager age como PUB uma vez que recebe as informações de um evento da Activity e agenda uma notificação.

Broadcast Receiver age como SUB uma vez que recebe a informação do NotificationManager, cria uma notificação e se comunica com o Sistema Android.

