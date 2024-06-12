package app;

import lib.ArvoreBinaria;
import lib.IArvoreBinaria;
import lib.No;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
public class Aplicativo {
    ArvoreBinaria<Aluno> alunos = new ArvoreBinaria<Aluno>(new ComparadorAlunoPorMatricula());
    ArvoreBinaria<Disciplina> disciplinas = new ArvoreBinaria<>(new ComparadorDisciplina());

    public void CadastrarAluno() {
        Scanner cadAluno = new Scanner(System.in);

        try {
            System.out.println("Digite o nome do aluno: ");
            String nomeAluno = cadAluno.nextLine();
            System.out.println("Digite a matrícula do aluno: ");
            int matriculaAluno = cadAluno.nextInt();
            Aluno aluno = new Aluno(matriculaAluno, nomeAluno);
            alunos.adicionar(aluno);
            System.out.println("Aluno " + nomeAluno + " cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar aluno!");
        }
    }

    public void CadastrarDisciplina() {
        Scanner cadDisciplina = new Scanner(System.in);

        try {
            System.out.println("Digite o nome da disciplina:");
            String nomeDisciplina = cadDisciplina.nextLine();
            System.out.println("Digite a carga horária da disciplina:");
            int horaDisciplina = cadDisciplina.nextInt();
            System.out.println("Digite o código da disciplina:");
            int codigoDisciplina = cadDisciplina.nextInt();
            Disciplina disciplina = new Disciplina(codigoDisciplina, nomeDisciplina, horaDisciplina);
            disciplinas.adicionar(disciplina);
            System.out.println("Disciplina " + nomeDisciplina + " cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao preencher os dados.");
        }
    }

