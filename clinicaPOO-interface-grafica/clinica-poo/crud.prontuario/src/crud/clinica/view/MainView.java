package crud.clinica.view;

import crud.clinica.facade.ClinicaFacade;
import crud.clinica.view.exame.ExameFormDialog;
import crud.clinica.view.exame.ExameListDialog;
import crud.clinica.view.paciente.PacienteFormDialog;
import crud.clinica.view.paciente.PacienteListDialog;
import crud.clinica.view.util.DialogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final ClinicaFacade facade;

    public MainView(ClinicaFacade facade) {
        this.facade = facade;

        setTitle("Sistema da Clínica");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmarSaida(null);
            }
        });
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PONTO DA MUDANÇA: Construção do JMenuBar ---
        JMenuBar menuBar = new JMenuBar();
        
        // --- Menu Pacientes ---
        JMenu menuPacientes = new JMenu("Pacientes");
        
        JMenuItem itemNovoPaciente = new JMenuItem("Novo");
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


        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(this::confirmarSaida);
        menuSair.add(itemSair);

        menuBar.add(menuPacientes);
        menuBar.add(menuExames);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

        JPanel painelCentral = new JPanel(new BorderLayout());
        JLabel labelBemVindo = new JLabel("Bem-vindo ao Sistema da Clínica", SwingConstants.CENTER);
        labelBemVindo.setFont(new Font("Arial", Font.BOLD, 24));
        painelCentral.add(labelBemVindo, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);
    }

    private void abrirJanela(String titulo) {
        switch (titulo) {

            case "Novo Paciente":
                new PacienteFormDialog(this, this.facade, null).setVisible(true);
                break;
            case "Editar Paciente":
                new PacienteListDialog(this, this.facade, true, false).setVisible(true);
                break;
            case "Excluir Paciente":
                new PacienteListDialog(this, this.facade, false, true).setVisible(true);
                break;
            case "Localizar Paciente":
                new PacienteListDialog(this, this.facade, false, false).setVisible(true);
                break;
            
            case "Novo Exame":
                new ExameFormDialog(this, this.facade, null).setVisible(true);
                break;
            case "Editar Exame":
                new ExameListDialog(this, this.facade, true, false).setVisible(true);
                break;
            case "Excluir Exame":
                new ExameListDialog(this, this.facade, false, true).setVisible(true);
                break;
            case "Localizar Exame":
                 new ExameListDialog(this, this.facade, false, false).setVisible(true);
                 break;

            default:
                DialogManager.showError(this, "Função não implementada: " + titulo);
        }
    }

    private void confirmarSaida(ActionEvent e) {
        if (DialogManager.showConfirm(this, "Deseja realmente sair?")) {
            System.exit(0);
        }
    }
}