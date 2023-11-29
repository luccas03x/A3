package a3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class Admin extends Usuario {

    static Conexao con = new Conexao("root", "RootAdmin123");

    public Admin(int id, String nome, float altura, int idade, float peso, int frequencia, String genero) {
        super(id, nome, altura, idade, peso, frequencia, genero, true, frequencia);
    }

    public static void showAdminOptions(Connection connection) {
        String[] adminOptions = { "Exibir Todos os Usuarios", "Deletar Usuario", "Alterar Usuario",
                "Cadastrar Usuario ou Cadastrar Admin",
                "Alterar Exercicio ou Cadastrar Exercicio", "Alterar Exercicio do Usuario",
                "Mostrar Todos os Exercicios" };

        JFrame frame = new JFrame("FitWeek : ADMIN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the title label properties
        JLabel tituloLabel = new JLabel("FitWeek : ADMIN");
        tituloLabel.setHorizontalAlignment(JLabel.CENTER);
        tituloLabel.setForeground(Color.RED);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(tituloLabel);

        frame.setLayout(new GridLayout(adminOptions.length + 1, 1));

        tituloLabel.setHorizontalAlignment(JLabel.CENTER);

        for (String option : adminOptions) {
            JButton button = new JButton(option);
            frame.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (option) {
                        case "Exibir Todos os Usuarios":
                            try {
                                Admin.exibirTodosUsuarios(connection);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Deletar Usuario":
                            try {
                                Admin.deletarUsuario(connection, new Scanner(System.in));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Alterar Usuario":
                            try {
                                Admin.alterarUsuario(connection, new Scanner(System.in));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Cadastrar Usuario ou Cadastrar Admin":
                            Admin.cadastrarAdmin(connection);
                            break;
                        case "Alterar Exercicio ou Cadastrar Exercicio":
                            try {
                                Admin.alterarExercicio(connection, new Scanner(System.in));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Alterar Exercicio do Usuario":
                            try {
                                Admin.AlterarExercicioDoUsuario(connection, new Scanner(System.in));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "Mostrar Todos os Exercicios":
                            try {
                                Admin.mostratTodosExercicios(connection);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opcao invalida.");
                            break;
                    }
                }
            });
        }

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public static void cadastrarAdmin(Connection connection) {
        JFrame frame = new JFrame("Cadastro de Administrador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(10, 2));

        JLabel nameLabel = new JLabel("Nome:");
        JTextField nameField = new JTextField();
        frame.add(nameLabel);
        frame.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        frame.add(emailLabel);
        frame.add(emailField);

        JLabel senhaLabel = new JLabel("Senha:");
        JPasswordField senhaField = new JPasswordField();
        frame.add(senhaLabel);
        frame.add(senhaField);

        JLabel alturaLabel = new JLabel("Altura (metros):");
        JTextField alturaField = new JTextField();
        frame.add(alturaLabel);
        frame.add(alturaField);

        JLabel idadeLabel = new JLabel("Idade (anos):");
        JTextField idadeField = new JTextField();
        frame.add(idadeLabel);
        frame.add(idadeField);

        JLabel pesoLabel = new JLabel("Peso (quilos):");
        JTextField pesoField = new JTextField();
        frame.add(pesoLabel);
        frame.add(pesoField);

        JLabel frequenciaLabel = new JLabel("Frequência semanal de exercícios (0 a 7):");
        String[] frequenciaOptions = { "0", "1", "2", "3", "4", "5", "6", "7" };
        JComboBox<String> frequenciaComboBox = new JComboBox<>(frequenciaOptions);
        frame.add(frequenciaLabel);
        frame.add(frequenciaComboBox);

        JLabel generoLabel = new JLabel("Gênero:");
        String[] generoOptions = { "M", "F" };
        JComboBox<String> generoComboBox = new JComboBox<>(generoOptions);
        frame.add(generoLabel);
        frame.add(generoComboBox);

        JLabel adminLabel = new JLabel("Administrador (S/N):");
        String[] adminOptions = { "S", "N" };
        JComboBox<String> adminComboBox = new JComboBox<>(adminOptions);
        frame.add(adminLabel);
        frame.add(adminComboBox);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nameField.getText();
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
                float altura = Float.parseFloat(alturaField.getText());
                int idade = Integer.parseInt(idadeField.getText());
                float peso = Float.parseFloat(pesoField.getText());
                int frequencia = Integer.parseInt((String) frequenciaComboBox.getSelectedItem());
                String genero = (String) generoComboBox.getSelectedItem();
                boolean admin = ((String) adminComboBox.getSelectedItem()).equalsIgnoreCase("S");

                String sql = "INSERT INTO usuario (nome, email, senha, altura, idade, peso, frequencia, genero, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = con.conectar().prepareStatement(sql)) {
                    statement.setString(1, nome);
                    statement.setString(2, email);
                    statement.setString(3, senha);
                    statement.setFloat(4, altura);
                    statement.setInt(5, idade);
                    statement.setFloat(6, peso);
                    statement.setInt(7, frequencia);
                    statement.setString(8, genero);
                    statement.setBoolean(9, admin);
                    int linhasAfetadas = statement.executeUpdate();
                    if (linhasAfetadas > 0) {
                        JOptionPane.showMessageDialog(frame, "Cadastro realizado com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Erro ao realizar cadastro.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao realizar cadastro: " + ex.getMessage());
                }
            }
        });
        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); 
            }
        });
        frame.add(backButton);
        frame.add(cadastrarButton);

        frame.pack();
        frame.setVisible(true);
    }

    public static void exibirTodosUsuarios(Connection connection) throws SQLException {
        System.out.println("Voce escolheu exibir todos os usuarios no banco de dados.");

        JFrame frame = new JFrame("Lista de Usuários");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        String sql = "SELECT * FROM usuario";
        try (PreparedStatement statement = con.conectar().prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery(sql)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn("<html><b>" + metaData.getColumnName(i) + "</b></html>");
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                tableModel.addRow(rowData);
            }
        }

        frame.setSize(1200, 600);
        frame.setVisible(true);
    }

    public static void deletarUsuario(Connection connection, Scanner sc) throws SQLException {
        System.out.println("Voce escolheu deletar um usuario do banco de dados.");
        String idInput = JOptionPane.showInputDialog(null, "Digite o ID do usuario que voce quer deletar:");
        int id = Integer.parseInt(idInput);
        String sql = "DELETE FROM usuario WHERE userID = ?";
        try (PreparedStatement statement = con.conectar().prepareStatement(sql)) {
            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Usuario deletado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null, "Nao existe usuario com esse ID.");
            }
        }
    }

    public static void alterarUsuario(Connection connection, Scanner sc) throws SQLException {
        System.out.println("Voce escolheu alterar informacoes de um usuario.");
        System.out.print("Digite o ID do usuario que voce quer alterar: ");
        int id = sc.nextInt();
        sc.nextLine(); // consumir a quebra de linha
        String sql = "SELECT * FROM usuario WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Usuario encontrado.");
                    int opcaoAlterar;
                    do {
                        System.out.println("Escolha qual informacao voce quer alterar: ");
                        System.out.println("1 - Nome");
                        System.out.println("2 - Email");
                        System.out.println("3 - Senha");
                        System.out.println("4 - Altura");
                        System.out.println("5 - Idade");
                        System.out.println("6 - Peso");
                        System.out.println("7 - Frequencia");
                        System.out.println("8 - Genero");
                        System.out.println("9 - Admin");
                        System.out.println("0 - Sair");
                        opcaoAlterar = sc.nextInt();
                        sc.nextLine(); // consumir a quebra de linha
                        switch (opcaoAlterar) {
                            case 1:
                                alterarNome(connection, sc, id);
                                break;
                            case 2:
                                alterarEmail(connection, sc, id);
                                break;
                            case 3:
                                alterarSenha(connection, sc, id);
                                break;
                            case 4:
                                alterarAltura(connection, sc, id);
                                break;
                            case 5:
                                alterarIdade(connection, sc, id);
                                break;
                            case 6:
                                alterarPeso(connection, sc, id);
                                break;
                            case 7:
                                alterarFrequencia(connection, sc, id);
                                break;
                            case 8:
                                alterarGenero(connection, sc, id);
                                break;
                            case 9:
                                alterarAdmin(connection, sc, id);
                                break;
                            case 10:
                                AlterarExercicioDoUsuario(connection, sc);
                                break;
                            case 0:
                                System.out.println("Saindo do modo de alteracao.");
                                break;
                            default:
                                System.out.println("Opcao invalida.");
                                break;
                        }
                    } while (opcaoAlterar != 0);
                } else {
                    System.out.println("Nao existe usuario com esse ID.");
                }
            }
        }
    }



        static void AlterarExercicioDoUsuario(Connection connection, Scanner sc) throws SQLException {
            JFrame frame = new JFrame("Alterar Exercício do Usuário");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setLayout(new FlowLayout());

            JLabel userIdLabel = new JLabel("ID do Usuário:");
            JTextField userIdField = new JTextField(10);
            JLabel exercicioIdLabel = new JLabel("ID do Exercício:");
            JTextField exercicioIdField = new JTextField(10);
            JButton alterarButton = new JButton("Alterar");

            alterarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int id = Integer.parseInt(userIdField.getText());
                    int exercicioID = Integer.parseInt(exercicioIdField.getText());

                    String sql = "UPDATE usuario SET exercicioID = ? WHERE userID = ?";
                    try (PreparedStatement statement = con.conectar().prepareStatement(sql)) {
                        statement.setInt(1, exercicioID);
                        statement.setInt(2, id);
                        int linhasAfetadas = statement.executeUpdate();
                        if (linhasAfetadas > 0) {
                            JOptionPane.showMessageDialog(frame, "Exercício adicionado com sucesso.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Erro ao adicionar exercício.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            frame.add(userIdLabel);
            frame.add(userIdField);
            frame.add(exercicioIdLabel);
            frame.add(exercicioIdField);
            frame.add(alterarButton);

            frame.setVisible(true);
        }


    private static void alterarNome(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o novo nome: ");
        String novoNome = sc.nextLine();
        String sql = "UPDATE usuario SET nome = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoNome);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Nome alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar nome.");
            }
        }
    }

    private static void alterarEmail(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o novo email: ");
        String novoEmail = sc.nextLine();
        String sql = "UPDATE usuario SET email = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoEmail);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Email alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar email.");
            }
        }
    }

    private static void alterarSenha(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite a nova senha: ");
        String novaSenha = sc.nextLine();
        String sql = "UPDATE usuario SET senha = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novaSenha);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Senha alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar senha.");
            }
        }
    }

    private static void alterarAltura(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite a nova altura em metros: ");
        float novaAltura = sc.nextFloat();
        String sql = "UPDATE usuario SET altura = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, novaAltura);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Altura alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar altura.");
            }
        }
    }

    private static void alterarIdade(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite a nova idade em anos: ");
        int novaIdade = sc.nextInt();
        String sql = "UPDATE usuario SET idade = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, novaIdade);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Idade alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar idade.");
            }
        }
    }

    private static void alterarPeso(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o novo peso em quilos: ");
        float novoPeso = sc.nextFloat();
        String sql = "UPDATE usuario SET peso = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, novoPeso);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Peso alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar peso.");
            }
        }
    }

    private static void alterarFrequencia(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite a nova frequencia semanal de exercicios (0 a 7): ");
        int novaFrequencia = sc.nextInt();
        String sql = "UPDATE usuario SET frequencia = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, novaFrequencia);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Frequencia alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar frequencia.");
            }
        }
    }

    private static void alterarGenero(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o novo genero (M ou F): ");
        String novoGenero = sc.nextLine();
        String sql = "UPDATE usuario SET genero = ? WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoGenero);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Genero alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar genero.");
            }
        }
    }

    private static void alterarAdmin(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.println("Voce escolheu alterar informacoes de um administrador.");
        System.out.print("Digite o ID do administrador que voce quer alterar: ");
        id = sc.nextInt();
        sc.nextLine(); // consumir a quebra de linha
        String sql = "SELECT * FROM usuario WHERE userID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Administrador encontrado.");
                    int opcaoAlterar;
                    System.out.println("Escolha qual informacao voce quer alterar: ");
                    System.out.println("1 - Nome");
                    System.out.println("2 - Email");
                    System.out.println("3 - Senha");
                    opcaoAlterar = sc.nextInt();
                    sc.nextLine(); // consumir a quebra de linha
                    switch (opcaoAlterar) {
                        case 1:
                            alterarNome(connection, sc, id);
                            break;
                        case 2:
                            alterarEmail(connection, sc, id);
                            break;
                        case 3:
                            alterarSenha(connection, sc, id);
                            break;

                    }
                    while (opcaoAlterar != 0)
                        ;
                } else {
                    System.out.println("Nao existe usuario com esse ID.");
                }
            }
        }
    }

    static void alterarExercicio(Connection connection, Scanner sc) throws SQLException {
        System.out.println("Voce escolheu alterar informacoes de um exercicio.");
        int opcaoAlterar;
        System.out.println("Escolha qual informacao voce quer alterar: ");
        System.out.println("-------------Cadastro--------------");
        System.out.println("0 - Cadastrar Exercicio");
        System.out.println("------------Alteraçoes-------------");
        System.out.println("1 - Nome");
        System.out.println("2 - Intensidade");
        System.out.println("3 - Fator de Intensidade");
        System.out.println("4 - MET");
        opcaoAlterar = sc.nextInt();
        sc.nextLine(); // consumir a quebra de linha
        switch (opcaoAlterar) {
            case 0:
                CadastraExercicio(connection, sc);
                break;
            case 1:
                alterarNomeExercicio(connection, sc, id);
                break;
            case 2:
                alterarIntensidade(connection, sc, id);
                break;
            case 3:
                alterarFatorIntensidade(connection, sc, id);
                break;
            case 4:
                alterarMET(connection, sc, id);
                break;
            default:
                System.out.println("Opcao invalida.");
                break;
        }
    }

    private static void CadastraExercicio(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Digite o nome do exercicio: ");
        String exercicio = sc.nextLine();
        System.out.print("Digite a intensidade do exercicio: ");
        String intensidade = sc.nextLine();
        System.out.print("Digite o fator de intensidade do exercicio: ");
        int fatorIntensidade = sc.nextInt();
        System.out.print("Digite o MET do exercicio: ");
        float MET = sc.nextFloat();
        String sql = "INSERT INTO exercicios (exercicio, intensidade, fatorIntensidade, MET) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, exercicio);
            statement.setString(2, intensidade);
            statement.setInt(3, fatorIntensidade);
            statement.setFloat(4, MET);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cadastro realizado com sucesso.");
            } else {
                System.out.println("Erro ao realizar cadastro.");
            }
        }
    }

    private static void alterarMET(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o id do exercicio que voce quer alterar: ");
        id = sc.nextInt();
        System.out.print("Digite o novo MET: ");
        float novoMET = sc.nextFloat();
        String sql = "UPDATE exercicios SET MET = ? WHERE exercicioID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setFloat(1, novoMET);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("MET alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar MET.");
            }
        }
    }

    private static void alterarFatorIntensidade(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o id do exercicio que voce quer alterar: ");
        id = sc.nextInt();
        System.out.print("Digite o novo fator de intensidade: ");
        int novoFatorIntensidade = sc.nextInt();
        String sql = "UPDATE exercicios SET fatorIntensidade = ? WHERE exercicioID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, novoFatorIntensidade);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Fator de intensidade alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar fator de intensidade.");
            }
        }
    }

    private static void alterarIntensidade(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o id do exercicio que voce quer alterar: ");
        id = sc.nextInt();
        System.out.print("Digite a nova intensidade: ");
        String novaIntensidade = sc.nextLine();
        String sql = "UPDATE exercicios SET intensidade = ? WHERE exercicioID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novaIntensidade);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Intensidade alterada com sucesso.");
            } else {
                System.out.println("Erro ao alterar intensidade.");
            }
        }
    }

    private static void alterarNomeExercicio(Connection connection, Scanner sc, int id) throws SQLException {
        System.out.print("Digite o id do exercicio que voce quer alterar: ");
        id = sc.nextInt();
        System.out.print("Digite o novo nome do exercicio: ");
        String novoNomeExercicio = sc.nextLine();
        String sql = "UPDATE exercicios SET exercicio = ? WHERE exercicioID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, novoNomeExercicio);
            statement.setInt(2, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Nome do exercicio alterado com sucesso.");
            } else {
                System.out.println("Erro ao alterar nome do exercicio.");
            }
        }
    }

    public static void mostratTodosExercicios(Connection connection) throws SQLException {
        System.out.println("Voce escolheu exibir todos os exercicios no banco de dados.");

        JFrame frame = new JFrame("Lista de Exercícios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        String sql = "SELECT * FROM exercicios";
        try (PreparedStatement statement = con.conectar().prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn("<html><b>" + metaData.getColumnName(i) + "</b></html>");
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getString(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
    }

}
