# DevTITANS-Projeto-IR

Equipe 5
Tema: Sensor Infravermelho
Orientador: Prof. Souto
Ciclo Atual: Planejamento Inicial
Período: 28/8 - 02/9
Alunos: André, Erlon, Evandro, Lucas e Wadrian

Parabéns a todos por chegarem à etapa final do curso! Vocês demonstraram muito empenho e dedicação ao longo dessa jornada,
e agora estamos na reta final para a entrega do projeto. De 28/08 até 02/09, vocês estarão focados na fase de planejamento,
uma etapa crucial para garantir que todo o trabalho seja executado de forma organizada e eficiente.

0 - Sobre o Tema

  O objetivo é criar um protótipo de sensor infravermelho usando esp32 e conecta-lo no android passando por todas as camadas. 
  Para auxiliar a equipe na divisão de tarefas, segue as seguintes atividades obrigatórias:

  Protótipo esp32 + sensores
  Driver do protótipo
  Instalação do driver no AOSP (Emulador)
  Biblioteca do dispositivo segundo o Lab 5-5 (Emulador)
  Implementar a HAL customizada para o sensor IR Lab 5-7 (Emulador)
  Cliente da HAL e aplicativo Inicial Lab 5-8 (Emulador)
  Manager Lab 5-9 (Emulador)
  Aplicativo Final (Emulador)
    Fica a critério da equipe as funcionalidades do aplicativo final, mas no mínimo ele deve ser capaz de enviar um comando para ligar/desligar um dispositivo

  #Implementar a IR HAL 
    Depois de ter uma versão funcional no emulador, a equipe deve pesquisar e implementar a solução usando a HAL de Infravermelho do android
    https://cs.android.com/android/platform/superproject/main/+/main:hardware/interfaces/ir/aidl/

  ##Implementar serviço e o cliente HAL em rust
    Rust oferece garantias de segurança de memória em tempo de compilação, evitando erros comuns como null pointer dereferencing, buffer overflows,
    e use-after-free sem a necessidade de um coletor de lixo. O sistema de "ownership" e "borrowing" de Rust gerencia a alocação de memória de forma eficiente e segura.

  Portar a solução para o celular
    Essa etapa deve acontecer de forma simultânea com a IR HAL, podem usar o Lab 5-13 como referência.
    Considerem no planejamento da equipe que cada etapa do projeto no emulador deve ser portado (biblioteca, hal, cliente, manager, aplicativo).


  #   Desafio Nível 1 - Requer pesquisa
  ##  Desafio Nível 2 - Requer aprender o básico dessa nova linguagem
  ### Desafio Nível 3 - ?

1 - Cronograma do Hands-On

28/08 - 02/09   Primeiro Ciclo / Planejamento Inicial
04/09 - 13/09   Segundo Ciclo
16/09 - 27/09   Terceiro Ciclo / Apresentação Parcial
30/09 - 11/10   Terceiro Ciclo
14/10 - 25/10   Quarto Ciclo / Apresentação final

cada ciclo+planejamento tem uma apresentação no final com um integrante diferente, a lista de apresentadores deve ser entregue

* As apresentações parciais e finais serão para o sponsor do projeto Daniel Finimundi

2 - O que entregar no dia 02/09

2.1 - Levantamento de Material Necessário:

Identifiquem todos os sensores e componentes necessários para o desenvolvimento do projeto. Entre em contato com o orientador da equipe para conferir a 
disponibilidade dos sensores.
Inicialmente 2 sensores: Emissor e Receptor IR
Certifiquem-se de que todos os materiais estão disponíveis e em condições de uso.

2.2 - Criação do Repositório no GitHub:

Criem um repositório no GitHub e adicionem todos os integrantes da equipe.
O repositório deve conter um projeto com a metodologia Kanban, onde todas as atividades necessárias até o término do projeto estejam bem definidas.
Deve conter uma página de Wiki para cada atividade.
Dividam as tarefas entre os membros do grupo, garantindo que todos saibam exatamente o que precisam fazer.
Todas as atividades devem ser claramente atribuídas a um ou mais responsável dentro do Kanban.

2.3 - Lista de apresentadores:
Teremos 5 apresentações durante o Hands-On final cada ciclo, a lista com a ordem de apresentação deve ser entregue no dia 02/09.

2.4 - Apresentação 

Criem um slide sobre o planejamento para o dia 02/09, nele deve ter todas as atividades e seus responsáveis.

Estrutura Recomendada para o Slide
  Título do Slide:
    "Planejamento da Equipe X - Tarefas e Responsáveis"

  Introdução Breve (1-2 frases) e fotos das reuniões:
    "Este slide apresenta a distribuição das tarefas entre os membros da equipe, com foco na organização e eficiência para a conclusão do projeto."
  
  Lista de Tarefas:
    Organize as tarefas principais em uma lista ou tabela.

    Colunas Sugeridas:

    Tarefa: Descrição breve da tarefa.
    Responsável: Nome do aluno responsável pela tarefa.
    Prazo: Data de conclusão esperada.
    Exemplo de Estrutura:

    Tarefa: "Levantamento de Sensores"
    Responsável: João Silva 
    Prazo: até 30/08

O slide deve ser apresentado por um único aluno no dia 02.