    public void CadastrarPreRequisito() {
        Scanner disciplinaScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Disciplinas cadastradas: \n");
            System.out.println(disciplinas.caminharEmOrdem());

            try {
                Disciplina disciplinaEscolhida = null;
                while (disciplinaEscolhida == null) {
                    System.out.println("Digite o código da disciplina que deseja adicionar um novo pré-requisito (ou 0 para voltar ao menu): ");
                    int codDisciplina = disciplinaScanner.nextInt();
                    if (codDisciplina == 0) {
                        return;  // Volta ao menu principal
                    }
                    disciplinaEscolhida = disciplinas.pesquisar(new Disciplina(codDisciplina, "", 0));
                    if (disciplinaEscolhida == null) {
                        System.out.println("A disciplina escolhida não foi encontrada. Por favor, insira um código válido.");
                    }
                }

                Disciplina preRequisito = null;
                while (preRequisito == null) {
                    System.out.println("Digite o código da disciplina que deseja adicionar como pré-requisito (ou 0 para voltar ao menu): ");
                    int codPreRequisito = disciplinaScanner.nextInt();
                    if (codPreRequisito == 0) {
                        return;  // Volta ao menu principal
                    }
                    preRequisito = disciplinas.pesquisar(new Disciplina(codPreRequisito, "", 0));
                    if (preRequisito == null) {
                        System.out.println("A disciplina pré-requisito não foi encontrada. Por favor, insira um código válido.");
                    }
                }

                if (disciplinaEscolhida != null && preRequisito != null) {
                    if (disciplinaEscolhida.getPreRequisitos().contains(preRequisito)) {
                        System.out.println("A disciplina escolhida já possui esse pré-requisito!");
                    } else {
                        disciplinaEscolhida.addPreRequisito(preRequisito);
                        System.out.println("Pré-requisito cadastrado com sucesso");
                        return;  // Volta ao menu principal
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao adicionar pré-requisito. Deseja tentar novamente? (s/n)");
                if (disciplinaScanner.next().equalsIgnoreCase("n")) {
                    return;  // Volta ao menu principal
                }
            }
        }
    }

    public void disciplinasCursadas() {
        Scanner scanner = new Scanner(System.in);
11
        try {
            System.out.println("Digite a matrícula do aluno:");
            int matricula = scanner.nextInt();
            Aluno aluno = new Aluno(matricula, ""); // Criando um objeto aluno apenas com a matrícula para pesquisa

            System.out.println("Digite o código da disciplina:");
            int codigoDisciplina = scanner.nextInt();
            Disciplina disciplina = new Disciplina(codigoDisciplina, "", 0); // Criando um objeto disciplina apenas com o código para pesquisa

            Aluno alunoEncontrado = alunos.pesquisar(aluno);
            Disciplina disciplinaEncontrada = disciplinas.pesquisar(disciplina);

            if (alunoEncontrado != null && disciplinaEncontrada != null) {
                boolean cursouPreRequisitos = true;

                // Verifica se o aluno cursou todos os pré-requisitos da disciplina
                for (Disciplina preRequisito : disciplinaEncontrada.getPreRequisitos()) {
                    boolean cursouPreRequisito = false;
                    for (Disciplina disciplinaCursada : alunoEncontrado.getDisciplinasCursadas()) {
                        if (disciplinaCursada.equals(preRequisito)) {
                            cursouPreRequisito = true;
                            break;
                        }
                    }
                    if (!cursouPreRequisito) {
                        cursouPreRequisitos = false;
                        System.out.println("O aluno não cursou o pré-requisito: " + preRequisito.getNome());
                    }
                }

                // Se o aluno cursou todos os pré-requisitos, registra que ele cursou a disciplina
                if (cursouPreRequisitos) {
                    System.out.println("Aluno cursou a disciplina " + disciplinaEncontrada.getNome() + " registrada com sucesso!");
                } else {
                    System.out.println("O aluno não cursou todas as disciplinas pré-requisito.");
                }
            } else {
                System.out.println("Aluno ou disciplina não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar a operação.");
        }
    }

    public void consultarAlunoPorNome() {
    Scanner scanner = new Scanner(System.in);

    try {
        System.out.println("Digite o nome do aluno:");
        String nomeAluno = scanner.nextLine();

        boolean encontrado = false;
        Stack<No<Aluno>> pilha = new Stack<>();
        
        // Obtendo a raiz da árvore de alunos usando um método na classe ArvoreBinaria
        No<Aluno> atual = alunos.getRaiz();

        while (atual != null || !pilha.isEmpty()) {
            while (atual != null) {
                pilha.push(atual);
                atual = atual.getFilhoEsquerda(); // Acessamos o filho à esquerda
            }

            atual = pilha.pop();
            if (atual.getValor().getNome().equalsIgnoreCase(nomeAluno)) {
                encontrado = true;
                System.out.println("Matrícula: " + atual.getValor().getMatricula());
                System.out.println("Nome: " + atual.getValor().getNome());
                System.out.println("Disciplinas cursadas:");

                // Convertendo o array de Disciplina para List<Disciplina>
                List<Disciplina> disciplinasCursadas = Arrays.asList(atual.getValor().getDisciplinasCursadas());

                if (disciplinasCursadas.isEmpty()) {
                    System.out.println("Nenhuma disciplina cursada.");
                } else {
                    for (Disciplina disciplina : disciplinasCursadas) {
                        System.out.println("Código: " + disciplina.getCodigo() + ", Nome: " + disciplina.getNome());
                    }
                }

                break;
            }

            atual = atual.getFilhoDireita(); // Acessamos o filho à direita
        }

        if (!encontrado) {
            System.out.println("Aluno não encontrado.");
        }
    } catch (Exception e) {
        System.out.println("Erro ao processar a operação.");
    }
}

public void consultarAlunoPorMatricula() {
    Scanner scanner = new Scanner(System.in);

    try {
        System.out.println("Digite a matrícula do aluno:");
        int matriculaAluno = scanner.nextInt();

        boolean encontrado = false;
        Stack<No<Aluno>> pilha = new Stack<>();

        // Obtendo a raiz da árvore de alunos usando o método getRaiz()
        No<Aluno> atual = alunos.getRaiz();

        while (atual != null || !pilha.isEmpty()) {
            while (atual != null) {
                pilha.push(atual);
                atual = atual.getFilhoEsquerda(); // Acessamos o filho à esquerda
            }

            atual = pilha.pop();
            if (atual.getValor().getMatricula() == matriculaAluno) {
                encontrado = true;
                System.out.println("Matrícula: " + atual.getValor().getMatricula());
                System.out.println("Nome: " + atual.getValor().getNome());
                System.out.println("Disciplinas cursadas:");

                // Convertendo o array de Disciplina para List<Disciplina>
                List<Disciplina> disciplinasCursadas = Arrays.asList(atual.getValor().getDisciplinasCursadas());

                if (disciplinasCursadas.isEmpty()) {
                    System.out.println("Nenhuma disciplina cursada.");
                } else {
                    for (Disciplina disciplina : disciplinasCursadas) {
                        System.out.println("Código: " + disciplina.getCodigo() + ", Nome: " + disciplina.getNome());
                    }
                }

                break;
            }

            atual = atual.getFilhoDireita(); // Acessamos o filho à direita
        }

        if (!encontrado) {
            System.out.println("Aluno com matrícula " + matriculaAluno + " não encontrado.");
        }
    } catch (Exception e) {
        System.out.println("Erro ao processar a operação.");
    }
}

public void excluirAlunoPorMatricula() {
    Scanner scanner = new Scanner(System.in);

    try {
        System.out.println("Digite a matrícula do aluno a ser excluído:");
        int matriculaAluno = scanner.nextInt();

        boolean encontrado = false;
        Stack<No<Aluno>> pilha = new Stack<>();

        // Obtendo a raiz da árvore de alunos usando o método getRaiz()
        No<Aluno> atual = alunos.getRaiz();
        No<Aluno> pai = null;

        while (atual != null || !pilha.isEmpty()) {
            while (atual != null) {
                pilha.push(atual);
                pai = atual; // Mantemos uma referência ao pai do nó atual
                atual = atual.getFilhoEsquerda(); // Acessamos o filho à esquerda
            }

            atual = pilha.pop();
            if (atual.getValor().getMatricula() == matriculaAluno) {
                encontrado = true;
                // Verificamos se o nó atual é a raiz da árvore
                if (atual == alunos.getRaiz()) {
                    // Se for, removemos a raiz e ajustamos a árvore
                    alunos.remover(atual.getValor());
                } else {
                    // Caso contrário, removemos o nó do pai
                    if (pai.getFilhoEsquerda() == atual) {
                        pai.setFilhoEsquerda(null);
                    } else {
                        pai.setFilhoDireita(null);
                    }
                }
                System.out.println("Aluno removido com sucesso.");
                break;
            }

            pai = atual; // Atualizamos o pai antes de acessar o próximo nó
            atual = atual.getFilhoDireita(); // Acessamos o filho à direita
        }

        if (!encontrado) {
            System.out.println("Aluno com matrícula " + matriculaAluno + " não encontrado.");
        }
    } catch (Exception e) {
        System.out.println("Erro ao processar a operação.");
    }
}

    public void menu() {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.println("1 - Cadastrar Aluno");
            System.out.println("2 - Cadastrar Disciplina");
            System.out.println("3 - Cadastrar pré-requisito");
            System.out.println("4 - Verificar disciplinas cursadas por aluno");
            System.out.println("5 - Consultar aluno por nome");
            System.out.println("6 - Consultar aluno por matrícula");
            System.out.println("7 - Excluir aluno por matrícula");
            System.out.println("0 - Sair");
            System.out.println("Digite sua opção:");
            String opcao = s.nextLine();
            if (opcao.equals("1")) {
                CadastrarAluno();
            } else if (opcao.equals("2")) {
                CadastrarDisciplina();
            } else if (opcao.equals("3")) {
                CadastrarPreRequisito();
            } else if (opcao.equals("4")) {
                disciplinasCursadas();
            } else if (opcao.equals("5")) {
                consultarAlunoPorNome();
            } else if (opcao.equals("6")) {
                consultarAlunoPorMatricula();
            } else if (opcao.equals("7")) {
                excluirAlunoPorMatricula();
            } else if (opcao.equals("0")) {
                System.out.println("Obrigado por usar o sistema!");
                return;
            } else {
                System.out.println("Opção inválida, por favor digite novamente.");
            }
        }
    }


    public static void main(String[] args) {
        Aplicativo app = new Aplicativo();
        app.menu();
    }
}
