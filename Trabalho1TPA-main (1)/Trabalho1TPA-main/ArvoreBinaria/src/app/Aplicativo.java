package app;

import lib.ArvoreBinaria;

import java.util.Scanner;
import java.util.Stack;

public class Aplicativo {
    ArvoreBinaria<Aluno> alunos = new ArvoreBinaria<>(new ComparadorAlunoPorMatricula());
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
                    for (Disciplina disciplinaCursada : alunoEncontrado.getDiscCursadas()) {
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
                    alunoEncontrado.addDiscCursada(disciplinaEncontrada);
                    System.out.println("Disciplina adicionada às disciplinas cursadas pelo aluno.");
                }
            } else {
                System.out.println("Aluno ou disciplina não encontrados.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao registrar disciplinas cursadas.");
        }
    }

    public void consultarAlunoPorNome() {
        Scanner alunoNomeScanner = new Scanner(System.in);
        System.out.println("Digite o nome do aluno a ser consultado: ");
        String nomeAluno = alunoNomeScanner.nextLine();
        Stack<Aluno> resultado = new Stack<>();
        alunos.emOrdem(aluno -> {
            if (aluno.getNome().equalsIgnoreCase(nomeAluno)) {
                resultado.add(aluno);
            }
        });
        if (resultado.isEmpty()) {
            System.out.println("Aluno não encontrado.");
        } else {
            for (Aluno aluno : resultado) {
                System.out.println("Matrícula: " + aluno.getMatricula());
                System.out.println("Nome: " + aluno.getNome());
                System.out.println("Disciplinas Cursadas: ");
                for (Disciplina disciplina : aluno.getDisciplinasCursadas()) {
                    System.out.println(disciplina);
                }
            }
        }
    }

    public void consultarAlunoPorMatricula() {
        Scanner alunoMatriculaScanner = new Scanner(System.in);
        System.out.println("Digite a matrícula do aluno a ser consultado: ");
        int matriculaAluno = alunoMatriculaScanner.nextInt();
        Aluno aluno = alunos.pesquisar(new Aluno(matriculaAluno, ""));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
        } else {
            System.out.println("Matrícula: " + aluno.getMatricula());
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("Disciplinas Cursadas: ");
            for (Disciplina disciplina : aluno.getDisciplinasCursadas()) {
                System.out.println(disciplina);
            }
        }
    }

    public void removerAluno() {
        Scanner remAluno = new Scanner(System.in);
        System.out.println("Digite a matrícula do aluno a ser removido:");
        int matRemAluno = remAluno.nextInt();
        Aluno alunoRemover = alunos.pesquisar(new Aluno(matRemAluno, ""));
        if (alunoRemover != null) {
            alunos.remover(alunoRemover);
            System.out.println("Aluno removido com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

// CÓDIGO PADRÃO
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
                removerAluno();
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

// CÓDIGO COM LEITURA DE INPUT A PARTIR DE ARQUIVOS (ISSO FOI COMPLICADO PRA CARAMBA)
    /*
    public void CadastrarAluno(String nomeAluno, int matriculaAluno) {
        try {
            Aluno aluno = new Aluno(matriculaAluno, nomeAluno);
            alunos.adicionar(aluno);
            System.out.println("Aluno " + nomeAluno + " cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar aluno!");
        }
    }

    public void CadastrarDisciplina(String nomeDisciplina, int horaDisciplina, int codigoDisciplina) {
        try {
            Disciplina disciplina = new Disciplina(codigoDisciplina, nomeDisciplina, horaDisciplina);
            disciplinas.adicionar(disciplina);
            System.out.println("Disciplina " + nomeDisciplina + " cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao preencher os dados.");
        }
    }

    public void CadastrarPreRequisito(int codDisciplina, int codPreRequisito) {
        try {
            Disciplina disciplinaEscolhida = disciplinas.pesquisar(new Disciplina(codDisciplina, "", 0));
            Disciplina preRequisito = disciplinas.pesquisar(new Disciplina(codPreRequisito, "", 0));
            if (disciplinaEscolhida != null && preRequisito != null) {
                if (disciplinaEscolhida.getPreRequisitos().contains(preRequisito)) {
                    System.out.println("A disciplina escolhida já possui esse pré-requisito!");
                } else {
                    disciplinaEscolhida.addPreRequisito(preRequisito);
                    System.out.println("Pré-requisito cadastrado com sucesso");
                }
            } else {
                System.out.println("Disciplina ou pré-requisito não encontrados.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao adicionar pré-requisito.");
        }
    }

    public void DisciplinasCursadas(int matricula, int codigoDisciplina) {
        try {
            Aluno aluno = new Aluno(matricula, ""); // Criando um objeto aluno apenas com a matrícula para pesquisa
            Disciplina disciplina = new Disciplina(codigoDisciplina, "", 0); // Criando um objeto disciplina apenas com o código para pesquisa
            Aluno alunoEncontrado = alunos.pesquisar(aluno);
            Disciplina disciplinaEncontrada = disciplinas.pesquisar(disciplina);
            if (alunoEncontrado != null && disciplinaEncontrada != null) {
                boolean cursouPreRequisitos = true;
                // Verifica se o aluno cursou todos os pré-requisitos da disciplina
                for (Disciplina preRequisito : disciplinaEncontrada.getPreRequisitos()) {
                    boolean cursouPreRequisito = false;
                    for (Disciplina disciplinaCursada : alunoEncontrado.getDiscCursadas()) {
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
                    alunoEncontrado.addDiscCursada(disciplinaEncontrada);
                    System.out.println("Disciplina adicionada às disciplinas cursadas pelo aluno.");
                }
            } else {
                System.out.println("Aluno ou disciplina não encontrados.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao registrar disciplinas cursadas.");
        }
    }

    public void ConsultarAlunoPorNome(String nomeAluno) {
        Stack<Aluno> resultado = new Stack<>();
        alunos.emOrdem(new NoVisitor<Aluno>() {
            @Override
            public void visit(Aluno aluno) {
                if (aluno.getNome().equalsIgnoreCase(nomeAluno)) {
                    resultado.add(aluno);
                }
            }
        });
        if (resultado.isEmpty()) {
            System.out.println("Aluno não encontrado.");
        } else {
            for (Aluno aluno : resultado) {
                System.out.println("Matrícula: " + aluno.getMatricula());
                System.out.println("Nome: " + aluno.getNome());
                System.out.println("Disciplinas Cursadas: ");
                for (Disciplina disciplina : aluno.getDiscCursadas()) {
                    System.out.println(disciplina);
                }
            }
        }
    }

    public void ConsultarAlunoPorMatricula(int matriculaAluno) {
        Aluno aluno = alunos.pesquisar(new Aluno(matriculaAluno, ""));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
        } else {
            System.out.println("Matrícula: " + aluno.getMatricula());
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("Disciplinas Cursadas: ");
            for (Disciplina disciplina : aluno.getDiscCursadas()) {
                System.out.println(disciplina);
            }
        }
    }

    public void RemoverAluno(int matRemAluno) {
        Aluno alunoRemover = alunos.pesquisar(new Aluno(matRemAluno, ""));
        if (alunoRemover != null) {
            alunos.remover(alunoRemover);
            System.out.println("Aluno removido com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    public void processarArquivoDeInput(String caminhoArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                int opcao = Integer.parseInt(linha);
                switch (opcao) {
                    case 1:
                        String nomeAluno = reader.readLine();
                        int matriculaAluno = Integer.parseInt(reader.readLine());
                        CadastrarAluno(nomeAluno, matriculaAluno);
                        break;
                    case 2:
                        String nomeDisciplina = reader.readLine();
                        int cargaHoraria = Integer.parseInt(reader.readLine());
                        int codigoDisciplina = Integer.parseInt(reader.readLine());
                        CadastrarDisciplina(nomeDisciplina, cargaHoraria, codigoDisciplina);
                        break;
                    case 3:
                        int codigoDisc = Integer.parseInt(reader.readLine());
                        int codigoPreReq = Integer.parseInt(reader.readLine());
                        CadastrarPreRequisito(codigoDisc, codigoPreReq);
                        break;
                    case 4:
                        int matricula = Integer.parseInt(reader.readLine());
                        codigoDisc = Integer.parseInt(reader.readLine());
                        DisciplinasCursadas(matricula, codigoDisc);
                        break;
                    case 5:
                        String nomeConsulta = reader.readLine();
                        ConsultarAlunoPorNome(nomeConsulta);
                        break;
                    case 6:
                        int matriculaConsulta = Integer.parseInt(reader.readLine());
                        ConsultarAlunoPorMatricula(matriculaConsulta);
                        break;
                    case 7:
                        int matriculaRemocao = Integer.parseInt(reader.readLine());
                        RemoverAluno(matriculaRemocao);
                        break;
                    default:
                        System.out.println("Opção inválida no arquivo de input: " + opcao);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de input: " + e.getMessage());
        }
    }

    public void menu() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Cadastrar disciplina");
            System.out.println("3 - Cadastrar pré-requisito");
            System.out.println("4 - Disciplinas cursadas");
            System.out.println("5 - Consultar aluno por nome");
            System.out.println("6 - Consultar aluno por matrícula");
            System.out.println("7 - Remover aluno");
            System.out.println("8 - Ler arquivo de input");
            System.out.println("9 - Sair");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    CadastrarAluno();
                    break;
                case 2:
                    CadastrarDisciplina();
                    break;
                case 3:
                    CadastrarPreRequisito();
                    break;
                case 4:
                    disciplinasCursadas();
                    break;
                case 5:
                    consultarAlunoPorNome();
                    break;
                case 6:
                    consultarAlunoPorMatricula();
                    break;
                case 7:
                    removerAluno();
                    break;
                case 8:
                    System.out.println("Digite o caminho do arquivo de input:");
                    String caminhoArquivo = scanner.next();
                    processarArquivoDeInput(caminhoArquivo);
                    break;
                case 9:
                    System.out.println("Encerrando sistema.");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static void main(String[] args) {
        Aplicativo app = new Aplicativo();
        app.processarArquivoDeInput("C:\\Users\\thalison.ribeiro\\Desktop\\tpateste-main\\Trabalho1TPA-main (1)\\Trabalho1TPA-main\\ArvoreBinaria\\src\\app\\input.txt");
        app.menu();
    }
}*/
