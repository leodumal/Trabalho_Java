import java.sql.*;
import java.util.Scanner;

public class Cadastro_Açougue {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Trab S";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "leolindo1";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirMenu();
        criarTabelaProdutos();
        
    }
    public static void criarTabelaProdutos() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos (id SERIAL PRIMARY KEY, nome VARCHAR(100), tipo_carne INT)";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Tabela 'produtos' criada com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela 'produtos': " + e.getMessage());
        }
    }

    public static void exibirMenu() {
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Cadastrar um Produto");
            System.out.println("2. Exibir um Produto");
            System.out.println("3. Atualizar um Produto");
            System.out.println("4. Excluir um Produto");
            System.out.println("5. Sair");
            System.out.println("6. Criar uma Tabela");
            

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    exibirProdutos();
                    break;
                case 3:
                    atualizarProduto();
                    break;
                case 4:
                    excluirProduto();
                    break;
                case 5:
                    System.out.println("Encerrando o programa...");
                    break;
                case 6 :
                    criarTabelaProdutos();
                break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cadastrarProduto() {
        System.out.println("Digite o nome do produto:");
        String nome = scanner.nextLine();

        System.out.println("Digite o tipo de carne (1 - 1°, 2 - 2°):");
        int tipoCarne = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer do scanner

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO produtos (nome, tipo_carne) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nome);
            statement.setInt(2, tipoCarne);
            statement.executeUpdate();
            System.out.println("Produto cadastrado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o produto: " + e.getMessage());
        }
    }

    public static void exibirProdutos() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM produtos";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Lista de produtos:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                int tipoCarne = resultSet.getInt("tipo_carne");
                System.out.println("ID: " + id + ", Nome: " + nome + ", Tipo de Carne: " + tipoCarne);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir produtos: " + e.getMessage());
        }
    }

    public static void atualizarProduto() {
        System.out.println("Escreva o ID do produto que quer atualizar:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Escreva o novo nome do produto:");
        String novoNome = scanner.nextLine();

        System.out.println("Diga se a carne é de primeira ou de segunda (1 - 1°, 2 - 2°):");
        int novoTipoCarne = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE produtos SET nome = ?, tipo_carne = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, novoNome);
            statement.setInt(2, novoTipoCarne);
            statement.setInt(3, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("O produto foi atualizado");
            } else {
                System.out.println("Nenhum produto foi encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    public static void excluirProduto() {
        System.out.println("Digite o ID do produto que deseja excluir:");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM produtos WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Produto excluído com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir o produto: " + e.getMessage());
        }
    }
}

