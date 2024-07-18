package tratarDados;

import pagamento.Cartao;
import pagamento.Pix;
import ingresso.Ingresso;

import java.io.Serializable;
import java.util.Scanner;

public class TesteDeConfirmacao implements Serializable {
    private static final long serialVersionUID = 1L;

    // Atributos
    private transient Scanner scanner;

    // Construtor
    public TesteDeConfirmacao() {
        this.scanner = new Scanner(System.in);
    }

    // Método para reinicializar o Scanner após a desserialização
    public void reinicializarScanner() {
        this.scanner = new Scanner(System.in);
    }

    // Método para verificar se o CPF contém apenas números
    public String receberCpf() {
        while (true) {
            System.out.println("Digite o CPF do comprador:");
            String cpf = scanner.nextLine();

            if (cpf.matches("\\d{11}")) { // Verifica se o CPF tem exatamente 11 dígitos
                return cpf;
            } else {
                System.out.println("Erro: o CPF deve conter exatamente 11 números. Tente novamente.");
            }
        }
    }

    // Método para formatar o CPF no padrão ###.###.###-##
    public String formatarCpf(String cpf) {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    // Método para verificar se a compra foi confirmada ou não.
    public String confirmacaoCompra() {
        while (true) {
            System.out.println("Deseja confirmar a compra? (1 para Sim, 2 para Não)");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equals("1") || confirmacao.equalsIgnoreCase("Sim")) {
                System.out.println("Pagamento realizado com sucesso!");
                return confirmacao;
            } else if (confirmacao.equals("2") || confirmacao.equalsIgnoreCase("Não")) {
                System.out.println("Pagamento cancelado com sucesso!");
                return confirmacao;
            } else {
                System.out.println("Erro ao realizar a compra! Escolha novamente.");
            }
        }
    }

    public void processarCompra() {
        boolean continuarComprando = true;

        while (continuarComprando) {
            System.out.print("Digite o nome do titular: ");
            String nomeComprador = scanner.nextLine();

            // Verificar se a variável nomeTitular possui apenas letras e espaços
            if (!nomeComprador.matches("[a-zA-Z ]+")) {
                System.out.println("Erro: O nome do titular deve conter apenas letras e espaços.");
                continue;
            }

            String cpf = receberCpf();
            Ingresso ingresso = new Ingresso(50.0); // Exemplo de valor de ingresso

            int quantidadeIngressos = ingresso.solicitarQuantidadeIngressos(scanner);
            double valorFinal = ingresso.solicitarTipoIngressos(quantidadeIngressos, scanner);

            String confirmacao = confirmacaoCompra();
            if (confirmacao.equals("1") || confirmacao.equalsIgnoreCase("Sim")) {
                while (true) {
                    System.out.println("Digite a escolha de pagamento (1 para PIX, 2 para Cartão): ");
                    String escolha = scanner.nextLine();

                    if (escolha.equals("1") || escolha.equalsIgnoreCase("Pix")) {
                        String cpfFormatado = formatarCpf(cpf);
                        String chavePixEstabelecimento = "u123y8ur891iu21h-9u1h931";
                        System.out.println("Copie a chave PIX do estabelecimento: " + chavePixEstabelecimento);

                        // Simulação de confirmação de pagamento
                        Pix pagamento = new Pix(chavePixEstabelecimento, valorFinal, cpfFormatado, nomeComprador);
                        System.out.println(pagamento);

                        break; // Saída do loop
                    } else if (escolha.equals("2") || escolha.equalsIgnoreCase("Cartao") || escolha.equalsIgnoreCase("Cartão")) {
                        Cartao cartao = new Cartao();

                        cartao.capturarDados(scanner);
                        cartao.identificarBandeira();
                        cartao.capturarCvc(scanner);
                        cartao.capturarTipoDePagamento(scanner);
                        cartao.perguntarParcelas(scanner);
                        cartao.setValor(valorFinal); // Define o valor final no cartão

                        // Simulação de confirmação de pagamento
                        System.out.println(cartao);

                        break; // Saída do loop principal
                    } else {
                        System.out.println("Escolha inválida, por favor tente novamente.");
                    }
                }
            } else {
                System.out.println("A compra não foi confirmada. Processo encerrado.");
            }

            while (true) {
                System.out.println("Deseja realizar outra compra? (1 para Sim, 2 para Não)");
                String resposta = scanner.nextLine();

                if (resposta.equals("1") || resposta.equalsIgnoreCase("Sim")) {
                    break;
                } else if (resposta.equals("2") || resposta.equalsIgnoreCase("Não")) {
                    continuarComprando = false;
                    break;
                } else {
                    System.out.println("Resposta inválida. Digite 1 para Sim ou 2 para Não.");
                }
            }
        }

        System.out.println("Processo encerrado.");
    }
}
