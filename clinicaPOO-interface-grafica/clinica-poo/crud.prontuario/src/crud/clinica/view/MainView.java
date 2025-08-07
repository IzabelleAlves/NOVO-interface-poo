package crud.clinica.view;

import crud.clinica.database.IConnection;
// Imports necessários
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.view.exame.*; // Usando wildcard para simplificar
import crud.clinica.view.paciente.*; // Usando wildcard para simplificar
import crud.clinica.view.util.DialogManager; // Importando nosso utilitário

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    // A MainView agora só precisa conhecer a Facade.
    private final ClinicaFacade facade;
    
    // REMOVIDO: O campo 'dbConnection' não é mais necessário aqui.
    // private IConnection dbConnection;

    public MainView(ClinicaFacade facade) {
        // O construtor agora SÓ armazena a facade. Simples e limpo.
        this.facade = facade;

        setTitle("Sistema da Clínica - v2.0 Refatorado");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Controlamos o fechamento
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmarSaida(null); // Chama o mesmo método do menu
            }
        });
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // REMOVIDO: A linha 'dbConnection = new DatabaseConnectionMySQL();' foi deletada.

        // A criação da JMenuBar continua exatamente igual, estava perfeita.
        JMenuBar menuBar = new JMenuBar();
        
        // --- Menu Pacientes ---
        JMenu menuPacientes = new JMenu("Pacientes");
        JMenuItem itemNovoPaciente = new JMenuItem("Novo");
        // A chamada ao método abrirJanela permanece, a mágica acontece dentro dele.
        itemNovoPaciente.addActionListener(e -> abrirJanela("Novo Paciente"));
        JMenuItem itemEditarPaciente = new JMenuItem("Editar");
        itemEditarPaciente.addActionListener(e -> abrirJanela("Editar Paciente"));
        JMenuItem itemLocalizarPaciente = new JMenuItem("Localizar");
        itemLocalizarPaciente.addActionListener(e -> abrirJanela("Localizar Paciente"));
        JMenuItem itemExcluirPaciente = new JMenuItem("Excluir");
        itemExcluirPaciente.addActionListener(e -> abrirJanela("Excluir Paciente"));
        menuPacientes.add(itemNovoPaciente);
        menuPacientes.add(itemEditarPaciente);
        menuPacientes.add(itemLocalizarPaciente);
        menuPacientes.add(itemExcluirPaciente);

        // --- Menu Exames ---
        JMenu menuExames = new JMenu("Exames");
        JMenuItem itemNovoExame = new JMenuItem("Novo");
        itemNovoExame.addActionListener(e -> abrirJanela("Novo Exame"));
        JMenuItem itemEditarExame = new JMenuItem("Editar");
        itemEditarExame.addActionListener(e -> abrirJanela("Editar Exame"));
        JMenuItem itemLocalizarExame = new JMenuItem("Localizar");
        itemLocalizarExame.addActionListener(e -> abrirJanela("Localizar Exame"));
        JMenuItem itemExcluirExame = new JMenuItem("Excluir");
        itemExcluirExame.addActionListener(e -> abrirJanela("Excluir Exame"));
        menuExames.add(itemNovoExame);
        menuExames.add(itemEditarExame);
        menuExames.add(itemLocalizarExame);
        menuExames.add(itemExcluirExame);

        // --- Menu Sair ---
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(this::confirmarSaida);
        menuSair.add(itemSair);

        menuBar.add(menuPacientes);
        menuBar.add(menuExames);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

        // O painel central também continua igual.
        JPanel painelCentral = new JPanel(new BorderLayout());
        JLabel labelBemVindo = new JLabel("Bem-vindo ao Sistema da Clínica", SwingConstants.CENTER);
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 24));
        painelCentral.add(labelBemVindo, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);
    }

    private void abrirJanela(String titulo) {
        // Este método agora está muito mais limpo e desacoplado.
        // Ele não sabe como o banco funciona, apenas passa a facade adiante.
        switch (titulo) {
            case "Novo Paciente":
                // Note que agora passamos 'this.facade' em vez de 'dbConnection'.
                // O PacienteFormDialog também precisará ser ajustado para receber a facade.
                new PacienteFormDialog(this, this.facade, null).setVisible(true);
                break;

            case "Editar Paciente":
                // A mesma lógica se aplica aqui.
                new PacienteListDialog(this, this.facade, true, false).setVisible(true);
                break;

            case "Excluir Paciente":
                new PacienteListDialog(this, this.facade, false, true).setVisible(true);
                break;

            case "Localizar Paciente":
                new PacienteLocalizacaoDialog(this, this.facade).setVisible(true);
                break;
            
            // --- EXAMES ---

            case "Novo Exame":
                // Veja como ficou simples! Não precisamos mais criar DAOs aqui.
                new ExameFormDialog(this, this.facade, null).setVisible(true);
                break;

            case "Editar Exame":
                new ExameListDialog(this, this.facade, true, false).setVisible(true);
                break;
            
            case "Excluir Exame":
                new ExameListDialog(this, this.facade, false, true).setVisible(true);
                break;
                
            case "Localizar Exame":
                 new ExameLocalizacaoDialog(this, this.facade).setVisible(true);
                 break;

            default:
                // Usando nosso DialogManager para consistência.
                DialogManager.showError(this, "Função ainda não implementada: " + titulo);
        }
    }

    private void confirmarSaida(ActionEvent e) {
        // Usando nossa classe utilitária! O código fica mais limpo e padronizado.
        if (DialogManager.showConfirm(this, "Deseja realmente sair?")) {
            // Lógica para fechar a conexão com o banco (se necessário) pode ser adicionada aqui
            // facade.closeConnection();
            System.exit(0);
        }
    }
}